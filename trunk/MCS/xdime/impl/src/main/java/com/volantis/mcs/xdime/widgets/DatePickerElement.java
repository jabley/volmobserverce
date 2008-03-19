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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.widgets.attributes.DatePickerAttributes;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.gallery.GalleryElement;
import com.volantis.synergetics.localization.ExceptionLocalizer;


/**
 * DatePicker widget element
 */
public class DatePickerElement extends WidgetElement implements Dismissable, Loadable {

    /**
     * Used to retrieve localized exception messages.
     */
    protected static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    GalleryElement.class);
    
    public DatePickerElement(XDIMEContextInternal context) {
        super(WidgetElements.DATE_PICKER, context);
        protocolAttributes = new DatePickerAttributes();
    }
    
    /**
     * Set's load attributes used for loading configuration of widget
     */
    public void setLoadAttributes(LoadAttributes attributes) {
        getDatePickerAttributes().setLoadAttributes(attributes);
    }

    /**
     * Get's load attributes used for loading configuration of widget
     */
    public DatePickerAttributes getDatePickerAttributes() {
        return ((DatePickerAttributes) protocolAttributes);
    }
    
//  Javadoc inherited
    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {
        
        // Process this DatePicker element for default.
        context.processElementForDefault(this);
        
        return super.callOpenOnProtocol(context, attributes);
    }
    
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        DatePickerAttributes datePickerAttributes = (DatePickerAttributes) protocolAttributes;
        
        /**
         * Input fields into which selected date will be typed
         */
        datePickerAttributes.setInputField(attributes.getValue("", "inputField"));
        datePickerAttributes.setDayInputField(attributes.getValue("", "dayInputField"));
        datePickerAttributes.setMonthInputField(attributes.getValue("", "monthInputField"));
        datePickerAttributes.setYearInputField(attributes.getValue("", "yearInputField"));
    }

    /**
     * Return id of a dismissable widget
     */
    public String getDismissableId() {
        return protocolAttributes.getId();
    }
}
