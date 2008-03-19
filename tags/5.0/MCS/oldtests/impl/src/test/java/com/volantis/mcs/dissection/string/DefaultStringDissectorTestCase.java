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
package com.volantis.mcs.dissection.string;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;

/**
 * A test case for {@link DefaultStringDissector}.
 * <p>
 * Currently only tests against {@link TestDissectableString} instances where
 * all the character costs are one. Maybe some that tested characters of
 * varying costs would be nice?
 * 
 * @todo add tests for whitespace handling once we figure out how it is 
 * supposed to work.
 */ 
public class DefaultStringDissectorTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private static final String BLOCK   = "01234567890123456789";

    private static final String AT_START  = " 1234567890123456789";
    
    private static final String AT_END   = "0123456789012345678 ";
    
    private static final int ELIPSES = ("... " + " ...").length(); 
    
    private static class MySegment extends StringSegment {
        MySegment(int start, int end) {
            setStart(start);
            setEnd(end);
        }
    }
    
    private static class Test {

        private String contents;
        private StringSegment segment;

        public Test(String segment) {
            this.contents = segment;
            this.segment = new MySegment(0, segment.length());
        }

        public Test(String prefix, String segment) {
            this.contents = prefix + segment;
            this.segment = new MySegment(prefix.length(), contents.length());
        }

        void test(int availableSpace, int expectedRC, String expectedResult) 
                throws DissectionException {
            test(availableSpace, expectedRC, expectedResult, false);
        }
        
        void test(int availableSpace, int expectedRC, String expectedResult, 
                boolean mustDissect) throws DissectionException {
            // Note that TDOM dissectable string also works here as well.
            DissectableString string = new TestDissectableString(contents);
            StringDissector dissector = new DefaultStringDissector();
            
            int rc = dissector.dissect(string, null, segment, availableSpace, 
                    mustDissect);
            assertEquals(expectedRC, rc);
            if (expectedResult != null) {
                assertEquals(expectedResult, contents.substring(
                        segment.getStart(), segment.getEnd()));
            }
        }
    }

    public DefaultStringDissectorTestCase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        BasicConfigurator.configure();
    }

    protected void tearDown() throws Exception {
        Category.shutdown();
        super.tearDown();
    }

    public void testShortFails() throws DissectionException {
        Test test = new Test(BLOCK);
        test.test(10, StringDissector.FAILED_TO_DISSECT, null, false);
    }
    
    public void testShortForce() throws DissectionException {
        Test test = new Test(BLOCK);
        test.test(10, StringDissector.DISSECTED_HAS_NEXT, null, true);
    }
    
    public void testComplete() throws DissectionException {
        Test test = new Test(BLOCK);
        test.test(20, StringDissector.DISSECTED_END, BLOCK);
    }
    
    public void testFirstSegment() throws DissectionException {
        Test test = new Test(BLOCK + BLOCK + BLOCK);
        test.test(20 + ELIPSES / 2, StringDissector.DISSECTED_HAS_NEXT, BLOCK);
    }

    public void testFirstSegmentWhitespaceAtEnd() throws DissectionException {
        Test test = new Test(AT_END + BLOCK + BLOCK);
        test.test(20 + ELIPSES / 2, StringDissector.DISSECTED_HAS_NEXT, AT_END.trim());
    }

    public void testNextSegment() throws DissectionException {
        Test test = new Test(BLOCK, BLOCK + BLOCK);
        test.test(20 + ELIPSES, StringDissector.DISSECTED_HAS_NEXT, BLOCK);
    }
    
    public void testNextSegmentWhitespaceAtStart() throws DissectionException {
        Test test = new Test(BLOCK, AT_START + BLOCK);
        test.test(20 + ELIPSES, StringDissector.DISSECTED_HAS_NEXT, AT_START.trim() +"0" );
    }

    // Hmm. Shouldn't the last segment only include space for a single elipsis?
    public void testLastSegment() throws DissectionException {
        Test test = new Test(BLOCK + BLOCK, BLOCK);
        test.test(20 + ELIPSES, StringDissector.DISSECTED_END, BLOCK);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jul-03	774/2	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/2	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/3	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
