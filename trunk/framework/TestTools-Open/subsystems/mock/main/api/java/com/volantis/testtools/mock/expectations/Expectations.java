/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.expectations;

import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.method.Occurrences;

public abstract class Expectations
        extends DelegatingOccurrences {

    protected ExpectationBuilderInternal expectations;

    protected Expectations() {
        super(MockFactory.getDefaultInstance().createOccurrences());
    }

    public Occurrences add(Expectations nested) {
        return expectations.add(nested);
    }

    public abstract void add();

    public abstract void addTo(ExpectationBuilderInternal expectations);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 14-Jun-05	7997/5	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 ===========================================================================
*/
