/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.java.sql;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import mock.java.sql.CallableStatementMock;
import mock.java.sql.ConnectionMock;
import mock.java.sql.DatabaseMetaDataMock;
import mock.java.sql.PreparedStatementMock;
import mock.java.sql.ResultSetMock;
import mock.java.sql.StatementMock;

/**
 * Tests for SQL related mock objects.
 */
public class SQLTestCase
        extends MockTestCaseAbstract {

    /**
     * Tests that all the mock objects can be initialised correctly.
     */
    public void testInitialisation() {
        new ResultSetMock("resultSet", expectations);
        new ConnectionMock("connection", expectations);
        new StatementMock("statement", expectations);
        new PreparedStatementMock("preparedStatement", expectations);
        new CallableStatementMock("callableStatement", expectations);
        new DatabaseMetaDataMock("databaseMetaData", expectations);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 ===========================================================================
*/
