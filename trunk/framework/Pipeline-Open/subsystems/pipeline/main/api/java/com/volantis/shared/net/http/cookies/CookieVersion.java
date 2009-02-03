/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.http.cookies;

import java.io.Serializable;
import java.io.ObjectStreamException;
import java.util.HashMap;

/**
 * A typesafe enumeration of CookieVersions. CookieVersions are used to
 * represent different approaches to handling cookies. The first commonly
 * adopted approach was defined by
 * <a href="http://wp.netscape.com/newsref/std/cookie_spec.html">Netscape</a>.
 * This was superceeded by
 * <a href="http://www.w3.org/Protocols/rfc2109/rfc2109">RFC 2109</a> which
 * was defined as version 1.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public final class CookieVersion implements Serializable {

    /**
     * Constant that defined how many entries are allowed in the indexedEntries
     * List. As CookieVersions are added to the list using the set method (so
     * as to avoid putting entries in the wrong index (as add can cause) we
     * have to define the List to be large enough before we add entries to
     * avoid IndexOutOfBoundsExceptions.
     */
    private static final int NUM_ENTRIES = 2;

    /**
     * The set of all CookieVersions indexed by their version name.
     * <p/>
     * NB: This static member *must* appear before the enumeration literals
     * for this to work. If it does not, the access of this variable within
     * the literal construction (within this class's constructor) will find
     * this variable to be null (i.e. it won't have been initialized yet).
     * The Java Language Spec second edition, section 8.7, specifically
     * states that initialization is performed in "textual order".
     *
     */
    private static final HashMap namedEntries = new HashMap();

    /**
     * The set of all CookieVersions indexed by their version number.
     * <p/>
     * NB: This static member *must* appear before the enumeration literals
     * for this to work. If it does not, the access of this variable within
     * the literal construction (within this class's constructor) will find
     * this variable to be null (i.e. it won't have been initialized yet).
     * The Java Language Spec second edition, section 8.7, specifically
     * states that initialization is performed in "textual order".
     *
     */
    private static final CookieVersion[] indexedEntries =
            new CookieVersion[NUM_ENTRIES];

    /**
     * CookieVersion representing "netscape" style cookies (Version 0).
     */
    public static final CookieVersion NETSCAPE =
            new CookieVersion("netscape", 0);

    /**
     * CookieVersion representing "rfc2109" style cookies (Version 1).
     */
    public static final CookieVersion RFC2109 =
            new CookieVersion("rfc2109", 1);

    /**
     * Used for readResolve method. This array contains the same information
     * as indexedEntries but is needed to allow readResolve to work correctly.
     * <p/>
     * NB: This static member must appear after the enumeration literals for
     * this to work.
     */
    private static final CookieVersion[] PRIVATE_VALUES = {NETSCAPE, RFC2109};

    /**
     * The name of the Cookie version. Transient as it is not serialised.
     */
    private transient final String versionName;

    /**
     * The integer representation of the cookie version.
     */
    private final int versionNumber;


    /**
     * Initializes the new instance using the given parameters.
     *
     * @param versionName the name for the new CookieVersion.
     * @param versionNumber the integer representation of the CookieVersion.
     */
    private CookieVersion(final String versionName, final int versionNumber) {
        if ((versionNumber < 0) || (versionNumber >= indexedEntries.length)) {
            // this should never happen unless you have broken this class.
            throw new IllegalArgumentException("Invalid version number");
        }

        this.versionName = versionName;
        this.versionNumber = versionNumber;
        namedEntries.put(versionName, this);
        indexedEntries[versionNumber] = this;
    }

    /**
     * Retrieves the CookieVersion instance that is equivalent to the given
     * cookie version name, or null if the name is not recognized.
     *
     * @param name the name of the cookie version name to be looked up.
     * @return the equivalent enumeration CookieVersion or null if the name is
     *         not recognized.
     */
    public static CookieVersion getCookieVersion(final String name) {
        return (CookieVersion) namedEntries.get(name);
    }

    /**
     * Retrieves the CookieVersion instance that is equivalent to the given
     * cookie version number or null if the number is not recognized.
     *
     * @param version the integer representation of the CookieVersion to be
     * looked up.
     * @return the equivalent enumeration CookieVersion or null if the version
     * number is not recognized.
     */
    public static CookieVersion getCookieVersion(final int version) {
        CookieVersion result = null;
        if ((version >= 0) && (version < indexedEntries.length)) {
            result = indexedEntries[version];
        }

        return result;
    }

    /**
     * @return a string representation of this cookie. This is a convienience
     * for debugging.
     */
    public String toString() {
        return "[" + getName() + " "+ getNumber() + "]";
    }

    /**
     * Return the integer representation for this cookie version. The returned
     * value will be 0 or 1 which correspond to version names "netscape" and
     * "rfc2109" respectively.
     *
     * @return the integer representation of this cookie version.
     */
    public int getNumber() {
        return versionNumber;
    }

    /**
     * Return the string representation of this cookie version.
     * The returned value will be "netscape" or "rfc2109" which correspond to
     * version numbers 0 and 1 respectively.
     *
     * @return the string representation of this cookie version.
     */
    public String getName() {
        return versionName;
    }

    /**
     * @return the unique version of the CookieVersion that corresponds to
     * this version number.
     */
    private Object readResolve() {
        return PRIVATE_VALUES[versionNumber];
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Mar-05	7337/5	matthew	VBM:2005030809 Add a CookieVersion typesafe enum and use it in the Cookie interface and its implementations

 09-Mar-05	7337/1	matthew	VBM:2005030809 Add a CookieVersion typesafe enum and use it in the Cookie interface and its implementations

 ===========================================================================
*/
