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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.osgi.cm;

import org.osgi.framework.Bundle;
import org.osgi.service.cm.ConfigurationPermission;

/**
 * Provides helper methods for checking security.
 */
public class SecurityHelper {

    /**
     * The permission to check.
     */
    public static final ConfigurationPermission CONFIGURATION_PERMISSION =
            new ConfigurationPermission("*", ConfigurationPermission.CONFIGURE);

    /**
     * Check to see if the specified bundle has permission to configure other
     * bundles.
     *
     * @param bundle The bundle to check.
     * @return True if it has, false otherwise.
     */
    public static boolean hasConfigurePermission(Bundle bundle) {

        return bundle.hasPermission(CONFIGURATION_PERMISSION);
    }

    /**
     * Ensure that the bundle has permission to configure other bundles.
     *
     * @param bundle The bundle to check.
     * @throws SecurityException If the bundle does not have permission.
     */
    public static void ensureHasConfigurePermission(Bundle bundle) {
        if (!hasConfigurePermission(bundle)) {
            throw new SecurityException("Bundle '" + bundle.getLocation() +
                    "' does not have permission to manage " +
                    "configuration for other bundles");
        }
    }
}
