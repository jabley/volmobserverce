package com.volantis.styling.impl.compiler;

import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.composites.Operator;
import com.volantis.styling.impl.engine.selectionstates.SelectionState;

/**
 * Key for indexing states. Contains the data that needs to be the same in
 * order for two states to be combined.
 */
public class StateIndexKey {
    /**
     * The required state that must be active before this state can be triggered,
     * or null if no state is required.
     */
    private final SelectionState requiredState;

    /**
     * The matcher on which this state will trigger.
     */
    private final Matcher matcher;

    /**
     * The operator to apply for this state.
     */
    private final Operator operator;

    public StateIndexKey(SelectionState requiredState,
                         Matcher matcher,
                         Operator operator) {
        this.requiredState = requiredState;
        this.matcher = matcher;
        this.operator = operator;
    }

    // Javadoc inherited
    public int hashCode() {
        int hash = 293;
        hash = 31 * (requiredState == null ? 0 : requiredState.hashCode());
        hash = 31 * matcher.hashCode();
        hash = 31 * operator.hashCode();
        return hash;
    }

    // Javadoc inherited
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            StateIndexKey other = (StateIndexKey) obj;
            // Required state should be the same instance and operators are
            // from a typesafe enum, so can use reference checks here
            return requiredState == other.requiredState &&
                    operator == other.operator &&
                    matcher.equals(other.matcher);
        } else {
            return false;
        }
    }
}
