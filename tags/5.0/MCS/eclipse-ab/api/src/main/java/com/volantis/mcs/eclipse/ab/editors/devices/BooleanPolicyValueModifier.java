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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The BooleanPolicyValueModifier allows a user to modify the value of a policy
 * element using a button widget with a SWT.CHECK style. The button is selected
 * for values that are true; and deselected otherwise. The policy is expected
 * to have a name and to have a boolean value. The localised policy name is
 * displayed next to the button. Values which are neither "true" nor "false"
 * result in the button not being selected.
 */
public class BooleanPolicyValueModifier
        extends SingleValuePolicyValueModifier {

    /**
     * The Button widget to use for displaying and modifying policy values.
     */
    private final Button button;

    /**
     * The DeviceRepositoryAccessorManager used to retrieve the localised
     * policy name.
     */
    private final DeviceRepositoryAccessorManager deviceRAM;

    /**
     * The initial policy name this modified was provided with. We store this
     * so that we can use it to overidde field element names in refreshControl.
     */
    private final String policyName;

    /**
     * Constructs a new BooleanPolicyValueModifier that uses its own Button
     * widget created with the given parent.
     * @param parent the parent Composite of the Button widget. Cannot be null.
     * @param dram the DeviceRepositoryAccessorManager used by this
     *             control to retrieve the localised policy name
     * @throws IllegalArgumentException if parent or dram is null.
     */
    public BooleanPolicyValueModifier(Composite parent,
                                      DeviceRepositoryAccessorManager dram) {
        this(parent, dram, null);
    }

    /**
     * Constructs a new BooleanPolicyValueModifier that uses its own Button
     * widget created with the given parent.
     * @param parent the parent Composite of the Button widget. Cannot be null.
     * @param dram the DeviceRepositoryAccessorManager used by this
     *             control to retrieve the localised policy name
     * @param policyName the name of the policy that this modifier is
     * associated with - used to set the label. Can be null in which case
     * the button label will need to be set at some later time be calling
     * setPolicy.
     * @throws IllegalArgumentException if parent or dram is null.
     */
    public BooleanPolicyValueModifier(Composite parent,
                                      DeviceRepositoryAccessorManager dram,
                                      String policyName) {
        if (parent == null) {
            throw new IllegalArgumentException("Cannot be null: parent");
        }
        if (dram == null) {
            throw new IllegalArgumentException("Cannot be null: dram");
        }
        button = createControl(parent);
        deviceRAM = dram;

        this.policyName = policyName;

        if (policyName != null) {
            // Retrieve and set the localised policy name
            setButtonText(deviceRAM.getLocalizedPolicyName(policyName));
        }
    }

    /**
     * Creates the button widget used by this BooleanPolicyValueModifier. The
     * button has a "checkbox" style, SWT.CHECK, to represent boolean values.
     * @param container the parent Composite for the button
     * @return the button widget
     */
    private Button createControl(Composite container) {
        Button checkButton = new Button(container, SWT.CHECK);
        checkButton.setBackground(checkButton.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        checkButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                updatePolicyElement((button.getSelection() ?
                        Boolean.TRUE :
                        Boolean.FALSE).toString());
            }
        });

        return checkButton;
    }

    /**
     * Overridden to also retrieve the localised policy name of the supplied
     * element and to set this name as the text of the Button widget.
     * @throws IllegalArgumentException if the policy element has no name or is
     *                                  the empty string
     */
    // rest of javadoc inherited
    protected void refreshControl() {
        super.refreshControl();
        // Check the policy or field element has a name.
        String name = policyName !=null ? policyName :
                policyElement.getAttributeValue(
                DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE);
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(
                    policyElement.getName() + " element has no name.");
        }

        // Retrieve and set the localised policy name
        setButtonText(deviceRAM.getLocalizedPolicyName(name));

    }

    /**
     * Sets the text that the button displays.
     * @param text the buttons text
     * @throws IllegalArgumentException if the text argument is null
     */
    private void setButtonText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text argument cannot be null");
        }
        button.setText(text);
        // Repack the button so that the new text is entirely visible.
        button.pack();
    }

    // javadoc inherited
    public Control getControl() {
        return button;
    }

    // javadoc inherited
    protected void setValue(String value) {
        button.setSelection(Boolean.valueOf(value).booleanValue());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 10-May-04	4068/3	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 22-Apr-04	3975/1	allan	VBM:2004042005 Fix multi-value policy migration and related issues.

 01-Apr-04	3602/1	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 29-Mar-04	3574/1	allan	VBM:2004032401 Completed device repository merging. Needs more testing.

 11-Mar-04	3405/3	pcameron	VBM:2004022312 Moved super call to top of setPolicy

 11-Mar-04	3405/1	pcameron	VBM:2004022312 Added BooleanPolicyValueModifier

 ===========================================================================
*/
