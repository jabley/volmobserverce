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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.device;

import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.PseudoStyleEntities;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.StatefulPseudoClassImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * The set of pseudo style entities support on the target device.
 */
public class DevicePseudoStyleEntities
        implements PseudoStyleEntities {

    private static final Map mapPseudoClassNameToEntity;
    private static final Map mapPseudoElementToEntity;

    static {
        mapPseudoClassNameToEntity = new HashMap();
        mapPseudoClassNameToEntity.put("active",
                StatefulPseudoClassImpl.ACTIVE);
        mapPseudoClassNameToEntity.put("focus", StatefulPseudoClassImpl.FOCUS);
        mapPseudoClassNameToEntity.put("hover", StatefulPseudoClassImpl.HOVER);
        mapPseudoClassNameToEntity.put("link", StatefulPseudoClassImpl.LINK);
        mapPseudoClassNameToEntity.put("visited",
                StatefulPseudoClassImpl.VISITED);

        mapPseudoElementToEntity = new HashMap();
        mapPseudoElementToEntity.put("first-line", PseudoElements.FIRST_LINE);
        mapPseudoElementToEntity.put("first-letter",
                PseudoElements.FIRST_LETTER);
    }

    // Javadoc inherited.
    public StatefulPseudoClass getStatefulPseudoClass(String cssName) {
        return (StatefulPseudoClass) mapPseudoClassNameToEntity.get(cssName);
    }

    // Javadoc inherited.
    public PseudoElement getPseudoElement(String cssName) {
        return (PseudoElement) mapPseudoElementToEntity.get(cssName);
    }
}
