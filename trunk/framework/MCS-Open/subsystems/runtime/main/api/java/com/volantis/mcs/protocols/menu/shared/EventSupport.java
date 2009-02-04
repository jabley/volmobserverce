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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.menu.model.EventTarget;
import com.volantis.mcs.protocols.menu.model.EventType;

/**
 * Provides methods to handle Menu Model to Protocol event transformations.
 *
 * @todo later it might be nice to make this more general, and non-static, to handle more "core" attributes (e.g. style)
 */
public class EventSupport {
    /**
     * This array is indexed by the protocol event type indices as defined in
     * {@link EventConstants} and maps relevant ones to their equivalent Menu
     * Model {@link EventType}. Not all protocol event types have equivalence
     * in the Menu Model.
     */
    private static final EventType[] eventMapping;

    static {
        // Initialize the required entries in the event mapping table. This
        // only covers the protocol event types associated with menu model
        // event types, meaning that a number of the array's entries will be
        // null. These should be ignored.
        eventMapping = new EventType[EventConstants.MAX_EVENTS];

        eventMapping[EventConstants.ON_CLICK] = EventType.ON_CLICK;
        eventMapping[EventConstants.ON_DOUBLE_CLICK] =
                EventType.ON_DOUBLE_CLICK;
        eventMapping[EventConstants.ON_KEY_DOWN] = EventType.ON_KEY_DOWN;
        eventMapping[EventConstants.ON_KEY_PRESS] = EventType.ON_KEY_PRESS;
        eventMapping[EventConstants.ON_KEY_UP] = EventType.ON_KEY_UP;
        eventMapping[EventConstants.ON_MOUSE_DOWN] = EventType.ON_MOUSE_DOWN;
        eventMapping[EventConstants.ON_MOUSE_MOVE] = EventType.ON_MOUSE_MOVE;
        eventMapping[EventConstants.ON_MOUSE_OUT] = EventType.ON_MOUSE_OUT;
        eventMapping[EventConstants.ON_MOUSE_OVER] = EventType.ON_MOUSE_OVER;
        eventMapping[EventConstants.ON_MOUSE_UP] = EventType.ON_MOUSE_UP;
        eventMapping[EventConstants.ON_BLUR] = EventType.ON_BLUR;
        eventMapping[EventConstants.ON_FOCUS] = EventType.ON_FOCUS;
    };

    /**
     * The event handlers for the given target will be set into the event
     * attributes associated with the given volantis attributes.
     *
     * @param target     the menu model event target with assigned event
     *                   handlers
     * @param attributes the protocol attributes that need to have the event
     *                   attributes populated
     */
    public static void setEvents(EventTarget target,
                                 MCSAttributes attributes) {
        EventAttributes events = attributes.getEventAttributes(true);

        for (int event = 0; event < eventMapping.length; event++) {
            EventType type = eventMapping[event];

            // Not all protocol events map to an equivalent menu model event
            if (type != null) {
                events.setEvent(event, target.getEventHandler(type));
            }
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 14-May-04	4388/3	philws	VBM:2004050405 Handle events on menu items

 ===========================================================================
*/
