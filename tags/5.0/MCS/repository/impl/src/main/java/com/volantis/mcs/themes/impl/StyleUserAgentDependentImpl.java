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

import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.StyleUserAgentDependent;

/**
 */
public final class StyleUserAgentDependentImpl
        extends StyleValueImpl implements StyleUserAgentDependent {

    /**
     * Package private constructor for use by JiBX.
     */
    StyleUserAgentDependentImpl() {
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        throw new UnsupportedOperationException();
    }

    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    protected boolean equalsImpl(Object o) {
        return false;
    }

    protected int hashCodeImpl() {
        return System.identityHashCode(this);
    }

    public String getStandardCSS() {
        return null;
    }

    public int getStandardCost() {
        return 0;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
