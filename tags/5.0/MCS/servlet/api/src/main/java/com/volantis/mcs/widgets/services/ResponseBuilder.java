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
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/**
 * Base class for builders of responses for widgets' queries 
 * 
 * The implementation must be thread safe, as it will be shared between 
 * simultaneously processed requests.
 */
public abstract class ResponseBuilder {
    
    private final static String XDIME_MIME_TYPE = "x-application/vnd.xdime+xml";

    protected void writeProlog(PrintWriter out) throws IOException {
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");            
    }

    protected void openResponse(PrintWriter out) throws IOException {
        out.println("<response:response " +
                "xmlns=\"http://www.w3.org/2002/06/xhtml2\" " +
                "xmlns:mcs=\"http://www.volantis.com/xmlns/2006/01/xdime/mcs\" " +
                "xmlns:response=\"http://www.volantis.com/xmlns/2006/05/widget/response\">");
    }

    protected void closeResponse(PrintWriter out) throws IOException {
        out.println("</response:response>");        
    }

    protected void openHead(PrintWriter out) throws IOException {
        out.println("<response:head>");
    }

    abstract protected void writeHeadContents(Map params, PrintWriter out) throws Exception;

    protected void closeHead(PrintWriter out) throws IOException {
        out.println("</response:head>");
    }

    protected void openBody(PrintWriter out) throws IOException {
        out.println("<response:body>");
    }

    abstract protected void writeBodyContents(Map params, PrintWriter out) throws Exception;

    protected void closeBody(PrintWriter out) throws IOException {
        out.println("</response:body>");
    }

    protected void openElementWithId(PrintWriter out, String element, Map params) 
            throws IOException {

        // Write the element in the response namespace
        out.print("<response:" + element);

        // If id is present, add it to the element
        // Note: the parameters map contains arrays of strings as values
        String[] ids = (String[])params.get("id");
        if (null != ids && ids.length > 0) {
            out.print(" id=\"" + ids[0] + "\"");
        }
        out.println(">");

    }
    protected void closeElement(PrintWriter out, String element) throws IOException {
        out.print("</response:" + element + ">");
    }
    
    protected Map getParameters(HttpServletRequest request) {
        return Collections.unmodifiableMap(request.getParameterMap());
    }

    
    public void buildResponse(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        response.setContentType(XDIME_MIME_TYPE);
        response.setHeader("Cache-Control", "no-cache, max-age=0");
        PrintWriter out = response.getWriter();
        writeProlog(out);
        openResponse(out);
        openHead(out);
        writeHeadContents(getParameters(request), out);
        closeHead(out);
        openBody(out);
        writeBodyContents(getParameters(request), out);
        closeBody(out);
        closeResponse(out);
        out.close();
    }    
}
