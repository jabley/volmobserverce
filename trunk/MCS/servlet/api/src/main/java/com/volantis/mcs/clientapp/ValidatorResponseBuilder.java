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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

/**
 * Builder of AJAX response for the Validator widget
 *
 * Uses a FormValidator to do the actual validation  
 */
class ValidatorResponseBuilder extends SampleAppResponseBuilder {

	private FormValidator validator;
	
	public ValidatorResponseBuilder(FormValidator validator) {
		this.validator = validator;
	}
	
	protected void writeBodyContents(Map params, PrintWriter out)
			throws IOException {
		
		FormValidator.ValidationResult result = validator.validate(params);
		
		if (result.validationSuccessful) {
			out.print("<response:validation result=\"passed\"/>");
		} else {
			out.print("<response:validation result=\"failed\">");
			
			out.print("<response:message>");
			out.print(ClientServiceHelper.getLocalizedString("validator.failed"));
			out.print("</response:message>");
			
			Iterator it = result.messages.keySet().iterator();
			while (it.hasNext()) {
				String field = (String)it.next();
				out.print("<response:field ref=\"");
				out.print(field);
				out.print("\">");
				
				out.print("<response:message>");
				out.print((String)result.messages.get(field));
				out.println("</response:message>");
				
				out.println("</response:field>");
			}
			
			out.println("</response:validation>");
		}
	}	
}
