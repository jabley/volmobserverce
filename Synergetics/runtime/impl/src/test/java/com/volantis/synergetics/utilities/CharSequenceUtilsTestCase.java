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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.utilities;

import junit.framework.TestCase;

/**
 * This class is responsible for testing the behaviour of
 * {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTestCase extends TestCase {

    /**
     * Test regionMatches for various conditions.
     */
    public void testRegionMatches() {
        CharSequence cs1 = "abc";
        CharSequence cs2 = new StringBuffer("ABC");
        assertTrue(CharSequenceUtils.regionMatches(cs1, true, 0, cs2, 0, 3));
        assertTrue(CharSequenceUtils.regionMatches(cs2, true, 0, cs1, 0, 3));
        assertFalse(CharSequenceUtils.regionMatches(cs1, false, 0, cs2, 0, 3));
        assertFalse(CharSequenceUtils.regionMatches(cs1, true, 0, cs2, 1, 2));
        assertTrue(CharSequenceUtils.regionMatches(cs1, true, 1, cs2, 1, 2));
        assertFalse(CharSequenceUtils.regionMatches(cs1, true, 0, cs2, 1, 1));

        cs1 = "allan";
        cs2 = new StringBuffer("hello allan");

        assertTrue(CharSequenceUtils.regionMatches(cs1, false, 0, cs2, 6, 5));
        assertFalse(CharSequenceUtils.regionMatches(cs2, false, 0, cs1, 6, 5));

        assertTrue(CharSequenceUtils.regionMatches(cs1, false, 0, cs2, 6, 0));
        assertFalse(CharSequenceUtils.regionMatches(cs1, false, 0, cs2, 6, 6));
    }

    /**
     * Test equalsIgnoreCase for various conditions.
     */
    public void testEqualsIgnoreCase() {
        CharSequence cs1 = "abc";
        CharSequence cs2 = new StringBuffer("ABC");

        assertTrue(CharSequenceUtils.equalsIgnoreCase(cs1, cs2));
        assertFalse(CharSequenceUtils.equalsIgnoreCase(cs1, "ab"));
        assertFalse(CharSequenceUtils.equalsIgnoreCase(cs2, "bc"));
        assertFalse(CharSequenceUtils.equalsIgnoreCase(cs1, "abcd"));
    }
    
    public void testIndexOfIgnoreCaseAndPrecedingWhiteSpaceWhenStringToBeFoundExistsInStringBeingSearched() {

        String toSearch = "abc";
        String toFind = "abc";
        int indexOf = CharSequenceUtils.indexOfIgnoreCaseAndPrecedingWhiteSpace(
                toSearch, toFind, 0);

        int expectedIndex = 0;
        assertEquals("Indexes should match", expectedIndex, indexOf);
    }

    public void testIndexOfIgnoreCaseAndPrecedingWhiteSpaceWhenStringToSearchHasPreceedingWhitespace() {

        String toSearch = "      abc";
        String toFind = "abc";
        int indexOf = CharSequenceUtils.indexOfIgnoreCaseAndPrecedingWhiteSpace(
                toSearch, toFind, 0);

        int expectedIndex = 0;
        assertEquals("Indexes should match", expectedIndex, indexOf);
    }

    public void testIndexOfIgnoreCaseAndPrecedingWhiteSpaceWhenStringToSearchHasPreceedingVariousWhitespaces() {

        String toSearch = " \t \r \n  abc";
        String toFind = "abc";
        int indexOf = CharSequenceUtils.indexOfIgnoreCaseAndPrecedingWhiteSpace(
                toSearch, toFind, 0);

        int expectedIndex = 0;
        assertEquals("Indexes should match", expectedIndex, indexOf);
    }

    public void testIndexOfIgnoreCaseAndPrecedingWhiteSpaceWhenStringToSearchHasWhitespacesOnly() {

        String toSearch = " \t \r \n  ";
        String toFind = "abc";
        int indexOf = CharSequenceUtils.indexOfIgnoreCaseAndPrecedingWhiteSpace(
                toSearch, toFind, 0);

        int expectedIndex = -1;
        assertEquals("Indexes should match", expectedIndex, indexOf);
    }

    public void testIndexOfIgnoreCaseAndPrecedingWhiteSpaceWhenNoMatchExpected() {

        String toSearch = "      abc";
        String toFind = "xyz";
        int indexOf = CharSequenceUtils.indexOfIgnoreCaseAndPrecedingWhiteSpace(
                toSearch, toFind, 0);

        int expectedIndex = -1;
        assertEquals("Indexes should match", expectedIndex, indexOf);
    }

    public void testNoMatchNoPrecedingWhitespace() {

        String toSearch = "abc";
        String toFind = "cyx";
        int indexOf = CharSequenceUtils.indexOfIgnoreCaseAndPrecedingWhiteSpace(
                    toSearch,
                   toFind, 0);

        int expectedIndex = -1;
        assertEquals("Indexes should match", expectedIndex, indexOf);
    }


    public void testWithInputThatCausedInfinateLoop() {

        String toSearch = "/minComp.jsp?cid=MO&amp;a=1";
        String toFind = "javascript:";
        int indexOf = CharSequenceUtils.indexOfIgnoreCaseAndPrecedingWhiteSpace(
                    toSearch,
                    toFind, 0);

        int expectedIndex = -1;
        assertEquals("Indexes should match", expectedIndex, indexOf);
    }

    public void testIndexOfWhenStringToBeFoundIsInStringBeingSearched() {

        String toSearch = "abcefg";
        String toFind = "abc";

        int indexOf = CharSequenceUtils.indexOfIgnoreCase(toSearch, toFind, 0);

        int expectedIndex = 0;
        assertEquals("Indexed should match", expectedIndex, indexOf);
    }

    public void testIndexOfWhenStringToBeFoundIsNotInStringBeingSearched() {

        String toSearch = "abcefg";
        String toFind = "xyz";

        int indexOf = CharSequenceUtils.indexOfIgnoreCase(toSearch, toFind, 0);

        int expectedIndex = -1;
        assertEquals("Indexed should match", expectedIndex, indexOf);
    }
}
