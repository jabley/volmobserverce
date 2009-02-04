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

import com.volantis.styling.impl.engine.matchers.constraints.MatchesLanguage;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraint;

/**
 * Test contains {@link com.volantis.styling.impl.engine.matchers.constraints.MatchesLanguage}.
 */
public class MatchesLanguageTestCase
        extends ValueComparatorTestCaseAbstract {

    // Javadoc inherited.
    protected ValueConstraint createConstraint(String value) {
        return new MatchesLanguage(value);
    }

    /**
     * Test that it matches the prefix of a language code.
     */
    public void testMatchesLanguagePrefix() {
        ValueConstraint constraint = createConstraint("abc");
        assertTrue("Language string did not match",
                constraint.satisfied("abc"));
        assertTrue("Language string did not match",
                   constraint.satisfied("abc-"));
        assertTrue("Language string did not match",
                constraint.satisfied("abc-def"));
        assertFalse("Language string should not match",
                constraint.satisfied("abcdef"));
        assertFalse("Language string should not match",
                constraint.satisfied("ab"));
        assertFalse("Language string should not match",
                constraint.satisfied(""));
    }

    /**
     * Test that it matches the prefix of a long language code.
     */
    public void testMatchesLongLanguagePrefix() {
        ValueConstraint constraint = createConstraint("abc-xyz");
        assertTrue("Language string did not match",
                constraint.satisfied("abc-xyz"));
        assertTrue("Language string did not match",
                constraint.satisfied("abc-xyz-"));
        assertTrue("Language string did not match",
                   constraint.satisfied("abc-xyz-qrs"));
        assertFalse("Language string should not match",
                constraint.satisfied("abc-xyzqrs"));
        assertFalse("Language string should not match",
                constraint.satisfied("abc-x"));
        assertFalse("Language string should not match",
                constraint.satisfied("abc-"));
        assertFalse("Language string should not match",
                constraint.satisfied("ab"));
        assertFalse("Language string should not match",
                constraint.satisfied(""));
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
