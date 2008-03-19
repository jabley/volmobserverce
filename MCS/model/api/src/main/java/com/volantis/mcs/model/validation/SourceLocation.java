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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.model.validation;

/**
 * Provides access to the location of a problem within the source.
 */
public interface SourceLocation {

    /**
     * Get the name of the source document.
     *
     * @return The name of the source document.
     */
    public String getSourceDocumentName();

    /**
     * Get the line number within the source document.
     *
     * @return The line number within the source document.
     */
    public int getSourceLineNumber();

    /**
     * Get the column number within the source document.
     *
     * @return The column number within the source document.
     */
    public int getSourceColumnNumber();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 ===========================================================================
*/
