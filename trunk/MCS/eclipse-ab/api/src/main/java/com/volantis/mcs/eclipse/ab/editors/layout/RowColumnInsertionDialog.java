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

import com.volantis.mcs.eclipse.controls.MessageAreaSelectionDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.text.MessageFormat;

/**
 * This dialog is used to gather insertion data for rows or columns.
 */
public class RowColumnInsertionDialog extends MessageAreaSelectionDialog {

    /**
     * Typesafe enumeration that defines the types of dialog that can be shown.
     */
    public final static class Type {
        /**
         * Show as a "Row" dialog for adding rows.
         */
        public static RowColumnInsertionDialog.Type ROW =
                new RowColumnInsertionDialog.Type("row"); //$NON-NLS-1$

        /**
         * Show as a "Column" dialog for adding columns.
         */
        public static RowColumnInsertionDialog.Type COLUMN =
                new RowColumnInsertionDialog.Type("column"); //$NON-NLS-1$

        /**
         * Internal value for use by the dialog in determining the title for
         * the dialog given the type required.
         */
        private String key;

        /**
         * Private to ensure that the literals above are the only instances.
         *
         * @param key the key for the given instance
         */
        private Type(String key) {
            this.key = key;
        }
    }

    /**
     * Resource prefix for the dialog.
     */
    private final static String RESOURCE_PREFIX = "RowColumnInsertionDialog."; //$NON-NLS-1$

    /**
     * The After text for the Combo widget.
     */
    private static final String AFTER_TEXT =
            LayoutMessages.getString(RESOURCE_PREFIX + "text.after"); //$NON-NLS-1$

    /**
     * The Before text for the Combo widget.
     */
    private static final String BEFORE_TEXT =
            LayoutMessages.getString(RESOURCE_PREFIX + "text.before"); //$NON-NLS-1$

    /**
     * The title of the dialog.
     */
    private final String dialogTitle;

    /**
     * The message format input errors.
     */
    private static MessageFormat ERROR_FORMAT =
            new MessageFormat(LayoutMessages.getString(RESOURCE_PREFIX +
            "errorFormat")); //$NON-NLS-1$

    /**
     * The horizontal spacing between widgets.
     */
    private static final int HORIZONTAL_SPACING =
            LayoutMessages.getInteger(RESOURCE_PREFIX +
            "horizontalSpacing").intValue(); //$NON-NLS-1$

    /**
     * The margin height of the dialog.
     */
    private static final int MARGIN_HEIGHT =
            LayoutMessages.getInteger(RESOURCE_PREFIX +
            "marginHeight").intValue(); //$NON-NLS-1$

    /**
     * The margin width of the dialog.
     */
    private static final int MARGIN_WIDTH =
            LayoutMessages.getInteger(RESOURCE_PREFIX +
            "marginWidth").intValue(); //$NON-NLS-1$

    /**
     * The text for the Number label.
     */
    private static final String NUMBER_LABEL_TEXT =
            LayoutMessages.getString(RESOURCE_PREFIX + "number.labelText"); //$NON-NLS-1$

    /**
     * The text for the Position label.
     */
    private static final String POSITION_LABEL_TEXT =
            LayoutMessages.getString(RESOURCE_PREFIX + "position.labelText"); //$NON-NLS-1$

    /**
     * The vertical spacing between widgets.
     */
    private static final int VERTICAL_SPACING =
            LayoutMessages.getInteger(RESOURCE_PREFIX +
            "verticalSpacing").intValue(); //$NON-NLS-1$

    /**
     * The result array returned by the dialog.
     */
    private final Object[] result = new Object[1];

    /**
     * The entered number of rows or columns to insert.
     */
    private int numberToInsert;

    /**
     * The entered choice of where the insertion takes place: before or after
     * the selected grid cell.
     */
    private boolean isBefore = true;

    /**
     * The Text input box for number of insertions.
     */
    private Text numberToInsertTextField;

    /**
     * The Combo selector for relative position (before or after).
     */
    private Combo relativePositionCombo;

    /**
     * Helper class to store and return the inputted data to the user.
     */
    public final class InsertionData {
        private int numToInsert;
        private boolean isBefore;

        /**
         * Creates a new instance of InsertionData.
         * @param numToInsert the number of rows or columns to insert
         * @param isBefore flag to indicate if the insertion is before
         *                 or after the selected grid cell.
         */
        public InsertionData(int numToInsert, boolean isBefore) {
            this.numToInsert = numToInsert;
            this.isBefore = isBefore;
        }

        /**
         * Get the number of rows or columns to insert.
         * @return the number
         */
        public int getNumToInsert() {
            return numToInsert;
        }

        /**
         * Flag to indicate whether the insertion is before or after the
         * selected grid cell.
         * @return true if before; false if after.
         */
        public boolean isBefore() {
            return isBefore;
        }
    }

