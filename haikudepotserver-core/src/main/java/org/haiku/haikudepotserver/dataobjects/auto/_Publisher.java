package org.haiku.haikudepotserver.dataobjects.auto;

import java.sql.Timestamp;
import java.util.List;

import org.apache.cayenne.exp.Property;
import org.haiku.haikudepotserver.dataobjects.Pkg;
import org.haiku.haikudepotserver.dataobjects.support.AbstractDataObject;

/**
 * Class _Publisher was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Publisher extends AbstractDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<Boolean> ACTIVE = Property.create("active", Boolean.class);
    public static final Property<String> CODE = Property.create("code", String.class);
    public static final Property<Timestamp> CREATE_TIMESTAMP = Property.create("createTimestamp", Timestamp.class);
    public static final Property<String> EMAIL = Property.create("email", String.class);
    public static final Property<Timestamp> MODIFY_TIMESTAMP = Property.create("modifyTimestamp", Timestamp.class);
    public static final Property<String> NAME = Property.create("name", String.class);
    public static final Property<String> SITE_URL = Property.create("siteUrl", String.class);
    public static final Property<List<Pkg>> PKGS = Property.create("pkgs", List.class);

    public void setActive(Boolean active) {
        writeProperty("active", active);
    }
    public Boolean getActive() {
        return (Boolean)readProperty("active");
    }

    public void setCode(String code) {
        writeProperty("code", code);
    }
    public String getCode() {
        return (String)readProperty("code");
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        writeProperty("createTimestamp", createTimestamp);
    }
    public Timestamp getCreateTimestamp() {
        return (Timestamp)readProperty("createTimestamp");
    }

    public void setEmail(String email) {
        writeProperty("email", email);
    }
    public String getEmail() {
        return (String)readProperty("email");
    }

    public void setModifyTimestamp(Timestamp modifyTimestamp) {
        writeProperty("modifyTimestamp", modifyTimestamp);
    }
    public Timestamp getModifyTimestamp() {
        return (Timestamp)readProperty("modifyTimestamp");
    }

    public void setName(String name) {
        writeProperty("name", name);
    }
    public String getName() {
        return (String)readProperty("name");
    }

    public void setSiteUrl(String siteUrl) {
        writeProperty("siteUrl", siteUrl);
    }
    public String getSiteUrl() {
        return (String)readProperty("siteUrl");
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
