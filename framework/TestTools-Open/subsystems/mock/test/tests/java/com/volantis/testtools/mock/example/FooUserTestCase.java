/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.example;

import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import junit.framework.AssertionFailedError;

/**
 * An example test case that shows how to test using mock objects.
 */
public class FooUserTestCase
        extends MockTestCaseAbstract {

    /**
     * Check that foo is being used properly.
     * @param user
     */
    protected void checkFooUsage(FooUser user) {

        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();

        // Create the mock object.
        FooMock foo = new FooMock(expectations);

        // Set the expectation.
        foo.expects.setBar("good value");

        // Perform the operation.
        user.useFoo(foo);

        // Set a repeated expectation.
        foo.expects.setBar("good value").min(2).max(4);

        user.useFoo(foo);
        user.useFoo(foo);
        user.useFoo(foo);
    }

    /**
     * Test that the good user works.
     */
    public void testGoodFooUsage() {

        GoodFooUser user = new GoodFooUser();

        checkFooUsage(user);
    }

    /**
     * Test that the bad user fails.
     *
     * <p>Typically this test would not catch the error thrown but would
     * instead cause the build to fail and the problem fixed, however this is
     * just supposed to be an example.</p>
     */
    public void testBadFooUsage() {

        BadFooUser user = new BadFooUser();
        boolean detectedError;

        try {
            checkFooUsage(user);

            // Expectation error was not detected.
            detectedError = false;
        } catch (AssertionFailedError e) {
            // The test worked as it should.
            detectedError = true;

            // Disable post test case verification, otherwise it will fail as
            // there is an expectation that has not yet been satisfied.
            disablePostTestCaseVerification();
        }

        // Fail the test if the error was not detected. Do this outside the
        // try...catch as that would catch the expection that the fail method
        // throws.
        if (!detectedError) {
            fail("Test failed to detect an error");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 10-May-04	4164/3	pduffin	VBM:2004050404 Fixed some problems with shard link test cases and some mock test cases

 10-May-04	4164/1	pduffin	VBM:2004050404 Fixed problems with test cases, specifically those caused by ConcreteMenuBuffer throwing an UnsupportedOperationException

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
