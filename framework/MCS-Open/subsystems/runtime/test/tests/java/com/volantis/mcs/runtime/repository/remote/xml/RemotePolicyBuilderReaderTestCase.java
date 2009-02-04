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


package com.volantis.mcs.runtime.repository.remote.xml;

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilderResponse;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.shared.net.http.HttpStatusCode;

import java.io.InputStream;


public class RemotePolicyBuilderReaderTestCase
        extends RemoteReaderTestAbstract {
    private String absoluteURL = "http://localhost:8090/europa/Remote/layouts/foo/bar/";

    protected void setUp() throws Exception {
        super.setUp();

        runtimeProjectMock.expects.isRemote().returns(true).any();
    }

    /**
     * Ensure that getting a single response from the global project works ok.
     */
    public void testSingleResponseFromGlobalProject() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        addHttpMethodExecutionExpectations(absoluteURL);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        RemotePolicyBuilderReader reader = createReader();
        PolicyBuilderResponse response = (PolicyBuilderResponse)
                reader.getPolicyBuilder(runtimeProjectMock, absoluteURL);
        assertNotNull(response.getBuilder());
        assertSame(runtimeProjectMock, response.getProject());
    }

    /**
     * Ensure that getting a single response from the global project works ok.
     */
    public void testSingleResponseFromRemoteProject() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        String url = "/layouts/foo/bar/";

        addHttpMethodExecutionExpectations(absoluteURL);

        runtimeProjectMock.expects.makeAbsolutePolicyURL(url)
                .returns(absoluteURL).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        RemotePolicyBuilderReader reader = createReader();
        PolicyBuilderResponse response = (PolicyBuilderResponse)
                reader.getPolicyBuilder(runtimeProjectMock, url);
        assertNotNull(response.getBuilder());
        assertSame(runtimeProjectMock, response.getProject());
    }

    private void addHttpMethodExecutionExpectations(String url) {
        methodFactoryMock.expects.createGetMethod(url)
                .returns(methodMock);

        methodMock.expects
                .addRequestHeader("Mariner-RequestType", getRequestType());

        methodMock.expects.execute().returns(HttpStatusCode.OK);

        projectManagerMock.expects.getGlobalProject()
                .returns(globalProjectMock);

        InputStream stream = getClass().getResourceAsStream("single-response.xml");
        methodMock.expects.getResponseBodyAsStream().returns(stream);

        methodMock.expects.releaseConnection();
    }

    private RemotePolicyBuilderReader createReader() {
        return new RemotePolicyBuilderReader(methodFactoryMock,
                projectManagerMock, parser);
    }

    protected void doFailureTest(String url)
            throws RepositoryException {

        RemotePolicyBuilderReader reader = createReader();
        PolicyBuilderResponse response = reader.getPolicyBuilder(
                runtimeProjectMock, url);
        PolicyBuilder builder = response.getBuilder();
        assertNull(builder);
    }

    protected String getRequestType() {
        return "singlePolicy";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/2	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 13-Oct-05	9727/4	ianw	VBM:2005100506 Fixed remote repository issues

 10-Oct-05	9727/1	ianw	VBM:2005100506 Fixed up remote repositories layout issues

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 03-Oct-05	9500/1	ianw	VBM:2005091308 Rationalise RPDM and LPDM

 ===========================================================================
*/
