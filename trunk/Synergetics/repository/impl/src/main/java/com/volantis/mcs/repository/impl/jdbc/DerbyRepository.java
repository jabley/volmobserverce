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
package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;

/**
 * A JDBC Repository for Apache Derby (aka Cloudscape).
 * <p>
 * NOTE: derby currently only supports short (18 character) index names. This
 * affects the SQL create scripts. Hopefully this will be fixed in future, see
 * http://issues.apache.org/jira/browse/DERBY-104.
 * <p>
 * NOTE: derby does not support CASCADE CONSTRAINTS part of DROP TABLE. This
 * affects the SQL drop scripts.
 *
 * @todo remove as this is no longer needed.
 */
class DerbyRepository
        extends JDBCRepositoryImpl {

    /**
     * Initialise.
     *
     * @param configuration The configuration.
     */
    public DerbyRepository(InternalJDBCRepositoryConfiguration configuration) {
        super(configuration);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 18-Apr-05	7692/1	allan	VBM:2005041504 SimpleDeviceRepositoryFactory - create a dev rep from a url

 12-Jan-05	6627/2	geoff	VBM:2005011001 Support Cloudscape 10/Apache Derby as a repository RDBMS (take 2)

 ===========================================================================
*/
