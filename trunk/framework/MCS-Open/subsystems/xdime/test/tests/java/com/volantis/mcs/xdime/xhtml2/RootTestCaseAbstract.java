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

import com.volantis.mcs.accessors.LayoutBuilder;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.CacheScopeConstant;
import com.volantis.mcs.context.CurrentProjectProvider;
import com.volantis.mcs.context.CurrentProjectProviderMock;
import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.ListenerEventRegistryMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TranscodableUrlResolver;
import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.css.version.DefaultCSSVersion;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.dom2theme.ExtractorContext;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.FormatNamespace;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.RuntimeLayoutFactory;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIElementFactory;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.PaneAttributes;
import com.volantis.mcs.papi.impl.DefaultPAPIFactory;
import com.volantis.mcs.project.InternalProjectFactory;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.MarkupTestCaseAbstract;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.protocols.css.RuntimeExtractorContext;
import com.volantis.mcs.protocols.layouts.PaneInstanceMock;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.StyleSheetConfiguration;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.packagers.AbstractPackageBodyOutput;
import com.volantis.mcs.runtime.packagers.PackagingException;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverTestHelper;
import com.volantis.mcs.runtime.policies.theme.RuntimeDeviceTheme;
import com.volantis.mcs.runtime.styling.CSSCompilerBuilder;
import com.volantis.mcs.runtime.styling.CompiledStyleSheetCollection;
import com.volantis.mcs.runtime.styling.DefaultStyleSheetCompilerFactory;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.xdime.XDIMEContentHandler;
import com.volantis.mcs.xdime.XDIMEContextFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementHandler;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.styling.compiler.InlineStyleSheetCompilerFactory;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

/**
 * Base class for integration tests for protocols.
 *
 * This class allows to specify the input markup and check the resulting markup
 * generated by the protocol.
 *
 * todo Refactor all the tests to use the XDIMEIntegrationTest in the
 * todo product-tests subsystem as these are just too fragile.
 */
public abstract class RootTestCaseAbstract extends MarkupTestCaseAbstract {

    private static final CSSParserFactory CSS_PARSER_FACTORY =
        CSSParserFactory.getDefaultInstance();

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    static final String THEME_NAME = "/theme.mthm";
    static final String LAYOUT_NAME = "/layout.mlyt";
    static final String PAGE_HEADER_COMMENT = "Heading Message";
    static final String PAGE_TITLE = "Page Title";

    private XDIMEContentHandler handler = null;

    /**
     * Factory for the creation of PAPI elements and attributes.
     */
    private DefaultPAPIFactory papiFactory;

    private final Stack deviceLayoutContextStack = new Stack();
    private final Stack outputBufferStack = new Stack();

    private final Stack canvasTypeStack = new Stack();

    protected EnvironmentContextMock environmentContextMock;
    private PAPIElement canvasElement;
    private PAPIElement paneElement;
    protected AssetResolverMock assetResolverMock;
    protected PolicyReferenceResolverMock referenceResolverMock;

    // Javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        papiFactory = new DefaultPAPIFactory();

        buildPageContext();

        protocol.initialise();
        protocol.initialiseCanvas();


        XDIMEContextInternal context =
            (XDIMEContextInternal)XDIMEContextFactory.getDefaultInstance()
            .createXDIMEContext();

        context.setInitialRequestContext(requestContextMock);

