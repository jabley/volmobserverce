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
 * $Header: /src/voyager/com/volantis/mcs/protocols/EventAttributes.java,v 1.3 2002/03/18 12:41:16 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jan-02    Paul            VBM:2001122105 - Created to contain event
 *                              attributes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The events attributes.
 */
public class EventAttributes {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(EventAttributes.class);


    private ScriptAssetReference[] events;

    /**
     * Create a new <code>EventAttributes</code>.
     */
    public EventAttributes() {
        // This constructor delegates all its work to the reinitialise method,
        // no extra initialisation should be added here, instead it should be
        // added to the reinitialise method.
        reinitialise();
    }

    /**
     * Resets the internal state so it is equivalent (not necessarily identical)
     * to a new instance.
     */
    public void reset() {
        // Call this after calling super.reset to allow reinitialise to
        // override any inherited attributes.
        reinitialise();
    }

    /**
     * Reinitialise all the data members. This is called from the constructor and
     * also from reset.
     */
    private void reinitialise() {
        if (events == null) {
            events = new ScriptAssetReference[EventConstants.MAX_EVENTS];
        } else {
            int count = events.length;
            for (int i = 0; i < count; i += 1) {
                events[i] = null;
            }
        }
    }

    /**
     * Set the value of the event.
     *
     * @param event The new value of the event.
     */
    public void setEvent(int index, ScriptAssetReference event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting event " + index + " to " + event);
        }

        events[index] = event;
    }

    /**
     * Set the value of the event.
     *
     * @param event The new value of the event.
     */
    public void setEvent(int index, String event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting event " + index + " to " + event);
        }

        events[index] = new LiteralScriptAssetReference(event);
    }

    /**
     * Get the value of the event.
     *
     * @param index The index of the event.
     * @return The value of the event.
     */
    public ScriptAssetReference getEvent(int index) {
        return events[index];
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/
