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
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the Swap action command appropriate to the Layout Outline page and
 * the Layout Graphical Editor page. It only allows a two non-empty format
 * elements within a single device layout to be swapped as long as the
 * resulting content changes would be valid.
 */
public class SwapActionCommand implements ODOMActionCommand {

    /**
     * Selection manager that can be used to reset the selection to
     * a defined element.
     */
    protected ODOMSelectionManager selectionManager;


    /**
     * Initializes the new instance using the given parameters.
     * @param selectionManager The selection Manager associated with the ODOM
     * this command modifies. Can not be null.
     */
    public SwapActionCommand(ODOMSelectionManager selectionManager) {
        if (selectionManager == null) {
            throw new IllegalArgumentException(
                    "selectionManager must not be null");
        }
        this.selectionManager = selectionManager;
    }

    /**
     * Enables the action if the selection is two non-empty formats within the
     * same device layout and the resulting content changes would be valid.
     * @return true if there are two elements selected and they would make a
     * valid swap
     */
    public boolean enable(ODOMActionDetails details) {
        // here is where I put the validation
        boolean valid = false;
        // check that there are two selections
        if (details.getNumberOfElements() == 2) {
            Element element1 = details.getElement(0);
            Element element2 = details.getElement(1);
            // check that they are both elements of the same device layout
            if (ActionSupport.haveCommonLayout(element1, element2)) {
                // check that neither of the elements are in each other to
                // avoid trying to make an element a descendant of itself
                valid = ActionSupport.canSwap(element1, element2);
            }
        }
        return valid;
    }

    /**
     * Performs the swap
     */
    public void run(ODOMActionDetails details) {
        if (details.getNumberOfElements() != 2) {
            throw new IllegalStateException(
                    "A two element selection should be available " +
                    "when the action is run (" +
                    details.getNumberOfElements() +
                    " elements selected)");
        } else {

            Element element1 = details.getElement(0);
            Element element2 = details.getElement(1);

            ActionSupport.swapFormatElements(element1, element2,
                    selectionManager);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6749/1	allan	VBM:2005012102 Drag n Drop support

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-04	4886/2	allan	VBM:2004052812 Workaround TreeItem.getItems() bug and tidy up.

 14-Jul-04	4833/1	tom	VBM:2004052812 Added Swap Functionality

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
