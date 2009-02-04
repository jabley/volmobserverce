package com.volantis.styling.impl.sheet;

import com.volantis.styling.debug.DebugStylingWriter;
import com.volantis.styling.impl.engine.StylerContext;
import com.volantis.styling.impl.engine.selectionstates.SelectionState;
import com.volantis.styling.impl.state.StateIdentifier;

/**
 * A style delta representing a new state. Has no style information associated
 * with it, but instead updates the selector state.
 */
public class StateChangeStylesDelta implements StylesDelta {
    private SelectionState state;

    public StateChangeStylesDelta(SelectionState state) {
        this.state = state;
    }

    // Javadoc inherited
    public void applyTo(StylerContext context) {
        context.getMatcherContext().stateMatched(state, context);
    }

    // Javadoc inherited
    public void debug(DebugStylingWriter writer) {
        // TODO later output debug information
    }

    public String toString() {
        return "State to " + state;
    }
}
