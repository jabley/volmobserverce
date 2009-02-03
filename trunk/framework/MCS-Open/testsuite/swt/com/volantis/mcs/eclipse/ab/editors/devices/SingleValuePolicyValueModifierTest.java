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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.mcs.eclipse.ab.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.xml.jaxp.JAXPTransformerMetaFactory;

import java.io.IOException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;

/**
 * A test for the SingleValuePolicyValueModifier.
 */
public class SingleValuePolicyValueModifierTest extends ControlsTestAbstract {

    /**
     * The TextPolicyValueModifier under test.
     */
    private TextPolicyValueModifier textPolicyValueModifier;

    /**
     * The ComboPolicyValueModifier under test.
     */
    private ComboPolicyValueModifier comboPolicyValueModifier;

    /**
     * The BooleanPolicyValueModifier under test.
     */
    private BooleanPolicyValueModifier booleanPolicyValueModifier;


    /**
     * The PolicyOriginSelector under test.
     */
    private PolicyOriginSelector pvos;

    /**
     * The details for the PolicyOriginSelector.
     */
    private PolicyOriginSelectorDetails details;

    /**
     * A Combo widget with device names.
     */
    private Combo deviceCombo;

    /**
     * A Combo widget with short policy value names.
     */
    private Combo policyCombo;

    /**
     * The current selected device.
     */
    private String selectedDevice;

    /**
     * The current selected policy.
     */
    private String selectedPolicy;

    /**
     * The device repository access manager.
     */
    private DeviceRepositoryAccessorManager deviceRAM;

    /**
     * Factory for creating JDOM objects. Note that devices do not use
     * the LPDM namespace.
     */
    private static final JDOMFactory factory = new DefaultJDOMFactory();

    /**
     * Creates a SingleValuePolicyValueModifier test.
     * @param title the title of the test
     */
    public SingleValuePolicyValueModifierTest(String title) {
        super(title);
    }

