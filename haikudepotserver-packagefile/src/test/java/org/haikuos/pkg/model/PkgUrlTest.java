/*
 * Copyright 2015, Andrew Lindesay
 * Distributed under the terms of the MIT License.
 */

package org.haikuos.pkg.model;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class PkgUrlTest {

    /**
     * <p>Checks that if there is an empty string put in that it will throw.</p>
     */

    @Test(expected = Throwable.class)
    public void testConstruct_bad() {
        new PkgUrl("", PkgUrlType.HOMEPAGE);
    }

    @Test
    public void testConstruct_nakedUrl() {
        PkgUrl pkgUrl = new PkgUrl("http://www.haiku-os.org", PkgUrlType.HOMEPAGE);
        Assertions.assertThat(pkgUrl.getUrl()).isEqualTo("http://www.haiku-os.org");
        Assertions.assertThat(pkgUrl.getName()).isNull();
    }

    @Test
    public void testConstruct_nameWithUrl() {
        PkgUrl pkgUrl = new PkgUrl("Waiheke Island <http://www.haiku-os.org>", PkgUrlType.HOMEPAGE);
        Assertions.assertThat(pkgUrl.getUrl()).isEqualTo("http://www.haiku-os.org");
        Assertions.assertThat(pkgUrl.getName()).isEqualTo("Waiheke Island");
    }

    @Test
    public void testConstruct_emptyNameWithUrl() {
        PkgUrl pkgUrl = new PkgUrl("<http://www.haiku-os.org>", PkgUrlType.HOMEPAGE);
        Assertions.assertThat(pkgUrl.getUrl()).isEqualTo("http://www.haiku-os.org");
        Assertions.assertThat(pkgUrl.getName()).isEqualTo(null);
    }

    @Test
    public void testConstruct_NameWithUrlNoSpaceSeparator() {
        PkgUrl pkgUrl = new PkgUrl("Iceland<http://www.haiku-os.org>", PkgUrlType.HOMEPAGE);
        Assertions.assertThat(pkgUrl.getUrl()).isEqualTo("http://www.haiku-os.org");
        Assertions.assertThat(pkgUrl.getName()).isEqualTo("Iceland");
    }

}
