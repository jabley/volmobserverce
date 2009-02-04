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
package com.volantis.mcs.eclipse.controls;

/**
 * This is the interface for History Managers
 */
public interface HistoryManager {


    /**
     * Inserts the provided value into the history list at the top if it does not
     * already occur in the list, otherwise it moves the existing entry to the top.
     * @param value - The new history value
     */
    public void updateHistory(String value);


    /**
     * Gets the history as an Array of Strings where the first element in the array
     * is the most recent history item.
     * @return a String array of the history items where the most recent history item
     * is at index [0].
     */
    public String[] getHistory();


    /**
     * Saves the history into a Eclipse session.
     */
    public void saveHistory();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jul-04	4888/3	tom	VBM:2004070605 Created DefaultHistoryManager

 ===========================================================================
*/
