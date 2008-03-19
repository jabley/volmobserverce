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
import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.ab.editors.devices.validation.ContentPresentElementValidator;
import com.volantis.mcs.eclipse.ab.editors.devices.validation.DevicePolicyLocationDetails;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorPart;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetails;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetailsRegistry;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelection;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilterConfiguration;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidatorDetails;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.controls.ControlUtils;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.IWorkbenchPart;
import org.jdom.Element;
import org.jdom.input.DefaultJDOMFactory;
import org.xml.sax.SAXException;
import org.jdom.Document;

import java.text.MessageFormat;

/**
 * The Editor part for displaying and editing device policies.
 */
public class DevicePoliciesPart extends ODOMEditorPart {

    /**
     * The prefix for resources properties associated with this class.
     */
    private static final String RESOURCE_PREFIX = "DevicePoliciesPart.";

    /**
     * The message for when no device is available to edit.
     */
    private static final String NO_DEVICE_MESSAGE =
            DevicesMessages.getString(RESOURCE_PREFIX + "noDevice");

    /**
     * The messsage (unformatted) for the heading.
     */
    private static final String HEADING_MESSAGE =
            DevicesMessages.getString(RESOURCE_PREFIX + "headingLabel.text");

    /**
     * Constant for device policies forms horizontal spacing
     */
    private static final int HORIZONTAL_SPACING =
            EditorMessages.getInteger("Editor.horizontalSpacing").intValue();

    /**
     * An ODOMElementSelectionListener listening for device selection.
     */
    private final ODOMElementSelectionListener deviceSelectionListener;

    /**
     * The Label that is the heading for this page.
     */
    private Label heading;

    /**
     * The Composite that encompasses the whole of the page.
     */
    private Composite displayArea;

    /**
     * The layout for the displayArea that encompasses the whole of the page.
     */
    private StackLayout displayAreaLayout;

    /**
     * The message page for when no device is selected.
     */
    private Composite messagePage;

    /**
     * The policies page for editing the policies of a selected device.
     */
    private Composite policiesPage;

    /**
     * Will reference the hierarchy element for the currently selected device.
     * Could be null if no device is selected.
     */
    private ODOMElement selectedDevice;

    /**
     * The CategoriesSection including in this part.
     */
    private CategoriesSection categoriesSection;

    /**
     * The PoliciesSection included in this part.
     */
    private PoliciesSection policiesSection;

    /**
     * The selected element.
     */
    private ODOMElement selectedElement;

    /**
     * Initialises a <code>DeviceOverviewPart</code> instance.
     * @param context the {@link DeviceEditorContext} for this overview
     */
    public DevicePoliciesPart(DeviceEditorContext context) {
        // the root element is the hierarchy element
        super(DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME,
                context);

        // Create a filter so that only device elements are included in
        // the selection.
        ODOMSelectionFilter deviceFilter = new ODOMSelectionFilter(null,
                new String[]{
                    DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME},
                new ODOMSelectionFilterConfiguration(true, true));

        deviceSelectionListener =
                new ODOMElementSelectionListener() {
                    public void selectionChanged(
                            ODOMElementSelectionEvent event) {
                        // retrieve the current selection
                        ODOMElementSelection selection = event.getSelection();
                        // store away the currently selected device
                        if (!selection.isEmpty()) {
                            selectedDevice =
                                    (ODOMElement) selection.getFirstElement();
                        } else {
                            selectedDevice = null;
                        }

                        updatePage();
                    }
                };
        context.getODOMSelectionManager().
                addSelectionListener(deviceSelectionListener, deviceFilter);


        context.addSupplementaryValidator(new DOMSupplementaryValidatorDetails(
                MCSNamespace.DEVICE.getURI(),
                DeviceRepositorySchemaConstants.POLICY_VALUE_ELEMENT_NAME,
                new ContentPresentElementValidator(DeviceRepositorySchemaConstants.
                POLICY_VALUE_ELEMENT_NAME, MCSNamespace.DEVICE)));
    }


    // javadoc inherited
    public void setFocus() {
        // this method is called whenever this editor part becomes the
        // active page. Ensure that the page is fully up to date.
        updatePage();
    }

