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

package com.volantis.styling.debug;

/**
 * Provides some helper methods for generating debug.
 */
public class DebugHelper {

    private static final String INDENT =
        "                                                                  ";

    /**
     * Get a string of the specified number of white space characters.
     *
     * @param length The number of white space characters.
     *
     * @return The white space string.
     */
    public static String getIndent(int length) {
        return INDENT.substring(0, length);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Aug-05	9370/1	gkoch	VBM:2005070507 xform select option to store caption styles instead of caption (style) class

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 ===========================================================================
*/
