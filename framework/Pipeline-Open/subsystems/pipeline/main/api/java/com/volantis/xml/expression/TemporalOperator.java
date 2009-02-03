/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.expression;

import com.volantis.xml.expression.atomic.temporal.DateTimeValue;
import com.volantis.xml.expression.atomic.temporal.DateValue;
import com.volantis.xml.expression.atomic.temporal.DurationValue;

/**
 * Interface that allows to compute the value of the binary operator on
 * two temporal operands. An implementation may use any binary operation like 
 * addition, subtraction etc. to compute the value.
 */
public interface TemporalOperator {

    /**
     * compute value of <code>DateTimeValue</code>
     * and <code>DurationValue</code> objects
     *
     * @param left the left operand
     * @param right the right operand
     * @return the computed value
     */
    Value compute(DateTimeValue left, DurationValue right);

    /**
     * compute value of <code>DateValue</code>
     * and <code>DurationValue</code> objects
     *
     * @param left the left operand
     * @param right the right operand
     * @return the computed value
     */
    Value compute(DateValue left, DurationValue right);

    /**
     * compute value of <code>DurationValue</code>
     * and <code>DurationValue</code> objects
     *
     * @param left the left operand
     * @param right the right operand
     * @return the computed value
     */
    Value compute(DurationValue left, DurationValue right);

}
