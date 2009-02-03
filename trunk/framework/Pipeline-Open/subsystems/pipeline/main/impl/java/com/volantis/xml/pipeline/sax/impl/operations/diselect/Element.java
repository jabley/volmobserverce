package com.volantis.xml.pipeline.sax.impl.operations.diselect;

import org.xml.sax.Attributes;

/**
 * Enumeration of the different elements that are supported by the DISelect
 * conditional processing.
 */
enum Element {

    /**
     * Represents an element in a namespace other than the diselect one.
     * <p>Supports 'diselect:expr', 'diselect:selidname', 'diselect:selid'
     * attributes and unless the expr is specified and evaluates to false the
     * element is forwarded for further processing.</p>
     */
    OTHER(Constants.DISELECT_NAMESPACE, Constants.DISELECT_NAMESPACE,
            true, true),

    /**
     * Represents the diselect:if element.
     * <p>Supports the 'expr' and 'selidname' attributes and is consumed by the
     * DISelect conditional processing.</p>
     */
    IF("", "", false, false),

    /**
     * Represents the diselect:select element.
     * <p>Supports the 'expr' and 'selidname' attributes and is consumed by the
     * DISelect conditional processing.</p>
     * <p>This has its own state specialization to keep track of the results of
     * processing the nested when and otherwise elements.</p>
     */
    SELECT("", "", false, false) {
        /**
         * Override to create specialization.
         * @param containingState
         * @param attributes
         * @return
         */
        public State createState(State containingState, Attributes attributes) {
            String value = attributes.getValue("", "precept");
            Precept precept = Precept.matchfirst;
            if (value != null) {
                precept = Precept.valueOf(value);
            }
            return new SelectState(containingState, precept);
        }
    },

    /**
     * Represents the diselect:when element.
     * <p>Supports the 'expr' and 'selidname' attributes and is consumed by the
     * DISelect conditional processing.</p>
     * <p>This has its own state specialization that interacts with the
     * containing select state to keep track of the results of processing the
     * nested when elements.</p>
     */
    WHEN("", "", false, false) {
        public State createState(State containingState, Attributes attributes) {
            if (containingState instanceof SelectState) {
                SelectState selectState = (SelectState) containingState;
                return new WhenState(selectState);
            } else {
                throw new IllegalStateException(
                        "DISelect element <when> must be a direct " +
                                "child of DISelect element <select>");
            }
        }
    },

    /**
     * Represents the diselect:otherwise element.
     * <p>Supports the 'selidname' attribute and is consumed by the DISelect
     * conditional processing.</p>
     * <p>This has its own state specialization that interacts with the
     * containing select state to determine whether it should be executed.</p>
     */
    OTHERWISE(null, "", false, false) {
        public State createState(State containingState, Attributes attributes) {
            if (containingState instanceof SelectState) {
                SelectState selectState = (SelectState) containingState;
                return new OtherwiseState(selectState);
            } else {
                throw new IllegalStateException(
                        "DISelect element <otherwise> must be a direct " +
                                "child of DISelect element <select>");
            }
        }
    };

    /**
     * The namespace for the expr attribute, either the diselect namespace if
     * the attribute is the diselect global one, an empty string if it is
     * element specific, or null if it is not supported.
     */
    private String exprAttributeURI;

    /**
     * The namespace for the selidname attribute, either the diselect namespace
     * if the attribute is the diselect global one, an empty string if it is
     * element specific, or null if it is not supported.
     */
    private String selidNameAttributeURI;

    /**
     * Indicates whether the element supports the global diselect selid
     * attribute.
     */
    private boolean canHaveSelidAttribute;

    /**
     * Indicates whether the event for which this element is used should be
     * forwarded to the next process or consumed by the DISelect conditional
     * processing.
     */
    private boolean forwardEvent;

    /**
     * Initialise.
     *
     * @param exprAttributeURI      The namespace for the expr attribute.
     * @param selidNameAttributeURI The namespace for the selidname attribute.
     * @param canHaveSelidAttribute Indicates whether the element supports the
     *                              global diselect selid attribute.
     * @param forwardEvent          Indicates whether the event for which this
     *                              element is used should be forwarded to the
     *                              next process or consumed by the DISelect
     *                              conditional processing.
     */
    Element(String exprAttributeURI, String selidNameAttributeURI,
            boolean canHaveSelidAttribute, boolean forwardEvent) {
        this.exprAttributeURI = exprAttributeURI;
        this.selidNameAttributeURI = selidNameAttributeURI;
        this.canHaveSelidAttribute = canHaveSelidAttribute;
        this.forwardEvent = forwardEvent;
    }

    public String getExprAttributeURI() {
        return exprAttributeURI;
    }

    public String getSelidNameAttributeURI() {
        return selidNameAttributeURI;
    }

    public boolean canHaveSelidAttribute() {
        return canHaveSelidAttribute;
    }

    public boolean getForwardEvent() {
        return forwardEvent;
    }

    /**
     * Create the instance needed to keep track of the state of processing this
     * element.
     *
     * @param containingState The containing state, may be null.
     * @param attributes      The attributes.
     * @return The new state.
     */
    public State createState(State containingState, Attributes attributes) {
        return new State(containingState);
    }
}
