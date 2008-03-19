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
import com.volantis.shared.metadata.impl.ImmutableGeneratingTypedSet;
import com.volantis.shared.metadata.impl.MetaDataHelper;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.type.FieldDefinition;
import com.volantis.shared.metadata.type.StructureType;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.StructureValue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;

/**
 * Implementation of {@link com.volantis.shared.metadata.type.StructureType}.
 */
abstract class StructureTypeImpl
        extends CompositeTypeImpl
        implements StructureType {

    /**
     * The contents of the set, this must never be exposed outside the object
     * without being wrapped.
     */
    private HashSet fields = new HashSet();

    /**
     * A mutable wrapper around the contents.
     *
     * <p>This does two additional things that a normal set does not do.
     * It checks to make sure that only instances of allowable types are
     * added and throws an exception otherwise. It also makes sure that only
     * immutable instances are added, creating one if necessary.</p>
     *
     * <p>This is marked as transient because it is not a fundamental part of
     * the object's state and does not need to be persisted.</p>
     */
    private transient Set mutableFields;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected StructureTypeImpl(StructureType value) {
        this(new HashSet(value.getFields()));
    }

    /**
     * Protected constructor for sub classes.
     */
    protected StructureTypeImpl() {
        this(new HashSet());
    }

    /**
     * Private constructor to perform initialisation common to all
     * constructors.
     */
    private StructureTypeImpl(HashSet contents) {
        this.fields = contents;
 }

    /**
     * Adds a new field definition to the structure.
     *
     * @note Added for JiBX only.
     *
     * @param fieldDefinition the field definition to add
     */
    public void addField(final FieldDefinition fieldDefinition) {
        fields.add(fieldDefinition);
        mutableFields = null;
    }

    /**
     * Returns the field definitions of this structure type.
     *
     * @note Added for JiBX only.
     *
     * @return an iterator over the current field definitions
     */
    public Iterator getFieldTypes() {
        return getFields().iterator();
    }

    // Javadoc inherited.
    public Set getFields() {
        return Collections.unmodifiableSet(getInternalFields());
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableInhibitor createImmutable() {
        return new ImmutableStructureTypeImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableInhibitor createMutable() {
        return new MutableStructureTypeImpl(this);
    }

    /**
     * Get a mutable set of
     * {@link com.volantis.shared.metadata.type.immutable.ImmutableFieldDefinition}
     * instances.
     *
     * <p>Modifications made to the returned set will change the state of this
     * object. The objects contained within the set are not themselves
     * mutable so if their state needs to be changed then a mutable copy will
     * need to be obtained, changed and then used to replace the original
     * object.</p>
     *
     * <p>The returned set only stores immutable instances of
     * {@link com.volantis.shared.metadata.type.FieldDefinition}. If a user attempts to add a mutable instance
     * to it then the set will obtain an immutable instance and store that
     * instead.</p>
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     *
     * @return a mutable set of
     * {@link com.volantis.shared.metadata.type.immutable.ImmutableFieldDefinition}
     * instances.
     */
    public Set getMutableFields() {

        // Create this lazily as it will never be needed for immutable
        // objects and it does not need to be synchronized as mutable objects
        // are not thread safe.
        if (mutableFields == null) {
            mutableFields = new ImmutableGeneratingTypedSet(
                getInternalFields(), FieldDefinition.class);
        }
        return mutableFields;
    }

    // Javadoc inherited.
    public int hashCode() {
        return MetaDataHelper.hashCode(getInternalFields());
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof StructureTypeImpl) &&
            equalsStructureType((StructureTypeImpl) other);
    }

    /**
     * Private access method
     */
    private HashSet getInternalFields() {
        return fields;
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>StructureType</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>StructureType</code> are equal to this one.
     */
    protected boolean equalsStructureType(StructureTypeImpl other) {
        return MetaDataHelper.equals(getInternalFields(), other.getInternalFields());
    }

    // javadoc inherited
    protected Class getExpectedValueType() {
        return StructureValue.class;
    }

    // Javadoc inherited.
    protected Collection verify(final MetaDataValue value, final String path) {
        final Collection errors = super.verify(value, path);
        if (value instanceof StructureValue) {
            final StructureValue structureValue = (StructureValue) value;
            final Map fieldValues = structureValue.getFieldValuesAsMap();
            final Set remainingFields = new HashSet(fieldValues.keySet());
            for (Iterator iter = fields.iterator(); iter.hasNext(); ) {
                final FieldDefinition fieldDef = (FieldDefinition) iter.next();
                final String fieldName = fieldDef.getName();
                final MetaDataTypeImpl fieldType =
                    (MetaDataTypeImpl) fieldDef.getType();
                final MetaDataValue fieldValue =
                    (MetaDataValue) fieldValues.get(fieldName);
                errors.addAll(
                    fieldType.verify(fieldValue, path + "/" + fieldName));
                remainingFields.remove(fieldName);
            }
            for (Iterator iter = remainingFields.iterator(); iter.hasNext(); ) {
                final String fieldName = (String) iter.next();
                errors.add(new VerificationError(
                    VerificationError.TYPE_UNEXPECTED_VALUE,
                    path + "/" + fieldName,
                    (MetaDataValue) fieldValues.get(fieldName), null,
                    "Unexpected field. Field '" + fieldName +
                        "' is not expected here."));
            }
        }
        return errors;
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // fetch myself
        EntryDAO mySelf = visitor.getNextEntry();
        collectionInhibitorInitializerHelper(visitor, fields);
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        // push myself
        visitor.push(getClassMapper().toString(), getClassMapper());
        collectionInhibitorVisitorHelper(visitor, fields);
        visitor.pop();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6560/9	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/7	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/5	tom	VBM:2004122401 Commit prior to creating Inhibitor baseclass

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
