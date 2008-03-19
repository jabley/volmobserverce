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

package com.volantis.mcs.model.jibx;

import com.volantis.mcs.model.validation.SourceLocation;

/**
 * Useful base class for objects that form part of a model that is marshalled
 * and unmarshalled using JiBX.
 */
public abstract class JiBXBase
        implements SourceLocation {

    private final SourceLocation location;

    protected JiBXBase() {
        this(null);
    }

    /**
     * Initialise.
     *
     * @param location The source location of the object, may be null.
     */
    public JiBXBase(SourceLocation location) {
        if (location == null) {
            // Get either the JiBX source for this class. This will return a
            // fake one if a real one is not available.
            location = new JiBXSourceLocation(
                    JIBXSourceFinder.getInstance().getJIBXSource(this));
        }
        this.location = location;
    }

    /**
     * Get the location of the definition of this in the source.
     *
     * @return The source location.
     */
    public SourceLocation getSourceLocation() {
        return this;
    }

    public String getSourceDocumentName() {
        return location.getSourceDocumentName();
    }

    public int getSourceLineNumber() {
        return location.getSourceLineNumber();
    }

    public int getSourceColumnNumber() {
        return location.getSourceColumnNumber();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 ===========================================================================
*/
