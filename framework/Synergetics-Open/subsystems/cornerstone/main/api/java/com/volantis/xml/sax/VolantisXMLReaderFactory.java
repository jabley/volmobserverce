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
package com.volantis.xml.sax;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import com.volantis.xml.schema.SchemaDefinition;

public class VolantisXMLReaderFactory {

    private boolean validation;

    private List schemas = new ArrayList();

    private URL noNamespaceSchemaLocation;

    public VolantisXMLReaderFactory() {
        validation = true;
    }

    public void disableValidation() {
        validation = false;
    }

    public void addSchema(SchemaDefinition schemaDefinition) {
        schemas.add(schemaDefinition);
    }

    public XMLReader create() throws SAXException {

        // Explicitly construct a Volantisized Xerces parser to
        // avoid any JRE 1.4 class loader issues
        XMLReader reader =
                new com.volantis.xml.xerces.parsers.SAXParser();

        // NOTE: namespaces are on by default, no need to turn on explicitly.

        if (validation) {
            enableValidation(reader);
            enableNoNamespaceSchemaLocation(reader);
            addSchemas(reader);
        }

        return reader;
    }

    private void enableValidation(XMLReader reader)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        reader.setFeature(
                "http://xml.org/sax/features/validation",
                true); //$NON-NLS-1$
        reader.setFeature(
                "http://apache.org/xml/features/validation/schema",
                true); //$NON-NLS-1$
        reader.setFeature(
                "http://apache.org/xml/features/validation/schema-full-checking",
                true); //$NON-NLS-1$
    }

    private void addSchemas(XMLReader reader) throws SAXNotRecognizedException,
            SAXNotSupportedException {
        StringBuffer namespaceResources = new StringBuffer();
        Iterator schemas = this.schemas.iterator();
        while (schemas.hasNext()) {
            SchemaDefinition schema = (SchemaDefinition)
                    schemas.next();
            // The namespace.
            namespaceResources.append(schema.getNamespaceURL());
            namespaceResources.append(" ");
            // And the resource it maps to.
            // TODO: schema resource names should be absolute already.
            URL url = getClass().getResource("/" + schema.getResourceName());
            if (url == null) {
                throw new IllegalStateException("Resource not available:" +
                        schema.getResourceName());
            }
            namespaceResources.append(url.toExternalForm());
            namespaceResources.append(" ");
        }

        reader.setProperty(
                "http://apache.org/xml/properties/schema/external-schemaLocation",
                namespaceResources.toString());
    }

    public void setNoNamespaceSchemaLocation(URL url) {
        noNamespaceSchemaLocation = url;
    }

    private void enableNoNamespaceSchemaLocation(XMLReader reader)
            throws SAXNotSupportedException, SAXNotRecognizedException {

        if (noNamespaceSchemaLocation != null) {
            reader.setProperty(
                    "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
                    noNamespaceSchemaLocation.toExternalForm());
        }
    }
}
