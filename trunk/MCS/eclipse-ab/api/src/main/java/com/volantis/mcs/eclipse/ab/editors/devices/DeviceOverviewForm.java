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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElement;
import com.volantis.mcs.eclipse.ab.editors.dom.AlertsActionsSection;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelection;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilterConfiguration;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;

/**
 * Composite that displays the form sections that constitute the device
 * overview page
 */
public class DeviceOverviewForm extends Composite implements XPathFocusable {

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(DeviceOverviewForm.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.
            createExceptionLocalizer(DeviceOverviewForm.class);

    /**
     * The prefix for resources associated with this class.
     */
    private static final String RESOURCE_PREFIX =
            "DeviceOverviewForm.";

    /**
     * Constant for device overview forms margin height
     */
    private static final int MARGIN_HEIGHT = EditorMessages.getInteger(
            "Editor.marginHeight").intValue();

    /**
     * Constant for device overview forms margin width
     */
    private static final int MARGIN_WIDTH = EditorMessages.getInteger(
            "Editor.marginWidth").intValue();

    /**
     * Constant for device overview forms horizontal spacing
     */
    private static final int HORIZONTAL_SPACING = EditorMessages.getInteger(
            "Editor.horizontalSpacing").intValue();

    /**
     * Constant for device overview forms vertical spacing
     */
    private static final int VERTICAL_SPACING = EditorMessages.getInteger(
            "Editor.verticalSpacing").intValue();

    /**
     * The text for the Restore Defaults button.
     */
    private static final String RESTORE_DEFAULTS_TEXT =
            DevicesMessages.getString(RESOURCE_PREFIX + "restoreDefaults.text");

    /**
     * Create a filter so that only device elements are included in
     * the selection.
     */
    private static final ODOMSelectionFilter DEVICE_FILTER =
            new ODOMSelectionFilter(null, new String[]{
                DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME},
                    new ODOMSelectionFilterConfiguration(true, true));


    /**
     * FormSection that displays the hierarchy document in a tree format.
     */
    private DeviceHierarchySection deviceHierarchySection;

    /**
     * The ODOMEditor context for the device repository
     */
    private DeviceEditorContext context;

    /**
     * The Restore Defaults button.
     */
    private Button restoreButton;

    /**
     * Maintain the selected element so that we restore it if necessary (via
     * the restore action).
     */
    private Element deviceIDElement;

    /**
     * Initializes a <code>DeviceOverviewForm</code> with the given arguments
     * @param parent the parent composite
     * @param style a bitset used to specify any styles
     * @param context the DeviceEditorContext.
     */
    public DeviceOverviewForm(Composite parent,
                              int style,
                              DeviceEditorContext context) {
        super(parent, style);
        this.context = context;
        createDisplayArea();
    }

    /**
     * Creates the display area for this form
     */
    private void createDisplayArea() {
        // create the top level layout for the form
        GridLayout layout = new GridLayout();
        layout.marginHeight = MARGIN_HEIGHT;
        layout.marginWidth = MARGIN_WIDTH;
        layout.verticalSpacing = VERTICAL_SPACING;
        layout.horizontalSpacing = HORIZONTAL_SPACING;
        GridData data = new GridData(GridData.FILL_BOTH);
        setLayout(layout);
        setLayoutData(data);
        // set the background color to white
        Color white = getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
        setBackground(white);

        // add an alerts and actions section
        AlertsActionsSection alerts =
                new AlertsActionsSection(this, SWT.NONE, context);

        alerts.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // add a two column contianer for the form sections. The first
        // column will display the DeviceHierarchy section and the second
        // column will display the primary and secondary pattern sections.
        Composite formContainer = new Composite(this, SWT.NONE);
        GridLayout formLayout = new GridLayout(2, false);
        formLayout.marginWidth = 0;
        formLayout.marginHeight = 0;
        formLayout.horizontalSpacing = HORIZONTAL_SPACING;
        GridData formData = new GridData(GridData.FILL_BOTH);
        formContainer.setLayout(formLayout);
        formContainer.setLayoutData(formData);
        formContainer.setBackground(white);

        // add the hierarchy section to the formContainer
        deviceHierarchySection = new DeviceHierarchySection(formContainer,
                SWT.NONE,
                context);
        GridData hierarchyData = new GridData(GridData.FILL_VERTICAL);
        deviceHierarchySection.setLayoutData(hierarchyData);

        // add a scroll area for the patterns sections
        ScrolledComposite controlsScroller = new ScrolledComposite(formContainer,
                SWT.H_SCROLL | SWT.V_SCROLL);
        controlsScroller.setLayout(new FillLayout());
        controlsScroller.setLayoutData(new GridData(GridData.FILL_BOTH));
        controlsScroller.setExpandHorizontal(true);
        controlsScroller.setExpandVertical(true);
        controlsScroller.setAlwaysShowScrollBars(false);
        controlsScroller.setBackground(white);

        // add the container for the patterns sections
        Composite patternContainer = new Composite(controlsScroller, SWT.NONE);
        GridLayout patternLayout = new GridLayout();
        patternLayout.marginHeight = 0;
        patternLayout.marginWidth = 0;
        patternLayout.verticalSpacing = VERTICAL_SPACING;
        GridData patternData = new GridData(GridData.FILL_BOTH);
        patternContainer.setLayout(patternLayout);
        patternContainer.setLayoutData(patternData);
        patternContainer.setBackground(white);

        // Add the primary patterns section
        PrimaryPatternsSection primaryPatterns =
                new PrimaryPatternsSection(patternContainer,
                        SWT.NONE,
                        context);
        primaryPatterns.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Add the secondary patterns section
        SecondaryPatternsSection secondaryPatterns =
                new SecondaryPatternsSection(patternContainer,
                        SWT.NONE,
                        context);
        secondaryPatterns.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        createRestoreMechanism(patternContainer);

        TACPatternsSection TACPatterns =
                new TACPatternsSection(patternContainer,
                        SWT.NONE,
                        context);
        TACPatterns.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        patternContainer.setSize(patternContainer.
                computeSize(SWT.DEFAULT, SWT.DEFAULT));
        patternContainer.layout();
        controlsScroller.setMinSize(
                patternContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        controlsScroller.setContent(patternContainer);

        layout();
    }

    /**
     * Get the name of the selected device.
     * @return the name of the selected device or null if none selected.
     */
    String getSelectedDeviceName() {
        String selectedDeviceName = null;
        if (deviceIDElement != null) {
            selectedDeviceName = deviceIDElement.
                    getAttributeValue(DeviceRepositorySchemaConstants.
                    DEVICE_NAME_ATTRIBUTE);
        }

        return selectedDeviceName;
    }

    /**
     * Add the restore button and the standard element handling facility.
     * @param parent the parent composite for the resotre button.
     */
    private void createRestoreMechanism(Composite parent) {
        restoreButton = new Button(parent, SWT.NONE);
        restoreButton.setText(RESTORE_DEFAULTS_TEXT);
        restoreButton.setEnabled(!context.isAdminProject());
        restoreButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        restoreButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                widgetDefaultSelected(e);
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                if (deviceIDElement != null) {
                    ((DeviceODOMElement) deviceIDElement).restore();
                }
            }
        });

