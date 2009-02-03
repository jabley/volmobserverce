/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

import com.volantis.testtools.mock.generator.model.MockableMethod;

/**
 * A simple object method that is not used by the framework and can therefore
 * simply behave just like any other method.
 */
public class SimpleObjectMethod
        implements ObjectMethodAction {
    
    private final String uniqueIdentifier;
    private final String returnType;
    private final String fixedBody;

    public SimpleObjectMethod(
            String uniqueIdentifier, String returnType, String fixedBody) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.returnType = returnType;
        this.fixedBody = fixedBody;
    }

    public MockableMethod createMockableMethod(MockableMethod method) {
        return new MockableMethod(method.getName(),
                method.getParameters(), method.getBindSignature(),
                method.getThrowables(), method.getReturnType(),
                GenerationHelper.getBuiltInCallUpdaterInfo(returnType),
                method.isUnique(), uniqueIdentifier, false, null, false, fixedBody,
                "com.volantis.testtools.mock.generated.MockObjectBase");
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
