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
package com.volantis.mcs.integration;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.utilities.MarinerURL;

/**
 * Test URL Rewriter.
 */
public class TestURLRewriter implements URLRewriter {

    // Javadoc inherited
    public MarinerURL mapToExternalURL(MarinerRequestContext context,
                                       MarinerURL url) {
        return null;
    }

    // Javadoc inherited
    public MarinerURL mapToMarinerURL(MarinerRequestContext context,
                                      MarinerURL url) {
        return url;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 ===========================================================================
*/
