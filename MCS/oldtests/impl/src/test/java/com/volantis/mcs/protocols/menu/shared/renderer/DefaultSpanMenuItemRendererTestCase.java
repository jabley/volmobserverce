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
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteElementDetails;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuItem;
import com.volantis.mcs.protocols.menu.shared.model.MenuLabelStub;
import com.volantis.styling.Styles;
import junitx.util.PrivateAccessor;

/**
 * Test case for {@link DefaultSpanMenuItemRenderer}.
 */ 
public class DefaultSpanMenuItemRendererTestCase 
        extends MenuItemBracketingRendererTestAbstract {

    /**
     * Test the basic functioning of the the class. 
     * <p>
     * This means that the it renders an "outer" as a span tag, with some 
     * style attributes and a contained delegate.
     * 
     * @throws Exception
     */ 
    public void testBasics() throws Exception {

        // Create the menu item to test.
        ConcreteElementDetails elementDetails = createTestElementDetails();
        ConcreteMenuItem item = new ConcreteMenuItem(
                elementDetails, new MenuLabelStub());
        
        // Create the renderer we are to test.
        DefaultSpanMenuItemRenderer renderer =
                new DefaultSpanMenuItemRenderer(
                        new TestDeprecatedSpanOutput());

        // Do a render.
        String actual = getRenderOutputAsString(renderer, item,
                                                "[test delegate]");
        //System.out.println(actual);
        
        // Assemble the expected value (rendered menu item).
        String expected =  
                "<test-span " +
                        "id=\"the id\"" + " " + 
//                        "style-class=\"the style class\"" + " " +
                        "tag-name=\"the tag name\"" + ">" + 
                    "[test delegate]" +
                "</test-span>";
        expected = DOMUtilities.provideDOMNormalizedString(expected);
        //System.out.println(expected);
        
        
        // Compare the expected value we calculated with the actual value 
        // which was rendered.
        assertEquals("Outer not as expected", expected, actual);
        
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
        ConcreteMenuItem item = new ConcreteMenuItem(
                elementDetails, new MenuLabelStub());

        // Create the renderer we are to test.
        DefaultSpanMenuItemRenderer renderer =
                new DefaultSpanMenuItemRenderer(
                        new TestDeprecatedSpanOutput());

        // Render.
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        renderer.open(buffer, item);
        SpanAttributes attributes = (SpanAttributes)PrivateAccessor.
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

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 30-Jun-05	8888/4	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 ===========================================================================
*/