        // Add a listener to the selection manager which responds to device
        // element selections.
        context.getODOMSelectionManager().
                addSelectionListener(new ODOMElementSelectionListener() {

                    public void selectionChanged(ODOMElementSelectionEvent event) {
                        ODOMElementSelection selection = event.getSelection();

                        if (!selection.isEmpty()) {
                            Element deviceElement =
                                    (Element) selection.getFirstElement();
                            // Retrieve the device name.
                            final String deviceName =
                                    deviceElement.getAttributeValue(
                                            DeviceRepositorySchemaConstants.
                                    DEVICE_NAME_ATTRIBUTE);

                            deviceIDElement = context.
                                    getDeviceRepositoryAccessorManager().
                                    retrieveDeviceIdentification(deviceName);

                            if (deviceIDElement == null) {
                                Object params = new Object[] {
                                    deviceName,
                                    context.getDeviceRepositoryAccessorManager().
                                        getDeviceRepositoryName()
                                };
                                LOGGER.error("device-not-found", params);
                                String message =
                                        EXCEPTION_LOCALIZER.
                                        format("device-not-found", params);
                                throw new UndeclaredThrowableException(new
                                        RepositoryException(message));
                            } else {
                                DeviceODOMElement parent = (DeviceODOMElement)
                                        deviceIDElement.getParent();
                                if (parent == null) {
                                    LOGGER.error("device-no-parent", deviceName);
                                    String message =
                                            EXCEPTION_LOCALIZER.
                                            format("device-no-parent",
                                                    deviceName);
                                    throw new UndeclaredThrowableException(new
                                            RepositoryException(message));
                                } else {
                                    parent.submitRestorableName(deviceName);
                                }
                            }
                        } else {
                            deviceIDElement = null;
                        }
                    }

                }, DEVICE_FILTER);
    }

    // javadoc inherited
    public boolean setFocus(XPath path) {
        // todo implement this
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 17-Nov-04	6012/5	allan	VBM:2004051307 Remove standard elements in admin mode.

 16-Nov-04	4394/6	allan	VBM:2004051018 Undo/Redo in device editor.

 17-May-04	4394/3	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 14-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 16-Nov-04	6218/2	adrianj	VBM:2004102021 Enhanced sizing for FormSections

 16-Nov-04	4394/6	allan	VBM:2004051018 Undo/Redo in device editor.

 17-May-04	4394/3	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 14-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 02-Sep-04	5369/3	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 11-Aug-04	5126/1	adrian	VBM:2004080303 Added GUI support for Device TACs

 12-May-04	4307/1	allan	VBM:2004051201 Fix restore button and moveListeners()

 11-May-04	4303/1	allan	VBM:2004051005 Restore button and Widget is disposed bug fix.

 11-May-04	4250/6	pcameron	VBM:2004051005 Added Restore Defaults button and changed ODOMElement and StandardElementHandler to deal with listener removal

 07-May-04	4172/4	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 05-May-04	4115/3	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 04-May-04	4007/1	doug	VBM:2004032304 Added a PrimaryPatterns form section

 22-Apr-04	3878/1	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 ===========================================================================
*/
