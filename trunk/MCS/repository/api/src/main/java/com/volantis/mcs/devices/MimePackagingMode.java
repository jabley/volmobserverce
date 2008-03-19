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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.devices;

import java.util.HashMap;

/**
 * A typesafe enumeration of the possible MIME packaging modes for a device.
 * See {@link DevicePolicyConstants#MIME_PACKAGING_MODE}.
 */
public class MimePackagingMode {
    /**
     * The set of all literals. Keyed on the internal string version of the
     * enumeration, mapping to the MimePackagingMode equivalent.
     * 
     * <p>NB: This static member *must* appear before the enumeration literals
     * for this to work. If it does not, the access of this variable within
     * the literal construction (within this class's constructor) will find
     * this variable to be null (i.e. it won't have been initialized yet).
     * The Java Language Spec second edition, section 8.7, specifically
     * states that initialization is performed in "textual order".</p>
     *
     * @associates <{MimePackagingMode}>
     * @supplierRole entries
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    private static final HashMap entries = new HashMap();

    /**
     * Default packaging behaviour should be followed.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole DEFAULT
     */
    public static final MimePackagingMode DEFAULT =
            new MimePackagingMode("default");

    /**
     * Packaging should never be used.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEVER
     */
    public static final MimePackagingMode NEVER =
            new MimePackagingMode("never");

    /**
     * Packaging should always be used (if globally enabled and the protocol
     * supports packaging).
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ALWAYS
     */
    public static final MimePackagingMode ALWAYS =
            new MimePackagingMode("always");

    /**
     * The internal name for the enumeration literal.
     */
    private final String name;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the internal name for the new literal
     */
    private MimePackagingMode(String name) {
        this.name = name;

        entries.put(name, this);
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return name;
    }

    /**
     * Retrieves the enumeration literal that is equivalent to the given
     * internal name, or null if the name is not recognized.
     *
     * @param name the internal name to be looked up
     * @return the equivalent enumeration literal or null if the name is
     *         not recognized
     */
    public static MimePackagingMode literal(String name) {
        return (MimePackagingMode)entries.get(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Sep-04	5495/1	philws	VBM:2004091003 Policy-based MIME packaging mode

 ===========================================================================
*/
