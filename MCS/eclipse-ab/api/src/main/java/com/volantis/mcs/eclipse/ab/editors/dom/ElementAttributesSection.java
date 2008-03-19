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
/*
----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 *
----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.ab.core.AttributesComposite;
import com.volantis.mcs.eclipse.ab.core.AttributesCompositeBuilder;
import com.volantis.mcs.eclipse.common.AttributesDetails;
import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.controls.ActionableHandler;
import com.volantis.mcs.eclipse.controls.ControlUtils;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Section;
import org.jdom.Attribute;

import java.util.List;


/**
 * Creates an ElementAttributesSection for use in component editors.
 */
public class ElementAttributesSection extends FormSection
        implements XPathFocusable {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ElementAttributesSection.class);

    /**
     * The display area control.
     */
    private Composite displayArea;

    /**
     * The attribute details of the ODOM element.
     */
    private final AttributesDetails attributesDetails;

    /**
     * The ODOMElement for the ElementAttributesSection.
     */
    private final ODOMElement odomElement;

    /**
     * The AttributesComposite for this page.
     */
    private AttributesComposite attributesComposite;

    /**
     * The project with the AttributesCompositeBuilder uses.
     */
    private final IProject project;

    /**
     * The change listener for the ODOM element.
     */
    private ODOMChangeListener odomChangeListener;

    /**
     * The IPropertyChangeListener for the AttributesComposite.
     */
    private IPropertyChangeListener ipropertyChangeListener;


    /**
     * Creates an ElementAttributesSection form for using in component editors.
     * @param parent the parent composite
     * @param style the style for the parent
     * @param project the project to use. Cannot be null.
     * @param odomElement the ODOMElement. Note that the element must already
     * have initialized all the possible attributes for the element. Cannot
     * be null.
     * @param title the title for the form section
     * @param message the message for the form section
     * @param attributesDetails the attribute details. Cannot be null.
     * @param handler the actionable handler.
     */
    public ElementAttributesSection(Composite parent, int style,
                                    IProject project,
                                    ODOMElement odomElement, String title,
                                    String message,
                                    AttributesDetails attributesDetails,
                                    ActionableHandler handler) {
        super(parent, style);
        if (project == null) {
            throw new IllegalArgumentException(
                        "Cannot be null:  project."); //$NON-NLS-1$
        }
        if (odomElement == null) {
            throw new IllegalArgumentException(
                        "Cannot be null: odomElement."); //$NON-NLS-1$
        }
        if (attributesDetails == null) {
            throw new IllegalArgumentException(
                        "Cannot be null: attributesDetails."); //$NON-NLS-1$
        }
        this.odomElement = odomElement;
        this.project = project;
        this.attributesDetails = attributesDetails;
        createDisplayArea(handler, title, message, style);
    }

    /**
     * Creates the display area of the form.
     * @param handler the actionable handler.
     */
    private void createDisplayArea(ActionableHandler handler,
                                   String title, String message, int style) {
        Section section =
                SectionFactory.createSection(this, style, title, message);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        section.setLayoutData(data);

        this.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        displayArea = new Composite(section, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;

        displayArea.setLayout(gridLayout);
        data = new GridData(GridData.FILL_HORIZONTAL);
        displayArea.setLayoutData(data);
        displayArea.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        AttributesCompositeBuilder builder =
                AttributesCompositeBuilder.getSingleton();
        attributesComposite = builder.
                buildAttributesComposite(displayArea, attributesDetails,
                        project, handler);
        attributesComposite.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        final GridLayout layout = (GridLayout) attributesComposite.getLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        enableDisplayArea();

        section.setClient(displayArea);

        if (odomElement.getName().equals(ODOMElement.NULL_ELEMENT_NAME)) {
            disableDisplayArea();
        }

        ipropertyChangeListener = new IPropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent changeEvent) {
                updateODOMAttributes(changeEvent);
            }
        };

        attributesComposite.addPropertyChangeListener(ipropertyChangeListener);

        odomChangeListener = new ODOMChangeListener() {
            // javadoc inherited
            public void changed(ODOMObservable node, ODOMChangeEvent event) {
                // Make sure we don't try updating if the widget has been
                // disposed (we often get updates to selection during
                // closure of a window)
                if (!attributesComposite.isDisposed()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("ODOMChangeEvent recieved.");
                    }

                    updateDisplayArea(event);
                }
            }
        };

        odomElement.addChangeListener(odomChangeListener);
    }

    /**
     * Enable displayArea and populate the AttributesComposite with
     * values from the odomElement.
     */
    private void enableDisplayArea() {
        ControlUtils.setEnabledHierarchical(displayArea, true);
        List attributes = odomElement.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attr = (Attribute) attributes.get(i);
            attributesComposite.setAttributeValue(attr.getName(),
                    attr.getValue());
        }
    }

    /**
     * Updates the ODOMElement's attributes after a PropertyChangeEvent from
     * the form.
     * @param changeEvent the PropertyChangeEvent
     */
    private void updateODOMAttributes(PropertyChangeEvent changeEvent) {
        String name = changeEvent.getProperty();
        Object newValue = changeEvent.getNewValue();
        if (newValue != null) {
            odomElement.removeChangeListener(odomChangeListener);
            odomElement.setAttribute(name, newValue.toString());
            odomElement.addChangeListener(odomChangeListener);
        }
    }

    /**
     * Updates the display area of the controls after an ODOM element has
     * changed. If the ODOMElement was removed, clear the form's fields and
     * disable the form. Otherwise, enable the form and update the form with
     * the new attribute values.
     *
     * @param event the ODOMChangeEvent
     */
    private void updateDisplayArea(ODOMChangeEvent event) {
        attributesComposite.removePropertyChangeListener(
                ipropertyChangeListener);

        if (event.getChangeQualifier() == ChangeQualifier.NAME &&
                event.getNewValue() == ODOMElement.NULL_ELEMENT_NAME) {
            // The element has been removed
            disableDisplayArea();
        } else if (event.getChangeQualifier() ==
                ChangeQualifier.ATTRIBUTE_VALUE) {
            // Attribute modified
            Attribute attribute = (Attribute) event.getSource();

            updateAttribute(attribute);

        } else if (event.getChangeQualifier() == ChangeQualifier.HIERARCHY &&
                event.getSource() instanceof Attribute &&
                event.getNewValue() != null) {
            // Attribute modified
            Attribute attribute = (Attribute) event.getSource();

            updateAttribute(attribute);
        } else if (event.getChangeQualifier() == ChangeQualifier.NAME &&
                !event.getNewValue().equals(ODOMElement.NULL_ELEMENT_NAME) &&
                event.getOldValue().equals(ODOMElement.NULL_ELEMENT_NAME)) {
            // A new element has been added
            enableDisplayArea();
        }

        attributesComposite.
                addPropertyChangeListener(ipropertyChangeListener);
    }

    /**
     * Disable the displayArea setting all attribute values to "".
     */
    private void disableDisplayArea() {
        String[] attributeNames = attributesComposite.getAttributeNames();
        for (int i = 0; i < attributeNames.length; i++) {
            attributesComposite.setAttributeValue(attributeNames[i], "");
        }
        ControlUtils.setEnabledHierarchical(displayArea, false);
    }

    /**
     * Update the given Attribute in the AttributesComposite if necessary.
     * @param attribute The Attribute
     */
    private void updateAttribute(Attribute attribute) {
        // First check that the element has not been nulled because of
        // a remove.
        if (!ODOMElement.NULL_ELEMENT_NAME.equals(odomElement.getName())) {
            // If the AttributesComposite is currently disabled then
            // reenable and populate all its values. Otherwise just set
            // the specific attribute.
            if (!displayArea.isEnabled()) {
                enableDisplayArea();
            } else {
                attributesComposite.setAttributeValue(attribute.getName(),
                        attribute.getValue());
            }
        }
    }

    //javadoc inherited
    protected Control getDisplayArea() {
        return displayArea;
    }

    // javadoc inherited
    public boolean setFocus(XPath path) {
        return attributesComposite.setFocus(path);
    }

    /**
     * Get the AttributesComposite that underlies this
     * ElementAttributesSection.
     * @return The AttributesComposite for this ElementAttributesSection.
     */
    public AttributesComposite getAttributesComposite() {
        return attributesComposite;
    }

    /**
     * Get the ODOMElement that this ElementAttributesSection is based on.
     * @return The ODOMElement associated with this ElementAttributesSection.
     */
    public ODOMElement getODOMElement() {
        return odomElement;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 26-Apr-05	7861/1	philws	VBM:2005042508 Port exception fixes from 3.3

 26-Apr-05	7846/1	philws	VBM:2005042508 Prevent erroneous updates causing exceptions during Layout Editor closure

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 27-Sep-04	5652/1	adrianj	VBM:2004090813 Correct enable/disable of sections when editing image assets

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 17-May-04	4231/1	tom	VBM:2004042704 Fixedup the 2004032606 change

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 17-Feb-04	3066/1	byron	VBM:2004021707 PackingLayout is creating invisible controls in AttributesComposite

 16-Feb-04	2891/3	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - added comments and renamed methods

 13-Feb-04	2891/1	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management

 03-Feb-04	2820/3	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 22-Jan-04	2540/1	byron	VBM:2003121505 Added main formats attribute page

 29-Dec-03	2258/1	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 17-Dec-03	2213/5	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 15-Dec-03	2213/3	allan	VBM:2003121401 Commit to make available to Doug

 13-Dec-03	2208/1	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 12-Dec-03	2123/3	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 08-Dec-03	2157/7	pcameron	VBM:2003111302 Fixed column widths

 08-Dec-03	2157/5	pcameron	VBM:2003111302 Another tweak to ElementAttributesSection

 08-Dec-03	2157/3	pcameron	VBM:2003111302 Some tweaks to ElementAttributesSection

 08-Dec-03	2157/1	pcameron	VBM:2003111302 Added ElementAttributesSection

 ===========================================================================
*/
