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
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 04-Jun-01    Paul            VBM:2001051103 - Added a work connection which
 *                              is used to do administration work for locks
 *                              which cannot / must not be done on the clients
 *                              connection. Also added generic lock / unlock
 *                              methods which can be used for any objects.
 * 26-Jun-01    Paul            VBM:2001051103 - Added a check to make sure
 *                              that an object was locked before trying to
 *                              unlock it.
 * 16-Jul-01    Paul            VBM:2001070508 - Cleaned up and added a check
 *                              to make sure that an object wasn't already
 *                              locked before we locked it.
 * 09-Aug-01    Kula            MSSQL Support added
 * 09-Aug-01    Allan           VBM:2001080805 - Added support for new
 *                              jdbc config properties:
 *                              keep-connections-alive, and
 *                              connection-pole-interval.
 * 10-Aug-01    Allan           VBM:2001080905 - Renamed connection-pole...
 *                              etc to connection-poll...
 * 10-Aug-01    Doug            VBM:2001080809 Added support for DB2. Modified
 *                              selectUniqueValues to use the
 *                              getVendorSpecificSQLKeyWord.
 * 10-Aug-01    Paul            VBM:2001071607 - Changed the default key words,
 *                              allowed getVendorSpecificSQLKeyWord to return
 *                              null to indicate no support for a keyword and
 *                              fixed a minor problem with selectUniqueValues.
 * 29-Aug-01    Allan           VBM:2001082902 - Modify all SQL statements
 *                              that refered to the KEY column to refer to
 *                              LOCKKEY instead.
 * 05-Oct-01    Paul            VBM:2001092801 - Added a list of supported
 *                              vendors and changed the getPort method to
 *                              return an int instead of a String.
 * 15-Oct-01    Paul            VBM:2001101202 - Added
 *                              createRepositoryAccessorManager method.
 * 16-Oct-01    Paul            VBM:2001082807 - Removed requester parameter
 *                              from the lock method.
 * 17-Oct-01    Paul            VBM:2001101701 - Modify the
 *                              createRepositoryAccessorManager method to
 *                              return the singleton instance of
 *                              JDBCRepositoryAccessorManager rather than
 *                              create a new one every time.
 * 07-Nov-01    Mat             VBM:2001110701 Add ability to use an
 *                              application servers connection pool instead
 *                              of ours.
 * 08-Nov-01    Paul            VBM:2001110701 - Added property to allow caller
 *                              to optionally specify a DataSource to use
 *                              in preference to creating our own, also
 *                              removed all references to Volantis and
 *                              AppServerInterfaceManager.
 * 21-Nov-01    Payal           VBM:2001111202 - Modified method
 *                              lock() to add TABLE_NAME constant.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 28-Jan-02    Payal           VBM:2002012305 - Modified lock () ,
 *                              Added  boolean flag called abortOperationSet
 *                              and set to the result of beginOperationSet and
 *                              endOperationSet. The boolean flag is checked
 *                              in finally block and if true the
 *                              abortOperationSet() is called.
 * 29-Jan-02    Payal           VBM:2002012305 - Modified lock ()  to set
 *                              boolean flag to ! the result of
 *                              endOperationSet.
 * 29-Jan-02    Payal           VBM:2002012305 - Modified lock () moved
 *                              lockConnection.beginOperationSet statement
 *                              before the lockStmt.executeQuery.
 * 30-Jan-02    Allan           VBM:2002013006 - Modified openConnection() so
 *                              as not to create a new underlying connection.
 *                              Added getDataSource().
 * 11-Feb-02    Paul            VBM:2001122105 - Fixed minor problem in a
 *                              comment.
 * 13-Feb-02    Allan           VBM:2002020502 - Removed obselete code comment.
 * 25-Feb-02    Payal           VBM:2002022008 - Modified createJDBCRepository()
 *                              to get JDBCRepositoryConnectionImpl to check
 *                              valid login details are entered for an database
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 31-Oct-02    Adrian          VBM:2002103004 - Added new member boolean
 *                              anonymous and getter/setter methods.  Added
 *                              static final String ANONYMOUS_PROPERTY as key
 *                              to retrieve repository "anonymous" property
 *                              from map of properties.  Updated method
 *                              createJDBCRepository to set value of anonymous.
 *                              Updated createConnectionPool to throw an
 *                              Exception if anonymous is true as Mariner
 *                              ConnectionPool does not support anonymous login
 * 08-Nov-02    Steve           VBM:2002071604  -  Added Hypersonic SQL ID's
 * 19-Dec-02    Allan           VBM:2002120402 - Added todo comment regarding
 *                              2002071604 change in getPassword() and javadoc.
 * 07-Jan-02    Allan           VBM:2003010711 - Removed HyperSonic SQL from
 *                              the vendors array.
 * 04-Feb-03    Ian             VBM:2003020413 - Added MS MSSQL driver.
 * 18-Feb-03    Ian             VBM:2003021405 - Implemented VBM 2003020413 for
 *                              Mimas.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 12-May-03    Allan           VBM:2003051303 - Handling of connections
 *                              moved to here from AbstractRepository involving
 *                              overriding  and terminate() and adding
 *                              addConnection() and removeConnection().
 * 19-May-03    Allan           VBM:2003051303 - Removed conflict in javadoc.
 * 23-May-03    Phil W-S        VBM:2003052301 - Wrap logger.debug() statements
 *                              in condition.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.jdbc;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.ProjectConfiguration;
