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

package com.volantis.mcs.interaction.event;

import com.volantis.mcs.interaction.Proxy;

/**
 * The base of all interaction events.
 */
public abstract class InteractionEvent
        extends ProxyEvent {

    /**
     * Initialise.
     *
     * @param source The proxy that was the source of the event.
     */
    protected InteractionEvent(Proxy source, boolean originator) {
        super(source, originator);
    }

    /**
     * Dispatch the event to the appropriate listener method.
     *
     * @param listener The listener to which the event should be dispatched.
     */
    public abstract void dispatch(InteractionEventListener listener);

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        return (obj instanceof InteractionEvent);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
