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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyType;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyTypeComposition;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilterConfiguration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.UndeclaredThrowableException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;

/**
 * This class constructs groups of controls for modifying policies of a given
 * category.
 * Note that controls are added in the order specified in the category element.
 */
public class CategoryCompositeBuilder {

    /**
     * The prefix for resources used by this class.
     */
    private static final String RESOURCE_PREFIX = "CategoryCompositeBuilder.";

    /**
     * The horizontal spacing for CategoryCompositeBuilder.
     */
    private static final int HORIZONTAL_SPACING =
            DevicesMessages.getInteger(RESOURCE_PREFIX + "horizontalSpacing").
            intValue();

    /**
     * The margin height for the CategoryCompositeBuilder.
     */
    private static final int MARGIN_HEIGHT =
            DevicesMessages.getInteger(RESOURCE_PREFIX + "marginHeight").
            intValue();

    /**
     * The margin width for the CategoryCompositeBuilder.
     */
    private static final int MARGIN_WIDTH =
            DevicesMessages.getInteger(RESOURCE_PREFIX + "marginWidth").
            intValue();

    /**
     * The vertical spacing for CategoryCompositeBuilder.
     */
    private static final int VERTICAL_SPACING =
            DevicesMessages.getInteger(RESOURCE_PREFIX + "verticalSpacing").
            intValue();

    /**
     * The collection of
     * {@link com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyType}s
     * which provide their own labels for their PolicyValueModifiers.
     */
    private static final List LABELLED_POLICY_TYPES;

    /**
     * Initialise the collection.
     */
    static {
        LABELLED_POLICY_TYPES = new ArrayList(2);
        LABELLED_POLICY_TYPES.add(PolicyType.BOOLEAN);
        LABELLED_POLICY_TYPES.add(PolicyType.EMULATE_EMPHASIS_TAG);
    }


    /**
     * A filter that only includes device elements in the selection.
     */
    private static final ODOMSelectionFilter DEVICE_FILTER =
            new ODOMSelectionFilter(null, new String[]{
                DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME},
                    new ODOMSelectionFilterConfiguration(true, true));

    /**
     * Construct a new CategoryCompositeBuilder.
     */
    private CategoryCompositeBuilder() {
    }

