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

import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyType;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyTypeComposition;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * The wizard for specify the name, composition and type of a device policy.
 * These can be retrieved with {@link #getPolicyName},
 * {@link #getPolicyTypeComposition} and {@link #getPolicyType} respectively.
 */
public class NewDevicePolicyWizard extends Wizard {

    /**
     * The title for the wizard.
     */
    private static final String TITLE =
            DevicesMessages.getString("NewDevicePolicyWizard.title");

    /**
     * The parent shell of the wizard.
     */
    private final Shell shell;

    /**
     * The manager to use for checking for duplicate policy names.
     */
    private final DeviceRepositoryAccessorManager deviceRAM;

    /**
     * The NewDevicePolicyWizardPage used by this wizard.
     */
    private NewDevicePolicyWizardPage policyPage;

    /**
     * The policy name specified by this wizard.
     */
    private String policyName;

    /**
     * The policy type composition specified by this wizard,
     */
    private PolicyTypeComposition policyTypeComposition;

    /**
     * The policy type specified by this wizard.
     */
    private PolicyType policyType;


    /**
     * Initializes a <code>NewDevicePolicyWizard</code> instance with the given
     * arguments
     * @param shell the shell associated with the Composite that invoked this
     * wizard. Cannot be null.
     * @param deviceRAM the manager to use for checking for duplicate policy
     * names. Cannot be null.
     * @throws IllegalArgumentException if shell or deviceRAM is null
     */
    public NewDevicePolicyWizard(Shell shell,
                                 DeviceRepositoryAccessorManager deviceRAM) {
        if (shell == null) {
            throw new IllegalArgumentException("Cannot be null: shell.");
        }
        if (deviceRAM == null) {
            throw new IllegalArgumentException("Cannot be null: deviceRAM.");
        }
        this.shell = shell;
        this.deviceRAM = deviceRAM;

        // set the window title for this wizard
        setWindowTitle(TITLE);
    }

    // javadoc inherited
    public void addPages() {
        policyPage = new NewDevicePolicyWizardPage(deviceRAM);
        addPage(policyPage);
    }

    /**
     * Validates the page in this wizard.
     * @return true if the page is valid; false otherwise.
     */
    public boolean validate() {
        return policyPage.isPageComplete();
    }

    /**
     * If the wizard can finish, retrieve and store  the policy name,
     * composition and type.
     * @return true if the wizard can finish, false otherwise
     */
    public boolean performFinish() {
        boolean canFinish = policyPage.isPageComplete();
        if (canFinish) {
            policyName = policyPage.getPolicyName();
            policyTypeComposition = policyPage.getPolicyTypeComposition();
            policyType = policyPage.getPolicyType();
        }
        return canFinish;
    }

    /**
     * Gets the policy name specified by this wizard.
     * @return the policy name or null if the wizard was cancelled
     */
    public String getPolicyName() {
        return policyName;
    }

    /**
     * Gets the policy type composition specified by this wizard.
     * @return the {@link PolicyTypeComposition} or null if the wizard was
     * cancelled
     */
    public PolicyTypeComposition getPolicyTypeComposition() {
        return policyTypeComposition;
    }

    /**
     * Gets the policy type specified by this wizard.
     * @return the {@link PolicyType} or null if the wizard was cancelled
     */
    public PolicyType getPolicyType() {
        return policyType;
    }

    /**
     * Creates a WizardDialog and adds this NewDevicePolicyWizard to it.
     * The WizardDialog is then opened.
     */
    public void open() {
        WizardDialog dialog = new WizardDialog(shell, this);
        dialog.create();
        dialog.open();
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

 14-May-04	4384/1	pcameron	VBM:2004050703 NewDevicePolicyWizard checks for duplicate policy names

 26-Apr-04	4001/9	pcameron	VBM:2004032104 Updated javadoc

 26-Apr-04	4001/7	pcameron	VBM:2004032104 Refactored the device policy wizard and page so that no policy element is created or returned

 26-Apr-04	4001/5	pcameron	VBM:2004032104 Added NewDevicePolicyWizard and NewDevicePolicyWizardPage

 ===========================================================================
*/
