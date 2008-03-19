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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.protocols.trans.GenericContainerValidator;

/**
 * A container validator for XHTML protocols which should be used when nested
 * tables are disabled.
 * <p>
 * In this case all nested tables will be removed, and we must specify
 * containment rules which ensure this is the case.
 * <p>
 * The classic example is a page which contains table->form->table. In this
 * case, they cannot be integrated together because of the form so the
 * outermost table must be removed as and the innermost table's "fine" layout
 * is considered more important than the outermost table's "gross" layout.
 */
public class XHTMLBasicNestedDisabledContainerValidator
        extends GenericContainerValidator {

    // Javadoc inherited.
    protected void initialize () {
        containerActionMap.put(
            "form", new Integer(INVERSE_REMAP));
        containerActionMap.put(
            DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT,
            new Integer(INVERSE_REMAP));
        containerActionMap.put(
            DissectionConstants.KEEPTOGETHER_ELEMENT,
            new Integer(INVERSE_REMAP));
    }
}
