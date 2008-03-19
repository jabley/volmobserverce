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
package com.volantis.styling.unit;

import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.styling.StatefulPseudoClassIteratee;

/**
 * An abstract method action to simplify mocking
 * {@link com.volantis.styling.StatefulPseudoClass#iterate}.
 */
public abstract class StatefulPseudoClassSetIterateMethodAction
        implements MethodAction {

    // Javadoc inherited.
    public Object perform(MethodActionEvent event) throws Throwable {

        StatefulPseudoClassIteratee iteratee =
                (StatefulPseudoClassIteratee) event.getArgument(
                        StatefulPseudoClassIteratee.class);

        iterate(iteratee);

        return null;
    }

    /**
     * Implement this with the mock implementation of iterate.
     *
     * @param iteratee the iteratee to process each mocked pseudo class.
     */
    public abstract void iterate(StatefulPseudoClassIteratee iteratee);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
