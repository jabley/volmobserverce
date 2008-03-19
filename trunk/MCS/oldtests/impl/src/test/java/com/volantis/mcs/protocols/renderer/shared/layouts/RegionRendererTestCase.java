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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.context.OutputBufferStack;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.RegionContent;
import com.volantis.mcs.protocols.RendererTestProtocol;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;

/**
 * Tests rendering functionality of default region renderer.
 *
 * @todo Remove when code coverage indicates that unit tests have same coverage.
 */
public class RegionRendererTestCase
        extends AbstractFormatRendererTestAbstract {
    /**
     * Tests that attributes are passed through correctly.
     * @throws Exception if an error occurs
     */
    public void testAttributesPassed() throws Exception {
        RendererTestProtocol protocol = new RendererTestProtocol(pageContext);
        pageContext.setProtocol(protocol);

        // Create a region
        Region region = new Region((CanvasLayout) layout);

        RegionInstance instance =
                (RegionInstance) dlContext.getFormatInstance(region,
                NDimensionalIndex.ZERO_DIMENSIONS);
        RegionContent content = new RegionContent() {
            public void render(FormatRendererContext ctx) {
                OutputBufferStack outputBufferStack = ctx.getOutputBufferStack();
                DOMOutputBuffer buffer = (DOMOutputBuffer)
                        outputBufferStack.getCurrentOutputBuffer();
                buffer.openElement(buffer.allocateElement("regionContent"));
                buffer.closeElement("regionContent");
            }
        };
        RegionContent content2 = new RegionContent() {
            public void render(FormatRendererContext ctx) {
                OutputBufferStack outputBufferStack = ctx.getOutputBufferStack();
                DOMOutputBuffer buffer = (DOMOutputBuffer)
                        outputBufferStack.getCurrentOutputBuffer();
                buffer.openElement(buffer.allocateElement("regionContent2"));
                buffer.closeElement("regionContent2");
            }
        };
        instance.addRegionContent(content);
        instance.addRegionContent(content2);

        //Render the format
        FormatRenderer renderer = new RegionRenderer();
        renderer.render(formatRendererContext, instance);

        // Expected output for attributes specified above
        String expected = "<regionContent/><regionContent2/>";
        compareStringToBuffer(expected, pageContext.getCurrentOutputBuffer());
    }

    /**
     * Test that blank lines added to a region are discarded.
     *
     * @throws Exception
     */
    public void testEmptyBuffersSkipped() throws Exception {

        RendererTestProtocol protocol = new RendererTestProtocol(pageContext);
        pageContext.setProtocol(protocol);

        // Create a region
        Region region = new Region((CanvasLayout) layout);

        RegionInstance instance =
                (RegionInstance) dlContext.getFormatInstance(region,
                NDimensionalIndex.ZERO_DIMENSIONS);

        RegionContent content = new RegionContent() {
            public void render(FormatRendererContext ctx) {
                OutputBufferStack outputBufferStack = ctx.getOutputBufferStack();
                DOMOutputBuffer buffer = (DOMOutputBuffer)
                        outputBufferStack.getCurrentOutputBuffer();
                buffer.openElement(buffer.allocateElement("regionContent"));
                buffer.closeElement("regionContent");
            }
        };

        instance.getCurrentBuffer().writeText("   \n");
        instance.addRegionContent(content);
        instance.getCurrentBuffer().writeText("   \n");

        //Render the format
        FormatRenderer renderer = new RegionRenderer();
        renderer.render(formatRendererContext, instance);

        // Expected output for attributes specified above
        String expected = "<regionContent/>";
        compareStringToBuffer(expected, pageContext.getCurrentOutputBuffer());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 03-Nov-05	10090/1	pabbott	VBM:2005103105 White space collapse problem

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/2	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 ===========================================================================
*/
