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

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Acts like a tee junction sending the error events to two separate handlers.
 */
public class TeeEntityResolver
        implements EntityResolver {

    private EntityResolver first;

    private EntityResolver second;

    public EntityResolver getFirst() {
        return first;
    }

    public void setFirst(EntityResolver first) {
        this.first = first;
    }

    public EntityResolver getSecond() {
        return second;
    }

    public void setSecond(EntityResolver second) {
        this.second = second;
    }

    public InputSource resolveEntity(String publicId, String systemId)
            throws IOException, SAXException {
        InputSource source = resolveEntity(first, publicId, systemId);
        if (source == null) {
            source = resolveEntity(second, publicId, systemId);
        }
        return source;
    }

    private InputSource resolveEntity(
            EntityResolver resolver, String publicId, String systemId)
            throws IOException, SAXException {
        if (resolver == null) {
            return null;
        } else {
            return resolver.resolveEntity(publicId, systemId);
        }
    }
}
