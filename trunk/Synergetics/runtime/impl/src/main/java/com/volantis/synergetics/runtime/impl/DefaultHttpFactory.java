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
package com.volantis.synergetics.runtime.impl;

import com.volantis.mcs.http.HttpFactory;
import com.volantis.mcs.http.MutableHttpHeaders;
import com.volantis.synergetics.runtime.impl.DefaultMutableHttpHeaders;

/**
 * Default implementation of {@link com.volantis.mcs.http.HttpFactory}.
 */
public class DefaultHttpFactory extends HttpFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    // Javadoc inherited.
    public MutableHttpHeaders createHTTPHeaders() {

        return new DefaultMutableHttpHeaders();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 02-Aug-04	5058/1	geoff	VBM:2004080208 Implement the missing mutable http headers for device repository

 ===========================================================================
*/
