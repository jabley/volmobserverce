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
package com.volantis.synergetics.descriptorstore;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Properties;

/**
 * Factory class used to obtain ResourceDescriptorStore instances
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public abstract class ResourceDescriptorStoreFactory {

    /**
     * The default instance.
     */
    private static final ResourceDescriptorStoreFactory DEFAULTINSTANCE;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        try {
            ClassLoader loader = ResourceDescriptorStoreFactory.class.getClassLoader();
            Class implClass = loader.loadClass(
                "com.volantis.synergetics.descriptorstore.impl." +
                "DefaultResourceDescriptorStoreFactory");
            DEFAULTINSTANCE = (ResourceDescriptorStoreFactory) implClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static ResourceDescriptorStoreFactory getDefaultInstance() {
        return DEFAULTINSTANCE;
    }

    /**
     * Get a ResourceDescriptorStore instance.
     */
    public abstract ResourceDescriptorStore getResourceDescriptorStore();

    /**
     * Return a configuration store
     * @param properties
     * @return
     */
    public abstract ResourceDescriptorStore getResourceDescriptorStore(
        final Properties properties);

}
