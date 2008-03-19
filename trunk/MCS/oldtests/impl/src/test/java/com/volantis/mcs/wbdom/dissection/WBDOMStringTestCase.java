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
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.string.DissectableStringTestAbstract;
import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.WBSAXString;

/**
 * A test case which exercises {@link DissectableWBSAXString}.
 * <p>
 * This test case uses it's {@link DissectableString} interface and 
 * {@link DissectableStringTestAbstract}. 
 * <p>
 * Currently this test case is fairly "light". For example, no failure cases
 * are tested. It could definitely use more work.
 */ 
public class WBDOMStringTestCase extends DissectableStringTestAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private CharsetCode charset;
    private Codec codec;
    private StringFactory strings;
    
    public WBDOMStringTestCase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        
        charset = new CharsetCode(0x6A, "UTF-8");
        codec = new Codec(charset);
        strings = new StringFactory(codec);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetCharacterIndexNormal() throws Exception {
        String string = "x";
        checkGetCharacterIndex(string, 0, new int[] {
        //     STR_I, nul, x,    
        //  0,     1,   2, 3, 4, 5
            0,     0,   0, 1, 1, 1 // ...
        });
    }

    public void testGetRangeCostNormal() throws Exception {
        checkGetRangeCost("x", 0, new int[] {
        //     STR_I, nul, x,    
        //  0,             1
            0,             3
        });
        checkGetRangeCost("x", 1, new int[] {
        //  (nothing)    
        //  0, 
            0, 
        });
    }
    
    public void testGetCharacterIndexMultiByte() throws Exception {
        // x<euro>y, euro is three bytes (apparently) in UTF8
        String string = "x" + '\u20AC' + "y";
        checkGetCharacterIndex(string, 0, new int[] {
        //     STR_I, nul, x,  <euro>, y   
        //  0,     1,   2, 3, 4, 5, 6, 7, 8, 9
            0,     0,   0, 1, 1, 1, 2, 3, 3, 3 // ...
        });
        checkGetCharacterIndex(string, 1, new int[] {
        //     STR_I, nul,  <euro>, y   
        //  0,     1,   2, 3, 4, 5, 6, 7, 8
            1,     1,   1, 1, 1, 2, 3, 3, 3, // ...
        });
        checkGetCharacterIndex(string, 2, new int[] {
        //     STR_I, nul, y   
        //  0,     1,   2, 3, 4, 5
            2,     2,   2, 3, 3, 3 // ...
        });
    }
    
    public void testGetRangeCostMultiByte() throws Exception {
        // x<euro>y, euro is three bytes (apparently) in UTF8
        checkGetRangeCost("x" + '\u20AC' + "y", 0, new int[] {
        //     STR_I, nul, x,  <euro>, y   
        //  0,             1,       2, 3
            0,             3,       6, 7
        });
        checkGetRangeCost("x" + '\u20AC' + "y", 1, new int[] {
        //     STR_I, nul, <euro>, y   
        //  0,                  1, 2
            0,                  5, 6
        });
        checkGetRangeCost("x" + '\u20AC' + "y", 2, new int[] {
        //     STR_I, nul, y   
        //  0,             1,
            0,             3
        });
    }

    protected DissectableString createDissectableString(String string) 
            throws Exception {
        WBSAXString wbsaxString = strings.create(string);
        DissectableString dstring = new DissectableWBSAXString(wbsaxString);
        return dstring;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/2	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	733/1	geoff	VBM:2003070403 port from mimas and fix renames manually

 04-Jul-03	724/1	geoff	VBM:2003070403 first take at cleanup

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
