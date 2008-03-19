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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.context.ListenerEventRegistryMock;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.papi.impl.CanvasElementImpl;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMOutputBufferMock;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.OutputBufferFactoryMock;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolMock;
import com.volantis.mcs.protocols.layouts.ContainerInstanceImplMock;
import com.volantis.mcs.protocols.layouts.RegionInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContextMock;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.VolantisMock;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngineMock;
import com.volantis.mcs.runtime.styling.CompiledStyleSheetCollection;
import com.volantis.mcs.themes.properties.MCSLayoutKeywords;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEAttributesImpl;
import com.volantis.mcs.xdime.XDIMEContextFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMock;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.engine.StylingEngineMock;
import com.volantis.styling.properties.StylePropertyImpl;
import com.volantis.styling.values.ImmutablePropertyValues;
import com.volantis.styling.values.ImmutablePropertyValuesMock;
import com.volantis.styling.values.MutablePropertyValuesMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;

/**
 * Test the body element is capable of running for a full request
 */
public class BodyElementTestCase extends TestCaseAbstract {

    private ImmutablePropertyValues immutablePropertyValues;

    private RuntimeProjectMock projectMock;

    private DeviceLayoutContext deviceLayoutContext;
    private MarinerPageContextMock pageContext;

    private void initialise() {
        projectMock = new RuntimeProjectMock("projectMock", expectations);

        projectMock.expects.getProjectThemesLocations()
                .returns(null).any();
        projectMock.expects.getDefaultProjectLayoutLocation()
                .returns(null).any();
    }

    /**
     * get a mock mariner request capable of handling body start and end events
     * @return
     */
    private MarinerRequestContext getMarinerRequestContext() {
        MarinerRequestContextMock requestContext =
                new MarinerRequestContextMock("requestContext", expectations);

        MarinerPageContext pageContext = getMarinerPageContext(requestContext);

        requestContext.expects.getMarinerPageContext().
                returns(pageContext).any();

        return requestContext;
    }

    /**
     * get a mock mariner page capable of handling body start and end events
     * @return
     */
    private MarinerPageContext getMarinerPageContext(
            MarinerRequestContext requestContext) {
        pageContext = new MarinerPageContextMock("pageContext", expectations);

        StylingEngine stylingEngine = getStylingEngine();

        final ContainerInstanceImplMock containerInstanceMock =
                new ContainerInstanceImplMock(
                        "containerInstanceMock", expectations,
                        NDimensionalIndex.ZERO_DIMENSIONS);

        VolantisProtocol protocol = getProtocol();

        RegionInstanceMock formatInstance =
                new RegionInstanceMock("formatInstance", expectations,
                        NDimensionalIndex.ZERO_DIMENSIONS);

        pageContext.expects.getStylingEngine().returns(stylingEngine).any();

        pageContext.expects.getCurrentContainerInstance()
                .returns(containerInstanceMock).any();

        pageContext.expects.getProtocol().returns(protocol).any();

        pageContext.expects.initialisedCanvas().returns(false);

        pageContext.expects.getRequestContext().returns(requestContext).any();

        pageContext.expects.getVolantisBean().returns(setupVolantis()).any();

        pageContext.fuzzy.pushDeviceLayoutContext(
                mockFactory.expectsInstanceOf(DeviceLayoutContext.class))
                .does(new MethodAction() {

                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        deviceLayoutContext = (DeviceLayoutContext)
                                event.getArgument(DeviceLayoutContext.class);

                        pageContext.expects.getDeviceLayoutContext()
                                .returns(deviceLayoutContext);
                        return null;
                    }
                });

        pageContext.fuzzy.getFormatInstance(
                mockFactory.expectsInstanceOf(Format.class),
                NDimensionalIndex.ZERO_DIMENSIONS).
                returns(formatInstance);

        pageContext.expects.pushContainerInstance(formatInstance);

        pageContext.fuzzy.initialise(Boolean.FALSE,
                Boolean.FALSE,
                null,
                null,
                mockFactory.expectsInstanceOf(CompiledStyleSheetCollection.class),
                null);

        pageContext.expects.pushCanvasType(CanvasElementImpl.CANVAS_TYPE_MAIN);

        pageContext.expects.updateFragmentationState();

        final DOMOutputBufferMock anonymousBufferMock =
                new DOMOutputBufferMock("anonymousBufferMock", expectations);

        // Buffer for anonymous region pushed explicitly.
        formatInstance.expects.getCurrentBuffer().returns(anonymousBufferMock);

        pageContext.expects.pushOutputBuffer(anonymousBufferMock);

        // Buffer for anonymous region popped explicitly. 
        formatInstance.expects.getCurrentBuffer().returns(anonymousBufferMock);

        pageContext.expects.popOutputBuffer(anonymousBufferMock);

        //pageContext.expects.popCanvasType().returns(null);

        pageContext.expects.popDeviceLayoutContext().returns(null);

        pageContext.expects.popContainerInstance(formatInstance);

        pageContext.expects.getCurrentProject().returns(projectMock).any();
//        pageContext.expects.getProjectLoader().returns(projectLoader).fixed(2);

//        projectLoader.expects.loadProject().returns(project);

//        projectLoader.expects.unloadProject();

        pageContext.expects.endPhase1BeginPhase2();

        ListenerEventRegistryMock listenerEventRegistryMock =
                new ListenerEventRegistryMock("listenerEventRegistry",
                        expectations);

        listenerEventRegistryMock.expects.complete();

