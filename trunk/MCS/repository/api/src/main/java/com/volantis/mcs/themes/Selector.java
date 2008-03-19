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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes;

import com.volantis.mcs.model.copy.Copyable;
import com.volantis.mcs.model.validation.Validatable;

/**
 * A Java representation of a CSS selector that matches with certain elements.
 *
 * @mock.generate base="Validatable"
 */
public interface Selector
        extends Validatable, Copyable, ThemeVisitorAcceptor {

    /**
     * Invoke the appropriate method on the visitor.
     *
     * @param visitor The visitor to invoke.
     */
    void accept(SelectorVisitor visitor);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 30-Aug-05	9407/3	pduffin	VBM:2005083007 Added SelectorVisitor

 30-Aug-05	9407/1	pduffin	VBM:2005083007 Added SelectorVisitor

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
