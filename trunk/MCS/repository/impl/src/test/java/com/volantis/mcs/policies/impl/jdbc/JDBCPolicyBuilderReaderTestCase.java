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

package com.volantis.mcs.policies.impl.jdbc;

import com.volantis.mcs.accessors.PolicyBuilderAccessorMock;
import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.policies.impl.PolicyBuilderReaderTestAbstract;
import com.volantis.mcs.project.jdbc.JDBCPolicySourceMock;
import com.volantis.mcs.repository.LocalRepositoryMock;
import com.volantis.mcs.repository.RepositoryConnectionMock;

/**
 * Test cases for {@link JDBCPolicyBuilderReader}.
 */
public class JDBCPolicyBuilderReaderTestCase
        extends PolicyBuilderReaderTestAbstract {

    private JDBCPolicySourceMock policySourceMock;
    private LocalRepositoryMock repositoryMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================
        policySourceMock = new JDBCPolicySourceMock("policySourceMock",
                expectations);

        repositoryMock = new LocalRepositoryMock("repositoryMock",
                expectations);

        final RepositoryConnectionMock connectionMock =
                new RepositoryConnectionMock("connectionMock",
                        expectations);

        projectMock.expects.getPolicySource().returns(policySourceMock).any();
        policySourceMock.expects.getRepository().returns(repositoryMock).any();

        repositoryMock.expects.connect().returns(connectionMock);
        connectionMock.expects.disconnect();
    }

    // Javadoc inherited.
    protected PolicyBuilderReader createReader(
            PolicyBuilderAccessorMock accessorMock) {
        return new JDBCPolicyBuilderReader(accessorMock);
    }
}
