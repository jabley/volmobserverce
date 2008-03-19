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
package com.volantis.mcs.protocols.vdxml.menu;

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.renderer.AbstractStyledTextMenuItemRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.AbstractStyledTextMenuItemRendererTestAbstract;
import com.volantis.mcs.protocols.menu.shared.renderer.TestDeprecatedSpanOutput;
import com.volantis.mcs.policies.variants.text.TextEncoding;

/**
 * A test case for {@link VDXMLTextMenuItemRenderer}.
 */
public class VDXMLTextMenuItemRendererTestCase
        extends AbstractStyledTextMenuItemRendererTestAbstract {

    // Javadoc inherited.
    protected AbstractStyledTextMenuItemRenderer createRenderer() {

        return new VDXMLTextMenuItemRenderer(new TestDeprecatedSpanOutput());
    }

    // Javadoc inherited.
    protected String createExpectedContent(MenuItem item)
            throws Exception {

        final String shortcut = item.getShortcut().getText(TextEncoding.PLAIN);
        final String menuText = DOMUtilities.toString(((DOMOutputBuffer)
                                item.getLabel().getText().getText()).getRoot());

        return shortcut + " " + menuText;
    }

    /**
     * TODO: Remove this method when the whitespace bug below is fixed.
     */
    public void testRenderWithStyle() throws Exception {

        // Disabled because of bug in DOMOutputBuffer.addOutputBuffer().
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 ===========================================================================
*/
