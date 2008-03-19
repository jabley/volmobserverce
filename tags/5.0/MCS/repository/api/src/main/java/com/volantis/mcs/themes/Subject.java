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
 * An object that can be applied as the subject of a combined selector,
 * either a selector sequence or a combined selector.
 *
 * @mock.generate base="Selector"
 */
public interface Subject
    extends Selector {
    
    /**
     * Appends a selector sequence to the end of a selector.
     *
     * <p>If the Subject is a SelectorSequence, then a new combined
     * selector will be created with the object on which this was invoked as
     * the context and the argument as the subject.</p>
     *
     * <p>If the Subject is a CombinedSelector, then the existing combined
     * selector will be returned, with the specified argument appended onto its
     * subject.</p>
     *
     * @param sequence The selector sequence to append to this subject
     * @return A combined selector representing the result of the append
     */
    public CombinedSelector append(
            CombinatorEnum combinator, SelectorSequence sequence);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/5	pduffin	VBM:2005083007 Removed old themes model

 30-Aug-05	9407/1	pduffin	VBM:2005083007 Added SelectorVisitor

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
