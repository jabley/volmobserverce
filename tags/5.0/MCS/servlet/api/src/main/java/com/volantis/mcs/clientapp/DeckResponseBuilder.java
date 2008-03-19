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

/**
 * Construct response for gallery widget ajax request.
 */
class DeckResponseBuilder extends SampleAppResponseBuilder {

    /**
     * Property name with start page number
     */
    private String MCS_START_PARAM = "mcs-start";
    
    /**
     * Property name with end page number
     */
    private String MCS_END_PARAM = "mcs-end";
    
    /**
     * Name of attribute describing total pages count.
     */
    private String TOTAL_PAGES_COUNT_ATTRIBUTE = "total-pages-count";
    
    /**
     * The Deck feed.
     */
    private final DeckFeed deckFeed;
    
    
    public DeckResponseBuilder(DeckFeed deckFeed){
        this.deckFeed = deckFeed;
    }

    /**
     * Method writes additional namespaces for deck.
     */
    protected void openResponse(PrintWriter out) throws IOException {
        out.println("<response:response " +
                "xmlns=\"http://www.w3.org/2002/06/xhtml2\" " +
                "xmlns:mcs=\"http://www.volantis.com/xmlns/2006/01/xdime/mcs\" " +
                "xmlns:widget=\"http://www.volantis.com/xmlns/2006/05/widget\" " +
                "xmlns:response=\"http://www.volantis.com/xmlns/2006/05/widget/response\" >");
    }    

    // javadoc inherited
    protected void writeBodyContents(Map params, PrintWriter out)
            throws IOException {
        
        // Retrieve deck parameters
        String[] starts = (String[]) params.get(MCS_START_PARAM);
        int start = (starts == null) ? 1 : Integer.parseInt(starts[0]);
        
        String[] ends = (String[]) params.get(MCS_END_PARAM);
        int end = (ends == null) ? deckFeed.getPagesCount() : Integer.parseInt(ends[0]);
        
        // Print deck opening
        out.print("<response:deck ");
        out.print(TOTAL_PAGES_COUNT_ATTRIBUTE);
        out.print("=\"");
        out.print(deckFeed.getPagesCount());
        out.print("\">");
        
        // Print all deck pages.
        for (int number = start; number <= end; number++) {
            out.print("<widget:deck-page>");
            out.print(deckFeed.getPageContent(number));
            out.print("</widget:deck-page>");
        }
        
        // Print deck closure.
        out.print("</response:deck>");
    }
}
