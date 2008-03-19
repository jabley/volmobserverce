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

package com.volantis.mcs.devices;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.testtools.application.AppConfigurator;
import com.volantis.mcs.testtools.application.MandatoryAppConfigurator;
import com.volantis.synergetics.testtools.Executor;
import com.volantis.synergetics.testtools.HypersonicManager;
import com.volantis.testtools.config.ConfigFileBuilder;
import com.volantis.testtools.config.ConfigProjectPoliciesJdbcValue;
import com.volantis.testtools.config.ConfigValue;
import com.volantis.synergetics.testtools.servletunit.ServletRunner;
import com.volantis.synergetics.testtools.servletunit.ServletTestCase;
import com.volantis.synergetics.testtools.servletunit.ServletUnitClient;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SecondaryIDHeaderTestCase
 *
 */
public class SecondaryIDHeaderTestCase extends ServletTestCase {

    private HypersonicManager hypersonicManager;

    /** Used to supply context parameter for mcs-config.xml to MCS */
    private InputStream webXMLInputStream;

    /**
     * Create the test case
     * @param name the name of the test case
     */
    public SecondaryIDHeaderTestCase(String name) {
        super(name);
    }

    /**
     * Set up the bean and an empty ServletContext.
     */
    public void setUp()
            throws ClassNotFoundException, SQLException, IOException {
        // NOTE: using the default file source, leaves background threads around...
        hypersonicManager = new HypersonicManager();
        webXMLInputStream =
                SecondaryIDHeaderTestCase.class.getResourceAsStream("web.xml");
    }

    /**
     * Destroy the objects created by the previous set up.
     */
    public void tearDown() throws SQLException, IOException {
        webXMLInputStream.close();
    }


    /**
     * Resolve the PC device which has a valid secondary ID header mapping to
     * the Wibble device. The Wibble device should be returned.
     */
    public void testSecondaryIDMapping() throws Exception {

        ServletRunner servletRunner = new ServletRunner(webXMLInputStream);
        servletRunner.registerServlet("MyServlet",
                "com.volantis.mcs.devices.ResolveDeviceServlet");

        final ServletUnitClient servletClient = servletRunner.newClient();

        final WebRequest request = new PostMethodWebRequest(
                "http://test.colantis.com/MyServlet");
        request.setHeaderField("User-Agent", "Mozilla/5.0 blah blah blah");
        request.setHeaderField("host", "www.volantis.com:8080");
        request.setHeaderField("Accept", "text/html");
        request.setHeaderField("HeaderName", "HeaderValue");

        ConfigValue cv = new ConfigValue();
        AppConfigurator ac = new MyAppConfigurator();
        ac.setUp(cv);
        try {
            ConfigFileBuilder configFileBuilder = new ConfigFileBuilder();
            configFileBuilder.buildConfigDocument(cv);
            servletRunner.setRealPath(configFileBuilder.getConfigFileDir());

            hypersonicManager.useCleanupWith(new Executor() {
                public void execute() throws Exception {
                    createRepository();

                    WebResponse response = servletClient.getResponse(request);
                    assertEquals("Response Not as expected.",
                            "Device is Wibble", response.getText());
                }
          });
        } finally {
            ac.tearDown(cv);
            // Need to shut down volantis or we leave a connection pool running.
            Volantis.getInstance().shutdown();
        }
    }

