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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.ab.editors.dom.validation.ValidationListener;
import com.volantis.mcs.eclipse.common.ProblemMarkerFinder;
import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMAttribute;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.eclipse.controls.ItemContainer;
import com.volantis.mcs.eclipse.controls.TreeItemContainer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * A ContentOutlinePage for ODOM based editors. This page is for use within
 * Outline views.
 */
public class ODOMOutlinePage extends ContentOutlinePage {

    /**
     * The ODOMEditorContext associated with this ODOMOutlinePage.
     */
    private final ODOMEditorContext context;

    /**
     * The root element that will be used as the root of the outline view.
     */
    private ODOMElement rootElement;

    /**
     * The elements in the tree to be skipped by this ODOMOutlinePage.
     */
    private final String[] skipElements;

    /**
     * The elements whose children should not be shown in the tree.
     */
    private final String[] stopElements;

    /**
     * The ODOMSelectionFilter for filtering selections for this page.
     */
    private final ODOMSelectionFilter selectionFilter;

    /**
     * Flag to indicate whether or not element attributes should be
     * included as "elements" by the content provider for this
     * ODOMOutlinePage (content providers refer to all their items as elements).
     * If true then attributes of an element will appear as leaf nodes in the
     * tree. If false attributes will not be shown unless the label
     * provider writes them out.
     */
    private final boolean attributesAsChildren;

    /**
     * The Label Provider for this ODOMOutlinePage.
     */
    private final ILabelProvider labelProvider;

    /**
     * The IMenuManager for the context menu.
     */
    private final MenuManager menuManager;

    /**
     * Used by the label decorator to determine if a node required decorating
     */
    private final ProblemMarkerFinder problemMarkerFinder;

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
     */
    public ODOMOutlinePage(ODOMEditorContext context,
                           String[] skipElements,
                           String[] stopElements,
                           boolean attributesAsChildren,
                           ILabelProvider labelProvider,
                           MenuManager menuManager) {
        this(context,
                null,
                null, skipElements,
                stopElements,
                attributesAsChildren,
                labelProvider,
                menuManager,
                null
        );
    }

    /**
     * Construct a new ODOMOutlinePage
     * @param context The ODOMEditorContext from which to derive the
     * root element of the tree view and possible other useful information.
     * @param rootElement The root element that will be used as the root of the
     * outline view. If this is null then context.getRootElement() will be
     * used.
     * @param selectionFilter The ODOMSelectionFilter used to filter selections
     * listened to by this ODOMOutlinePage. Can be null.
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
     * @param problemMarkerFinder a <code>ProblemMarkerFinder</code> that can
     * be used to locate problems. Can be null.
     */
    public ODOMOutlinePage(ODOMEditorContext context,
                           ODOMElement rootElement,
                           ODOMSelectionFilter selectionFilter,
                           String[] skipElements,
                           String[] stopElements,
                           boolean attributesAsChildren,
                           ILabelProvider labelProvider,
                           MenuManager menuManager,
                           ProblemMarkerFinder problemMarkerFinder) {

        this.context = context;
        this.rootElement = rootElement != null ? rootElement :
                context.getRootElement();
        this.selectionFilter = selectionFilter;
        this.skipElements = skipElements;
        this.stopElements = stopElements;
        this.attributesAsChildren = attributesAsChildren;
        this.menuManager = menuManager;
        this.problemMarkerFinder = problemMarkerFinder;

        if (labelProvider == null && attributesAsChildren) {
            // The default label provider has a configuration of
            // ELEMENT_AND_ATTRIBUTES which does not support attributes
            // as children.
            throw new IllegalStateException(
                    "If attributesAsChildren is true the " +
                    "labelProvider must be non-null and have " +
                    "a configuration of JUST_OBSERVABLE or " +
                    "JUST_ELEMENTS.");
        }

        if (labelProvider != null) {
            this.labelProvider = labelProvider;
        } else {
            ODOMLabelProviderConfiguration config =
                    ODOMLabelProviderConfiguration.ELEMENT_AND_ATTRIBUTES;
            this.labelProvider =
                    new ODOMLabelProvider(config, null, null, null);
        }
    }

    /**
     * Extends its superclass method by making contributions to the global
     * actions, with the undo/redo actions common to the ODOM editor associated
     * with this view.
     * <p>
     * This is suggested by the documentation (implementation comment) for
     * {@link org.eclipse.ui.part.Page#setActionBars} which says: <br/>
     * <em> (non-Javadoc)
     * This method exists for backward compatibility.
     * Subclasses should reimplement <code>init</code>. </em>
     * </p>
     *
     * @param pageSite
     */
    public void init(IPageSite pageSite) {
        /* Note : the order in which the eclipse calls us is :
         *
         *   ODOMOutlinePage.init
         *   ODOMOutlinePage.createControl
         *   ODOMOutlinePage.setActionBars //not to be used here
         */
        super.init(pageSite);
        context.updatePageSiteActions(pageSite);
    }

