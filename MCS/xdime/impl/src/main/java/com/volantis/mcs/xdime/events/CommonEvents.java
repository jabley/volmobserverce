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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.events;

import com.volantis.mcs.protocols.EventConstants;

/**
 * Common events.
 * <p>
 * These should to "most" elements according to the HTML 4 spec. ;-)
 */
public class CommonEvents implements EventsTable {

    // Javadoc inherited.
    public void registerEvents(EventRegistrar registrar) {

        // Initialise the DOM Level 2 mouse events.
        registrar.registerEvent("click", EventConstants.ON_CLICK);
        registrar.registerEvent("mousedown", EventConstants.ON_MOUSE_DOWN);
        registrar.registerEvent("mouseup", EventConstants.ON_MOUSE_UP);
        registrar.registerEvent("mouseover", EventConstants.ON_MOUSE_OVER);
        registrar.registerEvent("mousemove", EventConstants.ON_MOUSE_MOVE);
        registrar.registerEvent("mouseout", EventConstants.ON_MOUSE_OUT);
    }
}
