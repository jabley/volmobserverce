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

import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.impl.jxpath.JXPathExpression;
import com.volantis.xml.expression.sequence.Sequence;
import our.apache.commons.jxpath.JXPathException;
import our.apache.commons.jxpath.ri.EvalContext;
import our.apache.commons.jxpath.ri.compiler.CoreOperationOr;
import our.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Provides a specialist OR operator that correctly handles
 * the expression framework's {@link com.volantis.xml.expression.Value} and
 * java ("boxed") types.
 */
public class JXPathOperationOr extends CoreOperationOr {

    /**
     * An <code>ExpressionFactory</code> instance used for factoring expression
     * related objects
     */
    private ExpressionFactory factory;

    /**
     * Creates an new <code>JXPathOperationOr</code> instance
     * @param args the operators arguments
     * @param factory an ExpressionFactory instance
     */
    public JXPathOperationOr(Expression[] args, ExpressionFactory factory) {
        super(args);
        this.factory = factory;
    }

    // javadoc inherited
    public Object computeValue(EvalContext evalContext) {

        // return value
        BooleanValue eval = BooleanValue.FALSE;

        // evaluated value of the current operand
        Object operandEval;

        for (int i = 0;
             i < args.length && !eval.asJavaBoolean(); // breakout if eval true
             i++) {
            // evaluate each operand of the OR expression.
            // If any evalute to true then the OR expression evaluates to true
            // and true is returned

            // evaluate the current operand
            operandEval = args[i].computeValue(evalContext);

            try {
                // convert the result to a sequence
                Sequence sequence = JXPathExpression.asValue(
                        factory, operandEval).getSequence();

                // convert the sequence to the "effective boolean value"
                eval = PipelineExpressionHelper.fnBoolean(sequence);
            } catch (ExpressionException e) {
                throw new JXPathException("Error when evaluating the " + i +
                                          "operand of the OR function", e);
            }
        }
        // return the result as an Object
        return eval;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 27-Oct-03	433/1	doug	VBM:2003102002 Added several new comparison operators

 ===========================================================================
*/
