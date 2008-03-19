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

import java.net.URL;

/**
 * This class is responsible for rewriting the paths of cookies obtained 
 * from a remote host so that they are relative to an MCS webapp.
 */
public class CookiePathRewriter {

    /**
     * Rewrites the given cookie path relative to MCS.
     *
     * @param remoteProjectLocation  the location of the remote MCS project where
     * the cookie has originated.
     * @param remoteProjectName the name used to register the remote project
     * with MCS.
     * @param originalCookiePath the original path of the cookie sent
     * by the server hosting the remote MCS project
     * @param mcsContextPath the context path used to access the MCS webapp,
     * e.g Given the request URL:
     * http://localhost:8080/volantis/welcome/welcome.xdime, the context path
     * is /volantis
     *
     * @return the original cookie path rewritten relative to MCS.
     */
    public String rewritePath(URL remoteProjectLocation, String remoteProjectName,
                              String originalCookiePath, String mcsContextPath) {

        String remoteProjectLocationPath = getPath(remoteProjectLocation);
        String cookiePathMinusRemoteProjectLocationPath = null;
        if (!remoteProjectLocationPath.equals("")) {
            // The URL of the remote MCS project has a path component.
            // This path needs to be stripped from the supplied cookie path
            // if the start of the cookie path matches the path of the remote
            // host.
            if (originalCookiePath.startsWith(remoteProjectLocationPath)) {
                cookiePathMinusRemoteProjectLocationPath =
                        originalCookiePath.substring(
                        remoteProjectLocationPath.length());
            }
        }

        StringBuffer newPath = new StringBuffer();
         // add the context path of MCS, e.g. /volantis
        newPath.append(mcsContextPath);
        // add the name of the remote project
        newPath.append("/");
        newPath.append(remoteProjectName);
        // now add the path of the original cookie
        if (cookiePathMinusRemoteProjectLocationPath != null) {
            newPath.append(cookiePathMinusRemoteProjectLocationPath);
        } else {

            // Dont add the right most "/" as per RFC
            if (!originalCookiePath.equals("/")) {
                newPath.append(originalCookiePath);
            }

        }

        return newPath.toString();
    }

    /**
     * Returns the path component of the given URL minus any trailing /.
     * @param url the url whose path is required, minus any trailing /
     *
     * @return the path compoment of the given url minus any trailing /.
     */
    private String getPath(URL url) {

        String path = url.getPath();
        if (path.endsWith("/")) {
            // remove the trailing /.
            path = path.substring(0, path.length() - 1);

        }
        return path;
    }
}
