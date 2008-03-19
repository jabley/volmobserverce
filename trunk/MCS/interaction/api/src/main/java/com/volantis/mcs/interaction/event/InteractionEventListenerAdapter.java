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

import java.util.List;

/**
 * Adapter to aid in writing an event listener.
 *
 * <p>Unless they are overridden each method will pass the event to one of the
 * more general methods until it reaches the most general one which is
 * {@link #interactionEvent}. This makes it very easy to respond to all
 * events, or all list events simply by overriding the appropriate general
 * method.</p>
 */
public class InteractionEventListenerAdapter
        implements InteractionEventListener {

    /**
     * Invoked for all events unless a more specific method has been overridden.
     *
     * @param event The event that occurred.
     */
    protected void interactionEvent(InteractionEvent event) {
    }

    /**
     * Invoked for all list events unless a more specific method has been
     * overridden.
     *
     * <p>If this is not overridden then it will invoke
     * {@link #interactionEvent}.</p>
     *
     * @param event The event that occurred.
     */
    protected void listEvent(ListEvent event) {
        interactionEvent(event);
    }

    /**
     * Invoked when a bean proxy's property has changed.
     *
     * <p>If this is not overridden then it will invoke
     * {@link #interactionEvent}.</p>
     *
     * @param event The event that occurred.
     */
    public void propertySet(BeanPropertyChangedEvent event) {
        interactionEvent(event);
    }

    /**
     * Invoked when the underlying model object associated with a proxy
     * has changed.
     *
     * <p>If this is not overridden then it will invoke
     * {@link #interactionEvent}.</p>
     *
     * @param event The event that occurred.
     */
    public void proxyModelChanged(ProxyModelChangedEvent event) {
        interactionEvent(event);
    }

    /**
     * Invoked when an item is added to a list proxy.
     *
     * <p>If this is not overridden then it will invoke {@link #listEvent}.</p>
     *
     * @param event The event that occurred.
     */
    public void addedToList(InsertedIntoListEvent event) {
        listEvent(event);
    }

    /**
     * Invoked when an item is removed from a list proxy.
     *
     * <p>If this is not overridden then it will invoke {@link #listEvent}.</p>
     *
     * @param event The event that occurred.
     */
    public void removedFromList(RemovedFromListEvent event) {
        listEvent(event);
    }

    /**
     * Invoked when an atomic operation generated more than one event.
     *
     * <p>If this is not overridden then it will iterate over the events
     * invoking the appropriate method on this listener.</p>
     *
     * @param event The event that occurred.
     */
    public void compositeEvent(CompositeEvent event) {
        List events = event.getEvents();
        for (int i = 0; i < events.size(); i++) {
            InteractionEvent nestedEvent = (InteractionEvent) events.get(i);
            nestedEvent.dispatch(this);
        }
    }

    /**
     * Invoked when the read only state of an item changes.
     *
     * <p>If this is not overridden then it will invoke {@link #listEvent}.</p>
     *
     * @param event The event that occurred.
     */
    public void readOnlyStateChanged(ReadOnlyStateChangedEvent event) {
        interactionEvent(event);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/5	pduffin	VBM:2005101811 Added basic list operations

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
