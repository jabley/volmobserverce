/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.java.io;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import mock.java.io.InputStreamMock;
import mock.java.io.OutputStreamMock;
import mock.java.io.ReaderMock;
//import mock.java.io.WriterMock;
import mock.java.io.FilterInputStreamMock;

/**
 * Tests for IO related mock objects.
 */
public class IOTestCase
        extends MockTestCaseAbstract {

    /**
     * Tests that all the mock objects can be initialised correctly.
     */
    public void testInitialisation() {
        //new WriterMock("writer", expectations);
        new ReaderMock("reader", expectations);
        new InputStreamMock("inputStream", expectations);
        new OutputStreamMock("outputStream", expectations);
        new FilterInputStreamMock("filterInputStream", expectations,
                System.in);
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
