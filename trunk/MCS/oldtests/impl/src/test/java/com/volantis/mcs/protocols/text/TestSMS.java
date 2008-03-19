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
package com.volantis.mcs.protocols.text;

import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.protocols.*;
import com.volantis.mcs.protocols.layouts.ContainerInstance;

import java.io.Writer;
import java.io.StringWriter;

public class TestSMS extends SMS implements DOMProtocolTestable {

    SMS_DOMOutputBuffer outputBuffer;

    public TestSMS(ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {
        super(protocolSupportFactory, protocolConfiguration);
        outputBuffer = new SMS_DOMOutputBuffer();
        outputBuffer.initialise();
    }

    public void setPageHead(PageHead value) {
        this.pageHead = value;
    }

    public void setStyleSheetRenderer(StyleSheetRenderer renderer) {
        this.styleSheetRenderer = renderer;
    }

    public void setCurrentBuffer(ContainerInstance containerInstance, DOMOutputBuffer buffer) {
        if(buffer instanceof SMS_DOMOutputBuffer){
            this.outputBuffer = (SMS_DOMOutputBuffer)buffer;
        }
    }


    public void setPageBuffer(DOMOutputBuffer pageBuffer) {
        this.pageBuffer = pageBuffer;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 ===========================================================================
*/
