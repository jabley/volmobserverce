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

package com.volantis.mcs.eclipse.ab.views.layout;

import com.volantis.mcs.eclipse.ab.core.AttributesComposite;
import com.volantis.mcs.eclipse.ab.core.AttributesCompositeBuilder;
import com.volantis.mcs.eclipse.ab.editors.dom.ProxyElement;
import com.volantis.mcs.eclipse.ab.editors.layout.LayoutMessages;
import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.controls.ActionableHandler;
import com.volantis.mcs.eclipse.controls.HorizontalLabelLine;
import com.volantis.mcs.eclipse.controls.PackingLayout;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jdom.Attribute;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class that wraps attributes composite objects.
 *
 * It is responsible for displaying a horizontal line and title,
 * obtained from the appropriate resource class, and the relevant attributes
 * composite's controls.<p>
 *
 * When a given attributes composite is not displaying any controls,
 * the wrapping composite must hide itself. Thus, it seems sensible to direct
 * visibility updates at the wrapping composite which will delegate to the
 * attributes composite.<p>
 *
 * In addition, the wrapping composite must give its container information
 * about its visibility.<p>
 */
class SectionDetailsComposite extends Composite implements XPathFocusable {

    /**
     * The resource prefix.
     */
    private static final String RESOURCE_PREFIX = "WrappedAttributeComposite."; //$NON-NLS-1$

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SectionDetailsComposite.class);

    /**
     * The wrapped attributes composite object.
     */
    private AttributesComposite attributesComposite;

    /**
     * The listener that listens for changes on the attributes composite
     * and updates the ODOM Attributes if a change occurs.
     */
    private IPropertyChangeListener ipropertyChangeListener;

    /**
     * The proxy ODOM element itself.
     */
    private ODOMElement odomElement;

    /**
     * Store the label line here so that it may be made invisible/visible later.
     */
    private HorizontalLabelLine labelLine;

    /**
     * Store the section details (access to the supplementary values).
     */
    private SectionDetails details;

    /**
     * Store the odom selection manager used to add/remove odom selection
     * change events.
     */
    private ODOMSelectionManager odomSelectionManager;

    /**
     * Store the odom selection manager used to add/remove odom selection
     * change events.
     */
    private ODOMSelectionFilter filter;

    /**
     * The selection listener used to determine the set of selected elements
     * names.
     */
    private ODOMElementSelectionListener odomSelectionListener;

    /**
     * Construct the wrapped attribute composite with the specified parameters.
     *
     * @param parent  the parent composite.
     * @param style   the style
     * @param details the section details (used for displaying the section
     *                title).
     * @param odomElement the odom proxy element
     * @param project the current project.
     * @param odomSelectionManager
     *
     * @param filter  the odom selection filter.
     * @param handler the actionable handler.
     */
    public SectionDetailsComposite(Composite parent,
                                   int style,
                                   final SectionDetails details,
                                   ProxyElement odomElement,
                                   IProject project,
                                   ODOMSelectionManager odomSelectionManager,
                                   ODOMSelectionFilter filter,
                                   ActionableHandler handler) {
        super(parent, style);

        this.odomElement = odomElement;
        this.details = details;
        this.odomSelectionManager = odomSelectionManager;
        this.filter = filter;

        setLayout(new PackingLayout());

        odomSelectionListener = new ODOMElementSelectionListener() {
            // javadoc inherited.
            public void selectionChanged(ODOMElementSelectionEvent event) {
                updateSecondaryControls(event, details);
            }
        };

        odomSelectionManager.addSelectionListener(odomSelectionListener, filter);

        String title = details.getTitle();
        if (title != null) {
            // Create a horizontal line with a the section title as the label.
            labelLine = new HorizontalLabelLine(this, SWT.NONE,
                    getResourceString(title));
        }
        AttributesCompositeBuilder builder = AttributesCompositeBuilder.getSingleton();

        // All the controls of each section go into the wrapped composite.
        attributesComposite = builder.buildAttributesComposite(this,
                details.getDetails(), project, new PackingLayout(2), handler);

        enableDisplayArea();

        if (odomElement.getName().equals(ODOMElement.NULL_ELEMENT_NAME)) {
            disableDisplayArea();
        }
    }


    /**
     * Get the first column's width from the contained attributes composite.
     *
     * @return the first column's width from the contained attributes
     *         composite.
     */
    public int getFirstColumnWidth() {
        return attributesComposite.getFirstColumnWidth();
    }

    /**
     * Set the first column's preferred width.
     *
     * @param width the first column's preferred width.
     */
    public void setFirstColumnWidth(int width) {
        attributesComposite.setFirstColumnWidth(width);
    }

    /**
     * Helper method for updating the secondary controls visibility or enabled
     * state.
     * @param event the <code>ODOMElementSelectionEvent</code>.
     * @param details the section details.
     */
    private void updateSecondaryControls(ODOMElementSelectionEvent event,
                                         final SectionDetails details) {
        // We need to enable/disable and/or show/hide secondary controls
        // for each attribute's composites control in special
        // circumstances.
        Iterator elements = event.getSelection().iterator();

        boolean secondaryEnabled = true;
        boolean secondaryVisible = true;

        final String gridRowElement =
                LayoutSchemaType.GRID_FORMAT_ROWS_ELEMENT.getName();

        final String dissectingPane =
                FormatType.DISSECTING_PANE.getElementName();

        // Identify the special circumstances.
        while (elements.hasNext()) {
            final Element element = (Element) elements.next();
            if (element != null) {
                final String name = element.getName();
                if (dissectingPane.equals(name)) {
                    secondaryVisible = false;
                } else if (gridRowElement.equals(name)) {
                    secondaryVisible = false;
                }
            }
        }

        // Iterate over all the attributes and set the secondary
        // enablement and visibility to that identified above.
        if (attributesComposite != null) {
            final String[] attributes = details.getDetails().getAttributes();
            for (int i = 0; i < attributes.length; i++) {
                attributesComposite.setSecondaryEnabled(attributes[i],
                        secondaryEnabled);
                attributesComposite.setSecondaryVisible(attributes[i],
                        secondaryVisible);
            }
        }
    }

    /**
     * Helper method to return the resource string for the specified key.
     *
     * @param key the key.
     * @return the resource string for the specified key.
     */
    private String getResourceString(String key) {
        StringBuffer buffer = new StringBuffer(RESOURCE_PREFIX);
        buffer.append(key).append(".text"); //$NON-NLS-1$
        return LayoutMessages.getString(buffer.toString());
    }

    /**
     * Return the wrapped attributes composite.
     *
     * @return the wrapped attributes composite.
     */
    public AttributesComposite getAttributesComposite() {
        return attributesComposite;
    }

    /**
     * Remove the property change listener.
     *
     * @param listener the property change listener.
     */
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        if (listener != null) {
            attributesComposite.removePropertyChangeListener(listener);
        }
    }

    /**
     * Add the property change listener.
     *
     * @param listener the property change listener.
     */
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        this.ipropertyChangeListener = listener;
        if (listener != null) {
            attributesComposite.addPropertyChangeListener(listener);
        }
    }

    /**
     * Updates the display area of the controls after an ODOM element has
     * changed. If the ODOMElement was removed, clear the view's fields and
     * disable the view. Otherwise, enable the form and update the form with the
     * new attribute values.
     *
     * @param event the ODOMChangeEvent
     */
    public void updateDisplayArea(ODOMChangeEvent event) {

        removePropertyChangeListener(ipropertyChangeListener);

        final ChangeQualifier changeQualifier = event.getChangeQualifier();
        if ((changeQualifier == ChangeQualifier.HIERARCHY) &&
                event.getSource() instanceof Attribute) {
            if (event.getOldValue() == null &&
                    event.getNewValue() != null) {
                if (event.getNewValue().equals(odomElement)) {
                    updateAttribute((Attribute) event.getSource());
                }

            } else if (event.getOldValue() != null &&
                    event.getNewValue() == null) {
                if (event.getOldValue().equals(odomElement)) {
                    updateAttribute((Attribute) event.getSource());
                }
            } else {
                logger.warn("odom-hierarchy-event-ignored", event); //$NON-NLS-1$
            }
        } else if (changeQualifier == ChangeQualifier.ATTRIBUTE_VALUE) {
            // Attribute modified
            updateAttribute((Attribute) event.getSource());
        } else {
            logger.warn("odom-event-ignored", event); //$NON-NLS-1$
        }
        addPropertyChangeListener(ipropertyChangeListener);
    }

    /**
     * Enable displayArea and populate the AttributesComposite with
     * values from the odomElement.
     */
    private void enableDisplayArea() {
        List attributes = getOdomElement().getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attr = (Attribute) attributes.get(i);
            attributesComposite.setAttributeValue(attr.getName(),
                    attr.getValue());
        }
    }

    /**
     * Disable the displayArea setting all attribute values to "".
     */
    private void disableDisplayArea() {
        String[] attributeNames = attributesComposite.getAttributeNames();
        for (int i = 0; i < attributeNames.length; i++) {
            attributesComposite.setAttributeValue(attributeNames[i], ""); //$NON-NLS-1$
        }
    }

    /**
     * Return true if this attribute is part of this attributes composite.
     *
     * @param attribute the attribute name.
     * @return true if this attribute is part of this attributes composite,
     *         false otherwise.
     */
    private boolean isAttributePartOfThisComposite(String attribute) {
        boolean found = false;
        String[] attributeValues = attributesComposite.getAttributeNames();
        for (int i = 0; !found && i < attributeValues.length; i++) {
            String attributeValue = attributeValues[i];
            String supplementary = details.getDetails().getSupplementaryValue(attributeValue);
            if (attributeValue.equals(attribute) || attribute.equals(supplementary)) {
                found = true;
            }
        }
        return found;
    }

    /**
     * Update the given Attribute in the AttributesComposite if necessary.
     *
     * @param attribute The Attribute
     */
    private void updateAttribute(Attribute attribute) {
        // First check that the element has not been nulled because of a remove.
        if (!ODOMElement.NULL_ELEMENT_NAME.equals(getOdomElement().getName())) {
            // If the AttributesComposite is currently disabled then re-enable
            // and populate all its values. Otherwise just set the specific
            // attribute.
            if (!this.isEnabled()) {
                enableDisplayArea();
            } else {
                // Only set this attribute if is part of the composite's
                // attribute list.
                if (isAttributePartOfThisComposite(attribute.getName())) {
                    attributesComposite.setAttributeValue(attribute.getName(),
                            attribute.getValue());
                }
            }
        }
    }

    /**
     * Set the attributes in the list be visible. If this list is empty, or null
     * then ensure all controls are hidden.
     * @param attributeNamesToDisplay the attributes
     */
    public void setVisible(String[] attributeNamesToDisplay) {

        // Iterate over all this composite's attributes and set those we
        // want to see to be visible and the rest to be invisible.
        //
        // Note that the attributeNamesToDisplay may contain attribute names
        // that aren't in this composite's attributes list (for this section).
        //
        // For example if:
        //      attributesNamesToDisplay = { "name", "cellPadding" }
        //      compositeAttributes = { "name", "height", "borderWidth" }
        // then invisible will be (note 'borderWidth' is not in the
        // attributesNamesToDisplay):
        //      { "height", "borderWidth" }
        // and  visible will be:
        //      { "name" }
        String[] compositeAttributes = getAttributesComposite().getAttributeNames();

        // If the aren't any attribute names to display we hide all the ones
        // for this section by default.
        String[] attributesToShow = null;

        if (attributeNamesToDisplay != null && attributeNamesToDisplay.length > 0) {

            // Using asList() creates some garbage but doesn't actually
            // create a new copy of the array itself. This lightweight 'copy'
            // as a List provides an easy way to find objects using the contains
            // method.
            List attributes = Arrays.asList(attributeNamesToDisplay);
            List attributesToShowList = new ArrayList();

            for (int i = 0; i < compositeAttributes.length; i++) {
                if (attributes.contains(compositeAttributes[i])) {
                    // This attribute should be visible.
                    attributesToShowList.add(compositeAttributes[i]);
                }
            }
            attributesToShow = createArray(attributesToShowList);
        }

        getAttributesComposite().setVisible(attributesToShow);
        setVisible((attributesToShow != null) &&
                (attributesToShow.length > 0));
    }

    /**
     * Create a String array from the list of items passed in.
     *
     * @param list the list to create a sorted array of strings from.
     * @return a String array from the list of items passed in.
     */
    private String[] createArray(List list) {
        String[] array = null;
        if (list != null && list.size() > 0) {
            array = new String[list.size()];
            array = (String[]) list.toArray(array);
        }
        return array;
    }

    /**
     * Return the proxy element wrapped in this composite.
     * @return the proxy element.
     */
    private ODOMElement getOdomElement() {
        return odomElement;
    }

    // javadoc inherited.
    public void dispose() {
        removePropertyChangeListener(ipropertyChangeListener);
        odomSelectionManager.removeSelectionListener(odomSelectionListener, filter);
        attributesComposite = null;
        ipropertyChangeListener = null;
        labelLine  = null;

        super.dispose();
    }


    /**
     * implemented by delegation to the contained attributesComposite
     */
    // rest of javadoc inherited.
    public boolean setFocus(XPath path) {
        if (attributesComposite!=null) {
            return attributesComposite.setFocus(path);
        } else {
            return false;
        }
    }

    // javadoc inherited
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        setEnabled(enabled, getChildren());
    }

    /**
     * Recursively enables/disables the specified controls.
     *
     * @param enabled the new status
     * @param controls the controls to start with
     */
    private void setEnabled(final boolean enabled, final Control[] controls) {
        for (int i = 0; i < controls.length; i++) {
            if (controls[i] instanceof Composite) {
                setEnabled(enabled, ((Composite) controls[i]).getChildren());
            }
            controls[i].setEnabled(enabled);
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

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 22-Jul-04	4942/1	adrian	VBM:2004060203 Fixed problems with setting visibility of SectionDetailsComposite

 17-May-04	4231/1	tom	VBM:2004042704 Fixedup the 2004032606 change

 22-Mar-04	3509/1	byron	VBM:2004031610 Link Text and Style Class boxes in dissecting pane attributes remove unused buttons

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 17-Feb-04	2988/3	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 17-Feb-04	3066/1	byron	VBM:2004021707 PackingLayout is creating invisible controls in AttributesComposite

 16-Feb-04	2891/10	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - added comments and renamed methods

 16-Feb-04	2891/8	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - scrollbar fixed

 13-Feb-04	2891/6	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management

 06-Feb-04	2887/1	byron	VBM:2004020603 Format Attributes View throwing exceptions

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 02-Feb-04	2707/3	byron	VBM:2003121506 Eclipse PM Layout Editor: hide units control if element is a grid

 02-Feb-04	2707/1	byron	VBM:2003121506 Eclipse PM Layout Editor: Format Attributes View: Row Page

 29-Jan-04	2752/5	byron	VBM:2004012602 Fixed test case and reduced garbage creation

 28-Jan-04	2752/3	byron	VBM:2004012602 Addressed xxxlinkText enablement for dissecting panes and other bug fixes

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 23-Jan-04	2728/1	byron	VBM:2004012301 Miscellaneous bug fixes cell iterations/listeners/enablement/etc

 22-Jan-04	2540/1	byron	VBM:2003121505 Added main formats attribute page

 ===========================================================================
*/
