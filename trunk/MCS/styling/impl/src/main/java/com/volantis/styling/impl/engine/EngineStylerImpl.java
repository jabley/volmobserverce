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

package com.volantis.styling.impl.engine;

import com.volantis.mcs.themes.Priority;
import com.volantis.styling.compiler.Source;
import com.volantis.styling.compiler.Specificity;
import com.volantis.styling.debug.DebugStylingWriter;
import com.volantis.styling.impl.engine.matchers.InternalMatcherContext;
import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.state.StateContainer;

/**
 * An implementation of {@link EngineStyler}.
 */
public class EngineStylerImpl
        implements EngineStyler {

    /**
     * The nested styler.
     */
    private final Styler styler;

    /**
     * The container that this styler (and its contained objects) should use
     * if it needs to store any state.
     */
    private final StateContainer container;

    /**
     * The depth at which this styler was added to the engine.
     */
    private final int depth;

    /**
     * Initialise.
     *
     * @param styler The nested styler.
     * @param container The container for state used by this object.
     * @param depth The depth at which the styler was added to the engine.
     */
    public EngineStylerImpl(
            Styler styler, StateContainer container, int depth) {

        this.styler = styler;
        this.container = container;
        this.depth = depth;
    }

    public Source getSource() {
        return styler.getSource();
    }

    // Javadoc inherited.
    public Priority getPriority() {
        return styler.getPriority();
    }

    // Javadoc inherited.
    public Specificity getSpecificity() {
        return styler.getSpecificity();
    }

    // Javadoc inherited.
    public StylerResult style(StylerContext context) {
        InternalMatcherContext internal = (InternalMatcherContext)
                context.getMatcherContext();
        internal.setContainer(container);
        return styler.style(context);
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        styler.debug(writer);
    }

    // Javadoc inherited.
    public int getDepth() {
        return depth;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