        handler =
            new XDIMEContentHandler(null, context,
                                    XDIMEElementHandler.getDefaultInstance());

    }

    /**
     * Create a page context mock and set the standard set of expectations
     * on it.
     *
     * @throws RepositoryException
     * @throws IllegalAccessError
     * @throws ClassNotFoundException
     * @throws InstantiationException
     */
    protected void buildPageContext() throws RepositoryException,
        IllegalAccessException, InstantiationException,
        ClassNotFoundException {
        
        InternalDevice device = createInternalDevice();

        MarinerURL requestURL =
            new MarinerURL("http://localhost/test.jsp");


        environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);

        assetResolverMock = new AssetResolverMock("assetResolverMock",
                expectations);

        marinerPageContextMock.expects.getAssetResolver()
                .returns(assetResolverMock).any();

        referenceResolverMock = PolicyReferenceResolverTestHelper
                .getCommonExpectations(expectations, mockFactory);
        marinerPageContextMock.expects.getPolicyReferenceResolver()
                .returns(referenceResolverMock).any();

        marinerPageContextMock.expects.getTranscodableUrlResolver().returns(
            new TranscodableUrlResolver(requestContextMock)).any();

        DefaultCSSVersion cssVersion = new DefaultCSSVersion("testVersion");
        cssVersion.markImmutable();

        ExtractorContext returnValue = new RuntimeExtractorContext(
                referenceResolverMock, assetResolverMock, null, cssVersion);

        StylingEngine stylingEngine = createStyling();

        /*
         * Layout objects
         */
        CanvasLayout canvasLayout =
            createDeviceLayout("Test Layout", "Master");

        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        DeviceLayoutContext deviceLayoutContext = new DeviceLayoutContext();
        deviceLayoutContext.setMarinerPageContext(marinerPageContextMock);
        deviceLayoutContext.setDeviceLayout(runtimeDeviceLayout);

        deviceLayoutContextStack.push(deviceLayoutContext);

        /*
         * Pane related mock objects
         */
        Pane pane = new Pane(canvasLayout);

        com.volantis.mcs.protocols.PaneAttributes protocolPaneAttributes =
            new com.volantis.mcs.protocols.PaneAttributes();

        PaneInstanceMock paneInstance = new PaneInstanceMock(
            "mockPaneInstance", expectations,
            NDimensionalIndex.ZERO_DIMENSIONS);
        paneInstance.expects.ignore().returns(false).any();
        // optional for XDIME2.
        paneInstance.expects.setStyleClass(null).any();
        paneInstance.expects.getAttributes().returns(protocolPaneAttributes).any();
        paneInstance.expects.getFormat().returns(pane).any();

        StyleSheetConfiguration styleSheetConfig = new StyleSheetConfiguration();
        volantisMock.expects.getPageHeadingMsg().returns(PAGE_HEADER_COMMENT);
        volantisMock.expects.getStyleSheetConfiguration().returns(
            styleSheetConfig);
        
        volantisMock.expects.getPageTrackerFactory().returns(null).any();

        /*
         * Set expectations on MarinerPageContext Mock
         */
        marinerPageContextMock.expects.getRequestURL(false).returns(
            requestURL).any();

        marinerPageContextMock.expects.getDeviceLayoutContext().does(new MethodAction() {
            public Object perform(MethodActionEvent event) throws Throwable {
                Object result = null;
                if (!deviceLayoutContextStack.empty()) {
                    result = deviceLayoutContextStack.peek();
                }
                return result;
            }
        }).any();
        marinerPageContextMock.fuzzy.pushDeviceLayoutContext(
            mockFactory.expectsInstanceOf(DeviceLayoutContext.class))
            .does(new MethodAction() {
                public Object perform(MethodActionEvent event)
                    throws Throwable {
                    Object context = event.getArgument(
                        DeviceLayoutContext.class);

                    deviceLayoutContextStack.push(context);
                    return null;
                }
            }).any();
        // optional for XDIME2
        marinerPageContextMock.expects.setCanvasHasChildren(false).any();
        marinerPageContextMock.fuzzy.getFormatInstance(
            mockFactory.expectsInstanceOf(Pane.class),
            NDimensionalIndex.ZERO_DIMENSIONS)
            .returns(paneInstance)
            .any();

        InternalProjectFactory factory =
                InternalProjectFactory.getInternalInstance();

