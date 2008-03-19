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
package com.volantis.mcs.eclipse.ab.editors.xml.schema;

import com.volantis.xml.sax.ExtendedSAXException;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;

/**
 * This filter resolves references to external documents, causing the schema
 * content of the referenced document to be inserted in its place.
 *
 * <p>It is likely this will only become of interest when a schema definition
 * provides multiple namespace support.<p>
 */
public class ImportFilter extends XMLFilterImpl {
    /**
     * The schema element that identifies an import.
     */
    private static final String IMPORT_NAME = "import";

    /**
     * The attribute that identifies the schema file to load.
     */
    private static final String SCHEMA_LOCATION_NAME = "schemaLocation";

    /**
     * Defines the URI for which the filter was initially invoked.
     */
    private final URL baseURI;

    /**
     * The schema parser that should be used to parse the imported schema file.
     */
    private final SchemaParser parser;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param parser the schema parser used to parse the imported schema file
     * @param systemID the system ID for the document being parsed
     */
    public ImportFilter(SchemaParser parser,
			String systemID) {
        this(null, parser, systemID);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param parent the parent parser for this filter
     * @param parser the schema parser used to parse the imported schema file
     * @param systemID the system ID for the document being parsed
     */
    public ImportFilter(XMLReader parent,
			SchemaParser parser,
			String systemID) {
        super(parent);
        this.parser = parser;

        try {
            baseURI = new URL(systemID);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("The system ID \"" +
                                            systemID +
                                            "\" is not a valid URI");
        }
    }

    // javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes attributes) throws SAXException {
        if (IMPORT_NAME.equals(localName) &&
                SchemaConstants.NAMESPACE_URI.equals(namespaceURI)) {
            processImport(attributes);
        } else {
            super.startElement(namespaceURI, localName, qName, attributes);
        }
    }

    /**
     * Processes the attributes associated with an import element.
     *
     * @param attributes the attributes associated with an import element
     */
    private void processImport(Attributes attributes) throws SAXException {
        final String schemaLocation =
                attributes.getValue(SCHEMA_LOCATION_NAME);

        try {
            URL newURI = new URL(baseURI,
                                 schemaLocation);

            parser.parse(newURI.toExternalForm());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(
	            "The schema location \"" + schemaLocation + "\" could " +
		    "not be resolved against " + baseURI.toExternalForm());
        } catch (IOException e) {
            throw new ExtendedSAXException(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Mar-05	7457/4	philws	VBM:2005030811 Allow the MCS Source editor to work again on existing LPDM file types

 ===========================================================================
*/
