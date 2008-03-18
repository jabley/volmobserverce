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

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.AlternateNames;

/**
 * Test case for the DB2Repository.
 */
public class DB2MVSRepositoryTestCase extends JDBCRepositoryTestCase {

    /**
     * Provide an AbstractRepository (in this case a DB2MVSRepository) that can be
     * used to test with.
     *
     * @return a testable DB2MVSRepository.
     */
    protected JDBCRepositoryImpl createRepository(
            InternalJDBCRepositoryConfiguration configuration) {
        return new DB2MVSRepository(configuration);
    }

    public void testConstructURL() throws Exception {
//        Map properties = new HashMap();
//
//        properties.put(JDBCRepository.HOST_PROPERTY,
//                       "host");
//        properties.put(JDBCRepository.PORT_PROPERTY,
//                       new Integer(8086));
//        properties.put(JDBCRepository.SOURCE_PROPERTY,
//                       "source");
//
//        assertEquals("URL with non-default port not as",
//                     "jdbc:db2:source",
//                     DB2MVSRepository.constructURL(properties));

    }

    /**
     * Test that the getTableName() method returns the translated
     * table name.
     * @throws RepositoryException if there is a problem opening a
     * connection.
     */ 
    public void testGetTableName() throws RepositoryException {
        JDBCRepositoryImpl repository =
                (JDBCRepositoryImpl) createTestableRepository();

        AlternateNames names = new AlternateNames("NORMAL", "SHORT");

        String tableName = repository.getAppropriateName(names);
        assertEquals("The tablename is incorrect",
                     "SHORT", tableName);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	3649/1	mat	VBM:2004031910 Add short tablename support

 08-Apr-04	3653/1	mat	VBM:2004031910 Change accessors to support resolving the tablename from the repository

 04-May-04	4023/1	ianw	VBM:2004032302 Added support for short length tables

 22-Mar-04	3486/1	ianw	VBM:2004031909 Added support for 18 character table names

 ===========================================================================
*/
