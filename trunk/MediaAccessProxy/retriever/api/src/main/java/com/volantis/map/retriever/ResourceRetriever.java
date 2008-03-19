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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.retriever;

import com.volantis.map.retriever.http.HttpHeaders;
import com.volantis.map.retriever.http.MutableHttpHeaders;

import java.io.IOException;
import java.net.URL;

/**
 * The Resource retriever. This retrieve media resource 
 * and gains some other information from response.
 * 
 * @mock.generate
 */
public interface ResourceRetriever {

	/**
	 * Implementation of this method will be called when a request for resource is made
	 * 
	 * @param url URL to resource for retrieve. It is only HTTP protocol supported
	 * @param headers optional object which can contains additional headers, cookies for http request
	 * @throws ResourceRetrieverException
	 * @throws IOException
	 */
    public Representation execute(URL url, HttpHeaders headers)
    	throws ResourceRetrieverException, IOException;


    /**
     * Create a MutableHeadersInstance for use in requesting information
     *
     * @return a MutableHeaders instance
     */
    public MutableHttpHeaders createMutableHeaders();

}
