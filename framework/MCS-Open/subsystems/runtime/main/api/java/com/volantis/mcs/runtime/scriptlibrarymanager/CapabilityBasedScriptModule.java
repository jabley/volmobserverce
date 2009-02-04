/*
This file is part of Volantis Mobility Server.

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server. If not, see <http://www.gnu.org/licenses/>.
*/
/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.scriptlibrarymanager;

import com.volantis.mcs.protocols.VolantisProtocol;

import java.util.Set;

/**
 * Script module included base on device capabilities 
 */
public class CapabilityBasedScriptModule extends ConditionalScriptModule {

    public CapabilityBasedScriptModule(String assetName, Set dependencies,
                                       int size, boolean cacheable) {
        super(assetName, dependencies, size, cacheable);
    }

    /**
     * Right now it will returns always true as we do not have such capabalities policies in device repostory and temporarily
     * decision if use this module or not will be done in DAL, but this module is always required until we have new policy in device repository
     * @return true
     */
    public boolean isNeeded(Set<String> effectStyles, VolantisProtocol protocol) {
        return true;
    }
}
