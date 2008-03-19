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
package com.volantis.map.operation.impl;

import com.volantis.map.operation.ObjectParameters;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.operation.ResourceDescriptorFactory;

/**
 * Return a default Parameters implementation
 */
public class DefaultResourceDescriptorFactory extends ResourceDescriptorFactory {

    // javadoc inherited
    public ObjectParameters createParameters() {
        return new ProxiedParameters();
    }

    // javadoc inherited
    public ResourceDescriptor createDescriptor(String externalID,
                                               String resourceType) {
        return new DefaultResourceDescriptor(externalID, resourceType);
    }

}
