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
 * This is the standard implementation of the SessionIdentifierSearcher
 * interface. It assumes that the jsessionid is found in the place described in
 * the specification e.g.
 * http://www.my.com/page.jsp;jsessionid=737ea627?parm=value&parm2=value2#reference
 */
public class StandardSessionIdentifierSearcher
    implements SessionIdentifierSearcher {

    /**
     * Default constructor
     */
    public StandardSessionIdentifierSearcher() {        
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.runtime.dissection.SessionIdentifierSearcher#getJSessionId(java.lang.String)
     */
    public SessionIdentifierURL getJSessionId(String url) {
        /* Example: We break the url shown below
         * http://www.my.com/page.jsp;jsessionid=737ea627?parm=value&parm2=value2#reference
         * into the following, the path, the jsessionid, the query parameters
         * http://www.my.com/page.jsp
         * ;jsessionid=737ea627
         * ?parm=value&parm2=value2#reference
         * As a jsessionid is prefixed by a semicolon we include it as part of
         * the jsessionid. The query parameters are optional so the ? is
         * included only if they are present, rather than as part of the
         * jsessionid
         */
        SessionIdentifierURL toReturn = new SessionIdentifierURL();

        // Loop through the parameters until we find the jsessionid
        int paramStart = -1;
        boolean found = false;
        while (!found) {
            // Need to start searching after the current semicolon 
            paramStart = url.indexOf(';', paramStart + 1);                       
         
            if (paramStart == -1) {
                // The url has no parameters and therefore no jsessionid
                toReturn.setPrefix(url);
                return toReturn;         
            } else if (url.substring(paramStart).startsWith(";jsessionid")) {
                toReturn.setPrefix(url.substring(0, paramStart));
                found = true;   
            } 
        }                        
          
        // Get the jsessionid
        // Need to start searching after the current semicolon
        int startQuery = url.indexOf('?', paramStart + 1);
        int nextSemicolon = url.indexOf(';', paramStart + 1);
        int endJsessionid;
        if (startQuery == -1 && nextSemicolon == -1) {
            // Nothing follows the jsessionid
            toReturn.setJsessionid(url.substring(paramStart));
            return toReturn;
        } else  {
            // The jsessionid ends at the first of either the first question
            // mark or the next semicolon.
            if (startQuery == -1) {
                endJsessionid = nextSemicolon;
            } else if (nextSemicolon == -1) {
                endJsessionid = startQuery;
            } else {
                endJsessionid = Math.min(startQuery, nextSemicolon);
            }           
            toReturn.setJsessionid(url.substring(paramStart, endJsessionid));
        }
          
        // The remainder of the String are the query parameters  
        toReturn.setSuffix(url.substring(endJsessionid));  
        return toReturn;
    }

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
