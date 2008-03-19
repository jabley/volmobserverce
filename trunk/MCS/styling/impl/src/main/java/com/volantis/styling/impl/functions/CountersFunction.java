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
package com.volantis.styling.impl.functions;

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.styling.expressions.EvaluationContext;
import com.volantis.styling.impl.functions.counter.CounterFormatterSelector;
import com.volantis.styling.impl.functions.counter.DefaultCounterFormatterSelector;
import com.volantis.styling.impl.functions.counter.CounterFormatter;

import java.util.List;

/**
 * Function for retrieving all in-scope values of a counter.
 */
public class CountersFunction extends AbstractCounterFunction {
    
    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Initialise.
     *
     * @param formatterSelector The format selector.
     */
    public CountersFunction(CounterFormatterSelector formatterSelector) {
        super(formatterSelector);
    }

    /**
     * Initialise.
     */
    public CountersFunction() {
        this(new DefaultCounterFormatterSelector());
    }

    /**
     * Execute the counters() function.
     *
     * <p>The expected style values passed by the caller are:
     * <ol>
     * <li>A {@link StyleIdentifier} containing the counter name</li>
     * <li>A {@link StyleString} containing the separator for the counters</li>
     * <li>A {@link StyleKeyword} specifying the list
     *     rendering style</li>
     * </ol>
     * </p>
     *
     * @param context The evaluation context
     * @param functionName The name of the function being called
     * @param styleValues The style value arguments to this function
     * @return A StyleValue representing the value of the function
     */
    public StyleValue evaluate(EvaluationContext context, String functionName,
                               List styleValues) {
        if (styleValues == null || styleValues.size() < 2 ||
                styleValues.size() > 3) {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "styling-function-argument-count"));
        }

        String counterName = getCounterName(functionName, styleValues, 0);

        String separator = getArgumentAsString(functionName, styleValues, 1);

        StyleKeyword formatStyle = ListStyleTypeKeywords.DECIMAL;
        if (styleValues.size() == 3) {
            formatStyle = getArgumentAsKeyword(functionName, styleValues, 2,
                    ALLOWABLE_FORMAT_STYLES);
        }


        int[] counterValues = context.getCounterValues(counterName);

        StringBuffer valuesString = new StringBuffer();

        for (int i = 0; i < counterValues.length; i++) {
            // TODO later support non-decimal list styles
            int counterValue = counterValues[i];

            CounterFormatter formatter = formatterSelector.selectFormatter(
                    formatStyle, counterValue);

            String value = formatter.formatAsString(formatStyle, counterValue);
            valuesString.append(value);
            if (i < (counterValues.length - 1)) {
                valuesString.append(separator);
            }
        }

        return STYLE_VALUE_FACTORY.getString(null, valuesString.toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 30-Sep-05	9635/1	adrianj	VBM:2005092817 Counter functions for CSS

 ===========================================================================
*/
