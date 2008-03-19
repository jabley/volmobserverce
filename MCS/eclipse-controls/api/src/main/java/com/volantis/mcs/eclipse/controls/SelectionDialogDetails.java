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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.objects.FileExtension;

/**
 * Instances of this class are used to customize the appearance and
 * behaviour of selection dialogs (e.g. Policy selection dialogs)
 */
public class SelectionDialogDetails {

    /**
     * The ID of the entity which is to be selected
     */
    public final String entityId;

    /**
     * The title of the dialog to be displayed
     */
    public final String dialogTitle;

    /**
     * The "welcome" message within the dialog to be displayed
     */
    public final String dialogMessage;

    /**
     * The "no items" message to be displayed
     */
    public final String noItemsMessage;

    /**
     * The acceptable extensions: see constructor comments
     */
    public final FileExtension [] fileExtensions;

    /**
     * Constructor
     * @param entityId The ID of the entity which is to be selected
     * @param dialogTitle The title of the dialog to be displayed
     * @param dialogMessage The "welcome" message within the dialog to be displayed
     * @param noItemsMessage The "no items" message to be displayed
     * @param fileExtensions The acceptable extensions: this may
     * be empty or null, in which case checking the extension does not form
     * part of final validation of the item selected.
     */
    public SelectionDialogDetails(
        String entityId,
        String dialogTitle,
        String dialogMessage,
        String noItemsMessage,
        FileExtension [] fileExtensions) {

        // Copy into instance data
        this.entityId = entityId;
        this.dialogTitle = dialogTitle;
        this.dialogMessage = dialogMessage;
        this.noItemsMessage = noItemsMessage;
        this.fileExtensions = fileExtensions;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Feb-04	2962/2	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 05-Dec-03	2128/1	pcameron	VBM:2003112105 Added TextDefinition and refactored PolicySelector

 17-Nov-03	1878/6	richardc	VBM:2003110901 No extensions IS allowed + updated JUnit

 14-Nov-03	1878/4	richardc	VBM:2003110901 Moved SESSION stuff back to PolicySelector

 14-Nov-03	1878/2	richardc	VBM:2003110901 VBM 2003110901

 ===========================================================================
*/
