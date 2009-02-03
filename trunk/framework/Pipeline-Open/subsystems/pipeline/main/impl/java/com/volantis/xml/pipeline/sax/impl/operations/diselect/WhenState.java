package com.volantis.xml.pipeline.sax.impl.operations.diselect;

/**
 * Specialization for the diselect:when element.
 */
final class WhenState extends State {

    /**
     * The state for the containing select.
     */
    private SelectState selectState;

    /**
     * Initialise.
     *
     * @param selectState The state for the containing select.
     */
    public WhenState(SelectState selectState) {
        super(selectState);

        this.selectState = selectState;
    }

    /**
     * Override to inform the containing select state whether a when element
     * has matched.
     *
     * @param processedBody True if the body is to be processed, false otherwise.
     */
    public void setProcessBody(boolean processedBody) {
        super.setProcessBody(processedBody);

        if (processedBody) {
            selectState.setMatchFound();
        }
    }

    /**
     * Override to support when behaviour.
     *
     * @return True if nested select had a precept of matchevery, or no previous
     *         when elements had matched.
     */
    public boolean canProcessBody() {
        return selectState.shouldTestMatches();
    }
}