//        ProjectMock project = new ProjectMock("project mock", expectations);
        final RuntimeProjectMock project =
                new RuntimeProjectMock("projectMock", expectations);
        project.expects.getPolicySource()
                .returns(factory.createXMLPolicySource(null, ".")).any();

        MethodAction initialiseCanvasAction = new MethodAction() {

            public Object perform(MethodActionEvent event) throws Throwable {
                // Optional for XDIME2.
                marinerPageContextMock.expects.initialisedCanvas().returns(true).any();
                return null;
            }
        };
        marinerPageContextMock.fuzzy.initialise(Boolean.FALSE,
                Boolean.FALSE,
                null,
                null,
                mockFactory.expectsInstanceOf(CompiledStyleSheetCollection.class),
                LAYOUT_NAME).does(initialiseCanvasAction);

        marinerPageContextMock.expects.peekCanvasType().does(new MethodAction() {
            public Object perform(MethodActionEvent event) throws Throwable {
                Object result = null;
                if (!canvasTypeStack.empty()) {
                    result = canvasTypeStack.peek();
                }
                return result;
            }
        }).any();
        marinerPageContextMock.expects.popCanvasType().does(new MethodAction() {
            public Object perform(MethodActionEvent event) throws Throwable {
                Object result = null;
                if (!canvasTypeStack.empty()) {
                    result = canvasTypeStack.pop();
                }
                return result;
            }
        }).any();
        marinerPageContextMock.fuzzy.pushCanvasType(
            mockFactory.expectsInstanceOf(String.class))
            .does(new MethodAction() {
                public Object perform(MethodActionEvent event)
                    throws Throwable {
                    Object context = event.getArgument(String.class);

                    canvasTypeStack.push(context);
                    return null;
                }
            }).any();


        // Since we have pane set for XDIMECP via cdm:pane and for XDIME2 via
        // mcs-container these two will be called twice as we cannot vary the
        // stylesheet per test.
        marinerPageContextMock.expects.pushContainerInstance(paneInstance).any();
        marinerPageContextMock.expects.popContainerInstance(paneInstance).any();

        marinerPageContextMock.expects.getCurrentContainerInstance().returns(paneInstance).any();
        marinerPageContextMock.expects.getCurrentPane().returns(pane).any();

        marinerPageContextMock.expects.updateFragmentationState();
        marinerPageContextMock.expects.getProtocol().returns(protocol).any();
        marinerPageContextMock.expects.getDeviceName().returns("Master");
        marinerPageContextMock.expects.getDevice().returns(device).any();

        // optional for XDIME1?
        marinerPageContextMock.fuzzy.getFormat("pane",
                mockFactory.expectsInstanceOf(FormatNamespace.class))
                .returns(pane).any();
        // optional for XDIME2.
        marinerPageContextMock.expects.getPane("pane").returns(pane).any();
        marinerPageContextMock.fuzzy.pushOutputBuffer(
            mockFactory.expectsInstanceOf(DOMOutputBuffer.class))
            .does(new MethodAction() {
                public Object perform(MethodActionEvent event)
                    throws Throwable {
                    Object buffer = event.getArgument(OutputBuffer.class);

                    outputBufferStack.push(buffer);

                    return null;
                }
            }).any();
        marinerPageContextMock.fuzzy.popOutputBuffer(
            mockFactory.expectsInstanceOf(DOMOutputBuffer.class))
            .does(new MethodAction() {
                public Object perform(MethodActionEvent event)
                    throws Throwable {

                    return outputBufferStack.pop();
                }
            }).any();
        marinerPageContextMock.expects.getCurrentOutputBuffer().does(new MethodAction() {

            public Object perform(MethodActionEvent event) throws Throwable {
                return outputBufferStack.peek();
            }
        }).any();

        RuntimeDeviceTheme runtimeDeviceTheme = createRuntimeDeviceTheme();

        marinerPageContextMock.expects.retrieveThemeStyleSheet(THEME_NAME).
            returns(runtimeDeviceTheme.getCompiledStyleSheet());
        
        marinerPageContextMock.expects.enteringXDIMECPElement().any();
        marinerPageContextMock.expects.insideXDIMECPElement()
                .returns(false).any();
        marinerPageContextMock.expects.exitingXDIMECPElement().any();
        marinerPageContextMock.expects.getEnclosingRegionInstance()
                .returns(null).any();
        marinerPageContextMock.expects.popDeviceLayoutContext()
                .does(new MethodAction() {
                    public Object perform(MethodActionEvent methodActionEvent)
                            throws Throwable {

                        // Don't actually pop this as it prevents the markup
                        // from being generated at the end. NASTY.
                        return deviceLayoutContextStack.peek();
                    }
                }).any();
        setProtocolSpecificExpectations();

        ApplicationContext applicationContext = new ApplicationContext(
            requestContextMock);
        applicationContext.setDevice(device);
        applicationContext.setProtocol(protocol);
        applicationContext.setDissectionSupported(true);
        applicationContext.setFragmentationSupported(true);
        applicationContext.setCanvasTagSupported(true);
        applicationContext.setWMLCSupported(false);

        ExpressionFactory expressionFactory = ExpressionFactory.getDefaultInstance();

        ExpressionContext expressionContext =
            expressionFactory.createExpressionContext(null,
                                                      NamespaceFactory.getDefaultInstance()
                                                      .createPrefixTracker());

        expressionContext.setProperty(MarinerRequestContext.class,
                                      requestContextMock, false);

        final CurrentProjectProviderMock projectProviderMock =
                new CurrentProjectProviderMock("projectProviderMock",
                        expectations);
        projectProviderMock.expects.getCurrentProject().returns(project).any();

        // Add a project provider in for resolving relative policy expressions
        // without a project into absolute IDs containing a project.
        expressionContext.setProperty(CurrentProjectProvider.class,
                projectProviderMock, false);

        // Optional for XDIME2
        marinerPageContextMock.expects.setBrandName(null, false).any();

        marinerPageContextMock.expects.getStylingEngine().returns(
            stylingEngine)
            .any();

        requestContextMock.expects.getApplicationContext().returns(
            applicationContext)
            .any();
        requestContextMock.expects.getMarinerPageContext().returns(
            marinerPageContextMock)
            .any();

        requestContextMock.expects.getEnvironmentContext()
                .returns(environmentContextMock).any();

        requestContextMock.expects.pushProject(project).any();

        requestContextMock.expects.popProject(project).returns(project).any();

        marinerPageContextMock.expects.getEnvironmentContext()
            .returns(environmentContextMock).any();   //  fixed(2);


        environmentContextMock.fuzzy.setAttribute(
                CacheScopeConstant.CACHE_SCOPE_ATTRIBUTE,
                mockFactory.expectsInstanceOf(CacheScopeConstant.class));

        environmentContextMock.expects.getCachingDirectives().returns(null).any();
        environmentContextMock.expects.applyCachingDirectives().any();

        deviceLayoutContext.initialise();

        ListenerEventRegistryMock listenerEventRegistryMock =
                new ListenerEventRegistryMock("listenerEventRegistry",
                        expectations);

        listenerEventRegistryMock.expects.complete().any();

        marinerPageContextMock.expects.getListenerEventRegistry().returns(
                listenerEventRegistryMock).any();
    }

    /**
     * Add protocol specific expectations to the mock objects.
     */
    protected void setProtocolSpecificExpectations() {
    }


    /**
     * Create the Runtime DeviceTheme by first creating a deviceTheme
     * and then activating it.
     *
     * @return
     */
    private RuntimeDeviceTheme createRuntimeDeviceTheme() {

        String css = getCSS();

        CSSParser parser = CSS_PARSER_FACTORY.createStrictParser();
        StyleSheet styleSheet = parser.parseStyleSheet(new StringReader(css),
                                                       null);

        return RuntimeDeviceLayoutTestHelper.activate(styleSheet);
    }

    /**
     * Create an InternalDevice object.
     *
     * @return
     */
    private InternalDevice createInternalDevice() {
        HashMap policies = new HashMap();
        policies.put("preferredimagetype", ImageAsset.PNG_NAME);
        policies.put("rendermode", "greyscale");
        policies.put("bmpinpage", "true");
        policies.put("gifinpage", "false");
        policies.put("jpeginpage", "true");
        policies.put("pnginpage", "true");
        policies.put("maximagesize", "1024");
        policies.put("pixeldepth", "16");
        policies.put("pixelsx", "640");
        policies.put("gpng16rule", "gp16");
        policies.put("mp3inpage", "true");
        policies.put("mpeg4inpage", "true");
        policies.put("flashinpage", "true");
        policies.put("downloadeble.audio.maxsize", "1000");
        policies.put("downloadeble.audio.maxbitrate", "128");

        InternalDevice device = INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice("Test Device", policies, null));
        return device;
    }

    /**
     * Create a styling engine.
     *
     * @return
     */
    private StylingEngine createStyling() {

        // Create the styling engine.
        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();

        StyleSheetCompilerFactory inlineCompilerFactory =
                new InlineStyleSheetCompilerFactory(null);

        StylingEngine stylingEngine = stylingFactory.createStylingEngine(inlineCompilerFactory);

        // Create a CSS compiler.
        CSSCompilerBuilder builder = new CSSCompilerBuilder();
        builder.setStyleSheetCompilerFactory(
                DefaultStyleSheetCompilerFactory.getDefaultInstance());
        CSSCompiler cssCompiler = builder.getCSSCompiler();

        // Compile and push the system default style sheet into the styling
        // engine.
        CompiledStyleSheet defaultCompiledStyleSheet;
        try {
            URL url = getClass().getResource("/com/volantis/mcs/runtime/default.css");
            InputStream stream = url.openStream();
            defaultCompiledStyleSheet = cssCompiler.compile(
                    new InputStreamReader(stream), null);
        } catch (IOException e) {
            throw new ExtendedRuntimeException(e);
        }
        stylingEngine.pushStyleSheet(defaultCompiledStyleSheet);

        if (protocol != null &&
            protocol.getCompiledDefaultStyleSheet() != null) {
            // merge the default style sheet and the current one
            stylingEngine.pushStyleSheet(
                protocol.getCompiledDefaultStyleSheet());
        }
        return stylingEngine;
    }

    /**
     * Create a Layout object. This default implementation creates a simple
     * layout with a single Pane, subclassing testcases can override this
     * to create more complex layouts.
     *
     * @param name       Name passed to Layout object.
     * @param deviceName Device name to be passed to layout object.
     * @return Layout object.
     *
     */
    protected CanvasLayout createDeviceLayout(String name,
                                              String deviceName)
        throws RepositoryException {

        LayoutBuilder builder = new LayoutBuilder(new RuntimeLayoutFactory());

        builder.createLayout(LayoutType.CANVAS);

        builder.pushFormat("Pane", 0);

        builder.setAttribute("Name", "pane");

        builder.popFormat();

        return builder.getCanvasLayout();
    }

    /**
     * Return the selectors/style theme in CSS format. This method defines the
     * theme to be used by the testcase, can be overridden to change the
     * default theme.
     *
     * @return A string in CSS format.
     */
    protected String getCSS() {
        return
            ".bold{font-weight:bold}\n" +
            ".b900{font-weight:900}\n" +
            ".non-bold{font-weight:100}\n" +
            ".underline{text-decoration:underline}\n" +
            ".line-through{text-decoration:line-through}\n" +
            ".both{text-decoration:line-through; text-decoration:underline}\n" +
            ".none{text-decoration:none}\n" +
            ".italic{font-style:italic}\n" +
            ".oblique{font-style:oblique}\n" +
            ".normal{font-style:normal}\n" +
            ".color{color:red}\n" +
            ".xx-large{font-size:xx-large}\n" +
            ".x-large{font-size:x-large}\n" +
            ".large{font-size:large}\n" +
            ".medium{font-size:medium}\n" +
            ".small{font-size:small}\n" +
            ".x-small{font-size:x-small}\n" +
            ".xx-small{font-size:xx-small}\n" +
            ".smaller{font-size:smaller}\n" +
            ".larger{font-size:larger}\n" +
            ".left{text-align:left}\n" +
            ".center{text-align:center}\n" +
            ".right{text-align:right}\n" +
            ".top{vertical-align:top}\n" +
            ".middle{vertical-align:middle}\n" +
            ".bottom{vertical-align:bottom}\n" +
            ".padding{padding-top:10px}\n" +
            ".border-spacing{border-spacing:20px}\n" +
            ".border-width{border-top-width:30px}\n" +
            ".width{width:40px}\n" +
            ".height{height:50px}\n" +
            ".background-color{background-color:#0000FF}\n" +
            ".class1{color:red; text-align:center; font-size:x-small; " +
            "text-decoration:underline}\n" +
            ".bang{font-weight:bold; color:red}\n" +
            // add default pane for xdimecp/2 pages.
            // todo: inline styles would be the way to do this properly.
            "body {mcs-container: \"pane\"}";
    }

    /**
     * Start the XDIME element.
     *
     * @param elementName name of element to create and start.
     * @param attr
     * @throws SAXException
     */
    private void startElement(String elementName, Attributes attr)
        throws SAXException {

        handler.startElement(XDIMESchemata.XHTML2_NAMESPACE, elementName, null,
                             attr);
    }

    /**
     * End the XDIME element.
     *
     * @param elementName
     * @throws SAXException
     */
    protected void endElement(String elementName) throws SAXException {
        handler.endElement(XDIMESchemata.XHTML2_NAMESPACE, elementName, null);
    }

    /**
     * Return an instance of the Protocol class under test. This method needs
     * to be implemented by test class.
     *
     * @return Protocol class instance
     */
    protected abstract DOMProtocol createProtocol();

    /**
     * Generic open element method, which takes the name of the element to open
     * and parameters.
     *
     * @param name Name of PAPI element to open, must be valid PAPI element.
     */
    protected void startElement(String name) throws SAXException {

        startElement(name, new String[]{}, new String[]{});
    }


    /**
     * Generic open element method, which takes the name of the element to open
     * and parameters.
     *
     * @param name   Name of element to open, must be valid element.
     * @param paramNames
     * @param paramValues
     */
    protected void startElement(String name, String[] paramNames,
                                String[] paramValues) throws SAXException {

        AttributesImpl att = new AttributesImpl();

        for (int i = 0; i < paramNames.length; i++) {
            att.addAttribute(null,
                             paramNames[i], null, null,
                             paramValues[i]);
        }

        startElement(name, att);

    }

    /**
     * Write a string to the output buffer.
     *
     * @param message Massage to write.
     */
    protected void writeMessage(String message) {

        marinerPageContextMock.getCurrentOutputBuffer().writeText(message);
    }

    /**
     * Accessor method for Request Context.
     *
     * @return
     */
    protected MarinerRequestContext getCurrentRequestContext() {
        return marinerPageContextMock.getRequestContext();
    }

    /**
     * Extract the generated markup from the protocol object.
     *
     * @return Markup
     */
    protected String getMarkup() throws IOException,
        PackagingException {

        CanvasAttributes canvasAttributes
            = new CanvasAttributes();
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter(stream);

        protocol.write(new AbstractPackageBodyOutput() {
            public Writer getRealWriter() {
                return writer;
            }

            public OutputStream getRealOutputStream() {
                return stream;
            }

        }, null, canvasAttributes);

        writer.flush();

        return new String(stream.toByteArray());
    }

    protected void openDocument() throws PAPIException, SAXException {
        openCanvas();

        openPane();

        startElement("html");

        startElement("head");
        startElement("title");
        endElement("title");
        endElement("head");

        startElement("body");
    }

    protected void openPane() throws PAPIException {
        PAPIElementFactory factory =
            papiFactory.getPAPIElementFactory("pane");

        paneElement = factory.createPAPIElement();
        PaneAttributes attributes =
            (PaneAttributes)factory.createElementSpecificAttributes();

        attributes.setName("pane");

        paneElement.elementStart(getCurrentRequestContext(), attributes);

    }

    protected void openCanvas() throws PAPIException {
        PAPIElementFactory factory =
            papiFactory.getPAPIElementFactory("canvas");

        canvasElement = factory.createPAPIElement();

        com.volantis.mcs.papi.CanvasAttributes canvasAttributes =
            (com.volantis.mcs.papi.CanvasAttributes)factory.createElementSpecificAttributes();
        canvasAttributes.setPageTitle(PAGE_TITLE);
        canvasAttributes.setTheme(THEME_NAME);
        canvasAttributes.setLayoutName(LAYOUT_NAME);

        canvasElement.elementStart(getCurrentRequestContext(),
                                   canvasAttributes);

    }

    protected void closeDocument() throws PAPIException, SAXException {

        endElement("body");

        endElement("html");

        closePane();

        closeCanvas();
    }

    private void closeCanvas() throws PAPIException {
        PAPIElementFactory factory =
            papiFactory.getPAPIElementFactory("canvas");
        PAPIAttributes papiAttr = factory.createElementSpecificAttributes();

        canvasElement.elementEnd(getCurrentRequestContext(), papiAttr);

        canvasElement = null;
    }

    private void closePane() throws PAPIException {
        PAPIElementFactory factory =
            papiFactory.getPAPIElementFactory("pane");
        PAPIAttributes papiAttr = factory.createElementSpecificAttributes();

        paneElement.elementEnd(getCurrentRequestContext(), papiAttr);

        paneElement = null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 22-Nov-05	10394/1	ibush	VBM:2005111812 Fix Canvas Branding

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 27-Oct-05	10007/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 22-Nov-05	10394/1	ibush	VBM:2005111812 Fix Canvas Branding

 15-Nov-05	10326/7	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 27-Oct-05	10007/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 13-Oct-05	9727/3	ianw	VBM:2005100506 Fixed remote repository issues

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 06-Oct-05	9736/1	pabbott	VBM:2005100512 Add the XHTML2 object testcase

 ===========================================================================
*/