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
package com.volantis.mcs.utilities;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Locale;

/**
 * This class exercises the StringUtils methods.
 */
public class StringUtilsTestCase extends TestCaseAbstract {

    /**
     * Tests that a string which consists of only ASCII alphanumeric
     * characters is not quoted.
     */
    public void testASCIIAlphanumericOnlyQuoting() throws Exception {
        final String str = "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "0123456789";
        final String expected = str;

        assertTrue("String quoting failed",
                doQuoteIfNeededTest(str, expected));
    }

    /**
     * Tests that a string which uses whitespace is quoted.
     */
    public void testStringWithWhitespaceQuoting() throws Exception {
        final String str = "more than one word";
        final String expected =
                StringUtils.SINGLE_QUOTE +
                str +
                StringUtils.SINGLE_QUOTE;

        assertTrue("String quoting failed",
                doQuoteIfNeededTest(str, expected));
    }

    /**
     * Tests that a string which contains only single quotes is
     * surrounded with double quotes, and no escaping occurs.
     */
    public void testEmbeddedSingleQuoting() throws Exception {
        final String str = "some 'quotable' and 'quoted' words";
        final String expected =
                StringUtils.DOUBLE_QUOTE +
                str +
                StringUtils.DOUBLE_QUOTE;

        assertTrue("String quoting failed",
                doQuoteIfNeededTest(str, expected));
    }

    /**
     * Tests that a string which contains only double quotes is
     * surrounded with single quotes, and no escaping occurs.
     */
    public void testEmbeddedDoubleQuoting() throws Exception {
        final String str = "some \"quotable\" and \"quoted\" words";
        final String expected =
                StringUtils.SINGLE_QUOTE +
                str +
                StringUtils.SINGLE_QUOTE;

        assertTrue("String quoting failed",
                doQuoteIfNeededTest(str, expected));
    }

    /**
     * Tests that a string which contains double and single quotes is
     * surrounded with single quotes, and the embedded single quotes are
     * escaped.
     */
    public void testEmbeddedSingleAndDoubleQuoting() throws Exception {
        final String str = "some \"quotable\" and 'quoted' words";
        final String expected =
                StringUtils.SINGLE_QUOTE +
                "some \"quotable\" and &#39;quoted&#39; words" +
                StringUtils.SINGLE_QUOTE;

        assertTrue("String quoting failed",
                doQuoteIfNeededTest(str, expected));
    }

    /**
     * Tests that a string which is surrounded by first single then double
     * quotes is quoted with single quotes, and the embedded single quotes are
     * escaped.
     */
    public void testSingleThenDoubleSurroundQuoting() throws Exception {
        final String str = "'\"single then double\"'";
        final String expected =
                StringUtils.SINGLE_QUOTE +
                "&#39;\"single then double\"&#39;" +
                StringUtils.SINGLE_QUOTE;

        assertTrue("String quoting failed",
                doQuoteIfNeededTest(str, expected));
    }

    /**
     * Tests that a string which is surrounded by first double then single
     * quotes is quoted with single quotes, and the embedded single quotes are
     * escaped.
     */
    public void testDoubleThenSingleSurroundQuoting() throws Exception {
        final String str = "\"'double then single'\"";
        final String expected =
                StringUtils.SINGLE_QUOTE +
                "\"" + "&#39;" + "double then single&#39;" + "\"" +
                StringUtils.SINGLE_QUOTE;

        assertTrue("String quoting failed",
                doQuoteIfNeededTest(str, expected));
    }

    /**
     * Tests that a string with a leading single then double quote, and no
     * trailing quotes is surrounded by single quotes and the embedded single
     * is escaped.
     */
    public void testLeadingUnbalancedSingleThenDoubleSurroundQuoting()
            throws Exception {
        final String str = "'\"unbalanced";
        final String expected =
                StringUtils.SINGLE_QUOTE +
                "&#39;" + "\"unbalanced" +
                StringUtils.SINGLE_QUOTE;

        assertTrue("String quoting failed",
                doQuoteIfNeededTest(str, expected));
    }

    /**
     * Tests that a string with a leading double then single quote, and no
     * trailing quotes is surrounded by single quotes and the embedded single
     * is escaped.
     */
    public void testLeadingUnbalancedDoubleThenSingleSurroundQuoting()
            throws Exception {
        final String str = "\"'unbalanced";
        final String expected =
                StringUtils.SINGLE_QUOTE +
                "\"" + "&#39;unbalanced" +
                StringUtils.SINGLE_QUOTE;

        assertTrue("String quoting failed",
                doQuoteIfNeededTest(str, expected));
    }

