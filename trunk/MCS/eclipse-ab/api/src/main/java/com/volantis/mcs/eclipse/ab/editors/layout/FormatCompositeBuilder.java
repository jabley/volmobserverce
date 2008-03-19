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

import com.volantis.mcs.eclipse.ab.actions.ArrayBasedActionDetails;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMElementTransfer;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.LayoutSchemaType;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import java.util.Iterator;


/**
 * A builder for creating FormatComposite hierarchies from an Element of
 * a Document.
 */
public class FormatCompositeBuilder {
    /**
     * The MenuManager associated with this FormatCompositeBuilder.
     */
    private final IMenuListener contextMenuGeneratingListener;

    /**
     * The ODOMEditorContext associated with this FormatCompositeBuilder.
     */
    private final ODOMEditorContext context;

    /**
     * Creates a FormatCompositeBuilder.
     */
    public FormatCompositeBuilder(ODOMEditorContext context) {
        assert(context != null);
        this.context = context;
        contextMenuGeneratingListener =
                LayoutContextMenuGeneratingListenerFactory.getSingleton().
                createContextMenuGeneratingListener(context,
                        LayoutActions.createActions(context));
    }

    /**
     * Builds a FormatComposite structure for the element, in the specified
     * container, and with the specified parent FormatComposite.
     * @param parent the Composite parent for the new FormatComposite
     * @param element the element representing the format type to construct.
     * @return the root FormatComposite of the created hierarchy.
     * @throws IllegalArgumentException if container or element is null
     */
    public FormatComposite build(Composite parent, ODOMElement element,
                                 FormatComposite root) {
        if (parent == null) {
            throw new IllegalArgumentException("Cannot be null: container");
        }
        if (element == null) {
            throw new IllegalArgumentException("Cannot be null: element");
        }

        return buildFormatComposite(parent, element, root);
    }

    /**
     * Build a FormatComposite as a child of a given Composite and associated
     * with a specified ODOMElement. This method will recurse down though
     * the format hierarchy implied by the given ODOMElement creating
     * FormatComposites for the descendent elements it finds.
     * @param parent the parent Composite
     * @param element the ODOMElement to associate with the FormatComposite
     * to be created. This ODOMElement should represent some kind of
     * layout format.
     * @param root the FormatComposite at the top of the FormatComposite
     * hierarchy.
     * @return a FormatComposite for the given ODOMElement.
     */
    private FormatComposite buildFormatComposite(Composite parent,
                                                 ODOMElement element,
                                                 FormatComposite root) {

        FormatComposite formatComposite = null;

        FormatType formatType =
                FormatType.getFormatTypeForElementName(element.getName());

        // formatType will be null if the element is something that supports
        // formats rather than being an actual format e.g. gridFormatColumns
        // In this case there is nothing to build.
        if (formatType != null) {
            formatComposite = new FormatComposite(
                    parent,
                    element,
                    root,
                    contextMenuGeneratingListener);


            // If there are children of this newly created FormatComposite then
            // the FormatComposite is the parent.
            parent = formatComposite;

            if (root == null) {
                // The FormatComposite just created must be the root.
                root = formatComposite;
            }

            new FormatCompositeModifier(formatComposite, this);

            formatComposite.pack();

            supportDND(formatComposite);
        }

        Iterator childIterator = element.getChildren().iterator();
        while (childIterator.hasNext()) {
            ODOMElement child = (ODOMElement) childIterator.next();
            FormatComposite childFC =
                    buildFormatComposite(parent, child, root);
            if (childFC != null) {
                GridData data = new GridData(GridData.FILL_BOTH);
                childFC.setLayoutData(data);
            }
        }

        // If the FormatComposite is a FormatComposite that represents a
        // grid then its text must updated because of the children
        // that have just been added. There appears to be no way for the
        // parent to detect that a child Control has just been added -
        // Control.java inconveniently sets its parent variable directly
        // instead of calling setParent(). So the FormatComposite text is
        // updated here.
        if (formatComposite != null &&
                formatComposite.getFormatType().getStructure() ==
                FormatType.Structure.GRID) {
            formatComposite.setText(formatComposite.getLocalizedText());
        }

        return formatComposite;
    }


