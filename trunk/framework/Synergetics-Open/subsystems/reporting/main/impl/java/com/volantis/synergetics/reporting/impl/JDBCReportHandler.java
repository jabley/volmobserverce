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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.reporting.ReportHandler;
import com.volantis.synergetics.reporting.ExclusionManager;
import com.volantis.synergetics.reporting.config.ColumnMapping;
import com.volantis.synergetics.reporting.config.ConnectionStrategy;
import com.volantis.synergetics.reporting.config.ReportElement;
import com.volantis.synergetics.reporting.config.SqlHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class is not public. It implements ReportHandler but does not meet the
 * requirements placed on the constructor of a true ReportHandler. It can do
 * this because the Factory "knows" about it.
 */
class JDBCReportHandler implements ReportHandler {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(JDBCReportHandler.class);

    /**
     * The localizer used to retrieve localized messages for exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            JDBCReportHandler.class);

    private final ConnectionStrategy connectionStrategy;
    private final String insertSQLStatement;
    private final List bindingPositions;
    private final String datasourceName;
    private final String bindingName;

    /**
     * Constructor
     * @param connStrategy ConnectionStrategy
     * @param report ReportElement
     */
    JDBCReportHandler(ConnectionStrategy connStrategy, ReportElement report) {
        SqlHandler handler = report.getSqlHandler();
            connectionStrategy = connStrategy;
        insertSQLStatement = prepareSQLString(handler);
        bindingPositions = prepareBindingPositions(handler);
        datasourceName = handler.getDatasourceName();
        this.bindingName = report.getBinding();
    }

    // javadoc inherited
    public void report(Map metrics) {
        Connection conn = null;
        PreparedStatement statement = null;
        boolean neededBackup = false;
        Exception exception = null;

        // hacked in to work around refactoring
        if (!ExclusionManager.getDefaultInstance().isExcluded(
            this.bindingName, metrics)) {


            if(connectionStrategy == null) {
                neededBackup = true;
                exception = new IllegalStateException(EXCEPTION_LOCALIZER.format(
                    "datasource-creation-error", datasourceName));
            } else {
                try {
                    conn = connectionStrategy.getConnection();
                } catch (SQLException ex) {
                    LOGGER.error("sql-connection-not-created", datasourceName, ex);
                    neededBackup = true;
                    exception = ex;
                }
            }
            if(conn == null) {
                neededBackup = true;
                exception = new IllegalStateException(EXCEPTION_LOCALIZER.format(
                    "sql-connection-not-created", datasourceName));
            }

            if(!neededBackup) {
                try {
                    statement = conn.prepareStatement(insertSQLStatement);
                } catch (SQLException ex) {
                    LOGGER.error("could-not-create-prepared-statement",
                                 insertSQLStatement, ex);
                    neededBackup = true;
                    exception = ex;
                }
            }

            if(!neededBackup) {
                try {
                    boolean paramTypeSupported = true;
                    int i = 1;
                    for (Iterator it = bindingPositions.iterator();  it.hasNext(); i++) {
                        String name = (String) it.next();
                        Object value = metrics.get(name);
                        if (value != null) {
                            if (value instanceof java.util.Date) {
                                java.util.Date date = (java.util.Date) value;
                                statement.setTimestamp(
                                    i, new Timestamp(date.getTime()));
                            } else {
                                statement.setObject(i, value);
                            }
                        } else {
                            int paramType = Types.NULL;
                            if (paramTypeSupported) {
                                try {
                                    paramType = 
                                        statement.getParameterMetaData().getParameterType(i);
                                } catch (Exception e) {
                                    paramTypeSupported = false;
                                }
                            }                        
                            statement.setNull(i, paramType);
                        }
                    }

                    statement.executeUpdate();
                } catch (SQLException e) {
                    LOGGER.error("could-not-execute-prepared-statement", e);
                    neededBackup = true;
                    exception = e;
                } finally {
                    try {
                        if (! conn.getAutoCommit()) {
                            conn.commit();
                        }
                        statement.close();
                        conn.close();
                    } catch (SQLException e) {
                        LOGGER.warn("could-not-clear-prepared-statement", e);
                    }
                }
            }

            if(neededBackup) {
                LOGGER.error("sql-handler-error", bindingName, exception);
            }
        }
    }

    /**
     * Prepare SQL insert string using handler metrics definition
     * @param handler SqlHandler
     * @return String SQL insert string
     * @noinspection MagicCharacter
     */
    private static String prepareSQLString(SqlHandler handler) {

        StringBuffer statement = new StringBuffer("INSERT INTO ");
        statement.append(handler.getTableName());
        statement.append(" (");

        StringBuffer values = new StringBuffer(") VALUES (");

        int numColumns = handler.sizeColumnMappingList();
        for (int i = 0; i < numColumns; i++) {
            ColumnMapping cm = handler.getColumnMapping(i);
            if (i != 0) {
                statement.append(',');
                values.append(',');
            }
            statement.append(cm.getColumn());
            values.append('?');
        }

        values.append(')');
        return statement.append(values).toString();
    }

    /**
     * Prepare parametrs position order for prepared statement binding
     * @param handler SqlHandler
     * @return List of prepared statement bindings
     */
    private static List prepareBindingPositions(SqlHandler handler) {
        ArrayList ret = new ArrayList();
        int numColumns = handler.sizeColumnMappingList();
        for (int i = 0; i < numColumns; i++) {
            ColumnMapping cm = handler.getColumnMapping(i);
            ret.add(cm.getMetric());
        }
        return ret;
    }
}
