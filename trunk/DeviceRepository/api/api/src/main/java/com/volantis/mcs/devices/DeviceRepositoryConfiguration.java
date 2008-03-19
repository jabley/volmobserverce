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
package com.volantis.mcs.devices;

/**
 * Encapsulates the information necessary to create and configure a new
 * {@link com.volantis.mcs.devices.DeviceRepository} instance.
 * <p/>
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong></p>
 *
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */

public interface DeviceRepositoryConfiguration {
    /**
     * Set the value of the <a href="#maxAge">maxAge</a>
     * property.
     * @param maxAge The new value of the
     * <a href="#maxAge">maxAge</a> property.
     */
    public void setMaxAge(int maxAge);

    /**
     * Get the value of the <a href="#maxAge">maxAge</a>
     * property.
     * @return The value of the <a href="#maxAge">maxAge</a>
     * property.
     */
    public int getMaxAge();

    /**
     * Set the value of the <a href="#maxEntries">maxEntries</a>
     * property.
     * @param maxEntries The new value of the
     * <a href="#repositoryUrl">maxEntries</a> property.
     */
    public void setMaxEntries(int maxEntries);

    /**
     * Get the value of the <a href="#maxEntries">maxEntries</a>
     * property.
     * @return The value of the <a href="#maxEntries">maxEntries</a>
     * property.
     */
    public int getMaxEntries();

    public boolean getAllowExperimentalPolicies();

    public void setAllowExperimentalPolicies(boolean allowExperimentalPolicies);
}
