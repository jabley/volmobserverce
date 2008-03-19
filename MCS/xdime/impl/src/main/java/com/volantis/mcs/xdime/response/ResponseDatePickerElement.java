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
 * (c) Volantis Systems Ltd 20067. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.volantis.mcs.protocols.response.attributes.ResponseDatePickerAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.widgets.ResponseElements;

/**
 * Implementation of AJAX resposne for DatePicker widget.  
 */
public class ResponseDatePickerElement extends WidgetResponseElement {

    /**
     * Initializes this element with given XDIME context.
     * 
     * @param context An internal XDIME context.
     */
    public ResponseDatePickerElement(XDIMEContextInternal context) {    	    	
        super(ResponseElements.DATE_PICKER, context);
        protocolAttributes = new ResponseDatePickerAttributes();
    }
        
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(
        XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {
     
        ResponseDatePickerAttributes responseDatePickerAttributes = (ResponseDatePickerAttributes) protocolAttributes;

        String currentDate = attributes.getValue("", "current-date");
        String rangeStart = attributes.getValue("", "range-start");
        String rangeEnd = attributes.getValue("", "range-end");
        
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null; 
        
        try {
        	if(currentDate != null) {	
        		date = dateFormater.parse(currentDate);
        		responseDatePickerAttributes.setCurrentDate(dateFormater.format(date));
        	}	
		} catch (ParseException e) {
			throw new XDIMEException("Invalid date format for current-date attribute.");			
		}

        try {
        	if(rangeStart != null) {	        	
        		date = dateFormater.parse(rangeStart);
        		responseDatePickerAttributes.setRangeStart(dateFormater.format(date));
        	}	
		} catch (ParseException e) {
			throw new XDIMEException("Invalid date format for range-start attribute.");			
		}

        try {
        	if(rangeEnd != null) {	        	
        		date = dateFormater.parse(rangeEnd);
        		responseDatePickerAttributes.setRangeEnd(dateFormater.format(date));
        	}	
		} catch (ParseException e) {
			throw new XDIMEException("Invalid date format for range-end attribute.");			
		}
		                
        super.initialiseElementSpecificAttributes(context, attributes);        
    }
}
