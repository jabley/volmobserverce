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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import com.volantis.xml.pipeline.testtools.PassThruContentHandler;
import com.volantis.xml.pipeline.Namespace;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This process converts absoulute base uri attribute values to relative urls
 * by removing the intallation dependant part of the url. For example a
 * base url of
 *
 * xml:base="file:/work/2004031802/pipeline/classes/fetch/test.html"
 *
 * would be converted to
 *
 * xml:base="./fetch/test.html"
 */
public class AttributeNormalizingContentHandler
            extends PassThruContentHandler  {

    /**
     * The namespace of HREF attributes.
     */
    private Namespace hrefNamespace;

    /**
     * Initializes a ContentHandler with the given parameters.
     * @param contentHandler the {@link ContentHandler} that this class is
     * wrapping.
     */
    public AttributeNormalizingContentHandler(ContentHandler contentHandler) {
        super(contentHandler);
    }

    /**
     * Initializes a ContentHandler with the given parameters.
     * @param contentHandler the {@link ContentHandler} that this class is
     * wrapping.
     * @param hrefNamespace the namespace of HREF attributes.
     */
    public AttributeNormalizingContentHandler(ContentHandler contentHandler,
                                              Namespace hrefNamespace) {
        super(contentHandler);
        this.hrefNamespace = hrefNamespace;
    }

     // javadoc inherited
     public void startElement(String namespaceURI, String localName, String qName,
                              Attributes attributes)
                 throws SAXException {

         // normalize
         Attributes forward = normalizeURIAttributes(
                     attributes, NamespaceSupport.XMLNS, "base");
         forward = normalizeURIAttributes(forward,
                                          (null == hrefNamespace) ? "" :
                                          hrefNamespace.getURI(),
                                          "href");

         super.startElement(namespaceURI, localName, qName, forward);
     }

    /**
     * Normalize the URI attributes and return a clone of the normalized
     * attributes, or the original attributes if no normalization occurred.
     *
     * @param attributes the attributes to normalize.
     * @param namespaceURI the name space.
     * @param localName the local name.
     *
     * @return the normalized attributes.
     */
    private Attributes normalizeURIAttributes(Attributes attributes,
                                              String namespaceURI,
                                              String localName) {

        Attributes result = attributes;
        String value = attributes.getValue(namespaceURI,
                                           localName);
        if (null != value) {
            int index = value.lastIndexOf('!');
            if (-1 != index) {
                value = value.substring(index + 2);
                result = cloneAttributes(attributes,
                                         namespaceURI,
                                         localName,
                                         value);
            } else if (value.startsWith("file:")) {
                final String comVolantis = "com/volantis";
                index = value.lastIndexOf(comVolantis);
                if (-1 != index) {
                    value = value.substring(index);
                    result = cloneAttributes(attributes,
                                             namespaceURI,
                                             localName,
                                             value);
                }
            }
        }
        return result;
    }

    /**
     * Clone the attributes (create a copy of the attributes).
     * setting.
     *
     * @param attributes the attributes to clone.
     * @param namespaceURI the name space.
     * @param localName the local name.
     * @param attributeValue the attribute.
     */
    private Attributes cloneAttributes(Attributes attributes,
                                       String namespaceURI,
                                       String localName,
                                       String attributeValue) {
        AttributesImpl cloneAttrs =
                new AttributesImpl(attributes);
        int attIndex = cloneAttrs.getIndex(namespaceURI, localName);
        if (-1 != attIndex) {
            cloneAttrs.setValue(attIndex, attributeValue);
        }
        return cloneAttrs;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8751/5	schaloner	VBM:2005060711 Added a getter method for the AttributeNormalizingContentHandler in PipelineTestAbstract

 21-Jun-05	8751/3	schaloner	VBM:2005060711 Added an optional namespace for href attributes in AttributeNormalisingContentHandler

 20-Jun-05	8751/1	schaloner	VBM:2005060711 [Refactor - method signature] public DefaultHandler createDefaultHandler changed to public ContentHandler createContentHandler in PipelineTestAbstract

 19-May-05	8357/1	pduffin	VBM:2005051810 Fixed problem with pipeline tests relying on the build structure

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 ===========================================================================
*/
