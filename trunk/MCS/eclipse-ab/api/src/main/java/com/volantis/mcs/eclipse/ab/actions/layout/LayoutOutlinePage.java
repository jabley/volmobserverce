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

import com.volantis.mcs.eclipse.ab.actions.ArrayBasedActionDetails;
import com.volantis.mcs.eclipse.ab.actions.GlobalActions;
import com.volantis.mcs.eclipse.ab.actions.ViewerGlobalActionManager;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMElementTransfer;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMOutlinePage;
import com.volantis.mcs.eclipse.ab.editors.layout.LayoutDragSourceListener;
import com.volantis.mcs.eclipse.ab.editors.layout.LayoutDropTargetListener;
import com.volantis.mcs.eclipse.ab.editors.layout.LayoutODOMEditorContext;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.layouts.LayoutSchemaType;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

/**
 * An ODOMOutlinePage for the Layout editor. This class exists so that
 * drag and drop functionality that requires access to the TreeViewer when
 * it is created but before it is displayed.
 */
public final class LayoutOutlinePage extends ODOMOutlinePage {

    /**
     * The global actions used by this page.
     */
    private final GlobalActions globalActions;

    /**
     * Construct a new ODOMOutlinePage
     * @param context The ODOMEditorContext from which to derive the
     * root element of the tree view and possible other useful information.
     * @param skipElements Any elements that should be skipped in the
     * tree view i.e. not shown though their children may be. Can be null.
     * @param stopElements Any elements that should not have their child
     * elements shown in the tree. Can be null.
     * @param attributesAsChildren If true will cause element attributes
     * to be provided as child content of elements; otherwise element attributes
     * will not be provided separately from their associated elements - though
     * the label provider may be configured to display attributes with or
     * without the element. Note that to just display the attributes as
     * children of the element (i.e. leaf nodes in the tree) the
     * label provider must use a ELEMENT_AND_ATTRIBUTES kind of
     * ODOMLabelProviderConfiguration.
     * @param labelProvider The ILabelProvider for the TreeViewer
     * contained within this ODOMOutlinePage. Can be null and if so
     * a default ODOMLabelProvider will be used with a configuration of
     * ELEMENTS_AND_ATTRIBUTES. This ODOMLabelProvider will use
     * EclipseCommonMessages to find the images for elements and EditorMessages
     * for its attribute image. The format for labels will be ODOMLabelProvider
     * defaults when labelProvider is null. In this case if a format is not
     * found for a given element, a MissingResourceException will be thrown.
     * It is adviseable to provide your own ILabelProvider.
     * @param menuManager The IMenuManager for the context menu on this
     * ODOMOutlinePage. Can be null if no context menu is required.
     * @param globalActions the global actions used by this page. Cannot be
     * null.
     */
    public LayoutOutlinePage(LayoutODOMEditorContext context,
                             String[] skipElements,
                             String[] stopElements,
                             boolean attributesAsChildren,
                             ILabelProvider labelProvider,
                             MenuManager menuManager,
                             GlobalActions globalActions) {
        super(context,
                null,
                null, skipElements,
                stopElements,
                attributesAsChildren,
                labelProvider,
                menuManager,
                null);
        context.getLayoutEditorContext().addSelectedVariantListener(
            new BuilderSelectionListener() {
                public void selectionMade(BuilderSelectionEvent event) {
                    updateRootElement();
                }
            });
        assert(globalActions != null);
        this.globalActions = globalActions;
    }


    /**
     * Override createControl to support drag-n-drop and global actions.
     * @param parent the parent Composite
     */
    public void createControl(Composite parent) {
        super.createControl(parent);
        supportOutlineDND();
        supportGlobalActions();
    }

    /**
     * Support global actions within this page.
     */
    private void supportGlobalActions() {
        new ViewerGlobalActionManager(globalActions, getTreeViewer(),
                getSite().getActionBars());

        // Add a selection listener to the viewer for action bar update.
        getTreeViewer().
                addSelectionChangedListener(new ISelectionChangedListener() {
                    public void selectionChanged(SelectionChangedEvent event) {
                        getSite().getActionBars().updateActionBars();
                    }
                });
    }

    /**
     * Support drag-n-drop on the TreeViewer within the Outline Page
     * associated with this LayoutDesignPart.
     */
    private void supportOutlineDND() {

        TreeViewer treeViewer = getTreeViewer();

        final LayoutODOMEditorContext odomEditorContext =
            (LayoutODOMEditorContext) getODOMEditorContext();
        Transfer transfer [] = new Transfer[]{
            new ODOMElementTransfer(odomEditorContext.getODOMFactory(),
                    "layoutElements")
        };

        final ODOMSelectionFilter filter =
                new ODOMSelectionFilter(null,
                        new String[]{
                            LayoutSchemaType.LAYOUT.getName(),
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

        odomEditorContext.getODOMSelectionManager().
                addSelectionListener(dndListener, filter);

        int operations = DND.DROP_COPY | DND.DROP_MOVE;

        treeViewer.addDragSupport(operations, transfer,
                new LayoutDragSourceListener(transfer[0], actionDetails));
        treeViewer.addDropSupport(operations, transfer,
                new LayoutDropTargetListener(actionDetails,
                        odomEditorContext.getODOMSelectionManager(),
                        odomEditorContext.getLayoutEditorContext()));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8295/4	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 17-May-05	8295/2	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 02-Feb-05	6749/4	allan	VBM:2005012102 Drag n Drop support

 ===========================================================================
*/
