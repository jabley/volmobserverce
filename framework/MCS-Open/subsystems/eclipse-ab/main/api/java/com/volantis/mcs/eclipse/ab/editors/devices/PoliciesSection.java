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
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilterConfiguration;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;

/**
 * The FormSection for editing device policies of a specific category.
 */
public class PoliciesSection extends FormSection implements XPathFocusable {

    /**
     * The prefix for resource messages associated with PoliciesSection.
     */
    private static final String RESOURCE_PREFIX = "PoliciesSection.";

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
     * A filter that only includes device elements in the selection.
     */
    private static final ODOMSelectionFilter DEVICE_FILTER =
            new ODOMSelectionFilter(null, new String[]{
                DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME},
                    new ODOMSelectionFilterConfiguration(true, true));

    /**
     * Maintain the currently selected device so that PolicyControllers can
     * be initialized properly.
     */
    private String deviceName;

    /**
     * The ODOMSelectionManager needed for the CategoryCompositeBuilder.
     */
    private final ODOMSelectionManager selectionManager;

    /**
     * The scrolling display area control.
     */
    private ScrolledComposite displayArea;

    /**
     * The Composite created by the CategoryCompositeBuilder for the
     * selected category.
     */
    private Composite categoryComposite;

    /**
     * The currently selected category. Keeping track of this means that a
     * category composite is only created if the category has changed.
     */
    private String selectedCategory;

    /**
     * The DeviceEditorComposite associated with this PoliciesSection.
     */
    private DeviceEditorContext context;

    /**
     * Creates a new PoliciesSection.
     * @param parent the parent Composite. Cannot be null.
     * @param style the style for the PoliciesSection
     * @param context the device editor context for the section. Cannot be null.
     */
    public PoliciesSection(Composite parent, int style,
                           DeviceEditorContext context) {
        super(parent, style);
        if (context == null) {
            throw new IllegalArgumentException("Cannot be null: context.");
        }
        this.context = context;
        this.selectionManager = context.getODOMSelectionManager();

        createDisplayArea(TITLE, MESSAGE);

        // Add a listener on the definitions document so that if it changes
        // we dispose of our categoryComposite in case the change is affects
        // the category controls. This is a bit of a sledge-hammer approach
        // but since we regularly destroy categoryComposites anyway and there
        // is not much time to do more specific updating of the
        // categoryComposites this is the current chosen approach.
        ODOMElement definitions = (ODOMElement) context.
                getDeviceRepositoryAccessorManager().
                getDeviceDefinitionsDocument().getRootElement();
        definitions.addChangeListener(new ODOMChangeListener() {
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                if (categoryComposite != null) {
                    categoryComposite.dispose();
                    categoryComposite = null;
                    selectedCategory = null;
                }
            }

        });

        // Create a selection listener for the ODOMSelectionManager which
        // allows the PoliciesSection to know the currently selected device.
        // The selected device will be provided to the CategoryCompositeBuilder
        // so that it knows at the time of building what the selected device
        // is. It is possible for the selected device to be null at the
        // time the CategoryComposite is built e.g. before any device is
        // selected when the editor is first loaded. The
        // CategoryCompositeBuilder should handle this scenario.         
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

                            deviceName =
                                    selectedElement.getAttributeValue(
                                            DeviceRepositorySchemaConstants.
                                    DEVICE_NAME_ATTRIBUTE);

                        }
                    }
                };

        selectionManager.addSelectionListener(selectionListener,
                DEVICE_FILTER);
    }

    /**
     * Creates the scrolling display area for this section.
     */
    private void createDisplayArea(String title, String message) {
        Section section =
                SectionFactory.createSection(this, SWT.NONE, title, message);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        displayArea = new ScrolledComposite(section,
                SWT.H_SCROLL | SWT.V_SCROLL);
        section.setClient(displayArea);

        displayArea.setExpandHorizontal(true);
        displayArea.setExpandVertical(true);
        displayArea.setAlwaysShowScrollBars(false);
        displayArea.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        displayArea.setLayout(new FillLayout());
        displayArea.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    // javadoc inherited
    public boolean setFocus(XPath path) {
        // @todo this needs to be implemented
        return false;
    }

    /**
     * Set the category of policies for display. A new display is only
     * created if the category changes.
     * @param category the name of the category. Cannot be null.
     * @throws IllegalArgumentException if category is null or empty
     */
    public void setCategory(final String category) {
        if (category == null || category.length() == 0) {
            throw new IllegalArgumentException("Cannot be null or empty: " +
                    "category");
        }

        // Only create a new display if the category has changed.
        if (!category.equals(selectedCategory)) {

            selectedCategory = category;

            if (categoryComposite != null) {
                categoryComposite.dispose();
                categoryComposite = null;
            }

            // Create a new category composite showing a busy cursor during
            // its creation.
            BusyIndicator.showWhile(this.getDisplay(), new Runnable() {
                public void run() {
                    categoryComposite = CategoryCompositeBuilder.
                            buildCategoryComposite(displayArea, SWT.NONE,
                                    category, deviceName, context);
                    categoryComposite.
                            setLayoutData(new GridData(GridData.FILL_BOTH));
                    categoryComposite.setBackground(
                            getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

                    displayArea.setContent(categoryComposite);
                    refresh();
                }
            });
        }
    }

    /**
     * Performs a "refresh" of the scrolled area, so that the display's size
     * is recomputed, and the scroll bars adjust accordingly.
     */
    private void refresh() {
        if (categoryComposite != null && !categoryComposite.isDisposed()) {
            categoryComposite.setSize(this.computeSize(SWT.DEFAULT,
                    SWT.DEFAULT));
            categoryComposite.layout();
            categoryComposite.pack();
            displayArea.setMinSize(
                    categoryComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        }
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

 16-Nov-04	4394/2	allan	VBM:2004051018 Undo/Redo in device editor.

 03-Sep-04	5405/1	allan	VBM:2004082009 Ensure PolicyControllers have a device name when they need it

 03-Sep-04	5347/1	allan	VBM:2004082009 Ensure PolicyControllers have a device name when they need it

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 14-May-04	4369/1	allan	VBM:2004051311 Override fixed, widget dispose fix, new button fix.

 11-May-04	4290/2	doug	VBM:2004051016 Fixed spacing issue with the policies section

 10-May-04	4068/1	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 30-Apr-04	4081/8	pcameron	VBM:2004031007 Added PoliciesSection

 ===========================================================================
*/
