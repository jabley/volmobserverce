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

package com.volantis.shared.security;

import com.volantis.shared.security.acl.mutable.MutableACLEntry;

/**
 * Test the ACL entry.
 */
public class ACLEntryTestCase extends ACLTestAbstract {

    /**
     * Test the entry.
     */ 
    public void testEntry()
            throws Exception {

        MutableACLEntry entry = factory.createACLEntry(principalMock, false);

        // The permission is not contained as it is empty.
        assertFalse(entry.checkPermission(permission1Mock));

        // The permission should be added as the entry is empty.
        assertTrue(entry.addPermission(permission1Mock));

        // Adding it again does nothing.
        assertFalse(entry.addPermission(permission1Mock));

        // The entry should contain the permission as it has just been added.
        assertTrue(entry.checkPermission(permission1Mock));

        // Removing permission2 should fail as it has not been added.
        assertFalse(entry.removePermission(permission2Mock));

        // Removing permission1 should work as it has been added.
        assertTrue(entry.removePermission(permission1Mock));

        // Entries should be positive by default.
        assertFalse(entry.isNegative());

        // Making them negative should be honoured.
        entry = factory.createACLEntry(principalMock, true);
        assertTrue(entry.isNegative());
    }
}
