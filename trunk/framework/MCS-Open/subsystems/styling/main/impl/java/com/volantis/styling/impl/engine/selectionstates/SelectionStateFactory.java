package com.volantis.styling.impl.engine.selectionstates;

import com.volantis.styling.impl.engine.matchers.composites.Operator;

/**
 * Factory for Selection states
 */
public class SelectionStateFactory {
    private static final SelectionStateFactory factoryInstance =
            new SelectionStateFactory();

    public static SelectionStateFactory getInstance() {
        return factoryInstance;
    }

    private SelectionStateFactory() {
    }

    public SelectionState createSelectionState(Operator operator) {
        if (operator == Operator.ADJACENT) {
            return new AdjacentSelectionState();
        } else if (operator == Operator.CHILD) {
            return new ChildSelectionState();
        } else if (operator == Operator.DESCENDANT) {
            return new DescendantSelectionState();
        } else if (operator == Operator.SIBLING) {
            return new SiblingSelectionState();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
