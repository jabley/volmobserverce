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
package com.volantis.synergetics.reporting;

import com.volantis.synergetics.factory.MetaDefaultFactory;
import com.volantis.synergetics.reporting.config.ReportExclusion;

import java.util.Map;

public abstract class ExclusionManager {

    /**
     * The meta factory for creating default factories
     */
    private static MetaDefaultFactory factoryInstance =
        new MetaDefaultFactory("com.volantis.synergetics.reporting.impl." +
            "exclusions.DefaultExclusionManager",
                               ExclusionManager.class.getClassLoader());

    /**
     * Resturn an instance of the default Exclusion manager
     *
     * @return an instance of the default ExclusionManager
     */
    public static ExclusionManager getDefaultInstance() {
        return (ExclusionManager)
            factoryInstance.getDefaultFactoryInstance();
    }

    /**
     * Is event excluded from being reported
     *
     * @param binding String report binding name
     * @return metrics Map report event metrics
     */
    public abstract boolean isExcluded(String binding, Map metrics);

    /**
     * Initialization of report exclusions
     *
     * @param binding         String report binding name
     * @param reportExclusion ReportExclusion (congifuration object)
     */
    public abstract void initializeExclusions(String binding,
                                            ReportExclusion reportExclusion);
}

