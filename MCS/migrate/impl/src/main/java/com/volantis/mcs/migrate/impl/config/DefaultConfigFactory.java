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
package com.volantis.mcs.migrate.impl.config;

import com.volantis.mcs.migrate.api.config.ConfigFactory;
import com.volantis.mcs.migrate.api.config.RemotePolicyMigrator;
import com.volantis.mcs.migrate.api.framework.FrameworkFactory;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.api.framework.ResourceMigratorBuilder;
import com.volantis.mcs.migrate.api.framework.Version;
import com.volantis.mcs.migrate.api.framework.XSLStreamMigratorBuilder;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.mcs.repository.xml.PolicySchemas;
import org.xml.sax.EntityResolver;

public class DefaultConfigFactory extends ConfigFactory {

    public ResourceMigrator createDefaultResourceMigrator(
            NotificationReporter reporter, boolean strictMode) throws ResourceMigrationException {
        FrameworkFactory factory = FrameworkFactory.getDefaultInstance();

        Version rpdm27to30 = factory.createVersion("rpdm 2.7-3.0");
        Version lpdm30 = factory.createVersion("lpdm 3.0");

        Version repository200509 = factory.createVersion("repository 2005/09");
        Version repository200512 = factory.createVersion("repository 2005/12");
        Version repository200602 = factory.createVersion("repository 2006/02");

        ResourceMigratorBuilder builder =
                factory.createResourceMigratorBuilder(reporter);

        // ====================================================================
        //     Repository Migration
        // ====================================================================

        // Set the target version for the next type (YEUCH).
        // todo: add this inside the type, and validate to make sure that it is set properly.
        builder.setTarget(repository200602);

        builder.startType("repository");

        builder.setRegexpPathRecogniser(".*\\.(mthm|mimg|mlyt|mtxt|mlnk|" +
                "mrlv|mscr|mdyv|mcht|mbtn|mauc|mgrp)");

        // --------------------------------------------------------------------
        // Repository Versions
        // --------------------------------------------------------------------
        builder.addRegexpContentRecogniser(lpdm30,
                PolicySchemas.MARLIN_LPDM_V3_0.getNamespaceURL());

        builder.addRegexpContentRecogniser(rpdm27to30,
                "</remotePolicySetResponse>|</remotePolicyResponse>");

        // todo the namespace URLs may contain characters that have a special
        // todo meaning in a RE and they should really be quoted.
        builder.addRegexpContentRecogniser(repository200509,
                PolicySchemas.MARLIN_LPDM_2005_09.getNamespaceURL() + "|" +
                PolicySchemas.MARLIN_RPDM_2005_09.getNamespaceURL());

        builder.addRegexpContentRecogniser(repository200512,
                PolicySchemas.MARLIN_LPDM_2005_12.getNamespaceURL() + "|" +
                PolicySchemas.MARLIN_RPDM_2005_12.getNamespaceURL());

        builder.addRegexpContentRecogniser(repository200602,
                PolicySchemas.MARLIN_LPDM_2006_02.getNamespaceURL() + "|" +
                PolicySchemas.MARLIN_RPDM_2006_02.getNamespaceURL());

        XSLStreamMigratorBuilder xslBuilder;

        // --------------------------------------------------------------------
        // Step: LPDM 3.0 -> 2005/09
        // --------------------------------------------------------------------
        xslBuilder = builder.createXSLStreamMigratorBuilder();
        xslBuilder.setXSL(
                "/com/volantis/mcs/migrate/impl/config/lpdm/xsl/lpdm-v30-to-200509.xsl");

        xslBuilder.setStrictMode(strictMode);
        // Add the 3.0 LPDM schemas for input validation.
        xslBuilder.addInputSchema(PolicySchemas.MARLIN_LPDM_V3_0);

        // Add the 2005/09 LPDM schemas for output validation.
        xslBuilder.addOutputSchemata(PolicySchemas.REPOSITORY_2005_09);
        builder.addStep(lpdm30, repository200509, xslBuilder.getCompletedMigrator());

        // --------------------------------------------------------------------
        // Step: RPDM 2.7-3.0 -> 2005/09
        // --------------------------------------------------------------------
        xslBuilder = builder.createXSLStreamMigratorBuilder();
        xslBuilder.setXSL(
                "/com/volantis/mcs/migrate/impl/config/lpdm/xsl/rpdm-v30-to-200509.xsl");

        // Input DTDs are handled via the repository resolver.
        // TODO: handle rpdm DTDs explicitly.
        xslBuilder.addEntityResolver(createRepositoryEntityResolver());
        // Turn of strict mode because previous version of MCS allowed
        // garbage to de entered.
        xslBuilder.setStrictMode(false);

        // Add the 2005/12 RPDM schemas for output validation.
        xslBuilder.addOutputSchemata(PolicySchemas.REPOSITORY_2005_09);
        builder.addStep(rpdm27to30, repository200509, xslBuilder.getCompletedMigrator());

        // --------------------------------------------------------------------
        // Step: 2005/09 -> 2005/12
        // --------------------------------------------------------------------
        xslBuilder = builder.createXSLStreamMigratorBuilder();
        xslBuilder.setXSL(
                "/com/volantis/mcs/migrate/impl/config/lpdm/xsl/rpdm-200509-to-200512.xsl");

        xslBuilder.setStrictMode(strictMode);

        // Add schema for 2005/09
        xslBuilder.addInputSchemata(PolicySchemas.REPOSITORY_2005_09);
        // Add schema for 2005/12
        xslBuilder.addOutputSchemata(PolicySchemas.REPOSITORY_2005_12);
        builder.addStep(repository200509, repository200512,
                xslBuilder.getCompletedMigrator());

        // --------------------------------------------------------------------
        // Step: 2005/12 -> 2006/02
        // --------------------------------------------------------------------
        xslBuilder = builder.createXSLStreamMigratorBuilder();
        xslBuilder.setXSL(
                "/com/volantis/mcs/migrate/impl/config/lpdm/xsl/rpdm-200512-to-200602.xsl");
        xslBuilder.setStrictMode(strictMode);

        // Add schema for 2005/12
        xslBuilder.addInputSchemata(PolicySchemas.REPOSITORY_2005_12);

        // Add schema for 2006/02
        xslBuilder.addOutputSchemata(PolicySchemas.REPOSITORY_2006_02);
        builder.addStep(repository200512, repository200602, 
                xslBuilder.getCompletedMigrator());

        builder.endType();

        return builder.getCompletedResourceMigrator();
    }

    // Javadoc inherited
    public EntityResolver createRepositoryEntityResolver() {
        return new RepositoryEntityResolver();
    }

    // Javadoc inherited
    public RemotePolicyMigrator createRemotePolicyMigrator() {
        return new RemotePolicyMigratorImpl();
    }
}
