package com.volantis.styling.impl.engine.selectionstates;

import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.impl.state.impl.StateUnregisterer;

import java.util.BitSet;

/**
 * A selection state for sibling selection.
 */
public class SiblingSelectionState extends SelectionState {
    // Javadoc inherited
    public StateMarker createStateInstance(StateUnregisterer unreg) {
        return new SiblingSelectionStateMarker(this, unreg);
    }

    private class SiblingSelectionStateMarker implements StateMarker {
        private SelectionState state;

        private int depth;

        private BitSet matchingDepths = new BitSet();

        private StateUnregisterer unreg;

        public SiblingSelectionStateMarker(SelectionState state,
                                           StateUnregisterer unreg) {
            this.state = state;
            this.depth = 2;
            this.unreg = unreg;
            this.matchingDepths.set(depth);
        }

        // Javadoc inherited
        public SelectionState getState() {
            return state;
        }

        // Javadoc inherited
        public void match() {
            this.matchingDepths.set(depth);
        }

        // Javadoc inherited
        public boolean isActive() {
            // The descendant selection state is always active - once the state
            // becomes inactive it triggers its own removal.
            return matchingDepths.get(depth);
        }

        public void beforeStartElement(MatcherContext context) {
            depth++;
        }

        public void afterEndElement(MatcherContext context) {
            depth--;
            if (depth <= 0) {
                // We've iterated out of scope of the topmost element triggering this
                // state - flag ourselves for removal
                unreg.unregisterState(this, null);
            } else {
                matchingDepths.clear(depth + 2);
            }
        }

        /* Returns current depth */
        public int getDepth() {
            return depth;
        }
    }
}
