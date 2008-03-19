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

package com.volantis.mcs.xdime.ticker;

import com.volantis.mcs.context.ListenerEventRegistry;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.protocols.VolantisProtocolMock;
import com.volantis.mcs.protocols.layouts.RegionInstanceMock;
import com.volantis.mcs.protocols.ticker.TickerModuleMock;
import com.volantis.mcs.protocols.ticker.renderers.ElementRendererMock;
import com.volantis.mcs.xdime.XDIMEAttributesImpl;
import com.volantis.mcs.xdime.XDIMEContentHandler;
import com.volantis.mcs.xdime.XDIMEContextFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElement;
import com.volantis.mcs.xdime.XDIMEElementHandler;
import com.volantis.mcs.xdime.XDIMEElementInternal;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.DocumentValidator;
import com.volantis.mcs.xml.schema.validation.ValidationException;
import com.volantis.styling.engine.StylingEngineMock;
import com.volantis.styling.impl.StylesImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for testing elements from ticker namespace
 */
public abstract class TickerElementTestCaseAbstract extends TestCaseAbstract {

    protected XDIMEContentHandler handler;
    protected XDIMEContextInternal xdimeContext;
    protected AttributesImpl emptyAttributes; 

    protected VolantisProtocolMock protocolMock;
    protected TickerModuleMock moduleMock;
    protected ElementRendererMock rendererMock;
    protected MarinerPageContextMock pageCtxMock;
    protected RegionInstanceMock containerInstanceMock;
    protected StylingEngineMock stylingEngineMock;
    protected StylesImpl elementStyles;

    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create basic mocks 
        super.setUp();        
                                
        // Prepare fake attribuutes
        emptyAttributes = new AttributesImpl(); 
        emptyAttributes.addAttribute(
            "", /* uri */ 
            "id", /* localName */
            "", /* qName */
            "", /* type */
            getWidgetId() /* value */
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
        
        if (isElementStyled()) {
            stylingEngineMock
                .fuzzy.startElement(
                    XDIMESchemata.TICKER_NAMESPACE,
                    getElementName(),
                    mockFactory.expectsInstanceOf(XDIMEAttributesImpl.class));
            stylingEngineMock
                .expects.endElement(
                    XDIMESchemata.TICKER_NAMESPACE,
                    getElementName());
        }
        
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
    
    protected void addDefaultElementExpectations(Class attrClass) {
        
        moduleMock = new TickerModuleMock(
                "moduleMock", expectations);
        protocolMock
            .expects.getTickerModule()
            .returns(moduleMock).any();  

        rendererMock = new ElementRendererMock(
                "rendererMock", expectations);
       
        moduleMock
            .fuzzy.getElementRenderer(
                    mockFactory.expectsInstanceOf(attrClass))
            .returns(rendererMock).any();
       
        rendererMock
            .fuzzy.renderOpen(
                    protocolMock, 
                    mockFactory.expectsInstanceOf(attrClass));
       
        rendererMock
            .fuzzy.shouldRenderContents(
                    protocolMock,
                    mockFactory.expectsInstanceOf(attrClass))
            .returns(true);

        rendererMock
            .fuzzy.renderClose(
                protocolMock, 
                mockFactory.expectsInstanceOf(attrClass));
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
                XDIMESchemata.TICKER_NAMESPACE, 
                elementName, null, emptyAttributes);        
    }

    protected void endElement(String elementName)
            throws SAXException {
        
        handler.endElement(
                XDIMESchemata.TICKER_NAMESPACE, 
                elementName, null);                
    }

    protected void runThroughValidator(List elements) 
            throws ValidationException {
        
        DocumentValidator validator = xdimeContext.getDocumentValidator();
        Iterator i = elements.iterator();
        while(i.hasNext()) {
            ElementType element = (ElementType)i.next();
            validator.open(element);
            validator.close(element);   
        }            
    }

    protected String getWidgetId() {
        return "myTickerWidget";
    }
    
    protected void fakeFeedPoller() throws Exception {
        XDIMEElement poller =        
            XDIMEElementHandler
                .getDefaultInstance()
                    .createXDIMEElement(TickerElements.FEED_POLLER, xdimeContext);
        xdimeContext.addSingletonElement((XDIMEElementInternal)poller);
    }

    protected void executeTest() throws Exception {
        startDocumentAndElement(getElementName());
        runThroughValidator(getChildElements());
        endElementAndDocument(getElementName());
    }
    
    protected boolean isElementStyled() {
        return true;
    }

    protected List /*of ElementType*/ getChildElements() {
        return Collections.EMPTY_LIST;
    }

    protected abstract String getElementName();
}
