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

package com.volantis.synergetics.osgi.j2ee.boot;

import com.volantis.synergetics.osgi.OSGiManager;
import com.volantis.synergetics.osgi.OSGiManagerConstants;
import com.volantis.synergetics.osgi.boot.BootConstants;
import com.volantis.synergetics.osgi.boot.OSGiBootFactory;
import com.volantis.synergetics.osgi.j2ee.bridge.http.HttpBridge;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * A {@link ServletContextListener} that initialises and starts an OSGi
 * framework that can then be used within the J2EE application.
 *
 * <p>{@link ServletContextListener} are invoked before filters and servlets
 * are initialized and before any incoming requests are processed by the
 * application.</p>
 */
public class OSGiBootListener
        implements ServletContextListener {

    /**
     * The set of default properties to use to boot up the OSGi framework.
     */
    private static final Properties DEFAULT_PROPERTIES;

    static {
        // Load the default properties.
        DEFAULT_PROPERTIES = new Properties();
        try {
            DEFAULT_PROPERTIES.load(OSGiBootListener.class
                    .getResourceAsStream("default.properties"));
        } catch (IOException e) {
            IllegalStateException exception = new IllegalStateException(
                    "Could not load default properties");
            exception.initCause(e);
            throw exception;
        }
    }

    /**
     * The manager for the OSGi framework.
     */
    private OSGiManager manager;

    public void contextInitialized(ServletContextEvent event) {

        try {
            ServletContext servletContext = event.getServletContext();

            // Get the OSGi area as a servlet context parameter.
            String osgiArea = servletContext.getInitParameter(
                    BootConstants.OSGI_AREA);
            if (osgiArea == null) {
                osgiArea = "/WEB-INF/osgi";
            }

            List errors = new ArrayList();

            // Resolve the area to a file, if it does not exist then fail.
            File osgiFile = resolveServletFile(servletContext, osgiArea,
                    errors);
            if (osgiFile == null) {
                throw new ServletException(
                        "Cannot boot as '" + osgiArea + "' does not exist");
            }

            // Now try and load some user specific boot properties. If they
            // could not be found then don't worry, if they could be found but
            // there was an exception then fail as user must be expecting it to
            // work.
            Properties bootProperties = null;
            try {
                File bootPropertiesFile = new File(osgiFile, "boot.properties");
                if (bootPropertiesFile.exists()) {
                    InputStream is = new FileInputStream(bootPropertiesFile);
                    try {
                        bootProperties = new Properties();
                        bootProperties.load(is);
                    } finally {
                        is.close();
                    }
                }
            } catch (IOException e) {
                IllegalStateException exception = new IllegalStateException(
                        "Could not load boot properties");
                exception.initCause(e);
                throw exception;
            }

            // Copy the properties into a map as we need to add some non string
            // values.
            Map properties = new HashMap(DEFAULT_PROPERTIES);
            if (bootProperties != null) {
                properties.putAll(bootProperties);
            }

            // Allow any servlet context init parameters to override the
            // boot properties.
            Enumeration enumeration = servletContext.getInitParameterNames();
            while (enumeration.hasMoreElements()) {
                String propertyName = (String) enumeration.nextElement();
                properties.put(propertyName,
                        servletContext.getInitParameter(propertyName));
            }

            // Resolve the boot class path into an array of URLs.
            resolveBootClassPath(servletContext, properties, errors, osgiFile);

            // Add additional boot delegation packages to the set passed in.
            updateBootDelegationPackages(properties);

            // Add additional system packages to the set passed in.
            updateSystemPackages(properties);

            properties.put("volantis.watcher.dir",
                    new File(osgiFile, "bundles").toString());

            // Get the framework area as a URL and store it in the properties
            // for the framework.
            File frameworkDir = new File(osgiFile, "framework");

            // Make sure that the framework directory exists, otherwise the
            // toURI() method does not work correctly, it does not add a / on
            // the end.
            if (!frameworkDir.exists()) {
                if(!frameworkDir.mkdirs()) {
                   errors.add("'" + frameworkDir + "' directory does not exist and attempts to create it failed");
                }
            }

            if (!frameworkDir.isDirectory()) {
                errors.add("'" + frameworkDir + "' is not a directory");
            } else {
                URI frameworkURI = frameworkDir.toURI();
                properties.put(OSGiManagerConstants.FRAMEWORK_AREA, frameworkURI);
            }
            
            if (!properties.containsKey(BootConstants.CONTEXT_AREA)) {
                String configArea = servletContext.getRealPath(".");
                if (null == configArea) {
                    errors.add("Cannot boot as '" +
                        BootConstants.CONTEXT_AREA +
                        "' does not exist");
                } else {
                    properties.put(BootConstants.CONTEXT_AREA, configArea);
                }
            }
                
            // If any errors were found then report them and fail.
            if (errors != null && !errors.isEmpty()) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("Errors in ")
                        .append(BootConstants.BOOT_CLASSPATH)
                        .append("\n");
                for (Iterator i = errors.iterator(); i.hasNext();) {
                    String message = (String) i.next();
                    buffer.append(message).append("\n");
                }
                throw new IllegalStateException(buffer.toString());
            }

            // Create the factory using the supplied properties.
            OSGiBootFactory factory = OSGiBootFactory.createInstance(
                    properties);

            // Create the manager for the OSGi framework.
            manager = factory.createManager(properties);

            // Store the OSGiManager in the ServletContext so it can be retrieved
            // by other parts of the application if necessary.
            servletContext.setAttribute(OSGiManager.class.getName(), manager);

            // Make the servlet context available to the nested OSGi framework.
            registerService(servletContext, ServletContext.class,
                    servletContext, null);

            // Start the service.
            manager.start();

        } catch (Throwable t) {
            t.printStackTrace(System.err);
            if (t instanceof Error) {
                throw (Error) t;
            } else if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new UndeclaredThrowableException(t);
            }
        }
    }

    /**
     * Augment the boot delegation packages with any needed as part of booting up the
     * framework.
     *
     * @param properties The set of properties containing the system packages.
     */
    private void updateBootDelegationPackages(Map properties) {
        String bootPackages = (String) properties.get(
                BootConstants.BOOT_DELEGATION);
        StringBuffer buffer = new StringBuffer();
        String separator;
        if (bootPackages == null || bootPackages.equals("")) {
            separator = "";
        } else {
            separator = addItemToList(buffer, ",", bootPackages);
        }

        // Add the properties for all the extension system packages.
        for (Iterator i = properties.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String propertyName = (String) entry.getKey();
            if (propertyName.startsWith("boot.packages.")) {
                String packages = (String) entry.getValue();
                if (packages != null && !packages.equals("")) {
                    buffer.append(separator);
                    buffer.append(packages);
                    separator = ",";
                }
            }
        }

        properties.put(BootConstants.BOOT_DELEGATION, buffer.toString());
    }

    /**
     * Augment the system packages with any needed as part of booting up the
     * framework.
     *
     * @param properties The set of properties containing the system packages.
     */
    private void updateSystemPackages(Map properties) {
        String systemPackages = (String) properties.get(
                BootConstants.SYSTEM_PACKAGES);
        StringBuffer buffer = new StringBuffer();
        String separator;
        if (systemPackages == null || systemPackages.equals("")) {
            separator = "";
        } else {
            separator = addItemToList(buffer, ",", systemPackages);
        }

        // Add the properties for all the extension system packages.
        for (Iterator i = properties.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String propertyName = (String) entry.getKey();
            if (propertyName.startsWith("system.packages.")) {
                String packages = (String) entry.getValue();
                if (packages != null && !packages.equals("")) {
                    buffer.append(separator);
                    buffer.append(packages);
                    separator = ",";
                }
            }
        }

        Package bridgePackage = HttpBridge.class.getPackage();
        String packageName = bridgePackage.getName();
        String packageVersion = bridgePackage.getImplementationVersion();
        buffer.append(separator).append(packageName);
        if (packageVersion != null) {
            buffer.append(";version=").append(packageVersion);
        }
        separator = ",";

        properties.put(BootConstants.SYSTEM_PACKAGES, buffer.toString());
    }

    private String addItemToList(
            StringBuffer buffer, String separator, String packages) {

        buffer.append(packages);
        separator = ",";
        return separator;
    }

    /**
     * Resolve the boot class path to a list of URLs.
     *
     * <p>If an explicit
     *
     * @param servletContext The context within which the framework will be
     * running.
     * @param properties The set of properties that contains the boot class path.
     * @param errors The list to which errors should be added.
     * @param osgiFile The file for the OSGi area.
     */
    private void resolveBootClassPath(
            ServletContext servletContext,
            Map properties, List errors,
            File osgiFile) {

        String path = (String) properties.get(BootConstants.BOOT_CLASSPATH);
        List urls = new ArrayList();
        if (path == null) {
            File lib = new File(osgiFile, "lib");

            File[] jars = lib.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            });
            if (jars == null || jars.length == 0) {
                throw new IllegalStateException("Cannot boot as " +
                        BootConstants.BOOT_CLASSPATH + " is not set and default " +
                        "lib directory '" + lib + "' either does not exist, " +
                        "or is empty");
            }
            for (int i = 0; i < jars.length; i++) {
                File jar = jars[i];
                urls.add(fileAsURL(jar, errors));
            }
        } else {

            // Parse the boot class path property.
            StringTokenizer tokenizer = new StringTokenizer(path, ";:");
            while (tokenizer.hasMoreTokens()) {
                String path1 = tokenizer.nextToken();
                URL url = resolveServletFileAsURL(servletContext, path1,
                        errors);
                if (url != null) {
                    urls.add(url);
                }
            }
        }

        properties.put(BootConstants.BOOT_CLASSPATH, urls);
    }

    private URL resolveServletFileAsURL(
            ServletContext servletContext, String path, List errors) {
        File file = resolveServletFile(servletContext, path, errors);
        return fileAsURL(file, errors);
    }

    private URL fileAsURL(File file, List errors) {
        URL url = null;
        try {
            url = file.toURL();
        } catch (MalformedURLException e) {
            errors.add(e.getMessage());
        }
        return url;
    }

    private File resolveServletFile(
            ServletContext servletContext, String path, List errors) {
        String realPath = servletContext.getRealPath(path);
        if (realPath == null) {
            errors.add("File at " + path + " could not be found");
            return null;
        }
        File file = new File(realPath);
        return file;
    }

    private void registerService(
            ServletContext servletContext, final Class clazz, Object service,
            Dictionary properties) {

        try {
            manager.registerImportedBridgeService(clazz, service, properties);
        } catch (IllegalArgumentException e) {
            servletContext.log("Could not register " + service +
                    " as a service of type " + clazz, e);
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        servletContext.removeAttribute(OSGiManager.class.getName());

        try {
            manager.stop();
        } catch (Exception e) {
            IllegalStateException exception = new IllegalStateException(
                    "Error shutting down OSGi framework");
            exception.initCause(e);
            throw exception;
        }
    }
}
