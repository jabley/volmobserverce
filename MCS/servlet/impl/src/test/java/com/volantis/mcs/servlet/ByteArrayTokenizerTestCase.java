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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Tests for ByteArrayTokenizer class.
 */
public class ByteArrayTokenizerTestCase extends TestCaseAbstract {
    /**
     * Test a simple tokenization process with the simplest possible separator.
     */
    public void testSimpleTokenize() {
        byte[] array = { 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0 };
        byte[] separator = { 1 };
        byte[][] expectedResults = {
            { 0 },
            { 0, 0 },
            { 0, 0, 0 },
            { 0, 0 },
        };
        doTokenizationTest(array, separator, expectedResults);
    }

    /**
     * Test a multi-byte separator sequence, including partial matches.
     */
    public void testMultiByteSeparator() {
        byte[] array = { 0, 1, 0, 1, 1, 2, 2, 0, 0, 1, 2, 0, 0 };
        byte[] separator = { 1, 2 };
        byte[][] expectedResults = {
            { 0, 1, 0, 1 },
            { 2, 0, 0 },
            { 0, 0 },
        };
        doTokenizationTest(array, separator, expectedResults);
    }

    /**
     * Tests a tokenization where the separator starts within a partial match
     * for the separator.
     */
    public void testSeparatorStartsInPartialMatch() {
        byte[] array = { 0, 0, 0, 1, 2, 1, 2, 1, 3, 0, 0, 0 };
        byte[] separator = { 1, 2, 1, 3 };
        byte[][] expectedResults = {
            { 0, 0, 0, 1, 2 },
            { 0, 0, 0 },
        };
        doTokenizationTest(array, separator, expectedResults);
    }

    /**
     * Test for the case where the separator appears at the start of the
     * array.
     */
    public void testSeparatorAtStart() {
        byte[] array = { 1, 2, 0, 0, 0 };
        byte[] separator = { 1, 2 };
        byte[][] expectedResults = {
            { },
            { 0, 0, 0 },
        };
        doTokenizationTest(array, separator, expectedResults);
    }

    /**
     * Test for the case where the separator appears at the end of the
     * array.
     */
    public void testSeparatorAtEnd() {
        byte[] array = { 0, 0, 0, 1, 2 };
        byte[] separator = { 1, 2 };
        byte[][] expectedResults = {
            { 0, 0, 0 },
            { },
        };
        doTokenizationTest(array, separator, expectedResults);
    }

    /**
     * Test for the unlikely but possible case where the array and the
     * separator are equal.
     */
    public void testArrayIsSeparator() {
        byte[] array = { 1, 2 };
        byte[] separator = { 1, 2 };
        byte[][] expectedResults = {
            { },
            { },
        };
        doTokenizationTest(array, separator, expectedResults);
    }

    /**
     * Ensure that the enumeration approach to reading data gives the same
     * results as the getAllTokens method.
     */
    public void testEnumeration() {
        byte[] array = { 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0 };
        byte[] separator = { 1 };

        // Create two separate tokenizers so we can test the
        // hasMoreTokens/hasMoreElements and nextToken/nextElement synonyms
        // simultaneously.
        ByteArrayTokenizer batEnum = new ByteArrayTokenizer(array, separator);
        ByteArrayTokenizer batToke = new ByteArrayTokenizer(array, separator);

        byte[][] tokenized = batEnum.getAllTokens();
        for (int i = 0; i < tokenized.length; i++) {
            assertTrue("Tokenizer should have as many elements as " +
                    "tokenized data", batEnum.hasMoreElements());
            assertTrue("Tokenizer should have as many tokens as " +
                    "tokenized data", batToke.hasMoreTokens());
            assertTrue("Tokenizer next token should match tokenized data",
                    Arrays.equals(batToke.nextToken(), tokenized[i]));
            assertTrue("Tokenizer next element should match tokenized data",
                    Arrays.equals((byte[]) batEnum.nextElement(),
                            tokenized[i]));
        }
        assertFalse("Tokenizer should have no more elements than " +
                "tokenized data", batEnum.hasMoreElements());
        assertFalse("Tokenizer should have no more tokens than " +
                "tokenized data", batToke.hasMoreTokens());

        try {
            batEnum.nextElement();
            fail("Tokenizer should not return elements when it has none");
        } catch (NoSuchElementException nsee) {
            // Expected exception
        }

        try {
            batToke.nextToken();
            fail("Tokenizer should not return tokens when it has none");
        } catch (NoSuchElementException nsee) {
            // Expected exception
        }
    }

    /**
     * Carry out simple tests on the tokenizer: given an array and a separator,
     * ensure that the generated tokens match the expected results.
     *
     * @param array The byte array to tokenize
     * @param separator The separator to use
     * @param expectedResults The expected results
     */
    public void doTokenizationTest(byte[] array, byte[] separator,
                                   byte[][] expectedResults) {
        ByteArrayTokenizer tokenizer =
                new ByteArrayTokenizer(array, separator);

        byte[][] results = tokenizer.getAllTokens();

        assertEquals("Result array should be same size as expected",
                expectedResults.length, results.length);
        for (int i = 0; i < results.length; i++) {
            assertTrue("Entry " + i + " in results array should be equal to " +
                    "expected", Arrays.equals(expectedResults[i], results[i]));
        }
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Feb-05	6786/1	adrianj	VBM:2005012506 Rendered page cache rework

 ===========================================================================
*/
