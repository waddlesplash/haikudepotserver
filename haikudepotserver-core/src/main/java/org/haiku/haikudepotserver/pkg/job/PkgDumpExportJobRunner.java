/*
 * Copyright 2017, Andrew Lindesay
 * Distributed under the terms of the MIT License.
 */

package org.haiku.haikudepotserver.pkg.job;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.net.MediaType;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.cayenne.query.PrefetchTreeNode;
import org.haiku.haikudepotserver.dataobjects.NaturalLanguage;
import org.haiku.haikudepotserver.dataobjects.Pkg;
import org.haiku.haikudepotserver.dataobjects.PkgVersion;
import org.haiku.haikudepotserver.dataobjects.RepositorySource;
import org.haiku.haikudepotserver.job.AbstractJobRunner;
import org.haiku.haikudepotserver.job.model.JobDataWithByteSink;
import org.haiku.haikudepotserver.job.model.JobRunnerException;
import org.haiku.haikudepotserver.job.model.JobService;
import org.haiku.haikudepotserver.pkg.FixedPkgLocalizationLookupServiceImpl;
import org.haiku.haikudepotserver.pkg.model.PkgDumpExportJobSpecification;
import org.haiku.haikudepotserver.pkg.model.PkgLocalizationLookupService;
import org.haiku.haikudepotserver.pkg.model.PkgService;
import org.haiku.haikudepotserver.pkg.model.ResolvedPkgVersionLocalization;
import org.haiku.haikudepotserver.pkg.model.dumpexport.DumpExportPkg;
import org.haiku.haikudepotserver.pkg.model.dumpexport.DumpExportPkgCategory;
import org.haiku.haikudepotserver.pkg.model.dumpexport.DumpExportPkgScreenshot;
import org.haiku.haikudepotserver.pkg.model.dumpexport.DumpExportPkgVersion;
import org.haiku.haikudepotserver.support.ArchiveInfo;
import org.haiku.haikudepotserver.support.DateTimeHelper;
import org.haiku.haikudepotserver.support.RuntimeInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

@Component
public class PkgDumpExportJobRunner extends AbstractJobRunner<PkgDumpExportJobSpecification> {

    protected static Logger LOGGER = LoggerFactory.getLogger(PkgDumpExportJobRunner.class);

    private final static int BATCH_SIZE = 100;

    @Resource
    private RuntimeInformationService runtimeInformationService;

    @Resource
    private ServerRuntime serverRuntime;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private PkgService pkgService;

    @Override
    public void run(JobService jobService, PkgDumpExportJobSpecification specification)
            throws IOException, JobRunnerException {

        // this will register the outbound data against the job.
        JobDataWithByteSink jobDataWithByteSink = jobService.storeGeneratedData(
                specification.getGuid(),
                "download",
                MediaType.JSON_UTF_8.toString());

        try(
                final OutputStream outputStream = jobDataWithByteSink.getByteSink().openBufferedStream();
                final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
                final JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(gzipOutputStream)
        ) {
            jsonGenerator.writeStartObject();
            writeInfo(jsonGenerator, specification);
            writePkgs(jsonGenerator, specification);
            jsonGenerator.writeEndObject();
        }
    }

    private PrefetchTreeNode createPkgVersionPrefetchTree() {
        PrefetchTreeNode node = PkgVersion.PKG.disjoint();
        node.merge(PkgVersion.PKG.dot(Pkg.PKG_PROMINENCES).disjoint());
        node.merge(PkgVersion.PKG.dot(Pkg.PKG_SCREENSHOTS).disjoint());
        node.merge(PkgVersion.PKG.dot(Pkg.PKG_PKG_CATEGORIES).disjoint());
        node.merge(PkgVersion.PKG.dot(Pkg.PKG_CHANGELOGS).disjoint());
        node.merge(PkgVersion.PKG.dot(Pkg.PKG_USER_RATING_AGGREGATES).disjoint());
        return node;
    }

