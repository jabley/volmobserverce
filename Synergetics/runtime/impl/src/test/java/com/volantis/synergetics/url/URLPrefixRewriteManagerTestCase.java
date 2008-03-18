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
import junitx.util.PrivateAccessor;

/**
 * Test case for URLPrefixRewriteManager.
 */
public class URLPrefixRewriteManagerTestCase extends TestCase {

    /**
     * Test that addRewritableURLPrefix adds a URLPrefixRewriteRule to the
     * URLPrefixRewriteManager as expected.
     */
    public void testAddRewritableURLPrefix() throws Throwable {
        URLPrefixRewriteManager manager = new URLPrefixRewriteManager();
        String currentURL = "http://www.bbc.co.uk/news";
        String newURL = "http://volantis.com/dsb/BBCNews";
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.REPLACE_PREFIX;

        manager.addRewritableURLPrefix(currentURL, newURL, operation);

        SortedURLTreeMap urlTreeMap = (SortedURLTreeMap)
            PrivateAccessor.getField(manager, "urlTreeMap");

        URLPrefixRewriteRule rule = (URLPrefixRewriteRule)
            urlTreeMap.get(currentURL);

        assertNotNull("Expected a URLPrefixRewriteRule for currentURL",
                      rule);
    }

    /**
     * Test that findAndExecuteRule works as expected.
     */
    public void testFindAndExecuteRule() {
        URLPrefixRewriteManager manager = new URLPrefixRewriteManager();
        String currentURL = "http://www.bbc.co.uk/news";
        String newURL = "http://volantis.com/dsb/BBCNews";
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.REPLACE_PREFIX;
        manager.addRewritableURLPrefix(currentURL, newURL, operation);

        currentURL = "http://www.bbc.co.uk/news/uk";
        newURL = null;
        operation = URLPrefixRewriteOperation.REMOVE_PREFIX;
        manager.addRewritableURLPrefix(currentURL, newURL, operation);

        String url = "http://www.bbc.co.uk/news/uk.html";
        String result = manager.findAndExecuteRule(url, null);
        assertEquals("Expected url to have had its prefix replaced.",
                     "http://volantis.com/dsb/BBCNews/uk.html", result);

        url = "http://www.bbc.co.uk/news/uk/uk.html";
        String contextURL = currentURL;
        result = manager.findAndExecuteRule(url, contextURL);
        assertEquals("Expected url to have had its prefix removed.",
                     "/uk.html", result);
    }

    /**
     * Test that findAndExecureRule works where the current url is null or ""
     * implying a relative url.
     */
    public void testFindAndExecuteRuleRelative() {
        URLPrefixRewriteManager manager = new URLPrefixRewriteManager();
        String newURL = "http://volantis.com/dsb/BBCNews/";
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.ADD_PREFIX;
        manager.addRewritableURLPrefix(null, newURL, operation);

        String result = manager.findAndExecuteRule("uk/news.html", null);
        assertEquals("Expected url to have become absolute.",
                     "http://volantis.com/dsb/BBCNews/uk/news.html", result);

        newURL = "http://volantis.com/dsb/CNNNews/";
        manager.addRewritableURLPrefix("", newURL, operation);

        result = manager.findAndExecuteRule("uk/news.html", null);
        assertEquals("Expected url to have become absolute.",
                     "http://volantis.com/dsb/CNNNews/uk/news.html", result);
    }

    // This demonstrates something about the behaviour of this class. It isn't
    // in the API as a contract, but I wanted the test in place to serve as a
    // placeholder in case the implementation changes, since DSB has a reliance
    // on this property. DSB adds the rules for the context service first, and
    // then all the other ones, to ensure that redirects remain within the same
    // DSB service. 
    public void testRulesAreRetrievedInSameOrderTheyAreCreated() {

        // example of the DSB <httpdriver> <host/> <port/> <target-mount/>
        // </httpdriver>
        final String currentURLPrefix = "http://localhost:8080/support/";

        // resource to be accessed, via the rewritten URL
        final String indexPage = "index.jsp";

        // example of the Location of the redirect returned by the mounted
        // service
        final String url = currentURLPrefix + indexPage;

        // example of the requested URL
        final String contextURL = currentURLPrefix + "/redirect.jsp";

        // example of one DSB service webapp context + <targetmount/>
        final String virtualMountA = "/dsb/virtualMountA/";

        // example of another DSB service webapp context + <targetmount/>
        final String virtualMountB = "/dsb/virtualMountB/";

        String result = configureAndExecute(currentURLPrefix, virtualMountB,
                virtualMountA, url, contextURL
        );
        assertEquals("virtualMountB was added first, "
                + "so it should be retrieved first.",
                virtualMountB + indexPage, result);
        result = configureAndExecute(currentURLPrefix, virtualMountA,
                virtualMountB, url, contextURL
        );
        assertEquals("virtualMountA was added first, "
                + "so it should be retrieved first.",
                virtualMountA + indexPage, result);
    }

    /**
     * Creates a new URLPrefixRewriteManager, adds two rewritable URL prefix
     * {@link URLPrefixRewriteOperation.REPLACE_PREFIX} operations. The first
     * one added uses the firstNewURLPrefix and the second one added uses the
     * secondNewURLPrefix. The result of the
     * {@link URLPrefixRewriteManager#findAndExecuteRule(String, String)} is
     * then returned.
     *
     * @param currentURLPrefix   the currentURLPrefix used when adding the
     *                           rewritableURLPrefix operation to the
     *                           URLPrefixRewriteManager.
     * @param firstNewURLPrefix  the newURLPrefix used for the first
     *                           RewritableURLPrefix added to the
     *                           URLPrefixRewriteManager.
     * @param secondNewURLPrefix the newURLPrefix used for the second
     *                           RewritableURLPrefix added to the
     *                           URLPrefixRewriteManager.
     * @param url                the url used for the findAndExecute method
     *                           call.
     * @param contextURL         the contextURL used for the findAndExecute
     *                           method call.
     * @return the String returned from
     *         URLPrefixRewriteManager.findAndExecuteRule
     */
    private String configureAndExecute(final String currentURLPrefix,
                                       final String firstNewURLPrefix,
                                       final String secondNewURLPrefix,
                                       final String url,
                                       final String contextURL) {
        URLPrefixRewriteManager manager = new URLPrefixRewriteManager();
        manager.addRewritableURLPrefix(
                currentURLPrefix,
                firstNewURLPrefix,
                URLPrefixRewriteOperation.REPLACE_PREFIX);
        manager.addRewritableURLPrefix(
                currentURLPrefix,
                secondNewURLPrefix,
                URLPrefixRewriteOperation.REPLACE_PREFIX);
        return manager.findAndExecuteRule(url, contextURL);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-04	254/1	doug	VBM:2004052801 Fixed problem with REMOVE_PREFIX URLPrefixRewriteOperation

 26-May-04	246/1	allan	VBM:2004052102 Provide a relative url rule.

 25-May-04	227/1	allan	VBM:2004052103 Provide a general url prefix rewriting facility

 ===========================================================================
*/
