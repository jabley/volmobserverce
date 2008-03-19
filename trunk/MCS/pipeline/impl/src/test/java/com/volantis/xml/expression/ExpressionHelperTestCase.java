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

import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.NumericValue;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;

import junit.framework.TestCase;

/**
 * This class tests MCSExpressionHelper.
 */
public class ExpressionHelperTestCase extends TestCase {

    /**
     * A valid encoded expression string.
     */
    protected final static String ENCODED_EXPRESSION = "%{$myVar}";

    /**
     * An unencoded expression string.
     */
    protected final static String UNENCODED_EXPRESSION = "$myVar";

    /**
     * A escaped valid encoded expression string.
     */
    protected final static String ESCAPED_ENCODED_EXPRESSION = "\\%{$myVar}";

    /**
     * An invalid encoded expression string.
     */
    protected final static String NOT_AN_EXPRESSION = "xyz";

    /**
     * An ExpressionFactory instance.
     */
    protected final static ExpressionFactory factory =
            ExpressionFactory.getDefaultInstance();

    /**
     * Test the method isPipelineQuotedExpression(String expression).
     * An encoded expression string takes the form: %{expression}
     */
    public void testIsEncodedExpression() throws Exception {
        boolean encodedExpression =
                PipelineExpressionHelper.isPipelineQuotedExpression(ENCODED_EXPRESSION);
        assertTrue("The specified string should have resolved to an " +
                   "encoded expression string.", encodedExpression);

        encodedExpression =
                PipelineExpressionHelper.isPipelineQuotedExpression(NOT_AN_EXPRESSION);
        assertTrue("The specified string should not have resolved to an " +
                   "encoded expression string.", !encodedExpression);
    }

    /**
     * Test the method removePipelineQuoting(String expression)
     * This method strips the encoding from an expression, i.e. %{myExpr} is
     * returned as myExpr.
     */
    public void testGetUnencodedExpression() throws Exception {
        String result = PipelineExpressionHelper.
                removePipelineQuoting(ENCODED_EXPRESSION);

        assertTrue("The encoded expression string was not correctly unencoded",
                   UNENCODED_EXPRESSION.equals(result));
    }

    /**
     * Test the method isEscapedPipelineQuotedExpression(String expression)
     * As escaped encoded expression takes the form: \%{expression}
     */
    public void testIsEscapedEncodedExpression() throws Exception {
        boolean escapedExpression = PipelineExpressionHelper.
                isEscapedPipelineQuotedExpression(ESCAPED_ENCODED_EXPRESSION);
        assertTrue("The specified string should have resolved to an " +
                   "escaped encoded expression string.", escapedExpression);

        escapedExpression =
                PipelineExpressionHelper.isEscapedPipelineQuotedExpression(NOT_AN_EXPRESSION);
        assertTrue("The specified string should not have resolved to an " +
                   "escaped encoded expression string.", !escapedExpression);
    }

    /**
     * Test the method getUnescapedPipelineQuotedExpression(String expression)
     * This method strips the escape character from an escaped encoded
     * expression, i.e. \%{myExpr} is returned as %{myExpr}
     */
    public void testGetUnescapedExpression() throws Exception {
        String result = PipelineExpressionHelper.
                getUnescapedPipelineQuotedExpression(ESCAPED_ENCODED_EXPRESSION);

        assertTrue("The escaped expression was not correctly unescaped.",
                   ENCODED_EXPRESSION.equals(result));
    }

