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

package com.volantis.styling.impl.compiler;

import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.Selector;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.compiler.Specificity;

import java.util.List;

/**
 * Constructs a {@link Matcher} from a {@link com.volantis.mcs.themes.Selector} and collates some other
 * information from the {@link Selector} that does not fit into the
 * {@link Matcher}.
 */
public interface MatcherBuilder {

    /**
     * Get the {@link Matcher} for the specified selector.
     *
     * @param selector The selector for which the {@link Matcher} is required.
     *
     * @return The constructed {@link Matcher}.
     */
    Matcher getMatcher(Selector selector);

    /**
     * Get the pseudo style entities that appeared within the selector.
     * @return
     */
    List getPseudoStyleEntities();

    /**
     * Get the specificity of the {@link com.volantis.mcs.themes.Selector}.
     *
     * @return The specificity.
     */
    Specificity getSpecificity();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 30-Aug-05	9407/1	pduffin	VBM:2005083007 Added SelectorVisitor

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
