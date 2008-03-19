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

import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.jdom.Element;

import com.volantis.mcs.eclipse.ab.actions.DefaultActionCommand;
import com.volantis.mcs.eclipse.ab.actions.ODOMAction;
import com.volantis.mcs.eclipse.ab.actions.SubMenuAction;
import com.volantis.mcs.eclipse.ab.actions.layout.ActionID;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionListener;
import com.volantis.mcs.eclipse.common.odom.LPDMFilterFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelection;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.layouts.LayoutSchemaType;

/**
 * A singleton factory that provides context menu generating menu listeners
 * for Layouts.
 */
public class LayoutContextMenuGeneratingListenerFactory {
    /**
     * The bundle associated with LayoutDesignPart
     */
    private static final ResourceBundle BUNDLE =
            LayoutMessages.getResourceBundle();

    /**
     * The singleton.
     */
    private static final LayoutContextMenuGeneratingListenerFactory singleton =
            new LayoutContextMenuGeneratingListenerFactory();

    /**
     * ODOMSelectionFilter for filtering device layout selections.
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
     * The constructor. Private to enforce the singleton pattern.
     */
    private LayoutContextMenuGeneratingListenerFactory() {
    }

    /**
     * Provide the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static LayoutContextMenuGeneratingListenerFactory getSingleton() {
        return singleton;
    }

    /**
     * Create a new menu listener that is designed to be assigned to a menu
     * manager to populate the menu when shown. The menu manager to which the
     * listener is assigned should be set to "remove all when shown".
     *
     * @param context the ODOMEditorContext.
     * @param actions the actions used by the menu
     * @return an IMenuListener that will populate menus to contain all the
     *         actions approriate to the selected layout format.
     */
    public IMenuListener createContextMenuGeneratingListener(
            ODOMEditorContext context, Map actions) {

        // Populate, in a one-off manner, the various sub-menu actions with
        // the appropriate actions
        populateSubMenuActions(actions);
        return new ContextMenuListener(context, actions);
    }

    /**
     * An IMenuListener used to fill the menu with actions/menu-items upon
     * activation.
     */
    static class ContextMenuListener implements IMenuListener {
        /**
         * The current device layout selection.
         */
        private DeviceLayoutSelection deviceLayoutSelection =
                DeviceLayoutSelection.NONE;

        /**
         * The available actions.
         */
        private final Map actions;
        private boolean readOnly;
        private InteractionEventListener readOnlyListener;

        /**
         * Construct a new ConstextMenuListener.
         * @param context the ODOMEditorContext.
         * @param actions the actions available to the menu.
         */
        public ContextMenuListener(ODOMEditorContext context, Map actions) {
            ODOMElementSelectionListener listener =
                createSelectionListener(context);
            context.getODOMSelectionManager().addSelectionListener(listener,
                    FILTER);
            final LayoutODOMEditorContext odomEditorContext =
                (LayoutODOMEditorContext) context;
            final LayoutEditorContext layoutEditorContext =
                odomEditorContext.getLayoutEditorContext();
            readOnlyListener = new InteractionEventListenerAdapter() {
                public void readOnlyStateChanged(
                    final ReadOnlyStateChangedEvent event) {
                    readOnly = event.isReadOnly();
                }
            };
            layoutEditorContext.addSelectedVariantListener(new BuilderSelectionListener() {
                public void selectionMade(final BuilderSelectionEvent event) {
                    if (event.getOldSelection() != null) {
                        ((Proxy) event.getOldSelection()).removeListener(readOnlyListener);
                    }

                    if (event.getSelection() == null) {
                        readOnly = true;
                    } else {
                        BeanProxy selectedVariant = (BeanProxy) event.getSelection();
                        selectedVariant.addListener(readOnlyListener, false);
                    }
                }
            });
            this.actions = actions;
        }


        /**
         * Implement menuAboutToShow to fill the menu with the appropriate
         * actions.
         * @param mgr the IMenuManager to associate with the actions.
         */
        public void menuAboutToShow(IMenuManager mgr) {        	
            fillContextMenu((MenuManager) mgr, actions, deviceLayoutSelection, readOnly);
        }

