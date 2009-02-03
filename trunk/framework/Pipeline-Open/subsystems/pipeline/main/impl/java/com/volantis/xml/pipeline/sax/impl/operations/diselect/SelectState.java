package com.volantis.xml.pipeline.sax.impl.operations.diselect;

/**
 * Specialization to keep track of the precept for the select and whether any
 * nested whens have matched.
 */
final class SelectState extends State {

    /**
     * Indicates the precept of the select rule.
     * true for matchevery, false for matchfirst.
     */
    private boolean matchevery;

    /**
     * Indicates whether there have been any matches so far
     */
    private boolean matchFound = false;

    /**
     * Initialise.
     *
     * @param containingState The containing state.
     * @param precept         The precept.
     */
    public SelectState(State containingState, Precept precept) {
        super(containingState);
        this.matchevery = precept == Precept.matchevery;
    }

    /**
     * Indicates that a match has been found
     */
    public void setMatchFound() {
        matchFound = true;
    }

    /**
     * Indicates whether when elements should be tested
     *
     * @return true if when elements should be tested
     */
    public boolean shouldTestMatches() {
        return matchevery || !matchFound;
    }

    /**
     * Indicates whether a match has been found
     *
     * @return true if a match has been found
     */
    public boolean matchFound() {
        return matchFound;
    }
}