    /**
     * Creates and returns a Composite which contains all the controls
     * necessary for modifying all policies of the given category.
     * @param parent the parent Composite. Cannot be null.
     * @param style the style for the built category Composite
     * @param category the name of the category. Cannot be null.
     * @param selectedDeviceName the name of the currently selected device.
     * Can be null if no device is currently selected - but must not be
     * null subsequent to device selection.
     * @param context the DeviceEditorContext in use.
     * @return the category Composite for the given category
     * @throws IllegalArgumentException if parent, category or context is null
     */
    public static Composite
            buildCategoryComposite(Composite parent, int style,
                                   String category,
                                   final String selectedDeviceName,
                                   final DeviceEditorContext context) {
        if (parent == null) {
            throw new IllegalArgumentException("Cannot be null: parent.");
        }
        if (category == null || category.length() == 0) {
            throw new IllegalArgumentException("Cannot be null nor empty: " +
                    "category.");
        }
        if (context == null) {
            throw new IllegalArgumentException("Cannot be null: context.");
        }

        final Composite categoryComposite = new Composite(parent, style);

        // Set the layout of the category composite.
        GridLayout gridLayout = new GridLayout(3, false);
        gridLayout.horizontalSpacing = HORIZONTAL_SPACING;
        gridLayout.verticalSpacing = VERTICAL_SPACING;
        gridLayout.marginHeight = MARGIN_HEIGHT;
        gridLayout.marginWidth = MARGIN_WIDTH;
        categoryComposite.setLayout(gridLayout);

        // The PolicyValueModifierFactory to use for creating the
        // PolicyValueModifier controls used by the PolicyController.
        final PolicyValueModifierFactory pvmFactory =
                new PolicyValueModifierFactory(context
                .getDeviceRepositoryAccessorManager());

        // Get the policy name iterator for the given category.
        Iterator policyNamesIterator = null;
        try {
            policyNamesIterator = context.getDeviceRepositoryAccessorManager().
                    categoryPolicyNamesIterator(category);
        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            throw new UndeclaredThrowableException(e);
        }

        List sortedNames = new ArrayList();
        while (policyNamesIterator.hasNext()) {
            sortedNames.add(policyNamesIterator.next());
        }

        // Sort the names according to the label text that is displayed.
        Collections.sort(sortedNames, new Comparator() {
            public int compare(Object o1, Object o2) {
                String policyName1 =
                        context.getDeviceRepositoryAccessorManager().
                        getLocalizedPolicyName((String) o1);
                String policyName2 =
                        context.getDeviceRepositoryAccessorManager().
                        getLocalizedPolicyName((String) o2);

                return policyName1.compareTo(policyName2);
            }
        });

        policyNamesIterator = sortedNames.iterator();

        final Display display = categoryComposite.getDisplay();
        categoryComposite.
                setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        final List policyControllerList = new ArrayList(sortedNames.size());
        final List selectionListenerList = new ArrayList(sortedNames.size());

        // Create the controls and PolicyController for each policy in the
        // category.
        while (policyNamesIterator.hasNext()) {

            final PolicyOriginSelector selector =
                    new PolicyOriginSelector(categoryComposite, SWT.NONE,
                            context.getDeviceRepositoryAccessorManager(),
                            context.isAdminProject());

            selector.setLayoutData(
                    new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

            String policyName = (String) policyNamesIterator.next();

            Element typeElement = context.getDeviceRepositoryAccessorManager().
                    getTypeDefinitionElement(policyName);

            PolicyType policyType = PolicyType.getType(typeElement);

            PolicyTypeComposition composition =
                    PolicyTypeComposition.getComposition(typeElement);

            // Assume no label is required. {@See #policyTypesWithoutLabels}.
            Label policyLabel = null;

            // Only add a label if the PolicyValueModifier is not already
            // labelled. To get the label's text centered on the
            // PolicyOriginSelector to its left requires the use of an
            // intermediate Composite.
            if (!isLabelled(composition, policyType)) {

                // Create the Composite which holds the label. Note that the
                // Composite isn't given a layout until after the label is
                // created. This is because the layout calculates its margin
                // height based on the size of the created label.
                Composite labelComposite =
                        new Composite(categoryComposite, SWT.NONE);
                // The Composite is vertically aligned at the top.
                labelComposite.setLayoutData(
                        new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
                labelComposite.
                        setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));

                // Create the label and its text.
                policyLabel = new Label(labelComposite, SWT.NONE);
                String policyText =
                        context.getDeviceRepositoryAccessorManager().
                        getLocalizedPolicyName(policyName);
                policyLabel.setText(policyText);
                policyLabel.
                        setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
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
                    pvmFactory.createPolicyValueModifier(categoryComposite,
                            SWT.BORDER, policyName);

            // Ensure the PolicyValueModifier spans the correct number of
            // columns.
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
            if (policyLabel == null) {
                gridData.horizontalSpan = 2;
            }
            modifier.getControl().setLayoutData(gridData);

            modifier.getControl().
                    setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));

            // Create the PolicyController which manages interactions between
            // the individual controls.
            final PolicyController controller =
                    new PolicyController(policyName, selector, modifier,
                            policyLabel, context);

            policyControllerList.add(controller);



            // Create a selection listener for the ODOMSelectionManager which
            // updates the PolicyController whenever the device selection
            // changes. The PolicyController only updates if it detects a
            // different device.
            final ODOMElementSelectionListener selectionListener =
                    new ODOMElementSelectionListener() {
                        public void selectionChanged(
                                ODOMElementSelectionEvent event) {
                            // This is a single-select context. Note that this
                            // retrieved element comes from the hierarchy
                            // document and not from a device document.
                            if (!event.getSelection().isEmpty()) {
                                ODOMElement selectedElement = (ODOMElement)
                                        event.getSelection().getFirstElement();

                                String deviceName =
                                        selectedElement.getAttributeValue(
                                                DeviceRepositorySchemaConstants.
                                        DEVICE_NAME_ATTRIBUTE);

                                controller.setDeviceName(deviceName);
                            }
                        }
                    };

            selectionListenerList.add(selectionListener);

            context.getODOMSelectionManager().addSelectionListener(selectionListener,
                    DEVICE_FILTER);

            if (selectedDeviceName != null) {
                controller.setDeviceName(selectedDeviceName);
            }
        }

