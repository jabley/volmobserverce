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

package com.volantis.osgi.cm.dispatcher;

import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationEvent;

/**
 * An extension of {@link ConfigurationEvent} that produces better diagnostic
 * information.
 */
class ExtendedConfigurationEvent
        extends ConfigurationEvent {

    /**
     * Array of names for event types.
     */
    private static final String[] TYPE_NAMES = new String[]{
            "<unknown 0>",
            "CM_UPDATED",
            "CM_DELETED",
    };

    /**
     * Initialise.
     */
    public ExtendedConfigurationEvent(
            ServiceReference reference, int type, String factoryPid,
            String pid) {
        super(reference, type, factoryPid, pid);
    }

    // Javadoc inherited.
    public String toString() {
        int type = getType();
        String typeAsString;
        if (type < 0 || type >= TYPE_NAMES.length) {
            typeAsString = "<unknown " + type + ">";
        } else {
            typeAsString = TYPE_NAMES[type];
        }
        return "{factoryPid=" + getFactoryPid() + ", pid=" + getPid() +
                ", type=" + typeAsString + "}";
    }
}
