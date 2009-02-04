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
package com.volantis.mcs.xdime.events;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.ListenerEventRegistry;
import com.volantis.mcs.context.HandlerScriptRegistry;
import com.volantis.mcs.context.ListenerEvent;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.xdime.StylableXDIMEElement;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.xml.namespace.QName;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.namespace.ExpandedName;

/**
 * The XDIME 2 XML Events 'listener' element, used to link 'handler' elements
 * with the actual elements which require events.
 * <p>
 * See R1189 and the XML Events spec for more details.
 */
public class ListenerElement extends StylableXDIMEElement {

    /**
     * Initialise.
     * @param context
     */
    public ListenerElement(XDIMEContextInternal context) {
        super(XMLEventsElements.LISTENER, UnstyledStrategy.STRATEGY,
                context);
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        // The id of the element with which the event listener is to be
        // registered. Unlike the standard this is required.
        String observerAttribute = attributes.getValue("", "observer");
        if (observerAttribute == null) {
            throw new IllegalArgumentException(
                    "Listener element must have observer attribute");
        }

        // The URI for the resource that defines the action to be performed.
        // Unlike the standard this is required and can only be a fragment
        // identifier that references an element with a matching 'id'
        // attribute in the current document.
        String handlerAttribute = attributes.getValue("", "handler");
        if (handlerAttribute == null) {
            throw new IllegalArgumentException(
                    "Listener element must have handler attribute");
        }
        if (!handlerAttribute.startsWith("#")) {
            throw new IllegalArgumentException(
                    "Handler attribute " + handlerAttribute + " must be a " +
                    "fragment identifier (starts with #)");
        }

        // The type of event.
        // This differs from the current standard by representing this as a
        // QName, rather than an XML name. However, this is compatible with the
        // future direction of the standards.
        String eventAttribute = attributes.getValue("", "event");
        if (eventAttribute == null) {
            throw new IllegalArgumentException(
                    "Handler element must have event attribute");
        }

        QName qName = new ImmutableQName(eventAttribute);
        ExpandedName eventType = context.getExpressionContext().
                getNamespacePrefixTracker().resolveElementQName(qName);


        // Extract the id of the handler element from the handler attribute.
        // Since it must be a fragment id all we need to do is remove the #.
        String handlerId = handlerAttribute.substring(1);
        // Then we look up the handler to see if it has been defined. If it
        // has not this is an error.
        final MarinerPageContext pageContext = getPageContext(context);
        HandlerScriptRegistry scriptRegistry =
                pageContext.getHandlerScriptRegistry();
        ScriptAssetReference handlerScript =
                scriptRegistry.getScriptById(handlerId);
        if (handlerScript == null) {
            throw new IllegalArgumentException("Handler attribute " +
                    handlerAttribute + "must refer to a defined handler " +
                    "element");
        }

        // Note: we are not validating the event type or observer id at this
        // point, this validation will be done later when we have more context.

        ListenerEvent listenerEvent =
                new ListenerEvent(eventType, handlerScript);

        // Register the dom event listener by observer id.
        ListenerEventRegistry listenerEventRegistry =
                pageContext.getListenerEventRegistry();
        listenerEventRegistry.addListenerById(observerAttribute, listenerEvent);

        return XDIMEResult.SKIP_ELEMENT_BODY;
    }

}
