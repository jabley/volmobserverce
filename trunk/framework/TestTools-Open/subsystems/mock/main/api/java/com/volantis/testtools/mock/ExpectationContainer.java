/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock;

import com.volantis.testtools.mock.method.MethodCall;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface ExpectationContainer
        extends Verifiable {

    void add(Expectation expectation);

    Object doMethodCall(MethodCall methodCall)
            throws Throwable;

    /**
     * Dump details about the remaining expectations to the specified writer.
     *
     * @param writer The writer to which the details will be written.
     *
     * @throws IOException If there was a problem writing.
     */
    void dump(Writer writer)
            throws IOException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 ===========================================================================
*/
