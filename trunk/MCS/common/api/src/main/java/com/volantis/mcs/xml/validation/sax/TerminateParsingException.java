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
package com.volantis.mcs.xml.validation.sax;

import org.xml.sax.SAXException;

/**
 * Benign SAXParsException used to stop parsing continuing before the end
 * of the document is reached. This exception should only be used when
 * parsing should terminate legitmately as opposed to because of a parse error.
 * <p/>
 * When this exception is thrown it should just be caught outside of the
 * parser and ignored.
 */
public class TerminateParsingException extends SAXException {
    public TerminateParsingException(String message) {
        super(message);
    }
}
