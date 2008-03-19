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

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.AddressAttributes;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.BlockQuoteAttributes;
import com.volantis.mcs.protocols.CaptionAttributes;
import com.volantis.mcs.protocols.CiteAttributes;
import com.volantis.mcs.protocols.CodeAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DefinitionDataAttributes;
import com.volantis.mcs.protocols.DefinitionListAttributes;
import com.volantis.mcs.protocols.DefinitionTermAttributes;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.EmphasisAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.KeyboardAttributes;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.NavigationListAttributes;
import com.volantis.mcs.protocols.OrderedListAttributes;
import com.volantis.mcs.protocols.OutputBufferFactoryMock;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.PreAttributes;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.protocols.SampleAttributes;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.StrongAttributes;
import com.volantis.mcs.protocols.SubscriptAttributes;
import com.volantis.mcs.protocols.SuperscriptAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolMock;
import com.volantis.mcs.protocols.layouts.ContainerInstanceImplMock;
import com.volantis.mcs.protocols.widgets.WidgetModuleMock;
import com.volantis.mcs.protocols.widgets.renderers.DynamicMenuWidgetRendererMock;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.MCSMenuStyleKeywords;
import com.volantis.mcs.xdime.XDIMEContentHandler;
import com.volantis.mcs.xdime.XDIMEContextFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElement;
import com.volantis.mcs.xdime.XDIMEElementHandler;
import com.volantis.mcs.xdime.XDIMEElementImpl;
import com.volantis.mcs.xdime.XDIMEElementInternal;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.DocumentValidator;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMock;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StylingEngineMock;
import com.volantis.styling.values.MutablePropertyValuesMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Test the XHTML2 elements.
 */
