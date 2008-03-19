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
package com.volantis.mcs.eclipse.builder.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;

import java.util.List;

import com.volantis.mcs.eclipse.builder.editors.themes.ThemesMessages;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;

/**
 * A wizard for selecting zero or more devices as stored in the device
 * repository. Can provide a list of device names in string form.
 */
public class DeviceSelectionWizard extends Wizard {
    private static final String RESOURCE_PREFIX = "DeviceSelectionWizard.";

    /**
     * Title for the wizard dialog
     */
    private static final String TITLE =
            ThemesMessages.getString(RESOURCE_PREFIX + "title");

    /**
     * The width hint of the wizard page.
     */
    private static final int PAGE_WIDTH_HINT =
            EditorMessages.getInteger("Wizard." +
            "page.width.hint").intValue();

    /**
     * The wizard page that will handle the selection of the devices.
     */
    private DeviceSelectionPage page;

    /**
     * A list of device names selected by this wizard.
     */
    private List devices;

    /**
     * The associated project
     */
    private IProject project;

    /**
     * The parent shell of the wizard.
     */
    private Shell shell;

    /**
     * Construct a device selection wizard with the specified project and parent
     * shell.
     *
     * @param project The project for this wizard
     * @param shell The parent shell for this wizard
     */
    public DeviceSelectionWizard(IProject project, Shell shell) {
        // assert that the arguments passed in are valid
        if (shell == null) {
            throw new IllegalArgumentException("Shell cannot be null.");
        }
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }

        this.project = project;
        this.shell = shell;
        this.devices = null;

        setWindowTitle(TITLE);
    }

    /**
     * Creates a WizardDialog and adds this NewStyleRuleWizard to it to it.
     * The WizardDialog is then opened.
     */
    public void open() {
        WizardDialog dialog = new WizardDialog(shell, this);
        dialog.create();

        // Force the dialog width to the suggested size and then allow it to
        // resize itself - this sets the width of the selector list column to
        // a sensible default.
        Shell dialogShell = dialog.getShell();
        dialogShell.setSize(PAGE_WIDTH_HINT, dialogShell.getSize().y);
        dialogShell.pack();

        dialog.open();
    }

    // javadoc inherited
    public void addPages() {
        // create the single page that this wizard uses
        page = new DeviceSelectionPage(project);
        addPage(page);
    }

    // javadoc inherited
    public boolean performFinish() {
        boolean canFinish = page.isPageComplete();

        if (canFinish) {
            devices = page.getSelectedDevices();
        }
        return canFinish;
    }

    // Javadoc inherited
    public boolean canFinish() {
        return true;
    }

    /**
     * Returns the devices that were selected when this wizard was last finished.
     *
     * @return The devices that were selected when this wizard was last finished
     */
    public List getSelectedDevices() {
        return devices;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 ===========================================================================
*/
