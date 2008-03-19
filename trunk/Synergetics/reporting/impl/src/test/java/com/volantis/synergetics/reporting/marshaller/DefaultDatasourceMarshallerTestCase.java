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
 * Default datasource marshaller tests.
 */
public class DefaultDatasourceMarshallerTestCase extends TestCase {

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
        public TestDefaultMarshallerElement parse(InputStream inputStream) {
            try {
                IBindingFactory bfact = BindingDirectory
                        .getFactory(TestDefaultMarshallerElement.class);
                IUnmarshallingContext uctx = bfact.createUnmarshallingContext();

                return (TestDefaultMarshallerElement) uctx.unmarshalDocument(
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
                    .getFactory(TestDefaultMarshallerElement.class);
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
    private String NAMED_JDBC_DATASOURCE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<test-element-default>\n" +
        "    <jdbc-datasource>\n" +
        "        <driver-class>org.gjt.mm.mysql.Driver</driver-class>\n" +
        "        <connection-string>jdbc:mysql://127.0.0.1/logging?user=logger&amp;password=logpass&amp;database=logging</connection-string>\n" +
        "        <name>MYSQLDB</name>\n" +
        "    </jdbc-datasource>\n" +
        "</test-element-default>";


    private String NAMED_INTERNAL_POOL_DATASOURCE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<test-element-default>\n" +
        "    <internal-pool-datasource>\n" +
        "        <driver-class>org.gjt.mm.mysql.Driver</driver-class>\n" +
        "        <url>jdbc:mysql://127.0.0.1/logging?database=logging</url>\n" +
        "        <username>db_user</username>\n" +
        "        <password>db_password</password>\n" +
        "        <max-active>30</max-active>\n" +
        "        <max-idle>10</max-idle>\n" +
        "        <max-wait>5</max-wait>\n" +
        "        <name>POOL</name>\n" +
        "    </internal-pool-datasource>\n" + 
        "</test-element-default>";

    private String NAMED_JNDI_DATASOURCE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<test-element-default>\n" +
        "    <jndi-datasource>\n" +
        "        <jndi-name>jdbc/TestDB</jndi-name>\n" +
        "        <name>JNDI</name>\n" +
        "    </jndi-datasource>\n" +
        "</test-element-default>";
    
    public DefaultDatasourceMarshallerTestCase() {
        
    }
    
    /**
     * Test Default Datasource Marshaller
     */
    public void testDefaultDatasourceMarshaller() throws Exception {

        InputStream bais = new ByteArrayInputStream(NAMED_JDBC_DATASOURCE
                .getBytes());
        JIBXTestDatasourceConfigurationParser parser = 
                                new JIBXTestDatasourceConfigurationParser();
        TestDefaultMarshallerElement element = parser.parse(bais);

        assertEquals("Parsed datasource: " + element.getDatasource().getType()
                        + " is not JDBCDatasource", element.getDatasource().
                        getType() == DataSourceType.JDBC_DATASOURCE, true);
        assertEquals("Parsed datasource name: "
                + element.getDatasource().getName() + " is not MYSQLDB",
                element.getDatasource().getName(), "MYSQLDB");

        InputStream bais2 = new ByteArrayInputStream(
                NAMED_INTERNAL_POOL_DATASOURCE.getBytes());
        element = parser.parse(bais2);
        assertEquals("Parsed datasource: " + element.getDatasource().getType()
                + " is not InternalPoolDatasource", element.getDatasource()
                .getType() == DataSourceType.INTERNAL_POOL_DATASOURCE, true);
        assertEquals("Parsed datasource name: "
                + element.getDatasource().getName() + " is not POOL", element
                .getDatasource().getName(), "POOL");

        InputStream bais3 = new ByteArrayInputStream(NAMED_JNDI_DATASOURCE
                .getBytes());
        element = parser.parse(bais3);
        assertEquals("Parsed datasource: " + element.getDatasource().getType()
                     + " is not JNDIDatasource", element.getDatasource().
                     getType() == DataSourceType.JNDI_DATASOURCE, true);
        assertEquals("Parsed datasource name: "
                + element.getDatasource().getName() + " is not JNDI", element
                .getDatasource().getName(), "JNDI");
    }
}
