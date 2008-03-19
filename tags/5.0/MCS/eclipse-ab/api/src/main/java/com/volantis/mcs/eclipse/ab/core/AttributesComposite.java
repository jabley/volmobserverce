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

package com.volantis.mcs.eclipse.ab.core;

import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.validation.Validator;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.controls.SecondaryControl;
import com.volantis.mcs.eclipse.controls.PackingLayout;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The composite of controls for a particular element.
 *
 * The AttributesComposite must allow listeners to listen for change events. The
 * AttributesComposite itself must listen for changes on each of its attribute
 * controls so that it can pass these changes on to any listeners registered on
 * it. The change event information must include the name of the attribute that
 * has changed, the old value and the new value.
 */
public class AttributesComposite
        extends Composite
        implements IPropertyChangeListener, XPathFocusable {

    /**
     * A list of all listeners that may be interested in property change
     * events.
     */
    private List listeners = null;

    /**
     * The array of attributes names.
     */
    private String[] attributeNames;

    /**
     * The name used to extract the getter/setter object from a control.
     */
    static final String ATTRIBUTE_ACCESSOR_KEY = "attributeAccessor"; //$NON-NLS-1$

    /**
     * The key used to get the Validator for the control.
     */
    static final String VALIDATOR_KEY = "validator"; //$NON-NLS-1$

    /**
     * The key used to get the label for a control.
     */
    static final String LABEL_KEY = "label"; //$NON-NLS-1$


    /**
     * Create an instance of the composite with the specified parent and
     * style. This constructor is package local because AttributesComposite
     * objects should only be creatable with an AttributesCompositeBuilder.
     *
     * @param parent                 the parent composite.
     * @param style                  the style.
     */
    AttributesComposite(Composite parent,
                        int style) {
        super(parent, style);
    }

    /**
     * Set the attribute using the attribute name and value.
     *
     * @param attributeName the attribute name.
     * @param value         the value to set the attribute to.
     */
    public void setAttributeValue(String attributeName, String value) {
        Control control = getControl(attributeName);
        if (control != null) {
            AttributeAccessor aa = (AttributeAccessor)
                    control.getData(ATTRIBUTE_ACCESSOR_KEY);
            if (aa != null) {
                aa.setValue(attributeName, value);
            }
        }
    }

    /**
     * Get the Validator associated with an attribute.
     * @param attributeName The name of the attribute.
     * @return the Validator associated with the named attribute
     * or null is there is none or attributeName was null.
     */
    public Validator getAttributeValidator(String attributeName) {
        Validator validator = null;

        if (attributeName != null) {

            Control control = getControl(attributeName);
            if (control != null) {
                validator = (Validator)
                        control.getData(VALIDATOR_KEY);
            }
        }

        return validator;
    }

    /**
     * Get the value for a named attribute in this AttributesComposite.
     * @param attributeName The name of the attribute.
     * @return The value of the attributeName or null if attributeName does not
     * exist.
     */
    public String getAttributeValue(String attributeName) {
        String value = null;
        Control control = getControl(attributeName);
        if (control != null) {
            AttributeAccessor aa = (AttributeAccessor)
                    control.getData(ATTRIBUTE_ACCESSOR_KEY);
            if (aa != null) {
                value = aa.getValue(attributeName);
            }
        }

        return value;
    }

    /**
     * Get the Collection of the names of all the attributes represented by
     * this AttributesComposite.
     * @return the Collection of attribute names for this AttributesComposite.
     */
    public String[] getAttributeNames() {
        return attributeNames;
    }

    /**
     * Set the Collection of the names of all the attributes represented by
     * this AttributesComposite. This method is package local because
     * it is for use only by AttributesCompositeBuilder.
     * @param attributeNames The Collection of attributes names for this
     * AttributesComposite.
     */
    void setAttributesNames(String[] attributeNames) {
        this.attributeNames = attributeNames;
    }

    /**
     * Set the focus using the XPath object.
     * @param path The XPath.
     */
    public boolean setFocus(XPath path) {
        // Look for one of the attributes associated with this
        // AttributesComposite at the end of the XPath.
        String xPathString = path.getExternalForm();
        boolean success = false;
        int i;
        for (i = 0; i < attributeNames.length && !success; i++) {
            success = xPathString.endsWith('@' + attributeNames[i]);
        }

        if (success) {
            success = setFocus(attributeNames[i - 1]);
        }

        return success;
    }

    /**
     * Set the focus using the attribute name to identify the control.
     *
     * @param attributeName the attribute name used to identify the control.
     * @return true If focus was accepted; false otherwise.
     */
    public boolean setFocus(String attributeName) {
        boolean ok = false;
        Control control = getControl(attributeName);
        if (control != null) {
            ok = control.setFocus();
        }

        return ok;
    }

    /**
     * Get the attribute control using the attribute name as a key.
     *
     * @param  attribute the attribute name used to locate the edit control.
     * @return the control associated with the specified attribute.
     */
    private Control getControl(String attribute) {
        return (Control) getData(attribute);
    }

    /**
     * Get the Label widget associated with a particular attribute.
     */
    private Control getLabel(String attribute) {
        Control control = null;
        Control[] children = getChildren();
        for (int i = 0; control == null && i < children.length; i++) {
            Control child = children[i];
            if (attribute.equals(child.getData(
                    AttributesComposite.LABEL_KEY))) {
                control = child;
            }
        }
        return control;
    }

    /**
     * Set the controls and labels corresponding to an array of named attributes
     * to visible/invisible.<p>
     *
     * We need to check for null labels because some controls do not have an
     * associated label (e.g. Buttons in the form of checkboxes).<p>
     *
     * See {@link com.volantis.mcs.eclipse.ab.core.AttributesCompositeBuilder#createAttributeLabel}
     *
     * @param attributes The names of the attributes whose controls and labels
     *                   to set visible on.
     */
    public void setVisible(String[] attributes) {
        String[] sortedAttributes = null;
        boolean makeAllInvisible = false;
        if (attributes == null || attributes.length <= 0) {
            makeAllInvisible = true;
        } else {
            // Clone and sort the attributes array so that the binary search
            // will work.
            sortedAttributes = new String[attributes.length];
            System.arraycopy(attributes, 0, sortedAttributes, 0, attributes.length);
            Arrays.sort(sortedAttributes, String.CASE_INSENSITIVE_ORDER);
        }

        for (int i = 0; attributeNames != null && i < attributeNames.length; i++) {
            Control control = getControl(attributeNames[i]);
            Control label = getLabel(attributeNames[i]);

            boolean visible;
            if (makeAllInvisible) {
                visible = false;
            } else {
                if (Arrays.binarySearch(sortedAttributes, attributeNames[i]) >= 0) {
                    visible = true;
                } else {
                    visible = false;
                }
            }
            control.setVisible(visible);
            if (label != null) {
                label.setVisible(visible);
            }
        }
    }

    /**
     * Set the controls and labels corresponding to named
     * attribute to enabled/disabled.
     *
     * @param attribute The name of the attribute whose
     * controls and labels to set enabled on.
     * @param enabled true if controls should be enabled, false
     * otherwise.
     */
    public void setEnabled(String attribute, boolean enabled) {
        Control control = getControl(attribute);
        if (control != null) {
            control.setEnabled(enabled);
            Control label = getLabel(attribute);
            if (label != null) {
                label.setEnabled(enabled);
            }
        }
    }

    /**
     * Enables or disables a secondary part of a multi-part control. For example,
     * TextDefinition requires the image menu button and browse button to be
     * disabled if the current selection is a DissectingPane.
     *
     * @param attribute the attribute to use to obtain the control that require
     *                  secondary enablement/disablement.
     * @param enabled   the enabled state.
     */
    public void setSecondaryEnabled(String attribute, boolean enabled) {
        Control control = getControl(attribute);
        if (control != null) {
            if (control instanceof SecondaryControl) {
                ((SecondaryControl)control).setSecondaryEnabled(enabled);
            }
        }
    }

    /**
     * Shows or hides a secondary part of a multi-part control.
     *
     * @param attribute the attribute to use to obtain the control that requires
     *                  secondary visibility adjustment.
     * @param visible   the visible state (true for visible, false otherwise).
     */
    public void setSecondaryVisible(String attribute, boolean visible) {
        Control control = getControl(attribute);
        if (control != null) {
            if (control instanceof SecondaryControl) {
                ((SecondaryControl)control).setSecondaryVisible(visible);
            }
        }
    }


    /**
     * Determine whether or not this AttributesComposite has any visible
     * controls. This method assumes that labels and their associated
     * controls have the same visibility status.
     * @return true If there are any visible attribute controls; false
     * otherwise.
     */
    public boolean hasVisibleControl() {
        Control control = null;
        Control[] children = getChildren();
        boolean hasVisibleControl = false;
        for (int i = 0; !hasVisibleControl && i < children.length; i++) {
            Control child = children[i];
            // Look for labels as controls may represent multiple attributes
            // and because of this don't know what attribute(s) they
            // correspond to.
            if (child.getData(AttributesComposite.LABEL_KEY) != null) {
                hasVisibleControl = control.isVisible();
            }
        }
        return hasVisibleControl;
    }

    /**
     * We have received a property change event. Simply forward on the event to
     * all listeners.
     *
     * @param event the property change event.
     */
    public void propertyChange(PropertyChangeEvent event) {
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                IPropertyChangeListener listener =
                        (IPropertyChangeListener) listeners.get(i);
                listener.propertyChange(event);
            }
        }
    }

    /**
     * Add a property change listener to this class.
     *
     * @param listener the listener to register property change events with.
     * @throws java.lang.IllegalArgumentException if the listener that is to be added is
     *                                  null.
     */
    public void addPropertyChangeListener(IPropertyChangeListener listener)
            throws IllegalArgumentException {
        if (listener == null) {
            throw new IllegalArgumentException(
                    "Listener added may not be null.");
        }
        if (listeners == null) {
            listeners = new ArrayList();
        }
        listeners.add(listener);
    }

    /**
     * Remove the property change listener from this class - if it has been
     * registered.
     *
     * @param listener the listener to remove.
     */
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        if ((listeners != null) && (listener != null)) {
            listeners.remove(listener);
        }
    }

    /**
     * An interface that provides access to the attribute values for controls.
     * This interface allows values to be obtained from controls that may
     * support multiple attributes.
     */
    interface AttributeAccessor {
        /**
         * Get the value associated with a given attribute.
         * @param attributeName The name of the attribute.
         * @return The value associated with the attribute or null if no
         * value was found.
         */
        public String getValue(String attributeName);

        /**
         * Set the value associated with a given attribute.
         * @param attributeName The name of the attribute.
         * @param value The value.
         */
        public void setValue(String attributeName, String value);
    }

    /**
     * Overridden to set the background of the composite and of all
     * its children to the specified color.
     * @param color the background color to set
     */
    public void setBackground(Color color) {
        if (color != null) {
            super.setBackground(color);
            Control[] children = this.getChildren();
            for (int i = 0; i < children.length; i++) {
                children[i].setBackground(color);
            }
        }
    }

    /**
     * Get the first column width (delegate to the layout and return its width
     * for the first column. This is then used to determine the maximum width
     * across sections and its complimentary method {@link #setFirstColumnWidth}
     * should then be called with this maximum width.
     *
     * @return the first column's width from the PackingLayout.
     */
    public int getFirstColumnWidth() {
        return ((PackingLayout)getLayout()).getFirstColumnWidth();
    }

    /**
     * Set the column width by delegating the set to the PackingLayout instance.
     * @todo this currently doesn't work insofar as the width set to insn't honoured for some or other reason. 
     * @param width the first column width.
     */
    public void setFirstColumnWidth(int width) {
        ((PackingLayout)getLayout()).setFirstColumnWidth(width);
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

 17-May-04	4231/3	tom	VBM:2004042704 Fixedup the 2004032606 change

 16-Feb-04	2891/8	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - added comments and renamed methods

 13-Feb-04	2891/6	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - take 2

 13-Feb-04	2891/4	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management

 06-Feb-04	2887/1	byron	VBM:2004020603 Format Attributes View throwing exceptions

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 02-Feb-04	2707/1	byron	VBM:2003121506 Eclipse PM Layout Editor: hide units control if element is a grid

 28-Jan-04	2752/2	byron	VBM:2004012602 Address issues from review

 22-Jan-04	2540/2	byron	VBM:2003121505 Added main formats attribute page

 29-Dec-03	2258/1	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 17-Dec-03	2213/1	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 08-Dec-03	2157/3	pcameron	VBM:2003111302 Some tweaks to ElementAttributesSection

 28-Nov-03	2013/6	allan	VBM:2003112501 Renamed setAttribute to setAttributeValue.

 28-Nov-03	2013/4	allan	VBM:2003112501 Support multi-attribute controls and XPathFocusable.

 27-Nov-03	2013/2	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 16-Nov-03	1909/4	allan	VBM:2003102405 All policy wizards.

 16-Nov-03	1909/2	allan	VBM:2003102405 Done Image Component Wizard.

 15-Nov-03	1825/3	byron	VBM:2003092601 Create generic policy property composite

 15-Nov-03	1825/1	byron	VBM:2003092601 Create generic policy property composite

 ===========================================================================
*/
