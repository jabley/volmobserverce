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

import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;

/**
 * Interface to that provides access to a service. A service is a forest of
 * name value pairs that are grouped together
 *
 * @mock.generate 
 */
public interface ServiceDefinition {

    /**
     * Default ServiceDefinition
     */
    static ServiceDefinition DEFAULT_SERVICE_DEFINITION =
            new DefaultServiceDefinition();
    /**
     * To be used as a key when storing/retrieving implematations of this
     * from data structures
     */
    static final String SERVICE_DEFINITION_KEY =
            ServiceDefinition.class.getName();
    /**
     * Returns the name of the Service
     * @return the name of the service
     */
    String getName();

    /**
     * Returns the characteristics for the given path
     * @param path path to the characteristic
     * @return the SimpleValue of the Characteristic
     * @throws CharacteristicNotAvailableException if the path does
     * not point to a characteristic
     */
    ImmutableMetaDataValue getCharacteristic(String path)
            throws CharacteristicNotAvailableException;


    /**
     * Returns the transformation specified by the service.
     * @return a String that is the url to the transform
     */
    String getTransform();

    /**
     * Returns the remote target prefix URI
     * @return a String that is the url of the remote target
     */
    String getRemoteTarget();

}
