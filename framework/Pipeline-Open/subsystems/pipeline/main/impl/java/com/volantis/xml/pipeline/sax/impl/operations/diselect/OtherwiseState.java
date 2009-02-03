package com.volantis.xml.pipeline.sax.impl.operations.diselect;

/**
 * Specialization for the diselect:otherwise element.
 */
final class OtherwiseState extends State {

    /**
     * The state for the containing select.
     */
    private SelectState selectState;

    /**
     * Initialise.
     *
     * @param selectState The state for the containing select.
     */
    public OtherwiseState(SelectState selectState) {
        super(selectState);

        this.selectState = selectState;
    }

    /**
     * Override to support otherwise behaviour.
     *
     * @return True if and only if no previous when matches have been found.
     */
    public boolean canProcessBody() {
        return !selectState.matchFound();
    }
}
