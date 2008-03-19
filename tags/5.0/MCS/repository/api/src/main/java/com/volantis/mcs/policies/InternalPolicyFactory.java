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

package com.volantis.mcs.policies;

import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.accessors.xml.jibx.JiBXWriter;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.PolicyBuilderManager;

/**
 * Extension to the {@link PolicyFactory} for internal use only.
 *
 * @mock.generate
 */
public abstract class InternalPolicyFactory
        extends PolicyFactory {

    public static InternalPolicyFactory getInternalInstance() {
        return (InternalPolicyFactory) getDefaultInstance();
    }

    public abstract InternalThemeContentBuilder createThemeContentBuilder();

    public abstract InternalLayoutContentBuilder createLayoutContentBuilder();

    /**
     * Create a policy reader that always does schema validation.
     *
     * @return
     */
    public abstract JiBXReader createPolicyReader();

    /**
     * Temporary method to allow the creation of a non-validating policy
     * reader whilst we fix validation. This should be removed shortly and not
     * used unless you understand why. ;-).
     *
     * @todo: remove this method when we sort out validation.
     */
    public abstract JiBXReader createDangerousNonValidatingPolicyReader();

    /**
     * Create a policy write that always does schema validation.
     *
     * @return
     */
    public abstract JiBXWriter createPolicyWriter();

    /**
     * Create a {@link PolicyBuilderManager} that can be used for managing
     * policies in the specified project that stores its policies as XML.
     *
     * <p>The returned object is thread safe.</p>
     *
     * @param project The project whose policies are to be managed.
     * @return The newly instantiated {@link PolicyBuilderManager}.
     */
    public abstract PolicyBuilderManager createXMLPolicyBuilderManager(
            InternalProject project);

    /**
     * Create a {@link PolicyBuilderManager} that can be used for managing
     * policies in the specified project that stores its policies in a JDBC
     * database.
     *
     * <p>The returned object is thread safe.</p>
     *
     * @param project The project whose policies are to be managed.
     * @return The newly instantiated {@link PolicyBuilderManager}.
     */
    public abstract PolicyBuilderManager createJDBCPolicyBuilderManager(
            InternalProject project);
}
