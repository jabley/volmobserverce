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
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.papi.MenuItemGroupAttributes;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import junitx.util.PrivateAccessor;

public class MenuItemGroupElementImplTestCase
        extends AbstractElementImplTestAbstract {

    // javadoc inherited from superclass
    protected PAPIElement createTestablePAPIElement() {
        return new MenuItemGroupElementImpl();
    }

    /**
     * Test that styles are added to the ElementDetails for the menu item
     * group after elementStart has been called.
     */
    public void testElementStartAddsStyles() throws Throwable {
        MenuItemGroupElementImpl element =
                (MenuItemGroupElementImpl) createTestablePAPIElement();

        // configure MCS
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

        // pane setup required by #exprElementStart
        Pane testPane =
                new Pane(new CanvasLayout());
        final String pane = "testPane";
        testPane.setName(pane);
        pageContext.addPaneMapping(testPane);

        PaneInstance paneInstance = new TestPaneInstance();
        paneInstance.setFormat(testPane);
        TestDeviceLayoutContext deviceLayoutContext =
                new TestDeviceLayoutContext();
        deviceLayoutContext.setFormatInstance(testPane,
                NDimensionalIndex.ZERO_DIMENSIONS, paneInstance);
        pageContext.pushDeviceLayoutContext(deviceLayoutContext);
        pageContext.setFormatInstance(paneInstance);

        MenuModelBuilder menuModelBuilder = pageContext.getMenuBuilder();
        //builder#startMenuGroup will fail unless this has been called
        menuModelBuilder.startMenu();

        // set up protocol attributes
        MenuItemGroupAttributes menuItemGroupAttrs =
                new MenuItemGroupAttributes();
        menuItemGroupAttrs.setPane(pane);
        menuItemGroupAttrs.setStyleClass("styleClass");
        menuItemGroupAttrs.setId("id");

        int result = element.elementStart(requestContext, menuItemGroupAttrs);

        assertTrue("Unexpected value returned from MenuElement.  Should have" +
                "been PROCESS_ELEMENT_BODY.",
                result == PAPIElement.PROCESS_ELEMENT_BODY);

        final MenuItemGroup menuItemGroup =
                (MenuItemGroup) PrivateAccessor.getField(menuModelBuilder,
                        "currentEntity");

        assertNotNull("MenuGroup should not be null", menuItemGroup);

        ElementDetails elementDetails = menuItemGroup.getElementDetails();
        assertNotNull("MenuItemGroup ElementDetails should not be null",
                elementDetails);

        assertNotNull("MenuItemGroup Styles should not be null after " +
                "elementStart", elementDetails.getStyles());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 30-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 ===========================================================================
*/
