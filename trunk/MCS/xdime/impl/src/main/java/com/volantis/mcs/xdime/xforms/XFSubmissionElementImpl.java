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

import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.events.EventRegistrar;
import com.volantis.mcs.xdime.events.EventsTable;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;

/**
 * Describes how an xforms submission element should be processed.
 */
public class XFSubmissionElementImpl extends XFormsElement {

    /**
     * Defines the XForms xforms-submit event for submission.
     */
    private static final EventsTable EVENTS = new EventsTable() {
        public void registerEvents(EventRegistrar registrar) {
            registrar.registerEvent("xforms-submit", EventConstants.ON_SUBMIT);
        }
    };

    /**
     * Initialize a new instance.
     * @param context
     */
    public XFSubmissionElementImpl(XDIMEContextInternal context) {
        super(UnstyledStrategy.STRATEGY, XFormElements.SUBMISSION, context);

        // Add some protocol attributes so that events are initialised.
        this.protocolAttributes = new MCSAttributes() {};

        // Add xfsubmission specific events.
        EVENTS.registerEvents(eventMapper);
    }

    // Javadoc inherited.
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {

        String id = getAttribute(XDIMEAttribute.ID, attributes);
        String action = attributes.getValue("", "action");
        String method = attributes.getValue("", "method");

        // if action is not specified in XDIME, initialise it by empty value
        if(action == null) {
        	action = "";
        }        
        
        XFormBuilder builder = context.getXFormBuilder();
        EventAttributes events = protocolAttributes.getEventAttributes(false);
        // todo: add the events to the submission
        builder.addSubmission(id, events, action, method);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/2	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 02-Oct-05	9637/5	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
