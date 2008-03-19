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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.api.devices;

import com.volantis.mcs.devices.jdbc.JDBCDeviceRepositoryConfiguration;
import com.volantis.mcs.devices.DeviceRepositoryFactory;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.mcs.repository.jdbc.DriverDataSource;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryType;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.sql.DataSource;

/**
 * A factory class for providing access to XML or JDBC repositories via
 * a url.
 *
 * The DeviceRepositoryFactory unfortunately does not already contain the
 * facilities provided by SimpleDeviceRepositoryFactory and unfortunately it
 * has a method - getDeviceRepository(URL) - that only provides XML device
 * repositories despite being generally named and having a mechanism (the
 * URL) for specifying the repository that could be used for either XML or
 * JDBC repositories and perhaps more.
 *
 * Note that this class may be used by non-MCS Volantis products - in fact
 * this is one of the reasons for the creation of this class so that the
 * code can be reused.
 *
 * Also note that although jdbc urls are standard as far as JDBC and its
 * implementations are concerned (according to Sun anyway) they are not
 * standard urls as defined by W3C. This means they cannot be handled by either
 * URL or MarinerURL.
 *
 * @volantis-api-include-in InternalAPI
 */
public class SimpleDeviceRepositoryFactory {
    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(SimpleDeviceRepositoryFactory.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.
            createExceptionLocalizer(SimpleDeviceRepositoryFactory.class);

    private static final String INVALID_JDBC_URL_KEY =
            "device-repository-invalid-jdbc-url";

    private static final String JDBC_URL_MESSAGE =
            "jdbc:<sub-protocol>:<sub-name>?[username=" +
            "\"<username>\" [password=\"<password>\"]] " +
            "defaultproject=\"<default project>\"";
    /**
     * The name of the URL parameter representing the username for jdbc
     * urls. The value of this constant is "username".
     */
    public static final String JDBC_USERNAME = "username";

    /**
     * The name of the URL parameter representing the password for jdbc
     * urls. The value of this constant is "password".
     */
    public static final String JDBC_PASSWORD = "password";

    /**
     * The name of the URL parameter representing the default project for
     * jdbc urls. The value of this constant is "defaultproject".
     */
    public static final String JDBC_DEFAULT_PROJECT = "defaultproject";

    /**
     * The name of the scheme for a jdbc repository.
     */
    private static final String JDBC_SCHEME = "jdbc";

    /**
     * The name of the schema for an xml repository.
     */
    private static final String XML_SCHEME = "file";

    /**
     * The underlying DeviceRepositoryFactory that will be used to do the
     * actual DeviceRepository creation.
     */
    private DeviceRepositoryFactory deviceRepositoryFactory =
            DeviceRepositoryFactory.getDefaultInstance();

    private JDBCRepositoryFactory jdbcRepositoryFactory =
            JDBCRepositoryFactory.getDefaultInstance();

    /**
     * Flag to indicate that known JDBC driver classes have been loaded.
     */
    private boolean driverClassesLoaded;

    /**
     * Create and return the DeviceRepository specified by a url.
     *
     * This method will provide a jdbc repository where the url has a
     * jdbc scheme and an xml repository where the url has a file schema. Only
     * jdbc and file schemes are supported.
     *
     * If a username and password is required for a jdbc repository these
     * should be passed in as parameters on the URL where the parameter
     * names match the appropriate constants defined in this class.
     *
     * If a jdbc repository is requested a default project name must be
     * specified as a url parameter using the appropriate constant defined in
     * this class.
     *
     * In JDBC there is no distinction between the device repository and
     * policies repository i.e there is a single location/url for both.
     * Whereas with XML repositories the device repositories and policies
     * are represented by separate file paths. The path needed here for
     * an XML device repository is the path to the device repository file
     * (i.e. .mdpr file).
     *
     * @param repositoryURL the url of the device repository
     * @return the DeviceRepository specified by the url
     * @throws DeviceRepositoryException if there was a problem creating the
     * DeviceRepository.
     */
    public DeviceRepository createDeviceRepository(String repositoryURL)
            throws DeviceRepositoryException {
        return createDeviceRepository(repositoryURL, null);
    }

    /**
     * Create and return the DeviceRepository specified by a url.
     *
     * This method will provide a jdbc repository where the url has a
     * jdbc scheme and an xml repository where the url has a file schema. Only
     * jdbc and file schemes are supported.
     *
     * If a username and password is required for a jdbc repository these
     * should be passed in as parameters on the URL where the parameter
     * names match the appropriate constants defined in this class.
     *
     * If a jdbc repository is requested a default project name must be
     * specified as a url parameter using the appropriate constant defined in
     * this class.
     *
     * In JDBC there is no distinction between the device repository and
     * policies repository i.e there is a single location/url for both.
     * Whereas with XML repositories the device repositories and policies
     * are represented by separate file paths. The path needed here for
     * an XML device repository is the path to the device repository file
     * (i.e. .mdpr file).
     *
     * @param repositoryURL the url of the device repository
     * @param unknownDevicesLogFileName the log file name for unknown/abstract
     * devices, null is accepted
     * @return the DeviceRepository specified by the url
     * @throws DeviceRepositoryException if there was a problem creating the
     * DeviceRepository.
     */
    public DeviceRepository createDeviceRepository(
                final String repositoryURL,
                final String unknownDevicesLogFileName)
            throws DeviceRepositoryException {
        if (repositoryURL == null) {
            throw new IllegalArgumentException("Cannot be null: repositoryURL");
        }

        DeviceRepository deviceRepository = null;
        if (repositoryURL.startsWith(JDBC_SCHEME)) {
            deviceRepository = createJDBCDeviceRepository(repositoryURL,
                unknownDevicesLogFileName);
        } else if (repositoryURL.startsWith(XML_SCHEME)) {
            try {
                deviceRepository = deviceRepositoryFactory.getDeviceRepository(
                    new URL(repositoryURL), unknownDevicesLogFileName);
            } catch (MalformedURLException e) {
                throw new DeviceRepositoryException(e.getMessage(), e);
            }
        } else {
            String scheme = "null";
            if(repositoryURL.indexOf(":")!=-1) {
                scheme = repositoryURL.substring(0,
                        repositoryURL.indexOf(":"));
            }
            String args [] = {repositoryURL, scheme};
            throw new DeviceRepositoryException(EXCEPTION_LOCALIZER.
                    format("device-repository-scheme-unsupported", args));
        }

        return deviceRepository;
    }

    /**
     * Create a JDBC device repository using the creation methods available
     * in the DeviceRepositoryFactory. The DataSource used will use a username
     * and password for the repository if they were provided in the url.
     * @param repositoryURL the jdbc url that describes the repository location
     * @param unknownDevicesLogFileName the log file name for unknown/abstract
     * devices, null is accepted
     */
    private DeviceRepository createJDBCDeviceRepository(
                final String repositoryURL,
                final String unknownDevicesLogFileName)
            throws DeviceRepositoryException {
        ensureDriverClassesLoaded();
        JDBCDeviceRepositoryConfiguration configuration =
                deviceRepositoryFactory.createJDBCDeviceRepositoryConfiguration();

        Map params = getJDBCURLParams(repositoryURL);
        String userName = (String) params.remove(JDBC_USERNAME);
        String password = (String) params.remove(JDBC_PASSWORD);
        String defaultProject = (String) params.remove(JDBC_DEFAULT_PROJECT);

        if (defaultProject == null) {
            throw new DeviceRepositoryException(EXCEPTION_LOCALIZER.
                    format(INVALID_JDBC_URL_KEY, new String [] {
                        repositoryURL, JDBC_URL_MESSAGE}));
        }

        // remove the processed parameters
        int paramStart = repositoryURL.indexOf("?");
        final String baseURL;
        if (paramStart != -1) {
            baseURL = repositoryURL.substring(0, paramStart);
        } else {
            baseURL = repositoryURL;
        }
        final StringBuffer baseURLBuffer = new StringBuffer(baseURL);
        boolean first = true;
        for (Iterator iter = params.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry entry = (Map.Entry) iter.next();
            if (first) {
                baseURLBuffer.append('?');
                first = false;
            } else {
                baseURLBuffer.append('&');
            }
            final String key = (String) entry.getKey();
            baseURLBuffer.append(key);
            final String value = (String) entry.getValue();
            if (value != null) {
                baseURLBuffer.append('=');
                baseURLBuffer.append(value);
            }
        }

        DataSource dataSource = new DriverDataSource(baseURLBuffer.toString());

        if (userName != null) {
            // Since we have a username lets use a confusingly
            // named AnonymousDataSource.
            dataSource = jdbcRepositoryFactory.createAnonymousDataSource(
                    dataSource, userName, password);
        }
        configuration.setDataSource(dataSource);
        configuration.setDefaultProject(defaultProject);
        configuration.setUnknownDevicesLogFileName(unknownDevicesLogFileName);

        return deviceRepositoryFactory.createDeviceRepository(configuration);
    }

    /**
     * Parse a jdbc url to get the parameters and add them to a Map.
     * @param jdbcURL the jdbc url
     * @return a map of name to value parameter mappings or an empty Map if
     * there were no parameters.
     */
    private Map getJDBCURLParams(String jdbcURL) {
        Map map = Collections.EMPTY_MAP;
        int paramStart = jdbcURL.indexOf("?");
        if (paramStart != -1 && paramStart != jdbcURL.length()) {
            map = new HashMap();
            String queryString = jdbcURL.substring(paramStart + 1);
            StringTokenizer apmTokenizer =
                    new StringTokenizer(queryString, "&");
            while (apmTokenizer.hasMoreTokens()) {
                String nameValuePair = apmTokenizer.nextToken();
                if (null != nameValuePair) {
                    final String[] param = nameValuePair.split("=", 2);
                    final String name = param[0];
                    final String value;
                    if (param.length == 2) {
                        value = URLDecoder.decode(param[1]);
                    } else if (name.equals(nameValuePair)) {
                        // no equals sign
                        value = null;
                    } else {
                        // equals sign, but empty value
                        value = "";
                    }
                    map.put(name, value);
                }
            }
        }

        return map;
    }


    /**
     * Load all the JDBC driver classes known to MCS if they have not been
     * loaded already. This allows a DriverManager to be used without
     * having to know what kind of JDBC database is being used. Note that
     * although only MCS supported repositories are loaded here it is
     * still possible for a user to use their own driver (e.g. for a JDBC
     * database that is not supported by MCS). To do this they must set their
     * their jdbc.drivers system property - see DriverManager class javadoc
     * for more details.
     */
    private void ensureDriverClassesLoaded() {
        if (!driverClassesLoaded) {
            Iterator repositoryTypes = JDBCRepositoryType.iterator();
            while (repositoryTypes.hasNext()) {
                JDBCRepositoryType type = (JDBCRepositoryType)
                        repositoryTypes.next();
                try {
                    Class.forName(type.getDriverClassName());
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Loaded JDBC driver " +
                                type.getDriverClassName());
                    }
                } catch (ClassNotFoundException e) {
                    if (LOGGER.isDebugEnabled()) {
                        // Make this log on info so that we do not receive
                        // this every time we fail to find a driver - which
                        // will be almost all of the time. Use info so that
                        // we can use a localized message enabling the user
                        // to debug.
                        LOGGER.info("cannot-load-driver",
                                type.getDriverClassName());
                    }
                }
            }
            driverClassesLoaded = true;
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-May-05	8198/1	allan	VBM:2005050902 Make bundle license failure respond with internal server error

 25-Apr-05	7679/1	allan	VBM:2005041320 SmartClient Packager - minimal testing

 18-Apr-05	7692/1	allan	VBM:2005041504 SimpleDeviceRepositoryFactory - create a dev rep from a url

 ===========================================================================
*/
