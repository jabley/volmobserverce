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
package com.volantis.mcs.themes.types;

/**
 * This interface defines methods which will be called by the different
 * StyleType classes.
 */
public interface StyleTypeVisitor {
    public void visitStyleInheritType(StyleInheritType type);
    public void visitStyleLengthType(StyleLengthType type);
    public void visitStyleURIType(StyleURIType type);
    public void visitStyleKeywordsType(StyleKeywordsType type);
    public void visitStylePercentageType(StylePercentageType type);
    public void visitStyleTimeType(StyleTimeType type);
    public void visitStyleAngleType(StyleAngleType type);
    public void visitStylePairType(StylePairType type);
    public void visitStyleColorType(StyleColorType type);
    public void visitStyleFunctionCallType(StyleFunctionCallType type);
    public void visitStyleListType(StyleListType type);
    public void visitStyleIdentifierType(StyleIdentifierType type);
    public void visitStyleNumberType(StyleNumberType type);
    public void visitStyleIntegerType(StyleIntegerType type);
    public void visitStyleComponentURIType(StyleComponentURIType type);
    public void visitStyleTranscodableURIType(StyleTranscodableURIType type);
    public void visitStyleChoiceType(StyleChoiceType type);
    public void visitStyleOrderedSetType(StyleOrderedSetType type);
    public void visitStyleStringType(StyleStringType type);
    public void visitStyleFractionType(StyleFractionType type);
    public void visitStyleFrequencyType(StyleFrequencyType type);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
