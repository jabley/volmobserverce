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

/**
 * Visitor interface for navigating Type hierarchies.
 */
public interface TypeVisitor {
    /**
     * Visit a Keywords type.
     * @param visitee The Keywords being visited
     * @param obj A general-purpose argument
     */
    public void visitKeywords(Keywords visitee, Object obj);

    /**
     * Visit a PairType type.
     * @param visitee The PairType being visited
     * @param obj A general-purpose argument
     */
    public void visitPairType(PairType visitee, Object obj);

    /**
     * Visit a ChoiceType type.
     * @param visitee The ChoiceType being visited
     * @param obj A general-purpose argument
     */
    public void visitChoiceType(ChoiceType visitee, Object obj);

    /**
     * Visit a TypeRef type.
     * @param visitee The TypeRef being visited
     * @param obj A general-purpose argument
     */
    public void visitTypeRef(TypeRef visitee, Object obj);

    /**
     * Visit a FractionType type.
     * @param visitee The FractionType being visited
     * @param obj A general-purpose argument
     */
    public void visitFractionType(FractionType visitee, Object obj);
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Oct-04	5833/1	adrianj	VBM:2004082605 Fix initial values for StylePropertyDetails

 ===========================================================================
*/
