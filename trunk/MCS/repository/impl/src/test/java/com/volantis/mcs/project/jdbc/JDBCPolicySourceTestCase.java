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
/** (c) Volantis Systems Ltd 2003.  */
package com.volantis.mcs.project.jdbc;

import com.volantis.mcs.project.impl.jdbc.JDBCPolicySourceImpl;
import junit.framework.TestCase;

/**
 * TestCase for JDBCPolicySource
 * 
 */
public class JDBCPolicySourceTestCase extends TestCase {

    JDBCPolicySource s1;
    JDBCPolicySource s2;
    /**
     * Constructor for JDBCPolicySourceTestCase.
     * @param name
     */
    public JDBCPolicySourceTestCase(String name) {
        super(name);
    }

    public void setUp() {
        s1 = new JDBCPolicySourceImpl(null, "test1");
        s2 = new JDBCPolicySourceImpl(null, "test2");
    }
    
    public void testHashCode() {
        assertEquals("Hashcodes not equal", 
            s1.hashCode(), new JDBCPolicySourceImpl(null, "test1").hashCode());
        assertFalse("Hashcodes shouldn't be equal",
            s1.hashCode() == s2.hashCode());
    }

    /*
     * Test for boolean equals(Object)
     */
    public void testEquals() {
        assertFalse("Classes should not be equal", s1.equals(s2));
        assertFalse("Classes should not be equal", s2.equals(s1));
        assertTrue("Classes should be equal",
             s1.equals(new JDBCPolicySourceImpl(null, "test1")));
        assertTrue("Classes should be equal",
            new JDBCPolicySourceImpl(null, "test1").equals(s1));
        
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Jan-04	2769/2	mat	VBM:2004012702 Added testcases

 ===========================================================================
*/
