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

package com.volantis.styling;

import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.PseudoStyleEntities;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.StatefulPseudoClassImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of {@link PseudoStyleEntities} that contains all the
 * pseudo entities that affect styling and are used within MCS.
 */
public class PseudoStyleEntitiesImpl
        implements PseudoStyleEntities {

    static final Map mapPseudoClassNameToEntity;
    static final Map mapPseudoElementToEntity;

    static {
        mapPseudoClassNameToEntity = new HashMap();
        mapPseudoClassNameToEntity.put("active", StatefulPseudoClassImpl.ACTIVE);
        mapPseudoClassNameToEntity.put("focus", StatefulPseudoClassImpl.FOCUS);
        mapPseudoClassNameToEntity.put("hover", StatefulPseudoClassImpl.HOVER);
        mapPseudoClassNameToEntity.put("link", StatefulPseudoClassImpl.LINK);
        mapPseudoClassNameToEntity.put("visited", StatefulPseudoClassImpl.VISITED);
        mapPseudoClassNameToEntity.put("mcs-concealed", StatefulPseudoClassImpl.MCS_CONCEALED);
        mapPseudoClassNameToEntity.put("mcs-unfolded", StatefulPseudoClassImpl.MCS_UNFOLDED);
        mapPseudoClassNameToEntity.put("mcs-invalid", StatefulPseudoClassImpl.MCS_INVALID);
        mapPseudoClassNameToEntity.put("mcs-normal", StatefulPseudoClassImpl.MCS_NORMAL);
        mapPseudoClassNameToEntity.put("mcs-busy", StatefulPseudoClassImpl.MCS_BUSY);
        mapPseudoClassNameToEntity.put("mcs-failed", StatefulPseudoClassImpl.MCS_FAILED);
        mapPseudoClassNameToEntity.put("mcs-suspended", StatefulPseudoClassImpl.MCS_SUSPENDED);
        mapPseudoClassNameToEntity.put("mcs-disabled", StatefulPseudoClassImpl.MCS_DISABLED);

        mapPseudoElementToEntity = new HashMap();
        mapPseudoElementToEntity.put("first-line", PseudoElements.FIRST_LINE);
        mapPseudoElementToEntity.put("first-letter", PseudoElements.FIRST_LETTER);
        mapPseudoElementToEntity.put("mcs-shortcut", PseudoElements.MCS_SHORTCUT);
        mapPseudoElementToEntity.put("before", PseudoElements.BEFORE);
        mapPseudoElementToEntity.put("after", PseudoElements.AFTER);
        mapPseudoElementToEntity.put("marker", PseudoElements.MARKER);
        mapPseudoElementToEntity.put("mcs-next", PseudoElements.MCS_NEXT);
        mapPseudoElementToEntity.put("mcs-previous", PseudoElements.MCS_PREVIOUS);
        mapPseudoElementToEntity.put("mcs-reset", PseudoElements.MCS_RESET);
        mapPseudoElementToEntity.put("mcs-cancel", PseudoElements.MCS_CANCEL);
        mapPseudoElementToEntity.put("mcs-complete", PseudoElements.MCS_COMPLETE);
        mapPseudoElementToEntity.put("mcs-label", PseudoElements.MCS_LABEL);
        mapPseudoElementToEntity.put("mcs-item", PseudoElements.MCS_ITEM);
        mapPseudoElementToEntity.put("mcs-between", PseudoElements.MCS_BETWEEN);
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

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 25-Aug-05	9377/1	schaloner	VBM:2005071102 Migrated mcs-shortcut-after to mcs-shortcut and after

 22-Jul-05	9110/2	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/3	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
