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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.impl.devices.xml;

import com.volantis.devrep.repository.impl.devices.DefaultDeviceRepositoryConfiguration;
import com.volantis.mcs.devices.xml.XMLDeviceRepositoryConfiguration;

import java.net.URL;

/**
 * A default implementation of {@link XMLDeviceRepositoryConfiguration}.
 */
public class DefaultXMLDeviceRepositoryConfiguration
         extends DefaultDeviceRepositoryConfiguration
        implements XMLDeviceRepositoryConfiguration {

    /**
     * @see #getRepositoryUrl
     */
    private URL repositoryUrl;

    /**
     * @see #getSchemaValidation()
     */
    private boolean schemaValidation;

    public DefaultXMLDeviceRepositoryConfiguration() {
        schemaValidation = true;
    }

    //Javadoc inherited.
    public URL getRepositoryUrl() {
        return repositoryUrl;
    }

    //Javadoc inherited.
    public void setRepositoryURL(URL repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    //Javadoc inherited.
    public boolean getSchemaValidation() {
        return schemaValidation;
    }

    //Javadoc inherited.
    public void setSchemaValidation(boolean schemaValidation) {
        this.schemaValidation = schemaValidation;
    }
}
