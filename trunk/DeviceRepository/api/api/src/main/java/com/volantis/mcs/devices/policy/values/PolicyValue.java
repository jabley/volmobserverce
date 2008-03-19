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
package com.volantis.mcs.devices.policy.values;

/**
 * This interface represents a Policy Type.
 * 
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @deprecated This interface and all derived interfaces are deprecated in
 * favour of the ones defined in {@link com.volantis.shared.metadata.value}.
 * See the individual interfaces for details as to their replacement.
 */
public interface PolicyValue {

    /**
     * Get a String representation of this value the Text value of this policy.
     * 
     * <p>For simle types the value is returned as is e.g Boolean values are
     * represented by the strings "true" and "false".</p>
     * 
     * <p>For composite types an attempt is made to represent the value in as
     * sensible manner as is possible, no guarantee is made on the ability for
     * this value to be easily parsed.</p>
     * 
     * <p>List types are returned space separated e.g "item1 item2 item3".</p>
     * 
     * <p>Structure types are returned as "[fieldname1=value1] 
     * [fieldname2=value2]" wehere the values follow the rules above.</p>
     * 
     * @return an unmodifiable String value
     */
    public String getAsString();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 21-Jul-04	4930/1	geoff	VBM:2004072104 Public API for Device Repository: Basics

 ===========================================================================
*/
