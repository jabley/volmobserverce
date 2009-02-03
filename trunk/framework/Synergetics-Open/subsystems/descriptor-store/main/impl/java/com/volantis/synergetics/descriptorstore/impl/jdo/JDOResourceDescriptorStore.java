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
package com.volantis.synergetics.descriptorstore.impl.jdo;

import com.volantis.osgi.jdo.OSGiHelper;

import com.volantis.synergetics.descriptorstore.ParameterNames;
import com.volantis.synergetics.descriptorstore.Parameters;
import com.volantis.synergetics.descriptorstore.ResourceDescriptor;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStore;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStoreException;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStoreFactory;
import com.volantis.synergetics.descriptorstore.impl.DefaultParameterNames;
import com.volantis.synergetics.descriptorstore.impl.DefaultParameters;
import com.volantis.synergetics.descriptorstore.impl.DefaultResourceDescriptor;
import com.volantis.synergetics.descriptorstore.impl.ExternalIDGenerator;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.FetchPlan;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

import org.jpox.PersistenceManagerFactoryImpl;
import org.osgi.framework.Bundle;

/**
 * An implemenation of the Configuration store that uses JDO to store the
 * Configuration Items.
 */
public class JDOResourceDescriptorStore implements ResourceDescriptorStore {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(JDOResourceDescriptorStore.class);

    /**
     * The persistence manager factory to use
     */
    private static PersistenceManagerFactory PMF = null;

    /**
     * Default constructor.
     */
    public JDOResourceDescriptorStore() {
        // do nothing
    }

    /**
     * Allow a new persistence manager factory to be created with the associted
     * properties.
     *
     * @param properties the properties to use to configure the configuration
     * store.
     */
    public JDOResourceDescriptorStore(final Properties properties) {
        setPersistenceManagerFactory(properties);
    }

    /**
     * @param properties
     * @note Added for testing purposes only!
     */
    public void setPersistenceManagerFactory(final Properties properties) {
        PMF = JDOHelper.getPersistenceManagerFactory(properties);
        System.out.println("Set persistent manager factory to " + PMF);
    }

    // javadoc inherited
    public ResourceDescriptor getDescriptor(String externalID)
        throws ResourceDescriptorStoreException {

        DefaultResourceDescriptor result = null;
        PersistenceManager pm = getPersistenceManager();
        pm.getFetchPlan().setMaxFetchDepth(-1);
        pm.getFetchPlan().setFetchSize(FetchPlan.FETCH_SIZE_GREEDY);
        pm.setDetachAllOnCommit(true);
        final Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            // run the query
            final Query query = pm.newQuery(DefaultResourceDescriptor.class,
                                            "externalID == ID");
            query.declareImports("import java.lang.String");
            query.declareParameters("String ID");
            query.setUnique(true);
            result = (DefaultResourceDescriptor) query.execute(externalID);
            if (null == result) {
                throw new ResourceDescriptorStoreException(
                    "cs-external-id-not-found", externalID);
            } else {
                // update the last access time.
                result.setLastAccess(new Date());
            }
            tx.commit(); 
        } catch (ResourceDescriptorStoreException rde) {
            LOGGER.warn(rde);
            throw rde;
        } catch (Throwable t) {
            // ensure we log it here
            LOGGER.error(t);
            throw new RuntimeException(t);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        return result;
    }

