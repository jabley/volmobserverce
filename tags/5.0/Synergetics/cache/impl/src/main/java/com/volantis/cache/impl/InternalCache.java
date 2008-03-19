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

import com.volantis.cache.Cache;
import com.volantis.cache.impl.integrity.IntegrityCheckingReporter;
import com.volantis.cache.impl.group.InternalGroup;
import com.volantis.shared.io.IndentingWriter;

/**
 * Internal interface for caches.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate base="Cache"
 */
public interface InternalCache
        extends Cache {

    /**
     * Remove the entry with the specified key from the map.
     *
     * <p>This must be invoked while holding the mutexes associated with the
     * entry.</p>
     *
     * @param key The key whose entry should be removed.
     */
    void removeFromMap(Object key);

    /**
     * Perform an integrity check on the internal structure of the cache.
     *
     * <p>This method must only be called by tests when no other threads are
     * using the cache.</p>
     *
     * @param reporter The reposter to which problems are passed.
     */
    void performIntegrityCheck(IntegrityCheckingReporter reporter);

    /**
     * Output the internal structure of the cache.
     *
     * <p>This method must only be called by tests when no other threads are
     * using the cache.</p>

     * @param writer The writer to which the structure should be written.
     */
    void debugStructure(IndentingWriter writer);
}
