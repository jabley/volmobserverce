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

package com.volantis.osgi.j2ee.bridge.http.service;

import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReadWriteLock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantReadWriteLock;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Registry of servlets and resources.
 *
 * <p>This is a Declarative Services Component, look in the
 * <code>osgi.bnd</code> bundle definition file at the Service-Component
 * header and at the <code>OSGi Declarative Services</code> Wiki page for more
 * information about how the component declarations affect how they behave.</p>
 *
 * <p>This component references the following service(s):</p>
 * <ul>
 * <li>{@link ServletContext} - required / static</p>
 * </ul>
 */
public class ServletRegistry {

    /**
     * An empty enumeration.
     */
    private static final Enumeration EMPTY_ENUMERATION = new Enumeration() {
        public boolean hasMoreElements() {
            return false;
        }

        public Object nextElement() {
            throw new NoSuchElementException();
        }
    };

    /**
     * An empty dictionary.
     */
    private static final Dictionary EMPTY_DICTIONARY = new Dictionary() {
        public int size() {
            return 0;
        }

        public boolean isEmpty() {
            return true;
        }

        public Enumeration elements() {
            return EMPTY_ENUMERATION;
        }

        public Enumeration keys() {
            return EMPTY_ENUMERATION;
        }

        public Object get(Object key) {
            return null;
        }

        public Object remove(Object key) {
            return null;
        }

        public Object put(Object key, Object value) {
            throw new UnsupportedOperationException();
        }
    };

    /**
     * The servlet context from the container.
     */
    private ServletContext containerContext;

    /**
     * A lock to control read access to the registrations.
     */
    private final Lock readLock;

    /**
     * A lock to control write access to the registrations.
     */
    private final Lock writeLock;

    /**
     * Map from alias to servlet.
     */
    private Map alias2Registration;

    /**
     * Map from servlet to alias.
     */
    private Map servlet2Registration;

    /**
     * Map from HTTP context to Servlet context.
     */
    private Map http2Servlet;

    /**
     * Initialise.
     * @param servletContext
     */
    public ServletRegistry(ServletContext servletContext) {
        
        this.containerContext = servletContext;

        ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();

        this.alias2Registration = new HashMap();
        this.servlet2Registration = new HashMap();
        this.http2Servlet = new HashMap();
    }

    /**
     * Register a servlet.
     *
     * @param bundle      The bundle that is registering the servlet.
     * @param alias       The alias.
     * @param servlet     The servlet.
     * @param dictionary  The initialisation properties.
     * @param httpContext The {@link HttpContext}.
     * @throws ServletException   If there was a problem initialising the
     *                            servlet.
     * @throws NamespaceException If there was a clash between aliases.
     */
    public void registerServlet(
            Bundle bundle, String alias, Servlet servlet, Dictionary dictionary,
            HttpContext httpContext) throws ServletException,
            NamespaceException {

        Checker.checkAlias(alias);

        if (servlet == null) {
            throw new IllegalArgumentException("servlet cannot be null");
        }

        if (dictionary == null) {
            dictionary = EMPTY_DICTIONARY;
        }

        if (httpContext == null) {
            throw new IllegalArgumentException("httpContext cannot be null");
        }

        try {
            writeLock.lockInterruptibly();
            try {
                registerServletLocked(bundle, alias, servlet, httpContext,
                        dictionary);
            } finally {
                writeLock.unlock();
            }
        } catch (InterruptedException e) {
            IllegalStateException exception = new IllegalStateException(
                    "Could not register servlet");
            exception.initCause(e);
            throw exception;
        }

    }

    /**
     * Register a servlet.
     *
     * <p>This is called while holding the write lock.</p>
     *
     * @param bundle      The bundle that is registering the servlet.
     * @param alias       The alias.
     * @param servlet     The servlet.
     * @param dictionary  The initialisation properties.
     * @param httpContext The {@link HttpContext}.
     * @throws ServletException   If there was a problem initialising the
     *                            servlet.
     * @throws NamespaceException If there was a clash between aliases.
     */
    private void registerServletLocked(
            Bundle bundle, String alias, Servlet servlet,
            HttpContext httpContext,
            Dictionary dictionary)
            throws NamespaceException, ServletException {

        // Make sure that the alias is not already in use.
        Registration registration = (Registration)
                alias2Registration.get(alias);
        if (registration != null) {
            throw new NamespaceException("Alias '" + alias +
                    "' is already in use by '" + registration + "'");
        }

        // Make sure that the servlet has not already been registered
        // under another alias.
        registration = (Registration) servlet2Registration.get(servlet);
        if (registration != null) {
            throw new ServletException("Servlet '" + servlet +
                    "' cannot be registered under alias '" + alias +
                    "' as it has already been registered under alias '" +
                    registration.getAlias() + "'");
        }

        InternalServletContext servletContext =
                getServletContext(httpContext);

        ServletConfig servletConfig;
        servletConfig = new ServletConfigImpl(alias, servletContext,
                dictionary);

        ServletRegistration servletRegistration = new ServletRegistration(
                bundle, servletContext, alias, servlet, servletConfig);
        registration = servletRegistration;

        // Initialize the servlet, if this fails then an exception will be
        // thrown which will prevent registration.
        servletRegistration.initialise();

        // Register the servlet.
        alias2Registration.put(alias, registration);
        servlet2Registration.put(servlet, registration);
    }

