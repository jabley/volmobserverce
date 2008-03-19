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
package com.volantis.mcs.context;

import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.integration.PageURLDetails;
import com.volantis.mcs.utilities.MarinerURL;

/**
 * This class is for use in testing the page url rewriting facility. The
 * reason this is not an inner class is because the RuntimePageURLRewriter
 * must determine the default constructor for the PageURLRewriter it is
 * provided with if there is no MarinerApplication constructor. The way it
 * does this is to look for the constructor with no arguments. However,
 * inner class default constructors always have a single argument in their
 * default constructor - their outer class - so the RuntimePageURLRewriter
 * cannot find the default constructor and breaks.
 */
public class PageURLRewriterImpl implements PageURLRewriter {
    public static final String REWRITTEN_URL = "rewrittenURL";

    /**
     * Simply return the REWRITTEN_URL as a MarinerURL.
     *
     * @param context the MarinerRequestContext - unused
     * @param url the MarinerURL to rewrite - unused
     * @param details the PageURL - unused
     * @return REWRITTEN_URL.
     */
    public MarinerURL rewriteURL(MarinerRequestContext context,
                                 MarinerURL url,
                                 PageURLDetails details) {
        return new MarinerURL(REWRITTEN_URL);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Jul-04	4778/1	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 ===========================================================================
*/
