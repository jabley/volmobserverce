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

package com.volantis.xml.schema.validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * An {@link ErrorHandler} that collates the messages and will wrap them in a
 * single exception.
 */
class CollatingErrorHandler
        implements ErrorHandler {

    private final StringBuffer buffer = new StringBuffer();

    public CollatingErrorHandler() {
    }

    public void checkForErrors() throws SAXException {
        if (buffer.length() > 0) {
            throw new SAXException(buffer.toString());
        }
    }

    public void error(SAXParseException exception)
            throws SAXException {
        recordError("error", exception);
    }

    public void fatalError(SAXParseException exception)
            throws SAXException {
        recordError("fatal", exception);
    }

    public void warning(SAXParseException exception)
            throws SAXException {
        recordError("warning", exception);
    }

    private void recordError(String level, SAXParseException exception) {
        String systemId = exception.getSystemId();
        buffer.append(level).append(" - ");
        if (systemId != null) {
            buffer.append(systemId).append(":")
                    .append(exception.getLineNumber()).append(":")
                    .append(exception.getColumnNumber()).append(":");
        }
        buffer.append(exception.getLocalizedMessage()).append("\n");
    }
}
