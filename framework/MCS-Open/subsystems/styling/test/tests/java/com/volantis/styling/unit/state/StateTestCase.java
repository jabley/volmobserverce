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

package com.volantis.styling.unit.state;

import com.volantis.styling.impl.state.MutableStateRegistry;
import com.volantis.styling.impl.state.StateFactory;
import com.volantis.styling.impl.state.StateIdentifier;
import com.volantis.styling.impl.state.StateInstanceFactoryMock;
import com.volantis.styling.impl.state.StateMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.ExpectationBuilder;

/**
 * Tests the state infrastructure as a whole as all the pieces are
 * interrelated.
 */
public class StateTestCase
        extends TestCaseAbstract {

    private ExpectationBuilder expectations;
    private StateInstanceFactoryMock instanceFactoryMock;
    private StateMock state1Mock;
    private StateMock state2Mock;
    private StateFactory factory;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        expectations = mockFactory.createOrderedBuilder();
        instanceFactoryMock = new StateInstanceFactoryMock(
                "instanceFactory", expectations);
        state1Mock = new StateMock("state1", expectations);
        state2Mock = new StateMock("state1", expectations);

        factory = StateFactory.getDefaultInstance();
    }

    public void testRegisterMultiple() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        MutableStateRegistry registry = factory.createStateRegistry();

        // register the same factory multiple times.
        StateIdentifier identifier1 = registry.add(instanceFactoryMock);
        StateIdentifier identifier2 = registry.add(instanceFactoryMock);

        assertNotSame("Identifiers must be different",
                      identifier1, identifier2);
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
