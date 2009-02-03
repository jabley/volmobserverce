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
 * $Header: /src/voyager/com/volantis/mcs/repository/jdbc/HypersonicRepository.java,v 1.2 2003/03/07 10:21:46 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Nov-02    Steve           VBM:2002071604  -  Repository managed by the
 *                              Hypersonic SQL database driver.
 * 28-Feb-03    Geoff           VBM:2003010904 - Remove unneccessary code 
 *                              which was causing unexpected problems with the
 *                              unit test cases.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;

/**
 * A Hypersonic specialization of JDBCRepository.
 *
 * @todo remove as this is no longer needed.
 */
class HypersonicRepository
        extends JDBCRepositoryImpl {

    /**
     * Initialise.
     *
     * @param configuration The configuration.
     */
    protected HypersonicRepository(InternalJDBCRepositoryConfiguration configuration) {
        super(configuration);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Apr-05	7692/1	allan	VBM:2005041504 SimpleDeviceRepositoryFactory - create a dev rep from a url

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Jun-03	316/2	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
