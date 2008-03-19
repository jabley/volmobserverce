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
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.LabelAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.styling.StylesBuilder;

/**
 * Tests {@link com.volantis.mcs.papi.LabelElement}.
 */
public class LabelElementImplTestCase extends AbstractElementImplTestAbstract {

    // javadoc inherited
    protected PAPIElement createTestablePAPIElement() {
        return new LabelElementImpl();
    }

    /**
     * Extended command pattern interface used to define specific tests.
     *
     * @todo later this might want to go into the base PAPI test abstract
     */
    protected static interface TestCommand {
        /**
         * Used to execute the test itself.
         *
         * @param pageContext the page context needed by the tested method
         * @param element the element on which to execute the tested method
         */
        void execute(MarinerPageContext pageContext,
                     PAPIElement element) throws Exception;
    }

    /**
     * Indirectly tests {@link com.volantis.mcs.papi.impl.LabelElementImpl#exprElementStart}.
     */
    public void testElementStartImpl() throws Exception {
        doTest(new TestCommand() {
            protected final static String PANE = "pane";

            protected final static String TITLE = "title";

            protected final static String ON_CLICK = "onclick";

            /**
             * Tracks the output buffer originally set up in the MPC.
             */
            protected OutputBuffer originalBuffer;

            /**
             * Used by the test to finish the setup of the page context.
             *
             * @param pageContext
             *         the page context providing access to the various objects
             *         that need to be set up
             */
            protected void setUp(MarinerPageContext pageContext)
                    throws Exception {
                // Now set up an appropriate context within the Menu Model and
                // the page context
                MenuModelBuilder builder = pageContext.getMenuBuilder();
                VolantisProtocol protocol = pageContext.getProtocol();
                originalBuffer = protocol.getOutputBufferFactory().
                        createOutputBuffer();
                builder.startMenu();
                builder.setElementDetails("top", null,
                        StylesBuilder.getDeprecatedStyles());
                pageContext.pushOutputBuffer(originalBuffer);

                assertSame("current output buffer not as expected",
                           originalBuffer,
                           pageContext.getCurrentOutputBuffer());
            }

            /**
             * Returns the PAPI attributes required by the test.
             *
             * @return the initialized PAPI attributes, or null, as required by
             *         the test
             */
            protected PAPIAttributes getAttributes() throws Exception {
                BlockAttributes attributes = createAttributes();

                attributes.setPane(PANE);
                attributes.setStyleClass("style");
                attributes.setId("id");
                attributes.setOnClick(ON_CLICK);
                attributes.setTitle(TITLE);

                return attributes;
            }

            // javadoc inherited
            public void execute(MarinerPageContext pageContext,
                                PAPIElement element) throws Exception {
                setUp(pageContext);

                PolicyReferenceResolverMock referenceResolverMock =
                        (PolicyReferenceResolverMock)
                        pageContext.getPolicyReferenceResolver();

                referenceResolverMock.expects
                        .resolveQuotedScriptExpression("onclick")
                        .returns(new LiteralScriptAssetReference("onclick"))
                        .any();

                element.elementStart(pageContext.getRequestContext(),
                                     getAttributes());

                assertNotSame("a new output buffer should have been pushed",
                              originalBuffer,
                              pageContext.getCurrentOutputBuffer());

                Menu menu = pageContext.getMenuBuilder().endMenu();

                assertNotNull("the menu model should be complete",
                              menu);

                MenuLabel label = menu.getLabel();

                assertNotNull("the menu label should exist",
                              label);

                assertEquals("pane not as",
                             PANE,
                             label.getPane().getStem());

                assertSame("output buffers not same",
                           pageContext.getCurrentOutputBuffer(),
                           label.getText().getText());

                assertEquals("title not as",
                             TITLE,
                             label.getTitle());

                assertEquals("onclick not as",
                             ON_CLICK,
                             label.getEventHandler(EventType.ON_CLICK).getScript());

                ElementDetails menuElementDetails = menu.getElementDetails();
                // if element details are not null, styles should be non null
                assertNotNull("Menu ElementDetails should not be null",
                        menuElementDetails);
                assertNotNull("Menu Styles should not be null",
                        menuElementDetails.getStyles());

                ElementDetails elementDetails = label.getElementDetails();
                assertNotNull("Label ElementDetails should not be null",
                        elementDetails);
                assertNotNull("Styles should not be null after " +
                        "LabelElement#elementStart",
                        elementDetails.getStyles());
            }
        });
    }

