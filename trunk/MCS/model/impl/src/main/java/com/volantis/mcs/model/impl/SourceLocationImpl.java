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

package com.volantis.mcs.model.impl;

import com.volantis.mcs.model.validation.SourceLocation;

/**
 * Default implementation of {@link SourceLocation}.
 */
public class SourceLocationImpl
        implements SourceLocation {

    /**
     * The URL to the source document.
     */
    private final String url;

    /**
     * The line number.
     */
    private final int line;

    /**
     * The column number.
     */
    private final int column;

    /**
     * Initialise.
     *
     * @param url    The URL to the source document.
     * @param line   The line number.
     * @param column The column number.
     */
    public SourceLocationImpl(String url, int line, int column) {
        this.url = url;
        this.line = line;
        this.column = column;
    }

    // Javadoc inherited.
    public int getSourceLineNumber() {
        return line;
    }

    // Javadoc inherited.
    public int getSourceColumnNumber() {
        return column;
    }

    // Javadoc inherited.
    public String getSourceDocumentName() {
        return url;
    }
}
