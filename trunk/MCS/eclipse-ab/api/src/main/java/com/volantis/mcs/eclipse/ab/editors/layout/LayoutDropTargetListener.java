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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.ab.actions.layout.ActionSupport;
import com.volantis.mcs.eclipse.ab.actions.layout.FormatPrototype;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.xml.xpath.XPath;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Display;
import org.jdom.Element;

/**
 * DropTargetListener for layouts.
 */
public class LayoutDropTargetListener extends DropTargetAdapter {

    /**
     * The ODOMActionDetails associated with this LayoutTreeDropTargetListener.
     */
    private final ODOMActionDetails actionDetails;

    /**
     * The ODOMSelectionManager associated with this
     * LayoutTreeDropTargetListener.
     */
    private final ODOMSelectionManager selectionManager;

    /**
     * The layout editor context associated with this
     * LayoutTreeDropTargetListener.
     */
    private final LayoutEditorContext layoutEditorContext;

    /**
     * Construct a new LayoutDragSourceListener.
     *
     * @param actionDetails the ODOMActionDetails that provides the
     * ODOMElements to drag.
     * @param layoutEditorContext to get access to the selected variant
     */
    public LayoutDropTargetListener(
            ODOMActionDetails actionDetails,
            ODOMSelectionManager selectionManager,
            LayoutEditorContext layoutEditorContext) {

        assert(actionDetails != null);
        assert(selectionManager != null);
        assert(layoutEditorContext != null);

        this.actionDetails = actionDetails;
        this.selectionManager = selectionManager;
        this.layoutEditorContext = layoutEditorContext;
    }

    // javadoc inherited
    public void dragOver(DropTargetEvent event) {
        if (layoutEditorContext.isSelectedVariantReadOnly()) {
            event.feedback = DND.FEEDBACK_NONE;
            event.detail = DND.DROP_NONE;
        } else {
            event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
        }
    }

    // javadoc inherited
    public void drop(DropTargetEvent event) {
        ActionSupport.ActionIdentifier action = null;
        // If the drop is on an empty format then simply do the move/copy
        // as requested. Otherwise the operation to perform requires user
        // input to determine a swap or replace operation.
        if (event.data == null) {
            event.detail = DND.DROP_NONE;
        } else {
            ODOMElement elements [] = (ODOMElement[]) event.data;
            ODOMElement sourceElement = elements[0];
            ODOMElement targetElement = getCurrentTarget(event);
            if (targetElement == sourceElement) {
                event.detail = DND.DROP_NONE;
            } else {
                XPath srcXPath = new XPath(actionDetails.getElement(0));
                boolean canReplace = ActionSupport.canReplace(sourceElement,
                        targetElement, srcXPath);
                if (!targetElement.getName().
                        equals(FormatType.EMPTY.getElementName())) {
                    boolean canSwap = event.detail == DND.DROP_MOVE;
                    ActionSupport.canSwap(sourceElement, targetElement);
                    if (canReplace || (canReplace && canSwap)) {
                        SwapReplaceDialog dialog =
                                new SwapReplaceDialog(Display.getCurrent().
                                getActiveShell(),
                                        canSwap);
                        dialog.open();
                        action = dialog.getResult();
                    }
                } else if (canReplace) {
                    action = ActionSupport.REPLACE_ACTION;
                } else {
                    event.detail = DND.DROP_NONE;
                }

                if (action == ActionSupport.REPLACE_ACTION) {
                    Element replacement = FormatPrototype.get(FormatType.EMPTY);
                    if (event.detail == DND.DROP_MOVE) {
                        ActionSupport.pasteFormatElement(actionDetails.
                                getElement(0), replacement, selectionManager);
                    }
                    ActionSupport.pasteFormatElement(targetElement, sourceElement,
                            selectionManager);


                } else if (action == ActionSupport.SWAP_ACTION) {
                    ActionSupport.swapFormatElements(targetElement,
                            actionDetails.getElement(0),
                            selectionManager);
                }
            }
        }
    }

    /**
     * Get the ODOMElement that is the target of the drop.
     * @param event the DropTargetEvent
     * @return the ODOMElement upon which the drop will occur.
     */
    protected ODOMElement getCurrentTarget(DropTargetEvent event) {
        ODOMElement target = event.item == null ? null :
                (ODOMElement) event.item.getData();
        return target;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6749/2	allan	VBM:2005012102 Drag n Drop support

 ===========================================================================
*/
