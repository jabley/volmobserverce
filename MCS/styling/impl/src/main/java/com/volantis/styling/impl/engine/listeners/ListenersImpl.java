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

package com.volantis.styling.impl.engine.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Base implementation of {@link Listeners}.
 */
abstract class ListenersImpl
        implements Listeners {

    private List listeners;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected ListenersImpl(Listeners value) {
        ListenersImpl impl = (ListenersImpl) value;
        listeners = new ArrayList(impl.listeners);
    }

    /**
     * Protected constructor for sub classes.
     */
    protected ListenersImpl() {
        listeners = new ArrayList();
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableListeners createImmutableListeners() {
        return new ImmutableListenersImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableListeners createMutableListeners() {
        return new MutableListenersImpl(this);
    }

    /**
     * Implementation of accessor for immutable classes.
     */
    public void iterate(ListenerIteratee iteratee) {
        final int length = listeners.size();
        for (int i = 0; i < length; i += 1) {
            final Listener listener = (Listener) listeners.get(i);
            iteratee.next(listener);
        }
    }

    /**
     * Implementation of mutator.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     */
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Implementation of mutator.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     */
    public Iterator iterator() {
        return listeners.iterator();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
