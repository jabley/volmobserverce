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
package com.volantis.map.webservice;

import com.volantis.map.operation.OperationEngine;

import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

/**
 * A component activator.
 *
 * <p>Registers the ics servlet with a HttpService when
 * present. It assumes that there is a single instance of the servlet which is
 * bound to a single instance of the HttpService. i.e. it has a cardinality or
 * 1..1 with respect to the HttpService. It is also an immediate component
 * which means that as soon as it's references are satisfied, i.e. a
 * HttpService has been registered then it must be activated. This means that
 * for all practical purposes it is only ever UNSATISFIED, or ACTIVE.</p>
 *
 * <p>When moving from the UNSATISFIED to ACTIVE state, i.e. when a HttpService
 * is registered then the methods are invoked in the following order:</p> <ol>
 * <li>{@link #setHttp(org.osgi.service.http.HttpService)}</li> <li>{@link
 * #activate(org.osgi.service.component.ComponentContext)}</li> </ol>
 *
 * <p>The bind method is invoked in order to give the component the information
 * it needs in order to be satisfied and then it is activated.</p>
 *
 * <p>When moving from ACTIVE to UNSATISFIED, i.e. when the HttpService is
 * deregistered then the methods are invoked in the following order:</p> <ol>
 * <li>{@link #deactivate(org.osgi.service.component.ComponentContext)}</li>
 * <li>{@link #unsetHttp(org.osgi.service.http.HttpService)}</li> </ol>
 *
 * <p>It is deactivated and then the unbind method is called to clear out any
 * information that is now invalid.</p>
 *
 * <p>If this component had an optional cardinality, i.e. it did not care
 * whether the HttpService was present then the order of the methods can be
 * reversed. The bind will happen after the activate if the HttpService is
 * started sometime after this bundle is started. Similarly, the unbind will
 * happen before the deactivate if the HttpService is stopped sometime before
 * this bundle is.</p>
 */
public class MAPActivator {

    public static final String LOCATION = "/map";

    private MAPServlet servlet;

    /**
     * The operation engine the servlet will use
     */
    private OperationEngine opEngine;

    /**
     * The HTTP service this servlet will regiser with
     */
    private HttpService service;

    /**
     * The OSGI bundle context.
     */
    private BundleContext context;

    protected void setHttp(HttpService service) {

        this.service = service;
        upwards();
    }

    protected void setOperationEngine(OperationEngine opEngine) {
        this.opEngine = opEngine;
    }

    protected void activate(final ComponentContext componentContext) {
        context = componentContext.getBundleContext();
        upwards();
    }


    private void upwards() {

        if (!isSatisfied()) {
            return;
        }

        if (servlet == null) {
            servlet = new MAPServlet();
        }

        servlet.setOperationEngine(opEngine);

        HttpContext httpContext = new HttpContext() {
            public boolean handleSecurity(HttpServletRequest request,
                                          HttpServletResponse response) {
                return true;
            }

            public URL getResource(String name) {
                return context.getBundle().getResource(name);
            }

            public String getMimeType(String name) {
                if (name.endsWith(".xslt")) {
                    return "text/xml";
                }
                return null;
            }
        };

        try {
            service.registerServlet(LOCATION,
                                    servlet, new Properties(),
                                    httpContext);
        } catch (NamespaceException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private boolean isSatisfied() {
        return context != null && service != null && opEngine != null;
    }

    protected void deactivate(ComponentContext context) {
        downwards();
        this.context = null;
    }

    protected void unsetHttp(HttpService service) {
        downwards();
        this.service = null;
    }

    protected void unsetOperationEngine(OperationEngine opEngine) {
        downwards();
        this.opEngine = null;
    }

    private void downwards() {
        service.unregister(LOCATION);
    }
}
