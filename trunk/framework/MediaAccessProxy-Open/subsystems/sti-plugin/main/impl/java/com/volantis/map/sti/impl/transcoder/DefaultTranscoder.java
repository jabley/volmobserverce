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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.sti.impl.transcoder;

import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.sti.converters.ConverterException;
import com.volantis.map.sti.converters.DOM2TranscodingResponseConverter;
import com.volantis.map.sti.converters.ResourceDescriptor2TranscodingRequestConverter;
import com.volantis.map.sti.converters.TranscodingRequest2DOMConverter;
import com.volantis.map.sti.mime.MimeTypeRetrieverException;
import com.volantis.map.sti.model.TranscodingRequest;
import com.volantis.map.sti.model.TranscodingResponse;
import com.volantis.map.sti.transcoder.Transcoder;
import com.volantis.map.sti.transcoder.TranscoderException;
import com.volantis.synergetics.log.LogDispatcher;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;

/**
 * Default implementation of the transcoder.
 * <p/>
 * Transcoding is performed in following steps:
 * <ol>
 * <li>Convert specified ResourceDescriptor to TranscodingRequest using
 * specified ResourceDescriptor2TranscodinfRequestConverter instance.
 * <li>Convert TranscodingRequest to W3C DOM document with STI request using
 * specified TranscodingRequest2DOMConverter instance.
 * <li>Creates a SOAPMessage using specified MessageFactory instance.
 * <li>Puts W3C DOM document with transcoding request into SOAP message body.
 * <li>Sends SOAPMessage using a connection obtained from specified SOAPConnectionFactory instance.
 * <li>TODO: describe handling of SOAP response
 * </ol>
 */
public final class DefaultTranscoder implements Transcoder {

    /**
     * standard logger
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(DefaultTranscoder.class);


    private final URL stiServiceURL;
    private final ResourceDescriptor2TranscodingRequestConverter resDesc2TransReqConverter;
    private final TranscodingRequest2DOMConverter transReq2DOMConverter;
    private final DOM2TranscodingResponseConverter dom2TransRespConverter;
    private final MessageFactory soapMessageFactory;
    private final SOAPConnectionFactory soapConnectionFactory;

    public DefaultTranscoder(URL stiServiceURL,
                             ResourceDescriptor2TranscodingRequestConverter transcodingRequestBuilder,
                             TranscodingRequest2DOMConverter transcodingRequestDocumentBuilder,
                             DOM2TranscodingResponseConverter transcodingResponseBuilder,
                             MessageFactory soapMessageFactory,
                             SOAPConnectionFactory soapConnectionFactory) {
        this.stiServiceURL = stiServiceURL;
        this.resDesc2TransReqConverter = transcodingRequestBuilder;
        this.transReq2DOMConverter = transcodingRequestDocumentBuilder;
        this.dom2TransRespConverter = transcodingResponseBuilder;
        this.soapMessageFactory = soapMessageFactory;
        this.soapConnectionFactory = soapConnectionFactory;
    }

    public void transcode(ResourceDescriptor resourceDescriptor,
                          HttpServletRequest request,
                          HttpServletResponse response) throws TranscoderException {

        try {

            // Build TranscodingRequest from ResourceDescriptor. 
            TranscodingRequest transcodingRequest
                    = resDesc2TransReqConverter.convert(resourceDescriptor);

            // Build DOM from Transcoding request.
            Document document = transReq2DOMConverter.convert(transcodingRequest);

            // Create SOAP envelope.
            SOAPMessage requestMessage = soapMessageFactory.createMessage();
            SOAPEnvelope requestEnvelope = requestMessage.getSOAPPart().getEnvelope();
            SOAPBody requestBody = requestEnvelope.getBody();
            requestBody.addDocument(document);

            // Send SOAP request
            SOAPConnection connection = soapConnectionFactory.createConnection();
            SOAPMessage responseMessage = connection.call(requestMessage, stiServiceURL);

            // Get STI response
            SOAPEnvelope responseEnvelope = responseMessage.getSOAPPart().getEnvelope();
            SOAPBody responseBody = responseEnvelope.getBody();

            if (responseBody.hasFault()) {
                final SOAPFault fault = responseBody.getFault();
                throw new TranscoderException("soap-response-with-fault",
                        new Object[] { fault.getFaultCodeAsName(), fault.getFaultString() },
                        null);

            }
            
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Node node = responseBody.getFirstChild();
            node = document.importNode(node, true);
            document.appendChild(node);

            // Create TranscodingResponse
            TranscodingResponse transcodingResponse = dom2TransRespConverter.convert(document);

            if (!transcodingRequest.getOperationID().equals(
                    transcodingResponse.getOperationID())) {
                LOGGER.warn("invalid-operationID", new Object[] {
                        transcodingResponse.getOperationID(),
                        transcodingRequest.getOperationID()});
            }

            String contentID = transcodingResponse.getJobResult(0).getOutput().getLocation();

            Iterator iterator = responseMessage.getAttachments();

            // Copy the content of the first attachement to the HTTP response stream.
            boolean foundAttachement = false;

            while (!foundAttachement && iterator.hasNext()) {
                AttachmentPart part = (AttachmentPart) iterator.next();

//                    if (part.getContentId().equals(contentID)) {
                foundAttachement = true;

                InputStream inputStream = part.getDataHandler().getInputStream();

                if (inputStream == null) {
                    throw new TranscoderException(null);
                }

                OutputStream outputStream = response.getOutputStream();

                response.setContentType(part.getContentType());
                response.setContentLength(part.getSize());
                // Copy the content of input inputStream to the output inputStream.
                // The exception loggin is set to debug level
                // because of vbm 2008030721 the browser requests the map resource
                // two times in a row and aborts the firts request most of the times

                try {
                    byte buf[] = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) != -1) {
                        outputStream.write(buf, 0, len);
                    }
                    outputStream.flush();
                } finally {
                    // Ensure we close the input.
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug(e);
                        }
                    }
                    // Ensure we close the output.
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug(e);
                        }
                    }
                }
            }
        } catch (SOAPException e) {
            throw new TranscoderException(e);
        } catch (ConverterException e) {
            throw new TranscoderException(e);
        } catch (ParserConfigurationException e) {
            throw new TranscoderException(e);
        } catch (IOException e) {
            throw new TranscoderException(e);
        } catch (MimeTypeRetrieverException e) {
            throw new TranscoderException(e);
        }        
    }
}
