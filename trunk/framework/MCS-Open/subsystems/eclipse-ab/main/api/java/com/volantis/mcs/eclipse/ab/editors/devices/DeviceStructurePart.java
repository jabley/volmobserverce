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
import com.volantis.mcs.eclipse.ab.editors.devices.validation.CategoryPolicyLocationDetails;
import com.volantis.mcs.eclipse.ab.editors.devices.validation.RangeValidator;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorPart;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetails;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetailsRegistry;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidatorDetails;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.common.ObservableProperties;
import com.volantis.mcs.eclipse.controls.ControlUtils;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.DefaultJDOMFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The structure page for the device editor.
 */
public class DeviceStructurePart extends ODOMEditorPart {
    /**
     * The prefix for resources properties associated with this class.
     */
    private static final String RESOURCE_PREFIX = "DeviceStructurePart.";

    /**
     * The horizontal spacing for this part.
     */
    private static final int HORIZONTAL_SPACING =
            EditorMessages.getInteger("Editor.horizontalSpacing").intValue();

    /**
     * The vertical spacing for this part.
     */
    private static final int VERTICAL_SPACING =
            EditorMessages.getInteger("Editor.verticalSpacing").intValue();

    /**
     * The selected element.
     */
    private ODOMElement selectedElement;

    /**
     * The margin height for this part.
     */
    private static final int MARGIN_HEIGHT =
            EditorMessages.getInteger("Editor.marginHeight").intValue();

    /**
     * The margin width for this part.
     */
    private static final int MARGIN_WIDTH =
            EditorMessages.getInteger("Editor.marginWidth").intValue();

    /**
     * The scrolled composite containing the controls.
     */
    private ScrolledComposite controlsScroller;

    /**
     * The composite containing the controls for the selected policy.
     */
    private Composite controlsComposite;

    /**
     * The DeviceEditorContext associated with this part.
     */
    private final DeviceEditorContext context;

    /**
     * Initialises a <code>DeviceStructurePart</code> instance.
     * @param context the {@link DeviceEditorContext} for this structure
     */
    public DeviceStructurePart(DeviceEditorContext context) {
        // the root element is the definitions element
        super(DeviceRepositorySchemaConstants.DEFINITIONS_ELEMENT_NAME,
                context);

        this.context = context;

        context.addSupplementaryValidator(new DOMSupplementaryValidatorDetails(
                MCSNamespace.DEVICE_DEFINITIONS.getURI(),
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_RANGE_ELEMENT_NAME,
                new RangeValidator()));

        createLocationDetailsRegistry(context);
    }

