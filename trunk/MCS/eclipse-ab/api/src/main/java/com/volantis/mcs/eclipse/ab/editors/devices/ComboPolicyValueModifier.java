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

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.SWT;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;

/**
 * The ComboPolicyValueModifier allows a user to modify the value of a policy
 * element using a Combo widget with a pre-defined set of values.
 */
public class ComboPolicyValueModifier extends SingleValuePolicyValueModifier {

    /**
     * The Combo widget to use for displaying and modifying policy values.
     */
    private final Combo combo;

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
     * The listener this uses to update the device policies
     */
    private final ModifyListener modListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            updatePolicyElement(combo.getText());
        }
    };

    /**
     * Constructs a new ComboPolicyValueModifier that uses its own Combo widget
     * created with the given parent and style.
     * @param parent the parent Composite of the Combo widget. Cannot be null.
     * @param style the style for the Combo widget
     * @param values an array of values for the Combo. Cannot be null.
     * @throws IllegalArgumentException if parent or values is null.
     */
    public ComboPolicyValueModifier(Composite parent, int style,
                                    String[] values, String policyName,
                                    DeviceRepositoryAccessorManager dram) {
        if (parent == null) {
            throw new IllegalArgumentException("Cannot be null: parent");
        }
        if (values == null) {
            throw new IllegalArgumentException("Cannot be null: values");
        }

        // Make this read only
        combo = new Combo(parent, style | SWT.READ_ONLY);
        combo.setItems(values);
        combo.addModifyListener(modListener);
        this.policyName = policyName;
        this.deviceRAM = dram;
        initAccessibility(combo);
    }

    /**
     * Constructs a new ComboPolicyValueModifier using the supplied Combo
     * widget. The Combo should already be populated with its known values.
     * @param combo the Combo widget to use. Cannot be null.
     * @throws IllegalArgumentException if combo is null
     */
    public ComboPolicyValueModifier(Combo combo, String policyName,
                                    DeviceRepositoryAccessorManager dram) {
        if (combo == null) {
            throw new IllegalArgumentException("Cannot be null: combo");
        }
        this.combo = combo;
        combo.addModifyListener(modListener);
        this.policyName = policyName;
        this.deviceRAM = dram;
        initAccessibility(combo);
    }

    // javadoc inherited
    public Control getControl() {
        return combo;
    }

    // javadoc inherited
    protected void setValue(String value) {
        combo.removeModifyListener(modListener);
        combo.setText(value == null ? "" : value);
        combo.addModifyListener(modListener);
    }

    /**
     * Get the Combo widget used by this ComboPolicyValueModifier.
     * @return the Combo
     */
    public Combo getCombo() {
        return combo;
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
                String name = policyName !=null ? policyName :
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Nov-04	6199/1	adrianj	VBM:2004102512 Added accessibility support to PolicyValueModifiers

 14-May-04	4403/5	matthew	VBM:2004051404 Stop the device editors dirty flag from being set when a new device with different policy values is selected

 14-May-04	4382/3	matthew	VBM:2004051312 fix migration of ssversion to be an ordered list

 14-May-04	4382/1	matthew	VBM:2004051312 fix migration of ssversion to be an ordered list

 11-Mar-04	3398/4	pcameron	VBM:2004030906 Some further renamings

 11-Mar-04	3398/2	pcameron	VBM:2004030906 Renamed PolicyValueOriginSelector and associated classes and added method to PolicyValueModifier interface

 05-Mar-04	3318/3	pcameron	VBM:2004022305 Added ComboPolicyValueModifier and refactored

 ===========================================================================
*/
