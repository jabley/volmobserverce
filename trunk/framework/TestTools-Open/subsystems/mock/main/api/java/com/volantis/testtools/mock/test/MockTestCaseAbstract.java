/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.test;

import junit.framework.TestCase;

import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.MockFactory;

import java.net.URL;

/**
 * A test case abstract that supports mock testing.
 */
public abstract class MockTestCaseAbstract
        extends TestCase {

    protected static MockFactory mockFactory = MockFactory.getDefaultInstance();

    /**
     * If true then no verification of the mock objects is done during the
     * post verification phase.
     */
    private boolean disablePostTestCaseVerification;

    /**
     * The unordered builder to use to set up the test expectations.
     */
    protected ExpectationBuilder expectations;

    /**
     * @deprecated Use default construct 
     */
    public MockTestCaseAbstract(String s) {
        super(s);
    }

    public MockTestCaseAbstract() {
    }

    protected void setUp() throws Exception {
        super.setUp();

        expectations = mockFactory.createUnorderedBuilder("Default MockTest Expectations");
    }

    /**
     * Disable post test case verification.
     *
     * <p>This should only be called by tests that are testing the behaviour
     * of the mock object framework.</p>
     */
    protected void disablePostTestCaseVerification() {
        disablePostTestCaseVerification = true;
    }

    /**
     * Override so that failures in the test will prevent the cleanup code
     * from making additional checks that are likely to fail and could obscure
     * the real problem.
     *
     * @throws Throwable
     */
    public void runBare() throws Throwable {

        // Only verify the mock objects if the tests complete.
        boolean verify = false;

        // Post test case verification is done by default but may be overridden
        // by other tests.
        disablePostTestCaseVerification = false;

        try {
            MockTestHelper.begin();

            super.runBare();

            // The test worked so we should verify unless that has been
            // explicitly disabled.
            verify = !disablePostTestCaseVerification;
        } finally {
            MockTestHelper.end(verify);
        }
    }

    /**
     * Get the URL to the class relative resource as a string.
     *
     * @param resource The resource which must be a class relative path.
     * @return The URL to the class relative resource as a string.
     */
    protected String getClassRelativeResourceURLAsString(
            final String resource) {
        return getClassRelativeResourceURL(resource).toExternalForm();
    }

    /**
     * Get the URL to the class relative resource.
     *
     * @param resource The resource which must be a class relative path.
     * @return The URL to the class relative resource.
     */
    protected URL getClassRelativeResourceURL(final String resource) {
        return getClass().getResource(resource);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 24-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 14-Jun-05	7997/5	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 06-Jun-05	8613/1	geoff	VBM:2005052404 Holding VBM for XDIME CP prior to 3.3.1 release

 20-May-05	8277/3	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-May-04	4164/1	pduffin	VBM:2004050404 Fixed some problems with shard link test cases and some mock test cases

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
