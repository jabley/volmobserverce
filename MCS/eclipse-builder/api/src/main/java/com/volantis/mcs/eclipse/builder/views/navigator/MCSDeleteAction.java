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
package com.volantis.mcs.eclipse.builder.views.navigator;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.DeleteResourceAction;
import org.eclipse.ui.actions.SelectionListenerAction;

/**
 * A collaborative working aware implementation of a delete action. Takes
 * control of deletion for files that are handled by collaborative working, and
 * delegates other deletes to the standard delete action.
 */
public class MCSDeleteAction extends DelegatingSelectionListenerAction {
    /**
     * The shell which triggered this action (used for parenting dialogs etc.)
     */
    private Shell shell;

    /**
     * Creates a new MCSDeleteAction.
     *
     * @param shell the shell for any dialogs
     */
    public MCSDeleteAction(Shell shell) {
        super(shell, null);
        this.shell = shell;
    }

    // Javadoc not required
    protected Shell getShell() {
        return shell;
    }

    // Javadoc inherited
    protected SelectionListenerAction createUnderlyingAction(Shell shell) {
        return new DeleteResourceAction(shell);
    }
}
