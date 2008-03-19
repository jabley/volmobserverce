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

import com.volantis.shared.iteration.IterationAction;

/**
 * The set of shorthands.
 */
public class StyleShorthands {

    /**
     * The shorthand definitions.
     */
    private static final StyleShorthandsDefinitions definitions;

    static {
        definitions = new StyleShorthandsDefinitions();
        definitions.addShorthand("background",
                PropertyGroups.BACKGROUND_PROPERTIES);
        definitions.addShorthand("border-color",
                PropertyGroups.BORDER_COLOR_PROPERTIES);
        definitions.addShorthand("border-style",
                PropertyGroups.BORDER_STYLE_PROPERTIES);
        definitions.addShorthand("border-top",
                PropertyGroups.BORDER_TOP_PROPERTIES);
        definitions.addShorthand("border-right",
                PropertyGroups.BORDER_RIGHT_PROPERTIES);
        definitions.addShorthand("border-bottom",
                PropertyGroups.BORDER_BOTTOM_PROPERTIES);
        definitions.addShorthand("border-left",
                PropertyGroups.BORDER_LEFT_PROPERTIES);
        definitions.addShorthand("border-width",
                PropertyGroups.BORDER_WIDTH_PROPERTIES);
        definitions.addShorthand("border", PropertyGroups.BORDER_PROPERTIES);
        definitions.addShorthand("font", PropertyGroups.FONT_PROPERTIES);
        definitions.addShorthand("margin", PropertyGroups.MARGIN_PROPERTIES);
        definitions.addShorthand("padding", PropertyGroups.PADDING_PROPERTIES);
        definitions.addShorthand("mcs-effect", PropertyGroups.MCS_EFFECT);
        definitions.addShorthand("mcs-border-radius", 
                PropertyGroups.MCS_BORDER_RADIUS_PROPERTIES);
        definitions.addShorthand("mcs-marquee", 
                PropertyGroups.MARQUEE_PROPERTIES);                
    }

    /**
     * The 'background' shorthand.
     */
    public static final StyleShorthand BACKGROUND =
            definitions.getShorthand("background");

    /**
     * The 'border-color' shorthand.
     */
    public static final StyleShorthand BORDER_COLOR =
            definitions.getShorthand("border-color");

    /**
     * The 'border-style' shorthand.
     */
    public static final StyleShorthand BORDER_STYLE =
            definitions.getShorthand("border-style");

    /**
     * The 'border-top' shorthand.
     */
    public static final StyleShorthand BORDER_TOP =
            definitions.getShorthand("border-top");

    /**
     * The 'border-right' shorthand.
     */
    public static final StyleShorthand BORDER_RIGHT =
            definitions.getShorthand("border-right");

    /**
     * The 'border-bottom' shorthand.
     */
    public static final StyleShorthand BORDER_BOTTOM =
            definitions.getShorthand("border-bottom");

    /**
     * The 'border-left' shorthand.
     */
    public static final StyleShorthand BORDER_LEFT =
            definitions.getShorthand("border-left");

    /**
     * The 'border-width' shorthand.
     */
    public static final StyleShorthand BORDER_WIDTH =
            definitions.getShorthand("border-width");

    /**
     * The 'border' shorthand.
     */
    public static final StyleShorthand BORDER =
            definitions.getShorthand("border");

    /**
     * The 'font' shorthand.
     */
    public static final StyleShorthand FONT =
            definitions.getShorthand("font");

    /**
     * The 'border-width' shorthand.
     */
    public static final StyleShorthand LIST_STYLE =
            definitions.getShorthand("border-width");

    /**
     * The 'margin' shorthand.
     */
    public static final StyleShorthand MARGIN =
            definitions.getShorthand("margin");

    /**
     * The 'mcs-marquee' shorthand.
     */
    public static final StyleShorthand MARQUEE =
            definitions.getShorthand("mcs-marquee");

    /**
     * The 'padding' shorthand.
     */
    public static final StyleShorthand PADDING =
            definitions.getShorthand("padding");

    /**
     * The 'mcs-effect' shorthand.
     */
    public static final StyleShorthand MCS_EFFECT =
            definitions.getShorthand("mcs-effect");

    public static final StyleShorthand MCS_BORDER_RADIUS = 
        definitions.getShorthand("mcs-border-radius");
    
    /**
     * Iterate over the shorthands.
     *
     * @param iteratee The object to invoke for each shorthand.
     * @return The result of the last call to the iteratee.
     */
    public IterationAction iterate(StyleShorthandIteratee iteratee) {
        return definitions.iterate(iteratee);
    }

    /**
     * Get the shorthand definitions.
     */
    public static StyleShorthandsDefinitions getDefinitions() {
        return definitions;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10829/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 ===========================================================================
*/
