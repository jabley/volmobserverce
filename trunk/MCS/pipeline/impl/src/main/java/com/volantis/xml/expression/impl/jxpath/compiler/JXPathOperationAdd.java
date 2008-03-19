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

import com.volantis.xml.expression.BinaryOperator;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.atomic.temporal.DateTimeValue;
import com.volantis.xml.expression.atomic.temporal.DateTimeValueOperations;
import com.volantis.xml.expression.atomic.temporal.DurationValue;
import our.apache.commons.jxpath.ri.EvalContext;
import our.apache.commons.jxpath.ri.compiler.CoreOperationAdd;
import our.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Provides a specialist '+' operator that correctly handles the expression
 * framework's {@link com.volantis.xml.expression.Value} and java ("boxed")
 * types.
 */
public class JXPathOperationAdd extends CoreOperationAdd implements
        BinaryOperator {

    /**
     * An <code>ExpressionFactory</code> instance used for factoring
     * expression related objects
     */
    private ExpressionFactory factory;

    /**
     * Initializes a <code>JXPathOperationAdd</code> instance
     * 
     * @param expressions
     *            the array of Expressions that are the operands to this
     *            operator
     * @param factory
     *            an ExpressionFactory instance
     */
    public JXPathOperationAdd(Expression[] expressions,
            ExpressionFactory factory) {
        super(expressions);
        this.factory = factory;
    }

    // javadoc inherited
    public Object computeValue(EvalContext evalContext) {
        Value result = factory.createIntValue(0);

        // evaluated value of the current operand
        Object operandEval;

        for (int i = 0; i < args.length; i++) {
            // evaluate each operand of this expression.
            operandEval = args[i].computeValue(evalContext);

            // add the operand to the current result
            result = JXPathCompiler.compute(result, operandEval, this);
        }

        return result;
    }

    // javadoc inherited
    public Value compute(DoubleValue left, DoubleValue right) {
        return factory.createDoubleValue(left.asJavaDouble()
                + right.asJavaDouble());
    }

    // javadoc inherited
    public Value compute(DoubleValue left, IntValue right) {
        return factory.createDoubleValue(left.asJavaDouble()
                + ((double) right.asJavaInt()));
    }

    // javadoc inherited
    public Value compute(IntValue left, DoubleValue right) {
        return factory.createDoubleValue(((double) left.asJavaInt())
                + right.asJavaDouble());
    }

    // javadoc inherited
    public Value compute(IntValue left, IntValue right) {
        return factory.createIntValue(left.asJavaInt() + right.asJavaInt());
    }

    // javadoc inherited
    public Value compute(DateTimeValue date, DurationValue duration) {
        return ((DateTimeValueOperations)date).add(duration);
    }

    // javadoc inherited
    public Value compute(DurationValue duration, DateTimeValue date) {
        return ((DateTimeValueOperations)date).add(duration);
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
