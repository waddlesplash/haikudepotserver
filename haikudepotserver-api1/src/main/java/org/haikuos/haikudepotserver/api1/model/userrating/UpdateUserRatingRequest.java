/*
* Copyright 2014, Andrew Lindesay
* Distributed under the terms of the MIT License.
*/

package org.haikuos.haikudepotserver.api1.model.userrating;

import org.haikuos.haikudepotserver.api1.model.PkgVersionType;

import java.util.List;

public class UpdateUserRatingRequest {

    public enum Filter {
        NATURALLANGUAGE,
        USERRATINGSTABILITY,
        COMMENT,
        RATING
    };

    public String code;

    public String naturalLanguageCode;

    public String userRatingStabilityCode;

    public String comment;

    public Short rating;

    public List<Filter> filter;

}