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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to encapsulate (and make easier to test) the grammar details of the
 * functions defined for CSS property / Element attribute mappings. During
 * conception time, the intent was to allow XPath-like syntax, but the initial
 * implementation in L3 did not require that the full-blown capabilities be
 * delivered. So this is here as a place-holder, if you like.
 *
 * This class is thread-safe and immutable.
 */
public class ExpressionParser {

    private final String cssProperty;

    /**
     * Create a new ExpressionParser with the specified expression.
     *
     * @param expression String, not null.
     * @throws InvalidExpressionException if the expression isn't syntactically
     *                                  valid.
     */
    public ExpressionParser(String expression)
            throws InvalidExpressionException {

        // Pattern is immutable and thread-safe, but Matcher is not, so we
        // create a new one locally each time.
        Matcher matcher = Grammar.GRAMMAR.matcher(expression);

        if (matcher.matches()) {
            Grammar.checkQuotesAreBalanced(matcher);
            this.cssProperty = matcher.group(2);
        } else {
            throw new InvalidExpressionException("Invalid expression: "
                    + expression);
        }
    }

    /**
     * Return the name of the CSS property described in the expression.
     *
     * @return the CSS property name - not null
     */
    public String getCSSProperty() {
        return this.cssProperty;
    }

    /**
     * Utility constant class holding details of the expression grammar.
     */
    private static class Grammar {

        /**
         * For now, we support a very limited GRAMMAR, and use regex to define
         * it. Potentially, this should be defined using ANTLR / JavaCC or
         * similar if it gets more involved. We would at least split out the
         * expression stuff into a separate package / classes.
         */
        private static final Pattern GRAMMAR = Pattern.compile(
                "length" +  // "length"
                        "\\s*" +   // optionally, many space characters
                        "\\(" +     // opening bracket
                        "\\s*" +   // optionally, many space characters
                        "css" +     // "css"
                        "\\s*" +   // optionally, many space characters
                        "\\(" +     // opening bracket
                        "\\s*" +   // optionally, many space characters
                        "('|\")" +  // opening quote - group 1
                        "([a-z\\-]+)" +    // CSS property name - group 2
                        "('|\")" +  // closing quote - group 3
                        "\\s*" +   // optionally, many space characters
                        "\\)" +     // closing bracket
                        "\\s*" +   // optionally, many space characters
                        "," +       // ","
                        "\\s*" +   // optionally, many space characters
                        "('|\")" +  // opening quote - group 4
                        "px" +      // "'px'"
                        "('|\")" +  // closing quote - group 5
                        "\\s*" +   // optionally, many space characters
                        "\\)");     // closing bracket

        /**
         * Data field used for validating the quotes are balanced properly.
         * Each entry is a pair of integers representing the regexp matcher
         * group index that should contain balanced quotes.
         */
        private static final int [] [] QUOTES_INDICES = {{1, 3}, {4, 5}};

        /**
         * Since we are using regexp to validate the expression, this will
         * check that quotes in the expression are balanced. This shows up the
         * shortcomings of defining a grammar in this manner, and it would
         * probably be easier to either implement a recursive-descent parser or
         * use a tool like ANTLR / JavaCC to handle the syntactic check for us.
         * But since the grammar is limited, doing the syntax check at this
         * time isn't too painful.
         *
         * @param matcher
         * @throws IllegalArgumentException if the quotes in the grammar aren't
         *                                  consistently used.
         */
        static void checkQuotesAreBalanced(Matcher matcher) {
            for (int i = 0, n = Grammar.QUOTES_INDICES.length; i < n; ++i) {
                if (!matcher.group(Grammar.QUOTES_INDICES[i][0]).equals(
                        matcher.group(Grammar.QUOTES_INDICES[i][1]))) {
                    throw new IllegalArgumentException(
                            "Invalid expression: " + matcher.group());
                }
            }
        }

    }
}
