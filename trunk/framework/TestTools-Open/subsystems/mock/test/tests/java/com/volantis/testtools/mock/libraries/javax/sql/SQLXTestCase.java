/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.javax.sql;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import mock.javax.sql.DataSourceMock;

/**
 * Tests for extended SQL related mock objects.
 */
public class SQLXTestCase
        extends MockTestCaseAbstract {

    /**
     * Tests that all the mock objects can be initialised correctly.
     */
    public void testInitialisation() {
        new DataSourceMock("dataSource", expectations);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 ===========================================================================
*/
