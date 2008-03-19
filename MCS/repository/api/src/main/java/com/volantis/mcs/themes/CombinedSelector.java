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

/**
 * A combined selector, which relates two selectors to each other, for example
 * as descendants or siblings.
 *
 * <p>Examples in CSS would include:
 * <ul>
 * <li><code>a b</code></li>
 * <li><code>a + b</code></li>
 * <li><code>a &gt; b</code></li>
 * </ul>
 * </p>
 *
 * @mock.generate base="Subject"
 */
public interface CombinedSelector extends Subject {
    
    /**
     * Returns the contextual selector sequence for this combined selector.
     *
     * @return The contextual selector sequence for this combined selector
     */
    public SelectorSequence getContext();

    /**
     * Sets the contextual selector sequence for this combined selector.
     *
     * @param newContext The contextual selector sequence for this combined
     *                   selector
     */
    public void setContext(SelectorSequence newContext);

    /**
     * Returns the subject (either a selector sequence or another
     * combined selector) for this combined selector.
     *
     * @return The subject for this combined selector
     */
    public Subject getSubject();

    /**
     * Sets the subject for this combined selector
     *
     * @param newSubject The subject for this combined selector
     */
    public void setSubject(Subject newSubject);

    /**
     * Returns the combinator type for this combined selector.
     *
     * @return The combinator type for this combined selector
     */
    public CombinatorEnum getCombinator();

    /**
     * Sets the combinator type for this combined selector.
     *
     * @param newCombinator The combinator type for this combined selector
     */
    public void setCombinator(CombinatorEnum newCombinator);
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 30-Aug-05	9407/3	pduffin	VBM:2005083007 Added SelectorVisitor

 30-Aug-05	9407/1	pduffin	VBM:2005083007 Added SelectorVisitor

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