    /**
     * Indirectly tests {@link com.volantis.mcs.papi.impl.LabelElementImpl#exprElementStart}.
     */
    public void testElementEndImpl() throws Exception {
        doTest(new TestCommand() {
            /**
             * Tracks the output buffer originally set up in the MPC.
             */
            protected OutputBuffer originalBuffer;

            /**
             * Used by the test to finish the setup of the page context.
             *
             * @param pageContext
             *         the page context providing access to the various objects
             *         that need to be set up
             */
            protected void setUp(MarinerPageContext pageContext)
                    throws Exception {
                // Now set up an appropriate context within the Menu Model and
                // the page context
                MenuModelBuilder builder = pageContext.getMenuBuilder();
                VolantisProtocol protocol = pageContext.getProtocol();
                originalBuffer = protocol.getOutputBufferFactory().
                        createOutputBuffer();
                builder.startMenu();

                pageContext.pushOutputBuffer(originalBuffer);

                assertSame("current output buffer not as expected",
                           originalBuffer,
                           pageContext.getCurrentOutputBuffer());
            }

            // javadoc inherited
            public void execute(MarinerPageContext pageContext,
                                PAPIElement element) throws Exception {
                setUp(pageContext);

                PAPIAttributes attributes = createAttributes();

                element.elementStart(pageContext.getRequestContext(),
                                     attributes);

                element.elementEnd(pageContext.getRequestContext(),
                                   attributes);

                assertSame("the new output buffer should have been popped",
                           originalBuffer,
                           pageContext.getCurrentOutputBuffer());
            }
        });
    }

    /**
     * Supporting method used to create the required type of attributes.
     *
     * @return the required type of attributes
     * @todo later this might want to go into the base PAPI test abstract (with return type and accessibility changes)
     */
    private BlockAttributes createAttributes() {
        return new LabelAttributes();
    }

    /**
     * Supporting method used to execute a given test command. Note that the
     * page context is set up with a request context, protocol, device theme
     * and device layout.
     *
     * @param command the test command to be executed
     * @todo later this might want to go into the base PAPI test abstract
     */
    protected void doTest(TestCommand command) throws Exception {
        // Set up the element for testing
        PAPIElement element = createTestablePAPIElement();

        // Set up the common test environment
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                InternalDeviceTestHelper.createTestDevice());

        // Set up the protocol (needed for OutputBuffer management)
        // The test implementation avoids the need for an initialized volantis
        // bean, which is jolly handy in this case
        pageContext.setProtocol(protocol);

        // Set the Layout (needed for FormatReference and panes)
        CanvasLayout layout = new CanvasLayout();

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(layout);

        pageContext.setDeviceLayout(runtimeDeviceLayout);

        final PolicyReferenceResolverMock referenceResolverMock =
                new PolicyReferenceResolverMock("referenceResolverMock",
                        expectations);
        pageContext.setPolicyReferenceResolver(referenceResolverMock);

        // Link everything together
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        pageContext.pushRequestContext(requestContext);
        protocol.setMarinerPageContext(pageContext);

        // Finally, test the required method
        command.execute(pageContext, element);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9298/3	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 30-Jun-05	8888/2	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 27-Jun-05	8878/3	emma	VBM:2005062306 rework

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 24-May-05	8123/7	ianw	VBM:2005050906 Fix accurev merge issues

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 02-Apr-04	3429/2	philws	VBM:2004031502 MenuLabelElement implementation

 ===========================================================================
*/
