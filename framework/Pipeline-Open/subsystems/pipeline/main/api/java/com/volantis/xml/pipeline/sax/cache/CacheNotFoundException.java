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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.cache;

import org.xml.sax.Locator;

/**
 * This error class is used to indicate that we requested a named cache from
 * the configuration that was not present.
 */
public class CacheNotFoundException extends CacheProcessingException {

    /**
     * Construct a new XMLProcessingException instance
     * @param message The error or warning message, or null to use the
     * message from the embedded exception.
     * @param locator The locator object for the error or warning
     * (may be null).
     * @param cacheName The name of the cache that we failed to find.
     */
    public CacheNotFoundException(String message, Locator locator,
                                  String cacheName) {
        super(message, locator, cacheName);
    }

    /**
     * Construct a new XMLProcessingException instance
     * @param message The error or warning message, or null to use the
     * message from the embedded exception.
     * @param locator The locator object for the error or warning
     * (may be null).
     * @param cacheName The name of the cache that we failed to find.
     * @param e Another exception to embed in this one.
     */
    public CacheNotFoundException(String message, Locator locator,
                                  String cacheName, Exception e) {
        super(message, locator, cacheName, e);
    }

    /**
     * Construct a new XMLProcessingException instance
     * @param message The error or warning message.
     * @param publicId The public identifer of the entity that generated the
     * error or warning.
     * @param systemId The system identifer of the entity that generated the
     * error or warning.
     * @param lineNumber The line number of the end of the text that caused
     * the error or warning.
     * @param columnNumber The column number of the end of the text that cause
     * the error or warning.
     * @param cacheName The name of the cache that we failed to find.
     */
    public CacheNotFoundException(String message, String publicId,
                                  String systemId, int lineNumber,
                                  int columnNumber, String cacheName) {
        super(message, publicId, systemId,
              lineNumber, columnNumber, cacheName);
    }

    /**
     * Construct a new XMLProcessingException instance
     * @param message The error or warning message, or null to use the
     * message from the embedded exception.
     * @param publicId The public identifer of the entity that generated the
     * error or warning.
     * @param systemId The system identifer of the entity that generated the
     * error or warning.
     * @param lineNumber The line number of the end of the text that caused
     * the error or warning.
     * @param columnNumber The column number of the end of the text that cause
     * the error or warning.
     * @param cacheName The name of the cache that we failed to find.
     * @param e Another exception to embed in this one.
     */
    public CacheNotFoundException(String message, String publicId,
                                  String systemId, int lineNumber,
                                  int columnNumber, String cacheName,
                                  Exception e) {
        super(message, publicId, systemId,
              lineNumber, columnNumber, cacheName, e);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 09-Jun-03	49/1	adrian	VBM:2003060505 updated headers and cleaned up imports following changes required for addition of cacheBody elements

 ===========================================================================
*/
