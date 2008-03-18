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
package com.volantis.mcs.devices.xml;

import com.volantis.mcs.devices.DeviceRepositoryConfiguration;

import java.net.URL;

/**
 * Encapsulates the information necessary to create and configure a new
 * {@link com.volantis.mcs.devices.DeviceRepository} instance.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong></p>
 *
 * <p>This has the following properties.</p>
 * <dl>
 *     <dt id="repositoryUrl"><b>repositoryUrl</b></dt>
 *     <dd>The {@link java.net.URL} The URL of the repository.
 * </dd>
 *
 * </dl>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface XMLDeviceRepositoryConfiguration
        extends DeviceRepositoryConfiguration{

    /**
     * Get the value of the <a href="#repositoryUrl">repositoryUrl</a>
     * property.
     * @return The value of the <a href="#repositoryUrl">repositoryUrl</a>
     * property.
     */
    public URL getRepositoryUrl();

    /**
     * Set the value of the <a href="#repositoryURL">repositoryUrl</a>
     * property.
     * @param repositoryUrl The new value of the
     * <a href="#repositoryUrl">repositoryUrl</a> property.
     */
    public void setRepositoryURL(URL repositoryUrl);

    /**
     * Returns the value of the schema validation property.
     *
     * <p>If schema validation is enabled, XML's read from the repository are
     * validated.</p>
     */
    public boolean getSchemaValidation();

    /**
     * Sets the value of the schema validation property.
     */
    public void setSchemaValidation(boolean schemaValidation);
}
