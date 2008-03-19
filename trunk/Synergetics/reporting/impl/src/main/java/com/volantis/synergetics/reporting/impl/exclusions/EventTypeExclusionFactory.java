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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl.exclusions;

import com.volantis.synergetics.reporting.config.EventTypeExclusion;

/**
 * Creates proper EventExcluder class based on configuration
 *
 */
public class EventTypeExclusionFactory {

    private static final String EVENT_TYPE_START = "start";
    private static final String EVENT_TYPE_STOP = "stop";
    private static final String EVENT_TYPE_UPDATE = "update";

    /**
     * No Constructor
     *
     */
    private EventTypeExclusionFactory() {

    }
    /**
     * Factory method creates proper EventExcluder based on configuration
     * @param configuration ExclusionConfiguration
     * @return EventExcluder
     */
    public static EventExcluder createExcluder(EventTypeExclusion configuration) {
        EventExcluder ret = null;

        if(configuration.getExclusionType().toLowerCase().equals(EVENT_TYPE_START)) {
            ret = new StartEventExcluder();
        } else if(configuration.getExclusionType().toLowerCase().equals(EVENT_TYPE_STOP)) {
            ret = new StopEventExcluder();
        } else if(configuration.getExclusionType().toLowerCase().equals(EVENT_TYPE_UPDATE)) {
            ret = new UpdateEventExcluder();
        }

        return ret;
    }
}
