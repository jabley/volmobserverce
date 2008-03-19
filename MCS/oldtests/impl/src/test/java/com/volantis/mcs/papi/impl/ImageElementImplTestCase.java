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

import com.volantis.charset.EncodingManager;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.integration.PluggableAssetTranscoder;
import com.volantis.mcs.integration.transcoder.ICSWithoutGIF;
import com.volantis.mcs.papi.ImageAttributes;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.runtime.TestableVolantis;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.mcs.utilities.StringConvertor;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

/**
 * Tests that ImageElementImpl class.
 */
public class ImageElementImplTestCase extends TestCaseAbstract {
    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY = InternalDeviceFactory.getDefaultInstance();


    // javadoc inherited
    public ImageElementImplTestCase(String name) {
        super(name);
    }

    /**
     * Tests that when there is no aspect ratio width and height are not set
     * on the element.
     */
    public void testNoAspectRatio() throws Exception {
        TestMarinerRequestContext requestContext =
                createRequestContext(new ICSWithoutGIF(), "cj24", 250);

        // Create the urlc attribute for the image element implementation.
        ImageAttributes papiImageAttributes = new ImageAttributes();
        papiImageAttributes.
                setUrlc("http://www.volantis.com/myImage.jpg?myParam=56");

        // Create the image element implementation.
        ImageElementImpl element = new ImageElementImpl();

        // Generate the pane output for this image element.
        element.elementStartImpl(requestContext, papiImageAttributes);

        // Retrieve the image element's attributes. This is not exposed, so
        // PrivateAccessor must be used.
        com.volantis.mcs.protocols.ImageAttributes protocolImageAttributes =
                (com.volantis.mcs.protocols.ImageAttributes)
                PrivateAccessor.getField(element, "pattributes");

        assertEquals("width attribute should be null",
                null, protocolImageAttributes.getWidth());

        assertEquals("height attribute should be null",
                null, protocolImageAttributes.getHeight());
    }

    /**
     * Tests that when there is an aspect ratio, width and height are set
     * on the element.
     */
    public void testAspectRatio() throws Exception {
        TestMarinerRequestContext requestContext =
                createRequestContext(new ICSWithoutGIF(), "cj24", 250);

        // Create the urlc attribute for the image element implementation.
        ImageAttributes papiImageAttributes = new ImageAttributes();
        papiImageAttributes.
                setUrlc("http://www.volantis.com/myImage.jpg?mcs.ar=5:4");

        // Create the image element implementation.
        ImageElementImpl element = new ImageElementImpl();

        // Generate the pane output for this image element.
        element.elementStartImpl(requestContext, papiImageAttributes);

        // Retrieve the image element's attributes. This is not exposed, so
        // PrivateAccessor must be used.
        com.volantis.mcs.protocols.ImageAttributes protocolImageAttributes =
                (com.volantis.mcs.protocols.ImageAttributes)
                PrivateAccessor.getField(element, "pattributes");

        assertEquals("width attribute not as expected",
                "250", protocolImageAttributes.getWidth());

        // Calculated height should be 250 / aspectRatio
        assertEquals("height attribute not as expected",
                "200", protocolImageAttributes.getHeight());
    }

    /**
     * Tests that when there is an aspect ratio and an existing width and
     * height on the element, then these values are not overridden.
     */
    public void testAspectRatioNoOverwrite() throws Exception {
        TestMarinerRequestContext requestContext =
                createRequestContext(new ICSWithoutGIF(), "cj24", 250);

        // Create the urlc attribute for the image element implementation.
        ImageAttributes papiImageAttributes = new ImageAttributes();
        papiImageAttributes.
                setUrlc("http://www.volantis.com/myImage.jpg?mcs.ar=5:4");

        // Create the image element implementation.
        ImageElementImpl element = new ImageElementImpl();

        // Retrieve the image attributes so that they can be given an
        // existing width and height.
        com.volantis.mcs.protocols.ImageAttributes protocolImageAttributes =
                (com.volantis.mcs.protocols.ImageAttributes)
                PrivateAccessor.getField(element, "pattributes");

        protocolImageAttributes.setWidth("1234");
        protocolImageAttributes.setHeight("6789");

        // Width should not have been overwritten.
        assertEquals("width attribute was overwritten",
                "1234", protocolImageAttributes.getWidth());

        // Height should not have been overwritten.
        assertEquals("height attribute was overwritten",
                "6789", protocolImageAttributes.getHeight());

        // Generate the pane output for this image element.
        element.elementStartImpl(requestContext, papiImageAttributes);

        // Width should not have been overwritten.
        assertEquals("width attribute was overwritten",
                "1234", protocolImageAttributes.getWidth());

        // Height should not have been overwritten.
        assertEquals("height attribute was overwritten",
                "6789", protocolImageAttributes.getHeight());
    }

