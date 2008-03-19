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
package com.volantis.mcs.eclipse.builder.editors.common;

import java.util.EventObject;

/**
 * An event indicating the selection or deselection of a target (device or
 * category).
 */
public class TargetSelectionEvent extends EventObject {
    /**
     * True if the target has been selected, false if it has been deselected.
     */
    private boolean selected;

    /**
     * The selected target.
     */
    private Object target;

    /**
     * Constructs a target selection event.
     *
     * @param source The object on which the Event initially occurred.
     */
    public TargetSelectionEvent(Object source, Object target, boolean selected) {
        super(source);
        this.target = target;
        this.selected = selected;
    }

    // Javadoc not required
    public boolean isSelected() {
        return selected;
    }

    // Javadoc not required
    public Object getTarget() {
        return target;
    }
}