    // javadoc inherited
    public ResourceDescriptor createDescriptor(
            String resourceType,Parameters configParams, ParameterNames names,
            final long initialTimeToLive) {
        DefaultResourceDescriptor result = null;
        int hash = DefaultResourceDescriptor.computeDBHash(
            resourceType, configParams);
        PersistenceManager pm = getPersistenceManager();
        pm.getFetchPlan().setMaxFetchDepth(-1);
        pm.getFetchPlan().setFetchSize(FetchPlan.FETCH_SIZE_GREEDY);
        pm.setDetachAllOnCommit(true);
        final Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            // run the query
            final Query query = pm.newQuery(DefaultResourceDescriptor.class,
                                            "hash == hashcode");
            query.declareParameters("int hashcode");

            List hashMatches = (List) query.execute(new Integer(hash));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Found " + hashMatches.size() +
                             " matches for hashcode " + hash);
            }
            Iterator it = hashMatches.iterator();
            boolean finished = false;
            while (it.hasNext() && !finished) {
                DefaultResourceDescriptor gi = (DefaultResourceDescriptor) it.next();
                // here we check that the important bits of the retrieved entry
                // are equal to the requested information
                if (resourceType.equals(gi.getResourceType()) &&
                    gi.getInputParameters().equals(
                        gi.getInputParameters())) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Found exact match for hash " + hash);
                    }
                    result = gi;
                    finished = true;
                }
            }

            // there is a minor concurrency issue here where more then one
            // ResourceDefinition objects could be created for the same
            // resource. It is a small hole and causes an inefficiency in the
            // amount of data stored but does not break anything.
            if (null == result) {
               result = new DefaultResourceDescriptor(resourceType,
                   (DefaultParameters) configParams,
                   (DefaultParameterNames) names);
                result.computeDBHash();
                result.setExternalID(ExternalIDGenerator.getNextID());
                result.setTimeToLive(initialTimeToLive);
                pm.makePersistent(result);
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("updating last access time");
                }
                // update the last access time (and update any ttl)
                result.setLastAccess(new Date());
            }
            tx.commit();
        } catch (Throwable t) {
            // make sure its logged
            LOGGER.error(t);
            throw new RuntimeException(t);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        return result;
    }

    // javadoc inherited.
    public void updateDescriptor(ResourceDescriptor descriptor) {
        PersistenceManager pm = getPersistenceManager();
        DefaultResourceDescriptor configItem = (DefaultResourceDescriptor) descriptor;
        pm.getFetchPlan().setMaxFetchDepth(-1);
        pm.getFetchPlan().setFetchSize(FetchPlan.FETCH_SIZE_GREEDY);
        pm.setDetachAllOnCommit(true);
        final Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("updating descriptor " + configItem.getExternalID());
            }
            configItem.setLastAccess(new Date());
            configItem.computeDBHash();
            pm.makePersistent(descriptor);
            tx.commit();
        } catch (Throwable t) {
            // make sure its logged
            LOGGER.error(t);
            throw new RuntimeException(t);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    // javadoc inherited
    public void updateDescriptorTimeToLive(final String externalId,
                                           final long timeToLive)
            throws ResourceDescriptorStoreException {

        PersistenceManager pm = getPersistenceManager();
        pm.getFetchPlan().setMaxFetchDepth(-1);
        pm.getFetchPlan().setFetchSize(FetchPlan.FETCH_SIZE_GREEDY);
        pm.setDetachAllOnCommit(true);
        final Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            // run the query
            final Query query = pm.newQuery(DefaultResourceDescriptor.class,
                                            "externalID == ID");
            query.declareImports("import java.lang.String");
            query.declareParameters("String ID");
            query.setUnique(true);
            final DefaultResourceDescriptor descriptor =
                (DefaultResourceDescriptor) query.execute(externalId);
            if (null == descriptor) {
                throw new ResourceDescriptorStoreException(
                    "cs-external-id-not-found", externalId);
            }
            final long oldTtl = descriptor.getTimeToLive();
            if (timeToLive > oldTtl) {
                descriptor.setTimeToLive(timeToLive);
            }
            tx.commit();
        } catch (ResourceDescriptorStoreException rde) {
            LOGGER.warn(rde);
            throw rde;
        } catch (Throwable t) {
            // ensure we log it here
            LOGGER.error(t);
            throw new RuntimeException(t);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    /**
     * Remove the descriptors that have timed out from the data store
     */
    private void removeTimedOutDescriptors() {
        PersistenceManager pm = getPersistenceManager();
        pm.getFetchPlan().setMaxFetchDepth(-1);
        pm.getFetchPlan().setFetchSize(FetchPlan.FETCH_SIZE_GREEDY);
        pm.setDetachAllOnCommit(true);
        final Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            final Query query = pm.newQuery(
                DefaultResourceDescriptor.class, "expiry < date");
            query.declareParameters("java.util.Date date");
            long number = query.deletePersistentAll(new Object[] {new Date()});
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Removed " + number +" descriptors from store");
            }

            tx.commit();
        } catch (Throwable t) {
            // ensure its logged
            LOGGER.error(t);
            throw new RuntimeException(t);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    /**
     * Return the PersistenceManagerFactory for the specified bundle
     *
     * @param bundle
     * @return the PersistenceManagerFactory for the specified bundle
     */
    public synchronized PersistenceManagerFactory
        getPersistenceManagerFactory(Bundle bundle) {

        try {
            if (PMF == null) {
                Properties properties = new Properties();
                InputStream is =
                    JDOResourceDescriptorStore.class.getResourceAsStream("jpox.properties");
                try {
                    try {
                        properties.load(is);
                    } finally {
                        is.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Look the context up ourselves as Tomcat appears to traverse
                // the class loader hierarchy starting with the context class
                // loader in order to determine what resources are available
                // in the context but unfortunately the OSGiHelper class below
                // has to change the context class loader so that it looks in
                // the class loader for the JPOX bundle in order to pick up
                // JPOX storage managers. This prevents any named resources
                // from being found.
                InitialContext envContext = new InitialContext();

                String connectionFactoryName = (String) properties.remove(
                    "javax.jdo.option.ConnectionFactoryName");
                DataSource connectionFactory = (DataSource)
                    envContext.lookup(connectionFactoryName);
                properties.put("javax.jdo.option.ConnectionFactory",
                               connectionFactory);

                String connectionFactory2Name = (String) properties.remove(
                    "javax.jdo.option.ConnectionFactory2Name");
                DataSource connectionFactory2 = (DataSource)
                    envContext.lookup(connectionFactory2Name);
                properties.put("javax.jdo.option.ConnectionFactory2",
                               connectionFactory2);

                // note that this casts the properties to a Map. If you don't cast
                // to a map it does NOT work
                PMF = OSGiHelper.getPersistenceManagerFactory(bundle, properties);

                // create a thread to clean up the expired descriptors.
                // @todo this should probably take a sleep period from the
                // properties if set.
                Runnable cleanUp = new Runnable() {
                    public void run() {
                        Object lock = new Object();
                        ResourceDescriptorStoreFactory rdsf =
                            ResourceDescriptorStoreFactory.getDefaultInstance();
                        JDOResourceDescriptorStore ds =
                            (JDOResourceDescriptorStore)
                            rdsf.getResourceDescriptorStore();
                        while (true) {
                            synchronized (lock) {
                                try {
                                    Thread.sleep(30000);
                                } catch (InterruptedException e) {
                                    LOGGER.error(e);
                                }
                            }
                            try {
                                ds.removeTimedOutDescriptors();
                            } catch (Throwable t) {
                                // we don't want to stop this thread so quash
                                // the throwable (The message should already
                                // have been logged)

                            }
                        }
                    }
                };

                Thread t = new Thread(cleanUp, "ResourceDescriptorStore cleaner");
                t.setDaemon(true);
                t.start();
            }

            return PMF;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Return a Persistence Manager
     * @return a Persistence Manager
     */
    private synchronized PersistenceManager getPersistenceManager() {
        try {
            final PersistenceManagerFactory pmf =
                getPersistenceManagerFactory(Activator.getBundle());
            return pmf.getPersistenceManager();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    // javdoc inherited
    public ParameterNames createParameterNames() {
        return new DefaultParameterNames();
    }

    // javadoc inherited
    public Parameters createParameters() {
        return new DefaultParameters();
    }

    // javadoc inherited
    public void shutdown() {
        PMF.close();
    }
}
