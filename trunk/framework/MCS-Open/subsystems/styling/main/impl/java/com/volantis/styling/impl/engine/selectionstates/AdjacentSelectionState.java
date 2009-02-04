package com.volantis.styling.impl.engine.selectionstates;

import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.impl.state.impl.StateUnregisterer;

import java.util.BitSet;
import java.util.Stack;

/**
 * A selection state for adjacent selection.
 */
public class AdjacentSelectionState extends SelectionState {
    // Javadoc inherited
    public StateMarker createStateInstance(StateUnregisterer unreg) {
        return new AdjacentSelectionStateMarker(this, unreg);
    }
    
    private class AdjacentSelectionStateMarker implements StateMarker {
        private SelectionState state;

        private int depth;

        private BitSet withinMatchingElement = new BitSet();

        private BitSet matchDepth = new BitSet();

        private StateUnregisterer unregisterer;

        public AdjacentSelectionStateMarker(SelectionState state,
                                            StateUnregisterer unreg) {
            this.state = state;
            this.depth = 2;
            unregisterer = unreg;
            withinMatchingElement.set(depth);
            matchDepth.set(depth);
        }

        // Javadoc inherited
        public SelectionState getState() {
            return state;
        }

        // Javadoc inherited
        public void match() {
            withinMatchingElement.set(depth);
            matchDepth.set(depth);
        }

        // Javadoc inherited
        public boolean isActive() {
            // Direct descendant selectors are valid only in direct descendants
            // of the current targetted element.
            return matchDepth.get(depth);
        }

        // Javadoc inherited
        public void beforeStartElement(MatcherContext context) {
            depth++;
        }

        // Javadoc inherited
        public void afterEndElement(MatcherContext context) {
            if (withinMatchingElement.get(depth)) {
                withinMatchingElement.clear(depth);
            } else {
                matchDepth.clear(depth);
            }
            // Any child of this element clearly had no following sibling, so
            // can't match.
            matchDepth.clear(depth + 1);
            depth--;
        }

        /* Returns current depth */
        public int getDepth() {
            return depth;
        }
    }

}
