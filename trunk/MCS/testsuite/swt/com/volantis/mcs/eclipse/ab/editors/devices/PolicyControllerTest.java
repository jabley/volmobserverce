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

import java.io.File;
import java.io.IOException;

import com.volantis.mcs.eclipse.ab.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElementFactory;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyType;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoInfo;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMemento;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.mcs.xml.jaxp.JAXPTransformerMetaFactory;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import com.volantis.testtools.mocks.MockFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;

/**
 * A manual GUI test for PolicyController.
 */
public class PolicyControllerTest extends ControlsTestAbstract {

    /**
     * A Combo widget with a selection of devices.
     */
    private Combo deviceCombo;

    /**
     * A Combo widget with a selection of policies.
     */
    private Combo policyCombo;

    /**
     * The DeviceRepositoryAccessorManager used by the PolicyController.
     */
    private DeviceRepositoryAccessorManager deviceRAM;

    /**
     * The PolicyValueModifierFactory used to create the PolicyValueModifier
     * used by the PolicyController.
     */
    private PolicyValueModifierFactory pvmFactory;

    /**
     * The PolicyController under test.
     */
    private PolicyController policyController;

    /**
     * The currently selected policy.
     */
    private String currentPolicy;

    /**
     * The Composite container for the policy controls. This is updated with
     * a new PolicyController whenever a different policy is selected.
     */
    private Composite stackComposite;

    /**
     * The device repository file, this will be cleaned up automagically.
     */
    private File deviceRepositoryFile;

    // javadoc inherited
    public PolicyControllerTest(String title, File deviceRepositoryFile) {

        super(title);
        this.deviceRepositoryFile = deviceRepositoryFile;
    }

