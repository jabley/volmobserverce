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


package com.volantis.mcs.cache.impl;

import com.volantis.mcs.cache.CacheIdentity;
import com.volantis.mcs.cache.CacheEntry;
import com.volantis.mcs.cache.CacheTime;


import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides the default implementation of a @link CacheStore. This
 * implementation is purely in memory.
 */
public class DefaultCacheStore extends AbstractCacheStore {

    /**
     * Static comparator used to compare cache identities by 
     * expiresTime and sequenceNo attributes.
     */
    private final static Comparator comparator = new Comparator() {
        public int compare(Object object1, Object object2) {
            CacheIdentity identity1 = (CacheIdentity) object1;
            CacheIdentity identity2 = (CacheIdentity) object2;
            
            long expiresTime1 = identity1.getExpiresTime();
            long expiresTime2 = identity2.getExpiresTime();
            
            if (expiresTime1 < expiresTime2) {
                return -1;
            } else if (expiresTime1 == expiresTime2) {
                long sequenceNo1 = identity1.getSequenceNo();
                long sequenceNo2 = identity2.getSequenceNo();
                
                if (sequenceNo1 < sequenceNo2) {
                    return -1;
                } else if (sequenceNo1 == sequenceNo2) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        } 
    };
    
    /**
     * Actual cache storage. Identities are ordered by its expiresTime 
     * and sequenceNo.
     */
    private SortedMap cache = new TreeMap(comparator);

    /**
     * Create a new {@link DefaultCacheStore}.
     * @param cacheTime The {@link CacheTime} used to derive all Cache timings.
     * @param timeToLive The amount of time in milliseconds that a entry can
     * remain in cache and be conidered as current.
     */
    public DefaultCacheStore(CacheTime cacheTime, long timeToLive) {
        super(cacheTime, timeToLive);
    }

    //Javadoc inherited.
    public synchronized CacheIdentity store(CacheEntry entry) {
        // Calculate actual time-to-live for cache identity.
        long entryTimeToLive = entry.getTimeToLive();
        
        // If time-to-live was not specified for cache entry, 
        // use the one specified for cache store.
        // If time-to-live was given for cache entry,
        // take the greater of entry/store values.
        long identityTimeToLive = (entryTimeToLive == -1) ? timeToLive : 
            Math.max(timeToLive, entryTimeToLive); 
        
        // Create cache identity with actual time-to-live value.
        CacheIdentity identity =
                entry.generateIdentity(cacheTime, 
                        identityTimeToLive, 
                        getNextSequenceNumber());
        
        // Put an entry in the cache.
        cache.put(identity, entry);
        
        // Return identity to retrieve stored entries.
        return identity;
    }


    //Javadoc inherited.
    public CacheEntry retrieve(CacheIdentity identity) {
        return (CacheEntry) cache.get(identity);
    }

    //Javadoc inherited.
    public CacheIdentity retrieveOldestExpiredIdentity() {

        // This variable will hold expired identity to return
        CacheIdentity expiredIdentity = null;
        
        // This varialbe will hold the oldest identity.
        CacheIdentity identity = null;
        
        // Retrieve first key from the TreeMap, which would be the one with 
        // least expiresTime value. Note, that the firstKey() method throws 
        // NoSuchElementException in case TreeMap is empty, so we need 
        // to handle it there. 
        try {
            identity = (CacheIdentity) cache.firstKey();
        } catch (NoSuchElementException e) {
            // no op
        }
        
        if ((identity != null) && isExpired(identity)) {
            expiredIdentity = identity;
        }
        
        return expiredIdentity;
    }

    //Javadoc inherited.
    public synchronized void remove(CacheIdentity identity) {
        cache.remove(identity);
    }

    //Javadoc inherited.
    public void removeExpiredIdentities() {

        Set keySet = cache.keySet();
        Iterator keySetIterator = keySet.iterator();

        List identities = new ArrayList();

        synchronized(this) {
            while (keySetIterator.hasNext()) {
                CacheIdentity identity = (CacheIdentity) keySetIterator.next();
                if (isExpired(identity)) {
                    identities.add(identity);
                } else {
                    break;
                }
            }
        }
        Iterator identitiesIterator = identities.iterator();
        while (identitiesIterator.hasNext()) {
            remove((CacheIdentity)identitiesIterator.next());
        }

    }

    //Javadoc inherited.
    public void clear() {
        cache.clear();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/6	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10343/3	ianw	VBM:2005111405 interim commit

 29-Nov-05	10343/2	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0

 16-Nov-05	10343/1	ianw	VBM:2005111405 Phase 1 performance improvements

 11-Jul-05	8616/3	ianw	VBM:2005060103 Fixed Javadoc as per review comments

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
