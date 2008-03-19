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

package com.volantis.mcs.ibm.websphere.mcsi;

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.repository.LocalRepositoryMock;
import com.volantis.mcs.runtime.VolantisMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class PortletContextChildElementTestAbstract
        extends TestCaseAbstract {
    
    /**
     * The Policy Source
     */
    protected static String POLICY_SOURCE = "flobnobs";

    protected VolantisMock volantisMock;
    protected MarinerPageContextMock pageContextMock;
    protected MarinerRequestContextMock requestContextMock;
    private LocalRepositoryMock jdbcRepositoryMock;
    protected PortletContextElement parent;

    protected void setUp() throws Exception {
        super.setUp();

        // Create the JdbcPoliciesElement and test the elementStart method.
        parent = new PortletContextElement();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        volantisMock = new VolantisMock("volantisMock", expectations);

        pageContextMock = new MarinerPageContextMock("pageContextMock",
                expectations);

        requestContextMock = new MarinerRequestContextMock("requestContextMock",
                expectations);

        jdbcRepositoryMock = new LocalRepositoryMock(
                "jdbcRepositoryMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        requestContextMock.expects.getMarinerPageContext()
                .returns(pageContextMock).any();
        pageContextMock.expects.getVolantisBean().returns(volantisMock).any();
        volantisMock.expects.getJDBCRepository()
                .returns(jdbcRepositoryMock).any();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Pretend the parent element has been pushed onto the stack.
        pageContextMock.expects.peekMCSIElement().returns(parent).any();
    }
}
