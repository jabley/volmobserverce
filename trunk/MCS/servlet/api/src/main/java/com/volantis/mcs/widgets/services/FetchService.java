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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.widgets.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;   

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/** 
 * Fetch service
 * 
 * This servlet implements the server side of fetch widget
 */
public class FetchService extends HttpServlet {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(FetchService.class);
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
                
        Map params = getParameters(request);
        
        String src = WidgetServiceHelper.getParameter(params, "src");
        String transformation = WidgetServiceHelper.getParameter(params, "trans");
        String transCache = WidgetServiceHelper.getParameter(params, "trans-cache");
        String transCompile = WidgetServiceHelper.getParameter(params, "trans-compile");
        
        if(src == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid query. src param is required!");
            return;                        
        }
        
        if(transCache == null) {
            transCache = "true";    
        }
        if(transCompile == null) {
            transCompile = "true";    
        }        

        // create response 
        response.setContentType("x-application/vnd.xdime+xml");
        response.setHeader("Cache-Control", "no-cache, max-age=0");
        PrintWriter out = response.getWriter();        
        
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        
        if(transformation != null) {
            out.print("<pipeline:transform xmlns:pipeline=\"http://www.volantis.com/xmlns/marlin-pipeline\" xmlns:urid=\"http://www.volantis.com/xmlns/marlin-uri-driver\" compile=\"" + transCompile + "\" cache=\"" + transCache + "\">");
            out.print("<pipeline:transformation compilable=\"" + transCompile + "\" href=\"" + StringEscapeUtils.escapeXml(transformation) + "\"/>"); 
            out.print("<urid:fetch href=\"" + StringEscapeUtils.escapeXml(src) + "\" parse=\"xml\"/>");
            out.print("</pipeline:transform>");                
        } else {
            out.print("<urid:fetch xmlns:urid=\"http://www.volantis.com/xmlns/marlin-uri-driver\" href=\"" + StringEscapeUtils.escapeXml(src) + "\"/>");                                
        }            
        out.close();               
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        // Handle POST in the same way as GET
        doGet(request, response);
    }
    
    private Map getParameters(HttpServletRequest request) {
        return Collections.unmodifiableMap(request.getParameterMap());
    }     
                
}
