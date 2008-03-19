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
 * $Header: /cvs/architecture/architecture/api/src/java/com/volantis/mcs/http/servlet/HttpServletFactory.java,v 1.3 2004/07/22 14:44:23 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Jul-04    Ian             Assignment:886 - Created
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.http.servlet;

import javax.servlet.http.HttpServletRequest;

import com.volantis.mcs.http.HttpHeaders;

/**
 * This factory is used to generate HTTP specific objects from Servlet specific
 * objects.
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
public abstract class HttpServletFactory {

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static HttpServletFactory getDefaultInstance() {
        try {
            return (HttpServletFactory) Class.forName(
                    "com.volantis.mcs.http.servlet.DefaultHttpServletFactory").
                    newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * This method returns the {@link HttpHeaders} for the given
     * {@link HttpServletRequest}.
     * 
     * @param request The {@link HttpServletRequest}for which the headers
     *      should be retrieved.
     */
    abstract public HttpHeaders getHTTPHeaders(HttpServletRequest request);

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Aug-04	5058/1	geoff	VBM:2004080208 Implement the missing mutable http headers for device repository

 28-Jul-04	4940/1	geoff	VBM:2004072103 Public API for Device Repository (umbrella)

 28-Jul-04	4970/1	byron	VBM:2004072704 Public API for Device Repository: implement unit and/or integration tests

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 ===========================================================================
*/
