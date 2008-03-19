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
 *  {@link ServiceDefinition} used to implement a "Null" defintion
 */
public class DefaultServiceDefinition implements ServiceDefinition {

    // javadoc inherited
    public String getName() {
        return "Default Service";
    }

    // javadoc inherited
    public ImmutableMetaDataValue getCharacteristic(String path)
            throws CharacteristicNotAvailableException {
        throw new UnsupportedOperationException(
                "DefaultServiceDefinition#getName not supported");
    }

    // javadoc inherited
    public String getTransform() {
           throw new UnsupportedOperationException(
                "DefaultServiceDefinition#getTransform not supported");

    }

    // javadoc inherited
    public String getRemoteTarget() {
       throw new UnsupportedOperationException(
                "DefaultServiceDefinition#getRemoteTarget not supported");
    }
}
