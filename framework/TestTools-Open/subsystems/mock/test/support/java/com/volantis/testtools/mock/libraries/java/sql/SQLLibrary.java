/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.java.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Triggers auto generation of classes within <code>java.sql</code> and
 * <code>javax.sql</code> for which the source is not available.
 *
 * <p>If you add new fields in this file then remember to update the associated
 * test case to ensure that the generated mocks are usable.</p>
 *
 * @mock.generate library="true"
 */
public class SQLLibrary {

    /**
     * @mock.generate interface="true"
     */
    public ResultSet resultSet;

    /**
     * @mock.generate interface="true"
     */
    public Connection connection;

    /**
     * @mock.generate interface="true"
     */
    public Statement statement;

    /**
     * @mock.generate interface="true" base="Statement"
     */
    public PreparedStatement preparedStatement;

    /**
     * @mock.generate interface="true" base="PreparedStatement"
     */
    public CallableStatement callableStatement;

    /**
     * @mock.generate interface="true"
     */
    public DatabaseMetaData databaseMetaData;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 ===========================================================================
*/
