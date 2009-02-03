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
package com.volantis.synergetics.utilities;

/**
 * Class containing some useful methods for CharSequence interogation.
 */
public class CharSequenceUtils {

    /**
     * Provide a regionMatches for two CharSequences so that Strings and
     * StringBuffers and anything else implementing CharSequence can be region
     * matched without needing to create a String.
     * <p/>
     * This implemenation is based on String.regionMatches() and has the same
     * specification.
     *
     * @param chars1     the first CharSequence to match
     * @param ignoreCase indicate whether or not to
     * @param toffset    the offset in chars1 from which to begin comparison
     * @param chars2     the second CharSequence to match
     * @param ooffset    the offset in chars2 from which to being comparison
     * @param len        the number of chars to compare
     * @return true if the regions defined in the given CharSequences have the
     *         same set of chars (with the ignore case caveat); false
     *         otherwise
     */
    public static boolean regionMatches(CharSequence chars1,
                                        boolean ignoreCase,
                                        int toffset,
                                        CharSequence chars2, int ooffset,
                                        int len) {

        if ((ooffset < 0) || (toffset < 0) ||
                (toffset > chars1.length() - len) ||
                (ooffset > chars2.length() - len)) {
            return false;
        }
        while (len-- > 0) {
            char c1 = chars1.charAt(toffset++);
            char c2 = chars2.charAt(ooffset++);
            if (c1 == c2) {
                continue;
            }
            if (ignoreCase) {
                // If characters don't match but case may be ignored,
                // try converting both characters to uppercase.
                // If the results match, then the comparison scan should
                // continue.
                char u1 = Character.toUpperCase(c1);
                char u2 = Character.toUpperCase(c2);
                if (u1 == u2) {
                    continue;
                }
                // Unfortunately, conversion to uppercase does not work properly
                // for the Georgian alphabet, which has strange rules about case
                // conversion.  So we need to make one last check before
                // exiting.
                if (Character.toLowerCase(u1) == Character.toLowerCase(u2)) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Version of equalsIgnoreCase for CharSequence
     * @param chars1 the first CharSequence
     * @param chars2 the second CharSequence
     * @return true if the CharSequences are equals; false otherwise
     */
    public static boolean equalsIgnoreCase(CharSequence chars1,
                                           CharSequence chars2) {
        boolean equals = chars1 == null && chars2 == null;
        if (!equals && chars1 != null && chars2 != null) {
            int len = chars1.length();
            equals = len == chars2.length() &&
                    regionMatches(chars1, true, 0, chars2, 0, len);
        }
        return equals;
    }

    /**
     * A case-insensitive implementation of indexOf, based on the standard
     * String implementation.
     *
     * @param s        The string to search
     * @param fragment The fragment being searched for
     * @return The index at which fragment next appears within s
     */
    public static int indexOfIgnoreCase(CharSequence s,
                                        CharSequence fragment) {
        return indexOfIgnoreCase(s, fragment, 0);
    }

    /**
     * A case-insensitive implementation of indexOf, based on the standard
     * String implementation.
     *
     * @param s         The string to search
     * @param fragment  The fragment being searched for
     * @param fromIndex The index from which to start searching
     * @return The index at which fragment next appears within s
     */
    public static int indexOfIgnoreCase(CharSequence s, CharSequence fragment,
                                        int fromIndex) {
        return indexOfIgnoreCase(s, fragment, fromIndex,
                false);
    }

    /**
     * A case-insensitive implementation of indexOf, based on the standard
     * String implementation that will ignore preceeding whitespace.
     * <p/>
     * For example,
     * <pre>
     * indexOfIgnoreCaseAndPrecedingWhiteSpace("   abc", "abc", 0)
     * </pre>
     * would return 0
     *
     * @param toSearch  the string to search.
     * @param toFind    the string to be found.
     * @param fromIndex the index at which the search will begin.
     * @return the index at which the given string <code>toFind</code> has been
     *         found in the given string <code>toSearch</code>; or -1 if
     *         <code>toFind</code> was not found.
     */
    public static int indexOfIgnoreCaseAndPrecedingWhiteSpace(
            String toSearch, String toFind, int fromIndex) {
        return indexOfIgnoreCase(toSearch, toFind, fromIndex,
                true);
    }

    /**
     * A case-insensitive implementation of indexOf, based on the standard
     * String implementation.
     *
     * @param toSearch                  the string to search.
     * @param toFind                    the string to be found.
     * @param fromIndex                 the index at which the search will
     *                                  commence.
     * @param ignorePrecedingWhitespace specifies that preceeding whitespace
     *                                  should be ignored.
     * @return the index at which the string <code>toFind</code> has been found
     *         in the given string <code>toSearch</code>.
     */
    private static int indexOfIgnoreCase(CharSequence toSearch,
                                         CharSequence toFind,
                                         int fromIndex,
                                         boolean ignorePrecedingWhitespace) {

        int sourceCount = toSearch.length();
        int targetCount = toFind.length();
        if (fromIndex >= sourceCount) {
            return targetCount == 0 ? sourceCount : -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }

        int i = fromIndex;
        int max = sourceCount - targetCount;

        int precedingWhitespaceChars = 0;

        if (ignorePrecedingWhitespace) {
            precedingWhitespaceChars =
                    getPrecedingWhitespaceCharacters(toSearch, fromIndex);
            // skip over the whitespace.
            i = precedingWhitespaceChars > 0 ? i + precedingWhitespaceChars -1 : i;
        }

        startSearchForFirstChar:
        while (true) {

            /* Look for first character. */
            while (i <= max && !regionMatches(toSearch,
                    true, i, toFind, 0, 1)) {
                i++;
            }
            if (i > max) {
                return -1;
            }

            /* Found first character, now look at the rest of v2 */
            int j = i + 1;
            int end = j + targetCount - 1;
            int k = 1;
            while (j < end) {
                if (!regionMatches(toSearch, true, j++, toFind, k++, 1)) {
                    i++;
                    /* Look for str'toSearch first char again. */
                    continue startSearchForFirstChar;
                }
            }

            return i - precedingWhitespaceChars;
        }
    }

    /**
     * Returns the number of preceeding whitespace characters in the given
     * string starting at the given <code>startFrom</code> index.
     * <p/>
     * For example,
     * <pre>
     * getPrecedingWhitespaceCharacters("   abc", 0)
     * </pre>
     * Returns 3
     *
     * @param chars the CharSequence
     * @param startFrom the index to start the search for preceeding
     *                  whitespace.
     * @return the number of preceeding whitespace characters in the given
     *         string starting at the given <code>startFrom</code> index.
     */
    private static int getPrecedingWhitespaceCharacters(CharSequence chars,
                                                        int startFrom) {
        int i = startFrom;
        int max = chars.length() - i - 1;

        boolean stopWhitespaceSearch = false;
        while (i <= max && !stopWhitespaceSearch) {
            stopWhitespaceSearch = !Character.isWhitespace(chars.charAt(i++));
        }
        return (i - 1) - startFrom;
    }
}
