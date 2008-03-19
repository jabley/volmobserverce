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
 * Field element attributes.
 */
public class FieldAttributes extends WidgetAttributes {
    /**
     * Value of the 'ref' attribute.
     */
    private String ref;
    
    /**
     * Message area ID.
     */
    private String messageArea;

    /**
     * @return Returns the ref.
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param ref The ref to set.
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
     * @return Returns the messageAreaId.
     */
    public String getMessageArea() {
        return messageArea;
    }

    /**
     * @param messageAreaId The messageAreaId to set.
     */
    public void setMessageArea(String messageAreaId) {
        this.messageArea = messageAreaId;
    }
}
