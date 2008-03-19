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

package com.volantis.shared.security.impl.acl;

import com.volantis.shared.security.acl.immutable.ImmutableACLEntry;

/**
 * Implementation of {@link ImmutableACLEntry}.
 */
public class ImmutableACLEntryImpl
        extends AbstractACLEntry
        implements ImmutableACLEntry {

    /**
     * Copy constructor.
     *
     * @param entry The object to copy.
     */
    public ImmutableACLEntryImpl(AbstractACLEntry entry) {
        super(entry);
    }

    /**
     * Override to return this object rather than create a new one.
     *
     * <p>This is simply a performance optimisation and has no impact on the
     * behaviour.</p>
     */
    public ImmutableACLEntry createImmutableACLEntry() {
        return this;
    }
}
