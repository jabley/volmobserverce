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
package com.volantis.mcs.dissection.dom.impl;

import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.DissectableText;

/**
 * A very basic impl of dissectable string for the TDOM. Currently
 * only supports TSimpleString content.
 */ 
public class TDissectableString implements DissectableString {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public TDissectableString(DissectableText text) {
        TText ttext = (TText) text;
        TString tstring = ttext.getContents();
        if (tstring instanceof TSimpleString) {
            // Suck the contents out of the simple string.
            this.string = ((TSimpleString) tstring).getContents();
        } else {
            throw new UnsupportedOperationException(
                    "Can only dissect simple strings for now");
        }
    }

    private String string;
    
    public int getLength() throws DissectionException {
        return string.length();
    }

    public int charAt(int index) throws DissectionException {
        return string.charAt(index);
    }

    public int getNextBreakPoint(int breakpoint) throws DissectionException {
        // DefaultStringDissector doesn't use break points yet...
        throw new IllegalStateException("this should not be called yet");
//        if (breakpoint >= 0 && breakpoint < string.length()) {
//            return breakpoint + 1;
//        } else {
//            throw new IllegalStateException("Invalid breakpoint " + breakpoint);
//        }
    }

    public int getPreviousBreakPoint(int breakpoint) 
            throws DissectionException {
        // DefaultStringDissector doesn't use break points yet...
        throw new IllegalStateException("this should not be called yet");
//        if (breakpoint > 0 && breakpoint <= string.length()) {
//            return breakpoint - 1;
//        } else {
//            throw new IllegalStateException("Invalid breakpoint " + breakpoint);
//        }
    }

    public int getCost() throws DissectionException {
        return string.length();
    }

    public boolean isCostContextDependent() throws DissectionException {
        // We don't support shared references ... yet.
        return false;
    }

    public int getRangeCost(int startIndex, int endIndex) 
            throws DissectionException {
        return endIndex - startIndex;
    }

    public int getCharacterIndex(int startIndex, int cost)
            throws DissectionException {
        int endIndex = startIndex + cost;
        if (endIndex < string.length()) {
            return endIndex;
        } else {
            return string.length();
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-03	365/3	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
