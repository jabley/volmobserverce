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
 * A test case for {@link WBSAXString}. 
 * <p>
 * At the time of writing this class only tests the equals and hashcode 
 * methods. We also don't currently need a codec so we set it to null.
 */
public class WBSAXStringTestCase extends TestCase {

    /**
     * WBSAXStrings objects used in the tests.
     */
    private WBSAXString one;
    private WBSAXString two;
    private WBSAXString three;

    /**
     * Standard junit constructor
     * @param name
     */
    public WBSAXStringTestCase(String name) {
        super(name);        
    }

    /**
     * Test of the equals method for WBSAXString string that should or
     * should not be logically equal.
     */
    public void testEquals() {        
        one = new WBSAXString(null, "wbsaxstring");
        two = new WBSAXString(null, "wbsaxstring");       
        assertTrue("WBSAXStrings should be equal", one.equals(two));
                
        two = new WBSAXString(null, "fred");        
        assertTrue("WBSAXStrings shouldn't be equal", !one.equals(two));
        
        two = new WBSAXString(null, "wbsaxstring".toCharArray());        
        assertTrue("WBSAXStrings should be equal", one.equals(two));
        
        two = new WBSAXString(null, "fred".toCharArray());        
        assertTrue("WBSAXStrings shouldn't be equal", !one.equals(two));
    }

    /**
     * Test that the equals method is reflexive
     */
    public void testEqualsReflexive() {               
        one = new WBSAXString(null, "wbsaxstring");        
        assertTrue("WBSAXStrings should be equal", one.equals(one));
    }

    /**
     * Test that the equals method is symmetric
     */
    public void testEqualsSymmetric() {
        one = new WBSAXString(null, "wbsaxstring");
        two = new WBSAXString(null, "wbsaxstring");        
        assertTrue("WBSAXStrings should be equal", one.equals(two));
        assertTrue("WBSAXStrings should be equal", two.equals(one));

        one = new WBSAXString(null, "wbsaxstring");
        two = new WBSAXString(null, "wbsaxstring".toCharArray());        
        assertTrue("WBSAXStrings should be equal", one.equals(two));
        assertTrue("WBSAXStrings should be equal", two.equals(one));
    }

    /**
     * Test that the equals method is transitive
     */
    public void testEqualsTransitive() {
        one = new WBSAXString(null, "wbsaxstring");
        two = new WBSAXString(null, "wbsaxstring");
        three = new WBSAXString(null, "wbsaxstring".toCharArray());       
        assertTrue("WBSAXStrings should be equal", one.equals(two));
        assertTrue("WBSAXStrings should be equal", two.equals(three));
        assertTrue("WBSAXStrings should be equal", one.equals(three));
    }

    /**
     * Test that the equals method doesn't crash when it check for equality
     * with a null object
     */
    public void testEqualsNonNull() {
        one = new WBSAXString(null, "wbsaxstring");
        two = null;       
        assertTrue("WBSAXStrings shouldn't be equal", !one.equals(two));
    }

    /**
     * Test that the hashcode method returns equals hashcodes for logically
     * equivalent WBSAXStrings and different hashcodes for WBSAXStrings that
     * aren't equivalent.
     */
    public void testHashcode() {
        one = new WBSAXString(null, "wbsaxstring");
        two = new WBSAXString(null, "wbsaxstring");
        assertTrue("hashcodes should be equal", 
                   one.hashCode() == two.hashCode());
        
        two = new WBSAXString(null, "fred");        
        assertTrue("hashcodes shouldn't be equal",
                   one.hashCode() != two.hashCode());               
        
        two = new WBSAXString(null, "wbsaxstring".toCharArray());        
        assertTrue("hashcodes should be equal",
                   one.hashCode() == two.hashCode());
        
        two = new WBSAXString(null, "fred".toCharArray());        
        assertTrue("hashcodes shouldn't be equal",
                   one.hashCode() != two.hashCode());
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

 03-Jul-03	709/1	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/1	geoff	VBM:2003070209 clean up WBSAX test cases

 16-Jun-03	372/2	geoff	VBM:2003060609 avoid merge hell by committing on geoffs machine

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 ===========================================================================
*/
