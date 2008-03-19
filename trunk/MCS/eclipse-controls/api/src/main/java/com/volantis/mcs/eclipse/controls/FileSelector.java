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

import com.volantis.mcs.eclipse.validation.FilenameValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.utilities.FaultTypes;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * A control that allows the user to select a file name. This controller
 * provides a text and button. The user may enter the file name in the text
 * button or use the button (which displays a FileDialog) to select a
 * particular file.
 */
public class FileSelector
        extends PathSelector {

    private static final String mark = "(c) Volantis Systems Ltd 2003."; //$NON-NLS-1$

    /**
     * The validator for this class.
     */
    private static final FilenameValidator VALIDATOR = new FilenameValidator();

    /**
     * Resource prefix for the file selector.
     */
    private static final String FILE_SELECTOR_PREFIX = "FileSelector."; //$NON-NLS-1$

    /**
     * The qualified name used for storing the initial selection in the
     * session.
     */
    private static final QualifiedName QNAME_INITIAL_SELECTION = new QualifiedName(
            "com.volantis.mcs.eclipse.controls.FileSelector", //$NON-NLS-1$
            FILE_SELECTOR_PREFIX + "initialSelection"); //$NON-NLS-1$

    /**
     * Mapping between fault types understood by this page and message keys in
     * the Wizards properties.
     */
    private static final Map MESSAGE_KEY_MAPPINGS;

    /**
     * The key used for displaying custom invalid filename messages.
     */
    private static final String INVALID_FILENAME =
            "FileSelector.invalidFileName"; //$NON-NLS-1$

    static {
        MESSAGE_KEY_MAPPINGS = new HashMap(1);
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_FILENAME,
                INVALID_FILENAME);
    }

    /**
     * The filter selections that may be set.
     */
    private String[] filterSelections = null;

    /**
     * The file dialog used to display the selection.
     */
    private FileDialog dialog = null;

    /**
     * The filter path directory for the dialog.
     */
    private String filterPath;

    /**
     * Construct the FileSelector with the specified Validator and element
     * name.
     *
     * @param parent  the parent Composite.
     * @param style   the style
     * @param initialSelection The QualifiedName specifying the session property
     * key for the initial selection in the dialog. 
     */
    public FileSelector(Composite parent, int style,
                        QualifiedName initialSelection) {
        super(parent, style, initialSelection);
    }

    /**
     * Construct the FileSelector with the specified Validator and element
     * name.
     *
     * @param parent  the parent Composite.
     * @param style   the style
     */
    public FileSelector(Composite parent, int style) {
        this(parent, style, QNAME_INITIAL_SELECTION);
    }

    /**
     * Display the file selection dialog.
     *
     * @return the file name that has been selected.
     */
    protected String showDialog() {
        if (dialog == null) {
            dialog = new FileDialog(getShell(), SWT.OPEN);
        }
        if (getFilterSelections() != null) {
            dialog.setFilterExtensions(getFilterSelections());
        }
        String initialSelection = (String)getInitialSelection();
        if (initialSelection != null) {
            dialog.setFileName(initialSelection);
        }
        if(getFilterPath()!=null) {
            dialog.setFilterPath(getFilterPath());
        }
        return dialog.open();
    }

    // javadoc inherited
    public ValidationStatus validate() {
        return VALIDATOR.validate(getValue(), new ValidationMessageBuilder(
                ControlsMessages.getResourceBundle(),
                MESSAGE_KEY_MAPPINGS,
                null));
    }

    /**
     * Return the filter selections property.
     *
     * @return the filter selections property.
     */
    public String[] getFilterSelections() {
        return filterSelections;
    }

    /**
     * Set the filter selections property.
     * @param filterSelections the filter selections property.
     */
    public void setFilterSelections(String[] filterSelections) {
        this.filterSelections = filterSelections;
    }

    /**
     * Set the filter path for the dialog. Selections will only be available
     * from within the filter path.
     * @param filterPath The absolute path representing the filter path.
     * Can be null.
     */
    public void setFilterPath(String filterPath) {
        this.filterPath = filterPath;
    }

    /**
     * Get the filter path associated with this dialog.
     * @return the filter path.
     */
    public String getFilterPath() {
        return filterPath;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Feb-04	3200/2	allan	VBM:2004022410 Basic Update Client Wizard.

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 04-Nov-03	1790/4	byron	VBM:2003102408 Provide a FolderSelector - addressed rework issues

 03-Nov-03	1790/2	byron	VBM:2003102408 Provide a FolderSelector

 30-Oct-03	1639/5	byron	VBM:2003101602 Create a MCS Project properties page - addressed requirement change and other minor issues

 30-Oct-03	1639/3	byron	VBM:2003101602 Create a MCS Project properties page - addressed various review issues

 30-Oct-03	1639/1	byron	VBM:2003101602 Create a MCS Project properties page

 27-Oct-03	1618/1	byron	VBM:2003100804 Create a new project wizard for MCS projects

 ===========================================================================
*/
