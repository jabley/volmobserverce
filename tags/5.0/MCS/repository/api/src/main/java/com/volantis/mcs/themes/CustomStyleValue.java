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

package com.volantis.mcs.themes;


/**
 * The base class for all custom style values.
 */
public abstract class CustomStyleValue
        implements StyleValue {

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    // Javadoc inherited.
    public String getStandardCSS() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public int getStandardCost() {
        throw new UnsupportedOperationException();
    }

    public Object copy() {
        return this;
    }

    public String getSourceDocumentName() {
        throw new UnsupportedOperationException();
    }

    public int getSourceLineNumber() {
        throw new UnsupportedOperationException();
    }

    public int getSourceColumnNumber() {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return getStandardCSS();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/9	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/5	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 ===========================================================================
*/
