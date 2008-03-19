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

package com.volantis.mcs.css.impl.parser.functions;

import com.volantis.mcs.css.impl.parser.CSSParserMessages;
import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.StyleNumber;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.model.validation.SourceLocation;

import java.util.List;

/**
 * A function parser that converts function calls of the form rgb(r,g,b) into
 * the appropriate style color.
 */
public class RGBFunctionParser
     implements FunctionParser {

    // Javadoc inherited.
    public StyleValue parse(
            ParserContext context, SourceLocation location, String name,
            List arguments) {

        int argumentCount = arguments.size();
        if (argumentCount != 3) {
            context.addDiagnostic(CSSParserMessages.WRONG_NUMBER_ARGUMENTS,
                               new Object[]{
                                   name,
                                   new Integer(3),
                                   new Integer(argumentCount)
                               });
        }

        StyleValue redValue;
        StyleValue greenValue;
        StyleValue blueValue;

        redValue = argumentCount > 0 ? (StyleValue) arguments.get(0) : null;
        greenValue = argumentCount > 1 ? (StyleValue) arguments.get(1) : null;
        blueValue = argumentCount > 2 ? (StyleValue) arguments.get(2) : null;

        int red;
        int green;
        int blue;

        StylePercentage percentage;
        StyleNumber number;
        int numberCount = 0;
        int percentageCount = 0;

        // Process the red component.
        if (redValue instanceof StyleNumber) {
            number = (StyleNumber) redValue;
            numberCount += 1;
            red = getColorComponent(number);
        } else if (redValue instanceof StylePercentage) {
            percentage = (StylePercentage) redValue;
            percentageCount += 1;
            red = getColorComponent(percentage);
        } else {
            red = 0;
        }

        // Process the green component.
        if (greenValue instanceof StyleNumber) {
            number = (StyleNumber) greenValue;
            numberCount += 1;
            green = getColorComponent(number);
        } else if (greenValue instanceof StylePercentage) {
            percentage = (StylePercentage) greenValue;
            percentageCount += 1;
            green = getColorComponent(percentage);
        } else {
            green = 0;
        }

        // Process the blue component.
        if (blueValue instanceof StyleNumber) {
            number = (StyleNumber) blueValue;
            numberCount += 1;
            blue = getColorComponent(number);
        } else if (blueValue instanceof StylePercentage) {
            percentage = (StylePercentage) blueValue;
            percentageCount += 1;
            blue = getColorComponent(percentage);
        } else {
            blue = 0;
        }

        if (numberCount != 3 && percentageCount != 3) {
            context.addDiagnostic(CSSParserMessages.RGB_EXPECTED_ARGS_ALL_SAME_TYPE, null);
        }

        return StyleValueFactory.getDefaultInstance()
                .getColorByPercentages(location, red, green, blue);
    }

    /**
     * Get the color component as a percentage from a number.
     *
     * @param number The number value.
     *
     * @return The color component.
     */
    private int getColorComponent(StyleNumber number) {
        double d = number.getNumber();

        if (d < 0) {
            return 0;
        } else if (d > 255) {
            return 100;
        }
        return (int) (d / 2.55);
    }

    /**
     * Get the color component as a percentage from a percentage.
     *
     * @param percentage The percentage value.
     *
     * @return The color component.
     */
    private int getColorComponent(StylePercentage percentage) {
        double d = percentage.getPercentage();

        if (d < 0) {
            d = 0;
        } else if (d > 100) {
            d = 100;
        }

        return (int) d;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
