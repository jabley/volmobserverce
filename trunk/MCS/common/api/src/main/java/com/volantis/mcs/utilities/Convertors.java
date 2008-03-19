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
 * $Header: /src/voyager/com/volantis/mcs/utilities/Convertors.java,v 1.6 2002/03/18 12:41:19 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.utilities;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.*;
import java.util.StringTokenizer;

/*
  General class for methods that convert objects or types from one object or
  type to another object or type.

  @author Allan Boyd
*/
public class Convertors {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(Convertors.class);

    /**
     * Constant for CSS transparent colors.
     */
    public static final String TRANSPARENT = "transparent";


    /**
     * Gets the mime type from a content type.
     *
     * @return the mime type
     */
    public static String contentTypeToMimeType(String contentType) {
        String mimeType = contentType;

        if (contentType != null) {
            int idx = contentType.indexOf(";");

            if (idx > -1) {
                mimeType = contentType.substring(0, idx);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Extracted mime type = " + mimeType +
                    " from content type = " + contentType);
        }

        return mimeType;
    }

    /**
     * Takes a string of the format <red, green, blue> where red, green and
     * blue are integers in the range 0-255 and returns the Color object
     * represented by this rgb value.
     *
     * @param rgbString String reprentation of a color to convertor
     * @return Color representation of the String param
     */
    public static Color rgbStringToColor(String rgbString) {
        if (rgbString == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(rgbString, ",");
        int red = Integer.parseInt(st.nextToken().trim());
        int green = Integer.parseInt(st.nextToken().trim());
        int blue = Integer.parseInt(st.nextToken().trim());

        return new Color(red, green, blue);
    }

    /**
     * Takes a Color object and returns this to its CSS (Cascading
     * Style Sheet) equivalent string. CSS can cope with several color
     * formats. The format used here is #rrggbb where the rr, gg and bb
     * are the 0-255 hex values for the corresponding channels.
     *
     * @param c the Color to convert
     * @return String representation of the Color param in the format
     *         #rrggbb where the characters are the hex rgb values of the Color or
     *         TRANSPARENT of Color is null
     */
    public static String colorToCSSColor(Color c) {
        if (c == null) {
            return TRANSPARENT;
        }

        StringBuffer red = new StringBuffer(Integer.toHexString(c.getRed()));
        if (red.length() == 1) {
            red.insert(0, '0');
        }
        StringBuffer green =
                new StringBuffer(Integer.toHexString(c.getGreen()));
        if (green.length() == 1) {
            green.insert(0, '0');
        }
        StringBuffer blue = new StringBuffer(Integer.toHexString(c.getBlue()));
        if (blue.length() == 1) {
            blue.insert(0, '0');
        }

        StringBuffer cSS = new StringBuffer(7);
        cSS.insert(0, '#');
        cSS.append(red).append(green).append(blue);

        return cSS.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Apr-05	7376/2	allan	VBM:2005031101 SmartClient bundler - commit for testing

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
