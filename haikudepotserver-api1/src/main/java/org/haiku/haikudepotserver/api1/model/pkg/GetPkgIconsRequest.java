/*
 * Copyright 2014, Andrew Lindesay
 * Distributed under the terms of the MIT License.
 */

package org.haiku.haikudepotserver.api1.model.pkg;

public class GetPkgIconsRequest {

    public String pkgName;

    public GetPkgIconsRequest() {
    }

    public GetPkgIconsRequest(String pkgName) {
        this.pkgName = pkgName;
    }
}
