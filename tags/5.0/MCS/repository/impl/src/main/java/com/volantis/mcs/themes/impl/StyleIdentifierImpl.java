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

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;


public final class StyleIdentifierImpl
    extends StyleValueImpl implements StyleIdentifier {

    private String name;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleIdentifierImpl() {
    }

    /**
     * Initialise.
     *
     * @param identifier The identifier.
     */
    public StyleIdentifierImpl(String identifier) {
        this(null, identifier);
    }

    /**
     * Initialise.
     *
     * @param location   The source location of the object, may be null.
     * @param identifier The identifier.
     */
    public StyleIdentifierImpl(SourceLocation location, String identifier) {
        super(location);
        
        this.name = identifier;
    }

    public String getName() {
        return name;
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        return StyleValueType.IDENTIFIER;
    }

    // Javadoc inherited.
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    protected boolean equalsImpl(Object o) {
        if (!(o instanceof StyleIdentifierImpl)) {
            return false;
        }

        StyleIdentifierImpl other = (StyleIdentifierImpl) o;

        return name.equals(other.name);
    }

    protected int hashCodeImpl() {
        return name.hashCode();
    }

    public String getStandardCSS() {
        return name;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
