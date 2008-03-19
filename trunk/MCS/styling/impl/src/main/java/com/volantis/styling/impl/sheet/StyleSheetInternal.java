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

package com.volantis.styling.impl.sheet;

import com.volantis.styling.impl.engine.listeners.ImmutableListeners;
import com.volantis.styling.impl.engine.sheet.ImmutableStylerList;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.state.StateContainer;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.values.ImmutablePropertyValues;

/**
 * Exposes those aspects of a {@link CompiledStyleSheet} that are needed
 * internally.
 *
 * @mock.generate base="CompiledStyleSheet"
 */
public interface StyleSheetInternal
        extends CompiledStyleSheet {

    /**
     * Get the list of listeners that are interested in start / end element
     * events.
     *
     * @return An immutable list of listeners.
     */
    ImmutableListeners getListeners();

    /**
     * Create a container for the state needed by the {@link Styler}s and their
     * associated {@link Matcher}s.
     *
     * @return The newly created container.
     */
    StateContainer createStateContainer();

    /**
     * Get the list of {@link Styler}s.
     *
     * @return A list of {@link Styler}.
     */
    ImmutableStylerList getStylerList();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
