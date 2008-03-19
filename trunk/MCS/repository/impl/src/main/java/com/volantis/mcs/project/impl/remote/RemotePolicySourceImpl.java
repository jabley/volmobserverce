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
/** (c) Volantis Systems Ltd 2004.  */
package com.volantis.mcs.project.impl.remote;

import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.PolicyBuilderManager;
import com.volantis.mcs.project.remote.RemotePolicySource;
import com.volantis.mcs.project.impl.AbstractPolicySource;

/**
 * PolicySource for remote policies.
 */
public class RemotePolicySourceImpl
        extends AbstractPolicySource
        implements RemotePolicySource {

    /**
     * URL that should be used to resolve any references to policies in this
     * project using relative URLs. Ends with a /
     */
    private final String baseURL;

    /**
     * As for {@link #baseURL} except has no trailing /.
     */
    private final String baseURLWithoutTrailingSlash;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param reader
     * @param baseURL URL which is the root URL against which relative policy
     */
    public RemotePolicySourceImpl(PolicyBuilderReader reader, String baseURL) {
        super(reader);
        if (baseURL.equals("")) {
            this.baseURL = baseURL;
            this.baseURLWithoutTrailingSlash = baseURL;
        } else {
            if (!baseURL.endsWith("/")) {
                baseURL = baseURL + "/";
            }
            this.baseURL = baseURL;
            baseURLWithoutTrailingSlash =
                    this.baseURL.substring(0, this.baseURL.length() - 1);
        }
    }

    /**
     * Remote policy sources do not as yet support managing the policies.
     *
     * @param project
     * @return null.
     */
    public PolicyBuilderManager createPolicyBuilderManager(InternalProject project) {
        return null;
    }

    // Javadoc inherited
    public boolean equals(Object object) {
        if (!(object instanceof RemotePolicySourceImpl)) {
            return false;
        }
        return baseURL.equals(((RemotePolicySource) object).getBaseURL());
    }

    // Javadoc inherited
    public int hashCode() {
        return getClass().hashCode() + baseURL.hashCode();
    }

    public String getBaseURL() {
        return baseURL;
    }

    public String getBaseURLWithoutTrailingSlash() {
        return baseURLWithoutTrailingSlash;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Jan-04	2769/1	mat	VBM:2004012702 Add PolicySource

 ===========================================================================
*/
