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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.LineBreakAttributes;
import com.volantis.mcs.protocols.ProtocolException;

/**
 * An interface to allow the output of a line break from protocol line break
 * attributes.
 * <p>
 * <strong>
 * Note that this is deprecated because we will hopefully be replacing this
 * simple "existing protocol api callback" with a properly designed system for
 * rendering protocol elements outside of the protocol classes in future.
 * </strong>
 */
public interface DeprecatedLineBreakOutput {

    /**
     * Render the protocol line break element represented by the line break
     * attributes provided into the output buffer provided.
     *
     * @param dom the output buffer to render to
     * @param attributes the attributes of the line break to write out.
     */
    void outputLineBreak(DOMOutputBuffer dom, LineBreakAttributes attributes)
            throws ProtocolException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 ===========================================================================
*/
