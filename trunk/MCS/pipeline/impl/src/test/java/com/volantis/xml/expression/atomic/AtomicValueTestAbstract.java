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
package com.volantis.xml.expression.atomic;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;

/**
 * Tests the AtomicValue interface methods.
 */
public abstract class AtomicValueTestAbstract
        extends TestCaseAbstract {

    protected AtomicValue value;
    protected ExpressionFactory expressionFactory;

    protected void setUp() throws Exception {
        super.setUp();

        expressionFactory = ExpressionFactory.getDefaultInstance();

        value = createValue();
    }

    protected final void tearDown() throws Exception {
        value = null;

        super.tearDown();
    }

    /**
     * Must be overridden in specializations to return an instance of the
     * AtomicValue specialization to be tested.
     *
     * @return an instance of the the AtomicValue specialization to be tested
     */
    protected abstract AtomicValue createValue();

    /**
     * Must be overridden in specializations to return the Java string
     * equivalent to the atomic value instance returned by {@link
     * #createValue}.
     *
     * @return a Java string equivalent to the value returned by createValue
     */
    protected abstract String getStringValue();

    public void testStringValue() throws Exception {
        doTestStringValue(getStringValue());
    }

    public void testStreamContents() throws Exception {
        doTestStreamContents(getStringValue());
    }

    public void testGetSequence() throws Exception {
        doTestGetSequence();
    }

    public void doTestStringValue(String expected) throws Exception {
        assertEquals("stringValue() not as",
                expected,
                value.stringValue().asJavaString());
    }

    public void doTestStreamContents(String expected) throws Exception {
        final StringBuffer output = new StringBuffer();

        ContentHandler handler = new ContentHandler() {
            public void setDocumentLocator(Locator locator) {
            }

            public void startDocument() {
            }

            public void endDocument() {
            }

            public void startPrefixMapping(
                    String s,
                    String s1) {
            }

            public void endPrefixMapping(String s) {
            }

            public void startElement(
                    String s,
                    String s1,
                    String s2,
                    Attributes attributes) {
            }

            public void endElement(
                    String s,
                    String s1,
                    String s2) {
            }

            public void characters(
                    char[] chars,
                    int offset,
                    int len) {
                output.append(chars, offset, len);
            }

            public void ignorableWhitespace(
                    char[] chars,
                    int offset,
                    int len) {
            }

            public void processingInstruction(
                    String s,
                    String s1) {
            }

            public void skippedEntity(String s) {
            }
        };

        value.streamContents(handler);

        assertEquals("streamContents() not as",
                expected,
                output.toString());
    }

    public void doTestGetSequence() throws Exception {
        assertEquals("getSequence().getLength() not as",
                1,
                value.getSequence().getLength());
        assertSame("getSequence().getItem(1) not as",
                value,
                value.getSequence().getItem(1));
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
