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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom;

import com.volantis.shared.iteration.IterationAction;

/**
 * A visitor that can be used to recurse down through a DOM.
 *
 * <p>Implementations of this visitor are responsible for recursing through
 * the document structure themselves but have the advantage that if necessary
 * they can use the call stack to maintain hierarchical state instead of
 * using instance data.</p>
 */
public class RecursingDOMVisitor
        implements DOMVisitor, NodeIteratee {

    /**
     * The state of the recursion.
     */
    private RecursionState state;

    /**
     * Initialise.
     */
    public RecursingDOMVisitor() {
        state = RecursionState.CONTINUE;
    }

    // Javadoc inherited.
    public void visit(Element element) {
    }

    // Javadoc inherited.
    public void visit(Text text) {
    }

    // Javadoc inherited.
    public void visit(Comment comment) {
    }

    // Javadoc inherited.
    public void visit(Document document) {
    }

    // Javadoc inherited.
    public IterationAction next(Node node) {

        // If the state is continue then visit the node, otherwise skip over
        // as the nodes are being skipped.
        if (state == RecursionState.CONTINUE) {
            // Visit the node.
            node.accept(this);
        }

        // Check the state to see whether it will prevent the iteration from
        // continuing.
        IterationAction action;
        if (state == RecursionState.CONTINUE) {
            // Keep iterating.
            action = IterationAction.CONTINUE;
        } else if (state == RecursionState.SKIPPING_SIBLINGS) {
            // Break out of this iteration but continue afterwards.
            action = IterationAction.BREAK;
            state = RecursionState.CONTINUE;
        } else if (state == RecursionState.SKIPPING_REMAINING) {
            // Break out of this and any future iterations.
            action = IterationAction.BREAK;
        } else {
            throw new IllegalStateException("Unknown state: " + state);
        }

        return action;
    }

    /**
     * Reset the recursing state.
     *
     * <p>This must only be called if the visitor is intended to be reused. It
     * can be called either before using it, or after using it. It must never
     * be called while visiting.</p>
     */
    protected void resetRecursingState() {
        state = RecursionState.CONTINUE;
    }

    /**
     * Check to see whether the visitor has been asked to skip the remainder of
     * the DOM being visited.
     *
     * @return True if the visitor is skipping the remainder of the DOM, false
     *         otherwise.
     */
    protected boolean isSkippingRemainder() {
        return state == RecursionState.SKIPPING_REMAINING;
    }

    /**
     * Force the visitor to break out of the closest enclosing iteration.
     *
     * <p>This will only have an effect once control has been returned to the
     * iteration, it is up to the caller to explicitly skip over any code
     * following the call to this method if they so require.</p>
     */
    protected void skipSiblings() {
        if (state == RecursionState.CONTINUE) {
            state = RecursionState.SKIPPING_SIBLINGS;
        }
    }

    /**
     * Force the visitor to break out of the current enclosing iterations and
     * will also prevent it from entering any additional iterations.
     *
     * <p>This will only have an effect once control has been returned to the
     * iteration, it is up to the caller to explicitly skip over any code
     * following the call to this method if they so require.</p>
     */
    protected void skipRemainder() {
        state = RecursionState.SKIPPING_REMAINING;
    }
}
