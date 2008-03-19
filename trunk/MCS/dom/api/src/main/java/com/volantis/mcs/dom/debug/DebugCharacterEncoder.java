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
package com.volantis.mcs.dom.debug;

import com.volantis.mcs.dom.output.AbstractCharacterEncoder;

import java.io.IOException;
import java.io.Writer;

/**
 * The simplest possible test implementation of {@link com.volantis.mcs.dom.output.CharacterEncoder}.
 * <p>
 * Currently this does no encoding at all.
 */ 
public class DebugCharacterEncoder extends AbstractCharacterEncoder {

    /**
     * Just return the character as provided.
     */ 
    public void encode(int c, Writer out)
            throws IOException {
        out.write(c);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8856/2	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 02-Jun-04	4575/6	geoff	VBM:2004051807 Minitel VDXML protocol support (reverse video, tests and some cleanup)

 28-May-04	4575/3	geoff	VBM:2004051807 Minitel VDXML protocol support (incomplete inline integration)

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
