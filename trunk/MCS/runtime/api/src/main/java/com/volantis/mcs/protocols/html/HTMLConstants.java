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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLConstants.java,v 1.2 2002/05/23 09:49:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Paul            VBM:2002042202 - Created to contain constants
 *                              shared between the different parts of this
 *                              package.
 * 23-May-02    Paul            VBM:2002042202 - Moved ssi constants into
 *                              parent interface.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.ProtocolConstants;

import java.util.HashSet;

/**
 * This interface contains HTML related constants.
 */
public interface HTMLConstants
  extends ProtocolConstants {

    /**
     * Tag names of all the elements that are inline in HTML 4.0
     */
    public static final String[] INLINE_ELEMENTS = new String[] {
        "A", "ABBR", "ACRONYM", "B", "BASEFONT", "BDO", "BIG", "BR", "CITE",
        "CODE", "DFN", "EM", "FONT", "I", "IMG", "INPUT", "KBD", "LABEL", "Q",
        "S", "SAMP", "SELECT", "SMALL", "SPAN", "STRIKE", "STRONG", "SUB",
        "SUP", "TEXTAREA", "TT", "U", "VAR"
    };

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
