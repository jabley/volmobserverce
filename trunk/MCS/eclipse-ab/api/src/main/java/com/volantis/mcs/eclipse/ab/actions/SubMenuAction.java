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
package com.volantis.mcs.eclipse.ab.actions;

import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;

/**
 * An ODOMAction that behaves as a drop down menu. The actions to be displayed
 * must be added to the action, in display order, via the {@link #add} method.
 * If the set of actions must be changed, the {@link #clear} method can be
 * called prior to calling {@link #add}. Note, however, that the menus created
 * by this action may be cached elsewhere (e.g. within a MenuManager) so
 * additional processing will be required to get the displayed versions of the
 * sub-menu to be updated.
 */
public class SubMenuAction extends ODOMAction {
    /**
     * The required actions, in required display order.
     *
     * @supplierRole actions
     * @supplierCardinality 0..*
     * @associates <{IAction}>
     */
    private List actions = new ArrayList();

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param command the command that actually determines enablement and
     *                performs action processing
     * @param bundle  the bundle from which the action's properties will be
     *                obtained. Must be specified
     * @param prefix  the resource naming prefix. May be null
     */
    public SubMenuAction(ODOMActionCommand command,
                         ResourceBundle bundle,
                         String prefix) {
        this(command, null, null, bundle, prefix);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param command       the command that actually determines enablement and
     *                      performs action processing
     * @param editorContext the ODOMEditorContext that can provide this action
     *                      with a selection manager and an undoRedo manager.
     *                      May be null
     * @param filter        the filter that should be used when registering the
     *                      action. May be, and should commonly be, null since
     *                      all selections are probably important to this action
     *                      in determining its enablement status
     * @param bundle        the bundle from which the action's properties will
     *                      be obtained. Must be specified
     * @param prefix        the resource naming prefix. May be null
     */
    public SubMenuAction(ODOMActionCommand command,
                         ODOMEditorContext editorContext,
                         ODOMSelectionFilter filter,
                         ResourceBundle bundle,
                         String prefix) {
        super(command,
              editorContext,
              filter,
              bundle,
              prefix,
              IAction.AS_DROP_DOWN_MENU);

        // A separately implemented IMenuCreator is required since the
        // {@link #dispose} and {@link IMenuCreator#dispose} methods have
        // very different lifecycle approaches. The {@link #dispose} method
        // will be called when the action is finished with, while the
        // {@link IMenuCreator#dispose} method is created when the menu is
        // finished with, which will typically happen several times against
        // the same action.
        setMenuCreator(new IMenuCreator() {
            // javadoc inherited
            public void dispose() {
                // Nothing done here; disposal has been arranged via dispose
                // listeners on the generated menus' parent by the {@link
                // #getMenu(Control)} and {@link #getMenu(Menu)} methods.

                // It's not feasible to dispose of the menu here because we
                // don't know which instance to dispose of!
            }

            // javadoc inherited
            public Menu getMenu(Control parent) {
                return setup(parent, new Menu(parent));
            }

            // javadoc inherited
            public Menu getMenu(Menu parent) {
                return setup(parent, new Menu(parent));
            }

            /**
             * Sets up the menu with content and a disposal listener.
             *
             * @param parent the notional parent of the menu
             * @param menu the menu to be set up
             * @return the menu that has been set up
             */
            private Menu setup(Widget parent, final Menu menu) {
                populateMenu(menu);

                // We are using a single menu creator to handle potentially
                // multiple menus so instead of implementing the {@link
                // #dispose} method we use a dispose listener to do the job
                // (IMenuCreator's javadoc indicates that the menus *must* be
                // explicitly disposed).
                parent.addDisposeListener(new DisposeListener() {
                    public void widgetDisposed(DisposeEvent e) {
                        menu.dispose();
                    }
                });

                return menu;
            }
        });
    }

    /**
     * Allows actions to be registered for display in the menu. The order of
     * addition dictates the order of display.
     *
     * @param action the action to be added to the menu
     */
    public void add(IAction action) {
        actions.add(action);
    }

    /**
     * Allows all existing actions to be removed from the menu configuration.
     */
    public void clear() {
        actions.clear();
    }

    /**
     * Populates the given menu with menu items based on the various registered
     * actions.
     *
     * @param menu the new menu to be populated
     */
    protected void populateMenu(Menu menu) {
        IAction action;

        // Populate the menu with the set of required actions
        for (Iterator i = actions.iterator();
             i.hasNext();) {
            action = (IAction)i.next();

            addActionToMenu(menu, action);
        }
    }

    /**
     * Adds the specified action as a menu item to the given menu.
     *
     * @param parent the menu to which the action is to be added
     * @param action the action to be added as a menu item
     */
    protected void addActionToMenu(Menu parent, IAction action) {
        ActionContributionItem item = new ActionContributionItem(action);

        item.fill(parent, -1);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Aug-04	5266/1	philws	VBM:2004081007 Fix layout context menu handling

 09-Feb-04	2800/1	eduardo	VBM:2004012802 undo redo works from outline view

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 29-Jan-04	2797/1	philws	VBM:2004012903 Make the layout editor context menu device layout type sensitive

 28-Jan-04	2776/1	philws	VBM:2004012709 Add sub-menu actions

 ===========================================================================
*/
