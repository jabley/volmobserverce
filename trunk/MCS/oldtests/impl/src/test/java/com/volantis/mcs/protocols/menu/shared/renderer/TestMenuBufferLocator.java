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

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.menu.model.MenuEntry;

import java.io.IOException;

/**
 * A simple testing menu item buffer locator which returns the same menu 
 * buffer for all menu items.
 * <p>
 * This creates and manages the underlying menu buffer and output buffer and 
 * allows easy access to any rendered output.
 */ 
public class TestMenuBufferLocator 
        implements MenuBufferLocator {
        
    private DOMOutputBuffer outputBuffer;
    
    private MenuBuffer buffer;

    /**
     * Construct an instance of this class.
     * <p>
     * This will create the underlying menu and output buffer that this test
     * class manage on your behalf. 
     */ 
    public TestMenuBufferLocator(SeparatorRenderer orientationRenderer) {

        outputBuffer = new TestDOMOutputBuffer();
        buffer = new ConcreteMenuBuffer(outputBuffer,
                                        orientationRenderer);
    }

    // Javadoc inherited.
    public MenuBuffer getMenuBuffer(MenuEntry entry) {
        return buffer;
    }
    
    /**
     * Return the underlying output buffer of the menu buffer this locator 
     * is managing.
     * 
     * @return the contained output buffer.
     */ 
    public DOMOutputBuffer getOutputBuffer() {
        return outputBuffer;
    }
    
    /**
     * Return the content of the output buffer of the menu buffer this locator
     * is managing as a string.
     * 
     * @return the contained output buffer's content as text.
     * @throws IOException
     */ 
    public String getOutput() throws IOException {
        return DOMUtilities.toString(getOutputBuffer().getRoot());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/2	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	3999/1	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 26-Apr-04	3920/4	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 ===========================================================================
*/
