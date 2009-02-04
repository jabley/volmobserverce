package com.volantis.styling.impl.engine.selectionstates;

import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.impl.state.impl.StateUnregisterer;

import java.util.BitSet;

/**
 * A selection state for direct child selection.
 */
public class ChildSelectionState extends SelectionState {
    public StateMarker createStateInstance(StateUnregisterer unreg) {
        return new ChildSelectionStateMarker(this, unreg);
    }

    private class ChildSelectionStateMarker implements StateMarker {
        private SelectionState state;

        private int depth;

        private BitSet matchDepths = new BitSet();

        private StateUnregisterer unregisterer;

        public ChildSelectionStateMarker(SelectionState state,
                                         StateUnregisterer unreg) {
            this.state = state;
            this.depth = 1;
            matchDepths.set(depth + 1);
            this.unregisterer = unreg;
        }

        // Javadoc inherited
        public SelectionState getState() {
            return state;
        }

        // Javadoc inherited
        public void match() {
            // Store the previous depth for future reference, and set a new
            // match depth
            matchDepths.set(depth + 1);
        }

        // Javadoc inherited
        public boolean isActive() {
            // Direct descendant selectors are valid only in direct descendants
            // of the current targetted element.
            return matchDepths.get(depth);
        }

        // Javadoc inherited
        public void beforeStartElement(MatcherContext context) {
            depth++;
        }

        // Javadoc inherited
        public void afterEndElement(MatcherContext context) {
            depth--;
            if (depth <= 0) {
                // We've iterated out of the topmost element triggering this
                // state - flag ourselves for removal
                unregisterer.unregisterState(this, null);
            } else  {
                // We may have iterated out of a match - its children are no
                // longer active.
                matchDepths.clear(depth + 2);
            }
        }

        /* Returns current depth */
        public int getDepth() {
            return depth; 
        }
    }
}
