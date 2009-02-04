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

/**
 * This class is responsible for providing a buffer for storing characters
 * prior to being written by a
 * {@link com.volantis.mcs.dom.output.LineLengthRestrictingWriter}.
 * <p>
 * This class is implements the buffer using a circular array to avoid
 * shifting elements when the buffer items are removed from the buffer.
 * <p>
 */
public class CircularCharacterBuffer {

    /**
     * The underlying buffer used to store characters.
     */
    private char[] buffer;

    /**
     * The index in {@link #buffer} that represents the start of the buffer.
     * This will initialy be zero, but this will change as content in
     * the buffer is flushed and the head is updated to the index of the last
     * character in the buffer.
     */
    private int indexOfHead;

    /**
     * The index in {@link #buffer} at which a the next character added using
     * {@link #add} will be stored.
     */
    private int indexOfTail;

    /**
     * The number of characters that may be stored by this buffer.
     */
    private int maxSize;

    /**
     * The number of characters that are stored in this buffer.
     */
    private int numberOfCharacters;

    /**
     * Initialises an instance with the supplied parameters.
     *
     * @param size the number of characters that this buffer can store.  Note
     * that the maxSize may not be changed once set.
     */
    public CircularCharacterBuffer(int size) {
        this.maxSize = size;
        buffer = new char[size];
        indexOfHead = 0;
        indexOfTail = 0;
    }


    /**
     * Adds the supplied character to the buffer.
     *
     * @param character the character to be added.
     *
     * @throws CircularCharacterBufferFullException if the buffer is full.
     */
    public void add(char character)
            throws CircularCharacterBufferFullException {

        // Ensure that we do not exceed the maximum size allocated
        // for this buffer.
        if (isFull()) {
            throw new
                    CircularCharacterBufferFullException(
                            "The buffer is full.");
        }

        // Place the character in the buffer
        buffer[indexOfTail] = character;

        // Increment the tail ensuring that the tail
        // wraps to zero when the end of the array is reached.
        indexOfTail = nextIndex(indexOfTail);

        // Increment the number of characters stored.
        numberOfCharacters++;
    }

    /**
     * Adds the supplied characters to the buffer.
     *
     * @param characters the characters to be added.
     *
     * @throws CircularCharacterBufferFullException if buffer is full.
     **/
    public void add(char[] characters) throws
            CircularCharacterBufferFullException {

        int numberOfCharacterToAdd = characters.length;

        for (int i = 0; i < numberOfCharacterToAdd; i++) {
            add(characters[i]);
        }
    }

    /**
     * Returns the characters in the buffer upto
     * the character stored at the previous index to the
     * <code>endIndex</code>.  This method
     * will also remove the characters returned from the buffer.
     *
     * @param endIndex the index (exclusive) at which to start
     * the removal moving backwards toward the start of the buffer.
     *
     * @return the characters that exist in the buffer upto and including
     * the character (exclusive) stored at the supplied index.
     */
    public char[] removeChars(int endIndex) throws IllegalArgumentException {

        // Check that the index supplied in valid given the current position
        // of Head and Tail.

        int index = previousIndex(endIndex);
        assertIndexBetweenHeadAndTail(index);

        String charsToBeRemovedAsString =
                getCharactersBetweenHeadAndIndex(index);

        // Update the position of the head index accordingly
        if (!isFull()) {

            // The position of Head needs to be updated
            indexOfHead = nextIndex(index);
        } else {
            // we have a full buffer, i.e head = tail. If
            // index = Tail then head does
            // not need to be updated
            // as all items in the buffer will have been removed.

            if (index != indexOfTail) {
                indexOfHead = nextIndex(index);
            } else {
                // index == Tail so we are removing all items
                // in the buffer.  As Head = Tail nothing needs
                // to be done as we are already in the correct state ready
                // for more items to be added.
            }
        }

        // Update the count of items in the buffer
        numberOfCharacters =
                numberOfCharacters - charsToBeRemovedAsString.length();

        return charsToBeRemovedAsString.toCharArray();
    }

    /**
     * Removes all characters stored in the buffer.
     *
     * @return an array of characters that are stored in the buffer in
     * the order they were added.
     */
    public char[] emptyBuffer() {
        if (numberOfCharacters == 0) {
            return new char[0];
        } else {
            return removeChars(indexOfTail);
        }
    }

