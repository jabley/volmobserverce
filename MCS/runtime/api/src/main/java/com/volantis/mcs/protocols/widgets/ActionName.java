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
 * A name of the action.
 */
public final class ActionName extends MemberName {
    /**
     * Enumeration map.
     */
    private static final Map ACTION_NAMES = new HashMap();
    
    /**
     * Creates a new instance of ActionName for given name and adds it into the
     * enumeration map.
     * 
     * @param name The name of the action.
     * @return Creates instance of ActionName
     */
    private static ActionName add(String name) {
        if (ACTION_NAMES.containsKey(name)) {
            throw new IllegalStateException("Action name already used.");
        }

        ActionName actionName = new ActionName(name);
        
        ACTION_NAMES.put(name, actionName);
        
        return actionName;
    }
    
    // ActionName definitions follows here.
    public static final ActionName NEXT = add("next");
    public static final ActionName PREVIOUS = add("previous");
    public static final ActionName PLAY = add("play");
    public static final ActionName STOP = add("stop");
    public static final ActionName PAUSE = add("pause");
    public static final ActionName DISMISS = add("dismiss");
    public static final ActionName CANCEL = add("cancel");
    public static final ActionName COMPLETE = add("complete");
    public static final ActionName PAN_RIGHT = add("pan-right");
    public static final ActionName PAN_LEFT = add("pan-left");
    public static final ActionName PAN_UP = add("pan-up");
    public static final ActionName PAN_DOWN = add("pan-down");
    public static final ActionName ZOOM_IN = add("zoom-in");
    public static final ActionName ZOOM_OUT = add("zoom-out");
    public static final ActionName SET_MAP_STYLE_MAP = add("set-map-style-map");
    public static final ActionName SET_MAP_STYLE_PHOTO = add("set-map-style-photo");    
    public static final ActionName SEARCH = add("search");
    public static final ActionName NEXT_MONTH = add("next-month");
    public static final ActionName PREVIOUS_MONTH = add("previous-month");
    public static final ActionName NEXT_YEAR = add("next-year");
    public static final ActionName PREVIOUS_YEAR = add("previous-year");
    public static final ActionName SET_TODAY = add("set-today");
    public static final ActionName ACTIVATE = add("activate");
    public static final ActionName PRESS = add("press");
    public static final ActionName SHOW = add("show");
    public static final ActionName HIDE = add("hide");
    public static final ActionName SHOW_CONTENT = add("show-content");
    public static final ActionName HIDE_CONTENT = add("hide-content");
    public static final ActionName INVOKE = add("invoke");
    public static final ActionName RESET = add("reset");
    public static final ActionName ENABLE = add("enable");
    public static final ActionName DISABLE = add("disable");
    public static final ActionName CLEAR_CONTENT = add("clear-content");
    public static final ActionName LAUNCH = add("launch");
    public static final ActionName FORCE_SYNC = add("force-sync");
    public static final ActionName SELECT = add("select");
    public static final ActionName DESELECT = add("deselect");
    public static final ActionName START = add("start");
    public static final ActionName SPLIT = add("split");
    public static final ActionName NEXT_PAGE = add("next-page");
    public static final ActionName PREVIOUS_PAGE = add("previous-page");
    public static final ActionName FIRST_PAGE = add("first-page");
    public static final ActionName LAST_PAGE = add("last-page");
    public static final ActionName NEXT_ROW = add("next-row");
    public static final ActionName PREVIOUS_ROW = add("previous-row");
    public static final ActionName EXECUTE = add("execute");

    /**
     * The name of this action.
     */
    private final String name;
    
    /**
     * Creates new instance of ActionName.
     * 
     * @param name The name of the action.
     */
    private ActionName(String name) {        
        this.name = name;
    };
    
    /**
     * Returns the name of this action.
     * 
     * @return the name of this action.
     */
    public String getName() {
        return name; 
    }

    /**
     * Returns ActionName for the specified name.
     * 
     * @returns The action name.
     * @throws IllegalStateException If there's no action for given name.
     */
    public static ActionName forName(String name) {
        ActionName actionName = (ActionName) ACTION_NAMES.get(name);
        
        if (actionName == null) {
            throw new IllegalArgumentException("Unknown action name.");
        }
        
        return actionName;
    }

    public MemberType getMemberType() {
        return MemberType.ACTION;
    }
}