    // javadoc inherited
    protected void createPartControlImpl(Composite parent) {
        displayArea = new Composite(parent, SWT.NONE);
        displayAreaLayout = new StackLayout();
        displayArea.setLayout(displayAreaLayout);
        displayArea.setBackground(parent.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        messagePage = ControlUtils.createMessageComposite(displayArea,
                SWT.CENTER,
                new String[]{NO_DEVICE_MESSAGE});
        createPoliciesPage(displayArea);

        displayAreaLayout.topControl = messagePage;

        displayArea.layout();
    }

    // todo implement this method
    protected XPathFocusable[] getXPathFocusableControls() {
        return new XPathFocusable[0];
    }

    /**
     * Create the Composite that will contain all the controls that allow
     * the user to edit the policies for a selected device.
     * @param parent the parent Composite
     */
    private void createPoliciesPage(Composite parent) {
        policiesPage = new Composite(parent, SWT.NONE);
        policiesPage.setBackground(parent.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        policiesPage.setLayout(new GridLayout());
        GridData data = new GridData(GridData.FILL_BOTH);
        policiesPage.setLayoutData(data);

        heading = new Label(policiesPage, SWT.SINGLE);
        heading.setBackground(parent.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        Font font = heading.getFont();

        FontData fontData [] = font.getFontData();
        int designLabelHeight =
                DevicesMessages.getInteger(RESOURCE_PREFIX +
                "headingLabel.height").intValue();

        fontData[0].setHeight(designLabelHeight);
        fontData[0].setStyle(SWT.BOLD);
        final Font newFont = new Font(heading.getDisplay(), fontData[0]);
        heading.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                newFont.dispose();
            }
        });
        heading.setFont(newFont);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
        heading.setLayoutData(data);

        Composite sections = new Composite(policiesPage, SWT.NONE);
        sections.setBackground(parent.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        GridLayout layout = new GridLayout(2, false);
        layout.horizontalSpacing = HORIZONTAL_SPACING;
        sections.setLayout(layout);
        data = new GridData(GridData.FILL_BOTH);
        sections.setLayoutData(data);

        final DeviceEditorContext context =
                (DeviceEditorContext) getODOMEditorContext();

        categoriesSection =
                new CategoriesSection(sections, SWT.NONE, context);

        data = new GridData(GridData.FILL_VERTICAL);
        categoriesSection.setLayoutData(data);

        policiesSection =
                new PoliciesSection(sections, SWT.NONE, context);

        policiesSection.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Add a listener which updates the policies section when a new
        // category is selected.
        categoriesSection.addSelectionChangedListener(
                new ISelectionChangedListener() {
                    public void selectionChanged(SelectionChangedEvent event) {
                        // Currently, there is ALWAYS a selected category.
                        ODOMElement selectedCategoryElement =
                                (ODOMElement) ((IStructuredSelection)
                                event.getSelection()).getFirstElement();
                        String categoryName =
                                selectedCategoryElement.getAttributeValue(
                                        DeviceRepositorySchemaConstants.
                                CATEGORY_NAME_ATTRIBUTE);
                        policiesSection.setCategory(categoryName);
                    }
                });
        categoriesSection.setFocus();
    }

    /**
     * Create a LocationDetailsRegistry for possible errors in the
     * DevicePoliciesPart and set this registry on the ErrorReporter
     * associated with the given DeviceEditorContext.
     * @param context the DeviceEditorContext
     */
    private void createLocationDetailsRegistry(ODOMElement root,
                                               DeviceEditorContext context) {
        LocationDetailsRegistry registry = new LocationDetailsRegistry();
        DefaultJDOMFactory factory = new DefaultJDOMFactory();

        Element element = factory.element(DeviceRepositorySchemaConstants.
                POLICY_ELEMENT_NAME,
                MCSNamespace.DEVICE);

        LocationDetails details =
                new DevicePolicyLocationDetails(context.
                getDeviceRepositoryAccessorManager());

        registry.registerLocationDetails(element, details);

        context.getErrorReporter(root).setLocationDetailsRegistry(registry);
    }

    /**
     * Update this page with information from the given ODOMElement.
     */
    private void updatePage() {
        if (selectedDevice == null) {
            messagePage.layout();
            displayAreaLayout.topControl = messagePage;
        } else {
            String deviceName = selectedDevice.
                    getAttributeValue(DeviceRepositorySchemaConstants.
                    DEVICE_NAME_ATTRIBUTE);
            String headingText = new MessageFormat(HEADING_MESSAGE).
                    format(new String[]{deviceName});
            heading.setText(headingText);
            displayAreaLayout.topControl = policiesPage;

            DeviceEditorContext dec =
                    (DeviceEditorContext) getODOMEditorContext();
            selectedElement = (ODOMElement) dec.getDeviceRepositoryAccessorManager().
                    retrieveDeviceElement(deviceName);
            try {
                dec.addRootElement(selectedElement, deviceName);
                createLocationDetailsRegistry(selectedElement, dec);
                categoriesSection.setFocus();
                policiesSection.setCategory(categoriesSection.
                        getSelectedCategory());
                policiesPage.layout();
            } catch (SAXException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            } catch (ParserErrorException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            }
        }
        displayArea.layout();
    }


    // todo implement this
    // javadoc inherited
    protected void setFocus(XPath xpath) {
    }

    // javadoc inherited
    protected Document getRootElementDocument() {
        return selectedElement.getDocument();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	6260/1	allan	VBM:2004110907 NullPointerException and WidgetDisposed error

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 16-Nov-04	6218/2	adrianj	VBM:2004102021 Enhanced sizing for FormSections

 16-Nov-04	4394/2	allan	VBM:2004051018 Undo/Redo in device editor.

 13-May-04	4301/4	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 10-Sep-04	5488/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 02-Sep-04	5369/3	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 10-May-04	4068/3	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 05-May-04	4115/5	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 04-May-04	4007/4	doug	VBM:2004032304 Added a PrimaryPatterns form section

 30-Apr-04	4081/6	pcameron	VBM:2004031007 Added PoliciesSection

 29-Apr-04	4072/5	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 29-Apr-04	4072/3	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 27-Apr-04	4050/1	pcameron	VBM:2004040701 Added a device Information page and augmented DeviceRepositoryBrowser's title

 27-Apr-04	4016/1	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 ===========================================================================
*/
