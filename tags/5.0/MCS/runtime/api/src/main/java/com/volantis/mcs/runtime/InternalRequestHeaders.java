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
 * $Header: /src/voyager/com/volantis/mcs/runtime/InternalRequestHeaders.java,v 1.2 2003/02/18 13:58:03 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Feb-03    Byron           VBM:2003021204 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

/**
 * This class provides the implementation for the abstract methods in
 * RequestHeaders.
 *
 * @author Byron Wild
 */
public class InternalRequestHeaders extends RequestHeaders {

    // javadoc inherited
    public String getHeader(String name) {
        return null;
    }
}
