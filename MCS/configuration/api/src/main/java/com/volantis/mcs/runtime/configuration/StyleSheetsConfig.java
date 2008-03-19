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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration;

/**
 * Holds configuration information about stylesheet generation.
 */
public class StyleSheetsConfig {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The external generation configuratuon options - if they have been
     * specified.
     */
    private StyleSheetExternalGenerationConfiguration
            externalCacheConfiguration;

    /**
     * The page level generation configuration options - if they have been
     * specified,
     */
    private StyleSheetPageLevelGenerationConfiguration
            pageLevelGenerationConfiguration;

    /**
     * Obtain the external configuration.
     *
     * @return An object containing the external generation options specified.
     *         May be null.
     */
    public StyleSheetExternalGenerationConfiguration
            getExternalCacheConfiguration() {
        return externalCacheConfiguration;
    }

    /**
     * Set the external configuration options.
     *
     * @param externalCacheConfiguration The external options.
     */
    public void setExternalCacheConfiguration(
            StyleSheetExternalGenerationConfiguration
                    externalCacheConfiguration) {
        this.externalCacheConfiguration = externalCacheConfiguration;
    }

    /**
     * Set the page level configuration options.
     *
     * @param pageLevelCacheConfiguration The page level options.
     */
    public void setPageLevelCacheConfiguration(
            StyleSheetPageLevelGenerationConfiguration
                    pageLevelCacheConfiguration) {
        this.pageLevelGenerationConfiguration = pageLevelCacheConfiguration;
    }

    /**
     * Obtain the page level configuration.
     *
     * @return An object containing the page level generation options specified
     * or null if they have not been set.
     */
    public StyleSheetPageLevelGenerationConfiguration
            getPageLevelCacheConfiguration() {

        return pageLevelGenerationConfiguration;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 28-Jun-04	4726/3	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 ===========================================================================
*/
