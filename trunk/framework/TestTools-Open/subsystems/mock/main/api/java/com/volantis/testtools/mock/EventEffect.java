/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock;

/**
 * An enumeration of the possible effects that an event will have if processed
 * by an expectation.
 *
 * <dl>
 * <dt>{@link #WOULD_FAIL}</dt>
 * <dd>The event would cause the expectation to fail.</dd>
 * <dt>{@link #WOULD_SATISFY}</dt>
 * <dd>The event did not match any expectations but would satisfy the
 * expectation. This would only happen if for example an expectation was
 * optional. In that case any event other than one that matched would satisfy
 * the expectation.</dd>
 * <dt>{@link #MATCHED_EXPECTATION}</dt>
 * <dd>The event matched an expectation.</dd>
 * </dl>
 */
public final class EventEffect {

    public static final EventEffect WOULD_FAIL = new EventEffect("WOULD_FAIL");
    public static final EventEffect WOULD_SATISFY = new EventEffect("WOULD_SATISFY");
    public static final EventEffect MATCHED_EXPECTATION = new EventEffect("MATCHED_EXPECTATION");

    private final String name; // for debug only

    private EventEffect(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
