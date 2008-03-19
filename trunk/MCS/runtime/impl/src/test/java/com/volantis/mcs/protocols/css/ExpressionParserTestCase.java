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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css;

import junit.framework.TestCase;

public class ExpressionParserTestCase extends TestCase {

    public void testExpectedExpression() throws Exception {
        final String cssProperty = "height";
        ExpressionParser parser = new ExpressionParser("length(css('"
                + cssProperty + "'),'px')");
        assertEquals(cssProperty, parser.getCSSProperty());
    }

    public void testInvalidExpressionWithBadCSSLiteral() {
        try {
            new ExpressionParser("length(css-2('width'),'px')");
            fail("expression is invalid");
        } catch (InvalidExpressionException expected) {
            // ignore
        }
    }

    public void testInvalidExpressionWithBadLengthLiteral() throws Exception {
        try {
            new ExpressionParser("length-2(css('width'),'px')");
            fail("expression is invalid");
        } catch (InvalidExpressionException expected) {
            // ignore
        }
    }

    public void testSpacesInExpressionAreOK()
            throws InvalidExpressionException {
        final String cssProperty = "height";
        ExpressionParser parser = new ExpressionParser(
                "length  (   css    (  '" + cssProperty + "'  )  ,  'px'   )");
        assertEquals(cssProperty, parser.getCSSProperty());
    }

    public void testHyphensAreLegal()  throws Exception {
        final String cssProperty = "border-width";
        ExpressionParser parser = new ExpressionParser(
                "length(css('" + cssProperty + "'),'px')");
        assertEquals(cssProperty, parser.getCSSProperty());
    }

    public void testSpacesInCSSPropertyAreIllegal() throws Exception {
        try {
            new ExpressionParser("length(css('"
                    + "  height   " + "'),'px')");
            fail("Spaces aren't allowed in the CSSProperty - " +
                    "only lower-case letters and hyphens");
        } catch (InvalidExpressionException expected) {
            // ignore
        }
    }

    public void testDoubleQuoteIsAcceptable() throws Exception {
        final String cssProperty = "height";
        ExpressionParser parser = new ExpressionParser("length(css(\""
                + cssProperty + "\"),\"px\")");
        assertEquals(cssProperty, parser.getCSSProperty());
    }

    public void testMismatchedQuotesFails() throws Exception {
        try {
            new ExpressionParser("length(css(\"height'),\"px\")");
            fail("Expression is invalid due to mixed use of \" and '");
        } catch (IllegalArgumentException expected) {
            // ignore
        }
    }

    /**
     * Verify that an ExpressionParser created with an empty expression
     * throws an IllegalArgumentException.
     */
    public void testEmptyExpressionFails() {
        try {
            new ExpressionParser("");
            fail("Expression is invalid because it is empty");
        } catch (InvalidExpressionException e) {
            // do nothing - correct behaviour.
        }
    }

    /**
     * Verify that an ExpressionParser created with an null expression
     * throws an NullPointerException.
     */
    public void testNullExpressionFails() throws InvalidExpressionException {
        try {
            new ExpressionParser(null);
            fail("Expression is invalid because it is null");
        } catch (NullPointerException expected) {
            // ignore
        }
    }
}

