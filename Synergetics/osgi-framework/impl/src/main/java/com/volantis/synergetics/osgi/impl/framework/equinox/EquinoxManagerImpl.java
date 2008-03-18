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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.osgi.impl.framework.equinox;

import com.volantis.synergetics.osgi.OSGiManager;
import com.volantis.synergetics.osgi.OSGiManagerConstants;
import com.volantis.synergetics.osgi.boot.BootConstants;
import com.volantis.synergetics.osgi.framework.FrameworkManager;
import com.volantis.synergetics.osgi.impl.framework.bridge.BridgeServiceHelper;
import com.volantis.synergetics.osgi.impl.framework.manager.FrameworkManagerFactory;
import com.volantis.synergetics.osgi.impl.framework.watcher.Activator;
import org.eclipse.osgi.baseadaptor.BaseAdaptor;
import org.eclipse.osgi.framework.adaptor.FrameworkAdaptor;
import org.eclipse.osgi.framework.internal.core.FrameworkProperties;
import org.eclipse.osgi.framework.internal.core.OSGi;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.net.URI;
import java.net.URL;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * The manager of an equinox based OSGi framework.
 */
public class EquinoxManagerImpl
        implements OSGiManager {

    /**
     * The framework.
     */
    private final OSGi osgi;

    private final ClassLoader applicationContextClassLoader;

    private Activator watcherActivator;

    /**
     * Initialise.
     *
     * @param properties The properties for the framework.
     */
    public EquinoxManagerImpl(Map properties)
            throws Exception {

        Thread thread = Thread.currentThread();
        applicationContextClassLoader = thread.getContextClassLoader();

        // Remove any properties from the framework that are used by Equinox.
        Properties frameworkProperties = FrameworkProperties.getProperties();
        Iterator iterator = frameworkProperties.keySet().iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            if (propertyName.startsWith("org.osgi.")
                    || propertyName.startsWith("osgi.")
                    || propertyName.startsWith("org.eclipse.")) {
                iterator.remove();
            }
        }

        // Get URI to the framework work area.
        URI frameworkArea = (URI)
                properties.remove(OSGiManagerConstants.FRAMEWORK_AREA);
        if (frameworkArea == null) {
            throw new IllegalStateException("Must specify '" +
                    OSGiManagerConstants.FRAMEWORK_AREA + "'");
        }

        properties.put("osgi.install.area",
                resolveURIToString(frameworkArea, "install/"));
        properties.put("osgi.configuration.area",
                resolveURIToString(frameworkArea, "config/"));
        properties.put("osgi.instance.area",
                resolveURIToString(frameworkArea, "instance/"));
        properties.put("osgi.user.area",
                resolveURIToString(frameworkArea, "user/"));

        // Get the location of the JAR containing the OSGi framework as that
        // is necessary in order to load its manifest. Store it in the
        // properties.
        String osgiFramework;
        URL url = OSGi.class.getProtectionDomain().getCodeSource()
                .getLocation();
        if (url.getProtocol().equals("file")) {
            osgiFramework = url.toExternalForm();
        } else {
            throw new IllegalStateException("Unrecognized URL " +
                    url.toExternalForm() +
                    " to OSGi JAR");
        }
        properties.put("osgi.framework", osgiFramework);

        // Make sure that all bundles use the class loader for the framework,
        // (which is the class loader that was used to load the OSGiBootFactory
        // and this class) as their parent class loader.
        properties.put("osgi.parentClassloader", "fwk");

        // Disable backwards compatability boot delegation mode as it breaks
        // bundle isolation.
        properties.put("osgi.compatibility.bootdelegation", "false");

        // Add any internal packages to the system packages.
        addInternalSystemPackage(
                properties, FrameworkManager.class.getPackage());

        // Add the properties that were supplied by the caller.
        frameworkProperties.putAll(properties);

        // Create a standard adaptor and create the framework.
        FrameworkAdaptor adaptor = new BaseAdaptor(null);
        osgi = new OSGi(adaptor);

        BundleContext systemContext = osgi.getBundleContext();

        // Register the external service factory that can be used internally in
        // order to export services outside OSGi.
        systemContext.registerService(FrameworkManager.class.getName(),
                new FrameworkManagerFactory(), null);
    }

    private void addInternalSystemPackage(Map properties, Package pkg) {
        String systemPackages = (String) properties.get(
                BootConstants.SYSTEM_PACKAGES);
        StringBuffer buffer;
        String separator;
        if (systemPackages == null) {
            buffer = new StringBuffer();
            separator = "";
        } else {
            buffer = new StringBuffer(systemPackages);
            separator = ",";
        }

        String packageName = pkg.getName();
        String packageVersion = pkg.getImplementationVersion();
        buffer.append(separator).append(packageName);
        if (packageVersion != null) {
            buffer.append(";version=").append(packageVersion);
        }
        properties.put(BootConstants.SYSTEM_PACKAGES, buffer.toString());
    }

    /**
     * Resolve a relative path against a base URI and return the result as a
     * string.
     *
     * @param uri          The base URI.
     * @param relativePath The relative path.
     * @return The string result.
     */
    private String resolveURIToString(URI uri, final String relativePath) {
        return uri.resolve(relativePath).toASCIIString();
    }

    /**
     * Register a service that can only be used internally.
     */
    public void registerInternalService(
            Class clazz, Object service, Dictionary properties) {

        osgi.getBundleContext().registerService(clazz.getName(),
                service, properties);
    }

    // Javadoc inherited.
    public void registerImportedBridgeService(
            Class clazz, Object service, Dictionary properties) {

        // Add the bridge service type.
        properties = BridgeServiceHelper.addBridgeServiceProperty(
                properties, BridgeServiceHelper.VOLANTIS_IMPORT_SERVICE);

        osgi.getBundleContext().registerService(clazz.getName(),
                service, properties);
    }

    // Javadoc inherited.
    public Object createContextSwitchingProxy(Class clazz, Object object) {
        return BridgeServiceHelper.createContextSwitchingProxy(
                applicationContextClassLoader, clazz, object);
    }

    // Javadoc inherited.
    public Object getExportedBridgeService(Class clazz) {
        BundleContext bundleContext = osgi.getBundleContext();

        ServiceReference[] references;
        try {
            references = bundleContext.getServiceReferences(
                    clazz.getName(), BridgeServiceHelper.VOLANTIS_EXPORTED_SERVICE_FILTER);
        } catch (InvalidSyntaxException e) {
            IllegalStateException ise = new IllegalStateException();
            ise.initCause(e);
            throw ise;
        }

        Object service = null;
        if (references != null) {
            ServiceReference reference = references[0];
            service = bundleContext.getService(reference);
        }
        return service;
    }

    // Javadoc inherited.
    public void start() {
        osgi.launch();


       BundleContext systemContext = osgi.getBundleContext();

        // Activate the watcher bundle.
        watcherActivator = new Activator();
        try {
            watcherActivator.start(systemContext);
        } catch (Exception e) {
            IllegalStateException exception = new IllegalStateException(
                    "Could not start watcher");
            exception.initCause(e);
            exception.printStackTrace(System.err);
        }
    }

    // Javadoc inherited.
    public void stop() throws Exception {
        watcherActivator.stop(osgi.getBundleContext());

        osgi.shutdown();
    }
}
