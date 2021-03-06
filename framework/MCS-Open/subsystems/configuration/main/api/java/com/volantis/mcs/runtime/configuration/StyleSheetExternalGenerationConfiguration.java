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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/StyleSheetExternalGenerationConfiguration.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; holds configuration 
 *                              information about stylesheet generation. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

/**
 * Holds configuration information about external stylesheet generation. 
 */ 
public class StyleSheetExternalGenerationConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";
    
    private Boolean cache;
    private String baseUrl;
    
    /**
     * The css session type. Supported values are:
     * <ul>
     *   <li>include-id-in-url</li>
     * </ul>
     *
     * If this value is set to 'include-id-in-url' then the url will be
     * rewritten to include the sesssion id, otherwise it won't.
     */
    private String cssSessionType = null;
    

    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCssSessionType() {
        return cssSessionType;
    }

    public void setCssSessionType(String cssSessionType) {
        this.cssSessionType = cssSessionType;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 28-Jun-04	4726/6	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 24-Jun-04	4726/3	claire	VBM:2004060803 Implementation of internal style sheet caching

 ===========================================================================
*/
