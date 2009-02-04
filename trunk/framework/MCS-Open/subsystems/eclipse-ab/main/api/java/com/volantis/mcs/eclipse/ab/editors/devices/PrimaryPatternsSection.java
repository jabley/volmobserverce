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
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.devices.validation.DeviceLocationDetails;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetails;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetailsRegistry;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.MarkerGeneratingErrorReporter;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelection;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilterConfiguration;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.controls.ListValueBuilder;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.mcs.repository.RepositoryException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;
import org.xml.sax.SAXException;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.Element;

import java.text.MessageFormat;

/**
 * Form section that can be use to view and edit a devices primary patterns
 */
public class PrimaryPatternsSection
        extends FormSection implements XPathFocusable {

    /**
     * The prefix for resource messages associated with this class.
     */
    private static final String RESOURCE_PREFIX =
            "PrimaryPatternsSection."; 

    /**
     * The title for this form section
     */
    private static final String TITLE =
            DevicesMessages.getString(RESOURCE_PREFIX + "title");

    /**
     * The message for this form section
     */
    private static final String MESSAGE =
            DevicesMessages.getString(RESOURCE_PREFIX + "message");

    /**
     * The error location message format.
     */
    private static final MessageFormat ERROR_LOCATION_FORMAT =
            new MessageFormat(DevicesMessages.getString(RESOURCE_PREFIX +
            "locationDetails"));

    /**
     * An empty string array
     */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * The context associated with the editor
     */
    private DeviceEditorContext context;

    /**
     * The composite that this section uses to display its controls.
     */
    private Composite displayArea;

    /**
     * The control that will be used to display the selected devices patterns
     */
    private ListValueBuilder listBuilder;

    /**
     * A Modify listener that will be registered with the listBuilder in order
     * update the devices indentification xml
     */
    private ModifyListener listBuilderListener;

    /**
     * The ODOMElement that is the identification for the selected devcie.
     * Will be null if no device is selected.
     */
    private ODOMElement selectedDeviceIdentification;

    /**
     * Will reference the ODOMElement of the currently selected device
     */
    private ODOMElement selectedDevice;

    /**
     * Will listen for changes to the selected device in order to update
     * the contents of the list value builder
     */
    private ODOMChangeListener odomChangeListener;

    /**
     * Inititializes a <code>PrimaryPatternsSection</code> with the given
     * arguments
     * @param parent the parent composite
     * @param style the style bitset
     * @param context the DeviceEditorContext for the editor that is using
     * this PrimaryPatterns section.
     */
    public PrimaryPatternsSection(Composite parent,
                                  int style,
                                  DeviceEditorContext context) {
        super(parent, style);
        this.context = context;
        ODOMElement identificationRootElement =
                (ODOMElement) context.getDeviceRepositoryAccessorManager().
                getDeviceIdentificationDocument().getRootElement();

        try {
            context.addRootElement(identificationRootElement,
                    identificationRootElement.getName());
            MarkerGeneratingErrorReporter errorReporter =
                    context.getErrorReporter(identificationRootElement);

            LocationDetailsRegistry registry =
                    errorReporter.getLocationDetailsRegistry();
            if (registry == null) {
                registry = new LocationDetailsRegistry();
                errorReporter.setLocationDetailsRegistry(registry);
            }

            DefaultJDOMFactory factory = new DefaultJDOMFactory();

            Element element = factory.element(DeviceRepositorySchemaConstants.
                    USER_AGENT_PATTERN_ELEMENT_NAME,
                    MCSNamespace.DEVICE_IDENTIFICATION);

            LocationDetails details =
                    new DeviceLocationDetails(ERROR_LOCATION_FORMAT,
                            MCSNamespace.DEVICE_IDENTIFICATION);

            registry.registerLocationDetails(element, details);
        } catch (SAXException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } catch (ParserErrorException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        createDisplayArea(TITLE, MESSAGE);
        createListeners();
    }

    /**
     * Refreshes the control that displays the primary patterns so that
     * is up to date with the identification document
     */
    private void updateListBuilder() {
        // remove the listener that is registered with the listBuilder
        listBuilder.removeModifyListener(listBuilderListener);
        try {
            // get the currently selected device
            String device = getSelectedDeviceName();
            // retrieve the user agent patterns (primary patterns) for that
            // device.
            String[] patterns = (device == null)
                    ? EMPTY_STRING_ARRAY
                    : context.getDeviceRepositoryAccessorManager().
                    getUserAgentPatterns(device);
            // pass the patterns to the control.
            listBuilder.setItems(patterns);
        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } finally {
            // re-register the modify listener with the listBuilder
            listBuilder.addModifyListener(listBuilderListener);
        }
    }

    /**
     * Updates the identification document so that it is up to date with the
     * values in the ListValueBuilder control.
     */
    private void updateDeviceIdentification() {
        // get the array of patterns from the list builder
        Object[] items = listBuilder.getItems();
        String[] patterns = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            patterns[i] = (String) items[i];
        }

        UndoRedoManager undoRedoManager = context.getUndoRedoManager();
        undoRedoManager.demarcateUOW();

        selectedDeviceIdentification.removeChangeListener(odomChangeListener);
        try {
            context.getDeviceRepositoryAccessorManager().setUserAgentPatterns(
                    getSelectedDeviceName(), patterns);
        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } finally {
            selectedDeviceIdentification.addChangeListener(odomChangeListener);
            undoRedoManager.demarcateUOW();
        }
    }

    /**
     * Returns the name of the currently selected device or null if no device
     * is selected
     * @return the name of the selected device or null if no device is selected.
     */
    private String getSelectedDeviceName() {
        return (selectedDevice == null) ? null :
                selectedDevice.getAttributeValue(
                        DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE);
    }

    /**
     * Creates the display area for this composite
     */
    private void createDisplayArea(String title, String message) {
        Section section =
                SectionFactory.createSection(this, title, message);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        displayArea = new Composite(section, SWT.NONE);
        section.setClient(displayArea);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        displayArea.setLayout(layout);
        displayArea.setLayoutData(new GridData(GridData.FILL_BOTH));
        displayArea.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        // add the list value builder to the display area
        listBuilder = new ListValueBuilder(displayArea, false, null);
        GridData listData = new GridData(GridData.FILL_BOTH);
        listBuilder.setLayoutData(listData);
        listBuilder.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // list value builder is initially disabled.
        listBuilder.setEnabled(false);
    }

    /**
     * Initializes the various listeners that are required by this section.
     */
    private void createListeners() {

        // create the modify listener that will respond changes to the list
        // builder
        listBuilderListener = new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                updateDeviceIdentification();
            }
        };
        // register the listener with the list builder
        listBuilder.addModifyListener(listBuilderListener);

        odomChangeListener = new ODOMChangeListener() {
            // javadoc inherited
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                // update the lisBuilder to reflect the change
                updateListBuilder();
            }
        };
        // retrieve the ODOMSelectionManager. This is managing device
        // selections in the device hierarchy
        ODOMSelectionManager selectionManager =
                context.getODOMSelectionManager();

        // Create a filter so that only device elements are included in
        // the selection.
        ODOMSelectionFilter deviceFilter = new ODOMSelectionFilter(null,
                new String[]{DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME},
                new ODOMSelectionFilterConfiguration(true, true));

        // register an ODOMSelection listener with the ODOMSelectionManager
        // so that we can update the list builder whenever a device is selected
        selectionManager.addSelectionListener(
                new ODOMElementSelectionListener() {
                    // javadoc inherited
                    public void selectionChanged(ODOMElementSelectionEvent event) {
                        if (selectedDevice != null) {
                            selectedDevice.removeChangeListener(odomChangeListener);
                        }
                        if (selectedDeviceIdentification != null) {
                            selectedDeviceIdentification.removeChangeListener(
                                    odomChangeListener);
                        }
                        ODOMElementSelection selection = event.getSelection();
                        if (selection.isEmpty()) {
                            // ensure the selectedDevice and
                            // selectedDeviceIndentification fields are set to
                            // null to reflect the fact that no device is selected
                            selectedDevice = null;
                            selectedDeviceIdentification = null;
                            listBuilder.setEnabled(false);
                        } else {
                            // update the selectedDevice and
                            // selectedDeviceIndentification fields
                            selectedDevice =
                                    (ODOMElement) selection.getFirstElement();
                            selectedDevice.addChangeListener(odomChangeListener);
                            selectedDeviceIdentification =
                                    (ODOMElement) context.
                                    getDeviceRepositoryAccessorManager().
                                    retrieveDeviceIdentification(
                                            getSelectedDeviceName());
                            selectedDeviceIdentification.addChangeListener(
                                    odomChangeListener);
                            listBuilder.setEnabled(true);

                        }
                        // update the list builder so that it reflects the
                        // currently selected device.
                        updateListBuilder();
                    }
                }, deviceFilter);
    }

    // javadoc inherited
    public boolean setFocus(XPath path) {
        // todo this needs implementing
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Jan-05	6545/4	matthew	VBM:2004121703 remove up/down arrows from primary pattern section

 21-Dec-04	6545/1	matthew	VBM:2004121703 remove up/down buttons from primary pattern compoenent

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 28-Sep-04	5665/1	philws	VBM:2004090301 Fix initial active status of primary and secondary patterns Add buttons

 10-Sep-04	5488/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 02-Sep-04	5369/3	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 02-Sep-04	5369/1	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 16-Aug-04	5206/2	allan	VBM:2004081201 Auto-migration of mdpr with dialog.

 13-Aug-04	5202/1	allan	VBM:2004072803 Fix enablement of ListValueBuilder.

 14-May-04	4367/1	doug	VBM:2004051108 Ensured that the MasterValueSection refreshes whenever the policies type changes

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 10-May-04	4068/4	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 07-May-04	4172/4	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 05-May-04	4115/3	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 05-May-04	4115/1	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 04-May-04	4007/1	doug	VBM:2004032304 Added a PrimaryPatterns form section

 ===========================================================================
*/
