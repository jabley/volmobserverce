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

package com.volantis.mcs.interaction;

import com.volantis.mcs.interaction.event.InsertedIntoListEvent;
import com.volantis.mcs.interaction.event.RemovedFromListEvent;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.model.descriptor.ListClassDescriptor;

import java.util.List;

/**
 * A proxy for a list like object in the model.
 *
 * <p>This has a list of proxies and for each proxy that has a model object
 * there is a corresponding object in the underlying list. This means that
 * the index of a proxy in its list may be greater than the index of its
 * corresponding model object in its list.</p>
 */
public interface ListProxy
        extends ParentProxy {

    /**
     * Get the descriptor of the underlying object model.
     *
     * @return The descriptor of the underlying model object.
     */
    ListClassDescriptor getListClassDescriptor();

    /**
     * Get the size of the list of proxies.
     *
     * <p>The list of proxies may actually be bigger than the underlying model
     * list.</p>
     *
     * @return The size of the list of proxies.
     */
    int size();

    /**
     * Get the index of the proxy within this list proxy.
     *
     * @param proxy The proxy to locate.
     * @return The index of the proxy within this list proxy.
     * @throws IllegalArgumentException if the proxy could not be found.
     */
    int getItemProxyIndex(Proxy proxy);

    /**
     * Get the proxy for the item at the specified index within the list of
     * proxies.
     *
     * @param index The index of the proxy to retrieve within the list of
     *              proxies.
     * @return The proxy for the specified item.
     * @throws IndexOutOfBoundsException if the index is greater than or equal
     *                                   to the value returned by {@link #size}.
     */
    Proxy getItemProxy(int index);

    /**
     * Add an item proxy to the end of the list of proxies.
     *
     * <p>This will cause an {@link InsertedIntoListEvent} to be fired on this
     * proxy.</p>
     *
     * @return The newly created proxy.
     */
    Proxy addItemProxy();

    /**
     * Remove an item proxy.
     *
     * <p>This will cause a {@link RemovedFromListEvent} event to be fired on
     * this proxy.</p>
     *
     * @param itemProxy The proxy to remove.
     */
    void removeItemProxy(Proxy itemProxy);

    /**
     * Prepare an operation that will create and add a new proxy item to the
     * end of the list.
     *
     * @return The newly created operation.
     * @see Operation
     */
    Operation prepareCreateAndAddProxyItemOperation();

    /**
     * Prepare an operation that will add the specified proxy item to the
     * end of the list.
     *
     * @param itemProxy The proxy to add.
     * @return The newly created operation.
     * @see Operation
     */
    Operation prepareAddProxyItemOperation(Proxy itemProxy);

    /**
     * Prepare an operation that will add the specified model object to the
     * end of the list.
     *
     * @param modelItem The model object to add.
     * @return The newly created operation.
     * @see Operation
     */
    Operation prepareAddModelItemOperation(Object modelItem);

    /**
     * Prepare an operation that will remove the specified item.
     *
     * @param itemProxy The item to remove.
     * @return The newly created operation.
     * @see Operation
     */
    Operation prepareRemoveProxyItemOperation(Proxy itemProxy);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 31-Oct-05	9961/7	pduffin	VBM:2005101811 Committing restructuring

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 25-Oct-05	9961/5	pduffin	VBM:2005101811 Added basic list operations

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
