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
import org.jibx.runtime.ITrackSource;

/**
 * Adapts JiBX's jibxSource location into general source location for the
 * repository objects.
 */
public class JiBXSourceLocation
        implements SourceLocation {

    private final ITrackSource jibxSource;

    public JiBXSourceLocation(Object object) {
        // Get either the JiBX source for the provided object. This will return
        // a fake one if a real one is not available.
        jibxSource = JIBXSourceFinder.getInstance().getJIBXSource(object);
    }

    public String getSourceDocumentName() {
        return jibxSource.jibx_getDocumentName();
    }

    public int getSourceLineNumber() {
        return jibxSource.jibx_getLineNumber();
    }

    public int getSourceColumnNumber() {
        return jibxSource.jibx_getColumnNumber();
    }

    public String toString() {
        return getSourceDocumentName() + ":" + getSourceLineNumber() + ":" +
                getSourceColumnNumber();
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
