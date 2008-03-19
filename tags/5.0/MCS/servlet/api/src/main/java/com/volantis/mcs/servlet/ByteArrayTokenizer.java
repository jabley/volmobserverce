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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * A tokenizer class for byte arrays.
 * <p>Given a byte array to tokenize and a byte array representing a separator,
 * this class will divide the array into a series of byte array tokens
 * delimited by the separator sequence.</p>
 * <p>The tokenized values can be retrieved as a single set of values, or
 * through an Enumeration (by analogy with {@link java.util.StringTokenizer}).
 * </p>
 */
public class ByteArrayTokenizer implements Enumeration {
    /**
     * The tokenized values stored as an array of byte[] tokens.
     */
    private final byte[][] tokenized;

    /**
     * The index of the current token to return in the Enumeration.
     */
    int currentToken = 0;

    /**
     * Construct a new tokenizer for the given byte array and separator.
     * <p>Tokenizes the array up front, and stores the tokenized values for
     * future easy access.</p>
     *
     * @param toTokenize The array to tokenize
     * @param separator The separator sequence
     */
    public ByteArrayTokenizer(byte[] toTokenize, byte[] separator) {
        this.tokenized = tokenizeImpl(toTokenize, separator);
    }

    /**
     * Splits the byte array for tokenization into an array of byte arrays
     * divided by the separator byte array and returns the tokenized values.
     * Note that the separator sequence is not included in any of the resulting
     * byte arrays.
     *
     * @return The tokenized representation of the byte array
     */
    private byte[][] tokenizeImpl(byte[] toTokenize, byte[] separator) {
        byte[][] splitArray = null;
        // If the separator is null or empty, or the array is null or shorter
        // than the separator, then there's no need to split.
        if (separator == null || toTokenize == null || separator.length == 0 ||
                toTokenize.length < separator.length) {
            splitArray = new byte[][] { toTokenize };
        } else {
            ArrayList list = new ArrayList();
            int sepIndex = 0;
            int lastMatch = 0;
            for (int charIndex = 0;
                 charIndex < toTokenize.length;
                 charIndex++) {
                if (toTokenize[charIndex] == separator[sepIndex]) {
                    // We've matched a byte from the separator sequence -
                    // increment the index pointing into the separator to point
                    // at the next expected character
                    sepIndex += 1;
                    if (sepIndex == separator.length) {
                        // We've found the full separator - copy the previous
                        // token into a new array and store it.
                        byte[] token =
                                new byte[charIndex - sepIndex - lastMatch + 1];
                        for (int i = 0; i < token.length; i++) {
                            token[i] = toTokenize[lastMatch + i];
                        }
                        list.add(token);

                        // Update the location of the last match and reset our
                        // separator index to start from the first character
                        // again.
                        lastMatch = charIndex + 1;
                        sepIndex = 0;
                    }
                } else {
                    // The current index in the array was not the appropriate
                    // value in the separator sequence, so reset our counter
                    // through the sequence, and step back to the start of the
                    // current matching sequence.
                    charIndex -= sepIndex;
                    sepIndex = 0;
                }
            }

            // If we have data that isn't part of a token already, create a
            // token for the end of the array
            if (lastMatch <= toTokenize.length) {
                byte[] token = new byte[toTokenize.length - lastMatch];
                for (int i = 0; i < token.length; i++) {
                     token[i] = toTokenize[lastMatch + i];
                }
                list.add(token);
            }

            // Convert the ArrayList into a more lightweight array of byte[]
            // values
            splitArray = new byte[list.size()][];
            list.toArray(splitArray);
        }

        return splitArray;
    }

    /**
     * Returns all tokens as a single array of byte[] values.
     *
     * @return An array containing all tokens.
     */
    public byte[][] getAllTokens() {
        return tokenized;
    }

    /**
     * Returns true if there are more tokens that have not yet been seen.
     *
     * @return True if there are more tokens, false otherwise
     */
    public boolean hasMoreTokens() {
        return (currentToken < tokenized.length);
    }

    /**
     * Returns the next token that has not already been returned by this
     * method.
     *
     * @return The next byte[] token
     * @throws NoSuchElementException if there are no more tokens to return
     */
    public byte[] nextToken() {
        if (!hasMoreTokens()) {
            throw new NoSuchElementException();
        } else {
            return tokenized[currentToken++];
        }
    }

    // Javadoc inherited
    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    // Javadoc inherited
    public Object nextElement() {
        return nextToken();
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Feb-05	6786/3	adrianj	VBM:2005012506 Rendered page cache rework

 14-Feb-05	6786/1	adrianj	VBM:2005012506 Rendered page cache rework

 ===========================================================================
*/
