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
package com.volantis.synergetics.jdo;

import com.volantis.synergetics.io.IOUtils;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.File;
import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

/**
 * A top level access point for the jdo persistence of the metadata classes
 */
public class JDOPersistence {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(JDOPersistence.class);

    /**
     * The key for the directory that derby will write its databases in
     */
    private static final String DERBY_SYSTEM_DIR = "derby.system.home";

    /**
     * The name of the embedded derby driver.
     */
    private static final String DERBY_EMBEDDED_DRIVER =
        "org.apache.derby.jdbc.EmbeddedDriver";

    /**
     * The property key for the jdo driver class
     */
    private static final String JDO_DRIVER_PROPERTY_KEY =
        "javax.jdo.option.ConnectionDriverName";

    /**
     * The JDOPersistenceManagerFactory to use
     */
    private PersistenceManagerFactory persistenceManagerFactory;

    /**
     * The location the DB will be created at
     */
    private File dbBase;

    /**
     * Lock for safety
     */
    private final Object lock = new Object();

    /**
     * Create a JDO Persistence instance with the specified drivername,
     * connection url, username and password.
     *
     * @param properties the properties used to configure this JDO instance
     * @throws Exception
     */
    private JDOPersistence(Properties properties) throws Exception {
        synchronized (lock) {
            if(persistenceManagerFactory == null) {
                Properties props = new Properties();
                props.putAll(properties);

                // always add this property even if we are not using the built
                // in db
                if (properties.getProperty(
                    JDO_DRIVER_PROPERTY_KEY,
                    DERBY_EMBEDDED_DRIVER).equals(DERBY_EMBEDDED_DRIVER)) {
                    String tmpDir = System.getProperty("java.io.tmpdir");
                    dbBase = new File(tmpDir, "datastores");

                    System.setProperty(DERBY_SYSTEM_DIR, dbBase.getAbsolutePath());
                    props.put(DERBY_SYSTEM_DIR, dbBase.getAbsolutePath());
                }

                try {
                    persistenceManagerFactory =
                        JDOHelper.getPersistenceManagerFactory(props);

                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.fatal("failed-to-create-db", props, e);
                    throw new JDOException("db-failed-to-create-tables", e);
                }
            }
        }
    }

    /**
     * Destroy the Persistence object and its underlying File based DB.
     */
    public void destroy() {
        persistenceManagerFactory.close();

        IOUtils.deleteDirectoryContents(dbBase);
        dbBase.delete();

    }

    /**
     * Return the default instance of the JDOPersistence object
     *
     * @param properties the properties to use to configure the JDO persistence
     * manager factory
     * @return the default instance of the JDOPersistence object
     *
     * @throws java.io.IOException if an error occurs.
     */
    public static JDOPersistence getInstance(Properties properties)
        throws Exception {

        return new JDOPersistence(properties);
    }

    /**
     * This should only be used to tie
     * @return
     */
    protected PersistenceManagerFactory getPersistenceManagerFactory() {
        return this.persistenceManagerFactory;
    }

    /**
     * Retrieve an objct by its ID
     *
     * @param id the ID of the obejct to retrieve
     * @return the retrieved and completely detatched object
     */
    public Object retrieveObject(Object id) {
        PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
        // force all referenced stuff to be pulled in.
        pm.getFetchPlan().setMaxFetchDepth(-1);
        Object result = null;
        try {
            pm.currentTransaction().begin();
            result = pm.getObjectById(id);
            result = pm.detachCopy(result);
            pm.currentTransaction().commit();

        } finally {
            if (pm.currentTransaction().isActive()) {
                pm.currentTransaction().rollback();
            }
        }
        pm.close();
        return result;
    }

    /**
     * Persist the specified object. The object reference will be invalidated
     * after the object has been successfully persisted (its fields will be
     * nulled).
     *
     * @param object the object to persist.
     * @return the Id of the persisted object.
     */
    public Object persistObject(Object object) {
        PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
        pm.getFetchPlan().setMaxFetchDepth(-1);
        Object id = null;
        try {
            pm.currentTransaction().begin();
            pm.makePersistent(object);
            id = JDOHelper.getObjectId(object);
            pm.currentTransaction().commit();
        } finally {
            if (pm.currentTransaction().isActive()) {
                LOGGER.error("db-commit-failure");
                pm.currentTransaction().rollback();
            }
        }
        pm.close();
        return id;
    }

}
