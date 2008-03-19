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

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.functions.DefaultValueProvider;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.shared.dependency.DependencyContext;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Collections;

/**
 * Represents the request:getHeaders function call within an expression
 * environment.
 */
public class GetHeadersFunction extends AbstractRequestExpressionFunction {

    /**
     * The DefaultValueProvider for this class.
     */
    private static final DefaultValueProvider DEFAULT_VALUE_PROVIDER =
            new DefaultValueProvider();

    /**
     * Initializes the new instance with the given parameters.
     *
     */
    public GetHeadersFunction() {
    }

    protected String getFunctionName() {
        return "getHeaders";
    }

    protected DefaultValueProvider getDefaultValueProvider() {
        return DEFAULT_VALUE_PROVIDER;
    }

    /**
     * The given named header's set of values, if any, is extracted from the
     * specified requestContext and returned. If the requestContext doesn't
     * contain headers or the given named header doesn't exist, the specified
     * default value is returned instead.
     *
     * @param name           the name of the header to be obtained
     * @param defaultValue   the value to use if the header cannot be found
     * @return the header's set of values or the default value if the header is
     *         not found
     */
    protected Value execute(
            ExpressionContext expressionContext,
            MarinerRequestContext requestContext, String name,
            Value defaultValue) {
 
        ExpressionFactory factory = expressionContext.getFactory();
        Value value = defaultValue;

        EnvironmentContext environmentContext =
            ContextInternals.getEnvironmentContext(requestContext);


        // See if the header can be found
        Enumeration enumeration = environmentContext.getHeaders(name);

        // The number of headers isn't known until the enumeration has
        // been traversed so build up a dynamic array of the header
        // values
        List headers;
        if (enumeration == null || !enumeration.hasMoreElements()) {
            headers = Collections.EMPTY_LIST;
        } else {
            headers = new ArrayList();

            // Add each header, as a string value, to an array
            while (enumeration.hasMoreElements()) {
                headers.add(enumeration.nextElement());
            }

            int count = headers.size();
            // One or more header values were found so put them in
            // a sequence
            Item[] items = new Item[count];

            for (int i = 0; i < count; i++) {
                String string = (String) headers.get(i);
                items[i] = factory.createStringValue(string);
            }

            value = factory.createSequence(items);
        }

        // Add dependency information if necessary.
        DependencyContext context = expressionContext.getDependencyContext();
        if (context.isTrackingDependencies()) {
            context.addDependency(new RequestHeadersDependency(name, headers));
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

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/3	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 23-Jul-04	4948/1	ianw	VBM:2004052804 Pt II of Expression Support performance enhancements

 28-May-04	4611/1	ianw	VBM:2004052804 Performance improvments Pt II for object reduction

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 11-Aug-03	1017/2	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 05-Aug-03	928/1	philws	VBM:2003071601 Provide getHeader() and getHeaders() expression functions

 ===========================================================================
*/
