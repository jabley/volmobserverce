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

import com.volantis.mcs.repository.RepositoryException;

/**
 * Encapsulates a number of operations on the {@link PolicyBuilderManager} that
 * should be performed together.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.6
 */
public interface BuilderBatchOperation {

    /**
     * Perform the batch operation using the specified manager.
     *
     * @param manager The manager to use.
     * @return True if the batch operation was a success and the operations
     *         should be persisted and false if it was not.
     */
    boolean perform(PolicyBuilderManager manager)
            throws RepositoryException;
}
