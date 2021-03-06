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

package com.volantis.mcs.protocols.widgets.attributes;

import com.volantis.mcs.protocols.widgets.PropertyReference;

/**
 * Holds properties specific to select element
 */
public class SelectAttributes extends WidgetAttributes {

    private String mode;

    private PropertyReference propertyReference;    
    
    /**
     * @return Returns the mode.
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode The mode to set.
     */
    public void setMode(String mode) {
        this.mode = mode;
    }
    
    /**
     * @return Returns the propertyReference.
     */
    public PropertyReference getPropertyReference() {
        return propertyReference;
    }

    /**
     * @param propertyReference The propertyReference to set.
     */
    public void setPropertyReference(PropertyReference propertyReference) {
        this.propertyReference = propertyReference;
    }        
}
