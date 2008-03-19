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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.diselect;

import com.volantis.mcs.xdime.ElementOutputState;
import com.volantis.mcs.xdime.ElementOutputStateMock;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEAttributesImpl;
import com.volantis.mcs.xdime.XDIMEContextInternalMock;
import com.volantis.mcs.xdime.XDIMEElementInternalMock;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.schema.DISelectElements;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test for {@link SelectElementImpl}.
 */
public class SelectElementTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that {@link DISelectElement#getOutputState()} gets the state
     * from the parent element.
     */
    public void testGetOutputState() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final XDIMEContextInternalMock contextMock =
                new XDIMEContextInternalMock("contextMock",
                        expectations);

        final XDIMEElementInternalMock elementMock =
                new XDIMEElementInternalMock("elementMock", expectations);

        final ElementOutputStateMock parentStateMock =
                new ElementOutputStateMock("parentStateMock",
                        expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getCurrentElement().returns(elementMock).any();

        contextMock.expects.enteringXDIMECPElement();
        elementMock.expects.getOutputState().returns(parentStateMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        XDIMEAttributes attributes = new XDIMEAttributesImpl(
                DISelectElements.SELECT);

        DISelectElement element = new SelectElementImpl(contextMock);

        XDIMEResult result = element.elementStart(contextMock, attributes);
        assertEquals(XDIMEResult.PROCESS_ELEMENT_BODY, result);

        ElementOutputState state = element.getOutputState();
        assertSame(parentStateMock, state);
    }
}
