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
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.wbsax.WBSAXValueVisitor;

/**
 * A abstract Adaptor class which can be used to wrap a WBSAX object in order 
 * to make it implement {@link DissectableString}, where the object being 
 * wrapped is a single logical character.
 */ 
public abstract class DissectableWBSAXCharacter 
        implements DissectableString, WBSAXValueVisitor.Acceptor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    // Inherit Javadoc.
    public int getNextBreakPoint(int breakPoint)
            throws DissectionException {
        // DefaultStringDissector doesn't use break points yet...
        throw new IllegalStateException("this should not be called yet");
    }

    // Inherit Javadoc.
    public int getPreviousBreakPoint(int breakPoint)
            throws DissectionException {
        // DefaultStringDissector doesn't use break points yet...
        throw new IllegalStateException("this should not be called yet");
    }

    // Inherit Javadoc.
    public int getRangeCost(int startIndex, int endIndex)
            throws DissectionException {
        // Only one character in the string, so only valid start index is 0.
        if (startIndex != 0) {
            throw new IllegalArgumentException("Invalid start index " + 
                    startIndex);
        }
        int rangeCost;
        switch (endIndex) {
            case 0:
                // range from 0->0. Is this valid?
                rangeCost = 0;
                break;
            case 1:
                // range from 0->1. The only useful range.
                rangeCost = getCost();
                break;
            default:
                throw new IllegalArgumentException("Invalid end index " + 
                        endIndex);
        }
        return rangeCost;
    }

    // Inherit Javadoc.
    public int getCharacterIndex(int startIndex, int cost)
            throws DissectionException {
        // Only one character in the string, so only valid start index is 0.
        if (startIndex != 0) {
            throw new IllegalArgumentException("Invalid start index " + 
                    startIndex);
        }
        if (cost >= getCost()) {
            return 1;
        } else {
            return 0;
        }
    }

    // Inherit Javadoc.
    public int getLength() {
        // Length is always 1 for a single character string.
        return 1;
    }

    // Inherit Javadoc.
    public int charAt(int index) {
        // Only one character in the string, so only valid index is 0.
        if (index != 0) {
            throw new IllegalArgumentException("Index invalid:" + index);
        }
        return getChar();
    }

    /**
     * Return the single character that this string contains.
     * 
     * @return the character
     */ 
    protected abstract int getChar(); 
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 ===========================================================================
*/