    /**
     * Support drag-n-drop on the TreeViewer within the Outline Page
     * associated with this LayoutDesignPart.
     */
    private void supportDND(final FormatComposite formatComposite) {

        Transfer transfer [] = new Transfer[]{
            new ODOMElementTransfer(context.getODOMFactory(),
                    "layoutElements")
        };

        final ODOMSelectionFilter filter =
                new ODOMSelectionFilter(null,
                        new String[]{
                            context.getRootElement().getName(),
                            LayoutSchemaType.CANVAS_LAYOUT.getName(),
                            LayoutSchemaType.MONTAGE_LAYOUT.getName()
                        });

        final ArrayBasedActionDetails actionDetails =
                new ArrayBasedActionDetails();

        // Set up a listener that will update the action's enablement
        // status, using the {@link ODOMActionCommand#enable} method return
        // value, storing the selection in the actionDetails
        ODOMElementSelectionListener dndListener =
                new ODOMElementSelectionListener() {
                    public void selectionChanged(ODOMElementSelectionEvent event) {
                        actionDetails.setElements(event.getSelection().
                                toODOMElementArray());
                    }
                };

        context.getODOMSelectionManager().
                addSelectionListener(dndListener, filter);

        int operations = DND.DROP_COPY | DND.DROP_MOVE;

        DragSource source = new DragSource(formatComposite, operations);
        DropTarget target = new DropTarget(formatComposite, operations);

        source.addDragListener(
            new LayoutDragSourceListener(transfer[0], actionDetails));
        source.setTransfer(transfer);
        target.addDropListener(new LayoutDropTargetListener(
                actionDetails, context.getODOMSelectionManager(),
            ((LayoutODOMEditorContext) context).getLayoutEditorContext()) {

            protected ODOMElement getCurrentTarget(DropTargetEvent event) {
                return formatComposite.getElement();
            }
        });

        target.setTransfer(transfer);
    }

    /**
     * Builds the FormatComposite for the given element at the specified cell
     * position of the supplied GridFormatComposite. Used by
     * {@link com.volantis.mcs.eclipse.ab.editors.layout.states.FormatModifyState}.
     *
     * @param rowPos the row position of the cell
     * @param colPos the column position of the cell
     * @param element the ODOM element for which the FC is built
     * @param parent the parent GridFormatComposite
     * @return the new FormatComposite
     */
    public FormatComposite build(FormatComposite parent, ODOMElement element,
                                 int rowPos, int colPos) {

        // Create the new FormatComposite
        FormatComposite newFC = build(parent, element,
                parent.getRoot());

        // Either the new FormatComposite must be placed above (Z-order)
        // its neight below or below its neighbour above. However, in some
        // cases there will be no neighbour above and in other cases no
        // neighbour below.
        int childIndex = rowPos * colPos + colPos;
        Control children [] = parent.getChildren();
        if (childIndex <= children.length) {
            // There might not be a neighbour above but there must be a
            // neighbour below.
            FormatComposite neighbourBelow = parent.getChildFC(rowPos, colPos);
            newFC.moveAbove(neighbourBelow);
        }

        GridData data = new GridData(GridData.FILL_BOTH);
        newFC.setLayoutData(data);

        parent.setText(parent.getLocalizedText());

        return newFC;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-May-05	8295/1	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 17-May-05	8213/1	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 02-Feb-05	6749/2	allan	VBM:2005012102 Drag n Drop support

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Aug-04	5266/2	philws	VBM:2004081007 Fix layout context menu handling

 04-Aug-04	4902/11	allan	VBM:2004071504 Rework issues

 03-Aug-04	4902/9	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 24-Feb-04	3021/1	pcameron	VBM:2004020211 Some tweaks to StyledGroup and FormatComposites

 13-Feb-04	2915/4	pcameron	VBM:2004020905 Used LayoutSchemaType for attribute names

 12-Feb-04	2915/2	pcameron	VBM:2004020905 Refactored GridFormatComposite to use the State pattern for grid mods

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 30-Jan-04	2549/17	pcameron	VBM:2004011201 FormatComposite structures and graphical layout editor

 23-Jan-04	2726/8	pcameron	VBM:2004012301 Committed for integration into layout editor

 23-Jan-04	2726/6	pcameron	VBM:2004012301 Committed for integration into layout editor

 23-Jan-04	2726/4	pcameron	VBM:2004012301 Committed for integration into layout editor

 23-Jan-04	2726/2	pcameron	VBM:2004012301 Committed for integration into layout editor

 22-Jan-04	2549/3	pcameron	VBM:2004011201 Committed for integration into layout editor

 ===========================================================================
*/
