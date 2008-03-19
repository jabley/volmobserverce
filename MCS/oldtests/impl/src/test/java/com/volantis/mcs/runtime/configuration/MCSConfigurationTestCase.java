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

package com.volantis.mcs.runtime.configuration;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Test the {@link MCSConfiguration} class.  This makes no effort to exploit
 * the underlying {@link MarinerConfiguration} object that is delegated to
 * for actual data values.  These tests are purely for the logic contained
 * within the {@link MCSConfiguration} class.
 */
public class MCSConfigurationTestCase extends TestCaseAbstract {

    /**
     * Test the creation and initialization of MCSConfiguration instances.
     */
    public void testCreation() throws Exception {
        MCSConfiguration testConfig;

        // Test a null construction
        try {
            testConfig = new MCSConfiguration(null);
            fail("Null initialisation should throw an exception");
        } catch (IllegalArgumentException iae) {
            // Test succeeded :-)
        }

        // And test a valid construction
        MarinerConfiguration data = new MarinerConfiguration();
        testConfig = new MCSConfiguration(data);
        assertNotNull("Object should have been created successfully",
                      testConfig);
        MarinerConfiguration actualData =
                (MarinerConfiguration) PrivateAccessor.getField(testConfig,
                                                                "config");
        assertSame("The config objects should be the same", data, actualData);
    }

    /**
     * Test the {@link MCSConfiguration#resolveDataSources} method with
     * minimal configuration
     */
    public void testMinimalResolveDataSources() throws Exception {
        // Setup the necessary basic classes
        MarinerConfiguration data = new MarinerConfiguration() {
            public LocalRepositoryConfiguration getLocalRepository() {
                return new LocalRepositoryConfiguration();
            }

            public DataSourcesConfiguration getDataSourcesConfiguration() {
                DataSourcesConfiguration config =
                        new DataSourcesConfiguration();
                return config;
            }
        };
        MCSConfiguration testConfig = new MCSConfiguration(data);

        // This should execute without any problems
        testConfig.resolveDataSources();
    }

    /**
     * Test the {@link MCSConfiguration#resolveDataSources} method with
     * global data sources configuration
     */
    public void testDataResolveDataSources() throws Exception {
        // Setup some test data
        List dataSourceConfigs = new ArrayList();
        // Not all implementations of AnonymousDataSource are used as test
        // data here because of the test setup that would be required for them
        // to function (at all!).  The classes used in this test data provide
        // good coverage of the code under test anyway.
        dataSourceConfigs.add(
                createDataSource(JDBCDriverConfiguration.class));
        dataSourceConfigs.add(
                createDataSource(AnonymousDataSourceConfiguration.class));
        dataSourceConfigs.add(
                createDataSource(DataSourceConfiguration.class));

        // And create a final version for use in the anonymous class
        final List namedDataSources =
                Collections.unmodifiableList(dataSourceConfigs);

        // Create a suitable configuration object
        MarinerConfiguration data = new MarinerConfiguration() {
            // JavaDoc inherited
            public LocalRepositoryConfiguration getLocalRepository() {
                return new LocalRepositoryConfiguration();
            }

            // JavaDoc inherited
            public JNDIConfiguration getJndiConfiguration() {
                JNDIConfiguration config = new JNDIConfiguration() {
                    public InitialContextConfiguration
                            getInitialContext(String context) {
                        InitialContextConfiguration initial =
                                new InitialContextConfiguration() {
                            public Map getParameters() {
                                return null;
                            }
                        };
                        return initial;
                    }
                };

                return config;
            }

            // JavaDoc inherited
            public DataSourcesConfiguration getDataSourcesConfiguration() {
                DataSourcesConfiguration config =
                        new DataSourcesConfiguration() {
                            public List getNamedDataSources() {
                                return namedDataSources;
                            }
                        };
                return config;
            }
        };
        MCSConfiguration testConfig = new MCSConfiguration(data);

        // This should execute without any problems
        testConfig.resolveDataSources();
    }

   

