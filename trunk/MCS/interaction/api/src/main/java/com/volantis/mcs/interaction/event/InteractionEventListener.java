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

import java.util.EventListener;

/**
 * Listener for events fired by the interaction layer.
 *
 * <p>It is not recommended to implement this interface directly, rather you
 * should use the {@link InteractionEventListenerAdapter} as that has some nice
 * additional functionality and will protect the implementation against being
 * broken if additional methods are added to this.</p>
 *
 * @mock.generate
 */
public interface InteractionEventListener
        extends EventListener {

    /**
     * Invoked when a bean proxy's property has changed.
     *
     * @param event The event that occurred.
     */
    void propertySet(BeanPropertyChangedEvent event);

    /**
     * Invoked when the underlying model object associated with a proxy
     * has changed.
     *
     * @param event The event that occurred.
     */
    void proxyModelChanged(ProxyModelChangedEvent event);

    /**
     * Invoked when an item is added to a list proxy.
     *
     * @param event The event that occurred.
     */
    void addedToList(InsertedIntoListEvent event);

    /**
     * Invoked when an item is removed from a list proxy.
     *
     * @param event The event that occurred.
     */
    void removedFromList(RemovedFromListEvent event);

    /**
     * Invoked when an atomic operation generated more than one event.
     *
     * @param event The event that occurred.
     */
    void compositeEvent(CompositeEvent event);

    /**
     * Invoked when the read/write state of a proxy changes.
     *
     * @param event The event that occurred
     */
    void readOnlyStateChanged(ReadOnlyStateChangedEvent event);
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