    /**
     * Resolve the PC device which has a valid secondary ID header mapping to
     * the Wibble device. The Wibble device should be returned.
     */
    public void testCachedSecondaryIDMapping() throws Exception {

        ServletRunner servletRunner = new ServletRunner(webXMLInputStream);
        servletRunner.registerServlet("MyServlet",
                "com.volantis.mcs.devices.ResolveDeviceServlet");

        final ServletUnitClient servletClient = servletRunner.newClient();

        final WebRequest request = new PostMethodWebRequest(
                "http://test.colantis.com/MyServlet");
        request.setHeaderField("User-Agent", "Mozilla/5.0 blah blah blah");
        request.setHeaderField("host", "www.volantis.com:8080");
        request.setHeaderField("Accept", "text/html");
        request.setHeaderField("HeaderName", "HeaderValue");

        ConfigValue cv = new ConfigValue();
        AppConfigurator ac = new MyAppConfigurator();
        ac.setUp(cv);
        try {
            ConfigFileBuilder configFileBuilder = new ConfigFileBuilder();
            configFileBuilder.buildConfigDocument(cv);
            servletRunner.setRealPath(configFileBuilder.getConfigFileDir());

            hypersonicManager.useCleanupWith(new Executor() {
                public void execute() throws Exception {
                    createRepository();

                    WebResponse response = servletClient.getResponse(request);
                    assertEquals("Response Not as expected.",
                            "Device is Wibble", response.getText());
                    response = servletClient.getResponse(request);
                    assertEquals("Response Not as expected.",
                            "Device is Wibble", response.getText());

                    response = servletClient.getResponse(request);
                    assertEquals("Response Not as expected.",
                            "Device is Wibble", response.getText());
                }
            });
        } finally {
            ac.tearDown(cv);
            // Need to shut down volantis or we leave a connection pool running.
            Volantis.getInstance().shutdown();
        }
    }

    /**
     * Resolve the Test device. This is a valid device with no secondary ID header
     * defined so this should resolve as normal from the user agent alone.
     */
    public void testNoSecondaryIDMapping() throws Exception {

        ServletRunner servletRunner = new ServletRunner(webXMLInputStream);
        servletRunner.registerServlet( "MyServlet",
                                       "com.volantis.mcs.devices.ResolveDeviceServlet" );

        final ServletUnitClient servletClient = servletRunner.newClient();

        final WebRequest request = new PostMethodWebRequest(
                            "http://test.colantis.com/MyServlet" );
        request.setHeaderField( "User-Agent", "Mozilla/6.0 blah blah blah" );
        request.setHeaderField( "host", "www.volantis.com:8080" );
        request.setHeaderField( "Accept", "text/html");
        request.setHeaderField( "HeaderName", "HeaderValue");

        // todo: this should be using AppManager instead of the code below
        ConfigValue cv = new ConfigValue();
        AppConfigurator ac = new MyAppConfigurator();
        ac.setUp( cv );
        try {
            ConfigFileBuilder configFileBuilder = new ConfigFileBuilder();
            configFileBuilder.buildConfigDocument(cv);
            servletRunner.setRealPath(configFileBuilder.getConfigFileDir());

            hypersonicManager.useCleanupWith(new Executor() {
                public void execute() throws Exception {
                    createRepository();

                    WebResponse response = servletClient.getResponse(request);
                    assertEquals("Response Not as expected.",
                        "Device is Test", response.getText());
                }
            });
        } finally {
            ac.tearDown(cv);
            // Need to shut down volantis or we leave a connection pool running.
            Volantis.getInstance().shutdown();
        }
    }

