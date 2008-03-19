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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xml.validation.sax;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.ResourceBundle;
import java.util.ListResourceBundle;

import junitx.util.PrivateAccessor;

/**
 * Test case for the {@link ParserError} class
 */
public class ParserErrorTestCase extends TestCaseAbstract {

    /**
     * A ResourceBundle that will be used by the various tests
     */
    private ResourceBundle testBundle = new ListResourceBundle() {
        protected Object[][] getContents() {
            return new Object[][]{
                {"one", "The day is {1} (the) {2} of {3} {4}. {Do you agree}?"},
                {"two", "The day is {1}. {Do you agree}?"},
                {"three", "The day is {1} (the) {2} of '{3}' ''{4}''. {a}?"}
            };
        }
    };

    /**
     * Tests the ParseError Constructor
     */
    public void testConstructor() throws Exception {
        // A ParseErrorShould be thrown if the ParseError#N0_ARG value (-1)
        // is provided as the paramIndex argument when the attributes argument
        // is true. This is done as the paramIndex specifies the position
        // of the attribute name in the message.
        try {
            new ParserError(testBundle, "one", "error_key",
                            ParserError.NO_ARG, true);
            fail("ParserError constructor should throw an exception if the " +
                 "parmIndex argument == -1 ant the attribute argument == true");
        } catch (IllegalArgumentException e) {

        }
    }

    /**
     * Tests the {@link ParserError#matchedArgument} with a message that
     * contain some arguments, the paramIndex argument is valid,
     * the mathcing values are single words and a match is expected
     * @throws Exception if an error occurs
     */
    public void testMatchedArgumentMatching() throws Exception {
        doTestMatchedArgument(
                "one",
                2,
                false,
                "The day is Monday (the) 1st of December 2003. {Do you agree}?",
                "December");
    }

    /**
     * Tests the {@link ParserError#matchedArgument} with a message that
     * contains some simple arguments, the paramIndex argument is valid,
     * the mathcing values are single words and a match is NOT expected.
     * @throws Exception if an error occurs
     */
    public void testMatchedArgumentNotMatching() throws Exception {
        doTestMatchedArgument(
                "one",
                2,
                false,
                "fred",
                null);  // null is returned when a match is not found
    }

    /**
     * Tests the {@link ParserError#matchedArgument} with a message that
     * contains a single argument, the paramIndex argument is valid,
     * the mathcing value is a multi word string and a match is expected
     * @throws Exception if an error occurs
     */
    public void testMultiWordMatchedArgument() throws Exception {
        doTestMatchedArgument(
                "two",
                0,
                false,
                "The day is Monday (the) 1st of December 2003. {Do you agree}?",
                "Monday (the) 1st of December 2003");
    }

    /**
     * Tests the {@link ParserError#matchedArgument} with a message that
     * contains some simple arguments, the paramIndex is NO_ARG (-1) and
     * a match is NOT expected.
     * @throws Exception if an error occurs
     */
    public void testNoArgMatchedArgumentNotMatching() throws Exception {
        doTestMatchedArgument(
                "one",
                ParserError.NO_ARG,
                false,
                "This should never match",
                null);
    }

     /**
     * Tests the {@link ParserError#matchedArgument} with a message that
     * contains some simple arguments, the paramIndex is NO_ARG (-1) and
     * a match IS expected.
     * @throws Exception if an error occurs
     */
    public void testMatchedArgumentNoArgMatching() throws Exception {
        doTestMatchedArgument(
                "one",
                ParserError.NO_ARG,
                false,
                "The day is Monday (the) 1st of December 2003. {Do you agree}?",
                "");
    }

    /**
     * Tests the {@link ParserError#matchedArgument} when the message
     * contains some simple arguments and the paramIndex argument is out of
     * bounds
     * @throws Exception if an error occurs
     */
    public void testMatchedArgumentOutOfBoundsParamIndex() throws Exception {
        try {
            doTestMatchedArgument(
                    "one",
                     4,   // out of bounds as message only contains 4 args
                    false,
                    "The day is Monday (the) 1st of December 2003. " +
                        "{Do you agree}?",
                    "this will never be used");
            fail("matchArgument should have thrown a ParserErrorException");
        } catch (ParserErrorException e) {
            // expected condition
        }
    }

    /**
     * Tests the {@link ParserError#matchedArgument} when the message
     * contains some simple arguments and the paramIndex argument is valid and
     * the mathcing values are single words
     * @throws Exception if an error occurs
     */
    public void testMatchedArgumentNoArgParamMatching() throws Exception {
        doTestMatchedArgument(
                "one",
                ParserError.NO_ARG,
                false,
                "The day is Monday (the) 1st of December 2003. {Do you agree}?",
                "");
    }

