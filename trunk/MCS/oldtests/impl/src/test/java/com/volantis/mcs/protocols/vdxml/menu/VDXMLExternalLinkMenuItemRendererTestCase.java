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

import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteElementDetails;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuItem;
import com.volantis.mcs.protocols.menu.shared.model.ElementDetailsStub;
import com.volantis.mcs.protocols.menu.shared.model.MenuLabelStub;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRendererTestAbstract;
import com.volantis.mcs.protocols.menu.shared.renderer.TestSpanMenuItemRendererPair;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.dom.debug.DOMUtilities;

/**
 * Test case for {@link VDXMLExternalLinkMenuItemRenderer}.
 */
public class VDXMLExternalLinkMenuItemRendererTestCase
        extends MenuItemBracketingRendererTestAbstract {

    /**
     * Test the rendering of an outer external link.
     */
    public void testOuterLinkRendering() throws Exception {

        // Test the rendering, providing a span renderer to render the menu
        // item style required for an outer link.
        testLinkRendering(new TestSpanMenuItemRendererPair());
    }

    /**
     * Test the rendering of an inner external link.
     */
    public void testInnerLinkRendering() throws Exception {

        // Test the rendering, providing null to indicate this is an inner link.
        testLinkRendering(null);
    }

    /**
     * Test link rendering.
     * <p>
     * This handles both inner and outer link rendering.
     *
     * @param spanMenuItemRenderer the renderer used to render the span for the
     *      style present on an outer external link, or null if this was an
     *      inner link.
     * @throws Exception
     */
    private void testLinkRendering(
            final TestSpanMenuItemRendererPair spanMenuItemRenderer)
            throws Exception {

        // Create the menu item to test.
        final ConcreteElementDetails elementDetails =
                createTestElementDetails();
        final ConcreteMenuItem item = new ConcreteMenuItem(
                elementDetails, new MenuLabelStub());
        item.setHref(new LiteralLinkAssetReference("the href"));
        item.setShortcut(new LiteralTextAssetReference("the shortcut"));

        // Create the renderer we are to test.
        final TestDeprecatedExternalLinkOutput externalLinkOutput =
                new TestDeprecatedExternalLinkOutput();
        final VDXMLExternalLinkMenuItemRenderer renderer =
                new VDXMLExternalLinkMenuItemRenderer(externalLinkOutput,
                        spanMenuItemRenderer);

        // Do a render.
        final String actualNormal = getRenderOutputAsString(renderer, item,
                                                "[test delegate]");
        //System.out.println(actualNormal);
        final String actualExternal = externalLinkOutput.getResult();
        //System.out.println(actualExternal);

        // Assemble the expected value which was rendered normally.
        String expectedNormal;
        // If we have a span renderer
        if (spanMenuItemRenderer != null) {
            // then it should render something.
            expectedNormal =
                "<span " +
//                    "class=\"the style class\"" + " " +
                ">" +
                    "[test delegate]" +
                "</span>";
            expectedNormal = DOMUtilities.provideDOMNormalizedString(
                    expectedNormal);
        } else {
            // otherwise it can't render the span.
            expectedNormal = "[test delegate]" ;
        }

        // Compare the expected value we calculated with the actual value
        // which was rendered.
        assertEquals("Normal not as expected",
                expectedNormal, actualNormal);

        // Assemble the expected value which was rendered externally.
        String expectedExternal =
                "<external-link " +
                        "href=\"" + item.getHref().getURL() + "\" " +
                        "shortcut=\"" + item.getShortcut().getText(TextEncoding.PLAIN) + "\" " +
                        // style attributes ignored here
                "/>";
        expectedExternal = DOMUtilities.provideDOMNormalizedString(
                expectedExternal);

        // Compare the expected value we calculated with the actual value
        // which was rendered.
        assertEquals("External not as expected",
                expectedExternal, actualExternal);
    }

    /**
     * Test rendering of an external link with no href. In this case we expect
     * nothing to be rendered, as a link without a href makes no sense.
     */
    public void testNullHref() throws Exception {

        // Create the menu item to test.
        ConcreteMenuItem item = new ConcreteMenuItem(
                new ElementDetailsStub(), new MenuLabelStub());
        item.setHref(new LiteralLinkAssetReference(null));
        item.setShortcut(new LiteralTextAssetReference("the shortcut"));

        // Check that the renderer did not open any output.
        checkUnopened(item);
    }

    /**
     * Test rendering of an external link with no shortcut. In this case we
     * expect nothing to be rendered, as a link without a shortcut makes no
     * sense as the user cannot action it.
     */
    public void testNullShortcut() throws Exception {

        // Create the menu item to test.
        ConcreteMenuItem item = new ConcreteMenuItem(
                new ElementDetailsStub(), new MenuLabelStub());
        item.setHref(new LiteralLinkAssetReference("the href"));
        item.setShortcut(null);

        // Check that the renderer did not open any output.
        checkUnopened(item);

    }

    /**
     * Helper method that attempts to render an external link for a menu item
     * and ensures that nothing was in fact rendered.
     *
     * @param item the item to be rendered.
     * @throws RendererException
     */
    private void checkUnopened(ConcreteMenuItem item)
            throws Exception {

        // Create the renderer we are to test.
        TestDeprecatedExternalLinkOutput externalLinkOutput =
                new TestDeprecatedExternalLinkOutput();
        VDXMLExternalLinkMenuItemRenderer renderer =
                new VDXMLExternalLinkMenuItemRenderer(externalLinkOutput,
                        new TestSpanMenuItemRendererPair());

        // Do a render.
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        boolean opened = renderer.open(buffer, item);

        // Ensure that nothing happened.
        assertFalse("", opened);

        // Double check that nothing was in the output.
        String actualExternal = externalLinkOutput.getResult();
        //System.out.println(actualExternal);
        String actualNormal = DOMUtilities.toString(buffer.getRoot());
        //System.out.println(actualNormal);

        // Assemble the expected value (empty string as nothing should happen.
        String expected = "";

        // Compare the expected value we calculated with the actual value
        // which was rendered.
        assertEquals("External not as expected", expected, actualExternal);
        assertEquals("Normal not as expected", expected, actualNormal);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 01-Oct-04	5635/2	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 ===========================================================================
*/
