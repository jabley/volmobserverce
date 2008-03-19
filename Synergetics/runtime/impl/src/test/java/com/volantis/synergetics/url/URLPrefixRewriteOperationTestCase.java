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
 * Test case for URLPrefixRewriteOperation.
 */
public class URLPrefixRewriteOperationTestCase extends TestCase {

    /**
     * Test that optimizeURL does as expected with urls containing ".." where
     * all the provided urls are legal.
     */
    public void testOptimizeURLLegalDots() throws Throwable {
        // Any operation will do because we are testing a shared method
        // rather than behaviour that is specific to a particular operation.
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.ADD_PREFIX;

        String url = "/a/b/c/../d";
        String result = (String) PrivateAccessor.invoke(operation,
                                                        "optimizeURL",
                                                        new Class[]{
                                                            String.class},
                                                        new String[]{url});
        assertEquals("Expected the .. to be optimized", "/a/b/d", result);

        url = "a/b/c/../d";
        result = (String) PrivateAccessor.invoke(operation,
                                                 "optimizeURL",
                                                 new Class[]{String.class},
                                                 new String[]{url});
        assertEquals("Expected the .. to be optimized", "a/b/d", result);

        url = "/a/b/c/../../d";
        result = (String) PrivateAccessor.invoke(operation,
                                                 "optimizeURL",
                                                 new Class[]{String.class},
                                                 new String[]{url});
        assertEquals("Expected the .. to be optimized", "/a/d", result);

        url = "a/b/c/../../d";
        result = (String) PrivateAccessor.invoke(operation,
                                                 "optimizeURL",
                                                 new Class[]{String.class},
                                                 new String[]{url});
        assertEquals("Expected the .. to be optimized", "a/d", result);

        url = "/a/b/c/../../../d";
        result = (String) PrivateAccessor.invoke(operation,
                                                 "optimizeURL",
                                                 new Class[]{String.class},
                                                 new String[]{url});
        assertEquals("Expected the .. to be optimized", "/d", result);
    }

    /**
     * Test that optimizeURL does as expected with urls containing ".." where
     * all the provided urls are illegal.
     */
    public void testOptimizeURLIllegalDots() throws Throwable {
        // Any operation will do because we are testing a shared method
        // rather than behaviour that is specific to a particular operation.
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.ADD_PREFIX;

        String url = "/a/../../d";
        String result = (String) PrivateAccessor.invoke(operation,
                                                        "optimizeURL",
                                                        new Class[]{
                                                            String.class},
                                                        new String[]{url});
        assertEquals("Expected the .. not to be optimized", url, result);

        url = "a/../d";
        result = (String) PrivateAccessor.invoke(operation,
                                                 "optimizeURL",
                                                 new Class[]{String.class},
                                                 new String[]{url});
        assertEquals("Expected the .. not to be optimized", url, result);

        url = "../d";
        result = (String) PrivateAccessor.invoke(operation,
                                                 "optimizeURL",
                                                 new Class[]{String.class},
                                                 new String[]{url});
        assertEquals("Expected the .. not to be optimized", url, result);

        url = "/../d";
        result = (String) PrivateAccessor.invoke(operation,
                                                 "optimizeURL",
                                                 new Class[]{String.class},
                                                 new String[]{url});
        assertEquals("Expected the .. not to be optimized", url, result);
    }

    /**
     * Test the ADD_PREFIX URLPrefixRewriteOperation.performOperation().
     */
    public void testPerformOperationAddPrefix() {
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.ADD_PREFIX;

        String currentURL = null;
        String contextURL = null;
        String newURL = null;
        String url = null;

        newURL = "http://volantis.com/dsb/BBCNews";
        url = "uk.html";
        String result = operation.performOperation(currentURL, newURL,
                                                   contextURL, url);
        assertEquals("Expected newURL to become the prefix of url with a / " +
                     "in between", "http://volantis.com/dsb/BBCNews/uk.html",
                     result);

        newURL = "http://volantis.com/dsb/BBCNews/";
        url = "uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected newURL to become the prefix of url with a / " +
                     "in between", "http://volantis.com/dsb/BBCNews/uk.html",
                     result);

