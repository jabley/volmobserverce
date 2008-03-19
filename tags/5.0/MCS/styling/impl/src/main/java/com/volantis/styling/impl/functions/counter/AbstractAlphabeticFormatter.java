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
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;

/**
 * Base class for the alphabetic formatters.
 */
public class AbstractAlphabeticFormatter implements CounterFormatter {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * The alphabet.
     */
    private final String[] alphabet;

    /**
     * Initialise.
     *
     * @param alphabet The alphabet to use.
     */
    public AbstractAlphabeticFormatter(String[] alphabet) {
        this.alphabet = alphabet;
    }

    // Javadoc inherited.
    public StyleValue formatAsStyleValue(
            StyleKeyword formatStyle, int counterValue) {

        String string = formatAsString(formatStyle, counterValue);
        return STYLE_VALUE_FACTORY.getString(null, string);
    }

    // Javadoc inherited.
    public String formatAsString(StyleKeyword formatStyle, int counterValue) {
        if (counterValue < 1) {
            throw new IllegalArgumentException("Counter format style " +
                    formatStyle + " can only support values greater than " +
                    "or equal to 1 so " + counterValue + " is out of range");
        }

        int base = alphabet.length;
        StringBuffer buffer = new StringBuffer();
        do {
            // Counter value is 1 based but the alphabet array is zero based
            // so convert it to a zero base value by subtracting 1.
            int zeroBasedCounterValue = (counterValue - 1);

            // Calculate the numeric value of the digit, get the character
            // from the alphabet and insert it at the start of the buffer.
            int digit = zeroBasedCounterValue % base;
            buffer.insert(0, alphabet[digit]);

            // Drop the digit and move onto the next one.
            counterValue = zeroBasedCounterValue / base;
            
        } while (counterValue > 0);

        return buffer.toString();
    }
}
