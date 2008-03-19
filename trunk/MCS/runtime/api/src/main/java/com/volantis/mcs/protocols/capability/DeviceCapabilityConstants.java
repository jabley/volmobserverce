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
package com.volantis.mcs.protocols.capability;

/**
 * Device policy values which define a devices capabilities
 */
public interface DeviceCapabilityConstants {

    /**
     * prefix for the to the element part of the device policy value
     */
    static final String ELEMENT_ID = "x-element";

    /**
     * policy value parameter for showing an item supports something
     */
    static final String SUPPORTS = "supports";

    /**
     * policy value parameter for showing an item is supported
     */
    static final String SUPPORTED = "supported";

    /**
     * Prefix for the attribute part of the device policy value.
     */
    static final String ATTRIBUTE_ID = ".attribute";

    public static final String HR_SUPPORTED =
           ELEMENT_ID+".hr."+SUPPORTED;


    public static final String HR_BORDER_TOP_COLOR =
           ELEMENT_ID+".hr."+SUPPORTS+".css.border-top-color";

    public static final String HR_BORDER_TOP_STYLE =
           ELEMENT_ID+".hr."+SUPPORTS+".css.border-top-style";

    public static final String HR_BORDER_TOP_WIDTH =
           ELEMENT_ID+".hr."+SUPPORTS+".css.border-top-width";

    public static final String HR_BORDER_BOTTOM_COLOR =
           ELEMENT_ID+".hr."+SUPPORTS+".css.border-bottom-color";

    public static final String HR_BORDER_BOTTOM_STYLE =
           ELEMENT_ID+".hr."+SUPPORTS+".css.border-bottom-style";

    public static final String HR_BORDER_BOTTOM_WIDTH =
           ELEMENT_ID+".hr."+SUPPORTS+".css.border-bottom-width";

    public static final String HR_COLOR =
           ELEMENT_ID+".hr."+SUPPORTS+".css.color";

    public static final String HR_WIDTH =
           ELEMENT_ID+".hr."+SUPPORTS+".css.width";

    public static final String HR_HEIGHT =
           ELEMENT_ID+".hr."+SUPPORTS+".css.height";

    public static final String HR_MARGIN_TOP =
           ELEMENT_ID+".hr."+SUPPORTS+".css.margin-top";

    public static final String HR_MARGIN_BOTTOM =
           ELEMENT_ID+".hr."+SUPPORTS+".css.margin-bottom";

    public static final String DIV_SUPPORTED =
           ELEMENT_ID+".div."+SUPPORTED;

    public static final String DIV_MARGIN_TOP =
           ELEMENT_ID+".div."+SUPPORTS+".css.margin-top";

    public static final String DIV_MARGIN_BOTTOM =
           ELEMENT_ID+".div."+SUPPORTS+".css.margin-bottom";

    public static final String DIV_BORDER_TOP_COLOR =
           ELEMENT_ID+".div."+SUPPORTS+".css.border-top-color";

    public static final String DIV_BORDER_TOP_STYLE =
           ELEMENT_ID+".div."+SUPPORTS+".css.border-top-style";

    public static final String DIV_BORDER_TOP_WIDTH =
           ELEMENT_ID+".div."+SUPPORTS+".css.border-top-width";

    public static final String DIV_BORDER_BOTTOM_COLOR =
           ELEMENT_ID+".div."+SUPPORTS+".css.border-bottom-color";

    public static final String DIV_BORDER_BOTTOM_STYLE =
           ELEMENT_ID+".div."+SUPPORTS+".css.border-bottom-style";

    public static final String DIV_BORDER_BOTTOM_WIDTH =
           ELEMENT_ID+".div."+SUPPORTS+".css.border-bottom-width";

    public static final String TD_MARGIN_TOP =
           ELEMENT_ID+".td."+SUPPORTS+".css.margin-top";

    public static final String TD_MARGIN_BOTTOM =
           ELEMENT_ID+".td."+SUPPORTS+".css.margin-bottom";

    public static final String TD_MARGIN_LEFT =
           ELEMENT_ID+".td."+SUPPORTS+".css.margin-left";

    public static final String TD_MARGIN_RIGHT =
           ELEMENT_ID+".td."+SUPPORTS+".css.margin-right";

    public static final String TD_PADDING_TOP =
           ELEMENT_ID+".td."+SUPPORTS+".css.padding-top";

    public static final String TD_PADDING_BOTTOM =
           ELEMENT_ID+".td."+SUPPORTS+".css.padding-bottom";

    public static final String TD_PADDING_LEFT =
           ELEMENT_ID+".td."+SUPPORTS+".css.padding-left";

    public static final String TD_PADDING_RIGHT =
           ELEMENT_ID+".td."+SUPPORTS+".css.padding-right";

    public static final String MARQUEE_SUPPORTED =
            ELEMENT_ID+".marquee."+SUPPORTED;

    
    public static final String MARQUEE_BEHAVIOR_ATT = "behavior";
    public static final String MARQUEE_DIRECTION_ATT = "direction";
    public static final String MARQUEE_LOOP_ATT = "loop";
    public static final String MARQUEE_BGCOLOR_ATT = "bgcolor";

    public static final String MARQUEE_BEHAVIOR = ELEMENT_ID + ".marquee." +
            SUPPORTS + ATTRIBUTE_ID + "." + MARQUEE_BEHAVIOR_ATT;

    public static final String MARQUEE_DIRECTION = ELEMENT_ID + ".marquee." +
            SUPPORTS + ATTRIBUTE_ID + "." + MARQUEE_DIRECTION_ATT;

    public static final String MARQUEE_LOOP = ELEMENT_ID + ".marquee." +
            SUPPORTS + ATTRIBUTE_ID + "." + MARQUEE_LOOP_ATT;

    public static final String MARQUEE_BGCOLOR = ELEMENT_ID + ".marquee." +
            SUPPORTS + ATTRIBUTE_ID + "." + MARQUEE_BGCOLOR_ATT;

    public static final String BLINK_SUPPORTED =
            ELEMENT_ID + ".blink." + SUPPORTED;

    public static final String U_SUPPORTED =
            ELEMENT_ID + ".u." + SUPPORTED;

    public static final String STRIKE_SUPPORTED =
            ELEMENT_ID + ".strike." + SUPPORTED;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Oct-05	9565/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 ===========================================================================
*/
