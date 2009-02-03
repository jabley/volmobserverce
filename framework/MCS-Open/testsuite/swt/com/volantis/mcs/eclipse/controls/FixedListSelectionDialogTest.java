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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

/**
 * This is a manual test for the FixedListSelectionDialog. Run the test
 * and follow the instructions which describe the successful
 * test criteria.
 */
public class FixedListSelectionDialogTest extends ControlsTestAbstract {

    /**
     * Construct a FixedListSelectionDialogTest.
     * @param title the title of the test
     */
    public FixedListSelectionDialogTest(String title) {
        super(title);
    }

    /**
     * Create the buttons and actions to show the dialogs.
     */
    public void createControl() {
        String[] availableValues = {"a", "b", "c", "d", "e", "f", "g", "h"};

        createTestButtonDialog("FixedListSelectionDialog with duplicates",
                true, availableValues, null);

        availableValues = new String[] {
            "a", "b", "c", "qwertyuiopqwertyuiop", "d", "peter", "jean-paul",
            "francois", "e", "f", "g", "h"
        };

        String[] initialSelections = {"peter", "jean-paul", "francois",
                                      "qwertyuiopqwertyuiop"};

        createTestButtonDialog("FixedListSelectionDialog with no duplicates",
                false, availableValues, initialSelections);

        createTestButtonDialog("FixedListSelectionDialog with no values",
                false, new String[]{}, null);
    }

    /**
     * Helper method to create the button with an action that displays the
     * dialog with the specified values passed in as parameters.
     *
     * @param buttonText       the text to display on the button.
     * @param allowDuplicates  true if duplicates should be allowed, false
     *                         otherwise.
     * @param availableValues  the available values to display
     * @param intialSelection the initial selection.
     */
    private void createTestButtonDialog(String buttonText,
                                        boolean allowDuplicates,
                                        String[] availableValues,
                                        String[] intialSelection) {
        Button button = new Button(getShell(), SWT.PUSH);
        button.setText(buttonText);

        // A test dialog which doesn't allow duplicates
        final FixedListSelectionDialog dialog =
                new FixedListSelectionDialog(button.getShell(),
                        availableValues, allowDuplicates);
        dialog.setInitialSelections(new String[]{});
        dialog.setTitle(allowDuplicates ? "Duplicates are allowed" :
                "Duplicates not allowed");

        if (intialSelection != null) {
            dialog.setInitialSelections(intialSelection);
        }

        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                dialog.open();
                String[] results = (String[]) dialog.getResult();
                if (results != null) {
                    System.out.println("You chose:");
                    for (int i = 0; i < results.length; i++) {
                        System.out.println(results[i]);
                    }
                } else {
                    System.out.println("You chose nothing");
                }
            }
        });
    }

    // javadoc inherited
    public String getSuccessCriteria() {
        String msg = "";
        return msg;
    }

    /**
     * The main method must be implemented by the test class writer.
     * @param args the FixedListSelectionDialog does not require input.
     */
    public static void main(String[] args) {
        new FixedListSelectionDialogTest("FixedListSelectionDialog Test").
                display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-May-04	4276/1	byron	VBM:2004051007 New policy creation fails for selection type policies

 08-Mar-04	3335/3	pcameron	VBM:2004030411 Added FixedListSelectionDialog

 ===========================================================================
*/
