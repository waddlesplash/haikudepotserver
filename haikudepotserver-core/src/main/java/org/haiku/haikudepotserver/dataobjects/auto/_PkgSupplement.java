package org.haiku.haikudepotserver.dataobjects.auto;

import java.sql.Timestamp;
import java.util.List;

import org.apache.cayenne.exp.Property;
import org.haiku.haikudepotserver.dataobjects.Pkg;
import org.haiku.haikudepotserver.dataobjects.PkgChangelog;
import org.haiku.haikudepotserver.dataobjects.PkgIcon;
import org.haiku.haikudepotserver.dataobjects.PkgLocalization;
import org.haiku.haikudepotserver.dataobjects.PkgPkgCategory;
import org.haiku.haikudepotserver.dataobjects.PkgScreenshot;
import org.haiku.haikudepotserver.dataobjects.support.AbstractDataObject;

/**
 * Class _PkgSupplement was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _PkgSupplement extends AbstractDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> BASE_PKG_NAME = Property.create("basePkgName", String.class);
    public static final Property<Timestamp> CREATE_TIMESTAMP = Property.create("createTimestamp", Timestamp.class);
    public static final Property<Timestamp> ICON_MODIFY_TIMESTAMP = Property.create("iconModifyTimestamp", Timestamp.class);
    public static final Property<Timestamp> MODIFY_TIMESTAMP = Property.create("modifyTimestamp", Timestamp.class);
    public static final Property<List<PkgChangelog>> PKG_CHANGELOGS = Property.create("pkgChangelogs", List.class);
    public static final Property<List<PkgIcon>> PKG_ICONS = Property.create("pkgIcons", List.class);
    public static final Property<List<PkgLocalization>> PKG_LOCALIZATIONS = Property.create("pkgLocalizations", List.class);
    public static final Property<List<PkgPkgCategory>> PKG_PKG_CATEGORIES = Property.create("pkgPkgCategories", List.class);
    public static final Property<List<PkgScreenshot>> PKG_SCREENSHOTS = Property.create("pkgScreenshots", List.class);
    public static final Property<List<Pkg>> PKGS = Property.create("pkgs", List.class);

    public void setBasePkgName(String basePkgName) {
        writeProperty("basePkgName", basePkgName);
    }
    public String getBasePkgName() {
        return (String)readProperty("basePkgName");
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        writeProperty("createTimestamp", createTimestamp);
    }
    public Timestamp getCreateTimestamp() {
        return (Timestamp)readProperty("createTimestamp");
    }

    public void setIconModifyTimestamp(Timestamp iconModifyTimestamp) {
        writeProperty("iconModifyTimestamp", iconModifyTimestamp);
    }
    public Timestamp getIconModifyTimestamp() {
        return (Timestamp)readProperty("iconModifyTimestamp");
    }

    public void setModifyTimestamp(Timestamp modifyTimestamp) {
        writeProperty("modifyTimestamp", modifyTimestamp);
    }
    public Timestamp getModifyTimestamp() {
        return (Timestamp)readProperty("modifyTimestamp");
    }

    public void addToPkgChangelogs(PkgChangelog obj) {
        addToManyTarget("pkgChangelogs", obj, true);
    }
    public void removeFromPkgChangelogs(PkgChangelog obj) {
        removeToManyTarget("pkgChangelogs", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<PkgChangelog> getPkgChangelogs() {
        return (List<PkgChangelog>)readProperty("pkgChangelogs");
    }


    public void addToPkgIcons(PkgIcon obj) {
        addToManyTarget("pkgIcons", obj, true);
    }
    public void removeFromPkgIcons(PkgIcon obj) {
        removeToManyTarget("pkgIcons", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<PkgIcon> getPkgIcons() {
        return (List<PkgIcon>)readProperty("pkgIcons");
    }


    public void addToPkgLocalizations(PkgLocalization obj) {
        addToManyTarget("pkgLocalizations", obj, true);
    }
    public void removeFromPkgLocalizations(PkgLocalization obj) {
        removeToManyTarget("pkgLocalizations", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<PkgLocalization> getPkgLocalizations() {
        return (List<PkgLocalization>)readProperty("pkgLocalizations");
    }


    public void addToPkgPkgCategories(PkgPkgCategory obj) {
        addToManyTarget("pkgPkgCategories", obj, true);
    }
    public void removeFromPkgPkgCategories(PkgPkgCategory obj) {
        removeToManyTarget("pkgPkgCategories", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<PkgPkgCategory> getPkgPkgCategories() {
        return (List<PkgPkgCategory>)readProperty("pkgPkgCategories");
    }


    public void addToPkgScreenshots(PkgScreenshot obj) {
        addToManyTarget("pkgScreenshots", obj, true);
    }
    public void removeFromPkgScreenshots(PkgScreenshot obj) {
        removeToManyTarget("pkgScreenshots", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<PkgScreenshot> getPkgScreenshots() {
        return (List<PkgScreenshot>)readProperty("pkgScreenshots");
    }


    public void addToPkgs(Pkg obj) {
        addToManyTarget("pkgs", obj, true);
    }
    public void removeFromPkgs(Pkg obj) {
        removeToManyTarget("pkgs", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<Pkg> getPkgs() {
        return (List<Pkg>)readProperty("pkgs");
    }


}
