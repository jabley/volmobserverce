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
package com.volantis.mcs.repository;

import com.volantis.mcs.repository.jdbc.AlternateNames;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;

/**
 */
public interface LocalJDBCRepository extends LocalRepository {

    /**
     * Return the name appropriate for the repository from the set of
     * alternates.
     *
     * @param alternateNames The alternate names.
     * @return The repository specific name
     */
    String getAppropriateName(AlternateNames alternateNames);

    /**
     * Return an enumeration of the unique values in the column.
     */
    RepositoryEnumeration selectUniqueValues(
            JDBCRepositoryConnection connection,
            String columnName, String tableName,
            String projectName) throws RepositoryException;
}
