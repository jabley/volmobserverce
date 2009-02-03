/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.java.util;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import mock.java.util.CollectionMock;
import mock.java.util.IteratorMock;
import mock.java.util.ListMock;
import mock.java.util.MapMock;
import mock.java.util.SetMock;

/**
 * Tests for util related mock objects.
 */
public class UtilTestCase
        extends MockTestCaseAbstract {

    /**
     * Tests that all the mock objects can be initialised correctly.
     */
    public void testInitialisation() {
        new CollectionMock("collection", expectations);
        new ListMock("list", expectations);
        new SetMock("set", expectations);
        new IteratorMock("iterator", expectations);
        new MapMock("map", expectations);
    }

    /**
     * Ensure that clashing fuzzy methods are correctly handled.
     */
    public void testListExpectations() {
        ListMock listMock = new ListMock("listMock", expectations);
        listMock.fuzzy.remove(ListMock._getMethodIdentifier("remove(java.lang.Object)"), this).returns(false);
        assertFalse(listMock.remove(this));

        listMock.fuzzy.remove(ListMock._getMethodIdentifier("remove(int)"), new Integer(4))
                .returns(this);
        assertSame(this, listMock.remove(4));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 ===========================================================================
*/