        /**
         * Create a selection listener that will listen for changes of
         * device layout selection and update the current selection.
         * @param context ODOM editor context to get the read only status from
         * @return an ODOMElementSelectionListener
         */
        private ODOMElementSelectionListener createSelectionListener(
                final ODOMEditorContext context) {

            ODOMElementSelectionListener listener =
                    new ODOMElementSelectionListener() {
                        public void selectionChanged(ODOMElementSelectionEvent event) {
                            ODOMElementSelection selection =
                                    event.getSelection();

                            // Determine the current selection state for use in generating
                            // the context menu content
                            deviceLayoutSelection = DeviceLayoutSelection.NONE;

                            for (Iterator i = selection.iterator();
                                 i.hasNext();) {
                                final ODOMElement deviceLayout =
                                    (ODOMElement) i.next();

                                if (deviceLayout.getName().equals(
                                        LayoutSchemaType.CANVAS_LAYOUT.
                                        getName())) {
                                    if (deviceLayoutSelection ==
                                            DeviceLayoutSelection.NONE) {
                                        deviceLayoutSelection =
                                                DeviceLayoutSelection.CANVAS;
                                    } else if (deviceLayoutSelection !=
                                            DeviceLayoutSelection.CANVAS) {
                                        deviceLayoutSelection =
                                                DeviceLayoutSelection.MIXED;
                                    }
                                } else if (deviceLayout.getName().equals(
                                        LayoutSchemaType.MONTAGE_LAYOUT.
                                        getName())) {
                                    if (deviceLayoutSelection ==
                                            DeviceLayoutSelection.NONE) {
                                        deviceLayoutSelection =
                                                DeviceLayoutSelection.MONTAGE;
                                    } else if (deviceLayoutSelection !=

                                            DeviceLayoutSelection.MONTAGE) {
                                        deviceLayoutSelection =
                                                DeviceLayoutSelection.MIXED;
                                    }
                                }
                            }

                            final LayoutODOMEditorContext odomEditorContext =
                                (LayoutODOMEditorContext) context;
                            final LayoutEditorContext layoutEditorContext =
                                odomEditorContext.getLayoutEditorContext();
                            readOnly =
                                layoutEditorContext.isSelectedVariantReadOnly();
                        }
                    };

            return listener;
        }

    }


    /**
     * Typesafe enumerator describing a device layout selection.
     */
    private static final class DeviceLayoutSelection {
        /**
         * Literal used to indicate a selection of just montage device layouts
         */
        public static final DeviceLayoutSelection MONTAGE =
                new DeviceLayoutSelection("montage"); //$NON-NLS-1$

        /**
         * Literal used to indicate a selection of just canvas device layouts
         */
        public static final DeviceLayoutSelection CANVAS =
                new DeviceLayoutSelection("canvas"); //$NON-NLS-1$

        /**
         * Literal used to indicate a mixed selection of canvas and montage
         * device layouts
         */
        public static final DeviceLayoutSelection MIXED =
                new DeviceLayoutSelection("mixed"); //$NON-NLS-1$

        /**
         * Literal used to indicate no selected device layouts
         */
        public static final DeviceLayoutSelection NONE =
                new DeviceLayoutSelection("none"); //$NON-NLS-1$

        /**
         * String version of the literal. For debug purposes only.
         */
        private String name;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param name the debug name for this literal instance
         */
        private DeviceLayoutSelection(String name) {
            this.name = name;
        }

        /**
         * For debug purposes only.
         *
         * @return the string version of the literal
         */
        public String toString() {
            return name;
        }
    }
    
