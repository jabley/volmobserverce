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

package com.volantis.shared.net.url;

import com.volantis.shared.dependency.Dependency;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents the content of a resource referenced by a URL.
 *
 * @mock.generate
 */
public interface URLContent {

    /**
     * Open an input stream onto the content.
     *
     * @return The {@link InputStream}.
     */
    InputStream getInputStream() throws IOException;

    /**
     * Get the character encoding of the stream.
     *
     * <p>Currently this always returns null, this is due to the fact that
     * there was not enough time to write the code to extract it from the
     * header and because that is what the equivalent previous code used to
     * do, almost. In fact it returned the content encoding from the underlying
     * <code>URLConnection</code> but that indicates whether the content has
     * been compressed say with gzip which it hardly ever is so it always
     * returned null which was quite fortunate as otherwise it would have
     * tried to use it as a character set!!</p>  
     *
     * @return The character encoding, or null if no character encoding was
     *         specified.
     */
    String getCharacterEncoding() throws IOException;

    /**
     * Returns the dependency object for the URL content.
     * @return the dependency object
     */
    Dependency getDependency();
    
    /**
     * Attach a property object to the conetent. The property may be
     * for example a object created of unmarshaled content. This
     * is for improving cache performance.
     * @param key of the property
     * @param value of the property
     */
    void setProperty(Object key, Object value);
    
    /**
     * Retrieved property attached to the content;
     * The property may be for example a object 
     * created of unmarshaled content. This is for 
     * improving cache performance.
     * @param key of the property
     * @return the property attached to content
     */
    Object getProperty(Object key);
    
}
