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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.jdom.Element;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;

/**
 * This is a manual test for the PolicyValueSelectionDialog. Run the test
 * and follow the instructions which describe the successful test criteria.
 */
public class PolicyValueSelectionDialogTest extends ControlsTestAbstract {

    /**
     * Factory for creating JDOM objects. Note that devices do not use
     * the LPDM namespace.
     */
    private static final JDOMFactory factory = new DefaultJDOMFactory();

    /**
     * A selection element for the first dialog test.
     */
    private static final Element selectionElement1;

    /**
     * A selection element for the second dialog test.
     */
    private static final Element selectionElement2;

    /**
     * Initialise the two selection elements.
     */
    static {
        selectionElement1 = factory.element(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_SELECTION_ELEMENT_NAME);
        for (int i = 0; i < 10; i++) {
            Element keywordElement = factory.element(DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_KEYWORD_ELEMENT_NAME);
            keywordElement.addContent("choice" + i);
            selectionElement1.addContent(keywordElement);
        }

        selectionElement2 = factory.element(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_SELECTION_ELEMENT_NAME);
        Element keywordElement = factory.element(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_KEYWORD_ELEMENT_NAME);
        keywordElement.addContent("choice1");
        selectionElement2.addContent(keywordElement);
    }


    /**
     * Construct a PolicyValueSelectionDialogTest.
     * @param title the title of the test
     */
    public PolicyValueSelectionDialogTest(String title) {
        super(title);
    }

    /**
     * Create the buttons and actions to show the dialogs.
     */
    public void createControl() {
        Button button1 = new Button(getShell(), SWT.PUSH);
        button1.setText("PolicyValueSelectionDialog with valid\n" +
                "selection element at construction");

        final PolicyValueSelectionDialog dialog1 =
                new PolicyValueSelectionDialog(button1.getShell(),
                        "myPolicy1", selectionElement1);
        button1.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                dialog1.open();
                String[] results = (String[]) dialog1.getResult();
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
        dialog1.setInitialSelections(new String[]{"choice7", "choice3"});

        Button button2 = new Button(getShell(), SWT.PUSH);
        button2.setText("PolicyValueSelectionDialog with invalid\n" +
                "selection element at construction");

        final PolicyValueSelectionDialog dialog2 =
                new PolicyValueSelectionDialog(button1.getShell(),
                        "myPolicy2", selectionElement2);
        button2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                dialog2.open();
                String[] results = (String[]) dialog2.getResult();
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
     * @param args the PolicyValueSelectionDialog does not require input.
     */
    public static void main(String[] args) {
        new PolicyValueSelectionDialogTest("PolicyValueSelectionDialog Test").
                display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Mar-04	3574/1	allan	VBM:2004032401 Completed device repository merging. Needs more testing.

 10-Mar-04	3383/1	pcameron	VBM:2004030412 Added PolicyValueSelectionDialog

 ===========================================================================
*/
