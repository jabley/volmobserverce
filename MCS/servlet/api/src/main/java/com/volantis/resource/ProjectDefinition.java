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

package com.volantis.resource;

import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;

/**
 * Contains the definition of the project.
 *
 * todo there is code in here that is common with the code in RuntimeProject.
 */
public class ProjectDefinition {

    /**
     * The configuration used locally within the resource server.
     */
    private final RuntimeProjectConfiguration local;

    /**
     * The configuration sent to the client stored as a string.
     */
    private final String clientConfigurationAsXML;

    /**
     * The external URL as a string.
     */
    private final String externalProjectRoot;

    /**
     * The accessor used to access the policies in the project.
     */
    private final ResourceAccessor policyResourceAccessor;

    /**
     * The context relative path to the root of the project.
     */
    private final String contextRelativeProjectRoot;

    /**
     * The index of the start of that part of a context relative path that is
     * relative to the project.
     */
    private final int projectRelativeStart;

    /**
     * Initialise.
     *
     * @param local                  The configuration used locally on the resource
     *                               server.
     * @param clientAsString         The configuration sent to the client stored as a
     * @param externalProjectRoot    The project root sent to the client.
     * @param policyResourceAccessor
     */
    public ProjectDefinition(
            RuntimeProjectConfiguration local,
            String clientAsString,
            String externalProjectRoot,
            ResourceAccessor policyResourceAccessor,
            String contextRelativeProjectRoot) {

        this.local = local;
        this.clientConfigurationAsXML = clientAsString;
        this.externalProjectRoot = externalProjectRoot;
        this.policyResourceAccessor = policyResourceAccessor;

        this.contextRelativeProjectRoot = contextRelativeProjectRoot;
        if (!contextRelativeProjectRoot.endsWith("/")) {
            throw new IllegalStateException("Context relative project root '" +
                    contextRelativeProjectRoot + "' must end with a /");
        }
        projectRelativeStart = contextRelativeProjectRoot.length();
    }

    /**
     * Get the configuration used locally on the resource server.
     *
     * @return The configuration used locally on the resource server.
     */
    public RuntimeProjectConfiguration getLocal() {
        return local;
    }

    /**
     * Get the configuration sent to the client as a string.
     *
     * @return The configuration sent to the client as a string.
     */
    public String getClientConfigurationAsXML() {
        return clientConfigurationAsXML;
    }

    /**
     * Get the absolute URL to the project root.
     *
     * @return The absolute URL to the project root.
     */
    public String getExternalProjectRoot() {
        return externalProjectRoot;
    }

    /**
     * Get the accessor for resources in the project.
     *
     * @return The accessor, may be null.
     */
    public ResourceAccessor getPolicyResourceAccessor() {
        return policyResourceAccessor;
    }

    /**
     * Make a project relative path from the context relative one.
     *
     * @param contextRelativePath The context relative path that must be in
     *                            the project.
     * @return The project relative path.
     * @throws IllegalStateException If the path does not belong to the
     * project.
     */
    public String getProjectRelativePath(String contextRelativePath) {
        if (contextRelativePath.startsWith(contextRelativeProjectRoot)) {
            return contextRelativePath.substring(projectRelativeStart);
        } else {
            throw new IllegalStateException("Path '" + contextRelativePath +
                    "' is not relative to '" + contextRelativeProjectRoot +
                    "'");
        }
    }

    /**
     * Check to see whether the path belongs in the project.
     *
     * @param contextRelativePath A path to the resource that is relative to
     *                            the context.
     * @return True if the path belongs in the project and false otherwise.
     */
    public boolean containsPolicy(String contextRelativePath) {
        return (contextRelativePath.startsWith(contextRelativeProjectRoot));
    }
}
