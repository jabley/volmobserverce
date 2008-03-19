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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/PAPIInternalsTestCase.java,v 1.2 2003/02/27 09:52:04 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Feb-03    Byron           VBM:2003022105 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.papi.AnchorAttributes;
import com.volantis.mcs.papi.AttrsAttributes;
import com.volantis.mcs.papi.BoldAttributes;
import com.volantis.mcs.papi.XFActionAttributes;
import com.volantis.mcs.papi.XFFormAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class tests the PAPIInternals methods.
 * @author Byron Wild
 */
public class PAPIInternalsTestCase extends TestCaseAbstract {

    public TestMarinerPageContext pageContext;
    private PolicyReferenceResolverMock referenceResolverMock;

    protected void setUp() throws Exception {
        super.setUp();

        pageContext = new TestMarinerPageContext();
        TestMarinerRequestContext requestContext =
                new TestMarinerRequestContext();
        
        pageContext.pushRequestContext(requestContext);

        referenceResolverMock = new PolicyReferenceResolverMock("referenceResolverMock",
                                expectations);

        pageContext.setPolicyReferenceResolver(referenceResolverMock);

        requestContext.setMarinerPageContext(pageContext);

        referenceResolverMock.expects.resolveQuotedScriptExpression(null)
                .returns(null).any();

//        String [] scripts = new String[] {
//            // Page
//            "Onload",
//            "OnUnload",
//
//            // Form
//            "OnSubmit",
//            "OnRest",
//
//            // Focus
//            final String ON_BLUR = "OnBlur";
//            final String ON_FOCUS = "OnFocus";
//
//            // Field
//            final String ON_CHANGE = "OnChange";
//            final String ON_SELECT = "OnSelect";
//        };
//
//        for (int i = 0; i < scripts.length; i++) {
//            String script = scripts[i];
//
//            referenceResolverMock.expects.resolveQuotedScriptExpression(script)
//                .returns(new LiteralScriptAssetReference(script)).any();
//        }
    }

    protected void tearDown() throws Exception {
        pageContext = null;
    }

    /**
     * Test the initialise page event attributes method. The events checked are:
     * - ON_LOAD
     * - ON_UNLOAD
     */
    public void testInitialisePageEventAttributes() throws Exception {
        com.volantis.mcs.papi.CanvasAttributes papiAttributes =
                new com.volantis.mcs.papi.CanvasAttributes();

        String ON_LOAD = "Onload";
        String ON_UNLOAD = "OnUnload";

        referenceResolverMock.expects.resolveQuotedScriptExpression(ON_LOAD)
            .returns(new LiteralScriptAssetReference(ON_LOAD)).any();

        referenceResolverMock.expects.resolveQuotedScriptExpression(ON_UNLOAD)
            .returns(new LiteralScriptAssetReference(ON_UNLOAD)).any();

        papiAttributes.setOnLoad(ON_LOAD);
        papiAttributes.setOnUnload(ON_UNLOAD);
        CanvasAttributes attributes = new CanvasAttributes();

        EventAttributes events = attributes.getEventAttributes(false);
        ScriptAssetReference event = events.getEvent(EventConstants.ON_LOAD);
        assertNull(event);
        PAPIInternals.initialisePageEventAttributes(pageContext,
                                                    papiAttributes,
                                                    attributes);
        event = events.getEvent(EventConstants.ON_LOAD);
        assertNotNull(event);
        assertEquals(ON_LOAD, event.getScript());

        event = events.getEvent(EventConstants.ON_UNLOAD);
        assertNotNull(event);
        assertEquals(ON_UNLOAD, event.getScript());

        // Other events should not have been set.
        event = events.getEvent(EventConstants.ON_CLICK);
        assertNull(event);
    }


    /**
     * Test the field event attributes initialization. The events checked are:
     * - ON_SUBMIT
     * - ON_RESET
     */
    public void testInitialiseFormEventAttributes() throws Exception {
        XFFormAttributes papiAttributes = new XFFormAttributes();
        final String ON_SUBMIT = "OnSubmit";
        final String ON_RESET = "OnRest";

        referenceResolverMock.expects.resolveQuotedScriptExpression(ON_SUBMIT)
            .returns(new LiteralScriptAssetReference(ON_SUBMIT)).any();

        referenceResolverMock.expects.resolveQuotedScriptExpression(ON_RESET)
            .returns(new LiteralScriptAssetReference(ON_RESET)).any();

        papiAttributes.setOnSubmit(ON_SUBMIT);
        papiAttributes.setOnReset(ON_RESET);

        CanvasAttributes attributes = new CanvasAttributes();

        EventAttributes events = attributes.getEventAttributes(false);
        ScriptAssetReference event = events.getEvent(EventConstants.ON_LOAD);
        assertNull(event);
        PAPIInternals.initialiseFormEventAttributes(pageContext,
                                                    papiAttributes,
                                                    attributes);
        event = events.getEvent(EventConstants.ON_SUBMIT);
        assertNotNull(event);
        assertEquals(ON_SUBMIT, event.getScript());

        event = events.getEvent(EventConstants.ON_RESET);
        assertNotNull(event);
        assertEquals(ON_RESET, event.getScript());

        // Other events should not have been set.
        event = events.getEvent(EventConstants.ON_CLICK);
        assertNull(event);
    }

