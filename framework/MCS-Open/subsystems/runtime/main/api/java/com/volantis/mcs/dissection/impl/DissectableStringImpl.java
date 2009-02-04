package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.DissectionException;

/**
 * Created by IntelliJ IDEA.
 * User: mrybak
 * Date: Aug 19, 2008
 * Time: 8:41:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class DissectableStringImpl implements DissectableString {

    private String string;

    public DissectableStringImpl(DOMDissectableTextImpl text) {
        this.string = new String(text.getDOMContents(), 0, text.getDOMLength());
    }

    public int getLength() throws DissectionException {
        return string.length();
    }

    public int charAt(int index) throws DissectionException {
        return string.charAt(index);
    }

    public int getNextBreakPoint(int breakPoint) throws DissectionException {
        //TODO it is never used. Consider removing it from the interface.
        throw new RuntimeException("Not implemented");
    }

    public int getPreviousBreakPoint(int breakPoint) throws DissectionException {
        //TODO it is never used. Consider removing it from the interface.
        throw new RuntimeException("Not implemented");
    }

    public int getCost() throws DissectionException {
        return string.length();
    }

    public boolean isCostContextDependent() throws DissectionException {
        //TODO it is never used. Consider removing it from the interface.
        throw new RuntimeException("Not implemented");
    }

    public int getRangeCost(int startIndex, int endIndex) throws DissectionException {
        return endIndex - startIndex;
    }

    public int getCharacterIndex(int startIndex, int cost) throws DissectionException {
        int endIndex = startIndex + cost;
        if (endIndex < string.length()) {
            return endIndex;
        } else {
            return string.length();
        }
    }
}
