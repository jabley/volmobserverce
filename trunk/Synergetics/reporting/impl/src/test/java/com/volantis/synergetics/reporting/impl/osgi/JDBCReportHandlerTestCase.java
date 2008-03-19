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

import mock.javax.naming.ContextMock;
import mock.javax.sql.DataSourceMock;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.service.cm.ConfigurationException;

/**
 * Test the JDBCManagedServiceFactory
 */
public class JDBCReportHandlerTestCase extends TestCaseAbstract {


    public ReportEventHandler createReportEventHandler()
        throws ConfigurationException {
        Dictionary dict = new Hashtable();
        dict.put(JDBCReportHandler.TABLE_NAME_PROPERTY, "TableName");
        dict.put(JDBCReportHandler.JNDI_NAME_PROPERTY, "jndi://dsf");
        ContextMock context = new ContextMock(expectations);
        ContextMock envContext = new ContextMock(expectations);
        context.expects.lookup("java:comp/env").returns(envContext).any();
        envContext.expects.lookup("jndi://dsf").returns(
            new DataSourceMock(expectations)).any();

        return new JDBCReportHandler(dict, context);
    }

    public void testRequiredConfigProperties() throws Exception {

        ReportEventHandler reh = createReportEventHandler();

        {
            Dictionary dict = new Hashtable();
            try {
                reh.setConfiguration(dict);
                fail("ConfigurationException should have been thrown");
            } catch (ConfigurationException ce) {
                // success
            }
        }

        {
            Dictionary dict = new Hashtable();
            dict.put(JDBCReportHandler.JNDI_NAME_PROPERTY, "jndi://dsf");
            try {
                reh.setConfiguration(dict);
                fail("ConfigurationException should have been thrown");
            } catch (ConfigurationException ce) {
                // success
            }
        }

        {
            Dictionary dict = new Hashtable();
            dict.put(JDBCReportHandler.TABLE_NAME_PROPERTY, "TableName");
            try {
                reh.setConfiguration(dict);
                fail("ConfigurationException should have been thrown");
            } catch (ConfigurationException ce) {
                // success
            }
        }

        {
            Dictionary dict = new Hashtable();
            dict.put(JDBCReportHandler.TABLE_NAME_PROPERTY, "TableName");
            dict.put(JDBCReportHandler.JNDI_NAME_PROPERTY, "jndi://dsf");
            reh.setConfiguration(dict);

        }

    }




}
