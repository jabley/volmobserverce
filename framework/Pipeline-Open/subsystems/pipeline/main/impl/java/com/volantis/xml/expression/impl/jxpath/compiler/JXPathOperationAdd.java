/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.impl.jxpath.compiler;

import com.volantis.xml.expression.BinaryOperator;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.TemporalOperator;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.atomic.temporal.DateTimeValue;
import com.volantis.xml.expression.atomic.temporal.DateTimeValueOperations;
import com.volantis.xml.expression.atomic.temporal.DateValue;
import com.volantis.xml.expression.atomic.temporal.DateValueOperations;
import com.volantis.xml.expression.atomic.temporal.DurationValue;
import com.volantis.xml.expression.atomic.temporal.SimpleDurationValue;
import our.apache.commons.jxpath.ri.EvalContext;
import our.apache.commons.jxpath.ri.compiler.CoreOperationAdd;
import our.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Provides a specialist '+' operator that correctly handles the expression
 * framework's {@link com.volantis.xml.expression.Value} and java ("boxed")
 * types.
 */
public class JXPathOperationAdd extends CoreOperationAdd implements
        BinaryOperator, TemporalOperator {

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

    /**
     * {@inheritDoc}
     */
    public Object computeValue(EvalContext evalContext) {
        Object result;
        if (args.length == 0) {
            result = factory.createIntValue(0);
        } else {
            result = args[0].computeValue(evalContext);
            for (int i = 1; i < args.length; i++) {
                // evaluate each operand of this expression.
                result = JXPathCompiler.compute(
                        result,
                        args[i].computeValue(evalContext),
                        this);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(DoubleValue left, DoubleValue right) {
        return factory.createDoubleValue(left.asJavaDouble()
                + right.asJavaDouble());
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(DoubleValue left, IntValue right) {
        return factory.createDoubleValue(left.asJavaDouble()
                + ((double) right.asJavaInt()));
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(IntValue left, DoubleValue right) {
        return factory.createDoubleValue(((double) left.asJavaInt())
                + right.asJavaDouble());
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(IntValue left, IntValue right) {
        return factory.createIntValue(left.asJavaInt() + right.asJavaInt());
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(DateTimeValue date, DurationValue duration) {
        return ((DateTimeValueOperations)date).add(duration);
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(DateValue left, DurationValue right) {
        return ((DateValueOperations)left).add(right);
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(DurationValue left, DurationValue right) {
        return ((SimpleDurationValue)left).add(right);
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
