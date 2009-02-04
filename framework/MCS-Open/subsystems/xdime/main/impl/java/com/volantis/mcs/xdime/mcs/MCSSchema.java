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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.mcs;

import com.volantis.mcs.xdime.xhtml2.XHTML2Schema;
import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.ElementSchema;

/**
 * Schema for mcs namespace.
 */
public final class MCSSchema
        extends AbstractSchema {

    // Elements from MCS namespace
    private final ElementSchema script = createElementSchema(MCSElements.SCRIPT);
    private final ElementSchema unit = createElementSchema(MCSElements.UNIT);
    private final ElementSchema handler = createElementSchema(MCSElements.HANDLER);
    private final ElementSchema br = createElementSchema(MCSElements.BR);

    public MCSSchema(XHTML2Schema xhtml2) {

        // Add script to the head content.
        xhtml2.HEAD_CONTENT.add(script);

        script.setContentModel(PCDATA);

        // Allow unit to appear anywhere and to be transparent as far as its
        // effect
        unit.setUseAnywhere(true);
        unit.setTransparent(true);

        // Allow handler to appear anywhere.
        handler.setUseAnywhere(true);
        handler.setContentModel(PCDATA);

        // Line break is an inline element and must be empty
        xhtml2.TEXT.add(br);
        br.setContentModel(EMPTY);
    }
}