public class XHTML2ElementsTestCase
        extends TestCaseAbstract {

    private XDIMEContentHandler handler = null;

    private MarinerPageContextMock pageContextMock = null;
    private VolantisProtocolMock protocolMock = null;

    private org.xml.sax.Attributes emptyAttributes = new AttributesImpl();
    private StylingEngineMock stylingEngineMock;
    private XDIMEContextInternal xdimeContext;

    private void setupStyleMocks(
            String elementName, boolean requiresStyling, Styles styles) {
        
        stylingEngineMock = new StylingEngineMock(
                "stylingEngineMock",
                expectations);

        if (requiresStyling) {
            addStylingExpectations(elementName);

            stylingEngineMock.expects.getStyles().returns(styles).any();
        }        
    }

    private void setupStyleMocks(
            String elementName, boolean requiresStyling) {
        setupStyleMocks(elementName, requiresStyling, null);
    }

    private void addStylingExpectations(String elementName) {
        stylingEngineMock.fuzzy
                .startElement(
                        XDIMESchemata.XHTML2_NAMESPACE,
                        elementName,
                        mockFactory.expectsInstanceOf(Attributes.class));
        stylingEngineMock.expects
                .endElement(
                        XDIMESchemata.XHTML2_NAMESPACE,
                        elementName);
    }
    
    private MarinerRequestContextMock setupContextMocks() {

            ProtocolConfigurationMock protocolConfig =
                    new ProtocolConfigurationMock(
                        "protocolConfig", expectations);

            protocolMock =
                    new VolantisProtocolMock(
                            "protocolMock", expectations,
                            protocolConfig);

        pageContextMock = new MarinerPageContextMock(
                "pageContextMock", expectations);

        if (stylingEngineMock != null) {
            pageContextMock.expects.getStylingEngine().returns(
                    stylingEngineMock).any();
        }
        pageContextMock.expects.getProtocol().returns(protocolMock).any();

        pageContextMock.expects.enteringXDIMECPElement().any();
        pageContextMock.expects.exitingXDIMECPElement().any();

        final ContainerInstanceImplMock containerInstanceMock =
                new ContainerInstanceImplMock(
                        "containerInstanceMock", expectations,
                        NDimensionalIndex.ZERO_DIMENSIONS);

        pageContextMock.expects.getCurrentContainerInstance()
                .returns(containerInstanceMock).any();

        MarinerRequestContextMock requestContextMock =
                new MarinerRequestContextMock(
                        "requestContextMock", expectations);

        requestContextMock.expects.getMarinerPageContext().returns(
                pageContextMock).any();

        return requestContextMock;
    }

    /**
     * Setup all mock objects which are not test specific.
     *
     * @param elementName
     * @return
     */
    private MarinerRequestContextMock setupMocks(
            String elementName, Styles styles) {

        setupStyleMocks(elementName, true, styles);

        MarinerRequestContextMock requestContextMock = setupContextMocks();

        return requestContextMock;

    }

    /**
     * Setup all mock objects which are not test specific.
     *
     * @param elementName
     * @param requiresStyling
     * @return
     */
    private MarinerRequestContextMock setupMocks(
            String elementName, boolean requiresStyling) {

        setupStyleMocks(elementName, requiresStyling);

        MarinerRequestContextMock requestContextMock = setupContextMocks();

        return requestContextMock;

    }

    /**
     * Setup non-mock objects for the tests to run.
     *
     * @param requestContext
     */
    private void setupEnvironment(MarinerRequestContext requestContext) {

        xdimeContext = (XDIMEContextInternal) XDIMEContextFactory.getDefaultInstance()
                .createXDIMEContext();

        xdimeContext.setInitialRequestContext(requestContext);

        handler =
                new XDIMEContentHandler(
                        null, xdimeContext,
                        XDIMEElementHandler.getDefaultInstance());
    }

    /**
     * Generic method to invoke start and end element methods.
     *
     * @param elementName
     * @throws SAXException
     */
    private void callStartAndEndForDocumentAndElement(String elementName)
            throws SAXException {

        callStartDocumentAndStartElement(elementName);

        callEndElementAndEndDocument(elementName);
    }

    /**
     * Drive the creation of a new element and the calling of start on the new
     * element.
     *
     * @param elementName
     * @throws SAXException
     */
    private void callStartDocumentAndStartElement(String elementName)
            throws SAXException {

        startDocument();

        startElement(elementName);
    }

    /**
     * Start the XDIME element.
     *
     * @param elementName name of element to create and start.
     * @param attr
     * @throws SAXException
     */
    private void startElement(String elementName, org.xml.sax.Attributes attr)
            throws SAXException {

        handler.startElement(
                XDIMESchemata.XHTML2_NAMESPACE, elementName, null,
                attr);
    }

    /**
     * Start the XDIME element.
     *
     * @param elementName name of element to create and start.
     * @throws SAXException
     */
    private void startElement(String elementName)
            throws SAXException {

        handler.startElement(
                XDIMESchemata.XHTML2_NAMESPACE, elementName, null,
                emptyAttributes);
    }

    /**
     * Drive the end element and document methods.
     *
     * @param elementName
     * @throws SAXException
     */
    private void callEndElementAndEndDocument(String elementName)
            throws SAXException {

        endElement(elementName);

        endDocument();
    }

    /**
     * End the XDIME element.
     *
     * @param elementName
     * @throws SAXException
     */
    private void endElement(String elementName)
            throws SAXException {
        handler.endElement(XDIMESchemata.XHTML2_NAMESPACE, elementName, null);
    }    

    /**
     * Start the XDIME document.
     *
     * @throws SAXException
     */
    private void startDocument()
            throws SAXException {
        handler.startDocument();
    }

    /**
     * End the XDIME document.
     *
     * @throws SAXException
     */
    private void endDocument()
            throws SAXException {
        handler.endDocument();
    }

    // -----------------------------------------------------------------------
    // Test methods, one per XHTML2 element
    // Each checks that a call to the handler results in the correct
    // protocol methods being called.
    // -----------------------------------------------------------------------

    public void testAbbrElementCallsProtocol()
            throws Exception {

        String elementName = "abbr";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenSpan(
                mockFactory.expectsInstanceOf(SpanAttributes.class));
        protocolMock.fuzzy.writeCloseSpan(
                mockFactory.expectsInstanceOf(SpanAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    private void pushElement(final XDIMEElement element)
            throws Exception {

        XDIMEElementImpl elementImpl = (XDIMEElementImpl) element;
        // Normally the ElementOutputStateBuilder would be configured when
        // processing the element, but because this element isn't processed we
        // need to manually set the context and create the output state.
        elementImpl.getOutputState();

        DocumentValidator validator = xdimeContext.getDocumentValidator();
        validator.open(elementImpl.getElementType());
        xdimeContext.pushElement(element);
    }

    public void testAddressElementCallsProtocol() throws Exception {

        String elementName = "address";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenAddress(
                mockFactory.expectsInstanceOf(AddressAttributes.class));
        protocolMock.fuzzy.writeCloseAddress(
                mockFactory.expectsInstanceOf(AddressAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testAnchorElementCallsProtocol() throws Exception {

        String elementName = "a";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenAnchor(
                mockFactory.expectsInstanceOf(AnchorAttributes.class));
        protocolMock.fuzzy.writeCloseAnchor(
                mockFactory.expectsInstanceOf(AnchorAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testBlockQuoteElementCallsProtocol() throws Exception {

        String elementName = "blockquote";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenBlockQuote(
                mockFactory.expectsInstanceOf(BlockQuoteAttributes.class));
        protocolMock.fuzzy.writeCloseBlockQuote(
                mockFactory.expectsInstanceOf(BlockQuoteAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testCaptionElementCallsProtocol() throws Exception {

        String elementName = "caption";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenTableCaption(
                mockFactory.expectsInstanceOf(CaptionAttributes.class));
        protocolMock.fuzzy.writeCloseTableCaption(
                mockFactory.expectsInstanceOf(CaptionAttributes.class));

        pushElement(new TableElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testCiteElementCallsProtocol() throws Exception {

        String elementName = "cite";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenCite(
                mockFactory.expectsInstanceOf(CiteAttributes.class));
        protocolMock.fuzzy.writeCloseCite(
                mockFactory.expectsInstanceOf(CiteAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testCodeElementCallsProtocol() throws Exception {

        String elementName = "code";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenCode(
                mockFactory.expectsInstanceOf(CodeAttributes.class));
        protocolMock.fuzzy.writeCloseCode(
                mockFactory.expectsInstanceOf(CodeAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testDefDataElementCallsProtocol() throws Exception {

        String elementName = "dd";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenDefinitionData(
                mockFactory.expectsInstanceOf(DefinitionDataAttributes.class));
        protocolMock.fuzzy.writeCloseDefinitionData(
                mockFactory.expectsInstanceOf(DefinitionDataAttributes.class));

        pushElement(new DefinitionListElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);
    }

    public void testDefinitionElementCallsProtocol() throws Exception {

        String elementName = "dfn";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenSpan(
                mockFactory.expectsInstanceOf(SpanAttributes.class));
        protocolMock.fuzzy.writeCloseSpan(
                mockFactory.expectsInstanceOf(SpanAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testDefListElementCallsProtocol()
            throws Exception {

        String elementName = "dl";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenDefinitionList(
                mockFactory.expectsInstanceOf(DefinitionListAttributes.class));
        protocolMock.fuzzy.writeCloseDefinitionList(
                mockFactory.expectsInstanceOf(DefinitionListAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartDocumentAndStartElement(elementName);

        doElementForValidation(XHTML2Elements.DT);

        callEndElementAndEndDocument(elementName);

    }

    public void testDefTermElementCallsProtocol() throws Exception {

        String elementName = "dt";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenDefinitionTerm(
                mockFactory.expectsInstanceOf(DefinitionTermAttributes.class));
        protocolMock.fuzzy.writeCloseDefinitionTerm(
                mockFactory.expectsInstanceOf(DefinitionTermAttributes.class));

        pushElement(new DefinitionListElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testDivElementCallsProtocol() throws Exception {

        String elementName = "div";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenDiv(
                mockFactory.expectsInstanceOf(DivAttributes.class));
        protocolMock.fuzzy.writeCloseDiv(
                mockFactory.expectsInstanceOf(DivAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testEmphasisElementCallsProtocol() throws Exception {

        String elementName = "em";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenEmphasis(
                mockFactory.expectsInstanceOf(EmphasisAttributes.class));
        protocolMock.fuzzy.writeCloseEmphasis(
                mockFactory.expectsInstanceOf(EmphasisAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testh1ElementCallsProtocol() throws Exception {

        String elementName = "h1";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenHeading1(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));
        protocolMock.fuzzy.writeCloseHeading1(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testh2ElementCallsProtocol() throws Exception {

        String elementName = "h2";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenHeading2(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));
        protocolMock.fuzzy.writeCloseHeading2(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testh3ElementCallsProtocol() throws Exception {

        String elementName = "h3";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenHeading3(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));
        protocolMock.fuzzy.writeCloseHeading3(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testh4ElementCallsProtocol() throws Exception {

        String elementName = "h4";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenHeading4(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));
        protocolMock.fuzzy.writeCloseHeading4(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testh5ElementCallsProtocol() throws Exception {

        String elementName = "h5";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenHeading5(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));
        protocolMock.fuzzy.writeCloseHeading5(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testh6ElementCallsProtocol() throws Exception {

        String elementName = "h6";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenHeading6(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));
        protocolMock.fuzzy.writeCloseHeading6(
                mockFactory.expectsInstanceOf(HeadingAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testListLabelElementCallsProtocol() throws Exception {

        String elementName = "label";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);

        setupEnvironment(requestMock);
        
        ProtocolConfigurationMock protocolConfigMock =
            new ProtocolConfigurationMock("protocolConfigMock",
                    expectations);

        protocolMock.expects.getProtocolConfiguration().returns(protocolConfigMock).any();
        protocolConfigMock.expects.isFrameworkClientSupported().returns(true).any();                                
        
        pushElement(new OrderedListElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testListItemElementCallsProtocol() throws Exception {

        String elementName = "li";

        MutablePropertyValuesMock values = new MutablePropertyValuesMock(
                "property values", expectations);
        
        values.expects.getComputedValue(StylePropertyDetails.MCS_MENU_STYLE)
                .returns(MCSMenuStyleKeywords.STATIC).any();
        
        StylesMock styles = new StylesMock("styles", expectations);        
        styles.expects.getPropertyValues().returns(values).any();        
                
        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        ProtocolConfigurationMock protocolConfigMock =
            new ProtocolConfigurationMock("protocolConfigMock",
                    expectations);

        protocolMock.expects.getProtocolConfiguration().returns(protocolConfigMock).any();
        protocolConfigMock.expects.isFrameworkClientSupported().returns(true).any();                                
        
        stylingEngineMock.expects.getStyles().returns(styles).any();
        protocolMock.fuzzy.writeOpenListItem(
                mockFactory.expectsInstanceOf(ListItemAttributes.class));
        protocolMock.fuzzy.writeCloseListItem(
                mockFactory.expectsInstanceOf(ListItemAttributes.class));

        pushElement(new OrderedListElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testNavigationListElementCallsProtocol()
            throws Exception {

        String elementName = "nl";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        ProtocolConfigurationMock protocolConfigMock =
            new ProtocolConfigurationMock("protocolConfigMock",
                    expectations);

        protocolMock.expects.getProtocolConfiguration().returns(protocolConfigMock).any();
        protocolConfigMock.expects.isFrameworkClientSupported().returns(true).any();                                       
        protocolMock.expects.supportsJavaScript().returns(true).any();                                       
        
        protocolMock.fuzzy.writeOpenUnorderedList(
                mockFactory.expectsInstanceOf(UnorderedListAttributes.class));
        protocolMock.fuzzy.writeCloseUnorderedList(
                mockFactory.expectsInstanceOf(UnorderedListAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartDocumentAndStartElement(elementName);

        doElementForValidation(XHTML2Elements.LABEL);

        doElementForValidation(XHTML2Elements.LI);

        callEndElementAndEndDocument(elementName);

    }
    
    public void testOrderedListElementCallsProtocol()
            throws Exception {

        String elementName = "ol";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenOrderedList(
                mockFactory.expectsInstanceOf(OrderedListAttributes.class));
        protocolMock.fuzzy.writeCloseOrderedList(
                mockFactory.expectsInstanceOf(OrderedListAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartDocumentAndStartElement(elementName);

        doElementForValidation(XHTML2Elements.LI);

        callEndElementAndEndDocument(elementName);

    }

    public void testParagraphElementCallsProtocol() throws Exception {

        String elementName = "p";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenParagraph(
                mockFactory.expectsInstanceOf(ParagraphAttributes.class));
        protocolMock.fuzzy.writeCloseParagraph(
                mockFactory.expectsInstanceOf(ParagraphAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testParametersElementStoresParameterInObject() throws Exception {

        String elementName = "param";

        MarinerRequestContextMock requestMock = setupMocks(elementName, false);
        setupEnvironment(requestMock);

        ObjectElementMock objectMock =
                new ObjectElementMock("objectElementMock", expectations);
        objectMock.expects.addParameter(
                ObjectParameter.MCS_ASPECT_RATIO_HEIGHT,
                new Integer(5));
        objectMock.expects.getElementType().returns(XHTML2Elements.OBJECT)
                .any();
        objectMock.expects.getOutputState().returns(null);

        // Do the equivalent of #pushElement but without getting element
        // output state builder.
        XDIMEElementInternal elementImpl = objectMock;

        DocumentValidator validator = xdimeContext.getDocumentValidator();
        validator.open(elementImpl.getElementType());
        xdimeContext.pushElement(objectMock);

        startDocument();

        AttributesImpl attr = new AttributesImpl();

        attr.addAttribute(
                "", "name", null, null,
                ObjectParameter.MCS_ASPECT_RATIO_HEIGHT);
        attr.addAttribute("", "value", null, null, "5");

        startElement(elementName, attr);

        callEndElementAndEndDocument(elementName);

    }

    public void testPreElementCallsProtocol() throws Exception {

        String elementName = "pre";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenPre(
                mockFactory.expectsInstanceOf(PreAttributes.class));
        protocolMock.fuzzy.writeClosePre(
                mockFactory.expectsInstanceOf(PreAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testQuoteElementCallsProtocol() throws Exception {

        String elementName = "quote";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenSpan(
                mockFactory.expectsInstanceOf(SpanAttributes.class));
        protocolMock.fuzzy.writeCloseSpan(
                mockFactory.expectsInstanceOf(SpanAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testSampleElementCallsProtocol() throws Exception {

        String elementName = "samp";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenSample(
                mockFactory.expectsInstanceOf(SampleAttributes.class));
        protocolMock.fuzzy.writeCloseSample(
                mockFactory.expectsInstanceOf(SampleAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testSpanElementCallsProtocol() throws Exception {

        String elementName = "span";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenSpan(
                mockFactory.expectsInstanceOf(SpanAttributes.class));
        protocolMock.fuzzy.writeCloseSpan(
                mockFactory.expectsInstanceOf(SpanAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testStrongElementCallsProtocol() throws Exception {

        String elementName = "strong";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenStrong(
                mockFactory.expectsInstanceOf(StrongAttributes.class));
        protocolMock.fuzzy.writeCloseStrong(
                mockFactory.expectsInstanceOf(StrongAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testSubscriptElementCallsProtocol() throws Exception {

        String elementName = "sub";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenSubscript(
                mockFactory.expectsInstanceOf(SubscriptAttributes.class));
        protocolMock.fuzzy.writeCloseSubscript(
                mockFactory.expectsInstanceOf(SubscriptAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testSuperscriptElementCallsProtocol() throws Exception {

        String elementName = "sup";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenSuperscript(
                mockFactory.expectsInstanceOf(SuperscriptAttributes.class));
        protocolMock.fuzzy.writeCloseSuperscript(
                mockFactory.expectsInstanceOf(SuperscriptAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testTableDataElementCallsProtocol() throws Exception {

        String elementName = "td";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenTableDataCell(
                mockFactory.expectsInstanceOf(TableCellAttributes.class));
        protocolMock.fuzzy.writeCloseTableDataCell(
                mockFactory.expectsInstanceOf(TableCellAttributes.class));

        pushElement(new TableRowElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testTableElementCallsProtocol()
            throws Exception {

        String elementName = "table";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenTable(
                mockFactory.expectsInstanceOf(TableAttributes.class));
        protocolMock.fuzzy.writeCloseTable(
                mockFactory.expectsInstanceOf(TableAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartDocumentAndStartElement(elementName);

        openElementForValidation(XHTML2Elements.TR);
        doElementForValidation(XHTML2Elements.TD);
        closeElementForValidation(XHTML2Elements.TR);

        callEndElementAndEndDocument(elementName);

    }

    private void openElementForValidation(ElementType childType)
            throws Exception {

        DocumentValidator validator = xdimeContext.getDocumentValidator();
        validator.open(childType);
    }

    private void doElementForValidation(ElementType childType)
            throws Exception {

        DocumentValidator validator = xdimeContext.getDocumentValidator();
        validator.open(childType);
        validator.close(childType);
    }

    private void closeElementForValidation(ElementType childType)
            throws Exception {

        DocumentValidator validator = xdimeContext.getDocumentValidator();
        validator.close(childType);
    }

    public void testTableHeaderCellElementCallsProtocol() throws Exception {

        String elementName = "th";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenTableHeaderCell(
                mockFactory.expectsInstanceOf(TableCellAttributes.class));
        protocolMock.fuzzy.writeCloseTableHeaderCell(
                mockFactory.expectsInstanceOf(TableCellAttributes.class));

        pushElement(new TableRowElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testTableRowElementCallsProtocol()
            throws Exception {

        String elementName = "tr";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenTableRow(
                mockFactory.expectsInstanceOf(TableRowAttributes.class));
        protocolMock.fuzzy.writeCloseTableRow(
                mockFactory.expectsInstanceOf(TableRowAttributes.class));

        pushElement(new TableElement(xdimeContext));

        callStartDocumentAndStartElement(elementName);

        doElementForValidation(XHTML2Elements.TD);

        callEndElementAndEndDocument(elementName);

    }

    public void testTitleElementCallsProtocol() throws Exception {

        String elementName = "title";

        MarinerRequestContextMock requestMock = setupMocks(elementName, false);

        OutputBufferFactoryMock outputBufferFactory =
                new OutputBufferFactoryMock(
                        "outputBufferFactory", expectations);

        DOMOutputBuffer outputBuffer = new DOMOutputBuffer();
        outputBufferFactory.expects.createOutputBuffer().
                returns(outputBuffer);

        protocolMock.expects.getOutputBufferFactory()
                .returns(outputBufferFactory);

        pageContextMock.expects.pushOutputBuffer(outputBuffer);
        pageContextMock.expects.popOutputBuffer(outputBuffer);

        setupEnvironment(requestMock);

        // Currently a NO-OP
        pushElement(new HtmlElement(xdimeContext));
        pushElement(new HeadElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    public void testUnorderedListElementCallsProtocol()
            throws Exception {

        String elementName = "ul";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenUnorderedList(
                mockFactory.expectsInstanceOf(UnorderedListAttributes.class));
        protocolMock.fuzzy.writeCloseUnorderedList(
                mockFactory.expectsInstanceOf(UnorderedListAttributes.class));

        pushElement(new BodyElement(xdimeContext));

        callStartDocumentAndStartElement(elementName);

        doElementForValidation(XHTML2Elements.LI);

        callEndElementAndEndDocument(elementName);

    }

    public void testUserInputElementCallsProtocol() throws Exception {

        String elementName = "kbd";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenKeyboard(
                mockFactory.expectsInstanceOf(KeyboardAttributes.class));
        protocolMock.fuzzy.writeCloseKeyboard(
                mockFactory.expectsInstanceOf(KeyboardAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }


    public void testVariableElementCallsProtocol() throws Exception {

        String elementName = "var";

        MarinerRequestContextMock requestMock = setupMocks(elementName, true);
        setupEnvironment(requestMock);

        protocolMock.fuzzy.writeOpenSpan(
                mockFactory.expectsInstanceOf(SpanAttributes.class));
        protocolMock.fuzzy.writeCloseSpan(
                mockFactory.expectsInstanceOf(SpanAttributes.class));

        pushElement(new SpanElement(xdimeContext));

        callStartAndEndForDocumentAndElement(elementName);

    }

    /**
     * Test that the containing element is validated correctly.
     */
    public void testElementUsedInsideWrongParentElement()
            throws Exception {

        String elementName = "kbd";

        MarinerRequestContextMock requestMock = setupMocks(null, false);

        setupEnvironment(requestMock);

        pushElement(new BodyElement(xdimeContext));

        try {
            callStartAndEndForDocumentAndElement(elementName);

            fail("Should have thrown a SAXException");

        } catch (SAXException e) {
        }

    }

    /**
     * Test Dynamic Menu widget, which is expressed in XDIME as
     * NL element with mcs-menu-style set to "dynamic"
     */
    public void testDynamicNavigationListElementCallsProtocol()
        throws Exception {

        String elementName = "nl";
        
        MutablePropertyValuesMock styleValues 
            = new MutablePropertyValuesMock("styleValues", expectations);
    
        styleValues
            .expects.getComputedValue(StylePropertyDetails.MCS_MENU_STYLE)
                .returns(MCSMenuStyleKeywords.DYNAMIC).fixed(2);

        DynamicMenuWidgetRendererMock rendererMock 
            = buildDynamicMenuExpectations(elementName, styleValues);
            
        rendererMock
            .fuzzy.renderNlOpen(
                protocolMock, 
                mockFactory.expectsInstanceOf(NavigationListAttributes.class));

        rendererMock
            .fuzzy.renderNlClose(
                protocolMock, 
                mockFactory.expectsInstanceOf(NavigationListAttributes.class));

        pushElement(new BodyElement(xdimeContext));
        
        callStartDocumentAndStartElement(elementName);
        
        doElementForValidation(XHTML2Elements.LABEL);
        
        doElementForValidation(XHTML2Elements.LI);
        
        callEndElementAndEndDocument(elementName);
    
    }

    /**
     * Test item of Dynamic Menu widget, which is expressed in XDIME as
     * LI within NL element with mcs-menu-style set to "dynamic"
     */
    public void testDynamicListItemElementCallsProtocol() throws Exception {

        String elementName = "li";

        MutablePropertyValuesMock styleValues 
            = new MutablePropertyValuesMock("styleValues", expectations);
        
        DynamicMenuWidgetRendererMock rendererMock 
            = buildDynamicMenuExpectations(elementName, styleValues);
            
        rendererMock
            .fuzzy.renderLiOpen(
                protocolMock, 
                mockFactory.expectsInstanceOf(ListItemAttributes.class));

        rendererMock
            .fuzzy.renderLiClose(
                protocolMock, 
                mockFactory.expectsInstanceOf(ListItemAttributes.class));        

        NavigationListElement parent = new NavigationListElement(xdimeContext) {
            protected boolean isDynamicMenu(VolantisProtocol protocol) {
                return true;
            }
        };
        pushElement(parent);
        doElementForValidation(XHTML2Elements.LABEL);
        callStartAndEndForDocumentAndElement(elementName);
    }
    
    /**
     * Test label of Dynamic Menu widget, which is expressed in XDIME as
     * LABEL within NL element with mcs-menu-style set to "dynamic"
     */
    public void testDynamicListLabelElementCallsProtocol() throws Exception {

        String elementName = "label";

        MutablePropertyValuesMock styleValues 
            = new MutablePropertyValuesMock("styleValues", expectations);
        
        DynamicMenuWidgetRendererMock rendererMock 
            = buildDynamicMenuExpectations(elementName, styleValues);
            
        rendererMock
            .fuzzy.renderLabelOpen(
                protocolMock, 
                mockFactory.expectsInstanceOf(MCSAttributes.class))
             .returns(true);

        rendererMock
            .fuzzy.renderLabelClose(
                protocolMock, 
                mockFactory.expectsInstanceOf(MCSAttributes.class));        

        NavigationListElement parent = new NavigationListElement(xdimeContext) {
            protected boolean isDynamicMenu(VolantisProtocol protocol) {
                return true;
            }
        };
        pushElement(parent);
        callStartAndEndForDocumentAndElement(elementName);
    }

    /** 
     * Build expectations common for Dynamic Menu elements
     */
    private DynamicMenuWidgetRendererMock buildDynamicMenuExpectations(
            String elementName, MutablePropertyValuesMock styleValues) {
        
        styleValues
            .expects.getComputedValue(StylePropertyDetails.MCS_LAYOUT)
            .returns(null);
        styleValues
            .expects.getComputedValue(StylePropertyDetails.MCS_CONTAINER)
            .returns(null);
        styleValues
            .expects.getComputedValue(StylePropertyDetails.DISPLAY)
            .returns(null);
        
        StylesMock stylesMock = new StylesMock("stylesMock", expectations);
        stylesMock
            .expects.getPropertyValues()
            .returns(styleValues).any();     
                
        MarinerRequestContextMock requestMock = setupMocks(elementName, stylesMock);
        setupEnvironment(requestMock);
        
        ProtocolConfigurationMock protocolConfigMock =
            new ProtocolConfigurationMock("protocolConfigMock",
                    expectations);
        
        protocolMock
            .expects.getProtocolConfiguration()
            .returns(protocolConfigMock).any();
        protocolConfigMock
            .expects.isFrameworkClientSupported()
            .returns(true).any();
        protocolMock
            .expects.supportsJavaScript()
            .returns(true).any();
        
        WidgetModuleMock moduleMock 
            = new WidgetModuleMock("moduleMock", expectations);
        protocolMock
            .expects.getWidgetModule()
            .returns(moduleMock).fixed(2);        
    
        DynamicMenuWidgetRendererMock rendererMock 
            = new DynamicMenuWidgetRendererMock("rendererMock", expectations);
            
        moduleMock
            .expects.getDynamicMenuRenderer()
            .returns(rendererMock).fixed(2);       
        
        return rendererMock;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10523/1	ianw	VBM:2005112406 Make XDIMECP Meta tag noop for now until we process it properly

 02-Dec-05	10514/1	ianw	VBM:2005112406 Make XDIMECP Meta tag noop for now until we process it properly

 12-Oct-05	9673/8	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/6	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 02-Oct-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9562/9	pabbott	VBM:2005092011 Add XHTML2 Object element[B

 30-Sep-05	9562/7	pabbott	VBM:2005092011 Add XHTML2 Object element

 21-Sep-05	9128/6	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/4	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
