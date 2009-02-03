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
package com.volantis.map.retriever.impl;

import com.volantis.map.common.streams.SeekableInputStream;
import com.volantis.map.retriever.Representation;
import com.volantis.shared.net.url.http.CachedHttpContentState;

import java.io.IOException;

import org.apache.commons.httpclient.HttpMethod;

/**
 * Default implementation of {@link Representation}.
 */
public class DefaultRepresentation implements Representation {

    private final HttpMethod method;

    /**
	 * InputStream of retrived resource
	 */
	private final SeekableInputStream resource;

	/**
	 * mime type of retrieved resource
	 */
	private final String fileType;

	/**
	 * Contains all needed information about caching from response header
	 */
	private CachedHttpContentState cacheInfo;


	public DefaultRepresentation(
        HttpMethod method,
        String fileType,
        CachedHttpContentState state,
        SeekableInputStream stream) {

        this.method = method;
        this.fileType = fileType;
		this.cacheInfo = state;
		this.resource = stream;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * InputStream of resource.
	 * @return the resource
	 */
	public SeekableInputStream getSeekableInputStream() {
		return resource;
	}

	/**
	 * @return the cacheInfo
	 */
	public CachedHttpContentState getCacheInfo() {
		return cacheInfo;
	}

    /**
     * Close the underlying HttpMethods connection
     * @throws IOException
     */
    public void close() throws IOException {
        resource.close();
        method.releaseConnection();
    }
}
