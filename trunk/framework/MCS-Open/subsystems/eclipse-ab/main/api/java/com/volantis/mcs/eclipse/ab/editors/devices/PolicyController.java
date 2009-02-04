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

import java.util.List;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElement;
import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElementFactory;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.odom.*;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.core.ResolvedDevicePolicy;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.JDOMFactory;

/**
 * The PolicyController is the control that allows policies to be modified. It
 * uses two widgets, a PolicyOriginSelector and a PolicyValueModifier. An
 * optional Label widget may also be supplied to provide a text description of
 * the policy. If the PolicyController is overriding a particular policy, the
 * PolicyValueModifier and Label (if any) are enabled and the user can enter a
 * new value. If the policy inherits its value, the PolicyValueModifier and
 * Label are disabled. The PolicyController takes the policy name at
 * construction time. This name is fixed for the lifetime of the
 * PolicyController. The name of the device whose policy is being modified by
 * PolicyController is set with {@link #setDeviceName}.
 * <STRONG>
 * Note that when you have finished using a PolicyController instance, you
 * must call its {@link #dispose} method.
 * </STRONG>
 */
public class PolicyController {

    /**
     * The factory to use for creating <inherit /> elements when fallback is
     * selected.
     */
    private static final JDOMFactory ODOM_FACTORY =
            new DeviceODOMElementFactory();

    /**
     * The name of the policy to modify. This is set on construction and cannot
     * change.
     */
    private final String policyName;

    /**
     * The PolicyValueOriginSelector used by this controller.
     */
    private final PolicyOriginSelector selector;

    /**
     * The PolicyValueModifier used by this controller.
     */
    private final PolicyValueModifier modifier;

    /**
     * The label for the policy name, if any.
     */
    private final Label policyLabel;

    /**
     * The DeviceEditorContext to use.
     */
    private final DeviceEditorContext context;

    /**
     * The name of the device currently being controlled.
     */
    private String deviceName;

    /**
     * The device element with the selected device name whose policies
     * can be modified with the controller.
     */
    private ODOMElement deviceElement;

    /**
     * The current policy origin selection type.
     */
    private PolicyOriginSelectionType currentOriginSelection;

    /**
     * The policy element listener that is used to react odom change events fired
     * by UNDO/REDO events.
     */
    private ODOMChangeListener policiesElementListener;

