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
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * This class provides a default means of rendering a vertical separator.
 */
public class DefaultVerticalSeparatorRenderer
        implements VerticalSeparatorRenderer {

    /**
     * Reference to an object that is capable of rendering a linebreak based on
     * protocol "knowledge"
     */
    private final DeprecatedLineBreakOutput lineBreakOutput;

    /**
     * Initialise a new instance of this separator renderer.
     */
    public DefaultVerticalSeparatorRenderer(
            DeprecatedLineBreakOutput lineBreakOutput) {

        this.lineBreakOutput = lineBreakOutput;
    }

    // JavaDoc inherited
    public void render(OutputBuffer buffer)
            throws RendererException {

        // Get the writer
        DOMOutputBuffer domBuffer = (DOMOutputBuffer) buffer;

        // Output the vertical separator
        try {
            lineBreakOutput.outputLineBreak(domBuffer,
                    new LineBreakAttributes());
        } catch (ProtocolException e) {
            throw new RendererException(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 26-Apr-04	3920/3	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 22-Apr-04	4004/1	claire	VBM:2004042204 Implemented remaining required WML renderers

 ===========================================================================
*/
