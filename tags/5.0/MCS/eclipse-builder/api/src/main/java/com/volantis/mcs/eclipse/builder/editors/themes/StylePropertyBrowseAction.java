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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import org.eclipse.swt.widgets.Composite;

/**
 * An interface representing the ability to respond to a browse button being
 * pressed in a properties composite. Typically this will involve the display
 * of a dialog to edit a value.
 *
 * <p>Note that because the browse button is associated with a text field,
 * values are provided as strings.</p>
 */
public interface StylePropertyBrowseAction {
    /**
     * Carry out the browse action for this object.
     *
     * @param value The current value
     * @param parent The display component triggering the browse
     * @param context The context for the resource being edited
     * @return The new value
     */
    public String doBrowse(String value, Composite parent, EditorContext context);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 ===========================================================================
*/
