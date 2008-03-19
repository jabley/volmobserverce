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
package com.volantis.mcs.repository.jdbc;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.repository.RepositoryException;

/**
 * Test case for SQLState.
 *
 * <p><strong>NOTE</strong>: because the SQLState class behaves like a typesafe
 * enumeration, using a static map of instances keyed on unique SQLSTATE
 * classifications, the test case must remember to tidy up any (successfully)
 * added instances. This is done using the package protected
 * {@link SQLState#kill} method.
 */
public class SQLStateTestCase extends TestCaseAbstract {
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the name of the test case
     */
    public SQLStateTestCase(String name) {
        super(name);
    }

    /**
     * Tests failure conditions.
     */
    public void testConstructorBadClassification() throws Exception {
        try {
            new SQLState("BADLENGTH", 0) {};

            fail("Should have had an IllegalArgumentException for bad " +
                 "length classification");
        } catch (IllegalArgumentException e) {
            // Expected condition
        }

        try {
            new SQLState("BAD", 0) {};

            fail("Should have had an IllegalArgumentException for bad " +
                 "length classification");
        } catch (IllegalArgumentException e) {
            // Expected condition
        }

        try {
            new SQLState("C BAD", 0) {};

            fail("Should have had an IllegalArgumentException for bad " +
                 "characters in the classification");
        } catch (IllegalArgumentException e) {
            // Expected condition
        }
    }

    /**
     * Tests failure conditions.
     */
    public void testConstructorDuplicateClassification() throws Exception {
        SQLState abcde = new SQLState("ABCDE", 0);

        try {
            new SQLState("ABCDE", 2);

            fail("Should have had an IllegalArgumentException for a " +
                 "duplicate classification");
        } catch (IllegalArgumentException e) {
            // Expected condition
        }

        abcde.kill();
    }

    /**
     * Tests success conditions.
     */
    public void testConstructor() throws Exception {
        SQLState ab = new SQLState("AB", 10);
        SQLState abcde = new SQLState("ABCDE", 25);

        assertSame("Should have found instance ab",
                   ab,
                   SQLState.instance("AB"));

        assertSame("Should have found instance abcde",
                   abcde,
                   SQLState.instance("ABCDE"));

        ab.kill();
        abcde.kill();
    }

    /**
     * Test success conditions.
     */
    public void testInstance() throws Exception {
        SQLState ab = new SQLState("AB", 20);

        assertSame("Should have found instance ab",
                   ab,
                   SQLState.instance("AB"));

        assertSame("Should have found instance ab from full SQLSTATE code",
                   ab,
                   SQLState.instance("ABCDE"));

        assertNull("Should have had no instance returned",
                   SQLState.instance("NOTFD"));

        assertSame("Should have found pre-defined instance",
                   SQLState.INTEGRITY_CONSTRAINT_VIOLATION,
                   SQLState.instance(SQLState.INTEGRITY_CONSTRAINT_VIOLATION.
                                     getClassification()));

        String code = SQLState.INTEGRITY_CONSTRAINT_VIOLATION.
            getClassification();

        if (code.length() == SQLState.CLASS_LENGTH) {
            assertSame("Should have found pre-defined instance from full " +
                       "SQLSTATE code",
                       SQLState.INTEGRITY_CONSTRAINT_VIOLATION,
                       SQLState.instance(code + "123"));
        }

        ab.kill();
    }

    /**
     * Test success conditions.
     */
    public void testCode() throws Exception {
        SQLState ab = new SQLState("AB", 45);

        assertEquals("The code for the SQLState was not as",
                     45,
                     SQLState.code("AB"));

        assertEquals("The code for an unknown SQLState was not as",
                     RepositoryException.ERROR,
                     SQLState.code("UNKNO"));

        ab.kill();
    }

    /**
     * Test failure condition.
     */
    public void testMatchesBadCode() throws Exception {
        try {
            SQLState.INTEGRITY_CONSTRAINT_VIOLATION.matches("");

            fail("Should have had an IllegalArgumentException for bad " +
                 "input SQLSTATE code");
        } catch (Exception e) {
            // Expected condition
        }
    }

    /**
     * Test success conditions.
     */
    public void testMatches() throws Exception {
        SQLState ab = new SQLState("AB", 252);
        SQLState abcde = new SQLState("ABCDE", 255);

        if (SQLState.INTEGRITY_CONSTRAINT_VIOLATION.getClassification().
            length() == SQLState.CLASS_LENGTH) {
            assertTrue("Should have matched ICV",
                       SQLState.INTEGRITY_CONSTRAINT_VIOLATION.matches(
                           SQLState.INTEGRITY_CONSTRAINT_VIOLATION.
                           getClassification() + "ABC"));
        }

        if ((SQLState.INVALID_USER_NAME_OR_PASSWORD.getClassification().
             length() == SQLState.CLASS_LENGTH) &&
            (!SQLState.INVALID_USER_NAME_OR_PASSWORD.getClassification().
              equals(SQLState.INTEGRITY_CONSTRAINT_VIOLATION.
                     getClassification()))) {
            assertFalse("Should not have matched ICV",
                        SQLState.INTEGRITY_CONSTRAINT_VIOLATION.matches(
                            SQLState.INVALID_USER_NAME_OR_PASSWORD.
                            getClassification() + "XYZ"));
        }

        assertTrue("Should have matched AB",
                   ab.matches("AB000"));

        assertTrue("Should have matched ABCDE",
                   abcde.matches("ABCDE"));

        assertTrue("Should have matched AB",
                   ab.matches("ABDEF"));

        assertFalse("Should not have matched ABCDE",
                    abcde.matches("ABDEF"));

        ab.kill();
        abcde.kill();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Oct-03	1522/1	philws	VBM:2003090101 Ensure that SQLSTATE codes are matched against class codes only

 ===========================================================================
*/
