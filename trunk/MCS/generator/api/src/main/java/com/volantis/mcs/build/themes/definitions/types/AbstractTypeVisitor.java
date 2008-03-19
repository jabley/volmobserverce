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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes.definitions.types;

import java.util.Iterator;

/**
 * Abstract implementation of the TypeVisitor interface that provides
 * empty implementations for visits to SimpleTypes and recurses through
 * CompositeTypes.
 */
public abstract class AbstractTypeVisitor implements TypeVisitor {
    // Javadoc inherited
    public void visitKeywords(Keywords visitee, Object obj) {
    }

    // Javadoc inherited
    public void visitPairType(PairType visitee, Object obj) {
        // We need these null checks because the type hierarchy is currently
        // sparse - we don't bother creating the first and second types if they
        // are ones we are not interested in.
        final Type first = visitee.getFirst();
        if (first != null) {
            first.accept(this, obj);
        }
        final Type second = visitee.getSecond();
        if (second != null) {
            second.accept(this, obj);
        }
    }

    // Javadoc inherited
    public void visitChoiceType(ChoiceType visitee, Object obj) {
        Iterator it = visitee.getTypeIterator();
        while (it.hasNext()) {
            Type type = (Type) it.next();
            type.accept(this, obj);
        }
    }

    // Javadoc inherited
    public void visitTypeRef(TypeRef visitee, Object obj) {
    }

    // Javadoc inherited.
    public void visitFractionType(FractionType visitee, Object obj) {
        final Type numerator = visitee.getNumerator();
        if (numerator != null) {
            numerator.accept(this, obj);
        }
        final Type denominator = visitee.getDenominator();
        if (denominator != null) {
            denominator.accept(this, obj);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Oct-04	5833/1	adrianj	VBM:2004082605 Fix initial values for StylePropertyDetails

 ===========================================================================
*/