    /**
     * Returns a String that contains the characters stored in this
     * buffer between {@link #indexOfHead} and index (inclusive).
     *
     * @param index the index of the last character to be obtained from
     * the collection.
     *
     * @return the characters stored in this buffer between
     * {@link #indexOfHead} and the supplied index (inclusive).
     */
    private String getCharactersBetweenHeadAndIndex(int index) {

        // We need to handle the following situations:

        // 1. Index = Head, ie only one character between head and index.
        // e.g.

        //     (T)
        // [0] [1] [2] [3] [4] [5] [6] [7]
        //  g   /   /   /   /   /   /   /
        // (H)

        // 2. Tail not wrapped, need characters between head and index

        //                     (T)
        // [0] [1] [2] [3] [4] [5] [6] [7]
        //  a   b   c   d   e   /   /   /
        // (H)

        // 3. Tail has wrapped and buffer is full. Head = 0

        // (T)
        // [0] [1] [2] [3] [4] [5] [6] [7]
        //  a   b   c   d   e   f   g   h
        // (H)

        // 4. Tail has wrapped and buffer is full.  Head != 0

        //                 (T)
        // [0] [1] [2] [3] [4] [5] [6] [7]
        //  a   b   c   d   e   f   g   h
        //                 (H)

        // 5. Tail not wrapped and buffer not full, Head > 0.
        //                     (T)
        // [0] [1] [2] [3] [4] [5] [6] [7]
        //  /   /   c   d   e   /   /   /
        //         (H)

        // 5. Tail has wrapped and buffer not full, Head > 0.
        //     (T)
        // [0] [1] [2] [3] [4] [5] [6] [7]
        //  f   /   /   a   b   c   d   e
        //             (H)

        // Calculate the number of characters between head and the supplied
        // index inclusive
        int numberOfCharactersToCopy =
                getNumberOfCharactersBetweenHeadAndIndex(index);

        char[] charsBetweenHeadAndIndex = new char[numberOfCharactersToCopy];

        if (index == indexOfHead) {
            charsBetweenHeadAndIndex = new char[]{buffer[indexOfHead]};
        } else if (tailHasWrapped()) {
            if (index > indexOfHead) {
                // we need characters between head and index
                 int startingIndex = 0;
                 System.arraycopy(buffer, indexOfHead,
                             charsBetweenHeadAndIndex,
                             startingIndex, numberOfCharactersToCopy);
            } else {
                // we need characters between head and maxSize - 1
                int startingIndex = 0;
                int numOfCharsBetweenHeadAndIndex =
                    (maxSize) - indexOfHead;
                System.arraycopy(buffer, indexOfHead,
                             charsBetweenHeadAndIndex,
                             startingIndex, numOfCharsBetweenHeadAndIndex);



                // and also characters between 0 and index
                System.arraycopy(buffer, 0,
                             charsBetweenHeadAndIndex,
                             numOfCharsBetweenHeadAndIndex,
                             numberOfCharactersToCopy - numOfCharsBetweenHeadAndIndex);
            }

        } else {
            // The tail has not wrapped we simply need to copy the characters
            // between head and index
            int startingIndex = 0;
            System.arraycopy(buffer, indexOfHead,
                             charsBetweenHeadAndIndex,
                             startingIndex, numberOfCharactersToCopy);
        }

        return new String(charsBetweenHeadAndIndex);
    }

    /**
     * Returns the number of characters in the buffer between the head and
     * the supplied index (inclusive).
     *
     * @param index an index in the buffer.
     *
     * @return number of characters in the buffer between the head
     * and the supplied index (inclusive).
     */
    private int getNumberOfCharactersBetweenHeadAndIndex(int index) {
        // Calculate the number of characters between head and the supplied
        // index inclusive
        int numberOfCharactersToCopy = 0;

        // If the buffer is full and the index after the supplied index
        // is the tail then we know we need to copy everything in the
        // buffer.
        int nextIndex = nextIndex(index);
        if (isFull() && nextIndex == indexOfTail) {
            // we need to copy everything
            numberOfCharactersToCopy = maxSize;
        } else if (tailHasWrapped() && indexOfHead > 0) {

            // We have a situation has follows:

            //     (T)
            // [0] [1] [2] [3] [4] [5] [6] [7]
            //  g   /   /   /   e   f   g   h
            //                 (H)

            if (index >= indexOfHead) {
                // We are only interested in the characters between head
                // and index.
                int numOfCharsBetweenHeadAndIndex =
                    (index + 1) - indexOfHead;
                numberOfCharactersToCopy = numOfCharsBetweenHeadAndIndex;
            } else {

                // We are also interested in characters between the start
                // of the buffer and index.
                int numOfCharsBetweenStartOfArrayAndIndex = index + 1;

                int numOfCharsBetweenHeadAndIndex =
                    (maxSize) - indexOfHead;
                numberOfCharactersToCopy = numOfCharsBetweenHeadAndIndex +
                        numOfCharsBetweenStartOfArrayAndIndex;
            }

        } else {

            // We have the following situations:

            //                             (T)
            // [0] [1] [2] [3] [4] [5] [6] [7]
            //  a   b   c   d   e   f   g   /
            // (H)

            // Or

            // (T)
            // [0] [1] [2] [3] [4] [5] [6] [7]
            //  a   b   c   d   e   f   g   h
            // (H)

            // Or
            //                     (T)
            // [0] [1] [2] [3] [4] [5] [6] [7]
            //  /   /   c   d   e   /   /   /
            //         (H)
            numberOfCharactersToCopy = (index + 1) - indexOfHead;
        }
        return numberOfCharactersToCopy;
    }