    /**
     * Helper method that executes the {@link ParserError#matchedArgument}
     * method with a set of provided arguments and checks the returned String
     * against and expected value.
     * @param bundleKey the key into the resource bundle for the message
     * @param paramIndex the parameter index
     * @param attribute true iff and only if the ParserError is an
     *                  attribute error.
     * @param candidate the string that we will try to match the error with
     * @param expected the expected return value
     * @throws Exception if an error occurs
     */
    private void doTestMatchedArgument(String bundleKey,
                                      int paramIndex,
                                      boolean attribute,
                                      String candidate,
                                      String expected) throws Exception {
        // create a ParseError instance
        ParserError error = new ParserError(testBundle,
                                            bundleKey,
                                            "error_key",
                                            paramIndex,
                                            attribute);

        // call the matchedArgument method
        String actual = error.matchedArgument(null, candidate);

        // compare the actual result with the expected
        assertEquals("mathcedArgument produced unexpected result",
                     expected,
                     actual);
    }

    /**
     * Tests that the {@link ParserError#convertToRegExpString} method escapes
     * the characters that have a "special" regular expression meaning
     * @throws Throwable if an error occurs
     */
    public void testEscapingOfRegExChars() throws Throwable {
        doTestConvertToRegExpString(
                -1,
                "hello * \\ ^ $ ? + { }( fred ) *",
                "hello \\* \\\\ \\^ \\$ \\? \\+ \\{ \\}\\( fred \\) \\*");
    }

    /**
     * Tests that the {@link ParserError#convertToRegExpString} method
     * substitutes "{<number>}" strings with .* or (.*), where <number>
     * is any numeric value.
     * @throws Throwable if an error occurs
     */
    public void testParameterSubstitution() throws Throwable {
        doTestConvertToRegExpString(
                1, // the second {<number>} will be (.*)
                "hello {0} this {1} is {2a} {2} {12345o} test",
                "hello .* this (.*) is \\{2a\\} .* \\{12345o\\} test");
    }


    /**
     * Tests that the {@link ParserError#convertToRegExpString} method
     * escapes { and } when enclosed in single quotes. Also tests that
     * a single quote can be escaped by another single quote.
     * @throws Throwable if an error occurs
     */
    public void testParameterSubstitutionEscaping() throws Throwable {
        doTestConvertToRegExpString(
                 1, // the second {<number>} will be (.*)
                "hello '{0}' this ''{1}'' is {2a} {2} {12345o} test",
                "hello \\{0\\} this '.*' is \\{2a\\} (.*) \\{12345o\\} test");
    }

    /**
     * Tests that the {@link ParserError#convertToRegExpString} method
     * escapes { and } when enclosed in single quotes. Explicitly tests
     * that a quote at the end of the message is handled correctly
     * @throws Throwable if an error occurs
     */
    public void testParameterSubstitutionEscapingLastChar() throws Throwable {
        doTestConvertToRegExpString(
                 -1,
                "hello '{0}' this '{1}'",
                "hello \\{0\\} this \\{1\\}");
    }

    /**
     * Tests that the {@link ParserError#convertToRegExpString} method
     * substitutes "{<number>}" strings with .* or (.*), where <number>
     * is any numeric value.
     * @throws Throwable if an error occurs
     */
    public void testSimpleMessageArg() throws Throwable {
        doTestConvertToRegExpString(
                0,
                "{1}",
                "(.*)");
    }

    /**
     * Tests that the {@link ParserError#convertToRegExpString} method
     * substitutes "{<number>}" strings with .* or (.*), where <number>
     * is any numeric value.
     * @throws Throwable if an error occurs
     */
    public void testSimpleMessageNoArg() throws Throwable {
        doTestConvertToRegExpString(
                ParserError.NO_ARG,
                "{1}",
                ".*");
    }

    /**
     * Tests that the {@link ParserError#convertToRegExpString} method
     * substitutes "{<number>}" strings with .* or (.*), where <number>
     * is any numeric value.
     * @throws Throwable if an error occurs
     */
    public void testParameterSubstitutionTrailingBrace() throws Throwable {
        doTestConvertToRegExpString(
                2,
                "} hello {99} this {1} is {a} {3} test {",
                "\\} hello .* this .* is \\{a\\} (.*) test \\{");
    }

    /**
     * Helper method that invokes the {@link ParserError#convertToRegExp}
     * method with the specified parameters and then tests the value returned
     * against an expected value
     * @param paramIndex the paramIndex value
     * @param message the message that will be converted
     * @param expected the expected result
     * @throws Throwable if an error occurs
     */
    private void doTestConvertToRegExpString(int paramIndex,
                                             String message,
                                             String expected) throws Throwable {

        // parameter specifications
        Class[] types = {String.class};
        String[] params = {message};

        // create a parser error
        ParserError parserError = new ParserError(
                testBundle, "one", "error_key", paramIndex, false);

        // invoke the private convertToRegExpString
        String actual = (String) PrivateAccessor.invoke(parserError,
                                                        "convertToRegExpString",
                                                        types,
                                                        params);
        // ensure that result is as expected.
        assertEquals("The convertion failed", expected, actual);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Dec-03	2057/4	doug	VBM:2003112803 Addressed several rework issues

 09-Dec-03	2057/1	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 ===========================================================================
*/
