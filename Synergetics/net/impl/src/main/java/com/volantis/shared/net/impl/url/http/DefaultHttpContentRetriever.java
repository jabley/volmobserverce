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
package com.volantis.shared.net.impl.url.http;

import com.volantis.shared.net.impl.url.InternalURLContentManager;
import com.volantis.shared.net.url.http.HttpContent;

import java.io.IOException;
import java.net.URL;

import our.apache.commons.httpclient.HttpMethod;
import our.apache.commons.httpclient.HttpStatus;

/**
 * Default implementation of the AbstractHttpContentRetriever. Does no magic,
 * returns the content from the response stream, if the status code was 200 OK.
 */
public class DefaultHttpContentRetriever extends AbstractHttpContentRetriever {

    public DefaultHttpContentRetriever(InternalURLContentManager manager) {
        super(manager);
    }

    // javadoc inherited
    protected HttpContent createHttpContent(
            final URL url, final HttpMethod method) throws IOException {

        if (method.getStatusCode() != HttpStatus.SC_OK) {
            method.releaseConnection();
            throw new IOException(
                "Unexpected status code - " + method.getStatusCode());
        }

        return new HttpClientHttpContent(method);
    }
}
