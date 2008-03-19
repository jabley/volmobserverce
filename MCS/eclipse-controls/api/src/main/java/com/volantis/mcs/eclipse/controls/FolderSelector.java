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

import com.volantis.mcs.eclipse.validation.ValidationStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

import java.io.File;
import java.text.MessageFormat;

/**
 * The folder selector dialog that may be used to select folder resources.
 */
public class FolderSelector
        extends PathSelector {

    private static final String mark = "(c) Volantis Systems Ltd 2003."; //$NON-NLS-1$

    /**
     * Resource prefix for the file selector.
     */
    private final static String FOLDER_SELECTOR_PREFIX = "FolderSelector."; //$NON-NLS-1$

    /**
     * The qualified name used for storing the initial selection in the
     * session.
     */
    private static final QualifiedName QNAME_INITIAL_SELECTION = new QualifiedName(
            "com.volantis.mcs.eclipse.controls.FolderSelector", //$NON-NLS-1$
            FOLDER_SELECTOR_PREFIX + "initialSelection"); //$NON-NLS-1$

    /**
     * The title of the Folder Selection dialog.
     */
    private String title;

    /**
     * The directory (folder) dialog used to display the selection.
     */
    private DirectoryDialog dialog = null;

    /**
     * Store the MessageFormat in order to reduce garbage creation during
     * validation.
     * @see #validate
     */
    private MessageFormat messageFormat;

    /**
     * Store the formatArgs in order to reduce garbage creation during
     * validation.
     * @see #validate
     */
    private Object formatArgs[] = new Object[1];

    /**
     * Store the pattern in order to reduce garbage creation during validation.
     * @see #validate
     */
    private String pattern = ControlsMessages.getResourceBundle().
            getString(FOLDER_SELECTOR_PREFIX + "doesNotExist"); //$NON-NLS-1$

    /**
     * Construct the FolderSelector with the specified parent composite and
     * style.
     *
     * @param parent the parent Composite.
     * @param style  the style.
     */
    public FolderSelector(Composite parent, int style) {
        this(parent, style, null);
    }

    /**
     * Construct the FolderSelector with the specified parent composite,
     * style, context and dialog title.
     *
     * @param parent the parent Composite.
     * @param style  the style.
     * @param title  the folder selection dialog's title.
     */
    public FolderSelector(Composite parent,
                          int style,
                          String title) {

        super(parent, style, QNAME_INITIAL_SELECTION);

        if (title == null) {
            this.title = ControlsMessages.getString(FOLDER_SELECTOR_PREFIX + "title"); //$NON-NLS-1$
        } else {
            this.title = title;
        }
    }

    // javadoc inherited.
    protected String showDialog() {
        if (dialog == null) {
            dialog = new DirectoryDialog(getShell(), SWT.NONE);
        }
        if (title != null) {
            dialog.setText(title);
        }
        // Attempt to set the initial selection to the value typed in (only
        // if it is valid). If it isn't valid or empty, use the last selected
        // valid value. If this is null, then don't set the initial selection.
        String initialSelection = getValue();
        if (!isValueValid() || (initialSelection == null) ||
                (initialSelection.length() == 0)) {
            initialSelection = (String) getInitialSelection();
        }
        if (initialSelection != null) {
            dialog.setFilterPath(initialSelection);
        }
        return dialog.open();
    }

    /**
     * Return true if the value in the text control is valid, false otherwise.
     *
     * @return true if the value in the text control is valid, false
     *         otherwise.
     */
    private boolean isValueValid() {
        boolean valid = false;
        File file = new File(getValue());
        if (file.isDirectory() && file.exists()) {
            valid = true;
        }
        return valid;
    }

    // javadoc inherited
    public ValidationStatus validate() {
        ValidationStatus status = null;
        if (!isValueValid()) {
            formatArgs[0] = getValue();
            if (messageFormat == null) {
                messageFormat = new MessageFormat(pattern);
            }
            status = new ValidationStatus(ValidationStatus.WARNING,
                    messageFormat.format(formatArgs));
        }
        return status;
    }

    /**
     * Adds a listener
     * @param listener the listener to folder selections
     */
    public void addModifyListener(ModifyListener listener) {
        getText().addModifyListener(listener);
    }

    /**
     * Removes a listener
     * @param listener the listener to folder selections
     */
    public void removeModifyListener(ModifyListener listener) {
        getText().removeModifyListener(listener);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Feb-04	3200/1	allan	VBM:2004022410 Basic Update Client Wizard.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 14-Nov-03	1835/1	pcameron	VBM:2003102801 Added GenericAssetCreation wizard page and supporting resources

 04-Nov-03	1790/3	byron	VBM:2003102408 Provide a FolderSelector - addressed rework issues

 03-Nov-03	1790/1	byron	VBM:2003102408 Provide a FolderSelector

 ===========================================================================
*/
