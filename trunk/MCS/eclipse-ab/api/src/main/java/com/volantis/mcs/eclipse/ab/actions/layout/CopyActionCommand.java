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

import org.eclipse.swt.dnd.Clipboard;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.eclipse.ab.actions.ClipboardSupport;
import com.volantis.mcs.eclipse.ab.actions.ODOMActionCommand;
import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.layouts.FormatType;

/**
 * This is the Copy action command appropriate to the Layout Outline page and
 * the Layout Graphical Editor page. It only allows a single element to be
 * copied to the given clipboard, and ensures that the element is stored as an
 * appropriate type of transfer.
 */
public class CopyActionCommand extends LayoutActionCommand {
    /**
     * The object used to manage the clipboard.
     */
    protected ClipboardSupport clipboard;

    /**
     * The object used to identify transfer types
     */
    protected TransferSupport transfer = new TransferSupport();

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param clipboard the clipboard to be associated with this action command
     * @param selectionManager The selectionManager for the DOM you are
     * modifying. Can not be null.
     */
    public CopyActionCommand(Clipboard clipboard,ODOMSelectionManager selectionManager) {
        super(selectionManager);
        this.clipboard = new ClipboardSupport(clipboard);
    }

    /**
     * Copy is only valid when there is a single selection and that selection
     * is not the layout (i.e. the root node in the document) or an empty
     * format.
     */
    public boolean enable(ODOMActionDetails details) {
        boolean result = (details.getNumberOfElements() == 1);

        if (result) {
            ODOMElement element = details.getElement(0);

            result = (element.getParent() != null) &&
                (!element.getName().equals(
                    FormatType.EMPTY.getElementName()));
        }

        return result;
    }

    /**
     * Copy simply places a copy of the single selected element on the
     * clipboard.
     */
    public void run(ODOMActionDetails details) {
        if (details.getNumberOfElements() != 1) {
            throw new IllegalStateException(
                "Only a single selection should be available " + //$NON-NLS-1$
                "when the action is run (" + //$NON-NLS-1$
                details.getNumberOfElements() +
                " elements selected)"); //$NON-NLS-1$
        } else {
            // Copy the single element, with an appropriate transfer ID, to the
            // clipboard
            clipboard.copySelectionToClipboard(
                details,
                transfer.getTransferId(details.getElement(0)));
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

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
