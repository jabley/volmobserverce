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

package com.volantis.mcs.accessors.xml.jibx;

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.values.AngleUnit;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.TimeUnit;
import com.volantis.mcs.themes.values.FrequencyUnit;
import com.volantis.mcs.themes.values.converters.ColorConverter;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;

public class SerializeStyleValues {

    private static final StylePropertyDefinitions DEFINITIONS =
            StylePropertyDetails.getDefinitions();

    /**
     * Convert a angle unit as text into the enumeration.
     *
     * @param text The text representation of the angle unit.
     * @return The enumeration value, or -1 if the text was not a recognised
     *         unit.
     */
    public static AngleUnit convertAngleUnitFromText(String text) {
        return AngleUnit.getUnitByName(text);
    }

    /**
     * Convert a angle unit unit into text.
     *
     * @param unit The unit value.
     * @return The text representation of the angle unit, or null.
     */
    public static String convertAngleUnitToText(AngleUnit unit) {
        return unit.toString();
    }

    /**
     * Convert a color RGB as text into the number.
     *
     * @param text The text representation of the color RGB.
     * @return The int RGB value.
     */
    public static int convertColorRGBFromText(String text) {
        if (!text.startsWith("#")) {
            throw new IllegalArgumentException("Invalid rgb string");
        } else {
            text = text.substring(1);
            return Integer.parseInt(text, 16);
        }
    }

    /**
     * Convert a color RGB int value into text.
     *
     * @param rgb The RGB int value.
     * @return The text representation of the color name, or null.
     */
    public static String convertColorRGBToText(int rgb) {
        return ColorConverter.convertColorRGBToText(rgb, false);
    }

    /**
     * Convert a length unit as text into the enumeration.
     *
     * @param text The text representation of the length unit.
     * @return The enumeration value, or -1 if the text was not a recognised
     *         unit.
     */
    public static LengthUnit convertLengthUnitFromText(String text) {
        return LengthUnit.getUnitByName(text);
    }

    /**
     * Convert a length unit unit into text.
     *
     * @param unit The unit value.
     * @return The text representation of the length unit, or null.
     */
    public static String convertLengthUnitToText(LengthUnit unit) {
        return unit.toString();
    }

    /**
     * Convert a time unit as text into the enumeration.
     *
     * @param text The text representation of the time unit.
     * @return The enumeration value, or -1 if the text was not a recognised
     *         unit.
     */
    public static TimeUnit convertTimeUnitFromText(String text) {
        return TimeUnit.getUnitByName(text);
    }

    /**
     * Convert a time unit unit into text.
     *
     * @param unit The unit value.
     * @return The text representation of the time unit, or null.
     */
    public static String convertTimeUnitToText(TimeUnit unit) {
        return unit.toString();
    }

    /**
     * Display a double without .0 if necessary
     */
    public final static String convertDoubleToText(double value) {
        int intValue = (int) value;
        String text;
        if (intValue == value) {
            text = Integer.toString(intValue, 10);
        } else {
            text = Double.toString(value);
        }
        return text;
    }

    public static String convertStylePropertyToText(StyleProperty property) {
        return property.getName();
    }

    public static StyleProperty convertStylePropertyFromText(String name) {
        StyleProperty property = DEFINITIONS.getStyleProperty(name);
        if (property == null) {
            throw new IllegalArgumentException(
                    "Unknown property '" + name + "'");
        }

        return property;
    }

    public static String convertPriorityToText(Priority priority) {
        if (priority == Priority.NORMAL) {
            return "normal";
        } else if (priority == Priority.IMPORTANT) {
            return "important";
        } else {
            throw new IllegalArgumentException(
                    "Unknown priority '" + priority + "'");
        }
    }

    public static Priority convertPriorityFromText(String name) {
        if (name == null) {
            return Priority.NORMAL;
        } else if (name.equals("normal")) {
            return Priority.NORMAL;
        } else if (name.equals("important")) {
            return Priority.IMPORTANT;
        } else {
            throw new IllegalArgumentException(
                    "Unknown priority '" + name + "'");
        }
    }

    /**
     * Convert a frequency unit as text into the enumeration.
     *
     * @param text The text representation of the frequency unit.
     * @return The enumeration value, or -1 if the text was not a recognised
     *         unit.
     */
    public static FrequencyUnit convertFrequencyUnitFromText(String text) {
        return FrequencyUnit.getUnitByName(text);
    }

    /**
     * Convert a frequency unit into text.
     *
     * @param unit The unit value.
     * @return The text representation of the frequency unit, or null.
     */
    public static String convertFrequencyUnitToText(FrequencyUnit unit) {
        return unit.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10816/1	pduffin	VBM:2005121401 Porting forward changes from MCS 3.5

 14-Dec-05	10818/1	pduffin	VBM:2005121401 Added color orange, refactored NamedColor and StyleColorName to remove duplication of data

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 21-Oct-05	9942/1	emma	VBM:2005102007 Fixing problem with serializing colorRGB values

 21-Oct-05	9946/1	emma	VBM:2005102007 Fixing problem with serializing colorRGB values

 21-Oct-05	9942/1	emma	VBM:2005102007 Fixing problem with serializing colorRGB values

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 ===========================================================================
*/
