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
package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.mcs.repository.jdbc.MCSDriverConfiguration;

/**
 * Test case for the DB2Repository.
 */
public class DB2RepositoryTestCase extends JDBCRepositoryTestCase {

    /**
     * Provide an AbstractRepository (in this case a DB2Repository) that can be
     * used to test with.
     *
     * @return a testable DB2Repository.
     */
    protected JDBCRepositoryImpl createRepository(
            InternalJDBCRepositoryConfiguration configuration) {
        return new DB2Repository(configuration);
    }

    public void testConstructURL() throws Exception {

        JDBCRepositoryFactory factory =
                JDBCRepositoryFactory.getDefaultInstance();

        MCSDriverConfiguration configuration =
                factory.createMCSDriverConfiguration();
        configuration.setHost("host");
        configuration.setPort(8086);
        configuration.setSource("source");

        VendorFactory vendorFactory = new DB2VendorFactory();

        assertEquals("URL with non-default port not as",
                     "jdbc:db2://host:8086/source",
                     vendorFactory.getDriverSpecificURL(configuration));

        configuration.setPort(50000);

        assertEquals("URL with default port not as",
                     "jdbc:db2://host/source",
                vendorFactory.getDriverSpecificURL(configuration));
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Sep-03	1377/1	philws	VBM:2003082803 Fix DB2 JDBC driver URL generation

 ===========================================================================
*/
