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

package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.impl.ImmutableGeneratingTypedList;
import com.volantis.shared.metadata.impl.inhibitor.InhibitorImpl;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.type.constraint.EnumeratedConstraint;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.SimpleValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link com.volantis.shared.metadata.type.constraint.EnumeratedConstraint}.
 */
class EnumeratedConstraintImpl extends ConstraintImpl implements EnumeratedConstraint {

    /**
     * A List of enumerated values, this must never be exposed outside the object
     * without being wrapped.
     */
    private ArrayList enumeratedValues;

    /**
     * A mutable wrapper around the contents.
     *
     * <p>This does two additional things that a normal list does not do.
     * It checks to make sure that only instances of allowable types are
     * added and throws an exception otherwise. It also makes sure that only
     * immutable instances are added, creating one if necessary.</p>
     *
     * <p>This is marked as transient because it is not a fundamental part of
     * the object's state and does not need to be persisted.</p>
     */
    private transient List mutableEnumeratedValues;

    /**
     * Copy constructor.
     *
     * @param enumeratedConstraint The object to copy.
     */
    protected EnumeratedConstraintImpl(EnumeratedConstraint enumeratedConstraint) {
        this(new ArrayList(enumeratedConstraint.getEnumeratedValues()));
    }

    /**
     * Protected constructor for sub classes.
     */
    protected EnumeratedConstraintImpl() {
        this(new ArrayList());
    }

    /**
     * Private constructor to perform initialisation common to all
     * constructors.
     */
    private EnumeratedConstraintImpl(ArrayList enumeratedValues) {
        this.enumeratedValues = enumeratedValues;
    }

    /**
     * Implementation of mutator.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     */
    public List getMutableEnumeratedValues() {

        // Create this lazily as it will never be needed for immutable
        // objects and it does not need to be synchronized as mutable objects
        // are not thread safe.
        if (mutableEnumeratedValues == null) {
            mutableEnumeratedValues =
                    new ImmutableGeneratingTypedList(enumeratedValues, SimpleValue.class);
        }
        return mutableEnumeratedValues;
    }

    // Javadoc inherited.
    public List getEnumeratedValues() {
        return Collections.unmodifiableList(enumeratedValues);
    }


    /**
     * Adds a new metadata value to the list.
     *
     * @note Added for JiBX only.
     *
     * @param value the new value to add
     */
    public void addValue(final MetaDataValue value) {
        enumeratedValues.add(value);
    }

    /**
     * Returns an iterator over the values.
     *
     * @note Added for JiBX only.
     */
    public Iterator getValues() {
        return enumeratedValues.iterator();
    }
    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableInhibitor createImmutable() {
        return new ImmutableEnumeratedConstraintImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableInhibitor createMutable() {
        return new MutableEnumeratedConstraintImpl(this);
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof EnumeratedConstraint)
                ? equalsEnumeratedConstraint((EnumeratedConstraint) other)
                : false;
    }

    protected boolean equalsEnumeratedConstraint(EnumeratedConstraint other) {
        return enumeratedValues.equals(other.getEnumeratedValues());
    }

    // Javadoc inherited.
    public int hashCode() {
        return enumeratedValues.hashCode();
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // fetch myself
        EntryDAO mySelf = visitor.getNextEntry();
        collectionInhibitorInitializerHelper(visitor, enumeratedValues);
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        // push myself
        visitor.push(getClassMapper().toString(), getClassMapper());
        collectionInhibitorVisitorHelper(visitor, enumeratedValues);
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

 13-Jan-05	6560/3	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
