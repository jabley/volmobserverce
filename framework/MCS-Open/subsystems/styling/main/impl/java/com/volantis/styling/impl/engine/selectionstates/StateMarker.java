package com.volantis.styling.impl.engine.selectionstates;

import com.volantis.styling.impl.engine.matchers.composites.DepthChangeListener;
import com.volantis.styling.impl.engine.DepthLevel;

/**
 * A marker indicating that a particular state may be active.
 */
public interface StateMarker extends DepthChangeListener, DepthLevel {
    /**
     * Returns the selection state object that this marker represents.
     */
    public SelectionState getState();

    /**
     * Flag the current node as matching.
     */
    public void match();

    /**
     * Returns true if the state is currently active.
     *
     * @return True if the state is currently active
     */
    public boolean isActive();
}
