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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * This is a manual test for the TimeSelectionDialog. Run the test
 * and follow the instructions which describe the successful
 * test criteria.
 */
public class TimeSelectionDialogTest extends ControlsTestAbstract {

    /**
     * Construct a TimeSelectionDialogTest.
     * @param title the title of the test
     */
    public TimeSelectionDialogTest(String title) {
        super(title);
    }

    /**
     * Creates the button and action to show the dialog.
     */
    public void createControl() {
        Composite container = new Composite(getShell(), SWT.NONE);
        container.setLayout(new FillLayout());
        Button button = new Button(container, SWT.PUSH);
        button.setText("Press me for a time selection dialog");
        String[] times = new String[]{
            "hallo",
            "02:10:15.115",
            "goodbye",
            "23s",
            "00:00:00",
            "15min",
            "    ",
            "750 ms",
            "abcdefghijklmnop" +
                "qrstuvwxyz0123456789"};
        final TimeSelectionDialog dialog =
                new TimeSelectionDialog(button.getShell(), times);
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                dialog.open();
                String[] times = dialog.getTimes();
                if (times != null) {
                    for (int i = 0; i < times.length; i++) {
                        System.out.println(times[i]);
                    }
                }
            }
        });
    }

    /**
     * The description of the tests to carry out
     * and what the expected results are for success.
     * @return the success criteria
     */
    public String getSuccessCriteria() {
        String msg = "";
        return msg;
    }

    /**
     * The main method must be implemented by the test class writer.
     * @param args the FontSelector does not require input arguments.
     */
    public static void main(String[] args) {
        new TimeSelectionDialogTest("TimeSelectionDialog Test").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Mar-04	3362/2	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 18-Mar-04	3416/1	pcameron	VBM:2004022309 Added ListBuilder and ListPolicyValueModifier with tests

 09-Jan-04	2215/1	pcameron	VBM:2003112405 TimeSelectionDialog, ListValueBuilder and refactoring

 11-Dec-03	2187/1	pcameron	VBM:2003111403 Added FontSelector

 ===========================================================================
*/
