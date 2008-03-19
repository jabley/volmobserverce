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
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.styling.expressions.EvaluationContext;
import com.volantis.styling.impl.functions.counter.CounterFormatter;
import com.volantis.styling.impl.functions.counter.CounterFormatterSelector;
import com.volantis.styling.impl.functions.counter.DefaultCounterFormatterSelector;

import java.util.List;

/**
 * Function for retrieving counter values.
 */
public class CounterFunction
        extends AbstractCounterFunction {

    /**
     * Initialise.
     *
     * @param formatterSelector The format selector.
     */
    public CounterFunction(CounterFormatterSelector formatterSelector) {
        super(formatterSelector);
    }

    /**
     * Initialise.
     */
    public CounterFunction() {
        this(new DefaultCounterFormatterSelector());
    }

    /**
     * Execute the counter() function.
     *
     * <p>The expected style values passed by the caller are:
     * <ol>
     * <li>A {@link StyleString} containing the counter name</li>
     * <li>A {@link com.volantis.mcs.themes.StyleKeyword} specifying the list
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
        if (styleValues == null || styleValues.size() < 1 ||
                styleValues.size() > 2) {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "styling-function-argument-count"));
        }

        String counterName = getCounterName(functionName, styleValues, 0);

        StyleKeyword formatStyle = ListStyleTypeKeywords.DECIMAL;
        if (styleValues.size() == 2) {
            formatStyle = getArgumentAsKeyword(functionName, styleValues, 1,
                    ALLOWABLE_FORMAT_STYLES);
        }

        int counterValue = context.getCounterValue(counterName);

        CounterFormatter formatter = formatterSelector.selectFormatter(
                formatStyle, counterValue);

        StyleValue result = formatter.formatAsStyleValue(formatStyle, counterValue);

        return result;
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