    /**
     * Test the {@link MCSConfiguration#resolveDataSources} method with
     * local repository configuration
     */
    public void testLocalResolveDataSources() throws Exception {
        // Create a test setup
        MarinerConfiguration data = new MarinerConfiguration() {
            // JavaDoc inherited
            public LocalRepositoryConfiguration getLocalRepository() {
                LocalRepositoryConfiguration config =
                        new LocalRepositoryConfiguration() {
                    // JavaDoc inherited
                    public JDBCRepositoryConfiguration
                            getJDBCRepositoryConfiguration() {
                        JDBCRepositoryConfiguration jdbc =
                                new JDBCRepositoryConfiguration() {
                            // JavaDoc inherited
                            public AnonymousDataSource
                                    getDataSourceConfiguration() {
                                JDBCDriverConfiguration driver =
                                        new JDBCDriverConfiguration();
                                driver.setDriverClass(
                                        "com.volantis.mcs.runtime.configuration.FakeJDBCDriver");
                                driver.setDatabaseURL(
                                        "jdbc:fake:database");
                                return driver;
                            }
                        };
                        return jdbc;
                    }
                };
                return config;
            }
        };
        MCSConfiguration testConfig = new MCSConfiguration(data);

        // This should execute without any problems
        testConfig.resolveDataSources();
    }

    /**
     * A utility method that creates data sources with configurations as
     * specified by the <code>dataSourceType</code> provided.
     *
     * @param dataSourceType The anonymous data source instance that should
     *                       be used in initialising the data source
     *
     * @return An initialised named data source configuration
     */
    private NamedDataSourceConfiguration createDataSource(Class dataSourceType)
            throws Exception {
        NamedDataSourceConfiguration namedData =
                new NamedDataSourceConfiguration();

        namedData.setConnectAtStartUp(Boolean.TRUE);
        namedData.setName("TestDataSource");

        AnonymousDataSource dataSource =
                (AnonymousDataSource) dataSourceType.newInstance();

        // Specialise the data source with appropriate values
        if (dataSource instanceof JDBCDriverConfiguration) {
            ((JDBCDriverConfiguration)dataSource).setDriverClass(
                    "com.volantis.mcs.runtime.configuration.FakeJDBCDriver");
            ((JDBCDriverConfiguration)dataSource).setDatabaseURL(
                    "jdbc:fake:database");
        } else if (dataSource instanceof AnonymousDataSourceConfiguration) {
            ((AnonymousDataSourceConfiguration)dataSource).
                    setDataSourceConfiguration(createAnonymousDataSource());
            ((AnonymousDataSourceConfiguration)dataSource).setPassword("pwd");
            ((AnonymousDataSourceConfiguration)dataSource).setUser("user");
        } else if (dataSource instanceof DataSourceConfiguration) {
            ((DataSourceConfiguration)dataSource).setRef("Test Reference");
        }
        namedData.setDataSourceConfiguration(dataSource);

        return namedData;
    }

    /**
     * A utility to create an instance of a JDBC driver configuration object
     * and return it as an <code>AnonymousDataSource</code>.  This is intended
     * for making the writing of tests simpler hence the lack of flexibility in
     * the instance returned and its values.
     *
     * @return An initialised {@link JDBCDriverConfiguration}
     */
    private AnonymousDataSource createAnonymousDataSource() {
        JDBCDriverConfiguration dataSource = new JDBCDriverConfiguration();
        dataSource.setDriverClass("com.volantis.mcs.runtime.configuration.FakeJDBCDriver");
        dataSource.setDatabaseURL("jdbc:fake:database");
        return dataSource;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/5	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Sep-04	5466/1	claire	VBM:2004090905 New Build Mechanism: Refactor business logic out of MarinerConfiguration

 ===========================================================================
*/