    /**
     * Test the field event attributes initialization. The events checked are:
     * - ON_BLUR
     * - ON_FOCUS
     */
    public void testInitialiseFocusEventAttributes() throws Exception {
        final String ON_BLUR = "OnBlur";
        final String ON_FOCUS = "OnFocus";

        AnchorAttributes papiAttributes = new AnchorAttributes();
        doTestInitialiseFocusEventAttribute(papiAttributes,
                                             EventConstants.ON_BLUR,
                                             null);

        // Valid on change
        papiAttributes.setOnBlur(ON_BLUR);
        doTestInitialiseFocusEventAttribute(papiAttributes,
                                             EventConstants.ON_BLUR,
                                             ON_BLUR);

        // Valid on select
        papiAttributes.setOnFocus(ON_FOCUS);
        doTestInitialiseFocusEventAttribute(papiAttributes,
                                             EventConstants.ON_FOCUS,
                                             ON_FOCUS);

        // Other events should not have been set (choose one, say ON_CLICK).
        doTestInitialiseFocusEventAttribute(papiAttributes,
                                             EventConstants.ON_CLICK,
                                             null);
    }

    /**
     * Supporting method for <code>testInitialiseFieldEventAttributes</code>
     *
     * @param papiAttributes the papiAttributes
     * @param constant       the const value (from EventConstants)
     * @param expected       the expected result (may be null).
     * @see   #testInitialiseFieldEventAttributes
     */
    protected void doTestInitialiseFocusEventAttribute(
            AnchorAttributes papiAttributes,
            final int constant,
            String expected) {

        addScriptExpressionExpectations(expected);

        CanvasAttributes attributes = new CanvasAttributes();
        EventAttributes events = attributes.getEventAttributes(false);
        ScriptAssetReference event = events.getEvent(constant);
        assertNull(event);
        PAPIInternals.initialiseFocusEventAttributes(pageContext,
                                                    papiAttributes,
                                                    attributes);
        event = events.getEvent(constant);
        assertEquals(expected, event);
    }

    private void addScriptExpressionExpectations(String expected) {
        if (expected != null) {
            referenceResolverMock.expects
                    .resolveQuotedScriptExpression(expected)
                    .returns(new LiteralScriptAssetReference(expected))
                    .any();
        }
    }

    private void addScriptExpressionExpectation(String expected) {
        if (expected != null) {
            referenceResolverMock.expects
                    .resolveQuotedScriptExpression(expected)
                    .returns(new LiteralScriptAssetReference(expected));
        }
    }

    public void assertEquals(String expected, ScriptAssetReference event) {
        assertEquals(expected, event == null ? null : event.getScript());
    }

    /**
     * Test the field event attributes initialization. The events checked are:
     * - ON_CHANGE
     * - ON_SELECT
     */
    public void testInitialiseFieldEventAttributes() throws Exception {
        final String ON_CHANGE = "OnChange";
        final String ON_SELECT = "OnSelect";

        XFActionAttributes papiAttributes = new XFActionAttributes();
        doTestInitialiseFieldEventAttributes(papiAttributes,
                                             EventConstants.ON_CHANGE,
                                             null);

        // Valid on change
        papiAttributes.setOnChange(ON_CHANGE);
        doTestInitialiseFieldEventAttributes(papiAttributes,
                                             EventConstants.ON_CHANGE,
                                             ON_CHANGE);

        // Valid on select
        papiAttributes.setOnSelect(ON_SELECT);
        doTestInitialiseFieldEventAttributes(papiAttributes,
                                             EventConstants.ON_SELECT,
                                             ON_SELECT);

        // Other events should not have been set (choose one, say ON_CLICK).
        doTestInitialiseFieldEventAttributes(papiAttributes,
                                             EventConstants.ON_CLICK,
                                             null);
    }

