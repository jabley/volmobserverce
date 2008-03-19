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

import com.volantis.mcs.migrate.api.config.ConfigFactory;
import com.volantis.mcs.migrate.api.config.RemotePolicyMigrator;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.project.ProjectManagerMock;
import com.volantis.shared.net.http.HttpGetMethodMock;
import com.volantis.shared.net.http.HttpMethodFactoryMock;
import com.volantis.shared.net.http.HttpStatusCode;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class RemoteReaderTestAbstract
        extends TestCaseAbstract {
    protected RuntimeProjectMock runtimeProjectMock;
    protected HttpMethodFactoryMock methodFactoryMock;
    protected RuntimeProjectMock globalProjectMock;
    protected ProjectManagerMock projectManagerMock;
    protected HttpGetMethodMock methodMock;
    protected RemotePolicyBuilderParser parser;

    protected void setUp() throws Exception {
        super.setUp();

        runtimeProjectMock = new RuntimeProjectMock("runtimeProjectMock",
                expectations);

        methodFactoryMock = new HttpMethodFactoryMock("methodFactoryMock",
                expectations);

        projectManagerMock =
                new ProjectManagerMock("projectManagerMock", expectations);

        globalProjectMock =
                new RuntimeProjectMock("globalProjectMock", expectations);

        methodMock = new HttpGetMethodMock("methodMock", expectations);

        ConfigFactory migrationFactory = ConfigFactory.getDefaultInstance();
        RemotePolicyMigrator migrator =
                migrationFactory.createRemotePolicyMigrator();

        parser = new RemotePolicyBuilderParser(migrator);
    }

    /**
     * Ensure that if the get method fails that the reader behaves correctly.
     */
    public void testFailure()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final HttpGetMethodMock methodMock =
                new HttpGetMethodMock("methodMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        String url = "http://localhost:8090/europa/Remote/layouts/foo/bar/";
        methodFactoryMock.expects.createGetMethod(url)
                .returns(methodMock);

        methodMock.expects
                .addRequestHeader("Mariner-RequestType", getRequestType());

        methodMock.expects.execute().returns(HttpStatusCode.NOT_FOUND);

        methodMock.expects.releaseConnection();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        doFailureTest(url);
    }

    /**
     * Get the request type.
     *
     * @return The request type.
     */
    protected abstract String getRequestType();

    /**
     * Ensure that if the get method fails that the reader behaves correctly.
     */
    public void testProjectUpdate()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final HttpGetMethodMock methodMock =
                new HttpGetMethodMock("methodMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        String url = "http://localhost:8090/europa/Remote/layouts/foo/bar/";
        methodFactoryMock.expects.createGetMethod(url)
                .returns(methodMock);

        methodMock.expects
                .addRequestHeader("Mariner-RequestType", getRequestType());

        methodMock.expects.execute().returns(HttpStatusCode.NOT_FOUND);

        methodMock.expects.releaseConnection();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        doFailureTest(url);

    }

    protected abstract void doFailureTest(String url)
            throws RepositoryException;
}
