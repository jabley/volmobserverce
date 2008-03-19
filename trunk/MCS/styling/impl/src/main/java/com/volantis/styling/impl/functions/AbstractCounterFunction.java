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

package com.volantis.styling.impl.functions;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.styling.expressions.AbstractStylingFunction;
import com.volantis.styling.impl.functions.counter.CounterFormatterSelector;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.Arrays;
import java.util.List;

/**
 * Base class for all counter functions.
 */
public abstract class AbstractCounterFunction
        extends AbstractStylingFunction {

    /**
     * Used to retrieve localized exception messages.
     */
    protected static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(CounterFunction.class);

    /**
     * Allowable keywords for the format style.
     */
    protected static final AllowableKeywords ALLOWABLE_FORMAT_STYLES;

    static {
        List keywords = Arrays.asList(new StyleKeyword[] {
            ListStyleTypeKeywords.DECIMAL,
            ListStyleTypeKeywords.DECIMAL_LEADING_ZERO,
            ListStyleTypeKeywords.LOWER_ALPHA,
            ListStyleTypeKeywords.LOWER_GREEK,
            ListStyleTypeKeywords.LOWER_LATIN,
            ListStyleTypeKeywords.UPPER_ALPHA,
            ListStyleTypeKeywords.UPPER_LATIN,
        });
        ALLOWABLE_FORMAT_STYLES = new AllowableKeywords(keywords);
    }

    /**
     * The {@link CounterFormatterSelector}.
     */
    protected final CounterFormatterSelector formatterSelector;

    /**
     * Initialise.
     *
     * @param formatterSelector The format selector.
     */
    protected AbstractCounterFunction(CounterFormatterSelector formatterSelector) {
        this.formatterSelector = formatterSelector;
    }

    /**
     * Get the counter name.
     *
     * @param functionName The name of the function.
     * @param arguments    The arguments.
     * @param index        The index of the argument.
     * @return The counter name.
     */
    protected String getCounterName(
            String functionName, List arguments, int index) {
        StyleIdentifier identifier = getArgumentAsIdentifier(functionName,
                arguments, index);
        return identifier.getName();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 ===========================================================================
*/
