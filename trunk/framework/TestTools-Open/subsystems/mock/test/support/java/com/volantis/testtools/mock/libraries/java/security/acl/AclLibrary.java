/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.java.security.acl;

import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.Owner;
import java.security.acl.Permission;

/**
 * Triggers auto generation of classes within <code>java.security.acl</code>
 * for which the source is not available.
 *
 * <p>If you add new fields in this file then remember to update the associated
 * test case to ensure that the generated mocks are usable.</p>
 *
 * @mock.generate library="true"
 */
public class AclLibrary {

    /**
     * @mock.generate interface="true"
     */
    public Acl acl;

    /**
     * @mock.generate interface="true"
     */
    public AclEntry aclEntry;

    /**
     * @mock.generate interface="true"
     */
    public Group group;

    /**
     * @mock.generate interface="true"
     */
    public Owner owner;

    /**
     * @mock.generate interface="true"
     */
    public Permission permission;
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
