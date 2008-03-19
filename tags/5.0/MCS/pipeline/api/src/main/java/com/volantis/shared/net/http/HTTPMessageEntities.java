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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.http;

import java.util.Iterator;
import java.io.Serializable;

/**
 * A container for HTTPMessageEntity objects.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface HTTPMessageEntities extends Serializable {

    /**
     * Put a HTTPMessageEntity into this HTTPMessageEntities. If a
     * HTTPMessageEntity with the same identity already exists in this
     * HTTPMessageEntities then it will be replaced by be the given
     * HTTPMessageEntity and the replaced HTTPMessageEntity(s) will be
     * returned. If multiple instances of the specified entity already exist
     * then all of these existing instances will be replaced by the single
     * entity provided.
     * @param entity The HTTPMessageEntity.
     * @return The replaced HTTPMessageEntity objects or null if none were
     * replaced.
     * @see #put(com.volantis.shared.net.http.HTTPMessageEntity [] entities)
     */
    public HTTPMessageEntity[] put(HTTPMessageEntity entity);

    /**
     * Provide an Iterator over the HTTPMessageEntity objects in this
     * HTTPMessageEntities.
     * @return An HTTPMessageEntity Iterator
     */
    public Iterator iterator();

    /**
     * Provide an Iterator over the names of the HTTPMessageEntity objects
     * contained in this HTTPMessageEntities.
     * @return A names Iterator where each name is a String.
     */
    public Iterator namesIterator();

    /**
     * Remove a HTTPMessageEntity from this HTTPMessageEntities. If
     * multiple instances of the named HTTPMessageEntity exist then all
     * will be removed.
     * @param identity The identity of the entity to remove.
     * @return The removed HTTPMessageEntity objects or null if none were
     * removed.
     */
    public HTTPMessageEntity[] remove(HTTPMessageEntityIdentity identity);

    /**
     * Clear all HTTPMessageEntity objects from the HTTPMessageEntities.
     */
    public void clear();

    /**
     * Retrieve all the instances of a named HTTPMessageEntity in this
     * HTTPMessageEntities.
     * @param identity The identity of the entity to retrieve.
     * @return All the instances of the named HTTPMessageEntity or null
     * if none exist.
     */
    public HTTPMessageEntity[] retrieve(HTTPMessageEntityIdentity identity);

    /**
     * Determine whether a named HTTPMessageEntity is contained within this
     * HTTPMessageEntities.
     * @param identity The identity of the entity to test.
     * @return true if one or more instances of the name HTTPMessageEntity
     * exits; false otherwise.
     */
    public boolean containsIdentity(HTTPMessageEntityIdentity identity);

    /**
     * Add a TransmissionProperty to the HTTPMessageEntities.
     * HTTPMessageEntity objects with duplicate identities as the specifed
     * HTTPMessageEntity are ignored i.e. this method allows the addition
     * of HTTPMessageEntity objects with duplicate identities. However,
     * duplicate objects (those with all properties equal) will not be added.
     * @param entity The HTTPMessageEntity to add to this
     * HTTPMessageEntities.
     */
    public void add(HTTPMessageEntity entity);

    /**
     * Put an array of HTTPMessageEntity objects to the
     * HTTPMessageEntities. If a HTTPMessageEntity with the same identity
     * as one of the given HTTPMessageEntity objects already exists in this
     * HTTPMessageEntities then it will be replaced by be the given
     * HTTPMessageEntity and the replaced HTTPMessageEntity(s) will be
     * returned. If multiple instances of one of the given HTTPMessageEntity
     * object already exist then all of these existing instances will be
     * replaced by the single entity provided. All the HTTPMessageEntity
     * objects in the given array will be individually added not put i.e. if
     * the array contains entries that have duplicate identities these duplicate
     * entries will be added to this HTTPMessageEntities.
     * @param entities An array of HTTPMessageEntity objects to put into
     * this HTTPMessageEntities.
     * @return All of the HTTPMessageEntity objects that were replaced or
     * null if none were replaced.
     */
    public HTTPMessageEntity[] put(HTTPMessageEntity[] entities);

    /**
     * Provide an Iterator over the values for named HTTPMessageEntity.
     * @param identity The identity of the entity whose values to iterate.
     * @return An values Iterator where each value is a String.
     */
    public Iterator valuesIterator(HTTPMessageEntityIdentity identity);

    /**
     * Return the number of HTTPMessageEntity objects is this
     * HTTPMessageEntities.
     * @return The number of HTTPMessageEntities in this
     * HTTPMessageEntities.
     */
    public int size();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	217/11	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/9	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 31-Jul-03	217/7	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/5	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 ===========================================================================
*/
