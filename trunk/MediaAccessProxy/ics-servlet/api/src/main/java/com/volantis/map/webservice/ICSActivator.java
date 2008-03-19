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

import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

public class ICSActivator {

    private static final Properties PROPERTIES;

    public static final String LOCATION = "/ics";

    static {
        PROPERTIES = new Properties();
        PROPERTIES.setProperty("allow-non-secure-access", "true");
    }

    private ICSServlet servlet;

    /**
     * The HTTP service this servlet will regiser with
     */
    private HttpService service;

    /**
     * The OSGI bundle context.
     */
    private ComponentContext context;

    protected void setHttp(HttpService service) {

        this.service = service;
        upwards();
    }

    protected void activate(ComponentContext context) {
        this.context = context;
        upwards();
    }

    private void upwards() {

        if (!isSatisfied()) {
            return;
        }

        if (servlet == null) {
            servlet = new ICSServlet();
        }

        servlet.setComponentContext(context);

        HttpContext httpContext = new HttpContext() {
            public boolean handleSecurity(HttpServletRequest request,
                                          HttpServletResponse response) {
                return true;
            }

            public URL getResource(String name) {
                return context.getBundleContext().getBundle().getResource(name);
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
                                    servlet, PROPERTIES,
                                    httpContext);
        } catch (NamespaceException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private boolean isSatisfied() {
        return context != null && service != null;
    }

    protected void deactivate(ComponentContext context) {
        downwards();
        this.context = null;
    }

    protected void unsetHttp(HttpService service) {
        downwards();
        this.service = null;
    }

    private void downwards() {
        service.unregister(LOCATION);
    }
}
