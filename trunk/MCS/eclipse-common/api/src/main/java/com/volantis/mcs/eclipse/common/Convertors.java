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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.jface.resource.ColorRegistry;

import java.util.StringTokenizer;

/**
 * General class for converting objects of one type to another.
 */
public class Convertors {

    /**
     * The weighting to apply to reds in calculating contrasting colors.
     */
    private static final int RED_WEIGHT = 3;

    /**
     * The weighting to apply to greens in calculating contrasting colors.
     */
    private static final int GREEN_WEIGHT = 4;

    /**
     * The weighting to apply to blues in calculating contrasting colors.
     */
    private static final int BLUE_WEIGHT = 2;

    /**
     * The total maximum brightness of a color with weightings applied.
     */
    private static final int MAX_BRIGHTNESS = (0xff * RED_WEIGHT
            + 0xff * GREEN_WEIGHT + 0xff * BLUE_WEIGHT);

    /**
     * The brightness below which white will be used as the contrast
     */
    private static final int WHITE_THRESHOLD = (int) (MAX_BRIGHTNESS * 0.40);

    /**
     * The brightness above which black will be used as the contrast
     */
    private static final int BLACK_THRESHOLD = (int) (MAX_BRIGHTNESS * 0.60);

    /**
     * The Color black to return as a contrast.
     */
    private static Color BLACK_COLOR;

    /**
     * The Color white to return as a contrast.
     */
    private static Color WHITE_COLOR;

    static {
        final ColorRegistry registry = EclipseCommonPlugin.getColorRegistry();

        synchronized (registry) {
            BLACK_COLOR = registry.get(NamedColor.BLACK.getName());
            if (BLACK_COLOR == null) {
                RGB blackRGB = hexToRGB(NamedColor.BLACK.getHex());
                registry.put(NamedColor.BLACK.getHex(), blackRGB);
                registry.put(NamedColor.BLACK.getName(), blackRGB);
                BLACK_COLOR = registry.get(NamedColor.BLACK.getName());
            }

            WHITE_COLOR = registry.get(NamedColor.WHITE.getName());
            if (WHITE_COLOR == null) {
                RGB blackRGB = hexToRGB(NamedColor.WHITE.getHex());
                registry.put(NamedColor.WHITE.getHex(), blackRGB);
                registry.put(NamedColor.WHITE.getName(), blackRGB);
                WHITE_COLOR = registry.get(NamedColor.WHITE.getName());
            }
        }
    }

    /**
     * Converts a hex string to an SWT RGB object, or null if invalid value.
     * @param hex the hex value which must begin with the # character
     * @return an SWT RGB representation of the hex value
     * @throws NumberFormatException if hex contains invalid characters
     */
    public static RGB hexToRGB(String hex) {
        RGB rgb = null;
        if (hex != null && hex.length() > 0 && hex.charAt(0) == '#') {
            hex = hex.substring(1);
            if (hex.length() == 3) {
                int red =
                        Integer.parseInt(
                                hex.substring(0, 1) + hex.substring(0, 1),
                                16);
                int green =
                        Integer.parseInt(
                                hex.substring(1, 2) + hex.substring(1, 2),
                                16);
                int blue =
                        Integer.parseInt(
                                hex.substring(2, 3) + hex.substring(2, 3),
                                16);
                rgb = new RGB(red, green, blue);
            } else if (hex.length() == 6) {
                int red = Integer.parseInt(hex.substring(0, 2), 16);
                int green = Integer.parseInt(hex.substring(2, 4), 16);
                int blue = Integer.parseInt(hex.substring(4, 6), 16);
                rgb = new RGB(red, green, blue);
            }
        }
        return rgb;
    }

    /**
     * Converts an SWT RGB object to a 6-digit hex value beginning with a #.
     * @param rgb the SWT RGB object
     * @return a hex string representation, or null
     */
    public static String RGBToHex(RGB rgb) {
        if (rgb == null) {
            return null;
        }
        StringBuffer red = new StringBuffer(Integer.toHexString(rgb.red));
        if (red.length() == 1) {
            red.insert(0, '0');
        }
        StringBuffer green = new StringBuffer(Integer.toHexString(rgb.green));
        if (green.length() == 1) {
            green.insert(0, '0');
        }
        StringBuffer blue = new StringBuffer(Integer.toHexString(rgb.blue));
        if (blue.length() == 1) {
            blue.insert(0, '0');
        }
        StringBuffer hex = new StringBuffer(7);
        hex.append('#').append(red).append(green).append(blue);

        return hex.toString();
    }

    /**
     Takes a string of the format <red, green, blue> where red, green and
     blue are integers in the range 0-255 and returns the Color object
     represented by this rgb value.
     @param rgbString String reprentation of a color to convertor
     @return Color representation of the String param
     */
    public static Color rgbStringToColor(String rgbString) {
        if (rgbString == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(rgbString, ","); //$NON-NLS-1$
        int red = Integer.parseInt(st.nextToken().trim());
        int green = Integer.parseInt(st.nextToken().trim());
        int blue = Integer.parseInt(st.nextToken().trim());
        return new Color(null, red, green, blue);
    }

    /**
     * Given a {@link Color}, compute a contrasting {@link Color}
     *
     * @param color the {@link Color} to find a contrast for.
     * @return the contrasting {@link Color}
     */
    public static Color getContrastingColor(Color color) {
        Color contrast = BLACK_COLOR;

        if (color != null) {
            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();

            int brightness = (red * RED_WEIGHT
                    + green * GREEN_WEIGHT
                    + blue * BLUE_WEIGHT);

            if (brightness <= WHITE_THRESHOLD) {
                contrast = WHITE_COLOR;
            } else if (brightness >= BLACK_THRESHOLD) {
                contrast = BLACK_COLOR;
            } else {
                brightness = (red | green | blue);

                if (brightness < 0xA0) {
                    contrast = WHITE_COLOR;
                } else {
                    contrast = BLACK_COLOR;
                }
            }
        }
        return contrast;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Sep-04	5663/1	tom	VBM:2004081003 Replaced ColorRegistry with Eclipse V3.0.0 Version

 03-Aug-04	4902/3	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 22-Jul-04	4905/2	adrian	VBM:2004071507 Refactored Color support in layout formats

 24-Feb-04	3021/1	pcameron	VBM:2004020211 Some tweaks to StyledGroup and FormatComposites

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 25-Nov-03	1634/6	pcameron	VBM:2003102205 A few changes to ColorSelector

 ===========================================================================
*/
