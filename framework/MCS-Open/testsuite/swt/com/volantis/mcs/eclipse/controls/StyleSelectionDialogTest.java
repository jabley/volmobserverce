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
package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import com.volantis.mcs.eclipse.controls.StyleSelectionDialog;

/**
 * This is a manual test for the StyleSelectionDialog. Run the test
 * and follow the instructions which describe the successful
 * test criteria.
 */
public class StyleSelectionDialogTest extends ControlsTestAbstract {

    /**
     * Construct a StyleSelectionDialogTest.
     * @param title the title of the test
     */
    public StyleSelectionDialogTest(String title) {
        super(title);
    }

    /**
     * Creates the button and action to show the dialog.
     */
    public void createControl() {
        Composite container = new Composite(getShell(), SWT.NONE);
        container.setLayout(new FillLayout());
        Button button = new Button(container, SWT.PUSH);
        button.setText("Press me for a STYLECLASS selection dialog");
        String[] styles = new String[] {
            "class1",
            "class2",
            "cla ss2",  // invalid
            "4class2"  // invalid
        };
        final StyleSelectionDialog dialog =
                new StyleSelectionDialog(button.getShell(), styles);
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                dialog.open();
                System.out.println(dialog.getStyles());
            }
        });
    }

    /**
     * The description of the tests to carry out
     * and what the expected results are for success.
     * @return the success criteria
     */
    public String getSuccessCriteria() {
        return "";
    }

    /**
     * The main method must be implemented by the test class writer.
     * @param args the FontSelector does not require input arguments.
     */
    public static void main(String[] args) {
        new StyleSelectionDialogTest("StyleSelectionDialog Test").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Oct-04	5771/1	byron	VBM:2004100806 Support style classes on grids and spatial format iterators: GUI

 ===========================================================================
*/
