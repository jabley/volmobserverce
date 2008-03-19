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

package com.volantis.mcs.xdime.ticker.response;

import com.volantis.mcs.context.ListenerEventRegistry;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.protocols.VolantisProtocolMock;
import com.volantis.mcs.protocols.layouts.RegionInstanceMock;
import com.volantis.mcs.protocols.ticker.renderers.ElementRendererMock;
import com.volantis.mcs.protocols.ticker.response.TickerResponseModuleMock;
import com.volantis.mcs.protocols.ticker.response.attributes.AddItemAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.DescriptionAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.FeedPollerAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.IconAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.RemoveItemAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SetPollingIntervalAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SetSkipTimesAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SetURLAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SkipTimeAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.TitleAttributes;
import com.volantis.mcs.xdime.XDIMEAttributesImpl;
import com.volantis.mcs.xdime.XDIMEContentHandler;
import com.volantis.mcs.xdime.XDIMEContextFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementHandler;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.ticker.TickerResponseElements;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.DocumentValidator;
import com.volantis.mcs.xml.schema.validation.ValidationException;
import com.volantis.styling.engine.StylingEngineMock;
import com.volantis.styling.impl.StylesImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Test case for testing all elements from ticker response namespace
 */
public class TickerResponseElementsTestCase extends TestCaseAbstract {

    protected XDIMEContentHandler handler;
    protected XDIMEContextInternal xdimeContext;
    protected AttributesImpl emptyAttributes; 

