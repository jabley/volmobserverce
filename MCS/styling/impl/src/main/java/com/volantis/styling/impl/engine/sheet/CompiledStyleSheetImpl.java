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

package com.volantis.styling.impl.engine.sheet;

import com.volantis.styling.debug.DebugStylingWriter;
import com.volantis.styling.impl.engine.listeners.ImmutableListeners;
import com.volantis.styling.impl.sheet.StyleSheetInternal;
import com.volantis.styling.impl.state.ImmutableStateRegistry;
import com.volantis.styling.impl.state.StateContainer;
import com.volantis.styling.impl.state.StateFactory;

/**
 * A compiled style sheet.
 */
public class CompiledStyleSheetImpl
        implements StyleSheetInternal {

    /**
     * The factory used to create states.
     */
    private static final StateFactory STATE_FACTORY =
            StateFactory.getDefaultInstance();

    /**
     * The list of stylers.
     */
    private ImmutableStylerList stylerList;

    /**
     * The registry of states needed by the matchers.
     */
    private ImmutableStateRegistry stateRegistry;

    /**
     * The listeners that need to be informed of events during styling.
     */
    private ImmutableListeners listeners;

    /**
     * Initialise.
     *
     * @param stylerList    The list of stylers.
     * @param stateRegistry The registry of states needed by the matchers.
     * @param listeners     The listeners that need to be informed of events
     *                      during styling.
     */
    public CompiledStyleSheetImpl(
            ImmutableStylerList stylerList,
            ImmutableStateRegistry stateRegistry,
            ImmutableListeners listeners) {

        this.stylerList = stylerList;

        this.stateRegistry = stateRegistry;
        this.listeners = listeners;
    }

    // Javadoc inherited.
    public ImmutableListeners getListeners() {
        return listeners;
    }

    // Javadoc inherited.
    public StateContainer createStateContainer() {
        return STATE_FACTORY.createStateContainer(stateRegistry);
    }

    // Javadoc inherited.
    public ImmutableStylerList getStylerList() {
        return stylerList;
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        stylerList.debug(writer);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 20-Jul-05	9029/10	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 18-Jul-05	9029/7	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 19-Jul-05	9039/5	emma	VBM:2005071401 deep overlap - supermerge required

 19-Jul-05	9039/3	emma	VBM:2005071401 supermerge required

 15-Jul-05	9067/4	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.
 18-Jul-05	9029/4	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets


 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
