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
package com.volantis.mcs.servlet;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Tests the AcceptParser.
 *
 * @todo AcceptParser was ported to Synergetics (see VBM 2006032810). This test case was also ported, but it also tests related classes in MCS.
 * These tests were commented out in Synergetics, and the full test case is left here until frameworks ports related classes to Synergetics.
 */
public class AcceptParserTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Internal acceptable iterator for the accept charset parser which checks 
     * the acceptable contents of the accept charset parser against an array 
     * of charsets.
     */
    private static class ArrayCheckIterator
            implements AcceptParser.InternalAcceptableIterator {
        private AcceptParser parser;
        private ArrayList values = new ArrayList();
        private int i;

        public ArrayCheckIterator(AcceptParser parser) {
            this.parser = parser;
        }

        void add(String[] values) {
            this.values.addAll(Arrays.asList(values));
        }

        void add(String value) {
            values.add(value);
        }

        public void before() {
            // nothing to do before.
        }

        public void next(String name) {
            String expected = (String) values.get(i);
            // Check the entry against the array.
            assertEquals("found unexpected " + name + " at index " + i +
                    " expected " + values, expected, name);
            // Check the entry name/array name is contained as acceptable too.
            assertTrue(parser.containsAcceptable(expected));
            i++;
        }

        public void after() {
            // Ensure that the number of entries we got matched the array size 
            // (to ensure we didn't miss any).
            assertEquals(values.size(), i);
        }
    }

    public AcceptParserTestCase(String name) {
        super(name);
    }

    /**
     * Tests that parsing the header retrieves the expected charsets
     * @param header the http Accept-Charset header
     * @param expected an array of the expected charsets in order
     */
    private void doCharsetParserTest(String header, String[] expected) {
        doCharsetParserTest(new String[]{header}, expected);
    }

    /**
     * Tests that parsing the headers retrieves the expected charsets
     * @param headers the http Accept-Charset header
     * @param expected an array of the expected charsets in order
     */
    private void doCharsetParserTest(String[] headers, String[] expected) {
        doTest(AcceptParserFactory.createCharsetParser(), headers, expected);
    }

    /**
     * Tests that parsing the header retrieves the expected values
     * @param header the http Accept-* header
     * @param expected an array of the expected entries in order
     */
    private void doDefaultParserTest(String header, String[] expected) {
        doDefaultParserTest(new String[]{header,}, expected);
    }

    /**
     * Tests that parsing the headers retrieves the expected charsets
     * @param headers the http Accept-* header
     * @param expected an array of the expected entires in order
     */
    private void doDefaultParserTest(String[] headers, String[] expected) {
        doTest(AcceptParserFactory.createDefault(), headers, expected);
    }

    /**
     * Uses the parser instance to parse the headers and assert that the
     * resulting entries match those expected
     * @param parser the parser to test
     * @param headers the headers to parse with the parser
     * @param expected the expected entries for the headers provided
     */
    private void doTest(AcceptParser parser, String[] headers, String[] expected) {
        for (int i = 0; i < headers.length; i++) {
            parser.addHeaderField(headers[i]);
        }
        AcceptParser.Header header = parser.build();
        ArrayCheckIterator checkIterator = new ArrayCheckIterator(parser);
        checkIterator.add(expected);
        header.forEachAcceptable(checkIterator);
    }


    /**
     * Test that a default AcceptParser works with Accept-Language headers
     * and specificially _does not_ add the ISO-8859-1 value
     */
    public void testSingleToken() {
        doDefaultParserTest("en", new String[]{"en"});
    }

    /**
     * If items have equal q= values then us the order in which they
     * appear in the header. It isn't mentioned in the rfc but it
     * seems like it would be expected.
     */
    public void testEntriesWithEqualQvaluesRetainOrder() {
        doDefaultParserTest("A,B,C",
                new String[]{"A", "B", "C",});
    }

    /**
     * Tests that there can be no headers added
     */
    public void testNoHeaders() {
        doDefaultParserTest(new String[]{}, new String[]{});
    }

    /**
     * Test single charset name only. 
     * ISO-8859-1 will be added with qvalue 1 so it will be returned last.
     */
    public void testSingleCharsetOnly() {
        doCharsetParserTest("A", new String[]{"A", "ISO-8859-1"});
    }

    /**
     * Test single charset name with qvalue 0.5. 
     * ISO-8859-1 will be added with qvalue 1 so it will be returned first.
     */
    public void testSingleCharsetQvalue() {
        doCharsetParserTest("UTF-8;q=0.5", new String[]{"ISO-8859-1", "UTF-8"});
    }

    /**
     * Test multiple charset values with qvalues < 1.
     * ISO-8859-1 will be added with qvalue 1 so it will be returned first.
     */
    public void testMultipleCharsetQvalue() {
        doCharsetParserTest("A;q=0.5,B;q=0.600,C;q=0.2,D;q=0.5",
                new String[]{"ISO-8859-1", "B", "A", "D", "C"});
    }


    /**
     * Test the special value "*".
     * ISO-8859-1 will not be added when "*" is present.
     */
    public void testNotAcceptableValue() {
        AcceptParser parser = AcceptParserFactory.createCharsetParser();
        parser.addHeaderField("UTF-16;q=0");
        AcceptParser.Header header = parser.build();
        ArrayCheckIterator checkIterator = new ArrayCheckIterator(parser);
        checkIterator.add(new String[]{"ISO-8859-1"});
        header.forEachAcceptable(checkIterator);
        assertTrue(parser.containsNotAcceptable("UTF-16"));
    }

    /**
     * Test various charsets with bogus qvalues, which all default to qvalue 1. 
     * ISO-8859-1 will be added with qvalue 1 so it will be returned last.
     */
    public void testBogusQvalue() {
        doCharsetParserTest(
                "A;q=10," +     // too large
                        "B;q=-1," +     // too small
                        "C;q=n," +      // not an int
                        "D;q=," +       // no value
                        "E;q," +        // no equals
                        "F;"            // nothing after ;
                , new String[]{"A", "B", "C", "D", "E", "F", "ISO-8859-1"});
    }

    /**
     * Test various charsets and qvalues < 1 with spaces in between.
     * ISO-8859-1 will be added with qvalue 1 so it will be returned first.
     */
    public void testLotsaSpaces() {
        doCharsetParserTest(" A ; q = 0.3 , B ; q = 0.2 , C ; q = 0.1 ",
                new String[]{"ISO-8859-1", "A", "B", "C"});
    }

    /**
     * Test the special value "*".
     * ISO-8859-1 will not be added when "*" is present.
     */
    public void testStarNo8859_1() {
        doCharsetParserTest("*", new String[]{"*"});
    }

    /**
     * Test ISO-8859-1 with qvalue > 0.
     * ISO-8859-1 will <b>not</b> be added when it is already present as an 
     * explicit acceptable value.
     */
    public void testExplictAcceptable8859_1() {
        doCharsetParserTest("A;q=0.3, ISO-8859-1;q=0.2, B;q=0.1",
                new String[]{"A", "ISO-8859-1", "B"});
    }

    /**
     * Test ISO-8859-1 with qvalue 0 (not acceptable). 
     * ISO-8859-1 will <b>not</b> be added when it is already present as an 
     * explicit unacceptable value.
     */
    public void testExplicitNotAcceptable8859_1() {
        AcceptParser parser = AcceptParserFactory.createCharsetParser();
        parser.addHeaderField("ISO-8859-1;q=0.0");
        AcceptParser.Header header = parser.build();
        ArrayCheckIterator checkIterator = new ArrayCheckIterator(parser);
        checkIterator.add(new String[]{});
        header.forEachAcceptable(checkIterator);
        assertTrue(parser.containsNotAcceptable("ISO-8859-1"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	8675/8	trynne	VBM:2005052602 added javadoc and moved inner classes and interfaces to the bottom of the class

 06-Jun-05	8675/6	trynne	VBM:2005052602 Generalised AcceptCharsetParser to AcceptParser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jul-03	858/2	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 24-Jul-03	807/6	geoff	VBM:2003071405 use fallbacks more often and allow user to set it themselves if we can't

 23-Jul-03	807/4	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
