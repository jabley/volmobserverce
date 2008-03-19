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

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuText;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuText;
import com.volantis.mcs.protocols.menu.shared.model.ElementDetailsStub;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;

public abstract class MenuItemComponentRendererTestAbstract
        extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    public static String getRenderOutputAsString(MenuItemComponentRenderer renderer,
                                                 MenuItem item)
            throws RendererException, IOException {

        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        renderer.render(buffer, item);

        // Extract the output from the menu item rendering as a string.
        return DOMUtilities.toString(buffer.getRoot());
    }

    protected MenuText createMenuText() {
        ConcreteMenuText text = new ConcreteMenuText(new ElementDetailsStub());
        DOMOutputBuffer textBuffer = new TestDOMOutputBuffer();
        textBuffer.writeText("[text]");
        text.setText(textBuffer);
        return text;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
