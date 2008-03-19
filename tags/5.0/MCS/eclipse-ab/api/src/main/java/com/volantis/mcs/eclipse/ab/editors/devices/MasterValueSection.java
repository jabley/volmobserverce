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
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyType;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyTypeComposition;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.xpath.XPath;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Section;
import org.jdom.Element;
import org.xml.sax.SAXException;

/**
 * The form section that displays the {@link PolicyController} for modifying
 * the selected policy of the Master device.
 */
public class MasterValueSection extends FormSection implements XPathFocusable {

    /**
     * The prefix for resource messages associated with MasterValueSection.
     */
    private static final String RESOURCE_PREFIX = "MasterValueSection.";

    /**
     * The title for this section.
     */
    private static final String TITLE =
            DevicesMessages.getString(RESOURCE_PREFIX + "title");

    /**
     * The message format for this section.
     */
    private static final String MESSAGE =
            DevicesMessages.getString(RESOURCE_PREFIX + "message");

    /**
     *The horizontal spacing for this part.
     */
    private static final int HORIZONTAL_SPACING =
            DevicesMessages.getInteger(RESOURCE_PREFIX + "horizontalSpacing").
            intValue();

    /**
     * The PolicyValueModifierFactory used to create one of the
     * PolicyController's controls.
     */
    private final PolicyValueModifierFactory pvmFactory;

    /**
     * The display area used by this section.
     */
    private Composite displayArea;

    /**
     * The Composite used to hold the widgets for the PolicyController. This
     * is a field because it needs to be disposed of when the policy changes.
     */
    private Composite controllerComposite;

    /**
     * The PolicyController used by this section.
     */
    private PolicyController policyController;

    /**
     * The DeviceEditorContext used by the PolicyController.
     */
    private final DeviceEditorContext context;

    /**
     * Will be used to refresh the display whenever the policy definitions
     * type is updated.
     */
    private ODOMChangeListener structureUpdateListener;

    /**
     * The current policy element that is controlled by this section.
     */
    private ODOMElement currentPolicy;


    /**
     * Creates a new MasterValueSection.
     *
     * @param parent the parent Composite. Cannot be null.
     * @param style the style for the MasterValueSection
     * @param context the DeviceEditorContext associated with this section.
     * Cannot be null.
     * @throws IllegalArgumentException if context is null.
     */
    public MasterValueSection(Composite parent, int style,
                              DeviceEditorContext context) {
        super(parent, style);
        if (context == null) {
            throw new IllegalArgumentException("Cannot be null: context.");
        }
        this.context = context;

        // Create the factory used to created the PolicyValueModifiers,
        pvmFactory = new PolicyValueModifierFactory(context.
                getDeviceRepositoryAccessorManager());

        // this listener will be registered with the policy definition
        // associated with this page. This is required as the
        // PolicyDefinitionSection allows a policies type to be modified, if
        // the policy type is changed the controls that allow the master value
        // to be edited need to be disposed of and re-createated
        structureUpdateListener = new ODOMChangeListener() {
            // javadoc inherited
            public void changed(ODOMObservable node,
                ODOMChangeEvent event) {
                // The PolicyDefinitionSection modifies the policies by
                // detaching the old element and adding a new one. We
                // do not want to process the detach event so only perform the
                // refresh if the event's new value is not null.
                if (event.getNewValue() != null) {
                    String name = event.getSource().getName();
                    // Is it a type or keyword change event?
                    if (name.equals(DeviceRepositorySchemaConstants.
                            POLICY_DEFINITION_TYPE_ELEMENT_NAME) ||
                        name.equals(DeviceRepositorySchemaConstants.
                            POLICY_DEFINITION_KEYWORD_ELEMENT_NAME)) {
                        // refresh the display
                        displayPolicyControl(currentPolicy);
                    }
                }
            }
        };
        createDisplayArea(TITLE, MESSAGE);
    }

    /**
     * Create the display area for this section.
     */
    private void createDisplayArea(String title, String message) {
        Section section =
                SectionFactory.createSection(this, title, message);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        displayArea = new Composite(section, SWT.NONE);
        displayArea.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        displayArea.setLayout(new FillLayout());
        displayArea.setLayoutData(new GridData(GridData.FILL_BOTH));

        section.setClient(displayArea);
    }

