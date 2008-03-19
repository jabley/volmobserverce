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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * This is a manual test for the ComboViewer. Run the test
 * and follow the instructions which describe the successful
 * test criteria.
 */
public class FontSelectorTest extends ControlsTestAbstract {

    /**
     * Construct a FontSelectorTest.
     * @param title the title of the test
     */
    public FontSelectorTest(String title) {
        super(title);
    }

    /**
     * Adds the FontSelector widget to the shell.
     */
    public void createControl() {
        Composite container = new Composite(getShell(), SWT.NONE);
        GridLayout gridLayout = new GridLayout(3, false);
        gridLayout.verticalSpacing = 5;
        gridLayout.horizontalSpacing = 5;
        container.setLayout(gridLayout);
        final FontSelector fontSelector =
                new FontSelector(container, SWT.NONE);
        GridData fontSelectorGridData = new GridData(GridData.FILL_BOTH);
        fontSelectorGridData.horizontalSpan = 3;
        fontSelector.setLayoutData(fontSelectorGridData);
        fontSelector.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                System.out.println("You have selected " +
                        fontSelector.getSelectedFont());
            }
        });
        Button button1 = new Button(container, SWT.PUSH);
        button1.setText("Add Font");
        button1.addSelectionListener(new SelectionAdapter() {
            String[] randomNames =
                    {"peter", "jean-paul",
                     "cursive", "aaa",
                     "bbb", "ccc", "Peter", "JEAN-PAUL"};
            private int i = 0;
            public void widgetSelected(SelectionEvent e) {
                fontSelector.addFont(randomNames[i++ % randomNames.length]);
            }
        });
        Button button2 = new Button(container, SWT.PUSH);
        button2.setText("Set Selected\nFont");
        button2.addSelectionListener(new SelectionAdapter() {
            String[] randomNames =
                    {"peter", "jean-paul",
                     "francois", "art deco",
                     "monospace", "serif", "volantis"};
            private int i = 0;
            public void widgetSelected(SelectionEvent e) {
                fontSelector.
                        setSelectedFont(randomNames[i++ % randomNames.length]);
            }
        });


        Button button3 = new Button(container, SWT.PUSH);
        button3.setText("Remove Selected\nFont");
        button3.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                fontSelector.removeSelectedFont();
            }
        });
    }

    /**
     * The description of the tests to carry out for the FontSelector
     * and what the expected results are for success.
     * @return the success criteria
     */
    public String getSuccessCriteria() {
        String msg = "This is a test of the FontSelector.\n" +
                "The FontSelector must appear and function correctly\n" +
                "for the test to pass. Type text into the\n" +
                "Text control and you should see the text printed out.\n" +
                "Any selection in the list is unselected when text is being" +
                "entered. If the entered text corresponds to an item in\n" +
                "the list, it is NOT selected." +
                "Press the buttons to operate upon the list.\n\n" +
                "The Add Font button should add some new font names \n" +
                "to the list. Duplicates should not be added. The newly" +
                "added name should be selected in the list\n\n" +
                "The Set Selected Font button should set the text in\n" +
                "the text field and select that name in the list only if\n" +
                "it is already there. The name should not be added to the\n" +
                "list if it isn't there. In this case, volantis should not\n" +
                "be added to the list, but should appear in the text " +
                " field.\n\n" +
                "The Remove Selected Font button should remove any name\n" +
                "from the list that is entered into the text field, if\n" +
                "it exists in the list. Any selection in the list should\n" +
                "be deselected, and the text field should be cleared.\n\n";
        return msg;
    }

    /**
     * The main method must be implemented by the test class writer.
     * @param args the FontSelector does not require input arguments.
     */
    public static void main(String[] args) {
        new FontSelectorTest("FontSelector Test").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 11-Dec-03	2187/1	pcameron	VBM:2003111403 Added FontSelector

 ===========================================================================
*/
