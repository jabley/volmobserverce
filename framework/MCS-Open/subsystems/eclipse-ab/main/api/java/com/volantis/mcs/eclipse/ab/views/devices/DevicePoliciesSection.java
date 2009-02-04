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

import com.volantis.mcs.accessors.xml.jdom.XMLAccessorConstants;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.core.ResolvedDevicePolicy;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.UndeclaredThrowableException;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Section;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A FormSection for displaying device policies for a device.
 */
public class DevicePoliciesSection extends FormSection {

    /**
     * The prefix for resources associated with this class.
     */
    private static String RESOURCE_PREFIX = "DevicePoliciesSection.";

    /**
     * Create the ContentProvider for the polices.
     */
    private PoliciesContentProvider contentProvider =
            new PoliciesContentProvider();

    /**
     * Name for the name columnName - must match name in properties file.
     */
    private static final String NAME_COLUMN_NAME = "name";//$NON-NLS-1$

    /**
     * Name for the value columnName - must match name in properties file.
     */
    private static final String VALUE_COLUMN_NAME = "value"; //$NON-NLS-1$

    /**
     * Name for the category columnName - must match name in properties file.
     */
    private static final String CATEGORY_COLUMN_NAME = "category"; //$NON-NLS-1$

    /**
     * Name for the origin columnName - must match name in properties file.
     */
    private static final String ORIGIN_COLUMN_NAME = "origin"; //$NON-NLS-1$

    /**
     * Name for the short columnName - must match name in properties file.
     */
    private static final String SHORT_COLUMN_NAME = "short"; //$NON-NLS-1$

    /**
     * Name for the help columnName - must match name in properties file.
     */
    private static final String DESCRIPTION_COLUMN_NAME =
            "description"; //$NON-NLS-1$

    /**
     * Constant for indicating the preferred height of the policies table
     */
    private static final int TABLE_HEIGHT_HINT = DevicesMessages.getInteger(
                RESOURCE_PREFIX + "tableHeightHint").intValue();

    /**
     * Array specifiying the columnName properties (i.e. columnName names) in the
     * order they are displayed from left to right.
     */
    private String columnProperties [];

    /**
     * An array of all the policies that are displayed in this section - i.e.
     * their short names.
     */
    private String[] policiesToDisplay;

    /**
     * The DeviceRepositoryAccessorManager associated with this
     * DevicePoliciesSection.
     */
    private DeviceRepositoryAccessorManager accessorManager;

    /**
     * The display area control for this form section.
     */
    private Composite displayArea;

    /**
     * The viewer for the table of device policies.
     */
    private TableViewer tableViewer;


    /**
     * Construct a new DevicePolicesSection that derives its
     * content using a specified DeviceRepositoryAccessorManager.
     * @param parent The parent Composite.
     * @param style The swt style - unused.
     * @param accessorManager The DeviceRepositoryAccessorManager from
     * which to obtain the device identification information.
     * @throws IllegalArgumentException If accessorManager is null.
     */
    public DevicePoliciesSection(Composite parent, int style,
                                 DeviceRepositoryAccessorManager accessorManager) {
        super(parent, style);

        if (accessorManager == null) {
            throw new
                    IllegalArgumentException("Cannot be null: accessorManager");
        }

        this.accessorManager = accessorManager;
        try {
            initializePoliciesToDisplay();
        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            throw new UndeclaredThrowableException(e);
        }
        setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        createDisplayArea(DevicesMessages.getString(RESOURCE_PREFIX + "title"),
                DevicesMessages.getString(RESOURCE_PREFIX + "message"));
    }

