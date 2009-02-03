/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.method;

import com.volantis.testtools.mock.method.ExpectedCall;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodIdentifier;
import com.volantis.testtools.mock.method.ExpectedArguments;
import com.volantis.testtools.mock.impl.RepeatingExpectation;
import com.volantis.testtools.mock.impl.RepeatingExpectation;

public class ExpectedCallImpl
        extends RepeatingExpectation
        implements ExpectedCall {

    private final ExpectedMethodInvocationImpl invocation;

    public ExpectedCallImpl(Object source,
                            MethodIdentifier method,
                            ExpectedArguments arguments) {

        this.invocation = new ExpectedMethodInvocationImpl(
                source, method, arguments, this);
        addExpectation(invocation);
    }

    public void setAction(MethodAction action) {
        invocation.setAction(action);
    }

    public void setDescription(String description) {
        invocation.setDescription(description);
    }

    public void checkReturnType(Object returnValue) {
        invocation.checkReturnType(returnValue);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7995/3	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrencess of a method call

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrencess of a method call

 ===========================================================================
*/