    /**
     * Supporting method for <code>testInitialiseFieldEventAttributes</code>
     *
     * @param papiAttributes the papiAttributes
     * @param constant       the const value (from EventConstants)
     * @param expected       the expected result (may be null).
     * @see   #testInitialiseFieldEventAttributes
     */
    protected void doTestInitialiseFieldEventAttributes(
            XFActionAttributes papiAttributes,
            final int constant,
            String expected) {

        addScriptExpressionExpectations(expected);

        CanvasAttributes attributes = new CanvasAttributes();
        EventAttributes events = attributes.getEventAttributes(false);
        ScriptAssetReference event = events.getEvent(constant);
        assertNull(event);
        PAPIInternals.initialiseFieldEventAttributes(pageContext,
                                                    papiAttributes,
                                                    attributes);
        event = events.getEvent(constant);
        assertEquals(expected, event);
    }


    /**
     * Test the setting of the general event attributes defined in the array of
     * constant integers. Test that intitially all are null, and then for each
     * one, set the value and ensure that the result has been set. Also check
     * that another arbitrary constant is not set (e.g. ON_BLUR).
     */
    public void testInitialiseGeneralEventAttributes() throws Exception {
        BoldAttributes papiAttributes = new BoldAttributes();

        int constants[] = {
            EventConstants.ON_CLICK,        EventConstants.ON_DOUBLE_CLICK,
            EventConstants.ON_KEY_DOWN,     EventConstants.ON_KEY_PRESS,
            EventConstants.ON_KEY_UP,       EventConstants.ON_MOUSE_DOWN,
            EventConstants.ON_MOUSE_MOVE,   EventConstants.ON_MOUSE_OUT,
            EventConstants.ON_MOUSE_OVER,   EventConstants.ON_MOUSE_UP
        };
        // None should be set initially.
        for (int i = 0; i < constants.length; i++) {
            doTestInitialiseGeneralEventAttributes(papiAttributes,
                                                   constants[i],
                                                   null);
        }
        // Set each value one by one...
        for (int i = 0; i < constants.length; i++) {
            String expected = updateValue(papiAttributes, i);
            doTestInitialiseGeneralEventAttributes(papiAttributes,
                                                   constants[i],
                                                   expected);
        }
        // Arbitrary value (not set above) should be null.
        doTestInitialiseGeneralEventAttributes(papiAttributes,
                                               EventConstants.ON_BLUR,
                                               null);

    }

    /**
     * Helper method to set the papiAttributes given the event constant.
     *
     * @param  papiAttributes the papiAttributes
     * @param  constant       the event constant (from EventConstants)
     * @return                the value set, e.g. 'Event:1' where 1 is the
     *                        value of the constant.
     */
    protected String updateValue(AttrsAttributes papiAttributes, int constant) {
        String value = "Event:" + constant;
        switch(constant) {
            case EventConstants.ON_CLICK:
                papiAttributes.setOnClick(value);
                break;
            case EventConstants.ON_DOUBLE_CLICK:
                papiAttributes.setOnDoubleClick(value);
                break;
            case EventConstants.ON_KEY_DOWN:
                papiAttributes.setOnKeyDown(value);
                break;
            case EventConstants.ON_KEY_PRESS:
                papiAttributes.setOnKeyPress(value);
                break;
            case EventConstants.ON_KEY_UP:
                papiAttributes.setOnKeyUp(value);
                break;
            case EventConstants.ON_MOUSE_DOWN:
                papiAttributes.setOnMouseDown(value);
                break;
            case EventConstants.ON_MOUSE_MOVE:
                papiAttributes.setOnMouseMove(value);
                break;
            case EventConstants.ON_MOUSE_OUT:
                papiAttributes.setOnMouseOut(value);
                break;
            case EventConstants.ON_MOUSE_OVER:
                papiAttributes.setOnMouseOver(value);
                break;
            case EventConstants.ON_MOUSE_UP:
                papiAttributes.setOnMouseUp(value);
                break;
        }
        return value;
    }

    /**
     * Supporting method for <code>testInitialiseGeneralEventAttributes</code>
     *
     * @param papiAttributes the papiAttributes
     * @param constant       the const value (from EventConstants)
     * @param expected       the expected result (may be null).
     * @see   #testInitialiseGeneralEventAttributes
     */
    protected void doTestInitialiseGeneralEventAttributes(
            BoldAttributes papiAttributes,
            final int constant,
            String expected) {

        addScriptExpressionExpectations(expected);

        CanvasAttributes attributes = new CanvasAttributes();
        EventAttributes events = attributes.getEventAttributes(false);
        ScriptAssetReference event = events.getEvent(constant);
        assertNull(event);
        PAPIInternals.initialiseGeneralEventAttributes(pageContext,
                                                       papiAttributes,
                                                       attributes);
        event = events.getEvent(constant);
        assertEquals(expected, event);
    }


