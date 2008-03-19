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

import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraints;
import com.volantis.mcs.runtime.configuration.project.ProjectsConfiguration;
import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.cache.group.Group;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface PopulatableProjectManager
        extends ProjectManager {

    /**
     * Set the default project.
     *
     * @param defaultProject The default project.
     */
    void setDefaultProject(RuntimeProject defaultProject);

    /**
     * Create the predefined projects.
     *
     @param projects The set of predefined projects.
     * @param configContext
     * @param defaultLocalCacheControlConstraintsMap
     * @param defaultLocalGroup

     */
    void createPredefinedProjects(
            ProjectsConfiguration projects, ConfigContext configContext,
            CacheControlConstraintsMap defaultLocalCacheControlConstraintsMap,
            Group defaultLocalGroup)
            throws ConfigurationException;
}
