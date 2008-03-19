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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 08-May-2003  Sumit       VBM:2003050606 - Created
 * 20-May-2003  Sumit       VBM:2003050606 - Added fixColumnNumber() method
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.utilities.sax;

import org.xml.sax.Locator;

/**
 * A class that corrects line number information for a sax parser
 */
public class DocumentFragmentLocator implements Locator {

    private Locator locator;

    /**
     * This method subtracts one from the row number to
     * compensate for the added &lt;fragment&gt; element
     */
    public DocumentFragmentLocator(Locator locator) {
        this.locator = locator;
    }

    public int getLineNumber() {
        return locator.getLineNumber();
    }

    public int getColumnNumber() {
        return fixColumnNumber(locator.getLineNumber(), locator.getColumnNumber());
    }

    public String getPublicId() {
        return locator.getPublicId();
    }

    public String getSystemId() {
        return locator.getSystemId();
    }

    /**
     * Set the column number to column number - length of root element
     * if the current line number is  1;
     */

    static int fixColumnNumber(int lineNumber, int columnNumber) {

        return columnNumber = (lineNumber == 1) ? columnNumber
                - DocumentFragmentStreamConstants.OPEN_ELEMENT.length
                : columnNumber;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
