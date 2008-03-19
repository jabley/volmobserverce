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

package com.volantis.mcs.runtime.dissection;

import junit.framework.TestCase;

/**
 * This is the test case for the StandardSessionIdentifierSearcher
 */
public class StandardSessionIdentifierSearcherTestCase extends TestCase {

    /**
     * The object under test
     */
    private StandardSessionIdentifierSearcher searcher;

    /**
     * Standard junit constructor
     * @param name
     */
    public StandardSessionIdentifierSearcherTestCase(String name) {
        super(name);
    }

    /**
     * Tests getJSessionId for a variety of urls that may or may not contain a
     * jsessionid.
     * @throws Exception
     */
    public void testGetJSessionId() throws Exception {
        searcher  = new StandardSessionIdentifierSearcher();

        checkTestResults("http://www.my.com/page.jsp",
                         "http://www.my.com/page.jsp",
                         null,
                         null);

        checkTestResults("http://www.my.com/page.jsp;jsessionid=12734",
                         "http://www.my.com/page.jsp",
                         ";jsessionid=12734",
                         null);
       
        checkTestResults("http://www.my.com/page.jsp;jsessionid=12734?parm=value#ref",
                         "http://www.my.com/page.jsp",
                         ";jsessionid=12734",
                         "?parm=value#ref");

        checkTestResults("http://www.my.com/page.jsp?parm=value#ref",
                         "http://www.my.com/page.jsp?parm=value#ref",
                         null,
                         null);
                         
        checkTestResults("http://www.my.com/page.jsp;fred=bloggs",
                         "http://www.my.com/page.jsp;fred=bloggs",
                         null,
                         null);

        checkTestResults("http://www.my.com/page.jsp;fred=bloggs;jsessionid=23",
                         "http://www.my.com/page.jsp;fred=bloggs",
                         ";jsessionid=23",
                         null);

        checkTestResults("http://www.my.com/page.jsp;fred=bloggs;jsessionid=23;bill=jones",
                         "http://www.my.com/page.jsp;fred=bloggs",
                         ";jsessionid=23",
                         ";bill=jones");

        checkTestResults("http://www.my.com/page.jsp;fred=bloggs;jsessionid=23?parm=value#ref",
                         "http://www.my.com/page.jsp;fred=bloggs",
                         ";jsessionid=23",
                         "?parm=value#ref");

        checkTestResults("http://www.my.com/page.jsp;fred=bloggs;jsessionid=23;bill=jones?parm=value#ref",
                         "http://www.my.com/page.jsp;fred=bloggs",
                         ";jsessionid=23",
                         ";bill=jones?parm=value#ref");

        checkTestResults("http://www.my.com/page.jsp;fred=bloggs?parm=value#ref",
                         "http://www.my.com/page.jsp;fred=bloggs?parm=value#ref",
                         null,
                         null);

        checkTestResults("http://www.my.com/page.jsp;jsessionid=23?parm=value#ref",
                         "http://www.my.com/page.jsp",
                         ";jsessionid=23",
                         "?parm=value#ref");

        checkTestResults("http://www.my.com/page.jsp;jsessionid=23;bill=jones?parm=value#ref",
                         "http://www.my.com/page.jsp",
                         ";jsessionid=23",
                         ";bill=jones?parm=value#ref");
    }

    /**
     * Checks the test results.
     * @param url
     * @param path The expected part of the url before the jsessionid 
     * @param jsessionid The expected jsessionid
     * @param query The expected query parameters
     */
    private void checkTestResults(String url, String path,
                                  String jsessionid, String query) {
        SessionIdentifierURL results = searcher.getJSessionId(url);
        
        assertEquals(url + " path wrong", path, results.getPrefix());
        assertEquals(url + " jsessionid wrong", jsessionid, 
                     results.getJsessionid());
        assertEquals(url + " query wrong", query, results.getSuffix());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 16-Jun-03	372/2	geoff	VBM:2003060609 avoid merge hell by committing on geoffs machine

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 ===========================================================================
*/
