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

package com.volantis.styling.impl.state.impl;

import com.volantis.styling.impl.state.ImmutableStateRegistry;
import com.volantis.styling.impl.state.MutableStateRegistry;
import com.volantis.styling.impl.state.StateIdentifier;
import com.volantis.styling.impl.state.StateInstanceFactory;
import com.volantis.styling.impl.state.StateRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of {@link StateRegistry}.
 */
abstract class StateRegistryImpl
        implements StateRegistry {


    /**
     * The list of factories.
     */
    private List factories = new ArrayList();

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected StateRegistryImpl(StateRegistry value) {
        StateRegistryImpl impl = (StateRegistryImpl) value;
        this.factories = new ArrayList(impl.factories);
    }

    /**
     * Protected constructor for sub classes.
     */
    protected StateRegistryImpl() {
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableStateRegistry createImmutableStateRegistry() {
        return new ImmutableStateRegistryImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableStateRegistry createMutableStateRegistry() {
        return new MutableStateRegistryImpl(this);
    }

    /**
     * @see MutableStateRegistry#add
     */
    public StateIdentifier add(StateInstanceFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("factory cannot be null");
        }
        StateIdentifier identifier = new StateIdentifierImpl(factories.size());
        factories.add(factory);
        return identifier;
    }

    // Javadoc inherited.
    public StateInstanceFactory getFactory(StateIdentifier identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("identifier cannot be null");
        }
        StateIdentifierImpl impl = (StateIdentifierImpl) identifier;
        int index = impl.index;
        if (index < 0 || index >= factories.size()) {
            throw new IllegalArgumentException(
                    "Identifier " + identifier + " not from this registry " +
                    this);
        }

        return (StateInstanceFactory) factories.get(index);
    }

    /**
     * Get a count of the number of factories.
     *
     * @return The number of factories.
     */
    protected int getFactoryCount() {
        return factories.size();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
