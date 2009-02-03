/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.java.security.acl;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import mock.java.security.acl.AclEntryMock;
import mock.java.security.acl.AclMock;
import mock.java.security.acl.GroupMock;
import mock.java.security.acl.OwnerMock;
import mock.java.security.acl.PermissionMock;

/**
 * Tests for IO related mock objects.
 */
public class AclTestCase
        extends MockTestCaseAbstract {

    /**
     * Tests that all the mock objects can be initialised correctly.
     */
    public void testInitialisation() {
        new AclMock("acl", expectations);
        new AclEntryMock("aclEntry", expectations);
        new GroupMock("group", expectations);
        new OwnerMock("owner", expectations);
        new PermissionMock("permission", expectations);
    }
}
