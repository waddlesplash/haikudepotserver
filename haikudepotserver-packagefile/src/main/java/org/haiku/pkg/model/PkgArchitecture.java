/*
 * Copyright 2013-2014, Andrew Lindesay
 * Distributed under the terms of the MIT License.
 */

package org.haiku.pkg.model;

/**
 * <p>In the HPKR file format, the architecture is a numerical value.  These numerical
 * values correlate to this enum.</p>
 */

public enum PkgArchitecture {
    ANY,        // 0
    X86,        // 1
    X86_GCC2,   // 2
    SOURCE,     // 3
    X86_64,     // 4
    PPC,        // 5
    ARM,        // 6
    M68K,       // 7
    SPARC       // 8
}