    private InternalServletContext getServletContext(HttpContext httpContext) {
        // Create a mapping from the HttpContext to the ServletContext,
        // creating one if necessary.
        InternalServletContext servletContext = (InternalServletContext)
                http2Servlet.get(httpContext);
        if (servletContext == null) {
            servletContext =
                    new ServletContextImpl(containerContext, httpContext);
            http2Servlet.put(httpContext, servletContext);
        }
        return servletContext;
    }

    public void dispatchRequest(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Make sure that the path is valid and if it is empty change it to
        // '/' to simplify the following code.
        String originalPath = request.getPathInfo();
        Checker.checkPathInfo(originalPath);
        if (originalPath.equals("/")) {
            originalPath = "/";
        }

        String path = originalPath;
        do {
            Registration registration = getRegistration(originalPath, path);
            if (registration == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "'" + originalPath + "' was not found");
                return;
            }

            // Perform a security check before dispatching the request.
            HttpContext httpContext = registration.getHttpContext();
            if (!httpContext.handleSecurity(request, response)) {
                return;
            }

            // It is possible that in the time between getting the registration
            // and invoking it that the bundle that registered it has been
            // uninstalled, or has unregistered the item.
            if (registration.handleRequest(request, response)) {
                return;
            }

            path = reducePath(originalPath, registration.getAlias());

        } while (path != null);
    }

    /**
     * Get the registration.
     *
     * @param originalPath The original path that was requested, used for
     *                     errors.
     * @param path         The path for which the registration is required.
     * @return The registration or null if it could not be found.
     * @throws ServletException If the thread was interrupted while waiting for
     *                          access to the registry.
     */
    private Registration getRegistration(String originalPath, String path)
            throws ServletException {

        try {
            readLock.lockInterruptibly();
            try {
                do {
                    Registration registration = (Registration)
                            alias2Registration.get(path);
                    if (registration != null) {
                        return registration;
                    }

                    path = reducePath(originalPath, path);

                } while (path != null);
            } finally {
                readLock.unlock();
            }
        } catch (InterruptedException e) {
            ServletException exception = new ServletException(
                    "Could not dispatch request");
            exception.initCause(e);
            throw exception;
        }

        return null;
    }

    /**
     * Reduce the path as per the OSGi specification.
     *
     * @param originalPath The original path that was requested, used for
     *                     errors.
     * @param path         The path to reduce.
     * @return The reduced path, or null if the path wwas the root.
     */
    private String reducePath(String originalPath, String path) {
        if (path.equals("/")) {
            path = null;
        } else {
            int index = path.lastIndexOf('/');
            if (index == -1) {
                throw new IllegalStateException("Internal Error: Path '" +
                        originalPath +
                        "' was reduced to '" + path +
                        "' which is not valid as it does not " +
                        "start with a '/'");
            } else if (index == 0) {
                path = "/";
            } else {
                path = path.substring(0, index);
            }
        }
        return path;
    }

    /**
     * Register a set of resources.
     *
     * @param bundle      The bundle that is registering the servlet.
     * @param alias       The alias.
     * @param name        The name of the root directory containing the
     *                    resources.
     * @param httpContext The {@link HttpContext}.
     * @throws NamespaceException If there was a clash between aliases.
     */
    public void registerResources(
            Bundle bundle, String alias, String name, HttpContext httpContext)
            throws NamespaceException {

        Checker.checkAlias(alias);
        Checker.checkAlias(name);

        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }

        if (httpContext == null) {
            throw new IllegalArgumentException("httpContext cannot be null");
        }

        try {
            writeLock.lockInterruptibly();
            try {
                registerResourceLocked(bundle, alias, name, httpContext);
            } finally {
                writeLock.unlock();
            }
        } catch (InterruptedException e) {
            IllegalStateException exception = new IllegalStateException(
                    "Could not register resources");
            exception.initCause(e);
            throw exception;
        }
    }

    /**
     * Register a set of resources.
     *
     * <p>This is called while holding the write lock.</p>
     *
     * @param bundle      The bundle that is registering the servlet.
     * @param alias       The alias.
     * @param name        The name of the root directory containing the
     *                    resources.
     * @param httpContext The {@link HttpContext}.
     * @throws NamespaceException If there was a clash between aliases.
     */
    private void registerResourceLocked(
            Bundle bundle, String alias, String name, HttpContext httpContext)
            throws NamespaceException {

        // Make sure that the alias is not already in use.
        Registration registration = (Registration)
                alias2Registration.get(alias);
        if (registration != null) {
            throw new NamespaceException("Alias '" + alias +
                    "' is already in use by '" + registration + "'");
        }

        InternalServletContext servletContext =
                getServletContext(httpContext);

        registration = new ResourceRegistration(bundle, servletContext, alias,
                name);

        alias2Registration.put(alias, registration);
    }

    /**
     * Unregister the servlet or resource previously registered at the
     * specified alias.
     *
     * @param bundle The bundle that is unregistering the resource.
     * @param alias  The alias to unregister.
     */
    public void unregister(Bundle bundle, String alias) {

        Registration registration;
        try {
            writeLock.lockInterruptibly();
            try {
                registration = unregisterLocked(bundle, alias);
            } finally {
                writeLock.unlock();
            }
        } catch (InterruptedException e) {
            IllegalStateException exception = new IllegalStateException(
                    "Could not unregister resources");
            exception.initCause(e);
            throw exception;
        }

        // Complete registration shut down outside the lock as it may
        // wait for current requests to finish before returning. This could
        // lead to deadlocks if performed while holding the write lock.
        registration.completeShutDown();
    }

    /**
     * Unregister the servlet or resource previously registered at the
     * specified alias.
     *
     * <p>This is called while holding the write lock.</p>
     *
     * @param bundle The bundle that is unregistering the resource.
     * @param alias  The alias to unregister.
     */
    private Registration unregisterLocked(Bundle bundle, String alias) {

        // Make sure that the alias is not already in use.
        Registration registration = (Registration)
                alias2Registration.get(alias);
        if (registration == null) {
            throw new IllegalArgumentException("Alias '" + alias +
                    "' is not registered");
        }

        Bundle registeringBundle = registration.getBundle();
        if (registeringBundle != bundle) {
            throw new IllegalArgumentException("Alias '" + alias +
                    "' was previously registered by bundle " +
                    registeringBundle +
                    " and therefore cannot be unregistered by " + bundle);
        }

        alias2Registration.remove(alias);

        // Indicate that the registration is being shutdown to ensure that
        // subsequent requests fail.
        registration.startShutDown();

        return registration;
    }

    /**
     * Destroy the service.
     *
     * <p>The HttpService for the specified bundle needs to be destroyed
     * because the bundle has been stopped.</p>
     *
     * @param bundle The bundle whose services and resources should be
     *               unregistered.
     */
    public void destroyService(Bundle bundle) {

        Collection registrations;
        try {
            writeLock.lockInterruptibly();
            try {
                registrations = destroyServiceLocked(bundle);
            } finally {
                writeLock.unlock();
            }
        } catch (InterruptedException e) {
            IllegalStateException exception = new IllegalStateException(
                    "Could not unregister resources");
            exception.initCause(e);
            throw exception;
        }

        // Complete registration shut down outside the lock as it may
        // wait for current requests to finish before returning. This could
        // lead to deadlocks if performed while holding the write lock.
        for (Iterator i = registrations.iterator(); i.hasNext();) {
            Registration registration = (Registration) i.next();
            registration.completeShutDown();
        }
    }

    /**
     * Destroy the service.
     *
     * <p>This is called while holding the write lock.</p>
     *
     * <p>The HttpService for the specified bundle needs to be destroyed
     * because the bundle has been stopped.</p>
     *
     * @param bundle The bundle whose services and resources should be
     *               unregistered.
     */
    private Collection destroyServiceLocked(Bundle bundle) {
        Collection registrations = new ArrayList();
        Iterator i = alias2Registration.values().iterator();
        while (i.hasNext()) {
            Registration registration = (Registration) i.next();
            if (registration.getBundle() == bundle) {
                i.remove();
                registrations.add(registration);

                // Indicate that the registration is being shutdown to ensure that
                // subsequent requests fail.
                registration.startShutDown();
            }
        }

        return registrations;
    }

    /**
     * Invoked when {@link ServletContext} service becomes available.
     *
     * @param context The {@link ServletContext} service.
     */
    protected void setServletContext(ServletContext context) {
        this.containerContext = context;
    }
}
