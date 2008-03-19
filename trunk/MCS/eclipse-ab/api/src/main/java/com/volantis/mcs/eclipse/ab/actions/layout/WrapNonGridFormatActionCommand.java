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

import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;

import java.util.List;

import org.jdom.Element;

/**
 * This is the action used for each Wrap format action within the Wrap menu
 * that creates a non-grid format, based on a given prototypical element. It is
 * appropriate to the Layout Outline page and the Layout Graphical Editor page.
 * It is enabled when the wrapping of the currently selected element by the
 * given format type is appropriate.
 */
public class WrapNonGridFormatActionCommand
    extends NewNonGridFormatActionCommand {
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param formatType       identifies the type of format that this action
     *                         command will create. Must not be null and must
     *                         have a grid structure
     * @param selectionManager selection manager that allows the
     *                         action to set the new format as the current
     *                         selection. Can not be null.
     */
    public WrapNonGridFormatActionCommand(
                FormatType formatType,
                ODOMSelectionManager selectionManager) {
        super(formatType, selectionManager);
    }

    // javadoc inherited
    protected void checkFormatType(FormatType formatType) {
        if (formatType.getStructure() !=
            FormatType.Structure.SIMPLE_CONTAINER) {
            throw new IllegalArgumentException(
                "The format type must be a " + //$NON-NLS-1$
                FormatType.Structure.SIMPLE_CONTAINER +
                " structure (was given " + //$NON-NLS-1$
                formatType.getTypeName() +
                " which is a " + //$NON-NLS-1$
                formatType.getStructure() + " structure)"); //$NON-NLS-1$
        }
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
    protected Element replace(Element element) {
        Element replacement = null;
        try {
            // disable the selection manager this means that
            // no selection events will be fired by the super call
            if (selectionManager != null) {
                selectionManager.setEnabled(false);
            }
            replacement = super.replace(element);
            List content = replacement.getContent();

            //selection manager will always be enabled by this point

            // Now insert the original element into the replacement, replacing
            // the empty format that it contains (we know this is the structure
            // since we require a simple container format type in this class, see
            // {@link #checkFormatType}.

            replaceElement((Element) content.get(0), element);
        } finally {
            // ensure that selection manager is enabled (in case exceptions
            // are thrown
            if (selectionManager != null) {
                this.selectionManager.setEnabled(true);
            }
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

 28-Jan-04	2783/2	philws	VBM:2003121514 Implement Wrap Actions for the Layout Editor context menu

 23-Jan-04	2727/1	philws	VBM:2004012301 Fix clipboard management

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
