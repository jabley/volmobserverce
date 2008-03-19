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
package com.volantis.mcs.xdime;

import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.dom.DOMFactoryMock;
import com.volantis.mcs.protocols.DOMOutputBufferMock;
import com.volantis.mcs.protocols.OutputBufferFactoryMock;
import com.volantis.mcs.protocols.ProtocolConfigurationImplMock;
import com.volantis.mcs.protocols.VolantisProtocolMock;
import com.volantis.mcs.xdime.xforms.XFGroupElementImplMock;
import com.volantis.mcs.xdime.xforms.XFormsControlElementMock;
import com.volantis.mcs.xdime.xforms.model.XFormBuilderMock;
import com.volantis.mcs.xdime.xforms.model.XFormModelMock;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Verify that {@link LabelStrategy} behaves as expected.
 */
public class LabelStrategyTestCase extends TestCaseAbstract {

    private static final String CONTENT = "Some content";
    private static final ElementType ELEMENT_TYPE =
            new ElementType("", "xform-control");

    // Test objects.
    private XFormsControlElementMock control;
    private XFGroupElementImplMock group;
    private XDIMEContextInternalMock context;
    private MarinerRequestContextMock requestContext;
    private MarinerPageContextMock pageContext;
    private VolantisProtocolMock protocol;
    private OutputBufferFactoryMock outputBufferFactory;
    private DOMFactoryMock domFactoryMock;
    private DOMOutputBufferMock outputBuffer;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        // create mock objects
        context = new XDIMEContextInternalMock("context", expectations);
        requestContext = new MarinerRequestContextMock(
                "requestContext", expectations);
        pageContext = new MarinerPageContextMock("pageContext", expectations);
        ProtocolConfigurationImplMock protocolConfig =
                new ProtocolConfigurationImplMock(
                        "protocolConfig", expectations);
        protocol = new VolantisProtocolMock(
                "protocolMock", expectations, protocolConfig);
        outputBufferFactory = new OutputBufferFactoryMock(
                "outputBufferFactory", expectations);
        domFactoryMock = new DOMFactoryMock("domFactoryMock", expectations);
    }

    /**
     * Verify that if the label element to which this strategy applies is not
     * a child of a group element, then the label text is not set on the
     * current group in the current model.
     */
    public void testStopHandlingDataWithNonGroupParent()
            throws XDIMEException, NoSuchFieldException {

        // set expectations
        setHandleDataExpectations();
        // From LabelStrategy#stopHandlingData
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.popOutputBuffer(outputBuffer);
        context.expects.getCurrentElement().returns(control).fixed(3);
        domFactoryMock.expects.createElement().returns(null);

        // create environment context mock
        context.expects.getInitialRequestContext().returns(
            requestContext).fixed(2);
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        environmentContextMock.expects.getCachingDirectives().returns(null).
            any();
        requestContext.expects.getEnvironmentContext().returns(
            environmentContextMock).any();

        // Create any mock objects whose constructors require expectations
        // to be set.
        control = new XFormsControlElementMock(
                "control", expectations, ELEMENT_TYPE, context);
        group = new XFGroupElementImplMock("group", expectations, context);
        outputBuffer = new DOMOutputBufferMock(
                "outputBuffer", expectations, domFactoryMock);

        // run test
        LabelStrategy labelStrategy = new LabelStrategy();
        labelStrategy.handleData(context);
        labelStrategy.stopHandlingData(context);
    }

    /**
     * Verify that if the label element to which this strategy applies is a
     * child of a group element, the label text should be used as the current
     * model's current group's label.
     */
    public void testStopHandlingDataWithGroupAsParent()
            throws XDIMEException, NoSuchFieldException {
        // create mock objects
        XFormBuilderMock builder = new XFormBuilderMock("builder", expectations);
        XFormModelMock model = new XFormModelMock("model", expectations);

        // set expectations
        // From LabelStrategy#stopHandlingData
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        context.expects.getCurrentElement().returns(null);
        context.expects.getXFormBuilder().returns(builder);
        builder.expects.getCurrentModel().returns(model);
        domFactoryMock.expects.createElement().returns(null);

        // create environment context mock
        context.expects.getInitialRequestContext().returns(requestContext);
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        environmentContextMock.expects.getCachingDirectives().returns(null).
            any();
        requestContext.expects.getEnvironmentContext().returns(
            environmentContextMock).any();

        // Create any mock objects whose constructors require expectations
        // to be set.
        group = new XFGroupElementImplMock("group", expectations, context);
        outputBuffer = new DOMOutputBufferMock(
                "outputBuffer", expectations, domFactoryMock);
        context.expects.getCurrentElement().returns(group);
        outputBuffer.expects.getPCDATAValue().returns(CONTENT);
        pageContext.expects.popOutputBuffer(outputBuffer);
        model.expects.setGroupLabel(CONTENT);
        outputBuffer.expects.clear();

        setHandleDataExpectations();

        // run test
        LabelStrategy labelStrategy = new LabelStrategy();
        labelStrategy.handleData(context);
        labelStrategy.stopHandlingData(context);
    }

    /**
     * If the label element to which this strategy applies is a child of a
     * group element, then the label text should be used as the current
     * model's current group's label. Verify that it handles the case where the
     * current model is null.
     */
    public void testStopHandlingDataWithGroupAsParentAndNullModel()
            throws XDIMEException, NoSuchFieldException {

        // create mock objects
        XFormBuilderMock builder = new XFormBuilderMock("builder", expectations);

        // set expectations
        setHandleDataExpectations();

        // From LabelStrategy#stopHandlingData
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.popOutputBuffer(outputBuffer);
        context.expects.getCurrentElement().returns(null);
        context.expects.getXFormBuilder().returns(builder);
        builder.expects.getCurrentModel().returns(null);
        domFactoryMock.expects.createElement().returns(null);

        // create environment context mock
        context.expects.getInitialRequestContext().returns(requestContext);
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        environmentContextMock.expects.getCachingDirectives().returns(null).
            any();
        requestContext.expects.getEnvironmentContext().returns(
            environmentContextMock).any();

        // Create any mock objects whose constructors require expectations
        // to be set.
        group = new XFGroupElementImplMock("group", expectations, context);
        outputBuffer = new DOMOutputBufferMock(
                "outputBuffer", expectations, domFactoryMock);
        context.expects.getCurrentElement().returns(group);
        // run test
        LabelStrategy labelStrategy = new LabelStrategy();
        labelStrategy.handleData(context);
        labelStrategy.stopHandlingData(context);
    }

    /**
     * Set the expectations that will be required if
     * {@link LabelStrategy#handleData} is called.
     */
    private void setHandleDataExpectations() {
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.getProtocol().returns(protocol);
        protocol.expects.getOutputBufferFactory().returns(outputBufferFactory);
        outputBufferFactory.expects.createOutputBuffer().returns(outputBuffer);
        pageContext.expects.pushOutputBuffer(outputBuffer);
    }
}