    /**
     * This method is used to populate the context menu with menu items derived
     * from the actions created in {@link LayoutActions#createActions}. The set
     * of actions needed will depend on the type and number of device layout
     * selections.
     *
     * @param mgr the menu manager used to create the main part of the context
     * @param readOnly
     */
    private static void fillContextMenu(MenuManager mgr, Map actions,
                                        DeviceLayoutSelection deviceLayoutSelection,
                                        boolean readOnly) {
    	
        // Conditionally populate the New and Wrap sub-menus depending on the
        // device layout selection(s)
        if (deviceLayoutSelection == DeviceLayoutSelection.CANVAS) {
            final IAction newAction = (IAction) actions.get(ActionID.CANVAS_NEW_MENU);
            newAction.setEnabled(!readOnly && newAction.isEnabled());
            mgr.add(newAction);
            final IAction wrapAction = (IAction) actions.get(ActionID.CANVAS_WRAP_MENU);
            wrapAction.setEnabled(!readOnly && wrapAction.isEnabled());
            mgr.add(wrapAction);
        } else if (deviceLayoutSelection == DeviceLayoutSelection.MONTAGE) {
            final IAction newAction = (IAction) actions.get(ActionID.MONTAGE_NEW_MENU);
            newAction.setEnabled(!readOnly && newAction.isEnabled());
            mgr.add(newAction);
            final IAction wrapAction = (IAction) actions.get(ActionID.MONTAGE_WRAP_MENU);
            wrapAction.setEnabled(!readOnly && wrapAction.isEnabled());
            mgr.add(wrapAction);
        } else {
            final IAction newAction = (IAction) actions.get(ActionID.UNDEFINED_NEW_MENU);
            newAction.setEnabled(!readOnly && newAction.isEnabled());
            mgr.add(newAction);
            final IAction wrapAction = (IAction) actions.get(ActionID.UNDEFINED_WRAP_MENU);
            wrapAction.setEnabled(!readOnly && wrapAction.isEnabled());
            mgr.add(wrapAction);
        }

        // Add the clipboard actions to the top-level menu
        mgr.add(new Separator()); // @todo have a group name?
        final IAction cutAction = (IAction) actions.get(ActionID.CUT);
        cutAction.setEnabled(!readOnly && cutAction.isEnabled());
        mgr.add(cutAction);
        mgr.add((IAction) actions.get(ActionID.COPY));
        final IAction pasteAction = (IAction) actions.get(ActionID.PASTE);
        pasteAction.setEnabled(!readOnly && pasteAction.isEnabled());
        mgr.add(pasteAction);

        // Add deletion actions to the top-level menu
        mgr.add(new Separator()); // @todo have a group name?
        final IAction deleteAction = (IAction) actions.get(ActionID.DELETE);
        deleteAction.setEnabled(!readOnly && deleteAction.isEnabled());
        mgr.add(deleteAction);

        // Add the miscellaneous actions to the top-level menu
        mgr.add(new Separator()); // @todo have a group name?
        final IAction replaceAction = (IAction) actions.get(ActionID.REPLACE);
        replaceAction.setEnabled(!readOnly && replaceAction.isEnabled());
        mgr.add(replaceAction);
        final IAction swapAction = (IAction) actions.get(ActionID.SWAP);
        swapAction.setEnabled(!readOnly && swapAction.isEnabled());
        mgr.add(swapAction);

        // Add the Grid manipulation sub-menu
        final IAction gridAction = (IAction) actions.get(ActionID.GRID_MODIFY_MENU);
        gridAction.setEnabled(!readOnly && gridAction.isEnabled());
        mgr.add(gridAction);

        // Complete the top-level menu
        mgr.add((IAction) actions.get(ActionID.SHOW_ATTRIBUTE_VIEW));
    }

    /**
     * Populates all sub-menu actions with the appropriate actions.
     */
    protected static void populateSubMenuActions(Map actions) {
        populateCanvasSubMenuActions(actions);
        populateMontageSubMenuActions(actions);
        populateUndefinedSubMenuActions(actions);

        SubMenuAction gridModifyMenu =
                (SubMenuAction) actions.get(ActionID.GRID_MODIFY_MENU);

        gridModifyMenu.add((IAction) actions.get(ActionID.INSERT_COLUMNS));
        gridModifyMenu.add((IAction) actions.get(ActionID.INSERT_ROWS));
        gridModifyMenu.add((IAction) actions.get(ActionID.DELETE_COLUMN));
        gridModifyMenu.add((IAction) actions.get(ActionID.DELETE_ROW));
    }

