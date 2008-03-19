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
package com.volantis.mcs.service;

import com.volantis.shared.metadata.value.immutable.ImmutableStructureValue;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Abstract factory for creating {@link ServiceDefinition} instances.
 */
public abstract class ServiceDefinitionFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

     static {
        metaDefaultFactory = new MetaDefaultFactory(
                "com.volantis.mcs.service.impl.ServiceDefinitionFactoryImpl",
                ServiceDefinitionFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static ServiceDefinitionFactory getDefaultInstance() {
        return (ServiceDefinitionFactory)
                metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Method to create a {@link ServiceDefinition}
     * @param serviceName the name of the service
     * @param characteristics the services characteristics
     * @return a {@link ServiceDefinition} instance
     */
    public abstract ServiceDefinition createServiceDefintion(
            String serviceName,
            ImmutableStructureValue characteristics);
}
