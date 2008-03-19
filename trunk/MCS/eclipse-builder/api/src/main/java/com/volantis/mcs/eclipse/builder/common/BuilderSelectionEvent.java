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
package com.volantis.mcs.eclipse.builder.common;

import java.util.EventObject;

/**
 * An event representing a selection being made within the GUI.
 */
public class BuilderSelectionEvent extends EventObject {
    /**
     * The new selected object.
     */
    private Object selection;

    /**
     * The previously selected object
     */
    private Object oldSelection;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @param oldSelection
     * @param newSelection The new selected object.
     */
    public BuilderSelectionEvent(Object source, Object oldSelection, Object newSelection) {
        super(source);
        this.selection = newSelection;
        this.oldSelection = oldSelection;
    }

    // Javadoc unnecessary
    public Object getSelection() {
        return selection;
    }

    // Javadoc unnecessary
    public Object getOldSelection() {
        return oldSelection;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/1	adrianj	VBM:2005111601 Add style rule view

 ===========================================================================
*/
