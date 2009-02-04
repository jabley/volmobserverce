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

package com.volantis.styling.unit.engine;

import com.volantis.styling.engine.AttributesMock;
import com.volantis.styling.impl.engine.ElementStackFrameMock;
import com.volantis.styling.impl.engine.ElementStackMock;
import com.volantis.styling.impl.engine.MatcherContextImpl;
import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link MatcherContextImpl}.
 */
public class MatcherContextImplTestCase
        extends TestCaseAbstract {
    
    private static final String NAMESPACE = "namespace";
    private static final String LOCAL_NAME = "local-name";
    private static final String ATTRIBUTE_VALUE1 = "value1";
    private static final String ATTRIBUTE_VALUE2 = "value2";
    private static final int CHILD_COUNT = 99;
    private static final int POSITION = CHILD_COUNT;

    /**
     * Test that the context returns the appropriate information from all its
     * underlying objects.
     *
     * @throws Exception
     */
    public void test() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final AttributesMock attributesMock =
                new AttributesMock("attributesMock", expectations);

        final ElementStackMock elementStackMock =
                new ElementStackMock("elementStackMock", expectations);

        final ElementStackFrameMock currentStackFrameMock =
                new ElementStackFrameMock("currentStackFrameMock",
                                          expectations);

        final ElementStackFrameMock containingStackFrameMock =
                new ElementStackFrameMock("containingStackFrameMock",
                                          expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        elementStackMock.expects.getCurrentElementStackFrame()
                .returns(currentStackFrameMock).any();
        elementStackMock.expects.getContainingElementStackFrame()
                .returns(containingStackFrameMock).any();

        // Initialise the current stack frame.
        currentStackFrameMock.expects.getLocalName().returns(LOCAL_NAME).any();
        currentStackFrameMock.expects.getNamespace().returns(NAMESPACE).any();

        // Initialise the containing stack frame.
        containingStackFrameMock.expects.getChildCount()
                .returns(CHILD_COUNT).any();

        expectations.add(new OrderedExpectations() {
            public void add() {
                attributesMock.expects.getAttributeValue(NAMESPACE, LOCAL_NAME)
                        .returns(ATTRIBUTE_VALUE1);

                attributesMock.expects.getAttributeValue(NAMESPACE, LOCAL_NAME)
                        .returns(ATTRIBUTE_VALUE2);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        MatcherContext matcherContext = new MatcherContextImpl(
                elementStackMock);

        matcherContext.prepareForCascade(attributesMock);

        assertSame("Local name", LOCAL_NAME, matcherContext.getLocalName());
        assertSame("Namespace", NAMESPACE, matcherContext.getNamespace());
        assertEquals("Position", POSITION, matcherContext.getPosition());

        assertSame("Attribute Value (1)", ATTRIBUTE_VALUE1,
                   matcherContext.getAttributeValue(NAMESPACE, LOCAL_NAME));
        assertSame("Attribute Value (2)", ATTRIBUTE_VALUE2,
                   matcherContext.getAttributeValue(NAMESPACE, LOCAL_NAME));

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
