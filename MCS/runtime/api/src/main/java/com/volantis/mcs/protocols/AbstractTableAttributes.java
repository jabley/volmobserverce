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

package com.volantis.mcs.protocols;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbstractTableAttributes
        extends MCSAttributes {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(AbstractTableAttributes.class);

    /**
     * Regular expression pattern to recognise length values with units.
     */
    private static final Pattern LENGTH_PATTERN =
            Pattern.compile("([+-]?\\d*[.\\d*]?)\\s*(mm|cm|pt|pc|em|ex|px|in)");

    /**
     * Set the synthesized-value of the given property to the one represented
     * by <code>stringValue</code>. It will not reset the value if the new
     * value is null. Automatically checks the possible types of the property
     * and creates the value according to them.
     * <p>
     * If the property supports the keyword type the given keyword mapper is
     * used to try to convert the string value. If the property does not
     * support keyword type <code>null</code> argument is accepted.
     * <p>
     * Currently supports only keyword, string, color, length and percentage
     * types.
     *
     * @param stringValue       the value to use
     * @param allowableKeywords the keyword mapper, may be <code>null</code> if the
     *                          property does not support keyword type.
     * @param property          the property whose value to set
     * @throws IllegalStateException    if the property supports a type other than
     *                                  the ones above.
     * @throws IllegalArgumentException if the string value cannot be converted
     *                                  to any of the types supported by the property.
     */
    protected void setComputedValue(
            final String stringValue,
            final AllowableKeywords allowableKeywords,
            final StyleProperty property) {
        StyleValue value = null;
        if (stringValue != null) {
            final Set types = property.getStandardDetails().getSupportedTypes();

            if (types.contains(StyleValueType.KEYWORD)) {
                value = toStyleKeyword(allowableKeywords,
                        stringValue.toLowerCase());
            }
            if (value == null && types.contains(StyleValueType.STRING)) {
                value = STYLE_VALUE_FACTORY.getString(null, stringValue);
            }
            if (value == null && types.contains(StyleValueType.COLOR)) {
                value = toStyleColor(stringValue);
            }
            if (value == null && types.contains(StyleValueType.LENGTH)) {
                value = toStyleLength(stringValue.toLowerCase());
            }
            if (value == null && types.contains(StyleValueType.PERCENTAGE)) {
                value = toStylePercentage(stringValue);
            }
            if (value == null) {
                throw new IllegalArgumentException(
                        "Cannot convert value: " + stringValue);
            }
            getStyles().getPropertyValues().
                    setComputedAndSpecifiedValue(property, value);
        }
    }

    /**
     * Converts the specified <code>String</code> value into a
     * <code>StyleKeyword</code> value using the specified keyword mapper.
     * Returns <code>null</code> if the conversion was not successful.
     *
     * @param stringValue the value to convert
     * @return the converted value or <code>null</code>
     * @throws IllegalArgumentException if any of the arguments is
     *                                  <code>null</code>
     */
    private StyleKeyword toStyleKeyword(
            final AllowableKeywords keywordMapper,
            final String stringValue) {
        if (stringValue == null || keywordMapper == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        return keywordMapper.getKeyword(stringValue);
    }

    /**
     * Converts the specified <code>String</code> value into a
     * <code>StyleColor</code> value.
     * Returns <code>null</code> if the conversion was not successful.
     *
     * @param stringValue the value to convert, cannot be <code>null</code>
     * @return the converted value or <code>null</code>
     * @throws IllegalArgumentException if the argument is <code>null</code>
     */
    private StyleColor toStyleColor(final String stringValue) {
        if (stringValue == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }

        StyleColor value = StyleColorNames.getColorByName(
                stringValue.toLowerCase());
        if (value != null) {
            return value;
        }

        // check if it is a hex colour value
        if (stringValue.length() == 7 && stringValue.charAt(0) == '#') {
            try {
                int red = Integer.parseInt(stringValue.substring(1, 3), 16);
                int green = Integer.parseInt(stringValue.substring(3, 5), 16);
                int blue = Integer.parseInt(stringValue.substring(5), 16);

                final int rgb = (red << 16) + (green << 8) + blue;
                return STYLE_VALUE_FACTORY.getColorByRGB(null, rgb);
            } catch (NumberFormatException e) {
                NumberFormatException e2 =
                        new NumberFormatException(
                                "tried to parse color " + stringValue);
                logger.error("unexpected-exception", e2);
            }
        }

        // invalid color values are ignored
        return null;
    }

    /**
     * Converts the specified <code>String</code> value into a
     * <code>StyleLength</code> value.
     * Returns <code>null</code> if the conversion was not successful.
     *
     * @param stringValue the value to convert, cannot be <code>null</code>
     * @return the converted value or <code>null</code>
     * @throws IllegalArgumentException if the argument is <code>null</code>
     */
    private StyleLength toStyleLength(final String stringValue) {
        if (stringValue == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }
        final Matcher matcher = LENGTH_PATTERN.matcher(stringValue);
        if (matcher.matches()) {
            final double value = Double.parseDouble(matcher.group(1));
            LengthUnit unit = LengthUnit.getUnitByName(matcher.group(2));
            return STYLE_VALUE_FACTORY.getLength(null, value, unit);
        }
        // invalid length values are ignored
        return null;
    }

    /**
     * Converts the specified <code>String</code> value into a
     * <code>StylePercentage</code> value.
     * Returns <code>null</code> if the conversion was not successful.
     *
     * @param stringValue the value to convert, cannot be <code>null</code>
     * @return the converted value or <code>null</code>
     * @throws IllegalArgumentException if the argument is <code>null</code>
     */
    private StylePercentage toStylePercentage(final String stringValue) {
        if (stringValue == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }

        if (stringValue.endsWith("%")) {
            try {
                final double value = Double.parseDouble(
                        stringValue
                                .substring(0, stringValue.length() - 1).trim());
                return STYLE_VALUE_FACTORY.getPercentage(null, value);
            } catch (NumberFormatException e) {
                // do nothing
            }
        }

        // invalid percentage values are ignored
        return null;
    }

    /**
     * Set the align property.
     *
     * @param align The new value of the align property.
     */
    public void setAlign(final String align) {
        setComputedValue(
                align,
                TextAlignKeywords.getDefaultInstance(),
                StylePropertyDetails.TEXT_ALIGN);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 07-Nov-05	10173/1	emma	VBM:2005103107 Forward port: Fixes to correctly apply styles to various selectors

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
