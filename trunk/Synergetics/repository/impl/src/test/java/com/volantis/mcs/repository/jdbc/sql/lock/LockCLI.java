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

package com.volantis.mcs.repository.jdbc.sql.lock;

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.impl.jdbc.lock.JDBCPrincipal;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.JDBCDriverVendor;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.mcs.repository.jdbc.MCSDriverConfiguration;
import com.volantis.mcs.repository.lock.Lock;
import com.volantis.mcs.repository.lock.LockManager;
import org.apache.log4j.PropertyConfigurator;

import javax.sql.DataSource;
import java.security.Principal;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * A command line tool for locking and unlocking.
 */
public class LockCLI {

    public static void main(String[]args) throws RepositoryException {

        if (args.length == 0) {
            usage("lock/unlock/list");
        }

        PropertyConfigurator.configure("log4j.properties");

        JDBCRepositoryFactory jdbcFactory =
                JDBCRepositoryFactory.getDefaultInstance();

        MCSDriverConfiguration driverConfiguration =
                jdbcFactory.createMCSDriverConfiguration();
        driverConfiguration.setDriverVendor(JDBCDriverVendor.ORACLE8);
        driverConfiguration.setHost("haddock");
        driverConfiguration.setPort(1526);
        driverConfiguration.setSource("DEV1");
        DataSource dataSource =
                jdbcFactory.createMCSDriverDataSource(driverConfiguration);

        JDBCRepositoryConfiguration jdbcConfiguration =
                jdbcFactory.createJDBCRepositoryConfiguration();
        jdbcConfiguration.setDataSource(dataSource);
        jdbcConfiguration.setUsername("geoff");
        jdbcConfiguration.setPassword("geoff");

        InternalJDBCRepository repository = (InternalJDBCRepository)
                jdbcFactory.createJDBCRepository(jdbcConfiguration);

        LockManager manager = repository.getLockManager("project");

        String command = args[0];
        if (command.equals("lock")) {

            if (args.length != 3) {
                usage("lock <principal> <resource>");
            }

            String principalName = args[1];
            Principal principal = new JDBCPrincipal(principalName);
            String resource = args[2];

            Lock lock = manager.getLock(resource);
            lock.acquire(principal);
        } else if (command.equals("unlock")) {

            if (args.length != 3) {
                usage("lock <principal> <resource>");
            }

            String principalName = args[1];
            Principal principal = new JDBCPrincipal(principalName);
            String resource = args[2];
            
            Lock lock = manager.getLock(resource);
            lock.release(principal);
        } else if (command.equals("list")) {

            Collection locks = manager.getLocks();
            if (locks.size() == 0) {
                System.out.println("No locks found");
            } else {
                for (Iterator i = locks.iterator(); i.hasNext();) {
                    Lock lock = (Lock) i.next();
                    Date date = new Date(lock.getAcquisitionTime());
                    DateFormat format = DateFormat.getDateTimeInstance();
                    String formattedDate = format.format(date);
                    System.out.println("Resource " +
                            lock.getResourceIdentifier() +
                            " is owned by " + lock.getOwner().getName() +
                            " and was acquired on " + formattedDate);
                }
            }
        }
    }

    private static void usage(final String usage) {
        System.err.println("Usage: LockCLI " + usage);
        System.exit(1);
    }
}
