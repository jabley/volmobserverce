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
 * $Header: /src/voyager/com/volantis/mcs/protocols/EventConstants.java,v 1.4 2003/02/27 09:52:03 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jan-02    Paul            VBM:2001122105 - Created to contain constants
 *                              for event attributes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 06-Aug-02    Paul            VBM:2002080509 - Added ON_TIMER and ON_ENTER.
 * 24-Feb-03    Byron           VBM:2003022105 - Replaced ON_ENTER with
 *                              ON_ENTER_FORWARD and ON_ENTER_BACKWARD.
 *                              Similarly for ON_ENTER_BIT. Updated MAX_EVENTS
 *                              constant.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

public interface EventConstants {

    // General events.
    public static final int ON_CLICK = 0;
    public static final int ON_DOUBLE_CLICK = 1;
    public static final int ON_KEY_DOWN = 2;
    public static final int ON_KEY_PRESS = 3;
    public static final int ON_KEY_UP = 4;
    public static final int ON_MOUSE_DOWN = 5;
    public static final int ON_MOUSE_MOVE = 6;
    public static final int ON_MOUSE_OUT = 7;
    public static final int ON_MOUSE_OVER = 8;
    public static final int ON_MOUSE_UP = 9;

    public static final int ON_CLICK_BIT = 1 << ON_CLICK;
    public static final int ON_DOUBLE_CLICK_BIT = 1 << ON_DOUBLE_CLICK;
    public static final int ON_KEY_DOWN_BIT = 1 << ON_KEY_DOWN;
    public static final int ON_KEY_PRESS_BIT = 1 << ON_KEY_PRESS;
    public static final int ON_KEY_UP_BIT = 1 << ON_KEY_UP;
    public static final int ON_MOUSE_DOWN_BIT = 1 << ON_MOUSE_DOWN;
    public static final int ON_MOUSE_MOVE_BIT = 1 << ON_MOUSE_MOVE;
    public static final int ON_MOUSE_OUT_BIT = 1 << ON_MOUSE_OUT;
    public static final int ON_MOUSE_OVER_BIT = 1 << ON_MOUSE_OVER;
    public static final int ON_MOUSE_UP_BIT = 1 << ON_MOUSE_UP;

    public static final int GENERAL_EVENTS_MASK = (ON_CLICK_BIT
            | ON_DOUBLE_CLICK_BIT
            | ON_KEY_DOWN_BIT
            | ON_KEY_PRESS_BIT
            | ON_KEY_UP_BIT
            | ON_MOUSE_DOWN_BIT
            | ON_MOUSE_MOVE_BIT
            | ON_MOUSE_OUT_BIT
            | ON_MOUSE_OVER_BIT
            | ON_MOUSE_UP_BIT);

    // Page events.
    public static final int ON_LOAD = 10;
    public static final int ON_UNLOAD = 11;

    public static final int ON_LOAD_BIT = 1 << ON_LOAD;
    public static final int ON_UNLOAD_BIT = 1 << ON_UNLOAD;

    public static final int PAGE_EVENTS_MASK = (ON_LOAD_BIT
            | ON_UNLOAD_BIT);

    // Focus events.
    public static final int ON_FOCUS = 12;
    public static final int ON_BLUR = 13;

    public static final int ON_FOCUS_BIT = 1 << ON_FOCUS;
    public static final int ON_BLUR_BIT = 1 << ON_BLUR;

    public static final int FOCUS_EVENTS_MASK = (ON_FOCUS_BIT
            | ON_BLUR_BIT);

    // Form events.
    public static final int ON_SUBMIT = 14;
    public static final int ON_RESET = 15;

    public static final int ON_SUBMIT_BIT = 1 << ON_SUBMIT;
    public static final int ON_RESET_BIT = 1 << ON_RESET;

    public static final int FORM_EVENTS_MASK = (ON_SUBMIT_BIT
            | ON_RESET_BIT);

    // Form field events.
    public static final int ON_CHANGE = 16;
    public static final int ON_SELECT = 17;

    public static final int ON_CHANGE_BIT = 1 << ON_CHANGE;
    public static final int ON_SELECT_BIT = 1 << ON_SELECT;

    public static final int FIELD_EVENTS_MASK = (ON_CHANGE_BIT
            | ON_SELECT_BIT);

    // WML / WapTV specific events
    public static final int ON_TIMER = 18;
    public static final int ON_ENTER_FORWARD = 19;
    public static final int ON_ENTER_BACKWARD = 20;

    public static final int ON_TIMER_BIT = 1 << ON_TIMER;

    public static final int DOM_ACTIVATE = 21;
    public static final int DOM_ACTIVATE_BIT = 1 << DOM_ACTIVATE;

    // These two bit are not used anywhere???
    public static final int ON_ENTER_FORWARD_BIT = 1 << ON_ENTER_FORWARD;
    public static final int ON_ENTER_BACKWARD_BIT = 1 << ON_ENTER_BACKWARD;

    public static final int MAX_EVENTS = 22;
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
