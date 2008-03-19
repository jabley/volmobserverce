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
package com.volantis.xml.schema.validator;

import com.volantis.shared.content.ContentInput;
import com.volantis.shared.content.ContentStyle;
import com.volantis.xml.sax.VolantisXMLReaderFactory;
import com.volantis.xml.schema.JarFileEntityResolver;
import com.volantis.xml.schema.SchemaDefinition;
import com.volantis.xml.schema.Schemata;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.util.Iterator;

/**
 * Implements XML schema validation (using Xerces).
 */
public class SchemaValidator {

    private Schemata schemata = new Schemata();

    private JarFileEntityResolver jarFileEntityResolver;

    private static VolantisXMLReaderFactory xmlReaderFactory =
            new VolantisXMLReaderFactory();

    public SchemaValidator() {
        jarFileEntityResolver = new JarFileEntityResolver();
    }

    public SchemaValidator(JarFileEntityResolver resolver) {
        jarFileEntityResolver = resolver;
    }

    public void addSchema(SchemaDefinition schemaDefinition) {
        schemata.addSchema(schemaDefinition);
        jarFileEntityResolver.addSystemIdMapping(schemaDefinition);
    }

    public void addSchemata(Schemata schemata) {
        Iterator iterator = schemata.iterator();
        while (iterator.hasNext()) {
            SchemaDefinition schema = (SchemaDefinition) iterator.next();
            addSchema(schema);
        }
    }

    /**
     * Validate the contents of the supplied reader as LPDM XML.
     *
     * @param content the reader containing LPDM XML.
     */
    public void validate(ContentInput content)
            throws IOException, SAXException {

        XMLReader xmlReader = createValidatingReader(true);

        InputSource inputSource;
        ContentStyle contentStyle = content.getContentStyle();
        if (contentStyle == ContentStyle.BINARY) {
            inputSource = new InputSource(content.getInputStream());
        } else if (contentStyle == ContentStyle.TEXT) {
            inputSource = new InputSource(content.getReader());
        } else {
            throw new IllegalStateException("No content available.");
        }
        xmlReader.parse(inputSource);
    }

    /**
     * Get an {@link XMLReader} that will validate its input.
     *
     * @return The validating {@link XMLReader}.
     * @param failOnWarningsAndLog
     */
    public XMLReader createValidatingReader(boolean failOnWarningsAndLog)
        throws SAXException {

        XMLReader xmlReader = xmlReaderFactory.create();

        xmlReader.setProperty(
                "http://apache.org/xml/properties/schema/external-schemaLocation",
                schemata.getXSISchemaLocation());

        return new ValidatingXMLReader(xmlReader, jarFileEntityResolver,
                failOnWarningsAndLog);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/2	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 09-Dec-05	10756/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 13-Nov-05	9896/3	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 14-Oct-05	9673/1	pduffin	VBM:2005092906 Fixed welcome page and added XDIME-CP version

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 ===========================================================================
*/
