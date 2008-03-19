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

import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.runtime.configuration.project.AbstractPoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.project.JdbcPoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.project.XmlPoliciesConfiguration;

/**
 * Base class for all {@link PolicySourceSelector}.
 */
public abstract class AbstractPolicySourceSelector
        implements PolicySourceSelector {

    private final RuntimePolicySourceFactory factory;

    protected AbstractPolicySourceSelector(RuntimePolicySourceFactory factory) {
        this.factory = factory;
    }

    public PolicySource selectPolicySource(
            AbstractPoliciesConfiguration policies, boolean remote) {

        // If the location is remote then do not try and create local policy
        // sources.
        PolicySource policySource = null;
        if (policies instanceof JdbcPoliciesConfiguration) {
            if (remote) {
                throw new IllegalStateException("Remote projects cannot " +
                        "access policies in JDBC repository");
            }

            policySource = factory.createJDBCPolicySource(
                    ((JdbcPoliciesConfiguration) policies).getName());
        } else if (policies instanceof XmlPoliciesConfiguration) {
            if (remote) {
                throw new IllegalStateException("Remote projects cannot " +
                        "access policies in XML repository");
            }

            XmlPoliciesConfiguration xmlPoliciesConfiguration =
                    (XmlPoliciesConfiguration) policies;

            // If the directory is not absolute then resolve it against the
            // location of the mcs-project.xml file.
            String directory = xmlPoliciesConfiguration.getDirectory();
            String resolved = resolveRelativeFile(directory);

            policySource = factory.createXMLPolicySource(resolved);
        }

        // If no policies specified, and the location was on the file system
        // then use an XMLPolicySource with default project
        // file location
        if (policySource == null) {
            policySource = createDefaultPolicySource(factory, remote);
        }
        
        return policySource;
    }

    /**
     * Given the specified file system path which may be absolute or relative
     * resolve it against the location of the configuration file.
     *
     * @param path The path to resolve.
     * @return The resolved relative path.
     */
    protected abstract String resolveRelativeFile(String path);

    protected abstract PolicySource createDefaultPolicySource(
            RuntimePolicySourceFactory policySourceFactory, boolean remote);

}
