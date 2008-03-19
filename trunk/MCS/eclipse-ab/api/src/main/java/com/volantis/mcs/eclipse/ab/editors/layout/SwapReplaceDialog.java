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

import com.volantis.mcs.eclipse.ab.actions.layout.ActionSupport;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog that asks a user whether they require to swap or replace or just
 * replace depending on configuration.
 */
public class SwapReplaceDialog extends MessageDialog {
    /**
     * Message constants.
     */
    private static final String RESOURCE_PREFIX = "SwapReplaceDialog.";
    private static final String TITLE =
            LayoutMessages.getString(RESOURCE_PREFIX + "title");
    private static final String SWAP_MESSAGE =
            LayoutMessages.getString(RESOURCE_PREFIX + "swap.message");
    private static final String REPLACE_MESSAGE =
            LayoutMessages.getString(RESOURCE_PREFIX + "replace.message");
    private static final String SWAP_BUTTON_LABEL =
            LayoutMessages.getString(RESOURCE_PREFIX + "swap.button");
    private static final String REPLACE_BUTTON_LABEL =
            LayoutMessages.getString(RESOURCE_PREFIX + "replace.button");

    /**
     * Flag indicating that this dialog allows swap to be selected.
     */
    private final boolean canSwap;

    /**
     * Construct a new SwapReplaceDialog
     * @param parentShell the parent shell
     * @param canSwap flag to indicate if swap is possible
     */
    public SwapReplaceDialog(Shell parentShell,
                             boolean canSwap) {
        super(parentShell, TITLE, null,
                canSwap ? SWAP_MESSAGE : REPLACE_MESSAGE, QUESTION,
                canSwap ? new String[]{
                    SWAP_BUTTON_LABEL, REPLACE_BUTTON_LABEL,
                    IDialogConstants.CANCEL_LABEL
                } : new String[]{
                    REPLACE_BUTTON_LABEL, IDialogConstants.CANCEL_LABEL}, 0);
        this.canSwap = canSwap;
    }

    /**
     * Get the result of the dialog question. If cancel was pressed or the
     * dialog window closed then this method will return null.
     * @return the ActionSupport.ActionIdentifier for the dialog question or
     * null if neither swap or replace were selected.
     */
    public ActionSupport.ActionIdentifier getResult() {
        ActionSupport.ActionIdentifier result = null;
        if (canSwap) {
            if (getReturnCode() == 0) {
                result = ActionSupport.SWAP_ACTION;
            } else if (getReturnCode() == 1) {
                result = ActionSupport.REPLACE_ACTION;
            }
        } else if (getReturnCode() == 0) {
            result = ActionSupport.REPLACE_ACTION;
        }

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6749/1	allan	VBM:2005012102 Drag n Drop support

 ===========================================================================
*/
