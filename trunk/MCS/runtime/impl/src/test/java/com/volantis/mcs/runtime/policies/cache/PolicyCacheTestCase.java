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

package com.volantis.mcs.runtime.policies.cache;

import com.volantis.cache.CacheMock;
import com.volantis.cache.group.Group;
import com.volantis.cache.group.GroupMock;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.project.remote.RemotePolicySourceMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Test cases for {@link PolicyCache}.
 */
public class PolicyCacheTestCase
        extends TestCaseAbstract {

    private static final ExpectedValue REMOTE_GROUP_KEY =
            mockFactory.expectsToStringOf("<REMOTE_GROUP_KEY>");

    private static final ExpectedValue LOCAL_GROUP_KEY =
            mockFactory.expectsToStringOf("<LOCAL_GROUP_KEY>");

    private static final ExpectedValue DEFAULT_REMOTE_GROUP_KEY =
            mockFactory.expectsToStringOf("<REMOTE_DEFAULT_GROUP_KEY>");

    private static final ExpectedValue DEFAULT_LOCAL_GROUP_KEY =
            mockFactory.expectsToStringOf("<LOCAL_DEFAULT_GROUP_KEY>");

    private CacheMock cacheMock;
    private RemotePartitionsMock partitionsMock;
    private GroupMock rootGroupMock;
    private GroupMock defaultRemoteGroupMock;
    private RuntimeProjectMock projectMock;
    private GroupMock defaultLocalGroupMock;
    private PolicyCachePartitionConstraintsMock localPartitionConstraintsMock;
    private PolicyCachePartitionConstraintsMock remotePartitionConstraintsMock;
    private GroupMock localGroupMock;
    private GroupMock remoteGroupMock;
    private PolicyCacheImpl policyCache;
    private RemotePolicySourceMock sourceMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        cacheMock = new CacheMock("cacheMock", expectations);

        rootGroupMock = new GroupMock("rootGroupMock", expectations);

        partitionsMock = new RemotePartitionsMock("partitionsMock",
                expectations);

        localPartitionConstraintsMock =
                new PolicyCachePartitionConstraintsMock(
                        "localPartitionConstraintsMock", expectations);

        remotePartitionConstraintsMock =
                new PolicyCachePartitionConstraintsMock(
                        "remotePartitionConstraintsMock", expectations);

        localGroupMock = new GroupMock("localGroupMock", expectations);

        defaultLocalGroupMock = new GroupMock("defaultLocalGroupMock",
                expectations);

        remoteGroupMock = new GroupMock("remoteGroupMock", expectations);

        defaultRemoteGroupMock = new GroupMock("defaultRemoteGroupMock",
                expectations);

        projectMock = new RuntimeProjectMock("projectMock", expectations);

        sourceMock = new RemotePolicySourceMock("sourceMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        cacheMock.expects.getRootGroup().returns(rootGroupMock).any();

        rootGroupMock.expects.getGroup(PolicyCacheImpl.LOCAL_GROUP_KEY)
                .returns(localGroupMock).any();

        localGroupMock.expects.getGroup(PolicyCacheImpl.LOCAL_DEFAULT_GROUP_KEY)
                .returns(defaultLocalGroupMock).any();

        rootGroupMock.expects.getGroup(PolicyCacheImpl.REMOTE_GROUP_KEY)
                .returns(remoteGroupMock).any();

        remoteGroupMock.expects.getGroup(PolicyCacheImpl.REMOTE_DEFAULT_GROUP_KEY)
                .returns(defaultRemoteGroupMock).any();

        projectMock.expects.getPolicySource().returns(sourceMock).any();

        policyCache = new PolicyCacheImpl(cacheMock,
                        partitionsMock, localPartitionConstraintsMock,
                        remotePartitionConstraintsMock);
    }

    /**
     * Ensure that it is possible to select a group for a remote policy that
     * could not be found and does not belong to a partiton.
     */
    public void testSelectGroupForUnavailableRemotePolicyNoPartition()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        partitionsMock.expects.getRemotePartition("http://host/partition")
                .returns(null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Group group = policyCache.selectGroup("http://host/partition", null);
        assertSame(defaultRemoteGroupMock, group);
    }

    /**
     * Ensure that it is possible to select a group for a local policy that
     * could not be found and is in a project that has no cache group.
     */
    public void testSelectGroupForUnavailableLocalPolicyNoProjectCacheGroup()
            throws Exception {

        ProjectSpecificKey key = new ProjectSpecificKey(
                projectMock, "/fred.mimg");

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectMock.expects.isRemote().returns(false).any();
        projectMock.expects.getCacheGroup().returns(null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Group group = policyCache.selectGroup(key, null);
        assertSame(defaultLocalGroupMock, group);
    }

    /**
     * Ensure that the key for a local project relative policy is a key
     * consisting ot the project and the path.
     */
    public void testGetKeyProjectRelativeLocalProject() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(false).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Object key = policyCache.getKey(projectMock, "/fred.mimg");
        assertEquals(new ProjectSpecificKey(projectMock, "/fred.mimg"), key);
    }

    /**
     * Ensure that the key for a remote project relative policy is the fully
     * qualified URL.
     */
    public void testGetKeyProjectRelativeRemoteProject() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectMock.expects.makeAbsolutePolicyURL("/fred.mimg")
                .returns("http://remote:8080/project/fred.mimg").any();

        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Object key = policyCache.getKey(projectMock, "/fred.mimg");
        assertEquals("http://remote:8080/project/fred.mimg", key);
    }

    /**
     * Ensure that the key for the global project absolute policy is the fully
     * qualified URL.
     */
    public void testGetKeyProjectRelativeGlobalProject() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectMock.expects.getContainsOrphans().returns(true).any();
        projectMock.expects.isRemote().returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        try {
            Object key = policyCache.getKey(projectMock, "/fred.mimg");
            fail("No key was expected but created key of '" + key + "'");
        } catch (IllegalArgumentException expected) {
            assertEquals("Project is global but name '/fred.mimg' " +
                    "is project relative", expected.getMessage());
        }
    }

    /**
     * Ensure that an error is thrown if an attempt is made to get a key for a
     * policy with an absolute name from a local project.
     */
    public void testGetKeyAbsoluteLocalProject() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(false).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        try {
            Object key = policyCache.getKey(
                    projectMock, "http://remote:8080/project/fred.mimg");
            fail("No key was expected but created key of '" + key + "'");
        } catch (IllegalArgumentException expected) {
            assertEquals("Project is not remote and name " +
                    "'http://remote:8080/project/fred.mimg' " +
                    "is not project relative", expected.getMessage());
        }
    }

    /**
     * Ensure that an error is thrown if an attempt is made to get a key for a
     * policy with an absolute name from a remote, non global project.
     */
    public void testGetKeyAbsoluteRemoteProject() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        sourceMock.expects.getBaseURLWithoutTrailingSlash()
                .returns("http://remote:8080/project").any();

        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Object key = policyCache.getKey(
                projectMock, "http://remote:8080/project/fred.mimg");
        assertEquals("http://remote:8080/project/fred.mimg", key);
    }

    /**
     * Ensure that the key for the global project absolute policy is the fully
     * qualified URL.
     */
    public void testGetKeyAbsoluteGlobalProject() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectMock.expects.getContainsOrphans().returns(true).any();
        projectMock.expects.isRemote().returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Object key = policyCache.getKey(
                projectMock, "http://remote:8080/project/fred.mimg");
        assertEquals("http://remote:8080/project/fred.mimg", key);
    }
}
