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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes;

/**
 * A visitor for the different "types" of style values.
 * <p>
 * Really this is a degenerate visitor because it doesn't pass the visited
 * object itself. This wasn't required for my usage, but can certainly be 
 * added in future if necessary.  
 */ 
public interface StyleValueTypeVisitor {

    /**
     * Visit an Angle style value type.
     */ 
    void visitAngle();

    /**
     * Visit a Color style value type.
     */ 
    void visitColor();

    /**
     * Visit a function style value type.
     */
    void visitFunction();

    /**
     * Visit an Identifier style value type.
     */
    void visitIdentifier();

    /**
     * Visit an Inherit style value type.
     */ 
    void visitInherit();

    /**
     * Visit an Integer style value type.
     */ 
    void visitInteger();

    /**
     * Visit a Keyword style value type.
     */ 
    void visitKeyword();

    /**
     * Visit a Length style value type.
     */ 
    void visitLength();

    /**
     * Visit a List style value type.
     */ 
    void visitList();

    /**
     * Visit a Component URI style value type.
     */
    void visitComponentURI();

    /**
     * Visit a Transcodable URI style value type.
     */
    void visitTranscodableURI();

    /**
     * Visit a Number style value type.
     */ 
    void visitNumber();

    /**
     * Visit a Pair style value type.
     */ 
    void visitPair();

    /**
     * Visit a Percentage style value type.
     */ 
    void visitPercentage();

    /**
     * Visit a String style value type.
     */ 
    void visitString();

    /**
     * Visit a Time style value type.
     */ 
    void visitTime();

    /**
     * Visit a URI style value type.
     */ 
    void visitURI();

    /**
     * Visit a Frequency style value type.
     */
    void visitFrequency();

    /**
     * Visit a Fraction style value type.
     */
    void visitFraction();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Jan-04	2581/2	geoff	VBM:2003112704 Create the ThemeProxyElementDetailsFactory interface

 ===========================================================================
*/
