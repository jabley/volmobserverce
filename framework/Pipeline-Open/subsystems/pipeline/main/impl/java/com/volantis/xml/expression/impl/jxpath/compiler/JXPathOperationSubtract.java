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
import com.volantis.xml.expression.atomic.temporal.DateValue;
import com.volantis.xml.expression.atomic.temporal.DurationValue;
import com.volantis.xml.expression.atomic.temporal.DurationValueOperations;
import com.volantis.xml.expression.atomic.temporal.SimpleDateValue;
import our.apache.commons.jxpath.ri.EvalContext;
import our.apache.commons.jxpath.ri.compiler.CoreOperationSubtract;
import our.apache.commons.jxpath.ri.compiler.Expression;

import java.util.Calendar;

/**
 * Provides a specialist '-' operator that correctly handles the expression
 * framework's {@link com.volantis.xml.expression.Value} and java ("boxed")
 * types.
 */
public class JXPathOperationSubtract extends CoreOperationSubtract implements
        BinaryOperator, TemporalOperator {

    /**
     * An <code>ExpressionFactory</code> instance used for factoring
     * expression related objects
     */
    private ExpressionFactory factory;

    /**
     * Initializes a <code>JXPathOperationSubtract</code> instance
     * 
     * @param left
     *            the left operand to this operator
     * @param right
     *            the right operand to this operator
     * @param factory
     *            an ExpressionFactory instance
     */
    public JXPathOperationSubtract(Expression left, Expression right,
            ExpressionFactory factory) {
        super(left, right);
        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    public Object computeValue(EvalContext evalContext) {
        return JXPathCompiler.compute(
                args[0].computeValue(evalContext), args[1]
                        .computeValue(evalContext), this);
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(DoubleValue left, DoubleValue right) {
        return factory.createDoubleValue(left.asJavaDouble()
                - right.asJavaDouble());
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(DoubleValue left, IntValue right) {
        return factory.createDoubleValue(left.asJavaDouble()
                - ((double) right.asJavaInt()));
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(IntValue left, DoubleValue right) {
        return factory.createDoubleValue(((double) left.asJavaInt())
                - right.asJavaDouble());
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(IntValue left, IntValue right) {
        return factory.createIntValue(left.asJavaInt() - right.asJavaInt());
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(DateTimeValue left, DurationValue right) {
        Calendar calendar = left.getCalendar();
        calendar.add(Calendar.YEAR, -right.getYears());
        calendar.add(Calendar.MONTH, -right.getMonths());
        calendar.add(Calendar.DATE, -right.getDays());
        calendar.add(Calendar.HOUR, -right.getHours());
        calendar.add(Calendar.MINUTE, -right.getMinutes());
        calendar.add(Calendar.SECOND, -right.getSeconds());
        calendar.add(Calendar.MILLISECOND, -right.getMilliseconds());
        return factory.createDateTimeValue(calendar);
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(DateValue left, DurationValue right) {
        SimpleDateValue dv = (SimpleDateValue)left;
        Calendar calendar = dv.getCalendar();
        calendar.add(Calendar.YEAR, -right.getYears());
        calendar.add(Calendar.MONTH, -right.getMonths());
        calendar.add(Calendar.DATE, -right.getDays());
        calendar.add(Calendar.HOUR, -right.getHours());
        calendar.add(Calendar.MINUTE, -right.getMinutes());
        calendar.add(Calendar.SECOND, -right.getSeconds());
        calendar.add(Calendar.MILLISECOND, -right.getMilliseconds());
        return factory.createDateValue(calendar);
    }

    /**
     * {@inheritDoc}
     */
    public Value compute(DurationValue left, DurationValue right) {
        return ((DurationValueOperations)left).subtract(right);
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
