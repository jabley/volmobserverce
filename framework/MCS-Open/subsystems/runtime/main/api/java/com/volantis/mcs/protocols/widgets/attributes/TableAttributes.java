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

package com.volantis.mcs.protocols.widgets.attributes;

/**
 * Holds properties specific to Table Widget.
 */
public class TableAttributes
        extends WidgetAttributes {
    /**
     * Attributes of the XHTML2 Table element, on which this Table widget is
     * based.
     */
    private com.volantis.mcs.protocols.TableAttributes xhtml2Attributes;

    /**
     * Returns the attributes of the XHTML2 table element, on which this Table
     * Widget is based.
     *
     * @return Returns the xhtml2tableAttributes.
     */
    public com.volantis.mcs.protocols.TableAttributes getXHTML2Attributes() {
        return xhtml2Attributes;
    }

    /**
     * Sets the attributes of the XHTML2 table element, on which this Table
     * widget is based.
     *
     * @param xhtml2Attributes The xhtml2Attributes to set.
     */
    public void setXHTML2Attributes(
            com.volantis.mcs.protocols.TableAttributes xhtml2Attributes) {
        this.xhtml2Attributes = xhtml2Attributes;
    }
}
