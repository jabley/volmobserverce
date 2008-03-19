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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.unit.framework.recogniser;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * An abstract test case for the recognisers which use regexps.
 * <p>
 * This provides standard tests for different classes of regexp with standard
 * results for each. This is kind of in lieu of a proper volantis abstraction
 * of a regexp which we would like to add in the future.
 */
public abstract class RegexpRecogniserTestAbstract
        extends TestCaseAbstract {

    /**
     * Test a simple matching regexp.
     */
    public void testMatch() {

        boolean match = recognise("BLAHAHA", ".*AHA.*");
        assertTrue("", match);
    }

    /**
     * Test a regexp which does not match.
     */
    public void testNoMatch() {

        boolean match = recognise("xxxxxxx", ".*AHA.*");
        assertFalse("", match);
    }

    /**
     * Test a regexp which is invalid.
     */
    public void testInvalid() {

        try {
            /*boolean match =*/ recognise("xxxxxxx", "*.AHA*.");
            fail("invalid re");
        } catch (Exception e) {
            // success
        }
    }

    /**
     * Use the recogniser to recognise the data provided using the regexp
     * provided.
     *
     * @param data the content to recognise
     * @param re the regular expression to use.
     * @return true if recognised, false otherwise.
     */
    protected abstract boolean recognise(String data, String re);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
