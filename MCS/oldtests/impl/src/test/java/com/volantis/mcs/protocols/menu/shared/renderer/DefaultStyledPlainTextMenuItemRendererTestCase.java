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

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;

/**
 * This tests the operation of the styled text renderer.  The output is
 * simplistic as the menu renderers are split into small isolated pieces of
 * functionality.  The plain text renderer is one of the most basic of these.
 */
public class DefaultStyledPlainTextMenuItemRendererTestCase
        extends AbstractStyledTextMenuItemRendererTestAbstract {

    protected AbstractStyledTextMenuItemRenderer createRenderer() {

        return new DefaultStyledPlainTextMenuItemRenderer(
                new TestDeprecatedSpanOutput());
    }

    protected String createExpectedContent(MenuItem item) throws Exception {

        return DOMUtilities.toString(((DOMOutputBuffer)
                item.getLabel().getText().getText()).getRoot());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 ===========================================================================
*/
