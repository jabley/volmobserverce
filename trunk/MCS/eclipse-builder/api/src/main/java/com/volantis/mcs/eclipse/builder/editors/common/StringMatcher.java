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
package com.volantis.mcs.eclipse.builder.editors.common;

/**
 * A simple class for matching strings against a filter that can contain the
 * following special characters:
 * <dl>
 * <dt>?</dt><dd>Matches a single character</dd>
 * <dt>*</dt><dd>Matches zero or more characters</dd>
 * <dt>\</dt><dd>Escapes special characters</dd>
 * </dl>
 * <p>Note that this matcher acts as if all filter strings ended with the '*'
 * wildcard, matching against the start of a string only.</p>
 */
public class StringMatcher {
    /**
     * The single character wildcard.
     */
    private static final char WILDCARD_SINGLE = '?';

    /**
     * The multiple character wildcard.
     */
    private static final char WILDCARD_MULTIPLE = '*';

    /**
     * The escape character.
     */
    private static final char ESCAPE = '\\';

    /**
     * The filter for this matcher.
     */
    private String filter;

    /**
     * Create a new StringMatcher with a specified initial filter.
     *
     * @param filter The filter.
     */
    public StringMatcher(String filter) {
        this.filter = filter;
    }

    // Javadoc not required
    public void setFilter(String newFilter) {
        filter = newFilter;
    }

    /**
     * Checks whether a target string matches the filter.
     *
     * @param target The string to check
     * @return True if the string matches the filter, false otherwise
     */
    public boolean matches(String target) {
        return matches(target, 0, 0);
    }

    /**
     * Checks whether a target string matches the filter from a given point.
     *
     * @param target The string to check
     * @param targetIndex The point in the string from which to check
     * @param filterIndex The point in the filter from which to check
     * @return True if the string matches the filter for the specified region,
     *         false otherwise
     */
    private boolean matches(String target, int targetIndex, int filterIndex) {
        for (int i = targetIndex; i < target.length(); i++) {
            if (filterIndex >= filter.length()) {
                // We've matched the whole filter - succeed.
                return true;
            }
            char filterChar = filter.charAt(filterIndex++);
            if (filterChar == WILDCARD_MULTIPLE) {
                // If we're at the end of the pattern, all is well
                if (wildcardsOnly(filterIndex)) {
                    return true;
                }

                // Match any number of characters
                for (int j = i; j < target.length(); j++) {
                    if (matches(target, j, filterIndex)) {
                        return true;
                    }
                }

                // No sequence matched - failed
                return false;
            } else if (filterChar == WILDCARD_SINGLE) {
                // Match a single character - no processing is required as we
                // simply assume this character from the target matches.
            } else {
                // We're matching a literal character
                if (filterChar == ESCAPE) {
                    // The 'literal' character is a \ - read the next character
                    // and treat that as the value to match.
                    if (filterIndex < filter.length()) {
                        filterChar = filter.charAt(filterIndex++);
                    } else {
                        // The '\' was the last character, and so is meaningless
                        // and will be ignored. Treat this as a completed match
                        return true;
                    }
                }

                // Fail if the character does not match.
                if (target.charAt(i) != filterChar) {
                    return false;
                }
            }
        }

        // We reached the end of the string without reaching the end of the
        // pattern.

        // If we have a trailing escape character, this can be ignored
        if (filterIndex + 1 == filter.length()) {
            if (filter.charAt(filterIndex) == ESCAPE) {
                return true;
            }
        }

        // If the filter contains only '*' wildcards then we can have a match,
        // otherwise we failed.
        return wildcardsOnly(filterIndex);
    }

    /**
     * Returns true if the filter contains only the '*' wildcard character
     * through to the end.
     *
     * @param startIndex The index from which the check begins
     * @return True if all subsequent characters are '*', false otherwise
     */
    private boolean wildcardsOnly(int startIndex) {
        for (int i = startIndex; i < filter.length(); i++) {
            if (filter.charAt(i) != WILDCARD_MULTIPLE) {
                return false;
            }
        }
        return true;
    }
}
