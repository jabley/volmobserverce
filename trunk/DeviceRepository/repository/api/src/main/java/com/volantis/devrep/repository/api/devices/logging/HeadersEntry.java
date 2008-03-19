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
package com.volantis.devrep.repository.api.devices.logging;

import com.volantis.mcs.http.HttpHeaders;
import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Set;
import java.util.TreeSet;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * The value of this implementation of Entry class is a set of Header objects.
 */
public class HeadersEntry extends Entry {
    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(HeadersEntry.class);

    /**
     * List of headers in the entry.
     */
    private Set headers;

    public HeadersEntry(final String resolvedName, final String deviceType,
                        final HttpHeaders headers) {
        super(resolvedName, deviceType, "headers");
        this.headers = new TreeSet();
        if (headers != null) {
            for (Enumeration enum = headers.getHeaderNames();
                 enum.hasMoreElements(); ) {
                final String name = (String) enum.nextElement();
                final Enumeration headersEnum = headers.getHeaders(name);
                while (headersEnum.hasMoreElements()) {
                    final String value = (String) headersEnum.nextElement();
                    this.headers.add(new Header(name, value));
                }
            }
        }
    }

    /**
     * Adds a header to the stored headers.
     * @param header the header to add
     */
    public void addHeader(final Header header) {
        headers.add(header);
    }

    /**
     * Returns an iterator over the stored headers.
     * @return the iterator for the headers
     */
    public Iterator getHeaders() {
        return headers.iterator();
    }

    // javadoc inherited
    public String getValue() {
        final StringBuffer buffer = new StringBuffer();
        for (Iterator iter = headers.iterator(); iter.hasNext(); ) {
            final Header header = (Header) iter.next();
            buffer.append("header-name:");
            buffer.append(header.getName());
            buffer.append("\r\n");
            buffer.append("header-value:");
            buffer.append(header.getValue());
            if (iter.hasNext()) {
                buffer.append("\r\n");
            }
        }
        return buffer.toString();
    }

    // javadoc inherited
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        return headers.equals(((HeadersEntry) o).headers);
    }

    // javadoc inherited
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + headers.hashCode();
        return result;
    }
}
