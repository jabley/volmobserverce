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

import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

/**
 * Manual test for the {@link GridDimensionsDialog}.
 */
public class RowColumnInsertionDialogTest extends ControlsTestAbstract {

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param title the title of the test
     */
    public RowColumnInsertionDialogTest(String title) {
        super(title);
    }

    /**
     * Creates the button and action to show the dialog.
     */
    public void createControl() {
        Button button = new Button(getShell(), SWT.PUSH);
        button.setText("Display Row/Column Insertion Dialog");
        final RowColumnInsertionDialog dialog =
                new RowColumnInsertionDialog(button.getShell(),
                        RowColumnInsertionDialog.Type.ROW);
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                int code = dialog.open();

                if (code == IDialogConstants.OK_ID) {
                    Object[] result = dialog.getResult();
                    RowColumnInsertionDialog.InsertionData data =
                            (RowColumnInsertionDialog.InsertionData) result[0];
                    System.out.println("Number to insert is " +
                            data.getNumToInsert() +
                            ", relative position is " +
                            (data.isBefore() ? "before" : "after"));
                } else {
                    System.out.println("Dialog was cancelled");
                }
            }
        });
    }

    /**
     * The description of the tests to carry out and what the expected results
     * are for success.
     */
    public String getSuccessCriteria() {
        return "You should see a message of the form \"Number to insert is X," +
                " relative position is before | after\"" +
                "when OK is pressed. OK is only available " +
                "when a valid number is entered.";
    }

    /**
     * The main method must be implemented by the test class writer.
     *
     * @param args the dialog does not require input arguments.
     */
    public static void main(String[] args) {
        new RowColumnInsertionDialogTest("Row/Column Insertion Dialog Test").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Feb-04	2826/1	pcameron	VBM:2004020208 Added Row/Column insertion dialog and test

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 ===========================================================================
*/