    private void writePkgs(JsonGenerator jsonGenerator, PkgDumpExportJobSpecification specification) throws IOException {
        jsonGenerator.writeFieldName("items");
        jsonGenerator.writeStartArray();

        final ObjectContext context = serverRuntime.newContext();

        NaturalLanguage naturalLanguage = NaturalLanguage.getByCode(context, specification.getNaturalLanguageCode()).orElseThrow(
                () -> new IllegalStateException("unable to find the natural language [" + specification.getNaturalLanguageCode() + "]")
        );

        RepositorySource repositorySource = RepositorySource.getByCode(
                context,
                specification.getRepositorySourceCode()).orElseThrow(
                () -> new IllegalStateException(
                        "unable to find the repository source ["
                                + specification.getRepositorySourceCode() + "]"));

        ObjectSelect<PkgVersion> objectSelect = ObjectSelect
                .query(PkgVersion.class)
                .where(PkgVersion.ACTIVE.isTrue())
                .and(PkgVersion.PKG.dot(Pkg.ACTIVE).isTrue())
                .and(PkgVersion.IS_LATEST.isTrue())
                .and(PkgVersion.REPOSITORY_SOURCE.eq(repositorySource))
                .orderBy(PkgVersion.PKG.dot(Pkg.NAME).desc());

        // iterate through the pkgnames.  This is done in this manner so that if there is (erroneously)
        // two 'latest' pkg versions under the same pkg for two different architectures then these will
        // be grouped in the output instead of the same pkg appearing twice.

        List<String> pkgNames = objectSelect.column(PkgVersion.PKG.dot(Pkg.NAME)).select(context);

        LOGGER.info("will dump pkg versions for {} pkgs", pkgNames.size());

        Lists.partition(pkgNames, BATCH_SIZE).forEach((subPkgNames) -> {

            ObjectSelect<PkgVersion> pkgVersionObjectSelect = objectSelect
                    .and(PkgVersion.PKG.dot(Pkg.NAME).in(subPkgNames))
                    .prefetch(createPkgVersionPrefetchTree());

            List<PkgVersion> pkgVersions = pkgVersionObjectSelect.select(context);
            writePkgVersions(jsonGenerator, context, pkgVersions, repositorySource, naturalLanguage);

        });

        jsonGenerator.writeEndArray();
    }

