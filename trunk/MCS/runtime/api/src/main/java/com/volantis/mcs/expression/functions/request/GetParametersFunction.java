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

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.functions.DefaultValueProvider;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.shared.dependency.DependencyContext;

import java.util.Arrays;
import java.util.List;

/**
 * The GetParameters ExpressionFunction.
 */
public class GetParametersFunction extends AbstractRequestExpressionFunction {



    /**
     * The DefaultValueProvider for this class.
     */
    private static final DefaultValueProvider DEFAULT_VALUE_PROVIDER =
            new DefaultValueProvider();


    /**
     * Initializes the new instance with the given parameters.
     *
     */
    public GetParametersFunction() {
    }

    // javadoc inherited
    protected String getFunctionName() {
        return "getParameters";
    }

    // javadoc inherited
    protected DefaultValueProvider getDefaultValueProvider() {
        return DEFAULT_VALUE_PROVIDER;
    }

    // javadoc inherited
    protected Value execute(
            ExpressionContext expressionContext,
            MarinerRequestContext requestContext, String name,
            Value defaultValue) {

        ExpressionFactory factory = expressionContext.getFactory();
                                
        Value value = defaultValue;

        String params [] = requestContext.getParameterValues(name);
        if (params != null && params.length > 0) {
            Item[] items = new Item[params.length];

            for (int i = 0; i < params.length; i++) {
                items[i] = factory.createStringValue(params[i]);
            }

            value = factory.createSequence(items);

            // Add dependency information if necessary.
            DependencyContext context = expressionContext.getDependencyContext();
            if (context.isTrackingDependencies()) {
                List list = Arrays.asList(params);
                context.addDependency(new RequestParametersDependency(name, list));
            }
        }

        return value;
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

 23-Jul-04	4948/4	ianw	VBM:2004052804 Pt II of Expression Support performance enhancements

 28-May-04	4611/1	ianw	VBM:2004052804 Performance improvments Pt II for object reduction

 29-Jun-04	4773/1	adrianj	VBM:2003081804 Removed superfluous ArrayList

 28-May-04	4611/1	ianw	VBM:2004052804 Performance improvments Pt II for object reduction

 11-Aug-03	1017/1	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 ===========================================================================
*/
