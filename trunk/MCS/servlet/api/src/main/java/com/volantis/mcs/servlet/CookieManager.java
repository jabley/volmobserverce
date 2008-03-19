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
 * $Header: /src/voyager/com/volantis/mcs/servlet/CookieManager.java,v 1.1 2003/03/10 14:22:22 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06 Mar 03    Steve           VBM:2003021101 - Maintain a map of cookie 
 *                              collections keyed on an application name
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import java.util.Hashtable;
import java.util.Map;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

public class CookieManager
{
    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(CookieManager.class);
    
    /** The cookie map */
    private Map cookieMap = null;
    
    /** Creates a new instance of CookieManager */
    public CookieManager(){
        cookieMap = new Hashtable();
    }
    
    /** Return a named cookie store or null if there isnt one.
     * @param application  The name of the application owning the store
     * @return the CookieStore for that application or null
     */
    public CookieStore getCookieStore( String application ){
        return (CookieStore)cookieMap.get( application );
    }

    /** Create an empty cookie store for an application.
     * @param application  the application name
     */
    public void createCookieStore( String application ) {
        if( cookieMap.containsKey( application ) == false ) {
            cookieMap.put( application, new CookieStore() );
        }
    }

    /** Remove a CookieStore for a named application 
     * @param application  the application name
     */
    public void removeCookieStore( String application ) {
        if( cookieMap.containsKey( application ) ){
            cookieMap.remove( application );
        }
    }

    /** Create or remove an applications cookie store.
     * @param application  the application name
     * @param state  true if the store is to be created, false to remove it
     */
    public void setCookieStoreEnabled( String application, boolean state ) {
        if( state ) {
            if( isCookieStoreEnabled( application ) == false ) {
                createCookieStore( application );
            }
        } else {
            if( isCookieStoreEnabled( application ) == true ) {
                removeCookieStore( application );
            }
        }
    }

    /** Determine whether or not an application has an active cookie store
     * @param application  the application name
     * @return boolean flag  true denotes that a cookie store exists.
     */
    public boolean isCookieStoreEnabled( String application )  {
        return cookieMap.containsKey( application );
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

 ===========================================================================
*/
