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

package com.volantis.xml.expression.impl.jxpath;

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.NodeValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.namespace.DefaultNamespacePrefixTracker;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespacePrefixTracker;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import junit.framework.TestCase;

/**
 * TestCase for the JXPathExpression class.
 */
public class JXPathExpressionTestCase extends TestCase {

    /**
     * Create a new JXPathExpressionTestCase instance.
     * @param name the name
     */
    public JXPathExpressionTestCase(String name) {
        super(name);
    }

    /**
     * An Value reference
     */
    private Value result;

    /**
     * The factory to be used to create any expression related objects
     */
    private ExpressionFactory factory = new JXPathExpressionFactory();

    /**
     * An ExpressionContext reference
     */
    private JXPathExpressionContext context;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        result = factory.createStringValue("TestResult");

        // @todo fix parameters
        context = new JXPathExpressionContext(
                factory,
                null,
                new DefaultNamespacePrefixTracker());
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        result = null;
        context = null;
    }

    
    public void testExpressionListEqualSequence() throws Exception {
        Item[] items = new Item[3];
        items[0] = factory.createStringValue("a");
        items[1] = factory.createStringValue("b");
        items[2] = factory.createStringValue("c");   	
        context.getCurrentScope().declareVariable(
                   new ImmutableExpandedName("", "myVar"),
                   factory.createSequence(items)); 
       Expression exp = compileExpression("$myVar=(\"a\",\"b\",\"c\")");
       Value result = exp.evaluate(context);    
	
       assertTrue("Sequence comparison failed :" + 
    		   "'(\"a\",\"b\",\"c\")=(\"a\",\"d\")'",
            ((BooleanValue) result).asJavaBoolean());

       Expression exp2 = compileExpression("$myVar=(\"a\",\"d\")");
       Value result2 = exp2.evaluate(context);    

       assertTrue("Sequence comparison failed: " + 
    		   "'(\"a\",\"b\",\"c\")=(\"a\",\"d\")'",
            ((BooleanValue) result2).asJavaBoolean());     
       
       Expression exp3 = compileExpression("2=(3, 4 div 2)");
       Value result3 = exp3.evaluate(context);    

       assertTrue("Sequence comparison failed : '2=(3, 4 div 2)'",
            ((BooleanValue) result3).asJavaBoolean());        
    }
    
    public void testExpressionListEqual() throws Exception {
        Item[] left = {factory.createIntValue(2),
                 factory.createDoubleValue(42),
                 factory.createIntValue(53)};
        Item[] right = {factory.createDoubleValue(2.2),
                 factory.createIntValue(42),
                 factory.createDoubleValue(10101010)};

        context.getCurrentScope().declareVariable(
        		new ImmutableExpandedName("", "left"),
        		factory.createSequence(left));
        context.getCurrentScope().declareVariable(
        		new ImmutableExpandedName("", "right"),
        		factory.createSequence(right));

        Expression exp = compileExpression("$left = (($right))");
        Value result = exp.evaluate(context);    	
        assertTrue("Nested variable comparison failed", 
        		((BooleanValue) result).asJavaBoolean());
    }
    
    /**
     * Ensures that a qualified variable reference can be evaluated
     * @throws Exception if an error occurs
     */
    public void testQualifiedVariableEvaluate() throws Exception {
        NamespacePrefixTracker manager = context.getNamespacePrefixTracker();
        manager.startPrefixMapping("p", "http://myNamespace");

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("http://myNamespace",
                                          "var"),
                result);

        Expression exp = compileExpression("$p:var");

        assertTrue("Variable expression evaluation failed",
                   exp.evaluate(context) == result);
    }

    /**
     * Ensures that an unqualified variable reference can be evaluated
     * @throws Exception if an error occurs
     */
    public void testUnqualifiedVariableEvaluate() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                result);

        Expression exp = compileExpression("$myVar");

        assertTrue("Variable expression evaluation failed",
                   exp.evaluate(context) == result);
    }

    /**
     * Ensures that an unqualified variable reference that has not be declared
     * cannot be evaluated
     * @throws Exception if an error occurs
     */
    public void testUnDeclaredVariableEvaluate() throws Exception {
        Expression exp = compileExpression("$myVar");
        try {
            exp.evaluate(context);

            fail("Expected an expression exception");
        } catch (ExpressionException e) {
            // Expected condition
        }
    }

    public void testSequencePredicateOneEvaluate() throws Exception {
        Item[] items = new Item[3];

        items[0] = factory.createStringValue("a");
        items[1] = factory.createStringValue("b");
        items[2] = factory.createStringValue("c");

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                factory.createSequence(items));

        Expression exp = compileExpression("$myVar[1]");

        assertTrue("Variable expression evaluation failed",
                   exp.evaluate(context) == items[0]);
    }

    public void testSequencePredicateMidEvaluate() throws Exception {
        Item[] items = new Item[3];

        items[0] = factory.createStringValue("a");
        items[1] = factory.createStringValue("b");
        items[2] = factory.createStringValue("c");

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                factory.createSequence(items));

        Expression exp = compileExpression("$myVar[2]");

        assertTrue("Variable expression evaluation failed",
                   exp.evaluate(context) == items[1]);
    }

    public void testSequencePredicateLastEvaluate() throws Exception {
        Item[] items = new Item[3];

        items[0] = factory.createStringValue("a");
        items[1] = factory.createStringValue("b");
        items[2] = factory.createStringValue("c");

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                factory.createSequence(items));

        Expression exp = compileExpression("$myVar[3]");

        assertTrue("Variable expression evaluation failed",
                   exp.evaluate(context) == items[2]);
    }

    public void testSequencePredicateOutOfBoundsEvaluate() throws Exception {
        Item[] items = new Item[3];

        items[0] = factory.createStringValue("a");
        items[1] = factory.createStringValue("b");
        items[2] = factory.createStringValue("c");

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                factory.createSequence(items));

        Expression exp = compileExpression("$myVar[4]");

        Value result = exp.evaluate(context);

        // result should be an empty sequence
        assertTrue("Result is not a Sequence instance",
                   result instanceof Sequence);

        Sequence sequence = (Sequence) result;

        assertEquals("Sequence is not empty", 0, sequence.getLength());
    }

    public void testSequencePredicateZeroOutOfBoundsEvaluate() throws Exception {
        Item[] items = new Item[3];

        items[0] = factory.createStringValue("a");
        items[1] = factory.createStringValue("b");
        items[2] = factory.createStringValue("c");

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                factory.createSequence(items));


        Expression exp = compileExpression("$myVar[0]");

        Value result = exp.evaluate(context);

        // result should be an empty sequence
        assertTrue("Result is not a Sequence instance",
                   result instanceof Sequence);

        Sequence sequence = (Sequence) result;

        assertEquals("Sequence is not empty", 0, sequence.getLength());

    }

    /**
     * Tests if sequence for NodeList is created correctly.
     * 
     * @throws Exception
     */
    public void testSequenceNodeValues() throws Exception {
        Sequence sequence = factory.createSequence((NodeList) null);
        assertSame("Sequence should be empty", sequence, Sequence.EMPTY);
        
        final Node node = getNodeWithChildrenForSequence();
        final NodeList listOfNodes = node.getChildNodes();
        sequence = factory.createSequence(listOfNodes);
        Node[] nodes = new Node[listOfNodes.getLength()];
        for (int i = 0; i < listOfNodes.getLength(); i++) {
            // store nodes for later comparision
            nodes[i] = listOfNodes.item(i);
            assertEquals("Sequence doesn't contains correct Values",
                    listOfNodes.item(i).getNodeName(),
                    ((NodeValue)sequence.getItem(i + 1)).asW3CNode()
                            .getNodeName());
            assertSame("Nodes from Sequence should be " +
                    "the same as used while creating an Sequence",
                    listOfNodes.item(i),
                    ((NodeValue) sequence.getItem(i + 1)).asW3CNode());
        }
        // now change NodeList, this should left sequence unchanged
        node.removeChild(nodes[0]);
        assertSame("Removed from list of nodes node isn't exists in sequence",
                nodes[0], ((NodeValue) sequence.getItem(1)).asW3CNode());
    }

    /**
     * Gets simple Node to be used in this test case.
     * 
     * @return Node
     * @throws Exception
     */
    private Node getNodeWithChildrenForSequence() throws Exception {
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();
        Node rootNode = document.createElement("root");
        for (int i = 0; i < 10; i++) {
            rootNode.appendChild(document.createElement("test" + i));
        }
        return rootNode;
    }


    public void testEqualityErrorEvaluate() throws Exception {
        Item[] items = {factory.createIntValue(2),
                        factory.createStringValue(result.stringValue().
                                                  asJavaString())};
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                result);
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "mySequence"),
                factory.createSequence(items));

        Expression exp = compileExpression("$myVar = $mySequence");

        try {
            exp.evaluate(context);

            fail("Should have had an exception thrown");
        } catch (ExpressionException e) {
            // Expected condition
            System.out.println(e);
        }
    }

    public void testSequenceEqualityEvaluate() throws Exception {
        Item[] left = {factory.createIntValue(2),
                       factory.createDoubleValue(42),
                       factory.createIntValue(53)};
        Item[] right = {factory.createDoubleValue(2.2),
                        factory.createIntValue(42),
                        factory.createDoubleValue(10101010)};

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "left"),
                factory.createSequence(left));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "right"),
                factory.createSequence(right));

        Expression exp = compileExpression("$left = $right");
        Value result = exp.evaluate(context);

        assertTrue("Sequence comparison failed",
                   ((BooleanValue) result).asJavaBoolean());
    }

    /**
     * Test that inequality between two sequences
     * @throws Exception if an error occurs
     */
   public void testSequenceInequalityEvaluate() throws Exception {
        Item[] left = {factory.createIntValue(2),
                       factory.createDoubleValue(42),
                       factory.createIntValue(53)};
        Item[] right = {factory.createDoubleValue(2.2),
                        factory.createIntValue(42),
                        factory.createDoubleValue(10101010)};

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "left"),
                factory.createSequence(left));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "right"),
                factory.createSequence(right));

        Expression exp = compileExpression("$left != $right");
        Value result = exp.evaluate(context);

        // the sequences are not equal as the first item in both
        // sequences are not equal. Note these same 2 sequences
        // are both equal and not equal.
        assertTrue("Sequence comparison failed",
                   ((BooleanValue) result).asJavaBoolean());
    }

    public void testEqualityEvaluate() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                result);

        Expression exp = compileExpression("$myVar = \"" +
                                           result.stringValue().
                                           asJavaString() + "\"");
        Value result = exp.evaluate(context);

        assertTrue("Equality expression should result in a boolean value",
                   result instanceof BooleanValue);
        assertTrue("Equality expression result should be true",
                   ((BooleanValue) result).asJavaBoolean());
    }

    /**
     * Tests that the less than operator works with 2 sequences
     * @throws Exception if an error occurs
     */
    public void testLessThanWithSequences() throws Exception {
        Sequence first = factory.createSequence(new Item[] {
            factory.createIntValue(1000),
            factory.createIntValue(100),
            factory.createIntValue(10)
        });

        Sequence second = factory.createSequence(new Item[] {
            factory.createIntValue(1),
            factory.createIntValue(2)
        });

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "first"),
                first);

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "second"),
                second);

        doTestOperatorExpression("<", "$first < $second", false);
    }

    /**
     * Tests that the less than operator works with 2 sequences
     * @throws Exception if an error occurs
     */
    public void testLessThanWithSequencesTrue() throws Exception {
        Sequence first = factory.createSequence(new Item[] {
            factory.createIntValue(1000),
            factory.createIntValue(100),
            factory.createIntValue(1)
        });

        Sequence second = factory.createSequence(new Item[] {
            factory.createIntValue(1),
            factory.createIntValue(2)
        });

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "first"),
                first);

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "second"),
                second);

        doTestOperatorExpression("<", "$first < $second", true);
    }

    /**
     * Tests that the less than operator works with 2 booleans
     * @throws Exception if an error occurs
     */
    public void testBooleanLessThan() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                BooleanValue.TRUE);

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "anVar"),
                BooleanValue.FALSE);

        doTestOperatorExpression("<", "$myVar < $anVar", false);
        doTestOperatorExpression("<", "$anVar < $myVar", true);
        doTestOperatorExpression("<", "$anVar < $anVar", false);
    }

    /**
     * Tests that the <= operator works with 2 boolean values
     * @throws Exception if an error occurs
     */
    public void testBooleanLessThanOrEqual() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                BooleanValue.TRUE);

        doTestOperatorExpression("<=", "$myVar <= boolean(\"0\")", false);
        doTestOperatorExpression("<=", "false() <= $myVar", true);
        doTestOperatorExpression("<=", "$myVar <= $myVar", true);
    }

    /**
     * Tests that the > operator works with 2 booleans
     * @throws Exception if an error occurs
     */
    public void testBooleanGreaterThan() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                BooleanValue.TRUE);

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "anVar"),
                BooleanValue.FALSE);

        doTestOperatorExpression(">", "$myVar > $anVar", true);
        doTestOperatorExpression(">", "$anVar > $myVar", false);
        doTestOperatorExpression(">", "$anVar > $anVar", false);
    }

    /**
     * Tests that the >= operator works with 2 boolean values
     * @throws Exception if an error occurs
     */
    public void testBooleanGreaterThanOrEqual() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                BooleanValue.TRUE);

        doTestOperatorExpression(">=", "$myVar >= boolean(\"0\")", true);
        doTestOperatorExpression(">=", "false() >= true()", false);
        doTestOperatorExpression(">=", "$myVar >= $myVar", true);
    }

    /**
     * Tests that the < operator works with 2 numeric values
     * @throws Exception if an error occurs
     */
    public void testNumericLessThan() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                factory.createDoubleValue(2.2));

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "anVar"),
                factory.createIntValue(2));

        doTestOperatorExpression("<", "$anVar < $myVar", true);
        doTestOperatorExpression("<", "$myVar < $anVar", false);
        doTestOperatorExpression("<", "$myVar < $myVar", false);
    }

    /**
     * Tests that the <= operator works with 2 numeric values
     * @throws Exception if an error occurs
     */
    public void testNumericLessThanOrEqual() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                factory.createDoubleValue(2.2));

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "anVar"),
                factory.createIntValue(2));

        doTestOperatorExpression("<=", "$anVar <= $myVar", true);
        doTestOperatorExpression("<=", "$myVar <= $anVar", false);
        doTestOperatorExpression("<=", "$myVar <= $myVar", true);
    }

    /**
     * Tests that the > operator works with 2 numeric values
     * @throws Exception if an error occurs
     */
    public void testNumericGreaterThan() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                factory.createDoubleValue(2.2));

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "anVar"),
                factory.createIntValue(2));

        doTestOperatorExpression(">", "$anVar > $myVar", false);
        doTestOperatorExpression(">", "$myVar > $anVar", true);
        doTestOperatorExpression(">", "$myVar > $myVar", false);
    }


    /**
     * Tests that the >= operator works with 2 numeric values
     * @throws Exception if an error occurs
     */
    public void testNumericGreaterThanOrEqual() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                factory.createDoubleValue(2.2));

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "anVar"),
                factory.createIntValue(2));

        doTestOperatorExpression(">=", "$anVar >= $myVar", false);
        doTestOperatorExpression(">=", "$myVar >= $anVar", true);
        doTestOperatorExpression(">=", "$myVar >= $myVar", true);
    }

    /**
     * Tests that the + operator works with 2 ore more numeric values
     * 
     * @throws Exception
     *             if an error occurs
     */
    public void testOperatorAdd() throws Exception {
        Item[] empty = {};
        Item[] intval = { factory.createIntValue(2) };
        Item[] doubleval = { factory.createDoubleValue(2.2) };
        Value result;

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "empty"),
                factory.createSequence(empty));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "intval"),
                factory.createSequence(intval));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "doubleval"),
                factory.createSequence(doubleval));

        result = compileExpression("$doubleval + 0.2").evaluate(context);
        assertEquals("Operator '+' failed,", 2.2 + 0.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$empty + 0.2").evaluate(context);
        assertEquals("Operator '+' failed on empty sequence,", 0,
                ((Sequence) result).getLength());

        result = compileExpression("$doubleval + 2").evaluate(context);
        assertEquals("Operator '+' failed,", 2.2 + 2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$doubleval + 0.2 + 5").evaluate(context);
        assertEquals("Operator '+' failed,", 2.2 + 0.2 + 5,
                ((DoubleValue) result).asJavaDouble(), 0.0000001);

        result = compileExpression("$intval + $intval").evaluate(context);
        assertEquals("Operator '+' failed,", 2 + 2, ((IntValue) result)
                .asJavaInt());

        result = compileExpression("$intval + 0.2").evaluate(context);
        assertEquals("Operator '+' failed,", 2 + 0.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$doubleval + 0.2").evaluate(context);
        assertEquals("Operator '+' failed,", 2.2 + 0.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        doTestOperatorExpression("=", "2 + 3 + 4 = 9", true);
    }

    /**
     * Tests that the - operator works with 2 numeric values
     * 
     * @throws Exception
     *             if an error occurs
     */
    public void testOperatorSubtract() throws Exception {
        Item[] empty = {};
        Item[] intval = { factory.createIntValue(2) };
        Item[] doubleval = { factory.createDoubleValue(2.2) };
        Value result;

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "empty"),
                factory.createSequence(empty));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "intval"),
                factory.createSequence(intval));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "doubleval"),
                factory.createSequence(doubleval));

        result = compileExpression("$doubleval - 0.2").evaluate(context);
        assertEquals("Operator '-' failed,", 2.2 - 0.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$empty - 0.2").evaluate(context);
        assertEquals("Operator '-' failed on empty sequence,", 0,
                ((Sequence) result).getLength());

        result = compileExpression("$doubleval - 2").evaluate(context);
        assertEquals("Operator '-' failed,", 2.2 - 2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$doubleval - 0.2 - 5").evaluate(context);
        assertEquals("Operator '-' failed,", 2.2 - 0.2 - 5,
                ((DoubleValue) result).asJavaDouble(), 0.0000001);

        result = compileExpression("$intval - $intval").evaluate(context);
        assertEquals("Operator '-' failed,", 2 - 2, ((IntValue) result)
                .asJavaInt());

        result = compileExpression("$intval - 0.2").evaluate(context);
        assertEquals("Operator '-' failed,", 2 - 0.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$doubleval - 0.2").evaluate(context);
        assertEquals("Operator '-' failed,", 2.2 - 0.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        doTestOperatorExpression("=", "9 - 2 - 3 - 4 = 0", true);
    }

    /**
     * Tests that the * operator works with 2 numeric values
     * 
     * @throws Exception
     *             if an error occurs
     */
    public void testOperatorMultiply() throws Exception {
        Item[] empty = {};
        Item[] intval = { factory.createIntValue(2) };
        Item[] doubleval = { factory.createDoubleValue(2.2) };
        Value result;

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "empty"),
                factory.createSequence(empty));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "intval"),
                factory.createSequence(intval));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "doubleval"),
                factory.createSequence(doubleval));

        result = compileExpression("$doubleval * 0.2").evaluate(context);
        assertEquals("Operator '*' failed,", 2.2 * 0.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$empty * 0.2").evaluate(context);
        assertEquals("Operator '*' failed on empty sequence,", 0,
                ((Sequence) result).getLength());

        result = compileExpression("$doubleval * 2").evaluate(context);
        assertEquals("Operator '*' failed,", 2.2 * 2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$doubleval * 0.2 * 5").evaluate(context);
        assertEquals("Operator '*' failed,", 2.2 * 0.2 * 5,
                ((DoubleValue) result).asJavaDouble(), 0.0000001);

        result = compileExpression("$intval * $intval").evaluate(context);
        assertEquals("Operator '*' failed,", 2 * 2, ((IntValue) result)
                .asJavaInt());

        result = compileExpression("$intval * 0.2").evaluate(context);
        assertEquals("Operator '*' failed,", 2 * 0.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$doubleval * 0.2").evaluate(context);
        assertEquals("Operator '*' failed,", 2.2 * 0.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        doTestOperatorExpression("=", "2 * 3 * 4 = 24", true);
    }

    /**
     * Tests that the div operator works with 2 numeric values
     * 
     * @throws Exception
     *             if an error occurs
     */
    public void testOperatorDivide() throws Exception {
        Item[] empty = {};
        Item[] intval = { factory.createIntValue(2) };
        Item[] doubleval = { factory.createDoubleValue(2.2) };
        Value result;

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "empty"),
                factory.createSequence(empty));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "intval"),
                factory.createSequence(intval));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "doubleval"),
                factory.createSequence(doubleval));

        result = compileExpression("$doubleval div 0.2").evaluate(context);
        assertEquals("Operator 'div' failed,", 2.2 / 0.2,
                ((DoubleValue) result).asJavaDouble(), 0.0000001);

        result = compileExpression("$empty div 0.2").evaluate(context);
        assertEquals("Operator 'div' failed on empty sequence,", 0,
                ((Sequence) result).getLength());

        result = compileExpression("$doubleval div 2").evaluate(context);
        assertEquals("Operator 'div' failed,", 2.2 / 2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$doubleval div 0.2 div 5")
                .evaluate(context);
        assertEquals("Operator 'div' failed,", 2.2 / 0.2 / 5,
                ((DoubleValue) result).asJavaDouble(), 0.0000001);

        result = compileExpression("$intval div $intval").evaluate(context);
        assertEquals("Operator 'div' failed,", 2 / 2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$intval div 0.2").evaluate(context);
        assertEquals("Operator 'div' failed,", 2 / 0.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$doubleval div 0.2").evaluate(context);
        assertEquals("Operator 'div' failed,", 2.2 / 0.2,
                ((DoubleValue) result).asJavaDouble(), 0.0000001);

        doTestOperatorExpression("=", "4 div 2 div 4 = 0.5", true);
    }

    /**
     * Tests that the mod operator works with 2 numeric values
     * 
     * @throws Exception
     *             if an error occurs
     */
    public void testOperatorMod() throws Exception {
        Item[] empty = {};
        Item[] intval = { factory.createIntValue(2) };
        Item[] doubleval = { factory.createDoubleValue(2.2) };
        Value result;

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "empty"),
                factory.createSequence(empty));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "intval"),
                factory.createSequence(intval));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "doubleval"),
                factory.createSequence(doubleval));

        result = compileExpression("$doubleval mod 0.2").evaluate(context);
        assertEquals("Operator 'mod' failed,", 2.2 % 0.2,
                ((DoubleValue) result).asJavaDouble(), 0.0000001);

        result = compileExpression("$empty mod 0.2").evaluate(context);
        assertEquals("Operator 'mod' failed on empty sequence,", 0,
                ((Sequence) result).getLength());

        result = compileExpression("$doubleval mod 2").evaluate(context);
        assertEquals("Operator 'mod' failed,", 2.2 % 2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$doubleval mod 0.2 mod 5")
                .evaluate(context);
        assertEquals("Operator 'mod' failed,", 2.2 % 0.2 % 5,
                ((DoubleValue) result).asJavaDouble(), 0.0000001);

        result = compileExpression("$intval mod $intval").evaluate(context);
        assertEquals("Operator 'mod' failed,", 2 % 2, ((IntValue) result)
                .asJavaInt());

        result = compileExpression("$intval mod 0.2").evaluate(context);
        assertEquals("Operator 'mod' failed,", 2 % 0.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("$doubleval mod 0.2").evaluate(context);
        assertEquals("Operator 'mod' failed,", 2.2 % 0.2,
                ((DoubleValue) result).asJavaDouble(), 0.0000001);

        doTestOperatorExpression("=", "2 mod 3 = 2", true);
    }

    /**
     * Tests that the unary - operator works with numeric values
     * 
     * @throws Exception
     *             if an error occurs
     */
    public void testOperatorNegate() throws Exception {
        Item[] empty = {};
        Item[] intval = { factory.createIntValue(2) };
        Item[] doubleval = { factory.createDoubleValue(2.2) };
        Value result;

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "empty"),
                factory.createSequence(empty));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "intval"),
                factory.createSequence(intval));
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "doubleval"),
                factory.createSequence(doubleval));

        result = compileExpression("- $doubleval").evaluate(context);
        assertEquals("Operator '-' failed,", -2.2, ((DoubleValue) result)
                .asJavaDouble(), 0.0000001);

        result = compileExpression("- $empty").evaluate(context);
        assertEquals("Operator '-' failed on empty sequence,", 0,
                ((Sequence) result).getLength());

        result = compileExpression("- $intval").evaluate(context);
        assertEquals("Operator '-' failed,", -2, ((IntValue) result)
                .asJavaInt());

        doTestOperatorExpression("=", "- 2 = 2 - 4", true);
    }

    /**
     * Tests that the < operator works with 2 string values
     * 
     * @throws Exception
     *             if an error occurs
     */
    public void testStringLessThan() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "a"),
                factory.createStringValue("a"));

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "ab"),
                factory.createStringValue("ab"));

        doTestOperatorExpression("<", "$ab < $a", true);
        doTestOperatorExpression("<", "$a < $ab", false);
        doTestOperatorExpression("<", "$a < $a", false);
    }

    /**
     * Tests that the <= operator works with 2 string values
     * @throws Exception if an error occurs
     */
    public void testStringLessThanOrEqual() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "a"),
                factory.createStringValue("a"));

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "ab"),
                factory.createStringValue("ab"));

        doTestOperatorExpression("<=", "$ab <= $a", true);
        doTestOperatorExpression("<=", "$a <= $ab", false);
        doTestOperatorExpression("<=", "$a <= $a", true);
    }

    /**
     * Tests that the > operator works with 2 string values
     * @throws Exception if an error occurs
     */
    public void testStringGreaterThan() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "a"),
                factory.createStringValue("a"));

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "ab"),
                factory.createStringValue("ab"));

        doTestOperatorExpression(">", "$ab > $a", false);
        doTestOperatorExpression(">", "$a > $ab", true);
        doTestOperatorExpression(">", "$a > $a", false);
    }

    /**
     * Tests that the >= operator works with 2 string values
     * @throws Exception if an error occurs
     */
    public void testStringGreaterThanOrEqual() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "a"),
                factory.createStringValue("a"));

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "ab"),
                factory.createStringValue("ab"));

        doTestOperatorExpression(">=", "$ab >= $a", false);
        doTestOperatorExpression(">=", "$a >= $ab", true);
        doTestOperatorExpression(">=", "$a >= $a", true);
    }

    /**
     * Executes an "operator" expression an checks that the correct value
     * is returned
     * @param operator the String representation of the operator being tested.
     * Used for error reporting.
     * @param expression the expression to be tested
     * @param expected the expected result
     * @throws Exception if an error occurs
     */
    private void doTestOperatorExpression(String operator,
                                          String expression,
                                          boolean expected) throws Exception {
        // compile and evalute the expression
        Expression exp = compileExpression(expression);
        Value result = exp.evaluate(context);

        // check the result type
        assertTrue(operator + " expression should result in a boolean value",
                   result instanceof BooleanValue);

        // check the result
        assertEquals(operator + " expression result should be false",
                     expected,
                     ((BooleanValue) result).asJavaBoolean());

    }

    /**
     * Test for the not function in expressions
     * @throws Exception if an error occurs
     */
    public void testNot() throws Exception {
        Value one = factory.createIntValue(1);

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                one);

        Expression exp = compileExpression("not(3 < $myVar)");

        Value result = exp.evaluate(context);

        assertTrue("fn:not expression should result in a boolean value",
                   result instanceof BooleanValue);

        assertTrue("fn:not expression result should be true",
                   ((BooleanValue) result).asJavaBoolean());
    }

    /**
     * Test for the not function in expressions
     * @throws Exception if an error occurs
     */
    public void testNotWithTrue() throws Exception {

        Expression exp = compileExpression("not(true())");

        Value result = exp.evaluate(context);

        assertTrue("fn:not expression should result in a boolean value",
                   result instanceof BooleanValue);

        assertFalse("fn:not expression result should be false",
                    ((BooleanValue) result).asJavaBoolean());
    }

    /**
     * Test the boolean function with a String value argument.
     * @throws Exception if an error occurs
     */
    public void testBooleanStringArg() throws Exception {
        Expression exp = compileExpression("boolean(\"1\")");

        Value result = exp.evaluate(context);

        assertTrue("fn:boolean should result in a boolean value",
                   result instanceof BooleanValue);

        assertTrue("fn:boolean expression result should be true",
                   ((BooleanValue) result).asJavaBoolean());
    }

    /**
     * Test the boolean function with a sequence (of one numeric) argument.
     * @throws Exception if an error occurs
     */
    public void testBooleanNumericSequenceArg() throws Exception {
        Sequence seq = factory.createDoubleValue(1.0).getSequence();
        context.getCurrentScope().declareVariable(
                        new ImmutableExpandedName("", "myVar"),
                        seq);

        Expression exp = compileExpression("boolean($myVar)");

        Value result = exp.evaluate(context);

        assertTrue("fn:boolean should result in a boolean value",
                   result instanceof BooleanValue);

        assertTrue("fn:boolean expression result should be true",
                   ((BooleanValue) result).asJavaBoolean());
    }

    /**
     * Test the boolean function with an invalid String value argument.
     * @throws Exception if an error occurs
     */
    public void testBooleanStringInvalidString() throws Exception {
        try {
            Expression exp = compileExpression("boolean(\"hello\")");
            exp.evaluate(context);
            fail("boolean(\"hello\") should throw an exception");
        } catch (ExpressionException e) {
            // expected condition
        }
    }

    /**
     * Test the boolean function with a numeric argument.
     * @throws Exception if an error occurs
     */
    public void testBooleanNumericArg() throws Exception {
        Expression exp = compileExpression("boolean(0.0)");

        Value result = exp.evaluate(context);

        assertTrue("fn:boolean should result in a boolean value",
                   result instanceof BooleanValue);

        assertFalse("fn:boolean expression result should be false",
                    ((BooleanValue) result).asJavaBoolean());
    }

    /**
     * Test the number function with a numeric argument.
     * @throws Exception if an error occurs
     */
    public void testNumberNumericArg() throws Exception {
        Expression exp = compileExpression("number(0.0)");

        Value result = exp.evaluate(context);

        assertTrue("fn:number should result in a double value",
                   result instanceof DoubleValue);

        assertEquals("fn:number expression result should be 0.0",
                     0.0,
                     ((DoubleValue) result).asJavaDouble(),
                     0.1);
    }

    /**
     * Test the number function with a String argument.
     * @throws Exception if an error occurs
     */
    public void testNumberStringArg() throws Exception {
        Expression exp = compileExpression("number('99.9')");

        Value result = exp.evaluate(context);

        assertTrue("fn:number should result in a double value",
                   result instanceof DoubleValue);

        assertEquals("fn:number expression result should be 99.9",
                     99.9,
                     ((DoubleValue) result).asJavaDouble(),
                     0.1);
    }

    /**
     * Test the number function with a Boolean argument.
     * @throws Exception if an error occurs
     */
    public void testNumberBooleanArg() throws Exception {
        Expression exp = compileExpression("number(true())");

        Value result = exp.evaluate(context);

        assertTrue("fn:number should result in a double value",
                   result instanceof DoubleValue);

        assertEquals("fn:number expression result should be 1.0",
                     1.0,
                     ((DoubleValue) result).asJavaDouble(),
                     0.1);
    }

    /**
     * Test the number function with an invalid argument.
     * @throws Exception if an error occurs
     */
    public void testNumberInvalidArg() throws Exception {
        Expression exp = compileExpression("number('one')");

        Value result = exp.evaluate(context);

        assertTrue("fn:number should result in a Double value",
                   result instanceof DoubleValue);

        assertTrue("fn:number expression result should be NaN",
                   Double.isNaN(((DoubleValue) result).asJavaDouble()));
    }

    /**
     * Test the concat function with two string args
     * @throws Exception if an error occurs
     */
    public void testConcatTwoStringArgs() throws Exception {
        Expression exp = compileExpression("concat('con', 'cat')");

        Value result = exp.evaluate(context);

        assertTrue("fn:concat should result in a String value",
                   result instanceof StringValue);

        assertEquals("fn:concat expression result should be concat",
                     "concat",
                    ((StringValue) result).asJavaString());
    }

    /**
     * Test the concat function with five string args
     * @throws Exception if an error occurs
     */
    public void testConcatFiveStringArgs() throws Exception {
        Expression exp = compileExpression("concat('ex','pres','s','ion','s')");

        Value result = exp.evaluate(context);

        assertTrue("fn:concat should result in a String value",
                   result instanceof StringValue);

        assertEquals("fn:concat expression result should be expressions",
                     "expressions",
                    ((StringValue) result).asJavaString());
    }

    /**
     * Test the concat function with arguments of various types
     * @throws Exception if an error occurs
     */
    public void testConcatDifferentTypes() throws Exception {

        Value one = factory.createIntValue(1);

        context.getCurrentScope().declareVariable(
                        new ImmutableExpandedName("", "myVar"),
                        one);

        context.getCurrentScope().declareVariable(
                        new ImmutableExpandedName("", "emptySeq"),
                        Sequence.EMPTY);

        context.getCurrentScope().declareVariable(
                        new ImmutableExpandedName("", "singleSeq"),
                        factory.createSequence(new Item[]
                        {factory.createStringValue("one")}));

        Expression exp = compileExpression(
                "concat(true(), 25.9, $emptySeq, $myVar, $singleSeq)");

        Value result = exp.evaluate(context);

        assertTrue("fn:concat should result in a String value",
                   result instanceof StringValue);

        assertEquals("fn:concat expression result should be 'true25.91one'",
                     "true25.91one",
                    ((StringValue) result).asJavaString());
    }

    /**
     * Test the concat function with a single argument
     * @throws Exception if an error occurs
     */
    public void testConcatOneArg() throws Exception {
        Expression exp = compileExpression("concat('con')");

        try {
            exp.evaluate(context);
            fail("calling concat with a single argument should result " +
                 "in an ExpressionException");
        } catch (ExpressionException e) {
            // expected condition
        }
    }


    /**
     * Test the concat function when one of the args is a sequence of more
     * than one value.
     * @throws Exception if an error occurs
     */
    public void testConcatMultiValueSequence() throws Exception {

        // declare a variable whose value is a sequence of 2 items
        context.getCurrentScope().declareVariable(
                                new ImmutableExpandedName("", "seq"),
                                factory.createSequence(new Item[]
                                    {factory.createStringValue("one"),
                                     factory.createStringValue("two")}));


        Expression exp = compileExpression("concat('con', $seq)");

        try {
            exp.evaluate(context);
            fail("calling concat with a single argument should result " +
                 "in a JXPathException");
        } catch (ExpressionException e) {
            // expected condition
        }
    }

    /**
     * test for the AND operator
     * @throws Exception if an error occurs
     */
    public void testSimpleAndOperator() throws Exception {

        Expression exp = compileExpression("2 = 2 and 3 = 3");

        Value result = exp.evaluate(context);

        assertTrue("AND expression should result in a boolean value",
                   result instanceof BooleanValue);

        assertTrue("AND expression result should be true",
                   ((BooleanValue) result).asJavaBoolean());
    }

    /**
     * Another test for the AND operator
     * @throws Exception if an error occurs
     */
    public void testAndOperatorWithVariables() throws Exception {

        Value one = factory.createIntValue(1);

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                one);


        Expression exp = compileExpression("$myVar <= 1 and 4 > $myVar");

        Value result = exp.evaluate(context);

        assertTrue("AND expression should result in a boolean value",
                   result instanceof BooleanValue);

        assertTrue("AND expression result should be true",
                   ((BooleanValue) result).asJavaBoolean());
    }

    /**
     * test for the OR operator
     * @throws Exception if an error occurs
     */
    public void testSimpleOrOperator() throws Exception {

        Expression exp = compileExpression("2 = 3 or 3 = 3");

        Value result = exp.evaluate(context);

        assertTrue("OR expression should result in a boolean value",
                   result instanceof BooleanValue);

        assertTrue("OR expression result should be true",
                   ((BooleanValue) result).asJavaBoolean());
    }

    /**
     * another test for the OR operator
     * @throws Exception if an error occurs
     */
    public void testOrOperatorWithVariables() throws Exception {

        Value one = factory.createIntValue(1);

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                one);


        Expression exp = compileExpression("$myVar <= 1 or 0 > $myVar");

        Value result = exp.evaluate(context);

        assertTrue("OR expression should result in a boolean value",
                   result instanceof BooleanValue);

        assertTrue("OR expression result should be true",
                   ((BooleanValue) result).asJavaBoolean());
    }

    public void testInequalityEvaluate() throws Exception {
        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                result);

        Expression exp = compileExpression("$myVar != \"" +
                                           result.stringValue().
                                           asJavaString() + "\"");
        Value result = exp.evaluate(context);

        assertTrue("Inequality expression should result in a boolean value",
                   result instanceof BooleanValue);
        assertFalse("Inequality expression result should be false",
                    ((BooleanValue) result).asJavaBoolean());
    }
    
    public void testFunctionArgSequence() throws Exception {
        ImmutableExpandedName functionName =
                new ImmutableExpandedName("http://myNamespace", "test");
        NamespacePrefixTracker manager = context.getNamespacePrefixTracker();

        manager.startPrefixMapping("function",
                                   functionName.getNamespaceURI());

        context.registerFunction(
                functionName,
                new Function() {
                    public Value invoke(ExpressionContext context,
                                        Value[] arguments)
                            throws ExpressionException {
                        StringBuffer buffer = new StringBuffer();

                        for (int i=0; i<arguments.length; i++) {
                            addTo(buffer, arguments[i]);
                            if (i < arguments.length - 1) {
                                buffer.append(", ");
                            }                        		
                        }

                        return factory.createStringValue(buffer.toString());
                    }
                    
                    private void addTo(final StringBuffer buffer, final Value value) {
                    	try {
                            Sequence s = value.getSequence();

                            if (s.getLength() != 1) {
                                buffer.append('(');

                                for (int i = 0; i < s.getLength(); i++) {
                                   Item item = s.getItem(i+1);

                                   addTo(buffer, item);
                                   if (i<s.getLength()-1) {
                                       buffer.append(", ");
                                   }
                                }
                                buffer.append(')');
                            } else {
                               buffer.append(value.stringValue().asJavaString());
                            }
                    	} catch (Exception e) {
                    		
                    	}
                    }                    
                });

        Expression exp = compileExpression("function:test(((\"a\"),\"b\"), " 
        									+ "(\"c\",\"d\"), \"e\")");

        Value eval = exp.evaluate(context);
        
        assertTrue("Expression should result in a string value",
                   eval instanceof StringValue);

        assertEquals("Expression result not as",
                     "(a, b), (c, d), e",  eval.stringValue().asJavaString());
    }
    

    public void testExpressionWithFunction() throws Exception {
        ImmutableExpandedName functionName =
                new ImmutableExpandedName("http://myNamespace", "test");
        NamespacePrefixTracker manager = context.getNamespacePrefixTracker();

        manager.startPrefixMapping("function",
                                   functionName.getNamespaceURI());

        context.getCurrentScope().declareVariable(
                new ImmutableExpandedName("", "myVar"),
                result);

        context.registerFunction(
                functionName,
                new Function() {
                    public Value invoke(ExpressionContext context,
                                        Value[] arguments)
                            throws ExpressionException {
                        StringBuffer buffer = new StringBuffer();

                        for (int i = 0;
                             i < arguments.length;
                             i++) {
                            buffer.append(arguments[i].stringValue().
                                          asJavaString());

                            if (i < arguments.length - 1) {
                                buffer.append(' ');
                            }
                        }

                        return factory.createStringValue(buffer.toString());
                    }
                });

        // Literal numbers are promoted to double values, not ints
        Expression exp = compileExpression("function:test('a', 2, $myVar)");

        Value eval = exp.evaluate(context);

        assertTrue("Expression should result in a string value",
                   eval instanceof StringValue);

        assertEquals("Expression result not as",
                     "a 2 " + result.stringValue().asJavaString(),
                     eval.stringValue().asJavaString());
    }

    /**
     * Test to ensure that if a function throws an
     * <code>ExpressionException</code> this is propagated correctly when
     * the function is evaluated.
     *
     * @throws Exception if an unexpected exception occurs
     */
    public void testFunctionThrowingExpressionException() throws Exception {
        ImmutableExpandedName functionName =
                new ImmutableExpandedName("http://myNamespace", "test");
        NamespacePrefixTracker manager = context.getNamespacePrefixTracker();

        manager.startPrefixMapping("function",
                                   functionName.getNamespaceURI());

        context.registerFunction(
                functionName,
                new Function() {
                    public Value invoke(ExpressionContext context,
                                        Value[] arguments)
                            throws ExpressionException {
                        throw new ExpressionException();
                    }
                });

        // Compile and execute the function
        Expression exp = compileExpression("function:test()");

        try {
            Value eval = exp.evaluate(context);
            fail("Function should have thrown ExpressionException, but " +
                    "no exception thrown.");
        } catch (ExpressionException ee) {
            // Expected result - test successful
        }
    }

    /**
     * Test to ensure that an <code>ExpressionException</code> is thrown
     * if a unknown function is evaluated
     * @throws Exception if an error occurs
     */
    public void testInvokeNonExistentFunction() throws Exception {

        // pasre the expression
        Expression exp = compileExpression("undeclaredFuntion()");

        try {
            // try to evaluate the expression. The function has not been
            // registered with the context so we expect a ExpressionException
            // to be thrown.
            exp.evaluate(context);
            fail("Evaluating a non existent function should result in an " +
                 "ExpressionException");
        } catch (ExpressionException e) {
            // expected
        }

    }

    /**
     * Test to ensure that an empty sequence is parsed and correctly evaluated
     * @throws Exception an error occurs
     */
    public void testEmptySequence() throws Exception {
        Expression exp = compileExpression("()");
        Value result = exp.evaluate(context);
        // result should be an empty sequence
        assertTrue("Result is not a Sequence instance",
                   result instanceof Sequence);

        Sequence sequence = (Sequence) result;

        assertEquals("Sequence is not empty", 0, sequence.getLength());
    }

    /**
     * Test to ensure that an empty sequence inside an empty sequence is evaluated to empty sequence
     * @throws Exception an error occurs
     */
    public void testSequenceContainingEmptySequence() throws Exception {
        Expression exp = compileExpression("(())");
        Value result = exp.evaluate(context);
        // result should be an empty sequence
        assertTrue("Result is not a Sequence instance",
                   result instanceof Sequence);

        Sequence sequence = (Sequence) result;

        assertEquals("Sequence is not empty", 0, sequence.getLength());
    }

    /**
     * Test to ensure that empty sequence item is removed after evaluation
     * @throws Exception an error occurs
     */
    public void testSequenceContainingEmptySequenceAndStrings() throws Exception {
        Expression exp = compileExpression("('a', (), 'b')");
        Value result = exp.evaluate(context);
        assertTrue("Result is not a Sequence instance",
                   result instanceof Sequence);

        Sequence sequence = (Sequence) result;

        assertEquals("Sequence length is not 2", 2, sequence.getLength());
        assertTrue("First item is not a string", sequence.getItem(1) instanceof StringValue);
        assertTrue("Second item is not a string", sequence.getItem(2) instanceof StringValue);
        assertEquals("First item is not \"a\"", "a", ((StringValue) sequence.getItem(1)).asJavaString());
        assertEquals("Second item is not \"b\"", "b", ((StringValue) sequence.getItem(2)).asJavaString());
    }

    /**
     * Helper method that parse an XPath expression.
     *
     * @param xpath the xpath string to parse
     * @return an Expression instance
     */
    private Expression compileExpression(String xpath) throws Exception {
        JXPathExpressionParser parser = new JXPathExpressionParser(factory);

        return parser.parse(xpath);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Jan-06	10855/4	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expressions.

 28-Dec-05	10855/2	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expressions.

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 08-Jul-04	773/1	adrianj	VBM:2003080416 Fixed propagation of ExpressionException during function evaluation

 24-Nov-03	468/1	doug	VBM:2003112103 Added support for the XPath concat function

 20-Nov-03	458/2	doug	VBM:2003111910 Added test cases to test the XPath number() function

 27-Oct-03	433/1	doug	VBM:2003102002 Added several new comparison operators

 22-Aug-03	386/3	doug	VBM:2003080702 Fixed issue with expression predicates

 31-Jul-03	222/6	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 31-Jul-03	222/4	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
