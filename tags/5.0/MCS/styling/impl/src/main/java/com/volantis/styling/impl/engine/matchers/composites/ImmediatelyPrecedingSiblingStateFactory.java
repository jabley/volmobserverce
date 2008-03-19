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

package com.volantis.styling.impl.engine.matchers.composites;

import com.volantis.styling.impl.state.State;
import com.volantis.styling.impl.state.StateInstanceFactory;

/**
 * Create instances of {@link PrecedingSiblingState}.
 */
public class ImmediatelyPrecedingSiblingStateFactory
        extends AbstractCompositeStateFactory {

    // Javadoc inherited.
    public CompositeState createCompositeState() {
        return new ImmediatelyPrecedingSiblingState();
    }

    // Javadoc inherited.
    public String getOperator() {
        return " + ";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Fixed issue with build

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
