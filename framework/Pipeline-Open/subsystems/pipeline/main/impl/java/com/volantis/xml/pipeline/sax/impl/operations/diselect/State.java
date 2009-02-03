package com.volantis.xml.pipeline.sax.impl.operations.diselect;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;

/**
 * Tracks the state associated with the DISelect conditional processing.
 */
class State {

    /**
     * The default selid name.
     */
    private static final ExpandedName DEFAULT_SELIDNAME =
            new ImmutableExpandedName(
                    "http://www.w3.org/XML/1998/namespace", "id");

    /**
     * Indicates whether the body was processed or not.
     */
    private boolean processedBody;

    /**
     * The name of the attribute that should be created for the selid.
     */
    private ExpandedName selidName;

    /**
     * Initialise.
     *
     * @param containingState The containing state from which the current
     *                        selidname is inherited, or null in which case the
     *                        default is used.
     */
    public State(State containingState) {
        if (containingState == null) {
            setSelidName(DEFAULT_SELIDNAME);
        } else {
            setSelidName(containingState.selidName);
        }
    }

    /**
     * Determine whether the body has been processed.
     *
     * @return True if it has, false otherwise.
     */
    public boolean processedBody() {
        return processedBody;
    }

    /**
     * Indicates whether the body will be processed.
     *
     * @param processedBody True if it is false otherwise.
     */
    public void setProcessBody(boolean processedBody) {
        this.processedBody = processedBody;
    }

    /**
     * Get the name of the attribute used to replace the diselect:selid attribute.
     *
     * @return The name of the attribute.
     */
    public ExpandedName getSelidName() {
        return selidName;
    }

    /**
     * Set the        name of the attribute used to replace the diselect:selid attribute.
     *
     * @param selidName The name of the attribute.
     */
    public void setSelidName(ExpandedName selidName) {
        this.selidName = selidName;
    }

    /**
     * Indicates whether the state will allow the body to be processed.
     *
     * @return True if the state will allow the body to be processed, false otherwise.
     */
    public boolean canProcessBody() {
        return true;
    }
}
