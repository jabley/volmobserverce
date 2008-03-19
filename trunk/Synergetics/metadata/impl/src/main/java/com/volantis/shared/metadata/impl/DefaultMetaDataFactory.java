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

package com.volantis.shared.metadata.impl;

import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.shared.metadata.InternalMetaDataFactory;
import com.volantis.shared.metadata.impl.type.DefaultMetaDataTypeFactory;
import com.volantis.shared.metadata.impl.value.DefaultMetaDataValueFactory;
import com.volantis.shared.metadata.impl.value.ImmutableStructureValueImpl;
import com.volantis.shared.metadata.type.MetaDataTypeFactory;
import com.volantis.shared.metadata.value.MetaDataValueFactory;
import com.volantis.shared.security.acl.ACLFactory;
import com.volantis.shared.security.SecurityFactory;

/**
 * Default implementation of {@link MetaDataFactory}.
 */
public class DefaultMetaDataFactory
        extends InternalMetaDataFactory {

    /**
     * Factory for creating meta data type objects.
     */
    private final MetaDataTypeFactory typeFactory;

    /**
     * Factory for creating meta data value objects.
     */
    private final MetaDataValueFactory valueFactory;

    /**
     * Factory for creating ACL objects.
     */
    private final ACLFactory aclFactory;

    /**
     * Default constructor invoked by reflection from the public API.
     */
    public DefaultMetaDataFactory() {
        typeFactory = new DefaultMetaDataTypeFactory();
        valueFactory = new DefaultMetaDataValueFactory();
        aclFactory = SecurityFactory.getDefaultInstance().getACLFactory();
    }

    // Javadoc inherited.
    public MetaDataTypeFactory getTypeFactory() {
        return typeFactory;
    }

    // Javadoc inherited.
    public MetaDataValueFactory getValueFactory() {
        return valueFactory;
    }

    // Javadoc inherited.
    public ACLFactory getACLFactory() {
        return aclFactory;
    }

    // Javadoc inherited.
    public Class getJiBXFactoryClass() {
        return ImmutableStructureValueImpl.class;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/5	tom	VBM:2004122401 Changed Javadoc

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
