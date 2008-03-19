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
package com.volantis.styling.compiler;



public class StyleSheetSource implements Source {

    public static final StyleSheetSource DEFAULT =
            new StyleSheetSource("DEFAULT", 0);

    public static final StyleSheetSource LAYOUT =
            new StyleSheetSource("LAYOUT", 1);

    public static final StyleSheetSource THEME =
            new StyleSheetSource("THEME", 2);
    
    public static final Source DEVICE = new StyleSheetSource("DEVICE", -1);

    private final String name; // for debug only

    private final int ordering;

    private StyleSheetSource(String name, int ordering) {
        this.name = name;
        this.ordering = ordering;
    }

    public int compareTo(Object o) {

        int result;

        if (o instanceof StyleSheetSource) {
            StyleSheetSource specified = (StyleSheetSource) o;
            result = this.ordering - specified.ordering;
        } else {
            throw new IllegalArgumentException();
        }

        return result;
    }

    public String toString() {
        return name;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 ===========================================================================
*/
