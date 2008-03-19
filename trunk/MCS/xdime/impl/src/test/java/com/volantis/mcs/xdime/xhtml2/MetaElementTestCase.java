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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.context.MetaData;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstanceMock;
import com.volantis.mcs.xdime.XDIMEContentHandler;
import com.volantis.mcs.xdime.XDIMEContextFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementHandler;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.xhtml2.meta.property.MetaPropertyHandlerFactory;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Tests for MetaInformationElement
 */
public class MetaElementTestCase extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    private XDIMEContentHandler handler = null;
    private Stack outputBufferStack;
    private Map metaDataMap;

    protected void setUp() throws Exception {

        super.setUp();
        outputBufferStack = new Stack();
        metaDataMap = new HashMap();

        final XDIMEContextInternal context = (XDIMEContextInternal)
            XDIMEContextFactory.getDefaultInstance().createXDIMEContext();
        final MarinerRequestContext requestContextMock =
            getMarinerRequestContextMock();

        context.setInitialRequestContext(requestContextMock);


        handler = new XDIMEContentHandler(
            null, context, XDIMEElementHandler.getDefaultInstance());
    }

    /**
     * Return an instance of the Protocol class HTMLVersion4.
     *
     * @return HTML 4 Protocol class
     */
    protected DOMProtocol createProtocol() {

        final ProtocolBuilder builder = new ProtocolBuilder();

        final DefaultDevice defaultDevice =
            new DefaultDevice("Test Device", new HashMap(), null);
        defaultDevice.setPolicyValue(
            DevicePolicyConstants.SUPPORTS_JAVASCRIPT, "true");
        InternalDevice device =
            INTERNAL_DEVICE_FACTORY.createInternalDevice(defaultDevice);

        return (DOMProtocol)builder.build(
            new ProtocolRegistry.HTMLVersion4_0Factory(), device);
    }

    private MarinerRequestContextMock getMarinerRequestContextMock() {

        final MarinerPageContextMock pageContextMock =
            new MarinerPageContextMock("pageContextMock", expectations);
        final MarinerRequestContextMock requestContextMock =
            new MarinerRequestContextMock("requestContextMock", expectations);

        final DOMProtocol protocol = createProtocol();
        protocol.setMarinerPageContext(pageContextMock);

        requestContextMock.expects.
            getMarinerPageContext().returns(pageContextMock).any();

        final StylingEngine stylingEngine = getStylingEngine();

        final PaneInstanceMock paneInstance = getPaneInstance();

        pageContextMock.expects.enteringXDIMECPElement().any();
        pageContextMock.expects.insideXDIMECPElement().returns(false).any();
        pageContextMock.expects.exitingXDIMECPElement().any();
        pageContextMock.expects.getStylingEngine().returns(stylingEngine).any();
        pageContextMock.expects.getCurrentContainerInstance().
            returns(paneInstance).any();
        pageContextMock.expects.initialisedCanvas().returns(true).any();
        pageContextMock.expects.getProtocol().returns(protocol).any();
        pageContextMock.fuzzy.pushOutputBuffer(
            mockFactory.expectsInstanceOf(DOMOutputBuffer.class)).does(
                new MethodAction() {
                    public Object perform(MethodActionEvent event)
                        throws Throwable {
                        Object buffer = event.getArgument(OutputBuffer.class);

                        outputBufferStack.push(buffer);

                        return null;
                    }
                }).any();
        pageContextMock.fuzzy.popOutputBuffer(
            mockFactory.expectsInstanceOf(DOMOutputBuffer.class)).does(
                new MethodAction() {
                    public Object perform(MethodActionEvent event)
                        throws Throwable {

                        return outputBufferStack.pop();
                    }
                }).any();
        pageContextMock.expects.getCurrentOutputBuffer().does(
            new MethodAction() {
                public Object perform(MethodActionEvent event) throws Throwable {
                    return outputBufferStack.peek();
                }
            }).any();

        final ExpressionFactory expressionFactory =
            ExpressionFactory.getDefaultInstance();
        final NamespacePrefixTracker prefixTracker =
            NamespaceFactory.getDefaultInstance().createPrefixTracker();
        prefixTracker.startPrefixMapping("meta-property",
            XDIMESchemata.XDIME2_MCS_NAMESPACE);
        final ExpressionContext expressionContext =
            expressionFactory.createExpressionContext(null, prefixTracker);

        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        environmentContextMock.expects.getExpressionContext()
                .returns(expressionContext).any();
        requestContextMock.expects.getEnvironmentContext()
                .returns(environmentContextMock);
        pageContextMock.fuzzy.getElementMetaData(mockFactory.expectsAny()).does(
            new MethodAction() {
                public Object perform(MethodActionEvent event) throws Throwable {
                    final String id = (String) event.getArguments()[0];
                    return getMetaData(id);
                }
            }).any();
        pageContextMock.expects.getPageMetaData().does(
            new MethodAction() {
                public Object perform(MethodActionEvent event) throws Throwable {
                    return getMetaData(null);
                }
            }).any();


        return requestContextMock;
    }

    private Object getMetaData(final String id) {
        MetaData metaData = (MetaData) metaDataMap.get(id);
        if (metaData == null) {
            metaData = new MetaData();
            metaDataMap.put(id, metaData);
        }
        return metaData;
    }

    private PaneInstanceMock getPaneInstance() {
        final PaneAttributes protocolPaneAttributes = new PaneAttributes();

        final PaneInstanceMock paneInstance = new PaneInstanceMock(
            "mockPaneInstance", expectations,
            NDimensionalIndex.ZERO_DIMENSIONS);
        paneInstance.expects.ignore().returns(false).any();
        // optional for XDIME2.
        paneInstance.expects.setStyleClass(null).any();
        paneInstance.expects.getAttributes().returns(protocolPaneAttributes).any();
        return paneInstance;
    }

    private StylingEngine getStylingEngine() {
        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();
        return stylingFactory.createStylingEngine();
    }

    /**
     * Generic open element method, which takes the name of the element to open.
     *
     * XHTML2 namespace is assumed for the element.
     *
     * @param name Name of PAPI element to open, must be valid PAPI element.
     */
    private void startElement(final String name)
            throws SAXException {

        startElement(
            XDIMESchemata.XHTML2_NAMESPACE, name, new String[]{}, new String[]{});
    }

    /**
     * Generic open element method, which takes the name and the namespace of
     * the element to open.
     *
     * @param name Name of PAPI element to open, must be valid PAPI element.
     */
    protected void startElement(final String namespace, final String name)
            throws SAXException {

        startElement(namespace, name, new String[]{}, new String[]{});
    }

    /**
     * Generic open element method, which takes the name of the element to open
     * and parameters.
     *
     * @param namespace namespace of the element
     * @param name   Name of element to open, must be valid element.
     * @param paramNames
     * @param paramValues
     */
    protected void startElement(final String namespace, final String name,
                                final String[] paramNames,
                                final String[] paramValues) throws SAXException {

        final AttributesImpl att = new AttributesImpl();

        for (int i = 0; i < paramNames.length; i++) {
            att.addAttribute(namespace,
                             paramNames[i], null, null,
                             paramValues[i]);
        }

        startElement(namespace, name, att);
    }

    /**
     * Start the XDIME element.
     *
     * @param namespace namespace of the element
     * @param elementName name of element to create and start.
     * @param attr
     * @throws org.xml.sax.SAXException
     */
    private void startElement(final String namespace, final String elementName,
                              final Attributes attr)
            throws SAXException {

        handler.startElement(namespace, elementName, null, attr);
    }

    /**
     * Adds character data to the element.
     *
     * @param characters the characters
     * @throws SAXException
     */
    private void characters(final String characters) throws SAXException {
        handler.characters(characters.toCharArray(), 0, characters.length());
    }

    /**
     * End the XHTML2 element.
     *
     * @param elementName
     * @throws org.xml.sax.SAXException
     */
    protected void endElement(final String elementName) throws SAXException {
        handler.endElement(XDIMESchemata.XHTML2_NAMESPACE, elementName, null);
    }

    public void testDOMType() throws Exception {
        startElement("html");
        startElement("head");
        startElement("title");
        endElement("title");
        startElement(XDIMESchemata.XHTML2_NAMESPACE, "meta",
            new String[]{"about", "property"},
            new String[]{"#fragment-link-id",
                         "meta-property:" +
                            MetaPropertyHandlerFactory.FRAGMENT_LINK_LABEL});
        characters("link text 1");
        startElement("h1");
        characters("link heading");
        endElement("h1");
        characters("link text 2");
        endElement("meta");
        startElement(XDIMESchemata.XHTML2_NAMESPACE, "meta",
            new String[]{"about", "property", "content"},
            new String[]{
                "#fragment-link-id",
                "meta-property:" +
                    MetaPropertyHandlerFactory.ENCLOSING_FRAGMENT_LINK_LABEL,
                "back link text"});
        endElement("meta");

        assertEquals(1, metaDataMap.size());

        final MetaData metaData = (MetaData) metaDataMap.get("fragment-link-id");

        DOMOutputBuffer value = (DOMOutputBuffer) metaData.getPropertyValue(
            MetaPropertyHandlerFactory.FRAGMENT_LINK_LABEL);
        assertNotNull(value);
        Text head = (Text) value.getRoot().getHead();
        assertEquals("link text 1",
            new String(head.getContents(), 0, head.getLength()));
        Element heading = (Element) head.getNext();
        Text headingText = (Text) heading.getHead();
        assertEquals("link heading",
            new String(headingText.getContents(), 0, headingText.getLength()));
        Text tail = (Text) heading.getNext();
        assertEquals("link text 2",
            new String(tail.getContents(), 0, tail.getLength()));
        assertNull(tail.getNext());

        value = (DOMOutputBuffer) metaData.getPropertyValue(
            MetaPropertyHandlerFactory.ENCLOSING_FRAGMENT_LINK_LABEL);
        assertNotNull(value);
        head = (Text) value.getRoot().getHead();
        assertEquals("back link text",
            new String(head.getContents(), 0, head.getLength()));
        assertNull(head.getNext());
    }
}
