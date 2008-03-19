/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.assets.implementation;

import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.runtime.policies.PolicyFetcherMock;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.RuntimePolicyReferenceMock;
import com.volantis.mcs.runtime.policies.composite.ActivatedRolloverImagePolicyMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This tests the {@link RolloverComponentReference} class.  It ensures that
 * it is created successfully and that the various operations that it supports
 * function as designed WRT lazy lookup and storing results once found.
 */
public class RolloverComponentReferenceTestCase extends TestCaseAbstract {

    /**
     * A name used in the tests.
     */
    private final static String TEST_NAME = "Rollover Reference";

    private AssetResolverMock assetResolverMock;
    private PolicyFetcherMock policyFetcherMock;
    private RuntimePolicyReferenceMock referenceMock;
    private ActivatedRolloverImagePolicyMock rolloverMock;


    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        assetResolverMock = new AssetResolverMock("assetResolverMock",
                expectations);

        policyFetcherMock = new PolicyFetcherMock("policyFetcherMock", expectations);

        referenceMock = new RuntimePolicyReferenceMock("referenceMock",
                expectations);

        rolloverMock = new ActivatedRolloverImagePolicyMock("rolloverMock",
                                expectations);
    }

    /**
     * This tests getting the over image asset reference from the component.
     */
    public void testGetOver() {
        // Create a test instance
        RolloverComponentReference test = createTestInstance(TEST_NAME);
        assertNotNull("Rollover reference should exist", test);

        ImageAssetReference ref = test.getOver();
        assertNotNull("Reference should exist", ref);
    }

    /**
     * This tests getting the references to the composite policies.
     */
    public void testGetCompositeReferences()
            throws Exception {

        policyFetcherMock.expects.fetchPolicy(referenceMock)
                .returns(rolloverMock).any();

        final RuntimePolicyReferenceMock overReferenceMock =
                new RuntimePolicyReferenceMock("overReferenceMock",
                        expectations);

        rolloverMock.expects.getOverPolicy().returns(overReferenceMock).any();

        final RuntimePolicyReferenceMock normalReferenceMock =
                new RuntimePolicyReferenceMock("normalReferenceMock",
                        expectations);

        rolloverMock.expects.getNormalPolicy().returns(normalReferenceMock).any();

        // Create a test instance
        RolloverComponentReference test = createTestInstance(TEST_NAME);
        assertNotNull("Rollover reference should exist", test);

        // Access the over image asset reference
        RuntimePolicyReference reference = test.getOverReference();
        assertNotNull("Reference should exist", reference);

        // Rerequest the reference and check no extra resolve calls have
        // been made
        reference = test.getNormalReference();
    }

    /**
     * This tests getting the normal image asset reference from the component.
     */
    public void testGetNormal() {
        // Create a test instance
        RolloverComponentReference test = createTestInstance(TEST_NAME);
        assertNotNull("Rollover reference should exist", test);

        ImageAssetReference ref = test.getNormal();
        assertNotNull("Reference should exist", ref);
    }

    /**
     * This tests getting the normal and over image component identities when
     * the rollover image policy cannot be found.
     */
    public void testMissingComponents() throws Exception {

        policyFetcherMock.expects.fetchPolicy(referenceMock)
                .returns(null).any();

        // Create a test instance
        RolloverComponentReference test = createTestInstance(TEST_NAME);
        assertNotNull("Rollover reference should exist", test);

        // Access the normal image asset reference
        RuntimePolicyReference id = test.getNormalReference();
        assertNull("Identity should not exist (normal)", id);

        // Access the over image asset reference
        id = test.getNormalReference();
        assertNull("Identity should not exist (over)", id);
    }

// Commented out until we resolve VBM:2004040703.
//    /**
//     * Testing the equality method.
//     */
//    public void testEquals() {
//        RolloverComponentReference testClassOne = createMutableStyleProperties(TEST_NAME);
//        RolloverComponentReference testClassTwo = createMutableStyleProperties(TEST_NAME);
//        RolloverComponentReference testClassThree =
//                createMutableStyleProperties(TEST_NAME);
//        RolloverComponentReference testClassFour =
//                createMutableStyleProperties("Different " + TEST_NAME);
//
//        // Reflexive
//        assertEquals("Classes should be the same",
//                testClassOne, testClassOne);
//
//        // Symmetric
//        assertEquals("Classes should be the same",
//                testClassOne, testClassTwo);
//        assertEquals("Classes should be the same",
//                testClassTwo, testClassOne);
//
//        // Transitive
//        assertEquals("Classes should be the same",
//                testClassTwo, testClassThree);
//        assertEquals("Classes should be the same",
//                testClassOne, testClassThree);
//
//        // Null
//        assertTrue("Null is not equal", !testClassOne.equals(null));
//
//        // Inequality
//        assertTrue("Classes should not be the same",
//                !testClassOne.equals(testClassFour));
//    }
//
//    /**
//     * Testing the hash code generation.
//     */
//    public void testHashCode() {
//        RolloverComponentReference testClassOne = createMutableStyleProperties(TEST_NAME);
//        RolloverComponentReference testClassTwo = createMutableStyleProperties(TEST_NAME);
//        RolloverComponentReference testClassThree =
//                createMutableStyleProperties(TEST_NAME);
//        RolloverComponentReference testClassFour =
//                createMutableStyleProperties("Different " + TEST_NAME);
//
//        // Reflexive
//        assertEquals("Hash codes should be the same",
//                testClassOne.hashCode(), testClassOne.hashCode());
//
//        // Symmetric
//        assertEquals("Hash codes should be the same",
//                testClassOne.hashCode(), testClassTwo.hashCode());
//        assertEquals("Hash codes should be the same",
//                testClassTwo.hashCode(), testClassOne.hashCode());
//
//        // Transitive
//        assertEquals("Hash codes should be the same",
//                testClassTwo.hashCode(), testClassThree.hashCode());
//        assertEquals("Hash codes should be the same",
//                testClassOne.hashCode(), testClassThree.hashCode());
//
//        // Inequality
//        assertTrue("Hash codes should not be the same",
//                testClassOne.hashCode() != testClassFour.hashCode());
//    }

    /**
     * Create a test instance of a rollover reference object for use in the
     * tests.
     *
     * @param name Tha name of the reference to use.
     * @return     The initialised object
     */
    public RolloverComponentReference createTestInstance(String name) {
        referenceMock.expects.getName().returns(name).any();
        return new RolloverComponentReference(policyFetcherMock,
                assetResolverMock, referenceMock);
    }

    /**
     * A utility method that can be called with any exception and it will then
     * print the stacktrace of that exception as part of a test failure message.
     *
     * @param e The exception to include in the fail message.
     */
    protected void failException(Exception e) {
        StringWriter stackTrace = new StringWriter();
        PrintWriter writer = new PrintWriter(stackTrace, true);
        e.printStackTrace(writer);
        writer.flush();
        writer.close();
        fail("Should not get an exception here\n" + stackTrace.toString());
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-May-04	4253/1	claire	VBM:2004051006 Fix invalid rollover component names NPE

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3753/4	claire	VBM:2004040612 Fixed supermerge, tabs, and JavaDoc

 07-Apr-04	3753/1	claire	VBM:2004040612 Increasing laziness of reference resolution

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 ===========================================================================
*/
