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

package com.volantis.mcs.clientapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.volantis.mcs.widgets.services.WidgetServiceHelper;

class ProgressResponseBuilder extends SampleAppResponseBuilder {
    
    /** 
     * Source of data to be used by this builder 
     */
    private ProgressDataFeed feed;

    public ProgressResponseBuilder(ProgressDataFeed feed) {
        this.feed = feed;
    }
    
    protected Map getParameters(HttpServletRequest request) {
        Map originalParams = super.getParameters(request);   
        if (originalParams.containsKey("operationId")) {
            return originalParams;
        }
        
        HttpSession session = request.getSession();
        String operationId = session.getId();
        HashMap params = new HashMap();
        params.putAll(originalParams);
        params.put("operationId", new String[] { operationId });
        return params;
    }
    
    protected void writeBodyContents(Map params, PrintWriter out) throws IOException {
        
        String operationId = WidgetServiceHelper.getParameter(params, "operationId");        
        out.print("<response:progress>");
        if (null == operationId) {
            out.print(0);            
        } else {
            out.print(feed.getProgress(operationId));
        }
        out.println("</response:progress>");
    }        
}
