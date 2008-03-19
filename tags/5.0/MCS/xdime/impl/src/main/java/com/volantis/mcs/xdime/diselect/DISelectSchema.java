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

package com.volantis.mcs.xdime.diselect;

import com.volantis.mcs.xdime.schema.DISelectElements;
import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.ElementSchema;

public class DISelectSchema
        extends AbstractSchema {

    // Elements from MCS namespace
    private final ElementSchema if_ = createElementSchema(DISelectElements.IF);

    public DISelectSchema() {

        // Allow unit to appear anywhere and to be transparent as far as its
        // effect
        if_.setUseAnywhere(true);
        if_.setTransparent(true);
    }
}
