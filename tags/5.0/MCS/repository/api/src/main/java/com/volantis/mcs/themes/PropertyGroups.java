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

package com.volantis.mcs.themes;

import com.volantis.styling.properties.StyleProperty;

/**
 * The properties in the property groups defined by this class should be
 * specified in the order they appear in {@link StylePropertyDetails}.   
 */
public class PropertyGroups {

    public static final int TOP = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;
    public static final int EDGE_COUNT = 4;

    /**
     * The set of properties that constitute the border edge widths.
     */
    public static final StyleProperty[] BORDER_WIDTH_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.BORDER_TOP_WIDTH,
                StylePropertyDetails.BORDER_RIGHT_WIDTH,
                StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                StylePropertyDetails.BORDER_LEFT_WIDTH,
            };

    /**
     * The set of properties that constitute the border edge colors.
     */
    public static final StyleProperty[] BORDER_COLOR_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.BORDER_TOP_COLOR,
                StylePropertyDetails.BORDER_RIGHT_COLOR,
                StylePropertyDetails.BORDER_BOTTOM_COLOR,
                StylePropertyDetails.BORDER_LEFT_COLOR,
            };

    /**
     * The set of properties that constitute the border edge styles.
     */
    public static final StyleProperty[] BORDER_STYLE_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.BORDER_TOP_STYLE,
                StylePropertyDetails.BORDER_RIGHT_STYLE,
                StylePropertyDetails.BORDER_BOTTOM_STYLE,
                StylePropertyDetails.BORDER_LEFT_STYLE,
            };

    /**
     * The set of properties that constitute all the border edge properties.
     */
    public static final StyleProperty[] BORDER_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.BORDER_TOP_COLOR,
                StylePropertyDetails.BORDER_RIGHT_COLOR,
                StylePropertyDetails.BORDER_BOTTOM_COLOR,
                StylePropertyDetails.BORDER_LEFT_COLOR,

                StylePropertyDetails.BORDER_TOP_STYLE,
                StylePropertyDetails.BORDER_RIGHT_STYLE,
                StylePropertyDetails.BORDER_BOTTOM_STYLE,
                StylePropertyDetails.BORDER_LEFT_STYLE,

                StylePropertyDetails.BORDER_TOP_WIDTH,
                StylePropertyDetails.BORDER_RIGHT_WIDTH,
                StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                StylePropertyDetails.BORDER_LEFT_WIDTH,
            };

    /**
     * The set of properties that constitute the padding edges.
     */
    public static final StyleProperty[] PADDING_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.PADDING_TOP,
                StylePropertyDetails.PADDING_RIGHT,
                StylePropertyDetails.PADDING_BOTTOM,
                StylePropertyDetails.PADDING_LEFT,
            };

    /**
     * The set of properties that constitute the margin edges.
     */
    public static final StyleProperty[] MARGIN_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.MARGIN_TOP,
                StylePropertyDetails.MARGIN_RIGHT,
                StylePropertyDetails.MARGIN_BOTTOM,
                StylePropertyDetails.MARGIN_LEFT,
            };

    /**
     * The set of properties that constitute the mcs-border-radius.
     */
    public static final StyleProperty[] MCS_BORDER_RADIUS_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.MCS_BORDER_TOP_LEFT_RADIUS,
                StylePropertyDetails.MCS_BORDER_TOP_RIGHT_RADIUS,
                StylePropertyDetails.MCS_BORDER_BOTTOM_RIGHT_RADIUS,
                StylePropertyDetails.MCS_BORDER_BOTTOM_LEFT_RADIUS,
            };    
    
    /**
     * The set of properties that constitute the text decoration.
     */
    public static final StyleProperty[] TEXT_DECORATION_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.MCS_TEXT_BLINK,
                StylePropertyDetails.MCS_TEXT_LINE_THROUGH_COLOR,
                StylePropertyDetails.MCS_TEXT_LINE_THROUGH_STYLE,
                StylePropertyDetails.MCS_TEXT_OVERLINE_COLOR,
                StylePropertyDetails.MCS_TEXT_OVERLINE_STYLE,
                StylePropertyDetails.MCS_TEXT_UNDERLINE_COLOR,
                StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE,
            };

    /**
     * The properties in the background shorthands.
     */
    public static final StyleProperty[] BACKGROUND_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.BACKGROUND_ATTACHMENT,
                StylePropertyDetails.BACKGROUND_COLOR,
                StylePropertyDetails.BACKGROUND_IMAGE,
                StylePropertyDetails.BACKGROUND_POSITION,
                StylePropertyDetails.BACKGROUND_REPEAT,
            };

    /**
     * The properties in the font shorthands.
     */
    public static final StyleProperty[] FONT_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.FONT_STYLE,
                StylePropertyDetails.FONT_VARIANT,
                StylePropertyDetails.FONT_WEIGHT,
                StylePropertyDetails.FONT_SIZE,
                StylePropertyDetails.LINE_HEIGHT,
                StylePropertyDetails.FONT_FAMILY,
            };

    /**
     * The properties in the font shorthands.
     */
    public static final StyleProperty[] MCS_FONT_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.FONT_STYLE,
                StylePropertyDetails.FONT_VARIANT,
                StylePropertyDetails.FONT_WEIGHT,
                StylePropertyDetails.FONT_SIZE,
                StylePropertyDetails.LINE_HEIGHT,
                StylePropertyDetails.FONT_FAMILY,
                StylePropertyDetails.MCS_SYSTEM_FONT,
            };

    public static final StyleProperty[] BORDER_TOP_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.BORDER_TOP_COLOR,
                StylePropertyDetails.BORDER_TOP_STYLE,
                StylePropertyDetails.BORDER_TOP_WIDTH,
            };

    public static final StyleProperty[] BORDER_RIGHT_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.BORDER_RIGHT_COLOR,
                StylePropertyDetails.BORDER_RIGHT_STYLE,
                StylePropertyDetails.BORDER_RIGHT_WIDTH,
            };

    public static final StyleProperty[] BORDER_BOTTOM_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.BORDER_BOTTOM_COLOR,
                StylePropertyDetails.BORDER_BOTTOM_STYLE,
                StylePropertyDetails.BORDER_BOTTOM_WIDTH,
            };

    public static final StyleProperty[] BORDER_LEFT_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.BORDER_LEFT_COLOR,
                StylePropertyDetails.BORDER_LEFT_STYLE,
                StylePropertyDetails.BORDER_LEFT_WIDTH,
            };

    public static final StyleProperty[] MCS_EFFECT =
        new StyleProperty[]{
            StylePropertyDetails.MCS_EFFECT_STYLE,
            StylePropertyDetails.MCS_EFFECT_DURATION,
        };

    public static final StyleProperty[] MARQUEE_PROPERTIES = 
            new StyleProperty[] {
                StylePropertyDetails.MCS_MARQUEE_DIRECTION,
                StylePropertyDetails.MCS_MARQUEE_REPETITION,
                StylePropertyDetails.MCS_MARQUEE_SPEED,
                StylePropertyDetails.MCS_MARQUEE_STYLE,
            };
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
