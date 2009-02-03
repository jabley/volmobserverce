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

import com.volantis.shared.security.acl.ACLFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.java.security.PrincipalMock;
import mock.java.security.acl.PermissionMock;

/**
 * Base of test cases for ACL related classes.
 */
public abstract class ACLTestAbstract
        extends TestCaseAbstract {

    protected PrincipalMock principalMock;
    protected PermissionMock permission1Mock;
    protected PermissionMock permission2Mock;
    protected ACLFactory factory;

    protected void setUp() throws Exception {
        super.setUp();

        principalMock = new PrincipalMock("principalMock", expectations);

        permission1Mock = new PermissionMock("permission1Mock", expectations);
        permission2Mock = new PermissionMock("permission2Mock", expectations);

        factory = SecurityFactory.getDefaultInstance().getACLFactory();
    }
}
