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

package com.volantis.mcs.runtime.project;

import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.mcs.runtime.configuration.project.ProjectsConfiguration;
import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.cache.group.Group;

import java.util.Map;

/**
 * Responsible for building a project.
 *
 * <p>This has been extracted from the ProjectManager to enable it to be more
 * easily unit tested.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface RuntimeProjectConfigurator {

    /**
     * Create the global project.
     *
     * @param policySourceFactory The reader used by the policy source of the global
     *                     project.
     * @return The global project.
     */
    RuntimeProject createGlobalProject(RuntimePolicySourceFactory policySourceFactory);

    /**
     * Build the project from the configuration.
     *
     * @param configuration       The configuration from which the project
     *                            should be built.
     * @param baseProject         The base project, may be null.
     * @param policySourceFactory The factory for creating policy sources.
     * @return The built project.
     */
    RuntimeProject buildProject(
            RuntimeProjectConfiguration configuration,
            RuntimeProject baseProject,
            RuntimePolicySourceFactory policySourceFactory);

    PredefinedProjects createPredefinedProjects(
            ProjectsConfiguration projects, ConfigContext configContext,
            RuntimePolicySourceFactory policySourceFactory)
            throws ConfigurationException;
}
