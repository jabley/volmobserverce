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

package com.volantis.mcs.policies.impl;

import com.volantis.mcs.accessors.PolicyBuilderAccessorMock;
import com.volantis.mcs.policies.PolicyBuilderMock;
import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.policies.PolicyBuilderResponse;
import com.volantis.mcs.project.InternalProjectMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base for all classes that test local {@link PolicyBuilderReader}s.
 */
public abstract class PolicyBuilderReaderTestAbstract
        extends TestCaseAbstract {

    protected InternalProjectMock projectMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        projectMock = new InternalProjectMock("projectMock", expectations);

    }

    /**
     * Ensure that the returned {@link PolicyBuilderResponse} has the same
     * project that was passed in and the same builder that was returned from
     * the accessor.
     */
    public void testResponse() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final PolicyBuilderAccessorMock accessorMock =
                new PolicyBuilderAccessorMock("accessorMock",
                        expectations);

        final PolicyBuilderMock builderMock =
                new PolicyBuilderMock("builderMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        String policyName = "/foo.mimg";
        accessorMock.fuzzy
                .retrievePolicyBuilder(mockFactory.expectsAny(), projectMock,
                        policyName)
                .returns(builderMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        PolicyBuilderReader reader = createReader(accessorMock);
        PolicyBuilderResponse response = reader.getPolicyBuilder(projectMock,
                policyName);
        assertSame(projectMock, response.getProject());
        assertSame(builderMock, response.getBuilder());
    }

    /**
     * Create the {@link PolicyBuilderReader} wrapper.
     *
     * @param accessorMock The accessor to wrap.
     * @return The wrapping {@link PolicyBuilderReader}.
     */
    protected abstract PolicyBuilderReader createReader(
            PolicyBuilderAccessorMock accessorMock);
}
