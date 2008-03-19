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

package com.volantis.mcs.xml.schema.validation;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.volantis.mcs.xml.schema.model.SchemaNamespaces;
import com.volantis.mcs.xml.schema.model.ElementType;

class ValidatingHandler
        extends DefaultHandler {

    private final DocumentValidator validator;
    private SchemaNamespaces namespaces;

    public ValidatingHandler(
            DocumentValidator validator, SchemaNamespaces namespace) {
        this.validator = validator;
        this.namespaces = namespace;
    }

    public void startElement(
            String uri, String localName, String qName,
            Attributes attributes)
            throws SAXException {

        ElementType type = getElementType(uri, localName);
        validator.open(type);
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {

        // Iterate over all the characters in turn to emulate the behaviour
        // of parsers that split blocks of characters into pieces, e.g.
        // on line boundaries.
        int end = start + length;
        for (int i = start; i < end; i += 1) {
            char c = ch[i];
            boolean isWhitespace = Character.isWhitespace(c);
            validator.pcdata(isWhitespace);
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        ElementType type = getElementType(uri, localName);
        validator.close(type);
    }

    private ElementType getElementType(String uri, String localName) {
        return namespaces.getElementType(uri, localName);
    }
}
