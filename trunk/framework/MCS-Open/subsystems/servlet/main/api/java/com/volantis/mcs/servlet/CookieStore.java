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
 * $Header: /src/voyager/com/volantis/mcs/servlet/CookieStore.java,v 1.1 2003/03/10 14:22:22 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06 Mar 03    Steve           VBM:2003021101 - Maintain a cookie collection
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import our.apache.commons.httpclient.Cookie;
import our.apache.commons.httpclient.cookie.RFC2109Spec;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

public class CookieStore
{
    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(CookieStore.class);
    
    /** The actual cookies */
    private Cookie[] cookiejar = null;
    
    /** Creates a new instance of CookieStore */
    public CookieStore() {
    }
    
    /** Add an array of Cookies into the store
     * @param cookies the cookies to add
     */
    public void addCookies( Cookie[] cookies ) {
        if( cookiejar == null ) {
            cookiejar = new Cookie[ cookies.length ];
            for( int i = 0; i < cookies.length; i++ ) {
                cookiejar[i] = cookies[i];
            }
        } else {
            Cookie[] newJar = new Cookie[ cookiejar.length + cookies.length ];
            int i;
            for( i = 0; i < cookiejar.length; i++ ) {
                newJar[i] = cookiejar[i];
            }
            for( int j = 0; j < cookies.length; j++ ) {
                newJar[i++] = cookies[j];
            }
            cookiejar = newJar;
        }
    }

    /** Add an array of servlet request style cookies into the store
     * @param cookies the javax.servlet.http.Cookie cookies to add.
     */
    public void addServletCookies( javax.servlet.http.Cookie[] cookies ) {
        if( cookiejar == null ) {
            cookiejar = new Cookie[ cookies.length ];
            for( int i = 0; i < cookies.length; i++ ) {
                cookiejar[i] = CookieHelper.toHttpCookie( cookies[i] );
                // System.out.println( "Added cookie " + cookiejar[i].toString() );
            }
        } else {
            Cookie[] newJar = new Cookie[ cookiejar.length + cookies.length ];
            int i;
            for( i = 0; i < cookiejar.length; i++ ) {
                newJar[i] = cookiejar[i];
            }
            for( int j = 0; j < cookies.length; j++ ) {
                newJar[i] = CookieHelper.toHttpCookie( cookies[j] );
                // System.out.println( "Added cookie " + newJar[i].toString() );
                i++;
            }
            cookiejar = newJar;
        }
    }

    /** Return an array of Cookies that are destined for a given URL
     * an example call to this could be:
     * <code>
     * URL url = new URL( "http://www.volantis.com:8080/wibble/foo.html" );
     * Cookie[] cookies = store.getCookies( url.getHost(), url.getPort(), url.getPath() );
     * </code>
     * @param host  The host name of a remote server
     * @param port  Server port number
     * @param path  Request URI
     * @return array of Cookie objects destined for the given url.
     */
    public Cookie[] getCookies( String host, int port, String path ) {
        if( ( cookiejar == null ) || ( cookiejar.length == 0 ) ) {
           return null;
        }
        
        if( port == -1 ){
            port = 80;
        }

        RFC2109Spec spec = new RFC2109Spec();
        return spec.match( host, port, path, false, cookiejar );        
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 30-Jun-03	617/1	steve	VBM:2003061908 Repackage httpclient

 ===========================================================================
*/
