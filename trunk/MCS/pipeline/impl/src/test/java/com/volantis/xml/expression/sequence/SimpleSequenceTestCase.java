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
package com.volantis.xml.expression.sequence;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.SequenceIndexOutOfBoundsException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple test case.
 */
public class SimpleSequenceTestCase
        extends TestCaseAbstract {

    private ExpressionFactory factory;

    protected void setUp() throws Exception {
        super.setUp();

        factory = ExpressionFactory.getDefaultInstance();
    }

    protected SimpleSequence createSequence(Item[] items) {
        return new SimpleSequence(factory, items);
    }

    protected Item[] createItems(String[] values) {
        Item[] items;

        if (values != null) {
            items = new Item[values.length];

            for (int i = 0;
                 i < values.length;
                 i++) {
                items[i] = factory.createStringValue(values[i]);
            }
        } else {
            items = null;
        }

        return items;
    }

    public void testGetLength() throws Exception {
        String[] oneItem = {"In"};
        String[] manyItems = {"In", "the", "town", "where", "I", "was", "born",
                              "lived", "a", "man", "who", "sailed", "the",
                              "seas"};
        Sequence nil = createSequence((Item[]) null);
        Sequence empty = createSequence(new Item[0]);
        Sequence one = createSequence(createItems(oneItem));
        Sequence many = createSequence(createItems(manyItems));

        assertEquals("null length not as",
                     0,
                     nil.getLength());

        assertEquals("empty length not as",
                     0,
                     empty.getLength());

        assertEquals("one length not as",
                     oneItem.length,
                     one.getLength());

        assertEquals("many length not as",
                     manyItems.length,
                     many.getLength());
    }

    public void testGetItem() throws Exception {
        String[] oneItem = {"In"};
        String[] manyItems = {"In", "the", "town", "where", "I", "was", "born",
                              "lived", "a", "man", "who", "sailed", "the",
                              "seas"};
        Sequence nil = createSequence(null);
        Sequence empty = createSequence(new Item[0]);
        Sequence one = createSequence(createItems(oneItem));
        Sequence many = createSequence(createItems(manyItems));

        try {
            nil.getItem(1);

            fail("null getItem() should have thrown an exception");
        } catch (SequenceIndexOutOfBoundsException e) {
            // Expected behaviour
        }

        try {
            empty.getItem(1);

            fail("empty getItem() should have thrown an exception");
        } catch (SequenceIndexOutOfBoundsException e) {
            // Expected behaviour
        }

        try {
            one.getItem(0);

            fail("one getItem(0) should have thrown an exception");
        } catch (SequenceIndexOutOfBoundsException e) {
            // Expected behaviour
        }

        try {
            one.getItem(2);

            fail("one getItem(2) should have thrown an exception");
        } catch (SequenceIndexOutOfBoundsException e) {
            // Expected behaviour
        }

        assertEquals("one.getItem(1) not as",
                     oneItem[0],
                     one.getItem(1).stringValue().asJavaString());

        for (int i = 0;
             i < manyItems.length;
             i++) {
            assertEquals("many.getItem(" + (i + 1) + ") not as",
                         manyItems[i],
                         many.getItem(i + 1).stringValue().asJavaString());
        }
    }

    public void testStringValue() throws Exception {
        String[] oneItem = {"And"};
        String[] manyItems = {"And", "he", "told", "us", "of", "his", "life",
                              "in", "the", "land", "of", "submarines"};
        final String MANY = "And he told us of his life " +
            "in the land of submarines";
        Sequence nil = createSequence(null);
        Sequence empty = createSequence(new Item[0]);
        Sequence one = createSequence(createItems(oneItem));
        Sequence many = createSequence(createItems(manyItems));

        assertEquals("null stringValue not as",
                     "",
                     nil.stringValue().asJavaString());

        assertEquals("empty stringValue not as",
                     "",
                     empty.stringValue().asJavaString());

        assertEquals("one stringValue not as",
                     oneItem[0],
                     one.stringValue().asJavaString());

        assertEquals("many stringValue not as",
                     MANY,
                     many.stringValue().asJavaString());
    }

    public void testStreamContents() throws Exception {
        final StringBuffer output = new StringBuffer();
        String[] oneItem = {"So"};
        String[] manyItems = {"So", "we", "set", "of", "to", "the", "sun",
                              "until", "we", "found", "the", "sea", "of",
                              "green"};
        final String MANY = "So we set of to the sun " +
            "until we found the sea of green";
        Sequence nil = createSequence(null);
        Sequence empty = createSequence(new Item[0]);
        Sequence one = createSequence(createItems(oneItem));
        Sequence many = createSequence(createItems(manyItems));

        ContentHandler handler = new ContentHandler() {
            public void setDocumentLocator(Locator locator) {
            }

            public void startDocument() {
            }

            public void endDocument() {
            }

            public void startPrefixMapping(String s,
                                           String s1) {
            }

            public void endPrefixMapping(String s) {
            }

            public void startElement(String s,
                                     String s1,
                                     String s2,
                                     Attributes attributes) {
            }

            public void endElement(String s,
                                   String s1,
                                   String s2) {
            }

            public void characters(char[] chars,
                                   int offset,
                                   int len) {
                output.append(chars, offset, len);
            }

            public void ignorableWhitespace(char[] chars,
                                            int offset,
                                            int len) {
            }

            public void processingInstruction(String s,
                                              String s1) {
            }

            public void skippedEntity(String s) {
            }
        };

        nil.streamContents(handler);

        assertEquals("null streamContents not as",
                     "",
                     output.toString());

        output.setLength(0);

        empty.streamContents(handler);

        assertEquals("empty streamContents not as",
                     "",
                     output.toString());

        output.setLength(0);

        one.streamContents(handler);

        assertEquals("one streamContents not as",
                     oneItem[0],
                     output.toString());

        output.setLength(0);

        many.streamContents(handler);

        assertEquals("many streamContents not as",
                     MANY,
                     output.toString());
    }

    public void testGetSequence() throws Exception {
        String[] oneItem = {"There"};
        String[] manyItems = {"There", "we", "lived", "beneath", "the",
                              "waves", "in", "our", "yellow", "submarine"};
        Sequence nil = createSequence(null);
        Sequence empty = createSequence(new Item[0]);
        Sequence one = createSequence(createItems(oneItem));
        Sequence many = createSequence(createItems(manyItems));

        assertSame("null getSequence not as",
                   nil,
                   nil.getSequence());

        assertSame("empty getSequence not as",
                   empty,
                   empty.getSequence());

        assertSame("one getSequence not as",
                   one,
                   one.getSequence());

        assertSame("many getSequence not as",
                   many,
                   many.getSequence());
    }

    public void testSequenceConstants() throws Exception {
        assertEquals("EMPTY getLength not as",
                     0,
                     Sequence.EMPTY.getLength());

        assertEquals("EMPTY stringValue not as",
                     "",
                     Sequence.EMPTY.stringValue().asJavaString());
    }

    public void testIterator() throws Exception {
        String[] oneItem = {"We"};
        String[] manyItems = {"We", "all", "live", "in", "a",
                              "yellow", "submarine"};

        SimpleSequence nil = createSequence(null);
        SimpleSequence empty = createSequence(new Item[0]);
        SimpleSequence one = createSequence(createItems(oneItem));
        SimpleSequence many = createSequence(createItems(manyItems));
        Iterator iterator;

        assertFalse("null sequence iterator should not have next",
                    nil.iterator().hasNext());
        assertFalse("empty sequence iterator should not have next",
                    empty.iterator().hasNext());

        iterator = one.iterator();

        assertTrue("one sequence iterator should have next",
                   iterator.hasNext());

        assertSame("one sequence iterator next not as",
                   one.getItem(1),
                   iterator.next());
        assertFalse("one sequence iterator should not have next",
                    iterator.hasNext());

        iterator = many.iterator();

        for (int i = 0; i < many.getLength(); i++) {
            assertTrue("many sequence [" + i + "] iterator should have next",
                       iterator.hasNext());
            assertSame("many sequence [" + i + "] next not as",
                       many.getItem(i + 1),
                       iterator.next());
        }

        assertFalse("many sequence iterator should not have next",
                    iterator.hasNext());

        try {
            iterator.next();

            fail("Should have had an exception");
        } catch (NoSuchElementException e) {
            // Expected condition
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
