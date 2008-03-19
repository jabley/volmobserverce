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
import java.util.Map;

import com.volantis.mcs.widgets.services.WidgetServiceHelper;

/**
 * Builder of AJAX response for Folding Item widget
 *
 * The implementation is stateless and thread safe.
 */
class FoldingItemBuilder extends SampleAppResponseBuilder {

    /** 
     * Source of data to be used by this builder 
     */
    private ItemsDataFeed feed; 

    public FoldingItemBuilder(ItemsDataFeed feed) {
        this.feed = feed;
    }
            
    protected void writeBodyContents(Map params, PrintWriter out) throws IOException {
        
        // Start the folding-item response
        openElementWithId(out, "folding-item", params);
        
        String itemId = WidgetServiceHelper.getParameter(params, "itemId");
        
        // Write the data
        ItemsDataFeed.DataItem item = feed.getDataItem(itemId);
        out.print("<div>");
        out.print(ClientServiceHelper.getLocalizedString("folding"));
        out.print(" <a href=\"");
        out.print(item.linkUrl);
        out.print("\">");
        out.print(item.linkText);
        out.print("</a>");
        out.print(item.text);
        out.println("</div>");                
               
        // Finish the folding-item response
        closeElement(out, "folding-item");
    }        
}