    // javdoc inherited
    protected void createPartControlImpl(Composite parent) {
        // Add a two column container for the form sections.
        Composite displayArea = new Composite(parent, SWT.NONE);
        GridLayout displayAreaLayout = new GridLayout(2, false);
        displayAreaLayout.marginWidth = MARGIN_WIDTH;
        displayAreaLayout.marginHeight = MARGIN_HEIGHT;
        displayAreaLayout.horizontalSpacing = HORIZONTAL_SPACING;
        displayAreaLayout.verticalSpacing = VERTICAL_SPACING;
        displayArea.setLayout(displayAreaLayout);
        displayArea.setLayoutData(new GridData(GridData.FILL_BOTH));
        final Color white = parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
        displayArea.setBackground(white);

        final DeviceDefinitionPoliciesSection policiesSection =
                new DeviceDefinitionPoliciesSection(displayArea, SWT.NONE,
                        (DeviceEditorContext) getODOMEditorContext());

        GridData data = new GridData(GridData.FILL_VERTICAL);
        policiesSection.setLayoutData(data);

        final Composite stackedContainer = new Composite(displayArea,
                SWT.NONE);
        final StackLayout stackLayout = new StackLayout();
        stackLayout.marginHeight = 0;
        stackLayout.marginHeight = 0;
        stackedContainer.setLayout(stackLayout);
        stackedContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Create the scrolled composite in the second column.
        controlsScroller = new ScrolledComposite(stackedContainer,
                SWT.H_SCROLL | SWT.V_SCROLL);
        controlsScroller.setLayout(new FillLayout());
        controlsScroller.setLayoutData(new GridData(GridData.FILL_BOTH));
        controlsScroller.setExpandHorizontal(true);
        controlsScroller.setExpandVertical(true);
        controlsScroller.setAlwaysShowScrollBars(false);
        controlsScroller.setBackground(white);

        // Create the controls composite content for the scroller.
        controlsComposite = new Composite(controlsScroller, SWT.NONE);
        GridLayout controlsLayout = new GridLayout();
        controlsLayout.marginWidth = 0;
        controlsLayout.marginHeight = 0;
        controlsLayout.horizontalSpacing = HORIZONTAL_SPACING;
        controlsLayout.verticalSpacing = VERTICAL_SPACING;
        controlsComposite.setLayout(controlsLayout);
        controlsComposite.setBackground(white);

        // Create the policy definition form seciton
        final PolicyDefinitionSection policyDefinitionSection =
                new PolicyDefinitionSection(
                        controlsComposite,
                        SWT.NONE,
                        ((DeviceEditorContext) getODOMEditorContext()));
        policyDefinitionSection.setLayoutData(
                new GridData(GridData.FILL_HORIZONTAL));

        // Create the Master Value section.
        final MasterValueSection masterValueSection =
                new MasterValueSection(controlsComposite, SWT.NONE,
                        context);
        masterValueSection.setLayoutData(
                new GridData(GridData.FILL_HORIZONTAL));

        // Create the CCPP Form section
        final CCPPDefinitionSection ccppDefinitionSection =
                    new CCPPDefinitionSection(controlsComposite, SWT.NONE,
                            ((DeviceEditorContext) getODOMEditorContext()));
        ccppDefinitionSection.setLayoutData(new GridData(GridData.FILL_BOTH));

        controlsScroller.setContent(controlsComposite);

        // used to display a message whenever a category is selected
        final Composite categorySelectedComposite =
                ControlUtils.createMessageComposite(stackedContainer,
                        SWT.NONE,
                        new String[]{});

        categorySelectedComposite.setLayoutData(
                new GridData(GridData.FILL_BOTH));
        stackLayout.topControl = categorySelectedComposite;

        // Add a selection listener so that controls may respond to policy
        // selections.
        policiesSection.addSelectionChangedListener(
                new ISelectionChangedListener() {
                    public void selectionChanged(SelectionChangedEvent event) {
                        selectedElement =
                                (ODOMElement) policiesSection.
                                getSelectedPolicyElement();
                        if (selectedElement != null) {

                            // all policies can be edited if the user is the
                            // admin user.
                            boolean isPolicyEditable =
                                    getODOMEditorContext().isAdminProject();
                            if (!isPolicyEditable) {
                                // if the user is not the admin user but the
                                // policy belongs to the custom category then
                                // the user can edit the policy
                                String categoryName =
                                        policiesSection.
                                        getSelectedCategoryName();
                                isPolicyEditable = categoryName.equals(
                                        DeviceRepositorySchemaConstants.
                                        CUSTOM_CATEGORY_NAME);
                            }

                            // refresh the policyDefinition section
                            policyDefinitionSection.setElement(selectedElement);
                            // refresh the master value section
                            masterValueSection.setPolicy(selectedElement);
                            // refresh the ccpp section
                            ccppDefinitionSection.setElement(selectedElement);

                            // Compute new size for adjusting scroll bars
                            // automatically.
                            controlsComposite.setSize(controlsComposite.
                                    computeSize(SWT.DEFAULT, SWT.DEFAULT));
                            controlsComposite.layout();
                            controlsScroller.setMinSize(
                                    controlsComposite.computeSize(
                                            SWT.DEFAULT, SWT.DEFAULT));

                            // the master value section is always editable.
                            // However, policy definiton and ccpp definition
                            // sections should only be enabled if the user
                            // is allowed to edit the policy
                            ControlUtils.setEnabledHierarchical(
                                    policyDefinitionSection,
                                    isPolicyEditable);
                            // We can't use ControlUtils.setEnabledHierarchical
                            // to enable/disable the CCPP section as it
                            // overrides Control#setEnabled in order to
                            // provide it's own behaviour.
                            ccppDefinitionSection.setEnabled(isPolicyEditable);

                            // bring the composite that contains these sections
                            // to the top of the stack layout
                            stackLayout.topControl = controlsScroller;

                        } else {
                            // a category is selected so bring the
                            // categorySelectedComposite to the top of the
                            // stack layout.
                            stackLayout.topControl = categorySelectedComposite;
                        }
                        stackedContainer.layout();
                    }
                });

        displayArea.layout();
        policiesSection.setFocus();

        // Add listener for changes to properties in device repository
        ObservableProperties properties =
                context.getDeviceRepositoryAccessorManager().getProperties();
        properties.addPropertyChangeListener(new PropertyChangeListener() {
            // Javadoc inherited
            public void propertyChange(PropertyChangeEvent evt) {
                setDirty(true);
            }
        });
    }


