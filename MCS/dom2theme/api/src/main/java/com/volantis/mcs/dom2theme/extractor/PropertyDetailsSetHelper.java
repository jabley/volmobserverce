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

package com.volantis.mcs.dom2theme.extractor;

import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.properties.PropertyDetailsSetImpl;
import com.volantis.styling.properties.StyleProperty;

import java.util.ArrayList;
import java.util.List;

public class PropertyDetailsSetHelper {

    /**
     * Return a <code>PropertyDetailsSet</code> based on the specified
     * <code>List</code> of <code>StyleProperty</code>.
     *
     * @param properties a <code>List</code> of <code>StyleProperty</code>
     *                   objects.
     * @return a PropertyDetailsSet
     */
    public static PropertyDetailsSet getDetailsSet(List properties) {
        List detailsList = new ArrayList();
        for (int i = 0; i < properties.size(); i++) {
            StyleProperty property = (StyleProperty) properties.get(i);
            detailsList.add(property.getStandardDetails());
        }

        return new PropertyDetailsSetImpl(detailsList);
    }
}
