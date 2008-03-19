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
package com.volantis.xml.pipeline.sax.config;

import java.util.Map;
import java.util.HashMap;

/**
 * Simple implementation of the {@link XMLPipelineConfiguration} interface
 */
public class SimpleXMLPipelineConfiguration
        implements XMLPipelineConfiguration {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * A Map for storing the registered configurations
     */
    private Map configurations;

    /**
     * Creates a new SimpleXMLPipelineConfiguration instance
     */
    public SimpleXMLPipelineConfiguration() {
        configurations = new HashMap();
    }

    // javadoc inherited
    public void storeConfiguration(Object key,
                                   Configuration configuration) {
        if (key == null) {
            throw new NullPointerException("Key object cannot be null");
        }
        configurations.put(key, configuration);
    }

    // javadoc inherited
    public Configuration retrieveConfiguration(Object key) {
        return (Configuration)configurations.get(key);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 24-Jul-03	247/1	doug	VBM:2003072401 Simple XMLPipelineConfiguration implementation

 ===========================================================================
*/
