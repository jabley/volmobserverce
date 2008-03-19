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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.metadata;

import com.volantis.shared.metadata.type.MetaDataTypeFactory;
import com.volantis.shared.metadata.value.MetaDataValueFactory;
import com.volantis.shared.security.acl.ACLFactory;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * An object for creating instances of meta data classes.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User extensions of this class are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class MetaDataFactory {

    /**
     * The default instance.
     */
    private static final MetaDataFactory defaultInstance;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        try {
            ClassLoader loader = MetaDataFactory.class.getClassLoader();
            Class implClass = loader.loadClass("com.volantis.shared.metadata.impl.DefaultMetaDataFactory");
            defaultInstance = (MetaDataFactory) implClass.newInstance();
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
    public static MetaDataFactory getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Get the factory for creating <code>MetaDataType</code> related classes.
     *
     * @return The factory for creating <code>MetaDataType</code> related
     * classes.
     */
    public abstract MetaDataTypeFactory getTypeFactory();

    /**
     * Get the factory for creating <code>MetaDataValue</code> related classes.
     *
     * @return The factory for creating <code>MetaDataValue</code> related
     * classes.
     */
    public abstract MetaDataValueFactory getValueFactory();

    /**
     * Get the factory for creating <code>ACL</code> related
     * classes.
     * @return The factory for creating <code>ACL</code> related
     * classes.
     */
    public abstract ACLFactory getACLFactory();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
