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
package com.volantis.shared.metadata.impl;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import org.apache.xerces.jaxp.JAXPConstants;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * Abstract test case for JiBX tests
 */
public class JiBXTestCaseAbstract extends TestCaseAbstract {
    private static final String META_DATA_VALUES_NAMESPACE_URI =
        "http://www.volantis.com/xmlns/2004/12/meta-data-values";

    private static final String META_DATA_TYPES_NAMESPACE_URI =
        "http://www.volantis.com/xmlns/2004/12/meta-data-types";

    private static final SAXBuilder SAX_BUILDER;

    static {
        SAX_BUILDER = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
        SAX_BUILDER.setValidation(true);
        SAX_BUILDER.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE,
            JAXPConstants.W3C_XML_SCHEMA );

        // Turn on namespace support.
        SAX_BUILDER.setFeature("http://xml.org/sax/features/namespaces", true);
        // Turn on validation
        SAX_BUILDER.setFeature("http://xml.org/sax/features/validation", true);
        // Turn on XML Schema support
        SAX_BUILDER.setFeature(
            "http://apache.org/xml/features/validation/schema", true);
        // Turn off dynamic validation
        SAX_BUILDER.setFeature(
            "http://apache.org/xml/features/validation/dynamic", false);
        SAX_BUILDER.setFeature(
            "http://apache.org/xml/features/validation/schema-full-checking",
            true);

        final URL valuesUrl = JiBXTestCaseAbstract.class.getResource(
            "/com/volantis/schema/2004/12/meta-data-values.xsd");
        final URL typesUrl = JiBXTestCaseAbstract.class.getResource(
            "/com/volantis/schema/2004/12/meta-data-types.xsd");
        SAX_BUILDER.setProperty(
            "http://apache.org/xml/properties/schema/external-schemaLocation",
            META_DATA_VALUES_NAMESPACE_URI + " " + valuesUrl.toExternalForm() +
                " " + META_DATA_TYPES_NAMESPACE_URI + " " +
                typesUrl.toExternalForm());
    }

    protected Object doRoundTrip(final Object object)
            throws Exception {

        final String content = marshall(object);
        return unmarshall(content, object.getClass());
    }

    protected Object unmarshall(final String input, final Class expectedClass)
            throws JiBXException, IOException, JDOMException {

        // validate XML
        SAX_BUILDER.build(new StringReader(input));

        // unmarshal XML
        final IBindingFactory bfact = BindingDirectory.getFactory(expectedClass);
        final IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
        return uctx.unmarshalDocument(new StringReader(input));
    }

    protected String marshall(final Object object)
            throws JiBXException, IOException, JDOMException {

        // marshal XML
        final IBindingFactory bfact =
            BindingDirectory.getFactory(object.getClass());
        final IMarshallingContext mctx = bfact.createMarshallingContext();
        mctx.setIndent(4);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mctx.marshalDocument(object, "UTF-8", null, baos);
        baos.close();
        final String xmlContent = baos.toString();

        // validate XML
        SAX_BUILDER.build(new StringReader(xmlContent));

        return xmlContent;
    }
}
