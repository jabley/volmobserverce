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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.shared.environment.SimpleEnvironmentInteractionTracker;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.namespace.DefaultNamespacePrefixTracker;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;

/**
 * This class tests the index-of function.
 */
public class IndexOfFunctionTestCase extends PipelineTestAbstract {


    // javadoc inherited
    public void registerExpressionFunctions(ExpressionContext context) {
        context.registerFunction(new ImmutableExpandedName(
                Namespace.PIPELINE.getURI(), "index-of"),
                new IndexOfFunction()
        );
    }

    /**
     * Tests that the search string is found where expected.
     */
    public void testSearchPresent() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function indexOf = new IndexOfFunction();
        Sequence sequence = createStringSequence(factory,
                new String[]{"a", "dog", "and", "a", "duck"});
        Value search = factory.createStringValue("a");
        Value retVal = indexOf.invoke(context, new Value[]{sequence, search});

        final Sequence retSeq = retVal.getSequence();

        // Should have returned (1, 4)
        assertTrue(retSeq.getLength() == 2);
        assertTrue(((IntValue) retSeq.getItem(1)).asJavaInt() == 1);
        assertTrue(((IntValue) retSeq.getItem(2)).asJavaInt() == 4);
    }

    /**
     * Tests that the search string is not found.
     */
    public void testSearchNotPresent() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function indexOf = new IndexOfFunction();
        Sequence sequence = createStringSequence(factory,
                new String[]{"a", "dog", "and", "a", "duck"});
        Value search = factory.createStringValue("cat");
        Value retVal = indexOf.invoke(context, new Value[]{sequence, search});

        final Sequence retSeq = retVal.getSequence();

        // Should have returned ()
        assertSame(retSeq, Sequence.EMPTY);
    }

    /**
     * Tests that the search string is found at all positions.
     */
    public void testAllItemsMatch() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function indexOf = new IndexOfFunction();
        Sequence sequence = createStringSequence(factory,
                new String[]{"abc", "abc", "abc", "abc", "abc"});
        Value search = factory.createStringValue("abc");
        Value retVal = indexOf.invoke(context, new Value[]{sequence, search});

        final Sequence retSeq = retVal.getSequence();

        // Should have returned (1, 2, 3, 4, 5)
        assertTrue(retSeq.getLength() == 5);
        for (int i = 1; i <= 5; i++) {
            assertTrue(((IntValue) retSeq.getItem(i)).asJavaInt() == i);
        }
    }

    /**
     * Tests that an empty search string causes the empty sequence to be
     * returned.
     */
    public void testEmptySearch() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function indexOf = new IndexOfFunction();
        Sequence sequence = createStringSequence(factory,
                new String[]{"abc", "abc", "abc", "abc", "abc"});
        Value search = factory.createStringValue("");
        Value retVal = indexOf.invoke(context, new Value[]{sequence, search});

        final Sequence retSeq = retVal.getSequence();

        // Should have returned ()
        assertSame(retSeq, Sequence.EMPTY);
    }

    /**
     * Tests that an empty sequence causes the empty sequence to be
     * returned.
     */
    public void testEmptySequence() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function indexOf = new IndexOfFunction();
        Sequence sequence = createStringSequence(factory, null);
        Value search = factory.createStringValue("peter");
        Value retVal = indexOf.invoke(context, new Value[]{sequence, search});

        final Sequence retSeq = retVal.getSequence();

        // Should have returned ()
        assertSame(retSeq, Sequence.EMPTY);
    }

    /**
     * Tests that an empty sequence and empty search string causes the empty
     * sequence to be returned.
     */
    public void testEmptySequenceEmptySearch() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function indexOf = new IndexOfFunction();
        Sequence sequence = createStringSequence(factory, null);
        Value search = factory.createStringValue("");
        Value retVal = indexOf.invoke(context, new Value[]{sequence, search});

        final Sequence retSeq = retVal.getSequence();

        // Should have returned ()
        assertSame(retSeq, Sequence.EMPTY);
    }

    /**
     * Tests that the search integer is found where expected.
     * ((15, 40, 25, 40, 10), 40)
     */
    public void testIntegerSequence() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function indexOf = new IndexOfFunction();
        Sequence sequence = createIntegerSequence(factory,
                new int[]{15, 40, 25, 40, 10});
        Value search = factory.createIntValue(40);
        Value retVal = indexOf.invoke(context, new Value[]{sequence, search});

        final Sequence retSeq = retVal.getSequence();

        // Should have returned (2, 4)
        assertTrue(retSeq.getLength() == 2);
        assertTrue(((IntValue) retSeq.getItem(1)).asJavaInt() == 2);
        assertTrue(((IntValue) retSeq.getItem(2)).asJavaInt() == 4);
    }

    /**
     * Helper method which creates a sequence of strings.
     *
     * @param factory the factory which creates the sequence
     * @param strings the strings
     * @return the sequence of strings
     */
    private Sequence createStringSequence(ExpressionFactory factory,
                                          String[] strings) {
        Item [] items = null;
        if (strings != null && strings.length > 0) {
            items = new Item[strings.length];
            for (int i = 0; i < strings.length; i++) {
                items[i] = factory.createStringValue(strings[i]);
            }
        }

        return factory.createSequence(items);
    }

    /**
     * Helper method which creates a sequence of integers.
     *
     * @param factory the factory which creates the sequence
     * @param ints the integers
     * @return the sequence of integers
     */
    private Sequence createIntegerSequence(ExpressionFactory factory,
                                           int[] ints) {
        Item [] items = null;
        if (ints != null && ints.length > 0) {
            items = new Item[ints.length];
            for (int i = 0; i < ints.length; i++) {
                items[i] = factory.createIntValue(ints[i]);
            }
        }

        return factory.createSequence(items);
    }

    /**
     * Helper method which creates an expression context.
     *
     * @return the expression context
     */
    private ExpressionContext createExpressionContext() {
        EnvironmentInteractionTracker eit =
                new SimpleEnvironmentInteractionTracker();
        NamespacePrefixTracker npt = new DefaultNamespacePrefixTracker();
        ExpressionContext context = ExpressionFactory.getDefaultInstance().
                createExpressionContext(eit, npt);
        return context;
    }
}
