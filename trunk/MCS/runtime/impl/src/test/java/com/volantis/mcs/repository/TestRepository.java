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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.repository;

import com.volantis.mcs.repository.Repository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;

/**
 * A test implementation of {@link com.volantis.mcs.repository.Repository}
 * which does nothing.
 */
public class TestRepository implements Repository {

    // JavaDoc inherited
    public RepositoryConnection connect()
            throws RepositoryException {
        return null;
    }

    // JavaDoc inherited
    public void disconnect(RepositoryConnection connection)
            throws RepositoryException {
    }

    // JavaDoc inherited
    public void terminate()
            throws RepositoryException {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 ===========================================================================
*/
