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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.project;

import com.volantis.mcs.runtime.URLNormalizer;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.project.xml.XMLPolicySource;

/**
 * A resolver that is used for projects defined in separate mcs-project.xml
 * file.
 */
public class SeperatePolicySourceSelector
        extends AbstractPolicySourceSelector {

    /**
     * Absolute URL to the project file.
     */
    private final String absoluteProjectURL;

    public SeperatePolicySourceSelector(RuntimePolicySourceFactory factory,
                                        String absoluteProjectURL) {
        super(factory);
        this.absoluteProjectURL = absoluteProjectURL;
    }

    // Javadoc inherited.
    public String resolveRelativeFile(String path) {
        String resolved = URLNormalizer.normalizeFullURL(path);
        if (!resolved.startsWith("/")) {
            // Resolve the directory against the location url.
            MarinerURL url = new MarinerURL(absoluteProjectURL, resolved);
            String resolvedDirectory = url.getExternalForm();
            if (resolvedDirectory.startsWith("file:")) {
                resolved = resolvedDirectory.substring(5);
            } else {
                throw new IllegalStateException("Cannot convert URL " +
                        absoluteProjectURL + " to a file system location");
            }
        }
        return resolved;
    }

    // Javadoc inherited.
    public PolicySource createDefaultPolicySource(
            RuntimePolicySourceFactory policySourceFactory, boolean remote) {

        PolicySource policies;
        if (absoluteProjectURL.startsWith("file:")) {
            String directory = absoluteProjectURL.substring(5);
            policies = policySourceFactory.createXMLPolicySource(
                    directory);
        } else if (remote) {
            // The location is the location of the project file so get the
            // directory that contains it and use that as the policy source
            // root.
            policies = policySourceFactory.createRemotePolicySource(
                    absoluteProjectURL);
        } else {
            throw new IllegalStateException("Cannot access policies " +
                    "from location " + absoluteProjectURL);
        }

        return policies;
    }
}
