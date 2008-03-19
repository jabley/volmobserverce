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
package com.volantis.xml.expression;

import com.volantis.xml.expression.atomic.NodeValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.numeric.NumericValue;

/**
 * Interface that allows two operands of the same type to be "compared".
 * An implementation may compare operands to determine if one is less than the
 * other. Another may compare two operands to determine if they are equal, and
 * so on.
 */
public interface ValueComparator {

    /**
     * Compare two <code>StringValue<code> objects
     * @param left the left operand
     * @param right the right operand
     * @return true if the particular comparison is successful
     */
    boolean compare(StringValue left, StringValue right);

    /**
     * Compare two <code>BooleanValue</code> objects
     * @param left the left operand
     * @param right the right operand
     * @return true if the particular comparison is successful
     */
    boolean compare(BooleanValue left, BooleanValue right);

    /**
     * Compare two <code>NumericValue</code> objects
     * @param left the left operand
     * @param right the right operand
     * @return true if the particular comparison is successful
     */
    boolean compare(NumericValue left, NumericValue right);

    /**
     * Compare two <code>NodeValue</code> objects.
     * 
     * @param left the left operand
     * @param right the right operand
     * @return true if the particular comparison is successful
     */
    boolean compare(NodeValue left, NodeValue right);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 27-Oct-03	433/1	doug	VBM:2003102002 Added several new comparison operators

 ===========================================================================
*/
