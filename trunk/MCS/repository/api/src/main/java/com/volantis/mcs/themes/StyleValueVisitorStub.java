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
 * Stub implementation of {@link StyleValueVisitor} to allow easy subclassing.
 */ 
public class StyleValueVisitorStub implements StyleValueVisitor {

    public void visit(StyleAngle value, Object object) {
    }

    public void visit(StyleColor value, Object object) {
    }

    public void visit(StyleColorName value, Object object) {
    }

    public void visit(StyleColorPercentages value, Object object) {
    }

    public void visit(StyleColorRGB value, Object object) {
    }

    public void visit(StyleComponentURI value, Object object) {
    }

    public void visit(StyleTranscodableURI value, Object object) {
    }

    public void visit(StyleFrequency value, Object object) {
    }

    public void visit(StyleFunctionCall value, Object object) {
    }

    public void visit(StyleIdentifier value, Object object) {
    }

    public void visit(StyleInherit value, Object object) {
    }

    public void visit(StyleInteger value, Object object) {
    }

    public void visit(StyleInvalid value, Object object) {
    }

    public void visit(StyleKeyword value, Object object) {
    }

    public void visit(StyleLength value, Object object) {
    }

    public void visit(StyleList value, Object object) {
    }

    public void visit(StyleNumber value, Object object) {
    }

    public void visit(StylePair value, Object object) {
    }

    public void visit(StylePercentage value, Object object) {
    }

    public void visit(StyleString value, Object object) {
    }

    public void visit(StyleURI value, Object object) {
    }

    public void visit(StyleUserAgentDependent value, Object object) {
    }

    public void visit(StyleTime value, Object object) {
    }

    public void visit(CustomStyleValue value, Object object) {
    }

    public void visit(StyleFraction value, Object object) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/3	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
