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

import com.volantis.mcs.css.impl.parser.CSSParserMessages;
import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.CustomStyleValue;
import com.volantis.mcs.themes.StyleAngle;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorPercentages;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleFraction;
import com.volantis.mcs.themes.StyleFrequency;
import com.volantis.mcs.themes.StyleFunctionCall;
import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleInvalid;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleNumber;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleTime;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.mcs.themes.StyleUserAgentDependent;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.StyleKeywords;

import java.util.Set;

/**
 * Takes a StyleValue created by the parser and maps it to an appropriate
 * property specific style value.
 * <p/>
 * <p>A new instance of this is created for each parser context instance.</p>
 *
 * <p>This is given values from the CSS parser and converts them into property
 * specific values. The CSS parser does not generate all the different types of
 * values, e.g. lists, etc. so this class simply throws an exception if it
 * receives one unexpectedly.</p> 
 */
public class StyleValueConverter
        implements StyleValueVisitor {

    /**
     * The context that owns this instance.
     */
    private final ParserContext context;

    /**
     * The factory to use to create the values.
     */
    private final StyleValueFactory styleValueFactory;

    /**
     * The set of supported types.
     */
    private Set supportedTypes;

    /**
     * The optional allowableKeywords to use for keywords.
     */
    private AllowableKeywords allowableKeywords;

    /**
     * The value that is converted.
     */
    private StyleValue converted;

    /**
     * Initialise.
     *
     * @param context           The owning context.
     * @param styleValueFactory The factory for style values.
     */
    public StyleValueConverter(
            ParserContext context,
            StyleValueFactory styleValueFactory) {
        this.context = context;
        this.styleValueFactory = styleValueFactory;
    }

    /**
     * Check that the specified generic value is valid for this property and
     * if necessary convert it into a property specific value.
     *
     * @param supportedTypes The set of supported types.
     * @param allowableKeywords         The optional allowableKeywords.
     * @param value          The value to check and convert.
     * @return The converted value, or null if it was not valid.
     */
    public StyleValue convert(
            Set supportedTypes, AllowableKeywords allowableKeywords, StyleValue value) {
        this.supportedTypes = supportedTypes;
        this.allowableKeywords = allowableKeywords;
        this.converted = null;
        value.visit(this, null);
        return converted;
    }

    /**
     * Pass through if the type is supported.
     *
     * @param value The value to check.
     */
    private void passThroughIfSupported(StyleValue value) {
        if (supportedTypes.contains(value.getStyleValueType())) {
            converted = value;
        }
    }

    // Javadoc inherited.
    public void visit(StyleAngle value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleColorName value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleColorPercentages value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleColorRGB value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleComponentURI value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleTranscodableURI value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleFraction value, Object object) {
        throw new IllegalStateException("Fraction value unexpected");
    }

    // Javadoc inherited.
    public void visit(StyleFrequency value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleFunctionCall value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleIdentifier value, Object object) {
        String identifier = value.getName();

        // All properties can support inherit.
        if (identifier.equals("inherit")) {
            converted = styleValueFactory.getInherit();
            return;
        }

        // Check to see whether this identifier is a color name.
        if (supportedTypes.contains(StyleValueType.COLOR)) {
            StyleColorName color = StyleColorNames.getColorByName(identifier);
            if (color != null) {
                converted = color;
            }
        }

        // Check to see whether this identifier is a keyword.
        if (supportedTypes.contains(StyleValueType.KEYWORD)) {
            StyleKeyword keyword = StyleKeywords.getKeywordByName(identifier);
            if (allowableKeywords.isValidKeyword(keyword)) {
                converted = keyword;
                return;
            }
        }

        // Check to see whether this identifier is an identifier.
        if (supportedTypes.contains(StyleValueType.IDENTIFIER)) {
            converted = value;
            return;
        }
    }

    // Javadoc inherited.
    public void visit(StyleInteger value, Object object) {
        throw new IllegalStateException("Integer value unexpected");
    }

    // Javadoc inherited.
    public void visit(StyleInherit value, Object object) {
        throw new IllegalStateException("Inherit value unexpected");
    }

    // Javadoc inherited.
    public void visit(StyleInvalid value, Object object) {
        throw new IllegalStateException("Invalid value unexpected");
    }

    // Javadoc inherited.
    public void visit(StyleKeyword value, Object object) {
        throw new IllegalStateException("Keyword value unexpected");
    }

    // Javadoc inherited.
    public void visit(StyleLength value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleList value, Object object) {
        throw new IllegalStateException("List value unexpected");
    }

    // Javadoc inherited.
    public void visit(StyleNumber value, Object object) {

        // Check to see whether the number is actually a keyword.
        double number = value.getNumber();
        if (supportedTypes.contains(StyleValueType.KEYWORD)) {
            String identifier = Integer.toString((int) number);
            StyleKeyword keyword = allowableKeywords.getKeyword(identifier);
            if (keyword != null) {
                converted = keyword;
                return;
            }
        }

        // Check to see whether the number should be treated as an integer.
        if (supportedTypes.contains(StyleValueType.INTEGER)) {
            int integer = (int) number;
            if (integer != number) {
                context.addDiagnostic(
                        CSSParserMessages.EXPECTED_INTEGER_FOUND_NUMBER,
                        new Object[]{
                            context.getCurrentPropertyName(),
                            new Double(number),
                        });
            }
            converted = styleValueFactory.getInteger(null, integer);
            return;
        }

        // If this does not support number but it does support length and the
        // value is 0 then treat it as a length.
        if (!supportedTypes.contains(StyleValueType.NUMBER)) {
            // If the number is 0 and the type supports length then treat it as
            // a length.
            if (number == 0.0 && supportedTypes.contains(StyleValueType.LENGTH)) {
                converted = styleValueFactory.getLength(null, number, LengthUnit.PX);
            }
        } else {
            // Pass through.
            converted = value;
        }
    }

    // Javadoc inherited.
    public void visit(StylePair value, Object object) {
        throw new IllegalStateException("Pair value unexpected");
    }

    // Javadoc inherited.
    public void visit(StylePercentage value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleString value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleTime value, Object object) {
        passThroughIfSupported(value);
    }

    public void visit(CustomStyleValue value, Object object) {
        // Always pass through.
        converted = value;
    }

    // Javadoc inherited.
    public void visit(StyleURI value, Object object) {
        passThroughIfSupported(value);
    }

    // Javadoc inherited.
    public void visit(StyleUserAgentDependent value, Object object) {
        throw new IllegalStateException(
                "User agent dependent value unexpected");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 13-Oct-05	9732/1	adrianj	VBM:2005100509 Fixes to Eclipse GUI

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
