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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.properties.StyleProperty;

/**
 * Represents HTML size attribute (width or height), or CSS style property.
 */
abstract class HTMLSize {

    private final double number;
    private final LengthUnit unit;

    /**
     * Default constructor.
     *
     * @param number numerical size value
     * @param unit the length unit
     */
    private HTMLSize(double number, LengthUnit unit) {
        this.number = number;
        this.unit = unit;
    }

    /**
     * Factory method for creating an instance of the HTMLSize class.
     *
     * @param element element from which size should be extracted
     * @param attributeName name of the size attribute ("width" or "height")
     * @param property style property holding the size value
     * @return new HTMLSize instance
     */
    public static HTMLSize getHTMLSize(Element element, String attributeName,
                                       StyleProperty property) {

        String attributeValue = element.getAttributeValue(attributeName);
        if (attributeValue != null) {
            int length = attributeValue.length();
            if (length == 0) {
                throw new IllegalStateException(
                        "Shouldn't have empty attributeValue for '"
                                + attributeName + "'");
            }

            boolean percent = (attributeValue.charAt(length - 1) == '%');
            String numberStr =
                    percent ? attributeValue.substring(0, length - 1)
                            : attributeValue;
            double number = Double.parseDouble(numberStr);
            LengthUnit unit =
                    percent ? LengthUnit.PC : LengthUnit.PX;
            return new HTMLSizeAsAttribute(number, unit, attributeName);
        } else {
            StyleValue styleValue = element.getStyles().getPropertyValues()
                    .getStyleValue(property);
            if (styleValue instanceof StyleLength) {
                StyleLength length = (StyleLength) styleValue;
                double number = length.getNumber();
                if (number > 0) {
                    return new HTMLSizeAsStyle(number, length.getUnit(),
                            property);
                }
            } else if (styleValue instanceof StylePercentage) {
                StylePercentage percentage = (StylePercentage) styleValue;
                double number = percentage.getPercentage();
                if (number > 0) {
                    return new HTMLSizeAsStyle(number, LengthUnit.PC,
                            property);
                }
            }
        }

        return null;
    }

    // javadoc unnecessary
    public final boolean isPercentage() {
        return unit == LengthUnit.PC;
    }

    // javadoc unnecessary
    public final double getNumber() {
        return number;
    }

    // javadoc unnecessary
    protected final LengthUnit getUnit() {
        return unit;
    }

    /**
     * Updates the size of the element by applying new size value.
     *
     * @param element element to be updated
     * @param newSize new element size
     */
    public abstract void updateElementSize(Element element, double newSize);

    public static class HTMLSizeAsAttribute extends HTMLSize {

        private final String attributeName;

        /**
         * Creates an instance of this class.
         *
         * @param number numerical size value
         * @param unit the length unit
         * @param attributeName name of the size attribute ("width" or "height")
         */
        private HTMLSizeAsAttribute(double number, LengthUnit unit,
                        String attributeName) {
            super(number, unit);
            this.attributeName = attributeName;
        }

        // javadoc inherited
        @Override
        public void updateElementSize(Element element, double newSize) {
            LengthUnit unit = getUnit();

            if (unit == LengthUnit.PC) {
                element.setAttribute(attributeName,
                        Integer.toString((int) newSize) + "%");
            } else if (unit == LengthUnit.PX) {
                element.setAttribute(attributeName,
                        Integer.toString((int) newSize));
            } else {
                // we are unable to set attribute with other unit types
                element.removeAttribute(attributeName);
            }
        }
    }

    public static class HTMLSizeAsStyle extends HTMLSize {

        private final StyleProperty styleProperty;

        /**
         * Creates an instance of this class.
         *
         * @param number numerical size value
         * @param unit the length unit
         * @param styleProperty style property holding the size value
         */
        private HTMLSizeAsStyle(double number, LengthUnit unit,
                StyleProperty styleProperty) {
            super(number, unit);
            this.styleProperty = styleProperty;
        }

        // javadoc inherited
        @Override
        public void updateElementSize(Element element, double newSize) {
            LengthUnit unit = getUnit();
            StyleValue value;

            if (unit == LengthUnit.PC) {
                value = StyleValueFactory.getDefaultInstance().getPercentage(
                                null, newSize);
            } else {
                value = StyleValueFactory.getDefaultInstance().getLength(null,
                                newSize, unit);
            }
            element.getStyles().getPropertyValues().setComputedValue(
                    styleProperty, value);
        }
    }
}
