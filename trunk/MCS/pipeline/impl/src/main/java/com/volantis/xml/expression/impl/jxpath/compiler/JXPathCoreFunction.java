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
import com.volantis.xml.expression.impl.jxpath.JXPathExpression;
import com.volantis.xml.expression.sequence.Sequence;
import our.apache.commons.jxpath.JXPathException;
import our.apache.commons.jxpath.ri.EvalContext;
import our.apache.commons.jxpath.ri.compiler.CoreFunction;
import our.apache.commons.jxpath.ri.compiler.Expression;

/**
 * This CoreFunction subclass allows us to provide our own functions
 * that handle the {@link com.volantis.xml.expression.Value} instances and
 * java ("boxed") types.
 */
public class JXPathCoreFunction extends CoreFunction {

    /**
     * Factory for creating Expression related objects
     */
    private ExpressionFactory factory;

    /**
     * Creates a new <code>JXPathCoreFunction</code> instance
     * @param functionCode the function code
     * @param args the function arguments
     * @param factory an ExpressionFactory instance
     */
    public JXPathCoreFunction(int functionCode,
                              Expression[] args,
                              ExpressionFactory factory) {
        super(functionCode, args);
        this.factory = factory;
    }

    // javadoc inherited
    protected Object functionNumber(EvalContext context) {
        // ensure only one argument to this function
        assertArgumentCount(1);

        // evaluate this functions argument
        Object operand = getArg1().computeValue(context);

        try {
            // convert the operand to a sequence
            Sequence sequence =
                    JXPathExpression.asValue(factory, operand).getSequence();
            return PipelineExpressionHelper.fnNumber(sequence, factory);
        } catch (ExpressionException e) {
            // Tunnel the exception out in a JXPath runtime exception
            throw new JXPathException(
                    "Illegal argument to fn:number(): " + e);
        }
    }

    // javadoc inherited
    protected Object functionNot(EvalContext context) {
        // ensure only one argument to this function
        assertArgumentCount(1);

        // evaluate this functions argument
        Object operand = getArg1().computeValue(context);

        try {
            // convert the operand to a sequence
            Sequence sequence =
                    JXPathExpression.asValue(factory, operand).getSequence();
            return PipelineExpressionHelper.fnNot(sequence);
        } catch (ExpressionException e) {
            // Tunnel the exception out in a JXPath runtime exception
            throw new JXPathException(
                    "Illegal argument to fn:not(): " + e);
        }
    }

    // javadoc inherited
    protected Object functionBoolean(EvalContext context) {
        // ensure only one argument to this function
        assertArgumentCount(1);

        // evaluate this functions argument
        Object operand = getArg1().computeValue(context);

        try {
            // convert the operand to a sequence
            Sequence sequence =
                    JXPathExpression.asValue(factory, operand).getSequence();
            return PipelineExpressionHelper.fnBoolean(sequence);
        } catch (ExpressionException e) {
            // Tunnel the exception out in a JXPath runtime exception
            throw new JXPathException(
                    "Illegal argument to fn:boolean(): " + e);
        }
    }

    // javadoc inherited
    protected Object functionConcat(EvalContext context) {
        if (getArgumentCount() < 2) {
            // the concat function must be invoked with 2 or more arguments
            throw new JXPathException(
                    "concat function must have at least two arguments. " +
                    "Actual argument count is " + getArgumentCount());
        }

        // use a string buffer to perform the concatenation
        StringBuffer buffer = new StringBuffer();
        Expression[] operands = getArguments();
        Sequence sequence;
        try {
            for (int i = 0; i < operands.length; i++) {

                // evaluate the operand and convert it to a sequence
                sequence = JXPathExpression.asValue(
                        factory,
                        operands[i].computeValue(context)).getSequence();

                int length = sequence.getLength();
                // 1) If sequence is empty then we append the empty string.
                // 2) If sequence has one item we convert the item to a string
                //    and append to the buffer
                // 3) If sequence contains more that one item we raise an error
                if (length == 1) {
                    buffer.append(
                            sequence.getItem(1).stringValue().asJavaString());
                } else if (length > 1) {
                    throw new JXPathException(
                            "fn:concat cannot accept a sequence operand " +
                            "with more than one item");
                }
            }
        } catch (ExpressionException e) {
            // Tunnel the exception out in a JXPath runtime exception
            throw new JXPathException(
                    "Illegal argument to fn:concat(): " + e);
        }
        // return the concatenated arguments as a StringValue
        return factory.createStringValue(buffer.toString());
    }

    /**
     * Method that checks to see if an "expected" argument count, matches the
     * actual number of arguments provided. If it does not match a
     * <code>JXPathException</code> is thrown.
     * <p>
     * <strong>Note:</strong> The super class provides identical functionality
     * via its {@link CoreFunction#assertArgCount} method. Unfortunately, this
     * method is private and as it is third party code we cannot really modify
     * it
     * </p>
     * @param count the expected argument count
     */
    private void assertArgumentCount(int count) {
        if (getArgumentCount() != count) {
            throw new JXPathException("Incorrect number of arguments: " + this);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 24-Nov-03	468/1	doug	VBM:2003112103 Added support for the XPath concat function

 27-Oct-03	433/1	doug	VBM:2003102002 Added several new comparison operators

 ===========================================================================
*/
