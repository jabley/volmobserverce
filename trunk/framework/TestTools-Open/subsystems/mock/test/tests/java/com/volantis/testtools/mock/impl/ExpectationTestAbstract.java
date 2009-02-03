/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.expectations.Report;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

import java.io.StringWriter;

public abstract class ExpectationTestAbstract
        extends MockTestCaseAbstract {

    protected StringWriter out;
    protected Report report;

    protected void setUp() throws Exception {
        super.setUp();

        out = new StringWriter();
        report = new ReportImpl(out);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	8613/1	geoff	VBM:2005052404 Holding VBM for XDIME CP prior to 3.3.1 release

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
