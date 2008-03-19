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
package com.volantis.xml.expression.impl.jxpath.functions;

import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ValueConversionException;
import com.volantis.xml.expression.impl.jxpath.JXPathExpression;

/**
 * A function adapter for using volantis expression Functions in JXPath.
 */
public class JXPathExpressionFunctionAdapter implements
        our.apache.commons.jxpath.Function {
    /**
     * The factory that should be used to create any value instances.
     */
    private ExpressionFactory factory;

    /**
     * The function to invoke.
     */
    private Function function;

    /**
     * The context within which the function should be invoked.
     */
    private ExpressionContext context;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param function Function to adapt
     * @param context  Volantis expression context
     */
    public JXPathExpressionFunctionAdapter(
            ExpressionFactory factory,
            Function function,
            ExpressionContext context) {
        this.factory = factory;
        this.function = function;
        this.context = context;
    }

    // javadoc inherited
    public Object invoke(our.apache.commons.jxpath.ExpressionContext ctx,
                         Object[] parameters) {
        try {
            // Convert the incoming array of objects into an appropriate
            // array of Value implementation instances
            Value[] params = new Value[(parameters == null) ?
                                       0 : parameters.length];

            for (int i = 0; i < params.length; i++) {
                if (parameters[i] != null) {
                    try {
                        // Leverage the conversion supporting method in the
                        // associated {@link JXPathExpression} class
                        params[i] = JXPathExpression.asValue(factory,
                                                             parameters[i]);
                    } catch (ExpressionException e) {
                        throw new ValueConversionException(
                                "Cannot convert parameter " + i,
                                e);
                    }
                } else {
                    throw new NullPointerException(
                            "Parameter " + i + " is null");
                }
            }

            return function.invoke(context, params);
        } catch (ExpressionException e) {
            // Tunnel the exception out of this method as a runtime exception
            throw new ExtendedRuntimeException(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/3	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 25-Jun-03	102/3	sumit	VBM:2003061906 request:getParameter XPath function support

 ===========================================================================
*/