    /**
     * Populates the canvas-specific sub-menu actions with the appropriate
     * actions.
     */
    protected static void populateCanvasSubMenuActions(Map actions) {
        SubMenuAction newMenu =
                (SubMenuAction) actions.get(ActionID.CANVAS_NEW_MENU);

        // Build the New Pane sub-menu
        SubMenuAction newPaneMenu =
                (SubMenuAction) actions.get(ActionID.NEW_PANE_MENU);

        newMenu.add(newPaneMenu);

        newPaneMenu.add((IAction) actions.get(ActionID.NEW_PANE));
        newPaneMenu.add((IAction) actions.get(ActionID.NEW_ROW_ITERATOR_PANE));
        newPaneMenu.add((IAction) actions.get(ActionID.NEW_COLUMN_ITERATOR_PANE));
        newPaneMenu.add((IAction) actions.get(ActionID.NEW_DISSECTING_PANE));

        // Build the New Iterator sub-menu
        SubMenuAction newIteratorMenu =
                (SubMenuAction) actions.get(ActionID.NEW_ITERATOR_MENU);

        newMenu.add(newIteratorMenu);

        newIteratorMenu.add((IAction) actions.get(ActionID.NEW_SPATIAL_ITERATOR));
        newIteratorMenu.add((IAction) actions.get(ActionID.NEW_TEMPORAL_ITERATOR));

        // Build the New Grid sub-menu
        SubMenuAction newGridMenu =
                (SubMenuAction) actions.get(ActionID.NEW_GRID_MENU);

        newMenu.add(newGridMenu);

        newGridMenu.add((IAction) actions.get(ActionID.NEW_2_COLUMN_GRID));
        newGridMenu.add((IAction) actions.get(ActionID.NEW_3_ROW_GRID));
        newGridMenu.add((IAction) actions.get(ActionID.NEW_N_BY_M_GRID));

        // Complete the New sub-menu
        newMenu.add((IAction) actions.get(ActionID.NEW_FRAGMENT));
        newMenu.add((IAction) actions.get(ActionID.NEW_FORM));
        newMenu.add((IAction) actions.get(ActionID.NEW_REGION));
        newMenu.add((IAction) actions.get(ActionID.NEW_REPLICA));
        newMenu.add((IAction) actions.get(ActionID.NEW_FORM_FRAGMENT));

        // Build the Wrap sub-menu
        SubMenuAction wrapMenu =
                (SubMenuAction) actions.get(ActionID.CANVAS_WRAP_MENU);

        // Build the Wrap Iterator sub-menu
        SubMenuAction wrapIteratorMenu =
                (SubMenuAction) actions.get(ActionID.WRAP_ITERATOR_MENU);

        wrapMenu.add(wrapIteratorMenu);

        wrapIteratorMenu.add((IAction) actions.get(ActionID.WRAP_SPATIAL_ITERATOR));
        wrapIteratorMenu.add((IAction) actions.get(ActionID.WRAP_TEMPORAL_ITERATOR));

        // Complete the Wrap sub-menu
        wrapMenu.add((IAction) actions.get(ActionID.WRAP_N_BY_M_GRID));
        wrapMenu.add((IAction) actions.get(ActionID.WRAP_FRAGMENT));
        wrapMenu.add((IAction) actions.get(ActionID.WRAP_FORM));
        wrapMenu.add((IAction) actions.get(ActionID.WRAP_FORM_FRAGMENT));
    }

    /**
     * Populates the montage-specific sub-menu actions with the appropriate
     * actions.
     */
    protected static void populateMontageSubMenuActions(Map actions) {
        // Build the New sub-menu
        SubMenuAction newMenu =
                (SubMenuAction) actions.get(ActionID.MONTAGE_NEW_MENU);

        // Build the New Grid sub-menu
        SubMenuAction newGridMenu =
                (SubMenuAction) actions.get(ActionID.NEW_SEGMENT_GRID_MENU);

        newMenu.add(newGridMenu);

        newGridMenu.add((IAction) actions.get(ActionID.NEW_2_COLUMN_SEGMENT_GRID));
        newGridMenu.add((IAction) actions.get(ActionID.NEW_3_ROW_SEGMENT_GRID));
        newGridMenu.add((IAction) actions.get(ActionID.NEW_N_BY_M_SEGMENT_GRID));

        // Complete the New sub-menu
        newMenu.add((IAction) actions.get(ActionID.NEW_SEGMENT));

        // Build the Wrap sub-menu
        SubMenuAction wrapMenu =
                (SubMenuAction) actions.get(ActionID.MONTAGE_WRAP_MENU);

        // Complete the Wrap sub-menu
        wrapMenu.add((IAction) actions.get(ActionID.WRAP_N_BY_M_SEGMENT_GRID));
    }

    /**
     * Populates the undefined device layout-specific sub-menu actions with the
     * appropriate actions.
     */
    protected static void populateUndefinedSubMenuActions(Map actions) {
        // Build the New sub-menu
        SubMenuAction newMenu =
                (SubMenuAction) actions.get(ActionID.UNDEFINED_NEW_MENU);

        newMenu.add(new ODOMAction(new DefaultActionCommand(),
                BUNDLE,
                "dummy")); //$NON-NLS-1$

        // Build the Wrap sub-menu
        SubMenuAction wrapMenu =
                (SubMenuAction) actions.get(ActionID.UNDEFINED_WRAP_MENU);

        wrapMenu.add(new ODOMAction(new DefaultActionCommand(),
                BUNDLE,
                "dummy")); //$NON-NLS-1$
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-May-05	8295/1	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 17-May-05	8213/6	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Aug-04	5266/3	philws	VBM:2004081007 Fix layout context menu handling

 03-Aug-04	4902/2	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 ===========================================================================
*/
