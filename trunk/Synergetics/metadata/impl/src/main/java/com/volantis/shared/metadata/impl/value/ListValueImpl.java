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
import com.volantis.shared.metadata.impl.ImmutableGeneratingTypedList;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.value.ListValue;
import com.volantis.shared.metadata.value.MetaDataValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link ListValue}.
 */
abstract class ListValueImpl
        extends CollectionValueImpl
        implements ListValue {

    /**
     * The contents of the set, this must never be exposed outside the object
     * without being wrapped.
     *
     * <p>This is marked as transient because it is not a fundamental part of
     * the object's state and does not need to be persisted. The actual field 
     * being persisted is flatContents.</p>
     */
    private transient ArrayList contents;

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
    private transient List mutableContents;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected ListValueImpl(ListValue value) {
        this(new ArrayList(value.getContentsAsCollection()));
    }

    /**
     * Protected constructor for sub classes.
     */
    protected ListValueImpl() {
        this(new ArrayList());
    }

    /**
     * Private constructor to perform initialisation common to all
     * constructors.
     */
    private ListValueImpl(ArrayList contents) {
        this.contents = contents;
    }

    /**
     * Adds a new metadata value to the list.
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
        return getContentsAsList();
    }

    // Javadoc inherited.
    public List getContentsAsList() {
        return Collections.unmodifiableList(contents);
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableInhibitor createImmutable() {
        return new ImmutableListValueImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableInhibitor createMutable() {
        return new MutableListValueImpl(this);
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
    public List getContentsAsMutableList() {

        // Create this lazily as it will never be needed for immutable
        // objects and it does not need to be synchronized as mutable objects
        // are not thread safe.
        if (mutableContents == null) {
            mutableContents = new ImmutableGeneratingTypedList(
                    contents, MetaDataValue.class);
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

        return getContentsAsMutableList();
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!(obj instanceof ListValue)) {
            return false;
        }

        ListValue other = (ListValue) obj;

        return getContentsAsList().equals(other.getContentsAsList());
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

 17-Jan-05	6560/9	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/7	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/5	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
