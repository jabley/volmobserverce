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

import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.AssetReferenceException;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.dom.Element;
import com.volantis.shared.throwable.ExtendedRuntimeException;

/**
 * A test implementation of a separator appropriate for image based
 * menu group separator rendering.
 */
public class TestImageMenuGroupSeparator implements SeparatorRenderer {
    private ImageAssetReference assetReference;

    public TestImageMenuGroupSeparator(ImageAssetReference assetReference) {
        this.assetReference = assetReference;
    }

    public void render(OutputBuffer buffer)
            throws RendererException {
        DOMOutputBuffer buf = (DOMOutputBuffer)buffer;

        Element element = buf.openElement("<group-image-separator>");

        try {
            element.setAttribute("href", assetReference.getURL());
        } catch (AssetReferenceException e) {
            throw new ExtendedRuntimeException(e);
        }

        buf.closeElement("<group-image-separator>");
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

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
