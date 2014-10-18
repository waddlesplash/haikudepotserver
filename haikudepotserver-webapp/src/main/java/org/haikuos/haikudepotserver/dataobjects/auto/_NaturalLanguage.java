package org.haikuos.haikudepotserver.dataobjects.auto;

import org.haikuos.haikudepotserver.dataobjects.support.AbstractDataObject;

/**
 * Class _NaturalLanguage was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _NaturalLanguage extends AbstractDataObject {

    public static final String CODE_PROPERTY = "code";
    public static final String IS_POPULAR_PROPERTY = "isPopular";
    public static final String NAME_PROPERTY = "name";

    public static final String ID_PK_COLUMN = "id";

    public String getCode() {
        return (String)readProperty(CODE_PROPERTY);
    }

    public Boolean getIsPopular() {
        return (Boolean)readProperty(IS_POPULAR_PROPERTY);
    }

    public String getName() {
        return (String)readProperty(NAME_PROPERTY);
    }

}