    /**
     * Creates two Combo controls, a PolicyOriginSelector and the
     * TextPolicyValueModifier.
     */
    public void createControl() {
        Composite container = new Composite(getShell(), SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.horizontalSpacing = 10;
        container.setLayout(gridLayout);


        String filename = "/tmp/devices.mdpr";
        try {
            deviceRAM = new DeviceRepositoryAccessorManager(
                    filename, new JAXPTransformerMetaFactory(),
                    new DefaultJDOMFactory(), false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        createDeviceCombo(container);
        createPolicyValueCombo(container);
        createPolicyValueOriginSelector(container);
        createTextPolicyValueModifier(container);

        createPolicyValueOriginSelector(container);
        createComboPolicyValueModifier(container);
        createBooleanPolicyValueModifier(container);

        selectedDevice = deviceCombo.getText();
        selectedPolicy = policyCombo.getText();

        textPolicyValueModifier.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                processModifiedPolicyValue(textPolicyValueModifier);
            }
        });

        comboPolicyValueModifier.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                processModifiedPolicyValue(comboPolicyValueModifier);
            }
        });

        booleanPolicyValueModifier.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                processModifiedPolicyValue(booleanPolicyValueModifier);
            }
        });

        setPolicyValueElementOnModifier(textPolicyValueModifier);
        setPolicyValueElementOnModifier(comboPolicyValueModifier);
        setPolicyValueElementOnModifier(booleanPolicyValueModifier);
    }

    private void createBooleanPolicyValueModifier(Composite container) {
        booleanPolicyValueModifier =
                new BooleanPolicyValueModifier(container, deviceRAM);
    }

    /**
     * Creates a Combo widget with device names.
     * @param parent the parent Composite.
     */
    private void createDeviceCombo(Composite parent) {
        Label deviceLabel = new Label(parent, SWT.NONE);
        deviceLabel.setText("Choose device:");
        final String[] deviceNames = new String[]{
            "Master", "Mobile", "PC", "Voice", "Clamshell", "Handset",
            "Pixo-Handset", "SMS-Handset", "WAP-Handset", "Sony-WAP",
            "Sony-CMD-Z5-WML", "PC", "PC-UNIX", "PC-Win32", "PC-Unix-Mozilla",
            "PC-UNIX-Nautilus", "PC-UNIX-Konqueror", "PC-UNIX-Konqueror-2",
            "PC-UNIX-Konqueror-3", "PC-Win32-IE", "PC-Win32-Mozilla",
            "Web-Box", "Microsoft-WebTV", "TV"
        };
        deviceCombo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
        for (int i = 0; i < deviceNames.length; i++) {
            deviceCombo.add(deviceNames[i]);
        }

        // Adds a SelectionListener which updates the policy value element of
        // the TextPolicyValueModifier.
        deviceCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (!deviceCombo.getText().equals(selectedDevice)) {
                    selectedDevice = deviceCombo.getText();
                    setPolicyValueElementOnModifier(textPolicyValueModifier);
                    setPolicyValueElementOnModifier(comboPolicyValueModifier);
                    setPolicyValueElementOnModifier(booleanPolicyValueModifier);
                    setDetailsOnPVOS();
                }
            }
        });
        deviceCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Creates a Combo widget with short policy value names.
     * @param parent the parent Composite.
     */
    private void createPolicyValueCombo(Composite parent) {
        Label policyLabel = new Label(parent, SWT.NONE);
        policyLabel.setText("Choose policy value name:");
        final String[] policyNames = new String[]{
            "beep", "realaudioinpage", "baudioinpage", "bookmarks",
            "activesky", "aggregation", "brwsrname", "entrytype",
            "download", "firmversion", "disptech", "gifinpage",
            "brwsrvers", "pixeldepth", "pagedown", "pageup",
            "pixelsx", "pixelsy", "fallback", "UAProf.CcppAccept"
        };
        policyCombo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
        for (int i = 0; i < policyNames.length; i++) {
            policyCombo.add(policyNames[i]);
        }
        policyCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (!policyCombo.getText().equals(selectedPolicy)) {
                    selectedPolicy = policyCombo.getText();
                    setPolicyValueElementOnModifier(textPolicyValueModifier);
                    setPolicyValueElementOnModifier(comboPolicyValueModifier);
                    setPolicyValueElementOnModifier(booleanPolicyValueModifier);
                    setDetailsOnPVOS();
                }
            }
        });
        policyCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Creates the PolicyOriginSelector control.
     * @param parent the parent Composite.
     */
    private void createPolicyValueOriginSelector(Composite parent) {
        pvos = new PolicyOriginSelector(parent, SWT.NONE, deviceRAM, false);
        String deviceName = deviceCombo.getText();
        String policyName = policyCombo.getText();
        details = new PolicyOriginSelectorDetails(deviceName,
                policyName);
        pvos.setDetails(details);
    }

    /**
     * Creates the TextPolicyValueModifier.
     * @param parent the parent Composite.
     */
    private void createTextPolicyValueModifier(Composite parent) {
        Text text = new Text(parent, SWT.BORDER);
        textPolicyValueModifier = new TextPolicyValueModifier(text, "name",
                null);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Creates the ComboPolicyValueModifier.
     * @param parent the parent Composite.
     */
    private void createComboPolicyValueModifier(Composite parent) {
        Combo combo = new Combo(parent, SWT.BORDER);
        final String[] policyValues = new String[]{
            "1234", "6789", "value", "9876", "5432"
        };
        combo.setItems(policyValues);

        comboPolicyValueModifier = new ComboPolicyValueModifier(combo, "name",
                null);
        combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Update the PolicyOriginSelector with the new Combo selection.
     */
    private void setDetailsOnPVOS() {
        details = new PolicyOriginSelectorDetails(selectedDevice,
                selectedPolicy);
        pvos.setDetails(details);
    }

    /**
     * Updates the policy value element in response to modifications made by
     * the supplied SingleValuePolicyValueModifier.
     */
    private void processModifiedPolicyValue(
            SingleValuePolicyValueModifier singlePVMod) {
        Element deviceElement =
                deviceRAM.retrieveDeviceElement(selectedDevice);

        Element policies = deviceElement.getChild("policies",
                deviceElement.getNamespace());

        if (policies == null) {
            policies = factory.element("policies",
                    deviceElement.getNamespace());
            deviceElement.addContent(policies);
        }

        Element newPV = singlePVMod.getPolicy();

        String newValue =
                newPV.getAttributeValue(
                        DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE);

        booleanPolicyValueModifier.setValue(newValue);

        /**
         * Commented out for now as the equals is not publically accessible.
         *
         */
        /*
        if (pvos.getPolicyValueSelectorOrigin().
                equals(PolicyOriginSelector.FALLBACK)) {

            if (existingValue != null && !existingValue.equals(newValue)) {
                if (newValue == null) {

                } else {
                    policies.addContent(newPV);
                    setDetailsOnPVOS();
                }
            }

        } else if (pvos.getPolicyValueSelectorOrigin().
                equals(PolicyOriginSelector.OVERRIDE)) {

            if (existingValue != null && !existingValue.equals(newValue)) {
                if (newValue == null) {

                } else {
                    policies.addContent(newPV);
                }
            }
        }
        */
    }

    /**
     * Sets the policy value element for the current selections on the
     * specified SingleValuePolicyValueModifier. The policy value element is
     * for the originating device of the selected device and selected policy.
     * This ensures that a non-null element is retrieved. This element is found
     * each time the value changes.
     */
    private void setPolicyValueElementOnModifier(
            SingleValuePolicyValueModifier singlePVMod) {
        String originatingDevice =
                deviceRAM.getOriginatingDevice(selectedDevice, selectedPolicy);
        Element policyValueElement =
                deviceRAM.retrievePolicy(originatingDevice, selectedPolicy);

        if (!originatingDevice.equals(selectedDevice)) {
            // Get the fallback value and set it on the policy element.
            String fallbackValue =
                    policyValueElement.getAttributeValue(
                            DeviceRepositorySchemaConstants.
                    POLICY_VALUE_ATTRIBUTE);

            // Create a new policy element since the policy value element
            // does not belong to the selected device.
            policyValueElement = factory.element(
                    DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME);

            policyValueElement.setAttribute(
                    DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE,
                    fallbackValue);
        }
        singlePVMod.setPolicy(policyValueElement);
    }

    // javadoc inherited
    public String getSuccessCriteria() {
        return "";
    }

    // javadoc inherited
    public static void main(String[] args) {
        new SingleValuePolicyValueModifierTest(
                "SingleValuePolicyValueModifier test").
                display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-04	6012/3	allan	VBM:2004051307 Remove standard elements in admin mode.

 15-Nov-04	6199/1	adrianj	VBM:2004102512 Added accessibility support to PolicyValueModifiers

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 10-May-04	4210/1	matthew	VBM:2004050502 change DevicesDeviceMigrationjob.java to not write out attributes that are empty

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 29-Mar-04	3574/1	allan	VBM:2004032401 Completed device repository merging. Needs more testing.

 11-Mar-04	3405/3	pcameron	VBM:2004022312 Added BooleanPolicyValueModifier

 11-Mar-04	3398/2	pcameron	VBM:2004030906 Renamed PolicyValueOriginSelector and associated classes and added method to PolicyValueModifier interface

 10-Mar-04	3383/1	pcameron	VBM:2004030412 Added PolicyValueSelectionDialog

 05-Mar-04	3318/6	pcameron	VBM:2004022305 Added ComboPolicyValueModifier and refactored

 04-Mar-04	3284/13	pcameron	VBM:2004022007 Rework issues with TextPolicyValueModifier

 03-Mar-04	3284/3	pcameron	VBM:2004022007 Added TextPolicyValueModifier

 ===========================================================================
*/
