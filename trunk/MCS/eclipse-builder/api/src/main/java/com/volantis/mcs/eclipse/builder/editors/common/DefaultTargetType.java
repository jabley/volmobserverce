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
 * Typesafe enumeration for default target types.
 */
public class DefaultTargetType {
    /**
     * Identifier for fragment target types.
     */
    public static final DefaultTargetType FRAGMENT =
            new DefaultTargetType("fragment");

    /**
     * Identifier for segment target types.
     */
    public static final DefaultTargetType SEGMENT =
            new DefaultTargetType("segment");

    /**
     * String identifying the target type.
     */
    private String typeName;

    /**
     * Private constructor to prevent instantiation outside this class.
     *
     * @param typeName A string identifier for the target type.
     */
    private DefaultTargetType(String typeName) {
        this.typeName = typeName;
    }

    // Javadoc inherited
    public String toString() {
        return typeName;
    }
}
