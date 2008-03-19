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

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Element;

/**
 * Permit a new device to be added to the device hierarchy using this wizard.
 */
public class NewDeviceWizard extends Wizard {
    /**
     * Title for the wizard dialog
     */
    private static final String TITLE = DevicesMessages.getString(
            "NewDeviceWizard.title");

    /**
     * The shell that is used to display the wizard dialog.
     */
    private final Shell shell;

    /**
     * The new device wizard page.
     */
    private NewDeviceWizardPage page;

    /**
     * The label provider.
     */
    private final ILabelProvider labelProvider;

    /**
     * The content provider.
     */
    private final IContentProvider contentProvider;

    /**
     * The root element.
     */
    private final Element root;

    /**
     * Need to set the initial selection
     */
    private ISelection selection;

    /**
     * The DeviceEditorContext currently in use.
     */
    private final DeviceEditorContext context;

    /**
     * Create a new instance of the device wizard.
     *
     * @param shell           the <code>Shell</code> used to display the wizard
     *                        page.
     * @param contentProvider the content provider.
     * @param selection       the initial selection which may be null.
     * @param root            the root element.
     * @param context the DeviceEditorContext within which this wizard
     * is used
     */
    public NewDeviceWizard(Shell shell,
                           IContentProvider contentProvider,
                           ISelection selection,
                           Element root,
                           DeviceEditorContext context) {
        if (shell == null) {
            throw new IllegalArgumentException("Cannot be null: shell");
        }
        if (contentProvider == null) {
            throw new IllegalArgumentException("Cannot be null: contentProvider");
        }
        if (root == null) {
            throw new IllegalArgumentException("Cannot be null: root");
        }
        if(context == null) {
            throw new IllegalArgumentException("Cannot be null: context");
        }

        this.shell = shell;
        this.labelProvider = new DeviceHierarchyLabelProvider();
        this.contentProvider = contentProvider;
        this.root = root;
        this.selection = selection;
        this.context = context;

        // set the window title for this wizard
        setWindowTitle(TITLE);
    }

    // javadoc inherited
    public boolean performFinish() {
        return page.isPageComplete();
    }

    // javadoc inherited
    public void addPages() {
        // create the single page that this wizard uses
        page = new NewDeviceWizardPage("NewDeviceWizardPage",
                labelProvider, contentProvider, selection, root, context);
        addPage(page);
    }

    /**
     * Creates a WizardDialog and adds this NewDeviceWizard to it.
     * The WizardDialog is then opened.
     */
    public int open() {
        WizardDialog dialog = new WizardDialog(shell, this);
        dialog.create();
        return dialog.open();
    }

    /**
     * Get the device name.
     * @return the device name.
     */
    public String getDevice() {
        return page.getDevice();
    }

    /**
     * Get the fallback device.
     * @return the fallback device.
     */
    public String getFallbackDevice() {
        return page.getFallbackDevice();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Sep-04	5676/1	allan	VBM:2004092302 Fixes to update client ported from v3.2.2

 28-Sep-04	5615/1	allan	VBM:2004092302 UpdateClient fixes and custom device distinction

 27-Apr-04	4035/1	byron	VBM:2004032403 Create the NewDeviceWizard class

 ===========================================================================
*/
