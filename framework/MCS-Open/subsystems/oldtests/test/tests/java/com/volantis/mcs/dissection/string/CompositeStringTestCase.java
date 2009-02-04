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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;

/**
 * A test case for {@link CompositeDissectableString}.
 * <p>
 * Currently this is looking rather underfed and needs to put a lot more meat
 * on it's bones.
 */ 
public class CompositeStringTestCase extends DissectableStringTestAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public CompositeStringTestCase(String name) {
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

    protected DissectableString createDissectableString(String string)
            throws Exception {
        // Create a concrete CompositeDissectableString for testing.
        TestCompositeString parent = new TestCompositeString();
        // For now we put each char into a new child string.
        // We could invent a markup for the string to identify multi-character
        // children ...
        for (int i =0; i < string.length(); i++) {
            DissectableString child = new TestDissectableString(
                    new String(new char[] {string.charAt(i)}), new int[]{1});
            parent.add(child);
        }
        parent.initialise();
        return parent;
    }

    /**
     * Test that a composite string of simple individual characters implements 
     * range costing as expected.
     * 
     * @throws Exception
     */ 
    public void testGetRangeCostSimple() throws Exception {
        checkGetRangeCost("abc", new int[][] {
            {0, 1, 2, 3},
            {0, 1, 2},
            {0, 1},
            {0}
        });
    }
    
    /**
     * Test that a composite string of simple individual characters implements 
     * character indexing as expected.
     * 
     * @throws Exception
     */ 
    public void testGetCharacterIndexSimple() throws Exception {
        String abc = "abc";
        checkGetCharacterIndex(abc, new int[][] { 
            {0, 1, 2, 3},
            {1, 2, 3},
            {2, 3},
            {3}
        });
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

 10-Jul-03	770/3	geoff	VBM:2003070703 merge from metis, and rename files manually, and fix up sizes for whitespace differences

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	521/2	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
