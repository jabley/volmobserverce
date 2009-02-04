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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.widgets.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.widgets.services.googlemaps.BackgroundOperation;
import com.volantis.mcs.widgets.services.googlemaps.PanOperation;
import com.volantis.mcs.widgets.services.googlemaps.SearchOperation;
import com.volantis.mcs.widgets.services.googlemaps.SwitchModeOperation;
import com.volantis.mcs.widgets.services.googlemaps.ZoomOperation;
import com.volantis.synergetics.log.LogDispatcher;

/** 
 * Maps service
 * 
 * This servlet implements the server side of Maps widgets
 */
public class MapService extends HttpServlet {
        
    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MapService.class);

    private HashMap builders = new HashMap();
    
    public MapService() {         
        builders.put("pan", new MapResponseBuilder(new PanOperation()));
        builders.put("zoom", new MapResponseBuilder(new ZoomOperation()));
        builders.put("search", new MapResponseBuilder(new SearchOperation()));
        builders.put("switchMode", new MapResponseBuilder(new SwitchModeOperation()));        
        builders.put("bg", new MapResponseBuilder(new BackgroundOperation()));        
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        String operation = null;
        String pathInfo = request.getPathInfo();
        if (null != pathInfo) {
            StringTokenizer tokenizer = new StringTokenizer(pathInfo, "/?");
            if (tokenizer.hasMoreTokens()) {
                operation = tokenizer.nextToken();
            }
        }
        
        if (null == operation) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }  
        
        ResponseBuilder builder = (ResponseBuilder)builders.get(operation);
        if (null == builder) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Invalid operation '" + operation + "'");
            return;            
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Handling response for " + operation);
        }
        
        try {
            builder.buildResponse(request, response);
        } catch (Exception x) {
            throw new ServletException(x);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        // Handle POST in the same way as GET
        doGet(request, response);
    }
    
    /**
     * Common response builder for all operations
     */
    protected class MapResponseBuilder extends ResponseBuilder {
        
        protected MapOperation operation;
        
        public MapResponseBuilder(MapOperation operation) {
            this.operation = operation; 
        }

        protected void writeHeadContents(Map params, PrintWriter out) throws Exception {
            // do nothing, as head is supposed to be empty
        }

        protected void writeBodyContents(Map params, PrintWriter out) throws Exception {            
            out.print("<response:map>");
            out.print(operation.perform(params));
            out.println("</response:map>");            
        }       
    }
        
}
