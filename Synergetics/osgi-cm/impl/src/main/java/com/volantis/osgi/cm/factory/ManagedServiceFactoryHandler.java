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

package com.volantis.osgi.cm.factory;

import com.volantis.osgi.cm.AbstractManagedServiceHandler;
import com.volantis.osgi.cm.ConfigurationManager;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ManagedServiceFactory;

/**
 * Forwards service events for {@link ManagedServiceFactory} instances through
 * to the {@link ConfigurationManager}.
 */
public class ManagedServiceFactoryHandler
        extends AbstractManagedServiceHandler {

    /**
     * The name of the class whose events this will receive.
     */
    private static final String MANAGED_SERVICE_FACTORY_CLASS =
            ManagedServiceFactory.class.getName();

    /**
     * Initialise.
     *
     * @param manager The {@link ConfigurationManager}.
     */
    public ManagedServiceFactoryHandler(ConfigurationManager manager) {
        super(manager);
    }

    // Javadoc inherited.
    public String getRegisteredClass() {
        return MANAGED_SERVICE_FACTORY_CLASS;
    }

    // Javadoc inherited.
    public void serviceRegistered(ServiceReference reference) {
        manager.factoryRegistered(reference);
    }

    // Javadoc inherited.
    public void serviceModified(ServiceReference reference) {
        manager.factoryModified(reference);
    }

    // Javadoc inherited.
    public void serviceUnregistering(ServiceReference reference) {
        manager.factoryUnregistering(reference);
    }
}
