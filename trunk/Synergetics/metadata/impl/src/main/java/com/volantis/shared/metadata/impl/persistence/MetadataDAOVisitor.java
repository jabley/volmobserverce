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
package com.volantis.shared.metadata.impl.persistence;

import com.volantis.shared.metadata.impl.MetaDataObjectImpl;
import com.volantis.shared.metadata.impl.inhibitor.InhibitorImpl;
import com.volantis.synergetics.path.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the core of the flattened persistence mechanism. It is passed to
 * InhibitorImpl objects for them to persist and unpersist themselves.
 */
public class MetadataDAOVisitor {

    /**
     * the List of EntryDAO objects that will be persisted
     */
    private final List entries;

    /**
     * The current path to the entryDAO
     */
    private Path currentPath = Path.parse("");

    /**
     * The index used to retrieve data.
     */
    private int retrieveIndex = 0;

    /**
     * Create an empty visitor ready for persistence
     */
    MetadataDAOVisitor() {
        this.entries = new ArrayList();
    }

    /**
     * Create from a bundleDAO object
     * @param bundle
     */
    MetadataDAOVisitor(BundleDAO bundle) {
        if (bundle == null) {
            this.entries = new ArrayList();
        } else {
            this.entries = bundle.getEntries();
        }
    }

    /**
     * Pop the current path fragment
     */
    public void pop() {
        currentPath = currentPath.getParent();
    }

    /**
     * Push adds a path fragment to the current path. This can be removed using
     * pop.
     *
     * @param name
     * @param mapper
     */
    public void push(String name, MetadataClassMapper mapper) {
        if (null == name) {
            name = "null";
        }
        currentPath = currentPath.resolve(name);
        internalAdd(currentPath, mapper, false);
    }

    /**
     * Internal implementation of the add method.
     *
     * @param path the full path to add an entry at
     * @param mapper the class mapper instance to use
     * @param isNull if the node has a null value
     */
    private void internalAdd(Path path, MetadataClassMapper mapper,
                             boolean isNull) {

        entries.add(new EntryDAO(path.asRelativeString(),
                                 mapper.toString(),
                                 isNull));
    }

    /**
     * Add performs the equivelent of a push/pop operation for convienience
     *
     * @param value the path value/name to add
     * @param mapper the type of the class
     * @param isNull true if the value/class is null
     */
    public void add(String value,
                    MetadataClassMapper mapper,
                    boolean isNull) {
        if (null == value) {
            value = "null";
        }

        internalAdd(currentPath.resolve(value), mapper, isNull);
    }

    /**
     * Add a string value this may be null
     * @param value
     */
    public void addString(String value) {
        if (null == value) {
            internalAdd(currentPath.resolve("null"), MetadataClassMapper.NULL,
                        true);
        } else {
            internalAdd(currentPath.resolve(value), MetadataClassMapper.NULL, false);
        }
    }

    /**
     * Return a list of the EntryDAOs this obejct contains
     *
     * @return a list of the EntryDAOs this obejct contains
     */
    public List getEntries() {
        return entries;
    }

    /**
     * Retrieve the next EntryDAO object from the visitor.
     *
     * @return the next EntryDAO object from the visitor.
     */
    public EntryDAO getNextEntry() {
        EntryDAO result = (EntryDAO) entries.get(retrieveIndex);
        retrieveIndex++;
        return result;
    }

    /**
     * Return true if there is another entry in this visitor
     *
     * @return true if there is another entry in this visitor
     */
    public boolean hasNextEntry() {
        return retrieveIndex < entries.size();
    }

    /**
     * Return the inhibitor corresponding to the next entry. The inhibitor that
     * is returned will already have been initialized. May return null. This
     * leaves the MetadataDAOVisitor at the location of the last entry required
     * to populate the inhibitor
     *
     * @return
     */
    public InhibitorImpl getNextAsInhibitor() {
        InhibitorImpl result = null;
        if (hasNextEntry()) {
            // Note we do NOT incerement the index here as the initialize
            // Inhibitor might need access to its "self" element.
            EntryDAO entry = (EntryDAO) entries.get(retrieveIndex);
            if (!entry.isNull()) {
                MetaDataObjectImpl value = (MetaDataObjectImpl)
                    MetadataClassMapper.literal(entry.getClassName())
                        .getInstance();
                if (null == value) {
                    throw new IllegalStateException(
                        "MetaDataObjectMapper returned null for an " +
                            "entry marked as non-null. classname = " +
                            entry.getClassName() + " entry number = " +
                            retrieveIndex + " path = " + entry.getPath());
                }
                value.initializeInhibitor(this);
                result = value;
            } else {
                // ensure the retrieve index is incremented correctly
                getNextEntry();
            }
        }
        return result;
    }

    /**
     * Return the next entry as a String or null, This increments the position
     * by one
     *
     * @return the next entry as a string or null
     */
    public String getNextAsString() {
        String result = null;
        if (hasNextEntry()) {
            EntryDAO entry = getNextEntry();
            if (!entry.isNull()) {
                result = entry.getName();
            } 
        }
        return result;
    }

}
