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
 * (c) Volantis Systems Ltd 2003. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions.request;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.functions.AbstractFunction;


/**
 * Tests the servlet request function for an XPath value like so:
 * <pipeline:expr value="%{request:getParameter("PAPERS")}"/>
 */
public class GetParameterFunctionTestCase
        extends SingleValueFunctionTestAbstract {

    static String FUNCTION_NAME = "getParameter";

    static String FUNCTION_QNAME = "request:getParameter";

    // javadoc inherited
    protected String getFunctionQName() {
        return FUNCTION_QNAME;
    }

    // javadoc inherited
    protected String getFunctionName() {
        return FUNCTION_NAME;
    }

    // javadoc inherited
    protected String getURI() {
        return REQUEST_URI;
    }

    /**
     * Add a name/value pair to the request as a parameter.
     * @param name The parameter name
     * @param value The parameter value.
     */
    protected void addSingleValueExpectations(String name, String value) {
        requestContextMock.expects.getParameter(name).returns(value).any();
    }

    // javadoc inheritied
    protected AbstractFunction
            createTestableFunction(ExpressionFactory factory) {
        return new GetParameterFunction();
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

 05-Aug-03	928/2	philws	VBM:2003071601 Provide getHeader() and getHeaders() expression functions

 31-Jul-03	828/4	philws	VBM:2003071802 Update MCS against new Volantis Expression API from the Pipeline depot

 28-Jul-03	874/1	doug	VBM:2003072310 Refactored usages of XMLQName to ExpandedName

 27-Jun-03	503/1	sumit	VBM:2003061906 ServletRequestFunction fixed to take MarinerRequestContext as source of parameters

 25-Jun-03	102/2	sumit	VBM:2003061906 request:getParameter XPath function support

 ===========================================================================
*/
