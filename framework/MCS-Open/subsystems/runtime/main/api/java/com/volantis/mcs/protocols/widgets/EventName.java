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

package com.volantis.mcs.protocols.widgets;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerates property names.
 */
public final class EventName extends MemberName {
    /**
     * Enumeration map.
     */
    private static final Map EVENT_NAMES = new HashMap();
    
    /**
     * Creates a new instance of PropertyName for given name and adds it into the
     * enumeration map.
     * 
     * @param name The name of the property.
     * @return Creates instance of PropertyName
     */
    private static EventName add(String name) {
        if (EVENT_NAMES.containsKey(name)) {
            throw new IllegalStateException("EventName already exists.");
        }

        EventName eventName = new EventName(name);
        
        EVENT_NAMES.put(name, eventName);
        
        return eventName;
    }

    // Create special meta event, with empty name. 
    public static final EventName _META_EVENT = add("");

    // EventName definitions follows here.
    public static final EventName PRESSED = add("pressed");
    public static final EventName APPEARED = add("appeared");
    public static final EventName DISAPPEARED = add("disappeared");
    public static final EventName ACTIVATED = add("activated");
    public static final EventName VALUE_CHANGED = add("value-changed");
    public static final EventName HIDING = add("hiding");
    public static final EventName HIDDEN = add("hidden");
    public static final EventName SHOWING = add("showing");
    public static final EventName SHOWN = add("shown");
    public static final EventName CONTENT_HIDING = add("content-hiding");
    public static final EventName CONTENT_HIDDEN = add("content-hidden");
    public static final EventName CONTENT_SHOWING = add("content-showing");
    public static final EventName CONTENT_SHOWN = add("content-shown");
    public static final EventName ADDING = add("adding");
    public static final EventName ADDED = add("added");
    public static final EventName REMOVING = add("removing");
    public static final EventName REMOVED = add("removed");
    public static final EventName FINISHED = add("finished");
    public static final EventName CHANGED = add("changed");
    public static final EventName SELECTED = add("selected");
    public static final EventName DESELECTED = add("deselected");
    public static final EventName DISMISSED = add("dismissed");

    
    /**
     * The name of this action.
     */
    private final String name;
    
    /**
     * Creates new instance of EventName.
     * 
     * @param name The name of the event.
     */
    private EventName(String name) {
        this.name = name;
    };
    
    /**
     * Returns the name of this event.
     * 
     * @return the name of this event.
     */
    public String getName() {
        return name; 
    }

    /**
     * Returns an instance of this class for given name.
     * 
     * @return an instance of this class for given name.
     * @throws IllegalArgumentException if such property does not exist.
     */
    public final static EventName forName(String name) {
        EventName eventName = (EventName) EVENT_NAMES.get(name);
        
        if (eventName == null) {
            throw new IllegalArgumentException("Unknown event name.");
        }
        
        return eventName;
    }

    public MemberType getMemberType() {
        return MemberType.EVENT;
    }
}
