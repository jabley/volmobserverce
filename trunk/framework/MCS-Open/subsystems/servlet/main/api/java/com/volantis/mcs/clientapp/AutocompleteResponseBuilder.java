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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.volantis.mcs.widgets.services.WidgetServiceHelper;

/**
 * Autocompleter response builder.
 * 
 * Uses an Autocompleter to get a list of results to output. 
 */
public class AutocompleteResponseBuilder extends SampleAppResponseBuilder {

    private Autocompleter autocomp;

    public AutocompleteResponseBuilder(Autocompleter autocomp) {
        this.autocomp = autocomp;
    }

    protected void writeBodyContents(Map params, PrintWriter out) 
            throws IOException {

        String input = WidgetServiceHelper.getParameter(params, "mcs-value");
        if (null == input || input.length() == 0) {
            out.print("<response:autocomplete/>");
            return;
        } 
        
        // default limit is no limit
        int limit = -1;
        String limitStr = WidgetServiceHelper.getParameter(params, "mcs-item-limit");
        // mcs-item-limit is not sent when there should be no limit imposed
        if (null != limitStr) {
            try {
                limit = Integer.parseInt(limitStr);
                // if a negative limit is specified, no items should be displayed
                if (limit < 0)
                    limit = 0;
            } catch (NumberFormatException e) {
                // if mcs-item-limit is not an integer, revert to no limit 
                limit = -1;
            }
        }
        
        List results = autocomp.match(input, limit);
        
        out.print("<response:autocomplete>"); 
        
        for (Iterator it = results.iterator(); it.hasNext(); ) {
            out.print("<li>" + (String)it.next() + "</li>");
        }
        
        out.print("</response:autocomplete>");
        
    }
    
}