    // javadoc inherited
    public void createControl() {
        Composite comboContainer = new Composite(getShell(), SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.horizontalSpacing = 10;
        comboContainer.setLayout(gridLayout);

        stackComposite = new Composite(getShell(), SWT.NONE);
        stackComposite.setLayout(new StackLayout());


        // Create the manager from the repository file, giving it an
        // ODOMFactory,
        try {
            deviceRAM = new DeviceRepositoryAccessorManager(
                    deviceRepositoryFile.getPath(),
                    new JAXPTransformerMetaFactory(),
                    new DeviceODOMElementFactory(), false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        // Create the factory for policy value modifiers
        pvmFactory = new PolicyValueModifierFactory(deviceRAM);

        // Create the widgets for the test
        createDeviceCombo(comboContainer);
        createPolicyCombo(comboContainer);
        createPolicyController(policyCombo.getText());
    }

    /**
     * Creates the PolicyController for the given policy name.
     * @param policyName the name of the policy to be controlled by the
     * PolicyController
     */
    private void createPolicyController(String policyName) {
        Composite controllerComposite =
                new Composite(stackComposite, SWT.NONE);

        PolicyOriginSelector pos =
                createPolicyOriginSelector(controllerComposite);

        // The Label widget is only used if the policy type is not boolean,
        // since the boolean modifier does its own labelling
        Label policyLabel = null;

        Element policyTypeElement = deviceRAM.getTypeDefinitionElement(policyName);
        PolicyType policyType = PolicyType.getType(policyTypeElement);
        int numColumns = 2;

        if (!policyType.equals(PolicyType.BOOLEAN)) {
            // Non-boolean so a Label widget is needed.
            policyLabel = new Label(controllerComposite, SWT.NONE);
            // Set the label's text to be the localized policy name.
            policyLabel.setText(deviceRAM.getLocalizedPolicyName(policyName));
            // A non-null label means 3 columns are needed.
            numColumns = 3;
        }

        GridLayout gridLayout = new GridLayout(numColumns, false);
        gridLayout.horizontalSpacing = 10;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        controllerComposite.setLayout(gridLayout);
        controllerComposite.
                setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Create the PolicyValueModifier for the controller and lay it out.
        PolicyValueModifier pvm =
                createPolicyValueModifier(controllerComposite, policyName);
        pvm.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        UndoRedoMementoOriginator orig = new UndoRedoMementoOriginator() {
            public UndoRedoMemento takeSnapshot() {
                return null;
            }
            public void restoreSnapshot(UndoRedoInfo undoRedoInfo) {
            }
        };

        DeviceEditorContext context =
                DeviceEditorContext.createDeviceEditorContext(
                        new MockFile("name"),
                        orig,
                        deviceRAM);

        // Create the controller under test.
        policyController =
                new PolicyController(policyName, pos, pvm, policyLabel,
                        context);

        // Set the controller to be the top of the stack layout. The controller
        // changes each time a new policy is selected.
        ((StackLayout) stackComposite.getLayout()).topControl =
                controllerComposite;

        stackComposite.layout();

        // Simulate a device change so that the controller's device element is
        // set.
        this.handleDeviceChange();
    }


    /**
     * Creates the PolicyOriginSelector control.
     * @param parent the parent Composite.
     */
    private PolicyOriginSelector createPolicyOriginSelector(Composite parent) {
        PolicyOriginSelector pos =
                new PolicyOriginSelector(parent, SWT.NONE, deviceRAM, false);
        pos.setDetails(
                new PolicyOriginSelectorDetails(
                        deviceCombo.getText(), policyCombo.getText()));

        return pos;
    }


    /**
     * Creates the PolicyValueModifier control with a border.
     * @param parent the parent Composite.
     */
    private PolicyValueModifier createPolicyValueModifier(Composite parent,
                                                          String policyName) {
        return pvmFactory.
                createPolicyValueModifier(parent, SWT.BORDER, policyName);
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
        deviceCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleDeviceChange();
            }
        });
        deviceCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Creates a Combo widget with short policy names.
     * @param parent the parent Composite.
     */
    private void createPolicyCombo(Composite parent) {
        Label policyLabel = new Label(parent, SWT.NONE);
        policyLabel.setText("Choose policy:");
        final String[] policyNames = new String[]{
            "beep", "realaudioinpage", "baudioinpage", "bookmarks",
            "activesky", "aggregation", "brwsrname", "entrytype",
            "download", "firmversion", "disptech", "gifinpage",
            "brwsrvers", "pixeldepth", "pagedown", "pageup",
            "pixelsx", "pixelsy", "java"
        };
        policyCombo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
        for (int i = 0; i < policyNames.length; i++) {
            policyCombo.add(policyNames[i]);
        }
        policyCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handlePolicyChange();
            }
        });
        policyCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Handles selecting a device from the device Combo by retrieving the device
     * element and giving it to the PolicyController.
     */
    private void handleDeviceChange() {
        policyController.setDeviceName(deviceCombo.getText());
    }

    /**
     * Handles selecting a policy from the policy Combo. Action is taken only
     * if the policy changes. When the policy changes, a new PolicyController
     * is created.
     */
    private void handlePolicyChange() {
        String policyName = policyCombo.getText();
        if (!policyName.equals(currentPolicy)) {
            currentPolicy = policyName;
            createPolicyController(policyName);
        }
    }

    // javadoc inherited
    public String getSuccessCriteria() {
        return "";
    }

    // javadoc inherited
    public static void main(String[] args) {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        try {
            manager.executeWith(new TemporaryFileExecutor() {
                public void execute(File repositoryFile) throws Exception {
                    new PolicyControllerTest("PolicyControllerTest",
                            repositoryFile).display();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-04	6012/3	allan	VBM:2004051307 Remove standard elements in admin mode.

 16-Nov-04	4394/8	allan	VBM:2004051018 Undo/Redo in device editor.

 16-Nov-04	4394/2	allan	VBM:2004051018 Undo/Redo in device editor.

 13-Sep-04	5315/3	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 30-Apr-04	4081/2	pcameron	VBM:2004031007 Added PoliciesSection

 29-Apr-04	4103/1	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 13-Apr-04	3683/2	pcameron	VBM:2004030401 Added PolicyController

 ===========================================================================
*/
