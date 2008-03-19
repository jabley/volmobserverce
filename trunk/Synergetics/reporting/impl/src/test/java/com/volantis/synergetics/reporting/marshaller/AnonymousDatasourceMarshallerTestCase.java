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
package com.volantis.synergetics.reporting.marshaller;

import com.volantis.synergetics.reporting.config.DataSourceType;
import com.volantis.synergetics.reporting.config.DatasourceConfiguration;
import com.volantis.synergetics.reporting.config.InternalPoolDatasource;
import com.volantis.synergetics.reporting.config.JDBCDatasource;
import com.volantis.synergetics.reporting.config.JNDIDatasource;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;


/**
 * Anonymous marshaller tests.
 */
public class AnonymousDatasourceMarshallerTestCase extends TestCase {

    /**
     * Parser. 
     */
    public class JIBXTestDatasourceConfigurationParser {

        /**
         * Parses given stream.
         * 
         * @param inputStream input stream
         * @return test element
         */
        public TestAnonymousMarshallerElement parse(InputStream inputStream) {
            try {
                IBindingFactory bfact = BindingDirectory
                        .getFactory(TestAnonymousMarshallerElement.class);
                IUnmarshallingContext uctx = bfact.createUnmarshallingContext();

                return (TestAnonymousMarshallerElement) uctx.unmarshalDocument(
                        inputStream, null);
            } catch (JiBXException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Writes to given output stream.
         * 
         * @param config datasource
         * @param stream output stream
         */
        public void write(DatasourceConfiguration config, OutputStream stream) {
            try {
                IBindingFactory bfact = BindingDirectory
                    .getFactory(TestAnonymousMarshallerElement.class);
                IMarshallingContext mctx = bfact.createMarshallingContext();
                mctx.setIndent(4, null, ' ');
                mctx.marshalDocument(config, "UTF-8", null, stream);
            } catch (JiBXException e) {
                throw new RuntimeException(e);
            }
        }
    }    
    
    /**
     * Configurations
     */
    private String ANONYMOUS_JDBC_DATASOURCE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<test-element-anon>\n" +
        "    <jdbc-datasource>\n" +
        "        <driver-class>org.gjt.mm.mysql.Driver</driver-class>\n" +
        "        <connection-string>jdbc:mysql://127.0.0.1/logging?user=logger&amp;password=logpass&amp;database=logging</connection-string>\n" +
        "    </jdbc-datasource>\n" + 
        "</test-element-anon>";

    private String ANONYMOUS_INTERNAL_POOL_DATASOURCE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<test-element-anon>\n" +
        "    <internal-pool-datasource>\n" +
        "        <driver-class>org.gjt.mm.mysql.Driver</driver-class>\n" +
        "        <url>jdbc:mysql://127.0.0.1/logging?database=logging</url>\n" +
        "        <username>db_user</username>\n" +
        "        <password>db_password</password>\n" +
        "        <max-active>30</max-active>\n" +
        "        <max-idle>10</max-idle>\n" +
        "        <max-wait>5</max-wait>\n" +
        "    </internal-pool-datasource>\n" +
        "</test-element-anon>";

    private String ANONYMOUS_INTERNAL_POOL_DATASOURCE_EMPTY_PASSWORD = 
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<test-element-anon>\n" +
    "    <internal-pool-datasource>\n" +
    "        <driver-class>org.gjt.mm.mysql.Driver</driver-class>\n" +
    "        <url>jdbc:mysql://127.0.0.1/logging?database=logging</url>\n" +
    "        <username>db_user</username>\n" +
    "        <password></password>\n" +
    "        <max-active>30</max-active>\n" +
    "        <max-idle>10</max-idle>\n" +
    "        <max-wait>5</max-wait>\n" +
    "    </internal-pool-datasource>\n" +
    "</test-element-anon>";

    private String ANONYMOUS_JNDI_DATASOURCE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<test-element-anon>\n" +
        "    <jndi-datasource>\n" +
        "        <jndi-name>jdbc/TestDB</jndi-name>\n" +
        "    </jndi-datasource>\n" +
        "</test-element-anon>";

    /**
     * Test unmarshalling of a JDBC datasource
     */
    public void testJDBCUnmarshalling() throws Exception {
        
        DatasourceConfiguration datasource = parseDatasource(
                new ByteArrayInputStream(ANONYMOUS_JDBC_DATASOURCE.getBytes()));

        assertEquals(DataSourceType.JDBC_DATASOURCE, datasource.getType());
        
        JDBCDatasource jdbcDatasource = (JDBCDatasource) datasource;
        
        assertEquals("org.gjt.mm.mysql.Driver", jdbcDatasource.getDriverClass());
        assertEquals("jdbc:mysql://127.0.0.1/logging?user=logger&password=logpass&database=logging",
                jdbcDatasource.getConnectionString());        
        
    }
    
    
    /**
     * Test unmarshalling of a Internal Pool datasource
     */
    public void testInternalPoolDatasourceUnmarshalling() throws Exception {
        DatasourceConfiguration datasource = parseDatasource(
                new ByteArrayInputStream(
                        ANONYMOUS_INTERNAL_POOL_DATASOURCE.getBytes()));

        assertEquals(DataSourceType.INTERNAL_POOL_DATASOURCE, 
                datasource.getType());
        
        InternalPoolDatasource internalPoolDatasource = 
            (InternalPoolDatasource) datasource;
     
        assertEquals("org.gjt.mm.mysql.Driver", 
                internalPoolDatasource.getDriverClass());
        assertEquals("jdbc:mysql://127.0.0.1/logging?database=logging", 
                internalPoolDatasource.getUrl());
        assertEquals("db_user", internalPoolDatasource.getUsername());
        assertEquals("db_password", internalPoolDatasource.getPassword());
        assertEquals("30", internalPoolDatasource.getMaxActive());
        assertEquals("10", internalPoolDatasource.getMaxIdle());
        assertEquals("5", internalPoolDatasource.getMaxWait());
    }

    /**
     * Test unmarshalling of a Internal Pool datasource
     */
    public void testInternalPoolDatasourceUnmarshallingWithEmptyPassword() 
        throws Exception {
        
        DatasourceConfiguration datasource = parseDatasource(
                new ByteArrayInputStream(
                        ANONYMOUS_INTERNAL_POOL_DATASOURCE_EMPTY_PASSWORD.getBytes()));
        
        InternalPoolDatasource internalPoolDatasource = 
            (InternalPoolDatasource) datasource;
     
        assertEquals("", internalPoolDatasource.getPassword());
    }

    
    /**
     * Test unmarshalling of a JNDI datasource
     */
    public void testJNDIDatasourceUnmarshalling() throws Exception {
        DatasourceConfiguration datasource = parseDatasource(
                new ByteArrayInputStream(ANONYMOUS_JNDI_DATASOURCE.getBytes()));

        assertEquals(DataSourceType.JNDI_DATASOURCE, datasource.getType());
        
        JNDIDatasource jndiDatasource = 
            (JNDIDatasource) datasource;
     
        assertEquals("jdbc/TestDB", jndiDatasource.getJndiName());
    }    

    /**
     * Parse a datasource configuration and return a DatasourceConfiguration
     * object.
     * 
     * @param source input source with datasource configuration
     * @return the resulting DatasourceConfiguration object
     */
    private DatasourceConfiguration parseDatasource(InputStream source) {

        return (new JIBXTestDatasourceConfigurationParser().parse(source)).
            getDatasource();        
    }

}