    /**
     * Returns the index within this buffer of the last occurrence of the
     * specified character. The search is performed backwards through
     * the buffer starting with the last character added.
     *
     * @param characterMatcher the matcher to use to match the characters
     * in the buffer.
     *
     * @return the index of the first occurence of the supplied character,
     * searching backwards through the buffer starting with the last
     * character added; or -1 if the character is not found.
     */
    public int lastIndexMatching(CharacterMatcher characterMatcher) {

        int numberOfCharactersCompared = 0;
        int currentIndex = previousIndex(indexOfTail);
        boolean foundCharacter = false;
        int lastIndexOfCharacter = -1;

        while(numberOfCharactersCompared < numberOfCharacters &&
                !foundCharacter) {

            char currentChar = buffer[currentIndex];

            if (characterMatcher.matches(currentChar)) {
                lastIndexOfCharacter = currentIndex;
                foundCharacter = true;
            }
            numberOfCharactersCompared++;
            // increment the current index
            currentIndex = previousIndex(currentIndex);
        }

        return lastIndexOfCharacter;
    }

    /**
     * Returns the number of characters
     * that may be added before the buffer becomes full.
     *
     * @return the number of characters that may be added to the buffer before
     * the buffer becomes full.
     */
    public int spaceRemaining() {
        return maxSize - numberOfCharacters;
    }

    /**
     * Returns true if the buffer is full; otherwise false.
     *
     * @return true if full; otherwise false.
     */
    public boolean isFull() {
        return numberOfCharacters == maxSize;
    }

    /**
     * Returns the number of characters stored in this buffer.
     *
     * @return the number of characters stored in the buffer.
     */
    public int getNumberOfCharacters() {
        return numberOfCharacters;
    }

    /**
     * Returns a string containing all characters currently stored in
     * this buffer.
     *
     * @return a string containing all of the characters stored in this
     * buffer.
     */
    public String toString() {

        String characters = null;
        characters = getCharactersBetweenHeadAndIndex(
                previousIndex(indexOfTail));
        return characters;
    }

    /**
     * Returns the index value immediately after the supplied index.
     * <p>
     * If <code>index</code> is equals to <code>size</code> - 1, then
     * zero will be returned as we are using a circular array as the backing
     * store for the buffer.
     *
     * @param index the current index.
     *
     * @return the index immediately after the supplied index.
     */
    private int nextIndex(int index) {
        // ensure that the index returns to zero if
        // maxSize - 1 is supplied as an index.
        int nextIndex = (index + 1) % maxSize;
        return nextIndex;
    }

    /**
     * Returns the previous index to the one supplied.
     * <p>
     * If <code>index</code> is equal to zero then <code>maxSize</code> - 1
     * will be returned.
     *
     * @param index the current index.
     *
     * @return the index the index occuring immediately before the supplied
     * index.
     */
    private int previousIndex(int index) {

        int previousIndex;

        if (index == 0) {
            previousIndex = maxSize - 1;
        } else {
            previousIndex = index - 1;
        }
        return previousIndex;
    }

    /**
     * Returns true if tail has wrapped around, ie, tail < head.
     *
     * @return return true of tail has wrapped around.
     */
    private boolean tailHasWrapped() {

        // The tail is said to be wrapped when tail < head
        // e.g
        //     (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   h   /   /   e   f
        //                 (H)
        // or
        // tail = head
        //                 (T)
        // [0] [1] [2] [3] [4] [5]
        //  g   h   i   j   e   f
        //                 (H)

        boolean hasTailWrapped = false;

        if ((indexOfTail < indexOfHead) || (indexOfTail == indexOfHead)) {
            hasTailWrapped = true;
        }

        return hasTailWrapped;
    }

    /**
     * Checks that the supplied index is within the bounds of the
     * current index positions of head and tail.
     *
     * @param index the index to be tested
     *
     * @throws IllegalArgumentException if the supplied index is not within
     * the current index positions of head and tail.
     */
    private void assertIndexBetweenHeadAndTail(int index)
            throws IllegalArgumentException {

        if(isFull()) {
            // the supplied index must be within the bounds of the underlying
            // buffer array
            // char testIndex = buffer[index];
            if ( (index > maxSize - 1) || index < 0) {
                throw new IllegalArgumentException();
            }
        } else {
            if (!tailHasWrapped()) {
                // The tail has not wrapped yet.

                // Does the index exceed (Tail - 1)?
                if (index > (indexOfTail)) {
                  throw new IllegalArgumentException("Index: " + index +
                                                     " >= indexOfTail: " +
                                                     indexOfTail);
                }
            } else {
                // The tail has wrapped. e.g

                //         (T)
                // [0] [1] [2] [3] [4] [5]
                //  g   h   /   /   e   f
                //                 (H)

                // We need to ensure that index >= Head and index < Tail
                if ( index < indexOfHead && index >= indexOfTail ) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 03-Oct-05	9681/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 30-Sep-05	9583/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 ===========================================================================
*/
