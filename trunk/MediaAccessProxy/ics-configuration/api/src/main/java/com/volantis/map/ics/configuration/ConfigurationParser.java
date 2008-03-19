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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.configuration;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * A parser for the VTS configuration
 */
public interface ConfigurationParser {

    /**
     * Marshal the configuration object into the specified output stream
     * @param config the configuration object
     * @param stream the stream to marshal the configuration into
     */
    public  void marshal(Configuration config,
                               OutputStream stream);

    /**
     * Unmarshal a configuration from the specified input stream
     * @param stream the stream containing the configuration
     * @return the Configruation object
     */
    public Configuration unmarshal(InputStream stream);

    /**
     * Create a default configuration
     *
     * @return a default configuration
     */
    public Configuration createDefaultConfiguration();
}
