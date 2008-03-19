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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.net.URL;

/**
 * This class is responsible for testing the behaviour of
 * {@link CookiePathRewriter}.
 */
public class CookiePathRewriterTestCase extends TestCaseAbstract {

    /**
     * The MCS application context.
     */
    private static final String MCS_APPLICATION_CONTEXT = "/volantis";

    /**
     * Test the URL is rewritten as expected when the original path is a /
     * @throws Exception
     */
    public void testRewriteCookiePathWhenOriginalPathIsFowardSlash()
        throws Exception {

        URL remoteProjectLocation = new URL("http://localhost:8080/jsp");
        String remoteProjectName = "jsp";
        String originalPath = "/";
        String expectedPath = MCS_APPLICATION_CONTEXT + "/" +
                remoteProjectName;

        testRewriteCookiePath(remoteProjectLocation, remoteProjectName,
                              originalPath, MCS_APPLICATION_CONTEXT,
                              expectedPath);
    }

    /**
     * Test the URL is rewritten as expected when the original path is a /
     * and the host path ends with a /
     * @throws Exception
     */
    public void testRewriteCookiePathWhenOriginalPathIsFowardSlashAndHostPathHasTrailingForwardSlash()
        throws Exception {

        URL remoteProjectLocation = new URL("http://localhost:8080/jsp/");
        String remoteProjectName = "jsp";
        String originalPath = "/";
        String expectedPath = MCS_APPLICATION_CONTEXT + "/" +
                remoteProjectName;

        testRewriteCookiePath(remoteProjectLocation, remoteProjectName,
                              originalPath, MCS_APPLICATION_CONTEXT,
                              expectedPath);
    }

    /**
     * Test the URL is rewritten as expected when the original path overlaps
     * with the expected path
     * @throws Exception
     */
    public void testRewriteWhenCookiePathOverlapsWithRemoteProjectPath()
        throws Exception {
        URL projectRoot = new URL("http://localhost:8080/jsp");
        String remoteProjectName = "jsp";
        String originalPath = "/jsp";

        String expectedPath = MCS_APPLICATION_CONTEXT + "/" +
                remoteProjectName;

        testRewriteCookiePath(projectRoot, remoteProjectName, originalPath,
                              MCS_APPLICATION_CONTEXT, expectedPath);
    }

    /**
     * Test the URL is rewritten as expected when the original path overlaps
     * with the expected path and the host has a trailing /
     * @throws Exception
     */
    public void testRewriteWhenCookiePathOverlapsWithRemoteProjectPathAndHostPathHasTrailingForwardSlash()
        throws Exception {
        URL projectRoot = new URL("http://localhost:8080/jsp/");
        String remoteProjectName = "jsp";
        String originalPath = "/jsp";

        String expectedPath = MCS_APPLICATION_CONTEXT + "/" +
                remoteProjectName;

        testRewriteCookiePath(projectRoot, remoteProjectName, originalPath,
                              MCS_APPLICATION_CONTEXT, expectedPath);
    }

    /**
     * /**
     * Test the URL is rewritten as expected when the original path overlaps
     * and extends the expected path
     * @throws Exception
     */
    public void testRewriteWhenDeepCookiePathOverlapsWithRemoteProjectPath()
        throws Exception {
        URL projectRoot = new URL("http://localhost:8080/jsp");
        String remoteProjectName = "jsp";
        String originalPath = "/jsp/a/b";

        String expectedPath = MCS_APPLICATION_CONTEXT + "/" +
                remoteProjectName + "/a/b";

        testRewriteCookiePath(projectRoot, remoteProjectName, originalPath,
                              MCS_APPLICATION_CONTEXT, expectedPath);
    }

    /**
     * Test the URL is rewritten as expected when the original path overlaps
     * and extends the expected path and the host has a trailing /
     * @throws Exception
     */
    public void testRewriteWhenDeepCookiePathOverlapsWithRemoteProjectPathWithTrailingForwardSlash()
        throws Exception {
        URL projectRoot = new URL("http://localhost:8080/jsp/");
        String remoteProjectName = "jsp";
        String originalPath = "/jsp/a/b";

        String expectedPath = MCS_APPLICATION_CONTEXT + "/" +
                remoteProjectName + "/a/b";

        testRewriteCookiePath(projectRoot, remoteProjectName, originalPath,
                              MCS_APPLICATION_CONTEXT, expectedPath);
    }

    /**
     * Perform rewrite test.
     *
     * @param remoteProjectLocation - location of the remote project
     * @param remoteProjectName - name of the remote project
     * @param originalPath - The path initially requested
     * @param mcsApplicationContext - The mcs application context which
     * should be used for the rewrite
     * @param expectedPath - the expected result of the rewrite.
     */
    private void testRewriteCookiePath(URL remoteProjectLocation,
                                       String remoteProjectName,
                                       String originalPath,
                                       String mcsApplicationContext,
                                       String expectedPath) {
        
        CookiePathRewriter pathRewriter = new CookiePathRewriter();
        String newPath =
                pathRewriter.rewritePath(remoteProjectLocation,
                                         remoteProjectName, originalPath,
                                         mcsApplicationContext);
        assertEquals(expectedPath, newPath);

    }
}
