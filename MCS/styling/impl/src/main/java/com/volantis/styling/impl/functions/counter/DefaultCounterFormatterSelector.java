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

package com.volantis.styling.impl.functions.counter;

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates the built in formatters.
 *
 * <p>Selects an appropriate selector based on the style and then uses that to
 * select the appropriate selector.</p>
 */
public class DefaultCounterFormatterSelector
        implements CounterFormatterSelector {

    /**
     * A map from list style type to {@link CounterFormatterSelector}.
     */
    private static final Map formatterSelectors;

    static {
        formatterSelectors = new HashMap();
        // TODO later support other list styles
        CounterFormatterSelector decimalSelector = new FixedFormatterSelector(
                DecimalFormatter.getDefaultInstance());
        formatterSelectors.put(ListStyleTypeKeywords.DECIMAL, decimalSelector);
        formatterSelectors.put(ListStyleTypeKeywords.DECIMAL_LEADING_ZERO,
                new FixedFormatterSelector(new DecimalLeadingZeroFormatter()));
        CounterFormatterSelector lowerAlphaLatinSelector =
                new AlphabeticFormatterSelector(new LowerAlphaLatinFormatter());
        formatterSelectors.put(ListStyleTypeKeywords.LOWER_ALPHA,
                lowerAlphaLatinSelector);
        formatterSelectors.put(ListStyleTypeKeywords.LOWER_LATIN,
                lowerAlphaLatinSelector);
        formatterSelectors.put(ListStyleTypeKeywords.LOWER_GREEK,
                new AlphabeticFormatterSelector(new LowerGreekFormatter()));
        CounterFormatterSelector upperAlphaLatinSelector =
                new AlphabeticFormatterSelector(new UpperAlphaLatinFormatter());
        formatterSelectors.put(ListStyleTypeKeywords.UPPER_ALPHA,
                upperAlphaLatinSelector);
        formatterSelectors.put(ListStyleTypeKeywords.UPPER_LATIN,
                upperAlphaLatinSelector);
    }

    // Javadoc inherited.
    public CounterFormatter selectFormatter(
            StyleKeyword formatStyle, int counterValue) {
        CounterFormatterSelector selector = getSelector(formatStyle);
        return selector.selectFormatter(formatStyle, counterValue);
    }

    /**
     * Get the selector for the style.
     *
     * @param style The format style.
     * @return The selector.
     * @throws IllegalArgumentException If the style is not supported.
     */
    private CounterFormatterSelector getSelector(StyleKeyword style) {
        CounterFormatterSelector selector = (CounterFormatterSelector)
                formatterSelectors.get(style);
        if (selector == null) {
            throw new IllegalArgumentException(
                    "Unsupported counter format style - " + style);
        }
        return selector;
    }
}
