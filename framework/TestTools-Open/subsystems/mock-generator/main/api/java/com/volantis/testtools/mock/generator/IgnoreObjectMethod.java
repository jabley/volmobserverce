/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

import com.volantis.testtools.mock.generator.model.MockableMethod;

/**
 * Prevents anything from being generated for those object methods with which
 * it is associated.
 *
 * <p>This should be used for those {@link Object} methods that cannot be
 * sensibly mocked up.</p>
 */
public class IgnoreObjectMethod
        implements ObjectMethodAction {

    public MockableMethod createMockableMethod(MockableMethod method) {
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8996/2	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 ===========================================================================
*/
