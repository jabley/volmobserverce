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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests the string matcher
 */
public class StringMatcherTestCase extends TestCaseAbstract {
    /**
     * Test a series of simple exact character matches...
     */
    public void testSimpleMatches() {
        StringMatcher matcher = new StringMatcher("Monkey");
        assertTrue("Exact match should work.", matcher.matches("Monkey"));
        assertTrue("Extended string should match.", matcher.matches("Monkeys"));
        assertFalse("Shortened string should not match.", matcher.matches("Monke"));
        assertFalse("Different string should not match.", matcher.matches("Gibbons"));
    }

    /**
     * Test matches including the single-character wildcard
     */
    public void testSingleWildcard() {
        StringMatcher matcher = new StringMatcher("F?nk?");
        assertTrue("Exact matches should work.", matcher.matches("Funky"));
        assertTrue("Exact matches should work.", matcher.matches("Finks"));
        assertFalse("Trailing ? should cause match failure.", matcher.matches("Fink"));
        assertTrue("Extended matches should work.", matcher.matches("Funkiest"));
        assertFalse("Multiple characters should not match ? wildcard.", matcher.matches("Franklin"));
    }

    /**
     * Test matches including the multiple-character wildcard
     */
    public void testMultipleWildcard() {
        StringMatcher matcher = new StringMatcher("C*s*");
        assertTrue("Varying number of characters should match", matcher.matches("Cosh"));
        assertTrue("Varying number of characters should match", matcher.matches("Caramelised crabs"));
        assertTrue("Zero characters should match", matcher.matches("Cs"));
        assertFalse("Characters after a wildcard should still need to match", matcher.matches("Conjugate"));
    }

    /**
     * Test matches including escaping
     */
    public void testEscaping() {
        StringMatcher matcher = new StringMatcher("Lunch\\?");
        assertTrue("Exact match should work.", matcher.matches("Lunch?"));
        assertFalse("Escaped wildcard should not operate as wildcard", matcher.matches("Lunch!"));

        matcher.setFilter("Lunch\\");
        assertTrue("Escape character at end of filter should be ignored", matcher.matches("Lunch"));

        matcher.setFilter("\\L\\u\\n\\c\\h");
        assertTrue("Escaping of non-wildcard characters should work", matcher.matches("Lunch"));

        matcher.setFilter("Lu\\\\nch");
        assertTrue("Escaping the escape character should work", matcher.matches("Lu\\nch"));
    }
}
