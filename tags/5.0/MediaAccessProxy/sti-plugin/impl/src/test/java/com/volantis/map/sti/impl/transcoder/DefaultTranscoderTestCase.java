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
package com.volantis.map.sti.impl.transcoder;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;
import mock.javax.xml.soap.MessageFactoryMock;
import mock.javax.xml.soap.SOAPConnectionFactoryMock;
import mock.javax.xml.soap.SOAPConnectionMock;

import org.w3c.dom.Document;

import com.volantis.map.operation.ResourceDescriptorMock;
import com.volantis.map.sti.converters.DOM2TranscodingResponseConverterMock;
import com.volantis.map.sti.converters.ResourceDescriptor2TranscodingRequestConverterMock;
import com.volantis.map.sti.converters.TranscodingRequest2DOMConverterMock;
import com.volantis.map.sti.impl.STIOperation;
import com.volantis.map.sti.model.TranscodingRequest;
import com.volantis.map.sti.transcoder.TranscoderException;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

/**
 * A test case for {@link STIOperation}.
 */
public class DefaultTranscoderTestCase extends MockTestCaseAbstract {
    
    private ResourceDescriptor2TranscodingRequestConverterMock resDesc22TransReqConverterMock;
    private TranscodingRequest2DOMConverterMock transReq2DOMConverterMock;
    private DOM2TranscodingResponseConverterMock dom2TransRespConverterMock;
    private DefaultTranscoder transcoder;
    private ResourceDescriptorMock resourceDescriptorMock;
    private HttpServletRequestMock httpServletRequestMock;
    private HttpServletResponseMock httpServletResponseMock;
    private SOAPConnectionFactoryMock soapConnectionFactoryMock;
    private SOAPConnectionMock soapConnectionMock;
    private MessageFactory messageFactory;
    private URL stiServiceURL;
    private SOAPMessage responseMessage;
    private TranscodingRequest transcodingRequest;
    private Document requestDocument;
    private MessageFactoryMock messageFactoryMock;
    private SOAPMessage requestMessage;
    private Document responseDocument;

    // Javadoc inhertited
    public void setUp() throws Exception {
        super.setUp();
        
        // ==================================================================
        // Create mocks.
        // ==================================================================

        resDesc22TransReqConverterMock =
            new ResourceDescriptor2TranscodingRequestConverterMock("resDesc22TransReqConverter", expectations);

        transReq2DOMConverterMock =
            new TranscodingRequest2DOMConverterMock("transReq2DOMConverter", expectations);
        
        dom2TransRespConverterMock =
            new DOM2TranscodingResponseConverterMock("DOM2TransRespConverter", expectations);

        httpServletRequestMock =
            new HttpServletRequestMock("httpServletRequest", expectations);

        httpServletResponseMock =
            new HttpServletResponseMock("httpServletResponse", expectations);
        
        resourceDescriptorMock = 
            new ResourceDescriptorMock("resourceDescriptor", expectations);
        
        soapConnectionFactoryMock =
            new SOAPConnectionFactoryMock("soapConnectionFactory", expectations);
        
        soapConnectionMock = 
            new SOAPConnectionMock("soapConnection", expectations);
        
        transcodingRequest = 
            new TranscodingRequest();

        DocumentBuilder domBuilder = 
            DocumentBuilderFactory.newInstance().newDocumentBuilder();
        
        requestDocument = domBuilder.newDocument();
        requestDocument.appendChild(requestDocument.createElement("request-element"));
        
        responseDocument = domBuilder.newDocument();
        responseDocument.appendChild(responseDocument.createElement("response-element"));

        messageFactory = 
            MessageFactory.newInstance();
        
        messageFactoryMock = 
            new MessageFactoryMock("messageFactory", expectations);
        
        requestMessage = 
            messageFactory.createMessage();
        
        responseMessage = 
            messageFactory.createMessage();
        
        stiServiceURL = new URL("http://www.google.com"); 
        
        // ==================================================================
        // Create expectations.
        // ==================================================================
        resDesc22TransReqConverterMock
            .expects.convert(resourceDescriptorMock)
            .returns(transcodingRequest);
    
        transReq2DOMConverterMock
            .expects.convert(transcodingRequest)
            .returns(requestDocument);

        messageFactoryMock
            .expects.createMessage()
            .returns(requestMessage);
        
        soapConnectionFactoryMock
            .expects.createConnection()
            .returns(soapConnectionMock);
        
        soapConnectionMock
            .fuzzy.call(requestMessage, stiServiceURL)
            .returns(responseMessage);
        
        // ==================================================================
        // Create object to test.
        // ==================================================================
        transcoder =
            new DefaultTranscoder(stiServiceURL,
                    resDesc22TransReqConverterMock,
                    transReq2DOMConverterMock,
                    dom2TransRespConverterMock,
                    messageFactoryMock,
                    soapConnectionFactoryMock);
    }
    
    /**
     * Tests, that in case SOAP response contains Fault, the
     * TranscodingException is thrown.
     * 
     * @throws SOAPException
     */
    public void testSOAPWithFault() throws SOAPException {
        responseMessage.getSOAPBody().addFault();
        
        try {
            transcoder.transcode(resourceDescriptorMock,
                    httpServletRequestMock,
                    httpServletResponseMock);
            
            fail("TranscoderException expected");
            
        } catch (TranscoderException e) {
            // TranscoderException is expected.
        }
    }
}
