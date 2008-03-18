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

import java.io.InputStream;
import java.io.IOException;

import com.volantis.shared.net.url.http.CachedHttpContentState;

/**
 * Contains retrieved information from resource response 
 * like content type, caching information 
 * and InputStream of resource
 * 
 * @mock.generate
 */
public interface Representation {

	/**
	 * Return resource file type recognised by header of InputStream 
	 * 
	 * @return file type
	 */
	public String getFileType();
	
	/**
	 * Return InputStream of retrived resource
	 * 
	 * @return InputStream
	 */
	public SeekableInputStream getSeekableInputStream();
	
	/**
	 * Return CachedHttpContentState which contains needed information 
	 * about caching retrived resource
	 * 
	 * @return CachedHttpContentState
	 */
	public CachedHttpContentState getCacheInfo();

    /**
     * Close the representation and its underlying streams/sockets/files. The
     * same effect can also be achieved by calling close on the stream obtained
     * from this Represenation.
     *
     * @throws IOException
     */
    public void close() throws IOException;
}
