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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.layouts.ContainerInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * A mock HTMLVersion3_2 protocol.
 */
public class TestHTMLVersion3_2 extends HTMLVersion3_2
        implements XHTMLBasicTestable {

    private Map containerInstanceBufferMap = new HashMap();

    /**
     * Construct a default instance.
     */
    public TestHTMLVersion3_2(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
        // Create the style emulation renderer.
        createStyleEmulationRenderer();
    }

    // javadoc inherited
    public void setPageHead(PageHead value) {
        this.pageHead = value;
    }

    // javadoc inherited
    public void setStyleSheetRenderer(StyleSheetRenderer renderer) {
        this.styleSheetRenderer = renderer;
    }

    // javadoc inherited
    public void setPageBuffer(DOMOutputBuffer pageBuffer) {
        this.pageBuffer = pageBuffer;
    }

    public void setCurrentBuffer(ContainerInstance containerInstance,
            DOMOutputBuffer buffer) {
        containerInstanceBufferMap.put(containerInstance, buffer);
    }

    protected DOMOutputBuffer getCurrentBuffer (
            ContainerInstance containerInstance) {

        DOMOutputBuffer buffer = (DOMOutputBuffer)
                containerInstanceBufferMap.get(containerInstance);
        return (null != buffer) ? buffer :
                super.getCurrentBuffer(containerInstance);
    }

    // javadoc inherited
    public int getMaxOptGroupNestingDepth() {
        return maxOptgroupDepth;
    }

    public void setSupportsFormatOptimization(boolean value) {
        supportsFormatOptimization = value;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Nov-04	6076/4	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 15-Jul-04	4869/2	geoff	VBM:2004062303 Implementation of theme style options: HTMLVersion 3.2 Family

 13-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 ===========================================================================
*/
