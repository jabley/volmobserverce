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

package com.volantis.mcs.repository;

import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.Project;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 */
public abstract class DeprecatedRepository
        implements Repository {

    private final LocalRepository repository;
    private final Project defaultProject;

    public DeprecatedRepository(LocalRepository repository,
                                Project defaultProject) {
        this.repository = repository;
        this.defaultProject = defaultProject;
    }

    /**
     * Get the underlying local repository.
     *
     * @return The underlying local repository.
     */
    public LocalRepository getLocalRepository() {
        return repository;
    }

    public Project getDefaultProject() {
        return defaultProject;
    }

    public RepositoryConnection connect() throws RepositoryException {
        return new DeprecatedConnection(this, repository.connect());
    }

    public void disconnect(RepositoryConnection connection)
            throws RepositoryException {
        repository.disconnect(connection.getUnderLyingConnection());
    }

    public void terminate() throws RepositoryException {
        repository.terminate();
    }
}
