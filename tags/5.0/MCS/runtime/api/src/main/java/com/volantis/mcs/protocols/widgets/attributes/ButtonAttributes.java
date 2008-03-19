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

import com.volantis.mcs.protocols.widgets.ActionReference;

/**
 * Holds properties specific to ActionButton
 */
public class ButtonAttributes extends WidgetAttributes {

    private ActionReference actionReference;

    /**
     * @return Returns the actionReference.
     */
    public ActionReference getActionReference() {
        return actionReference;
    }

    /**
     * @param actionReference The actionReference to set.
     */
    public void setActionReference(ActionReference actionReference) {
        this.actionReference = actionReference;
    }    
}
