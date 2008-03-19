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
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * The TextPolicyValueModifier allows a user to modify the value of a policy
 * element using a Text widget.
 */
public class TextPolicyValueModifier extends SingleValuePolicyValueModifier {

    /**
     * The Text widget to use for displaying and modifying policy values.
     */
    private final Text text;

    /**
     * The initial policy name this modifier was provided with.
     */
    private final String policyName;

    /**
     * The DeviceRepositoryAccessorManager used to retrieve the localised
     * policy name.
     */
    private final DeviceRepositoryAccessorManager deviceRAM;

    /**
     * Flag indicating that the modify listener is currently in action.
     */
    private boolean modifying;

    /**
     * The listener to register with the text component. This is stored here
     * so that it can be removed from the text component to allow the text
     * to be changed without updating the policyElement.
     */
    private final ModifyListener modListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            TextPolicyValueModifier.this.modifying = true;
            updatePolicyElement(text.getText());
            TextPolicyValueModifier.this.modifying = false;
        }
    };

    /**
     * Constructs a new TextPolicyValueModifier that uses its own Text widget
     * created with the given parent and style.
     * @param parent the parent Composite of the Text widget. Cannot be null.
     * @param style the style for the Text widget
     * @throws IllegalArgumentException if parent is null.
     */
    public TextPolicyValueModifier(Composite parent, int style,
                                   String policyName,
                                   DeviceRepositoryAccessorManager dram) {
        if (parent == null) {
            throw new IllegalArgumentException("Cannot be null: parent");
        }
        text = new Text(parent, style);
        // add a listener
        text.addModifyListener(modListener);
        this.policyName = policyName;
        this.deviceRAM = dram;
        initAccessibility(text);
    }

    // javadoc inherited
    public Control getControl() {
        return text;
    }

    // javadoc inherited
    protected void setValue(String value) {
        if (!modifying) {
            // remove the listener, update the text widget
            // then add the listener back to it.
            text.removeModifyListener(modListener);
            text.setText(value == null ? "" : value);
            text.addModifyListener(modListener);
        }
    }

    /**
     * Get the Text widget used by this TextPolicyValueModifier.
     * @return the text
     */
    public Text getText() {
        return text;
    }

    /**
     * Creates an {@link AccessibleListener} for the specified control and
     * associates it with its {@link org.eclipse.swt.accessibility.Accessible}
     * object. This allows the name for the control to be generated correctly.
     * @param control
     */
    private void initAccessibility(Control control) {
        AccessibleListener al = new AccessibleAdapter() {
            public void getName(AccessibleEvent ae) {
                String name = policyName != null ? policyName :
                        policyElement.getAttributeValue(
                                DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE);
                name = deviceRAM.getLocalizedPolicyName(name);
                ae.result = name;
            }
        };
        control.getAccessible().addAccessibleListener(al);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 01-Mar-05	7169/1	pcameron	VBM:2005021109 Workaround for TextPolicyValueModifier on Windows

 01-Mar-05	7165/1	pcameron	VBM:2005021109 Workaround for TextPolicyValueModifier on Windows

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Nov-04	6199/1	adrianj	VBM:2004102512 Added accessibility support to PolicyValueModifiers

 14-May-04	4403/1	matthew	VBM:2004051404 Stop the device editors dirty flag from being set when a new device with different policy values is selected

 11-Mar-04	3398/4	pcameron	VBM:2004030906 Some further renamings

 11-Mar-04	3398/2	pcameron	VBM:2004030906 Renamed PolicyValueOriginSelector and associated classes and added method to PolicyValueModifier interface

 05-Mar-04	3318/5	pcameron	VBM:2004022305 Added ComboPolicyValueModifier and refactored

 04-Mar-04	3284/18	pcameron	VBM:2004022007 Rework issues with TextPolicyValueModifier

 03-Mar-04	3284/7	pcameron	VBM:2004022007 Added TextPolicyValueModifier

 ===========================================================================
*/
