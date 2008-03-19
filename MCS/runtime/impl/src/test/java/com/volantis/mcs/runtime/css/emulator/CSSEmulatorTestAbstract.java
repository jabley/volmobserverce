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
package com.volantis.mcs.runtime.css.emulator;

import com.volantis.charset.EncodingManager;
import com.volantis.mcs.accessors.LayoutBuilder;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.CurrentProjectProvider;
import com.volantis.mcs.context.CurrentProjectProviderMock;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.RuntimeTestEnvironmentContext;
import com.volantis.mcs.context.RuntimeTestMarinerPageContext;
import com.volantis.mcs.context.RuntimeTestMarinerRequestContext;
import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.RuntimeLayoutFactory;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.papi.BaseAttributes;
import com.volantis.mcs.papi.CanvasAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIConstants;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIElementFactory;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.PaneAttributes;
import com.volantis.mcs.papi.RegionAttributes;
import com.volantis.mcs.papi.impl.DefaultPAPIFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RequestHeaders;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.TestVolantis;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.packagers.DefaultPackager;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactoryImpl;
import com.volantis.mcs.runtime.policies.SelectedVariantMock;
import com.volantis.mcs.runtime.policies.theme.RuntimeDeviceTheme;
import com.volantis.mcs.runtime.project.ProjectManagerMock;
import com.volantis.mcs.runtime.selection.VariantSelectionPolicyMock;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Stack;

/**
 * This class is the Root of all CSS style emulation. Driving the start and end
 * methods on the specified PAPI elements.
 */
