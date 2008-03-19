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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.unit.engine.matchers.constraints;

import com.volantis.styling.impl.engine.matchers.constraints.ListContains;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraint;

/**
 * Test contains {@link com.volantis.styling.impl.engine.matchers.constraints.ListContains}.
 */
public class ListContainsTestCase
        extends ValueComparatorTestCaseAbstract {

    // Javadoc inherited.
    protected ValueConstraint createConstraint(String value) {
        return new ListContains(value);
    }

    /**
     * Test that a value containing white space fails.
     */
    public void testWhitespaceValueFails() {
        try {
            new ListContains("a b");
            fail("Whitespace value not detected");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message incorrect",
                         "value 'a b' contains whitespace",
                         e.getMessage());
        }
    }

    /**
     * Test that a value that is the only word in the list succeeds.
     */
    public void testOnlyWord() {
        ValueConstraint constraint = new ListContains("alpha");
        assertTrue("Only word did not match",
                   constraint.satisfied("alpha"));
    }

    /**
     * Test that a value that is the first word in the list succeeds.
     */
    public void testFirstWord() {
        ValueConstraint constraint = new ListContains("alpha");
        assertTrue("First word did not match",
                   constraint.satisfied("alpha beta gamma"));
    }

    /**
     * Test that a value that is the middle word in the list succeeds.
     */
    public void testMiddleWord() {
        ValueConstraint constraint = new ListContains("beta");
        assertTrue("Middle word did not match",
                   constraint.satisfied("alpha beta gamma"));
    }

    /**
     * Test that a value that is the last word in the list succeeds.
     */
    public void testLastWord() {
        ValueConstraint constraint = new ListContains("gamma");
        assertTrue("Last word did not match",
                   constraint.satisfied("alpha beta gamma"));
    }

    /**
     * Test that a value that is a suffix of an item works.
     */
    public void testWordSuffix() {
        ValueConstraint constraint = new ListContains("eta");
        assertFalse("Suffix matched",
                    constraint.satisfied("alpha beta gamma"));
    }

    /**
     * Test that a value that is a prefix of an item works.
     */
    public void testWordPrefix() {
        ValueConstraint constraint = new ListContains("bet");
        assertFalse("Prefix matched",
                    constraint.satisfied("alpha beta gamma"));
    }

    /**
     * Test that a value that is not in the list works.
     */
    public void testMismatchWord() {
        ValueConstraint constraint = new ListContains("epsilon");
        assertFalse("Mismatch word matched",
                    constraint.satisfied("alpha beta gamma"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