    /**
     * Tests that a string with a trailing double then single quote, and no
     * trailing quotes is surrounded by single quotes and the embedded single
     * is escaped.
     */
    public void testTrailingUnbalancedDoubleThenSingleSurroundQuoting()
            throws Exception {
        final String str = "unbalanced\"'";
        final String expected =
                StringUtils.SINGLE_QUOTE +
                "unbalanced\"&#39;" +
                StringUtils.SINGLE_QUOTE;

        assertTrue("String quoting failed",
                doQuoteIfNeededTest(str, expected));
    }

    /**
     * Tests that a string with a trailing double then single quote, and no
     * trailing quotes is surrounded by single quotes and the embedded single
     * is escaped.
     */
    public void testTrailingUnbalancedSingleThenDoubleSurroundQuoting()
            throws Exception {
        final String str = "unbalanced'\"";
        final String expected =
                StringUtils.SINGLE_QUOTE +
                "unbalanced&#39;\"" +
                StringUtils.SINGLE_QUOTE;

        assertTrue("String quoting failed",
                doQuoteIfNeededTest(str, expected));
    }

    /**
     * Tests that a null string gives a null result.
     */
    public void testNullStringQuoting() throws Exception {
        assertTrue("A null string should give a null result",
                doQuoteIfNeededTest(null, null));
    }

    /**
     * Tests that an empty string gives an empty string result.
     */
    public void testEmptyStringQuoting() throws Exception {
        assertTrue("An empty string should give an empty result",
                doQuoteIfNeededTest("", ""));
    }

    /**
     * Tests that a string which consists of only ASCII alphanumeric
     * characters is unchanged after quote removal.
     */
    public void testQuotedASCIIAlphanumericOnlyRemoval() throws Exception {
        final String str = "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "0123456789 more than one word";
        final String expected = str;

        assertTrue("Quote removal failed",
                doRemoveQuotesTest(str, expected));
    }

    /**
     * Tests that a single-quoted string which uses whitespace has quotes
     * removed.
     */
    public void testSingleQuotedStringWithWhitespaceRemoval()
            throws Exception {
        final String expected = "more than one word";
        final String str =
                StringUtils.SINGLE_QUOTE +
                expected +
                StringUtils.SINGLE_QUOTE;

        assertTrue("Quote removal failed",
                doRemoveQuotesTest(str, expected));
    }

    /**
     * Tests that a double-quoted string which uses whitespace has quotes
     * removed.
     */
    public void testDoubleQuotedStringWithWhitespaceRemoval()
            throws Exception {
        final String expected = "more than one word";
        final String str =
                StringUtils.DOUBLE_QUOTE +
                expected +
                StringUtils.DOUBLE_QUOTE;

        assertTrue("Quote removal failed",
                doRemoveQuotesTest(str, expected));
    }

    /**
     * Tests that a string surrounded with single quotes and with embedded
     * double quotes, has the single quotes removed and the double quotes
     * unchanged.
     */
    public void testSingleQuotedEmbeddedDoubleRemoval() throws Exception {
        final String expected = "some \"quotable\" and \"quoted\" words";
        final String str =
                StringUtils.SINGLE_QUOTE +
                expected +
                StringUtils.SINGLE_QUOTE;

        assertTrue("Quote removal failed",
                doRemoveQuotesTest(str, expected));

    }

    /**
     * Tests that a string surrounded with double quotes and with embedded
     * single quotes, has the double quotes removed and the single quotes
     * unchanged.
     */
    public void testDoubleQuotedEmbeddedSingleRemoval() throws Exception {
        final String expected = "some 'quotable' and 'quoted' words";
        final String str =
                StringUtils.DOUBLE_QUOTE +
                expected +
                StringUtils.DOUBLE_QUOTE;

        assertTrue("Quote removal failed",
                doRemoveQuotesTest(str, expected));
    }

    /**
     * Tests that a string surrounded with single quotes with embedded
     * double and single quotes where the embedded single quotes are
     * escaped, has the surrounding quotes removed and the embedded
     * single quotes unescaped, and the embedded double quotes unchanged.
     */
    public void testSingleQuotedMixedEmbeddedSingleEscaped() throws Exception {
        final String expected = "some \"quotable\" and 'quoted' words";
        final String str =
                StringUtils.SINGLE_QUOTE +
                "some \"quotable\" and &#39;quoted&#39; words" +
                StringUtils.SINGLE_QUOTE;

        assertTrue("Quote removal failed",
                doRemoveQuotesTest(str, expected));
    }

