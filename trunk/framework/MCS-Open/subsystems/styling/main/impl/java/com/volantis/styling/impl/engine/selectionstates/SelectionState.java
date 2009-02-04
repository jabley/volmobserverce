package com.volantis.styling.impl.engine.selectionstates;

import com.volantis.styling.impl.state.impl.StateUnregisterer;

/**
 * A state object representing a possible state in CSS matching.
 */
public abstract class SelectionState {
    public abstract StateMarker createStateInstance(StateUnregisterer unreg);
}
