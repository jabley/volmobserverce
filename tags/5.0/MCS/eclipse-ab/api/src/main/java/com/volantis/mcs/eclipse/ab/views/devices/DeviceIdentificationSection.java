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

import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * FormSection showing the idenfication details for a device.
 */
public class DeviceIdentificationSection extends FormSection {
    /**
     * The prefix for resources associated with this class.
     */
    private static String RESOURCE_PREFIX = "DeviceIdentificationSection.";

    /**
     * String for use when there are no identifiers for a given device.
     */
    private static String NO_IDENTIFIERS = DevicesMessages.
            getString(RESOURCE_PREFIX + "noIdentifiers.message");

    /**
     * Constant for the height hint for the text area
     */
    private static int HEIGHT_HINT = DevicesMessages.getInteger(
                RESOURCE_PREFIX + "heightHint").intValue();

    /**
     * The DeviceRepositoryAccessorManager associated with this
     * DeviceIdentificationSection.
     */
    private DeviceRepositoryAccessorManager accessorManager;


    /**
     * The XMLOutputter for displaying device identification as text.
     */
    private final XMLOutputter xmlOutputter;

    /**
     * The display area control for this section.
     */
    private Text text;

    /**
     * Construct a new DeviceIdentificationSection that derives its
     * content using a specified DeviceRepositoryAccessorManager.
     * @param parent The parent Composite.
     * @param style The swt style - unused.
     * @param accessorManager The DeviceRepositoryAccessorManager from
     * which to obtain the device identification information.
     * @throws IllegalArgumentException If accessorManager is null.
     */
    public DeviceIdentificationSection(Composite parent, int style,
                                       DeviceRepositoryAccessorManager accessorManager) {
        super(parent, style);

        if (accessorManager == null) {
            throw new
                    IllegalArgumentException("Cannot be null: accessorManager");
        }

        this.accessorManager = accessorManager;

        xmlOutputter = new XMLOutputter("   ");
        xmlOutputter.setNewlines(true);
        xmlOutputter.setTrimAllWhite(true);

        createDisplayArea(DevicesMessages.getString(RESOURCE_PREFIX + "title"),
                DevicesMessages.getString(RESOURCE_PREFIX + "message"));
    }

    /**
     * Update the DeviceRepositoryAccessorManager associated with this
     * DevicePoliciesSection. This method will cause the table of policies
     * to be refreshed with content provided by the given
     * DeviceRepositoryAccessorManager.
     * @param dram the DeviceRepositoryAccessorManager
     */
    void updateDeviceRepositoryAccessorManager(
            DeviceRepositoryAccessorManager dram) {
        this.accessorManager = dram;
    }

    /**
     * Set the device whose identification details should be shown in this
     * section. This method will update the text of this section to show
     * the identication details for the named device.
     *
     * A deviceName of null will cause the identifiers section to be blank.
     * Where the device has no identifiers a message is displayed to this
     * effect. Otherwise the identifier element is displayed for the
     * named device.
     *
     * @param deviceName The name of the device whose identification details
     * should be shown in this section.
     */
    public void setDevice(String deviceName) {
        String idDisplay = "";

        if (deviceName != null) {
            Element idElement =
                    accessorManager.retrieveDeviceIdentification(deviceName);
            boolean hasIdentification = false;
            if (idElement != null) {
                List content = idElement.getContent();
                if (content != null && content.size() > 0) {
                    hasIdentification = true;
                }
            }
            idDisplay = hasIdentification
                    ? xmlOutputter.outputString(idElement) : NO_IDENTIFIERS;
        }

        text.setText(idDisplay);
    }

    /**
     * Create the display area for this section.
     */
    private void createDisplayArea(String title, String message) {
        Section section =
                SectionFactory.createSection(this, title, message);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        Composite displayArea = new Composite(section, SWT.NONE);
        section.setClient(displayArea);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        displayArea.setLayout(layout);
        displayArea.setLayoutData(new GridData(GridData.FILL_BOTH));
        displayArea.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        text = new Text(displayArea, SWT.READ_ONLY | SWT.MULTI | SWT.LEFT |
                SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        text.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        data = new GridData(GridData.FILL_BOTH);
        data.heightHint = HEIGHT_HINT;
        text.setLayoutData(data);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 02-Mar-05	7226/1	doug	VBM:2005022101 Fixed Scroll bar issues with DeviceRepositoryBrowser page

 02-Mar-05	7163/2	doug	VBM:2005022101 Fixed Scroll bar issues with DeviceRepositoryBrowser page

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 04-May-04	4007/1	doug	VBM:2004032304 Added a PrimaryPatterns form section

 11-Feb-04	2862/2	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/