    /**
     * Set the policy that whose Master device value is to be editable.
     * @param policy  the policy that requires editing. Cannot be null
     * @throws IllegalArgumentException if the policy is null.
     */
    public void setPolicy(ODOMElement policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Cannot be null: policy.");
        }
        if (currentPolicy != policy) {
            displayPolicyControl(policy);
        }
    }

    /**
     * Updates the display area with the controls that allow the user
     * to edit the master devices policy
     * @param policy the policy that requires editing. Cannot be null
     * @throws IllegalArgumentException if the policy is null.
     */
    private void displayPolicyControl(ODOMElement policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Cannot be null: policy.");
        }
        if (currentPolicy != null) {
            // remove the listener from the currentPolicy
            currentPolicy.removeChangeListener(structureUpdateListener);
        }

        if (policyController != null) {
            // Dispose of existing controls before creating new ones.
            policyController.dispose();
            controllerComposite.dispose();
        }

        // register the listener with the new element.
        policy.addChangeListener(structureUpdateListener);

        // set the new policy
        currentPolicy = policy;

        String policyName = policy.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_NAME_ATTRIBUTE);

        // Create a new Composite for the PolicyController's widgets.
        controllerComposite = new Composite(displayArea, SWT.NONE);
        controllerComposite.setBackground(displayArea.getDisplay().
                                          getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // Set the layout needed by the PolicyController.
        GridLayout gridLayout = new GridLayout(3, false);
        gridLayout.horizontalSpacing = HORIZONTAL_SPACING;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        controllerComposite.setLayout(gridLayout);

        // @todo The construction of the individual controls for the
        // PolicyController is the same as the code in the
        // CategoryCompositeBuilder.

        // Create the origin selector for the PolicyController.
        final PolicyOriginSelector selector =
                    new PolicyOriginSelector(controllerComposite, SWT.NONE,
                                             context.
                getDeviceRepositoryAccessorManager(), context.isAdminProject());
        selector.setLayoutData(
                    new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        Element typeElement =
                        context.getDeviceRepositoryAccessorManager().
                getTypeDefinitionElement(policyName);

        PolicyType policyType = PolicyType.getType(typeElement);

        PolicyTypeComposition composition =
                    PolicyTypeComposition.getComposition(typeElement);


        // Assume no label is required.
        Label policyLabel = null;

        // Only add a label if the PolicyValueModifier is not already
        // labelled.
        if (!CategoryCompositeBuilder.isLabelled(composition, policyType)) {

            // Create the Composite which holds the label. Note that the
            // Composite isn't given a layout until after the label is
            // created. This is because the layout calculates its margin
            // height based on the size of the created label.
            Composite labelComposite =
                        new Composite(controllerComposite, SWT.NONE);
            // The Composite is vertically aligned at the top.
            labelComposite.setLayoutData(
                        new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
            labelComposite.setBackground(displayArea.getDisplay().
                                         getSystemColor(SWT.COLOR_LIST_BACKGROUND));

            // Create the label and its text.
            policyLabel = new Label(labelComposite, SWT.NONE);
            String policyText =
                        context.getDeviceRepositoryAccessorManager().
                    getLocalizedPolicyName(policyName);
            policyLabel.setText(policyText);
            policyLabel.setBackground(displayArea.getDisplay().
                                      getSystemColor(SWT.COLOR_LIST_BACKGROUND));
            policyLabel.
                        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            // Create the layout for the label's Composite.
            GridLayout grid = new GridLayout();
            grid.marginWidth = 0;
            // The layout's margin height depends on the heights of the
            // created PolicyOriginSelector and label (half the difference).
            // These two controls must be packed before they yield their
            // correct size. Note that laying them out doesn't work (in
            // fact there is no layout() for a Label widget).
            selector.pack();
            policyLabel.pack();
            grid.marginHeight = Math.abs(selector.getSize().y -
                                         policyLabel.getSize().y) / 2;
            labelComposite.setLayout(grid);
        }

        PolicyValueModifier modifier =
                    pvmFactory.createPolicyValueModifier(controllerComposite,
                                                         SWT.BORDER, policyName);

        // Ensure the PolicyValueModifier spans the correct number of
        // columns.
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        if (policyLabel == null) {
            gridData.horizontalSpan = 2;
        }
        modifier.getControl().setLayoutData(gridData);

        modifier.getControl().setBackground(displayArea.getDisplay().
                                            getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // Create the PolicyController which manages interactions between
        // the individual controls.
        policyController = new PolicyController(policyName, selector,
                                                modifier, policyLabel, context);

        // The PolicyController is only ever interested in the Master
        // device.
        String masterDeviceName = context.
            getDeviceRepositoryAccessorManager().retrieveRootDeviceName();

        policyController.setDeviceName(masterDeviceName);

        ODOMElement masterElement =
            (ODOMElement) context.getDeviceRepositoryAccessorManager().
                retrieveDeviceElement(masterDeviceName);

        // Add the master element as a root of the context, this is so
        // that if the user changes the master value the device document
        // is flagged as needing to be saved.
        try {
            context.addRootElement(masterElement, masterDeviceName);
        } catch (SAXException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } catch (ParserErrorException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        // Layout the controls.
        controllerComposite.layout();
        displayArea.layout();
    }

    // javadoc inherited
    public boolean setFocus(XPath path) {
        // @todo implement later
        return false;
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

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 16-Nov-04	4394/2	allan	VBM:2004051018 Undo/Redo in device editor.

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 14-May-04	4413/3	doug	VBM:2004051412 Fixed PolicyValueModifier labelling issue

 14-May-04	4367/3	doug	VBM:2004051108 Ensured that the MasterValueSection refreshes whenever the policies type changes

 13-May-04	4354/2	pcameron	VBM:2004051013 Fixed PolicyController label alignments in CategoryCompositeBuilder and MasterValueSection

 10-May-04	4235/5	pcameron	VBM:2004031603 Added MasterValueSection

 10-May-04	4235/3	pcameron	VBM:2004031603 Added MasterValueSection

 ===========================================================================
*/
