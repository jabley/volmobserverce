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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/DOMProtocolTestAbstract.java,v 1.9 2003/04/23 09:44:20 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Nov-02    Adrian          VBM:2002100404 - Created this class as a
 *                              testcase for DOMProtocol
 * 01-Dec-02    Phil W-S        VBM:2002112901 - Updated after refactoring.
 * 06-Jan-03    Chris W         VBM:2002121904 - Added testWriteImage.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and uses the
 *                              new shared Test... versions of classes rather
 *                              their own "cut & paste" inner classes.
 * 06-Mar-03    Sumit           VBM:2003022605 - Moved bufferToString() and
 *                              some static constants up into this class so
 *                              they are available to all child tests. Added
 *                              testSpatialFormatIterator()
 * 12-Mar-03    Mat             VBM:2003031203 - Added testDoMeta()
 * 13-Mar-03    Mat             VBM:2003031203 - Improved assertions in
 *                              testDoMeta()
 * 26-Mar-03    Allan           VBM:2003021803 - Modified usages of the
 *                              DeviceLayout no-arg constructor to use the
 *                              String, String version instead. Fixed @see
 *                              on testSpatialFormatIterator javadoc.
 * 28-Mar-03    Geoff           VBM:2003031711 - Refactored from
 *                              DOMProtocolTestCase so that this class is
 *                              abstract, required to add the new
 *                              testRenderAltText since the test requires a
 *                              real protocol to run against, and added the
 *                              bodgy checkTextEquals, checkElementEquals
 *                              methods which should really be in their own
 *                              object. L8r.
 * 28-Mar-03    Geoff           VBM:2003031711 - Remove some unnecessary code
 *                              I missed originally.
 * 09-Apr-03    Sumit           VBM:2003032713 - Tests for render support for
 *                              menu item groups
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Add tests for
 *                              addPhoneNumberContents.
 * 17-Apr-03    Geoff           VBM:2003041505 - Expanded static objects into
 *                              instance variables, made abstract as per name,
 *                              modifiedBufferToString to stop hiding
 *                              exceptions.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * 22-Apr-03    Geoff           VBM:2003040305 - Add to do comments.
 * 09-May-03    Byron           VBM:2003042205 - Updated
 *                              testSpatialFormatIterator to cater for changes
 *                              in XTHMLBasic.
 * 27-May-03    Sumit           VBM:2003032713 - Corrected menu separator test
 *                              case to render correct number of separator
 * 27-May-03    Byron           VBM:2003051904 - Added testDoMenu(),
 *                              testDoMenuNoAttributes and several
 *                              helper/template methods.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.XMLDeclaration;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.output.DocumentWriter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.mcs.dom.impl.DocumentImpl;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.integration.PageURLDetails;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.papi.ParagraphAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.forms.FormDataManager;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.forms.Link;
import com.volantis.mcs.protocols.forms.SessionFormData;
import com.volantis.mcs.protocols.layouts.FormFragmentInstance;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.mcs.servlet.MarinerServletApplication;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import junitx.util.PrivateAccessor;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

/**
 * This class unit test the DOMProtocolclass.
 *
 * @todo extract TestDOMProtocol and make the XHTMLTrans test cases use it too.
 * (In IDEA, Control-H on VolantisProtocol will show all subclasses...)
 */
