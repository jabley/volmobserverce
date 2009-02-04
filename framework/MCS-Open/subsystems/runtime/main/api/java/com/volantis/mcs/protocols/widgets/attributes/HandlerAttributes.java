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
import com.volantis.mcs.protocols.widgets.EventReference;

/**
 * Holds properties specific to Handler element.
 */
public class HandlerAttributes extends WidgetAttributes {

    private EventReference eventReference;
    private ActionReference actionReference;
    private boolean enabled = true;
    
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
    /**
     * @return Returns the eventReference.
     */
    public EventReference getEventReference() {
        return eventReference;
    }
    /**
     * @param eventReference The eventReference to set.
     */
    public void setEventReference(EventReference eventReference) {
        this.eventReference = eventReference;
    }
    /**
     * @return Returns the enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }
    /**
     * @param enabled The enabled to set.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
