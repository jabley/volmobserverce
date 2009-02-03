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

import com.volantis.xml.expression.BinaryOperator;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.UnaryOperator;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ValueComparator;
import com.volantis.xml.expression.impl.jxpath.JXPathExpression;
import com.volantis.xml.expression.sequence.Sequence;
import our.apache.commons.jxpath.JXPathException;
import our.apache.commons.jxpath.ri.compiler.Expression;
import our.apache.commons.jxpath.ri.compiler.ExpressionPath;
import our.apache.commons.jxpath.ri.compiler.Step;
import our.apache.commons.jxpath.ri.compiler.TreeCompiler;

/**
 * This compiler allows us to provide our own operator tests
 * that handle the {@link com.volantis.xml.expression.Value} instances and
 * java ("boxed") types.
 */
public class JXPathCompiler extends TreeCompiler {
    // javadoc inherited
    public Object equal(Object left, Object right) {
        return new JXPathOperationEqual((Expression)left,
                                        (Expression)right);
    }

    // javadoc inherited
    public Object notEqual(Object left, Object right) {
        return new JXPathOperationNotEqual((Expression)left,
                                           (Expression)right);
    }

    // javadoc inherited
    public Object lessThan(Object left, Object right) {
        return new JXPathOperationLessThan((Expression)left,
                                           (Expression)right);
    }

    // javadoc inherited
    public Object lessThanOrEqual(Object left, Object right) {
        return new JXPathOperationLessThanOrEqual((Expression)left,
                                                  (Expression)right);
    }

    // javadoc inherited
    public Object greaterThan(Object left, Object right) {
        return new JXPathOperationGreaterThan((Expression)left,
                                              (Expression)right);
    }

    // javadoc inherited
    public Object greaterThanOrEqual(Object left, Object right) {
        return new JXPathOperationGreaterThanOrEqual((Expression)left,
                                                     (Expression)right);
    }

    // javadoc inherited
    public Object and(Object[] args) {
        return new JXPathOperationAnd(convertToExpressionArray(args),
                                      getExpressionFactory());
    }

    // javadoc inherited
    public Object or(Object[] args) {
        return new JXPathOperationOr(convertToExpressionArray(args),
                                     getExpressionFactory());
    }

    // javadoc inherited
    public Object function(int code, Object args) {
        JXPathExpressionList expList = (JXPathExpressionList) args;
        Expression expArray[] = null;
        if (expList != null) {
        	expArray = expList.toArray();
        }
        return new JXPathCoreFunction(code, expArray,
                                           getExpressionFactory());
    }
    
    // javadoc inherited
    public Object sum(Object[] args) {
        return new JXPathOperationAdd(convertToExpressionArray(args),
                getExpressionFactory());
    }

    // javadoc inherited
    public Object minus(Object left, Object right) {
        return new JXPathOperationSubtract((Expression) left,
                (Expression) right, getExpressionFactory());
    }

    // javadoc inherited
    public Object multiply(Object left, Object right) {
        return new JXPathOperationMultiply((Expression) left,
                (Expression) right, getExpressionFactory());
    }

    // javadoc inherited
    public Object divide(Object left, Object right) {
        return new JXPathOperationDivide((Expression) left, (Expression) right,
                getExpressionFactory());
    }

    // javadoc inherited
    public Object mod(Object left, Object right) {
        return new JXPathOperationMod((Expression) left, (Expression) right,
                getExpressionFactory());
    }

    // javadoc inherited
    public Object minus(Object arg) {
        return new JXPathOperationNegate((Expression) arg,
                getExpressionFactory());
    }

    public Object expressionList(Object[] arguments) {
        return new JXPathExpressionList(convertToExpressionArray(arguments),
                 getExpressionFactory());
    }    
    
    /**
     * Takes an array of Objects and casts each Object to an Expression,
     * retuning a new Expression array of Expression Objects
     * 
     * NOTE - The base class provides identical functionality via the
     * {@link TreeCompiler#toExpressionArray} method. Unfortunately,this is
     * private so we cannot call it from this subclass. As the TreeCompiler
     * class is part of the JXPath source we should not change this methods
     * protection level.
     * 
     * @param array
     *            an array of Objects that are "instanceof" the Expression class
     * @return an array of Expression objects.
     */
    private Expression[] convertToExpressionArray(Object[] array) {
        Expression expArray[] = null;
        if (array != null) {
            expArray = new Expression[array.length];
            for (int i = 0; i < expArray.length; i++) {
                expArray[i] = (Expression)array[i];
            }
        }
        return expArray;
    }

