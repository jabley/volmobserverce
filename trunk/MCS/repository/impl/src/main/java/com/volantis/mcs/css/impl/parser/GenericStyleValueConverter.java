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

package com.volantis.mcs.css.impl.parser;

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
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.StyleTranscodableURI;

/**
 * Takes a StyleValue created by the parser and maps it to a more specific
 * but still generic style value.
 */
public class GenericStyleValueConverter
    implements StyleValueVisitor {

    /**
     * The factory to use to create style values.
     */
    private final StyleValueFactory styleValueFactory =
            StyleValueFactory.getDefaultInstance();

    /**
     * The resulting converted value.
     */
    private StyleValue converted;

    /**
     * Perform generic conversions on the value.
     *
     * @param value The value to convert.
     *
     * @return The converted value.
     */
    public StyleValue convert(StyleValue value) {
        this.converted = value;
        value.visit(this, null);
        return converted;
    }

    /**
     * Pass the value straight through.
     *
     * @param value The value to pass through unchanged.
     */
    private void passThrough(StyleValue value) {
        converted = value;
    }

    // Javadoc inherited.
    public void visit(StyleAngle value, Object object) {
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StyleColorName value, Object object) {
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StyleColorPercentages value, Object object) {
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StyleColorRGB value, Object object) {
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StyleComponentURI value, Object object) {
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StyleTranscodableURI value, Object object) {
        passThrough(value);
    }

    public void visit(StyleFrequency value, Object object) {
        passThrough(value);
    }

    public void visit(StyleFunctionCall value, Object object) {
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StyleIdentifier value, Object object) {
        String identifier = value.getName();

        // All properties can support inherit.
        if (identifier.equals("inherit")) {
            passThrough(styleValueFactory.getInherit());
            return;
        }

        passThrough(value);
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
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StyleList value, Object object) {
        throw new IllegalStateException("List value unexpected");
    }

    // Javadoc inherited.
    public void visit(StyleNumber value, Object object) {
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StylePair value, Object object) {
        throw new IllegalStateException("Pair value unexpected");
    }

    // Javadoc inherited.
    public void visit(StylePercentage value, Object object) {
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StyleString value, Object object) {
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StyleTime value, Object object) {
        passThrough(value);
    }

    public void visit(CustomStyleValue value, Object object) {
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StyleURI value, Object object) {
        passThrough(value);
    }

    // Javadoc inherited.
    public void visit(StyleUserAgentDependent value, Object object) {
        throw new IllegalStateException("User agent dependent value unexpected");
    }

    // Javadoc inherited.
    public void visit(StyleFraction value, Object object) {
        passThrough(value);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
