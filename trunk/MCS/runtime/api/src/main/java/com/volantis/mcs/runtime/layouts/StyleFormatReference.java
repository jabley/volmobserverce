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

package com.volantis.mcs.runtime.layouts;

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.themes.CustomStyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;

/**
 * Wrapper for a format reference.
 */
public final class StyleFormatReference
        extends CustomStyleValue {

    private final FormatReference reference;

    public StyleFormatReference(FormatReference reference) {
        this.reference = reference;
    }

    public FormatReference getReference() {
        return reference;
    }

    public String getStandardCSS() {
        // @todo add parameters
        return reference.getStem();
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    // Javadoc inherited.
    public int getStandardCost() {
        throw new UnsupportedOperationException();
    }


    public Object copy() {
        return this;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Dec-05	10632/3	pduffin	VBM:2005120504 Porting forward changes from MCS 3.5

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 29-Nov-05	10505/4	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 ===========================================================================
*/
