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

import java.util.Locale;

/**
 * Provides generally useful string manipulation methods.
 */
public class StringUtils {
    /**
     * The single quote character.
     */
    public static final char SINGLE_QUOTE = '\'';

    /**
     * The double quote character.
     */
    public static final char DOUBLE_QUOTE = '"';

    /**
     * The character entity used to escape single quotes.
     */
    private static final String SINGLE_QUOTE_ENTITY =
            "&#" + ((int) SINGLE_QUOTE) + ';';

    /**
     * The character entity used to escape double quotes.
     */
    private static final String DOUBLE_QUOTE_ENTITY =
            "&#" + ((int) DOUBLE_QUOTE) + ';';

    /**
     * The single quote string which is used to replace all single quote
     * character entities when the string is unescaped.
     */
    private static final String SINGLE_QUOTE_STRING = "" + SINGLE_QUOTE;

    /**
     * The double quote string which is used to replace all double quote
     * character entities when the string is unescaped.
     */
    private static final String DOUBLE_QUOTE_STRING = "" + DOUBLE_QUOTE;

    /**
     * If the given string is surrounded by a pair of single or double quotes
     * these are removed, any XML character entities for the quote are
     * replaced with the actual quote character, and the result returned.
     * Otherwise the original string is returned.
     *
     * @param str the string from which quotes should be removed
     * @return the string with any surrounding quotes removed and any single
     * quote XML character entities replaced with single quotes
     */
    public static String removeQuotes(final String str) {
        String result = str;

        // A string less than 2 characters in length cannot be trimmed
        if ((str != null) && (str.length() > 1)) {
            int length = str.length() - 1;
            final char firstChar = str.charAt(0);
            final char lastChar = str.charAt(length);
            boolean needsTrim =
                    ((firstChar == SINGLE_QUOTE) ||
                    (firstChar == DOUBLE_QUOTE)) &&
                    (lastChar == firstChar);

            if (needsTrim) {
                // Remove the quotes
                result = str.substring(1, length);

                // Unescape any XML quote character entities only if necessary.
                if (firstChar == SINGLE_QUOTE) {
                    result = StringUtils.literalReplaceAll(result,
                            SINGLE_QUOTE_ENTITY,
                            SINGLE_QUOTE_STRING);
                } else if (firstChar == DOUBLE_QUOTE) {
                    result = StringUtils.literalReplaceAll(result,
                            DOUBLE_QUOTE_ENTITY,
                            DOUBLE_QUOTE_STRING);
                }
            }
        }

        return result;
    }

    /**
     * Replaces all literal (that is, not regular expression-based) occurrences
     * of the search string with the replacement string.
     *
     * @param str the original string in which to search. If null, no
     * replacement is done.
     * @param search the string to search for. If null, no replacement is done.
     * @param replacement the replacement string. If null, all occurrences of
     * the <code>search</code> string are removed.
     * @return a string where all occurrences of <code>search</code> have been
     * replaced with <code>replacement</code>, or null if <code>str</code> is
     * null, or <code>str</code> if <code>search</code> is null.
     */
    public static String literalReplaceAll(final String str,
                                           final String search,
                                           final String replacement) {
        String result = str;

        if (str != null && search != null) {
            final int strLen = str.length();
            final int searchLen = search.length();

            // Only start replacement if there's something to replace.
            if (strLen > 0 && searchLen > 0) {
                final StringBuffer buf = new StringBuffer(strLen);
                int searchIdx = 0;
                int foundIdx = 0;

                // While there is still string to search
                while (searchIdx < strLen) {
                    if ((foundIdx = str.indexOf(search, searchIdx)) != -1) {

                        // Found search string somewhere after searchIdx, so
                        // append any string in between.
                        buf.append(str.substring(searchIdx, foundIdx));
                        // advance the search
                        searchIdx = foundIdx + searchLen;

                        // Substitute the replacement, if any.
                        if (replacement != null && replacement.length() > 0) {
                            buf.append(replacement);
                        }
                    } else {
                        // Did not find the search string, so append rest of
                        // string.
                        buf.append(str.substring(searchIdx));

                        // terminate search
                        searchIdx = strLen;
                    }
                }

                // get the result
                result = buf.toString();
            }
        }

        return result;
    }

