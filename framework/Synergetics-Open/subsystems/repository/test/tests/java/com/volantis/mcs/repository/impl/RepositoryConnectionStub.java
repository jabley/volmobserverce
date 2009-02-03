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
 * $Header$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-May-03    Allan           VBM:2003051303 - Created. A stub for 
 *                              RepositoryConnection. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.repository.impl;

import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.Repository;

/**
 * A stub for RepositoryConnection.
 */ 
public class RepositoryConnectionStub implements RepositoryConnection {
    // javadoc inherited
    public boolean beginOperationSet()
            throws RepositoryException {
        return false;
    }

    // javadoc inherited
    public boolean endOperationSet()
            throws RepositoryException {
        return false;
    }

    // javadoc inherited
    public boolean abortOperationSet()
            throws RepositoryException {
        return false;
    }

    // javadoc inherited
    public boolean supportsOperationSets() {
        return false;
    }

    // javadoc inherited
    public void disconnect()
            throws RepositoryException {
    }

    // javadoc inherited
    public boolean isConnected()
            throws RepositoryException {
        return false;
    }

    // javadoc inherited
    public Repository getRepository() {
        return null;
    }

    // javadoc inherited
    public void setCacheRetrievedObjects(boolean cacheRetrievedObjects) {
    }

    // javadoc inherited
    public RepositoryConnection getUnderLyingConnection() 
            throws RepositoryException {
        return null;
    }

    // javadoc inherited
    public void acquireResources() throws RepositoryException {
    }

    // javadoc inherited
    public void releaseResources() throws RepositoryException {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
