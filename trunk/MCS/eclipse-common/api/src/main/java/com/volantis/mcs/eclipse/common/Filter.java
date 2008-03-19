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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common;

/**
 * An an array of Objects with an associated type thar specify a group
 * to be filtered. The types are either INCLUDE or EXCLUDE that
 * indicate whether the array of Objects that make up the filter are to be
 * included (i.e. comprise all of the group) or excluded (i.e. the group is
 * all except those attributes specfied in the filter.)
 */
public class Filter {

    /**
     * A StringFilter that indicates the associated array of strings that
     * make up the filter should be included i.e. comprise the whole of the
     * group they are filtering.
     */
    public static final FilterType INCLUDE = new FilterType();

    /**
     * A StringFilter that indicates the associated array of strings that
     * make up the filter should be exclude i.e. the group they are filtering
     * should be the whole group except those Objects in the filter.
     */
    public static final FilterType EXCLUDE = new FilterType();

    /**
     * The array of Objects that comprise the filter.
     */
    public final Object[] filter;

    /**
     * The type of this filter.
     */
    public final FilterType type;

    /**
     * Construct a new Filter.
     * @param filter The Objects to filter.
     * @param type The type of this Filter.
     */
    public Filter(Object [] filter, FilterType type) {
        this.filter = filter;
        this.type = type;
    }

    /**
     * A typesafe enumerator for specifying the type of a Filter.
     */
    public static class FilterType {
        /**
         * The constructor.
         */
        protected FilterType() {
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Aug-04	5294/1	geoff	VBM:2004082405 Reduce unnecessary background threads in testsuite

 13-Jan-04	2534/2	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 17-Dec-03	2213/1	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 ===========================================================================
*/
