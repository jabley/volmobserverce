/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.java.security;

import java.security.Certificate;
import java.security.DomainCombiner;
import java.security.Guard;
import java.security.Key;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.PublicKey;

/**
 * Triggers auto generation of classes within <code>java.security</code> for
 * which the source is not available.
 *
 * <p>If you add new fields in this file then remember to update the associated
 * test case to ensure that the generated mocks are usable.</p>
 *
 * @mock.generate library="true"
 */
public class SecurityLibrary {

    /**
     * @mock.generate interface="true"
     */
    public Certificate certificate;

    /**
     * @mock.generate interface="true"
     */
    public DomainCombiner domainCombiner;

    /**
     * @mock.generate interface="true"
     */
    public Guard guard;

    /**
     * @mock.generate interface="true"
     */
    public Key key;

    /**
     * @mock.generate interface="true"
     */
    public Principal principal;

    /**
     * @mock.generate interface="true"
     */
    public PrivateKey privateKey;

    /**
     * @mock.generate interface="true"
     */
    public PrivilegedAction privilegedAction;

    /**
     * @mock.generate interface="true"
     */
    public PrivilegedExceptionAction privilegedExceptionAction;

    /**
     * @mock.generate interface="true"
     */
    public PublicKey publicKey;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 ===========================================================================
*/
