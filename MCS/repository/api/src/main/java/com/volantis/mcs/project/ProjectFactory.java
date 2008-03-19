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

package com.volantis.mcs.project;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Factory for creating project related objects.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User specializations of this class are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 */
public abstract class ProjectFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.mcs.project.impl.ProjectFactoryImpl",
                        ProjectFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static ProjectFactory getDefaultInstance() {
        return (ProjectFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    protected ProjectFactory() {
    }

    /**
     * Create an instance to encapsulate the project configuration.
     *
     * @return A new {@link ProjectConfiguration} instance.
     */
    public abstract ProjectConfiguration createProjectConfiguration();

    /**
     * Create a {@link Project} from the specified configuration.
     *
     * @param configuration The configuration.
     * @return The newly instantiated {@link Project} instance.
     */
    public abstract Project createProject(ProjectConfiguration configuration);
}
