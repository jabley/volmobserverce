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

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.SourceLocation;

import java.util.List;

/**
 * A SelectorSequence contains a list of selectors and will either be used as
 * the subject or context of a {@link CombinedSelector}. Selector sequences can
 * contain element, attribute, class, id, pseudo class and pseudo element
 * selectors. However, it is invalid for a selector sequence that is the context
 * of a combined selector to contain either pseudo element or stateful pseudo
 * class selectors.
 *
 * @mock.generate base="Selector"
 */
public interface SelectorSequence extends Selector, Subject {

    /**
     * Get the location of the definition of this in the source.
     *
     * @return The source location.
     */ 
    SourceLocation getSourceLocation();

    /**
     * Return the selectors that make up this selector sequence. May be null
     * if no selectors have been added.
     *
     * @return List of selectors that make up this selector sequence.
     */
    List getSelectors();

    /**
     * Set the list of selectors that make up this selector sequence.
     *
     * <p>The list is copied so changes to the list after invoking this method
     * will not affect this object.</p>
     *
     * @param selectors List of selectors that will make up this selector
     * sequence
     */
    void setSelectors(List selectors);

    /**
     * Add a selector to the list of selectors that make up this selector
     * sequence.
     *
     * @param selector to be added to the list of selectors that make up this
     * selector sequence
     */
    void addSelector(Selector selector);

    /**
     * Checks whether there are any selectors associated with this sequence
     *
     * @return True if this sequence has selectors, false otherwise
     */
    boolean hasSelector();

    /**
     * Visit all the children selectors.
     *
     * @param visitor The visitor that will be invoked for each child selector.
     */
    void visitChildren(SelectorVisitor visitor);

    /**
     * Perform validation of SelectorSequence, varying what is acceptable
     * depending on whether the sequence is the context or subject of a
     * combined selector.
     *
     * @param context       ValidationContext against which to report errors
     * @param isContext     true if this selector sequence is the context of a
     *                      combined selector and false if it is a subject
     *                      selector.
     */
    void validate(ValidationContext context, boolean isContext);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 01-Nov-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/9	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/5	pduffin	VBM:2005083007 Fixed issue with build

 30-Aug-05	9407/1	pduffin	VBM:2005083007 Added SelectorVisitor

 ===========================================================================
*/
