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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.events.XFormsFocusEvent;
import com.volantis.mcs.xdime.events.XFormsValueChangedEvent;
import com.volantis.mcs.xdime.xforms.model.SIItem;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.List;

/**
 * Abstract class which encapsulates the common behaviour of all XForms select
 * elements.
 */
public abstract class AbstractXFSelectElementImpl extends XFormsControlElement {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    AbstractXFSelectElementImpl.class);

    /**
     * The set of initial values that should be selected. This should be
     * compared with the values of the options that are defined in this
     * selector, and if the values match, that option is selected.
     */
    protected String[] initialValues;

    protected AbstractXFSelectElementImpl(
            ElementType type, XDIMEContextInternal context) {
        super(type, context);

        // Add xfselect/1 specific events.
        new XFormsValueChangedEvent().registerEvents(eventMapper);
        new XFormsFocusEvent().registerEvents(eventMapper);
    }

    // Javadoc inherited.
    protected void setInitialValue(XFormBuilder builder, String ref,
            String modelID) throws XDIMEException {

        // deal with any referenced items
        SIItem item = builder.getItem(ref, modelID);
        // the item could be null because the control could legitimately
        // refer to an non-existent model item.
        if (item != null) {
            item.setIsReferenced();

            String[] initialValues = item.getValue();

            if (initialValues != null) {
                if (initialValues.length > 1 &&
                        !((XFSelectAttributes) protocolAttributes).isMultiple()) {
                    // multiple values are only expected by multiple select
                    // elements so throw exception in any other case
                    throw new XDIMEException(exceptionLocalizer.format(
                            "xforms-invalid-initial-value",
                            this.getClass().getName()));
                }
            }
            // store the defined initial values until they can be compared with
            // the items that have been defined for this selector.
            this.initialValues = initialValues;
        }
    }

    // Javadoc inherited.
    protected void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {

        if (initialValues != null) {
            // determine which items are selected
            List options = ((XFSelectAttributes)protocolAttributes).getOptions();

            int numValues = initialValues.length;

            if (numValues > 1 && !(this instanceof XFSelectElementImpl)) {
                // multiple values are only allowed for multiple select elements
                throw new XDIMEException(exceptionLocalizer.format(
                        "xforms-invalid-initial-value",
                        this.getClass().getName()));
            }

            int numOptions = options.size();

            for (int i = 0; i < numValues; i++) {
                final String initialValue = initialValues[i];
                for (int j = 0; j < numOptions; j++) {
                    final SelectOption selectOption = (SelectOption)options.get(j);
                    if (initialValue.equals(selectOption.getValue())) {
                        selectOption.setSelected(true);

                        // This only seems to be of interest for single selectors -
                        // it's not used when processing multiple selectors.
                        ((XFFormFieldAttributes) protocolAttributes).
                                setInitial(initialValue);
                        fieldDescriptor.setInitialValue(initialValue);

                        // break out of the loop
                        j = numOptions;
                    }
                }
            }
        }
        // continue processing as normal.
        super.callCloseOnProtocol(context);
    }

    // Javadoc inherited.
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {
        // do nothing
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/4	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 02-Oct-05	9637/4	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
