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
package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.impl.ThemeProperty;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 */

public class ThemeStructureDefinition {

    private List properties;

    public Map getPropertiesAsMap() {
        Map map = new HashMap();
        if (properties != null) {
            Iterator iterator = properties.iterator();
            while (iterator.hasNext()) {
                com.volantis.mcs.themes.impl.ThemeProperty property = (ThemeProperty)iterator.next();
                map.put(property.getName(),property.getStyleType());
            }
        }
        return map;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9965/4	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 27-Oct-05	9965/3	ianw	VBM:2005101811 interim commit

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 ===========================================================================
*/
