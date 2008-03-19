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

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.actions.GlobalActions;
import com.volantis.mcs.eclipse.ab.actions.ODOMAction;
import com.volantis.mcs.eclipse.ab.actions.layout.LayoutOutlinePage;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorPart;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMLabelFormatProvider;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMLabelProvider;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMLabelProviderConfiguration;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMOutlinePage;
import com.volantis.mcs.eclipse.ab.views.layout.FormatAttributesViewPage;
import com.volantis.mcs.eclipse.common.AttributesDetails;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.PolicyAttributesDetails;
import com.volantis.mcs.eclipse.common.odom.LPDMFilterFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelection;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.builder.common.InteractionFocussable;
import com.volantis.mcs.eclipse.builder.editors.common.DirtyStateListener;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.mcs.model.path.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.resources.IMarker;
import org.jdom.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The design page for the layout editor.
 */
public class LayoutDesignPart extends ODOMEditorPart
        implements InteractionFocussable {
    /**
     * The prefix for resources associated with LayoutDesignPart.
     */
    private static final String RESOURCE_PREFIX = "LayoutDesignPart.";

    /**
     * The bundle associated with LayoutDesignPart
     */
    private static final ResourceBundle BUNDLE =
            LayoutMessages.getResourceBundle();

    /**
     * The layout editor context for the file edited by this part.
     */
    private LayoutEditorContext editorContext;

    /**
     * The FormatAttributesViewPage associated with this LayoutDesignPart.
     */
    private FormatAttributesViewPage formatAttributesViewPage;

    /**
     * A listener for changes in the dirty state of the editor context that
     * propagates the change in dirty state to the Eclipse GUI.
     */
    private DirtyStateListener dirtyStateListener = new DirtyStateListener() {
        public void dirtyStateChanged(boolean dirty) {
            firePropertyChange(PROP_DIRTY);
        }
    };

    /**
     * The ODOMLabelFormatProvider for the content outline page
     * label provider.
     */
    private static final ODOMLabelFormatProvider LABEL_FORMAT_PROVIDER =
            new ODOMLabelFormatProvider() {
                public String provideLabelFormat(ODOMElement element) {
                    StringBuffer key = new StringBuffer();
                    key.append(RESOURCE_PREFIX).
                            append(element.getName()).
                            append(".summary");
                    String message =
                            EclipseCommonMessages.getString(BUNDLE,
                                    key.toString(), null);
                    return message;
                }
            };

    /**
     * AttributesDetails for use by the content outline page label provider.
     */
    private static final AttributesDetails ATTRIBUTES_DETAILS =
            new PolicyAttributesDetails(
                    LayoutSchemaType.LAYOUT.getName(), true);

    /**
     * Set up the 'stopElements' for the content outline page.
     */
    private static final String STOP_ELEMENTS [] = {
        LayoutSchemaType.GRID_FORMAT_COLUMNS_ELEMENT.getName(),
        LayoutSchemaType.SEGMENT_GRID_FORMAT_COLUMNS_ELEMENT.getName()
    };

    /**
     * Set up the 'skipElements' for the content outline page.
     */
    private static final String SKIP_ELEMENTS [] = {
        LayoutSchemaType.GRID_FORMAT_COLUMNS_ELEMENT.getName(),
        LayoutSchemaType.GRID_FORMAT_ROWS_ELEMENT.getName(),
        LayoutSchemaType.SEGMENT_GRID_FORMAT_COLUMNS_ELEMENT.getName(),
        LayoutSchemaType.SEGMENT_GRID_FORMAT_ROW_ELEMENT.getName()
    };

    /**
     * The shared filter used to register the individual {@link #listener}s.
     */
    private static final ODOMSelectionFilter FILTER;

    static {
        // Because the filter must be initialized with an element with the
        // required name, initialization must be done in a static block, thus:

        // The actual type of element created is not important, so a base JDOM
        // element is used here since that is what the filter factory requires
        Element deviceLayout = new Element(
                LayoutSchemaType.LAYOUT.getName());

        FILTER = LPDMFilterFactory.createAssetFilter(deviceLayout);
    }

    /**
     * The map of actions available, indexed by their action ID.
     * @todo actions are never stored in this map, however {@link #dispose} expects them there!
     */
    private Map actions = new HashMap();

    /**
     * The LayoutDesigner for this LayoutDesignPart.
     */
    private LayoutDesigner layoutDesigner;

    /**
     * This listener used to track device layout selections and to notify
     * the interested parties of this change.
     */
    private ODOMElementSelectionListener listener =
            new ODOMElementSelectionListener() {
                public void selectionChanged(ODOMElementSelectionEvent event) {
                    ODOMElementSelection selection =
                            event.getSelection();
                    ODOMElement deviceLayout = selection.size() != 1 ? null :
                            (ODOMElement) selection.getFirstElement();

                    // Tell the layout designer of the single selected device
                    // layout or that there is none/more than one by passing null
                    layoutDesigner.updateDesigner(deviceLayout);
                }
            };

    // Javadoc inherited
    public boolean isDirty() {
        return editorContext.isDirty();
    }

    /**
     * Construct a new LayoutDesignPart.
     *
     * @param context The ODOMEditorContext for this ODOMEditorPart
     * @param editorContext The layout editor context for this part
     */
    public LayoutDesignPart(ODOMEditorContext context,
                            LayoutEditorContext editorContext) {
        super(
            (context.getRootElement() != null?
                context.getRootElement().getName() : ""),
            context);
        this.editorContext = editorContext;
        editorContext.addDirtyStateListener(dirtyStateListener);
    }

    public void setFocus() {
    }

    /**
     * Override getAdapter() to provide the ODOMOutlinePage to callers.
     */
    // rest of javadoc inherited
    public Object getAdapter(Class required) {
        Object adapter;
        LayoutODOMEditorContext context =
            (LayoutODOMEditorContext) getODOMEditorContext();
        if (IContentOutlinePage.class.equals(required)) {
            // Create a configuration that ensures labels are retrieved
            // from the resources required by this class.
            ODOMLabelProviderConfiguration config =
                    ODOMLabelProviderConfiguration.ELEMENT_AND_ATTRIBUTES;


            ODOMLabelProvider labelProvider =
                    new ODOMLabelProvider(config, null, null,
                            ATTRIBUTES_DETAILS, LABEL_FORMAT_PROVIDER);

            // Create the actions used by this part.
            Map actions = LayoutActions.createActions(context);

            // Extract the global actions from the actions. These are needed
            // by the outline page.
            GlobalActions globalActions =
                    LayoutActions.getGlobalActions(actions);
            MenuManager menuManager = createContextMenu(actions);

            // Note that the outline page is not cached because the page
            // is disposed if the user closes the view and if re-opened
            // must be recreated. There is no dispose listener interface
            // for view parts.
            ODOMOutlinePage outlinePage = new LayoutOutlinePage(context,
                    SKIP_ELEMENTS, STOP_ELEMENTS, false, labelProvider,
                    menuManager, globalActions);

            adapter = outlinePage;
        } else if (FormatAttributesViewPage.class.equals(required)) {
            formatAttributesViewPage =
                    new FormatAttributesViewPage(getODOMEditorContext());
            adapter = formatAttributesViewPage;
        } else {
            adapter = super.getAdapter(required);
        }

        return adapter;
    }



    // javadoc inherited
    protected void createPartControlImpl(Composite parent) {

        ODOMEditorContext context = getODOMEditorContext();

        context.getODOMSelectionManager().addSelectionListener(listener,
                FILTER);

        createMenu();
        createToolbar();


        // Create menu.
        createContextMenu(LayoutActions.createActions(context));

        layoutDesigner =
                new LayoutDesigner(parent, SWT.NONE, getODOMEditorContext());
    }

    // javadoc inherited
    public void dispose() {
        getODOMEditorContext().getODOMSelectionManager().
                removeSelectionListener(listener,
                        FILTER);

        // Dispose of the now defunct actions
        Iterator i = actions.keySet().iterator();
        IAction action;

        while (i.hasNext()) {
            action = (IAction) actions.get(i.next());

            if (action instanceof ODOMAction) {
                ((ODOMAction) action).dispose();
            }
        }

        super.dispose();
    }

    /**
     * Implemented by returning the focusable of the FormatAttributesViewPage
     * associated with this editor page.
     * <p>
     * If the FormatAttributesViewPage is not available, it is activated and
     * shown.
     * </p>
     * @todo needs the F.A.ViewPage activation to execute within a Workbench runnable ?
     */
    protected XPathFocusable[] getXPathFocusableControls() {
        try {
            IWorkbenchPage activePage =
                    PlatformUI.getWorkbench().
                    getActiveWorkbenchWindow().
                    getActivePage();
            activePage.showView("com.volantis.mcs.eclipse.ab.views" +
                    ".layout.FormatAttributesView");

        } catch (PartInitException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        if (formatAttributesViewPage == null) {
            return new XPathFocusable[0];
        }
        return formatAttributesViewPage.getXPathFocusableControls();
    }

    /**
     * Create menu.
     */
    private void createMenu() {
    }

    /**
     * Create toolbar.
     */
    private void createToolbar() {
    }

    /**
     * Creates and returns a menu manager appropriately configured to handle
     * the context menu on the outline page.
     *
     * @param actions the actions to use for populating the menus
     *
     * @return a menu manager designed to handle the context menu for the
     *         outline page
     */
    private MenuManager createContextMenu(Map actions) {
        // Create menu manager.
        MenuManager menuManager = new MenuManager();
        menuManager.setRemoveAllWhenShown(true);

        menuManager.addMenuListener(
                LayoutContextMenuGeneratingListenerFactory.
                getSingleton().
                createContextMenuGeneratingListener(
                        getODOMEditorContext(), actions));

        return menuManager;
    }

    // Javadoc inherited
    public void setFocus(Path path) {
        // TODO later Convert the path to an XPath and use this to set the focus
    }

    /**
     * Delegate save requests to the overview page - saving <strong>must</strong>
     * be handled by model-aware code.
     *
     * @param progressMonitor The progress monitor to display
     */
    public void doSave(IProgressMonitor progressMonitor) {
        LayoutOverview overview = editorContext.getLayoutOverview();
        if (overview != null) {
            overview.doSave(progressMonitor);
        }
    }

    /**
     * Delegate save requests to the overview page - saving <strong>must</strong>
     * be handled by model-aware code.
     */
    public void doSaveAs() {
        LayoutOverview overview = editorContext.getLayoutOverview();
        if (overview != null) {
            overview.doSaveAs();
        }
    }

    // javadoc inherited
    public void gotoMarker(IMarker marker) {
        // Layout editors no longer use ODOM validation, so the default
        // ODOM marker code throws an exception on finding no validator.
        // This method is overridden to prevent that from happening, and the
        // marker is handled in LayoutEditor#gotoMarker.
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-May-05	8295/1	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 17-May-05	8213/11	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 02-Feb-05	6749/2	allan	VBM:2005012102 Drag n Drop support

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Aug-04	5266/2	philws	VBM:2004081007 Fix layout context menu handling

 03-Aug-04	4902/1	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 14-Jul-04	4833/1	tom	VBM:2004052812 Added Swap Functionality

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 07-May-04	4195/1	tom	VBM:2004041902 Made .java files use the relevant resource bundles

 27-Apr-04	3983/2	matthew	VBM:2004040203 Fixes to setFocusWith(XPath)

 23-Mar-04	3389/3	byron	VBM:2004030905 NLV properties files need adding to build - missed some comments and exception handling

 23-Mar-04	3389/1	byron	VBM:2004030905 NLV properties files need adding to build

 17-Feb-04	2988/4	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 16-Feb-04	2891/1	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - updated FormatAttributesView hierarchy as per rework item

 09-Feb-04	2800/3	eduardo	VBM:2004012802 codestyle fixes

 09-Feb-04	2800/1	eduardo	VBM:2004012802 undo redo works from outline view

 09-Feb-04	2906/1	pcameron	VBM:2004020204 Added ColumnDeleteActionCommand

 05-Feb-04	2875/1	pcameron	VBM:2004020202 Added RowDeleteAction

 04-Feb-04	2848/6	pcameron	VBM:2004020203 Fixed merge problems

 04-Feb-04	2848/3	pcameron	VBM:2004020203 Added ColumnInsertActionCommand

 04-Feb-04	2820/6	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 03-Feb-04	2820/3	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 03-Feb-04	2824/1	pcameron	VBM:2004020201 Added RowInsertActionCommand

 02-Feb-04	2707/2	byron	VBM:2003121506 Eclipse PM Layout Editor: Format Attributes View: Row Page

 29-Jan-04	2797/3	philws	VBM:2004012903 Make the layout editor context menu device layout type sensitive

 29-Jan-04	2752/5	byron	VBM:2004012602 Fixed merge conflicts

 28-Jan-04	2752/2	byron	VBM:2004012602 Address issues from review

 28-Jan-04	2783/1	philws	VBM:2003121514 Implement Wrap Actions for the Layout Editor context menu

 28-Jan-04	2776/2	philws	VBM:2004012709 Add sub-menu actions

 27-Jan-04	2705/1	philws	VBM:2003121517 Full implementation of Paste and Replace without unit tests

 23-Jan-04	2727/3	philws	VBM:2004012301 Fix clipboard management

 23-Jan-04	2727/1	philws	VBM:2004012301 First draft of new, delete and clipboard actions in context menu

 23-Jan-04	2720/2	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 ===========================================================================
*/
