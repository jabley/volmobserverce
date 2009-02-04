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
 * $Header: /src/voyager/com/volantis/mcs/servlet/CookieHelper.java,v 1.1 2003/03/10 14:22:22 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04 Mar 03    Steve           VBM:2003021101 - Conversion methods for cookies
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import java.util.Date;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

public class CookieHelper
{
    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(CookieHelper.class);
    
    /** Creates a new instance of CookieHelper */
    public CookieHelper()
    {
    }
    
    /** Convert an HttpClient cookie into a java servlet cookie
     * @param httpCookie  an  our.apache.commons.httpclient.Cookie object
     * @return an javax.servlet.http.Cookie version of the cookie
     */
    public static javax.servlet.http.Cookie toServletCookie( 
                  our.apache.commons.httpclient.Cookie httpCookie ) {
        if( httpCookie == null ) {
            return null;
        }
        
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie( 
                                httpCookie.getName(), httpCookie.getValue() );
        cookie.setComment( httpCookie.getComment() );
        
        if( httpCookie.getDomain() != null ) {
            cookie.setDomain( httpCookie.getDomain() );
        }

        if( httpCookie.getPath() != null ) {
            cookie.setPath( httpCookie.getPath() );
        }
        
        cookie.setSecure( httpCookie.getSecure() );
        cookie.setVersion( httpCookie.getVersion() );
        
        Date dte = httpCookie.getExpiryDate();
        if( dte != null ) {
            long millis = dte.getTime();
            long now = System.currentTimeMillis();
            int  seconds = (int)( ( millis - now ) / 1000 );
            cookie.setMaxAge( seconds );
        }
                
        return cookie;
    }
    
    /** Convert a servlet format cookie into an HttpClient format cookie 
     * @param servletCookie a javax.servlet.http.Cookie cookie object
     * @return an our.apache.commons.httpclient.Cookie format cookie.
     */
    public static our.apache.commons.httpclient.Cookie toHttpCookie(
                  javax.servlet.http.Cookie servletCookie )
    {
        if( servletCookie == null ) {
            return null;
        }
        
        our.apache.commons.httpclient.Cookie cookie = 
                                new our.apache.commons.httpclient.Cookie();
        cookie.setName( servletCookie.getName() );
        cookie.setValue( servletCookie.getValue() );
        cookie.setComment( servletCookie.getComment() );
        if( servletCookie.getDomain() == null ) {
            cookie.setDomainAttributeSpecified( false );
        } else {
            cookie.setDomain( servletCookie.getDomain() );
            cookie.setDomainAttributeSpecified( true );
        }
        if( servletCookie.getPath() == null ) {
            cookie.setPathAttributeSpecified( false );
        } else {
            cookie.setPath( servletCookie.getPath() );
            cookie.setPathAttributeSpecified( true );
        }
        cookie.setSecure( servletCookie.getSecure() );
        cookie.setVersion( servletCookie.getVersion() );
        
        int seconds = servletCookie.getMaxAge();
        long millis = System.currentTimeMillis() + ( seconds * 1000 );
        cookie.setExpiryDate( new Date( millis ) );
        
        return cookie;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 30-Jun-03	617/1	steve	VBM:2003061908 Repackage httpclient

 ===========================================================================
*/
