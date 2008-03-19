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
package com.volantis.mcs.eclipse.builder.editors.themes;

import org.eclipse.swt.widgets.Composite;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;

/**
 * This class provide utility methods to convert dialog representation of
 * lists to and from a string representaon of a seperatd list.
 */

public abstract class GenericListBrowsActionAbstract
        implements StylePropertyBrowseAction {

    //Javadoc Inherited
    public abstract String doBrowse(String value,
                                    Composite parent,
                                    EditorContext context);

    /**
     * Convert the seperated list to a String[] array.
     *
     * @param value The string value to convert.
     * @return The array of values.
     */
    protected String[] convertValueToArray(String value) {
        if ("".equals(value)) {
            return new String[]{};
        }
        return value.split("\\s*,\\s*");
    }

    /**
     * Convert the array of values back into a seperated list.
     *
     * @param array The array to convert.
     * @param sep the seperator to append after each item.
     * @return The value as a seperated list.
     */
    protected String convertArrayToValue(String[] array, String sep) {
        StringBuffer intermediateResult = new StringBuffer();
        String actualSep = null;
        for (int n =0; n < array.length;) {
           if (actualSep != null) {
               intermediateResult.append(sep);
           } else {
               actualSep = sep;
           }
           intermediateResult.append(array[n]);
           n++;
        }
        return intermediateResult.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10608/1	ianw	VBM:2005120206 Added browse actions for font-family and mcs-chart-forground-color

 ===========================================================================
*/