        newURL = "http://volantis.com/dsb/BBCNews";
        url = "/uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected newURL to become the prefix of url with a / " +
                     "in between", "http://volantis.com/dsb/BBCNews/uk.html",
                     result);

        newURL = "http://volantis.com/dsb/BBCNews/";
        url = "/uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected newURL to become the prefix of url with a / " +
                     "in between", "http://volantis.com/dsb/BBCNews/uk.html",
                     result);
    }

    /**
     * Test the REMOVE_PREFIX URLPrefixRewriteOperation.performOperation().
     */
    public void testPerformOperationRemovePrefix() {
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.REMOVE_PREFIX;

        String currentURL = null;
        String contextURL = null;
        String newURL = null;
        String url = null;
        String result = null;

        currentURL = "http://www.bbc.co.uk/news";
        // context URL equal to currentURL allowing a potential prefix removal.
        contextURL = currentURL;
        newURL = null;
        url = "http://www.bbc.co.uk/news/uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected currentURL to be removed from url",
                     "/uk.html", result);

        currentURL = "http://www.bbc.co.uk/news/";
        // context URL equivalent (ignoring trailing "/") to currentURL
        // allowing a potential prefix removal.
        contextURL = "http://www.bbc.co.uk/news";
        newURL = null;
        url = "http://www.bbc.co.uk/news/uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected currentURL to be removed from url",
                     "uk.html", result);

        currentURL = "http://www.bbc.co.uk/news";
        // context URL equivalent (ignoring trailing "/") to currentURL
        // allowing a potential prefix removal.
        contextURL = "http://www.bbc.co.uk/news/";
        newURL = null;
        url = "http://www.bbc.co.uk/news/uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected currentURL to be removed from url",
                     "/uk.html", result);

        currentURL = "http://www.bbc.co.uk/news";
        // context URL equal to currentURL allowing a potential prefix removal.
        contextURL = currentURL;
        newURL = null;
        url = "http://www.itv.co.uk/news/uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected url since no remove should have occurred",
                     url, result);

        currentURL = "http://www.bbc.co.uk/news";
        // context URL is not equal to the currentURL so no prefix can be
        // removed
        contextURL = "http://www.bbc.co.uk/sport";
        newURL = null;
        url = "http://www.bbc.co.uk/news/uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected url since no remove should have occurred",
                     url, result);

        currentURL = "http://www.bbc.co.uk/news";
        // context URL is null so no prefix can be removed
        contextURL = null;
        newURL = null;
        url = "http://www.bbc.co.uk/news/uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected url since no remove should have occurred",
                     url, result);
    }

    /**
     * Test the REPLACE_PREFIX URLPrefixRewriteOperation.performOperation().
     */
    public void testPerformOperationReplacePrefix() {
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.REPLACE_PREFIX;

        String currentURL = null;
        String contextURL = null;
        String newURL = null;
        String url = null;
        String result = null;

        currentURL = "http://www.bbc.co.uk/news";
        newURL = "http://volantis.com/dsb/BBCNews";
        url = "http://www.bbc.co.uk/news/uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected newURL to replace the currentURL prefix of " +
                     "url", "http://volantis.com/dsb/BBCNews/uk.html", result);

        currentURL = "http://www.bbc.co.uk/news";
        newURL = "http://volantis.com/dsb/BBCNews";
        url = "http://www.bbc.co.uk/news/uk/uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected newURL to replace the currentURL prefix of " +
                     "url", "http://volantis.com/dsb/BBCNews/uk/uk.html",
                     result);

        currentURL = "http://www.bbc.co.uk/news";
        newURL = "http://volantis.com/dsb/BBCNews";
        url = "http://www.itv.co.uk/news/uk.html";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Expected url since no replace should have occurred",
                     url, result);

        currentURL = "http://www.mobissimo.com";
        newURL = "/vgt/www.mobissimo.com/";
        url = "http://www.mobissimo.com/company/";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Replacement should should have occurred",
                     "/vgt/www.mobissimo.com/company/", result);

        currentURL = "http://www.mobissimo.com";
        newURL = "/vgt/www.mobissimo.com/";
        url = "http://www.mobissimo.com";
        result = operation.performOperation(currentURL, newURL,
                                            contextURL, url);
        assertEquals("Replacement should should have occurred",
                     "/vgt/www.mobissimo.com/", result);
    }


    /**
     * Test that a replace not do a replace for prefixes that should not be
     * matched because of ".."s in the url.
     */
    public void testReplaceWithDots() {
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.REPLACE_PREFIX;

        String currentURL = "http://www.bbc.co.uk/news";
        String contextURL = null;
        String newURL = "http://volantis.com/dsb/BBCNews";
        String url = "http://www.bbc.co.uk/news/../uk.html";
        String result = operation.performOperation(currentURL, newURL,
                                                   contextURL, url);
        assertEquals("Expected url to be optimized only " +
                     "url", "http://www.bbc.co.uk/uk.html", result);
    }

    /**
     * Test that a replace not do a replace for prefixes that should not be
     * matched because of ".."s in the url.
     */
    public void testRemoveWithDots() {
        URLPrefixRewriteOperation operation =
            URLPrefixRewriteOperation.REMOVE_PREFIX;

        String currentURL = "http://www.bbc.co.uk/news";
        String contextURL = currentURL;
        String newURL = null;
        String url = "http://www.bbc.co.uk/news/../uk.html";
        String result = operation.performOperation(currentURL, newURL,
                                                   contextURL, url);
        assertEquals("Expected url to be optimized only " +
                     "url", "http://www.bbc.co.uk/uk.html", result);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-May-05	456/1	pcameron	VBM:2005050416 Fixed multiple slash path optimization

 17-Feb-05	406/1	byron	VBM:2005021002 Remove extraneous / from url rewriting

 17-Feb-05	404/3	byron	VBM:2005021002 Remove extraneous / from url rewriting

 16-Feb-05	404/1	byron	VBM:2005021002 Remove extraneous / from url rewriting

 01-Sep-04	254/3	doug	VBM:2004052801 Fixed problem with REMOVE_PREFIX URLPrefixRewriteOperation

 26-May-04	246/1	allan	VBM:2004052102 Optimize url before rewriting.

 25-May-04	227/1	allan	VBM:2004052103 Provide a general url prefix rewriting facility

 ===========================================================================
*/
