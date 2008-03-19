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

package com.volantis.mcs.themes.values;

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.synergetics.ObjectHelper;
import com.volantis.styling.properties.StyleProperty;

/**
 * A composite style value containing a number of other values.
 */
public class ShorthandValue {

    /**
     * The shorthand for which these values apply.
     */
    protected final StyleShorthand shorthand;

    /**
     * The nested values.
     */
    protected final StyleValue[] values;

    /**
     * The priority.
     */ 
    protected final Priority priority;

    /**
     * Initialise.
     *
     * @param shorthand The shorthand.
     * @param values    The values.
     * @param priority  The priority.
     */
    public ShorthandValue(StyleShorthand shorthand, StyleValue[] values,
                               Priority priority) {
        this.shorthand = shorthand;
        this.values = values;
        this.priority = priority;
    }

    /**
     * Get the shorthand.
     *
     * @return The shorthand.
     */
    public StyleShorthand getShorthand() {
        return shorthand;
    }

    /**
     * Get the count of the number of values in the shorthand.
     *
     * @return The value count.
     */
    public int getCount() {
        return values.length;
    }

    /**
     * Get the value at the specified index.
     *
     * @param index The index of the value.
     * @return The value, may be null.
     */
    public StyleValue getValue(int index) {
        return values[index];
    }

    /**
     * Get the property for the value at the specified index.
     *
     * @param index The index of the value.
     * @return The property.
     */
    public StyleProperty getProperty(int index) {
        return shorthand.getStandardProperties()[index];
    }

    /**
     * Get the priority of the shorthand.
     *
     * @return The priority of the shorthand.
     */
    public Priority getPriority() {
        return priority;
    }

    // Javadoc inherited.
    public String toString() {
        return getStandardCSS();
    }

    /**
     * Get the standard CSS representation of this shorthand value.
     *
     * @return The standard CSS representation of this shorthand value.
     */
    public String getStandardCSS() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(shorthand.getName()).append(':');
        String separator = "";
        for (int i = 0; i < values.length; i++) {
            StyleValue value = values[i];
            if (value != null) {
                buffer.append(selectSeparator(separator, i));
                separator = " ";
                buffer.append(value.getStandardCSS());
            }
        }
        buffer.append(priority.getStandardCSS());
        return buffer.toString();
    }

    /**
     * Select the separator to use between the <code>index - 1</code> value of
     * the shorthand and <code>index</code>.
     *
     * <p>By default this returns the separator passed in.</p>
     *
     * @param separator The default separator.
     * @param index     The index.
     * @return The selected separator.
     */
    protected String selectSeparator(String separator, int index) {
        return separator;
    }

    /**
     * Get the cost of this shorthand.
     *
     * <p>The cost is the number of characters in the standard rendering
     * of the property, values and priority.</p>
     *
     * @return The cost.
     */
    public int getStandardCost() {
        int cost = 0;
        cost += shorthand.getName().length();
        cost += 1; // The ':'
        int separator = 0;
        for (int i = 0; i < values.length; i++) {
            StyleValue value = values[i];
            if (value != null) {
                 // The separator, either ' ', or '/' between values.
                cost += separator;
                cost += value.getStandardCost();
                separator = 1;
            }
        }
        cost += priority.getStandardCost();
        return cost;
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ShorthandValue)) {
            return false;
        }

        ShorthandValue other = (ShorthandValue) obj;

        return other.shorthand == shorthand
                && other.priority == priority
                && ObjectHelper.equals(other.values, values);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = 0;
        result = 37 * result + shorthand.hashCode();
        result = 37 * result + priority.hashCode();
        result = 37 * result + ObjectHelper.hashCode(values);
        return result;
    }
}
