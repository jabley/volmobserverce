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
package com.volantis.synergetics.url;


import junit.framework.TestCase;

/**
 * Test case for URLPrefixRewriteRule.
 */
public class URLPrefixRewriteRuleTestCase extends TestCase {

    /**
     * Test that the execute work as expected. This is just a basic test
     * because the more involved testing is done by the testcase for
     * URLPrefixRewriteOperation.
     */
    public void testExecuteCallsPerformOperation() {
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.ADD_PREFIX;

        String currentURL = null;
        String contextURL = null;
        String newURL = "http://volantis.com/dsb/BBCNews";

        URLPrefixRewriteRule rule = new URLPrefixRewriteRule(currentURL,
                                                             newURL,
                                                             operation);

        String result = rule.execute("news/uk.html", contextURL);

        assertEquals("Expected newURL to become the prefix of url",
                     "http://volantis.com/dsb/BBCNews/news/uk.html", result);
    }

    /**
     * Test that constructor throws an IllegalArgumentException only when it
     * should.
     */
    public void testContructorIllegalArgs() {
        // Test valid ADD_PREFIX
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.ADD_PREFIX;
        String currentURL = null;
        String newURL = "http://volantis.com/dsb/BBCNews";
        new URLPrefixRewriteRule(currentURL,
                                 newURL, operation);

        // Test invalid REMOVE_PREFIX
        operation = URLPrefixRewriteOperation.REMOVE_PREFIX;
        try {
            new URLPrefixRewriteRule(currentURL,
                                     newURL, operation);
            fail("Expected IllegalArgumentException for trying to remove " +
                 "null.");
        } catch (IllegalArgumentException e) {
            // Success.
        }

        // Test valid REMOVE_PREFIX
        currentURL = "http://volantis.com/dsb/BBCNews";
        newURL = null;
        new URLPrefixRewriteRule(currentURL,
                                 newURL, operation);

        // Test invalid ADD_PREFIX
        operation = URLPrefixRewriteOperation.ADD_PREFIX;
        try {
            new URLPrefixRewriteRule(currentURL,
                                     newURL, operation);
            fail("Expected IllegalArgumentException for trying to add " +
                 "null.");
        } catch (IllegalArgumentException e) {
            // Success.
        }

        // Test with invalid replace operations.
        operation = URLPrefixRewriteOperation.REPLACE_PREFIX;
        currentURL = null;
        newURL = null;
        try {
            new URLPrefixRewriteRule(currentURL,
                                     newURL, operation);
            fail("Expected IllegalArgumentException for trying to replace " +
                 "null with null.");
        } catch (IllegalArgumentException e) {
            // Success.
        }

        currentURL = "http://volantis.com/dsb/BBCNews";
        try {
            new URLPrefixRewriteRule(currentURL,
                                     newURL, operation);
            fail("Expected IllegalArgumentException for trying to replace " +
                 "something with null.");
        } catch (IllegalArgumentException e) {
            // Success.
        }

        currentURL = null;
        newURL = "http://volantis.com/dsb/BBCNews";
        try {
            new URLPrefixRewriteRule(currentURL,
                                     newURL, operation);
            fail("Expected IllegalArgumentException for trying to replace " +
                 "null with something.");
        } catch (IllegalArgumentException e) {
            // Success.
        }

        // Test with null operation
        operation = null;
        try {

            new URLPrefixRewriteRule(currentURL,
                                     newURL, operation);
            fail("Expected IllegalArgumentException for having no operation.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-04	254/1	doug	VBM:2004052801 Fixed problem with REMOVE_PREFIX URLPrefixRewriteOperation

 25-May-04	227/1	allan	VBM:2004052103 Provide a general url prefix rewriting facility

 ===========================================================================
*/
