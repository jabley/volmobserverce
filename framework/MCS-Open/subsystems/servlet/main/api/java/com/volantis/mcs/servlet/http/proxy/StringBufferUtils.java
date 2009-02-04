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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet.http.proxy;

/**
 * Utility class that provides useful methods for working with
 * StringBuffers. This exists becuase many useful methods, such as
 * <code>indexOf</code> do not exist in JDK1.3 StringBuffer implementations.
 *
 * In JDK 1.4 many of these methods have been implemented.
 *
 * @todo This class was copied over from DSB as it is required by the
 * Proxy Session handling code.
 * @todo Should be replaced with java.lang.StringBuffer.
 */
public class StringBufferUtils {


    /**
     * Utility method to find a specific char in a string buffer. JDK 1.3 does
     * not provide this functionality. JDK1.4 does.
     *
     * @see java.lang.StringBuffer (JDK1.4) for info on behaviour
     * @param buffer the buffer for search for the match
     * @param match the character to find.
     * @param startIndex the index at which the search should start.
     * @return the index of the character if found. -1 otherwise.
     */
    public static int indexOf(StringBuffer buffer, char match, int startIndex) {
        int result = -1;
        if(startIndex<0) {
            startIndex = 0;
        }
        if (buffer != null && buffer.length() > 0
               && startIndex < buffer.length()) {
            for (int i = startIndex; i < buffer.length(); i++) {
                if (buffer.charAt(i) == match) {
                    result = i - startIndex;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Utility method to find the index of a String in a StringBuffer. This
     * is here because jdk1.4 gives you the functionality while 1.3 does not.
     * @param buffer the buffer to find the match in.
     * @param match the string to match
     * @param startIndex the index to start searching from.
     * @return the index of the firt character of the match. -1 if no match is
     * found.
     */
    public static int indexOf(StringBuffer buffer,
                              String match, int startIndex) {
        int result = -1;
        if(startIndex<0) {
            startIndex = 0;
        }
        if (buffer != null && match != null
                && buffer.length() > 0
                && match.length() > 0
                && startIndex < buffer.length()
                && match.length() <= buffer.length() + startIndex) {
            // loop over the buffer and see if the first char matches
            int endPoint = buffer.length() - match.length();
            for (int i = startIndex; i < endPoint; i++) {
                if (areEqual(buffer, i, match)) {
                    result = i - startIndex;
                    break;
                }
            }

        }
        return result;
    }

    /**
     * Utility method to find the index of a String in a StringBuffer. This
     * is here because jdk1.4 gives you the functionality while 1.3 does not.
     * This search ignores the case of the buffer and match strings.
     * @param buffer the buffer to find the match in.
     * @param match the string to match
     * @param startIndex the index to start searching from.
     * @return the index of the firt character of the match. -1 if no match is
     * found.
     */
    public static int indexOfIgnoreCase(StringBuffer buffer,
                                        String match, int startIndex) {
        int result = -1;
        if(startIndex<0) {
            startIndex = 0;
        }
        if (buffer != null && match != null
                && buffer.length() > 0
                && match.length() > 0
                && startIndex < buffer.length()
                && match.length() <= buffer.length() + startIndex) {
            // loop over the buffer and see if the first char matches
            int endPoint = buffer.length() - match.length();
            for (int i = startIndex; i < endPoint; i++) {
                if (areEqualIgnoreCase(buffer, i, match)) {
                    result = i - startIndex;
                    break;
                }
            }

        }
        return result;
    }

    /**
     * Return true if the characters in <code>buffer</code> starting at <code>
     * startIndex</code> match those in <code>match</code>. The comparison is
     * only carried out for <code>match.length()</code> characters.
     * This does NOT perform sanity checks. (that's why it's private)
     * @param buffer the buffer
     * @param startIndex the index to start searching at.
     * @param match the String to compare to the buffer section.
     * @return true if a match is found. False otherwise.
     */
    private static boolean areEqual(StringBuffer buffer, final int startIndex,
                                    String match) {
        boolean result = false;
        final int i = startIndex;
        int j = 0;
        for (; j < match.length(); j++) {
            if (buffer.charAt(j + i) != match.charAt(j)) {
                break;
            }
        }
        if (j == match.length()) {
            result = true;
        }
        return result;
    }

    /**
     * Return true if the characters in <code>buffer</code> starting at <code>
     * startIndex</code> match those in <code>match</code>. The comparison is
     * only carried out for <code>match.length()</code> characters. This
     * comparison ignores the case of the buffer and match strings.
     * This does NOT perform sanity checks. (that's why it's private)
     * @param buffer the buffer
     * @param startIndex the index to start searching at.
     * @param match the String to compare to the buffer section.
     * @return true if a match is found. False otherwise.
     */
    private static boolean areEqualIgnoreCase(StringBuffer buffer, final int startIndex,
                                              String match) {
        boolean result = false;
        final int i = startIndex;
        int j = 0;
        for (; j < match.length(); j++) {
            if (Character.toUpperCase(buffer.charAt(j + i)) !=
                    Character.toUpperCase(match.charAt(j))) {
                break;
            }
        }
        if (j == match.length()) {
            result = true;
        }
        return result;
    }

}
