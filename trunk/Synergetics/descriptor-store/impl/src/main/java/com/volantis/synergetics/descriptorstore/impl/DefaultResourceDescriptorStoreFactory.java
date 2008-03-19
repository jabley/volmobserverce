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
package com.volantis.synergetics.descriptorstore.impl;

import com.volantis.synergetics.descriptorstore.ResourceDescriptorStore;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStoreFactory;
import com.volantis.synergetics.descriptorstore.ParameterNames;
import com.volantis.synergetics.descriptorstore.Parameters;
import com.volantis.synergetics.descriptorstore.impl.jdo.JDOResourceDescriptorStore;

import java.util.Properties;

/**
 * Default ResourceDescriptorStore factory implementation. This returns JDO
 * compatible objects
 */
public class DefaultResourceDescriptorStoreFactory extends ResourceDescriptorStoreFactory {

    // javadoc inherited
    public ResourceDescriptorStore getResourceDescriptorStore() {
        return new JDOResourceDescriptorStore();
    }

    // javadoc inherited
    public ResourceDescriptorStore getResourceDescriptorStore(
        final Properties properties) {
        return new JDOResourceDescriptorStore(properties);
    }

}
