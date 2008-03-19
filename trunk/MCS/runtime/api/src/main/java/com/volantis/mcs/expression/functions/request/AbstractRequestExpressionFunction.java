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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.expression.functions.request;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.functions.diselect.SingleMandatoryArgumentExpressionFunction;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.Value;

/**
 * Base for the request related functions.
 */
public abstract class AbstractRequestExpressionFunction
        extends SingleMandatoryArgumentExpressionFunction {

    // Javadoc inherited.
    protected Value execute(
            ExpressionContext expressionContext, String name,
            Value defaultValue) {

        MarinerRequestContext requestContext = (MarinerRequestContext)
                expressionContext.getProperty(MarinerRequestContext.class);

        return execute(expressionContext, requestContext, name, defaultValue);
    }

    /**
     * Execute the function.
     *
     * @param expressionContext The ExpressionContext used by this function
     * @param requestContext    The MarinerRequestContext
     * @param name              Name of the parameter to get
     * @param defaultValue      Value to return if the parameter does not exist
     * @return The Value returned by the execution of the function.
     */
    protected abstract Value execute(
            ExpressionContext expressionContext,
            MarinerRequestContext requestContext, String name,
            Value defaultValue);
}
