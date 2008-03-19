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

package com.volantis.styling.impl.compiler;

import com.volantis.styling.impl.engine.listeners.MutableListeners;
import com.volantis.styling.impl.engine.matchers.MatcherBuilderContext;
import com.volantis.styling.impl.engine.matchers.composites.DepthChangeListener;
import com.volantis.styling.impl.state.MutableStateRegistry;
import com.volantis.styling.impl.state.StateIdentifier;
import com.volantis.styling.impl.state.StateInstanceFactory;

public class MatcherBuilderContextImpl
 implements MatcherBuilderContext {

    private final MutableStateRegistry stateRegistry;

    private final MutableListeners listeners;

    public MatcherBuilderContextImpl(MutableStateRegistry stateRegistry,
                                     MutableListeners listeners) {
        this.stateRegistry = stateRegistry;
        this.listeners = listeners;
    }

    public void addDepthChangeListener(DepthChangeListener listener) {
        listeners.addListener(listener);
    }

    public StateIdentifier register(StateInstanceFactory factory) {
        return stateRegistry.add(factory);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
