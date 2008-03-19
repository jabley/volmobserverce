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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css;

/**
 * Defines a set of constants that are used in generating CSS or in CSS
 * emulation.
 */
public final class CSSConstants {
    /**
     * The style class that should be used for the shortcut prefix in a menu
     * item.
     */
    public static final String SHORTCUT_STYLE_CLASS = "VE-mcs-sc";

    /**
     * The pseudo element for shortcut definitions in an MCS theme. This name
     * must match the value from the LPDM schema.
     */
    public static final String SHORTCUT_PSEUDO_ELEMENT = "mcs-shortcut";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Aug-05	9377/1	schaloner	VBM:2005071102 Migrated mcs-shortcut-after to mcs-shortcut and after

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6090/1	matthew	VBM:2004102016 allow pseudo element selectors to be processed by SelectorVisitors

 23-Sep-04	5565/1	philws	VBM:2004092009 Render menu shortcut prefix and separator with styling

 ===========================================================================
*/
