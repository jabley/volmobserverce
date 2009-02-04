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

import com.volantis.devrep.repository.accessors.DeletionFilter;
import com.volantis.mcs.eclipse.common.RepositorySchemaResolverFactory;
import com.volantis.xml.schema.JarFileEntityResolver;

import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

/**
 * A simple implementation that utilizes a SAX Parser and the various SAX
 * XMLFilters defined in this package in order to populate the schema
 * definition.
 */
public class SimpleSchemaParser implements SchemaParser {
    /**
     * The schema definition to be updated by the parser.
     */
    private final SchemaDefinition schema;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param schema the schema definition to be updated by the parser
     */
    public SimpleSchemaParser(SchemaDefinition schema) {
        this.schema = schema;
    }

    // javadoc inherited
    public void parse(String systemID) throws SAXException, IOException {
        // Create the XML parser to be used to parse the schema file
        XMLReader parser = new com.volantis.xml.xerces.parsers.SAXParser();
        JarFileEntityResolver repositorySchemaResolver =
                RepositorySchemaResolverFactory.create();
        parser.setEntityResolver(repositorySchemaResolver);

        // Set up a filter stack that will provide nicely resolved element
        // and attribute information that can be fed into a
        // SchemaConfigurator
        final DeletionFilter.NodeIdentifier[] filteredElements = {
            new DeletionFilter.NodeIdentifier("simpleType",
                    SchemaConstants.NAMESPACE_URI),
            new DeletionFilter.NodeIdentifier("annotation",
                    SchemaConstants.NAMESPACE_URI),
            new DeletionFilter.NodeIdentifier("key",
                    SchemaConstants.NAMESPACE_URI),
            new DeletionFilter.NodeIdentifier("keyref",
                    SchemaConstants.NAMESPACE_URI),
            new DeletionFilter.NodeIdentifier("unique",
                    SchemaConstants.NAMESPACE_URI),
            // Rather than process imports, they are simply ignored for
            // now
            new DeletionFilter.NodeIdentifier("import",
                    SchemaConstants.NAMESPACE_URI),
            // Complex content is causing problems with the XDIME CP schema:
            // ignore it for now
            // TODO later Find something sensible to do about this
            new DeletionFilter.NodeIdentifier("complexContent",
                    SchemaConstants.NAMESPACE_URI),
            new DeletionFilter.NodeIdentifier("simpleContent",
                    SchemaConstants.NAMESPACE_URI),
        };

        final String[] nodeNames = new String[]{
            "complexType", "sequence", "all", "choice"
        };

        XMLFilter filter = new GroupFilter(new DeletionFilter(
                filteredElements,
                new ExposeChildrenFilter(nodeNames,
                                         parser)));

        // Set up the features that we need
        try {
            parser.setFeature("http://xml.org/sax/features/namespaces",
                              true);
        } catch (SAXNotSupportedException e) {
            throw new IllegalStateException(
                    "The parser should support all required features (" +
                    e.getMessage() + ")");
        } catch (SAXNotRecognizedException e) {
            throw new IllegalStateException(
                    "The parser should support all required features (" +
                    e.getMessage() + ")");
        }

        // Set up the schema configurator needed to populate the schema
        // definition.
        filter.setContentHandler(new SchemaConfigurator(schema));

        // Now parse the schema file itself to populate the schema definition
        filter.parse(systemID);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 14-Nov-05	10311/1	adrianj	VBM:2005110414 Fix for error on MCS schema editor startup

 14-Nov-05	10248/1	adrianj	VBM:2005110414 Fix error on startup of MCS source editor

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Mar-05	7457/1	philws	VBM:2005030811 Allow the MCS Source editor to work again on existing LPDM file types

 ===========================================================================
*/
