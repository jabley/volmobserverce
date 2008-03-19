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

import com.volantis.shared.security.acl.ACL;
import com.volantis.shared.security.acl.ACLEntryMock;
import com.volantis.shared.security.acl.mutable.MutableACL;
import com.volantis.shared.security.acl.mutable.MutableACLEntry;
import mock.java.security.PrincipalMock;

import java.security.acl.LastOwnerException;
import java.security.acl.NotOwnerException;
import java.util.Collections;
import java.util.Set;

/**
 * Test cases for {@link ACL}.
 */
public class ACLTestCase
        extends ACLTestAbstract {
    private PrincipalMock otherMock;

    protected void setUp() throws Exception {
        super.setUp();

        otherMock = new PrincipalMock("otherMock", expectations);
    }

    /**
     * Ensure that changes to the AccessControlList are protected.
     */
    public void testProtection() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final PrincipalMock attackerMock =
                new PrincipalMock("attackerMock", expectations);

        final ACLEntryMock entryMock =
                new ACLEntryMock("entryMock", expectations);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        MutableACL acl = factory.createACL(principalMock);

        try {
            acl.setName(attackerMock, "new name");
            fail("Did not detect attempt to change by a principal other " +
                    "than the owner");
        } catch (NotOwnerException expected) {
        }

        try {
            acl.addEntry(attackerMock, entryMock);
            fail("Did not detect attempt to change by a principal other " +
                    "than the owner");
        } catch (NotOwnerException expected) {
        }

        try {
            acl.removeEntry(attackerMock, entryMock);
            fail("Did not detect attempt to change by a principal other " +
                    "than the owner");
        } catch (NotOwnerException expected) {
        }

        try {
            acl.addOwner(attackerMock, principalMock);
            fail("Did not detect attempt to change by a principal other " +
                    "than the owner");
        } catch (NotOwnerException expected) {
        }

        try {
            acl.deleteOwner(attackerMock, principalMock);
            fail("Did not detect attempt to change by a principal other " +
                    "than the owner");
        } catch (NotOwnerException expected) {
        }
    }

    /**
     * Ensure that ownership is managed properly.
     */
    public void testOwnership() throws Exception {


        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        MutableACL acl = factory.createACL(principalMock);

        // The principal with which the ACL is first created is an owner.
        assertTrue(acl.isOwner(principalMock));

        // Another principal is not an owner.
        assertFalse(acl.isOwner(otherMock));

        // Adding the principal again should return false.
        assertFalse(acl.addOwner(principalMock, principalMock));

        // Adding another principal should return true and it is then an owner.
        assertTrue(acl.addOwner(principalMock, otherMock));
        assertTrue(acl.isOwner(otherMock));

        // Removing the original principal should return true, removing it
        // again should return false.
        assertTrue(acl.deleteOwner(principalMock, principalMock));
        assertFalse(acl.deleteOwner(otherMock, principalMock));

        // Attempting to remove the last owner should fail.
        try {
            acl.deleteOwner(otherMock, otherMock);
            fail("Did not detect attempt to remove last owner");
        } catch (LastOwnerException expected) {
        }
    }

    /**
     * Ensure that the set of permissions is calculated properly.
     *
     * @todo support Group.
     */ 
    public void testPermissions() throws Exception {

        MutableACL acl = factory.createACL(principalMock);

        MutableACLEntry negative = factory.createACLEntry(otherMock, true);
        negative.addPermission(permission1Mock);

        acl.addEntry(principalMock, negative);

        MutableACLEntry positive = factory.createACLEntry(otherMock, false);
        positive.addPermission(permission1Mock);
        positive.addPermission(permission2Mock);

        acl.addEntry(principalMock, positive);

        Set permissions = acl.permissions(otherMock);
        assertEquals(permissions, Collections.singleton(permission2Mock));
    }
}
