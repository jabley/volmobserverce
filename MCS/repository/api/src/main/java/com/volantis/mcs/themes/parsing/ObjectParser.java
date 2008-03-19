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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.parsing;

/**
 * A parser for converting between objects and text representations of those
 * objects.
 */
public interface ObjectParser {
    /**
     * Converts from an object to a textual representation of that object.
     * Using a separate interface rather than the object's own implementation
     * of the {@link Object#toString} method allows for varying renderings of
     * the same object.
     *
     * @param object The object to convert to text
     * @return The text version of the object
     * @throws IllegalArgumentException if the object is not of the expected
     *                                  type
     */
    public String objectToText(Object object);

    /**
     * Converts from a textual representation of an object to the object form.
     *
     * <p>It is optional for ObjectParser implementations to provide this
     * method since some objects do not lend themselves to this form of
     * translation, but functionality of the code using the ObjectParser may
     * be limited if the method is not implemented.</p>
     *
     * @param text The text representation of the object
     * @return The object represented by the specified text
     * @throws UnsupportedOperationException if this functionality is not to be
     *                                       provided
     */
    public Object textToObject(String text);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/2	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
