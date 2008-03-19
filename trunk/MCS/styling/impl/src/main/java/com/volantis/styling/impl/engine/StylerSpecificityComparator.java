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

package com.volantis.styling.impl.engine;

import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.compiler.SpecificityImpl;
import com.volantis.styling.compiler.Specificity;
import com.volantis.styling.compiler.Specificity;
import com.volantis.mcs.themes.Priority;

import java.util.Comparator;

/**
 * Compare two stylers based on their priority and specificity.
 */
public class StylerSpecificityComparator
        implements Comparator {

    // Javadoc inherited.
    public int compare(Object o1, Object o2) {

        Styler styler1 = (Styler) o1;
        Styler styler2 = (Styler) o2;

        Priority priority1 = styler1.getPriority();
        Priority priority2 = styler2.getPriority();
        int result = priority1.compareTo(priority2);
        if (result != 0) {
            return result;
        }

        Specificity specificity1 = styler1.getSpecificity();
        Specificity specificity2 = styler2.getSpecificity();
        return specificity1.compareTo(specificity2);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
