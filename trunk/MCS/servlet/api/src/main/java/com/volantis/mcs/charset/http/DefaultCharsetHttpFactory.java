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
package com.volantis.mcs.charset.http;

/**
 * Default implementation of the CharsetHttpFactory abstract. This
 * implementation returned instances of DefaultCharsetHttpSelector.
 */
public class DefaultCharsetHttpFactory extends CharsetHttpFactory {

    /**
     * Hide the constructor. An instance can be obtained from the
     * CharsetHttpFactory.
     */
    DefaultCharsetHttpFactory() {
        super();
    }

    // javadoc inherited
    public CharsetHttpSelector getDefaultCharsetHttpSelector() {
        return new DefaultCharsetHttpSelector();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 04-Aug-04	5017/3	matthew	VBM:2004073003 clean up javadoc

 02-Aug-04	5017/1	matthew	VBM:2004073003 CharsetHttpFactory and Selector

 ===========================================================================
*/
