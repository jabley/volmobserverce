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

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.devices.validation.DeviceLocationDetails;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetails;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetailsRegistry;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.MarkerGeneratingErrorReporter;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelection;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilterConfiguration;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.controls.ListValueBuilder;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.mcs.eclipse.core.DeviceHeaderPattern;
import com.volantis.mcs.repository.RepositoryException;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.Section;
import org.jdom.Element;
import org.jdom.input.DefaultJDOMFactory;
import org.xml.sax.SAXException;

import java.text.MessageFormat;

/**
 * Form section that can be use to view and edit a device's secondary patterns.
 */
public class SecondaryPatternsSection extends FormSection
        implements XPathFocusable {

    /**
     * The prefix for resource messages associated with this class.
     */
    private static final String RESOURCE_PREFIX = "SecondaryPatternsSection.";

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
     * The device name column title.
     */
    private static final String DEVICE_NAME_HEADER =
            DevicesMessages.getString(RESOURCE_PREFIX + "pattern.deviceName");

    /**
     * The header name column title.
     */
    private static final String HEADER_NAME_HEADER =
            DevicesMessages.getString(RESOURCE_PREFIX + "pattern.headerName");

    /**
     * The value column title.
     */
    private static final String VALUE_HEADER =
            DevicesMessages.getString(RESOURCE_PREFIX + "pattern.value");

    /**
     * An empty string array
     */
    private static final DeviceHeaderPattern[] EMPTY_PATTERN_ARRAY =
            new DeviceHeaderPattern[0];

    /**
     * The error location message format.
     */
    private static final MessageFormat ERROR_LOCATION_FORMAT =
            new MessageFormat(DevicesMessages.getString(RESOURCE_PREFIX +
            "locationDetails"));

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
    private ListValueBuilder listValueBuilder;

    /**
     * A Modify listener that will be registered with the listBuilder in order
     * to update the devices indentification xml
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
     * Inititializes a <code>SecondaryPatternsSection</code> with the given
     * arguments.
     * @param parent the parent composite. Cannot be null.
     * @param style the style bitset
     * @param context the device editor context associated with this editor.
     * Cannot be null.
     * @throws IllegalArgumentException if context is null.
     */
    public SecondaryPatternsSection(Composite parent,
                                    int style,
                                    DeviceEditorContext context) {
        super(parent, style);
        this.context = context;

        createDisplayArea(TITLE, MESSAGE);
        createListeners();

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
                    HEADER_PATTERN_ELEMENT_NAME,
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

        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        displayArea.setLayout(gridLayout);
        displayArea.setLayoutData(new GridData(GridData.FILL_BOTH));
        displayArea.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // add a custom list value builder to the display area
        listValueBuilder = createListValueBuilder();

        GridData listData = new GridData(GridData.FILL_BOTH);
        listValueBuilder.setLayoutData(listData);
        listValueBuilder.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // list value builder is initially disabled.
        listValueBuilder.setEnabled(false);
    }

    /**
     * Creates and returns a custom ListValueBuilder control for this section.
     * @return the ListValueBuilder control
     */
    private ListValueBuilder createListValueBuilder() {
        return new ListValueBuilder(displayArea, true, null) {

            // javadoc inherited
            protected TableViewer createTableViewer(Composite container) {
                // Create a single-selection table with scroll bars and
                // three columns.
                Table table = new Table(container,
                        SWT.SINGLE | SWT.FULL_SELECTION |
                        SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

                final TableColumn headerCol = new TableColumn(table, SWT.LEFT);
                headerCol.setText(HEADER_NAME_HEADER);

                final TableColumn valueCol = new TableColumn(table, SWT.LEFT);
                valueCol.setText(VALUE_HEADER);

                final TableColumn deviceCol = new TableColumn(table, SWT.LEFT);
                deviceCol.setText(DEVICE_NAME_HEADER);

                table.setHeaderVisible(true);

                // Sub-class the TableLayout in order to pad the 2nd column so
                // that the combined column widths fill the table. This is a
                // workaround for Windows which doesn't do this by default.
                final TableLayout tableLayout = new TableLayout() {
                    public void layout(Composite comp, boolean flush) {
                        super.layout(comp, flush);

                        // Ensure we are laying out a table.
                        if (comp instanceof Table) {
                            final int tableWidth = comp.getClientArea().width;

                            // Give the header and device column's a quarter
                            // of the width each, and the value column half of
                            // the width.
                            final int oneColWidth = tableWidth / 4;
                            int valueWidth = 2 * oneColWidth;
                            headerCol.setWidth(oneColWidth);
                            deviceCol.setWidth(oneColWidth);

                            // Check if integer division has resulted in left
                            // over pixels. If so, the Value column takes up
                            // the slack.
                            final int totalWidth = 4 * oneColWidth;

                            if (totalWidth < tableWidth) {
                                // Adjust the Value column's width.
                                valueWidth += (tableWidth - totalWidth);
                            }

                            valueCol.setWidth(valueWidth);
                        }
                    }
                };
                tableLayout.addColumnData(new ColumnWeightData(1, 100, true));
                tableLayout.addColumnData(new ColumnWeightData(2, 200, true));
                tableLayout.addColumnData(new ColumnWeightData(1, 100, true));
                table.setLayout(tableLayout);

                // Create the table's viewer.
                final TableViewer tableViewer = new TableViewer(table);

                tableViewer.setColumnProperties(new String[]{
                    HEADER_NAME_HEADER, VALUE_HEADER, DEVICE_NAME_HEADER});

                return tableViewer;
            }

            // javadoc inherited
            protected ITableLabelProvider createLabelProvider() {
                return new ITableLabelProvider() {
                    // javadoc inherited
                    public Image getColumnImage(Object element,
                                                int columnIndex) {
                        // No column images
                        return null;
                    }

                    // javadoc inherited
                    public String getColumnText(Object element,
                                                int columnIndex) {
                        DeviceHeaderPattern pattern =
                                (DeviceHeaderPattern) element;

                        final String columnName =
                                tableViewer.getTable().
                                getColumn(columnIndex).getText();
                        String value = null;
                        if (columnName == HEADER_NAME_HEADER) {
                            value = pattern.getName();
                        } else if (columnName == VALUE_HEADER) {
                            value = pattern.getRegularExpression();
                        } else if (columnName == DEVICE_NAME_HEADER) {
                            value = pattern.getBaseDevice();
                            if (value == null) {
                                value = "";
                            }
                        }

                        return value;
                    }

                    // javadoc inherited
                    public void addListener(ILabelProviderListener listener) {
                    }

                    // javadoc inherited
                    public void dispose() {
                    }

                    // javadoc inherited
                    public boolean isLabelProperty(Object element,
                                                   String property) {
                        return false;
                    }

                    // javadoc inherited
                    public void removeListener(ILabelProviderListener listener) {
                    }
                };
            }

            // javadoc inherited
            protected Object createNewItem() {
                return new DeviceHeaderPattern("", "", "");
            }

            // javadoc inherited
            protected ICellModifier createCellModifier() {
                return new ICellModifier() {
                    // javadoc inherited
                    public boolean canModify(Object element, String property) {
                        return true;
                    }

                    // javadoc inherited
                    public Object getValue(Object element, String property) {
                        DeviceHeaderPattern pattern =
                                (DeviceHeaderPattern) element;
                        Object value = null;
                        if (property == HEADER_NAME_HEADER) {
                            value = pattern.getName();
                        } else if (property == VALUE_HEADER) {
                            value = pattern.getRegularExpression();
                        } else if (property == DEVICE_NAME_HEADER) {
                            value = pattern.getBaseDevice();
                            if (value == null) {
                                value = "";
                            }

                        }
                        return value;
                    }

                    // javadoc inherited
                    public void modify(Object element, String property,
                                       Object value) {
                        TableItem item = (TableItem) element;
                        DeviceHeaderPattern pattern =
                                (DeviceHeaderPattern) item.getData();
                        String newValue = (String) value;

                        boolean changed = false;
                        if (property == HEADER_NAME_HEADER) {
                            // The name cannot be null.
                            changed = !pattern.getName().equals(newValue);
                            pattern.setName(newValue);
                        } else if (property == VALUE_HEADER) {
                            // The regular expression cannot be null.
                            changed = !pattern.getRegularExpression().
                                    equals(newValue);
                            pattern.setRegularExpression(newValue);
                        } else if (property == DEVICE_NAME_HEADER) {
                            // The base device CAN be null.
                            String oldBase = pattern.getBaseDevice();
                            if (oldBase == null && newValue == null) {
                                changed = false;
                            } else {
                                if (oldBase != null) {
                                    changed = !oldBase.equals(newValue);
                                } else {
                                    changed = !newValue.equals(oldBase);
                                }
                            }

                            pattern.setBaseDevice(
                                    (newValue.equals("")) ? null : newValue);
                        }
                        // Inform the model of a change only if the object was
                        // modified.
                        if (changed) {
                            int index = tableViewer.getTable().indexOf(item);
                            getModel().itemChanged(index, pattern);
                        }
                    }
                };
            }
        };
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
        listValueBuilder.addModifyListener(listBuilderListener);

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
                new String[]{DeviceRepositorySchemaConstants.
                DEVICE_ELEMENT_NAME},
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
                            listValueBuilder.setEnabled(false);
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
                            listValueBuilder.setEnabled(true);

                        }
                        // update the list builder so that it reflects the
                        // currently selected device.
                        updateListBuilder();
                    }
                },
                deviceFilter);
    }

    /**
     * Refreshes the control that displays the secondary patterns so that it
     * is up to date with the identification document
     */
    private void updateListBuilder() {
        // remove the listener that is registered with the listBuilder
        listValueBuilder.removeModifyListener(listBuilderListener);
        try {
            // get the currently selected device
            String device = getSelectedDeviceName();
            // retrieve the header patterns (secondary patterns) for the
            // selected device.
            DeviceHeaderPattern[] patterns = (device == null)
                    ? EMPTY_PATTERN_ARRAY
                    : context.getDeviceRepositoryAccessorManager().
                    getHeaderPatterns(device);

            // pass the patterns to the control.
            listValueBuilder.setItems(patterns);
        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } finally {
            // re-register the modify listener with the listBuilder
            listValueBuilder.addModifyListener(listBuilderListener);
        }
    }

    /**
     * Updates the identification document so that it is up to date with the
     * values in the ListValueBuilder control.
     */
    private void updateDeviceIdentification() {
        // get the array of patterns from the list builder
        Object[] items = listValueBuilder.getItems();
        DeviceHeaderPattern[] patterns = new DeviceHeaderPattern[items.length];
        for (int i = 0; i < items.length; i++) {
            patterns[i] = (DeviceHeaderPattern) items[i];
        }

        UndoRedoManager undoRedoManager = context.getUndoRedoManager();
        undoRedoManager.demarcateUOW();

        selectedDeviceIdentification.removeChangeListener(odomChangeListener);
        try {
            context.getDeviceRepositoryAccessorManager().
                    setHeaderPatterns(getSelectedDeviceName(), patterns);

        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } finally {
            selectedDeviceIdentification.addChangeListener(odomChangeListener);
            undoRedoManager.demarcateUOW();
        }
    }

    /**
     * Returns the name of the currently selected device.
     * @return the device name or null if no device is selected.
     */
    private String getSelectedDeviceName() {
        return (selectedDevice == null) ? null :
                selectedDevice.getAttributeValue(
                        DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE);
    }

    // javadoc inherited
    public boolean setFocus(XPath path) {
        // @todo implement this
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 28-Sep-04	5665/1	philws	VBM:2004090301 Fix initial active status of primary and secondary patterns Add buttons

 10-Sep-04	5488/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 02-Sep-04	5369/3	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 16-Aug-04	5206/2	allan	VBM:2004081201 Auto-migration of mdpr with dialog.

 13-Aug-04	5202/1	allan	VBM:2004072803 Fix enablement of ListValueBuilder.

 14-May-04	4367/1	doug	VBM:2004051108 Ensured that the MasterValueSection refreshes whenever the policies type changes

 13-May-04	4360/12	pcameron	VBM:2004051303 Fixed column headers for secondary patterns in SecondaryPatternsSection

 13-May-04	4363/1	matthew	VBM:2004051302 stop nulls from being passed to SecondaryPatternSections.tableViwer

 10-May-04	4068/1	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 07-May-04	4172/9	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 07-May-04	4172/7	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 ===========================================================================
*/
