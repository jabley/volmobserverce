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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.type.FieldDefinition;
import com.volantis.shared.metadata.type.immutable.ImmutableFieldDefinition;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

import java.io.Serializable;

/**
 * Implementation of {@link ImmutableFieldDefinition}.
 */
final class ImmutableFieldDefinitionImpl
        extends FieldDefinitionImpl
        implements ImmutableFieldDefinition, Serializable {

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = 4246239316408891803L;

    /**
     * Copy constructor.
     *
     * @param fieldDefinition The object to copy.
     */
    public ImmutableFieldDefinitionImpl(FieldDefinition fieldDefinition) {
        super(fieldDefinition);
    }

    /**
     * Protected constructor for future use by JDO.
     */
    protected ImmutableFieldDefinitionImpl() {
    }

    /**
     * Public constructor for Factory.
     * @param name The name of the field.
     */
    public ImmutableFieldDefinitionImpl(String name) {
        super(name);
    }

    /**
     * Override to return this object rather than create a new one.
     *
     * <p>This is simply a performance optimisation and has no impact on the
     * behaviour.</p>
     */
    public ImmutableInhibitor createImmutable() {
        return this;
    }

    // Javadoc inherited
    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.IMMUTABLE_FIELD_DEFINITION;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/7	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/3	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
