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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.testtools.mock.libraries.org.osgi.framework;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * Triggers auto generation of classes within <code>org.osgi.framework</code>
 * and contained packages for which the source is not available.
 *
 * @mock.generate library="true"
 */
public class OSGiFrameworkLibrary {

    /**
     * @mock.generate interface="true"
     */
    public Bundle bundle;

    /**
     * @mock.generate interface="true"
     */
    public BundleContext bundleContext;

    /**
     * @mock.generate interface="true"
     */
    public ServiceReference serviceReference;

    /**
     * @mock.generate interface="true"
     */
    public ServiceRegistration serviceRegistration;

    /**
     * @mock.generate interface="true"
     */
    public Filter filter;

    /**
     * @mock.generate interface="true"
     */
    public ServiceListener serviceListener;
}
