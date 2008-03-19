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
package com.volantis.mcs.xdime.mcs;

import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstanceMock;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.VolantisMock;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactoryMock;
import com.volantis.mcs.runtime.policies.RuntimePolicyReferenceMock;
import com.volantis.mcs.runtime.policies.SelectedVariantMock;
import com.volantis.mcs.runtime.project.ProjectManagerMock;
import com.volantis.mcs.xdime.XDIMEContentHandler;
import com.volantis.mcs.xdime.XDIMEContextFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementHandler;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.shared.content.BinaryContentInput;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.schema.validator.SchemaValidator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.io.IOException;
import java.util.Stack;
import java.util.HashMap;

/**
 * Tests for ScriptElement
 */
public class ScriptElementTestCase extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    private XDIMEContentHandler handler = null;
    private Stack outputBufferStack;
    private MarinerPageContextMock pageContextMock;

    protected void setUp() throws Exception {

        super.setUp();
        outputBufferStack = new Stack();

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

        final DOMProtocol protocol = (DOMProtocol)builder.build(
            new ProtocolRegistry.HTMLVersion4_0Factory(), device);
        return protocol;
    }

    private MarinerRequestContextMock getMarinerRequestContextMock() {

        pageContextMock =
            new MarinerPageContextMock("pageContextMock", expectations);
        final MarinerRequestContextMock requestContextMock =
            new MarinerRequestContextMock("requestContextMock", expectations);
        final VolantisMock volantisBeanMock =
            new VolantisMock("volantisMock", expectations);
        final ProjectManagerMock projectManagerMock =
            new ProjectManagerMock("projectManagerMock", expectations);
        final RuntimeProjectMock runtimeProjectMock =
            new RuntimeProjectMock("runtimeProjectMock", expectations);
        final PolicyReferenceFactoryMock policyReferenceFactoryMock =
            new PolicyReferenceFactoryMock(
                "policyReferenceFactoryMock", expectations);
        final RuntimePolicyReferenceMock urlRuntimePolicyReferenceMock =
            new RuntimePolicyReferenceMock("urlRuntimePolicyReferenceMock",
                expectations);
        final RuntimePolicyReferenceMock noUrlRuntimePolicyReferenceMock =
            new RuntimePolicyReferenceMock("noUrlRuntimePolicyReferenceMock",
                expectations);
        final AssetResolverMock assetResolverMock =
            new AssetResolverMock("assetResolverMock", expectations);

        policyReferenceFactoryMock.fuzzy.createLazyNormalizedReference(
            runtimeProjectMock, null, mockFactory.expectsInstanceOf(String.class),
            PolicyType.SCRIPT).does(new MethodAction(){
            public Object perform(MethodActionEvent event) throws Throwable {
                final String url = (String) event.getArguments()[2];
                if (url.startsWith("URL")) {
                    return urlRuntimePolicyReferenceMock;
                } else {
                    return noUrlRuntimePolicyReferenceMock;
                }
            }
        }).any();

        final SelectedVariantMock selectedVariantMock =
            new SelectedVariantMock("selectedVariantMock", expectations);
        selectedVariantMock.expects.getOldObject().does(new MethodAction(){
            public Object perform(MethodActionEvent event) throws Throwable {
                return new ScriptAsset("name", "deviceName",
                    "programmingLanguage", "mimeType", "characterSet",
                    ScriptAsset.URL, "assetGroupName", "value");
            }}).any();

        assetResolverMock.expects.selectBestVariant(
            urlRuntimePolicyReferenceMock, null).returns(selectedVariantMock).any();

        assetResolverMock.expects.selectBestVariant(
            noUrlRuntimePolicyReferenceMock, null).returns(null).any();
        assetResolverMock.expects.computeURLAsString(selectedVariantMock).
            returns("url-for-the-script-variant").any();
        assetResolverMock.expects
            .rewriteURLWithPageURLRewriter("url-for-the-script-variant", PageURLType.SCRIPT)
            .returns("url-for-the-script-variant").any();

        final DOMProtocol protocol = createProtocol();

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
        pageContextMock.expects.getVolantisBean().returns(volantisBeanMock).any();
        pageContextMock.expects.getPolicyReferenceFactory().returns(
            policyReferenceFactoryMock).any();
        pageContextMock.fuzzy.getDevicePolicyValue(mockFactory.expectsAny()).
            returns(null).any();
        pageContextMock.expects.getPolicyReferenceResolver().returns(null).any();
        pageContextMock.expects.getAssetResolver().returns(assetResolverMock).
            any();
        pageContextMock.expects.getCurrentProject().returns(runtimeProjectMock).
            any();
        pageContextMock.expects.getBaseURL().returns(null).any();

        protocol.setMarinerPageContext(pageContextMock);
        protocol.initialiseCanvas();

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
                .returns(environmentContextMock).any();

        return requestContextMock;
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
            att.addAttribute("",
                             paramNames[i], paramNames[i], "CDATA",
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
     * @throws org.xml.sax.SAXException
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
        endElement(XDIMESchemata.XHTML2_NAMESPACE, elementName);
    }

    private void endElement(final String namespace,
                            final String elementName) throws SAXException {
        handler.endElement(namespace, elementName, null);
    }

    public void testPosition() throws SAXException {
        final DOMOutputBuffer outputBuffer = (DOMOutputBuffer)
            pageContextMock.getProtocol().getOutputBufferFactory().
                createOutputBuffer();
        pageContextMock.pushOutputBuffer(outputBuffer);
        startElement("html");
//      @todo fix the srctype when script element is moved from MCS namespace
        try {
            startElement(XDIMESchemata.XDIME2_MCS_NAMESPACE, "script",
                new String[]{"src", "srctype"},
                new String[]{"URL://test.mscr", ""});
            fail("script can only be used in the head");
        } catch (SAXException e) {
            // expected
        }
    }

    public void testFunctionality() throws Exception {
        startElement("html");
        startElement("head");
        startElement("title");
        endElement("title");
//      @todo fix the srctype when script element is moved from MCS namespace
        startElement(XDIMESchemata.XDIME2_MCS_NAMESPACE, "script",
            new String[]{"src", "srctype"},
            new String[]{"URL://test.mscr", ""});
        characters("some javascript");
        endElement("script");
        startElement(XDIMESchemata.XDIME2_MCS_NAMESPACE, "script",
            new String[]{"src", "srctype"},
            new String[]{"invalidURL.mscr", ""});
        characters("some javascript 2");
        endElement(XDIMESchemata.XDIME2_MCS_NAMESPACE, "script");
        startElement(XDIMESchemata.XDIME2_MCS_NAMESPACE, "script",
            new String[]{"srctype"},
            new String[]{""});
        characters("some javascript 3");
        endElement(XDIMESchemata.XDIME2_MCS_NAMESPACE, "script");

        final DOMOutputBuffer headBuffer = (DOMOutputBuffer)
            pageContextMock.getProtocol().getPageHead().getHead();
        final Element first = (Element) headBuffer.getRoot().getHead();
        assertEquals("script", first.getName());
        assertEquals("url-for-the-script-variant", first.getAttributeValue("src"));
        assertEquals("characterSet", first.getAttributeValue("charset"));
        assertEquals("programmingLanguage", first.getAttributeValue("language"));
        assertEquals("mimeType", first.getAttributeValue("type"));
        assertNull(first.getHead());

        final Element second = (Element) first.getNext();
        assertEquals("script", second.getName());
        assertNull(second.getAttributeValue("src"));
        assertNull(second.getAttributeValue("charset"));
        assertEquals("text/javascript", second.getAttributeValue("type"));
        Text text = (Text) second.getHead();
        assertEquals("some javascript 2",
            new String(text.getContents(), 0, text.getLength()));

        final Element third = (Element) second.getNext();
        assertEquals("script", third.getName());
        assertNull(third.getAttributeValue("src"));
        assertNull(third.getAttributeValue("charset"));
        assertEquals("text/javascript", third.getAttributeValue("type"));
        text = (Text) third.getHead();
        assertEquals("some javascript 3",
            new String(text.getContents(), 0, text.getLength()));

        assertNull(third.getNext());
    }

    public void testSchema() throws SAXException, IOException {
        final SchemaValidator validator = new SchemaValidator();
        validator.addSchemata(XDIMESchemata.ALL_XDIME2_SCHEMATA);
        validator.validate(new BinaryContentInput(
            getClass().getResourceAsStream("res/script-test.xml")));
        try {
            validator.validate(new BinaryContentInput(
                getClass().getResourceAsStream("res/invalid-script-test.xml")));
            fail("should fail with exception");
        } catch (SAXException e) {
            // expected
        }
    }
}
