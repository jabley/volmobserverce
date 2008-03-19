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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;

/**
 */
public final class StyleTranscodableURIImpl
    extends StyleValueImpl implements StyleTranscodableURI {

    private String uri;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleTranscodableURIImpl() {
    }

    /**
     * Initialise.
     *
     * @param uri The URI to the transcodable resource.
     */
    public StyleTranscodableURIImpl(final String uri) {
        this(null, uri);
    }

    /**
     * Initialise.
     *
     * @param location   The source location of the object, may be null.
     * @param uri The URI to the transcodable resource.
     */
    public StyleTranscodableURIImpl(final SourceLocation location,
                                 final String uri) {
        super(location);

        if (uri == null) {
            throw new IllegalArgumentException("reference cannot be null");
        }
        this.uri = uri;
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        return StyleValueType.TRANSCODABLE_URI;
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    public String getUri() {
        return uri;
    }

    protected boolean equalsImpl(final Object o) {
        if (!(o instanceof StyleTranscodableURIImpl)) {
            return false;
        }

        final StyleTranscodableURIImpl other = (StyleTranscodableURIImpl) o;

        return uri.equals(other.uri);
    }

    protected int hashCodeImpl() {
        return uri.hashCode();
    }

    public String getStandardCSS() {
        return "mcs-transcodable-url(" + getUri() + ")";
    }

    public int getStandardCost() {
        // 21 for mcs-transcodable-url(, 1 for )
        return 21 + getUri().length() + 1;
    }
}
