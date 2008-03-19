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

import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.impl.engine.listeners.Listener;
import com.volantis.styling.impl.engine.listeners.ListenerIteratee;
import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.impl.engine.matchers.composites.DepthChangeListener;

/**
 * Iterates over a list of {@link DepthChangeListener} and invokes their
 * {@link DepthChangeListener#afterEndElement(MatcherContext)} method.
 */
public class AfterEndElementIteratee
        implements ListenerIteratee {

    /**
     * The context to pass to the listeners.
     */
    private final MatcherContext matcherContext;

    /**
     * Initialise.
     *
     * @param matcherContext The context to pass to the listeners.
     */
    public AfterEndElementIteratee(MatcherContext matcherContext) {
        this.matcherContext = matcherContext;
    }

    // Javadoc inherited.
    public IterationAction next(Listener listener) {
        DepthChangeListener dc = (DepthChangeListener) listener;
        dc.afterEndElement(matcherContext);
        return IterationAction.CONTINUE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
