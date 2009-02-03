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
package com.volantis.mcs.accessors.jdbc;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryException;
import com.volantis.synergetics.log.LogDispatcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class to factor common low level JDBC access code.
 * <p>
 * Inspired by Spring's JDBCTemplate class.
 * <p>
 * NOTE: this is currently nowhere near complete. Feel free to reseach Spring's
 * JDBCTemplate class and add more methods as you see fit.
 */
public class JDBCTemplate {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(JDBCTemplate.class);

    /**
     * Query for a single row.
     *
     * @param connection the database connection to query on.
     * @param sql the SQL containing the declaration of the query.
     * @param rowProcessor processes the row as it is read.
     * @return the value found, or null if none was found.
     * @throws RepositoryException if there was a problem.
     */
    public Object queryForObject(JDBCRepositoryConnection connection,
            String sql, RowProcessor rowProcessor)
            throws RepositoryException {

        Connection sqlConnection = connection.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Object o = null;
        try {
            stmt = sqlConnection.createStatement();
            if (logger.isDebugEnabled()) {
                logger.debug(sql);
            }
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                o = rowProcessor.processRow(rs);
            }
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                logger.error("sql-exception", e);
                throw new JDBCRepositoryException(e);
            }
            throw new JDBCRepositoryException(sqle);
        }
        return o;
    }

    /**
     * Query for a single row/column containing an int value.
     *
     * @param connection the database connection to query on.
     * @param sql the SQL containing the declaration of the query.
     * @return the value found, or null if none was found.
     * @throws RepositoryException if there was a problem.
     */
    public Integer queryForInteger(JDBCRepositoryConnection connection,
            String sql) throws RepositoryException {

        Integer categoryId = null;
        Connection sqlConnection = connection.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = sqlConnection.createStatement();
            if (logger.isDebugEnabled()) {
                logger.debug(sql);
            }
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                categoryId = new Integer(rs.getInt(1));
            }
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                logger.error("sql-exception", e);
                throw new JDBCRepositoryException(e);
            }
            throw new JDBCRepositoryException(sqle);
        }
        return categoryId;
    }

    /**
     * Query for a list of results.
     *
     * @param connection the database connection to query on.
     * @param sql the SQL containing the declaration of the query.
     * @param rowProcessor processes each row as it is read.
     * @param setter sets up the prepared statement values.
     * @return a list containing the results of the processed rows.
     * @throws RepositoryException if there was a problem.
     */
    public List query(JDBCRepositoryConnection connection, String sql,
            RowProcessor rowProcessor, PreparedStatementSetter setter)
            throws RepositoryException {

        Connection sqlConnection = connection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List list = null;

        try {
            pstmt = sqlConnection.prepareStatement(sql);
            setter.setValues(pstmt);

            if (logger.isDebugEnabled()) {
                logger.debug(sql);
            }
            rs = pstmt.executeQuery();
            list = new ArrayList();
            while (rs.next()) {
                Object row = rowProcessor.processRow(rs);
                list.add(row);
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("sql-exception", e);
                throw new JDBCRepositoryException(e);
            }
            throw new JDBCRepositoryException(sqle);
        }

        return list;
    }

    /**
     * Issue a single SQL update.
     *
     * @param connection the database connection to execute against.
     * @param sql static SQL to execute.
     * @throws RepositoryException if there was a problem.
     */
    public void update(JDBCRepositoryConnection connection, String sql)
            throws RepositoryException {
        Connection sqlConnection = connection.getConnection();
        Statement stmt = null;
        try {
            stmt = sqlConnection.createStatement();
            if (logger.isDebugEnabled()) {
                logger.debug(sql);
            }
            stmt.executeUpdate(sql);
            stmt.close();
            stmt = null;
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            throw new JDBCRepositoryException(sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                logger.error("sql-exception", e);
                throw new JDBCRepositoryException(e);
            }
        }
    }

    /**
     * Issue an update using a PreparedStatementSetter to set bind parameters,
     * with given SQL
     *
     * @param connection the database connection to execute against.
     * @param sql the SQL to execute.
     * @param setter sets up the prepared statement values.
     * @throws RepositoryException if there was a problem.
     */
    public void update(final JDBCRepositoryConnection connection, String sql,
            PreparedStatementSetter setter) throws RepositoryException {

        Connection sqlConnection = connection.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = sqlConnection.prepareStatement(sql);
            setter.setValues(pstmt);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = null;
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            throw new JDBCRepositoryException(sqle);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("sql-exception", e);
                throw new JDBCRepositoryException(e);
            }
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 05-Aug-04	5072/3	byron	VBM:2004080304 JDBC foundation accessors for device identification by TAC - rework issues

 04-Aug-04	5072/1	byron	VBM:2004080304 JDBC foundation accessors for device identification by TAC

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 ===========================================================================
*/
