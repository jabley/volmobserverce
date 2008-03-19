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
package com.volantis.mcs.dom.output.util;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

/**
 * This class is responsible for testing the behaviour of
 * {@link CircularCharacterBuffer}.
 */
public class CircularCharacterBufferTestCase extends TestCaseAbstract {

    /**
     * Test the behaviour of {@link CircularCharacterBuffer#add} when
     * adding a single character to an empty buffer.
     */
    public void testAddWithSingleCharacter() throws Throwable {

        int size = 10;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        // Add a character
        char charToBeAdded = 'a';
        buffer.add(charToBeAdded);

        // Check the size of the buffer
        int expectedNumberOfChars = 1;
        assertEquals("Should only contain one character",
                     expectedNumberOfChars,
                     buffer.getNumberOfCharacters());

        String expectedContents = "a";
        String actualContents = buffer.toString();

        // Test the position of tail.
        int expectedIndexOfTail = 1;
        assertTailIndex(buffer, expectedIndexOfTail);

        assertEquals("Buffer should contain 'a'.",
                     expectedContents,
                     actualContents);
    }

    /**
     * Test the behaviour of {@link CircularCharacterBuffer#add}
     * when multiple calls are made.
     */
    public void testAddByAddingSeveralCharsOneByOne() throws Exception {
        int size = 9;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'1','2','3','4','5','6','7','8'};

        for (int i = 0; i < (size - 1); i++) {

            buffer.add(charactersToBeAdded[i]);

            int expecetedNumberOfChars = i + 1;
            assertEquals("Should contain " + expecetedNumberOfChars + " items",
                         expecetedNumberOfChars,
                         buffer.getNumberOfCharacters());
        }

        // Ensure that the characters really are in the buffer
        String expectedContent = "12345678";
        String actualContent = buffer.toString();

