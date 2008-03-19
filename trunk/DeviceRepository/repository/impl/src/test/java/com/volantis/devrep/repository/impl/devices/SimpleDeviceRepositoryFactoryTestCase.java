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
package com.volantis.devrep.repository.impl.devices;

import com.volantis.devrep.repository.api.devices.SimpleDeviceRepositoryFactory;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryType;
import com.volantis.synergetics.testtools.HypersonicManager;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.sql.SQLException;

/**
 * Test case for SimpleDeviceRepositoryFactory.
 */
public class SimpleDeviceRepositoryFactoryTestCase extends TestCaseAbstract {
    private static final String DEFAULT_PROJECT = "#DEFAULT_PROJECT";

    /**
     * Test that a JDBC device repository can be created from a jdbc url.
     * Note that originally a real Hypersonic database was configured for
     * this test. However, this was useless because DeviceRepositoryFactory
     * configures its own repository and it is not possible to provide
     * the underlying repository or repository connection - making it
     * not very testable. There was not enough time to rework
     * DeviceRepositoryFactory to be more testable.
     * @throws Exception
     */
    public void testCreateDeviceRepositoryJDBC() throws Exception {
        String url = "jdbc:" +
                JDBCRepositoryType.HYPERSONIC.getSubProtocol() +
                ":" + HypersonicManager.IN_MEMORY_SOURCE;
        url += "?" + SimpleDeviceRepositoryFactory.JDBC_USERNAME +
                "=" + HypersonicManager.DEFAULT_USERNAME +
                "&" + SimpleDeviceRepositoryFactory.JDBC_PASSWORD +
                "=" + HypersonicManager.DEFAULT_PASSWORD +
                "&" + SimpleDeviceRepositoryFactory.JDBC_DEFAULT_PROJECT +
                "=" + DEFAULT_PROJECT;

        SimpleDeviceRepositoryFactory factory =
                new SimpleDeviceRepositoryFactory();
        try {
            factory.createDeviceRepository(url);
        } catch (DeviceRepositoryException e) {
            // A DeviceRepositoryException with an SQLException
            // root cause is expected since DeviceRepositoryFactory
            // is untestable without significant redesign. If
            // an SQLException is received then the DriverManager
            // has sucessfully opened a database connection which is
            // what we really care about.
            Throwable cause = e.getCause();
            if (!cause.getClass().equals(JDBCRepositoryException.class)) {
                throw e;
            }

            Throwable specific = ((JDBCRepositoryException) cause).
                    getCause();
            if (!specific.getClass().equals(SQLException.class)) {
                throw e;
            }
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 18-Apr-05	7692/5	allan	VBM:2005041504 SimpleDeviceRepositoryFactory - create a dev rep from a url

 18-Apr-05	7692/3	allan	VBM:2005041504 SimpleDeviceRepositoryFactory - create a dev rep from a url

 ===========================================================================
*/
