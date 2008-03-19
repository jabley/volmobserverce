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

import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.project.PolicySource;
import com.volantis.shared.throwable.ExtendedRuntimeException;

import java.io.File;

/**
 * A resolver that resolves relative paths against the location of the
 * configuration file.
 */
public class ConfigPolicySourceSelector
        extends AbstractPolicySourceSelector {

    private final ConfigContext configContext;

    public ConfigPolicySourceSelector(RuntimePolicySourceFactory factory,
                                      ConfigContext configContext) {
        super(factory);
        this.configContext = configContext;
    }

    public String resolveRelativeFile(String path) {
        try {
            File file = configContext.getConfigRelativeFile(path, true);
            if (file == null) {
                throw new ConfigurationException(
                        "The directory you provided does not exist");
            }
            return file.getAbsolutePath();
        } catch (ConfigurationException e) {
            throw new ExtendedRuntimeException(e);
        }
    }

    public PolicySource createDefaultPolicySource(
            RuntimePolicySourceFactory policySourceFactory, boolean remote) {
        throw new IllegalStateException(
                "No policy source specified for project");
    }
}
