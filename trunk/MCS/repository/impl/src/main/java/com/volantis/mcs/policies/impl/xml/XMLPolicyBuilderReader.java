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

package com.volantis.mcs.policies.impl.xml;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.policies.PolicyBuilderResponse;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Reads a {@link PolicyBuilder} from an XML policy source.
 */
public class XMLPolicyBuilderReader
        implements PolicyBuilderReader {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(XMLPolicyBuilderReader.class);

    /**
     * The underlying accessor.
     */
    private final PolicyBuilderAccessor accessor;

    /**
     * Initialise.
     *
     * @param accessor The underlying accessor.
     */
    public XMLPolicyBuilderReader(PolicyBuilderAccessor accessor) {
        this.accessor = accessor;
    }

    // Javadoc inherited.
    public PolicyBuilderResponse getPolicyBuilder(
            Project project, String name) {

        PolicyBuilderResponse response = null;
        try {
            PolicyBuilder builder = accessor.retrievePolicyBuilder(null,
                    project, name);

            response = new PolicyBuilderResponse(project, builder);
        } catch (RepositoryException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e);
            }
        }

        return response;
    }
}
