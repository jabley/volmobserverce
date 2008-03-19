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

import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraint;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class ValueComparatorTestCaseAbstract
        extends TestCaseAbstract {

    /**
     * Create a constraint with the specified value.
     */
    protected abstract ValueConstraint createConstraint(String value);

    /**
     * Create a constraint with a null value that is expected to fail.
     */
    protected void createConstraintWithNullValue() {
        createConstraint(null);
    }

    protected ValueConstraint createSuccessfulValue(String value) {
        return createConstraint(value);
    }

    protected ValueConstraint createFailingValue(String value) {
        return createConstraint(value);
    }

    /**
     * Test that a null value fails.
     */
    public void testNullValueFails() {
        try {
            createConstraintWithNullValue();
            fail("Null value not detected");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message incorrect",
                         "value cannot be null",
                         e.getMessage());
        }
    }

    /**
     * Test a successful value.
     */
    public void testSuccess() {
        ValueConstraint constraint = createSuccessfulValue("alpha");
        assertTrue("Successful value did not match", constraint.satisfied("alpha"));
    }

    /**
     * Test a failing value.
     */
    public void testFailure() {
        ValueConstraint constraint = createFailingValue("beta");
        assertFalse("Failing value matched", constraint.satisfied("alpha"));
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
