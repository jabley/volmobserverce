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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions.device;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.functions.AbstractFunction;
import com.volantis.mcs.expression.functions.device.GetAncestorRelationshipFunction;
import com.volantis.mcs.context.DeviceAncestorRelationship;
import junit.framework.Assert;

/**
 * Unit test for the {@link com.volantis.mcs.expression.functions.device.GetAncestorRelationshipFunction} class
 */
public class GetAncestorRelationshipFunctionTestCase
        extends DeviceRelationshipFunctionTestAbstract {

    /**
     * Name of the function
     */
    private static final String FNAME = "getAncestorRelationship";

    // javadoc inherited
    public void assertInvokeWhenDeviceResult(Value result)
            throws Exception {
        // ensure that the value is a StringValue instance
        Assert.assertTrue("Value should be a StringValue instance",
                   result instanceof StringValue);

        // ensure the string value is correct
        Assert.assertEquals("StringValue should be 'device'",
                     DeviceAncestorRelationship.DEVICE.getRelationshipName(),
                     result.stringValue().asJavaString());
    }

    // javadoc inherited
    public void assertInvokeWhenAncestorResult(Value result)
            throws Exception {
        // ensure that the value is a StringValue instance
        Assert.assertTrue("Value should be a StringValue instance",
                   result instanceof StringValue);

        // ensure the string value is correct
        Assert.assertEquals("StringValue should be 'ancestor'",
                     DeviceAncestorRelationship.ANCESTOR.getRelationshipName(),
                     result.stringValue().asJavaString());
    }

    // javadoc inherited
    public void assertInvokeWhenUnrelatedResult(Value result)
            throws Exception {
        // ensure that the value is a StringValue instance
        Assert.assertTrue("Value should be a StringValue instance",
                   result instanceof StringValue);

        // ensure the string value is correct
        Assert.assertEquals("StringValue should be 'unrelated'",
                     DeviceAncestorRelationship.UNRELATED.getRelationshipName(),
                     result.stringValue().asJavaString());
    }

    // javadoc inherited
    public void assertInvokeWhenUnknownResult(Value result)
            throws Exception {
        // ensure that the value is a StringValue instance
        Assert.assertTrue("Value should be a StringValue instance",
                   result instanceof StringValue);

        // ensure the string value is correct
        Assert.assertEquals("StringValue should be 'unknown'",
                     DeviceAncestorRelationship.UNKNOWN.getRelationshipName(),
                     result.stringValue().asJavaString());
    }

    // javadoc inherited
    protected AbstractFunction
            createTestableFunction(ExpressionFactory factory) {
        return new GetAncestorRelationshipFunction();
    }

    // javadoc inherited
    protected String getFunctionName() {
        return FNAME;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
