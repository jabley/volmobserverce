/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

import com.volantis.testtools.mock.generator.model.MockableMethod;

/**
 * Describes how to mock methods that are inherited from Object.
 */
public interface ObjectMethodAction {

    MockableMethod createMockableMethod(MockableMethod method);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/4	pduffin	VBM:2005071103 Updated based on review comments

 11-Jul-05	8996/2	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 ===========================================================================
*/
