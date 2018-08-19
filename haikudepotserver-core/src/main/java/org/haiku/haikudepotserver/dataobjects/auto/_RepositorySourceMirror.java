package org.haiku.haikudepotserver.dataobjects.auto;

import java.sql.Timestamp;

import org.apache.cayenne.exp.Property;
import org.haiku.haikudepotserver.dataobjects.Country;
import org.haiku.haikudepotserver.dataobjects.RepositorySource;
import org.haiku.haikudepotserver.dataobjects.support.AbstractDataObject;

/**
 * Class _RepositorySourceMirror was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _RepositorySourceMirror extends AbstractDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<Boolean> ACTIVE = Property.create("active", Boolean.class);
    public static final Property<String> BASE_URL = Property.create("baseUrl", String.class);
    public static final Property<String> CODE = Property.create("code", String.class);
    public static final Property<Timestamp> CREATE_TIMESTAMP = Property.create("createTimestamp", Timestamp.class);
    public static final Property<String> DESCRIPTION = Property.create("description", String.class);
    public static final Property<Boolean> IS_PRIMARY = Property.create("isPrimary", Boolean.class);
    public static final Property<Timestamp> MODIFY_TIMESTAMP = Property.create("modifyTimestamp", Timestamp.class);
    public static final Property<Country> COUNTRY = Property.create("country", Country.class);
    public static final Property<RepositorySource> REPOSITORY_SOURCE = Property.create("repositorySource", RepositorySource.class);

    public void setActive(Boolean active) {
        writeProperty("active", active);
    }
    public Boolean getActive() {
        return (Boolean)readProperty("active");
    }

    public void setBaseUrl(String baseUrl) {
        writeProperty("baseUrl", baseUrl);
    }
    public String getBaseUrl() {
        return (String)readProperty("baseUrl");
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

    public void setDescription(String description) {
        writeProperty("description", description);
    }
    public String getDescription() {
        return (String)readProperty("description");
    }

    public void setIsPrimary(Boolean isPrimary) {
        writeProperty("isPrimary", isPrimary);
    }
    public Boolean getIsPrimary() {
        return (Boolean)readProperty("isPrimary");
    }

    public void setModifyTimestamp(Timestamp modifyTimestamp) {
        writeProperty("modifyTimestamp", modifyTimestamp);
    }
    public Timestamp getModifyTimestamp() {
        return (Timestamp)readProperty("modifyTimestamp");
    }

    public void setCountry(Country country) {
        setToOneTarget("country", country, true);
    }

    public Country getCountry() {
        return (Country)readProperty("country");
    }


    public void setRepositorySource(RepositorySource repositorySource) {
        setToOneTarget("repositorySource", repositorySource, true);
    }

    public RepositorySource getRepositorySource() {
        return (RepositorySource)readProperty("repositorySource");
    }


}
