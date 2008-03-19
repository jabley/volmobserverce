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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl.osgi;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.reporting.impl.DefaultMetrics;
import com.volantis.synergetics.reporting.impl.Metric;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.event.Event;

/**
 * An OSGi event listener that can be used to write certain reports to a jndi
 * obtained datastore.
 */
public class JDBCReportHandler implements ReportEventHandler {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(JDBCReportHandler.class);

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(JDBCReportHandler.class);

    /**
     * The property name prefix for all properties used in the JDBC handler.
     */
    public static final String CONFIG_PROPERTIES_PREFIX =
        DefaultMetrics.REPORT_PREFIX + "jdbc";

    /**
     * The key used to obtain the jndi name for the DataSource
     */
    public static final String JNDI_NAME_PROPERTY =
        CONFIG_PROPERTIES_PREFIX + ".jndi.name";

    /**
     * The key used to obtain the table name
     */
    public static final String TABLE_NAME_PROPERTY =
        CONFIG_PROPERTIES_PREFIX + ".table.name";

    /**
     * The prefix used to store column names against thier associated metric
     * name (bla.table.column.DATE=startDate)
     */
    public static final String TABLE_COLUMN_PROTERTY_PREFIX =
        CONFIG_PROPERTIES_PREFIX + ".table.column.";

    public static final String TABLE_BUILTINCOLUMN_PROTERTY_PREFIX =
        CONFIG_PROPERTIES_PREFIX + ".table.builtincolumn.";

    /**
     * Used to synchronize the handler.
     */
    private final Object LOCK = new Object();

    /**
     * The datasource to use.
     */
    private DataSource datasource = null;

    /**
     * The pre-built prepared statement string
     */
    private String preparedStatement = null;

    /**
     * This array of indices and the column they map to.
     */
    private List indicies = null;

    /**
     * The naming context to use
     */
    private Context context;

    /**
     * Used for test only.
     *
     * @param configuration The configuration to use
     * @param context       the naming context
     */
    JDBCReportHandler(Dictionary configuration, Context context)
        throws ConfigurationException {
        if (null == context) {
            try {
                this.context = new InitialContext();
            } catch (NamingException e) {
                throw new ConfigurationException(
                    JNDI_NAME_PROPERTY, EXCEPTION_LOCALIZER.format(
                    "jndi-naming-exception"), e);
            }
        } else {
            this.context = context;
        }
        setConfiguration(configuration);
    }


    /**
     * Constructor that allows configuration of this object.
     *
     * @param configuration the configuration provided by OSGi ConfigurationAdmin
     * @throws ConfigurationException if any properties are incorrect.
     */
    JDBCReportHandler(Dictionary configuration) throws ConfigurationException {
        this(configuration, null);
    }

