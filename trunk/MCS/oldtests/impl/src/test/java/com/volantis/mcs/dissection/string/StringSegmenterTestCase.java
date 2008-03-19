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

/**
 * A test case for {@link StringSegmenter}.
 * <p>
 * This is currently very minimal, and needs a lot more work.
 */ 
public class StringSegmenterTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public StringSegmenterTestCase(String name) {
        super(name);
    }
    
    /**
     * A simple test which uses the bodgy string dissector to cut the 
     * string into 3 identical parts. It mainly just tests that the
     * segments returned are contiguous and contain all the data.
     *   
     * @throws DissectionException
     */ 
    public void testSegmenter() throws DissectionException {
        // Create the segments
        String[] segments = new String[] {// 72
            " A nice long string whic", // 24 
            "h we wish to dissect int", // 24
            "o three separate parts. "  // 24
        };
        // Create the string to dissect from the segments.
        StringBuffer contents = new StringBuffer();
        for (int i = 0; i < segments.length; i++) {
            contents.append(segments[i]);
        }
        // Note that TDOM dissectable string also works here as well.
        DissectableString string = new TestDissectableString(
                contents.toString());
        
        // Loop over the content, splitting it into identical thirds. 
        StringDissector dissector = new BodgyStringDissector(3,1);
        StringSegmenter segmenter = new StringSegmenter(dissector, string);
        for (int i = 0; i < segments.length; i++) {
            // Ensure each segment matches what we expect.
            segmenter.segment(null, i, (segments[i].length() + 8), false);
            StringSegment segment = segmenter.getSegment(i);
            String result = contents.substring(
                    segment.getStart(), segment.getEnd());
            assertEquals(segments[i], result);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	521/2	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
