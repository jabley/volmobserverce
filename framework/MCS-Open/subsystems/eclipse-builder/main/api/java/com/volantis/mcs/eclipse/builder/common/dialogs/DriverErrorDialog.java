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
package com.volantis.mcs.eclipse.builder.common.dialogs;

import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * A class providing the ability to create driver error dialogs.
 */
public class DriverErrorDialog {
    /**
     * The resource prefix.
     */
    private static final String RESOURCE_PREFIX = "DriverErrorDialog.";

    /**
     * Title for the missing driver error box.
     */
    private static final String MISSING_DRIVER_TITLE =
            EditorMessages.getString(RESOURCE_PREFIX + "missingDriver.title");

    /**
     * Message for the missing driver error box.
     */
    private static final String MISSING_DRIVER_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX + "missingDriver.message");

    public static void openDriverErrorDialog(Shell shell) {
        MessageDialog.openError(shell, MISSING_DRIVER_TITLE,
                MISSING_DRIVER_MESSAGE);
    }
}
