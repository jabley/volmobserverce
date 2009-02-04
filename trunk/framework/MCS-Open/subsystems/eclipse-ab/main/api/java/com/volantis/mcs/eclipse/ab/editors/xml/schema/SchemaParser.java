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

import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * Permits abstraction of how a schema can be parsed into a schema
 * definition.
 */
public interface SchemaParser {
    /**
     * The document, identified by the specified system ID, is parsed and
     * relevant data is added to the schema definition.
     *
     * @param systemID the system ID that identifies the document to be parsed
     * @throws SAXException if there is a problem parsing the document
     * @throws IOException if there is a problem parsing the document
     */
    void parse(String systemID) throws SAXException, IOException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Mar-05	7457/1	philws	VBM:2005030811 Allow the MCS Source editor to work again on existing LPDM file types

 ===========================================================================
*/
