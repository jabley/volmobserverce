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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.sti.impl.transcoder;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.JiBXException;

import com.volantis.map.retriever.ResourceRetriever;
import com.volantis.map.sti.converters.DOM2TranscodingResponseConverter;
import com.volantis.map.sti.converters.ResourceDescriptor2TranscodingRequestConverter;
import com.volantis.map.sti.converters.TranscodingRequest2DOMConverter;
import com.volantis.map.sti.impl.converters.DefaultDOM2TranscodingResponseConverter;
import com.volantis.map.sti.impl.converters.DefaultResourceDescriptor2TranscodingRequestConverter;
import com.volantis.map.sti.impl.converters.DefaultTranscodingRequest2DOMConverter;
import com.volantis.map.sti.impl.mime.DefaultMimeTypeRetriever;
import com.volantis.map.sti.mime.MimeTypeRetriever;
import com.volantis.map.sti.transcoder.Transcoder;
import com.volantis.map.sti.transcoder.TranscoderFactory;
import com.volantis.map.sti.transcoder.TranscoderFactoryException;

/**
 * Default implementation of TranscoderFactory.
 */
public final class DefaultTranscoderFactory implements TranscoderFactory {
    private final DocumentBuilderFactory documentBuilderFactory;

    private final TransformerFactory transformerFactory;

    private final IBindingFactory bindingFactory;

    private final ResourceRetriever resourceRetriever;

    private final URL stiServerURL;

    private final String originatorID;

    public DefaultTranscoderFactory(
            URL stiServerURL,
            DocumentBuilderFactory documentBuilderFactory,
            TransformerFactory transformerFactory,
            IBindingFactory bindingFactory,
            ResourceRetriever resourceRetriever,
            String originatorID) {
        
        this.stiServerURL = stiServerURL;
        this.documentBuilderFactory = documentBuilderFactory;
        this.transformerFactory = transformerFactory;
        this.bindingFactory = bindingFactory;
        this.resourceRetriever = resourceRetriever;
        this.originatorID = originatorID;
    }

    public Transcoder createTranscoder() throws TranscoderFactoryException {
        try {
            MimeTypeRetriever mimeTypeRetriever = 
                new DefaultMimeTypeRetriever(resourceRetriever);
            
            ResourceDescriptor2TranscodingRequestConverter transcodingRequestBuilder = 
                new DefaultResourceDescriptor2TranscodingRequestConverter(
                        mimeTypeRetriever, originatorID);

            TranscodingRequest2DOMConverter transcodingRequestDocumentBuilder =
                new DefaultTranscodingRequest2DOMConverter(
                    documentBuilderFactory.newDocumentBuilder(),
                    bindingFactory.createMarshallingContext());

            DOM2TranscodingResponseConverter transcodingResponseBuilder = 
                new DefaultDOM2TranscodingResponseConverter(
                    transformerFactory.newTransformer(),
                    bindingFactory.createUnmarshallingContext());

            return new DefaultTranscoder(stiServerURL,
                    transcodingRequestBuilder,
                    transcodingRequestDocumentBuilder,
                    transcodingResponseBuilder,
                    MessageFactory.newInstance(),
                    SOAPConnectionFactory.newInstance());
            
        } catch (ParserConfigurationException e) {
            throw new TranscoderFactoryException(e);
        } catch (JiBXException e) {
            throw new TranscoderFactoryException(e);
        } catch (TransformerConfigurationException e) {
            throw new TranscoderFactoryException(e);
        } catch (UnsupportedOperationException e) {
            throw new TranscoderFactoryException(e);
        } catch (SOAPException e) {
            throw new TranscoderFactoryException(e);
        }
    }
}
