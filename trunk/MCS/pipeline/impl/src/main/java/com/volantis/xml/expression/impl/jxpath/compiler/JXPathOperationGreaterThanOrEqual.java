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
package com.volantis.xml.expression.impl.jxpath.compiler;

import com.volantis.xml.expression.CompareResult;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.ValueComparator;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.NodeValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.NumericValue;

import our.apache.commons.jxpath.ri.EvalContext;
import our.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import our.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Provides a specialist GREATER THAN OR EQUAL operator that correctly handles
 * the expression framework's {@link com.volantis.xml.expression.Value} and
 * java ("boxed") types.
 */
public class JXPathOperationGreaterThanOrEqual
        extends CoreOperationGreaterThanOrEqual
        implements ValueComparator {

    /**
     * Creates a new <code>JXPathOperationGreaterThanOrEqual</code> instance.
     * @param left left operand
     * @param right right operand
     */
    public JXPathOperationGreaterThanOrEqual(Expression left,
                                             Expression right) {
        super(left, right);
    }

    // javadoc inherited
    public Object computeValue(EvalContext evalContext) {
        boolean result = JXPathCompiler.compare(
                args[0].computeValue(evalContext),
                args[1].computeValue(evalContext),
                this);

        return result ? BooleanValue.TRUE : BooleanValue.FALSE;
    }

    // javadoc inherited
    public boolean compare(StringValue left, StringValue right) {
        return PipelineExpressionHelper.compare(left, right) <= 0;
    }

    // javadoc inherited
    public boolean compare(BooleanValue left, BooleanValue right) {
        return PipelineExpressionHelper.compare(left, right) <= 0;
    }

    // javadoc inherited
    public boolean compare(NumericValue left, NumericValue right) {
        final CompareResult result = PipelineExpressionHelper.compareNumeric(
                left, right);
        return result == CompareResult.EQUAL
                || result == CompareResult.LESS_THAN;
    }

    public boolean compare(NodeValue left, NodeValue right) {
        return PipelineExpressionHelper.compare(left, right) <= 0;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 27-Oct-03	433/1	doug	VBM:2003102002 Added several new comparison operators

 ===========================================================================
*/
