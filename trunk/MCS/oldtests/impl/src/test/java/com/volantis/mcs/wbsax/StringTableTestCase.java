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

package com.volantis.mcs.wbsax;

import junit.framework.TestCase;

/**
 * A test case for {@link StringTable}. 
 * <p>
 * At the moment we only test that strings are added once and once only.
 */
public class StringTableTestCase extends TestCase {

    /**
     * The Codec used.
     */
    private Codec codec;

    /**
     * Default junit constructor
     */
    public StringTableTestCase(String name) {
        super(name);

        codec = new Codec(new CharsetCode(4, "iso-8859-1"));
    }

    /**
     * Tests that strings are added to the string table once and once only.
     */
    public void testCreateReferenceEquals() throws Exception {
        StringTable stringTable = new StringTable();             
        StringReference stringRef = 
            stringTable.createReference(new WBSAXString(codec, "hello"));
            
        StringReference stringRef2 = 
            stringTable.createReference(new WBSAXString(codec, "hello"));

        StringReference stringRef3 = 
            stringTable.createReference(new WBSAXString(codec, "goodbye"));

        StringReference stringRef4 = 
            stringTable.createReference(new WBSAXString(codec, "hello"));
            
        assertEquals("WBSAXStrings should be the same", 
           stringRef.resolveString(), stringRef2.resolveString());
        assertEquals("Logical index should be the same", 
           stringRef.resolveLogicalIndex(), stringRef2.resolveLogicalIndex());               
        assertEquals("Physical index should be the same", 
           stringRef.resolvePhysicalIndex(), stringRef2.resolvePhysicalIndex());                              

        assertTrue("WBSAXStrings shouldn't be the same", 
           !stringRef.resolveString().equals(stringRef3.resolveString()));
        assertTrue("Logical index shouldn't be the same", 
           stringRef.resolveLogicalIndex() != stringRef3.resolveLogicalIndex());               
        assertTrue("Physical index shouldn't be the same", 
           !stringRef.resolvePhysicalIndex().equals(stringRef3.resolvePhysicalIndex()));                              

        assertEquals("WBSAXStrings should be the same", 
           stringRef.resolveString(), stringRef4.resolveString());
        assertEquals("Logical index should be the same", 
           stringRef.resolveLogicalIndex(), stringRef4.resolveLogicalIndex());               
        assertEquals("Physical index should be the same", 
           stringRef.resolvePhysicalIndex(), stringRef4.resolvePhysicalIndex());                              
    }

    /**
     * Test getReference returns references to string that exist in the
     * StringTable and no others.
     */
    public void testGetReference() throws Exception {
        StringTable stringTable = new StringTable();
        
        assertNull("StringReference should be null for empty StringTable",
                   stringTable.getReference(new WBSAXString(codec, "hello")));
                     
        StringReference stringRef = 
            stringTable.createReference(new WBSAXString(codec, "hello"));
            
        StringReference stringRef2 = 
            stringTable.getReference(new WBSAXString(codec, "hello"));

        assertEquals("WBSAXStrings should be the same", 
           stringRef.resolveString(), stringRef2.resolveString());
        assertEquals("Logical index should be the same", 
           stringRef.resolveLogicalIndex(), stringRef2.resolveLogicalIndex());               
        assertEquals("Physical index should be the same", 
           stringRef.resolvePhysicalIndex(), stringRef2.resolvePhysicalIndex());                              

        assertNull("StringReference should be null when string not in StringTable",
                   stringTable.getReference(new WBSAXString(codec, "goodbye")));        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 03-Jul-03	709/1	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/1	geoff	VBM:2003070209 clean up WBSAX test cases

 16-Jun-03	372/2	geoff	VBM:2003060609 avoid merge hell by committing on geoffs machine

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 ===========================================================================
*/
