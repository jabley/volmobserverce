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
 * $Header: /src/voyager/com/volantis/mcs/protocols/ElementAttributeMapper.java,v 1.2 2003/02/18 14:01:32 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Feb-03    Phil W-S        VBM:2003021306 - Created. Stores sets of the
 *                              attribute names against element names. Allows
 *                              this mapping to be incrementally created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Allows a mapping to be defined that maps given element types (tags) to sets
 * of attribute names that can be associated with those elements.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public final class ElementAttributeMapper {
    /**
     * Indexed by the element name, maps to a set of attribute names for the
     * given element.
     *
     * @associates <{Set}>
     */
    private final Map elements = new HashMap();

    public ElementAttributeMapper() {
    }

    /**
     * Allows a new attribute entry to be added to the mapping against a given
     * element name.
     */
    public void add(String element, String attribute) {
        Set set = (Set) elements.get(element);

        if (set == null) {
            set = new HashSet();
            elements.put(element, set);
        }

        set.add(attribute);
    }

    /**
     * Returns the set of attribute names associated with the given element
     * name. May be null. This returned set must be treated as immutable.
     */
    public Set getElementAttributes(String element) {
        return (Set) elements.get(element);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