    /**
     * Helper function that the comparison operators (<, <=, >, >=, !=, ==)
     * use in order compare two items.
     * @param left the left item
     * @param right the right item
     * @param comparator the <code>ValueComparator</code> that "compares"
     * the left item to the right.
     * @return true if and only if the comparison succeeded
     */
    static boolean compare(Object left,
                           Object right,
                           ValueComparator comparator) {
        boolean result = false;

        try {
            ExpressionFactory factory = getExpressionFactory();

            Sequence lseq = JXPathExpression.asValue(factory, left).
                    getSequence();
            Sequence rseq = JXPathExpression.asValue(factory, right).
                    getSequence();

            result = PipelineExpressionHelper.compare(lseq, rseq, comparator);
        } catch (ExpressionException e) {
            // Tunnel the exception out in a JXPath runtime exception
            throw new JXPathException(
                    "Illegal argument to comparison: " + e);
        }
        return result;
    }

    /**
     * Helper function that the binary operators (such as addition,
     * multiplication etc.) use in order calculate the value for two items.
     * 
     * @param left
     *            the left item
     * @param right
     *            the right item
     * @param operator
     *            the <code>BinaryOperator</code> that performs the arithmetic
     *            operation on items.
     * @return the computed value
     */
    static Value compute(Object left, Object right, BinaryOperator operator) {
        Value result;

        try {
            ExpressionFactory factory = getExpressionFactory();

            Sequence lseq = JXPathExpression.asValue(factory, left)
                    .getSequence();
            Sequence rseq = JXPathExpression.asValue(factory, right)
                    .getSequence();

            result = PipelineExpressionHelper.compute(lseq, rseq, operator);
        } catch (ExpressionException e) {
            // Tunnel the exception out in a JXPath runtime exception
            throw new JXPathException("Illegal argument to operation: " + e);
        }
        return result;
    }

    /**
     * Helper function that the unary operators (such as negation) use in order
     * calculate the value for the item.
     * 
     * @param arg
     *            the item
     * @param operator
     *            the <code>UnaryOperator</code> that performs the arithmetic
     *            operation on item.
     * @return the computed value
     */
    static Value compute(Object arg, UnaryOperator operator) {
        Value result;

        try {
            ExpressionFactory factory = getExpressionFactory();

            Sequence seq = JXPathExpression.asValue(factory, arg).getSequence();

            result = PipelineExpressionHelper.compute(seq, operator);
        } catch (ExpressionException e) {
            // Tunnel the exception out in a JXPath runtime exception
            throw new JXPathException("Illegal argument to operation: " + e);
        }
        return result;
    }

    /**
     * Returns an <code>ExpressionFactory</code> instance.
     * 
     * @return an ExpressionFactory instance
     */
    static ExpressionFactory getExpressionFactory() {
        // @todo if this compiler maintained state we could hold a factory
        // Use the default factory because JXPathExpressionParser
        // uses a single, stateless instance of this class!
        return ExpressionFactory.getDefaultInstance();
    }

    // javadoc inherited
    public Object expressionPath(Object expression, Object[] predicates,
            Object[] steps) {
        
        return new ExpressionPath(
                (Expression) expression,
                toPredicatesArray(predicates),
                toStepArray(steps));
    }
    
    /**
     * Creates array of {@link Step}s from given array of {@link Object}s.
     *  
     * @param array input array. All elements of argument array must be 
     * instances of {@link Step}
     * @return an array of {@link Step}s
     */
    private Step[] toStepArray(Object[] array) {
        Step stepArray[] = null;
        if (array != null) {
            stepArray = new Step[array.length];
            for (int i = 0; i < stepArray.length; i++) {
                stepArray[i] = (Step) array[i];
            }
        }
        return stepArray;
    }
    
    /**
     * Creates array of {@link Expression}s from given array of {@link Object}s.
     * It is intended to be used for array of predicates. All items are wrapped
     * in {@link ConvertingToJavaObjectExpressionWrapper}
     *  
     * @param array input array. All elements of argument array must be 
     * instances of {@link Step}
     * @return an array of {@link Step}s
     */
    private Expression[] toPredicatesArray(Object[] array) {
        Expression predicatesArray[] = null;
        if (array != null) {
            predicatesArray = new Expression[array.length];
            for (int i = 0; i < predicatesArray.length; i++) {
                predicatesArray[i] = new ConvertingToJavaObjectExpressionWrapper(
                        (Expression) array[i]);
            }
        }
        return predicatesArray;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Jan-06	10855/5	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expressions.

 04-Jan-06	10855/3	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expression.

 28-Dec-05	10855/1	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expressions.

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 27-Oct-03	433/1	doug	VBM:2003102002 Added several new comparison operators

 11-Aug-03	341/1	philws	VBM:2003081107 Make ExpressionHelper.equals(Sequence, Sequence) available

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
