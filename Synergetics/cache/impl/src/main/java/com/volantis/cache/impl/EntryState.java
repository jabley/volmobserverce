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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.cache.impl;

/**
 * Type safe enumeration of the possible states of a cache entry.
 */
public class EntryState {

    /**
     * Indicates that the entry has just been created and has no value, or
     * has expired and needs updating.
     */
    public static final EntryState UPDATE = new EntryState("UPDATE"
            , false // does not require waiting
            , false // cannot have expired
    );

    /**
     * Indicates that the value for the entry is being obtained by a thread
     * and other threads should wait until that is complete.
     */
    public static final EntryState PENDING = new EntryState("PENDING"
            , true  // does require waiting
            , false // cannot have expired
    );

    /**
     * Indicates that the entry is ready.
     */
    public static final EntryState READY = new EntryState("READY"
            , false // does not require waiting
            , true  // may have expired
    );

    /**
     * Indicates that the entry is in error.
     */
    public static final EntryState ERROR = new EntryState("ERROR"
            , false // does not require waiting
            , false // cannot have expired
    );

    /**
     * Indicates the the entry does not contain a value as it was not allowed
     * to be cached and is only there to prevent concurrent requests to block
     * when they should all invoke the provider directly.
     */
    public static final EntryState UNCACHEABLE = new EntryState("UNCACHEABLE"
            , false // does not require waiting
            , false // cannot have expired
    );

    /**
     * The name of the enumeration.
     */
    private final String name;

    /**
     * True if the cache must wait for it false otherwise.
     */
    private final boolean mustWait;

    /**
     * True if the entry may have expired, false otherwise.
     */
    private final boolean mayHaveExpired;

    /**
     * Initialise.
     *
     * @param name The name of the enumeration.
     */
    private EntryState(String name, boolean mustWait, boolean mayHaveExpired) {
        this.name = name;
        this.mustWait = mustWait;
        this.mayHaveExpired = mayHaveExpired;
    }

    /**
     * Indicates whether the caller must wait before accessing the entry.
     *
     * @return True if the cache must wait, false otherwise.
     */
    public boolean mustWait() {
        return mustWait;
    }

    /**
     * Indicates whether an entry in this state may have expired or not.
     *
     * @return Trye if the entry may have expired, false otherwise.
     */
    public boolean mayHaveExpired() {
        return mayHaveExpired;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
