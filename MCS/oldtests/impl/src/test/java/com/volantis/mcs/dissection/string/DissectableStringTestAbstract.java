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

import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;

/**
 * Abstract test case for instances of {@link DissectableString}.
 * <p>
 * This test case does not define any tests itself since each test will
 * be specific for each type of dissectable string under test. It does, 
 * however provide methods which may be called which do most of the work of
 * a test.
 */ 
public abstract class DissectableStringTestAbstract extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public DissectableStringTestAbstract(String name) {
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

    protected void checkGetRangeCost(String string, int[][] costs) 
            throws Exception {
        for (int i = 0; i <= string.length(); i++) {
            checkGetRangeCost(string, i, costs[i]);
        }
    }
    
    protected void checkGetCharacterIndex(String string, int[][] indexes) 
            throws Exception {
        for (int i = 0; i <= string.length(); i++) {
            checkGetCharacterIndex(string, i, indexes[i]);
        }
    }
    
    protected void checkGetRangeCost(String string, int start, 
            int[] expectedCosts) throws Exception {
        DissectableString dstring = checkCharacters(string);

        // check the cost for each character index
        int [] actualCosts = new int[expectedCosts.length];
        for (int i = 0; i < expectedCosts.length; i++) {
            actualCosts[i] = dstring.getRangeCost(start, start + i);
        }
        assertEquals(expectedCosts, actualCosts);
        // ensure the total matches if this is the entire string.
        if (start == 0) {
            assertEquals(expectedCosts[expectedCosts.length-1], 
                    dstring.getCost());
        }
    }

    protected void checkGetCharacterIndex(String string, int start, 
            int[] expectedIndexes) throws Exception {
        DissectableString dstring = checkCharacters(string);

        // check the character indexes for each cost
        int [] actualIndexes = new int[expectedIndexes.length];
        for (int i = 0; i < expectedIndexes.length; i++) {
            actualIndexes[i] = dstring.getCharacterIndex(start, i);
        }
        assertEquals(expectedIndexes, actualIndexes);
    }

    private DissectableString checkCharacters(String string) 
            throws Exception {
        DissectableString dstring = createDissectableString(string);
        char[] chars = string.toCharArray();
        
        assertEquals(string.length(), dstring.getLength());
        
        // Check the chars
        int[] expectedChars = new int[chars.length];
        int[] actualChars = new int[chars.length];
        for (int i=0; i < string.length(); i++) {
            expectedChars[i] = chars[i];
            actualChars[i] = dstring.charAt(i);
        }
        assertEquals(expectedChars, actualChars);
        return dstring;
    }

    protected abstract DissectableString createDissectableString(String string) 
            throws Exception;

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

 10-Jul-03	751/4	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	722/7	geoff	VBM:2003070403 merge from mimas; fix rename failures manually

 04-Jul-03	724/1	geoff	VBM:2003070403 first take at cleanup

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
