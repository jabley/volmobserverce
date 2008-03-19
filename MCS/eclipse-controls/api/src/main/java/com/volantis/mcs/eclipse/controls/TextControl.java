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
 * Interface for components that are text controls.
 */
public interface TextControl {
    /**
     * Get the value of the text control.
     * @return the text string of the control.
     */
    public String getValue();

    /**
     * Sets the value of the text control.
     * @param s string value for the text control.
     */
    public void setValue(String s);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Nov-03	1909/1	allan	VBM:2003102405 Done Image Component Wizard.

 24-Oct-03	1636/1	pcameron	VBM:2003102402 Added TextControl and ValidatedTextControl interfaces, and made TextButton conform to latter

 ===========================================================================
*/
