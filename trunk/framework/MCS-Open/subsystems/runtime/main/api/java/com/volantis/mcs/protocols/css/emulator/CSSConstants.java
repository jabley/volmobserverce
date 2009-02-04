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
package com.volantis.mcs.protocols.css.emulator;

/**
 * This interface defines the constants used to specify which css properties
 * need emulating.
 */
public interface CSSConstants {

    /**
     * The CSS2 attribute for border spacing.
     */
    public static final String BORDER_SPACING = "border-spacing";

    /**
     * This constant defines the name of special place holder elements
     * which are added to the DOM tree to hold styles. These elements are
     * needed if we don't have a real element to attach the styles to.
     */
    public static final String STYLE_ELEMENT = "STYLE-EMULATION-ELEMENT";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 06-Sep-05	9413/1	schaloner	VBM:2005070406 Implemented before and after pseudo-element support

 22-Aug-05	9184/1	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1152/1	chrisw	VBM:2003070811 Emulate CSS2 border-spacing using cellspacing on table element

 ===========================================================================
*/
