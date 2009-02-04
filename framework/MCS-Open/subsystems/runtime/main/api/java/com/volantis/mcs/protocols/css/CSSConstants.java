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
package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.Element;

import java.util.Set;
import java.util.TreeSet;

public class CSSConstants {

    /**
     * The names of all supported list types.
     // todo: check: is case insensitive necessary?
     */
    private static final Set SUPPORTED_LISTS =
            new TreeSet(String.CASE_INSENSITIVE_ORDER);

    static {
        SUPPORTED_LISTS.add("nl");
        SUPPORTED_LISTS.add("ol");
        SUPPORTED_LISTS.add("ul");
    }

    /**
     * Checks if the element is a supported list type.
     * @param element the element to check.  May be null.
     * @return true iff the element is a supported list type.
     */
    static boolean isSupportedList(Element element) {
        boolean isSupported = false;

        if (element != null) {
            String name = element.getName();
            if (name != null) {
                isSupported = SUPPORTED_LISTS.contains(name);
            }
        }

        return isSupported;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	9440/2	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 ===========================================================================
*/
