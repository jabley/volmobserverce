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

package com.volantis.mcs.runtime.repository.remote.xml;

import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.migrate.api.config.RemotePolicyMigrator;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.remote.PolicyBuilders;
import com.volantis.shared.content.StringContentInput;

import java.io.IOException;
import java.io.InputStream;

/**
 * Responsible for parsing a remote policy definition.
 */
public class RemotePolicyBuilderParser {

    /**
     * The migrator to use to ensure that the definition is up to date.
     */
    private final RemotePolicyMigrator migrator;

    /**
     * Initialise.
     *
     * @param migrator The migrator.
     */
    public RemotePolicyBuilderParser(RemotePolicyMigrator migrator) {
        this.migrator = migrator;
    }

    /**
     * Parse the policy definition as a {@link PolicyBuilder}.
     *
     * @param stream The policy definition.
     * @param url    The location of the policy, used for relative paths and
     *               error logging.
     * @return
     * @throws ResourceMigrationException If there was a problem migrating.
     * @throws IOException                If there was a problem reading the
     *                                    stream.
     */
    public PolicyBuilder parsePolicyBuilder(InputStream stream, String url)
            throws ResourceMigrationException, IOException {

        Object object = parseRemoteObject(stream, url);
        PolicyBuilder builder = (PolicyBuilder) object;
        builder.setName(url);
        return builder;
    }

    public PolicyBuilders parsePolicyBuilders(InputStream stream, String url)
            throws ResourceMigrationException, IOException {

        Object object = parseRemoteObject(stream, url);

        PolicyBuilders builders = (PolicyBuilders) object;
        return builders;
    }

    /**
     * Parse the policy definition as a {@link PolicyBuilders}.
     *
     * @param stream The policy definitions.
     * @param url    The location of the policies, used for relative paths and
     *               error logging.
     * @return
     * @throws ResourceMigrationException If there was a problem migrating.
     * @throws IOException                If there was a problem reading the
     *                                    stream.
     */
    public Object parseRemoteObject(InputStream stream, String url)
            throws ResourceMigrationException, IOException {

        String xml = migrator.migratePolicy(stream, url);
        StringContentInput content = new StringContentInput(xml);

        InternalPolicyFactory factory =
                InternalPolicyFactory.getInternalInstance();

        JiBXReader jibxReader = factory.createPolicyReader();

        Object object = jibxReader.read(content, url);
        return object;
    }
}
