/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.NodeValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.atomic.numeric.NumericValue;
import com.volantis.xml.expression.atomic.temporal.DateTimeValue;
import com.volantis.xml.expression.atomic.temporal.DateValue;
import com.volantis.xml.expression.atomic.temporal.DurationValue;
import com.volantis.xml.expression.atomic.temporal.TimeValue;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 * This class provides helper methods for the manipulation of
 * {@link Expression}s and their evaluated values.
 *
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-exclude-from ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class PipelineExpressionHelper {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(PipelineExpressionHelper.class);
    
    /**
     * Used for localizing exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(PipelineExpressionHelper.class);

    /**
     * The prefix for an Pipeline expression string.
     */
    private static final String[] PIPELINE_EXPR_PREFIX = {"%{"};

    /**
     * The suffix for an Pipeline expression string.
     */
    private static final String[] PIPELINE_EXPR_SUFFIX = {"}"};

    /**
     * The prefix for an encoded expression string.
     */
    private static final String[] ESCAPED_PIPELINE_EXPR_PREFIX = {"\\%{"};

    /**
     * ValueComparator for equality operations
     */
    private static ValueComparator EQUALITY_COMPARATOR =
            new com.volantis.xml.expression.ValueComparator() {

                // javadoc inherited
                public boolean compare(StringValue left, StringValue right) {
                    return PipelineExpressionHelper.compare(left,
                                                            right) == 0;
                }

                // javadoc inherited
                public boolean compare(BooleanValue left, BooleanValue right) {
                    return PipelineExpressionHelper.compare(left,
                                                            right) == 0;
                }

                // javadoc inherited
                public boolean compare(NumericValue left, NumericValue right) {
                    return PipelineExpressionHelper
                            .compareNumeric(left, right) == CompareResult.EQUAL;
                }

                // javadoc inherited
                public boolean compare(NodeValue left, NodeValue right) {
                    return PipelineExpressionHelper.compare(left, right) == 0;
                }
            };

    /**
     * Check if the specified String is an encoded expression - that is to say
     * it is formatted as %{string}.
     *
     * @param expression The String to check for valid expression markup.
     * @return true if the specified String is an expression.
     */
    public static boolean isPipelineQuotedExpression(String expression) {

        return isPipelineQuotedExpression(expression,
                                          PIPELINE_EXPR_PREFIX,
                                          PIPELINE_EXPR_SUFFIX);
    }

    /**
     * Check if the specified String is an encoded expression - that is to say
     * it is formatted as %{string}.  If the prefix or suffix arrays are empty,
     * both arrays are set to the default values.
     *
     * @param expression The String to check for valid expression markup.
     * @param prefixes the possible prefixes.
     * @param suffixes the possible suffixes.
     * @return true if the specified String is an expression.
     */
    public static boolean isPipelineQuotedExpression(String expression,
                                                     String[] prefixes,
                                                     String[] suffixes) {

        return isExpression(expression,
                            prefixes,
                            suffixes,
                            PIPELINE_EXPR_PREFIX,
                            PIPELINE_EXPR_SUFFIX);
    }

    /**
     * Get the specified encoded expression String without the expression
     * encoding prefix and suffix.
     *
     * @param encodedExpr The String from which to remove the encoded
     *                    expression prefix and suffic.
     * @return The unencoded expression String.
     */
    public static String removePipelineQuoting(String encodedExpr) {
        return removePipelineQuoting(encodedExpr,
                                     PIPELINE_EXPR_PREFIX,
                                     PIPELINE_EXPR_SUFFIX);
    }

    /**
     * Get the specified encoded expression String without the expression
     * encoding prefix and suffix.  The prefix and suffix are taken to be the
     * first matching pair from the prefixes and suffixes parameters.  If the
     * prefix or suffix arrays are empty, both arrays are set to the default
     * values.
     *
     * @param encodedExpr The String from which to remove the encoded
     *                    expression prefix and suffix.
     * @param prefixes an array of possible prefixes. These are paired with
     * the suffixes parameter on a prefixes[i] -> suffixes[i] basis.
     * @param suffixes an array of possible suffixes. These are paired with
     * the prefixes parameter on a prefixes[i] -> suffixes[i] basis.
     * @return The unencoded expression String.
     */
    public static String removePipelineQuoting(String encodedExpr,
                                               String[] prefixes,
                                               String[] suffixes) {

        String result = encodedExpr;

        if (null != encodedExpr) {

            if ((0 == prefixes.length) || (0 == suffixes.length)) {
                prefixes = PIPELINE_EXPR_PREFIX;
                suffixes = PIPELINE_EXPR_SUFFIX;
            }

            int index = getExpressionDeclarationIndex(encodedExpr,
                                                      prefixes,
                                                      suffixes);
            if (-1 < index) {
                result = encodedExpr.substring(prefixes[index].length(),
                                               encodedExpr.length() -
                                               suffixes[index].length());
            }
        }

        return result;
    }

    /**
     * Check if the specified String is an escaped encoded expression - that is
     * to say it is formatted as \%{string}.
     *
     * @param expression The String to check for valid expression markup.
     * @return true if the specified String is an escaped expression.
     */
    public static boolean isEscapedPipelineQuotedExpression(String expression) {

        return isEscapedPipelineQuotedExpression(expression,
                                                 ESCAPED_PIPELINE_EXPR_PREFIX,
                                                 PIPELINE_EXPR_SUFFIX);
    }

    /**
     * Check if the specified String is an escaped encoded expression - that is
     * to say it is formatted as %{string}.  If the prefix or suffix arrays are
     * empty, both arrays are set to the default values.
     *
     * @param expression The String to check for valid expression markup.
     * @param prefixes the possible prefixes.
     * @param suffixes the possible suffixes.
     * @return true if the specified String is an expression.
     */
    public static boolean isEscapedPipelineQuotedExpression(String expression,
                                                            String[] prefixes,
                                                            String[] suffixes) {

        return isExpression(expression,
                            prefixes,
                            suffixes,
                            ESCAPED_PIPELINE_EXPR_PREFIX,
                            PIPELINE_EXPR_SUFFIX);
    }

    /**
     * Check if the specified String is an escaped encoded expression - that is
     * to say it is formatted as %{string}.  If the prefix or suffix arrays are
     * empty, both arrays are set to the default values.
     *
     * @param expression The String to check for valid expression markup.
     * @param prefixes the possible prefixes.
     * @param suffixes the possible suffixes.
     * @return true if the specified String is an expression.
     */
    private static boolean isExpression(String expression,
                                        String[] prefixes,
                                        String[] suffixes,
                                        String[] defaultPrefixes,
                                        String[] defaultSuffixes) {

        int index = -1;

        if (null != expression) {
            if ((0 == prefixes.length) || (0 == suffixes.length)) {
                prefixes = defaultPrefixes;
                suffixes = defaultSuffixes;
            }

            index = getExpressionDeclarationIndex(expression,
                                                  prefixes,
                                                  suffixes);

        }

        return (-1 < index);
    }

    /**
     * Gets the index of the prefix/suffix the expression uses as declarative
     * markup.
     *
     * @param expression The String to check is an expression.
     * @param prefixes the possible prefixes.
     * @param suffixes the possible suffixes.
     * @return the index of the prefix/suffix, or -1 if it is not found.
     */
    private static int getExpressionDeclarationIndex(String expression,
                                                     String[] prefixes,
                                                     String[] suffixes) {

        int index = -1;

        if (null != expression) {
            for (int i = 0; i < prefixes.length; i++) {
                if (expression.startsWith(prefixes[i]) &&
                    expression.endsWith(suffixes[i])) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }


    /**
     * Get the specified escaped encoded expression String without the escape
     * prefix character.
     *
     * @param escapedExpr The String from which to remove the escape prefix.
     * @return The unescaped expression String.
     */
    public static String getUnescapedPipelineQuotedExpression(
            String escapedExpr) {

        String result = null;

        if (escapedExpr != null) {
            result = escapedExpr.substring(1);
        }

        return result;
    }

    /**
     * Permits two sequences to be compared. The type of comparison
     * (<, <=, >, >=, ==, !=, etc) is determined via a
     * {@link ValueComparator} instance.
     * Each item in the left sequence is compared with each item in the right
     * sequence until a match is found or all items have been visited. This is
     * computationally expensive if the left and right sequences are of
     * moderate or large size.
     *
     * <p>An exception will be thrown if an item from the left sequence is of
     * an incompatible type (e.g. compare string with boolean) with an item
     * from the right sequence and the comparison isn't deemed <tt>true</tt>
     * before the given pair are to be compared.</p>
     *
     * <p><strong>Only {@link StringValue}, {@link NumericValue} and {@link
     * BooleanValue} types are currently supported as comparable item
     * types.</strong></p>
     *
     * @see <a href="http://www.w3.org/TR/xpath20/#id-general-comparisons">
     * http://www.w3.org/TR/xpath20/#id-general-comparisons</a>
     *
     * @param l left hand sequence operand
     * @param r right hand sequence operand
     * @return <tt>true</tt> if the l comparison with r is true,
     * following sequence comparison rules as defined by XPath 2.0
     * @throws ExpressionException if an error occurs
     */
    public static boolean compare(Sequence l,
                                  Sequence r,
                                  ValueComparator comparator)
            throws ExpressionException {
        boolean result = false;
        Value left;
        Value right;

        // Iterate as needed
        for (int j, i = 1;
             !result && (i <= l.getLength());
             i++) {
            left = l.getItem(i);

            if (!((left instanceof StringValue) ||
                  (left instanceof NumericValue) ||
                  (left instanceof BooleanValue) ||
                  (left instanceof NodeValue))) {
                throw new ExpressionException("left hand item at index " + i +
                                              "is of a type not supported by the comparison " +
                                              "operations (" + left.getClass()
                                                               .getName() +
                                              ')');
            }

            for (j = 1;
                 !result && (j <= r.getLength());
                 j++) {
                right = r.getItem(j);

                if (!((right instanceof StringValue) ||
                      (right instanceof NumericValue) ||
                      (right instanceof BooleanValue) ||
                      (right instanceof NodeValue))) {
                    throw new ExpressionException("right hand item at index " +
                                                  j +
                                                  "is of a type not supported by the comparison " +
                                                  "operator (" + right.getClass()
                                                                 .getName() +
                                                  ')');
                } else {
                    // Determine the type of comparison/equality test
                    // required. We compare "like with like" only (see
                    // the else comment below).
                    if ((left instanceof StringValue) &&
                        (right instanceof StringValue)) {
                        result = comparator.compare((StringValue) left,
                                                    (StringValue) right);
                    } else if ((left instanceof NumericValue) &&
                               (right instanceof NumericValue)) {
                        result = comparator.compare((NumericValue) left,
                                                    (NumericValue) right);
                    } else if ((left instanceof BooleanValue) &&
                               (right instanceof BooleanValue)) {
                        result = comparator.compare((BooleanValue) left,
                                                    (BooleanValue) right);
                    } else if ((left instanceof NodeValue) &&
                               (right instanceof NodeValue)) {
                        result = comparator.compare((NodeValue) left,
                                                    (NodeValue) right);
                    } else {
                        // The XPath 2.0 specification states that a
                        // dynamic error should be raised if there is
                        // a problem in comparing the two operands. Since
                        // we don't implement XPath 1.0 compatibility mode
                        // (and therefore casting from string to numeric
                        // etc.). Since we can't compare like with like,
                        // we raise that dynamic error
                        throw new ExpressionException("Cannot compare the two operands as we can " +
                                                      "only compare like with like: " +
                                                      "left[" + i +
                                                      "] is of type " +
                                                      left.getClass().getName() +
                                                      " value \"" +
                                                      left.stringValue()
                                                      .asJavaString() +
                                                      "\" and " +
                                                      "right[" + j +
                                                      "] is of type " +
                                                      right.getClass()
                                                      .getName() +
                                                      " value \"" +
                                                      right.stringValue()
                                                      .asJavaString() + '\"');
                    }
                }
            }
        }

        return result;
    }

    /**
     * Permits an operation on two sequences to be computed. The type of
     * operation (such as addition, multiplication, etc.) is determined via
     * the {@link BinaryOperator} implementation provided.
     * 
     * If the operand is a {@link Sequence} of length = 1 the one and only item 
     * of the {@link Sequence} is taken into the operation.
     * 
     * If any of the operands is an empty {@link Sequence} the value is an 
     * empty {@link Sequence}.
     * 
     * If any of the operands is a {@link Sequence} of length > 1 the 
     * exception is thrown.
     * 
     * <p><strong>Only {@link IntValue} and {@link DoubleValue} types are 
     * currently supported as item types.</strong></p> <p>An exception will be 
     * thrown if either item is of an incompatible type (e.g. string or 
     * boolean).
     *
     * @param l left hand sequence operand
     * @param r right hand sequence operand
     * @param operator the binary operator being applied
     * @return the computed value
     * @throws ExpressionException if an error occurs
     */
    public static Value compute(Sequence l, Sequence r, BinaryOperator operator)
                        throws ExpressionException {
        Value result;
        Value left;
        Value right;

        if (l.getLength() == 0 || r.getLength() == 0) {
            result = Sequence.EMPTY;
        } else if (l.getLength() > 1 || r.getLength() > 1){
            throw new ExpressionException("Error when evaluating the subtract function. " +
                    "Operand cannot be a sequence of the length greather then 1.");
        } else {
            // get single values from sequences
            left = l.getItem(1);
            right = r.getItem(1);
                
            if ((left instanceof DoubleValue) && (right instanceof DoubleValue)) {
                result = operator.compute((DoubleValue)left, (DoubleValue)right);
            } else if ((left instanceof DoubleValue) && (right instanceof IntValue)) {
                result = operator.compute((DoubleValue)left, (IntValue)right);
            } else if ((left instanceof IntValue) && (right instanceof DoubleValue)) {
                result = operator.compute((IntValue)left, (DoubleValue)right);
            } else if ((left instanceof IntValue) && (right instanceof IntValue)) {
                result = operator.compute((IntValue)left, (IntValue)right);
            } else if ((right instanceof DurationValue) &&
                    (operator instanceof TemporalOperator)) {
                TemporalOperator temporalOperator = (TemporalOperator)operator;
                DurationValue durationValue = (DurationValue)right;
                if (left instanceof DateTimeValue) {
                    result = temporalOperator.compute((DateTimeValue)left, durationValue);
                } else if (left instanceof DurationValue) {
                    result = temporalOperator.compute((DurationValue)left, durationValue);
                } else if (left instanceof DateValue) {
                    result = temporalOperator.compute((DateValue)left, durationValue);
                } else {
                    // the only two numeric types IntValue
                    // and Double value are supprted
                    throw new ExpressionException("Cannot compute the value of" +
                            "the operator: " +
                            "left is of type " + left.getClass().getName() +
                            " value \"" + left.stringValue().asJavaString() +
                            "\" and right is of type " + right.getClass().getName() +
                            " value \"" + right.stringValue().asJavaString() + '\"');
                }
            } else {
                // the only two numeric types IntValue
                // and Double value are supprted
                throw new ExpressionException("Cannot compute the value of" +
                        "the operator: " +
                        "left is of type " + left.getClass().getName() +
                        " value \"" + left.stringValue().asJavaString() +
                        "\" and right is of type " + right.getClass().getName() +
                        " value \"" + right.stringValue().asJavaString() + '\"');
            }
            
        }

        return result;
    }

    /**
     * Permits a unary operation on a sequence to be computed. The type of 
     * operation (such as negation) is determined via the
     * {@link UnaryOperator} implementation provided.
     * 
     * If the operand is a {@link Sequence} of length = 1 the one and only item 
     * of the {@link Sequence} is taken into the operation.
     * 
     * If the operand is an empty {@link Sequence} the value is an 
     * empty {@link Sequence}.
     * 
     * If the operand is a {@link Sequence} of length > 1 the 
     * exception is thrown.
     * 
     * <p><strong>Only {@link IntValue} and {@link DoubleValue} types are 
     * currently supported as item type.</strong></p> <p>An exception will be 
     * thrown if the item is of an incompatible type (e.g. string or 
     * boolean).
     *
     * @param s single sequence operand
     * @param operator the unary operator being applied
     * @return the computed value
     * @throws ExpressionException if an error occurs
     */
    public static Value compute(Sequence s, UnaryOperator operator)
                        throws ExpressionException {
        Value result;
        Value arg;

        if (s.getLength() == 0) {
            result = Sequence.EMPTY;
        } else if (s.getLength() > 1){
            throw new ExpressionException("Error when evaluating the subtract function. " +
                    "Operand cannot be a sequence of the length greather then 1.");
        } else {
            // get single value from sequences
            arg = s.getItem(1);

            if (arg instanceof NumericValue) {
                
                if (arg instanceof DoubleValue) {
                    result = operator.compute((DoubleValue)arg);
                } else if (arg instanceof IntValue) {
                    result = operator.compute((IntValue)arg);
                } else { 
                    // the only two numeric types IntValue 
                    // and Double value are supprted
                    throw new ExpressionException("Cannot compute the value of" +
                            "the operator as we can only operate on numeric types: " +
                            "operand is of type " + arg.getClass().getName() +
                            " value \"" + arg.stringValue().asJavaString() + "\"");
                }
            } else {
                // The XPath 2.0 specification states that a dynamic error 
                // should be raised if the types of the operands are not 
                // a valid combination for the given operator. Since
                // we don't implement XPath 1.0 compatibility mode
                // (and therefore casting from string to numeric
                // etc.) and we can't add or subtract non numeric,
                // we raise that dynamic error
                throw new ExpressionException("Cannot compute the value of" +
                        "the operator as we can only operate on numeric types: " +
                        "operand is of type " + arg.getClass().getName() +
                        " value \"" + arg.stringValue().asJavaString() + "\"");
            }
            
        }

        return result;
    }
    
    /**
     * Permits two sequences to be tested for equality.
     * Each item in the left sequence is tested for equality with each item
     * in the right sequence until a match is found or all items have been
     * visited. This is computationally expensive if the left and right
     * sequences are of moderate or large size.
     *
     * <p>An exception will be thrown if an item from the left sequence is of
     * an incompatible type (e.g. compare string with boolean) with an item
     * from the right sequence and the comparison isn't deemed <tt>true</tt>
     * before the given pair are to be compared.</p>
     *
     * <p><strong>Only {@link StringValue}, {@link NumericValue} and {@link
     * BooleanValue} types are currently supported as comparable item
     * types.</strong></p>
     *
     * <p><strong>This method must not be used to test for inequlity</strong>
     * </p>
     *
     * @param l left hand sequence operand
     * @param r right hand sequence operand
     * @return <tt>true</tt> if l is equal to r
     * @throws ExpressionException if an error occurs
     */
    public static boolean equals(Sequence l,
                                 Sequence r) throws ExpressionException {

        return compare(l,
                       r,
                       EQUALITY_COMPARATOR);
    }

    /**
     * Models the XPath 2.0 fn:compare.
     * See <a href="http://www.w3.org/TR/xquery-operators/#func-compare">
     * Compare</a>.
     *
     * <p>I suspect that the
     * <a href="http://www.w3.org/TR/xpath20/#mapping">Operator Mapping</a>
     * table incorrectly states how the string equality is performed since the
     * fn:compare is defined to return 0 when the values are equal (just as
     * the {@link String#compareTo} method does).</p>
     *
     * @param l left hand string operand
     * @param r right hand string operand
     * @return a comparison value derived from {@link String#compareTo}
     */
    public static int compare(StringValue l,
                              StringValue r) {

        return l.asJavaString().compareTo(r.asJavaString());
    }

    /**
     * Models the Models the XPath 2.0 op:boolean-equal, op:boolean-less-than
     * and op:boolean-greater-than functions.
     *
     * <a href="http://www.w3.org/TR/xquery-operators/#func-boolean-equal">
     * Boolean Equal</a>.
     * <a href="http://www.w3.org/TR/xquery-operators/#func-boolean-less-than">
     * Boolean Numeric Less Than</a>.
     * <a href="http://www.w3.org/TR/xquery-operators/#func-boolean-greater-than">
     * Boolean Greater Than</a>.
     *
     * @param l left hand numeric operand
     * @param r right hand numeric operand
     * @return 0 if both the l and r operands are the same boolean value,
     *  1 if r is true l is false, and -1 if r is false and l is true.
     */
    public static int compare(BooleanValue l,
                              BooleanValue r) {

        int left = l.asJavaBoolean() ? 1 : 0;
        int right = r.asJavaBoolean() ? 1 : 0;

        return (right - left);
    }

    /**
     * According to the W3C specification the only comparison available
     * between two nodes is to test against document position. However we
     * do not currently support document position information and thus cannot
     * support this feature fully, simply returning the equality comparison
     * if the two nodes are literally the same node instance and an arbitrary
     * inequality value otherwise.
     * See: <a href="http://www.w3.org/TR/xquery-operators/#func-is-same-node">
     * op:is-same-node</a>, 
     * <a href="http://www.w3.org/TR/xquery-operators/#func-node-before">
     * op:node-before</a>,
     * <a href="http://www.w3.org/TR/xquery-operators/#func-node-after">
     * op:node-after</a>.
     * 
     * @todo Please note that the DOM API we are currently using is not
     * the DOM 3 level API. That mean {@link Node} doesn't contain the
     * <code>isSameNode</code> method.
     * We are currently comparing objects by reference what is not best
     * solution because, depends on the DOM API implementation, 
     * {@link Node} implementation instance can be proxy object what will cause
     * actual test to fail.
     * For comparing document positions there can be implemented our own
     * functionality or it may be good solution to use the
     * <code>compareDocumentPosition</code> method of the DOM level 3 API.
     * 
     * @param left left node value operand
     * @param right right node value operand
     * @return 0 if nodes are the same, 1 otherwise
     */
    public static int compare(NodeValue left, NodeValue right) {
        return left.asW3CNode() == right.asW3CNode() ? 0 : 
            1;
    }

    /**
     * Models the XPath 2.0 op:numeric-equal, op:numeric-less-than
     * and op:numeric-greater-than functions.
     *
     * <a href="http://www.w3.org/TR/xquery-operators/#func-numeric-equal">
     * <a href="http://www.w3.org/TR/xquery-operators/#func-numeric-less-than">
     * Numeric Numeric Less Than</a>.
     * <a href="http://www.w3.org/TR/xquery-operators/#func-numeric-greater-than">
     * Numeric Greater Than</a>.
     *
     * Operands are automatically promoted to double values
     * before the comparison is made.
     *
     * <p><strong>Only supports {@link IntValue} and {@link DoubleValue}
     * numeric values.</strong></p>
     *
     * @deprecated use {@link PipelineExpressionHelper#compareNumeric(NumericValue, NumericValue)}
     * @param l left hand numeric operand
     * @param r right hand numeric operand
     * @return 0 if l is equal to r, 1 if r is greater than l and
     * -1 if r is less than l.
     */
    public static int compare(NumericValue l, NumericValue r) {

        double left;
        double right;

        if (l instanceof IntValue) {
            left = ((IntValue) l).asJavaInt();
        } else {
            left = ((DoubleValue) l).asJavaDouble();
        }

        if (r instanceof IntValue) {
            right = ((IntValue) r).asJavaInt();
        } else {
            right = ((DoubleValue) r).asJavaDouble();
        }
        int result = (right == left) ? 0 : (right > left) ? 1 : -1;
        return result;
    }
    
    /**
     * Models the Models the XPath 2.0 op:numeric-equal, op:numeric-less-than
     * and op:numeric-greater-than functions.
     * 
     * <a href="http://www.w3.org/TR/xquery-operators/#func-numeric-equal"> <a
     * href="http://www.w3.org/TR/xquery-operators/#func-numeric-less-than">
     * Numeric Numeric Less Than</a>. <a
     * href="http://www.w3.org/TR/xquery-operators/#func-numeric-greater-than">
     * Numeric Greater Than</a>.
     * 
     * Operands are automatically promoted to double values before the
     * comparison is made.
     * 
     * <p>
     * <strong>Only supports {@link IntValue} and {@link DoubleValue} numeric
     * values.</strong>
     * </p>
     * 
     * @param l left hand numeric operand
     * @param r right hand numeric operand
     * @return {@link CompareResult#EQUAL} if l is equal to r,
     *         {@link CompareResult#GREATER_THAN} if r is greater than l,
     *         {@link CompareResult#LESS_THAN} if r is less than l and
     *         {@link CompareResult#INCOMPARABLE} when l or r is a
     *         {@link Double#NaN}
     */
    public static CompareResult compareNumeric(NumericValue l, NumericValue r) {
        
        double left;
        double right;
        
        if (l instanceof IntValue) {
            left = ((IntValue) l).asJavaInt();
        } else {
            left = ((DoubleValue) l).asJavaDouble();
        }
        
        if (r instanceof IntValue) {
            right = ((IntValue) r).asJavaInt();
        } else {
            right = ((DoubleValue) r).asJavaDouble();
        }
        
        CompareResult result;
        if (Double.isNaN(left) || Double.isNaN(right)) {
            result = CompareResult.INCOMPARABLE;
        } else {
            result = (right == left) ? CompareResult.EQUAL
                    : (right > left) ? CompareResult.GREATER_THAN
                            : CompareResult.LESS_THAN;
        }
        return result;
    }
    
    /**
     * Compares two {@link Value}s and returns an instance of {@link CompareResult}.
     * Values are compared if they are of the same type and the type is
     * string, numeric, boolean, datetime, date or time. Otherwise 
     * {@link CompareResult#INCOMPARABLE} is returned. {@link DoubleValue#NOT_A_NUMBER}
     * is also incomparable with any numeric value.
     * 
     * @param l first value
     * @param r second value
     * @return {@link CompareResult#EQUAL} if values are equal,
     * {@link CompareResult#LESS_THAN} if second value is less than first
     * {@link CompareResult#GREATER_THAN} if second value is greater than first
     * {@link CompareResult#INCOMPARABLE} if values are incomparable  
     */
    public static CompareResult compare(Value l, Value r) {
        int intResult = 0;
        CompareResult result = null;
        if (l instanceof StringValue && r instanceof StringValue) {
            intResult = compare((StringValue) l, (StringValue) r);
        } else if (l instanceof NumericValue && r instanceof NumericValue) {
            return compareNumeric((NumericValue) l, (NumericValue) r);
        } else if (l instanceof BooleanValue && r instanceof BooleanValue) {
            intResult = compare((BooleanValue) l, (BooleanValue) r);
        } else if (l instanceof DateTimeValue && r instanceof DateTimeValue) {
            intResult = compare((DateTimeValue) l, (DateTimeValue) r);
        } else if (l instanceof DateTimeValue) {
            result = CompareResult.INCOMPARABLE;
        } else if (r instanceof DateTimeValue) {
            result = CompareResult.INCOMPARABLE;
        } else if (l instanceof DateValue && r instanceof DateValue) {
            intResult = compare((DateValue) l, (DateValue) r);
        } else if (l instanceof TimeValue && r instanceof TimeValue) {
            intResult = compare((TimeValue) l, (TimeValue) r);
        } else {
            result = CompareResult.INCOMPARABLE;
        }
        if (result == null) {
            if (intResult < 0) {
                result = CompareResult.GREATER_THAN;
            } else if (intResult > 0) {
                result = CompareResult.LESS_THAN;
            } else {
                result = CompareResult.EQUAL;
            }
        }
        return result;
    }
    
    /**
     * Compares two {@link DateTimeValue}s
     * 
     * @param l first datetime value
     * @param r second datetime value
     * @return -1 if first datetime value is earlier, 
     * 1 if second datetime value is earlier
     * 0 if they are equal
     */
    public static int compare(DateTimeValue l, DateTimeValue r) {
        long difference = getCalendar(l).getTimeInMillis()-getCalendar(r).getTimeInMillis();
        return difference < 0 ? -1 : (difference > 0 ? 1 : 0);
    }
    
    /**
     * Compares two {@link DateValue}s
     * 
     * @param l first date value
     * @param r second date value
     * @return -1 if first date value is earlier, 
     * 1 if second date value is earlier
     * 0 if they are equal
     */
    public static int compare(DateValue l, DateValue r) {
        int result = l.getYear() - r.getYear();
        if (result == 0) {
            result = l.getMonth() - r.getMonth();
            if (result == 0) {
                result = l.getDay() - r.getDay();
            }
        }
        return result;
    }
    
    /**
     * Compares two {@link TimeValue}s
     * 
     * @param l first time value
     * @param r second time value
     * @return -1 if first time value is earlier, 
     * 1 if second time value is earlier
     * 0 if they are equal
     */
    public static int compare(TimeValue l, TimeValue r) {
        long difference = getCalendar(l).getTimeInMillis()-getCalendar(r).getTimeInMillis();
        return difference < 0 ? -1 : (difference > 0 ? 1 : 0);
    }
    
    /**
     * Creates {@link Calendar} and initializes it with values from
     * specified time
     * 
     * @param time {@link TimeValue} representing the time to 
     * initialize calendar
     * @return the calendar instance
     */
    private static Calendar getCalendar(TimeValue time) {
        Calendar calendar = Calendar.getInstance(time.isTimezoned() ? 
                time.getTimeZone() : TimeZone.getDefault());
        calendar.clear();
        calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
        calendar.set(Calendar.MINUTE, time.getMinutes());
        calendar.set(Calendar.SECOND, time.getSeconds());
        calendar.set(Calendar.MILLISECOND, time.getMilliseconds());
        return calendar;
    }
    
    /**
     * Creates {@link Calendar} and initializes it with values from
     * specified datetime
     * 
     * @param datetime {@link DateTimeValue} representing the datetime to 
     * initialize calendar
     * @return the calendar instance
     */
    private static Calendar getCalendar(DateTimeValue datetime) {
        Calendar calendar = Calendar.getInstance(datetime.isTimezoned() ? 
                datetime.getTimeZone() : TimeZone.getDefault());
        calendar.clear();
        calendar.set(Calendar.YEAR, datetime.getYear());
        calendar.set(Calendar.MONTH, datetime.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datetime.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, datetime.getHours());
        calendar.set(Calendar.MINUTE, datetime.getMinutes());
        calendar.set(Calendar.SECOND, datetime.getSeconds());
        calendar.set(Calendar.MILLISECOND, datetime.getMilliseconds());
        return calendar;
    }
    
    /**
     * Parses the number represented by string and returns it as double. 
     * 
     * @param string string representation of a number 
     * @return parsed number as double
     * @throws ExpressionException
     */
    public static double stringToDouble(StringValue string) throws ExpressionException {
        double doubleValue;
        String javaString = string.asJavaString();
        if ("inf".equalsIgnoreCase(javaString) || "+inf".equalsIgnoreCase(javaString)) {
            doubleValue = Double.POSITIVE_INFINITY;
        } else if ("-inf".equalsIgnoreCase(javaString)) {
            doubleValue = Double.NEGATIVE_INFINITY;
        } else if ("nan".equalsIgnoreCase(javaString)) {
            doubleValue = Double.NaN;
        } else {
            try {
                doubleValue = Double.parseDouble(javaString);
            } catch (NumberFormatException e) {
                throw new ExpressionException(
                        EXCEPTION_LOCALIZER.format(
                                "invalid-numeric-value",
                                new Object[]{
                                    javaString}));
            }
        }
        return doubleValue;
    }

    /**
     * Models the XPath 2.0 fn:boolean. See
     * <a href="http://www.w3.org/TR/xquery-operators/#func-boolean">
     * fn:boolean</a>.
     *
     * @param sequence the operand
     * @return the boolean representation of the sequence argument
     */
    public static BooleanValue fnBoolean(Sequence sequence)
            throws ExpressionException {

        BooleanValue eval = BooleanValue.TRUE;
        int length = sequence.getLength();

        if (length == 0) {
            // if empty sequence then return false
            eval = BooleanValue.FALSE;
        } else if (length == 1) {
            // Atomic value
            Value sv = sequence.getItem(1);
            if (sv instanceof NodeValue) {
                // according to the XPath specification if the first item in 
                // the sequence is NodeValue then fn:boolean is true
                eval = BooleanValue.TRUE;
            } else if (sv instanceof BooleanValue) {
                // If ST is xs:boolean, then TV is SV.
                eval = (BooleanValue) sv;
            } else if (sv instanceof NumericValue) {
                double numeric = 0;
                // obtain the representation of the numeric value
                if (sv instanceof DoubleValue) {
                    numeric = ((DoubleValue) sv).asJavaDouble();
                } else if (sv instanceof IntValue) {
                    numeric = ((IntValue) sv).asJavaInt();
                }
                // If ST is xs:float, xs:double, xs:decimal or xs:integer
                // and SV is 0, +0, -0, or NaN, then TV is false.
                // If ST is xs:float, xs:double, xs:decimal or xs:integer
                //and SV is not one of the above values, then TV is true.
                if (numeric == 0 || Double.isNaN(numeric)) {
                    eval = BooleanValue.FALSE;
                }
            } else if (sv instanceof StringValue) {
                // if IV is not a valid lexical representation for xs:boolean
                // as specified in [XML Schema Part 2: Datatypes], then an
                // error is raised ("Invalid lexical value").

                // If IV is "true " or " 1 ", then TV is true; if ST is
                // "false" or " 0 ", then TV is false.
                String str = ((StringValue) sv).asJavaString().trim();
                if ("false".equals(str) || "0".equals(str)) {
                    eval = BooleanValue.FALSE;
                } else if ("true".equals(str) || "1".equals(str)) {
                    // eval is true
                } else {
                    throw new ExpressionException("Invalid lexical value: " +
                                                  str);
                }
            }
        }
        // return the boolean value.
        return eval;
    }

    /**
     * Models the XPath 2.0 fn:number. See
     * <a href="http://www.w3.org/TR/xquery-operators/#func-number">
     * fn:number</a>.
     *
     * @param sequence the operand
     * @param factory an <code>ExpressionFactory</code> that can be used
     *        to factor expression related objects
     * @return the numeric representation of the sequence argument
     */
    public static DoubleValue fnNumber(Sequence sequence,
                                       ExpressionFactory factory)
            throws ExpressionException {

        // obtain length of sequence
        int length = sequence.getLength();

        // value of operand cast to a double
        DoubleValue eval = DoubleValue.NOT_A_NUMBER;

        // if length is zero then we return NaN
        if (length == 1) {
            // if length is 1 (ie atomic value) then the following rules apply
            Value sv = sequence.getItem(1);

            if (sv instanceof NumericValue) {
                if (sv instanceof DoubleValue) {
                    // if ST is xs:double, then TV is SV and the conversion
                    // is complete.
                    eval = (DoubleValue) sv;
                } else if (sv instanceof IntValue) {
                    double dbl = ((IntValue) sv).asJavaInt();
                    eval = factory.createDoubleValue(dbl);
                }
            } else if (sv instanceof BooleanValue) {
                // If ST is xs:boolean, SV is converted to 1.0 if SV is 1 or
                // true and to 0.0 if SV is 0 or false and the conversion
                // is complete.
                double dbl = ((BooleanValue) sv).asJavaBoolean() ? 1.0 : 0.0;
                eval = factory.createDoubleValue(dbl);
            } else if (sv instanceof StringValue) {
                // If ST is xdt:untypedAtomic, xs:anySimpleType or xs:string,
                // or a type derived from xs:string, SV is converted to an
                // intermediate value IV of type xs:token.
                String str = ((StringValue) sv).asJavaString().trim();
                if ("inf".equalsIgnoreCase(str) ||
                    "+inf".equalsIgnoreCase(str)) {
                    // if the value of fn:upper-case(  IV ) is INF or +INF,
                    // then TV is INF and the conversion is complete.
                    eval = DoubleValue.POSITIVE_INFINITY;
                } else if ("-inf".equalsIgnoreCase(str)) {
                    // if the value of fn:upper-case(  IV ) is -INF  or NAN,
                    // then TV is -INF or NaN, respectively, and the conversion
                    // is complete.
                    eval = DoubleValue.NEGATIVE_INFINITY;
                } else if ("nan".equalsIgnoreCase(str)) {
                    // DoubleValue.NaN - default
                } else {
                    try {
                        // If IV is not in the lexical space of xs:double, as
                        // defined in [XML Schema Part 2: Datatypes], then an
                        // error is raised ("Invalid lexical value").
                        // Otherwise, TV is xs:double(IV). Implementations may
                        // return negative zero for xs:double(-0.0E0).
                        double dbl = Double.parseDouble(str);
                        eval = factory.createDoubleValue(dbl);
                    }
                    catch (NumberFormatException e) {
                        // explicity catch this unchecked exception
                        logger.warn("could-not-convert-value-to-a-number",
                                    str);
                        eval = DoubleValue.NOT_A_NUMBER;
                    }
                }
            }
        } else if (length > 1) {
            // The XPath 2.0 spec is a bit vague at this point.
            // However, it has been decided by architecture that we should
            // raise a "Dynamic Error" in this situation
            throw new ExpressionException("Cannot cast a multi-value " +
                                          "sequence to a number");
        }
        return eval;
    }

    /**
     * Models the XPath 2.0 fn:not. See
     * <a href="http://www.w3.org/TR/xquery-operators/#func-not">
     * fn:not</a>.
     *
     * @param sequence the operand
     * @return Returns true if the effective boolean value of the operand is
     * false, and false if the effective boolean value is true.
     */
    public static BooleanValue fnNot(Sequence sequence)
            throws ExpressionException {

        // sequence is first reduced to an effective boolean value by applying
        // the fn:boolean()  function.
        // Returns true if the effective boolean value is false,
        // and false if the effective boolean value is true
        return (fnBoolean(sequence).asJavaBoolean())
               ? BooleanValue.FALSE : BooleanValue.TRUE;
    }
    
    /**
     * Gets information if given sequence contains given value.
     * 
     * <p>Method is comparing values if types of values are the same and are
     * supported by current implementation of the XPath. Values with different
     * types are treated as different values.</p>
     * 
     * <p><string>Warning:</strong> if types of values to be compared are
     * not supported by current implementation of the XPath then references
     * of these values are compared.</p> 
     *
     * <p>Please <strong>note</strong> that if there are new types of values
     * added to {@link #compare} method these should be also added into
     * this method.</p> 
     * 
     * @param sequence the sequence to be checked
     * @param value the value to be found in sequence
     * @return if sequence contains value
     * @throws ExpressionException on error while operations on {@link Value}
     *                             or {@link Sequence}
     */
    public static boolean isValueInSequence(Sequence sequence, Value value)
            throws ExpressionException {
        boolean result = false;
        final int length = sequence.getLength() + 1;
        for (int i = 1; i < length && !result; i++) {
            final Value compareValue = (Value) sequence.getItem(i);
            if (value == compareValue) {
                result = true;
            } else if ((value instanceof StringValue 
                    && compareValue instanceof StringValue) ||
                    (value instanceof NumericValue
                            && compareValue instanceof NumericValue) ||
                    (value instanceof BooleanValue
                            && compareValue instanceof BooleanValue) ||
                    (value instanceof NodeValue
                            && compareValue instanceof NodeValue)) {
                result = equals(value.getSequence(),
                        compareValue.getSequence());
            }
        }
        return result;
    }

    /**
     * Gets the sequence with unique values from given sequence.
     * 
     * <p>Method is comparing values if types of values are the same and are
     * supported by current implementation of the XPath. Values with different
     * types are treated as different values.</p>
     * 
     * <p><string>Warning:</strong> if types of values to be compared are
     * not supported by current implementation of the XPath then references
     * of these values are compared.</p> 
     *
     * <p>Please <strong>note</strong> that if there are new types of values
     * added to {@link #compare} method these should be also added into
     * this method.</p> 
     * 
     * @param sequence the sequence to be used to obtain values
     * @param factory used to create new sequence
     * @return the sequence with unique values from given sequence
     * @throws ExpressionException if problems while retrieving items
     *                             from given sequence or while comparing items
     */
    public static Sequence getSequenceWithUniqueValues(Sequence sequence,
            ExpressionFactory factory) throws ExpressionException {
        final List values = new ArrayList();
        final int length = sequence.getLength() + 1;
        for (int i = 1; i < length; i++) {
            final Value value = (Value) sequence.getItem(i);
            boolean isInList = false;
            for (final Iterator iterator = values.iterator();
                    iterator.hasNext() && !isInList; ) {
                final Value compareValue = (Value) iterator.next();
                if (value == compareValue) {
                    isInList = true;
                } else if ((value instanceof StringValue 
                        && compareValue instanceof StringValue) ||
                        (value instanceof NumericValue
                                && compareValue instanceof NumericValue) ||
                        (value instanceof BooleanValue
                                && compareValue instanceof BooleanValue) ||
                        (value instanceof NodeValue
                                && compareValue instanceof NodeValue)) {
                    isInList = equals(value.getSequence(),
                            compareValue.getSequence());
                }
            }
            if (!isInList) {
                values.add(value);
            }
        }
        return factory.createSequence((Item[]) values.toArray(new Item[0]));
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Jan-06	10855/6	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expressions.

 04-Jan-06	10855/4	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expression.

 28-Dec-05	10855/1	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expressions.

 22-Jun-05	8751/5	schaloner	VBM:2005060711 Updated code style

 15-Jun-05	8751/3	schaloner	VBM:2005060711 Streamlined multi-expr parts of PipelineExpressionHelper

 15-Jun-05	8751/1	schaloner	VBM:2005060711 ExpressionProcess and PipelineExpressionHelper can now support multiple expression declaration markup

 11-Feb-05	6931/2	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Oct-03	433/10	doug	VBM:2003102002 Changed the behaviour of the number function when operand is multi-value sequence

 27-Oct-03	433/1	doug	VBM:2003102002 Added several new comparison operators

 11-Aug-03	341/1	philws	VBM:2003081107 Make ExpressionHelper.equals(Sequence, Sequence) available

 31-Jul-03	222/3	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 19-Jun-03	90/1	adrian	VBM:2003061606 Added Expression support to Tag attributes

 ===========================================================================
*/
