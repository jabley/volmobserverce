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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Manage the global actions available on a Viewer i.e. the swapping of these
 * actions in and out of the actions bars depending on the focus of the viewer.
 */
public class ViewerGlobalActionManager {

    /**
     * The IActionBars from which to get/set the global actions.
     */
    private final IActionBars actionBars;

    /**
     * A Map mapping actual action instances to their ActionFactory key.
     */
    private Map actionMap = new HashMap();

    /**
     * A Map mapping the original global actions within the actionBars to
     * their ActionFactory key.
     */
    private Map origActionMap = new HashMap();

    /**
     * Construct a new ViewerGlobalActionManager.
     * @param actions the GlobalActions to be managed
     * @param viewer the Viewer upon which the GlobalActions act
     * @param actionBars the IActionBars containing the global actions
     * which will be replaced as necessary with the given GlobalActions by
     * this ViewerGlobalActionManager.
     */
    public ViewerGlobalActionManager(final GlobalActions actions,
                                     Viewer viewer, IActionBars actionBars) {
        assert(actions != null);
        assert(actionBars != null);
        this.actionBars = actionBars;

        actionMap.put(ActionFactory.COPY, actions.getCopy());
        actionMap.put(ActionFactory.CUT, actions.getCut());
        actionMap.put(ActionFactory.DELETE, actions.getDelete());
        actionMap.put(ActionFactory.PASTE, actions.getPaste());
        actionMap.put(ActionFactory.SELECT_ALL, actions.getSelectAll());

        viewer.getControl().addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent event) {
                setupActions();
            }

            public void focusLost(FocusEvent event) {
                restoreActions();
            }
        });
    }

    /**
     * Set the cut, copy and paste actions into the global action handler.
     */
    private void setupActions() {
        Iterator actionKeys = actionMap.keySet().iterator();
        while (actionKeys.hasNext()) {
            ActionFactory key = (ActionFactory) actionKeys.next();
            IAction action = (IAction) actionMap.get(key);
            if (action != null) {
                IAction origAction =
                        actionBars.getGlobalActionHandler(key.getId());
                origActionMap.put(key, origAction);
                actionBars.setGlobalActionHandler(key.getId(), action);
            }
        }

        actionBars.updateActionBars();
    }

    /**
     * Restore the original global action handlers for cut, copy and paste.
     */
    private void restoreActions() {
        Iterator actionKeys = origActionMap.keySet().iterator();
        while (actionKeys.hasNext()) {
            ActionFactory key = (ActionFactory) actionKeys.next();
            IAction action = (IAction) origActionMap.get(key);
            if (action != null) {
                actionBars.setGlobalActionHandler(key.getId(), action);
            }
        }
        actionBars.updateActionBars();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-May-05	8213/1	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 11-Mar-05	6895/1	allan	VBM:2005020412 Commit to allow access to changes

 ===========================================================================
*/
