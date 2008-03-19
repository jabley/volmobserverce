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
package com.volantis.mcs.dom2theme.impl.generator.rule.type;

import java.util.Comparator;

public class TypeSelectorSequenceSpecificityComparator
        implements Comparator {

    public int compare(Object o1, Object o2) {
        
        TypeSelectorSequence selectors1 = (TypeSelectorSequence) o1;
        TypeSelectorSequence selectors2 = (TypeSelectorSequence) o2;

        int specificity1 = selectors1.getSpecificity();
        int specificity2 = selectors2.getSpecificity();

        // Sort by specificity.
        if (specificity1 == specificity2) {
            // Sort by type within groups, According to AN004 this is not
            // strictly necessary but makes it easier to create integration
            // tests from the examples there.
            String type1 = selectors1.getType();
            String type2 = selectors2.getType();
            if (type1 != null && type2 != null) {
                return type1.compareTo(type2);
            } else if (type1 == null && type2 != null) {
                return 1;
            } else if (type1 != null && type2 == null) {
                return -1;
            } else {
                return 0;
            }
        } else if (specificity1 < specificity2) {
            return -1;
        } else if (specificity1 > specificity2) {
            return 1;
        } else {
            throw new IllegalStateException();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/1	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