    /**
     * Tests that a string surrounded with double quotes with embedded
     * double and single quotes where the embedded double quotes are
     * escaped, has the surrounding quotes removed and the embedded
     * double quotes unescaped, and the embedded single quotes unchanged.
     */
    public void testDoubleQuotedMixedEmbeddedDoubleEscaped() throws Exception {
        final String expected = "some \"quotable\" and 'quoted' words";
        final String str =
                StringUtils.DOUBLE_QUOTE +
                "some &#34;quotable&#34; and 'quoted' words" +
                StringUtils.DOUBLE_QUOTE;

        assertTrue("Quote removal failed",
                doRemoveQuotesTest(str, expected));
    }

    /**
     * Tests that a string with leading unbalanced quotes (single then double)
     * results in no quote removal.
     */
    public void testLeadingUnbalancedSingleThenDoubleRemoval()
            throws Exception {
        final String str = "'\"unbalanced";
        final String expected = str;

        assertTrue("Quote removal failed",
                doRemoveQuotesTest(str, expected));
    }

    /**
     * Tests that a string with leading unbalanced quotes (double then single)
     * results in no quote removal.
     */
    public void testLeadingUnbalancedDoubleThenSingleRemoval()
            throws Exception {
        final String str = "\"'unbalanced";
        final String expected = str;

        assertTrue("Quote removal failed",
                doRemoveQuotesTest(str, expected));
    }

    /**
     * Tests that a string surrounded by single quotes and with embedded
     * lliteral quotes only has the surrounding quotes removed.
     */
    public void testSingleQuotedUnbalancedMixedLiteralRemoval()
            throws Exception {
        final String expected = "\"'some text";
        final String str =
                StringUtils.SINGLE_QUOTE +
                expected +
                StringUtils.SINGLE_QUOTE;

        assertTrue("Quote removal failed",
                doRemoveQuotesTest(str, expected));
    }

    /**
     * Tests that a string surrounded by single quotes and with embedded
     * lliteral quotes only has the surrounding quotes removed.
     */
    public void testDoubleQuotedUnbalancedMixedLiteralRemoval()
            throws Exception {
        final String expected = "\"'some text";
        final String str =
                StringUtils.DOUBLE_QUOTE +
                expected +
                StringUtils.DOUBLE_QUOTE;

        assertTrue("Quote removal failed",
                doRemoveQuotesTest(str, expected));
    }

    /**
     * Tests that a null string gives a null result.
     */
    public void testNullStringRemoval() throws Exception {
        assertTrue("A null string should give a null result",
                doRemoveQuotesTest(null, null));
    }

    /**
     * Tests that an empty string gives an empty result.
     */
    public void testEmptyStringRemoval() throws Exception {
        assertTrue("An empty string should give an empty result",
                doRemoveQuotesTest("", ""));
    }

    /**
     * Tests that replacement occurs with distinct words.
     */
    public void testMultiWordReplacement() throws Exception {
        final String str = "hallo there Peter and hallo John";
        final String search = "hallo";
        final String replace = "goodbye";
        final String expected = "goodbye there Peter and goodbye John";

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }

    /**
     * Tests that replacement occurs within words.
     */
    public void testWithinWordReplacement() throws Exception {
        final String str = "abqwertycde !qwertyqwerty! fqwertygqwertyh";
        final String search = "qwerty";
        final String replace = "_peter_";
        final String expected =
                "ab_peter_cde !_peter__peter_! f_peter_g_peter_h";

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }

    /**
     * Tests that no replacement occurs when a match isn't found.
     */
    public void testNoMatchReplacement() throws Exception {
        final String str = "xxxxxxxxxx";
        final String search = "z";
        final String replace = "y";
        final String expected = str;

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }

    /**
     * Tests that multiple consecutive matches results in correct
     * removal.
     */
    public void testMultipleConsecutiveMatchReplacement() throws Exception {
        final String str = "yababyabababy";
        final String search = "ab";
        final String replace = null;
        final String expected = "yyy";

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }

    /**
     * Tests that multiple consecutive matches does not remove too much.
     */
    public void testMultipleConsecutiveMatchWithLeftoversReplacement()
            throws Exception {
        final String str = "yxxxxxyxxxxxxxyxxxxxxy";
        final String search = "xxx";
        final String replace = null;
        final String expected = "yxxyxyy";

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }

    /**
     * Tests that a multi-character string consisting of the same character
     * is completely replaced with another character.
     */
    public void testAllCharsReplacement() throws Exception {
        final String str = "xxxxxxxxxx";
        final String search = "x";
        final String replace = "y";
        final String expected = "yyyyyyyyyy";

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }

    /**
     * Tests that a leading match results in correct removal.
     */
    public void testLeadingMatchEmptyReplacement() throws Exception {
        final String str = "abcdefg";
        final String search = "ab";
        final String replace = "";
        final String expected = "cdefg";

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }

