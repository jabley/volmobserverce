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
package com.volantis.mcs.dom;

/**
 * Stub implementations of {@link WalkingDOMVisitor} methods to make
 * subclassing easier.
 */
public class WalkingDOMVisitorStub
        implements WalkingDOMVisitor {

    // Javadoc inherited.
    public void visit(Element element) {
         // Do nothing in a stub.
    }

    // Javadoc inherited.
    public void beforeChildren(Element element) {
         // Do nothing in a stub.
    }

    // Javadoc inherited.
    public void afterChildren(Element element) {
         // Do nothing in a stub.
    }

    // Javadoc inherited.
    public void visit(Text text) {
        // Do nothing in a stub.
    }

    // Javadoc inherited.
    public void visit(Comment comment) {
        // Do nothing in a stub.
    }

    public void visit(Document document) {
        // Do nothing in a stub.
    }

    public void beforeChildren(Document element) {
        // Do nothing in a stub.
    }

    public void afterChildren(Document element) {
        // Do nothing in a stub.
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9128/1	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
