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

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.functions.DefaultValueProvider;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.shared.dependency.DependencyContext;

/**
 * Represents the request:getParameter function call within an expression
 * environment.
 */
public class GetParameterFunction extends AbstractRequestExpressionFunction {
    /**
     * The DefaultValueProvider for this class.
     */
    private static final DefaultValueProvider DEFAULT_VALUE_PROVIDER =
            new StringDefaultValueProvider();

    /**
     * Gets the parameter value with the specified name and specified index
     * from the request. If the parameter does not exist of if the parameter
     * has no value at the specified index it will return the default value
     *
     * @param expressionContext The ExpressionContext used by this function
     * @param name              Name of the parameter to get
     * @param defaultValue      Value to return if the parameter does not exist
     * @return Value of parameter or default value if the parameter was not
     *         found
     */
    protected Value execute(
            ExpressionContext expressionContext,
            MarinerRequestContext requestContext, String name,
            Value defaultValue) {

        ExpressionFactory factory = expressionContext.getFactory();
                                
        Value value = defaultValue;

        String param = requestContext.getParameter(name);
        if (param != null) {
            value = factory.createStringValue(param);
        }

        // Add dependency information if necessary.
        DependencyContext context = expressionContext.getDependencyContext();
        if (context.isTrackingDependencies()) {
            context.addDependency(new RequestParameterDependency(name, param));
        }


        return value;
    }

    // javadoc inherited
    protected String getFunctionName() {
        return "getParameter";
    }

    // javadoc inherited
    protected DefaultValueProvider getDefaultValueProvider() {
        return DEFAULT_VALUE_PROVIDER;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Sep-05	9415/2	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Jul-04	4948/1	ianw	VBM:2004052804 Pt II of Expression Support performance enhancements

 28-May-04	4611/1	ianw	VBM:2004052804 Performance improvments Pt II for object reduction

 11-Aug-03	1017/2	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 05-Aug-03	928/3	philws	VBM:2003071601 Provide getHeader() and getHeaders() expression functions

 31-Jul-03	828/2	philws	VBM:2003071802 Update MCS against new Volantis Expression API from the Pipeline depot

 27-Jun-03	503/3	sumit	VBM:2003061906 request:getParameter function support

 27-Jun-03	503/1	sumit	VBM:2003061906 ServletRequestFunction fixed to take MarinerRequestContext as source of parameters

 ===========================================================================
*/
