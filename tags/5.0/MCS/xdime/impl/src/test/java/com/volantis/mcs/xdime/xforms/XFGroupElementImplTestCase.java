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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.xdime.ElementOutputState;
import com.volantis.mcs.xdime.ElementOutputStateMock;
import com.volantis.mcs.xdime.FormattingResult;
import com.volantis.mcs.xdime.XDIMEAttributesImpl;
import com.volantis.mcs.xdime.XDIMEContextInternalMock;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.xforms.XFormElements;
import com.volantis.mcs.xdime.xforms.model.XFormBuilderMock;
import com.volantis.mcs.xdime.xforms.model.XFormModelMock;
import com.volantis.styling.StylesMock;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StylingEngineMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.system.SystemClock;

import junitx.util.PrivateAccessor;

/**
 * Verify that {@link XFGroupElementImpl} behaves as expected.
 * @todo later add more extensive test cases!
 */
public class XFGroupElementImplTestCase extends TestCaseAbstract {

    private XDIMEContextInternalMock context;
    private StylesMock styles;
    private XDIMEAttributesImpl attributes;
    private MarinerRequestContextMock requestContext;
    private MarinerPageContextMock pageContext;
    private XFormBuilderMock builder;
    private XFormModelMock model;
    private StylingEngineMock stylingEngine;
    ElementOutputStateMock outputState;
    ExpectationBuilder ordered;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        ordered = mockFactory.createOrderedBuilder();

        context =
            new XDIMEContextInternalMock("context", ordered);
        attributes = new XDIMEAttributesImpl(XFormElements.GROUP);
        builder = new XFormBuilderMock("builder", ordered);
        model = new XFormModelMock("model", ordered);

