package com.volantis.styling.impl.engine.selectionstates;

import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.impl.state.impl.StateUnregisterer;

/**
 * A selection state for descendant selection.
 */
public class DescendantSelectionState extends SelectionState {
    // Javadoc inherited
    public StateMarker createStateInstance(StateUnregisterer unreg) {
        return new DescendantSelectionStateMarker(this, unreg);
    }

    private class DescendantSelectionStateMarker implements StateMarker {
        private SelectionState state;

        private int depth;

        StateUnregisterer unregisterer;

        public DescendantSelectionStateMarker(SelectionState state,
                                              StateUnregisterer unreg) {
            this.state = state;
            this.depth = 1;
            this.unregisterer = unreg;
        }

        // Javadoc inherited
        public SelectionState getState() {
            return state;
        }

        // Javadoc inherited
        public void match() {
            // No action required - we're already within the triggering
            // element - being within a second one isn't really relevant
        }

        // Javadoc inherited
        public boolean isActive() {
            // The descendant selection state is always active - once the state
            // becomes inactive it triggers its own removal.
            return true;
        }

        public void beforeStartElement(MatcherContext context) {
            depth++;
        }

        public void afterEndElement(MatcherContext context) {
            depth--;
            if (depth <= 0) {
                // We've iterated out of the topmost element triggering this
                // state - flag ourselves for removal
                unregisterer.unregisterState(this, null);
            }
        }

        /* Returns current depth */
        public int getDepth() {
            return depth;
        }
    }
}
