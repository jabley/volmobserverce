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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom;

/**
 * A type safe enumeration of the different families of markup that are
 * supported by the document.
 */
public abstract class MarkupFamily {

    /**
     * An XML based markup language.
     */
    public static final MarkupFamily XML = new MarkupFamily("XML") {

        public boolean compareRootElementNames(String n1, String n2) {
            return n1.equals(n2);
        }
    };

    /**
     * An SGML based markup language.
     */
    public static final MarkupFamily SGML = new MarkupFamily("SGML") {

        public boolean compareRootElementNames(String n1, String n2) {
            return n1.equalsIgnoreCase(n2);
        }
    };

    private final String name;

    private MarkupFamily(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public abstract boolean compareRootElementNames(String n1, String n2);
}
