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
 * Acts like a tee junction sending the error events to two separate handlers.
 */
public class TeeErrorHandler
        implements ErrorHandler {

    private ErrorHandler first;

    private ErrorHandler second;

    public ErrorHandler getFirst() {
        return first;
    }

    public void setFirst(ErrorHandler first) {
        this.first = first;
    }

    public ErrorHandler getSecond() {
        return second;
    }

    public void setSecond(ErrorHandler second) {
        this.second = second;
    }

    public void error(SAXParseException exception)
            throws SAXException {

        dispatchError(first, exception);
        dispatchError(second, exception);
    }

    private void dispatchError(
            ErrorHandler handler, SAXParseException exception)
            throws SAXException {

        if (handler != null) {
            handler.error(exception);
        }
    }

    public void fatalError(SAXParseException exception)
            throws SAXException {

        dispatchFatalError(first, exception);
        dispatchFatalError(second, exception);
        throw exception;
    }

    private void dispatchFatalError(
            ErrorHandler handler, SAXParseException exception)
            throws SAXException {

        if (handler != null) {
            handler.fatalError(exception);
        }
    }

    public void warning(SAXParseException exception)
            throws SAXException {

        dispatchWarning(first, exception);
        dispatchWarning(second, exception);
    }

    private void dispatchWarning(
            ErrorHandler handler, SAXParseException exception)
            throws SAXException {

        if (handler != null) {
            handler.warning(exception);
        }
    }
}
