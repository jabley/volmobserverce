package com.volantis.styling.impl.state.impl;

import com.volantis.styling.impl.engine.selectionstates.StateMarker;
import com.volantis.styling.impl.engine.StylerContext;

/**
 * Allows for unregistration of a state.
 */
public interface StateUnregisterer {
    /**
     * Unregister a given marker within a given context.
     *
     * @param state The marker to unregister
     * @param context The context with which to unregister
     */
    public void unregisterState(StateMarker state, StylerContext context);
}
