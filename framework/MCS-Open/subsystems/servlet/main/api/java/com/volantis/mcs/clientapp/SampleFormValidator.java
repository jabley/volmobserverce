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

package com.volantis.mcs.clientapp;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.volantis.mcs.widgets.services.WidgetServiceHelper;

/**
 * A sample form validator 
 */
class SampleFormValidator implements FormValidator {

	/**
	 * Expected names of fields to validate
	 */
	private final static String NAME_FIELDNAME = "name";
	private final static String PIN_FIELDNAME = "pin";
	
	/**
	 * A pattern matching a string at least 4 characters in length 
	 */
	private final static Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]{4,}");
	
	/**
	 * A pattern matching exactly 6 digits
	 */
	private final static Pattern PIN_PATTERN = Pattern.compile("\\d{6}");

	public ValidationResult validate(Map formData) {
		ValidationResult result = new ValidationResult();
		
		boolean nameOK = false;
		boolean pinOK = false;
		
		nameOK = validateField(formData, result.messages, NAME_FIELDNAME, NAME_PATTERN);
		pinOK = validateField(formData, result.messages, PIN_FIELDNAME, PIN_PATTERN);

		result.validationSuccessful = nameOK && pinOK;
		return result;
	}
	
	private boolean validateField(Map formData, Map messages, 
			String fieldName, Pattern pattern) {
		String value = WidgetServiceHelper.getParameter(formData, fieldName);
		if (null != value || value.length() == 0) {
			Matcher m = pattern.matcher(value);
			if (m.matches()) {
				return true;
			} else {
				messages.put(fieldName, ClientServiceHelper.getLocalizedString(
						"validator."+fieldName+".incorrect"));
				return false;
			}
		} else {
			messages.put(fieldName, ClientServiceHelper.getLocalizedString(
					"validator."+fieldName+".empty"));
			return false;
		}
	}

}
