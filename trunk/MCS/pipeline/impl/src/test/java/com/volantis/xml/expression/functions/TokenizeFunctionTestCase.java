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
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.namespace.DefaultNamespacePrefixTracker;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;


/**
 * This class tests the tokenize function.
 */
public class TokenizeFunctionTestCase extends PipelineTestAbstract {


    // javadoc inherited
    public void registerExpressionFunctions(ExpressionContext context) {
        context.registerFunction(new ImmutableExpandedName(
                Namespace.PIPELINE.getURI(), "tokenize"),
                new TokenizeFunction()
        );
    }

    /**
     * Tests the empty sequence input gives an empty sequence result.
     */
    public void testEmptySequenceInput() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function tokenize = new TokenizeFunction();
        Sequence sequence = Sequence.EMPTY;
        Value search = factory.createStringValue("a");
        Value retVal = tokenize.invoke(context, new Value[]{sequence, search});

        final Sequence retSeq = retVal.getSequence();

        // Should have returned ()
        assertSame(retSeq, Sequence.EMPTY);
    }

    /**
     * Tests the empty string input gives an empty sequence result.
     */
    public void testEmptyStringInput() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function tokenize = new TokenizeFunction();
        Sequence sequence = createSequence(factory,
                new String[]{""});
        Value search = factory.createStringValue("a");
        Value retVal = tokenize.invoke(context, new Value[]{sequence, search});

        final Sequence retSeq = retVal.getSequence();

        // Should have returned ()
        assertSame(retSeq, Sequence.EMPTY);
    }

    /**
     * Tests that the empty pattern throws an ExpressionException.
     */
    public void testEmptyPattern() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function tokenize = new TokenizeFunction();
        Sequence sequence = createSequence(factory,
                new String[]{"my search string"});
        Value pattern = factory.createStringValue("");
        try {
            Value retVal = tokenize.invoke(context,
                    new Value[]{sequence, pattern});
            fail("An empty pattern should result in an ExpressionException");
        } catch (ExpressionException ee) {
            // This exception is expected
        }
    }

    /**
     * Tests that pattern at start gives an empty string token at start.
     */
    public void testStartsWithPattern() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function tokenize = new TokenizeFunction();
        Sequence sequence = createSequence(factory,
                new String[]{"abcdefghicat"});
        Value pattern = factory.createStringValue("abc");
        Value retVal = tokenize.invoke(context,
                new Value[]{sequence, pattern});

        final Sequence retSeq = retVal.getSequence();

        doSequenceEquality(retSeq, new String[]{"", "defghicat"});
    }

    /**
     * Tests that pattern at end gives an empty string token at end.
     */
    public void testEndsWithPattern() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function tokenize = new TokenizeFunction();
        Sequence sequence = createSequence(factory,
                new String[]{"abcdefghicat"});
        Value pattern = factory.createStringValue("cat");
        Value retVal = tokenize.invoke(context,
                new Value[]{sequence, pattern});

        final Sequence retSeq = retVal.getSequence();

        doSequenceEquality(retSeq, new String[]{"abcdefghi", ""});
    }

    /**
     * Tests a regexp whitespace pattern.
     */
    public void testWhitespacePattern() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function tokenize = new TokenizeFunction();
        Sequence sequence = createSequence(factory,
                new String[]{"the cat  sat   on    the     mat"});
        Value pattern = factory.createStringValue("\\s+");
        Value retVal = tokenize.invoke(context,
                new Value[]{sequence, pattern});

        final Sequence retSeq = retVal.getSequence();

        doSequenceEquality(retSeq,
                new String[]{"the", "cat", "sat", "on", "the", "mat"});
    }

    /**
     * Tests a 'comma x' pattern.
     */
    public void testCommaPattern() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function tokenize = new TokenizeFunction();
        Sequence sequence = createSequence(factory,
                new String[]{"thexcat,x,xxxsatxxxxon,thexxxxxmat"});
        Value pattern = factory.createStringValue(",x");
        Value retVal = tokenize.invoke(context,
                new Value[]{sequence, pattern});

        final Sequence retSeq = retVal.getSequence();

        doSequenceEquality(retSeq,
                new String[]{"thexcat", "", "xxsatxxxxon,thexxxxxmat"});
    }

    /**
     * Tests when input matches pattern, a sequence with an empty string
     * results.
     */
    public void testStringEqualsPattern() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function tokenize = new TokenizeFunction();
        Sequence sequence = createSequence(factory,
                new String[]{"one big long missspelt sentence"});
        Value pattern =
                factory.createStringValue("one big long missspelt sentence");
        Value retVal = tokenize.invoke(context,
                new Value[]{sequence, pattern});

        final Sequence retSeq = retVal.getSequence();

        doSequenceEquality(retSeq, new String[]{""});
    }

    /**
     * Tests when input equals multiple occurrences of pattern that a sequence
     * of empty strings (one for each match, plus one extra) results.
     */
    public void testStringEqualsMultipleSingleLetterPattern()
            throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function tokenize = new TokenizeFunction();
        Sequence sequence = createSequence(factory,
                new String[]{"xxxxxxxxxx"});
        Value pattern = factory.createStringValue("x");
        Value retVal = tokenize.invoke(context,
                new Value[]{sequence, pattern});

        final Sequence retSeq = retVal.getSequence();

        doSequenceEquality(retSeq,
                new String[]{"", "", "", "", "", "", "", "", "", "", ""});
    }

    /**
     * Tests when input equals multiple occurrences of pattern that a sequence
     * of empty strings (one for each match, plus one extra) results.
     */
    public void testStringEqualsMultipleMultipleLetterPattern()
            throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function tokenize = new TokenizeFunction();
        Sequence sequence = createSequence(factory,
                new String[]{"abcabcabcabcabcabcabcabcabcabc"});
        Value pattern = factory.createStringValue("abc");
        Value retVal = tokenize.invoke(context,
                new Value[]{sequence, pattern});

        final Sequence retSeq = retVal.getSequence();

        doSequenceEquality(retSeq,
                new String[]{"", "", "", "", "", "", "", "", "", "", ""});
    }

    /**
     * Tests when input is a multiple concatenation of pattern with a
     * 'leftover', that each match generates an empty string and the last
     * sequence member is the leftover bit.
     */
    public void testRemainderX() throws Exception {
        ExpressionContext context = createExpressionContext();
        ExpressionFactory factory = context.getFactory();
        Function tokenize = new TokenizeFunction();
        Sequence sequence = createSequence(factory,
                new String[]{"xxxxxxxxxx"});
        Value pattern = factory.createStringValue("xxx");
        Value retVal = tokenize.invoke(context,
                new Value[]{sequence, pattern});

        final Sequence retSeq = retVal.getSequence();

        doSequenceEquality(retSeq, new String[]{"", "", "", "x"});
    }

    /**
     * Tests that failure occurs when function is given no arguments.
     */
    public void testNoArguments() throws Exception {
        try {
            ExpressionContext context = createExpressionContext();
            Function tokenize = new TokenizeFunction();
            Value retVal = tokenize.invoke(context, new Value[]{});
            fail("Should have failed when invoked with no arguments");
        } catch (ExpressionException expected) {
        }
    }

    /**
     * Tests that failure occurs when function is given one argument.
     */
    public void testOneArgument() throws Exception {
        try {
            ExpressionContext context = createExpressionContext();
            ExpressionFactory factory = context.getFactory();
            Sequence sequence = createSequence(factory,
                    new String[]{"xxxxxxxxxx"});
            Function tokenize = new TokenizeFunction();
            Value retVal = tokenize.invoke(context, new Value[]{sequence});
            fail("Should have failed when invoked with only one argument");
        } catch (ExpressionException expected) {
        }
    }

    /**
     * Tests that failure occurs when function is given three arguments.
     */
    public void testThreeArguments() throws Exception {
        try {
            ExpressionContext context = createExpressionContext();
            ExpressionFactory factory = context.getFactory();
            Sequence sequence = createSequence(factory,
                    new String[]{"xxxxxxxxxx"});
            Value pattern = factory.createStringValue("xxx");
            Function tokenize = new TokenizeFunction();
            Value retVal = tokenize.invoke(context,
                    new Value[]{sequence, pattern, pattern});
            fail("Should have failed when invoked with three arguments");
        } catch (ExpressionException expected) {
        }
    }

    /**
     * Helper method which tests the given sequence consists of the given
     * strings.
     *
     * @param retSeq the sequence to check
     * @param strings the strings
     */
    private void doSequenceEquality(Sequence retSeq,
                                    String[] strings) throws Exception {
        assertTrue("Sequence length should be " + strings.length +
                ", was " + retSeq.getLength(),
                retSeq.getLength() == strings.length);
        for (int i = 1; i <= strings.length; i++) {
            Item item = retSeq.getItem(i);
            assertTrue("Item " + i + " should be same string",
                    strings[i - 1].equals(item.stringValue().asJavaString()));
        }
    }

    /**
     * Helper method which creates a sequence of strings.
     *
     * @param factory the factory which creates the sequence
     * @param strings the strings
     * @return the sequence of strings
     */
    private Sequence createSequence(ExpressionFactory factory,
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
