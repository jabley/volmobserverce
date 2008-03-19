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
package com.volantis.mcs.ibm.websphere.portalserver;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.ibm.websphere.servlet.request.HttpServletRequestProxy;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;


/**
 * A wrapper for the request.  This class is used when passing the request
 * to MCS to enable the request header accept-charset to be modifed by the 
 * portal filter.
 *
 */
public class MCSPortalRequestWrapper extends HttpServletRequestProxy {


    /**
     * The copyright statement.
     */
    private static String mark =
        "(c) Volantis Systems Ltd 2004. ";
        
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MCSPortalRequestWrapper.class);

    /**
     * The wrapped request
     */
    private HttpServletRequest request;
    
    /**
     * The character encoding to use for 
     * the accept-charset header.
     */
    private String characterEncoding;
    
    /**
     * Our fake accept-charset header.
     */
    private Vector acceptCharsetHeader;
    /**
     * Construct our proxied request
     * @param request The original request
     * @param characterEncoding Character Encoding to use.
     */
    public MCSPortalRequestWrapper(HttpServletRequest request, 
                                    String characterEncoding) {                                       
        this.request = request;
        this.characterEncoding = characterEncoding;
        acceptCharsetHeader = new Vector();
        acceptCharsetHeader.add(characterEncoding);
    }

    //javadoc inherited                                                                                                                                                                                                            
    public String getHeader(String s)
    {
        /*
         * We need to fool MCS into using the charset from the
         * portal.
         */
        if ("accept-charset".equals(s.toLowerCase())) {
            // Check that we have elements before trying to send the first one.
            // We should always have one element but we are just making sure.
            if (!acceptCharsetHeader.isEmpty()) {
                return (String)acceptCharsetHeader.firstElement();
            }
        }        
        return getProxiedHttpServletRequest().getHeader(s);
    }
                                                                                                                                                                                                     

    //javadoc inherited                                                                                                                                                                                                            
    public Enumeration getHeaders(String s)
    {
        // We need to fool MCS into using the charset from the
        // portal.
        if ("accept-charset".equals(s.toLowerCase())) {

            return acceptCharsetHeader.elements();
        }
        return getProxiedHttpServletRequest().getHeaders(s);
    }

    //javadoc inherited                                                                                                                                                                                                            
    public String getCharacterEncoding()
    {
        return this.characterEncoding;
    }

    //javadoc inherited                                                                                                                                                                                                            
    protected HttpServletRequest getProxiedHttpServletRequest() {
        return request;
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

 23-Sep-04	5620/1	ianw	VBM:2004052202 ported forward IBM charset fixes

 25-May-04	4537/2	ianw	VBM:2004052202 Set MCS accept-charset to be specified by WPS

 ===========================================================================
*/
