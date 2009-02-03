/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.osgi.jdo;

import org.osgi.framework.Bundle;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.datastore.DataStoreCache;
import javax.jdo.listener.InstanceLifecycleListener;
import java.util.Collection;
import java.util.Properties;

public class OSGiPersistenceManagerFactory
        implements PersistenceManagerFactory {

    private final PersistenceManagerFactory factory;

    private final Bundle bundle;

    public OSGiPersistenceManagerFactory(
            Bundle bundle, PersistenceManagerFactory factory) {

        this.bundle = bundle;
        this.factory = factory;
    }

    public void close() {
        factory.close();
    }

    public boolean isClosed() {
        return factory.isClosed();
    }

    public PersistenceManager getPersistenceManager() {
        ClassLoader oldLoader = OSGiHelper.pushContextClassLoader(bundle);
        try {
            return factory.getPersistenceManager();
        } finally {
            OSGiHelper.popContextClassLoader(oldLoader);
        }
    }

    public PersistenceManager getPersistenceManager(
            String userid, String password) {
        ClassLoader oldLoader = OSGiHelper.pushContextClassLoader(bundle);
        try {
            return factory.getPersistenceManager(userid, password);
        } finally {
            OSGiHelper.popContextClassLoader(oldLoader);
        }
    }

    public void setConnectionUserName(String userName) {
        factory.setConnectionUserName(userName);
    }

    public String getConnectionUserName() {
        return factory.getConnectionUserName();
    }

    public void setConnectionPassword(String password) {
        factory.setConnectionPassword(password);
    }

    public void setConnectionURL(String url) {
        factory.setConnectionURL(url);
    }

    public String getConnectionURL() {
        return factory.getConnectionURL();
    }

    public void setConnectionDriverName(String driverName) {
        factory.setConnectionDriverName(driverName);
    }

    public String getConnectionDriverName() {
        return factory.getConnectionDriverName();
    }

    public void setConnectionFactoryName(String connectionFactoryName) {
        factory.setConnectionFactoryName(connectionFactoryName);
    }

    public String getConnectionFactoryName() {
        return factory.getConnectionFactoryName();
    }

    public void setConnectionFactory(Object connectionFactory) {
        factory.setConnectionFactory(connectionFactory);
    }

    public Object getConnectionFactory() {
        return factory.getConnectionFactory();
    }

    public void setConnectionFactory2Name(String connectionFactoryName) {
        factory.setConnectionFactory2Name(connectionFactoryName);
    }

    public String getConnectionFactory2Name() {
        return factory.getConnectionFactory2Name();
    }

    public void setConnectionFactory2(Object connectionFactory) {
        factory.setConnectionFactory2(connectionFactory);
    }

    public Object getConnectionFactory2() {
        return factory.getConnectionFactory2();
    }

    public void setMultithreaded(boolean flag) {
        factory.setMultithreaded(flag);
    }

    public boolean getMultithreaded() {
        return factory.getMultithreaded();
    }

    public void setMapping(String mapping) {
        factory.setMapping(mapping);
    }

    public String getMapping() {
        return factory.getMapping();
    }

    public void setOptimistic(boolean flag) {
        factory.setOptimistic(flag);
    }

    public boolean getOptimistic() {
        return factory.getOptimistic();
    }

    public void setRetainValues(boolean flag) {
        factory.setRetainValues(flag);
    }

    public boolean getRetainValues() {
        return factory.getRetainValues();
    }

    public void setRestoreValues(boolean restoreValues) {
        factory.setRestoreValues(restoreValues);
    }

    public boolean getRestoreValues() {
        return factory.getRestoreValues();
    }

    public void setNontransactionalRead(boolean flag) {
        factory.setNontransactionalRead(flag);
    }

    public boolean getNontransactionalRead() {
        return factory.getNontransactionalRead();
    }

    public void setNontransactionalWrite(boolean flag) {
        factory.setNontransactionalWrite(flag);
    }

    public boolean getNontransactionalWrite() {
        return factory.getNontransactionalWrite();
    }

    public void setIgnoreCache(boolean flag) {
        factory.setIgnoreCache(flag);
    }

    public boolean getIgnoreCache() {
        return factory.getIgnoreCache();
    }

    public boolean getDetachAllOnCommit() {
        return factory.getDetachAllOnCommit();
    }

    public void setDetachAllOnCommit(boolean flag) {
        factory.setDetachAllOnCommit(flag);
    }

    public Properties getProperties() {
        return factory.getProperties();
    }

    public Collection supportedOptions() {
        return factory.supportedOptions();
    }

    public DataStoreCache getDataStoreCache() {
        return factory.getDataStoreCache();
    }

    public void addInstanceLifecycleListener(
            InstanceLifecycleListener instanceLifecycleListener,
            Class[] classes) {
        factory.addInstanceLifecycleListener(instanceLifecycleListener,
                classes);
    }

    public void removeInstanceLifecycleListener(
            InstanceLifecycleListener instanceLifecycleListener) {
        factory.removeInstanceLifecycleListener(instanceLifecycleListener);
    }
}
