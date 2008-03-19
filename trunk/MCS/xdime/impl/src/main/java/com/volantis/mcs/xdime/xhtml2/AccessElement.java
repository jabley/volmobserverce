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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * XHTML V2 Access element object.
 */

public class AccessElement extends XHTML2Element {

	/**
	 * Used for logging.
	 */
	private static final LogDispatcher logger = LocalizationFactory
			.createLogger(LinkElement.class);

	
	public AccessElement(XDIMEContextInternal context) {
		super(XHTML2Elements.ACCESS, UnstyledStrategy.STRATEGY, context);
	}

	// Javadoc inherited
	protected XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
			XDIMEAttributes attributes) throws XDIMEException {

		// get the value of the key and targetid attributes for the access

		String keyValue = attributes.getValue("", "key");
		String targetIdValue = attributes.getValue("", "targetid");

		if (logger.isDebugEnabled()) {
			logger.debug("Processing: key= " + keyValue + " targetid = "
					+ targetIdValue);
		}

		context.getIdToAccessKeyMap().put(targetIdValue, keyValue);
		
		return XDIMEResult.PROCESS_ELEMENT_BODY;
	}

	// Javadoc inherited
	protected void callCloseOnProtocol(XDIMEContextInternal context) {

		// NO-OP
	}

	
}
