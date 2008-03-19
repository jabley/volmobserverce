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

/**
 * The SessionIdentifierSearcher interface describes the way we find a 
 * jsessionid within urls.
 */
public interface SessionIdentifierSearcher {

    /**
     * No matter which application server is being used, a url e.g.
     * http://www.my.com/page.jsp;jsessionid=737ea627?parm=value&parm2=value2#reference
     * can always be split into three components:
     * 1. The bit before the jsessionid. e.g. http://www.my.com/page.jsp in our example.
     * 2. The jsessionid e.g. ;jsessionid=737ea627
     * 3. The bit after the jsessionid e.g. ?parm=value&parm2=value2#reference. 
     * Note that the jsessionid can also be followed by a semicolon. 
     * <p>
     * We choose not
     * to make the definition of the three bits any more precise as the
     * jsessionid has been previously been found in a query parameter. In some
     * application servers jsessionid can be changed to something else.
     * @param url A String containing the url to be inspected. 
     * @return SessionIdentiferURL The SessionIdentifierURL contains the URL
     * but split up into the three parts described above.
     */
    public SessionIdentifierURL getJSessionId(String url);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jun-03	372/2	geoff	VBM:2003060609 avoid merge hell by committing on geoffs machine

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 ===========================================================================
*/
