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

package com.volantis.mcs.layouts;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.mcs.layouts.iterators.IteratorConstraintFactoryMock;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;

/**
 * Base class for all test cases that test classes that activate
 * {@link FormatIterator}s.
 */
public abstract class FormatIteratorActivatorTestAbstract
        extends TestCaseAbstract {

    protected IteratorConstraintFactoryMock iteratorConstraintFactoryMock;
    protected CanvasLayoutMock canvasLayoutMock;
    protected static final boolean FIXED = true;
    protected static final boolean VARIABLE = false;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        canvasLayoutMock = new CanvasLayoutMock("canvasLayoutMock", expectations);

        iteratorConstraintFactoryMock = new IteratorConstraintFactoryMock(
                "iteratorConstraintFactoryMock", expectations);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/