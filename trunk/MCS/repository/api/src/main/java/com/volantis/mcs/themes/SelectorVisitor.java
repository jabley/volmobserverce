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
 * Type safe mechanism for accessing a selector.
 *
 * @mock.generate
 */
public interface SelectorVisitor {

    /**
     * Visit an attribute selector.
     *
     * @param selector The selector to visit.
     */
    void visit(AttributeSelector selector);

    /**
     * Visit a class selector.
     *
     * @param selector The selector to visit.
     */
    void visit(ClassSelector selector);

    /**
     * Visit an id selector.
     *
     * @param selector The selector to visit.
     */
    void visit(IdSelector selector);

    /**
     * Visit a pseudo class selector.
     *
     * @param selector The selector to visit.
     */
    void visit(PseudoClassSelector selector);

    /**
     * Visit a nth child selector.
     *
     * @param selector The selector to visit.
     */
    void visit(NthChildSelector selector);

    /**
     * Visit a pseudo element selector.
     *
     * @param selector The selector to visit.
     */
    void visit(PseudoElementSelector selector);

    /**
     * Visit a universal selector.
     *
     * @param selector The selector to visit.
     */
    void visit(UniversalSelector selector);

    /**
     * Visit a type selector.
     *
     * @param selector The selector to visit.
     */
    void visit(TypeSelector selector);

    /**
     * Visit a selector sequence.
     *
     * @param sequence The sequence to visit.
     */
    void visit(SelectorSequence sequence);
    
    /**
     * Visit a combined selector.
     *
     * @param selector The selector to visit.
     */
    void visit(CombinedSelector selector);


    /**
     * Visit an invalid selector.
     *
     * @param selector The selector to visit.
     */
    void visit(InvalidSelector selector);

    /**
     * Visit an inline style selector
     * @param selector
     */
    void visit(InlineStyleSelector selector);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9965/1	ianw	VBM:2005101811 Fix file locations

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/5	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 ===========================================================================
*/
