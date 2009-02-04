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
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteElementDetails;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class MenuItemBracketingRendererTestAbstract
        extends TestCaseAbstract {

    /**
     * Convenience method to avoid code duplication in tests which extend
     * this abstract test class.
     * @return a fully populated ElementDetails instance for use in tests
     */
    public static ConcreteElementDetails createTestElementDetails(){
        ConcreteElementDetails elementDetails = new ConcreteElementDetails();
        elementDetails.setId("the id");
        elementDetails.setStyles(StylesBuilder.getInitialValueStyles());
        elementDetails.setElementName("the tag name");
        return elementDetails;
    }

    /**
     * Invoke the renderer using the given content and return the result as
     * a String.
     */
    public static String getRenderOutputAsString(MenuItemBracketingRenderer renderer,
                                                 MenuItem item,
                                                 String content)
            throws RendererException {

        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        if (renderer.open(buffer, item)) {

            buffer.writeText(content);

            renderer.close(buffer, item);
        }

        // Extract the output from the menu item rendering as a string.
        return DOMUtilities.toString(buffer.getRoot());
    }

    /**
     * Invoke the renderer using the item text as content and return the result
     * as a String.
     */
    public static String getRenderOutputAsString(MenuItemBracketingRenderer renderer,
                                                 MenuItem item)
            throws RendererException {

        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        if (renderer.open(buffer, item)) {

            buffer.transferContentsFrom(item.getLabel().getText().getText());

            renderer.close(buffer, item);
        }

        // Extract the output from the menu item rendering as a string.
        return DOMUtilities.toString(buffer.getRoot());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 09-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