    /**
     * Create a LocationDetailsRegistry for possible errors in the
     * DeviceStructurePart and set this registry on the ErrorReporter
     * associated with the given DeviceEditorContext.
     * @param context the DeviceEditorContext
     */
    private void createLocationDetailsRegistry(DeviceEditorContext context) {
        LocationDetailsRegistry registry = new LocationDetailsRegistry();
        DefaultJDOMFactory factory = new DefaultJDOMFactory();

        Element element = factory.element(DeviceRepositorySchemaConstants.
                POLICY_ELEMENT_NAME,
                MCSNamespace.DEVICE_DEFINITIONS);

        ODOMElement root = (ODOMElement)
                context.getDeviceRepositoryAccessorManager().
                getDeviceDefinitionsDocument().getRootElement();

        LocationDetails details =
                new CategoryPolicyLocationDetails(context.getDeviceRepositoryAccessorManager());

        registry.registerLocationDetails(element, details);

        context.getErrorReporter(root).setLocationDetailsRegistry(registry);
    }


    // javadoc inherited
    protected XPathFocusable[] getXPathFocusableControls() {
        // @todo implement this
        return null;
    }

    // todo implement this
    public void setFocus() {
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

 22-Nov-04	6270/1	adrianj	VBM:2004110908 Fix to make changing properties files flag editor as dirty

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 16-Nov-04	6218/2	adrianj	VBM:2004102021 Enhanced sizing for FormSections

 16-Nov-04	4394/6	allan	VBM:2004051018 Undo/Redo in device editor.

 17-May-04	4394/2	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 13-May-04	4301/7	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 28-Sep-04	5671/1	adrianj	VBM:2004090111 Fix for closed 'twisties' taking too much space

 10-Sep-04	5488/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 08-Sep-04	5432/1	allan	VBM:2004081803 Validation for range min and max values

 25-Aug-04	5298/1	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 17-May-04	4442/3	pcameron	VBM:2004042608 Usages of constants from DeviceRepositorySchemaConstants in XMLDeviceRepositoryAccessor

 14-May-04	4367/4	doug	VBM:2004051108 Ensured that the MasterValueSection refreshes whenever the policies type changes

 12-May-04	4313/11	pcameron	VBM:2004051203 Added scrolling to DeviceStructurePart and fixed a few layout issues

 12-May-04	4313/9	pcameron	VBM:2004051203 Added scrolling to DeviceStructurePart and fixed a few layout issues

 12-May-04	4288/4	doug	VBM:2004051107 Ensured RHS of Structure page is disabled for non admin user

 11-May-04	4161/10	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 10-May-04	4237/5	byron	VBM:2004031601 Provide the CCPP form section - update

 10-May-04	4237/3	byron	VBM:2004031601 Provide the CCPP form section

 10-May-04	4235/5	pcameron	VBM:2004031603 Added MasterValueSection

 10-May-04	4235/3	pcameron	VBM:2004031603 Added MasterValueSection

 06-May-04	4068/2	allan	VBM:2004032103 Structure page policies section.

 05-May-04	4115/1	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 04-May-04	4007/1	doug	VBM:2004032304 Added a PrimaryPatterns form section

 26-Apr-04	4040/8	pcameron	VBM:2004032211 DeviceStructurePart uses definitions element name

 26-Apr-04	4040/2	pcameron	VBM:2004032211 Added DeviceStructurePart

 ===========================================================================
*/
