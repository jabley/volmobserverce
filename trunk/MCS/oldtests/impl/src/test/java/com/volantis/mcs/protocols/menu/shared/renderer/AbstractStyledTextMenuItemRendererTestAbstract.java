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

import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteElementDetails;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuItem;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuLabel;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuText;
import com.volantis.mcs.protocols.menu.shared.model.ElementDetailsStub;
import com.volantis.styling.StylesBuilder;

public abstract class AbstractStyledTextMenuItemRendererTestAbstract
        extends MenuItemComponentRendererTestAbstract {

    /**
     * This tests the render method to ensure the correct output is generated.
     */
    public void testRenderWithStyle() throws Exception {

        checkRender(true);
    }

    /**
     * This tests the render method to ensure the correct output is generated.
     */
    public void testRenderWithoutStyle() throws Exception {

        checkRender(false);
    }

    private void checkRender(boolean withStyle) throws Exception {

        ConcreteElementDetails elementDetails;
        if (withStyle) {
            elementDetails = new ConcreteElementDetails();
            elementDetails.setElementName("the tag name");
            elementDetails.setStyles(StylesBuilder.getEmptyStyles());
        } else {
            elementDetails = null;
        }

        String content = "test menu item";

        // Create the test DOM for the text buffer
        TestDOMOutputBuffer menuText = new TestDOMOutputBuffer();
        menuText.appendEncoded(content);

        // Create the test text, label, and then the menu item
        final ConcreteMenuText text = new ConcreteMenuText(elementDetails);
        text.setText(menuText);
        final ConcreteMenuLabel label = new ConcreteMenuLabel(
                new ElementDetailsStub(), text);
        final ConcreteMenuItem item = new ConcreteMenuItem(
                new ElementDetailsStub(), label);
        item.setHref(new LiteralLinkAssetReference("the href"));
        item.setShortcut(new LiteralTextAssetReference("the shortcut"));

        String expectedContent = createExpectedContent(item);
        String expected = null;
        if (withStyle) {
            expected =
                "<test-span " +
//                        "style-class=\"the style class\" " +
                        "tag-name=\"the tag name\"" +
                    ">" +
                    expectedContent +
                "</test-span>";
        } else {
            expected = expectedContent;
        }

        // Create test renderer object
        AbstractStyledTextMenuItemRenderer renderer = createRenderer();

        // Generate the rendered result string
        String actual = getRenderOutputAsString(renderer, item);
        assertNotNull("The actual string should exist", actual);

        // Compare state and expected
        assertEquals("Strings should match", expected, actual);
    }

    /**
     * Create the renderer subclass under test.
     *
     * @return the created renderer to test.
     */
    protected abstract AbstractStyledTextMenuItemRenderer createRenderer();

    /**
     * Create the expected content for this menu item.
     *
     * @param item the item to create the expected content for.
     * @return the expected content of the menu item.
     * @throws Exception
     */
    protected abstract String createExpectedContent(MenuItem item)
            throws Exception;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 ===========================================================================
*/
