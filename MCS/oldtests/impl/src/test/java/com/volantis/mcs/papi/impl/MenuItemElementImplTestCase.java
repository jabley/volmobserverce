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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/MenuItemElementImplTestCase.java,v 1.2 2003/04/24 16:42:23 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Adrian          VBM:2003040903 - Added this class to test 
 *                              MenuItemElement 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.papi.MenuItemAttributes;
import com.volantis.mcs.papi.MockMenuModelBuilder;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverTestHelper;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import junitx.util.PrivateAccessor;

/**
 * This class tests the PAPIElement MenuItemElement.
 */
public class MenuItemElementImplTestCase extends AbstractElementImplTestAbstract {

    final String shortcut = "shortcut";
    final String href = "http://www.volantis.com";
    final String prompt = "<prompt>enter a value</prompt>";
    final String offcolor = "red";
    final String offimage = "stars";
    final String oncolor = "blue";
    final String onimage = "volantis";
    final String rolloverimage = "rollover";
    final String segment = "segment";
    final String target = "target";
    final String text = "some arbitrary text";
    final String title = "title";
    final String styleClass = "styleClass";
    final String id = "id";

    // javadoc inherited from superclass
    protected PAPIElement createTestablePAPIElement() {
        return new MenuItemElementImpl();
    }

    /**
     * Convenience method which populates the MenuItemAttributes with all the
     * required values.
     *
     * @return populated MenuItemAttributes.
     */
    private MenuItemAttributes createAttributes() {
        MenuItemAttributes menuItemAttrs = new MenuItemAttributes();

        menuItemAttrs.setShortcut(shortcut);
        menuItemAttrs.setHref(href);
        menuItemAttrs.setPrompt(prompt);
        menuItemAttrs.setOffColor(offcolor);
        menuItemAttrs.setOffImage(offimage);
        menuItemAttrs.setOnColor(oncolor);
        menuItemAttrs.setOnImage(onimage);
        menuItemAttrs.setRolloverImage(rolloverimage);
        menuItemAttrs.setSegment(segment);
        menuItemAttrs.setTarget(target);
        menuItemAttrs.setText(text);
        menuItemAttrs.setTitle(title);
        menuItemAttrs.setStyleClass(styleClass);
        menuItemAttrs.setId(id);

        return menuItemAttrs;
    }

    /**
     * Test the method elementStart.
     */
    public void testElementStart() throws Exception {
        MenuItemElementImpl element = (MenuItemElementImpl)createTestablePAPIElement();

        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        MockMenuModelBuilder menuModelBuilder = new MockMenuModelBuilder();
        pageContext.setMenuBuilder(menuModelBuilder);

        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                InternalDeviceTestHelper.createTestDevice());

        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);

        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);

        MenuElementImpl menuElement = new MenuElementImpl();
        Styles testStyles = StylesBuilder.getStyles(
                "mcs-menu-image-style: rollover");

        PrivateAccessor.setField(menuElement, "menuProperties",
                testStyles.getPropertyValues());
        pageContext.pushElement(menuElement);

        pageContext.setPolicyReferenceResolver(
                PolicyReferenceResolverTestHelper.getCommonExpectations(
                        expectations, mockFactory));

        MenuItemAttributes menuItemAttrs = createAttributes();

        assertEquals("Set title should not have been called",
                null,
                menuModelBuilder.getTitle());
        int result = element.elementStart(requestContext, menuItemAttrs);

        assertTrue("Unexpected value returned from MenuElement.  Should have" +
                "been PROCESS_ELEMENT_BODY.",
                result == PAPIElement.PROCESS_ELEMENT_BODY);
        final String msg = "PAPI and Protocol attribute values should match.";

        assertEquals(msg, shortcut, menuModelBuilder.getShortcut()
                .getText(TextEncoding.PLAIN));
        assertEquals(msg, href, menuModelBuilder.getHref().getURL());
        assertEquals(msg, prompt, menuModelBuilder.getPrompt()
                .getText(TextEncoding.PLAIN));
        assertEquals(msg, segment, menuModelBuilder.getSegment());
        assertEquals(msg, target, menuModelBuilder.getTarget());
        String textString = DOMUtilities.toString((Element)PrivateAccessor.
                getField(menuModelBuilder.getText(), "currentElement"));
        assertEquals(msg, text, textString);
        assertEquals(msg, title, menuModelBuilder.getTitle());
        assertNotNull(menuModelBuilder.getOffimage());
        assertNotNull(menuModelBuilder.getOnimage());
    }

    /**
     * Test that styles are added to the menu item's ElementDetails after
     * elementStart has been called.
     */
    public void testElementStartAddsStyles() throws Throwable {
        MenuItemElementImpl element =
                (MenuItemElementImpl)createTestablePAPIElement();

        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                InternalDeviceTestHelper.createTestDevice());

        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);

        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);

        pageContext.setPolicyReferenceResolver(
                PolicyReferenceResolverTestHelper.getCommonExpectations(
                        expectations, mockFactory));

        // need to set up parent element with properties otherwise there will
        // be a NPE in #setNormalAndOverImages
        MenuElementImpl menuElement = new MenuElementImpl();
        Styles testStyles = StylesBuilder.getStyles(
                "mcs-menu-image-style: rollover");
        PrivateAccessor.setField(menuElement, "menuProperties",
                testStyles.getPropertyValues());
        pageContext.pushElement(menuElement);

        MenuModelBuilder menuModelBuilder = pageContext.getMenuBuilder();
        //builder#startMenuItem will fail unless this has been called
        menuModelBuilder.startMenu();

        MenuItemAttributes menuItemAttrs = createAttributes();
        int result = element.elementStart(requestContext, menuItemAttrs);

        assertTrue("Unexpected value returned from MenuItemElement.  Should " +
                "have been PROCESS_ELEMENT_BODY.",
                result == PAPIElement.PROCESS_ELEMENT_BODY);

        final MenuItem menuItem = (MenuItem) PrivateAccessor.getField(
                menuModelBuilder, "currentEntity");

        ElementDetails elementDetails = menuItem.getElementDetails();
        assertNotNull("MenuItem ElementDetails should not be null",
                elementDetails);

        assertNotNull("MenuItem Styles should not be null after elementStart",
                elementDetails.getStyles());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 17-Oct-05	9840/3	pduffin	VBM:2005082215 Committing after fixing conflicts

 27-Sep-05	9609/1	ibush	VBM:2005082215 Move on/off color values for menu items

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 05-Aug-05	8859/4	emma	VBM:2005062006 Fixing merge conflicts

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 30-Jun-05	8888/2	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 13-Sep-04	5371/1	byron	VBM:2004083102 Title attribute on the <menuitem> element is being ignored

 18-Mar-04	3412/1	claire	VBM:2004031201 Early implementation of new menus in PAPI

 ===========================================================================
*/
