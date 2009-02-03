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
package com.volantis.shared.metadata.impl.inhibitor;

import com.volantis.shared.inhibitor.Inhibitor;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public abstract class InhibitorImpl implements Inhibitor {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(InhibitorImpl.class);

    public void visitInhibitor(MetadataDAOVisitor visitor) {
        throw new UnsupportedOperationException(
            EXCEPTION_LOCALIZER.format(
                "class-not-persistable", getClass().getName()));
    }

    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        throw new UnsupportedOperationException(
            EXCEPTION_LOCALIZER.format(
                "class-not-persistable", getClass().getName()));
    }

    /**
     * Override this for any persisteable classes
     *
     * @return a class mapper instance representing the actual class.
     */
    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.UNSUPPORTED;
    }

    /**
     * Helper for handling collections from MetaDataObjects
     *
     * @param visitor the visitor to populate
     * @param col the collection to persist
     */
    protected static void collectionInhibitorVisitorHelper(
        MetadataDAOVisitor visitor, Collection col) {
        visitor.add(Integer.toString(col.size()), MetadataClassMapper.NULL, false);
        Iterator it = col.iterator();
        int i = 0;
        while (it.hasNext()) {
            InhibitorImpl inhibitor = (InhibitorImpl) it.next();
            visitor.push("" + i, MetadataClassMapper.NULL);
            //visitor.push("" + i, inhibitor.getClassMapper());
            inhibitor.visitInhibitor(visitor);
            visitor.pop();
            i++;
        }
    }

    /**
     * Helper for handling collections from MetaDataObjects
     *
     * @param visitor the visitor to peruse
     * @param col the collection to initialize
     */
    protected static void collectionInhibitorInitializerHelper(
        MetadataDAOVisitor visitor, Collection col) {

        int size = Integer.parseInt(visitor.getNextEntry().getName());
        for (int i=0; i<size; i++) {
            visitor.getNextEntry(); // pop the index value
            col.add(visitor.getNextAsInhibitor());
        }
    }

    /**
     * Helper for handling collections from MetaDataObjects
     *
     * @param visitor the visitor to populate
     * @param map     the collection to persist must map String to Inhibitor
     */
    protected static void collectionInhibitorVisitorHelper(
        MetadataDAOVisitor visitor, Map map) {
        visitor.add(Integer.toString(map.size()),
                    MetadataClassMapper.NULL,
                    false);
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String name = (String) entry.getKey();
            InhibitorImpl inhibitor = (InhibitorImpl) entry.getValue();
            visitor.push(name, inhibitor.getClassMapper());
            inhibitor.visitInhibitor(visitor);
            visitor.pop();
        }
    }

    /**
     * Helper for handling collections from MetaDataObjects
     *
     * @param visitor the visitor to peruse
     * @param map     the collection to initialize
     */
    protected static void collectionInhibitorInitializerHelper(
        MetadataDAOVisitor visitor, Map map) {

        int size = Integer.parseInt(visitor.getNextEntry().getName());
        for (int i = 0; i < size; i++) {
            EntryDAO dao = visitor.getNextEntry();
            map.put(dao.getName(), visitor.getNextAsInhibitor());
        }
    }
}
