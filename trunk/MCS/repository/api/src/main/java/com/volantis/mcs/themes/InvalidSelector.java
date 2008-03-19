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
package com.volantis.mcs.themes;

/**
 * Represents an invalid selector for use in the GUI.
 * 
 */

public interface InvalidSelector extends Subject {

    /**
     * Get the textual representation of he invalid selector.
     *
     * @return The text.
     */
    public String getText();


    /**
     * Set the invalid text.
     *
     * @param text The text to set
     */
    public void setText(String text);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9965/6	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9965/5	ianw	VBM:2005101811 Fixed up invalid selectors

 28-Oct-05	9965/3	ianw	VBM:2005101811 stabalise InvalidSelector code

 28-Oct-05	9965/1	ianw	VBM:2005101811 Fix file locations

 ===========================================================================
*/
