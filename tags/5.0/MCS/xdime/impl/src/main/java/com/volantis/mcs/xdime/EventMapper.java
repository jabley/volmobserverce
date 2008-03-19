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
package com.volantis.mcs.xdime;

import com.volantis.mcs.context.ListenerEvent;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.xdime.events.EventRegistrar;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps XDIME 2 listener events into the protocol event attributes.
 * <p>
 * This class performs two functions.
 * <p>
 * Firstly, at element initialisation time, it allows the individual XDIME 2
 * elements to register which event types they support, and which
 * {@link com.volantis.mcs.protocols.EventConstants} they map to.
 * <p>
 * Secondly, as those elements are processing their attributes, it allows any
 * listener events which are registered for the element's id to added as events
 * into the protocol {@link EventAttributes}. If a listener is found which
 * tries to use an event type which the element has not registered, an
 * exception will be thrown and processing of the page will abort.
 *
 * todo: this could be done better - we should split the two functions above
 * For example we could define all the event mappings for each element type
 * in a shared structure the same way that we do for validation. That way we
 * could avoid having to create and set up an event mapper for each element
 * instance.
 */
public class EventMapper implements EventRegistrar {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(EventMapper.class);

    /**
     * A map of event type to event setter.
     */
    private final Map eventType2SetterMap = new HashMap();

    /**
     * The type of the element, used for error messages.
     */
    private final ElementType elementType;

    /**
     * The protocol event attributes that we map events into.
     */
    private EventAttributes eventAttributes;

    /**
     * Initialise.
     *
     * @param elementType the type of element this mapper is operating on
     *      behalf of.
     */
    public EventMapper(ElementType elementType) {
        this.elementType = elementType;
    }

    /**
     * Set the protocol event attributes that this class will map XDIME 2
     * events into.
     *
     * @param eventAttributes the protocol event attributes.
     */
    public void setEventAttributes(EventAttributes eventAttributes) {
        this.eventAttributes = eventAttributes;
    }

    // Javadoc inherited.
    public void registerEvent(String type, final int constant) {

        // Convert the string version of the type into an expanded name.
        // The events currently use no namespace so we use "" as the namespace.
        final ImmutableExpandedName eventType = new ImmutableExpandedName(
                "", type);

        eventType2SetterMap.put(eventType, new Mapper() {
            public void map(ScriptAssetReference script,
                    EventAttributes eventAttributes) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Setting event " + eventType +
                            " as constant " + constant + " for script " +
                            script);
                }

                eventAttributes.setEvent(constant, script);
            }
        });
    }

    /**
     * Add the provided listener event into the protocol event attributes.
     * <p>
     * It does this by adding the listener's handler script to the particular
     * protocol event attribute that relates to the listener's events type.
     * <p>
     * If no mapping has been established for the event type of the listener
     * (i.e. the element does not support the event type that the listener is
     * using), then an exception will be thrown and processing will abort.
     *
     * @param listenerEvent the event to map.
     */
    public void mapEventToAttributes(
            ListenerEvent listenerEvent) {

        ExpandedName eventType = listenerEvent.getEventType();
        Mapper mapper = (Mapper) eventType2SetterMap.get(eventType);
        if (mapper != null) {
            ScriptAssetReference handlerScript =
                    listenerEvent.getHandlerScript();
            mapper.map(handlerScript, eventAttributes);
        } else {
            // no event setter is available for this event type
            // so this event type is unknown for this element
            throw new RuntimeException("Event type " +
                    eventType + " is not supported for " +
                    elementType);
        }
    }

    /**
     * Private interface to allow mapping of script into event attributes.
     */
    private interface Mapper {

        /**
         * Map the provided script into the provided event attributes.
         * <p>
         * The implementation will provide the event constant required to
         * complete the mapping.
         *
         * @param script the script to map.
         * @param eventAttributes the event attributes to map into.
         */
        void map(ScriptAssetReference script, EventAttributes eventAttributes);

    }

}