public abstract class DOMProtocolTestAbstract
    extends VolantisProtocolTestAbstract {

    protected static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    protected CanvasLayout canvasLayout;

    protected static final String DEVICE_NAME = "myDeviceName";

    /**
     * Name of a style class for menus.
     */
    protected static String MENU_STYLE_CLASS = "menu_style_class";

    /**
     * Name of a style class for menu items
     */
    protected static String MENU_ITEM_STYLE_CLASS = "menu_item_style_class";

    /**
     * static StringWriter to capture the content of MyDOMOutputBuffer
     */
    private StringWriter stringWriter;

    /**
     * static MarinerRequestContext to return from MyMarinerPageContext
     */
    private MarinerRequestContext requestContext
        = new TestMarinerRequestContext();

    protected DOMProtocol protocol;

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    private DOMProtocolTestable testable;

    public DOMProtocolTestAbstract(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
                                       VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (DOMProtocol)protocol;
        this.testable = (DOMProtocolTestable)testable;
    }

    private void privateSetUp() throws Exception {
        canvasLayout = new CanvasLayout();

        protocol.setWriteHead(true);

        testable.setPageHead(new MyPageHead());

        MyPageOutputBuffer pageBuffer = new MyPageOutputBuffer();
        pageBuffer.initialise();
        testable.setPageBuffer(pageBuffer);

        stringWriter = new StringWriter();
    }

    /**
     * This method tests the method public void writeImage ( ImageAttributes )
     * for the com.volantis.mcs.protocols.DOMProtocol class.
     */
    public void testWriteImage()
        throws Exception {
        privateSetUp();
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        protocol.setMarinerPageContext(context);
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();
        context.setCurrentOutputBuffer(buffer);
        context.setCurrentPane(new Pane(new CanvasLayout()));

        CanvasLayout canvasLayout = new CanvasLayout();

        Pane pane = new Pane(canvasLayout);
        pane.setWidth("100");
        pane.setWidthUnits("pixels");

        ImageAttributes attributes = new ImageAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        
        attributes.setSrc("/path/to/image.gif");
        attributes.setWidth("100");
        attributes.setHeight("100");
        attributes.setConvertibleImageAsset(true);
        attributes.setPane(pane);

        // When we render a convertible image asset, we ignore the width
        // and height attributes
        protocol.writeImage(attributes);
        assertNull("width and height should be null for convertible image asset",
                   attributes.getWidth());
        assertNull("width and height should be null for convertible image asset",
                   attributes.getHeight());

        attributes = new ImageAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        attributes.setSrc("/path/to/image.gif");
        attributes.setWidth("100");
        attributes.setHeight("100");
        attributes.setConvertibleImageAsset(false);
        attributes.setPane(pane);

        // For normal image assets, the
        protocol.writeImage(attributes);
        assertNotNull("width and height should not be null for normal images",
                      attributes.getWidth());
        assertNotNull("width and height should not be null for normal images",
                      attributes.getHeight());
    }

    public void testWritePageHead() throws Exception {
        privateSetUp();
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        context.setBooleanDevicePolicyValue(DOMProtocol.SUPPORTS_JAVASCRIPT, true);
        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);

        protocol.writePageHead();
        stringWriter.flush();
        // NOTE: see to do below for why this looks so weird
        assertEquals("DOMProtocol.writePageHead generated incorrect output",
                     //            "<head>" +
                     "<style>.VF-1-1{background-color:red}</style>" +
                     //                "<script>" +
                     "document.open()"
                     //                "</script>" +
                     //            "</head>"
                     ,
                     stringWriter.toString());
    }

    public void testDoMeta() throws Exception {
        privateSetUp();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        protocol.setMarinerPageContext(pageContext);

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        MetaAttributes attributes = new MetaAttributes();
        attributes.setContent("Test & encoding");
        attributes.setHttpEquiv("refresh");
        attributes.setName("URL");

        protocol.doMeta(buffer, attributes);

        Element element = (Element)buffer.popElement().getHead();
        // We need to get the content string from the buffer as it
        // isn't encoded in the element.
        String result = bufferToString(buffer);
        int start = result.indexOf("content");
        int end = result.indexOf("ing\"", start);
        assertEquals("content=\"Test &amp; encoding\"", result.substring(start, end + 4));
        assertEquals("meta", element.getName());
        assertEquals("refresh", element.getAttributeValue("http-equiv"));
        assertEquals("URL", element.getAttributeValue("name"));
    }

    /**
     * Do the test by comparing the expected dom with the actual dom. Note that
     * the actual dom is converted to a string and reparsed in order to
     * guarentee that the strings being compared are as similar as possible.
     * @see #getDOMGeneratedString
     *
     * @param attributes the menuAttributes objects to be passed in for the
     *                  test.
     * @param buffer    the buffer containing the root element.
     * @param expected  the expected dom as a string.
     */
    protected void doTestDoMenu(MenuAttributes attributes,
                                DOMOutputBuffer buffer,
                                String expected)
        throws Exception {

        protocol.doMenu(attributes);

        // If the expected does not include a surrounding p tag,
        if (!expected.startsWith("<p")) {
            // then we must add one as the DOM transformer will do this for
            // the actual in the code below.
            expected = "<p>" + expected + "</p>";
        }

        // The expected dom as a string (wrapped in xml tags).
        expected = "<!DOCTYPE xml [ <!ENTITY nbsp \"&#160;\"> ]>" +
            expected;


        // The actual dom - constructed from the bufferRoot and wrapped in
        // an xml and form tag.
        Document document = domFactory.createDocument();


        // We require &nbsb; to be parse to place this doctype into the dom.
        Text node = domFactory.createText();
        node.setEncoded(true);
        node.append("<!DOCTYPE xml [ <!ENTITY nbsp \"&#160;\"> ]>");

        document.addNode(node);
        Element root = domFactory.createElement();
        root.setName("p");

        buffer.getRoot().addChildrenToHead(root);

        document.addNode(root);
        DOMTransformer domTransformer = protocol.getDOMTransformer();
        if (domTransformer != null) {
            document = domTransformer.transform(protocol, document);
        }

        String actual = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());

        final String expectedDOM = getDOMGeneratedString(expected);
        final String actualDOM = getDOMGeneratedString(actual);

        assertEquals("The processed DOM is not as expected.",
                     expectedDOM,
                     actualDOM);
    }

    /**
     * Provide a default set of menu attributes to be used for testing.
     *
     * @return      the newly created MenuAttributes.
     */
    protected MenuAttributes createMenuAttributes() {
        // An array of values to create menu items out of. The indices are:
        // 0: Text, e.g. 'Sports News'
        // 1: hRef, e.g. 'http://www.volantis.com:8080/volantis/sports.jsp'
        // 2: prompt, e.g. '<prompt><audio src=\"....</prompt>'
        // 3: shortcut, e.g. '9'
        String values[][] = {{
            "Sports News",
            "http://www.volantis.com:8080/volantis/sports.jsp",
            null,
            null
        }, {
            "Astrology",
            "http://www.volantis.com:8080/volantis/astrology.jsp",
            "<audio src=\"rtsp://www.volantis.com/mysticmeg.wav\">" +
            "Mystic Megs Astrology</audio>",
            null
        }, {
            "Fun and Games",
            "http://www.volantis.com:8080/volantis/games.jsp",
            null,
            "9"}
        };
        return createMenuAttributes(values);
    }

    /**
     * Create a MenuAttributes object from an array of menu item strings.
     *
     * @param  values the 2D array containing strings representing a menuitem.
     * @return        the newly created MenuAttributes object
     */
    private MenuAttributes createMenuAttributes(String[][] values) {
        MenuAttributes menuAttrs = new MenuAttributes();
        // We use Strings for errmsg, prompt, and help to avoid having to
        // resolve TextComponentIdentity which would require a repository
        menuAttrs.setId("menuId");
        menuAttrs.setErrmsg(new LiteralTextAssetReference(
                "<noinput>Sorry I did not hear you</noinput>" +
                "<nomatch>Sorry I did not understand you</nomatch>"));
        menuAttrs.setHelp(new LiteralTextAssetReference(
                "<help>Please select an option from the menu</help>"));
        menuAttrs.setPrompt(new LiteralTextAssetReference(
                "<prompt>Welcome home.<enumerate>For <value " +
                "expr=\"_prompt\"/>, press <value expr=\"_dtmf\"/> or say " +
                "<value expr=\"_prompt\"/></enumerate></prompt>"));

        for (int i = 0; i < values.length; i++) {
            MenuItem item = new MenuItem();
            item.setText(values[i][0]);
            item.setHref(values[i][1]);
            item.setPrompt(new LiteralTextAssetReference(values[i][2]));
            item.setShortcut(new LiteralTextAssetReference(values[i][3]));
            item.setTagName("menuitem");
            menuAttrs.addItem(item);
        }

        menuAttrs.setTagName("menu");

        menuAttrs.setPane(new Pane(new CanvasLayout()));
        return menuAttrs;
    }

    /**
     * Get the name of the menu element for the protocol under test.
     * @return The name of the menu element for the protocol under test.
     */
    protected abstract String getMenuElementName();

    /**
     * Tests the {@link DOMProtocol#getMenuRenderer} method
     * @throws Exception if an error occurs
     */
    public void testGetMenuRenderer() throws Exception {
        MenuAttributes attributes = new MenuAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        MenuRenderer renderer = protocol.getMenuRenderer(attributes);
        assertNull("getMenuRenderer should return a null MenuRenderer if the" +
                   "mariner-menu-link-style property has not been set",
                   renderer);
    }

    /**
     * Tests the {@link DOMProtocol#getMenuRenderer} method when the menu
     * has a stye that specifies "numeric-shortcut" style menus.
     * @throws Exception if an error occurs
     */
    public void testGetMenuRendererNumericShortcutStyle() throws Exception {
        MenuAttributes atts = new MenuAttributes();
        atts.setStyles(StylesBuilder.getCompleteStyles(
                "mcs-menu-link-style: numeric-shortcut"));
        // if the protocol supports numeric-shortcut style menus the
        // createNumericShortcutMenuRenderer factory method will return
        // the menu renderer for rendering shortcut menus. If the protocol
        // does not support shortcut menus the method will return null.
        MenuRenderer shortcutRenderer =
                protocol.createNumericShortcutMenuRenderer();

        // the getMenuRenderer method should return the same value as the
        // the protocols createNumericShortcutMenuRenderer
        MenuRenderer renderer = protocol.getMenuRenderer(atts);

        if (shortcutRenderer == null) {
            assertNull("getMenuRenderer should return a null renderer",
                       renderer);
        } else {
            assertEquals("getMenuRenderer should return a " +
                         "the same menu renderer as returned by the " +
                         "createNumericShortcutMenuRenderer method",
                         shortcutRenderer.getClass(),
                         renderer.getClass());
        }
    }

    protected void checkDoProtocolString(
            DOMProtocol protocol, String expectedProtocolString)
            throws IOException {

        Document document = new DocumentImpl(domFactory);
        protocol.writeProtocolString(document);

        StringWriter writer = new StringWriter();
        DocumentWriter documentWriter = new XMLDocumentWriter(writer);

        XMLDeclaration declaration = document.getDeclaration();
        if (declaration != null) {
            documentWriter.outputXMLDeclaration(declaration);
        }

        DocType docType = document.getDocType();
        if (docType != null) {
            documentWriter.outputDocType(docType);
        }

        assertEquals("Protocol string should match",
                expectedProtocolString,
                writer.getBuffer().toString());
    }

    /**
     * A specialisation of PageHead to fake the methods and members to
     * test DOMProtocol.writePageHead()
     */
    protected class MyPageHead extends PageHead {
        /**
         * fake the head buffer
         */
        private DOMOutputBuffer head;

        /**
         * fake the script buffer
         */
        private DOMOutputBuffer script;

        /**
         * Construct the fake PageHead
         */
        public MyPageHead() {
            super();
            head = new TestDOMOutputBuffer();

            script = new TestDOMOutputBuffer();
            try {
                script.getWriter().write("document.open()");
            } catch (IOException ioe) {
                throw new RuntimeException(ioe.getMessage());
            }
        }

        public void writeCssCandidates(VolantisProtocol protocol)
            throws IOException {
            head.getWriter().write("<style>.VF-1-1{background-color:red}</style>");
        }

        /**
         * get the head buffer
         * @return the head buffer
         */
        public OutputBuffer getHead() {
            return head;
        }

        /**
         * get the script buffer
         * @return the script buffer.
         */
        public OutputBuffer getScript() {
            return script;
        }
    }

    /**
     * Specialisation of DOMOutputBuffer to allow us to return a writer from
     * which we can extract the content.
     *
     * @todo should get this to use TestDOMOutputBuffer instead.
     */
    private class MyPageOutputBuffer extends DOMOutputBuffer {
        // Javadoc inherited from super class.
        public Writer getWriter() {
            return stringWriter;
        }

        public boolean isEmpty() {
            return false;
        }

        /**
         * Add the contents of the output buffer.
         */
        public void addOutputBuffer(DOMOutputBuffer buffer) {
            StringWriter writer = (StringWriter)buffer.getWriter();
            stringWriter.write(writer.toString());
        }
    }

    /**
     * Utility method that returns a string from an DOMOutputBuffer
     * @param buffer the DOMOutputBuffer
     * @return a string representation of the DOMOutputBuffer
     */
    protected String bufferToString(DOMOutputBuffer buffer) {
        String result = null;
        Document doc = domFactory.createDocument();
        doc.addNode(buffer.getRoot());
        try {
            result = DOMUtilities.toString(doc, protocol.getCharacterEncoder());
            //System.out.println("buffer = " + result);
        } catch (Exception e) {
            // @todo change this to throw Exception and change all it's clients
            // to do the same.
            throw new RuntimeException(
                e.getClass().getName() + ":" + e.getMessage());
        }
        return result;
    }

    /**
     * This tests the execution of the spatial format iterator.
     * We are only interested in seeing that when the methods execute in the
     * correct order (if they are overridden or not) they create a valid DOM
     *
     * E.g. XHTMLBasic ovverides openSpatialFormatIterator to create a &lt;table&gt;
     * element. We must ensure that on writeCloseSpatialFormatIterator the
     * close element call is a &lt;/table&gt; and not something else
     * @see com.volantis.mcs.protocols.html.XHTMLBasic
     */
    public void testSpatialFormatIterator() throws Exception {
        privateSetUp();

        MarinerRequestContext requestContext
            = new TestMarinerRequestContext();
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        protocol.setMarinerPageContext(context);
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();
        context.setCurrentOutputBuffer(buffer);
        InternalDevice d = INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice("Netscape4", new HashMap(), null));
        context.setDevice(d);
        context.setDeviceName("Netscape4");

        // Activate the device layout.
        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout1 =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);
        RuntimeDeviceLayout runtimeDeviceLayout =
                runtimeDeviceLayout1;

        TestDeviceLayoutContext deviceLayoutContext = new TestDeviceLayoutContext();
        deviceLayoutContext.setDeviceLayout(runtimeDeviceLayout);
        context.pushDeviceLayoutContext(deviceLayoutContext);

        protocol.writeOpenSpatialFormatIterator(
            getSpatialFormatIteratorAttributes());
        protocol.writeOpenSpatialFormatIteratorRow(
            getSpatialFormatIteratorAttributes());
        protocol.writeOpenSpatialFormatIteratorChild(
            getSpatialFormatIteratorAttributes());
        protocol.writeCloseSpatialFormatIteratorChild(
            getSpatialFormatIteratorAttributes());
        protocol.writeCloseSpatialFormatIteratorRow(
            getSpatialFormatIteratorAttributes());
        protocol.writeCloseSpatialFormatIterator(
            getSpatialFormatIteratorAttributes());
    }

    /**
     * Return a property filled SpatialFormatIteratorAttributes for testing
     * @return SpatialFormatIteratorAttributes
     */
    protected SpatialFormatIteratorAttributes getSpatialFormatIteratorAttributes() {
        SpatialFormatIteratorAttributes attr = new SpatialFormatIteratorAttributes();
        final SpatialFormatIterator spatial = new SpatialFormatIterator(canvasLayout);
        spatial.setColumns(1);
        attr.setFormat(spatial);
        attr.setStyles(StylesBuilder.getDeprecatedStyles());
        return attr;
    }

    public void testAddPhoneNumberContentsNull() throws Exception {
        // Use null content
        doPhoneNumberContentTest(null,
                                 phoneNumber,
                                 "null");
    }

    public void testAddPhoneNumberContentsEmptyDom() throws Exception {
        // Use empty DOMOutputBuffer content
        DOMOutputBuffer contentDom = new DOMOutputBuffer();
        contentDom.initialise();

        doPhoneNumberContentTest(contentDom,
                                 phoneNumber,
                                 "empty DOM");
    }

    /**
     * Test the URLRewriting aspect of doFormLink.
     */
    public void testDoFormLinkURLRewriting() throws Throwable {
        CanvasLayout layout = new CanvasLayout();
        Form form = new Form(layout);
        final String formName = "form";
        final String fragmentName = "formFragment";
        form.setName(formName);

        FormInstance formInstance = new FormInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        formInstance.setFormat(form);

        final PolicyReferenceResolverMock referenceResolverMock =
                new PolicyReferenceResolverMock("referenceResolverMock",
                        expectations);
        referenceResolverMock.expects.
                resolveQuotedTextExpression(fragmentName).returns(null);
        MarinerApplication application = new MarinerServletApplication();
        MarinerConfiguration config = new MarinerConfiguration();
        Volantis volantis = getVolantis();
        PrivateAccessor.setField(volantis, "marinerConfig", config);
        PrivateAccessor.invoke(volantis, "initializeURLRewriters",
                new Class [] {MarinerApplication.class},
                new Object [] { application });

        final String testURL = "http://test/url";

        PageURLRewriter layoutURLRewriter = new PageURLRewriter() {
            public MarinerURL rewriteURL(MarinerRequestContext context,
                                         MarinerURL url,
                                         PageURLDetails details) {
                return new MarinerURL(testURL);
            }
        };
        PrivateAccessor.setField(volantis, "layoutURLRewriter",
                layoutURLRewriter);

        TestMarinerPageContext context = new TestMarinerPageContext();
        context.setVolantis(volantis);
        context.setRequestURL(new MarinerURL("http://a.url"));

        FormDescriptor fd = new FormDescriptor();
        fd.setName(formName);
        final FormDataManager formDataManager = context.getFormDataManager();
        String formSpecifier = formDataManager.getFormSpecifier(fd);

        context.setPolicyReferenceResolver(referenceResolverMock);

        context.pushDeviceLayoutContext(new DeviceLayoutContext());
        PageGenerationCache pageGenerationCache = new PageGenerationCache();
        pageGenerationCache.createFormFragmentationStates(form.getName());
        context.setPageGenerationCache(pageGenerationCache);
        protocol.setMarinerPageContext(context);
        if(protocol.getPageHead()==null) {
            protocol.initialisePageHead();
        }
        context.setProtocol(protocol);

        TestDeviceLayoutContext dlc = new TestDeviceLayoutContext();
        Pane pane = new Pane(layout);
        FormFragmentInstance ffInstance = new FormFragmentInstance(
                NDimensionalIndex.ZERO_DIMENSIONS) {
            protected boolean isEmptyImpl() {
                return false;
            }

        };
        pane.setName("pane");

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(layout);

        context.setDeviceLayout(runtimeDeviceLayout);
        context.setDevice(InternalDeviceTestHelper.createTestDevice());
        context.setCurrentPane(pane);
        dlc.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS,
                ffInstance);
        dlc.setMarinerPageContext(context);
        dlc.setDeviceLayout(runtimeDeviceLayout);
        //dlc.initialise();
        context.pushDeviceLayoutContext(dlc);
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        ContextInternals.setEnvironmentContext(requestContext, new TestEnvironmentContext());
        context.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, context);

        DOMOutputBuffer dom = new DOMOutputBuffer();
        dom.initialise();
        XFFormAttributes attributes = new XFFormAttributes();
        final Styles styles = StylesBuilder.getInitialValueStyles();
        attributes.setStyles(styles);
        attributes.setFormData(formInstance);
        attributes.setAction(new LiteralLinkAssetReference("testaction"));
        attributes.setMethod("post");
        attributes.setFormSpecifier(formSpecifier);
        attributes.setFormDescriptor(fd);
        FormFragment formFragment = new FormFragment(layout);
        formFragment.setName(fragmentName);
        FormFragmentInstance formFragmentInstance = new FormFragmentInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        formFragmentInstance.setFormat(formFragment);
        Link link = new Link(fragmentName, URLConstants.NEXT_FORM_FRAGMENT, styles);
        link.setFormFragment(formFragmentInstance);

        protocol.doFormLink(dom, attributes, link);

        SessionFormData retrievedFormData =
                formDataManager.getSessionFormData(formSpecifier);
        assertEquals("Unexpected FormFragmentData in the SessionContext " +
                fragmentName + " field.", testURL,
                retrievedFormData.getFieldValue(URLConstants.NEXT_FORM_FRAGMENT));
    }

    public void testAddPhoneNumberContentsPopulatedDom() throws Exception {
        // Use populated DOMOutputBuffer content
        DOMOutputBuffer contentDom = new DOMOutputBuffer();
        contentDom.initialise();
        contentDom.openElement("content");
        contentDom.addElement("example");
        contentDom.appendEncoded("with text");
        contentDom.closeElement("content");

        doPhoneNumberContentTest(contentDom,
                                 "<content>" +
                                 "<example/>" +
                                 "with text" +
                                 "</content>",
                                 "DOM");
    }

    public void testAddPhoneNumberContentsString() throws Exception {
        doPhoneNumberContentTest("example with text",
                                 "example with text",
                                 "string");
    }

    protected void doPhoneNumberContentTest(Object content,
                                            String expected,
                                            String messageContext)
        throws Exception {
        Volantis volantis = getVolantis();
        TestMarinerPageContext context = new TestMarinerPageContext();
        XMLReader reader = DOMUtilities.getReader();
        DOMOutputBuffer dom = new DOMOutputBuffer();

        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setVolantis(volantis);
        context.setCurrentOutputBuffer(dom);

        PhoneNumberAttributes attributes =
            createPhoneNumberAttributes(phoneNumberReference);

        protocol.supportsDiallingLinks = true;

        protocol.diallingLinkInfoType =
            VolantisProtocol.DiallingLinkInfoType.PREFIX;

        protocol.diallingLinkInfo = phoneNumberPrefix;

        protocol.resolvePhoneNumberAttributes(attributes);

        dom.initialise();

        dom.openStyledElement("test", attributes);
        attributes.setContent(content);
        protocol.addPhoneNumberContents(dom, attributes);
        dom.closeElement("test");

        assertEquals("Addition of " + messageContext + " content not as",
                     DOMUtilities.toString(
                         DOMUtilities.read(
                             reader,
                             "<test>" +
                             ((expected == null) ? "" : expected) +
                             "</test>"),
                         protocol.getCharacterEncoder()),
                     DOMUtilities.toString(
                             dom.getRoot(), protocol.getCharacterEncoder()));
    }

    public void testRenderAltText() throws Exception {
        // Initialise the protocol.
        Volantis volantis = getVolantis();
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.setVolantis(volantis);
        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setDevice(InternalDeviceTestHelper.createTestDevice());
        protocol.initialiseCanvas();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        context.setCurrentOutputBuffer(buffer);

        MCSAttributes attributes = new AltTextAttributes(
            new ParagraphAttributes());
        String altText = "some alt text";
        protocol.renderAltText(altText, attributes);

        checkRenderAltText(altText, buffer);
    }

    protected void checkRenderAltText(String altText, DOMOutputBuffer buffer) {
        // Default behaviour is enclose the alt text in a P.
        // This is not necessarily the best since P introduces extra
        // whitespace, but thats how it used to work when it was hardcoded in
        // the PAPI so thats how it is here for now.
        Element root = buffer.getCurrentElement();
        Element p = checkElementEquals("p", root.getHead());
        checkTextEquals(altText, p.getHead());
    }

    // @todo These bodgy static methods should be refactored into an object
    // which allows us to inspect the contents of DOMOutputBuffers.
    // Something like DOMBufferInspector or something?
    // @todo these compete with similar things in DOMUtilities
    // more motivation for a refactoring...

    /**
     * Ensure that the actual Node provided is a Text node and that it
     * contains the provided expected text.
     *
     * @param expectedText
     * @param actualNode
     *
     * @return the Node as a Text.
     */
    protected static Text checkTextEquals(String expectedText,
                                          Node actualNode) {
        assertTrue("Node not a Text: " + actualNode.getClass().getName(),
                   actualNode instanceof Text);
        Text text = (Text)actualNode;
        assertEquals(expectedText,
                     new String(text.getContents(), 0, text.getLength()));
        return text;
    }

    /**
     * Ensure that the actual Node provided is an Element node and that it's
     * name matches the provided expected name.
     *
     * @param expectedName
     * @param actualNode
     *
     * @return the Node as an Element.
     */
    protected static Element checkElementEquals(String expectedName,
                                                Node actualNode) {
        // These two lines should be a method.
        assertTrue("Node not an Element: " + actualNode.getClass().getName(),
                   actualNode instanceof Element);
        Element element = (Element)actualNode;
        assertEquals(expectedName, element.getName());
        return element;
    }

    /**
     * Interface used to invoke the tests for open and close native markup
     * methods. This allows a common test setup to be used for the two types
     * of test.
     */
    protected static interface NativeMarkupTest {
        public void execute(OutputBufferFactory factory,
                            TestMarinerPageContext pageContext,
                            PageHead pageHead,
                            DeviceLayoutContext dlc,
                            DOMOutputBuffer buffer) throws Exception;
    }

    /**
     * Tests {@link DOMProtocol#writeOpenNativeMarkup} if the given protocol
     * supports native markup.
     */
    public void testWriteOpenNativeMarkup() throws Exception {
        doNativeMarkupTest(new NativeMarkupTest() {
            public void execute(OutputBufferFactory factory,
                                TestMarinerPageContext pageContext,
                                PageHead pageHead,
                                DeviceLayoutContext dlc,
                                DOMOutputBuffer buffer) throws Exception {
                if (protocol.supportsNativeMarkup()) {
                    doWriteOpenNativeMarkupTest(factory,
                                                pageContext,
                                                pageHead,
                                                dlc);
                }
            }
        });
    }

    /**
     * Tests {@link DOMProtocol#writeCloseNativeMarkup} if the given protocol
     * supports native markup.
     */
    public void testWriteCloseNativeMarkup() throws Exception {
        doNativeMarkupTest(new NativeMarkupTest() {
            public void execute(OutputBufferFactory factory,
                                TestMarinerPageContext pageContext,
                                PageHead pageHead,
                                DeviceLayoutContext dlc,
                                DOMOutputBuffer buffer) throws Exception {
                if (protocol.supportsNativeMarkup()) {
                    doWriteCloseNativeMarkupTest(pageContext,
                                                 pageHead,
                                                 dlc,
                                                 buffer);
                }
            }
        });
    }

    /**
     * The test setup for the write open and close native markup methods.
     *
     * @param test the actual test to be executed
     */
    public void doNativeMarkupTest(NativeMarkupTest test) throws Exception {
        Volantis volantis = new Volantis();
        TestMarinerPageContext context = new TestMarinerPageContext();
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();
        PageHead pageHead = new PageHead();
        TestDeviceLayoutContext dlc = new TestDeviceLayoutContext();
        OutputBufferFactory factory = new TestDOMOutputBufferFactory();
        CanvasLayout canvasLayout = new CanvasLayout();
        Pane pane = new Pane(canvasLayout);
        PaneInstance paneInstance = new TestPaneInstance();

        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setVolantis(volantis);

        buffer.initialise();

        context.setCurrentOutputBuffer(buffer);
        testable.setPageHead(pageHead);
        testable.setCurrentBuffer(null, buffer);

//        pageHead.setMarinerPageContext(context);
        pageHead.setOutputBufferFactory(factory);

        pane.setName("pane");

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        context.setDeviceLayout(runtimeDeviceLayout);
        context.setDevice(InternalDeviceTestHelper.createTestDevice());
        context.setCurrentPane(pane);
        dlc.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS,
                paneInstance);
        dlc.setMarinerPageContext(context);
        dlc.setDeviceLayout(runtimeDeviceLayout);
        dlc.initialise();
        context.pushDeviceLayoutContext(dlc);

        test.execute(factory,
                     context,
                     pageHead,
                     dlc,
                     buffer);
    }

    /**
     * Should be augmented by test case specializations for protocol
     * specializations that augment the
     * {@link DOMProtocol#getNativeMarkupOutputBuffer} method.
     *
     * @param factory     the output buffer factory used in the tests
     * @param pageContext the page context used in the tests
     * @param pageHead    the page head used in the tests
     * @param dlc         the device layout context used in the tests
     */
    protected void doWriteOpenNativeMarkupTest(
        OutputBufferFactory factory,
        TestMarinerPageContext pageContext,
        PageHead pageHead,
        DeviceLayoutContext dlc) throws Exception {
        NativeMarkupAttributes attributes = new NativeMarkupAttributes();
        attributes.setPane("pane");
        attributes.setTargetLocation("pane");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for pane",
                     protocol.getMarinerPageContext().getCurrentOutputBuffer(),
                     pageContext.getCurrentOutputBuffer());

        attributes.resetAttributes();
        attributes.setTargetLocation("here");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for here",
                     protocol.getMarinerPageContext().getCurrentOutputBuffer(),
                     pageContext.getCurrentOutputBuffer());
    }

    /**
     * Should be augmented by test case specializations for protocol
     * specializations that augment the
     * {@link DOMProtocol#getNativeMarkupOutputBuffer} method.
     *
     * @param pageContext the page context used in the tests
     * @param pageHead    the page head used in the tests
     * @param dlc         the device layout context used in the tests
     * @param buffer      the initial output buffer used in the tests
     */
    protected void doWriteCloseNativeMarkupTest(
        TestMarinerPageContext pageContext,
        PageHead pageHead,
        DeviceLayoutContext dlc,
        DOMOutputBuffer buffer) throws Exception {
        NativeMarkupAttributes attributes = new NativeMarkupAttributes();
        attributes.setPane("pane");
        attributes.setTargetLocation("pane");

        pageContext.pushOutputBuffer(buffer);

        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for pane",
                     protocol.getMarinerPageContext().getCurrentOutputBuffer(),
                     pageContext.getCurrentOutputBuffer());
        protocol.writeCloseNativeMarkup(attributes);

        // Check that output buffers have been removed from stack
        assertEquals("after closeNativeMarkup wrong output buffer on stack",
                     buffer, pageContext.getCurrentOutputBuffer());

        attributes.resetAttributes();
        attributes.setTargetLocation("here");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for here",
                     protocol.getMarinerPageContext().getCurrentOutputBuffer(),
                     pageContext.getCurrentOutputBuffer());
        protocol.writeCloseNativeMarkup(attributes);

        // Check that output buffers have been removed from stack
        assertEquals("after closeNativeMarkup wrong output buffer on stack",
                     buffer, pageContext.getCurrentOutputBuffer());
    }

    protected void checkFragmentLinkRendererContext(
            FragmentLinkRendererContext context) {
        assertTrue(context instanceof DOMFragmentLinkRendererContext);
    }


    /**
     * Private class that returns the current dom buffer for any format instance
     * reference passed to it
     */
    class MenuSeparatorTestContext extends TestPaneInstance {

        private OutputBuffer mBuffer;

        public void setOutputBuffer(OutputBuffer buffer) {
            mBuffer = buffer;
        }

        /* (non-Javadoc)
        * @see com.volantis.mcs.protocols.layouts.AbstractPaneInstance#getCurrentBuffer(com.volantis.mcs.protocols.NDimensionalIndex, boolean)
        */
        public OutputBuffer getCurrentBuffer(boolean create) {
            return mBuffer;
        }

        /* (non-Javadoc)
         * @see com.volantis.mcs.protocols.layouts.AbstractPaneInstance#getCurrentBuffer(com.volantis.mcs.protocols.NDimensionalIndex)
         */
        public OutputBuffer getCurrentBuffer() {
            return mBuffer;
        }

    }

    /**
     * Override to output the expected and actual values.
     */
    public void assertXMLEquals(String message, String expected, String actual)
            throws SAXException, ParserConfigurationException, IOException {

        System.out.println("Message: " + message);
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);

        super.assertXMLEquals(message, expected, actual);
    }

    /**
     * Tests if writing out a pre element keeps white-spaces.
     */
    public void testPre() throws Exception {
        final DOMOutputBuffer rootBuffer = new TestDOMOutputBuffer();
        final MarinerPageContext context = protocol.getMarinerPageContext();
        context.pushOutputBuffer(rootBuffer);
        final PreAttributes attributes = new PreAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());
        final EmphasisAttributes emphasisAttrs = new EmphasisAttributes();

        protocol.writeOpenPre(attributes);
        final DOMOutputBuffer buffer =
            (DOMOutputBuffer) context.getCurrentOutputBuffer();
        assertNotEquals(rootBuffer, buffer);

        buffer.writeText("     before     ");
        protocol.writeOpenEmphasis(emphasisAttrs);
        context.getCurrentOutputBuffer().writeText("     child     text     ");
        protocol.writeCloseEmphasis(emphasisAttrs);
        buffer.writeText("     after     ");
        protocol.writeClosePre(attributes);

        // check the result
        checkResultForPre(rootBuffer);
    }

    /**
     * Checks the result of the pre test.
     * @param buffer the buffer that contains the result
     */
    protected void checkResultForPre(final DOMOutputBuffer buffer) {
        final Text beforeText = getContentHeadForPre(buffer);
        assertEquals("     before     ",
            new String(beforeText.getContents(), 0, beforeText.getLength()));
        final Element emElement = (Element) beforeText.getNext();
        final Text emText = (Text) emElement.getHead();
        assertEquals("     child     text     ",
            new String(emText.getContents(), 0, emText.getLength()));
        assertNull(emText.getNext());
        final Text afterText = (Text) emElement.getNext();
        assertEquals("     after     ",
            new String(afterText.getContents(), 0, afterText.getLength()));
    }

    /**
     * Returns the content that needs to be checked for the testPre test.
     *
     * <p>By default the content is inside the pre element.</p>
     *
     * @param buffer - the buffer that contains the content.
     * @return the first (text) node of the content
     */
    protected Text getContentHeadForPre(final DOMOutputBuffer buffer) {
        final Element preElement = (Element) buffer.getRoot().getHead();
        return (Text) preElement.getHead();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/4	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/3	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 09-Jun-05	8665/7	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 08-Jun-05	7997/2	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 07-Jun-05	8637/2	pcameron	VBM:2005050402 Fixed quoting of string style values and font-family values

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 17-Feb-05	6957/4	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 16-Feb-05	6129/14	matthew	VBM:2004102019 yet another supermerge

 14-Jan-05	6346/1	geoff	VBM:2004110112 Certain form layouts generate invalid WML

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 26-Nov-04	6076/5	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	6253/1	claire	VBM:2004111704 mergevbm: Handle portal themes correctly and remove caching of themes and emulation in protocols

 19-Nov-04	6236/1	claire	VBM:2004111704 Handle portal themes correctly and remove caching of themes and emulation in protocols

 05-Nov-04	6112/3	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 03-Nov-04	5871/2	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/2	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 26-Oct-04	5877/2	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 02-Jul-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 28-Jun-04	4733/2	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 07-May-04	4164/4	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 30-Apr-04	4124/2	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 06-May-04	3272/2	philws	VBM:2004021117 Fix merge issues

 30-Apr-04	4124/2	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 05-May-04	4167/3	steve	VBM:2004042901 Fix up javadoc

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 04-May-04	4117/2	steve	VBM:2004042901 Style class rendering fix

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 16-Apr-04	3834/4	steve	VBM:2004041306 deprecate StringProtocol fragmentation

 14-Apr-04	3834/1	steve	VBM:2004041306 Converted MMLBasic to a DOMProtocol derived protocol

 16-Apr-04	3362/4	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 23-Mar-04	3512/3	allan	VBM:2004032205 MCS performance enhancements.

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 02-Oct-03	1469/4	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 02-Oct-03	1469/2	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 23-Sep-03	1412/5	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (rework to get link style from source fragment)

 17-Sep-03	1412/2	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 22-Sep-03	1394/4	doug	VBM:2003090902 centralised common openwave menu rendering code

 17-Sep-03	1394/2	doug	VBM:2003090902 added support for openwave numeric shortcut menus

 10-Sep-03	1386/1	philws	VBM:2003090801 (X)HTML support for native markup

 10-Sep-03	1379/2	philws	VBM:2003090801 (X)HTML support for native markup

 21-Aug-03	1052/8	allan	VBM:2003073101 Wrap each menu item within a menu if required

 17-Aug-03	1052/4	allan	VBM:2003073101 Support styles on menu and menuitems

 04-Jul-03	706/1	allan	VBM:2003070302 Added TestSuiteGenerator ant task. Run testsuite in a single jvm

 ===========================================================================
*/
