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
package com.volantis.mcs.dom;

import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;

/**
 * An abstract method action to simplify mocking
 * {@link Element#forEachChild}.
 */
public abstract class ElementForEachMethodAction implements MethodAction {

    // Javadoc inherited.
    public Object perform(MethodActionEvent event) throws Throwable {

        NodeIteratee iteratee = (NodeIteratee) event.getArgument(
                NodeIteratee.class);

        iterate(iteratee);

        return null;
    }

    /**
     * Implement this with the mock implementation of forEachChild.
     *
     * @param iteratee the iteratee to process each mocked child node.
     */
    public abstract void iterate(NodeIteratee iteratee);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jul-05	9067/1	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
