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
package com.volantis.map.sti.configuration;

import java.net.URL;

/**
 * The STI Plugin configuration.
 * 
 * @mock.generate
 */
public interface Configuration {
    /**
     * Returns the URL to the STI Service.
     * 
     * @return The URL.
     */
    URL getServiceURL();
    
    /**
     * Returns the Originator ID, which will be passed with each STI request.
     * 
     * @return The originator ID
     */
    String getOriginatorID();
}
