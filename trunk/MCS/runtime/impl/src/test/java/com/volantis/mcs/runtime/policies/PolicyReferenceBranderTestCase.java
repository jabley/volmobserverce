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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.context.BrandNameProviderMock;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link PolicyReferenceBrander}.
 */
public class PolicyReferenceBranderTestCase
        extends TestCaseAbstract {

    private PolicyReferenceFactoryMock referenceFactoryMock;
    private RuntimePolicyReferenceMock referenceMock;
    private RuntimeProjectMock projectMock;
    private RuntimePolicyReferenceMock otherReferenceMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        referenceFactoryMock = new PolicyReferenceFactoryMock(
                "referenceFactoryMock", expectations);

        referenceMock = new RuntimePolicyReferenceMock(
                "referenceMock", expectations);

        projectMock = new RuntimeProjectMock("projectMock", expectations);

        otherReferenceMock = new RuntimePolicyReferenceMock(
                "otherReferenceMock", expectations);
    }

    /**
     * Make sure that if no brand name has been specified that the supplied
     * reference is given back.
     */
    public void testNoBrandName() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        PolicyReferenceBrander brander = new PolicyReferenceBrander(
                referenceFactoryMock);

        RuntimePolicyReference reference =
                brander.getBrandedReference(referenceMock, null);

        // Expect the same reference back if not brand name has been provided.
        assertSame(referenceMock, reference);
    }

    /**
     * Make sure that if the reference is unbrandable that the supplied
     * reference is given back.
     */
    public void testUnbrandable() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The reference is not brandable.
        referenceMock.expects.isBrandable().returns(false).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        PolicyReferenceBrander brander = new PolicyReferenceBrander(
                referenceFactoryMock);

        RuntimePolicyReference reference =
                brander.getBrandedReference(referenceMock, "/brand/");

        // Expect the same reference back as it is not brandable..
        assertSame(referenceMock, reference);
    }

    /**
     * Make sure that if the brand name is host relative that the returned
     * reference has the correct brand name and is in the same project as the
     * supplied reference.
     */
    public void testProjectRelativeBrand() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The reference is brandable.
        referenceMock.expects.isBrandable().returns(true).any();
        referenceMock.expects.getName().returns("/fred/flintstone.mimg").any();
        referenceMock.expects.getProject().returns(projectMock).any();
        referenceMock.expects.getExpectedPolicyType()
                .returns(PolicyType.IMAGE).any();

        referenceFactoryMock.expects.createNormalizedReference(projectMock,
                "/brand/fred/flintstone.mimg", PolicyType.IMAGE)
                .returns(otherReferenceMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        PolicyReferenceBrander brander = new PolicyReferenceBrander(
                referenceFactoryMock);

        RuntimePolicyReference reference =
                brander.getBrandedReference(referenceMock, "/brand/");

        assertSame(otherReferenceMock, reference);
    }

    /**
     * Make sure that if the brand name is absolute that the reference is
     * renormalized.
     */
    public void testAbsoluteBrand() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The reference is brandable.
        referenceMock.expects.isBrandable().returns(true).any();
        referenceMock.expects.getName().returns("/fred/flintstone.mimg").any();
        referenceMock.expects.getProject().returns(projectMock).any();
        referenceMock.expects.getExpectedPolicyType()
                .returns(PolicyType.IMAGE).any();

        referenceFactoryMock.expects.createLazyNormalizedReference(projectMock,
                new MarinerURL(), "http://brand.com/fred/flintstone.mimg",
                PolicyType.IMAGE)
                .returns(otherReferenceMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        PolicyReferenceBrander brander = new PolicyReferenceBrander(
                referenceFactoryMock);

        RuntimePolicyReference reference =
                brander.getBrandedReference(referenceMock, "http://brand.com/");

        assertSame(otherReferenceMock, reference);
    }
}
