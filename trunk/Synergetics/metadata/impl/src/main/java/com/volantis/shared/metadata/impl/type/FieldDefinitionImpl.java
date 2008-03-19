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
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataHelper;
import com.volantis.shared.metadata.impl.MetaDataObjectImpl;
import com.volantis.shared.metadata.impl.inhibitor.InhibitorImpl;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.type.FieldDefinition;
import com.volantis.shared.metadata.type.immutable.ImmutableMetaDataType;
import com.volantis.shared.security.acl.immutable.ImmutableACL;

/**
 * Implementation of {@link FieldDefinition}.
 */
abstract class FieldDefinitionImpl
        extends MetaDataObjectImpl
        implements FieldDefinition {

    /**
     * The name of the field.
     */
    private String name;

    /**
     * The type of the field.
     */
    protected ImmutableMetaDataType type;

    /**
     * The access control list.
     */
    protected ImmutableACL acl;

    /**
     * Protected constructor for sub classes.
     */
    protected FieldDefinitionImpl() {
    }

    /**
     * Protected constructor for Factory.
     * @param name The name of the field.
     */
    protected FieldDefinitionImpl(String name) {
        this.name = name;
    }

    /**
     * Copy constructor.
     *
     * @param fieldDefinition The object to copy.
     */
    protected FieldDefinitionImpl(FieldDefinition fieldDefinition) {
        this.name = fieldDefinition.getName();
        this.type = fieldDefinition.getType();
        this.acl = fieldDefinition.getACL();
    }

    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return new ImmutableFieldDefinitionImpl(this);
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return new MutableFieldDefinitionImpl(this);
    }

    // Javadoc inherited.
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the field definition.
     *
     * @note Added for JiBX only.
     *
     * @param name the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public ImmutableMetaDataType getType() {
        return type;
    }

    /**
     * Sets the type of the field definition.
     *
     * @note Added for JiBX only.
     *
     * @param type the new type
     */
    public void setType(final ImmutableMetaDataType type) {
        this.type = type;
    }

    // Javadoc inherited.
    public ImmutableACL getACL() {
        return acl;
    }

    // Javadoc inherited.
    public int hashCode() {
        return 19 * MetaDataHelper.hashCode(name)
                + MetaDataHelper.hashCode(type);
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof FieldDefinitionImpl)
                ? equalsFieldDefinition((FieldDefinitionImpl) other)
                : false;
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>NumberType</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>NumberType</code> are equal to this one.
     */
    protected boolean equalsFieldDefinition(FieldDefinitionImpl other) {
        return MetaDataHelper.equals(name, other.getName())
                && MetaDataHelper.equals(type, other.getType())
                && MetaDataHelper.equals(acl, other.getACL());
    }

    // Javado inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // get myself and ignore
        visitor.getNextEntry();
        EntryDAO entry = visitor.getNextEntry();
        if (!entry.isNull()) {
            name = entry.getName();
        }
        type = (ImmutableMetaDataType) visitor.getNextAsInhibitor();
        acl = (ImmutableACL) visitor.getNextAsInhibitor();
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.push(getClassMapper().toString(), getClassMapper());
        if (null == name) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            visitor.addString(name);
        }
        if (null == type) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) type).visitInhibitor(visitor);
        }
        // we cannot persist ACL's yet
        visitor.add(null, MetadataClassMapper.NULL, true);

        visitor.pop();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6560/7	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/3	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
