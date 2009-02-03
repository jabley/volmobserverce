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

import com.volantis.shared.security.acl.ACLFactory;
import com.volantis.shared.security.acl.mutable.MutableACL;
import com.volantis.shared.security.acl.mutable.MutableACLEntry;

import java.security.Principal;

/**
 * Default implementation of {@link ACLFactory}.
 */
public class ACLFactoryImpl
        extends ACLFactory {

    // Javadoc inherited.
    public MutableACL createACL(Principal owner) {
        return new MutableACLImpl(owner);
    }

    /**
     * Only to be used by persistence mechanisms
     * @return to be used by persistence mechanisms
     */
    public MutableACL createUninitializedACL() {
        return new MutableACLImpl();
    }

    // Javadoc inherited.
    public MutableACLEntry createACLEntry(
            Principal principal, boolean negative) {

        return new MutableACLEntryImpl(principal, negative);
    }
}