    /**
     * Resolve a device with an invalid secondary ID header name. In this instance
     * the header does not exist in the request so the device mapped from the user
     * agent should be returned.
     */
    public void testInvalidSecondaryIDMapping() throws Exception {

        ServletRunner servletRunner = new ServletRunner(webXMLInputStream);
        servletRunner.registerServlet( "MyServlet",
                                       "com.volantis.mcs.devices.ResolveDeviceServlet" );

        final ServletUnitClient servletClient = servletRunner.newClient();

        final WebRequest request = new PostMethodWebRequest(
                            "http://test.colantis.com/MyServlet" );
        request.setHeaderField( "User-Agent", "Mozilla/7.0 blah blah blah" );
        request.setHeaderField( "host", "www.volantis.com:8080" );
        request.setHeaderField( "Accept", "text/html");
        request.setHeaderField( "HeaderName", "HeaderValue");

        // todo: this should be using AppManager instead of the code below
        ConfigValue cv = new ConfigValue();
        AppConfigurator ac = new MyAppConfigurator();
        ac.setUp( cv );
        try {
            ConfigFileBuilder configFileBuilder = new ConfigFileBuilder();
            configFileBuilder.buildConfigDocument(cv);
            servletRunner.setRealPath(configFileBuilder.getConfigFileDir());
            hypersonicManager.useCleanupWith(new Executor() {
                public void execute() throws Exception {
                    createRepository();

                    WebResponse response = servletClient.getResponse(request);
                    assertEquals("Response Not as expected.",
                        "Device is Invalid", response.getText());
                }
            });
        } finally {
            ac.tearDown(cv);
            // Need to shut down volantis or we leave a connection pool running.
            Volantis.getInstance().shutdown();
        }
    }


    /** Create a dummy database */
    class MyAppConfigurator extends MandatoryAppConfigurator {
        public static final String SOURCE = "hypersonic-db";
        // Inherit Javadoc.
        public void setUp(ConfigValue value) throws Exception {
            super.setUp(value);

            value.repositoryType = "odbc";
            value.repositoryUser = "sa";
            value.repositoryPassword = "";
            value.repositoryVendor = "hypersonic";
            value.repositorySource = SOURCE;
            value.repositoryHost = "haddock";
            value.repositoryPort = new Integer( 1526 );

            ConfigProjectPoliciesJdbcValue jdbcPolicies =
                    new ConfigProjectPoliciesJdbcValue();
            jdbcPolicies.projectName = "#DefaultProject";
            value.defaultProjectPolicies = jdbcPolicies;
            value.standardJDBCDeviceRepositoryProject =
                    jdbcPolicies.projectName;

            value.sessionProxyCookieMappingEnabled = Boolean.FALSE;

            // Dont know why these are needed but we crash without them.
            value.repositoryDbPoolMax = new Integer(999);
            value.repositoryKeepConnectionsAlive = Boolean.TRUE;
            value.repositoryConnectionPollInterval = new Integer(60);
        }
    }

