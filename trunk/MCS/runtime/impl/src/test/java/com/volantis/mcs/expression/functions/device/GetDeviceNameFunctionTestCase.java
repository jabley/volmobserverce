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
package com.volantis.mcs.expression.functions.device;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionContextMock;
import com.volantis.xml.expression.ExpressionFactoryMock;
import com.volantis.xml.expression.atomic.SimpleStringValue;
import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.mcs.expression.DevicePolicyValueAccessorMock;
import com.volantis.mcs.expression.functions.device.GetDeviceNameFunction;

/**
 * Test case for the GetDeviceNameFunction class.
 */
public class GetDeviceNameFunctionTestCase extends TestCaseAbstract {

    private static final Value[] EMPTY_VALUE_ARRAY = {};

    private ExpressionContextMock expressionContextMock;

    protected ExpressionFactoryMock expressionFactoryMock;
    private DevicePolicyValueAccessorMock accessorMock;

    protected void setUp() throws Exception {
        super.setUp();

        expressionContextMock =
                new ExpressionContextMock("expressionContextMock",
                                          expectations);
        expressionFactoryMock =
                new ExpressionFactoryMock("expressionFactoryMock",
                                          expectations);

        accessorMock = new DevicePolicyValueAccessorMock(
                "requestContext", expectations);

        expressionContextMock.expects.
                getProperty(DevicePolicyValueAccessor.class)
                .returns(accessorMock).any();
        expressionContextMock.expects.getFactory().
                returns(expressionFactoryMock).any();
        expressionFactoryMock.fuzzy.createStringValue(
                mockFactory.expectsInstanceOf(String.class))
                .returns(new SimpleStringValue(expressionFactoryMock,
                                               "device-name")).any();
    }

    /**
     * Tests the retrieval of a device name.
     */
    public void testGetDeviceName() throws Exception {

        accessorMock.expects.getDeviceName().returns("device-name");

        Function function = new GetDeviceNameFunction();
        Value value = function.invoke(expressionContextMock,
                                      EMPTY_VALUE_ARRAY);
        String name = value.stringValue().asJavaString();
        assertNotNull(name);
        assertEquals("device-name",
                     name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Nov-05	10049/1	schaloner	VBM:2005092818 Ongoing device name support

 ===========================================================================
*/
