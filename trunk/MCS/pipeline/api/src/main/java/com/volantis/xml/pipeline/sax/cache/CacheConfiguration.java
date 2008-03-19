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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.cache;

/**
 * This class holds the configuration information for a pipeline cache.
 */
public class CacheConfiguration {

    /**
     * The volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * The name of the cache.
     */
    private String name;

    /**
     * The strategy of the cache.
     */
    private String strategy;

    /**
     * The maximum number of entries in the cache.
     */
    private String maxEntries;

    /**
     * Set the value of the name attribute.
     * @param name The value to set as the name attribute.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of the name attribute.
     * @return the value of the name attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of the strategy attribute.
     * @param strategy The value to set as the strategy attribute.
     */
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    /**
     * Get the value of the strategy attribute.
     * @return the value of the strategy attribute.
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * Set the value of the maxEntries attribute.
     * @param maxEntries The value to set as the maxEntries attribute.
     */
    public void setMaxEntries(String maxEntries) {
        this.maxEntries = maxEntries;
    }

    /**
     * Get the value of the maxEntries attribute.
     * @return the value of the maxEntries attribute.
     */
    public String getMaxEntries() {
        return maxEntries;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 09-Jun-03	49/1	adrian	VBM:2003060505 updated headers and cleaned up imports following changes required for addition of cacheBody elements

 ===========================================================================
*/
