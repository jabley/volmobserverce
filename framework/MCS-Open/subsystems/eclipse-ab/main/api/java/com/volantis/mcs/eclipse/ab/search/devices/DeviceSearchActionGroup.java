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
package com.volantis.mcs.eclipse.ab.search.devices;

import com.volantis.mcs.eclipse.ab.search.SearchMessages;
import com.volantis.mcs.eclipse.ab.search.SearchSupport;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.search.ui.IContextMenuConstants;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.actions.OpenFileAction;
import org.eclipse.ui.actions.OpenWithMenu;
import org.eclipse.ui.dialogs.PropertyDialogAction;

/**
 * ActionGroup that provides contextual and global actions for the
 * device search result view.
 */
public class DeviceSearchActionGroup extends ActionGroup {
    /**
     * Constants for prefix of resources associated with this class.
     */
    private static final String RESOURCE_PREFIX = "DeviceSearchActionGroup.";

    /**
     * The ISelectionProvider that provides the selection to which the
     * action is applied.
     */
    private ISelectionProvider selectionProvider;

    /**
     * The IWorkbench page associated with this ActionGroup.
     */
    private IWorkbenchPage workbenchPage;

    /**
     * The OpenDeviceRepository open action that will open the selected
     * device repository and select the selected device.
     */
    private OpenDeviceRepositoryAction openAction;

    /**
     * Action to provide the properties for a selected device respository.
     */
    private PropertyDialogAction propertiesAction;

    /**
     * Construct a new DeviceSearchActionGroup.
     * @param part the IViewPart within which the DeviceSearchActionGroup
     * performs.
     */
    public DeviceSearchActionGroup(IViewPart part) {
        assert(part != null);
        IWorkbenchPartSite site = part.getSite();
        selectionProvider = site.getSelectionProvider();
        workbenchPage = site.getPage();
        propertiesAction = new PropertyDialogAction(site.getShell(),
                selectionProvider);
        openAction = new OpenDeviceRepositoryAction();

        ISelection selection = selectionProvider.getSelection();

        // The PropertiesDialogAction and its parent have two different
        // selectionChanged methods one for IStructuredSelection and another
        // for ISelection hence the reason for this condition.
        if (selection instanceof IStructuredSelection) {
            propertiesAction.selectionChanged((IStructuredSelection) selection);
        } else {
            propertiesAction.selectionChanged(selection);
        }

    }

    /**
     * Fill a content menu with the actions associated with this ActionGroup.
     * @param menu the menu or rather MenuManger to fill.
     */
    public void fillContextMenu(IMenuManager menu) {
        // view must exist if we create a context menu for it.
        ISelection selection = getContext().getSelection();
        if (selection instanceof IStructuredSelection) {
            addOpenWithMenu(menu, (IStructuredSelection) selection);

            if (propertiesAction != null && propertiesAction.isEnabled() &&
                    selection != null &&
                    propertiesAction.
                    isApplicableForSelection((IStructuredSelection) selection)) {
                menu.appendToGroup(IContextMenuConstants.GROUP_PROPERTIES,
                        propertiesAction);
            }
        }

    }

    /**
     * Add the "Open With" and "Open" kinds of menu options.
     * @param menu the the menu or rather MenuManger to fill.
     * @param selection upon with to decide what open options to provide
     */
    private void addOpenWithMenu(IMenuManager menu,
                                 IStructuredSelection selection) {
        if (selection != null && selection.size() == 1) {
            Object o = selection.getFirstElement();

            if (o instanceof IAdaptable) {
                openAction.selectionChanged(selection);
                menu.appendToGroup(IContextMenuConstants.GROUP_OPEN,
                        openAction);

                // Create menu
                IMenuManager submenu = new MenuManager(SearchMessages.
                        getString(RESOURCE_PREFIX + "openWith.label"));
                submenu.add(new OpenWithMenu(workbenchPage, (IAdaptable) o));

                // Add the submenu.
                menu.appendToGroup(IContextMenuConstants.GROUP_OPEN, submenu);
            }
        }
    }

    // javadoc inherited
    public void fillActionBars(IActionBars actionBar) {
        super.fillActionBars(actionBar);
        setGlobalActionHandlers(actionBar);
    }

    /**
     * Set up the global action handlers for this ActionGroup so the
     * required actions appear in the main menu.
     * @param actionBars the IActionBars to add the global actions to.
     */
    private void setGlobalActionHandlers(IActionBars actionBars) {
        actionBars.setGlobalActionHandler(ActionFactory.PROPERTIES.getId(),
                propertiesAction);
    }

    /**
     * Override OpenFileAction so that the selected device can be selected
     * in the device repository editor when opened.
     */
    class OpenDeviceRepositoryAction extends OpenFileAction {
        /**
         * Construct a new OpenDeviceRepositoryAction.
         */
        public OpenDeviceRepositoryAction() {
            super(workbenchPage);
        }

        /**
         * Override run to set the selection to the right device in
         * the opened device repository.
         */
        public void run() {

            IStructuredSelection structuredSelection =
                    getStructuredSelection();

            // The selection may contain IContainers as well as
            // DeviceSearchMatch objects for several different resources.
            // However we can only goto a selection when there is a single
            // selected object - at least that is the Eclipse convention anyway.
            if (structuredSelection.size() == 1 &&
                    structuredSelection.getFirstElement()
                    instanceof DeviceSearchMatch) {

                SearchSupport.gotoSelection(getStructuredSelection(),
                        workbenchPage);
            }
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

 08-Oct-04	5557/4	allan	VBM:2004070608 Unit tests and rework issues

 08-Oct-04	5557/2	allan	VBM:2004070608 Device search

 ===========================================================================
*/
