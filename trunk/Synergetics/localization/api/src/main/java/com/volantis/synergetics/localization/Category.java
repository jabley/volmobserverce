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
package com.volantis.synergetics.localization;

/**
 * Type safe enumeration that enumerates the various message categories
 */
public class Category {

    /**
     * ID for a Category instance
     */
    private String identifier;

    /**
     * Represents the DEBUG category
     */
    public static Category DEBUG = new Category("D");

    /**
     * Represents the INFO category
     */
    public static Category INFO = new Category("I");

    /**
     * Represents the DEBUG category
     */
    public static Category WARN = new Category("W");

    /**
     * Represents the ERROR category
     */
    public static Category ERROR = new Category("E");

    /**
     * Represents the FATAL category
     */
    public static Category FATAL = new Category("F");

    /**
     * Represents the EXCEPTION category
     */
    public static Category EXCEPTION = new Category("X");

    /**
     * Initializes a <code>CategoryInstance</code>. Private access level to
     * disable external instantiation.
     */
    private Category(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the identifier for this Category
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Nov-04	343/1	doug	VBM:2004111702 Refactoring of logging framework

 ===========================================================================
*/