        assertEquals("Buffer should contain: 12345678",
                     expectedContent,
                     actualContent);
    }

    /**
     * Add characters to the buffer until it is full.
     */
    public void testAddByAddingCharactersUtilBufferFull() throws Exception {
        int size = 9;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'1','2','3','4','5','6','7','8', '9'};

        for (int i = 0; i < size; i++) {

            buffer.add(charactersToBeAdded[i]);

            int expectedNumberOfChars = i + 1;
            assertEquals("Should contain " + expectedNumberOfChars + " items",
                         expectedNumberOfChars,
                         buffer.getNumberOfCharacters());
        }

        // Ensure that the characters really are in the buffer

        String actualCharacters = buffer.toString();
        String expectedCharacters = "123456789";

        assertEquals("Buffer should contain: 123456789",
                     expectedCharacters,
                     actualCharacters);
    }

    /**
     * Test to ensure that the maximum size of the buffer cannot be
     * exceeded.
     */
    public void testAddWhenBufferFull() {
        int size = 9;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'1','2','3','4','5','6','7','8', '9', '1'};

        try {
            for (int i = 0; i < size + 1; i++) {

                buffer.add(charactersToBeAdded[i]);

                int expectedNumberOfChars = i + 1;
                assertEquals("Should contain " + expectedNumberOfChars + " items",
                         expectedNumberOfChars,
                         buffer.getNumberOfCharacters());
            }

            // should never get here, CircularBufferFullException should be
            // thrown.
            fail();

        } catch(CircularCharacterBufferFullException e) {

        }
    }

    public void testRemoveCharsWhenOnlyOneCharInBufferAndTailNotWrapped()
        throws Throwable {
        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        // Add a character
        char charToBeAdded = 'a';
        buffer.add(charToBeAdded);

        // We have the following situation:

        //     (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   /   /   /   /   /
        // (H)

        // now remove 'a'
        char[] removedCharacters = buffer.removeChars(1);


        String expectedCharacter = "a";
        String actualCharacter = new String(removedCharacters);



        assertEquals("Should have retrieved 'a'.",
                     expectedCharacter,
                     actualCharacter);

        int expectedTailIndex = 1;
        assertTailIndex(buffer, expectedTailIndex);

        int expectedHeadIndex = 1;
        assertHeadIndex(buffer, expectedHeadIndex);
    }

    /**
     * This method tests {@link CircularCharacterBuffer#removeChars(int)} when
     * we have a situation where the index of tail has not wrapped and
     * the buffer is not full.
     */
    public void testRemoveCharsWhenTailNotWrappedAndBufferNotFull()
        throws Throwable {

        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   /   /
        // (H)             (T)

        char[] removedCharacters = buffer.removeChars(4);

        String expectedChars = "abcd";
        String actualChars = new String(removedCharacters);

        // ensure that head is updated accordingly
        int expectedHeadIndex = 4;
        assertHeadIndex(buffer, expectedHeadIndex);

        assertEquals("Should have obtained 'abcd' from buffer",
                     expectedChars, actualChars);

        // ensure that the size of the buffer is correct
        int itemsInBuffer = buffer.getNumberOfCharacters();
        int expectedItemsInBuffer = 0;

        assertEquals("Number of items in buffer should be 0",
                     itemsInBuffer, expectedItemsInBuffer);
    }

    public void testRemoveCharsWhenTailNotWrappedAndBufferFull()
        throws Throwable {
        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','f'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   f
        // (H)

        char[] removedCharacters = buffer.removeChars(0);

        String expectedChars = "abcdef";
        String actualChars = new String(removedCharacters);

        // ensure that head stays the same
        int expectedHeadIndex = 0;
        assertHeadIndex(buffer, expectedHeadIndex);

        assertEquals("Should have obtained 'abcdef' from buffer",
                     expectedChars, actualChars);

        // ensure that the size of the buffer is correct
        int itemsInBuffer = buffer.getNumberOfCharacters();
        int expectedItemsInBuffer = 0;

        assertEquals("Number of items in buffer should be 0",
                     itemsInBuffer, expectedItemsInBuffer);
    }

    public void testRemoveCharsWhenTailNotWrappedAndCharsRemaining()
        throws Throwable {

        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','f'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   f
        // (H)

        char[] removedCharacters = buffer.removeChars(4);

        String expectedChars = "abcd";
        String actualChars = new String(removedCharacters);

        // ensure that head is updated.
        int expectedHeadIndex = 4;
        assertHeadIndex(buffer, expectedHeadIndex);

        assertEquals("Should have obtained 'abcd' " + "from buffer",
                     expectedChars, actualChars);

        // ensure that the size of the buffer is correct
        int itemsInBuffer = buffer.getNumberOfCharacters();
        int expectedItemsInBuffer = 2;

        assertEquals("Number of items in buffer should be 2",
                     itemsInBuffer, expectedItemsInBuffer);
    }

    public void testRemoveCharsWhenTailWrappedButHeadNotAtZero()
        throws Throwable {

        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','f'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   f
        // (H)

        buffer.removeChars(4);

        // Now we have:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  /   /   /   /   e   f
        //                 (H)

        char[] removedChars = buffer.removeChars(6);

        String expectedChars = "ef";
        String actualChars = new String(removedChars);

        assertEquals("Should have obtained 'ef'", expectedChars, actualChars);

        // ensure that the size of the buffer is correct
        int itemsInBuffer = buffer.getNumberOfCharacters();
        int expectedItemsInBuffer = 0;

        assertEquals("Number of items in buffer should be 0",
                     expectedItemsInBuffer, itemsInBuffer);

        int expectedHeadIndex = 0;
        assertHeadIndex(buffer, expectedHeadIndex);
    }

    public void testRemoveCharsWhenTailWrappedAndBufferNotFull()
        throws Throwable {
        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','f'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   f
        // (H)

        buffer.removeChars(4);

        // Now we have:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  /   /   /   /   e   f
        //                 (H)

        // add another character
        buffer.add('g');

        // Now we have:

        //     (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   /   /   /   e   f
        //                 (H)

        char[] removedChars = buffer.removeChars(1);

        String expectedChars = "efg";
        String actualChars = new String(removedChars);

        assertEquals("Should have obtained 'efg'", expectedChars, actualChars);

        int expectedHeadIndex = 1;
        assertHeadIndex(buffer, expectedHeadIndex);
    }

    public
    void testRemoveCharsWhenTailWrappedButRemovingCharAtIndexLargerThanHead()
        throws Throwable {

        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','f'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   f
        // (H)

        buffer.removeChars(4);

        buffer.add('g');

        // Now we have:

        //     (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   /   /   /   e   f
        //                 (H)
        char[] removedChars = buffer.removeChars(6);

        String expectedCharacters = "ef";
        String actualCharacters = new String(removedChars);

        assertEquals("Should have obtained 'ef'",
                     expectedCharacters, actualCharacters);

    }

    public void testRemoveCharFromHeadWhenTailWrapped() throws Throwable {
        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','f'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   f
        // (H)

        buffer.removeChars(4);

        buffer.add('g');

        // Now we have:

        //     (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   /   /   /   e   f
        //                 (H)
        char[] removedChars = buffer.removeChars(5);

        String expectedCharacters = "e";
        String actualCharacters = new String(removedChars);

        assertEquals("Should have obtained 'e'",
                      expectedCharacters, actualCharacters);

    }

    public void testRemoveCharsWhenTailWrappedAndBufferFull()
        throws Throwable {
        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','f'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   f
        // (H)

        buffer.removeChars(4);

        // Now we have:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  /   /   /   /   e   f
        //                 (H)

        // add another character
        char[] moreChars = new char[] {'g', 'h', 'i', 'j'};
        buffer.add(moreChars);

        // Now we have:

        //                 (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   h   i   j   e   f
        //                 (H)

        char[] removedChars = buffer.removeChars(2);
        String expectedChars = "efgh";
        String actualChars = new String(removedChars);

        assertEquals("Should have obtained 'efgh'",
                     expectedChars,
                     actualChars);

        int expectedHeadIndex = 2;
        assertHeadIndex(buffer, expectedHeadIndex);

        int expetectedTailIndex = 4;
        assertTailIndex(buffer, expetectedTailIndex);

    }

    public void testRemoveCharsWithInvalidIndexWhenTailNotWrappedAndNotFull()
        throws Throwable {

        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   /   /
        // (H)             (T)

        try {

            // try an index that exceeds the bounds of (Tail - 1)
            buffer.removeChars(6);

            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {

        }
    }

    public void testRemoveCharsWithIndexLargerThanBufferSize()
        throws Throwable {
        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   /   /
        // (H)             (T)

        try {

            // try an index that exceeds the bounds of (Tail - 1)

            int indexExceededSizeOfBuffer = 7;
            buffer.removeChars(indexExceededSizeOfBuffer);

            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {

        }
    }

    public void testRemoveCharsWithIndexesNotWithinHeadAndTailWhenTailWrapped()
        throws Throwable {

        // We have:
        //
        //     (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   /   /   /   e   f
        //                 (H)

        try {
            testRemoveCharsWithIllegalIndexWhenTailHasWrapped(4);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {

        }

        try {
            testRemoveCharsWithIllegalIndexWhenTailHasWrapped(3);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {

        }

        try {
            testRemoveCharsWithIllegalIndexWhenTailHasWrapped(2);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {

        }
    }

    public void testSpaceRemainingWhenBufferEmpty() {
        int size = 10;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        int expectedSpaceRemaining = 10;
        testSpaceRemaining(buffer, expectedSpaceRemaining);
    }

    public void testSpaceRemainingWhenBufferFull() throws Exception {
        int size = 5;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToAdd = new char[] {'1', '2', '3', '4', '5'};
        buffer.add(charactersToAdd);

        int expectedSpaceRemaining = 0;
        testSpaceRemaining(buffer, expectedSpaceRemaining);
    }

    public void testSpaceRemainingWhenBufferHasItems() throws Exception {
        int size = 5;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToAdd = new char[] {'1', '2', '3'};
        buffer.add(charactersToAdd);

        int expectedSpaceRemaining = 2;
        testSpaceRemaining(buffer, expectedSpaceRemaining);
    }

    public void testSpaceRemainingAfterCharsRemoved() throws Exception {
        int size = 5;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToAdd = new char[] {'1', '2', '3', '4', '5'};
        buffer.add(charactersToAdd);

        buffer.removeChars(3);

        // 4, 5 should remain in buffer leaving 3 empty spaces.
        int expectedSpaceRemaining = 3;
        testSpaceRemaining(buffer, expectedSpaceRemaining);
    }

    public void testLastIndexOfWhenTailNotWrappedAndCharInBuffer()
        throws Exception {

        int size = 5;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToAdd = new char[] {'a', 'a', 'a', 'a', 'a'};

        buffer.add(charactersToAdd);

        int expectedLastIndex = 4;

        testLastIndexMatching(buffer, expectedLastIndex);
    }

    public void testLastIndexOfWhenTailNotWrappedAndCharNotInBuffer()
        throws Exception {

        int size = 5;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToAdd = new char[] {'b', 'b', 'b', 'b', 'b'};

        buffer.add(charactersToAdd);

        int expectedLastIndex = -1;
        testLastIndexMatching(buffer, expectedLastIndex);
    }

    public void testLastIndexOfWhenTailWrappedAndCharInBuffer()
        throws Exception {

        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','a','a','a','a','a'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   a   a   a   a   a
        // (H)

        buffer.removeChars(3);

        // Now we have:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  /   /   /   /   a   a
        //                 (H)

        // add another character
        buffer.add('a');

        // Now we have:

        //     (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   /   /   /   g   g
        //                 (H)

        int expectedLastIndex = 0;
        testLastIndexMatching(buffer, expectedLastIndex);
    }

    public void testLastIndexOfWhenTailWrappedHeadNotZeroAndCharInBuffer()
        throws Exception {

        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','g','g'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   g   g
        // (H)

        buffer.removeChars(3);

        // Now we have:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  /   /   /   /   g   g
        //                 (H)

        // add another character
        buffer.add('g');

        // Now we have:

        //     (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   /   /   /   g   g
        //                 (H)

        int expectedLastIndex = -1;
        testLastIndexMatching(buffer, expectedLastIndex);
    }

    public void testLastIndexOfWhenBufferFullAndTailNotZero()
        throws Exception {

        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','j'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   j
        // (H)

        buffer.removeChars(4);

        // Now we have:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  /   /   /   /   e   f
        //                 (H)

        // add another character
        char[] moreChars = new char[] {'g', 'h', 'a', 'j'};
        buffer.add(moreChars);

        // Now we have:

        //                 (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   h   i   j   e   j
        //                 (H)

        int expectedLastIndex = 2;
        testLastIndexMatching(buffer, expectedLastIndex);
    }

    public void testEmptyBufferWhenTailNotWrappedAndNotFull()
        throws Throwable {

        int size = 10;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        // Add a character
        char charToBeAdded = 'a';
        buffer.add(charToBeAdded);

        char[] removedChars = buffer.emptyBuffer();

        String expectedContents = "a";
        String actualContents = new String(removedChars);

        assertEquals("Should remove 'a'.",
                     expectedContents,
                     actualContents);

        assertEquals("Buffer should be empty", 0,
                     buffer.getNumberOfCharacters());

        int expectedHeadIndex = 1;
        assertHeadIndex(buffer, expectedHeadIndex);

        int expetectedTailIndex = 1;
        assertTailIndex(buffer, expetectedTailIndex);

    }

    public void testEmptyBufferWhenFull() throws Throwable {
        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','f'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   f
        // (H)

        char[] removedChars = buffer.emptyBuffer();

        String expectedContents = "abcdef";
        String actualContents = new String(removedChars);

        assertEquals("Should remove 'abcdef'.",
                     expectedContents,
                     actualContents);

        assertEquals("Buffer should be empty", 0,
                     buffer.getNumberOfCharacters());

        int expectedHeadIndex = 0;
        assertHeadIndex(buffer, expectedHeadIndex);

        int expetectedTailIndex = 0;
        assertTailIndex(buffer, expetectedTailIndex);
    }

    public void testEmptyBufferWhenTailWrappedAndFull() throws Throwable {
        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','j'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   j
        // (H)

        buffer.removeChars(4);

        // Now we have:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  /   /   /   /   e   f
        //                 (H)

        // add another character
        char[] moreChars = new char[] {'g', 'h', 'i', 'j'};
        buffer.add(moreChars);

        // Now we have:

        //                 (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   h   i   j   e   j
        //                 (H)

        char[] removedChars = buffer.emptyBuffer();

        String expectedContents = "ejghij";
        String actualContents = new String(removedChars);

        assertEquals("Should remove 'ejghij'.",
                     expectedContents,
                     actualContents);

        assertEquals("Buffer should be empty", 0,
                     buffer.getNumberOfCharacters());

        int expectedHeadIndex = 4;
        assertHeadIndex(buffer, expectedHeadIndex);

        int expetectedTailIndex = 4;
        assertTailIndex(buffer, expetectedTailIndex);

    }

    public void testEmptyBufferWhenTailWrappedAndNotFull() throws Throwable {
        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','j'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   j
        // (H)

        buffer.removeChars(4);

        // Now we have:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  /   /   /   /   e   f
        //                 (H)

        // add another character
        char[] moreChars = new char[] {'g', 'h', 'i'};
        buffer.add(moreChars);

        // Now we have:

        //             (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   h   i   /   e   j
        //                 (H)

        char[] removedChars = buffer.emptyBuffer();

        String expectedContents = "ejghi";
        String actualContents = new String(removedChars);

        assertEquals("Should remove 'ejghi'.",
                     expectedContents,
                     actualContents);

        assertEquals("Buffer should be empty", 0,
                     buffer.getNumberOfCharacters());

        int expectedHeadIndex = 3;
        assertHeadIndex(buffer, expectedHeadIndex);

        int expetectedTailIndex = 3;
        assertTailIndex(buffer, expetectedTailIndex);
    }

    /**
     * Ensure that the buffer is emptied correctly when we have the
     * following situation:
     *
     *                        (T)
     *    [0] [1] [2] [3] [4] [5]
     *     /   /   /   a   b   /
     *                (H)
     *
     */
    public void testEmptyBufferWhenNotFullTailNotWrappedAndHeadNotZero()
        throws Throwable {

        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','j'};

        buffer.add(charactersToBeAdded);

        // We have the following situation:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  a   b   c   d   e   j
        // (H)

        buffer.removeChars(4);

        // Now we have:

        // (T)
        // [0] [1] [2] [3] [4] [5]
        //  /   /   /   /   e   f
        //                 (H)

        // add another character
        char[] moreChars = new char[] {'g', 'h', 'i'};
        buffer.add(moreChars);

        // Now we have:

        //             (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   h   i   /   e   j
        //                 (H)

        char[] removedChars = buffer.emptyBuffer();

        String expectedContents = "ejghi";
        String actualContents = new String(removedChars);

        assertEquals("Should remove 'ejghi'.",
                     expectedContents,
                     actualContents);

        assertEquals("Buffer should be empty", 0,
                     buffer.getNumberOfCharacters());

        int expectedHeadIndex = 3;
        assertHeadIndex(buffer, expectedHeadIndex);

        int expetectedTailIndex = 3;
        assertTailIndex(buffer, expetectedTailIndex);

        buffer.add('a');
        buffer.add('b');

        // We now have:
        //                     (T)
        // [0] [1] [2] [3] [4] [5]
        //  /   /   /   a   b   /
        //             (H)


        removedChars = buffer.emptyBuffer();
        expectedContents = "ab";
        actualContents = new String(removedChars);

        assertEquals("Should remove 'ab'.",
                     expectedContents,
                     actualContents);

        assertEquals("Buffer should be empty", 0,
                     buffer.getNumberOfCharacters());

        expectedHeadIndex = 5;
        assertHeadIndex(buffer, expectedHeadIndex);

        expetectedTailIndex = 5;
        assertTailIndex(buffer, expetectedTailIndex);
    }

    /**
     * Helper method to test
     * {@link CircularCharacterBuffer#lastIndexMatching(CharacterMatcher)}.
     * <p>
     * This test is executed using a CharacterMatcher that matches on
     * the 'a' character.
     *
     * @param buffer the buffer to be used.
     * @param expectedLastIndexOfChar the expected index of characterToFind.
     */
    private void testLastIndexMatching(CircularCharacterBuffer buffer,
                                 int expectedLastIndexOfChar) {

        CharacterMatcher matchOnA = new CharacterMatcher() {
            public boolean matches(char c) {
                return c == 'a';
            }
        };

        int actualLastIndexOfChar = buffer.lastIndexMatching(matchOnA);
        assertEquals(expectedLastIndexOfChar, actualLastIndexOfChar);
    }

    /**
     * Helper method to test the space remaining in the supplied buffer.
     *
     * @param buffer the buffer to be examined.
     * @param expectedSpaceRemaining the space expected to be remaining
     * in buffer.
     */
    private void testSpaceRemaining(CircularCharacterBuffer buffer,
                                    int expectedSpaceRemaining) {
       int actualSpaceRemaining = buffer.spaceRemaining();

       assertEquals("Remaining space should be: " + expectedSpaceRemaining,
                     expectedSpaceRemaining, actualSpaceRemaining);
    }

    /**
     * This is a helper method that will create a
     * {@link CircularCharacterBuffer} with the following internal values: <br>
     *
     * (T) <br>
     * [0] [1] [2] [3] [4] [5] <br>
     *  g   /   /   /   e   f <br>
     *                 (H) <br>
     *
     * {@link CircularCharacterBuffer#removeChars(int)} will be called with
     * the supplied index.
     *
     * @param index the index from which removal will begin moving backwards
     * towards the head.
     */
    private void testRemoveCharsWithIllegalIndexWhenTailHasWrapped(int index) {
        int size = 6;
        CircularCharacterBuffer buffer = new CircularCharacterBuffer(size);

        char[] charactersToBeAdded =
                new char[] {'a','b','c','d','e','f'};

        try {
            buffer.add(charactersToBeAdded);

            // We have the following situation:

            // (T)
            // [0] [1] [2] [3] [4] [5]
            //  a   b   c   d   e   f
            // (H)

            buffer.removeChars(4);

            // Now we have:

            // (T)
            // [0] [1] [2] [3] [4] [5]
            //  /   /   /   /   e   f
            //                 (H)

            // add another character
            buffer.add('g');

            //     (T)
            // [0] [1] [2] [3] [4] [5]
            //  g   /   /   /   e   f
            //                 (H)

            buffer.removeChars(index);
        } catch (CircularCharacterBufferFullException e){
            // this should never happen as we are not completely
            // filling the collection.
            fail("The buffer is full");
        }
    }

    /**
     * Tests the value of the head index used to maintain the starting
     * point of the buffer.
     *
     * @param buffer the buffer being inspected.
     * @param expectedTailIndex the expected value of 'indexOfTail'.
     */
    private void assertTailIndex(CircularCharacterBuffer buffer,
                                 int expectedTailIndex)
            throws Throwable {
        
        assertValueOfPrivateIntMember(buffer, 
                                      "indexOfTail", 
                                      expectedTailIndex);
    }

    /**
     * Tests the value of the head index used to maintain the starting
     * point of the buffer.
     *
     * @param buffer the buffer being inspected.
     * @param expectedHeadIndex the expected value of 'indexOfHead'.
     */
    private void assertHeadIndex(CircularCharacterBuffer buffer,
                                 int expectedHeadIndex) throws Throwable {

        assertValueOfPrivateIntMember(buffer,
                                      "indexOfHead",
                                      expectedHeadIndex);
    }

    /**
     * Helper method to test the value of a private member in
     * {@link CircularCharacterBuffer}.
     *
     * @param buffer the buffer to inspect.
     * @param memberName the member name we want the value of
     * @param expectedValue the value expected for memeberName
     *
     * @throws Throwable
     */
    private void assertValueOfPrivateIntMember(CircularCharacterBuffer buffer,
                                               String memberName,
                                               int expectedValue) 
        throws Throwable {
        
        int actualValue = ((Integer)
                PrivateAccessor.getField(buffer,
                        memberName)).intValue();
        assertEquals("The value of " + "'" + memberName + "'in " + buffer + 
                     " was" + " not as expected", expectedValue, actualValue);
        
        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 20-Oct-05	9907/1	rgreenall	VBM:2005101901 ArrayIndexOutOfBoundsException thrown when emptying the buffer under the following conditions: Tail not wrapped, not full and head not at index 0.

 20-Oct-05	9894/1	rgreenall	VBM:2005101901 ArrayIndexOutOfBoundsException thrown when emptying the buffer under the following conditions: Tail not wrapped, not full and head not at index 0.

 03-Oct-05	9681/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 30-Sep-05	9583/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 ===========================================================================
*/
