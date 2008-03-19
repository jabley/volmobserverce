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

package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.impl.ImmutableGeneratingTypedMap;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.StructureValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Implementation of {@link StructureValue}.
 */
abstract class StructureValueImpl
        extends CompositeValueImpl
        implements StructureValue {

    /**
     * The contents of this Structure, this must never be exposed outside the object
     * without being wrapped.
     */
    private HashMap fieldValues;
    
    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected StructureValueImpl(StructureValue value) {
        this(new HashMap(value.getFieldValuesAsMap()));
    }

    /**
     * Protected constructor for sub classes.
     */
    protected StructureValueImpl() {
        this(new HashMap());
    }

    /**
     * Private constructor to perform initialisation common to all
     * constructors.
     */
    private StructureValueImpl(HashMap contents) {
        this.fieldValues = contents;
    }
 /**
     * Adds a new field to the structure.
     *
     * @note Added for JiBX only.
     *
     * @param field the new field to add
     */
    public void addField(StructureFieldValue field) {
        fieldValues.put(field.getName(), field.getValue());
    }

    /**
     * Returns an iterator over the fields.
     *
     * @note Added for JiBX only.
     */
    public Iterator getFieldValues() {
        return new FieldIterator(fieldValues.entrySet().iterator());
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableInhibitor createImmutable() {
        return new ImmutableStructureValueImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableInhibitor createMutable() {
        return new MutableStructureValueImpl(this);
    }

    /**
     * Returns a Map keyed on field name containing the {@link MetaDataValue}s.
     *
     * <p><strong>Note</strong>: Although the returned map is not itself
     * immutable if this method is called on a mutable instance then it is
     * possible that the underlying map may be changed through a mutable
     * representation of the map.</p>
     *
     * @return an unmodifiable {@link Map} of field values. If a
     * field value is empty then an entry will exist in the map with a null
     * value.
     */
    public Map getFieldValuesAsMap() {
        return Collections.unmodifiableMap(fieldValues);
    }

    /**
     * Get a String representation of this value.
     *
     * <p>
     * For simple types the value is returned as is e.g Boolean values are
     * represented by the strings "true" and "false"
     * </p>
     * <p>
     * For composite types an attempt is made to represent the value in as
     * sensible manner as is possible, no guarantee is made that it will be
     * possible to reconstruct the original value from the string
     * representation.
     * </p>
     * <p>
     * List types are returned space separated e.g "item1 item2 item3".
     * </p>
     * <p>
     * Structure types are returned as "[fieldname1=value1] [fieldname2=value2]"
     * wehere the values follow the rules above.
     * </p>
     *
     * @return an unmodifiable String value
     */
    public String getAsString() {
        Map map = getFieldValuesAsMap();
        String s;
        if (map.isEmpty()) {
            s = "";
        } else {
            StringBuffer buffer = new StringBuffer();
            for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                String key = (String) entry.getKey();
                MetaDataValue value = (MetaDataValue) entry.getValue();
                if (value != null) {
                    if (buffer.length() != 0) {
                        buffer.append(" ");
                    }
                    buffer.append("[");
                    buffer.append(key);
                    buffer.append("=");
                    buffer.append(value.getAsString());
                    buffer.append("]");
                }
            }
            s = buffer.toString();
        }

        return s;
    }

    /**
     * Get a mutable map keyed on field name containing
     * {@link com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue} values.
     *
     * <p>Modifications made to the returned map will change the state of this
     * object. The objects contained within the map are not themselves
     * mutable so if their state needs to be changed then a mutable copy will
     * need to be obtained, changed and then used to replace the original
     * object.</p>
     *
     * <p>The returned map only stores immutable instances of
     * {@link MetaDataValue}. If a user attempts to put a mutable instance
     * it then the map will obtain an immutable instance and store that
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
     * @return a mutable map of
     *         {@link com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue}
     *         instances.
     */
    public Map getFieldValuesAsMutableMap() {
        return new ImmutableGeneratingTypedMap(fieldValues,
                                               MetaDataValue.class, true);
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!(obj instanceof StructureValue)) {
            return false;
        }

        StructureValue other = (StructureValue) obj;

        return fieldValues.equals(other.getFieldValuesAsMap());
    }

    // Javadoc inherited.
    public int hashCode() {
        return fieldValues.hashCode();
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // fetch myself
        EntryDAO mySelf = visitor.getNextEntry();
        collectionInhibitorInitializerHelper(visitor, fieldValues);
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        // push myself
        visitor.push(getClassMapper().toString(), getClassMapper());
        collectionInhibitorVisitorHelper(visitor, fieldValues);
        visitor.pop();
    }

    /**
     * Field iterator to convert Map.Entry fields to StructureFieldValue.
     */
    private static class FieldIterator implements Iterator {
        private Iterator iter;

        public FieldIterator(final Iterator iter) {
            this.iter = iter;
        }

        public void remove() {
            iter.remove();
        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        public Object next() {
            final Map.Entry entry = (Map.Entry) iter.next();
            return new StructureFieldValue(
                (String) entry.getKey(), (MetaDataValue) entry.getValue());
        }
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

 14-Jan-05	6560/5	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/3	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
