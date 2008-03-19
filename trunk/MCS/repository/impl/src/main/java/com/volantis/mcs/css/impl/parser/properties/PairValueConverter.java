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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.css.impl.parser.Separator;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * A converter that can handle a pair of values where the second value is
 * optional.
 */
public class PairValueConverter implements ValueConverter {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    PairValueConverter.class);

    /**
     * The object to use to create style values.
     */
    private static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    /**
     * The converter for the first component of the pair.
     */
    private final PairComponentValueHandler firstConverter;

    /**
     * Any separator which may appear between the pair of values. For StylePairs,
     * this will be null but for StyleFractions it will be Separator.SLASH.
     */
    private Separator separator = null;

    /**
     * The converter for the second component of the pair.
     */
    private final PairComponentValueHandler secondConverter;

    /**
     * The "pair" {@link StyleValueType} which this converter is configured to
     * handle. This should be either {@link StyleValueType.FRACTION} or
     * {@link StyleValueType.PAIR}.
     */
    private final StyleValueType type;

    /**
     * Initialise.
     *
     * @param firstConverter  The converter for the first component of the pair.
     * @param secondConverter The converter for the second component of the
     *                        pair.
     */
    public PairValueConverter(
            PairComponentValueHandler firstConverter,
            PairComponentValueHandler secondConverter) {

        this (firstConverter, secondConverter, null, StyleValueType.PAIR);
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param firstConverter    converter for the first component of the pair.
     * @param secondConverter   converter for the second component of the pair.
     * @param separator         string to render between the components
     * @param type              type of the "pair" that will be converted
     */
    public PairValueConverter(PairComponentValueHandler firstConverter,
                              PairComponentValueHandler secondConverter,
                              Separator separator, StyleValueType type) {
        this.firstConverter = firstConverter;
        this.secondConverter = secondConverter;
        this.separator = separator;
        this.type = type;
    }

    // Javadoc inherited.
    public StyleValue convert(ParserContext context, StyleValueIterator iterator) {

        StyleValue first = null;
        StyleValue second = null;

        first = firstConverter.convert(context, iterator);
        if (first == null) {
            return null;
        }

        // If there are any more left and a separator should appear, verify
        // that it is correct.
        if (iterator.hasMore() && separator != null) {
            iterator.separator(separator);
        }

        // If there are any more left then try the second converter.
        if (iterator.hasMore()) {
            second = secondConverter.convert(context, iterator);
        }

        // If the second could not be converted then assume it is a different
        // value and use it's initial value.
        if (second == null) {
            second = secondConverter.initial(first);
        }

        return getStyleValue(type, first, second);
    }

    private StyleValue getStyleValue(
            StyleValueType type, StyleValue first, StyleValue second) {
        StyleValue result;
        if (StyleValueType.PAIR.equals(type)) {
            result = STYLE_VALUE_FACTORY.getPair(first, second);
        } else if (StyleValueType.FRACTION.equals(type)) {
            result = STYLE_VALUE_FACTORY.getFraction(first, second);
        } else {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "style-value-type-invalid", new Object[]{type,
                    "either a StylePair or StyleFraction is required"}));
        }
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