    private void writePkgVersions(
            JsonGenerator jsonGenerator,
            ObjectContext context,
            List<PkgVersion> pkgVersions,
            RepositorySource repositorySource,
            NaturalLanguage naturalLanguage) {

        final PkgLocalizationLookupService pkgLocalizationLookupService =
                new FixedPkgLocalizationLookupServiceImpl(context, pkgVersions, naturalLanguage);

        Map<String, List<PkgVersion>> pkgVersionsUnderCommonPkg = pkgVersions
                .stream()
                .collect(Collectors.groupingBy((pv) -> pv.getPkg().getName()));

        pkgVersionsUnderCommonPkg.values().forEach((pvs) -> {
            try {
                objectMapper.writeValue(
                        jsonGenerator,
                        createDumpPkg(context, pvs, repositorySource, naturalLanguage, pkgLocalizationLookupService));
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        });

    }

    private DumpExportPkg createDumpPkg(
            ObjectContext context,
            List<PkgVersion> pkgVersionsUnderCommonPkg,
            RepositorySource repositorySource,
            NaturalLanguage naturalLanguage,
            PkgLocalizationLookupService pkgLocalizationLookupService) {
        Preconditions.checkArgument(null != pkgVersionsUnderCommonPkg && !pkgVersionsUnderCommonPkg.isEmpty());

        Pkg pkg = pkgVersionsUnderCommonPkg.get(0).getPkg(); // any will do to get the pkg.

        DumpExportPkg dumpExportPkg = new DumpExportPkg();
        dumpExportPkg.setModifyTimestamp(pkg.getModifyTimestamp().getTime());
        dumpExportPkg.setName(pkg.getName());
        dumpExportPkg.setProminenceOrdering(pkg.getPkgProminence(repositorySource.getRepository())
                .map((p) -> p.getProminence().getOrdering()).map(Long::new).orElse(null));
        dumpExportPkg.setDerivedRating(
                pkg.getPkgUserRatingAggregate(repositorySource.getRepository())
                        .map((v) -> v.getDerivedRating().doubleValue()).orElse(null));

        dumpExportPkg.setPkgCategories(
                pkg.getPkgPkgCategories().stream().map((ppc) -> {
                    DumpExportPkgCategory dumpExportPkgCategory = new DumpExportPkgCategory();
                    dumpExportPkgCategory.setCode(ppc.getPkgCategory().getCode());
                    return dumpExportPkgCategory;
                }).collect(Collectors.toList()));

        dumpExportPkg.setPkgScreenshots(
                pkg.getPkgScreenshots().stream().sorted().map((ps) -> {
                    DumpExportPkgScreenshot dumpExportPkgScreenshot = new DumpExportPkgScreenshot();
                    dumpExportPkgScreenshot.setCode(ps.getCode());
                    dumpExportPkgScreenshot.setHeight(ps.getHeight().longValue());
                    dumpExportPkgScreenshot.setWidth(ps.getWidth().longValue());
                    dumpExportPkgScreenshot.setLength(ps.getLength().longValue());
                    dumpExportPkgScreenshot.setOrdering(ps.getOrdering().longValue());
                    return dumpExportPkgScreenshot;
                }).collect(Collectors.toList()));

        dumpExportPkg.setPkgVersions(
                pkgVersionsUnderCommonPkg
                        .stream()
                        .sorted()
                        .map((pv) -> createDumpPkgVersion(context, pv, naturalLanguage, pkgLocalizationLookupService))
                        .collect(Collectors.toList()));

        return dumpExportPkg;
    }

    private DumpExportPkgVersion createDumpPkgVersion(
            ObjectContext context,
            PkgVersion pkgVersion,
            NaturalLanguage naturalLanguage,
            PkgLocalizationLookupService pkgLocalizationLookupService) {
        DumpExportPkgVersion result = new DumpExportPkgVersion();

        result.setMajor(pkgVersion.getMajor());
        result.setMinor(pkgVersion.getMinor());
        result.setMicro(pkgVersion.getMicro());
        result.setRevision(null == pkgVersion.getRevision() ? null : pkgVersion.getRevision().longValue());
        result.setArchitectureCode(pkgVersion.getArchitecture().getCode());
        result.setPayloadLength(pkgVersion.getPayloadLength());

        ResolvedPkgVersionLocalization resolvedPkgVersionLocalization =
                pkgLocalizationLookupService.resolvePkgVersionLocalization(context, pkgVersion, null, naturalLanguage);

        result.setDescription(resolvedPkgVersionLocalization.getDescription());
        result.setSummary(resolvedPkgVersionLocalization.getSummary());

        return result;
    }

    private void writeInfo(
            JsonGenerator jsonGenerator,
            PkgDumpExportJobSpecification specification) throws IOException {
        jsonGenerator.writeFieldName("info");
        objectMapper.writeValue(jsonGenerator, createArchiveInfo(serverRuntime.newContext(), specification));
    }

    private ArchiveInfo createArchiveInfo(ObjectContext context, PkgDumpExportJobSpecification specification) {
        RepositorySource repositorySource = RepositorySource.getByCode(
                context,
                specification.getRepositorySourceCode()
        ).orElseThrow(() -> new IllegalStateException(
                "unable to find the repository source [" + specification.getRepositorySourceCode() + "]")
        );
        Date modifyTimestamp = pkgService.getLastModifyTimestampSecondAccuracy(context, repositorySource);
        return new ArchiveInfo(
                DateTimeHelper.secondAccuracyDatePlusOneSecond(modifyTimestamp),
                runtimeInformationService.getProjectVersion());
    }

}
