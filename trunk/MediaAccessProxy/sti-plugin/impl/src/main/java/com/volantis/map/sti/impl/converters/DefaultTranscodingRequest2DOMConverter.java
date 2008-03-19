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
package com.volantis.map.sti.impl.converters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;

import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.volantis.map.sti.converters.ConverterException;
import com.volantis.map.sti.converters.TranscodingRequest2DOMConverter;
import com.volantis.map.sti.model.TranscodingRequest;

/**
 * An implementation of TranscodingRequest2DOMConverter.
 * <p>
 * Conversion is performed in two steps. First, a XML document as String is
 * marshalled from specified TranscodingRequest instance, using specified JiBX
 * marshaller. Then, A DOM Document is built from resulting XML String, using
 * specified DocumentBuilder.
 * <p>
 * This implementation is not thread-safe.
 */
public class DefaultTranscodingRequest2DOMConverter implements
        TranscodingRequest2DOMConverter {

    private final DocumentBuilder documentBuilder;

    private final IMarshallingContext marshallingContext;

    /**
     * Initialized new instance of STIModelBuilderImpl based on specified DOM
     * DocumentBuilder and JiBX IMarshallingContext.
     * 
     * @param documentBuilder The DocumentBuilder to build W3C DOM Document.
     * @param marshallingContext The IMarshallingContext to marshall
     *            TranscodingRequest.
     */
    public DefaultTranscodingRequest2DOMConverter(
            DocumentBuilder documentBuilder,
            IMarshallingContext marshallingContext) {
        this.documentBuilder = documentBuilder;
        this.marshallingContext = marshallingContext;
    }

    // Javadoc inherited.
    public Document convert(TranscodingRequest request)
            throws ConverterException {
        // Marshall the request to byte array with XML.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            marshallingContext.marshalDocument(request, null, null,
                    outputStream);

            marshallingContext.endDocument();
        } catch (JiBXException e) {
            throw new ConverterException(e);
        }

        // Parse the XML in byte array to W3C DOM Document
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());

        InputSource inputSource = new InputSource(inputStream);

        Document document;

        try {
            document = documentBuilder.parse(inputSource);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return document;
    }
}
