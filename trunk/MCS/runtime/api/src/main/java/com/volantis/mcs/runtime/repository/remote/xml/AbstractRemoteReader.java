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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.mcs.runtime.project.RemoteProjectLoader;
import com.volantis.shared.net.http.HttpGetMethod;
import com.volantis.shared.net.http.HttpMethodFactory;
import com.volantis.synergetics.log.LogDispatcher;
import our.apache.commons.httpclient.Header;

/**
 * Base for those classes responsible for reading either a single, or multiple
 * policies from a remote source.
 */
public class AbstractRemoteReader {

    /**
     * Header sent to remote site indicating the request type.
     */
    protected static final String REQUEST_TYPE_HEADER_NAME =
            "Mariner-RequestType";

    /**
     * Used for logging.
     */
     protected static final LogDispatcher logger =
             LocalizationFactory.createLogger(RemotePolicyBuilderReader.class);

    /**
     * Factory to use to create {@link HttpGetMethod}.
     */
    protected final HttpMethodFactory methodFactory;

    /**
     * The prooject manager.
     */
    private final ProjectManager projectManager;

    /**
     * The policy parser.
     */
    protected final RemotePolicyBuilderParser parser;

    /**
     * Initialise.
     *
     * @param methodFactory  Creates {@link HttpGetMethod} instances.
     * @param projectManager The project manager.
     * @param parser         The parser responsible for migrating and parsing
     *                       remote policies.
     */
    public AbstractRemoteReader(
            HttpMethodFactory methodFactory, ProjectManager projectManager,
            RemotePolicyBuilderParser parser) {
        this.methodFactory = methodFactory;
        this.projectManager = projectManager;
        this.parser = parser;
    }

    /**
     * Get the project from the HTTP method.
     *
     * @param method The HTTP method that has been executed and which may
     *               contain information about the project.
     * @return The project to use.
     */
    protected RuntimeProject getProject(HttpGetMethod method) {

        // Look at the special header to see whether the remote server has
        // identified the project that contains the policy. If it has not then
        // assume that the remote server
        RuntimeProject project;
        Header header = method.getResponseHeader(
                RemoteProjectLoader.MCS_PROJECT_HEADER);
        if (header == null) {
            project = projectManager.getGlobalProject();
        } else {
            String projectRoot = header.getValue();
            project = projectManager.getProject(projectRoot, null);
        }
        return project;
    }

    /**
     * Check to see whether the response indicated that the project for the
     * policy is different to the one supplied.
     *
     * <p>This only has an effect if the supplied project is the global
     * one.</p>
     *
     * @param project The assumed project.
     * @param method  The response from the http request.
     * @return The updated project.
     */
    protected Project updateProject(Project project, HttpGetMethod method) {
        // If the project is the global project then it
        // maybe that the policy is actually in a different project
        // but the request was deferred until now in order to allow
        // the project identification to be combined with retrieval
        // of the policy in order to save time on the server.
        RuntimeProject global = projectManager.getGlobalProject();
        if (project == global) {
            project = getProject(method);
        }
        return project;
    }
}