    /**
     * Tests that a leading match results in correct replacement.
     */
    public void testLeadingMatchReplacement() throws Exception {
        final String str = "abcdefg";
        final String search = "ab";
        final String replace = "zy";
        final String expected = "zycdefg";

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }

    /**
     * Tests that a trailing match results in correct removal.
     */
    public void testTrailingMatchEmptyReplacement() {
        final String str = "abcdefg";
        final String search = "fg";
        final String replace = "";
        final String expected = "abcde";

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }

    /**
     * Tests that a trailing match results in correct replacement.
     */
    public void testTrailingMatchReplacement() {
        final String str12 = "abcdefg";
        final String search12 = "fg";
        final String replace12 = "zy";
        final String expected12 = "abcdezy";

        assertTrue("String replacement failed",
                doReplaceAllTest(str12, search12, replace12, expected12));
    }

    /**
     * Tests that a null search string results in no replacement.
     */
    public void testNullSearch() throws Exception {
        final String str = "xxxxxxxxxx";
        final String search = null;
        final String replace = "";
        final String expected = str;

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }

    /**
     * Tests that an empty search string results in no replacement.
     */
    public void testEmptySearch() throws Exception {
        final String str = "xxxxxxxxxx";
        final String search = "";
        final String replace = "";
        final String expected = str;

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }

    /**
     * Tests that a nulll string results in a null result.
     */
    public void testNullStringReplacement() throws Exception {
        final String str = null;
        final String search = "y";
        final String replace = "x";
        final String expected = null;

        assertTrue("String replacement failed",
                doReplaceAllTest(str, search, replace, expected));
    }


    /**
     * Helper method which performs a "quoteIfNeeded" test.
     *
     * @param str the original string
     * @param expected the expected result
     * @return true if the test passes; false otherwise
     */
    private boolean doQuoteIfNeededTest(String str, String expected) {
        boolean result;
        String resultStr = StringUtils.quoteIfNeeded(str);

        if (expected == null) {
            result = resultStr == null;
        } else {
            result = expected.equals(resultStr);
        }

        return result;
    }

    /**
     * Helper method which performs a "removeQuotes" test.
     *
     * @param str the original string
     * @param expected the expected result
     * @return true if the test passes; false otherwise
     */
    private boolean doRemoveQuotesTest(String str, String expected) {
        boolean result;
        String resultStr = StringUtils.removeQuotes(str);

        if (expected == null) {
            result = resultStr == null;
        } else {
            result = expected.equals(resultStr);
        }

        return result;
    }

    /**
     * Helper method which performs a "replaceAll" test.
     *
     * @param str the original string
     * @param search the string to search for
     * @param replace the replacement string
     * @param expected the expected result
     * @return true if the test passes; false otherwise
     */
    private boolean doReplaceAllTest(String str,
                                     String search, String replace,
                                     String expected) {
        boolean result;
        String resultStr = StringUtils.literalReplaceAll(str, search, replace);

        if (expected == null) {
            result = resultStr == null;
        } else {
            result = expected.equals(resultStr);
        }

        return result;
    }

    /**
     * Tests that an IllegalArgumentException is thrown if a null arg is
     * passed into {@link StringUtils#toLowerIgnoreLocale(String)}
     * @throws Exception if an error occurs
     */
    public void testToLowerIgnoreLocaleNull() throws Exception {
        try {
            String str = null;
            StringUtils.toLowerIgnoreLocale(str);
            fail("Expected IllegalArgumentException due to null arg");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests that
     * @throws Exception
     */
    public void testLowerIgnoreLocale() throws Exception {
        Locale defaultLocale = Locale.getDefault();
        try {
            String str = "ImaGe";
            String expected = "image";
            // set the Locale to Turkish
            Locale.setDefault(new Locale("TR"));

            // sanity check to ensure that when converting uppercase I we
            // do not get i
            String turkResult = str.toLowerCase();
            assertNotEquals(expected, turkResult);

            // ok no for the proper test. Check that the conversion results in
            // the correct English string
            String result = StringUtils.toLowerIgnoreLocale(str);
            assertEquals(expected, result);
        } finally {
            // set the default Locale back to what is expected
            Locale.setDefault(defaultLocale);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	8637/5	pcameron	VBM:2005050402 Fixed quoting of string style values and font-family values

 08-Jun-05	8638/7	pcameron	VBM:2005050402 Fixed quoting of string style values and font-family values

 08-Jun-05	8637/3	pcameron	VBM:2005050402 Implemented a replaceAll for strings and added test case

 ===========================================================================
*/