    /**
     * Test the canvas event attribute initialization. Events checked are:
     * - ON_TIMER
     * - ON_ENTER
     * - ON_ENTER_FORWARD
     * - ON_ENTER_BACKWARD
     */
    public void testInitialiseCanvasEventAttributes() throws Exception {
        final String ENTER = "OnEnter";
        final String FORWARD = "OnEnterForward";
        final String BACKWARD = "OnEnterBackward";
        final String ON_TIMER = "OnTimer";

        com.volantis.mcs.papi.CanvasAttributes papiAttributes =
                new com.volantis.mcs.papi.CanvasAttributes();

        doTestInitialiseCanvasEventAttributes(papiAttributes,
                                             EventConstants.ON_TIMER,
                                             null, true);

        // Valid on timer
        papiAttributes.setOnTimer(ON_TIMER);
        doTestInitialiseCanvasEventAttributes(papiAttributes,
                                             EventConstants.ON_TIMER,
                                             ON_TIMER, true);

        // IF on enter is set and backward and forward aren't then the latter
        // two values should take on the value of the on enter value.
        doEnterTest(papiAttributes, null,  null,     null,    null,      null);
        doEnterTest(papiAttributes, null,  null,     FORWARD, null,      FORWARD);
        doEnterTest(papiAttributes, null,  BACKWARD, null,    BACKWARD,  null);
        doEnterTest(papiAttributes, null,  BACKWARD, FORWARD, BACKWARD,  FORWARD);
        doEnterTest(papiAttributes, ENTER, null,     null,    ENTER,    ENTER);
        doEnterTest(papiAttributes, ENTER, null,     FORWARD, ENTER,    FORWARD);
        doEnterTest(papiAttributes, ENTER, BACKWARD, null,    BACKWARD, ENTER);
        doEnterTest(papiAttributes, ENTER, BACKWARD, FORWARD, BACKWARD, FORWARD);

        // Other events should not have been set (choose one, say ON_CLICK).
        papiAttributes = new com.volantis.mcs.papi.CanvasAttributes();
        doTestInitialiseCanvasEventAttributes(papiAttributes,
                                             EventConstants.ON_CLICK,
                                             null, true);
    }

    /**
     * Helper method to test the permutations of enter, forward and backward
     * variables where each can be null or contain a valid value.
     *
     * @param papiAttributes   the papiAttributes
     * @param onEnter          the value for the ON_ENTER event
     * @param onEnterBackward  the value for the ON_ENTER_BACKWARD event
     * @param onEnterForward   the value for the ON_ENTER_FORWARD event
     * @param expectedBackward the expected value for the backward event
     * @param expectedForward  the expected value for the forward event
     */
    protected void doEnterTest(
            com.volantis.mcs.papi.CanvasAttributes papiAttributes,
            String onEnter,
            String onEnterBackward,
            String onEnterForward,
            String expectedBackward,
            String expectedForward) {

        papiAttributes.setOnEnter(onEnter);
        papiAttributes.setOnEnterBackward(onEnterBackward);
        papiAttributes.setOnEnterForward(onEnterForward);

        addScriptExpressionExpectation(expectedBackward);
        addScriptExpressionExpectation(expectedForward);

        doTestInitialiseCanvasEventAttributes(papiAttributes,
                                             EventConstants.ON_ENTER_BACKWARD,
                                             expectedBackward, false);

        addScriptExpressionExpectation(expectedBackward);
        addScriptExpressionExpectation(expectedForward);

        doTestInitialiseCanvasEventAttributes(papiAttributes,
                                             EventConstants.ON_ENTER_FORWARD,
                                             expectedForward, false);
    }

    /**
     * Supporting method for <code>testInitialiseCanvasEventAttributes</code>
     *
     * @param papiAttributes the papiAttributes
     * @param constant       the const value (from EventConstants)
     * @param expected       the expected result (may be null).
     * @param addExpectation
     * @see   #testInitialiseCanvasEventAttributes
     */
    protected void doTestInitialiseCanvasEventAttributes(
            com.volantis.mcs.papi.CanvasAttributes papiAttributes,
            final int constant,
            String expected,
            boolean addExpectation) {

        if (addExpectation) {
            addScriptExpressionExpectations(expected);
        }

        CanvasAttributes attributes = new CanvasAttributes();
        EventAttributes events = attributes.getEventAttributes(false);
        ScriptAssetReference event = events.getEvent(constant);
        assertNull(event);
        PAPIInternals.initialiseCanvasEventAttributes(pageContext,
                                                       papiAttributes,
                                                       attributes);
        event = events.getEvent(constant);
        assertEquals(expected, event);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