    /**
     * Creates a PolicyController.
     * @param policyName the name of the policy to control. Cannot be null or
     * empty.
     * @param selector the PolicyValueOriginSelector to use. Cannot be null.
     * @param modifier the PolicyValueModifier to use. Cannot be null.
     * @param policyLabel the Label widget for the policy. Can be null.
     * @param context the DeviceEditorContext to use for retrieving
     * policy information. Cannot be null.
     * @throws IllegalArgumentException if policyName is null or empty, or
     * either selector, modifier or dram is null
     */
    public PolicyController(final String policyName,
                            PolicyOriginSelector selector,
                            final PolicyValueModifier modifier,
                            Label policyLabel,
                            final DeviceEditorContext context) {
        if (policyName == null || policyName.length() == 0) {
            throw new IllegalArgumentException("Cannot be null or empty: " +
                    "policyName.");
        }
        if (selector == null) {
            throw new IllegalArgumentException("Cannot be null: selector.");
        }
        if (modifier == null) {
            throw new IllegalArgumentException("Cannot be null: modifier.");
        }
        if (context == null) {
            throw new IllegalArgumentException("Cannot be null: context");
        }
        this.policyName = policyName;
        this.selector = selector;
        this.modifier = modifier;
        this.policyLabel = policyLabel;
        this.context = context;
        this.currentOriginSelection = selector.getPolicySelectorOriginType();

        // Only enable the PolicyValueModifier controls and label if
        // overriding.
        enableModifierControls(currentOriginSelection ==
                PolicyOriginSelectionType.OVERRIDE);

        // Add the selection listener which processes origin selections.
        this.selector.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                handlePolicyOriginChange();
            }
        });

        policiesElementListener = new ODOMChangeListener() {
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {

                try {
                    ODOMElement policyElement = null;
                    if (event.getSource() instanceof ODOMAttribute &&
                            event.getSource().getParent() == null) {
                        // If the value attribute has just been deleted
                        // then the attribute will become detached and have
                        // no parent. In this case the policyElement is
                        // the old value provided by the event.
                        policyElement = (ODOMElement) event.getOldValue();
                    } else {
                        // Find out what policy element has changed. If it is
                        // the one associated with this controller then update.
                        // The source will either be the policy element we are
                        // interested in or a decendent - either element or
                        // attribute.
                        String xPathString = "ancestor-or-self::" +
                                MCSNamespace.DEVICE.getPrefix() + ":" +
                                DeviceRepositorySchemaConstants.
                                POLICY_ELEMENT_NAME;
                        XPath xPath = new XPath(xPathString,
                                new Namespace[]{MCSNamespace.DEVICE});
                        policyElement = (ODOMElement)
                                xPath.selectSingleNode(event.getSource());
                    }
                    // policyElement could still be null if for example a
                    // standard element has just been added - we are not
                    // interested in this case.
                    if (policyElement != null) {
                        String policyElementName =
                                policyElement.
                                getAttributeValue(DeviceRepositorySchemaConstants.
                                POLICY_NAME_ATTRIBUTE);
                        if (policyElementName.equals(policyName)) {

                            // Retrieve the current policy and give it to the
                            // modifier,
                            ResolvedDevicePolicy rdp = context.
                                    getDeviceRepositoryAccessorManager().
                                    resolvePolicy(
                                            deviceName, policyName);

                            node.removeChangeListener(policiesElementListener);

                            // The resolved policy will be null if the policy
                            // has just been deleted.
                            if (rdp != null) {
                                // Update the origin selector to reflect the new
                                // device element.
                                updateOriginSelector();

                                modifier.setPolicy(rdp.policy);
                                node.addChangeListener(policiesElementListener);
                            } else {
                                dispose();
                            }
                        }
                    }
                } catch (XPathException e) {
                    EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);

                }
            }
        };
    }

    /**
     * Handler for policy origin selector changes. Action is taken only if the
     * selection has changed. The PolicyValueModifier is updated accordingly.
     */
    private void handlePolicyOriginChange() {
        PolicyOriginSelectionType type = selector.getPolicySelectorOriginType();

        if (type != currentOriginSelection) {

            // The selection has changed so process it.
            currentOriginSelection = type;

            if (PolicyOriginSelectionType.OVERRIDE == type) {
                handleOverrideSelection();
            } else if (PolicyOriginSelectionType.FALLBACK == type) {
                handleFallbackSelection();
            } else if (PolicyOriginSelectionType.RESTORE == type) {
                handleRestoreSelection();
            }
        }
    }

    /**
     * Handles changing to the Override policy origin by retrieving the
     * fallback policy, cloning it and adding the clone to the current policies
     * element.
     */
    private void handleOverrideSelection() {
        // Retrieve and clone the current policy.
        ResolvedDevicePolicy resolvedDevicePolicy =
                context.getDeviceRepositoryAccessorManager().
                resolvePolicy(deviceName, policyName);

        ODOMElement policy = (ODOMElement) resolvedDevicePolicy.policy.clone();
        // If the clone already has a standard element then remove it.
        ODOMElement clonedStandardElement =
                (ODOMElement) policy.getChild(
                        DeviceRepositorySchemaConstants.STANDARD_ELEMENT_NAME,
                        deviceElement.getNamespace());
        if (clonedStandardElement != null) {
            clonedStandardElement.detach();
        }

        // Retrieve the policies element.
        ODOMElement policies = (ODOMElement) deviceElement.getChild(
                DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME,
                deviceElement.getNamespace());

        DeviceODOMElement currentPolicy =
                (DeviceODOMElement) context.
                getDeviceRepositoryAccessorManager().
                retrievePolicy(deviceName, policyName);

        UndoRedoManager undoRedoManager = context.getUndoRedoManager();
        undoRedoManager.demarcateUOW();
        try {
            // It may be the case that the user has chosen to override when
            // override was previously selected in which case the resolved policy
            // will come from the current device and we must do some special
            // processing to handle the standard element that should be present
            if (currentPolicy != null) {
                currentPolicy.override((DeviceODOMElement) policy);
            } else {
                // This is an override. Where there is no existing policy so nothing to
                // remove, only something to add.
                policies.addContent(policy);
            }
        } finally {
            undoRedoManager.demarcateUOW();
        }
        // Enable the controls to allow the user to modify the override value.
        enableModifierControls(true);

        modifier.setPolicy(policy);
    }

    /**
     * Handles changing to the Fallback policy origin by replacing all value
     * content or attributes, or field content from the overridden policy and
     * adding an inherit element instead.
     */
    private void handleFallbackSelection() {
        // Retrieve the current policy.
        ODOMElement policy = (ODOMElement)
                context.getDeviceRepositoryAccessorManager().
                resolvePolicy(deviceName, policyName).policy;

        UndoRedoManager undoRedoManager = context.getUndoRedoManager();
        undoRedoManager.demarcateUOW();
        try {
            // Remove all value or field content from the policy.
            if (policy.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                    POLICY_VALUE_ATTRIBUTE) != null) {
                policy.removeAttribute(
                        DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE);
            } else {

                // Remove all value element children, if any.
                policy.removeChildren(DeviceRepositorySchemaConstants.
                        POLICY_VALUE_ELEMENT_NAME, policy.getNamespace());

                // Remove all field element children, if any.
                policy.removeChildren(DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_FIELD_ELEMENT_NAME,
                        policy.getNamespace());
            }

            // Create and add the inherit element
            Element inherit =
                    ODOM_FACTORY.element(DeviceRepositorySchemaConstants.
                    INHERIT_ELEMENT_NAME, policy.getNamespace());

            // Adding the inherit element as the first element child node ensures
            // that it precedes any standard element, as this must come last
            // according to the schema.
            List children = policy.getChildren();
            children.add(0, inherit);

            // Retrieve the fallback policy and give it to the PolicyValueModifier.
            Element fallbackPolicy =
                    context.getDeviceRepositoryAccessorManager().
                    resolvePolicy(deviceName, policyName).policy;
            modifier.setPolicy(fallbackPolicy);

            // Disable the PolicyValueModifier controls since the device now
            // inherits the value rather than providing it directly.
            enableModifierControls(false);
        } finally {
            undoRedoManager.demarcateUOW();
        }
    }

    /**
     * Handles changing to the restore policy origin by restoring the old
     * content using and updating the origin selector accordingly.
     */
    private void handleRestoreSelection() {
        Element policy = context.getDeviceRepositoryAccessorManager().
                retrievePolicy(deviceName, policyName);
        if (policy != null) {

            UndoRedoManager undoRedoManager = context.getUndoRedoManager();
            undoRedoManager.demarcateUOW();
            try {
                // If we are here then there is something to restore.
                ((DeviceODOMElement) policy).restore();

                // We need to do the resolve again in case the restore
                // has changed it.
                policy = context.getDeviceRepositoryAccessorManager().
                        resolvePolicy(deviceName, policyName).policy;

                modifier.setPolicy(policy);
            } finally {
                undoRedoManager.demarcateUOW();
            }
        }
        updateOriginSelector();
    }

    /**
     * Enables or disables the PolicyValueModifier controls and optional label.
     * @param enable enable the controls if true; disable otherwise
     */
    private void enableModifierControls(boolean enable) {
        modifier.getControl().setEnabled(enable);
        if (policyLabel != null) {
            policyLabel.setEnabled(enable);
        }
    }

    /**
     * Updates the PolicyOriginSelector with the current device and policy,
     * and enables the PolicyValueModifier controls accordingly.
     */
    private void updateOriginSelector() {
        selector.setDetails(
                new PolicyOriginSelectorDetails(deviceName, policyName));

        currentOriginSelection = selector.getPolicySelectorOriginType();

        // Only enable the PolicyValueModifier controls and label if
        // overriding.
        enableModifierControls(currentOriginSelection ==
                PolicyOriginSelectionType.OVERRIDE);
    }

    /**
     * Sets the device name for the device element controlled by the
     * PolicyController. Action is only taken if the device element is for a
     * different device.
     * @param deviceName the name of the device. Cannot be null or empty.
     * @throws IllegalArgumentException if deviceElement is null or is not a
     * device element.
     */
    public void setDeviceName(String deviceName) {
        if (deviceName == null || deviceName.length() == 0) {
            throw new IllegalArgumentException("Cannot be null: deviceName");
        }

        // Only do something if the device has changed.
        if (!deviceName.equals(this.deviceName)) {
            this.deviceName = deviceName;

            deviceElement =
                    (ODOMElement) context.getDeviceRepositoryAccessorManager().
                    retrieveDeviceElement(deviceName);

            // Update the origin selector to reflect the new device element.
            updateOriginSelector();

            // Retrieve the current policy and give it to the modifier,
            ResolvedDevicePolicy rdp =
                    context.getDeviceRepositoryAccessorManager().
                    resolvePolicy(deviceName, policyName);
            modifier.setPolicy(rdp.policy);

            // Retrieve the policies element and manage its named policy with a
            // standard handler.
            DeviceODOMElement policies = (DeviceODOMElement) deviceElement.getChild(
                    DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME,
                    deviceElement.getNamespace());
            policies.submitRestorableName(policyName);

            // Remove and then add the listener again (just in case it has been
            // added already). This should ensure only one listener is added.
            policies.removeChangeListener(policiesElementListener);
            policies.addChangeListener(policiesElementListener);
        }
    }

    /**
     * Removes internal ODOMChangeListeners used by PolicyController. This
     * method must be called when a user is finished with this instance
     * of PolicyController.
     */
    public void dispose() {
        if (deviceElement != null) {
            DeviceODOMElement policies = (DeviceODOMElement) deviceElement.getChild(
                    DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME,
                    deviceElement.getNamespace());
            policies.removeChangeListener(policiesElementListener);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	6260/1	allan	VBM:2004110907 NullPointerException and WidgetDisposed error

 16-Nov-04	4394/14	allan	VBM:2004051018 Undo/Redo in device editor.

 16-Nov-04	4394/12	allan	VBM:2004051018 Undo/Redo in device editor.

 16-Nov-04	4394/10	allan	VBM:2004051018 Undo/Redo in device editor.

 17-May-04	4394/5	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 14-May-04	4301/5	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 13-May-04	4301/3	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 14-May-04	4369/1	allan	VBM:2004051311 Override fixed, widget dispose fix, new button fix.

 12-May-04	4307/1	allan	VBM:2004051201 Fix restore button and moveListeners()

 30-Apr-04	4081/4	pcameron	VBM:2004031007 Added PoliciesSection

 14-Apr-04	3683/15	pcameron	VBM:2004030401 Some further tweaks

 14-Apr-04	3683/13	pcameron	VBM:2004030401 Some tweaks to PolicyController and refactoring of PolicyOriginSelection

 13-Apr-04	3683/9	pcameron	VBM:2004030401 Added PolicyController

 ===========================================================================
*/