    protected VolantisProtocolMock protocolMock;
    protected TickerResponseModuleMock moduleMock;
    protected ElementRendererMock rendererMock;
    protected MarinerPageContextMock pageCtxMock;
    protected RegionInstanceMock containerInstanceMock;
    protected StylingEngineMock stylingEngineMock;
    protected StylesImpl elementStyles;

    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create basic mocks 
        super.setUp();        
        // Now create mocks that are common for all tests
        setCommonExcpectations();
    }
    
    /**
     * Set up element-independent environment of stubs and mocks.
     * 
     * This method is common for all tested elements and called 
     * automatically before each test in setUp() method.   
     */
    protected void setCommonExcpectations() {
        
        // Prepare fake attribuutes
        emptyAttributes = new AttributesImpl(); 
        emptyAttributes.addAttribute(
            "", /* uri */ 
            "id", /* localName */
            "", /* qName */
            "", /* type */
            "myId" /* value */
        );

        elementStyles = new StylesImpl(null, null);
        
        // Setup XDIME processing environment
        pageCtxMock = new MarinerPageContextMock(
                "pageCtxMock", expectations);
        
        MarinerRequestContextMock requestCtxMock = new MarinerRequestContextMock(
                "requestCtxMock", expectations);
        requestCtxMock
            .expects.getMarinerPageContext()
            .returns(pageCtxMock).any();

        XDIMEContextFactory factory = XDIMEContextFactory.getDefaultInstance();
        xdimeContext = 
            (XDIMEContextInternal)factory.createXDIMEContext();
        xdimeContext.setInitialRequestContext(requestCtxMock);
        
        handler = new XDIMEContentHandler(
                null, xdimeContext, XDIMEElementHandler.getDefaultInstance());
        
        pageCtxMock.expects.enteringXDIMECPElement().any();
        pageCtxMock.expects.exitingXDIMECPElement().any();
        pageCtxMock
            .expects.getListenerEventRegistry()
            .returns(new ListenerEventRegistry());

        // Setup styling engine mock
        stylingEngineMock = new StylingEngineMock(
                "stylingEngineMock", expectations);
        
        stylingEngineMock
            .expects.getStyles()
            .returns(elementStyles).any();
        pageCtxMock
            .expects.getStylingEngine()
            .returns(stylingEngineMock).any();
    
        // Container mock
        containerInstanceMock =  new RegionInstanceMock(
                "containerInstanceMock", expectations, NDimensionalIndex.ZERO_DIMENSIONS);
        pageCtxMock
            .expects.getCurrentContainerInstance()
            .returns(containerInstanceMock).any();
    
        //  Protocol mock
        ProtocolConfigurationMock protocolConfigMock = new ProtocolConfigurationMock(
                "protocolConfigMock", expectations);
        protocolMock = new VolantisProtocolMock(
                "protocolMock", expectations, protocolConfigMock);
        pageCtxMock
            .expects.getProtocol()
            .returns(protocolMock).any();
    }
    
    /**
     * Sets element-dependent expectaions. 
     * 
     * Should be called at the beginning of each test.  
     */
    protected void setElementExpectations(
            Class attrClass,
            ElementType elementType,
            boolean isElementStyled) {
        
        if (isElementStyled) {
            stylingEngineMock
                .fuzzy.startElement(
                    XDIMESchemata.TICKER_RESPONSE_NAMESPACE,
                    elementType.getLocalName(),
                    mockFactory.expectsInstanceOf(XDIMEAttributesImpl.class));
            stylingEngineMock
                .expects.endElement(
                    XDIMESchemata.TICKER_RESPONSE_NAMESPACE,
                    elementType.getLocalName());
        }
        
        moduleMock = new TickerResponseModuleMock(
                "moduleMock", expectations);

        protocolMock
            .expects.getTickerResponseModule()
            .returns(moduleMock).any();  
       
    }
    
    protected void startDocumentAndElement(String elementName) 
            throws SAXException {
        
        handler.startDocument();
        startElement(elementName);
    }
    
    protected void endElementAndDocument(String elementName) 
            throws SAXException {
        
        endElement(elementName);
        handler.endDocument();
    }

    protected void startElement(String elementName) 
        throws SAXException {
        
        handler.startElement(
                XDIMESchemata.TICKER_RESPONSE_NAMESPACE, 
                elementName, null, emptyAttributes);        
    }

    protected void endElement(String elementName)
            throws SAXException {
        
        handler.endElement(
                XDIMESchemata.TICKER_RESPONSE_NAMESPACE, 
                elementName, null);                
    }

    protected void runThroughValidator(List /* of ElementType */ elements) 
            throws ValidationException {
        
        DocumentValidator validator = xdimeContext.getDocumentValidator();
        Iterator i = elements.iterator();
        while(i.hasNext()) {
            ElementType element = (ElementType)i.next();
            validator.open(element);
            validator.close(element);   
        }            
    }

    protected void executeTest(ElementType elementType, List children) throws Exception {
        String elementName = elementType.getLocalName();
        startDocumentAndElement(elementName);
        runThroughValidator(children);        
        endElementAndDocument(elementName);
    }   

    protected void executeTest(ElementType elementType) throws Exception {
        executeTest(elementType, Collections.EMPTY_LIST);
    }   

    
    /** 
     * Test for Feed Poller response element
     */
    public void testFeedPollerElement() throws Exception {
        ElementType elementType = TickerResponseElements.FEED_POLLER;
        Class attrClass = FeedPollerAttributes.class;
            
        setElementExpectations(attrClass, elementType, true);            
        moduleMock
            .fuzzy.openFeedPoller(mockFactory.expectsInstanceOf(attrClass));
        moduleMock
            .fuzzy.closeFeedPoller(mockFactory.expectsInstanceOf(attrClass));
        executeTest(elementType);
    }
    
    /** 
     * Test for AddItem response element
     */
    public void testAddItemElement() throws Exception {
        ElementType elementType = TickerResponseElements.ADD_ITEM;
        Class attrClass = AddItemAttributes.class;
        
        setElementExpectations(attrClass, elementType, true);            
        moduleMock
            .fuzzy.openAddItem(mockFactory.expectsInstanceOf(attrClass));
        moduleMock
            .fuzzy.closeAddItem(mockFactory.expectsInstanceOf(attrClass));
        
        ElementType[] children = {
                TickerResponseElements.TITLE,
                TickerResponseElements.ICON,
                TickerResponseElements.DESCRIPTION
        };
        executeTest(elementType, Arrays.asList(children));
    }

    /** 
     * Test for RemoveItem response element
     */
    public void testRemoveItemElement() throws Exception {
        ElementType elementType = TickerResponseElements.REMOVE_ITEM;
        Class attrClass = RemoveItemAttributes.class;
        
        setElementExpectations(attrClass, elementType, true);            
        moduleMock
            .fuzzy.openRemoveItem(mockFactory.expectsInstanceOf(attrClass));
        moduleMock
            .fuzzy.closeRemoveItem(mockFactory.expectsInstanceOf(attrClass));
        
        executeTest(elementType);
    }

    /** 
     * Test for Description response element
     */
    public void testDescriptionElement() throws Exception {
        ElementType elementType = TickerResponseElements.DESCRIPTION;
        Class attrClass = DescriptionAttributes.class;
            
        setElementExpectations(attrClass, elementType, true);            
        moduleMock
            .fuzzy.openDescription(mockFactory.expectsInstanceOf(attrClass));
        moduleMock
            .fuzzy.closeDescription(mockFactory.expectsInstanceOf(attrClass));
        executeTest(elementType);
    }

    /** 
     * Test for Icon response element
     */
    public void testIconElement() throws Exception {
        ElementType elementType = TickerResponseElements.ICON;
        Class attrClass = IconAttributes.class;
            
        setElementExpectations(attrClass, elementType, true);            
        moduleMock
            .fuzzy.openIcon(mockFactory.expectsInstanceOf(attrClass));
        moduleMock
            .fuzzy.closeIcon(mockFactory.expectsInstanceOf(attrClass));
        executeTest(elementType);
    }

    /** 
     * Test for Title response element
     */
    public void testTitleElement() throws Exception {
        ElementType elementType = TickerResponseElements.TITLE;
        Class attrClass = TitleAttributes.class;
            
        setElementExpectations(attrClass, elementType, true);            
        moduleMock
            .fuzzy.openTitle(mockFactory.expectsInstanceOf(attrClass));
        moduleMock
            .fuzzy.closeTitle(mockFactory.expectsInstanceOf(attrClass));
        executeTest(elementType);
    }

    /** 
     * Test for SetPollingInterval response element
     */
    public void testSetPollingIntervalElement() throws Exception {
        ElementType elementType = TickerResponseElements.SET_POLLING_INTERVAL;
        Class attrClass = SetPollingIntervalAttributes.class;
            
        setElementExpectations(attrClass, elementType, true);            
        moduleMock
            .fuzzy.openSetPollingInterval(mockFactory.expectsInstanceOf(attrClass));
        moduleMock
            .fuzzy.closeSetPollingInterval(mockFactory.expectsInstanceOf(attrClass));
        executeTest(elementType);
    }

    /** 
     * Test for SetSkipTimes response element
     */
    public void testSetSkipTimesElement() throws Exception {
        ElementType elementType = TickerResponseElements.SET_SKIP_TIMES;
        Class attrClass = SetSkipTimesAttributes.class;
            
        setElementExpectations(attrClass, elementType, true);            
        moduleMock
            .fuzzy.openSetSkipTimes(mockFactory.expectsInstanceOf(attrClass));
        moduleMock
            .fuzzy.closeSetSkipTimes(mockFactory.expectsInstanceOf(attrClass));
        executeTest(elementType);
    }

    /** 
     * Test for SkipTime response element
     */
    public void testSkipTimeElement() throws Exception {
        ElementType elementType = TickerResponseElements.SKIP_TIME;
        Class attrClass = SkipTimeAttributes.class;
            
        setElementExpectations(attrClass, elementType, true);            
        moduleMock
            .fuzzy.openSkipTime(mockFactory.expectsInstanceOf(attrClass));
        moduleMock
            .fuzzy.closeSkipTime(mockFactory.expectsInstanceOf(attrClass));
        executeTest(elementType);
    }

    /** 
     * Test for SetURL response element
     */
    public void testSetURLElement() throws Exception {
        ElementType elementType = TickerResponseElements.SET_URL;
        Class attrClass = SetURLAttributes.class;
            
        setElementExpectations(attrClass, elementType, true);            
        moduleMock
            .fuzzy.openSetURL(mockFactory.expectsInstanceOf(attrClass));
        moduleMock
            .fuzzy.closeSetURL(mockFactory.expectsInstanceOf(attrClass));
        executeTest(elementType);
    }
}
