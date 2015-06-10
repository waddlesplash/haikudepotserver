/*
 * Copyright 2014-2015, Andrew Lindesay
 * Distributed under the terms of the MIT License.
 */

package org.haikuos.haikudepotserver.api1;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.haikuos.haikudepotserver.api1.model.repository.*;
import org.haikuos.haikudepotserver.api1.support.AuthorizationFailureException;
import org.haikuos.haikudepotserver.api1.support.ObjectNotFoundException;
import org.haikuos.haikudepotserver.api1.support.ValidationException;
import org.haikuos.haikudepotserver.api1.support.ValidationFailure;
import org.haikuos.haikudepotserver.dataobjects.Repository;
import org.haikuos.haikudepotserver.dataobjects.RepositorySource;
import org.haikuos.haikudepotserver.job.JobOrchestrationService;
import org.haikuos.haikudepotserver.pkg.model.PkgSearchSpecification;
import org.haikuos.haikudepotserver.repository.RepositoryOrchestrationService;
import org.haikuos.haikudepotserver.repository.model.PkgRepositoryImportJobSpecification;
import org.haikuos.haikudepotserver.repository.model.RepositorySearchSpecification;
import org.haikuos.haikudepotserver.security.AuthorizationService;
import org.haikuos.haikudepotserver.security.model.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RepositoryApiImpl extends AbstractApiImpl implements RepositoryApi {

    protected static Logger LOGGER = LoggerFactory.getLogger(RepositoryApiImpl.class);

    @Resource
    private ServerRuntime serverRuntime;

    @Resource
    private AuthorizationService authorizationService;

    @Resource
    private RepositoryOrchestrationService repositoryService;

    @Resource
    private JobOrchestrationService jobOrchestrationService;

    @Override
    public GetRepositoriesResult getRepositories(GetRepositoriesRequest getRepositoriesRequest) {
        Preconditions.checkArgument(null!=getRepositoriesRequest);
        GetRepositoriesResult result = new GetRepositoriesResult();
        result.repositories = Repository.getAll(serverRuntime.getContext())
                .stream()
                .map(r -> {
                    GetRepositoriesResult.Repository resultRepository = new GetRepositoriesResult.Repository();
                    resultRepository.code = r.getCode();
                    resultRepository.name = r.getName();
                    return resultRepository;
                })
                .collect(Collectors.toList());
        return result;
    }

    // note; no integration test for this one.
    @Override
    public TriggerImportRepositoryResult triggerImportRepository(
            TriggerImportRepositoryRequest triggerImportRepositoryRequest)
            throws ObjectNotFoundException {

        Preconditions.checkNotNull(triggerImportRepositoryRequest);
        Preconditions.checkState(!Strings.isNullOrEmpty(triggerImportRepositoryRequest.code));

        final ObjectContext context = serverRuntime.getContext();

        Optional<Repository> repositoryOptional = Repository.getByCode(context, triggerImportRepositoryRequest.code);

        if(!repositoryOptional.isPresent()) {
            throw new ObjectNotFoundException(Repository.class.getSimpleName(), triggerImportRepositoryRequest.code);
        }

        if(!authorizationService.check(
                context,
                tryObtainAuthenticatedUser(context).orElse(null),
                repositoryOptional.get(),
                Permission.REPOSITORY_IMPORT)) {
            throw new AuthorizationFailureException();
        }

        jobOrchestrationService.submit(
                new PkgRepositoryImportJobSpecification(repositoryOptional.get().getCode()),
                JobOrchestrationService.CoalesceMode.QUEUED);

        return new TriggerImportRepositoryResult();
    }

    @Override
    public SearchRepositoriesResult searchRepositories(SearchRepositoriesRequest request) {
        Preconditions.checkNotNull(request);

        final ObjectContext context = serverRuntime.getContext();

        if(!authorizationService.check(
                context,
                tryObtainAuthenticatedUser(context).orElse(null),
                null,
                Permission.REPOSITORY_LIST)) {
            throw new AuthorizationFailureException();
        }

        if(null!=request.includeInactive && request.includeInactive) {
            if(!authorizationService.check(
                    context,
                    tryObtainAuthenticatedUser(context).orElse(null),
                    null,
                    Permission.REPOSITORY_LIST_INACTIVE)) {
                throw new AuthorizationFailureException();
            }
        }

        RepositorySearchSpecification specification = new RepositorySearchSpecification();
        String exp = request.expression;

        if(null!=exp) {
            exp = Strings.emptyToNull(exp.trim().toLowerCase());
        }

        specification.setExpression(exp);

        if(null!=request.expressionType) {
            specification.setExpressionType(
                    PkgSearchSpecification.ExpressionType.valueOf(request.expressionType.name()));
        }

        specification.setLimit(request.limit);
        specification.setOffset(request.offset);
        specification.setIncludeInactive(null!=request.includeInactive && request.includeInactive);

        SearchRepositoriesResult result = new SearchRepositoriesResult();
        List<Repository> searchedRepositories = repositoryService.search(context,specification);

        result.total = repositoryService.total(context,specification);
        result.items = searchedRepositories.stream().map(sr -> {
            SearchRepositoriesResult.Repository resultRepository = new SearchRepositoriesResult.Repository();
            resultRepository.active = sr.getActive();
            resultRepository.name = sr.getName();
            resultRepository.code = sr.getCode();
            return resultRepository;
        }).collect(Collectors.toList());

        return result;
    }

    @Override
    public GetRepositoryResult getRepository(GetRepositoryRequest getRepositoryRequest) throws ObjectNotFoundException {
        Preconditions.checkNotNull(getRepositoryRequest);
        Preconditions.checkState(!Strings.isNullOrEmpty(getRepositoryRequest.code));

        final ObjectContext context = serverRuntime.getContext();

        Optional<Repository> repositoryOptional = Repository.getByCode(context, getRepositoryRequest.code);

        if(!repositoryOptional.isPresent()) {
            throw new ObjectNotFoundException(Repository.class.getSimpleName(), getRepositoryRequest.code);
        }

        if(!authorizationService.check(
                context,
                tryObtainAuthenticatedUser(context).orElse(null),
                repositoryOptional.get(),
                Permission.REPOSITORY_VIEW)) {
            throw new AuthorizationFailureException();
        }

        GetRepositoryResult result = new GetRepositoryResult();
        result.active = repositoryOptional.get().getActive();
        result.code = repositoryOptional.get().getCode();
        result.name = repositoryOptional.get().getName();
        result.createTimestamp = repositoryOptional.get().getCreateTimestamp().getTime();
        result.modifyTimestamp = repositoryOptional.get().getModifyTimestamp().getTime();
        result.informationUrl = repositoryOptional.get().getInformationUrl();
        result.repositorySources = repositoryOptional.get().getRepositorySources()
                .stream()
                .filter(
                        rs -> rs.getActive() ||
                                (null != getRepositoryRequest.includeInactiveRepositorySources &&
                                        getRepositoryRequest.includeInactiveRepositorySources)
                )
                .map(rs -> {
                    GetRepositoryResult.RepositorySource resultRs = new GetRepositoryResult.RepositorySource();
                    resultRs.active = rs.getActive();
                    resultRs.code = rs.getCode();
                    resultRs.url = rs.getUrl();
                    return resultRs;
                })
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public UpdateRepositoryResult updateRepository(UpdateRepositoryRequest updateRepositoryRequest) throws ObjectNotFoundException {
        Preconditions.checkNotNull(updateRepositoryRequest);

        final ObjectContext context = serverRuntime.getContext();

        Optional<Repository> repositoryOptional = Repository.getByCode(context, updateRepositoryRequest.code);

        if(!repositoryOptional.isPresent()) {
            throw new ObjectNotFoundException(Repository.class.getSimpleName(), updateRepositoryRequest.code);
        }

        authorizationService.check(
                context,
                tryObtainAuthenticatedUser(context).orElse(null),
                repositoryOptional.get(),
                Permission.REPOSITORY_EDIT);

        for(UpdateRepositoryRequest.Filter filter : updateRepositoryRequest.filter) {
            switch(filter) {

                case ACTIVE:
                    if(null==updateRepositoryRequest.active) {
                        throw new IllegalStateException("the active flag must be supplied");
                    }

                    if(repositoryOptional.get().getActive() != updateRepositoryRequest.active) {
                        repositoryOptional.get().setActive(updateRepositoryRequest.active);
                        LOGGER.info("did set the active flag on repository {} to {}", updateRepositoryRequest.code, updateRepositoryRequest.active);
                    }

                    if(!updateRepositoryRequest.active) {
                        for(RepositorySource repositorySource : repositoryOptional.get().getRepositorySources()) {
                            if(repositorySource.getActive()) {
                                repositorySource.setActive(false);
                                LOGGER.info("did set the active flag on the repository source {} to false", repositorySource.getCode());
                            }
                        }
                    }

                    break;

                case NAME:
                    if(null==updateRepositoryRequest.name) {
                        throw new IllegalStateException("the name must be supplied to update the repository");
                    }

                    String name = updateRepositoryRequest.name.trim();

                    if(0==name.length()) {
                        throw new ValidationException(new ValidationFailure(Repository.NAME_PROPERTY, "invalid"));
                    }

                    repositoryOptional.get().setName(name);
                    break;

                case INFORMATIONURL:
                    repositoryOptional.get().setInformationUrl(updateRepositoryRequest.informationUrl);
                    LOGGER.info("did set the information url on repository {} to {}", updateRepositoryRequest.code, updateRepositoryRequest.informationUrl);
                    break;

                default:
                    throw new IllegalStateException("unhandled filter for updating a repository");
            }
        }

        if(context.hasChanges()) {
            context.commitChanges();
        }
        else {
            LOGGER.info("update repository {} with no changes made", updateRepositoryRequest.code);
        }

        return new UpdateRepositoryResult();
    }

    @Override
    public CreateRepositoryResult createRepository(
            CreateRepositoryRequest createRepositoryRequest)
            throws ObjectNotFoundException {

        Preconditions.checkNotNull(createRepositoryRequest);

        final ObjectContext context = serverRuntime.getContext();

        if(!authorizationService.check(
                context,
                tryObtainAuthenticatedUser(context).orElse(null),
                null,
                Permission.REPOSITORY_ADD)) {
            throw new AuthorizationFailureException();
        }

        // the code must be supplied.

        if(Strings.isNullOrEmpty(createRepositoryRequest.code)) {
            throw new ValidationException(new ValidationFailure(Repository.CODE_PROPERTY, "required"));
        }

        // check to see if there is an existing repository with the same code; non-unique.

        {
            Optional<Repository> repositoryOptional = Repository.getByCode(context, createRepositoryRequest.code);

            if(repositoryOptional.isPresent()) {
                throw new ValidationException(new ValidationFailure(Repository.CODE_PROPERTY, "unique"));
            }
        }

        Repository repository = context.newObject(Repository.class);

        repository.setCode(createRepositoryRequest.code);
        repository.setName(createRepositoryRequest.name);
        repository.setInformationUrl(createRepositoryRequest.informationUrl);

        context.commitChanges();

        return new CreateRepositoryResult();
    }

    @Override
    public GetRepositorySourceResult getRepositorySource(GetRepositorySourceRequest request) throws ObjectNotFoundException {
        Preconditions.checkArgument(null!=request);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(request.code));

        final ObjectContext context = serverRuntime.getContext();

        Optional<RepositorySource> repositorySourceOptional = RepositorySource.getByCode(context, request.code);

        if(!repositorySourceOptional.isPresent()) {
            throw new ObjectNotFoundException(RepositorySource.class.getSimpleName(), request.code);
        }

        GetRepositorySourceResult result = new GetRepositorySourceResult();
        result.active = repositorySourceOptional.get().getActive();
        result.code = repositorySourceOptional.get().getCode();
        result.repositoryCode = repositorySourceOptional.get().getRepository().getCode();
        result.url = repositorySourceOptional.get().getUrl();
        return result;
    }

    @Override
    public UpdateRepositorySourceResult updateRepositorySource(UpdateRepositorySourceRequest request) throws ObjectNotFoundException {
        Preconditions.checkArgument(null!=request);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(request.code), "a code is required to identify the repository source to update");
        Preconditions.checkArgument(null!=request.filter, "filters must be provided to specify what aspects of the repository source should be updated");

        final ObjectContext context = serverRuntime.getContext();

        Optional<RepositorySource> repositorySourceOptional = RepositorySource.getByCode(context, request.code);

        if(!repositorySourceOptional.isPresent()) {
            throw new ObjectNotFoundException(RepositorySource.class.getSimpleName(), request.code);
        }

        if(!authorizationService.check(
                context,
                tryObtainAuthenticatedUser(context).orElse(null),
                repositorySourceOptional.get().getRepository(),
                Permission.REPOSITORY_EDIT)) {
            throw new AuthorizationFailureException();
        }

        for(UpdateRepositorySourceRequest.Filter filter : request.filter) {

            switch(filter) {

                case ACTIVE:
                    if(null==request.active) {
                        throw new IllegalArgumentException("the active field must be provided if the request requires it to be updated");
                    }
                    repositorySourceOptional.get().setActive(request.active);
                    LOGGER.info("did set the repository source {} active to {}", repositorySourceOptional.get(), request.active);
                    break;

                case URL:
                    repositorySourceOptional.get().setUrl(request.url);
                    break;

                default:
                    throw new IllegalStateException("unhandled filter; " + filter.name());

            }

        }

        if(context.hasChanges()) {
            context.commitChanges();
        }
        else {
            LOGGER.info("update repository source {} with no changes made", repositorySourceOptional.get());
        }

        return new UpdateRepositorySourceResult();

    }

}
