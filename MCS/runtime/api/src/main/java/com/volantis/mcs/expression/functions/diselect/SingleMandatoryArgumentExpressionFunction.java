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
package com.volantis.mcs.expression.functions.diselect;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.expression.functions.AbstractExpressionFunction;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * A general class for handling common code for functions that have a single
 * mandatory argument and can take an additional optional default value argument.
 */
public abstract class SingleMandatoryArgumentExpressionFunction
        extends AbstractExpressionFunction {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(
                    SingleMandatoryArgumentExpressionFunction.class);

    /**
     * A generic implementation of Function.invoke that takes a couple
     * of additional parameters to handle some specific issues in a generic
     * way.
     *
     * @param expressionContext     encapsulates all the information required
     *                              to evaluate an expression
     * @return The Value returned by invoking the function.
     * @throws ExpressionException If there is a problem with the expression or
     * function invocation.
     */
    public Value invoke(ExpressionContext expressionContext,
                        Value[] values) throws ExpressionException {

        String name = null;
        Value defaultValue = Sequence.EMPTY;

        if ((values != null) && (values.length > 0)) {
            // Ensure that the name is always in string form
            name = values[0].stringValue().asJavaString();
        } else {
            throw new ExpressionException(
                getFunctionName() + " must be invoked with at least one name" +
                "value (and optionally with a default value)");
        }

        if (values.length > 1) {
            // Set the default value
            defaultValue =
                    getDefaultValueProvider().provideDefaultValue(values);
        }

        if (values.length > 2) {
            logger.warn("too-many-arguments", new Object[]{getFunctionName()});
        }

        return execute(expressionContext, name, defaultValue);
    }

    /**
     * Execute the specific function.
     * @param expressionContext The ExpressionContext used by this function
     * @param name              Name of the parameter to get
     * @param defaultValue      Value to return if the parameter does not exist
     * @return The Value returned by the execution of the function.
     */
    protected abstract Value execute(
            ExpressionContext expressionContext,
            String name, Value defaultValue);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Sep-05	9415/4	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 23-Jul-04	4948/1	ianw	VBM:2004052804 Pt II of Expression Support performance enhancements

 28-May-04	4611/1	ianw	VBM:2004052804 Performance improvments Pt II for object reduction

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 11-Aug-03	1017/3	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 ===========================================================================
*/
