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
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.properties.BackgroundXPositionKeywords;
import com.volantis.mcs.themes.properties.BackgroundYPositionKeywords;

import java.util.HashSet;
import java.util.Set;

/**
 * Parser for the background-position property.
 * <p/>
 * <p>This is different from the other properties that can accept pairs in a
 * number of ways that require it to have its own class.</p>
 * <ul>
 * <li>If both values are keywords then the order is irrelevant.</li>
 * <li>If one of the components is not present then the initial value for the
 * other depends on whether the component that is present is a keyword, or
 * another value.</li>
 * </ul>
 */
public class BackgroundPositionParser
        extends AbstractSinglePropertyParser
        implements ShorthandValueHandler {

    /**
     * The handler for the X component part of the pair.
     */
    private static final PairComponentValueHandler X_HANDLER_COMPONENT;

    /**
     * The handler for the Y component part of the pair.
     */
    private static final PairComponentValueHandler Y_HANDLER_COMPONENT;

    static {
        AllowableKeywords allowableKeywords;
        Set supportedTypes;
        StyleKeyword initialKeyword;
        StyleValue initialPercentage;

        // The initial value for either component as a percentage.
        initialPercentage = STYLE_VALUE_FACTORY.getPercentage(null, 50);

        // Create the handler for the X component of the pair.
        supportedTypes = new HashSet();
        supportedTypes.add(StyleValueType.LENGTH);
        supportedTypes.add(StyleValueType.PERCENTAGE);
        supportedTypes.add(StyleValueType.KEYWORD);

        allowableKeywords = BackgroundXPositionKeywords.getDefaultInstance();

        // The initial value for the X component as a keyword.
        initialKeyword = BackgroundXPositionKeywords.CENTER;

        // Create the component handler.
        X_HANDLER_COMPONENT = new PositionComponentHandler(supportedTypes,
                                                           allowableKeywords,
                                                           initialKeyword,
                                                           initialPercentage);

        // Create the handler for the Y component of the pair.
        supportedTypes = new HashSet();
        supportedTypes.add(StyleValueType.LENGTH);
        supportedTypes.add(StyleValueType.PERCENTAGE);
        supportedTypes.add(StyleValueType.KEYWORD);

        allowableKeywords = BackgroundYPositionKeywords.getDefaultInstance();

        initialKeyword = BackgroundYPositionKeywords.CENTER;

        Y_HANDLER_COMPONENT = new PositionComponentHandler(supportedTypes,
                                                           allowableKeywords,
                                                           initialKeyword,
                                                           initialPercentage);
    }

    /**
     * Initialise.
     */
    public BackgroundPositionParser() {
        super(StylePropertyDetails.BACKGROUND_POSITION, 2);
    }

    // Javadoc inherited.
    public StyleValue convert(
            ParserContext context,
            StyleValueIterator iterator) {

        StyleValue xPosition = null;
        StyleValue yPosition = null;

        // Try and match them both.
        StyleValue firstAsX = X_HANDLER_COMPONENT.convert(context, iterator);
        StyleValue firstAsY = Y_HANDLER_COMPONENT.convert(context, iterator);
        StyleValue secondAsX;
        StyleValue secondAsY;

        // If both of them matched then consume it and try the next one.
        if (firstAsX != null && firstAsY != null) {
            iterator.consume();

            if (iterator.hasMore()) {
                secondAsX = X_HANDLER_COMPONENT.convert(context, iterator);
                secondAsY = Y_HANDLER_COMPONENT.convert(context, iterator);

                if (secondAsX != null && secondAsY != null) {
                    // The second value matched both so maintain order.
                    xPosition = firstAsX;
                    yPosition = secondAsY;

                    // Consume the value.
                    iterator.consume();
                } else if (secondAsX != null) {
                    // The second did not match Y so the first value must be Y
                    // and the second value must be X.
                    xPosition = secondAsX;
                    yPosition = firstAsY;

                    // Consume the value.
                    iterator.consume();
                } else if (secondAsY != null) {
                    // The second did not match X so the first value must be X
                    // and the second value must be Y.
                    xPosition = firstAsX;
                    yPosition = secondAsY;

                    // Consume the value.
                    iterator.consume();
                } else {
                    // The second value did not match any of them so ignore it
                    // as it may be the next value in the shorthand and assume
                    // that the first value is the x position.
                    xPosition = firstAsX;
                }
            } else {
                // There are no more values so assume that the first value is
                // X and the Y value should default to its initial value.
                xPosition = firstAsX;
            }
        } else if (firstAsX != null) {

            xPosition = firstAsX;

            // The first only matched X so consume it and then try the second
            // value (if any) as Y.
            iterator.consume();

            if (iterator.hasMore()) {
                secondAsY = Y_HANDLER_COMPONENT.convert(context, iterator);
                if (secondAsY != null) {
                    yPosition = secondAsY;

                    // Consume the value.
                    iterator.consume();
                }
            }
        } else if (firstAsY != null) {

            yPosition = firstAsY;

            // The first only matched X so consume it and then try the second
            // value (if any) as Y.
            iterator.consume();

            if (iterator.hasMore()) {
                secondAsX = X_HANDLER_COMPONENT.convert(context, iterator);
                if (secondAsX != null) {
                    xPosition = secondAsX;

                    // Consume the value.
                    iterator.consume();
                }
            }
        } else {
            return null;
        }

        // Set any unset parts to their initial values. If the set value is
        // a keyword then the initial value should also be a keyword, otherwise
        // it is a percentage.
        if (xPosition == null) {
            xPosition = X_HANDLER_COMPONENT.initial(yPosition);
        }
        if (yPosition == null) {
            yPosition = Y_HANDLER_COMPONENT.initial(xPosition);
        }

        StyleValue pair = STYLE_VALUE_FACTORY.getPair(xPosition, yPosition);

        return pair;
    }

    /**
     * Extend the simple overriding the initial value to return either a
     * keyword or percentage depending on the other value.
     */
    private static class PositionComponentHandler
            extends SimplePairComponentValueHandler {

        /**
         * The value to use when the other value is a keyword.
         */
        private final StyleKeyword initialKeyword;

        /**
         * The value to use when the other value is a percentage.
         */
        private final StyleValue initialPercentage;

        public PositionComponentHandler(
                Set supportedTypes, AllowableKeywords allowableKeywords,
                StyleKeyword initialKeyword,
                StyleValue initialPercentage) {
            super(supportedTypes, allowableKeywords, null);
            this.initialKeyword = initialKeyword;
            this.initialPercentage = initialPercentage;
        }

        // Javadoc inherited.
        public StyleValue convert(
                ParserContext context, StyleValueIterator iterator) {

            // This does not consume the value as although it may be valid
            // there is an ambiguity in the input that means that it is not
            // possible to determine whether this value should be used for
            // this component until both components have been checked.

            return context.convert(supportedTypes, allowableKeywords, iterator.value());
        }

        // Javadoc inherited.
        public StyleValue initial(StyleValue other) {
            if (other instanceof StyleKeyword) {
                return initialKeyword;
            } else {
                return initialPercentage;
            }
        };
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
