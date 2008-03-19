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

import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.service.component.ComponentContext;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.util.Dictionary;

/**
 * Implementation of {@link HttpService}.
 *
 * <p>An instance of this is created for each bundle that registers servlets
 * or resources with the {@link HttpService}. It delegates its requests to
 * the underlying registry passing in the bundle for which this was
 * created.</p>
 *
 * <p>This is a Declarative Services Component, look in the
 * <code>osgi.bnd</code> bundle definition file at the Service-Component
 * header and at the <code>OSGi Declarative Services</code> Wiki page for more
 * information about how the component declarations affect how they behave.</p>
 *
 * <p>This component references the following service(s):</p>
 * <ul>
 * <li>{@link ServletRegistry} - required / static</p>
 * </ul>
 */
public class HttpServiceImpl
        implements HttpService {

    /**
     * The registry with which all servlets and resources are registered.
     */
    private ServletRegistry registry;

    /**
     * The bundle for which this service was created.
     */
    private Bundle bundle;

    public HttpServiceImpl() {
    }

    public HttpServiceImpl(ServletRegistry registry, Bundle bundle) {
        this.registry = registry;
        this.bundle = bundle;
    }

    // Javadoc inherited.
    public void registerServlet(
            String alias, Servlet servlet, Dictionary dictionary,
            HttpContext httpContext) throws ServletException,
            NamespaceException {

        if (httpContext == null) {
            httpContext = createDefaultHttpContext();
        }


        registry.registerServlet(bundle, alias, servlet, dictionary,
                httpContext);
    }

    // Javadoc inherited.
    public void registerResources(
            String alias, String name, HttpContext httpContext)
            throws NamespaceException {

        if (httpContext == null) {
            httpContext = createDefaultHttpContext();
        }

        registry.registerResources(bundle, alias, name, httpContext);
    }

    // Javadoc inherited.
    public void unregister(String alias) {
        registry.unregister(bundle, alias);
    }

    // Javadoc inherited.
    public HttpContext createDefaultHttpContext() {
        return new DefaultHttpContext(bundle);
    }

    /**
     * Invoked when {@link ServletRegistry} service becomes available.
     *
     * @param registry The {@link ServletRegistry} service.
     */
    protected void setServletRegistry(ServletRegistry registry) {
        this.registry = registry;
    }

    /**
     * Invoked after the references required by this component have been
     * satisfied.
     *
     * @param context The context for this component.
     */
    protected void activate(ComponentContext context) {
        bundle = context.getUsingBundle();
    }
}
