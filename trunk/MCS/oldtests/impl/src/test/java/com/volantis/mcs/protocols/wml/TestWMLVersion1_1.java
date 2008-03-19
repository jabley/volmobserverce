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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.TestDOMOutputBufferFactory;
import com.volantis.mcs.protocols.layouts.ContainerInstance;

/**
 * Implement the testable methods to create a testable Protocol.
 * <p>
 * We need to ensure that all implementations of these methods are the
 * same so the use of cut & paste here is recommended!
 * <p>
 * All the javadoc for these methods is defined in the interfaces.
 */
public class TestWMLVersion1_1 extends WMLVersion1_1
    implements WMLRootTestable {

    public TestWMLVersion1_1(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
        // Create the style emulation renderer.
        createStyleEmulationRenderer();
    }

    public void setPageHead(PageHead value) {
        this.pageHead = value;
    }

    public void setSupportsAccessKeyAttribute(boolean value) {
        this.supportsAccessKeyAttribute = value;
    }

    public void setPageBuffer(DOMOutputBuffer pageBuffer) {
        this.pageBuffer = pageBuffer;
    }

    public void setStyleSheetRenderer(StyleSheetRenderer renderer) {
        this.styleSheetRenderer = renderer;
    }

    private DOMOutputBuffer fakeCurrentContainerBuffer;

    public void setCurrentBuffer(ContainerInstance containerInstance,
            DOMOutputBuffer buffer) {
        this.fakeCurrentContainerBuffer = buffer;
    }

    protected DOMOutputBuffer getCurrentBuffer(
            ContainerInstance containerInstance) {
        return this.fakeCurrentContainerBuffer;
    }

    public OutputBufferFactory getOutputBufferFactory () {
        return new TestDOMOutputBufferFactory();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 ===========================================================================
*/
