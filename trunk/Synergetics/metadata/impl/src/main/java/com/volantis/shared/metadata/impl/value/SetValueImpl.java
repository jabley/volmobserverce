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

package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.impl.ImmutableGeneratingTypedSet;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.SetValue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of {@link SetValue}.
 *
 * <p><strong>Note</strong>: This is package private so that it cannot be
 * accessed from outside this package. This is primarily to protect the
 * mutator method(s) from being called on an immutable instance of this
 * class. All access to these mutators must be done through the relevant
 * mutable interface and not directly via this class.</p>
 */
abstract class SetValueImpl
        extends CollectionValueImpl
        implements SetValue {

    /**
     * The contents of the set, this must never be exposed outside the object
     * without being wrapped.
     *
     * <p>This is marked as transient because it is not a fundamental part of
     * the object's state and does not need to be persisted. The actual field 
     * being persisted is flatContents.</p>
     */
    private transient Set contents;

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
    private transient Set mutableContents;
    
    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected SetValueImpl(SetValue value) {
        this(new HashSet(value.getContentsAsCollection()));
    }

    /**
     * Protected constructor for sub classes.
     */
    protected SetValueImpl() {
        this(new HashSet());
    }

    /**
     * Private constructor to perform initialisation common to all
     * constructors.
     */
    private SetValueImpl(HashSet contents) {
        this.contents = contents;
    }
    /**
     * Adds a new metadata value to the set.
     *
     * @note Added for JiBX only.
     *
     * @param value the new value to add
     */
    public void addValue(final MetaDataValue value) {
        contents.add(value);
    }

    /**
     * Returns an iterator over the values.
     *
     * @note Added for JiBX only.
     */
    public Iterator getValues() {
        return contents.iterator();
    }

    // Javadoc inherited.
    public Collection getContentsAsCollection() {
        return getContentsAsSet();
    }

    // Javadoc inherited.
    public Set getContentsAsSet() {
        return Collections.unmodifiableSet(contents);
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableInhibitor createImmutable() {
        return new ImmutableSetValueImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableInhibitor createMutable() {
        return new MutableSetValueImpl(this);
    }

    /**
     * Get a mutable set of
     * {@link com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue} instances.
     *
     * <p>Modifications made to the returned set will change the state of this
     * object. The objects contained within the set are not themselves
     * mutable so if their state needs to be changed then a mutable copy will
     * need to be obtained, changed and then used to replace the original
     * object.</p>
     *
     * <p>The returned set only stores immutable instances of
     * {@link com.volantis.shared.metadata.value.MetaDataValue}. If a user attempts to add
     * a mutable instance to it then the set will obtain an immutable instance and store
     * that instead.</p>
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     */
    public Set getContentsAsMutableSet() {

        // Create this lazily as it will never be needed for immutable
        // objects and it does not need to be synchronized as mutable objects
        // are not thread safe.
        if (mutableContents == null) {
            mutableContents = new ImmutableGeneratingTypedSet(contents, MetaDataValue.class);
        }

        return mutableContents;
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
    public Collection getContentsAsMutableCollection() {

        return getContentsAsMutableSet();
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!(obj instanceof SetValue)) {
            return false;
        }

        SetValue other = (SetValue) obj;

        return contents.equals(other.getContentsAsSet());
    }

    // Javadoc inherited.
    public int hashCode() {
        return contents.hashCode();
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // fetch myself
        EntryDAO mySelf = visitor.getNextEntry();
        collectionInhibitorInitializerHelper(visitor, contents);
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        // push myself
        visitor.push(getClassMapper().toString(), getClassMapper());
        collectionInhibitorVisitorHelper(visitor, contents);
        visitor.pop();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/11	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/9	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/7	tom	VBM:2004122401 Commit prior to creating Inhibitor baseclass

 13-Jan-05	6560/5	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
