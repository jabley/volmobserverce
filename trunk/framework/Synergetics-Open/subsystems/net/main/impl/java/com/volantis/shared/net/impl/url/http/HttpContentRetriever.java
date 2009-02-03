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
package com.volantis.shared.net.impl.url.http;

import com.volantis.shared.net.url.http.HttpUrlConfiguration;
import com.volantis.shared.net.url.http.HttpContent;
import com.volantis.shared.time.Period;

import java.io.IOException;
import java.net.URL;

/**
 * Interface to retrieve HTTP contents from a given URL.
 */
public interface HttpContentRetriever {

    /**
     * Returns an HTTP content from the specified URL.
     *
     * @param url the URL to retrieve the content from
     * @param timeout timeout to be used for the request
     * @param httpConfig configuration for extra parameters
     * @return the retrieved HTTP content
     * @throws IOException if getting the content failed
     */
    public HttpContent retrieve(URL url, Period timeout,
                                HttpUrlConfiguration httpConfig)
        throws IOException;
}
