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

import com.volantis.styling.impl.state.MutableStateRegistry;
import com.volantis.styling.impl.state.StateRegistry;

/**
 * Implementation of {@link MutableStateRegistry}.
 */
final class MutableStateRegistryImpl
        extends StateRegistryImpl
        implements MutableStateRegistry {

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public MutableStateRegistryImpl(StateRegistry value) {
        super(value);
    }

    /**
     * Public constructor for use by factory.
     */
    public MutableStateRegistryImpl() {
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
