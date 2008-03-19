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

import java.util.HashMap;
import java.util.Map;

/**
 * Interface to be implemented by a form validator 
 */
interface FormValidator {
	
	/**
	 * Class providing validation result from the validator
	 */
	public static class ValidationResult {
		public boolean validationSuccessful;
		/**
		 * Maps form field names to error messages indicating reasons for 
		 * an unsuccessful validation
		 */
		public Map messages;
		
		public ValidationResult() {
			validationSuccessful = false;
			messages = new HashMap();
		}
	}
	
	/**
	 * Main validation method - returns true if form fields validate
	 * @param params is a field - value map of form fields   
	 */
	public ValidationResult validate(Map formData);
}
