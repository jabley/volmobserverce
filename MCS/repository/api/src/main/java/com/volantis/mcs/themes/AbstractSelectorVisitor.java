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

package com.volantis.mcs.themes;



public abstract class AbstractSelectorVisitor
        implements SelectorVisitor {

    protected void visitSelector(Selector selector) {
    }

    protected void visitElementSelector(ElementSelector selector) {
        visitSelector(selector);
    }

    public void visit(AttributeSelector selector) {
        visitSelector(selector);
    }

    public void visit(ClassSelector selector) {
        visitSelector(selector);
    }

    public void visit(IdSelector selector) {
        visitSelector(selector);
    }

    public void visit(PseudoClassSelector selector) {
        visitSelector(selector);
    }

    public void visit(NthChildSelector selector) {
        visitSelector(selector);
    }

    public void visit(PseudoElementSelector selector) {
        visitSelector(selector);
    }

    public void visit(UniversalSelector selector) {
        visitElementSelector(selector);
    }

    public void visit(TypeSelector selector) {
        visitElementSelector(selector);
    }

    public void visit(InvalidSelector selector) {
        visitSelector(selector);
    }

    public void visit (InlineStyleSelector selector) {
        visitSelector(selector);
    }

    // Javadoc inherited.
    public void visit(SelectorSequence sequence) {
        sequence.visitChildren(this);
    }

    /**
     * Visit the children of this combined selector.
     *
     * @param selector The selector being visited.
     */
    public void visit(CombinedSelector selector) {
        selector.getSubject().accept(this);
        selector.getContext().accept(this);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9965/1	ianw	VBM:2005101811 Fix file locations

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9412/1	adrianj	VBM:2005083007 CSS renderer using new model

 ===========================================================================
*/
