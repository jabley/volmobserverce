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

package com.volantis.mcs.runtime;

import com.volantis.mcs.repository.LocalRepository;

/**
 * Contains XML and JDBC repositories.
 */
public class RepositoryContainer {

    /**
     * The JDBC repository.
     */
    private final LocalRepository jdbcRepository;

    /**
     * The XML repository.
     */
    private final LocalRepository xmlRepository;

    /**
     * Initialise.
     *
     * @param jdbcRepository The JDBC repository.
     * @param xmlRepository  The XML repository.
     */
    public RepositoryContainer(
            LocalRepository jdbcRepository,
            LocalRepository xmlRepository) {

        this.jdbcRepository = jdbcRepository;
        this.xmlRepository = xmlRepository;
    }

    /**
     * Get the JDBC repository.
     *
     * @return The JDBC repository.
     */
    public LocalRepository getJDBCRepository() {
        return jdbcRepository;
    }

    /**
     * Get the XML repository.
     *
     * @return The XML repository.
     */
    public LocalRepository getXMLRepository() {
        return xmlRepository;
    }
}