        // Adds a dispose listener to the built category composite. This
        // listener cleans up by removing the selection listeners added to
        // the selection manager (above) for each PolicyController, and
        // disposes of each PolicyController.
        categoryComposite.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                // The selection listeners inform the policy controllers
                // of changes. Logically, it is safest to remove the listeners
                // before dealing with the policy controller cleanup.
                Iterator it = selectionListenerList.iterator();
                while (selectionListenerList.size() > 0) {
                    ODOMElementSelectionListener listener =
                            (ODOMElementSelectionListener)
                            selectionListenerList.remove(0);

                    context.getODOMSelectionManager().
                            removeSelectionListener(listener,
                            DEVICE_FILTER);
                }

                it = policyControllerList.iterator();
                while (it.hasNext()) {
                    ((PolicyController) it.next()).dispose();
                }
            }
        });

        return categoryComposite;
    }


    /**
     * Specifies whether or not the PolicyValueModifier for the given policy
     * type provides its own label.
     * @param composition the policies composition
     * @param type the policies type
     * @return true if a label is provided, false otherwise
     */
    public static boolean isLabelled(PolicyTypeComposition composition,
                                     PolicyType type) {
        // those labelled policy types will require a label if they composition
        // is ORDERED_SET or UNORDERED_SET
        return LABELLED_POLICY_TYPES.contains(type) &&
                composition != PolicyTypeComposition.ORDERED_SET &&
                composition != PolicyTypeComposition.UNORDERED_SET;

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 16-Nov-04	4394/2	allan	VBM:2004051018 Undo/Redo in device editor.

 03-Sep-04	5405/1	allan	VBM:2004082009 Ensure PolicyControllers have a device name when they need it

 03-Sep-04	5347/1	allan	VBM:2004082009 Ensure PolicyControllers have a device name when they need it

 03-Sep-04	5369/3	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 02-Sep-04	5369/1	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 14-May-04	4413/1	doug	VBM:2004051412 Fixed PolicyValueModifier labelling issue

 13-May-04	4354/2	pcameron	VBM:2004051013 Fixed PolicyController label alignments in CategoryCompositeBuilder and MasterValueSection

 12-May-04	4307/2	allan	VBM:2004051201 Fix restore button and moveListeners()

 12-May-04	4290/7	doug	VBM:2004051016 Fixed spacing issue with the policies section

 11-May-04	4290/4	doug	VBM:2004051016 Fixed spacing issue with the policies section

 11-May-04	4303/1	allan	VBM:2004051005 Restore button and Widget is disposed bug fix.

 10-May-04	4235/1	pcameron	VBM:2004031603 Added MasterValueSection

 10-May-04	4068/3	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 30-Apr-04	4081/10	pcameron	VBM:2004031007 Added PoliciesSection

 30-Apr-04	4081/8	pcameron	VBM:2004031007 Added PoliciesSection

 29-Apr-04	4103/1	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 21-Apr-04	3909/5	pcameron	VBM:2004031004 Refactored the iterators

 20-Apr-04	3909/1	pcameron	VBM:2004031004 Added CategoryCompositeBuilder

 ===========================================================================
*/
