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

package com.volantis.mcs.policies.variants.content;

import com.volantis.mcs.policies.BaseURLPolicy;

/**
 * A type safe enumeration of the base locations within which resources may be
 * found.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see BaseURLRelative
 * @see BaseURLPolicy
 * @since 3.5.1
 */
public class BaseLocation {

    /**
     * Indicates that the associated resource is relative to the device.
     */
    public static final BaseLocation DEVICE = new BaseLocation("DEVICE", true);

    /**
     * Indicates that the associated resource is relative to the context.
     */
    public static final BaseLocation CONTEXT = new BaseLocation("CONTEXT", true);

    /**
     * Indicates that the associated resource is relative to the host.
     */
    public static final BaseLocation HOST = new BaseLocation("HOST", true);

    /**
     * Indicates that the location of the associated resource is the default
     * location.
     *
     * <p>The default location may be specified in a BaseURLPolicy, or if
     * not then defaults to {@link #CONTEXT}.</p>
     */
    public static final BaseLocation DEFAULT = new BaseLocation("DEFAULT", true);

    private final String myName; // for debug only

    private BaseLocation(String name, boolean onServer) {
        myName = name;
    }

    public String toString() {
        return myName;
    }
}
