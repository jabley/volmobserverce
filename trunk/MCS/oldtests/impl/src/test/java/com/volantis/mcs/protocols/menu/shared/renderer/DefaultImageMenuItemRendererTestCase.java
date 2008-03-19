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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.TestEmptyImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.TestNormalImageAssetReference;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteElementDetails;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuIcon;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuItem;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuLabel;
import com.volantis.mcs.protocols.menu.shared.model.ElementDetailsStub;
import com.volantis.styling.StylesBuilder;

/**
 * Test case for {@link DefaultImageMenuItemRenderer}.
 */ 
public class DefaultImageMenuItemRendererTestCase 
        extends MenuItemComponentRendererTestAbstract {

    /**
     * Create the image menu item renderer to test.
     *
     * @param imageOutput
     * @return
     */
    protected MenuItemComponentRenderer createImageMenuItemRenderer(
            DeprecatedImageOutput imageOutput,
            boolean provideAltText) {

        return new DefaultImageMenuItemRenderer(imageOutput, provideAltText);
    }

    /**
     * Test the rendering of the image part of a menu item, when the
     * item's image asset has a normal URL..
     * <p>
     * This means that the it renders a menu item as an image tag, with a src 
     * from the normal url, and some style attributes.
     *
     * @throws Exception
     */
    public void testNormal() throws Exception {

        // Create the menu item to test.
        ConcreteElementDetails elementDetails = new ConcreteElementDetails();
        elementDetails.setElementName("the tag name");
        elementDetails.setId("the id");
        elementDetails.setStyles(StylesBuilder.getEmptyStyles());
        ConcreteMenuIcon icon = new ConcreteMenuIcon(elementDetails);
        ImageAssetReference imageRef =
                new TestNormalImageAssetReference("the normal url");
        icon.setNormalURL(imageRef);
        ConcreteMenuItem item = new ConcreteMenuItem(
                new ElementDetailsStub(), new ConcreteMenuLabel(
                        new ElementDetailsStub(), createMenuText(),
                        icon));

        // Create the renderer we are to test.
        MenuItemComponentRenderer renderer = createImageMenuItemRenderer(
                new TestDeprecatedImageOutput(), false);

        // Do a render.
        String actual = getRenderOutputAsString(renderer, item);
        //System.out.println(actual);

        // Assemble the expected value (rendered menu item).
        String expected =
                "<test-image " +
                        "src=\"" + imageRef.getURL() + "\" " +
                        "id=\"the id\"" + " " + 
//                        "style-class=\"the style class\"" + " " +
                        "tag-name=\"the tag name\"" + 
                    "/>";
        expected = DOMUtilities.provideDOMNormalizedString(expected);
        //System.out.println(expected);

        // Compare the expected value we calculated with the actual value
        // which was rendered.
        assertEquals("Plain Image not as expected", expected, actual);

    }

    /**
     * Test the rendering of the image part of an XHTML menu item, when the
     * item's image asset has no normal URL or fallback text.
     *
     * @throws Exception
     */
    public void testNone() throws Exception {

        // Create the menu item to test.
        ConcreteMenuIcon icon = new ConcreteMenuIcon(new ElementDetailsStub());
        icon.setNormalURL(new TestEmptyImageAssetReference());
        ConcreteMenuItem item = new ConcreteMenuItem(
                new ElementDetailsStub(), new ConcreteMenuLabel(
                        new ElementDetailsStub(), createMenuText(),
                        icon));

        // Create the renderer we are to test.
        MenuItemComponentRenderer renderer = createImageMenuItemRenderer(
                new TestDeprecatedImageOutput(), true);

        // Do a render.
        String actual = getRenderOutputAsString(renderer, item);
        //System.out.println(actual);

        // Assemble the expected value (rendered menu item).
        String expected = "";
        expected = DOMUtilities.provideDOMNormalizedString(expected);
        //System.out.println(expected);

        // Compare the expected value we calculated with the actual value
        // which was rendered.
        assertEquals("Plain Image not as expected", expected, actual);

    }


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

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 ===========================================================================
*/
