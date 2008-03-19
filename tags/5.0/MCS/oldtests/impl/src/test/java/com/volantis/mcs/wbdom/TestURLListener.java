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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom;

import com.volantis.mcs.dom.output.SerialisationURLListener;
import org.apache.log4j.Category;

/**
 * A dubious test implementation of {@link com.volantis.mcs.dom.output.SerialisationURLListener}.
 * <p>
 * All it does currently is nicely violate the first law of testcases, (that
 * tests must not require an "expert" user to observe their results), by
 * logging URL attribute events. Yuck!
 * <p>
 * We will need to extend this to do something automated as well in order for
 * it to be generally useful.
 * 
 * @todo make detecting failures assert based rather than relying on logging! 
 */ 
public class TestURLListener implements SerialisationURLListener {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The log4j object to log to.
     */
    private static Category logger = Category.getInstance(
            "com.volantis.mcs.wbdom.TestURLAttributeListener");
    
    public void foundURL(String url) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received URL Attribute event for '" + url + "'");
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	733/2	geoff	VBM:2003070403 port from mimas and fix renames manually

 04-Jul-03	724/5	geoff	VBM:2003070403 first take at cleanup

 30-Jun-03	605/2	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/2	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
