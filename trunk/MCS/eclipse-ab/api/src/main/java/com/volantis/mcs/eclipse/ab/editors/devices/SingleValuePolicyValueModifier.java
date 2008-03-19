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

import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Event;
import org.jdom.Attribute;
import org.jdom.Element;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;

/**
 * The SingleValuePolicyValueModifier allows a user to modify the value of a
 * single-valued policy using an SWT control. The policy element is
 * supplied with {@link #setPolicy}, and the modified policy can be
 * retrieved with {@link #getPolicy}. ModifyListeners can be added and
 * removed from a SingleValuePolicyValueModifier. When a change occurs, a
 * ModifyEvent is fired off to all registered listeners. This event object
 * contains the changed text in its data field, and the SWT control that gave
 * rise to the change in its widget field.
 */
public abstract class SingleValuePolicyValueModifier
            extends AbstractPolicyValueModifier {

    /**
     * Constructs a new SingleValuePolicyValueModifier.
     */
    protected SingleValuePolicyValueModifier() {
    }

    /**
     * Sets the current value of the policy element as the current
     * value of the Control used by the subclass. This is used by
     * SingleValuePolicyValueModifier to display the current policy value in
     * the control when the policy element is set with {@link #setPolicy}.
     * @param value the value to set
     */
    protected abstract void setValue(String value);

    // javadoc inherited
    protected void refreshControl() {

        // Check that the element is not multi-valued.
        if(!policyElement.getChildren(
                    DeviceRepositorySchemaConstants.POLICY_VALUE_ELEMENT_NAME,
                    policyElement.getNamespace()).isEmpty()) {
            throw new IllegalArgumentException(
                        "Element " + policyElement.getName() +
                        " is not single-valued.");
        }
        String value = policyElement.getAttributeValue(
                DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE);
        setValue(value);
    }

    /**
     * Updates the policy element in response to changes in the control
     * used by SingleValuePolicyValueModifier. Subclasses should call this
     * method when notified of changes in their control.
     * @param newPolicyValue the new value for the policy element. Can be null.
     */
    protected void updatePolicyElement(String newPolicyValue) {
        boolean valueChanged = false;

        Attribute valueAttr = policyElement.getAttribute(
                DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE);

        if (newPolicyValue == null || newPolicyValue.length() == 0) {
            if (valueAttr != null) {
                // The new value is empty and there is an existing value
                // attribute so remove it.
                policyElement.removeAttribute(valueAttr);
                valueChanged = true;
            }
        } else if (valueAttr == null) {
            // setAttribute removes any existing attribute and creates the
            // attribute if it does not already exist.
            policyElement.setAttribute(
                    DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE,
                    newPolicyValue);
            valueChanged = true;
        } else {
            // Update the existing value Attribute with the new value.
            valueAttr.setValue(newPolicyValue);
            valueChanged = true;
        }

        if (valueChanged) {
            fireModifyEvent(newPolicyValue);
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

 01-Apr-04	3602/3	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 29-Mar-04	3574/1	allan	VBM:2004032401 Completed device repository merging. Needs more testing.

 11-Mar-04	3405/1	pcameron	VBM:2004022312 Added BooleanPolicyValueModifier

 11-Mar-04	3398/5	pcameron	VBM:2004030906 Some further renamings

 11-Mar-04	3398/3	pcameron	VBM:2004030906 Renamed PolicyValueOriginSelector and associated classes and added method to PolicyValueModifier interface

 08-Mar-04	3318/5	pcameron	VBM:2004022305 Fixed some javadoc

 05-Mar-04	3318/3	pcameron	VBM:2004022305 Added ComboPolicyValueModifier and refactored

 ===========================================================================
*/
