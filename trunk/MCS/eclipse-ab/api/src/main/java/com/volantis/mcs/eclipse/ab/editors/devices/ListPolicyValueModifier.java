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
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.controls.ListValueBuilder;
import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElementFactory;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

/**
 * The ListPolicyValueModifier uses a
 * {@link com.volantis.mcs.eclipse.controls.ListValueBuilder} control to
 * present a list of policy values for modification. Depending on the
 * ListValueBuilder used, the list may or may not be ordered, and its items
 * may or may not be editable.
 *
 * The ListPolicyValueModifier allows the modification of a multi-valued
 * policy element. A policy element which has a value attribute cannot be
 * modified with this control. A policy element with no value attribute can be
 * modified. The policy element is set with {@link #setPolicy} and the
 * current (modified) policy element is retrieved with {@link #getPolicy}.
 * The ListPolicyValueModifier can have ModifyListeners registered on it.
 * These listeners are informed of all changes via a ModifyEvent object.
 */
public class ListPolicyValueModifier extends AbstractPolicyValueModifier {

    /**
     * The ListValueBuilder widget to use for displaying policy values.
     */
    private final ListValueBuilder listBuilder;

    /**
     * The factory used to create new value Elements.
     */
    private static final ODOMFactory factory =
            new DeviceODOMElementFactory();

    /**
     * The element filter to use when modifing the policy element's
     * contents.
     */
    private static final ElementFilter ELEMENT_FILTER = new ElementFilter();

    /**
     * The initial policy name this modified was provided with.
     */
    private final String policyName;

    /**
     * The DeviceRepositoryAccessorManager used to retrieve the localised
     * policy name.
     */
    private final DeviceRepositoryAccessorManager deviceRAM;

    /**
     * Creates a ListPolicyValueModifier control.
     * @param listBuilder the ListValueBuilder to use. Cannot be null.
     * @throws IllegalArgumentException if listBuilder is null.
     */
    public ListPolicyValueModifier(ListValueBuilder listBuilder,
                                   String policyName,
                                   DeviceRepositoryAccessorManager dram) {
        if (listBuilder == null) {
            throw new IllegalArgumentException("Cannot be null: listBuilder");
        }
        this.listBuilder = listBuilder;

        // Adds a ModifyListener to the ListValueBuilder which updates the
        // policy element whevener there is a change.
        this.listBuilder.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Object[] items =
                        ListPolicyValueModifier.this.listBuilder.getItems();
                String[] values = new String[items.length];
                for (int i = 0; i < items.length; i++) {
                    values[i] = (String) items[i];
                }
                updatePolicyElement(values);
            }
        });

        this.policyName = policyName;
        this.deviceRAM = dram;
        initAccessibility(listBuilder);
    }

    // javadoc inherited
    protected void refreshControl() {
        // Check that the policy element has no value attribute.
        if (policyElement.getAttribute(
                DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE)
                != null) {
            throw new IllegalArgumentException(
                    "Policy element " +
                    policyElement.getAttributeValue(
                            DeviceRepositorySchemaConstants.
                    POLICY_NAME_ATTRIBUTE) + " has a " +
                    DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE +
                    " attribute.");
        }
        // build up the lists items based on the elements "value" children
        List children = policyElement.getChildren(
                DeviceRepositorySchemaConstants.POLICY_VALUE_ELEMENT_NAME,
                policyElement.getNamespace());
        String[] values = new String[children == null ? 0 : children.size()];
        if (children != null) {
            Iterator it = children.iterator();
            int index = 0;
            while (it.hasNext()) {
                Element child = (Element) it.next();
                // Save the value for the ListValueBuilder.
                values[index++] = child.getText();
            }
        }
        listBuilder.setItems(values);
    }

    /**
     * Updates the policy element in response to changes in the
     * ListValueBuilder control used by ListPolicyValueModifier.
     * @param newPolicyValues the new values for the policy element. Can be
     *                        null.
     */
    private void updatePolicyElement(String[] newPolicyValues) {
        List newChildren = new ArrayList(newPolicyValues.length);

        if (newPolicyValues != null) {
            for (int i = 0; i < newPolicyValues.length; i++) {
                Element valueElement = factory.element(
                        DeviceRepositorySchemaConstants.
                        POLICY_VALUE_ELEMENT_NAME,
                        policyElement.getNamespace());
                valueElement.addContent(newPolicyValues[i]);
                newChildren.add(valueElement);
            }
        }
        prependNewValueContent(newChildren);
        fireModifyEvent(newPolicyValues);
    }

    /**
     * Removes all existing value content and prepends the new value
     * content to the remaining content in the supplied order.
     * @param valueChildren the new value content
     */
    private void prependNewValueContent(List valueChildren) {
        // Only remove value element children. These leaves all other elements
        // such as the standard element intact,
        policyElement.removeChildren(DeviceRepositorySchemaConstants.
                POLICY_VALUE_ELEMENT_NAME, policyElement.getNamespace());

        // Retrieve the remaining children and prepend with the new content.
        // This ensures that the content comes before any standard element.
        List remainingChildren = policyElement.getContent(ELEMENT_FILTER);
        remainingChildren.addAll(0, valueChildren);
    }

    // javadoc inherited
    public Control getControl() {
        return listBuilder;
    }

    /**
     * Creates an {@link AccessibleListener} for the specified control and
     * associates it with its {@link org.eclipse.swt.accessibility.Accessible}
     * object. This allows the name for the control to be generated correctly.
     * @param control
     */
    private void initAccessibility(ListValueBuilder control) {
        String name = policyName !=null ? policyName :
                policyElement.getAttributeValue(
                DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE);
        name = deviceRAM.getLocalizedPolicyName(name);
        control.setAccessibleName(name);

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

 19-Nov-04	6121/1	adrianj	VBM:2004102602 Accessibility support for custom controls

 16-Nov-04	4394/7	allan	VBM:2004051018 Undo/Redo in device editor.

 17-May-04	4394/2	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 13-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 15-Nov-04	6199/1	adrianj	VBM:2004102512 Added accessibility support to PolicyValueModifiers

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 14-May-04	4403/3	matthew	VBM:2004051404 Stop the device editors dirty flag from being set when a new device with different policy values is selected

 07-May-04	4172/3	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 07-May-04	4172/1	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 22-Apr-04	3980/2	pcameron	VBM:2004040510 Bug fix: ListPolicyValueModifier now leaves any standard element intact

 20-Apr-04	3909/1	pcameron	VBM:2004031004 Added CategoryCompositeBuilder

 01-Apr-04	3602/3	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 29-Mar-04	3574/1	allan	VBM:2004032401 Completed device repository merging. Needs more testing.

 19-Mar-04	3416/11	pcameron	VBM:2004022309 Some tweaks to ListValueBuilder and ListPolicyValueModifier

 19-Mar-04	3416/9	pcameron	VBM:2004022309 Some tweaks to ListValueBuilder and ListPolicyValueModifier

 18-Mar-04	3416/6	pcameron	VBM:2004022309 Added ListBuilder and ListPolicyValueModifier with tests

 ===========================================================================
*/
