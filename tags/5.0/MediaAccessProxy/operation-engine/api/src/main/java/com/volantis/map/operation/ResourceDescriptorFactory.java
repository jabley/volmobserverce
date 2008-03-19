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
package com.volantis.map.operation;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Use to create empty Parameter instances for use by the ICS front end.
 */
public abstract class ResourceDescriptorFactory {

    private static final MetaDefaultFactory instance =
        new MetaDefaultFactory("com.volantis.map.operation.impl." +
                               "DefaultResourceDescriptorFactory",
                               ResourceDescriptorFactory.class.getClassLoader());

    /**
     * Creates a new empty parameter block.
     * @return Parameters - an empty paramters block
     */
    public abstract ObjectParameters createParameters();

    /**
     * Return an empty resource descriptor with the specified external ID and
     * resource Type.
     *
     * @param externalID
     * @param resourceType
     * @return
     */
    public abstract ResourceDescriptor createDescriptor(String externalID,
                                                        String resourceType);

    /**
     * Gets an instance of ParameterBuilder.
     *
     * @return ParameterBuilder - an instance of the factory.
     */
    public static ResourceDescriptorFactory getInstance() {
        return (ResourceDescriptorFactory) instance.getDefaultFactoryInstance();
    }

}
