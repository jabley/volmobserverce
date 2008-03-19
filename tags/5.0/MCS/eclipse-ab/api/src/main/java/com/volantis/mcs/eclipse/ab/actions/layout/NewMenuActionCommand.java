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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.eclipse.ab.actions.ODOMActionCommand;
import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;

/**
 * This is the action command that represents the entire menu for the New
 * format actions. It is appropriate to the Layout Outline page and the Layout
 * Graphical Editor page. It is enabled when there is a single selected empty
 * format element. It doesn't actually do anything when selected; it is simply
 * a drop-down menu of other action choices.
 */
public class NewMenuActionCommand extends LayoutActionCommand {

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param selectionManager The selection manager associated with the DOM this
     * command modifies. Can not be null.
     */
    public NewMenuActionCommand(ODOMSelectionManager selectionManager) {
        super(selectionManager);
    }

    /**
     * The action is enabled if there is a single empty format selected.
     */
    public boolean enable(ODOMActionDetails details) {
        boolean result = (details.getNumberOfElements() == 1);

        if (result) {
            result = details.getElement(0).getName().equals(
                FormatType.EMPTY.getElementName());
        }

        return result;
    }

    // javadoc inherited
    public void run(ODOMActionDetails details) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
