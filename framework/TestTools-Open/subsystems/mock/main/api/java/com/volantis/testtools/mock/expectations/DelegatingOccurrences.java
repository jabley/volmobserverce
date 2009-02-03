/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2007. All rights reserved
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.mock.expectations;

import com.volantis.testtools.mock.method.MaxOcurrences;
import com.volantis.testtools.mock.method.Occurrences;

/**
 * This class delegates all calls to the specified Occurrences instance
 */
public class DelegatingOccurrences implements Occurrences {

    /**
     * The class to delegate to
     */
    private Occurrences delegatee;

    /**
     * Specify the Occurrences instance to delegate to.
     *
     * @param delegatee the instance to delegate to
     */
    public DelegatingOccurrences(Occurrences delegatee) {
        this.delegatee = delegatee;
    }

    // javadoc not necessary
    public MaxOcurrences min(int minimum) {
        return delegatee.min(minimum);
    }

    // javadoc not necessary
    public void optional() {
        delegatee.optional();
    }

    // javadoc not necessary
    public void fixed(int number) {
        delegatee.fixed(number);
    }

    // javadoc not necessary
    public void any() {
        delegatee.any();
    }

    // javadoc not necessary
    public void atLeast(int minimum) {
        delegatee.atLeast(minimum);
    }

    // javadoc not necessary
    public void max(int maximum) {
        delegatee.max(maximum);
    }

    // javadoc not necessary
    public void unbounded() {
        delegatee.unbounded();
    }

}
