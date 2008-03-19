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

package com.volantis.mcs.themes.values;

import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleValueFactory;

import java.util.HashMap;
import java.util.Map;

public class StyleColorNames {

    private static final Map colors = new HashMap();

    public static final StyleColorName AQUA = add("aqua", 0x00ffff);
    public static final StyleColorName BLACK = add("black", 0x000000);
    public static final StyleColorName BLUE = add("blue", 0x0000ff);
    public static final StyleColorName FUCHSIA = add("fuchsia", 0xff00ff);
    public static final StyleColorName GRAY = add("gray", 0x808080);
    public static final StyleColorName GREEN = add("green", 0x008000);
    public static final StyleColorName LIME = add("lime", 0x00ff00);
    public static final StyleColorName MAROON = add("maroon", 0x800000);
    public static final StyleColorName NAVY = add("navy", 0x000080);
    public static final StyleColorName OLIVE = add("olive", 0x808000);
    public static final StyleColorName PURPLE = add("purple", 0x800080);
    public static final StyleColorName RED = add("red", 0xff0000);
    public static final StyleColorName SILVER = add("silver", 0xc0c0c0);
    public static final StyleColorName TEAL = add("teal", 0x008080);
    public static final StyleColorName WHITE = add("white", 0xffffff);
    public static final StyleColorName YELLOW = add("yellow", 0xffff00);
    public static final StyleColorName ORANGE = add("orange", 0xffa500);


    public static final StyleColorName ACTIVE_BORDER = add("ActiveBorder");
    public static final StyleColorName ACTIVE_CAPTION = add("ActiveCaption");
    public static final StyleColorName APP_WORKSPACE = add("AppWorkspace");
    public static final StyleColorName BACKGROUND = add("Background");
    public static final StyleColorName BUTTON_FACE = add("ButtonFace");
    public static final StyleColorName BUTTON_HIGHLIGHT =
            add("ButtonHighlight");
    public static final StyleColorName BUTTON_SHADOW = add("ButtonShadow");
    public static final StyleColorName BUTTON_TEXT = add("ButtonText");
    public static final StyleColorName CAPTION_TEXT = add("CaptionText");
    public static final StyleColorName GRAY_TEXT = add("GrayText");
    public static final StyleColorName HIGHLIGHT = add("Highlight");
    public static final StyleColorName HIGHLIGHT_TEXT = add("HighlightText");
    public static final StyleColorName INACTIVE_BORDER = add("InactiveBorder");
    public static final StyleColorName INACTIVE_CAPTION =
            add("InactiveCaption");
    public static final StyleColorName INACTIVE_CAPTION_TEXT =
            add("InactiveCaptionText");
    public static final StyleColorName INFO_BACKGROUND = add("InfoBackground");
    public static final StyleColorName INFO_TEXT = add("InfoText");
    public static final StyleColorName MENU = add("Menu");
    public static final StyleColorName MENU_TEXT = add("MenuText");
    public static final StyleColorName SCROLLBAR = add("Scrollbar");
    public static final StyleColorName THREE_DDARK_SHADOW =
            add("ThreeDDarkShadow");
    public static final StyleColorName THREE_DFACE = add("ThreeDFace");
    public static final StyleColorName THREE_DHIGHLIGHT =
            add("ThreeDHighlight");
    public static final StyleColorName THREE_DLIGHT_SHADOW =
            add("ThreeDLightShadow");
    public static final StyleColorName THREE_DSHADOW = add("ThreeDShadow");
    public static final StyleColorName WINDOW = add("Window");
    public static final StyleColorName WINDOW_FRAME = add("WindowFrame");
    public static final StyleColorName WINDOW_TEXT = add("WindowText");

    private static StyleColorName add(String name) {
        return add(name, null);
    }

    private static StyleColorName add(String name, int rgb) {
        return add(name, (StyleColorRGB)
            StyleValueFactory.getDefaultInstance().getColorByRGB(null, rgb));
    }

    private static StyleColorName add(String name, StyleColorRGB rgb) {
        // Colour names should not be case sensitive.
        name = name.toLowerCase();
        if (colors.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate colors " + name);
        }

        StyleColorName color =
            StyleValueFactory.getDefaultInstance().getColorByName(name, rgb);
        colors.put(name, color);

        return color;
    }

    public static StyleColorName getColorByName(String identifier) {
        // Colour names should not be case sensitive.
        return (StyleColorName) colors.get(identifier.toLowerCase());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10816/1	pduffin	VBM:2005121401 Porting forward changes from MCS 3.5

 14-Dec-05	10818/1	pduffin	VBM:2005121401 Added color orange, refactored NamedColor and StyleColorName to remove duplication of data

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/