        pageContext.expects.getListenerEventRegistry().returns(
                listenerEventRegistryMock);
        
        pageContext.expects.getMediaAgent(false).returns(null);

        return pageContext;
    }

    /**
     * get a format renderer which can provide a format styling engine
     * @return
     */
    public FormatRendererContext getFormatRendererContext() {
        FormatRendererContextMock formatRendererContext =
                new FormatRendererContextMock("formatRendererContext",
                        expectations);

        FormatStylingEngineMock formatStylingEngine = new
                FormatStylingEngineMock("formatStylingEngine", expectations);

        formatRendererContext.expects.getFormatStylingEngine().
                returns(formatStylingEngine).any();

        formatStylingEngine.expects.pushPropertyValues(immutablePropertyValues);

        formatStylingEngine.expects.popPropertyValues(immutablePropertyValues);

        return formatRendererContext;
    }

    /**
     * setup volantis
     * @return
     */
    private Volantis setupVolantis() {
        return new VolantisMock("volantis", expectations);
    }

    /**
     * get a volantis protocol capable of handling body start and end events.
     * this should see a call to open div and close div which elmulates the
     * opening and closing of the anonymous region
     * @return
     */
    private VolantisProtocolMock getProtocol() {

        ProtocolConfigurationMock protocolConfig =
                    new ProtocolConfigurationMock(
                        "protocolConfig", expectations);

        VolantisProtocolMock protocol = new VolantisProtocolMock(
                "protocolMock", expectations, protocolConfig);

        OutputBufferFactoryMock outputBufferFactory =
                new OutputBufferFactoryMock("outputBufferFactory", expectations);

        protocol.fuzzy.writeOpenDiv(
                mockFactory.expectsInstanceOf(DivAttributes.class));

        protocol.expects.getOutputBufferFactory().returns(outputBufferFactory);

        protocol.expects.setWriteHead(true);

        protocol.fuzzy.openCanvasPage(
                mockFactory.expectsInstanceOf(CanvasAttributes.class));

        protocol.fuzzy.writeCloseDiv(
                mockFactory.expectsInstanceOf(DivAttributes.class));

        protocol.fuzzy.closeCanvasPage(
                mockFactory.expectsInstanceOf(CanvasAttributes.class));
        
        protocol.expects.getWidgetModule().returns(null).any();

        return protocol;
    }

    /**
     * get a styling engine which can start and end the body element
     * @return
     */
    private StylingEngine getStylingEngine() {
        StylingEngineMock stylingEngine =
                new StylingEngineMock("stylingEngine", expectations);

        stylingEngine.fuzzy
                .startElement(
                        XDIMESchemata.XHTML2_NAMESPACE,
                        "body",
                        mockFactory.expectsInstanceOf(Attributes.class));

        stylingEngine.expects.getStyles().returns(getStyles());

        stylingEngine.expects.endElement(XDIMESchemata.XHTML2_NAMESPACE, "body");

        return stylingEngine;
    }

    /**
     * get styles object
     * @return
     */
    private Styles getStyles() {
        StylesMock styles = new StylesMock("styles", expectations);
        MutablePropertyValuesMock propertyValues =
                new MutablePropertyValuesMock("propertyValues", expectations);

        immutablePropertyValues =
                new ImmutablePropertyValuesMock("immutablePropertyValues",
                        expectations);

        styles.expects.getPropertyValues().returns(propertyValues).any();

        propertyValues.expects.createImmutablePropertyValues().
                returns(immutablePropertyValues);

        propertyValues.fuzzy.getComputedValue(
                mockFactory.expectsInstanceOf(StylePropertyImpl.class))
                .returns(MCSLayoutKeywords.CURRENT).any();

        return styles;
    }

    /**
     * get an xdime context
     * @param requestContext
     * @return
     * @throws XDIMEException
     */
    private XDIMEContextInternal getXdimeContext(
            MarinerRequestContext requestContext) throws XDIMEException {

        XDIMEContextInternal xdimeContext =
                (XDIMEContextInternal) XDIMEContextFactory.getDefaultInstance()
                    .createXDIMEContext();

        xdimeContext.setInitialRequestContext(requestContext);
        xdimeContext.pushElement(createHTMLElement(xdimeContext));

        return xdimeContext;
    }

    /**
     * get xdime attributes
     * @return
     */
    private XDIMEAttributes getXDIMEAttributes() {
        XDIMEAttributesImpl attributes =
                new XDIMEAttributesImpl(XHTML2Elements.BODY);

        return attributes;
    }

    /**
     * create an html element - the callOpenOnProtocol needs to be called so
     * that the elment will be set to use XDIME2 mode
     * @param context
     * @return
     * @throws XDIMEException
     */
    private HtmlElement createHTMLElement(XDIMEContextInternal context)
            throws XDIMEException {
        HtmlElement html = new HtmlElement(context);

        XDIMEResult result = html.callOpenOnProtocol(context, null);

        return html;
    }

    /**
     * test the body element can start and end with no themes or layouts
     * specified
     * @throws Exception
     */
    public void testBody() throws Exception {
        initialise();

        MarinerRequestContext marinerRequestContext =
                getMarinerRequestContext();

        XDIMEContextInternal xdimeContext =
                getXdimeContext(marinerRequestContext);

        BodyElement body = new BodyElement(xdimeContext);

        XDIMEAttributes xdimeAttributes = getXDIMEAttributes();

        body.exprElementStart(xdimeContext, xdimeAttributes);

        body.exprElementEnd(xdimeContext);

        return;
    }

}
