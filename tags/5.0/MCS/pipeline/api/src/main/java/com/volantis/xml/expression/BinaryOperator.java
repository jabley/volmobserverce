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
package com.volantis.xml.expression;

import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;

/**
 * Interface that allows to compute the value of the binary operator on 
 * two operands. An implementation may use any binary operation like 
 * addition, subtraction etc. to compute the value.
 */
public interface BinaryOperator {

    /**
     * compute value for two <code>DoubleValue<code> objects
     * @param left the left operand
     * @param right the right operand
     * @return the computed value
     */
    Value compute(DoubleValue left, DoubleValue right);

    /**
     * compute value for <code>DoubleValue<code> 
     * and <code>IntValue<code> objects
     * @param left the left operand
     * @param right the right operand
     * @return the computed value
     */
    Value compute(DoubleValue left, IntValue right);

    /**
     * compute value for <code>IntValue<code> 
     * and <code>DoubleValue<code> objects
     * @param left the left operand
     * @param right the right operand
     * @return the computed value
     */
    Value compute(IntValue left, DoubleValue right);

    /**
     * compute value for two <code>IntValue<code> objects
     * @param left the left operand
     * @param right the right operand
     * @return the computed value
     */
    Value compute(IntValue left, IntValue right);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Jan-06	10855/3	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expressions.

 28-Dec-05	10855/1	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expressions.

 ===========================================================================
*/
