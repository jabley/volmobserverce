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
package com.volantis.mcs.migrate.api.config;

import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.InputMetadata;
import com.volantis.mcs.migrate.api.framework.OutputCreator;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.mcs.migrate.api.ExtensionClassLoader;
import com.volantis.synergetics.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Factory to create configuration related migration classes.
 */
public abstract class ConfigFactory {

    /**
     * The default instance.
     */
    private static final ConfigFactory defaultInstance;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        defaultInstance = (ConfigFactory) ExtensionClassLoader.loadExtension(
                "com.volantis.mcs.migrate.impl.config.DefaultConfigFactory",
                new NopConfigFactory(),
                null);
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static ConfigFactory getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Return a Entity Resolver that will resolve against the repository
     * @return an entity resolver
     */
    public abstract EntityResolver createRepositoryEntityResolver();

    /**
     * Create a resource migrator for migrating all relevant files to the
     * current version.
     *
     * @param reporter The notification reporter to use for feedback
     * @param strictMode The flag to indicate if the input validation failures should be informational only
     *          or should cause the migration to fail
     * @return a fully configured resource migrator
     * @throws ResourceMigrationException if a problem occurs when attempting
     * to migrate the resource.
     */
    public abstract ResourceMigrator createDefaultResourceMigrator(
            NotificationReporter reporter, boolean strictMode)
            throws ResourceMigrationException;

    /**
     * Create an object that can migrate a remote policy.
     *
     * @return A {@link RemotePolicyMigrator}.
     */
    public abstract RemotePolicyMigrator createRemotePolicyMigrator();


    /**
     * A simple config factory. This <b>MUST</b> be stateless as only one
     * is created to service all needs.
     */
    private static class NopConfigFactory extends ConfigFactory {

        public EntityResolver createRepositoryEntityResolver() {
            return new EntityResolver() {

                // return null as we will not resolve this. Null is a valid response
                public InputSource resolveEntity(
                        String publicId, String systemId)
                        throws SAXException, IOException {
                    return null;
                }
            };
        }

        /**
         * This method just copies the input to the output
         */
        // Rest of javadoc inherited
        public ResourceMigrator createDefaultResourceMigrator(
                NotificationReporter reporter, boolean strictMode)
                throws ResourceMigrationException {
            return new ResourceMigrator() {

                public void migrate(InputMetadata meta,
                                    InputStream inputStream,
                                    OutputCreator outputCreator)
                        throws IOException, ResourceMigrationException {
                    // just pass the data through
                    OutputStream outputStream =
                            outputCreator.createOutputStream();
                    IOUtils.copy(inputStream,outputStream);
                }
            };
        }

        /**
         * Create a Remote Policy Manager that does nothing.
         *
         * @return a Remote Policy Manager that does nothing.
         */
        public RemotePolicyMigrator createRemotePolicyMigrator() {
            return new RemotePolicyMigrator() {

                public String migratePolicy(InputStream stream, String url)
                        throws ResourceMigrationException, IOException {
                    // @todo somthing.
                    // My god this is ugly. Why on earth does this method take
                    // an InputStream and return a platform dependant String.
                    // The following code attempts to replicate the behaviour
                    // of the real policy migrator without doing any migration.
                    ByteArrayOutputCreator oc = new ByteArrayOutputCreator();
                    IOUtils.copyAndClose(stream, oc.getOutputStream());
                    return oc.getOutputStream().toString();
                }
            };
        }
    }

}
