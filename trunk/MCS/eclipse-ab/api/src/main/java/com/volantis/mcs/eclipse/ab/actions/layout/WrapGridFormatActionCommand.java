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

import com.volantis.mcs.eclipse.ab.editors.layout.LayoutMessages;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;
import org.jdom.Element;

import java.util.List;

/**
 * This is the action command used for each Wrap format action within the Wrap
 * menu that creates a grid-based format, based on a given format type. It is
 * appropriate to the Layout Outline page and the Layout Graphical Editor page.
 * It is enabled when the wrapping of the currently selected element by the
 * given grid format type is appropriate.
 */
public class WrapGridFormatActionCommand extends NewGridFormatActionCommand {
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param formatType identifies the type of grid that this action command
     *                   will create. Must not be null and must have a grid
     *                   structur.
     * @param selectionManager the selection manager associated with the DOM
     *                  this command modifies. Can not be null.
     */
    public WrapGridFormatActionCommand(FormatType formatType,
                                       ODOMSelectionManager selectionManager) {
        super(formatType, selectionManager);
    }

    // javadoc inherited
    protected boolean selectionIsAppropriate(Element selection) {
        // This finds selection of the layout or the device layouts
        // inappropriate (in addition to the empty format, from the superclass)
        return !super.selectionIsAppropriate(selection) &&
            (selection.getParent() != null) &&
            (selection.getParent().getParent() != null);
    }

    // javadoc inherited
    protected Element replace(Element element,
                              int rows,
                              int cols) {
        Element parent = element.getParent();
        Element replacement = null;

        if (!selectionIsAppropriate(element)) {
            throw new IllegalArgumentException(
                "The action can not replace a " + //$NON-NLS-1$
                element.getName());
        } else if (parent == null) {
            throw new IllegalArgumentException(
                "The element \"" + element.getName() + //$NON-NLS-1$
                " must have a non-null parent"); //$NON-NLS-1$
        } else {
            List content = parent.getContent();
            int index = content.indexOf(element);

            try {
                if (selectionManager != null) {
                    // disable the ODOMSelectionManager to stop selection
                    // event being fired.
                    selectionManager.setEnabled(false);
                }
                // This will detach the given element, so the content will not
                // contain it any more
                replacement = FormatPrototype.createSizedGrid(formatType,
                        element,
                        rows,
                        cols,
                        progressMonitor);
            } finally {
                // ensure the manager is enabled on exit
                if (selectionManager != null) {
                    selectionManager.setEnabled(true);
                }
            }

            // Insert the replacement where the original element was
            // replaceElement(element, replacement);
            content.add(index, replacement);
        }

        return replacement;
    }
    
    public String getTaskName() {
        return LayoutMessages.getString("WrapGridFormatActionCommand.task.actionName");
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

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2783/1	philws	VBM:2003121514 Implement Wrap Actions for the Layout Editor context menu

 23-Jan-04	2727/1	philws	VBM:2004012301 Fix clipboard management

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 13-Jan-04	2534/1	philws	VB  M:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
