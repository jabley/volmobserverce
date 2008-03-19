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
package com.volantis.devrep.repository.impl.devices;

import com.volantis.mcs.devices.DeviceRepositoryConfiguration;


/**
 * A default implementation of {@link DeviceRepositoryConfiguration}.
 */

public class DefaultDeviceRepositoryConfiguration
        implements DeviceRepositoryConfiguration {


    /**
     * @see #getMaxAge
     */
    private int maxAge;

    /**
     * @see #getMaxEntries
     */
    private int maxEntries;

    private boolean allowExperimentalPolicies;

    //Javadoc inherited.
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    //Javadoc inherited.
    public int getMaxAge() {
        return maxAge;
    }

    //Javadoc inherited.
    public void setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    //Javadoc inherited.
    public int getMaxEntries() {
        return maxEntries;
    }

    public boolean getAllowExperimentalPolicies() {
        return allowExperimentalPolicies;
    }

    public void setAllowExperimentalPolicies(boolean allowExperimentalPolicies) {
        this.allowExperimentalPolicies = allowExperimentalPolicies;
    }
}