    /**
     * Initializes the new instance using the given parameters. Type defaults
     * to {@link com.volantis.mcs.eclipse.ab.editors.layout.RowColumnInsertionDialog.Type#ROW}.
     *
     * @param parent the shell within which the dialog is displayed
     */
    public RowColumnInsertionDialog(Shell parent) {
        this(parent, RowColumnInsertionDialog.Type.ROW);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param parent the shell within which the dialog is displayed
     * @param type   the type of usage for the dialog. Must not be null.
     */
    public RowColumnInsertionDialog(Shell parent,
                                    RowColumnInsertionDialog.Type type) {
        super(parent);

        if (type == null) {
            throw new IllegalArgumentException(
                    "Cannot be null: type"); //$NON-NLS-1$
        }

        dialogTitle = LayoutMessages.getString(
                new StringBuffer(RESOURCE_PREFIX).append("title."). //$NON-NLS-1$
                append(type.key).toString());

        setTitle(dialogTitle);
        setBlockOnOpen(true);
    }

    // javadoc inherited
    public Object[] getResult() {
        result[0] = new InsertionData(numberToInsert, isBefore);
        return result;
    }

    // javadoc inherited
    protected void cancelPressed() {
        result[0] = null;

        super.cancelPressed();
    }

    /**
     * Creates the dialog's control. This consists of a two column grid where
     * the first column contains the labels for the two inputs and the second
     * column contains the Text and the Combo. The first column is sized to the
     * labels while the second column is sized to fill the remaining horizontal
     * space. The error label spans both columns to fill the horizontal space
     * available.
     *
     * @param parent the parent of the dialog's control
     * @return the created top-most control
     */
    protected Control createDialogArea(Composite parent) {
        Composite topLevel = (Composite) super.createDialogArea(parent);

        // Set up the grid layout
        GridLayout topLevelGrid = new GridLayout(2, false);
        topLevelGrid.horizontalSpacing = HORIZONTAL_SPACING;
        topLevelGrid.verticalSpacing = VERTICAL_SPACING;
        topLevelGrid.marginHeight = MARGIN_HEIGHT;
        topLevelGrid.marginWidth = MARGIN_WIDTH;
        topLevel.setLayout(topLevelGrid);

        // Add the number label
        Label numberLabel = new Label(topLevel, SWT.NONE);
        numberLabel.setText(NUMBER_LABEL_TEXT);

        // Add the text field for insertion number.
        numberToInsertTextField = new Text(topLevel, SWT.SINGLE | SWT.BORDER);
        numberToInsertTextField.setLayoutData(
                new GridData(GridData.FILL_HORIZONTAL));
        numberToInsertTextField.addModifyListener(new ModifyListener() {
            // javadoc inherited
            public void modifyText(ModifyEvent modifyEvent) {
                validateDialog();
            }
        });

        // Add position label
        Label positionLabel = new Label(topLevel, SWT.NONE);
        positionLabel.setText(POSITION_LABEL_TEXT);

        // Add Before/After Combo
        relativePositionCombo = new Combo(topLevel, SWT.READ_ONLY);
        relativePositionCombo.add(BEFORE_TEXT);
        relativePositionCombo.add(AFTER_TEXT);
        relativePositionCombo.select(0);
        relativePositionCombo.setLayoutData(
                new GridData(GridData.FILL_HORIZONTAL));
        relativePositionCombo.addModifyListener(new ModifyListener() {
            // javadoc inherited
            public void modifyText(ModifyEvent modifyEvent) {
                validateDialog();
            }
        });

        // Create the error message area and span it across both columns
        Composite errorMessageComposite =
                (Composite) createErrorMessageArea(topLevel);
        GridData errorMessageCompositeGridData =
                new GridData(GridData.FILL_HORIZONTAL);
        errorMessageCompositeGridData.horizontalSpan = 2;
        errorMessageComposite.setLayoutData(errorMessageCompositeGridData);

        return topLevel;
    }

    // javadoc inherited
    protected Button createButton(Composite parent, int id, String label,
                                  boolean defaultButton) {
        Button button = super.createButton(parent, id, label, defaultButton);

        if (id == IDialogConstants.OK_ID) {
            // The inputs will be empty by default, which gives invalid values
            // without an error message, so simply disable the OK button
            button.setEnabled(false);
        }

        return button;
    }

    /**
     * Validates the number input. An empty input is invalid but does not
     * generate an error message. Other errors in the input generate an error
     * message.
     *
     * <p>If the inputs are invalid the OK button is disabled.</p>
     */
    private void validateDialog() {
        String numberText = numberToInsertTextField.getText();
        isBefore = relativePositionCombo.getText().equals(BEFORE_TEXT);
        boolean valid = false;

        try {
            numberToInsert =
                    Integer.parseInt(numberText);
            valid = (numberToInsert > 0);
        } catch (NumberFormatException e) {
            // Safe to ignore, as text is invalid
        }

        getOkButton().setEnabled(valid);

        if (valid || numberText.length() == 0) {
            // Clear the error message
            setErrorMessage(null);
        } else {
            // Generate the required error message
            String message = ERROR_FORMAT.format(new Object[]{numberText});

            setErrorMessage(message);
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

 05-Feb-04	2875/1	pcameron	VBM:2004020202 Added RowDeleteAction

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 03-Feb-04	2826/4	pcameron	VBM:2004020208 A few tweaks

 03-Feb-04	2826/2	pcameron	VBM:2004020208 Added Row/Column insertion dialog and test

 ===========================================================================
*/
