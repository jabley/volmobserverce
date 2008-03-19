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
 * $Header: /src/voyager/com/volantis/mcs/atg/dynamo561/PostDataServletUtils.java
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-Sep-02    Chris W         VBM:2002081511 - refactored to share common
 *                              methods with ATGFormFragmentationServlet by
 *                              creating PostDataServletUtils.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.atg.dynamo;

import com.volantis.mcs.utilities.HttpResponseHeader;
import com.volantis.synergetics.NameValuePair;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for Remap servlet and ATGFormFragmentationServlet.
 * Its methods are thread safe.
 */ 
public class PostDataServletUtils {
    /**
     * Used for logging
     */
    private static LogDispatcher logger =
            LocalizationFactory.createLogger(PostDataServletUtils.class);


    /**
     * Returns a collection of http request headers excluding the content length
     * and content type headers which we are going to change by sending different
     * POST data back.
     * @param request The HttpServletRequest
     * @return Collection
     */
    public Collection getHeaders(HttpServletRequest request)
    {                
        Enumeration headerNames = request.getHeaderNames();        
        ArrayList headers = new ArrayList();
        
        while (headerNames.hasMoreElements())
        {
            String headerName = (String)headerNames.nextElement();
            
            // Ignore the content length and content type headers as 
            // the POST data we are adding will change these.
            if (   headerName.equalsIgnoreCase("Content-Length")
                || headerName.equalsIgnoreCase("Content-Type" ) )
            {
                continue;
            }
            
            // Each header may have more than one value
            Enumeration headerValues = request.getHeaders(headerName);
            
            StringBuffer headerValue= new StringBuffer();
            boolean firstValue=true;
            while (headerValues.hasMoreElements())
            {
                String aValue = (String)headerValues.nextElement();
                
                if (firstValue)
                {
                    headerValue.append(aValue);
                    firstValue = false;                  
                }
                else
                {
                    headerValue.append(", ");
                    headerValue.append(aValue);
                }
            }
            
            headers.add(headerName + ": " + headerValue.toString());                        
        }
        
        return headers;
    }

    /**
     * Dispatch ATG Dynamo's response back to the user.
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @param responseReader A BufferedReader that allow us to read the body of ATG's response.
     * @param responseHeader A HttpResponseHeader object containing the HTTP header of ATG's response.
     */ 
    public void dispatchUserResponse(HttpServletRequest request, HttpServletResponse response,
        BufferedReader responseReader, HttpResponseHeader responseHeader)
        throws IOException
    {
        int responseCode = responseHeader.getErrorCode();
        
        boolean redirect = false;
        if (responseCode == 302)
        {
            redirect = true;
        }
               
        // Recreate request headers received from Dynamo for the response
        // we send to the browser.
        response.setStatus(responseCode);
        if (logger.isDebugEnabled()) {
            logger.debug("responseCode="+responseCode);
        }
        Map headers = responseHeader.getHeaders();
        Set keys = headers.keySet();
        Iterator iter = keys.iterator();
        while( iter.hasNext() )
        {
            String key = (String)iter.next();
            NameValuePair pair = (NameValuePair)headers.get(key);
            String header = pair.getValue();
        
            if ("Location".equalsIgnoreCase(key))
            {
                header = getAbsoluteLocation(header, request);
            }
        
            /*if ("Content-Type".equalsIgnoreCase(key))
            {
                header = "text/vnd.wap.wml";    
            }*/
            if ("content-length".equalsIgnoreCase(key) && redirect)
            {
                header = "0";
            }
            if (logger.isDebugEnabled()) {
                logger.debug("adding header "+key+"="+header);    
            }
            response.addHeader(key, header);
        }
        
        PrintWriter printWriter = response.getWriter();
        
        if (redirect)
        {
            printWriter.println();
        }
        else
        {
            String line = null;
            while ( (line = responseReader.readLine()) != null)
            {
                if (logger.isDebugEnabled()) {
                    logger.debug("writing line="+line);
                }
                printWriter.println(line);
            }
        }
    }
    
    /**
     * Returns an absolute url to redirect to.
     * Can't just do a redirect as some app servers e.g. Weblogic
     * append on the context path as per the servlet 2.2 specs.
     * However ATG Dynamo App server does not.
     * @param location a relative or absolute url
     * @param request The HttpServletRequest
     * @return String an absolute url
     */
    private String getAbsoluteLocation(String location, HttpServletRequest request)
    {
        if (location.startsWith("http://"))
        {
            return location;
        }
        else
        {
            StringBuffer sb = new StringBuffer("http://");
            sb.append(request.getHeader("HOST"));
            
            // Assumes location is /someContext/someUrl.            
            if (!location.startsWith("/"))
            {
                sb.append("/");                
            }
            
            sb.append(location);
            
            String redirect=java.net.URLDecoder.decode(sb.toString());
                        
            return redirect;
        }
    }

    /**
     * URL encodes a string
     * @param s String to be url encoded
     * @return String a URL encoded version of the string passed in
     */    
    public String encodedValue( String s )
    {
        if( s != null )
        {
            return( java.net.URLEncoder.encode(s) );
        } else {
            return new String( "" );
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jan-05	6565/1	adrianj	VBM:2004122902 Created Dynamo 7 version of Volantis custom tags

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 27-Apr-04	3843/2	ianw	VBM:2004041408 Port forward ATG 5.6.1 integration

 ===========================================================================
*/
