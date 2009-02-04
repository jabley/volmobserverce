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

package com.volantis.mcs.utilities;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.regex.Pattern;

/**
 * Immutable class representing a product version.
 *
 * Please do not change it to mutable, as users of this class
 * depend on its immutability.
 */
public class ProductVersion implements Comparable {

    /**
     * The build major version.
     */
    private final int major;

    /**
     * The build minor version.
     */
    private final int minor;

    /**
     * The build version revision.
     */
    private final int revision;

    /**
     * Regular expression used for parsing version string.
     *
     * Assumes that version separators are any non-digit characters
     */
    private final static Pattern REGEXP = Pattern.compile("\\D");

    public ProductVersion(int versionMajor, int versionMinor, int versionRevision) {        
        major = Math.max(0, versionMajor);
        minor = Math.max(0, versionMinor);
        revision = Math.max(0, versionRevision);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getRevision() {
        return revision;
    }

    /**
     * Create a ProductVersion from a string representation.
     *
     * Tries to parse a string into version number using very relaxed approach:
     * Any non-digit characters are treated as separators and the first three
     * numbers are treated as major, minor and revision.
     *
     * If there is no numbers in the string, null is returned.
     * If there is at least one number, a valid ProductVersion is returned
     * with missing numbers set to 0.
     *
     * Examples:
     * parse("1") returns 1.0.0
     * parse("1_2") returns 1.2.0
     * parse("1.2.3.4") returns 1.2.3
     * parse("1 2 rev. 12) returns 1.2.12
     * parse(""latest greatest") returns null
     *
     */
    public static ProductVersion parse(String external) {

        if (null == external || external.trim().length() == 0) {
            return null;
        }

        String[] chunks = REGEXP.split(external);

        int major = 0;
        int minor = 0;
        int revision = 0;

        // We eliminate non-digit charcaters when splitting,
        // so calling parseInt is safe. The only possibility
        // for parseInt failure is if somneone uses ridicolously
        // long version number (greater than maxint), but in such
        // case exception thrown by parseInt is a correct reaction
        if (chunks.length > 0) {
            major = Math.max(0, Integer.parseInt(chunks[0]));
        }
        if (chunks.length > 1) {
            minor = Math.max(0, Integer.parseInt(chunks[1]));
        }
        if (chunks.length > 2) {
            revision = Math.max(0, Integer.parseInt(chunks[2]));
        }
        return (major + minor + revision > 0) ?
                new ProductVersion(major, minor, revision) : null;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProductVersion))
            return false;

       return (compareTo(o) == 0);
    }

    /**
     * hashCode implementation guarantees, that
     * if a.equals(b) then hashCode(a) == hashCode(b)
     */
    public int hashCode() {
        return toString().hashCode();
    }

    public int compareTo(Object o) {

        // According to Sun's documentation, implementation of compareTo
        // method SHOULD throw ClassCastException for incomparable type
        ProductVersion that = (ProductVersion) o;

        if (major == that.major) {
            if(minor == that.minor) {
                return revision - that.revision;                
            }
            else {
                return minor - that.minor;
            }
        } else {
            return major - that.major;
        }
    }

    public boolean isGreaterOrEqual(ProductVersion that) {
        return (this.compareTo(that) >= 0);
    }

    /**
     * Returns string representation using dots as separators
     *
     * It's guaranteed, that
     * if a.equals(b) then a.toString().equals(b.toString())    
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(major).append(".")
              .append(minor).append(".")
              .append(revision);
        return buffer.toString();
    }
}
