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
import com.volantis.styling.impl.state.State;
import com.volantis.styling.impl.state.StateContainer;
import com.volantis.styling.impl.state.StateIdentifier;
import com.volantis.styling.impl.state.StateInstanceFactory;

public class StateContainerImpl
        implements StateContainer {

    /**
     * The registry that provides the state factories.
     */
    private final ImmutableStateRegistry registry;

    /**
     * The array of states contained within this.
     */
    private final State[] states;

    public StateContainerImpl(ImmutableStateRegistry registry, int size) {
        this.registry = registry;
        this.states = new State[size];
    }

    // Javadoc inherited.
    public State getState(StateIdentifier identifier) {
        StateIdentifierImpl impl = (StateIdentifierImpl) identifier;
        int index = impl.index;
        if (index < 0 || index >= states.length) {
            throw new IllegalArgumentException(
                    "Identifier " + identifier + " not from correct registry");
        }

        State state = states[index];
        if (state == null) {
            StateInstanceFactory factory = registry.getFactory(identifier);
            if (factory == null) {
                throw new IllegalStateException(
                        "Factory not found for identifier " + identifier +
                        " in registry " + registry);
            }

            state = factory.createState();
            states[index] = state;
        }

        return state;
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
