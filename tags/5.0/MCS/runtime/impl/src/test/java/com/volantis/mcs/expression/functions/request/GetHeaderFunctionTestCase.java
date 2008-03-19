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
package com.volantis.mcs.expression.functions.request;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.functions.AbstractFunction;

/**
 * Simple test case
 */
public class GetHeaderFunctionTestCase
        extends SingleValueFunctionTestAbstract {

    static String FUNCTION_NAME = "getHeader";

    static String FUNCTION_QNAME = "request:getHeader";

    // javadoc inherited
    protected String getFunctionQName() {
        return FUNCTION_QNAME;
    }

    // javadoc inherited
    protected String getURI() {
        return REQUEST_URI;
    }

    // javadoc inherited
    protected String getFunctionName() {
        return FUNCTION_NAME;
    }

    /**
     * Add a name/value pair to the request as a header.
     * @param name The header name
     * @param value The header value.
     */
    protected void addSingleValueExpectations(String name, String value) {
        environmentContextMock.expects.getHeader(name).returns(value).any();
    }

    // javadoc inheritied
    protected AbstractFunction
            createTestableFunction(ExpressionFactory factory) {
        return new GetHeaderFunction();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Jul-04	4948/1	ianw	VBM:2004052804 Pt II of Expression Support performance enhancements

 28-May-04	4611/1	ianw	VBM:2004052804 Performance improvments Pt II for object reduction

 11-Aug-03	1017/2	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 05-Aug-03	928/1	philws	VBM:2003071601 Provide getHeader() and getHeaders() expression functions

 ===========================================================================
*/