    /** Build a repository */
    private void createRepository()
            throws ClassNotFoundException, SQLException {

        try {
            Class.forName("org.hsqldb.jdbcDriver");
        }
        catch( ClassNotFoundException cnfe ){
            System.err.println( "Unable to load org.hsqldb.jdbcDriver" );
            return;
        }
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:" +
                MyAppConfigurator.SOURCE, "sa", "" );
        createDevicePatterns( conn );
        createPolicyValues( conn );
        createDeviceTACs(conn);
        conn.close();
    }


    /** Create the device patterns table */
    private void createDevicePatterns( Connection conn ) throws SQLException
    {
        Statement st = conn.createStatement();
        st.execute( "create table VMDEVICE_PATTERNS (" +
                    " PROJECT VARCHAR(255)," +
                    " NAME VARCHAR(20)," +
                    " PATTERN VARCHAR(255)," +
                    " REVISION INTEGER DEFAULT 0," +
                    " CONSTRAINT PK_VMDEVICEPATTERNS " +
                    " PRIMARY KEY ( NAME, PATTERN ) )" ) ;
        st.execute( "insert into VMDEVICE_PATTERNS values (" +
                    "'#DefaultProject','PC','Mozilla/5.*',0)" );
        st.execute( "insert into VMDEVICE_PATTERNS values (" +
                    "'#DefaultProject','Wibble','HeaderName: HeaderValue PC',0)" );
        st.execute( "insert into VMDEVICE_PATTERNS values (" +
                    "'#DefaultProject','Test','Mozilla/6.*',0)" );
        st.execute( "insert into VMDEVICE_PATTERNS values (" +
                    "'#DefaultProject','Invalid','Mozilla/7.*',0)" );
        st.close();
    }

    /**
     * Create the TAC table
     *
     * @param conn The connection to create the TAC table on.
     * @throws SQLException if an error occurs accessing the database.
     */
    private void createDeviceTACs(Connection conn) throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            st.execute("CREATE TABLE VMDEVICE_TACS (" +
                    " PROJECT VARCHAR(255)," +
                    " NAME VARCHAR(20)," +
                    " TAC NUMERIC(8)," +
                    " CONSTRAINT PK_VMDEVICETACS " +
                    " PRIMARY KEY ( NAME, PROJECT, TAC ) " +
                    ")");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /** Create the Policy values */
    private void createPolicyValues( Connection conn ) throws SQLException
    {
        Statement st = conn.createStatement();
        st.execute( "CREATE TABLE VMPOLICY_VALUES ( " +
                    "PROJECT VARCHAR(255) NOT NULL, " +
                    "NAME VARCHAR(20) NOT NULL, " +
                    "POLICY VARCHAR(200) NOT NULL, " +
                    "VALUE VARCHAR(1024), " +
                    "REVISION INTEGER DEFAULT 0, " +
                    "CONSTRAINT PK_VMPOLICYVALUES " +
                    "PRIMARY KEY ( NAME, POLICY ) )" );

        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','PC','rendermode','rgb',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','PC','pixelsy','550',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','PC','pixelsx','750',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','PC','height','200',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','PC','fullpixelsy','600',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','PC','fullpixelsx','800',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','PC','numpalette','4000000',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','PC','pixeldepth','24',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','PC','protocol','HTMLVersion4_0',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','PC','sec.id.header','HeaderName',0)" );

        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Invalid','rendermode','rgb',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Invalid','pixelsy','550',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Invalid','pixelsx','750',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Invalid','height','200',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Invalid','fullpixelsy','600',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Invalid','fullpixelsx','800',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Invalid','numpalette','4000000',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Invalid','pixeldepth','24',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Invalid','protocol','HTMLVersion4_0',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Invalid','sec.id.header','DoesNotExist',0)" );


        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Wibble','rendermode','rgb',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Wibble','pixelsy','550',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Wibble','pixelsx','750',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Wibble','height','200',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Wibble','fullpixelsy','600',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Wibble','fullpixelsx','800',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Wibble','numpalette','4000000',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Wibble','pixeldepth','24',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Wibble','protocol','HTMLVersion4_0',0 )" );

        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Test','rendermode','rgb',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Test','pixelsy','550',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Test','pixelsx','750',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Test','height','200',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Test','fullpixelsy','600',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Test','fullpixelsx','800',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Test','numpalette','4000000',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Test','pixeldepth','24',0 )" );
        st.execute( "insert into VMPOLICY_VALUES values (" +
                    "'#DefaultProject','Test','protocol','HTMLVersion4_0',0 )" );
        st.close();

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 13-Mar-05	6842/4	emma	VBM:2005020302 Make all file/resource references in config files relative to that file

 11-Mar-05	6842/2	emma	VBM:2005020302 Making file references in config files relative to those files

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 26-Aug-04	5294/1	geoff	VBM:2004082405 Reduce unnecessary background threads in testsuite

 10-Aug-04	5147/1	adrianj	VBM:2004080318 Added support for TAC values to importer

 18-May-04	3649/1	mat	VBM:2004031910 Add short tablename support

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 30-Jan-04	2807/1	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 12-Jan-04	2360/6	andy	VBM:2003121710 fixed conflict

 04-Jan-04	2360/3	andy	VBM:2003121710 added PROJECT column to all tables

 06-Jan-04	2271/1	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 02-Nov-03	1593/1	mat	VBM:2003101502 Changed to use HypersonicManager

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 02-Sep-03	1290/3	steve	VBM:2003082105 Secondary ID Header implementation

 02-Sep-03	1290/1	steve	VBM:2003082105 Secondary ID Header implementation

 ===========================================================================
*/