        styles = new StylesMock("styles", ordered);
        requestContext =
                new MarinerRequestContextMock("requestContext", ordered);
        pageContext = new MarinerPageContextMock("pageContext", ordered);
        stylingEngine = new StylingEngineMock("stylingEngine", ordered);
        outputState = new ElementOutputStateMock("outputState", ordered);
    }

    /**
     * Verify that calling {@link XFGroupElementImpl#exprElementStart} when
     * the group being started is active and is not nested results in
     * {@link XDIMEResult} returned by its parent being returned.
     *
     * @throws XDIMEException if there was a problem running the test
     * @throws NoSuchFieldException if there was a problem running the test
     */
    public void testExprElementStartWhenGroupIsActive() throws XDIMEException,
            NoSuchFieldException {

        doTestExprElementStart(true);
    }

    /**
     * Verify that calling {@link XFGroupElementImpl#exprElementStart} when the
     * group being started is inactive and not nested results in the
     * {@link XDIMEResult} returned by its parent being returned, but that the
     * {@link com.volantis.mcs.xdime.ElementOutputState} indicates that the
     * output markup should be suppressed.
     *
     * @throws XDIMEException if there was a problem running the test
     * @throws NoSuchFieldException if there was a problem running the test
     */
    public void testExprElementStartWhenGroupIsInactive()
            throws XDIMEException, NoSuchFieldException {

        doTestExprElementStart(false);
    }

    /**
     * Test {@link XFGroupElementImpl#exprElementStart}. It should always
     * result in the {@link XDIMEResult} returned by the parent method being
     * returned, but it may vary the {@link ElementOutputState} of the group
     * element.
     *
     * @throws XDIMEException if there was a problem running the test
     * @throws NoSuchFieldException if there was a problem running the test
     */
    public void doTestExprElementStart(boolean isActive)
            throws XDIMEException, NoSuchFieldException {

        // From new XFGroupElementImpl(context);
        context.expects.getCurrentElement().returns(null);
        context.expects.getInitialRequestContext().returns(requestContext);
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        final ResponseCachingDirectives cachingDirectives =
            new ResponseCachingDirectives(SystemClock.getDefaultInstance());
        cachingDirectives.enable();
        assertTrue(cachingDirectives.isEnabled());

        environmentContextMock.expects.getCachingDirectives().returns(
            cachingDirectives);
        requestContext.expects.getEnvironmentContext().returns(
            environmentContextMock);

        XFGroupElementImpl group = new XFGroupElementImpl(context);

        assertFalse(cachingDirectives.isEnabled());

        // Set expectations.
        // From StylableXDIMEElement#styleElementStart
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.getStylingEngine().returns(stylingEngine);
        stylingEngine.fuzzy.startElement(
                mockFactory.expectsInstanceOf(String.class),
                mockFactory.expectsInstanceOf(String.class),
                mockFactory.expectsInstanceOf(Attributes.class));
        stylingEngine.expects.getStyles().returns(styles);

        // From StylableXDIMEElement#exprElementStart
        outputState.expects.apply().returns(FormattingResult.PROCESS);

        // From XFGroupElementImpl#callOpenOnProtocol
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.getProtocol().returns(null);

        // From XFGroupElementImpl#exprElementStart
        context.expects.getXFormBuilder().returns(builder);
        builder.expects.registerGroup(attributes).returns(model);
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.getStylingEngine().returns(stylingEngine);
        stylingEngine.expects.getStyles().returns(styles);
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        model.expects.pushGroup(styles, pageContext);
        model.expects.isActive().returns(isActive);
        outputState.expects.setIsInactiveGroup(!isActive);
        model.expects.pushElementOutputState(outputState);

        // Run test.
        PrivateAccessor.setField(group, "state", outputState);
        XDIMEResult actualResult = group.exprElementStart(context, attributes);
        assertEquals(XDIMEResult.PROCESS_ELEMENT_BODY, actualResult);
    }

    /**
     * Verify that calling {@link XFGroupElementImpl#exprElementEnd} when the
     * model's state changes from active to inactive whilst processing this end
     * tag, results in the {@link XDIMEResult} returned by its parent being
     * returned, but that the {@link com.volantis.mcs.xdime.ElementOutputState}
     * of any nested groups are changed to indicate that the output markup
     * should now be suppressed.
     *
     * @throws XDIMEException if there was a problem running the test
     * @throws NoSuchFieldException if there was a problem running the test
     */
    public void testExprElementEndWhenGroupIsActiveBeforeAndNotAfter()
            throws XDIMEException, NoSuchFieldException {

        doTestExprElementEnd(true, true);
    }

    /**
     * Verify that calling {@link XFGroupElementImpl#exprElementEnd} when the
     * model's state changes from inactive to active whilst processing this end
     * tag, results in the {@link XDIMEResult} returned by its parent being
     * returned, but that the {@link com.volantis.mcs.xdime.ElementOutputState}
     * of any nested groups are changed to indicate that the output markup
     * should now not be suppressed.
     *
     * @throws XDIMEException if there was a problem running the test
     * @throws NoSuchFieldException if there was a problem running the test
     */
    public void testExprElementEndWhenGroupIsInactiveBeforeAndActiveAfter()
            throws XDIMEException, NoSuchFieldException {

        doTestExprElementEnd(false, true);
    }

    /**
     * Verify that calling {@link XFGroupElementImpl#exprElementEnd} when the
     * model's state remains active whilst processing this end tag, results in
     * the {@link XDIMEResult} returned by its parent being returned and that
     * the output state is reverted.
     *
     * @throws XDIMEException if there was a problem running the test
     * @throws NoSuchFieldException if there was a problem running the test
     */
    public void testExprElementEndWhenGroupRemainsActive()
            throws XDIMEException, NoSuchFieldException {

        doTestExprElementEnd(true, false);
    }

    /**
     * Verify that calling {@link XFGroupElementImpl#exprElementEnd} when the
     * model's state remains inactive whilst processing this end tag, results
     * in the {@link XDIMEResult} returned by its parent being returned and
     * that the output state is reverted.
     *
     * @throws XDIMEException if there was a problem running the test
     * @throws NoSuchFieldException if there was a problem running the test
     */
    public void testExprElementEndWhenGroupRemainsInactive()
            throws XDIMEException, NoSuchFieldException {

        doTestExprElementEnd(false, false);
    }

    /**
     * Test {@link XFGroupElementImpl#exprElementEnd}. It should always
     * result in the {@link XDIMEResult} returned by the parent method being
     * returned, but it should revert the {@link com.volantis.mcs.xdime.ElementOutputState}
     * update any nested group if the state of the group changes whilst
     * processing this end tag.
     *
     * @throws XDIMEException if there was a problem running the test
     */
    public void doTestExprElementEnd(boolean activeBefore,
                                     boolean stateChanged)
            throws XDIMEException, NoSuchFieldException {

        context.expects.getCurrentElement().returns(null);
        context.expects.getInitialRequestContext().returns(requestContext);
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        final ResponseCachingDirectives cachingDirectives =
            new ResponseCachingDirectives(SystemClock.getDefaultInstance());
        cachingDirectives.enable();
        assertTrue(cachingDirectives.isEnabled());

        environmentContextMock.expects.getCachingDirectives().returns(
            cachingDirectives);
        requestContext.expects.getEnvironmentContext().returns(
            environmentContextMock);

        XFGroupElementImpl group = new XFGroupElementImpl(context);

        assertFalse(cachingDirectives.isEnabled());

        // Set expectations.

        model.expects.isActive().returns(activeBefore);
        model.expects.popElementOutputState().returns(outputState);
        model.expects.popGroup();

        boolean activeAfter = stateChanged? !activeBefore: activeBefore;
        model.expects.isActive().returns(activeAfter);
        if (stateChanged) {
            boolean suppressed = activeBefore && !activeAfter;
            model.expects.updateAllGroups(suppressed);
        }

        // From StylableXDIMEElement#exprElementEnd
        outputState.expects.isSuppressing().returns(false);

        // From XFGroupElementImpl#callCloseOnProtocol
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.getProtocol().returns(null);

        // From StylableXDIMEElement#getStylingEngine
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.getStylingEngine().returns(stylingEngine);

        // From StyledStrategy#endElement
        stylingEngine.fuzzy.endElement(
                mockFactory.expectsInstanceOf(String.class),
                mockFactory.expectsInstanceOf(String.class));

        // From StylableXDIMEElement#exprElementEnd
        outputState.expects.revert();

        // Run test.
        XDIMEResult result = XDIMEResult.CONTINUE_PROCESSING;
        group.containingModel = model;
        PrivateAccessor.setField(group, "state", outputState);
        XDIMEResult actualResult = group.exprElementEnd(context);
        assertEquals(result, actualResult);
    }
}