    /**
     * Helper method to determine the quote character to use for surrounding
     * the string.
     *
     * @param str the string of interest
     * @return the quote character to use
     */
    private static char determineQuoteChar(String str) {
        // default character is single quote
        char quoteChar = SINGLE_QUOTE;
        final boolean hasSingle = str.indexOf(SINGLE_QUOTE) != -1;
        final boolean hasDouble = str.indexOf(DOUBLE_QUOTE) != -1;

        if (hasSingle && !hasDouble) {
            // string contains only single quotes so use the double quote so
            // that single quotes do not need escaping.
            quoteChar = DOUBLE_QUOTE;
        } else if (!hasSingle && hasDouble) {
            // string contains only double quotes so use the single quote toso
            // that double quotes do not need escaping
            quoteChar = SINGLE_QUOTE;
        }

        return quoteChar;
    }


    /**
     * Adds quotes to the specified string, ensuring that any existing
     * quotes within the string are encoded as an XML entity. A null
     * input string is returned as a null output string.
     *
     * @param str the string to be enclosed in quotes. May be null
     * @return the string enclosed in single quotes
     */
    public static String addQuotesAndEscape(final String str) {
        String result = str;

        if (str != null) {
            final char quoteChar = determineQuoteChar(str);
            StringBuffer sb = new StringBuffer(str.length() + 2);

            // Output the attribute's value surrounded by quotes
            sb.append(quoteChar);

            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);

                if (ch == quoteChar) {
                    // Need to encode the quote to avoid parsing problems
                    if (ch == SINGLE_QUOTE) {
                        sb.append(SINGLE_QUOTE_ENTITY);
                    } else {
                        sb.append(DOUBLE_QUOTE_ENTITY);
                    }
                } else {
                    sb.append(ch);
                }
            }

            result = sb.append(quoteChar).toString();
        }

        return result;
    }


    /**
     * Puts quotes around the string if the string contains any non alpha
     * numeric characters. Multi-byte characters are not considered to be
     * alphanumeric by this process and so their presence will result in
     * quoting.
     *
     * @param str the string to be quoted if necessary
     * @return the string in quotes if necessary
     */
    public static String quoteIfNeeded(String str) {
        String result = str;

        // This does a simple check: all characters in the string must be
        // ASCII alpha numeric to not require quoting. Note that we cannot
        // use Java's Character class to determine the alphanumericity
        // of a character because that will consider multi-byte characters to
        // be alphanumeric (if they *are* alphanumeric, that is). However, we
        // must quote multi-byte characters. Hence we use ASCII codes in the
        // check.
        if (result != null) {
            boolean needsQuoting = false;

            for (int i = 0; !needsQuoting && (i < result.length()); i++) {
                char ch = result.charAt(i);

                needsQuoting = !((ch >= '0' && ch <= '9') ||
                        (ch >= 'a' && ch <= 'z') ||
                        (ch >= 'A' && ch <= 'Z'));
            }

            if (needsQuoting) {
                result = StringUtils.addQuotesAndEscape(result);
            }
        }

        return result;
    }

    /**
     * This method converts the given String to lower case. It uses a english
     * Locale to do this. This is needed as some uses of
     * {@link String#toLowerCase()} in MCS use the string internally and expect
     * the Characters to be from the English Locale. However, if a customer is
     * in a different Locale the conversion may result in a non English
     * character. For Example if the Locale is Turkish (tr) then converting I
     * (Upper case i) to lower case results in a lower case i without the dot.
     *
     * @param str the String to convert. Cannot be null
     * @return the String converted to lower case
     */
    public static String toLowerIgnoreLocale(String str) {
        if (str == null) {
            throw new IllegalArgumentException("str argument cannot be null");
        }
        return str.toLowerCase(Locale.ENGLISH);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Oct-05	9732/1	adrianj	VBM:2005100509 Fixes to Eclipse GUI

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Jun-05	8637/15	pcameron	VBM:2005050402 Implemented a replaceAll for strings and added test case

 07-Jun-05	8637/11	pcameron	VBM:2005050402 Fixed quoting of font-family values

 06-May-05	8095/1	philws	VBM:2005050402 Resolve font family and other style string value handling

 ===========================================================================
*/
