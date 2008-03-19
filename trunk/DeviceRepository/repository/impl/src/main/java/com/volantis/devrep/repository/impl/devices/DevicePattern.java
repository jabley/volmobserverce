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
 * $Header: /src/voyager/com/volantis/mcs/devices/DevicePattern.java,v 1.4 2002/05/20 16:52:15 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Oct-01    Doug            VBM:2001100906: Created. DevicePattern is a
 *                              class that encapsulates a pattern and device
 *                              pair. It provides a match method that uses
 *                              the Gnu Rex (regular expresion package) to
 *                              determine whether a user agent string matches
 *                              the pattern. The compareTo method allows
 *                              DevicePattern objets to be stored in a
 *                              SortedSet according to the pattern length.
 * 29-Oct-01    Paul            VBM:2001102901 - DevicePattern has moved from
 *                              utilities package to devices package.
 * 04-Jan-02    Paul            VBM:2002010403 - Map Rex specific excpetions
 *                              into IllegalArgumentException to prevent the
 *                              caller from having to import rex code.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 20-May-02    Byron           VBM:2002041501 Changed compareTo so that objects
 *                              of this type may be sorted by length and by
 *                              whether the string contains wildcards or not.
 *                              Long, non-wildcard strings appear at the top
 *                              and short wildcard strings appear at the bottom
 *                              Added containsWildcard() method
 * ----------------------------------------------------------------------------
 */

package com.volantis.devrep.repository.impl.devices;


import java.util.regex.Pattern;

/**
 * Class that encapsulates a device pattern and device name pair.
 */
public class DevicePattern implements Comparable {

    /**
     * The pattern string.
     */
    private final String pattern;

    /**
     * The name of the Device associated with the pattern string.
     */
    private final String deviceName;

    /**
     * The compiled pattern
     */
    private Pattern compiledPattern;

    /**
     * DevicePattern constructor.
     * @param pattern the pattern string.
     * @param deviceName the name of the device associted with the pattern.
     * @throws IllegalArgumentException If the pattern is not valid or either
     *         patern or deviceName is null.
     */
    public DevicePattern(String pattern, String deviceName)
            throws IllegalArgumentException {

        // Check that null values have not been supplied.
        if (pattern == null) {
            throw new IllegalArgumentException("pattern must not be null");
        }
        if (deviceName == null) {
             throw new IllegalArgumentException("deviceName must not be null");
        }

        this.pattern = pattern;
        this.deviceName = deviceName;
        pattern = fixParenthesis(pattern);
        compiledPattern = Pattern.compile(pattern);        
    }

    /**
     * Utility method that replaces any occurrence of the character
     * 'charToReplace' with the replacement string iff the character prior to
     * replacement character is not a '\' character.
     *
     * Note there may be ways to reduce the garbage/performance of this method,
     * however, it should start modifying the original string in 40 out of +-880
     * cases.
     *
     * @param original      the original input string. Should not be null.
     * @param charToReplace the character to search and replace.
     * @param replacement   the replacement string.
     * @return the modified string or original if no modifications were
     *         necessary.
     */
    protected String replace(String original,
                             char charToReplace,
                             String replacement) {

        String result = original;
        int index = original.indexOf(charToReplace);
        if (index >= 0) {
            StringBuffer buffer = new StringBuffer(index);
            int startIndex = 0;

            // Replace all characters, if necessary iteratively.
            while (index >= 0) {
                if (startIndex != index) {
                    buffer.append(original.substring(startIndex, index));
                }
                boolean replace = true;
                // Prevent OutOfBoundException
                if ((index - 1) >= 0) {
                    replace = original.charAt(index-1) != '\\';
                }

                if (replace) {
                    buffer.append(replacement);
                } else {
                    buffer.append(charToReplace);
                }

                // Update the indices and search for the next character to
                // replace.
                startIndex = index + 1;
                index = original.indexOf(charToReplace, index + 1);
            }
            // append the part after the last charToReplace to the buffer.
            int lastIndexOf = original.lastIndexOf(charToReplace);
            if (lastIndexOf < (original.length() - 1)) {
                buffer.append(original.substring(lastIndexOf + 1));
            }
            result = buffer.toString();
        }
        return result;
    }

    /**
     * Helper method that will fix the parenthesis problem with the jakarta
     * regular expression constructor. (a lot of the device patterns do not have
     * matching parenthesis - 40 out 880 - so this method replaces any '(' or
     * ')' character with an escaped version, namely '\(' or '\)'.
     *
     * @param pattern the pattern to modify.
     * @return the modified pattern or the original if no changes were
     *         necessary.
     */
    private String fixParenthesis(String pattern) {
        String result = replace(pattern, '(', "\\(");
        return replace(result, ')', "\\)");
    }

    /**
     * Return the pattern string.
     * @return the pattern string
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Return the device name.
     * @return the device name.
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * Does a user agent string match the pattern property.
     * @param userAgent a user agent string.
     * @return true if matched, false otherwise.
     */
    public boolean match(String userAgent) {
        return compiledPattern.matcher(userAgent).find();
    }

    /**
     * Implementation of the Comparable interface.
     *
     * @param obj the object to compare against
     * @return a negative integer, zero, or a positive integer if this object
     * is less than, equal to, or greater than the specified object.
     */
    public int compareTo(Object obj) {
        if (!(obj instanceof DevicePattern)) {
            throw new ClassCastException(obj.getClass() +
                    " not comparable with DevicePattern");
        }
        DevicePattern dp = (DevicePattern) obj;
        // This classes natural ordering must be consistent with equals
        // if we are planning to store DevicePattern object in a SortedSet
        // or a SortedMap.
        if (this.equals(dp)) {
            return 0;
        }

        // device patterns with longer patterns should be ahead of shorter
        // patterns
        // natural string ordering is used if two patters have the same length
        int diff = dp.pattern.length() - this.pattern.length();
        if (diff == 0) {
            diff = this.pattern.compareTo(dp.pattern);
        }
        return diff;
    }

    /**
     * Examine the string for wildcards. The string contains wildcards if it
     * contains a '*' or a non-escaped '.'.
     * @param str the string to examine
     * @return true if a wildcard is found, false otherwise
     */
    private boolean containsWildcards(String str) {

        if (str == null) {
            return false;
        }
        if (str.indexOf('*') != -1) {
            return true;
        }
        int index = str.indexOf('.');
        if (index == -1) {
            return false;
        }
        if (index == 0) {
            return true;
        }
        do {
            if (str.charAt(index - 1) != '\\') {
                return true;
            }
        } while ((index = str.indexOf('.', index + 1)) >= 0);
        return false;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the object that we are testing for equality
     * @return true if obj is eqaul to this object, false otherwise
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof DevicePattern)) {
            return false;
        }
        DevicePattern dp = (DevicePattern) obj;
        return (this.pattern.equals(dp.pattern) &&
                this.deviceName.equals(dp.deviceName));
    }

    // Javadoc inherited
    public int hashCode() {
        return pattern.length() * deviceName.length();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Mar-05	7130/5	rgreenall	VBM:2005011201 Further changes post review

 02-Mar-05	7130/3	rgreenall	VBM:2005011201 Post review corrections

 02-Mar-05	7130/1	rgreenall	VBM:2005011201 Fixed bug where the mapping of a user agent pattern to a device name would fail if the pattern was one character greater than the device name.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 02-Jan-04	2339/1	byron	VBM:2003123104 Replace rex.jar with Jakarta regexp

 ===========================================================================
*/
