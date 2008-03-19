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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.sax.recorder;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * Outputs details of the events.
 */
class EventOutputter implements ContentHandler {

    private final PrintWriter printer;
    private final boolean outputLocation;
    private final String systemIdPrefix;
    private Locator locator;

    public EventOutputter(Writer writer, boolean outputLocation,
                          String systemIdPrefix) {
        this.outputLocation = outputLocation;
        this.systemIdPrefix = systemIdPrefix;
        printer = new PrintWriter(writer);
    }

    public EventOutputter(Writer writer) {
        this(writer, false, null);
    }

    public void endDocument() {
        outputLocation();
        printer.println("endDocument");
    }

    private void outputLocation() {
        if (outputLocation) {
            String separator = "";
            String publicId = locator.getPublicId();
            if (publicId != null) {
                printer.print(separator);
                printer.print(publicId);
                separator = ",";
            }
            String systemId = locator.getSystemId();
            if (systemId != null) {
                if (systemId.startsWith(systemIdPrefix)) {
                    systemId = systemId.substring(systemIdPrefix.length());
                } else {
                    throw new IllegalStateException("System id '" + systemId +
                            "' does not start with prefix '" +
                            systemIdPrefix + "'");
                }
                printer.print(separator);
                printer.print(systemId);
                separator = ",";
            }
            int line = locator.getLineNumber();
            if (line != -1) {
                printer.print(separator);
                printer.print(line);
                separator = ",";
            }
            int column = locator.getColumnNumber();
            if (column != -1) {
                printer.print(separator);
                printer.print(column);
                separator = ",";
            }

            if (separator.equals(",")) {
                printer.print(": ");
            }
        }
    }

    public void startDocument() {
        outputLocation();
        printer.println("startDocument");
    }

    public void characters(char ch[], int start, int length) {
        outputLocation();
        printer.println("characters: {" + new String(ch, start,  length) + "}");
    }

    public void ignorableWhitespace(char ch[], int start, int length) {
        outputLocation();
        printer.println("ignorableWhitespace: {" + new String(ch, start,  length) + "}");
    }

    public void endPrefixMapping(String prefix) {
        outputLocation();
        printer.println("endPrefixMapping: " + prefix);
    }

    public void skippedEntity(String name) {
        outputLocation();
        printer.println("skippedEntity: " + name);
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    public void processingInstruction(String target, String data) {
        outputLocation();
        printer.println("processingInstruction: " + target + " = " + data);
    }

    public void startPrefixMapping(String prefix, String uri) {
        outputLocation();
        printer.println("startPrefixMapping: " + prefix + "->" + uri);
    }

    public void endElement(
            String namespaceURI, String localName, String qName) {
        outputLocation();
        printer.println("endElement: {" + namespaceURI + "}" + localName + "/" + qName);
    }

    public void startElement(
            String namespaceURI, String localName, String qName,
            Attributes atts) {
        outputLocation();
        printer.println("startElement: {" + namespaceURI + "}" + localName + "/" + qName);
    }
}
