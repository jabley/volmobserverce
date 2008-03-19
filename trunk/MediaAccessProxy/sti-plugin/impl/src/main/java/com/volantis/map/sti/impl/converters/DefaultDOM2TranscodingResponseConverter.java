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

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.w3c.dom.Document;

import com.volantis.map.sti.converters.ConverterException;
import com.volantis.map.sti.converters.DOM2TranscodingResponseConverter;
import com.volantis.map.sti.model.TranscodingResponse;

/**
 * An implementation of STIDocumentBuilder based on JiBX.
 * <p>
 * Conversion is performed in two phases. First, DOM document is transformed to
 * String representation using specified transformer. Then, the string is
 * unmarshalled to TranscodingResponse instance using specified JiBX unmarshaller.
 * <p>
 * This implementation is not thread-safe.
 */
public final class DefaultDOM2TranscodingResponseConverter implements
        DOM2TranscodingResponseConverter {

    /**
     * The transformer to convert DOM Document to String.
     */
    private final Transformer transformer;

    /**
     * The unmarshalling context to unmarshall TranscoderResponse from XML.
     */
    private final IUnmarshallingContext unmarshallingContext;

    /**
     * Initialized new instance of STIModelBuilderImpl based on default JiBX
     * IUnmarshallingContext and DOM Transformer.
     * 
     * @param transformer The DOM Transformer to transform W3C DOM Document.
     * @param unmarshallingContext The IUnmarshallingContext to unmarshall
     *            TranscodingRequest.
     */
    public DefaultDOM2TranscodingResponseConverter(Transformer transformer,
            IUnmarshallingContext unmarshallingContext) {
        this.transformer = transformer;
        this.unmarshallingContext = unmarshallingContext;
    }

    // Javadoc inherited.
    public TranscodingResponse convert(Document document)
            throws ConverterException {
        // Serialize XML document to byte array.
        TranscodingResponse transcodingResponse;

        try {
            DOMSource source = new DOMSource(document);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            StreamResult result = new StreamResult(outputStream);

            transformer.transform(source, result);

            // Unmarshall XML document in byte array to TranscodingResponse.
            ByteArrayInputStream inputStream = new ByteArrayInputStream(
                    outputStream.toByteArray());

            transcodingResponse = (TranscodingResponse) unmarshallingContext
                    .unmarshalDocument(inputStream, null, null);

        } catch (TransformerException e) {
            // Caught in case of DOM to String transformation.
            throw new ConverterException(e);

        } catch (JiBXException e) {
            // Caught in case JiBX unmarshalling failed.
            throw new ConverterException(e);
        }

        return transcodingResponse;
    }
}