    /**
     * Override createControl to configure the TreeViewer for this
     * ODOMOutlinePage. This method must call super.createControl() so
     * that the TreeViewer is instantiated by the parent class.
     */
    // rest of javadoc inherited
    public void createControl(Composite parent) {
        super.createControl(parent);

        final ElementChildrenTreeContentProvider contentProvider =
                new ElementChildrenTreeContentProvider(skipElements,
                        stopElements, attributesAsChildren, false);

        final TreeViewer treeViewer = getTreeViewer();
        final ODOMSelectionManager selectionManager =
                context.getODOMSelectionManager();
        treeViewer.addSelectionChangedListener(selectionManager);

        ODOMElementSelectionListener selectionListener =
                new ODOMElementSelectionListener() {
                    public void
                            selectionChanged(ODOMElementSelectionEvent event) {
                        treeViewer.
                                removeSelectionChangedListener(selectionManager);
                        treeViewer.setSelection(event.getSelection(), true);
                        treeViewer.addSelectionChangedListener(selectionManager);
                    }
                };

        selectionManager.addSelectionListener(selectionListener,
                selectionFilter);

        treeViewer.setContentProvider(contentProvider);



        // Set up the decorator
        Tree tree = (Tree) treeViewer.getControl();
        ItemContainer container = new TreeItemContainer(tree);
        // create the label decorator
        ILabelDecorator decorator = createLabelDecorator(container);
        DecoratingLabelProvider dlp =
                new DecoratingLabelProvider(labelProvider, decorator);

        treeViewer.setLabelProvider(dlp);

        // Ensure the labelProvider dispose method is called when the tree
        // is disposed.
        tree.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                labelProvider.dispose();
            }
        });

        treeViewer.setInput(rootElement);
        treeViewer.expandToLevel(2);

        // Add an ODOMChangeListener to the root element to allow us to
        // update/refresh the table as appropriate.
        context.addChangeListener(new ODOMChangeListener() {
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {

                final ODOMElement contextRootElement = context.getRootElement();
                if (rootElement == null && contextRootElement != null ||
                    rootElement != null && !rootElement.equals(contextRootElement)) {
                    updateRootElement();
                } else if (event.getChangeQualifier() == ChangeQualifier.HIERARCHY) {
                    treeViewer.refresh();
                } else {
                    // If the source is an attribute then it is the parent
                    // element that we want to update. Otherwise just
                    // use the source as the element to update.
                    Object source = event.getSource();
                    if (source instanceof ODOMAttribute) {
                        source = ((ODOMAttribute) source).getParent();
                    }

                    treeViewer.update(source, null);
                }
            }
        });

        // Create a ValidationListener and add it to the XMLValidator
        // associated with the ODOMEditorContext so that we are notified
        // when validation occurs allowing us to refresh and if necessary
        // redecorate images that indicate or should not now indicate errors.
        final ValidationListener validationListener = new ValidationListener() {
            public void validated() {
                if (!treeViewer.getControl().isDisposed()) {
                    treeViewer.getControl().getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            // ensure that the widget still exists when this
                            // runnable is run
                            if (!treeViewer.getControl().isDisposed()) {
                                treeViewer.refresh();
                            }
                        }
                    });
                }
            }
        };
        context.addValidationsListener(validationListener);

        if (menuManager != null) {
            Menu menu = menuManager.createContextMenu(tree);
            tree.setMenu(menu);
        }
    }

    /**
     * Get the ODOMEditorContext associated with this ODOMOutlinePage.
     * @return the ODOMEditorContext
     */
    protected ODOMEditorContext getODOMEditorContext() {
        return context;
    }

    /**
     * Updates the stored rootElement reference from the ODOMEditorContext and
     * sets the new one as the input of the tree view.
     */
    protected void updateRootElement() {
        rootElement = context.getRootElement();
        getTreeViewer().setInput(rootElement);
    }

    /**
     * Creates the ILabelDecorator that the outline pages LabelProvider
     * requires
     * @param container an ItemContainer instance.
     * @return a ILableDecorator
     */
    private ILabelDecorator createLabelDecorator(ItemContainer container) {
        return (problemMarkerFinder == null)
                ? new ODOMLabelDecorator(context.getPolicyResource(), container)
                : new ODOMLabelDecorator(context.getPolicyResource(),
                        problemMarkerFinder,
                        container);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6749/2	allan	VBM:2005012102 Drag n Drop support

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Sep-04	5369/1	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 15-Jul-04	4886/3	allan	VBM:2004052812 Tidied some more and added basic enable tests.

 15-Jul-04	4886/1	allan	VBM:2004052812 Workaround TreeItem.getItems() bug and tidy up.

 13-May-04	4321/1	doug	VBM:2004051202 Added label decorating to the device hierarchy tree

 12-May-04	4281/1	matthew	VBM:2004051103 ODOMOutlinePage and AlertsActionsSection modified to stop Widget is disposed errors due to multithreading and callbacks

 06-May-04	4068/2	allan	VBM:2004032103 Structure page policies section.

 05-May-04	4115/2	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 29-Apr-04	4072/4	matthew	VBM:2004042601 Sorting of device hierarchy views removed

 29-Apr-04	4072/1	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 22-Apr-04	3878/3	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 22-Apr-04	3878/1	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 30-Mar-04	3614/1	byron	VBM:2004022404 Layout: Panes are allowed same names

 24-Feb-04	3115/5	doug	VBM:2004021023 Fixed theme selection issue

 24-Feb-04	3115/3	doug	VBM:2004021023 Fixed theme selection issue

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 16-Feb-04	2891/3	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - added comments and renamed methods

 16-Feb-04	2891/1	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - updated FormatAttributesView hierarchy as per rework item

 09-Feb-04	2800/2	eduardo	VBM:2004012802 undo redo works from outline view

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Jan-04	2659/6	allan	VBM:2003112801 The ThemeDesign Editor

 22-Jan-04	2659/1	allan	VBM:2003112801 Fix selection filtering and add basic cell editing.

 23-Jan-04	2733/2	doug	VBM:2003112801 moved important.gif into the redist dir for theme icons

 22-Jan-04	2659/1	allan	VBM:2003112801 Fix selection filtering and add basic cell editing.

 23-Jan-04	2720/1	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 19-Jan-04	2562/3	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 ===========================================================================
*/
