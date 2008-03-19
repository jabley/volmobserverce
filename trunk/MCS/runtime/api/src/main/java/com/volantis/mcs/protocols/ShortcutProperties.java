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
/** ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;


/**
 * This provides a means of controlling what is output for access key shortcuts
 * and also how it is output.
 *
 * @mock.generate 
 */
public class ShortcutProperties {

    /**
     * The default separator to use when none has been specified.
     */
    private static final String DEFAULT_SEPARATOR = " ";

    /**
     * The default style value to be used for the seperator.
     */
    private static final StyleValue DEFAULT_STYLE_VALUE =
         StyleValueFactory.getDefaultInstance().getString(
             null, DEFAULT_SEPARATOR);

    /**
     * The style value that represents the separator to be rendered
     * between a shortcut and what it is a shortcut for.
     */
    private StyleValue separator = DEFAULT_STYLE_VALUE;

    /**
     * Whether the shortcut should be active or not.
     */
    private boolean active = true;

    /**
     * This indicates whether the shortcut label can be surrounded in span
     * elements when output (if not, any styling will be lost).
     *
     * This is set depending on whether the device actually supports mixed
     * content (i.e. text and nested elements within the body of the a
     * element).
     */
    private boolean supportsSpan = true;


    /**
     * Retrieve the text to output between a shortcut value and the what it
     * is a shortcut for.
     *
     * @return the shortcut separator text. Will not be null
     */
    public StyleValue getSeparatorStyleValue() {
        return separator;
    }

    /**
     * Set the separator style value that represents the seperator that
     * will be output between the shortcut value and what follows it.
     * If a null separator is given the default separator is used.
     * <p>
     * Typically this will be a string, e.g ".".
     *
     * @param separator the new separator string to use. May be null
     */
    public void setSeparatorStyleValue(StyleValue separator) {
        if (separator == null) {
            this.separator = DEFAULT_STYLE_VALUE;
        } else {
            this.separator = separator;
        }
    }

    // javadoc unnecessary
    public boolean isActive() {
        return active;
    }

    // javadoc unnecessary
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Return true if the shortcut label text can be surrounded by span
     * elements when output, and false if not.
     * 
     * @return true if the shortcut label text can be surrounded by span
     * elements when output, and false if not.
     */
    public boolean supportsSpan() {
        return supportsSpan;
    }

    /**
     * Indicate whether the shortcut label text can be surrounded by span
     * elements when output, and false if not.
     *
     * This is set depending on whether the device actually supports mixed
     * content (i.e. text and nested elements within the body of the a
     * element).
     *
     * @param requiresSpan      Indicate whether the shortcut label text can
     *                          be surrounded by span elements when output,
     *                          and false if not.
     */
    public void setSupportsSpan(boolean requiresSpan) {
        this.supportsSpan = requiresSpan;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 27-Jan-05	6129/4	matthew	VBM:2004102019 supermerge required

 ===========================================================================
*/
