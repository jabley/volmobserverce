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
package com.volantis.mcs.protocols;

import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.runtime.packagers.PackageBodyOutput;

import java.io.IOException;

/**
 * Local concrete implementation that can be used for testing.
 */
public class TestVolantisProtocol extends VolantisProtocol
        implements VolantisProtocolTestable {

    public TestVolantisProtocol(ProtocolConfiguration configuration) {
        super(configuration);
    }

    public OutputBufferFactory getOutputBufferFactory() {
        return null;
    }

    public void openCanvasPage(CanvasAttributes attributes)
        throws IOException {
    }

    public void openInclusionPage(CanvasAttributes attributes)
        throws IOException {
    }

    public void closeInclusionPage(CanvasAttributes attributes)
        throws IOException, ProtocolException {
    }

    public void openMontagePage(MontageAttributes attributes)
        throws IOException {
    }

    public void setPageHead(PageHead value) {
    }

    public void setStyleSheetRenderer(StyleSheetRenderer renderer) {
    }

    protected void writeCanvasContent(PackageBodyOutput output,
                                      CanvasAttributes attributes)
        throws IOException, ProtocolException {
    }

    protected void writeMontageContent(PackageBodyOutput output,
                                       MontageAttributes attributes)
        throws IOException, ProtocolException {
    }

    public String defaultMimeType() {
        return "";
    }

    public ValidationHelper getValidationHelper() {
        return null;
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
