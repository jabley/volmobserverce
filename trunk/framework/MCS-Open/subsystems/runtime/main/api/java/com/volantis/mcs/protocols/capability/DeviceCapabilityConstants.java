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

    public static final String HR_SUPPORTED =
            PolicyNameBuilder.elementSupported("hr");

    public static final String HR_BORDER_TOP_COLOR =
            PolicyNameBuilder.elementSupportsStyleProperty("hr", "border-top-color");

    public static final String HR_BORDER_TOP_STYLE =
            PolicyNameBuilder.elementSupportsStyleProperty("hr", "border-top-style");

    public static final String HR_BORDER_TOP_WIDTH =
            PolicyNameBuilder.elementSupportsStyleProperty("hr", "border-top-width");

    public static final String HR_BORDER_BOTTOM_COLOR =
            PolicyNameBuilder.elementSupportsStyleProperty("hr", "border-bottom-color");

    public static final String HR_BORDER_BOTTOM_STYLE =
            PolicyNameBuilder.elementSupportsStyleProperty("hr", "border-bottom-style");

    public static final String HR_BORDER_BOTTOM_WIDTH =
            PolicyNameBuilder.elementSupportsStyleProperty("hr", "border-bottom-width");

    public static final String HR_COLOR =
            PolicyNameBuilder.elementSupportsStyleProperty("hr", "color");

    public static final String HR_WIDTH =
            PolicyNameBuilder.elementSupportsStyleProperty("hr", "width");

    public static final String HR_HEIGHT =
            PolicyNameBuilder.elementSupportsStyleProperty("hr", "height");

    public static final String HR_MARGIN_TOP =
            PolicyNameBuilder.elementSupportsStyleProperty("hr", "margin-top");

    public static final String HR_MARGIN_BOTTOM =
            PolicyNameBuilder.elementSupportsStyleProperty("hr", "margin-bottom");

    public static final String DIV_SUPPORTED =
            PolicyNameBuilder.elementSupported("div");

    public static final String DIV_MARGIN_TOP =
            PolicyNameBuilder.elementSupportsStyleProperty("div", "margin-top");

    public static final String DIV_MARGIN_BOTTOM =
            PolicyNameBuilder.elementSupportsStyleProperty("div", "margin-bottom");

    public static final String DIV_BORDER_TOP_COLOR =
            PolicyNameBuilder.elementSupportsStyleProperty("div", "border-top-color");

    public static final String DIV_BORDER_TOP_STYLE =
            PolicyNameBuilder.elementSupportsStyleProperty("div", "border-top-style");

    public static final String DIV_BORDER_TOP_WIDTH =
            PolicyNameBuilder.elementSupportsStyleProperty("div", "border-top-width");

    public static final String DIV_BORDER_BOTTOM_COLOR =
            PolicyNameBuilder.elementSupportsStyleProperty("div", "border-bottom-color");

    public static final String DIV_BORDER_BOTTOM_STYLE =
            PolicyNameBuilder.elementSupportsStyleProperty("div", "border-bottom-style");

    public static final String DIV_BORDER_BOTTOM_WIDTH =
            PolicyNameBuilder.elementSupportsStyleProperty("div", "border-bottom-width");

    public static final String TR_VERTICAL_ALIGN =
            PolicyNameBuilder.elementSupportsStyleProperty("tr", "vertical-align");

    public static final String TR_ATTRIBUTE_VALIGN =
            PolicyNameBuilder.elementSupportsAttribute("tr", "valign");

    public static final String TD_MARGIN_TOP =
            PolicyNameBuilder.elementSupportsStyleProperty("td", "margin-top");

    public static final String TD_MARGIN_BOTTOM =
            PolicyNameBuilder.elementSupportsStyleProperty("td", "margin-bottom");

    public static final String TD_MARGIN_LEFT =
            PolicyNameBuilder.elementSupportsStyleProperty("td", "margin-left");

    public static final String TD_MARGIN_RIGHT =
            PolicyNameBuilder.elementSupportsStyleProperty("td", "margin-right");

    public static final String TD_PADDING_TOP =
            PolicyNameBuilder.elementSupportsStyleProperty("td", "padding-top");

    public static final String TD_PADDING_BOTTOM =
            PolicyNameBuilder.elementSupportsStyleProperty("td", "padding-bottom");

    public static final String TD_PADDING_LEFT =
            PolicyNameBuilder.elementSupportsStyleProperty("td", "padding-left");

    public static final String TD_PADDING_RIGHT =
            PolicyNameBuilder.elementSupportsStyleProperty("td", "padding-right");

    public static final String TD_VERTICAL_ALIGN =
            PolicyNameBuilder.elementSupportsStyleProperty("td", "vertical-align");

    public static final String TD_ATTRIBUTE_VALIGN =
            PolicyNameBuilder.elementSupportsAttribute("td", "valign");

    public static final String TH_VERTICAL_ALIGN =
            PolicyNameBuilder.elementSupportsStyleProperty("th", "vertical-align");

    public static final String TH_ATTRIBUTE_VALIGN =
            PolicyNameBuilder.elementSupportsAttribute("th", "valign");

    public static final String VERTICAL_ALIGN_TOP =
            PolicyNameBuilder.styleKeywordSupport("vertical-align", "top");

    public static final String VERTICAL_ALIGN_BOTTOM =
            PolicyNameBuilder.styleKeywordSupport("vertical-align", "bottom");

    public static final String VERTICAL_ALIGN_SUB =
            PolicyNameBuilder.styleKeywordSupport("vertical-align", "sub");

    public static final String VERTICAL_ALIGN_SUPER =
            PolicyNameBuilder.styleKeywordSupport("vertical-align", "super");

    public static final String MARQUEE_SUPPORTED =
            PolicyNameBuilder.elementSupported("marquee");

    public static final String MARQUEE_BEHAVIOR_ATT = "behavior";
    public static final String MARQUEE_DIRECTION_ATT = "direction";
    public static final String MARQUEE_LOOP_ATT = "loop";
    public static final String MARQUEE_BGCOLOR_ATT = "bgcolor";

    public static final String MARQUEE_BEHAVIOR =
            PolicyNameBuilder.elementSupportsAttribute("marquee", MARQUEE_BEHAVIOR_ATT);

    public static final String MARQUEE_DIRECTION =
            PolicyNameBuilder.elementSupportsAttribute("marquee", MARQUEE_DIRECTION_ATT);

    public static final String MARQUEE_LOOP =
            PolicyNameBuilder.elementSupportsAttribute("marquee", MARQUEE_LOOP_ATT);

    public static final String MARQUEE_BGCOLOR =
            PolicyNameBuilder.elementSupportsAttribute("marquee", MARQUEE_BGCOLOR_ATT);

    public static final String BLINK_SUPPORTED =
            PolicyNameBuilder.elementSupported("blink");

    public static final String U_SUPPORTED =
            PolicyNameBuilder.elementSupported("u");

    public static final String STRIKE_SUPPORTED =
            PolicyNameBuilder.elementSupported("strike");
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Oct-05	9565/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 ===========================================================================
*/
