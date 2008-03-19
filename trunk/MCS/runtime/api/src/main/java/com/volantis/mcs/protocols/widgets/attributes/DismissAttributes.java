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
 * Holds properties specific to Dismiss attributes.
 */
public class DismissAttributes extends WidgetAttributes {

    /**
     * type attributes describe type dismiss button 
     * e.g dialog popup has "yes" and "no" type buttons
     */    
    private String type;
    
    /**
     * id of widget to dismiss
     */
    private String dismissableId;
    
    /**
     * Set type for dismiss
     * @param type type attribute from widget:dismiss element
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Get type for dismiss
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns id of widget to dismiss
     * @return
     */
    public String getDismissableId() {
        return dismissableId;
    }

    /**
     * Sets id of widget to dismiss
     * @param dismissableId - id of widget to dismiss
     */
    public void setDismissableId(String dismissableId) {
        this.dismissableId = dismissableId;
    }    


}
