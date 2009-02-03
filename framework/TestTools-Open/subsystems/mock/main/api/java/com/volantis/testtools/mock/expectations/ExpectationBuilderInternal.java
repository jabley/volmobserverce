/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.expectations;

import com.volantis.testtools.mock.ExpectationBuilder;

/**
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface ExpectationBuilderInternal
        extends ExpectationBuilder {

    /**
     * If the current composite is not a set then push a new one.
     */
    void beginSet();

    /**
     * Pop the expectation set previously pushed by the matching call to
     * {@link #beginSet}.
     */
    void endSet();

    /**
     * If the current composite is not a sequence then push a new one.
     */
    void beginSequence();

    /**
     * Pop the expectation sequence previously pushed by the matching call to
     * {@link #beginSequence}.
     */
    void endSequence();

    void addSequence(Expectations expectations);

    void addSet(Expectations expectations);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 ===========================================================================
*/
