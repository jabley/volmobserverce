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
package com.volantis.mcs.clientapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class TableResponseBuilder extends SampleAppResponseBuilder {

    /**
     * Name of attribute describing total rows count.
     */
    private String TOTAL_ROWS_COUNT_ATTRIBUTE = "total-rows-count";    
  
    private String MCS_START_PARAM = "mcs-start";
    private String MCS_COUNT_PARAM = "mcs-count";
    private String MCS_QUERY_PARAM = "mcs-query";
    
    private CountryFinder countryFinder;
    
    
    public TableResponseBuilder(CountryFinder countryFinder){
        this.countryFinder = countryFinder;
    }    
    
    /**
     * Method writes additional namespaces for AJAX table.
     */
    protected void openResponse(PrintWriter out) throws IOException {
        out.println("<response:response " +
                "xmlns=\"http://www.w3.org/2002/06/xhtml2\" " +
                "xmlns:mcs=\"http://www.volantis.com/xmlns/2006/01/xdime/mcs\" " +
                "xmlns:widget=\"http://www.volantis.com/xmlns/2006/05/widget\" " +
                "xmlns:response=\"http://www.volantis.com/xmlns/2006/05/widget/response\" >");
    }     
    
    protected void writeBodyContents(Map params, PrintWriter out) throws Exception {
        // Retrieve table parameters
        String[] starts = (String[]) params.get(MCS_START_PARAM);
        int start = (starts == null) ? 0 : Integer.parseInt(starts[0]) - 1;
        
        String[] counts = (String[]) params.get(MCS_COUNT_PARAM);
        int count = (counts == null) ? 10 : Integer.parseInt(counts[0]);

        String[] queries = (String[]) params.get(MCS_QUERY_PARAM);
        String query = (queries == null) ? "" : queries[0];

        if (query.length() <= 10) {
            CountriesFeedResponse response = countryFinder.find(query, start, count);
            
            Country[] countries = response.getCountries();
            
            // In case response contains no results, prepare stub response with
            // one row indicating that no countries were found matching
            // specified queery.
            if (countries.length == 0) {
                countries = new Country[] { new Country("--",
                        "No countries matching '" + query + "'") };
                response = new CountriesFeedResponse(countries, 1);
            }
            
            // Print table opening
            out.print("<response:tbody ");
            out.print(TOTAL_ROWS_COUNT_ATTRIBUTE);
            out.print("=\"");
            out.print(response.getTotalCountriesNumber());
            out.print("\">");
            out.print(getTableRows(countries));
            // Print table closure.
            out.print("</response:tbody>");
        } else {
            out.print("<response:error>" + 
                "Filter may contain at most 10 characters.</response:error>");
        }
    }
    
    private String getTableRows(Country[] countries) {
        StringBuffer rows = new StringBuffer();

        final String[] bgs = {"#000", "#111", "#222", "#333"};

        for (int i = 0; i < countries.length; i++) {
            String countryName = countries[i].getName();
            String countryCode = countries[i].getCode();
            rows.append("<tr style='background-color:")
                .append(bgs[i % bgs.length])
                .append("'><td style='border: 1px solid #442'>")
                .append(countryCode)
                .append("</td><td style='border: 1px solid #442'>")
                .append(countryName).append("</td></tr>");
        }
        return rows.toString();
    }  
}
