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

import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.StringWriter;
import java.io.Writer;

/**
 * This test tests the delegation mechanism used by the PAPI facade mechanism.
 */
public class AbstractPAPIDelegatingElementTestCase
        extends TestCaseAbstract {

    public static final Writer EXPECTED_WRITER = new StringWriter();
    private static final int EXPECTED_INT = 12;
    private static final String EXPECTED_STRING ="the rain in spain falls mainly on the plain";

    public void testDelegate()
            throws Exception {

        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();

        PAPIElementFactoryMock factoryMock = new PAPIElementFactoryMock(
                "factoryMock", expectations);
        PAPIElementMock elementMock = new PAPIElementMock(
                "elementMock", expectations);
        PAPIAttributesMock attributesMock = new PAPIAttributesMock(
                "attributesMock", expectations);
        final MarinerRequestContextMock requestContextMock =
                new MarinerRequestContextMock(
                        "requestContextMock", expectations);

        factoryMock.expects.createPAPIElement().returns(elementMock);

        PAPIElement element = new AbstractPAPIDelegatingElement(factoryMock);

        // Test elementStart
        elementMock.expects.elementStart(requestContextMock, attributesMock)
                .returns(EXPECTED_INT);

        assertEquals("Return value did not match", EXPECTED_INT,
                     element.elementStart(requestContextMock, attributesMock));

        //Test elementContent
        elementMock.expects.elementContent(
                requestContextMock, attributesMock,EXPECTED_STRING);
        element.elementContent(
                requestContextMock,attributesMock,EXPECTED_STRING);

        //Test getContentWriter
        elementMock.expects
                .getContentWriter(requestContextMock, attributesMock)
                .returns(EXPECTED_WRITER);
        assertEquals("Return value did not match", EXPECTED_WRITER,
                     element.getContentWriter(
                             requestContextMock, attributesMock));

        // Test elementEnd
        elementMock.expects.elementEnd(requestContextMock, attributesMock)
                .returns(EXPECTED_INT);

        assertEquals("Return value did not match", EXPECTED_INT,
                     element.elementEnd(requestContextMock, attributesMock));

        //Test elementReset
        elementMock.expects.elementReset(requestContextMock);
        element.elementReset(requestContextMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8196/3	ianw	VBM:2005051203 Fixed up javadoc

 ===========================================================================
*/
