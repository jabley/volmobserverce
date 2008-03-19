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

import com.volantis.mcs.policies.PolicyBuildersResponse;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.remote.PolicyBuilders;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.shared.net.http.HttpGetMethod;
import com.volantis.shared.net.http.HttpMethodFactory;
import com.volantis.shared.net.http.HttpStatusCode;

import java.io.InputStream;

/**
 * Reads a {@link PolicyBuilders} from a remote source.
 */
public class RemotePolicyBuildersReader
        extends AbstractRemoteReader {

    /**
     * Initialise.
     *
     * @param methodFactory  Creates {@link HttpGetMethod} instances.
     * @param projectManager The project manager.
     * @param parser         The parser responsible for migrating and parsing
     *                       remote policies.
     */
    public RemotePolicyBuildersReader(
            HttpMethodFactory methodFactory, ProjectManager projectManager,
            RemotePolicyBuilderParser parser) {
        super(methodFactory, projectManager, parser);
    }

    /**
     * Get the {@link PolicyBuilders}.
     *
     * @param project The project to check.
     * @param name    The name of the resource containing the policies.
     * @return The response containing the {@link PolicyBuilders} and
     *         the owning project.
     * @throws RepositoryException If there was a problem accessing the remote
     *                             policy.
     */ 
    public PolicyBuildersResponse getPolicyBuilders(
            Project project, String name)
            throws RepositoryException {

        PolicyBuilders builders;

        HttpGetMethod method = methodFactory.createGetMethod(name);
        try {
            method.addRequestHeader(
                    REQUEST_TYPE_HEADER_NAME,
                    "policySet");

            // todo The policy may not exist but the project may in which case
            // todo this should load the project and then try and load the
            // todo policy from a fallback (if any).
            HttpStatusCode code = method.execute();
            if (code == HttpStatusCode.OK) {

                project = updateProject(project, method);

                InputStream in = method.getResponseBodyAsStream();

                builders = parser.parsePolicyBuilders(in, name);
            } else {
                builders = null;
            }
        } catch (Exception e) {
            // If we get an error, we log it but don't throw an exception.
            // We just carry on as if we hadn't found the asset.
            logger.error("remote-component-not-available",
                    new Object[]{name}, e);
            builders = null;
        } finally {
            method.releaseConnection();
        }

        return new PolicyBuildersResponse(project, builders);
    }

}