    // Javadoc inherited
    public void setConfiguration(Dictionary configuration)
        throws ConfigurationException {

        if (null == configuration) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format(
                    "argument-must-not-be",
                    new Object[]{"configuration", "null"}));
        }

        synchronized (LOCK) {
            String jndiDataSourceName =
                (String) configuration.get(JNDI_NAME_PROPERTY);
            if (null == jndiDataSourceName || "".equals(jndiDataSourceName)) {
                throw new ConfigurationException(
                    JNDI_NAME_PROPERTY, EXCEPTION_LOCALIZER.format(
                    "property-is-not-set", JNDI_NAME_PROPERTY));
            }

            String tableName = (String) configuration.get(TABLE_NAME_PROPERTY);
            if (null == tableName || "".equals(tableName)) {
                throw new ConfigurationException(
                    TABLE_NAME_PROPERTY, EXCEPTION_LOCALIZER.format(
                    "property-is-not-set", TABLE_NAME_PROPERTY));
            }

            try {
                // now try to set up the datasource.
                final Context envContext =
                    (Context) context.lookup("java:comp/env");
                Object obj = envContext.lookup(jndiDataSourceName);
                // check it is the kind of object we expect
                if (!(obj instanceof DataSource)) {
                    throw new ConfigurationException(
                        JNDI_NAME_PROPERTY, EXCEPTION_LOCALIZER.format(
                        "unexpected-class",
                        new Object[]{DataSource.class, obj.getClass()}));
                }

                this.datasource = (DataSource) obj;
            } catch (NamingException e) {
                throw new ConfigurationException(
                    JNDI_NAME_PROPERTY, EXCEPTION_LOCALIZER.format(
                    "jndi-naming-exception"), e);
            }

            // now generate the prepared statement string
            Map columnMap = new HashMap();
            Enumeration enumer = configuration.keys();
            while (enumer.hasMoreElements()) {
                String key = (String) enumer.nextElement();
                String value = (String) configuration.get(key);
                if (key.startsWith(TABLE_COLUMN_PROTERTY_PREFIX)) {
                    // trim everything from the end of the prefix as this is the
                    // name of the column
                    String columnName = key.substring(
                        TABLE_COLUMN_PROTERTY_PREFIX.length());
                    columnMap.put(columnName, value);
                } else if (key.startsWith(TABLE_BUILTINCOLUMN_PROTERTY_PREFIX)) {
                    // trim everything from the end of the prefix as this is the
                    // name of the column
                    String columnName = ":" + key.substring(
                        TABLE_BUILTINCOLUMN_PROTERTY_PREFIX.length());
                    if (Metric.lookup(columnName) == null) {
                        throw new IllegalStateException(
                            "Invalid built-in metric name: " + columnName);
                    }
                    columnMap.put(columnName, value);
                }
            }
            preparedStatement = prepareStatement(tableName, columnMap);
        }
    }

    // Javadoc inherited
    public void handleEvent(Event event) {

        // get local copies of all our state
        String localPreparedStatement = null;
        DataSource localDataSource = null;
        List localIndicies = null;
        synchronized (LOCK) {
            localPreparedStatement = preparedStatement;
            localDataSource = datasource;
            localIndicies = indicies;
        }

        // as it is very likely we are using a connection pool we cannot
        // explicitly reuse the prepared statment. We use it to enable easier
        // insertion of parameters and in the hope that the underlying
        // pool can share the database representation of the PS accross
        // connections
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = localDataSource.getConnection();
            statement =
                connection.prepareStatement(localPreparedStatement);

            // now put the values into the prepared statement.
            int i = 1;
            Iterator it = localIndicies.iterator();
            while (it.hasNext()) {
                boolean paramTypeSupported = true;
                String name = (String) it.next();
                Object value = event.getProperty(DefaultMetrics.REPORT_PROPERTIES_PREFIX + name);
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
                i++;
            }
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("could-not-execute-prepared-statement", e);
        } finally {
            try {
                if (null != statement) {
                    statement.close();
                }
                if (null != connection) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.warn("could-not-close-connection", e);
            }
        }
    }

    /**
     * Generate the prepared statement string
     *
     * @param tableName      the name of the table to generate the prepared
     *                       statement against.
     * @param metricToColumn
     * @return a string representation of a prepared statement
     *
     * @noinspection MagicCharacter
     */
    private String prepareStatement(String tableName, Map metricToColumn) {

        synchronized (LOCK) {
            List columnIndicies = new ArrayList();
            StringBuffer statement = new StringBuffer("INSERT INTO ");
            statement.append(tableName);
            statement.append(" (");

            StringBuffer values = new StringBuffer(") VALUES (");

            Iterator columnIter = metricToColumn.entrySet().iterator();
            boolean firstIt = true;
            while (columnIter.hasNext()) {
                Map.Entry entry = (Map.Entry) columnIter.next();

                // if we are not on the first iteration then prepend the ","'s
                // to both buffers.
                if (!firstIt) {
                    statement.append(',');
                    values.append(',');
                }
                firstIt = false;
                statement.append(entry.getValue());
                values.append('?');
                columnIndicies.add(entry.getKey());
            }

            values.append(')');
            // make the indicies unmodifiable
            this.indicies = Collections.unmodifiableList(columnIndicies);
            return statement.append(values).toString();
        }
    }
}
