package org.haikuos.haikudepotserver.dataobjects.auto;

import org.haikuos.haikudepotserver.dataobjects.PkgUrlType;
import org.haikuos.haikudepotserver.dataobjects.PkgVersion;
import org.haikuos.haikudepotserver.dataobjects.support.AbstractDataObject;

/**
 * Class _PkgVersionUrl was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _PkgVersionUrl extends AbstractDataObject {

    public static final String NAME_PROPERTY = "name";
    public static final String URL_PROPERTY = "url";
    public static final String PKG_URL_TYPE_PROPERTY = "pkgUrlType";
    public static final String PKG_VERSION_PROPERTY = "pkgVersion";

    public static final String ID_PK_COLUMN = "id";

    public void setName(String name) {
        writeProperty(NAME_PROPERTY, name);
    }
    public String getName() {
        return (String)readProperty(NAME_PROPERTY);
    }

    public void setUrl(String url) {
        writeProperty(URL_PROPERTY, url);
    }
    public String getUrl() {
        return (String)readProperty(URL_PROPERTY);
    }

    public void setPkgUrlType(PkgUrlType pkgUrlType) {
        setToOneTarget(PKG_URL_TYPE_PROPERTY, pkgUrlType, true);
    }

    public PkgUrlType getPkgUrlType() {
        return (PkgUrlType)readProperty(PKG_URL_TYPE_PROPERTY);
    }


    public void setPkgVersion(PkgVersion pkgVersion) {
        setToOneTarget(PKG_VERSION_PROPERTY, pkgVersion, true);
    }

    public PkgVersion getPkgVersion() {
        return (PkgVersion)readProperty(PKG_VERSION_PROPERTY);
    }


}
