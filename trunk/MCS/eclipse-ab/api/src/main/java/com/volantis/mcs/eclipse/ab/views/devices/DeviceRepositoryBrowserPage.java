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
package com.volantis.mcs.eclipse.ab.views.devices;

import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.Page;

/**
 * Page for browsing the device repository.
 */
public class DeviceRepositoryBrowserPage extends Page {

    /**
     * The control for this page.
     */
    private DeviceRepositoryForm control;

    /**
     * The DeviceRepositoryAccessorManager used by this page.
     */
    private DeviceRepositoryAccessorManager dram;

    /**
     * Create a new DeviceRepositoryBrowserPage to browse the
     * specified device repository.
     * @param dram the DeviceRepositoryAccessorManager
     * used to access the device repository to browse
     */
    public DeviceRepositoryBrowserPage(DeviceRepositoryAccessorManager dram) {
        this.dram = dram;
    }

    // javadoc inherited.
    public void createControl(Composite composite) {
        control = new DeviceRepositoryForm(composite, SWT.NONE,
                dram);
    }

    /**
     * Update the DeviceRepositoryAccessorManager associated with this
     * DeviceRepositoryForm.
     * @param dram the DeviceRepositoryAccessorManager
     */
    public void updateDeviceRepositoryAccessorManager(
            DeviceRepositoryAccessorManager dram) {
        control.updateDeviceRepositoryAccessorManager(dram);
    }

    // javadoc inherited
    public Control getControl() {
        return control;
    }

    // javadoc inherited
    public void setFocus() {
        control.setFocus();
    }

    /**
     * Gets the DeviceRepositoryAccessorManager used by this page.
     * @return the DeviceRepositoryAccessorManager
     */
    public DeviceRepositoryAccessorManager getDeviceRepositoryAccessorManager() {
        return dram;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 27-Apr-04	4050/1	pcameron	VBM:2004040701 Added a device Information page and augmented DeviceRepositoryBrowser's title

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 11-Feb-04	2862/3	allan	VBM:2004020411 Rework issues.

 11-Feb-04	2862/1	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/
