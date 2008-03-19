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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An event that is composed of a number of other events.
 *
 * <p>One of these is fired when an atomic set of operations is executed on
 * single proxy. It contains all the events that would have been fired by the
 * individual operations in order.</p>
 */
public class CompositeEvent
        extends InteractionEvent {

    private final List events;

    public CompositeEvent(Proxy source, List events, boolean originator) {
        super(source, originator);

        this.events = Collections.unmodifiableList(new ArrayList(events));
    }

    /**
     * Get the events contained within.
     *
     * @return The events contained within.
     */
    public List getEvents() {
        return events;
    }

    public void dispatch(InteractionEventListener listener) {
        listener.compositeEvent(this);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