    /**
     * Tests the {@link PipelineExpressionHelper#compare} with NumericValue operands
     * that are equal
     * @throws Exception if an error occurs
     */
    public void testCompareWithEqualNumerics() throws Exception {
        doTestNumericCompare(factory.createDoubleValue(20.0),
                             factory.createIntValue(20),
                             0);
        doTestNumericCompare(factory.createDoubleValue(20.0),
                             factory.createIntValue(20),
                             CompareResult.EQUAL);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#compare} with NumericValue operands
     * that where the first is less than the second
     * @throws Exception if an error occurs
     */
    public void testCompareWithGreaterThanNumerics() throws Exception {
        doTestNumericCompare(factory.createDoubleValue(-0.5),
                             factory.createIntValue(0),
                             1);
        doTestNumericCompare(factory.createDoubleValue(-0.5),
                             factory.createIntValue(0),
                             CompareResult.GREATER_THAN); 
    }

    /**
     * Tests the {@link PipelineExpressionHelper#compare} with NumericValue operands
     * that where the first is greater than the second
     * @throws Exception if an error occurs
     */
    public void testCompareWithLessThanNumerics() throws Exception {
        doTestNumericCompare(factory.createDoubleValue(10.1),
                             factory.createIntValue(6),
                             -1);
        doTestNumericCompare(factory.createDoubleValue(10.1),
                             factory.createIntValue(6),
                             CompareResult.LESS_THAN);
    }

    /**
     * Method that invokes the {@link PipelineExpressionHelper#compare} method with
     * two NumericValue operands and checks the result against an expected
     * result
     * @param left the left operand
     * @param right the right operand
     * @param expected the expected result
     * @throws Exception if an error occurs
     * @todo should not really invoke the {@link #doTestEquals} methd.
     */
    public void doTestNumericCompare(NumericValue left,
                                     NumericValue right,
                                     int expected) throws Exception {
        // invoke the compare result
        int result = PipelineExpressionHelper.compare(left, right);
        // check the result
        assertEquals("unexpected compare(NumericValue, NumericValue) result",
                     expected,
                     result);

        // check that the equals method returns the expected result
        doTestEquals(left.getSequence(), right.getSequence(), expected == 0);
    }
    
    /**
     * Method that invokes the {@link PipelineExpressionHelper#compareNumeric} method with
     * two NumericValue operands and checks the result against an expected
     * result
     * 
     * @param left the left operand
     * @param right the right operand
     * @param expected the expected result
     * @throws Exception if an error occurs
     */
    public void doTestNumericCompare(NumericValue left, 
                                     NumericValue right,
                                     CompareResult expected) throws Exception {
        // invoke the compare result
        CompareResult result = PipelineExpressionHelper.compareNumeric(left,
                right);
        // check the result
        assertSame("Unexpected compare(NumericValue, NumericValue) result",
                expected, result);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#compare} with BooleanValue
     * operands that are both TRUE
     * @throws Exception if an error occurs
     */
    public void testCompareWithEqualTrueBoolean() throws Exception {
        doTestBooleanCompare(BooleanValue.TRUE,
                             BooleanValue.TRUE,
                             0);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#compare} with BooleanValue operands
     * that are both FALSE
     * @throws Exception if an error occurs
     */
    public void testCompareWithEqualFalseBoolean() throws Exception {
        doTestBooleanCompare(BooleanValue.FALSE,
                             BooleanValue.FALSE,
                             0);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#compare} with BooleanValue operands
     * that where the left one is TRUE and the right one FALSE
     * @throws Exception if an error occurs
     */
    public void testCompareWithGreaterThanBooleans() throws Exception {
        doTestBooleanCompare(BooleanValue.TRUE,
                             BooleanValue.FALSE,
                             -1);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#compare} with BooleanValue operands
     * that where the left one is FALSE and the right one TRUE
     * @throws Exception if an error occurs
     */
    public void testCompareWithLessThanBoolean() throws Exception {
        doTestBooleanCompare(BooleanValue.FALSE,
                             BooleanValue.TRUE,
                             1);
    }

    /**
     * Method that invokes the {@link PipelineExpressionHelper#compare} method with
     * two BooleanValue operands and checks the result against an expected
     * result
     * @param left the left operand
     * @param right the right operand
     * @param expected the expected result
     * @throws Exception if an error occurs
     * @todo should not really invoke the {@link #doTestEquals} methd.
     */
    public void doTestBooleanCompare(BooleanValue left,
                                     BooleanValue right,
                                     int expected) throws Exception {
        // invoke the compare method
        int result = PipelineExpressionHelper.compare(left, right);
        // check the result
        assertEquals("unexpected compare(BooleanValue, BooleanValue) result",
                     expected,
                     result);

        // test the equals method as well
        doTestEquals(left.getSequence(), right.getSequence(), expected == 0);

    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with an empty
     * Sequence argument
     * @throws Exception if an error occurs
     */
    public void testFnBooleanEmptySequence() throws Exception {
        // empty sequence should evaluate to false
        doTestFnBoolean(Sequence.EMPTY, false);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence with more than one item
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceSeveralItems() throws Exception {
        Sequence seq = factory.createSequence(
                new Item[]{BooleanValue.TRUE,
                           BooleanValue.TRUE});

        doTestFnBoolean(seq, true);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one Boolean false item
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceBooleanFalseItem() throws Exception {
        Sequence seq = BooleanValue.FALSE.getSequence();
        doTestFnBoolean(seq, false);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one Boolean true item
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceBooleanTrueItem() throws Exception {
        Sequence seq = BooleanValue.TRUE.getSequence();
        doTestFnBoolean(seq, true);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one zero IntValue item that
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceZeroIntItem() throws Exception {
        Sequence seq = factory.createIntValue(0).getSequence();
        doTestFnBoolean(seq, false);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one non-zero IntValue item that
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceNonZeroIntItem() throws Exception {

        Sequence seq = factory.createIntValue(99).getSequence();
        doTestFnBoolean(seq, true);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one zero DoubleValue item that
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceZeroDoubleItem() throws Exception {

        Sequence seq = factory.createDoubleValue(-0.0).getSequence();
        doTestFnBoolean(seq, false);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one non-zero DoubleValue item that
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceNonZeroDoubleItem() throws Exception {

        Sequence seq = factory.createDoubleValue(0.3).getSequence();
        doTestFnBoolean(seq, true);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one NaN DoubleValue item that
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceNaNDoubleItem() throws Exception {

        Sequence seq = DoubleValue.NOT_A_NUMBER.getSequence();
        doTestFnBoolean(seq, false);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one positive infinity DoubleValue item that
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequencePosInfDoubleItem() throws Exception {

        Sequence seq = DoubleValue.POSITIVE_INFINITY.getSequence();
        doTestFnBoolean(seq, true);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one negative infinity DoubleValue item that
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceNegInfDoubleItem() throws Exception {

        Sequence seq = DoubleValue.NEGATIVE_INFINITY.getSequence();
        doTestFnBoolean(seq, true);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one "true" StringValue item with
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceTrueStringItem() throws Exception {

        Sequence seq = factory.createStringValue("true").getSequence();
        doTestFnBoolean(seq, true);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one "1" StringValue item with
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequence1StringItem() throws Exception {

        Sequence seq = factory.createStringValue("1").getSequence();
        doTestFnBoolean(seq, true);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one "false" StringValue item with
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceFalseStringItem() throws Exception {

        Sequence seq = factory.createStringValue("false").getSequence();
        doTestFnBoolean(seq, false);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence of one "0" StringValue item with
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequence0StringItem() throws Exception {

        Sequence seq = factory.createStringValue("0").getSequence();
        doTestFnBoolean(seq, false);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnBoolean} method with a
     * Sequence arbitary literal StringValue item with
     * @throws Exception if an error occurs
     */
    public void testFnBooleanSequenceStringItem() throws Exception {

        Sequence seq = factory.createStringValue("fred").getSequence();
        try {
            doTestFnBoolean(seq, false);
            fail("Invalid Lexical Value should result in exception");
        } catch (ExpressionException e) {
            // expected condition
        }
    }

    /**
     * Method that invokes the {@link PipelineExpressionHelper#fnBoolean} method with
     * with a given <code>Sequence</code> and checks the result against an
     * expected result
     * @param sequence the Sequence operand
     * @param expected the expected result
     * @throws Exception if an error occurs
     */
    public void doTestFnBoolean(Sequence sequence, boolean expected)
            throws Exception {
        // invoke the method
        BooleanValue result = PipelineExpressionHelper.fnBoolean(sequence);
        // check the result
        assertEquals("unexpected fnBoolean(Sequence) result",
                     expected,
                     result.asJavaBoolean());

    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with an empty
     * Sequence argument
     * @throws Exception if an error occurs
     */
    public void testNumberWithEmptySequence() throws Exception {
        // invoke the method
        DoubleValue result = PipelineExpressionHelper.fnNumber(Sequence.EMPTY,
                                                       factory);
        // check the result
        assertTrue("unexpected number result",
                   Double.isNaN(result.asJavaDouble()));

    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with a Sequence
     * of one IntValue item
     * @throws Exception if an error occurs
     */
    public void testNumberWithIntValueItem() throws Exception {
        Sequence seq = factory.createIntValue(9).getSequence();
        doTestNumber(seq, 9);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with a Sequence
     * of one IntValue item
     * @throws Exception if an error occurs
     */
    public void testNumberWithDoubleValueItem() throws Exception {
        Sequence seq = factory.createDoubleValue(9.9).getSequence();
        doTestNumber(seq, 9.9);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with a Sequence
     * of one "true" BooleanValue Item
     * @throws Exception if an error occurs
     */
    public void testNumberWithTrueBooleanValueItem() throws Exception {
        Sequence seq = BooleanValue.TRUE.getSequence();
        doTestNumber(seq, 1.0);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with a Sequence
     * of one "false" BooleanValue Item
     * @throws Exception if an error occurs
     */
    public void testNumberWithFalseBooleanValueItem() throws Exception {
        Sequence seq = BooleanValue.FALSE.getSequence();
        doTestNumber(seq, 0.0);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with a Sequence
     * of one "inf" StringValue Item
     * @throws Exception if an error occurs
     */
    public void testNumberWithInfStringValueItem() throws Exception {
        Sequence seq = factory.createStringValue("inf").getSequence();
        doTestNumber(seq, Double.POSITIVE_INFINITY);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with a Sequence
     * of one "+INF" StringValue Item
     * @throws Exception if an error occurs
     */
    public void testNumberWithPlusInfStringValueItem() throws Exception {
        Sequence seq = factory.createStringValue("+INF").getSequence();
        doTestNumber(seq, Double.POSITIVE_INFINITY);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with a Sequence
     * of one "NaN" StringValue Item
     * @throws Exception if an error occurs
     */
    public void testNumberWithNaNStringValueItem() throws Exception {
        Sequence seq = factory.createStringValue("NaN").getSequence();
        DoubleValue result = PipelineExpressionHelper.fnNumber(seq, factory);
        assertTrue("number('NaN') should return NaN DoubleValue",
                   Double.isNaN(result.asJavaDouble()));
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with a Sequence
     * of one "-inf" StringValue item
     * @throws Exception if an error occurs
     */
    public void testNumberWithMinusInfStringValueItem() throws Exception {
        Sequence seq = factory.createStringValue("-inf").getSequence();
        doTestNumber(seq, Double.NEGATIVE_INFINITY);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with a Sequence
     * of one valid double StringValue Item
     * @throws Exception if an error occurs
     */
    public void testNumberWithValidDoubleStringValueItem() throws Exception {
        Sequence seq = factory.createStringValue("0.444E5").getSequence();
        doTestNumber(seq, 0.444E5);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with a Sequence
     * of one invalid double StringValue Item
     * @throws Exception if an error occurs
     */
    public void testNumberWithinValidDoubleStringValueItem() throws Exception {
        Sequence seq = factory.createStringValue("fred").getSequence();
        DoubleValue result = PipelineExpressionHelper.fnNumber(seq, factory);
        assertTrue("number('fred') should return NaN DoubleValue",
                   Double.isNaN(result.asJavaDouble()));
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNumber} method with a Sequence
     * of several Items
     * @throws Exception if an error occurs
     */
    public void testNumberWithSeveralItemSequence() throws Exception {
        Sequence seq = factory.createSequence(
                new Item[]{factory.createStringValue("0.444E5"),
                           factory.createDoubleValue(9.9),
                           BooleanValue.FALSE});

        try {
            PipelineExpressionHelper.fnNumber(seq, factory);
            fail("Casting a sequence of more than one item to a Number " +
                 "should result in an exception being thrown");
        } catch (ExpressionException e) {
            // expected condition
        }
    }

    /**
     * Method that invokes the {@link PipelineExpressionHelper#fnNumber} method with
     * with a given <code>Sequence</code> and checks the result against an
     * expected result
     * @param sequence the Sequence operand
     * @param expected the expected result
     * @throws Exception if an error occurs
     */
    public void doTestNumber(Sequence sequence, double expected)
            throws Exception {
        // invoke the method
        DoubleValue result = PipelineExpressionHelper.fnNumber(sequence, factory);
        // check the result
        assertEquals("unexpected number result",
                     expected,
                     result.asJavaDouble(),
                     0.1);

    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNot} method
     * @throws Exception if an error occurs
     */
    public void testNotEmptySequence() throws Exception {
        doTestNot(Sequence.EMPTY);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#fnNot} method
     * @throws Exception if an error occurs
     */
    public void testNotBooleanValueItem() throws Exception {
        doTestNot(BooleanValue.TRUE.getSequence());
    }

    /**
     * Method that invokes the {@link PipelineExpressionHelper#fnNot} method with
     * with a given <code>Sequence</code> and checks that the result is
     * ! the fnBoolean(sequence) result.
     * @param sequence the Sequence operand
     * @throws Exception if an error occurs
     */
    public void doTestNot(Sequence sequence) throws Exception {
        // invoke the method
        boolean expected  =
                !(PipelineExpressionHelper.fnBoolean(sequence).asJavaBoolean());

        BooleanValue actual = PipelineExpressionHelper.fnNot(sequence);

        // check the result
        assertEquals("unexpected fnBoolean(Sequence) result",
                     expected,
                     actual.asJavaBoolean());

    }

    /**
     * Tests the {@link PipelineExpressionHelper#equals} method with two equal
     * sequences
     * @throws Exception if an error occurs
     */
    public void testSequenceEqualityWhenTrue() throws Exception {
        // right operand
        Sequence left = factory.createSequence(new Item[] {
           factory.createIntValue(9),
           factory.createIntValue(6),
           factory.createIntValue(77),
           factory.createIntValue(66),
        });

        // left operand
        Sequence right = factory.createSequence(new Item[] {
           factory.createIntValue(44),
           factory.createIntValue(-33),
           factory.createIntValue(7777),
           factory.createIntValue(11),
           factory.createIntValue(66),
        });

        // ensure that the equals method reports that the sequences are equal
        // (they both contain TRUE)
        doTestEquals(left, right, true);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#equals} method with two sequences that
     * should produce a false result
     * @throws Exception if an error occurs
     */
    public void testSequenceEqualityWhenFalse() throws Exception {
        // right operand
        Sequence left = factory.createSequence(new Item[] {
           factory.createIntValue(1),
           factory.createIntValue(11),
           factory.createIntValue(7)
        });

        // left operand
        Sequence right = factory.createSequence(new Item[] {
           factory.createIntValue(0),
           factory.createIntValue(99),
           factory.createIntValue(88)
        });

        // ensure that the equals method reports that the sequences are
        // NOTequal
        doTestEquals(left, right, false);
    }

    /**
     * Tests the {@link PipelineExpressionHelper#equals} method
     * @param left the left Sequence operand
     * @param right the right Sequence operand
     * @param expected the expected result
     * @throws Exception if an error occurs
     */
    public void doTestEquals(Sequence left, Sequence right, boolean expected)
        throws Exception {

        // invoke the equals method
        boolean isEqual = PipelineExpressionHelper.equals(left, right);
        // check against the expected result
        assertEquals("Unexpected result from equal(Sequence, Sequence) method",
                     expected,
                     isEqual);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Oct-03	433/7	doug	VBM:2003102002 Changed the behaviour of the number function when operand is multi-value sequence

 27-Oct-03	433/1	doug	VBM:2003102002 Added several new comparison operators

 31-Jul-03	222/2	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 19-Jun-03	90/1	adrian	VBM:2003061606 Added Expression support to Tag attributes

 ===========================================================================
*/
