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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dissection.links.ShardLinkGroupAttributes;
import com.volantis.mcs.dom.DOMFactoryMock;
import com.volantis.mcs.dom.DocumentMock;
import com.volantis.mcs.dom.ElementMock;
import com.volantis.mcs.dom.TextMock;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.protocols.dissection.ShardLinkMenu;
import com.volantis.mcs.protocols.dissection.ShardLinkMenuBracketingRenderer;
import com.volantis.mcs.protocols.menu.model.ElementDetailsMock;
import com.volantis.mcs.protocols.menu.model.MenuMock;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

public class MenuBracketingRendererTestCase extends TestCaseAbstract {

    DOMOutputBuffer buffer;
    ElementDetailsMock elementDetails;
    MenuMock menu;
    StylesMock stylesMock;
    DeprecatedDivOutputMock divOutput;
    ElementMock element;
    ElementMock slElement;
    DocumentMock document;
    TextMock text;
    ShardLinkGroupAttributes slAttributes;
    DOMFactoryMock domFactoryMock;

    public void setUp() throws Exception {
        super.setUp();

        // Create mocks.
        elementDetails = new ElementDetailsMock("elementDetails", expectations);
        menu = new MenuMock("menu", expectations);
        stylesMock = new StylesMock("styles", expectations);
        divOutput = new DeprecatedDivOutputMock("divOutput", expectations);

        element = new ElementMock("element", expectations);
        slElement = new ElementMock("slElement", expectations);
        document = new DocumentMock("document", expectations);
        text = new TextMock("text", expectations);
        slAttributes = new ShardLinkGroupAttributes();

        domFactoryMock = new DOMFactoryMock("domFactoryMock", expectations);
        domFactoryMock.expects.createElement().returns(element);
    }

    /**
     * Verify that styles in ElementDetails are propagated through to the
     * generated MCSAttributes when the render method is called.
     *
     * @param  renderer for which to check the MCSAttributes
     * @throws NoSuchFieldException if there is a problem accessing the
     * DivAttributes
     */
    public void checkAnnotatedAttributes(MenuBracketingRenderer renderer)
            throws NoSuchFieldException{

        DivAttributes attributes = (DivAttributes)PrivateAccessor.getField(
                renderer, "attributes");
        assertNotNull("MCS Attributes should not be null", attributes);
        Styles styles = attributes.getStyles();

        assertNotNull("The Styles on the MCS Attributes must not be null",
                styles);
        assertEquals("The styles on the MCS Attributes must be the same " +
                "as the styles on the ElementDetails",
                stylesMock, styles);
    }

    /**
     * Verify that styles in ElementDetails are propagated through to the
     * generated MCS Attributes when using a DefaultMenuBracketingRenderer.
     * <p/>
     * It does not make sense to check the actual rendered output in this test,
     * as that depends on the underlying DeprecatedXXOutput class that was
     * used.
     *
     * @throws Exception if there was a problem rendering the menu
     */
    public void testMCSAttributesAnnotatedWithStylesForDefaultRenderer()
            throws Exception {
        // Set expectations.
        menu.expects.getElementDetails().returns(elementDetails);
        elementDetails.expects.getElementName().returns("testElement");
        elementDetails.expects.getId().returns("id");
        elementDetails.expects.getStyles().returns(stylesMock);

        // create buffer - not mocked because there it throws exceptions
        // which are because of the DOMFactory static initialisation
        buffer = new DOMOutputBuffer(domFactoryMock);

        divOutput.fuzzy.openDiv(buffer,
                mockFactory.expectsInstanceOf(DivAttributes.class));

        // Render.
        DefaultMenuBracketingRenderer renderer =
                new DefaultMenuBracketingRenderer(divOutput);
        renderer.open(buffer, menu);
        checkAnnotatedAttributes(renderer);
    }

    /**
     * Verify that styles in ElementDetails are propagated through to the
     * generated MCS Attributes when using a ShardLinkMenuBracketingRenderer.
     * <p/>
     * It does not make sense to check the actual rendered output in this test,
     * as that depends on the underlying DeprecatedXXOutput class that was
     * used.
     *
     * @throws Exception if there was a problem rendering the menu
     */
    public void testMCSAttributesAnnotatedWithStylesForDelegateRenderer()
            throws Exception {
        // Set expectations.
        domFactoryMock.expects
                .createElement(DissectionConstants.SHARD_LINK_GROUP_ELEMENT)
                .returns(slElement);

        ShardLinkMenu slMenu = new ShardLinkMenu(elementDetails, slAttributes);
        element.expects.addTail(slElement);
        slElement.expects.setAnnotation(slAttributes);
        elementDetails.expects.getElementName().returns("testElement");
        elementDetails.expects.getId().returns("id");
        elementDetails.expects.getStyles().returns(stylesMock);

        buffer = new DOMOutputBuffer(domFactoryMock);

        divOutput.fuzzy.openDiv(buffer,
                mockFactory.expectsInstanceOf(DivAttributes.class));

        // Render.
        DefaultMenuBracketingRenderer defaultRenderer =
                new DefaultMenuBracketingRenderer(divOutput);
        ShardLinkMenuBracketingRenderer renderer =
                new ShardLinkMenuBracketingRenderer(defaultRenderer);

        renderer.open(buffer, slMenu);
        checkAnnotatedAttributes(defaultRenderer);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Oct-05	9825/3	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 30-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 ===========================================================================
*/
