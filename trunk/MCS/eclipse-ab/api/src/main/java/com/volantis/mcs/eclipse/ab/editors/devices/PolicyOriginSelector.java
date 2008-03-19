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

import com.volantis.mcs.eclipse.controls.ControlsMessages;
import com.volantis.mcs.eclipse.controls.ImageDropDown;
import com.volantis.mcs.eclipse.controls.ImageDropDownItem;
import com.volantis.mcs.eclipse.controls.StandardAccessibleListener;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import java.text.MessageFormat;

/**
 * The PolicyOriginSelector control indicates where a policy has been set. The
 * control provides the user with three choices: override, fallback and restore.
 * The selection is returned by the {@link #getPolicySelectorOriginType}.
 */
public class PolicyOriginSelector extends Composite {

    /**
     * The resource prefix for this control.
     */
    private static final String RESOURCE_PREFIX =
            "PolicyOriginSelector.";

    /**
     * The name of the control.
     */
    private static final String POLICY_ORIGIN_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "policyOrigin");

    /**
     * The menu item text for fallback.
     */
    private static final String FALLBACK_MENU_TEXT =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "fallback.text");

    /**
     * The menu item text for override.
     */
    private static final String OVERRIDE_MENU_TEXT =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "override.text");

    /**
     * The menu item text for restore.
     */
    private static final String RESTORE_MENU_TEXT =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "restore.text");

    /**
     * The tooltip format for fallback.
     */
    private static final String FALLBACK_TOOLTIP_FORMAT =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "fallback.tooltip");

    /**
     * The tooltip format for override.
     */
    private static final String OVERRIDE_TOOLTIP_FORMAT =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "override.tooltip");

    /**
     * The tooltip format for restore.
     */
    private static final String RESTORE_TOOLTIP_FORMAT =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "restore.tooltip");

    /**
     * An array of PolicyOriginSelections used to associate various data
     * as private members.
     */
    private PolicyOriginSelection[] selectorOrigins;

    /**
     * The name of the policy currently being used by PolicyOriginSelector.
     */
    private String policyName;

    /**
     * The name of the originating device. This is set each time
     * {#setDetails} is called.
     */
    private String originatingDeviceName;

    /**
     * The ImageDropDown widget,
     */
    private ImageDropDown imageDropDown;

    /**
     * The image indicating fallback.
     */
    private Image fallbackImage;

    /**
     * The image indicating override.
     */
    private Image overrideImage;

    /**
     * The image indicating restore.
     */
    private Image restoreImage;

    /**
     * This array is defined to reduce garbage collection. It is used when
     * setting the device name into the tooltip message formats.
     */
    private final String[] deviceNameForFormat = new String[1];

    /**
     * The name of the currently selected device.
     */
    private String selectedDeviceName;

    /**
     * The {@link DeviceRepositoryAccessorManager}
     * used by PolicyOriginSelector to query the device repository for
     * fallback devices and originating devices.
     */
    private final DeviceRepositoryAccessorManager deviceRAM;

    /**
     * Flag indicating whether or not this PolicyOriginSelector is being used
     * in an admin project
     */
    private boolean isAdminProject;

    /**
     * Constructs a PolicyOriginSelector control.
     * @param parent the parent Composite
     * @param style the style for the control
     * @param deviceRAM the device repository access manager used to determine
     *                  fallback and originating devices. Cannot be null.
     * @param isAdminProject flag indicating whether or not this
     * PolicyOriginSelector is being used in an admin project
     * @throws IllegalArgumentException if deviceRAM is null
     */
    public PolicyOriginSelector(Composite parent, int style,
                                DeviceRepositoryAccessorManager deviceRAM,
                                boolean isAdminProject) {
        super(parent, style);
        if (deviceRAM == null) {
            throw new IllegalArgumentException("Cannot be null: deviceRAM");
        }
        this.deviceRAM = deviceRAM;
        this.isAdminProject = isAdminProject;
        createControl();
        initAccessible();
    }

    /**
     * Creates and adds the widgets to the PolicyOriginSelector control.
     */
    private void createControl() {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        this.setLayout(layout);
        addImageDropDown();
        this.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                disposeResources();
            }
        });
    }

    /**
     * Creates and adds the three ImageDropDown widgets.
     */
    private void addImageDropDown() {
        // Create the images used by the ImageDropDownItems.
        fallbackImage =
                DevicesMessages.getImage(RESOURCE_PREFIX + "fallback.icon");
        overrideImage =
                DevicesMessages.getImage(RESOURCE_PREFIX + "override.icon");
        restoreImage =
                DevicesMessages.getImage(RESOURCE_PREFIX + "restore.icon");

        // Create the ImageDropDownItems.
        ImageDropDownItem overrideDDI =
                new ImageDropDownItem(overrideImage,
                        OVERRIDE_MENU_TEXT, OVERRIDE_TOOLTIP_FORMAT);
        ImageDropDownItem fallbackDDI =
                new ImageDropDownItem(fallbackImage,
                        FALLBACK_MENU_TEXT, FALLBACK_TOOLTIP_FORMAT);
        ImageDropDownItem restoreDDI =
                new ImageDropDownItem(restoreImage,
                        RESTORE_MENU_TEXT, RESTORE_TOOLTIP_FORMAT);

        selectorOrigins = new PolicyOriginSelection[3];

        selectorOrigins[0] =
                new PolicyOriginSelection(PolicyOriginSelectionType.OVERRIDE,
                        overrideDDI,
                        new MessageFormat(OVERRIDE_TOOLTIP_FORMAT));

        selectorOrigins[1] =
                new PolicyOriginSelection(PolicyOriginSelectionType.FALLBACK,
                        fallbackDDI,
                        new MessageFormat(FALLBACK_TOOLTIP_FORMAT));

        selectorOrigins[2] =
                new PolicyOriginSelection(PolicyOriginSelectionType.RESTORE,
                        restoreDDI,
                        new MessageFormat(RESTORE_TOOLTIP_FORMAT));

        // The order of the items here must be the same as the order of
        // the correspondng items created in the selectorOrigins array above.
        ImageDropDownItem[] imageDropDownItems = new ImageDropDownItem[]{
            overrideDDI, fallbackDDI, restoreDDI
        };

        // Create the ImageDropDown and add a SelectionListener to listen for
        // menu item selections.
        imageDropDown = new ImageDropDown(this, SWT.NONE, imageDropDownItems);
        imageDropDown.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                changeState();
            }
        });
    }

    /**
     * Update the tooltip text of the ImageDropDown. This is typically by
     * inserting the device name into the tooltip's message format. There is
     * also a special case when the currently selected device is the root
     * device and the menu item selection is Overrides. In this one case, the
     * tooltip text should be blank.
     */
    private void updateTooltip(int selectedItem) {
        String tooltip = null;
        boolean isRootDevice = deviceRAM.retrieveRootDeviceName().
                equals(selectedDeviceName);
        selectorOrigins[0].imageDDI.setEnabled(!isRootDevice);
        selectorOrigins[1].imageDDI.setEnabled(!isRootDevice);
        selectorOrigins[2].imageDDI.setEnabled(!isAdminProject);

        if (getPolicySelectorOriginType() != PolicyOriginSelectionType.RESTORE
                && isRootDevice) {
            tooltip = "";
        } else {
            // The originating device is the device used for the tooltip,
            // unless there's an override in which case the originating device
            // is the same as the selected device. In this case you need to
            // find the originating device of the selected device's fallback
            // device.
            //
            // For example, for the policy pixelsx, the Microsoft-WebTV
            // defines this value, providing an override. The parent of
            // Microsoft-WebTV is WebTV. WebTV does not provide a value for
            // pixelsx but inherits it from TV. Therefore, for the
            // Microsoft-WebTV and pixelsx pair, the tooltips will be
            // "Overrides TV" and "Falls back to TV", with the former
            // initially selected.
            String deviceForTP = originatingDeviceName;
            if (originatingDeviceName.equals(selectedDeviceName)) {
                deviceForTP = deviceRAM.
                        getFallbackDeviceName(originatingDeviceName);
                if (deviceForTP != null &&
                        !deviceRAM.retrieveRootDeviceName().
                        equals(deviceForTP)) {
                    deviceForTP = deviceRAM.getOriginatingDevice(deviceForTP,
                            policyName);
                }
            }
            MessageFormat msgF = selectorOrigins[selectedItem].tooltipMF;
            deviceNameForFormat[0] =
                    deviceForTP == null ? originatingDeviceName : deviceForTP;
            originatingDeviceName = deviceNameForFormat[0];
            tooltip = msgF.format(deviceNameForFormat);
        }
        selectorOrigins[selectedItem].imageDDI.setToolTipText(tooltip);
    }

    /**
     * Disposes of the image resources used for the menu items.
     */
    private void disposeResources() {
        for (int i = 0; i < selectorOrigins.length; i++) {
            Image image = selectorOrigins[i].imageDDI.getImage();
            if (image != null && !image.isDisposed()) {
                image.dispose();
            }
        }
    }

    /**
     * Changes the state of the ImageDropDown when the user selects a
     * different menu item.
     */
    private void changeState() {
        // Update the fallback or override tooltip
        if (getPolicySelectorOriginType() != PolicyOriginSelectionType.RESTORE) {
            updateTooltip(imageDropDown.getItem());
        }
    }

    /**
     * Sets the PolicyOriginSelectorDetails for this
     * PolicyOriginSelector. The details become the current selection
     * for the control, and the control updates itself appropriately.
     * @param details the details to set
     * @throws IllegalArgumentException if details is null
     */
    public void setDetails(PolicyOriginSelectorDetails details) {
        if (details == null) {
            throw new IllegalArgumentException("Cannot be null: details");
        }
        selectedDeviceName = details.selectedDeviceName;
        policyName = details.policyName;

        // Find the originating device.
        originatingDeviceName = deviceRAM.
                getOriginatingDevice(selectedDeviceName,
                        policyName);

        // Update the state of the control by selecting the correct option,
        // and changing the tooltip.
        if (!originatingDeviceName.equals(selectedDeviceName)) {
            // Fallback
            imageDropDown.setItem(selectorOrigins[1].imageDDI);
        } else {
            // Override
            imageDropDown.setItem(selectorOrigins[0].imageDDI);
        }
        updateTooltip(imageDropDown.getItem());
    }

    /**
     * Returns the current selection type of the PolicyOriginSelector. The
     * selection type is one of:
     * {@link PolicyOriginSelectionType#OVERRIDE},
     * {@link PolicyOriginSelectionType#FALLBACK} or
     * {@link PolicyOriginSelectionType#RESTORE}.
     * @return the selected {@link PolicyOriginSelectionType}.
     */
    public PolicyOriginSelectionType getPolicySelectorOriginType() {
        return selectorOrigins[imageDropDown.getItem()].type;
    }

    /**
     * Adds a SelectionListener.
     * @param listener the listener to add
     */
    public void addSelectionListener(SelectionListener listener) {
        imageDropDown.addSelectionListener(listener);
    }

    /**
     * Removes a SelectionListener.
     * @param listener the listener to remove
     */
    public void removeSelectionListener(SelectionListener listener) {
        imageDropDown.removeSelectionListener(listener);
    }

    // javadoc inherited
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        imageDropDown.setEnabled(enabled);
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        StandardAccessibleListener al =
                new StandardAccessibleListener(imageDropDown) {
            public void getName(AccessibleEvent event) {
                event.result = POLICY_ORIGIN_TEXT;
            }
        };
        imageDropDown.getAccessible().addAccessibleListener(al);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 07-Jan-05	6603/1	adrianj	VBM:2004120801 Added names for accessible components

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 18-May-04	4410/1	byron	VBM:2004051307 Device Editor Administrator mode proliferates standard elements

 14-Apr-04	3683/5	pcameron	VBM:2004030401 Some further tweaks

 14-Apr-04	3683/3	pcameron	VBM:2004030401 Some tweaks to PolicyController and refactoring of PolicyOriginSelection

 01-Apr-04	3602/3	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 11-Mar-04	3398/3	pcameron	VBM:2004030906 Renamed PolicyValueOriginSelector and associated classes and added method to PolicyValueModifier interface

 04-Mar-04	3284/11	pcameron	VBM:2004022007 Rework issues with TextPolicyValueModifier

 03-Mar-04	3284/2	pcameron	VBM:2004022007 Added TextPolicyValueModifier

 02-Mar-04	3197/28	pcameron	VBM:2004021904 Changed name field from default to private

 01-Mar-04	3197/19	pcameron	VBM:2004021904 Rework issues

 01-Mar-04	3197/11	pcameron	VBM:2004021904 Added PolicyValueOriginSelector

 ===========================================================================
*/
