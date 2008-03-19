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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * A very simple implementation of separator renderer.
 * <p>
 * All it does is render a custom string which should be easy to check for
 * in a unit test.  
 */ 
public class TestSimpleSeparatorRenderer implements SeparatorRenderer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The custom content of the separator to render.
     */ 
    private String content;

    /**
     * Construct an instance of this class.
     * 
     * @param content the custom content of the separator to render.
     */ 
    public TestSimpleSeparatorRenderer(String content) {
        this.content = content;
    }

    // Javadoc inherited.
    public void render(OutputBuffer buffer)
            throws RendererException {
        
        // render the custom content as the separator.
        buffer.writeText(content);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 28-Apr-04	4048/2	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
