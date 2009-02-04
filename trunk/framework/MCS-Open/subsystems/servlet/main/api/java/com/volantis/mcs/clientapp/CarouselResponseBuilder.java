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

/**
 * Builder of AJAX response for Carousel widget
 *
 * The implementation is stateless and thread safe.
 */
class CarouselResponseBuilder extends SampleAppResponseBuilder {

    /** 
     * Source of data to be used by this builder 
     */
    private ItemsDataFeed feed; 

    public CarouselResponseBuilder(ItemsDataFeed feed) {
        this.feed = feed;
    }
        
    protected void writeBodyContents(Map params, PrintWriter out) throws IOException {
        // Start the carousel response
        openElementWithId(out, "carousel", params);

        // Get the data from the feed
        List items = feed.getDataItems();

        // Write the data wrapped in li
        Iterator i = items.iterator();
        while (i.hasNext()) {
            ItemsDataFeed.DataItem item = (ItemsDataFeed.DataItem)i.next();
            out.print("<li><a href=\"");
            out.print(item.linkUrl);
            out.print("\">");
            out.print(item.linkText);
            out.print("</a>");
            out.print(item.text);
            out.println("</li>");                
        }       
        
        // Finish the carousel response
        closeElement(out, "carousel");
    }        
}
