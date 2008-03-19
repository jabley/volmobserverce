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
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteElementDetails;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuItem;
import com.volantis.mcs.protocols.menu.shared.model.MenuLabelStub;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import junitx.util.PrivateAccessor;

/**
 * Test case for {@link DefaultAnchorMenuItemRenderer}.
 */ 
public class DefaultAnchorMenuItemRendererTestCase  
        extends MenuItemBracketingRendererTestAbstract {

    /**
     * Test that we can render an inner link, with all the attributes. 
     * <p> 
     * Inner links ignore any stylistic information provided.
     * 
     * @throws Exception
     */ 
    public void testInner() throws Exception {

        // Create the menu item to test.
        ConcreteElementDetails elementDetails = createTestElementDetails();
        ConcreteMenuItem item = new ConcreteMenuItem(
                elementDetails, new MenuLabelStub());
        item.setHref(new LiteralLinkAssetReference("the href"));
        item.setShortcut(new LiteralTextAssetReference("the shortcut"));
        item.setTitle("the title");
        
        // Create the renderer we are to test.
        MenuItemBracketingRenderer renderer =
                new DefaultAnchorMenuItemRenderer(
                        new TestDeprecatedAnchorOutput(), false, null);

        // Do a render.
        String actual = getRenderOutputAsString(renderer, item,
                                                "[test delegate]");
        //System.out.println(actual);
        
        // Assemble the expected value (rendered menu item).
        String expected =  
                "<test-anchor " +
                        "href=\"" + item.getHref().getURL() + "\" " +
                        "shortcut=\"" + item.getShortcut().getText(TextEncoding.PLAIN) + "\" " +
                        "title=\"" + item.getTitle() + "\" " +
                        // style attributes ignored here
                        "tag-name=\"a\"" + ">" + 
                    "[test delegate]" +
                "</test-anchor>";
        expected = DOMUtilities.provideDOMNormalizedString(expected);
        
        // Compare the expected value we calculated with the actual value 
        // which was rendered.
        assertEquals("Inner Link not as expected", expected, actual);
        
    }

    /**
     * Test that we can render an outer link.
     * <p>
     * Outer links respect the stylistic information from the item. 
     * 
     * @throws Exception
     */ 
    public void testOuter() throws Exception {

        // Create the menu item to test.
        ConcreteElementDetails elementDetails = createTestElementDetails();
        ConcreteMenuItem item = new ConcreteMenuItem(
                elementDetails, new MenuLabelStub());
        item.setHref(new LiteralLinkAssetReference("the href"));
        
        // Create the renderer we are to test.
        MenuItemBracketingRenderer renderer =
                new DefaultAnchorMenuItemRenderer(
                        new TestDeprecatedAnchorOutput(), true, null);

        // Do a render.
        String actual = getRenderOutputAsString(renderer, item,
                                                "[test delegate]");
        //System.out.println(actual);
        
        // Assemble the expected value (rendered menu item).
        String expected =  
                "<test-anchor " +
                        "href=\"" + item.getHref().getURL() + "\" " +
                        "id=\"the id\"" + " " + 
//                        "style-class=\"the style class\"" + " " +
                        "tag-name=\"the tag name\"" + ">" + 
                    "[test delegate]" +
                "</test-anchor>";
        expected = DOMUtilities.provideDOMNormalizedString(expected);
        
        // Compare the expected value we calculated with the actual value 
        // which was rendered.
        assertEquals("Outer Link not as expected", expected, actual);
        
    }
    
    /**
     * Test that we can render an inner link, with numeric emulation. 
     * 
     * @throws Exception
     */ 
    public void testEmulation() throws Exception {

        // Create the menu item to test.
        ConcreteElementDetails elementDetails = new ConcreteElementDetails();
        elementDetails.setElementName("the tag name");
        ConcreteMenuItem item = new ConcreteMenuItem(
                elementDetails, new MenuLabelStub());
        item.setHref(new LiteralLinkAssetReference("the href"));
        
        // Create the renderer we are to test.
        MenuItemBracketingRenderer renderer =
                new DefaultAnchorMenuItemRenderer(
                        new TestDeprecatedAnchorOutput(), false,
                        new TestNumericShortcutEmulationRenderer());

        // Do a render.
        String actual = getRenderOutputAsString(renderer, item,
                                                "[test delegate]");
        System.out.println(actual);
        
        // Assemble the expected value (rendered menu item).
        String expected =  
                "[emul-start]" +
                "<test-anchor " +
                        "href=\"" + item.getHref().getURL() + "\" " +
                        "shortcut=\"[emul-access-key]\"" + " " + 
                        "tag-name=\"a\"" + ">" +
                    "[emul-prefix]" +
                    "[test delegate]" +
                "</test-anchor>" +
                "[emul-end]";
        // todo: extend dom utilities to handle content without root element.
        //expected = DOMUtilities.provideDOMNormalizedString(expected);
        System.out.println(expected);
        
        // Compare the expected value we calculated with the actual value 
        // which was rendered.
        assertEquals("Emulation not as expected", expected, actual);
        
    }

    /**
     * This tests the generation of target attributes on links if a segment
     * is set.
     */
    public void testSegments() throws Exception {
        final String elementTagName = "the tag name";

        // Create the menu item for the test
        ConcreteElementDetails elementDetails = new ConcreteElementDetails();
        elementDetails.setElementName(elementTagName);
        elementDetails.setStyles(StylesBuilder.getEmptyStyles());
        ConcreteMenuItem item = new ConcreteMenuItem(
                elementDetails, new MenuLabelStub());
        item.setHref(new LiteralLinkAssetReference("the href"));
        item.setSegment("anotherframe");

        // Create the renderer to test
        MenuItemBracketingRenderer renderer =
                new DefaultAnchorMenuItemRenderer(
                        new TestDeprecatedAnchorOutput(), true, null);

        // Render the anchor
        String actual = getRenderOutputAsString(renderer, item,
                                                "[test delegate]");
        // Assemble the expected value
        String expected =
                "<test-anchor " +
                        "href=\"" + item.getHref().getURL() + "\" " +
                        "tag-name=\"" + elementTagName + "\" " +
                        "target=\"" + item.getSegment() + "\"" +
                        ">" +
                    "[test delegate]" +
                "</test-anchor>";
        expected = DOMUtilities.provideDOMNormalizedString(expected);

        // Compare the expected value we calculated with the actual value
        // which was rendered.
        assertEquals("Emulation not as expected", expected, actual);
    }

    /**
     * Verify that styles in ElementDetails are propagated through to the
     * generated MCS Attributes.
     * <p/>
     * It does not make sense to check the actual rendered output in this test,
     * as that depends on the underlying DeprecatedXXOutput class that was
     * used.
     *
     * @throws Exception if there was a problem rendering the menu item
     */
    public void testMCSAttributesAnnotatedWithStyles() throws Exception {

        // Create the menu item to test.
        ConcreteElementDetails elementDetails = createTestElementDetails();
        ConcreteMenuItem item =
                new ConcreteMenuItem(elementDetails, new MenuLabelStub());
        item.setHref(new LiteralLinkAssetReference("http://www.bbc.co.uk"));

        // Create the renderer we are to test.
        DefaultAnchorMenuItemRenderer renderer =
                new DefaultAnchorMenuItemRenderer(
                        new TestDeprecatedAnchorOutput(), true, null);

        // Render.
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        renderer.open(buffer, item);

        AnchorAttributes attributes = (AnchorAttributes)PrivateAccessor.
                getField(renderer, "attributes");
        assertNotNull("MCS Attributes should not be null", attributes);
        Styles styles = attributes.getStyles();

        assertNotNull("The Styles on the MCS Attributes must not be null",
                styles);
        assertEquals("The styles on the MCS Attributes must be the same " +
                "as the styles on the ElementDetails",
                elementDetails.getStyles(), styles);
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

 30-Jun-05	8888/3	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Oct-04	5980/1	claire	VBM:2004100104 mergevbm: Segment attribute rendered on menu items

 26-Oct-04	5966/1	claire	VBM:2004100104 Segment attribute rendered on menu items

 17-May-04	4440/1	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 ===========================================================================
*/
