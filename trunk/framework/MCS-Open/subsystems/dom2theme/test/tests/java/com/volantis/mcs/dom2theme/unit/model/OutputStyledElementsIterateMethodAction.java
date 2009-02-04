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
package com.volantis.mcs.dom2theme.unit.model;

import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;

/**
 * An abstract method action to simplify mocking
 * {@link com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList#iterate}
 */
public abstract class OutputStyledElementsIterateMethodAction
        implements MethodAction {

    // Javadoc inherited.
    public Object perform(MethodActionEvent event) throws Throwable {
        OutputStyledElementIteratee iteratee =
                (OutputStyledElementIteratee) event.getArgument(
                        OutputStyledElementIteratee.class);
        iterate(iteratee);

        return null; // todo: later: this should be MethodVoidAction?
    }

    /**
     * Implement this with the mock implementation of iterate.
     *
     * @param iteratee the iteratee to process each mocked element.
     */
    public abstract void iterate(OutputStyledElementIteratee iteratee);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/2	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
