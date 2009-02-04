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
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.xdime.DataHandlingStrategy;
import com.volantis.mcs.xdime.StoreDataStrategy;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.events.EventRegistrar;
import com.volantis.mcs.xdime.events.EventsTable;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

public class XFSetValueElementImpl extends StyledXFormsElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(XFSetValueElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    XFSetValueElementImpl.class);

    /**
     * Defines the DOMActivate event for setvalue.
     */
    private static final EventsTable EVENTS = new EventsTable() {
        public void registerEvents(EventRegistrar registrar) {
            registrar.registerEvent("DOMActivate", EventConstants.DOM_ACTIVATE);
        }
    };

    /**
     * String which identifies the supported event type..
     */
    private static final String DOM_ACTIVATE_EVENT = "DOMActivate";

    /**
     * Name of the element which should be populated with the specified value.
     */
    private String name;

    /**
     * Value with which to populate the specified element.
     */
    private String value;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param context in which this element is being processed
     */
    public XFSetValueElementImpl(XDIMEContextInternal context) {
        this (context, new StoreDataStrategy());
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param context   in which this element is being processed
     * @param strategy  determines how to handle data
     */
    public XFSetValueElementImpl(XDIMEContextInternal context,
                                 DataHandlingStrategy strategy) {
        super(XFormElements.SETVALUE, strategy, context);

        // Add some protocol attributes so that events are initialised.
        this.protocolAttributes = new MCSAttributes() {};

        // Add xfsetvalue specific events.
        EVENTS.registerEvents(eventMapper);
    }

    // Javadoc inherited.
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        // do nothing.
    }

    // Javadoc inherited.
    protected XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
                                             XDIMEAttributes attributes)
            throws XDIMEException {

        // We handle the DOMActivate event differently to other events (it may
        // also be handled via the standard mechanism) and use it to populate
        // the value of the referenced element name. Need to confirm that the
        // event type is DOMActivate.
        String event = attributes.getValue(
                XDIMESchemata.XML_EVENTS_NAMESPACE, "event");
        if (!event.equals(DOM_ACTIVATE_EVENT)) {
            String localized = EXCEPTION_LOCALIZER.format(
                    "xforms-invalid-event-type",
                    new Object[]{"setvalue", event});
            throw new XDIMEException(localized);
        }

        // Capture the value with which the referenced element should
        // be populated.
        value = attributes.getValue("", XDIMEAttribute.VALUE.toString());
        name = attributes.getValue("",
                XDIMEAttribute.REF.toString());

        // Update the event attributes (just in case anyone has registered
        // for this event - not currently expected).
        EventAttributes events = protocolAttributes.getEventAttributes(false);
        context.getXFormBuilder().getCurrentModel().updateEventAttributes(events);
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited.
    protected void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {

        // If the element has character data in the body, then it replaces the
        // attribute value.
        String charData = getCharData();
        if (charData != null) {
            value = charData;
        }
        // Retrieve this element's parent from the stack (should only be
        // permitted as a child of a submit element).
        XDIMEElementInternal element = context.getCurrentElement();
        if (element instanceof XFSubmitElementImpl) {
            // If an action field is tagged to indicate that its query
            // parameters should contain an extra name value pair (used to
            // indicate things like which of two submit buttons was pressed to
            // trigger the submission) then HTMLActionFieldHandler appends this
            // name value pair using the name and initial value specified in
            // the field descriptor of the submit element. In order for our
            // parent submit element to set this flag, we should set the
            // initial value to the value specified.
            XFActionAttributes parentAttributes = ((XFActionAttributes)
                    ((XFSubmitElementImpl)element).getProtocolAttributes());
            parentAttributes.setName(name);
            parentAttributes.setInitial(value);
            parentAttributes.setValue(value);
            
            FieldDescriptor fd = parentAttributes.getFieldDescriptor();
            fd.setName(name);
            fd.setInitialValue(value);            
        } else {            
            String localizedException = EXCEPTION_LOCALIZER.format(
                    "xforms-invalid-setvalue-parent",
                    element.getElementType().getLocalName());
            LOGGER.error(localizedException);
            throw new XDIMEException(localizedException);
        }
    }
}