    /**
     * Initialize the list of polices that will be displayed in this section.
     * This list is determined by examination of the root device in the
     * device hierarchy (i.e. Master) since it must have a value for every
     * single policy that exists hence it can provide the list of all policies.
     * @throws RepositoryException if there is a problem reading the policies
     * from the device repository.
     */
    private void initializePoliciesToDisplay() throws RepositoryException {
        List policiesToDisplayList = new ArrayList();
        Iterator policyNames = accessorManager.policyNamesIterator();
        while (policyNames.hasNext()) {
            policiesToDisplayList.add(policyNames.next());
        }

        // Store the policies to display as an array to avoid casts of every
        // policy every time a new device is selected.
        policiesToDisplay = new String[policiesToDisplayList.size()];
        policiesToDisplay = (String[])
                policiesToDisplayList.toArray(policiesToDisplay);
    }

    /**
     * Set the device whose polices details should be shown in this
     * section.
     *
     * @param deviceElement The element that contains the name of the device
     * whose polices should be shown in this section. This element must contain
     * a DEVICE_NAME_ATTRIBUTE attribute.
     */
    public void setDevice(Element deviceElement) {
        if (deviceElement == null) {
            throw new IllegalArgumentException("deviceElement must not be null");
        }
        tableViewer.setInput(deviceElement);
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
     * Create the display area for this section.
     */
    private void createDisplayArea(String title, String message) {
        Section section =
                SectionFactory.createSection(this, title, message);

        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        displayArea = new Composite(section, SWT.BORDER);
        section.setClient(displayArea);

        GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        displayArea.setLayout(gridLayout);
        displayArea.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        displayArea.setLayoutData(new GridData(GridData.FILL_BOTH));

        Table table = new Table(displayArea, SWT.SINGLE | SWT.H_SCROLL |
                SWT.V_SCROLL);

        table.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        addColumns(table);

        table.setHeaderVisible(true);

        TableLayout tableLayout = new TableLayout();
        table.setLinesVisible(true);
        table.setLayout(tableLayout);

        GridData tableData = new GridData(GridData.FILL_BOTH);
        tableData.heightHint = TABLE_HEIGHT_HINT;
        table.setLayoutData(tableData);

        tableViewer = new TableViewer(table);
        tableViewer.setContentProvider(contentProvider);
        final ITableLabelProvider labelProvider = createLabelProvider();
        tableViewer.setLabelProvider(labelProvider);
        tableViewer.setColumnProperties(columnProperties);

        // Initially sort by the name column.
        tableViewer.setSorter(new PoliciesSorter(NAME_COLUMN_NAME));

    }

    /**
     * Add the columns to the table. The columns along with their order in
     * the table are defined in the properties file.
     * @param table The Table to which the columns are to be added.
     */
    private void addColumns(Table table) {

        String columnFormat = DevicesMessages.getString(RESOURCE_PREFIX
                + "columns");
        StringTokenizer columns = new StringTokenizer(columnFormat, ",");
        TableColumn tableColumn;
        StringBuffer columnTitleKey;
        StringBuffer columnWidthKey;
        List columnList = new ArrayList();
        while (columns.hasMoreTokens()) {
            String column = columns.nextToken();
            column = column.trim();
            tableColumn = new TableColumn(table, SWT.LEFT);
            columnTitleKey = new StringBuffer(RESOURCE_PREFIX);
            columnTitleKey.append(column).append(".title");
            tableColumn.setText(DevicesMessages.getString(columnTitleKey.
                    toString()));
            columnWidthKey = new StringBuffer(RESOURCE_PREFIX);
            columnWidthKey.append(column).append(".width");
            tableColumn.setWidth(DevicesMessages.
                    getInteger(columnWidthKey.toString()).intValue());
            final String columnName = column;
            // This listen sets the sortter on the viewer to a new sorter
            // based on the column name. The act of provided a newly created
            // sorter forces the view to resort.
            tableColumn.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    tableViewer.setSorter(
                            new PoliciesSorter(columnName));
                }
            });
            columnList.add(column);
        }

        columnProperties = new String[columnList.size()];
        columnProperties = (String[]) columnList.toArray(columnProperties);
    }

    /**
     * Create the label provider for the table viewer of this section.
     * This label provider will only be compatible with the
     * contentProvider of this class.
     */
    private ITableLabelProvider createLabelProvider() {

        ITableLabelProvider labelProvider = new ITableLabelProvider() {
            // javadoc inherited
            public Image getColumnImage(Object o, int i) {
                return null;
            }

            // javadoc inherited
            public String getColumnText(Object o, int i) {
                ResolvedDevicePolicy resolvedPolicy = (ResolvedDevicePolicy) o;
                String columnText = null;
                String columnName = columnProperties[i];
                if (columnName.equals(NAME_COLUMN_NAME)) {
                    columnText = getPolicyName(resolvedPolicy.policy);
                } else if (columnName.equals(VALUE_COLUMN_NAME)) {
                    columnText = getPolicyValue(resolvedPolicy.policy);
                } else if (columnName.equals(CATEGORY_COLUMN_NAME)) {
                    columnText = getPolicyCategory(resolvedPolicy.policy);
                } else if (columnName.equals(ORIGIN_COLUMN_NAME)) {
                    columnText = resolvedPolicy.deviceName;
                } else if (columnName.equals(DESCRIPTION_COLUMN_NAME)) {
                    columnText = getPolicyDescription(resolvedPolicy.policy);
                } else if (columnName.equals(SHORT_COLUMN_NAME)) {
                    columnText = getPolicyShortName(resolvedPolicy.policy);
                } else {
                    // Unexpected columnName name.
                    throw new IllegalStateException("No such columnName: " +
                            columnName);
                }

                return columnText;
            }

            // javadoc inherited
            public void addListener(ILabelProviderListener
                    iLabelProviderListener) {
            }

            // javadoc inherited
            public void dispose() {
            }

            // javadoc inherited
            public boolean isLabelProperty(Object o, String s) {
                return false;
            }

            // javadoc inherited
            public void removeListener(ILabelProviderListener
                    iLabelProviderListener) {
            }

        };

        return labelProvider;
    }

    /**
     * Get the policy name for a given policy element i.e. the localized
     * name.
     * @param policy The policy.
     */
    private String getPolicyName(Element policy) {
        String policyName = policy.
                getAttributeValue(XMLAccessorConstants.DEVICE_NAME_ATTRIBUTE);
        return accessorManager.getLocalizedPolicyName(policyName);
    }

    /**
     * Get the policy description for a given policy element i.e. the localized
     * description.
     * @param policy The policy.
     */
    private String getPolicyDescription(Element policy) {
        String policyName = policy.
                getAttributeValue(XMLAccessorConstants.DEVICE_NAME_ATTRIBUTE);
        return accessorManager.getPolicyDescription(policyName);
    }

    /**
     * Get the category for a given policy element.
     * @param policy The policy
     */
    private String getPolicyCategory(Element policy) {
        String policyName = policy.
                getAttributeValue(XMLAccessorConstants.DEVICE_NAME_ATTRIBUTE);
        String localizedCategory = accessorManager.
                getLocalizedPolicyCategory(accessorManager.
                getPolicyCategory(policyName));
        return localizedCategory;
    }

    /**
     * Get the short name of a policy from a given policy element.
     * @param policy The policy
     */
    private String getPolicyShortName(Element policy) {
        // The short name is value of the name attribute.
        String shortName = policy.
                getAttributeValue(XMLAccessorConstants.DEVICE_NAME_ATTRIBUTE);
        return shortName;
    }

    /**
     * Get the value for a given policy element. When there are multiple
     * values these are combined into a single comma separated String.
     */
    private String getPolicyValue(Element policy) {

        String value = policy.
                getAttributeValue(DeviceRepositorySchemaConstants.
                POLICY_VALUE_ATTRIBUTE);

        if (value == null) {
            // There could be multiple values in a child elements or field child
            // elements. These are presented as a comma separated list.
            Iterator children = policy.getChildren().iterator();
            StringBuffer buffer = null;
            boolean hasChildren = children.hasNext();
            while (children.hasNext()) {
                Element childElement = (Element) children.next();
                if (childElement.getName().
                        equals(DeviceRepositorySchemaConstants.
                        POLICY_VALUE_ELEMENT_NAME) ||
                        childElement.getName().
                        equals(DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_FIELD_ELEMENT_NAME)) {

                    if (buffer == null) {
                        buffer = new StringBuffer();
                    }

                    if (childElement.getName().
                            equals(DeviceRepositorySchemaConstants.
                            POLICY_VALUE_ELEMENT_NAME)) {
                        buffer.append(childElement.getText());
                    } else {
                        // Must be a field element
                        buffer.append(childElement.
                                getAttributeValue(DeviceRepositorySchemaConstants.
                                POLICY_DEFINITION_FIELD_NAME_ATTRIBUTE));
                        buffer.append("=\"");
                        buffer.append(childElement.
                                getAttributeValue(DeviceRepositorySchemaConstants.
                                POLICY_DEFINITION_FIELD_VALUE_ATTRIBUTE));
                        buffer.append("\"");
                    }
                    if (children.hasNext()) {
                        buffer.append(", ");
                    }
                }
            }
            if (buffer != null) {
                value = buffer.toString();
            }
            if (value == null) { // no values were found
                if (hasChildren) {
                    throw new
                            IllegalStateException("policy value is missing a value for policy " +
                            policy.getAttributeValue(
                                    XMLAccessorConstants.DEVICE_NAME_ATTRIBUTE));
                } else {
                    value = "";
                }
            }
        }

        return value;
    }

    /**
     * An IStructuredContentProvider that provides the resolved policy values
     * for all the polices of a given device.
     */
    private class PoliciesContentProvider
            implements IStructuredContentProvider {

        /**
         * Given an element that is expected to be a device. Retrieve all
         * the policies of that device as ResolvedDevicePolicy objects
         * and return them.
         */
        // rest of javadoc inherited
        public Object[] getElements(Object parentElement) {
            if (parentElement == null) {
                throw new IllegalArgumentException(
                        "Cannot be null: parentElement"); //$NON-NLS-1$
            }
            if (!(parentElement instanceof Element)) {
                throw new IllegalArgumentException(
                        "Expected a JDOM Element " + //$NON-NLS-1$
                        "for parentElement but was: " + //$NON-NLS-1$
                        parentElement.getClass().getName());
            }

            Element device = (Element) parentElement;
            String deviceName = device.getAttributeValue(XMLAccessorConstants.
                    DEVICE_NAME_ATTRIBUTE);

            if (deviceName == null) {
                throw new IllegalArgumentException("There is no \"" +
                        XMLAccessorConstants.DEVICE_NAME_ATTRIBUTE +
                        "\" attribute associated with element " +
                        parentElement);
            }

            // create a list of the resolved policies. We use a list because
            // the we do not know the number of resolved policies up front.
            // In theory all policies should be resolved, however if there
            // is a problem with a migration some policies may not be resolved.
            List resolvedPolicies = new ArrayList();
            ResolvedDevicePolicy resolvedPolicy;
            for (int i = 0; i < policiesToDisplay.length; i++) {
                resolvedPolicy = accessorManager.resolvePolicy(deviceName,
                        policiesToDisplay[i]);
                if (resolvedPolicy != null) {
                    resolvedPolicies.add(resolvedPolicy);
                }
            }
            // create the array that will be populated with the resolved
            // policies
            ResolvedDevicePolicy policies [] =
                    new ResolvedDevicePolicy[resolvedPolicies.size()];
            // populate and return the array
            return resolvedPolicies.toArray(policies);
        }

        // javadoc inherited
        public void dispose() {
        }

        // javadoc inherited
        public void inputChanged(Viewer viewer, Object o, Object o1) {
        }
    }

    /**
     * A ViewerSorter that sorts ReolvedDevicePolicy objects based on
     * a specified columnName.
     */
    private class PoliciesSorter extends ViewerSorter {

        /**
         * The name of the columnName against which this sorter operates
         */
        private final String columnName;

        /**
         * Construct a new PoliciesSorter specifying the columnName to sort
         * against.
         * @param columnName The name of the columnName to sort against taken
         * from the columnName properties of the table.
         */
        public PoliciesSorter(String columnName) {
            this.columnName = columnName;
        }

        /**
         * Override compare to sort against the specified columnName.
         */
        // rest of javadoc inherited
        public int compare(Viewer viewer, Object o1, Object o2) {
            ResolvedDevicePolicy resolved1 = (ResolvedDevicePolicy) o1;
            ResolvedDevicePolicy resolved2 = (ResolvedDevicePolicy) o2;

            int result = 0;
            if (columnName.equals(NAME_COLUMN_NAME)) {
                result = getPolicyName(resolved1.policy).
                        compareTo(getPolicyName(resolved2.policy));
            } else if (columnName.equals(VALUE_COLUMN_NAME)) {
                result = getPolicyValue(resolved1.policy).
                        compareTo(getPolicyValue(resolved2.policy));
            } else if (columnName.equals(CATEGORY_COLUMN_NAME)) {
                result = getPolicyCategory(resolved1.policy).
                        compareTo(getPolicyCategory(resolved2.policy));
            } else if (columnName.equals(ORIGIN_COLUMN_NAME)) {
                result = resolved1.deviceName.
                        compareTo(resolved2.deviceName);
            } else if (columnName.equals(DESCRIPTION_COLUMN_NAME)) {
                result = getPolicyDescription(resolved1.policy).
                        compareTo(getPolicyDescription(resolved2.policy));
            } else if (columnName.equals(SHORT_COLUMN_NAME)) {
                result = getPolicyShortName(resolved1.policy).
                        compareTo(getPolicyShortName(resolved2.policy));
            } else {
                // Unexpected columnName name.
                throw new IllegalStateException("No such columnName: " +
                        columnName);
            }

            return result;
        }
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

 02-Mar-05	7163/5	doug	VBM:2005022101 Fixed Scroll bar issues with DeviceRepositoryBrowser page

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 13-May-04	4335/1	matthew	VBM:2004051206 Fix Device Repository Viewer so that it actually shows device policies

 07-May-04	4195/3	tom	VBM:2004041902 added block comment regarding {} and changed line length

 07-May-04	4195/1	tom	VBM:2004041902 Made .java files use the relevant resource bundles

 04-May-04	4007/3	doug	VBM:2004032304 Added a PrimaryPatterns form section

 29-Apr-04	4072/3	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 29-Apr-04	4072/1	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 22-Apr-04	3975/3	allan	VBM:2004042005 Fix multi-value policy migration and related issues.

 21-Apr-04	3975/1	allan	VBM:2004042005 Fix multi-valued migration.

 21-Apr-04	3935/4	allan	VBM:2004020906 Fix merge issues.

 20-Apr-04	3935/2	allan	VBM:2004020906 Migration, Device Browser & Import support for policy fields.

 19-Apr-04	3904/3	allan	VBM:2004020903 Support localized device policy categories

 19-Apr-04	3904/1	allan	VBM:2004020903 Support localized device policy categories

 30-Mar-04	3574/3	allan	VBM:2004032401 Rework issues.

 29-Mar-04	3574/1	allan	VBM:2004032401 Completed device repository merging. Needs more testing.

 23-Mar-04	3389/2	byron	VBM:2004030905 NLV properties files need adding to build

 10-Mar-04	3383/1	pcameron	VBM:2004030412 Added PolicyValueSelectionDialog

 11-Feb-04	2862/6	allan	VBM:2004020411 Rework issues.

 11-Feb-04	2862/4	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/
