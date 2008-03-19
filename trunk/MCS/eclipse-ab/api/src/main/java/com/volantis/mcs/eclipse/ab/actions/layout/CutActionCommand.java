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

import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;
import org.eclipse.swt.dnd.Clipboard;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Cut action command appropriate to the Layout Outline page and
 * the Layout Graphical Editor page. It has the same limitations on selections
 * as the {@link CopyActionCommand}.
 */
public class CutActionCommand extends CopyActionCommand {
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param clipboard the clipboard to be associated with this action.
     * @param selectionManager The selectionManager for the DOM you are
     * modifying. Can not be null.
     */
    public CutActionCommand(Clipboard clipboard,
                            ODOMSelectionManager selectionManager) {
        super(clipboard, selectionManager);
    }

    /**
     * Cut simply removes the selected element (and content) from the document
     * after performing the actions of its superclass. A cut format will be
     * replaced by an empty format.
     */
    public void run(ODOMActionDetails details) {
        // Do what the superclass needs to do
        super.run(details);

        // Now remove the element from the document, ensuring that it is
        // replaced by an empty element if necessary
        ODOMElement element = details.getElement(0);
        Element parent = element.getParent();

        if (parent.getParent() != null) {
            // A format is being cut as opposed to a device layout
            // so an empty format must take its place
            List content = parent.getContent();
            int index = content.indexOf(element);
            Element replacement = FormatPrototype.get(FormatType.EMPTY);
            replaceElement(element, replacement);
            List list = new ArrayList();
            list.add(replacement);
            selectionManager.setSelection(list);

        } else {
            element.detach();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-04	4886/1	allan	VBM:2004052812 Workaround TreeItem.getItems() bug and tidy up.

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 12-Feb-04	2924/1	eduardo	VBM:2004021003 editor actions demarcate UndoRedo UOWs

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
