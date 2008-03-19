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



public class DeprecatedConnection
        implements RepositoryConnection {

    private final DeprecatedRepository repository;
    private final RepositoryConnection connection;

    public DeprecatedConnection(
            DeprecatedRepository repository,
            RepositoryConnection connection) {
        this.repository = repository;
        this.connection = connection;
    }

    public boolean beginOperationSet()
            throws RepositoryException {
        return connection.beginOperationSet();
    }

    public boolean endOperationSet()
            throws RepositoryException {
        return connection.endOperationSet();
    }

    public boolean abortOperationSet()
            throws RepositoryException {
        return connection.abortOperationSet();
    }

    public boolean supportsOperationSets() {
        return connection.supportsOperationSets();
    }

    public void disconnect()
            throws RepositoryException {
        connection.disconnect();
    }

    public boolean isConnected()
            throws RepositoryException {
        return connection.isConnected();
    }

    public Repository getRepository() {
        return repository;
    }

    public RepositoryConnection getUnderLyingConnection()
            throws RepositoryException {
        return connection;
    }
}
