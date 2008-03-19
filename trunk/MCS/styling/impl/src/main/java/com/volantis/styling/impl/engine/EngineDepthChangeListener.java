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

import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.impl.engine.matchers.InternalMatcherContext;
import com.volantis.styling.impl.engine.matchers.composites.DepthChangeListener;
import com.volantis.styling.impl.state.StateContainer;

/**
 * A {@link DepthChangeListener} that wraps a {@link DepthChangeListener} for
 * use within the engine.
 *
 * <p>This ensures that the wrapped listener has access to the state container
 * for the style sheet to which they belong by setting it before invoking the
 * methods. It also keeps track of the depth at which the associated style
 * sheet was added so that they can be removed at the appropriate time.</p>
 */
public class EngineDepthChangeListener
        implements DepthChangeListener {

    /**
     * The wrapped listener.
     */
    private final DepthChangeListener listener;

    /**
     * The state container that the wrapped listener needs to access.
     */
    private final StateContainer container;

    /**
     * The depth at which the associated style sheet was added.
     */
    private final int depth;

    /**
     * Initialise.
     *
     * @param listener  The wrapped listener.
     * @param container The state container that the wrapped listener needs to access.
     * @param depth     The depth at which the associated style sheet was added.
     */
    public EngineDepthChangeListener(
            DepthChangeListener listener,
            StateContainer container, int depth) {
        this.listener = listener;
        this.container = container;
        this.depth = depth;
    }

    // Javadoc inherited.
    public void beforeStartElement(MatcherContext context) {
        InternalMatcherContext internal = (InternalMatcherContext) context;
        internal.setContainer(container);
        listener.beforeStartElement(context);
    }

    // Javadoc inherited.
    public void afterEndElement(MatcherContext context) {
        InternalMatcherContext internal = (InternalMatcherContext) context;
        internal.setContainer(container);
        listener.afterEndElement(context);
    }

    public int getDepth() {
        return depth;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
