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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.repository;



/**
 * A local repository connection (either XML or JDBC).
 *
 * Adds support for default project to the abstract repository connection.
 *
 * @mock.generate base="RepositoryConnection"
 */
public interface LocalRepositoryConnection
        extends RepositoryConnection {

    /**
     * Return the repository for this connection.
     *
     * @return the repository for this connection.
     */
    LocalRepository getLocalRepository();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 ===========================================================================
*/