    /**
     * Tests that when there is an aspect ratio parameter, it is removed from
     * the urlc.
     */
    public void testAspectRatioRemoval() throws Exception {
        final String rule = "cj24";
        final PluggableAssetTranscoder transcoder = new ICSWithoutGIF();
        final int width = 369;
        final String urlc = "http://www.volantis.com/myImage.jpg?mcs.ar=5:4";
        TestMarinerRequestContext requestContext =
                createRequestContext(transcoder, rule, width);

        // Create the urlc attribute for the image element implementation.
        ImageAttributes papiImageAttributes = new ImageAttributes();
        papiImageAttributes.setUrlc(urlc);

        // Create the image element implementation.
        ImageElementImpl element = new ImageElementImpl();

        // Generate the pane output for this image element.
        element.elementStartImpl(requestContext, papiImageAttributes);

        // Retrieve the image attributes so that the src can be checked for the
        // removal of the aspect ratio parameter.
        com.volantis.mcs.protocols.ImageAttributes protocolImageAttributes =
                (com.volantis.mcs.protocols.ImageAttributes)
                PrivateAccessor.getField(element, "pattributes");

        final String urlcNoAspectRatio = protocolImageAttributes.getSrc();
        assertEquals("There should be no ratio parameter",
                "http://www.volantis.com/" + rule + "/myImage.jpg?" +
                transcoder.getWidthParameter() + "=" + width,
                urlcNoAspectRatio);
    }

    /**
     * Creates a request context for using in the tests.
     *
     * @param transcoder the transcoder to use
     * @param rule the transcoding rule
     * @param deviceWidth the width of the device
     * @return the request context
     */
    private TestMarinerRequestContext createRequestContext(final PluggableAssetTranscoder transcoder,
                                                           final String rule,
                                                           final int deviceWidth)
            throws Exception {
        TestMarinerRequestContext requestContext =
                new TestMarinerRequestContext();

        final ApplicationContext appContext =
                new ApplicationContext(requestContext);
        appContext.setEncodingManager(new EncodingManager());

        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        pageContext.pushRequestContext(requestContext);

        final PolicyReferenceResolverMock referenceResolverMock =
                new PolicyReferenceResolverMock("referenceResolverMock",
                        expectations);
        pageContext.setPolicyReferenceResolver(referenceResolverMock);

        referenceResolverMock.expects.resolveQuotedTextExpression(null)
                .returns(null).any();

        EnvironmentContext envContext = new TestEnvironmentContext();
        ContextInternals.setEnvironmentContext(requestContext, envContext);

        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        ContextInternals.setApplicationContext(requestContext, appContext);

        final Volantis volantis = new TestableVolantis() {
            public MarinerPageContext createMarinerPageContext() {
                return new TestMarinerPageContext();
            }

            public PluggableAssetTranscoder getAssetTranscoder() {
                return transcoder;
            }
        };

        pageContext.initialisePage(volantis, requestContext,
                null, null, null);

        final DefaultDevice dev = new DefaultDevice("myDevice", null, null);
        dev.setPolicyValue("protocol", "XHTMLBasic");
        dev.setPolicyValue("rendermode", "not-monochrome");
        dev.setPolicyValue("pixeldepth", "32");
        dev.setPolicyValue("jpeginpage", "true");
        dev.setPolicyValue("cjpeg24rule", rule);
        dev.setPolicyValue("preferredimagetype", "JPEG");

        // Set width on device.
        if (deviceWidth != 0) {
            dev.setPolicyValue("pixelsx",
                    StringConvertor.valueOf(deviceWidth));
        }

        final InternalDevice internalDevice =
            INTERNAL_DEVICE_FACTORY.createInternalDevice(dev);
        appContext.setDevice(internalDevice);
        pageContext.setDevice(internalDevice);

        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol domProtocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                InternalDeviceTestHelper.createTestDevice());
        final DeviceLayoutContext layoutContext = new DeviceLayoutContext();

        pageContext.pushDeviceLayoutContext(layoutContext);
        pageContext.setProtocol(domProtocol);
        domProtocol.setMarinerPageContext(pageContext);

        return requestContext;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 02-Sep-05	9391/3	emma	VBM:2005082604 Supermerge required

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 05-Jul-05	8813/6	pcameron	VBM:2005061608 Added aspect ratio parameter processing to XDIME-CP

 ===========================================================================
*/
