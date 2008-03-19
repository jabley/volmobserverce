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
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.TestDOMOutputBufferFactory;
import com.volantis.mcs.protocols.css.emulator.EmulatorRendererContext;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.protocols.wml.css.emulator.styles.WapTV5_WMLVersion1_3Style;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.StatefulPseudoClasses;

import java.util.HashMap;
import java.util.Map;

/**
 * Implement the testable methods to create a testable Protocol.
 * <p>
 * We need to ensure that all implementations of these methods are the
 * same so the use of cut & paste here is recommended!
 * <p>
 * All the javadoc for these methods is defined in the interfaces.
 */
public class TestWapTV5_WMLVersion1_3 extends WapTV5_WMLVersion1_3
        implements WMLRootTestable {

    WapTV5_WMLVersion1_3Style style = null;
    WapTV5_WMLVersion1_3Style formatStyle = null;
    WapTV5_WMLVersion1_3Style activeStyle = null;

    public TestWapTV5_WMLVersion1_3(ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {
        super(protocolSupportFactory, protocolConfiguration);
        // Create the style emulation renderer.
        createStyleEmulationRenderer();
    }

    private Map styleMap = new HashMap();

    public void setSupportsAccessKeyAttribute(boolean value) {
        supportsAccessKeyAttribute = value;
    }

    private DOMOutputBuffer fakeCurrentContainerBuffer;

    public void setCurrentBuffer(ContainerInstance containerInstance,
            DOMOutputBuffer buffer) {
        this.fakeCurrentContainerBuffer = buffer;
    }

    public DOMOutputBuffer getCurrentBuffer(
            ContainerInstance containerInstance) {
        return this.fakeCurrentContainerBuffer;
    }

    public void setPageHead(PageHead pageHead) {
        this.pageHead = pageHead;
    }

    public void setPageBuffer(DOMOutputBuffer pageBuffer) {
        this.pageBuffer = pageBuffer;
    }

    public void setStyleSheetRenderer(StyleSheetRenderer renderer) {
        this.styleSheetRenderer = renderer;
    }

     public void setStyle(WapTV5_WMLVersion1_3Style style) {
         this.style = style;
     }

    public WapTV5_WMLVersion1_3Style getStyle(MCSAttributes attributes) {
        if (style == null) {
            WapTV5_WMLVersion1_3Style style = (WapTV5_WMLVersion1_3Style)
                    styleMap.get(attributes);
            return (null == style) ? super.getStyle(attributes) : style;
        }
        return style;
    }

     public void setFormatStyle(WapTV5_WMLVersion1_3Style style) {
         formatStyle = style;
     }

     public void setActiveStyle(WapTV5_WMLVersion1_3Style style) {
         activeStyle = style;
     }

     public WapTV5_WMLVersion1_3Style getStyle(MCSAttributes attributes,
                           PseudoStyleEntity pseudoEntity) {
         if (pseudoEntity != null) {
             if (StatefulPseudoClasses.ACTIVE.equals(pseudoEntity)) {
                 return activeStyle;
             }
             if (StatefulPseudoClasses.FOCUS.equals(pseudoEntity)) {
                 return formatStyle;
             }
         }
         return super.getStyle(attributes, pseudoEntity);
     }

     public void setEmulatorRendererContext(EmulatorRendererContext emulator) {
         emulatorRendererContext = emulator;
     }

     public EmulatorRendererContext getEmulatorRendererContext() {
         if (emulatorRendererContext == null) {
             return super.getEmulatorRendererContext();
         }
         return emulatorRendererContext;
     }

     protected MarinerURL rewriteFormURL (MarinerURL url) {
         return url;
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
