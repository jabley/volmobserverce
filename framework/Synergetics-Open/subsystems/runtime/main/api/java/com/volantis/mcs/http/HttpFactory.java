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
package com.volantis.mcs.http;

/**
 * This factory is used to generate HTTP specific objects.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
abstract public class HttpFactory {

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static HttpFactory getDefaultInstance() {
        try {
            return (HttpFactory) Class.forName(
                "com.volantis.synergetics.runtime.impl.DefaultHttpFactory").newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * This method creates a new MutableHttpHeaders instance.
     *
     * @return A modifiable {@link HttpHeaders} object which must be populated
     *         by the user.
     */
    abstract public MutableHttpHeaders createHTTPHeaders();
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Aug-04	5058/1	geoff	VBM:2004080208 Implement the missing mutable http headers for device repository

 27-Jul-04	4937/3	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers - rework issues

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 ===========================================================================
*/
