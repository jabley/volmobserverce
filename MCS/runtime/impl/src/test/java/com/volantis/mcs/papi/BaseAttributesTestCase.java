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

package com.volantis.mcs.papi;

import com.volantis.mcs.papi.impl.PAPIAttributesImplMock;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link BaseAttributes}.
 */
public class BaseAttributesTestCase
        extends TestCaseAbstract {
    private static final String VALUE = "VALUE";

    /**
     * Test to make sure that when setting styleClass that sets the class
     * attribute on the underlying
     * @throws Exception
     */
    public void testSetStyleClassSetsClass() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final PAPIElementFactoryMock papiElementFactoryMock =
                new PAPIElementFactoryMock("papiElementFactoryMock",
                                           expectations);

        final PAPIAttributesImplMock attributesMock =
                new PAPIAttributesImplMock("attributesMock",
                                           expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                papiElementFactoryMock.expects.createGenericAttributes()
                        .returns(attributesMock);

                attributesMock.expects.reset();
                attributesMock.expects.setAttributeValue(null, "class", VALUE);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        BaseAttributes attributes = new BaseAttributes(papiElementFactoryMock) {
            public String getElementName() {
                throw new UnsupportedOperationException();
            }
        };

        attributes.setStyleClass(VALUE);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/2	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 ===========================================================================
*/
