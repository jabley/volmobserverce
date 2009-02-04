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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.events.EventRegistrar;
import com.volantis.mcs.xdime.events.EventsTable;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * XHTML V2 Anchor element object.
 */
public class AnchorElement extends XHTML2Element {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(AnchorElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(AnchorElement.class);

    /**
     * Defines the DOM Level 2 focus and blur events for anchor. In HTML 4 the
     * focus events apply for the form fields as well, but in this case the
     * form fields use the releveant XForms events instead.
     */
    private static final EventsTable EVENTS = new EventsTable() {
        public void registerEvents(EventRegistrar registrar) {
            registrar.registerEvent("focus", EventConstants.ON_FOCUS);
            registrar.registerEvent("blur", EventConstants.ON_BLUR);
        }
    };

    public AnchorElement(XDIMEContextInternal context) {
        super(XHTML2Elements.A, context);

        protocolAttributes = new AnchorAttributes();

        // Add anchor specific events.
        EVENTS.registerEvents(eventMapper);
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

    	AnchorAttributes anchorAttributes = (AnchorAttributes) protocolAttributes;

    	String id = anchorAttributes.getId();
    	
    	if (id != null) {
    		String keyValue = (String) context.getIdToAccessKeyMap().get(id);
    		
    		if (keyValue != null) {
    			anchorAttributes.setShortcut(new LiteralTextAssetReference(keyValue));
    		}
    	}
    	
        VolantisProtocol protocol = getProtocol(context);

        protocol.writeOpenAnchor(anchorAttributes);
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    protected void callCloseOnProtocol(XDIMEContextInternal context)
        throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);

        try {
            protocol.writeCloseAnchor((AnchorAttributes)protocolAttributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);

            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Oct-05	9673/5	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 21-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
