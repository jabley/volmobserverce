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

import java.util.List;

import org.jdom.Element;

/**
 * This is the action used for each New format action within the New menu that
 * creates a non-grid format, based on a given prototypical element. It is
 * appropriate to the Layout Outline page and the Layout Graphical Editor page.
 * It is enabled when the creation of the given format type is appropriate to
 * the currently selected element.
 */
public class NewNonGridFormatActionCommand
    extends FormatTypeBasedActionCommand {

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param formatType       identifies the type of grid that this action
     *                         command will create. Must not be null and must
     *                         have a non-grid structure
     * @param selectionManager selection manager that allows the
     *                         action to set the new format as the current
     *                         selection. Can not be null.
     */
    public NewNonGridFormatActionCommand(
        FormatType formatType,
        ODOMSelectionManager selectionManager) {
        super(formatType, selectionManager);
    }

    // javadoc inherited
    protected void checkFormatType(FormatType formatType) {
        if (formatType.getStructure() == FormatType.Structure.GRID) {
            throw new IllegalArgumentException(
                "The format type must be a non-grid structure " + //$NON-NLS-1$
                "(was given " + //$NON-NLS-1$
                formatType.getTypeName() + ")"); //$NON-NLS-1$
        }
    }

    /**
     * This action command can only be performed when a single empty format is
     * selected and the format type can reasonably be created at the selected
     * point in the document.
     */
    public boolean enable(ODOMActionDetails details) {
        return ((details.getNumberOfElements() == 1) &&
            selectionIsAppropriate(details.getElement(0)) &&
            canReplace(details.getElement(0)));
    }

    /**
     * Supporting method that is used to make sure that the selection is
     * appropriate for this action command.
     *
     * @param selection the single selected element
     * @return true if the element is of an appropriate type for this command
     */
    protected boolean selectionIsAppropriate(Element selection) {
        return selection.getName().equals(FormatType.EMPTY.getElementName());
    }

    /**
     * The selected empty format is replaced by a prototype for the format
     * type.
     */
    public void run(ODOMActionDetails details) {
        if (details.getNumberOfElements() != 1) {
            throw new IllegalStateException(
                "Only a single selection should be available " + //$NON-NLS-1$
                "when the action is run (" + //$NON-NLS-1$
                details.getNumberOfElements() +
                " elements selected)"); //$NON-NLS-1$
        } else {
            Element e = replace(details.getElement(0));

            setSelection(e);
        }
    }

    /**
     * Determines whether the given element can be replaced by the required
     * type of format without causing layout constraints to be violated.
     *
     * @param element the empty format element that the action wants to replace
     * @return true if the given element can be replaced without causing
     *         constraints to be violated
     */
    protected boolean canReplace(ODOMElement element) {
        Element e = ActionSupport.cloneContainingDeviceLayout(element);

        e = replace(e);

        return !LayoutConstraintsProvider.constraints.violated(e, null);
    }

    /**
     * The specified format is replaced by a prototypical instance of the
     * required type of format.
     *
     * @param element the format to be replaced
     * @return the new (top-level) element representing the required format
     *         type
     */
    protected Element replace(Element element) {
        Element replacement = null;

        if (!selectionIsAppropriate(element)) {
            throw new IllegalArgumentException(
                    "The action can not replace a " + //$NON-NLS-1$
                    element.getName());
        }  else {
            replacement = FormatPrototype.get(formatType);
            //call common utility method
            this.replaceElement(element, replacement);
        }
        return replacement;
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

 23-Jan-04	2727/1	philws	VBM:2004012301 Fix clipboard management

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
