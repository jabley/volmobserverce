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

package com.volantis.mcs.protocols.menu.model;

/**
 * A typesafe enumeration defining the types of event for which event handlers
 * may be defined (in the context of menus).
 */
public final class EventType {

    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_BLUR
     */
    public static final EventType ON_BLUR = new EventType("ON_BLUR");

    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_CLICK
     */
    public static final EventType ON_CLICK = new EventType("ON_CLICK");

    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_DOUBLE_CLICK
     */
    public static final EventType ON_DOUBLE_CLICK =
        new EventType("ON_DOUBLE_CLICK");

    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_FOCUS
     */
    public static final EventType ON_FOCUS = new EventType("ON_FOCUS");
    
    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_KEY_DOWN
     */
    public static final EventType ON_KEY_DOWN = new EventType("ON_KEY_DOWN");

    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_KEY_PRESS
     */
    public static final EventType ON_KEY_PRESS = new EventType("ON_KEY_PRESS");

    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_KEY_UP
     */
    public static final EventType ON_KEY_UP = new EventType("ON_KEY_UP");

    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_MOUSE_DOWN
     */
    public static final EventType ON_MOUSE_DOWN =
        new EventType("ON_MOUSE_DOWN");

    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_MOUSE_MOVE
     */
    public static final EventType ON_MOUSE_MOVE =
        new EventType("ON_MOUSE_MOVE");

    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_MOUSE_OUT
     */
    public static final EventType ON_MOUSE_OUT = new EventType("ON_MOUSE_OUT");

    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_MOUSE_OVER
     */
    public static final EventType ON_MOUSE_OVER =
        new EventType("ON_MOUSE_OVER");

    /**
     * An event type literal.
     * 
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ON_MOUSE_UP
     */
    public static final EventType ON_MOUSE_UP = new EventType("ON_MOUSE_UP");
    
    /**
     * The internal name for the enumeration literal.
     */
    private final String name;

    /**
     * Initializes the new instance using the given parameters.
     * 
     * @param name the internal name for the new literal
     */
    private EventType(final String name) {
        this.name = name;
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     * 
     * @return internal name for the enumeration literal
     */
    public final String toString() {
        return name;
    }

}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Mar-04	3288/1	philws	VBM:2004022702 Add Menu Model API

 ===========================================================================
*/
