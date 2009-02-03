/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.java.security;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import mock.java.security.CertificateMock;
import mock.java.security.DomainCombinerMock;
import mock.java.security.GuardMock;
import mock.java.security.KeyMock;
import mock.java.security.PrincipalMock;
import mock.java.security.PrivateKeyMock;
import mock.java.security.PrivilegedActionMock;
import mock.java.security.PrivilegedExceptionActionMock;
import mock.java.security.PublicKeyMock;

/**
 * Tests for IO related mock objects.
 */
public class SecurityTestCase
        extends MockTestCaseAbstract {

    /**
     * Tests that all the mock objects can be initialised correctly.
     */
    public void testInitialisation() {
        new CertificateMock("certificate", expectations);
        new DomainCombinerMock("domainCombiner", expectations);
        new GuardMock("guard", expectations);
        new KeyMock("key", expectations);
        new PrincipalMock("principal", expectations);
        new PrivateKeyMock("privateKey", expectations);
        new PrivilegedActionMock("privilegedAction", expectations);
        new PrivilegedExceptionActionMock("privilegedExceptionAction",
                expectations);
        new PublicKeyMock("publicKey", expectations);
    }
}
