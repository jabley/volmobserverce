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
package com.volantis.xml.expression.impl.jxpath.compiler;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.UnaryOperator;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import our.apache.commons.jxpath.ri.EvalContext;
import our.apache.commons.jxpath.ri.compiler.CoreOperationNegate;
import our.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Provides a specialist unary '-' operator that correctly handles the
 * expression framework's {@link com.volantis.xml.expression.Value} and java
 * ("boxed") types.
 */
public class JXPathOperationNegate extends CoreOperationNegate implements
        UnaryOperator {

    /**
     * An <code>ExpressionFactory</code> instance used for factoring
     * expression related objects
     */
    private ExpressionFactory factory;

    /**
     * Initializes a <code>JXPathOperationNegate</code> instance
     * 
     * @param left
     *            the left operand to this operator
     * @param right
     *            the right operand to this operator
     * @param factory
     *            an ExpressionFactory instance
     */
    public JXPathOperationNegate(Expression arg, ExpressionFactory factory) {
        super(arg);
        this.factory = factory;
    }

    // javadoc inherited
    public Object computeValue(EvalContext evalContext) {
        // value to be returned
        Value result = JXPathCompiler.compute(
                args[0].computeValue(evalContext), this);

        return result;
    }

    // javadoc inherited
    public Value compute(DoubleValue value) {
        return factory.createDoubleValue(-value.asJavaDouble());
    }

    // javadoc inherited
    public Value compute(IntValue value) {
        return factory.createIntValue(-value.asJavaInt());
    }
}


/*
 * ===========================================================================
 * Change History
 * ===========================================================================
 * $Log$
 * 
 * 28-Dec-05 10855/1 pszul VBM:2005121508 Arithmetic operators +, -, *, div, mod
 * and unary- added to pipeline expressions.
 * 
 * ===========================================================================
 */