import com.volantis.mcs.project.ProjectFactory;
import com.volantis.mcs.repository.DeprecatedRepository;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * A repository accessed by the java JDBC interface.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated See {@link LocalRepository} and {@link JDBCRepositoryFactory}.
 *             This was deprecated in version 3.5.1.
 */
public class JDBCRepository
        extends DeprecatedRepository {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(JDBCRepository.class);

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(JDBCRepository.class);

    private static final JDBCRepositoryFactory factory = JDBCRepositoryFactory.getDefaultInstance();

    /**
     * Constant for the key used to obtain/store the database vendor
     */
    public static final String VENDOR_PROPERTY = "vendor";

    /**
     * Constant for the database name to use when accessing
     * an oracle database.
     */
    public static final String VENDOR_ORACLE8 = "oracle";

    /**
     * Constant for the database name to use when accessing
     * a postgres database.
     */
    public static final String VENDOR_POSTGRESQL = "postgres";

    /**
     * Constant for the database name to use when accessing
     * an ODBC database.
     */
    public static final String VENDOR_ODBC = "odbc";

    /**
     * Constant for the database name to use when accessing
     * a MSSQLDATAD database.
     */
    public static final String VENDOR_MSSQLDATAD = "mssql-datad";

    /**
     * Constant for the database name to use when accessing
     * a MSSQLJSQL database.
     */
    public static final String VENDOR_MSSQLJSQL = "mssql-jsql";

    /**
     * Constant for the database name to use when accessing
     * a MSSQLMICROSOFT database.
     */
    public static final String VENDOR_MSSQLMICROSOFT = "mssql-ms";

    /**
     * Constant for the database name to use when accessing
     * a DB2 database.
     */
    public static final String VENDOR_DB2 = "db2";

    /**
     * Constant for the database name to use when accessing
     * a DB2 MVS database.
     */
    public static final String VENDOR_DB2MVS = "db2-mvs";

    /**
     * Constant for the database name to use when accessing
     * a Hypersonic database.
     */
    public static final String VENDOR_HYPERSONIC = "hypersonic";

    /**
     * Constant for the database name to use when accessing
     * a MySql database.
     */
    public static final String VENDOR_MYSQL = "mysql";

    /**
     * The database name to use for Apache Derby in "server" mode.
     * <p/>
     * Derby also supports "embedded" mode but this has not been implemented
     * yet.
     */
    public static final String VENDOR_DERBY_SERVER = "derby-server";

    /**
     * Constant for the database name to use when accessing
     * a Sybase database.
     */
    public static final String VENDOR_SYBASE = "sybase";

    /** Default project name. */
    public static final String DEFAULT_PROJECT = "#DefaultProject";

    /**
     * This property specifies a complete DataSource to use.
     *
     * <p>If specified then the other properties which are used to find a
     *  DataSource, i.e. host, port, source, are all ignored.</p>
     */
    public static final String DATASOURCE_PROPERTY = "datasource";

    /**
     * Constant for the key used to store/retrieve the host property.
     */
    public static final String HOST_PROPERTY = "host";

    /**
     * Constant for the key used to store/retrieve the port property.
     */
    public static final String PORT_PROPERTY = "port";

    /**
     * Constant for the key used to store/retrieve the source property.
     */
    public static final String SOURCE_PROPERTY = "source";

    /**
     * Constant for the key used to store/retrieve the database URL property.
     */
    public static final String DATABASE_URL_PROPERTY = "database.url";

    /**
     * Constant for the key used to store/retrieve the driver class property.
     */
    public static final String DRIVER_CLASS_PROPERTY = "driver.class";

    /**
     * Constant for the key used to store/retrieve the parameters property.
     */
    public static final String PARAMETERS_PROPERTY = "parameters";

    /**
     * Constant for the key used to store/retrieve the username property.
     */
    public static final String USERNAME_PROPERTY = "user";

    /**
     * Constant for the key used to store/retrieve the password property.
     */
    public static final String PASSWORD_PROPERTY = "password";

    /**
     * This property determines if a username and password are required to
     * log into the database.
     */
    public static final String ANONYMOUS_PROPERTY = "anonymous";

    /**
     * Constant for the key used to store/retrieve the keep connections alive
     * property.
     */
    public static final String KEEP_CONNECTIONS_ALIVE_PROPERTY =
            "keep-connections-alive";

    /**
     * Constant for the key used to store/retrieve the connection poll interval
     * property.
     */
    public static final String CONNECTION_POLL_INTERVAL_PROPERTY =
            "connection-poll-interval";

    /**
     * Constant for the key used to store/retrieve the property indicating if
     * connection pooling is enabled.
     */
    public static final String POOL_ENABLED_PROPERTY
            = "pool.enabled";

    /**
     * Constant for the key used to store/retrieve the property providing
     * the maximum number of connections available within the connection
     * pool.
     */
    public static final String POOL_MAX_CONNECTIONS_PROPERTY
            = "pool.maxConnections";

    /**
     * Constant for the key used to store/retrieve the maximum number of
     * free connections avaiable from the connection pool.
     */
    public static final String POOL_MAX_FREE_CONNECTIONS_PROPERTY
            = "pool.maxFreeConnections";

    /**
     * Constant for the key used to store/retrieve the minimum number of
     * free connection the connection pool is restricted to.
     */
    public static final String POOL_MIN_FREE_CONNECTIONS_PROPERTY
            = "pool.minFreeConnections";

    /**
     * Constant for the key used to store/retrieve the initial number of
     * connections available to the connection pool.
     */
    public static final String POOL_INITIAL_CONNECTIONS_PROPERTY
            = "pool.initialConnections";

    /**
     * The name of the default project.
     * <p/>
     * This is the project to use to access objects that do not explicitly
     * specify the project.
     *
     * @deprecated Projects are being separated from
     */
    public static final String DEFAULT_PROJECT_NAME_PROPERTY =
            "defaultProjectName";

    /**
     * The name of the standard device project.
     * <p/>
     * This is the project to use to access devices.
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-include-in ProfessionalServicesAPI
     * @volantis-api-include-in InternalAPI
     *
     * @deprecated no longer has any effect.
     */
    public static final String STANDARD_DEVICE_PROJECT_NAME_PROPERTY =
            "standardDeviceProjectName";

    /**
     * This is used by Volantis to ensure
     * that connections are only released immediately at runtime.  The value
     * of this property is expected to be of type {@link Boolean}
     *
     * @deprecated Internal use only.
     */
    public static final String RELEASE_CONNECTIONS_IMMEDIATELY =
            "releaseConnectionsImmediately";

    /**
     * The name of the property which determines whether short or descriptive
     * names are used for tables and columns. If the value of the property is
     * set to true then short names are used, otherwise descriptive names are
     * used. The short names are all 18 characters or less.
     * <p/>
     * Note: Some databases (e.g. DB2 z/OS 7.1) will only work with short names.
     * In these cases this configuration is ignored.
     * <p/>
     * The value of this property is expected to be of type {@link Boolean}.
     */
    public static final String USE_SHORT_NAMES = "useShortNames";

    private JDBCRepository(
            LocalRepository repository, Project defaultProject) {
        super(repository, defaultProject);
    }

    /**
     * Helper method to obtain an integer value stored against the
     * supplied propertyName in the supplied properties Map.
     *
     * @param properties   map to be inspected.
     * @param propertyName the key of the property required
     * @param defaultValue default value to be returned if the key
     *                     propertyName does not exist in properties.
     * @return the value stored in properties against the key propertyName
     *         as an int, or the default value if propertyName does not exist in
     *         properties.
     * @throws NumberFormatException if an error occurs converting
     *                               the property to an integer.
     */
    private static int getInt(
            Map properties, String propertyName,
            int defaultValue)
            throws NumberFormatException {

        // Get the maximum number of connections allowed.
        Object value = properties.get(propertyName);

        if (value != null) {
            if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else if (value instanceof Integer) {
                return ((Integer) value).intValue();
            }
        }
        return defaultValue;
    }

    /**
     * Create a JDBC DataSource object that may or may not be in the
     * connection pool.
     *
     * @param properties the list of properties that is used to
     *                   configure the DatSource
     * @return a valid DataSource object
     * @throws RepositoryException
     */
    public static DataSource createJDBCDriverDataSource(Map properties)
            throws RepositoryException {

        JDBCDriverConfiguration configuration =
                factory.createJDBCDriverConfiguration();

        // If we find parameters then we place these parameters as key/value
        // pairs in the properties file. The idea is to provide a mechanism
        // for supplying JDBC configuration values using this map.
        // For example, if the key/value is 'user'/'volantis' then this value
        // would be used instead of any value for 'user' found in the config
        // file.
        final Properties driverProperties = new Properties();
        driverProperties.putAll((Map) properties.get(PARAMETERS_PROPERTY));
        configuration.setDriverProperties(driverProperties);

        final String driverClassName = (String) properties.get(
                DRIVER_CLASS_PROPERTY);
        configuration.setDriverClassName(driverClassName);

        final String url = (String) properties.get(DATABASE_URL_PROPERTY);
        configuration.setDriverSpecificDatabaseURL(url);

        MCSConnectionPoolConfiguration connectionPoolConfiguration =
                getConnectionPoolConfiguration(factory, properties);
        configuration.setConnectionPoolConfiguration(
                connectionPoolConfiguration);

        return factory.createJDBCDriverDataSource(configuration);
    }


    /**
     * Create a Mariner DataSource object that may or may not be in the
     * connection pool.
     *
     * @param properties the list of properties that is used to
     *                   configure the DatSource
     * @return a valid DataSource object
     * @throws RepositoryException
     * @throws JDBCRepositoryException
     * @todo better this method could benefit from
     * some refactoring (see the method
     * createJDBCRepository()
     */
    public static DataSource createDriverDataSource(Map properties)
            throws JDBCRepositoryException, RepositoryException {

        // Get the vendor string.
        JDBCDriverVendor vendor = getVendor(properties);

        MCSDriverConfiguration configuration =
                factory.createMCSDriverConfiguration();
        configuration.setDriverVendor(vendor);
        if (vendor != JDBCDriverVendor.HYPERSONIC) {
            configuration.setHost(getHost(properties));
            configuration.setPort(getPort(properties));
        }
        configuration.setSource(getSource(properties));

        MCSConnectionPoolConfiguration connectionPoolConfiguration =
                getConnectionPoolConfiguration(factory, properties);
        configuration.setConnectionPoolConfiguration(
                connectionPoolConfiguration);

        return factory.createMCSDriverDataSource(configuration);
    }

    /**
     * Create an anonymous datasource, one which implicitly knows its own
     * username and password without the user having to specify it at
     * connection time.
     *
     * @param dataSource The DataSource to anonimise.
     * @param username   The username to use during connection.
     * @param password   The password to use during connection.
     * @return Tha anonimised DataSource
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-include-in ProfessionalServicesAPI
     * @volantis-api-include-in InternalAPI
     */
    public static DataSource createAnonymousDataSource(
            DataSource dataSource,
            String username,
            String password) {
        return factory.createAnonymousDataSource(dataSource,
                username, password);
    }

    /**
     * Factory method to create a JDBCRepository initialised with the
     * properties stored in properties.
     *
     * @param properties contains the database properties
     * @return a JDBCRepository initialised with the properties supplied
     *         in properties.
     * @throws RepositoryException if an error occurs whilst creating the
     *                             repository.
     */
    public static JDBCRepository createRepository(Map properties)
            throws RepositoryException {

        JDBCRepositoryConfiguration configuration =
                factory.createJDBCRepositoryConfiguration();

        JDBCDriverVendor vendor = getVendor(properties);
        configuration.setDriverVendor(vendor);

        // See whether there is a complete datasource specified, if there is
        // then use that directly.
        DataSource dataSource = (DataSource) properties.get(
                DATASOURCE_PROPERTY);
        if (dataSource == null) {
            dataSource = createDriverDataSource(properties);
        }

        configuration.setDataSource(dataSource);

        // Extract the default project name from the properties.
        String defaultProjectName = (String)
                properties.get(DEFAULT_PROJECT_NAME_PROPERTY);

        configuration.setAnonymous(getAnonymous(properties));
        configuration.setUsername(getUsername(properties));
        configuration.setPassword(getPassword(properties));
        configuration.setReleaseConnectionsImmediately(
                getReleaseConnectionsImmediately(properties));
        configuration.setShortNames(getUseShortNames(properties));

        LocalRepository repository = factory.createJDBCRepository(configuration);

        // Create the project.
        ProjectFactory projectFactory = ProjectFactory.getDefaultInstance();
        ProjectConfiguration projectConfiguration = projectFactory.createProjectConfiguration();
        projectConfiguration.setRepository(repository);
        projectConfiguration.setPolicyLocation(defaultProjectName);
        Project project = projectFactory.createProject(projectConfiguration);

        return new JDBCRepository(repository, project);
    }

    /**
     * Helper method to obtain the value of
     * {@link #CONNECTION_POLL_INTERVAL_PROPERTY} from the supplied properties.
     *
     * @param properties the map containing
     *                   {@link #CONNECTION_POLL_INTERVAL_PROPERTY}
     * @return the connection poll interval.
     *         If {@link #CONNECTION_POLL_INTERVAL_PROPERTY} does not exist in
     *         properties then returns 0.
     */
    protected static boolean getKeepConnectionsAlive(Map properties) {

        Object value = properties.get(KEEP_CONNECTIONS_ALIVE_PROPERTY);
        if (value != null) {
            if (value instanceof Boolean) {
                return ((Boolean) value).booleanValue();
            } else if (value instanceof String) {
                return "true".equalsIgnoreCase((String) value);

            }
        }
        return false;
    }

    /**
     * Helper method to obtain the value of
     * {@link #KEEP_CONNECTIONS_ALIVE_PROPERTY} from the supplied
     * properties
     *
     * @param properties map containing the property,
     *                   {@link #KEEP_CONNECTIONS_ALIVE_PROPERTY}
     * @return true if connections should be kept alive; otherwise false.
     * @throws RepositoryException
     */
    protected static int getConnectionPollInterval(Map properties)
            throws RepositoryException {

        Object cpi = properties.get(CONNECTION_POLL_INTERVAL_PROPERTY);
        if (cpi != null) {
            if (cpi instanceof Integer) {
                return ((Integer) cpi).intValue();
            } else if (cpi instanceof String) {
                try {
                    return Integer.parseInt((String) cpi);
                } catch (NumberFormatException e) {
                    logger.error("unexpected-exception", e);
                    throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                            "connection-invalid-poll-interval", cpi));
                }
            }
        }
        return 0;
    }

    /**
     * Extract the value of the property ANONYMOUS_PROPERTY and return it as
     * a boolean.
     *
     * @param properties The Repository properties
     * @return true if and only if there is a value of "true" in the map of
     *         repository properties.
     */
    protected static boolean getAnonymous(Map properties) {

        boolean result = false;
        String anon = (String) properties.get(ANONYMOUS_PROPERTY);
        if (anon != null && anon.length() != 0) {
            result = Boolean.valueOf(anon).booleanValue();
        }
        return result;
    }

    /**
     * Helper method to obtain {@link #USERNAME_PROPERTY} from the supplied
     * properties.
     *
     * @param properties map containing {@link #USERNAME_PROPERTY}.
     * @return The user name obtained from the supplied properties.
     * @throws RepositoryException if {@link #USERNAME_PROPERTY} does not
     *                             exists in the map or no value is associated with
     *                             {@link #USERNAME_PROPERTY}.
     */
    protected static String getUsername(Map properties)
            throws RepositoryException {

        String username = (String) properties.get(USERNAME_PROPERTY);
        if (username == null || username.length() == 0) {
            throw new JDBCRepositoryException(
                    EXCEPTION_LOCALIZER.format("jdbc-missing-user-name"));
        }

        return username;
    }

    /**
     * Get the password from the properties Map/
     *
     * @param properties The Repository properties
     * @return The value of the password property or null sometimes (see todo)
     * @throws RepositoryException If the password is not found (sometimes).
     * @todo later The HyperSonic code is a hack and should be done in a
     * more generic fashion perhaps not even in this method.
     */
    protected static String getPassword(Map properties)
            throws RepositoryException {

        String password = (String) properties.get(PASSWORD_PROPERTY);
        String vendor = (String) properties.get(VENDOR_PROPERTY);
        // todo - this HYPERSONIC stuff is a bit of a hack - it could be done
        // better. I don't have time to fix it up now.
        if (VENDOR_HYPERSONIC.equalsIgnoreCase(vendor) == false) {
            if (password == null || password.length() == 0) {
                throw new JDBCRepositoryException(
                        EXCEPTION_LOCALIZER.format("jdbc-missing-password"));
            }
        }
        return password;
    }

    /**
     * Get the property from the map that determines whether the connection
     * will allow immediate release.  This will only be present in the map
     * if we are in a runtime environment.  By default we return false.
     *
     * @param properties The Repository properties
     * @return true if we allow connections to be released immediately,
     *         otherwise false.
     */
    protected static boolean getReleaseConnectionsImmediately(Map properties) {
        boolean result = false;
        Boolean release = (Boolean)
                properties.get(RELEASE_CONNECTIONS_IMMEDIATELY);
        if (release != null) {
            result = release.booleanValue();
        }
        return result;
    }

    /**
     * Get the property from the map that determines whether this repository
     * is to use short table and column names. By default we return false.
     *
     * @param properties The Repository properties
     * @return true if we allow connections to be released immediately,
     *         otherwise false.
     */
    protected static boolean getUseShortNames(Map properties) {
        boolean result = false;
        Boolean useShortNames = (Boolean)
                properties.get(USE_SHORT_NAMES);
        if (useShortNames != null) {
            result = useShortNames.booleanValue();
        }
        return result;
    }

    /**
     * Helper method to obtain the database host stored against
     * {@link #HOST_PROPERTY} in the supplied
     * properties.
     *
     * @param properties the map containing {@link #HOST_PROPERTY}
     * @return The name of the host to use when connecting to the database.
     * @throws RepositoryException if {@link #HOST_PROPERTY} does not exist
     *                             in properties.
     */
    protected static String getHost(Map properties)
            throws RepositoryException {

        String host = (String) properties.get(HOST_PROPERTY);
        if (host == null || host.length() == 0) {
            throw new JDBCRepositoryException(
                    EXCEPTION_LOCALIZER.format("jdbc-missing-host"));
        }

        return host;
    }

    /**
     * Helper method to obtain the port stored against
     * {@link #PORT_PROPERTY} in the supplied
     * properties.
     *
     * @param properties the map containing {@link #PORT_PROPERTY}
     * @return The port number to use when attempting to connect to the
     *         database.
     * @throws RepositoryException if {@link #PORT_PROPERTY} does not exist
     *                             in properties or an error occured converting the value to an int.
     */
    protected static int getPort(Map properties)
            throws RepositoryException {

        Object value = properties.get(PORT_PROPERTY);
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        } else {
            String string = (String) value;
            if (string == null || string.length() == 0) {
                throw new JDBCRepositoryException(
                        EXCEPTION_LOCALIZER.format("jdbc-missing-port"));
            }

            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException nfe) {
                throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                        "jdbc-invalid-port",
                        string));
            }
        }
    }

    /**
     * Helper method to obtain the source property stored against
     * {@link #SOURCE_PROPERTY} in the supplied
     * properties.
     *
     * @param properties the map containing {@link #SOURCE_PROPERTY}
     * @return The source to use when attempting to connect to the
     *         database.
     * @throws RepositoryException if {@link #SOURCE_PROPERTY} does not exist
     *                             in properties.
     */
    protected static String getSource(Map properties)
            throws RepositoryException {

        String source = (String) properties.get(SOURCE_PROPERTY);
        if (source == null || source.length() == 0) {
            throw new JDBCRepositoryException(
                    EXCEPTION_LOCALIZER.format("jdbc-missing-source"));
        }

        return source;
    }

    /**
     * Helper method to obtain the vendor property stored against
     * {@link #VENDOR_PROPERTY} in the supplied
     * properties.
     *
     * @param properties the map containing {@link #VENDOR_PROPERTY}
     * @return the vendor of the database being used.
     * @throws RepositoryException if {@link #VENDOR_PROPERTY} does not exist
     *                             in properties.
     */
    protected static JDBCDriverVendor getVendor(Map properties)
            throws RepositoryException {

        String vendor = (String) properties.get(VENDOR_PROPERTY);
        if (vendor == null || vendor.length() == 0) {
            throw new JDBCRepositoryException(
                    EXCEPTION_LOCALIZER.format("jdbc-missing-vendor"));
        }

        JDBCRepositoryType type = JDBCRepositoryType.getTypeForVendor(vendor);
        if (type == null) {
            throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                    "jdbc-unknown-vendor", vendor));
        }

        return type.getVendor();
    }

    /**
     * Retrieve a Mariner ConnectionPool DataSource.
     *
     * @param properties the Repository properties
     * @param dataSource The DataSource with which the ConnectionPool will be
     *                   constructed.
     * @return A Mariner ConnectionPool DataSource.
     * @throws RepositoryException if there was an error creating the
     *                             ConnectionPool.
     */
    public static DataSource createConnectionPool(
            Map properties,
            DataSource dataSource)
            throws RepositoryException {

        MCSConnectionPoolConfiguration configuration =
                getConnectionPoolConfiguration(factory, properties);

        return factory.createMCSConnectionPool(configuration, dataSource);
    }

    private static MCSConnectionPoolConfiguration getConnectionPoolConfiguration(
            JDBCRepositoryFactory factory, Map properties)
            throws RepositoryException {
        MCSConnectionPoolConfiguration configuration =
                factory.createMCSConnectionPoolConfiguration();

        // Create and configure a Connection pool if there is one.
        String value = (String) properties.get(POOL_ENABLED_PROPERTY);
        configuration.setEnabled("true".equalsIgnoreCase(value));

        int maxConnections;
        int maxFreeConnections;
        //int optimalFreeConnections;
        int minFreeConnections;
        int initialConnections;

        // Get the maximum number of connections allowed.
        try {
            maxConnections =
                    getInt(properties, POOL_MAX_CONNECTIONS_PROPERTY, 10);
        } catch (NumberFormatException nfe) {
            throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                    "jdbc-pool-invalid-max-connections"));
        }

        // Get the maximum number of free connections allowed.
        try {
            maxFreeConnections = getInt(properties,
                    POOL_MAX_FREE_CONNECTIONS_PROPERTY,
                    8);
        } catch (NumberFormatException nfe) {
            throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                    "jdbc-pool-invalid-maxFree-connections"));
        }

        // Get the minimum number of free connections allowed.
        try {
            minFreeConnections = getInt(properties,
                    POOL_MIN_FREE_CONNECTIONS_PROPERTY,
                    2);
        } catch (NumberFormatException nfe) {
            throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                    "jdbc-pool-invalid-minFree-connections"));
        }

        // Get the initial number of free connections created.
        try {
            initialConnections = getInt(properties,
                    POOL_INITIAL_CONNECTIONS_PROPERTY,
                    minFreeConnections);
        } catch (NumberFormatException nfe) {
            throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                    "jdbc-pool-invalid-initial-connections"));
        }

        boolean keepConnectionsAlive = getKeepConnectionsAlive(properties);
        int connectionPollInterval = getConnectionPollInterval(properties);

        configuration.setInitialConnections(initialConnections);
        configuration.setKeepAliveActive(keepConnectionsAlive);
        configuration.setKeepAlivePollInterval(connectionPollInterval);
        configuration.setMaxConnections(maxConnections);
        configuration.setMaxFreeConnections(maxFreeConnections);
        configuration.setMinFreeConnections(minFreeConnections);
        return configuration;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Nov-05	10168/1	ianw	VBM:2005102504 Fixup table definitions

 10-Nov-05	10170/1	ianw	VBM:2005102504 Fixup DB column names

 20-May-05	8365/2	rgreenall	VBM:2005051614 Added Javadoc

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 22-Apr-05	7785/1	emma	VBM:2005041207 Merged from 3.3.0 - bug fix - now retrieves unique values from a JDBCRepository for a particular project (rather than just for the default project)

 22-Apr-05	7783/2	emma	VBM:2005041207 Merged from 3.2.3 - bug fix - now retrieves unique values from a JDBCRepository for a particular project (rather than just for the default project)

 23-Feb-05	7101/1	geoff	VBM:2005020703 Sybase integration

 23-Feb-05	7091/1	geoff	VBM:2005020703 Sybase integration

 23-Feb-05	6905/2	allan	VBM:2005020703 Added support for Sybase

 12-Jan-05	6627/1	geoff	VBM:2005011001 Support Cloudscape 10/Apache Derby as a repository RDBMS (take 2)

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 06-Oct-04	5710/3	geoff	VBM:2004052005 Short column name support

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 30-Sep-04	4511/6	tom	VBM:2004052005 Added short column support for new table columns and cache accessors

 27-May-04	4511/3	tom	VBM:2004052005 Added support for short column names

 06-Aug-04	5123/2	matthew	VBM:2004080401 Add MySQL Support

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 17-May-04	3649/1	mat	VBM:2004031910 Add short tablename support

 08-Apr-04	3653/1	mat	VBM:2004031910 Change accessors to support resolving the tablename from the repository

 04-May-04	4023/2	ianw	VBM:2004032302 Added support for short length tables

 30-Apr-04	4111/1	ianw	VBM:2004042908 Added new Public API to get a local JDBC Repository

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 16-Mar-04	2867/12	ianw	VBM:2004012603 Fixed some rework issues with datasource rationalisation

 09-Mar-04	2867/8	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 11-Mar-04	3376/3	adrian	VBM:2004030908 Rework to fix javadoc duplication

 11-Mar-04	3376/1	adrian	VBM:2004030908 Implemented a fix to release DB connections immediately after use at runtime

 25-Feb-04	3136/1	philws	VBM:2004021908 Remove accessor manager singletons and make MCSDeviceRepositoryProvider and its test case use the runtime device accessor correctly

 19-Feb-04	2789/7	tony	VBM:2004012601 refactored localised logging to synergetics

 16-Feb-04	2789/5	tony	VBM:2004012601 add localised logging and exception services

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Feb-04	3007/3	doug	VBM:2004021103 Ensured the JDBCRepository#unlock method does not throw an IllegaArgumentException

 06-Feb-04	2883/3	claire	VBM:2004020516 Refactoring project code

 06-Feb-04	2883/1	claire	VBM:2004020516 Including project when selecting values

 30-Jan-04	2807/3	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 04-Jan-04	2360/2	andy	VBM:2003121710 added PROJECT column to all tables

 09-Oct-03	1524/1	philws	VBM:2003090101 Port of SQLSTATE handling from PROTEUS

 09-Oct-03	1522/1	philws	VBM:2003090101 Ensure that SQLSTATE codes are matched against class codes only

 27-Jun-03	586/1	byron	VBM:2003062704 Username and password are not used if no connection pooling in mariner-config.xml

 24-Jun-03	497/1	byron	VBM:2003062302 Issues with Database configuring and sql connector

 20-Jun-03	480/1	byron	VBM:2003062006 Fixed ClassCastException

 12-Jun-03	316/3	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