public abstract class CSSEmulatorTestAbstract extends TestCaseAbstract
    implements PAPIConstants {

    private static final CSSParserFactory CSS_PARSER_FACTORY =
        CSSParserFactory.getDefaultInstance();

    /**
     * Factory for the creation of PAPI elements and attributes.
     */
    protected DefaultPAPIFactory papiFactory = new DefaultPAPIFactory();;

    /**
     * PageContext for protocol
     */
    private RuntimeTestMarinerPageContext pageContext;

    /**
     * Stack holding elements that have been opened.
     */
    protected Stack elements = new Stack();

    /**
     * Internal device object.
     */
    protected InternalDevice device;
    public StringWriter writer;

    // Javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        writer = new StringWriter();
        setCurrentPageContext(buildPageContext(null,writer));

    }

    /**
     * Create a page context, linking it to a
     * @param parent
     * @return
     * @throws RepositoryException
     */
    protected RuntimeTestMarinerPageContext buildPageContext(
            RuntimeTestMarinerPageContext parent,
            Writer writer) throws RepositoryException {

        device = InternalDeviceTestHelper.createTestDevice();

        // Original protocol object, note this will be cloned by the
        // MarinerPageContext and the clone used.
        DOMProtocol protocol = createProtocol();

        // Create a basic layout object
        RuntimeDeviceLayout deviceLayout = createDeviceLayout();

        RuntimeTestMarinerPageContext pageContext = new RuntimeTestMarinerPageContext();
        RuntimeTestMarinerRequestContext requestContext = new RuntimeTestMarinerRequestContext();
        EnvironmentContext environmentContext = new RuntimeTestEnvironmentContext();
        ((RuntimeTestEnvironmentContext)environmentContext)
                .setResponseWriter(writer);

        ApplicationContext applicationContext = new ApplicationContext(
            requestContext);

        pageContext.setDeviceLayout(deviceLayout);

        applicationContext.setDevice(device);
        applicationContext.setProtocol(protocol);
        applicationContext.setDissectionSupported(true);
        applicationContext.setFragmentationSupported(true);
        applicationContext.setCanvasTagSupported(true);
        applicationContext.setWMLCSupported(false);
        applicationContext.setEncodingManager(new EncodingManager());
        applicationContext.setPackager(new DefaultPackager());

        TestVolantis volantisBean = new TestVolantis();

        RuntimeDeviceTheme runtimeDeviceTheme = createRuntimeDeviceTheme();

        final VariantSelectionPolicyMock variantSelectionPolicyMock =
                new VariantSelectionPolicyMock("variantSelectionPolicyMock",
                        expectations);

        final SelectedVariantMock selectedVariantMock =
                new SelectedVariantMock("selectedVariantMock", expectations);
        selectedVariantMock.expects.getOldObject()
                .returns(runtimeDeviceTheme).any();

        final ProjectManagerMock projectManagerMock =
                new ProjectManagerMock("projectManagerMock", expectations);
        PolicyReferenceFactory referenceFactory =
                new PolicyReferenceFactoryImpl(projectManagerMock);

        variantSelectionPolicyMock.fuzzy
                .retrieveBestObject(mockFactory.expectsAny(),
                        mockFactory.expectsAny(),
                        mockFactory.expectsAny())
                .returns(selectedVariantMock).any();

        volantisBean.setVariantSelectionPolicy(variantSelectionPolicyMock);
        volantisBean.setPolicyReferenceFactory(referenceFactory);

        volantisBean.setPageHeadingMsg("Test Page Heading Message");

        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        ContextInternals.setEnvironmentContext(requestContext,
            environmentContext);
        ContextInternals.setApplicationContext(requestContext,
            applicationContext);

        MarinerURL marinerRequestURL =
            new MarinerURL("http://localhost/volantis/test.jsp");
        RequestHeaders requestHeaders = null;

        // Get the parent request context if there is one.
        MarinerRequestContext parentRequest = null;
        if (parent != null) {
            parentRequest = parent.getRequestContext();
        }
        // Initialise expression context.

        ContextInternals.setEnvironmentContext(requestContext,
                environmentContext);

        ExpressionFactory expressionFactory = ExpressionFactory.getDefaultInstance();

        ExpressionContext expressionContext =
                expressionFactory.createExpressionContext(
                        null,
                        NamespaceFactory.getDefaultInstance()
                        .createPrefixTracker());

        expressionContext.setProperty(MarinerRequestContext.class,
                                      requestContext, false);

        RuntimeProject project = new RuntimeProjectMock("runtimeProjectMock",
                expectations);
        volantisBean.setDefaultProject(project);

        final CurrentProjectProviderMock projectProviderMock =
                new CurrentProjectProviderMock("projectProviderMock",
                        expectations);
        projectProviderMock.expects.getCurrentProject().returns(project).any();

        // Add a project provider in for resolving relative policy expressions
        // without a project into absolute IDs containing a project.
        expressionContext.setProperty(CurrentProjectProvider.class,
                projectProviderMock, false);

        environmentContext.setExpressionContext(expressionContext);

        // Initialise the page context.
        pageContext.initialisePage(volantisBean, requestContext, parentRequest,
            marinerRequestURL, requestHeaders);

        pageContext.setCharsetName("UTF-8");

        //deviceLayoutContext.initialise();

        return pageContext;
    }

    /**
     * Create a Layout object. This default implementation creates a simple
     * with a single Pane, subclassing testcases can override this to create
     * more complex layouts.
     *
     * @return Layout object.
     * @throws com.volantis.mcs.layouts.LayoutException
     *
     */
    protected RuntimeDeviceLayout createDeviceLayout()
            throws RepositoryException {

        LayoutBuilder builder = new LayoutBuilder(new RuntimeLayoutFactory());

        builder.createLayout(LayoutType.CANVAS);

        builder.pushFormat("Pane", 0);

        builder.setAttribute("Name", "pane");

        builder.popFormat();

        Layout layout = builder.getLayout();

        return RuntimeDeviceLayoutTestHelper.activate(layout);
    }

    /**
     * Create a complete test device theme with rules, selectors and styles.
     *
     * @return RuntimeDeviceTheme structure.
     */
    protected RuntimeDeviceTheme createRuntimeDeviceTheme() {

        String css = getCSS();

        CSSParser parser = CSS_PARSER_FACTORY.createStrictParser();
        StyleSheet styleSheet = parser.parseStyleSheet(new StringReader(css), null);

        return RuntimeDeviceLayoutTestHelper.activate(styleSheet);

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
            // wrap and nowrap are for WML paragraph testing.
            // todo: enhance test framework to allow having per test css files.
            // todo: enhance test framework to be driveable from pseudo XDIME?
            ".wrap {white-space: normal}\n" +
            ".nowrap {white-space: nowrap}\n" +
            ".left {text-align: left}\n" +
            ".center {text-align: center}\n" +
            ".right {text-align: right}\n" +
            "";
    }

    /**
     * Return an instance of the Protocol class under test. This method needs
     * to be implemented by test class.
     *
     * @return Protocol class instance
     */
    protected abstract DOMProtocol createProtocol();

    /**
     * Return the name of the Protocol under test. This method needs to be
     * implemented by the test class.
     *
     * @return Valid protocol name.
     */
    protected abstract String getProtocolName();

    /**
     * Accessor method to allow testcase to get to the protocol under test.
     *
     * @return Protocol under test
     */
    protected DOMProtocol getProtocol() {
        return (DOMProtocol)getCurrentPageContext().getProtocol();
    }

    /**
     * Generic open element method, which takes the name of the element to open
     * and parameters.
     *
     * @param name Name of PAPI element to open, must be valid PAPI element.
     * @throws PAPIException
     */
    protected void open(String name)
        throws PAPIException {

        open(name, new String[0]);
    }

    /**
     * Generic open element method, which takes the name of the element to open
     * and one parameter.
     *
     * @param name  Name of PAPI element to open, must be valid PAPI element.
     * @param style Single style parameter
     * @throws PAPIException
     */
    protected void open(String name, String style)
        throws PAPIException {

        open(name, new String[]{style});
    }

    /**
     * Generic open element method, which takes the name of the element to open
     * and 2 parameters.
     *
     * @param name   Name of PAPI element to open, must be valid PAPI element.
     * @param style  Single style parameter
     * @param param1 Extra parameter
     * @throws PAPIException
     */
    protected void open(String name, String style, String param1)
        throws PAPIException {

        open(name, new String[]{style, param1});
    }

    /**
     * Generic open element method, which takes the name of the element to open
     * and parameters.
     *
     * @param name   Name of PAPI element to open, must be valid PAPI element.
     * @param params Array of parameters
     */
    protected void open(String name, String[] params)
        throws PAPIException {

        PAPIElementFactory factory =
            papiFactory.getPAPIElementFactory(name.toLowerCase());

        assertNotNull("Unknown element name, '" + name + "' check testcase.",
            factory);

        PAPIElement element = factory.createPAPIElement();
        PAPIAttributes attributes = factory.createElementSpecificAttributes();

        // If we are opening a subclass of BaseAttributes
        // then param[0] will be a style class name
        if (attributes instanceof BaseAttributes && params.length >= 1 &&
            params[0] != null) {
            BaseAttributes ba = (BaseAttributes)attributes;
            ba.setStyleClass(params[0]);
        }

        // If we are opening a Pane param[1] will be the name
        if (attributes instanceof PaneAttributes && params.length >= 2) {
            PaneAttributes pa = (PaneAttributes)attributes;
            pa.setName(params[1]);
        }

        // If its a Canvas then param[1] will be the page title,
        // param[2] the theme file name
        // param[3] the layout file name and
        // param[4] the type.
        if (attributes instanceof CanvasAttributes && params.length >= 5) {
            CanvasAttributes ca = (CanvasAttributes)attributes;
            ca.setPageTitle(params[1]);
            ca.setTheme(params[2]);
            ca.setLayoutName(params[3]);

            if (params[4] != null) {
                ca.setType(params[4]);
            }
        }

        // If we are opening a Region param[1] will be the name
        if (attributes instanceof RegionAttributes && params.length >= 2) {
            RegionAttributes ra = (RegionAttributes)attributes;
            ra.setName(params[1]);
        }

        // Process start element, this is the thing which does
        // the work.
        int result = element.elementStart(getCurrentRequestContext(), attributes);

        assertEquals("Open did not return PROCESS_ELEMENT_BODY as expected",
            result, PROCESS_ELEMENT_BODY);

        // Push the element on the stack so that we can close it later.
        elements.push(element);

    }

    /**
     * Close the named element.
     *
     * @param name Element name
     * @throws PAPIException
     */
    protected void close(String name) throws PAPIException {
        PAPIElementFactory factory =
            papiFactory.getPAPIElementFactory(name.toLowerCase());
        PAPIAttributes papiAttr = factory.createElementSpecificAttributes();

        PAPIElement element = (PAPIElement)elements.pop();
        int result = element.elementEnd(getCurrentRequestContext(), papiAttr);

        assertEquals("Close did not return CONTINUE_PROCESSING as expected",
            result, CONTINUE_PROCESSING);

    }


    /**
     * Write a string to the output buffer.
     *
     * @param message Massage to write.
     * @throws PAPIException
     */
    protected void writeMessage(String message) throws PAPIException {

        PAPIElement element = (PAPIElement)elements.peek();
        element.elementContent(getCurrentRequestContext(), null, message);
    }

    /**
     * Accessor method for Request Context.
     *
     * @return
     */
    protected MarinerRequestContext getCurrentRequestContext() {
        return getCurrentPageContext().getRequestContext();
    }

    /**
     * Accessor method for Page Context.
     * @return
     */
    protected RuntimeTestMarinerPageContext getCurrentPageContext() {
        return pageContext;
    }

    /**
     * Store a page context on the stack.
     *
     * @param newPageContext
     */
    void setCurrentPageContext(RuntimeTestMarinerPageContext newPageContext) {
        pageContext = newPageContext;
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	10330/4	pabbott	VBM:2005110907 Honour align with mode=nospace

 15-Nov-05	10326/7	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 27-Oct-05	10007/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 10-Oct-05	9727/1	ianw	VBM:2005100506 Fixed up remote repositories layout issues

 29-Sep-05	9600/3	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 02-Sep-05	9408/2	pabbott	VBM:2005083007 Move over to using JiBX accessor

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9331/8	gkoch	VBM:2005081603 InputStream -> Reader

 22-Aug-05	9331/5	gkoch	VBM:2005081603 InputStream -> Reader

 18-Aug-05	9331/2	gkoch	VBM:2005081603 vbm2005081603: refactoring to convert InputStreams into Readers

 22-Aug-05	9184/3	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 16-Aug-05	9287/1	gkoch	VBM:2005080509 vbm2005080509 applied review comments

 11-Aug-05	9187/1	tom	VBM:2005080509 release of 2005080509 [extract WML default styles into a CSS file] - third attempt

 10-Aug-05	9211/5	pabbott	VBM:2005080902 End to End CSS emulation test

 10-Aug-05	9211/3	pabbott	VBM:2005080902 End to End CSS emulation test

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 ===========================================================================
*/
