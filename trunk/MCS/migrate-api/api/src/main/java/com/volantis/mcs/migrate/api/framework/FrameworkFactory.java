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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.api.framework;

import com.volantis.mcs.migrate.api.ExtensionClassLoader;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.synergetics.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A factory for creating the major API classes of the migration framework.
 */
public abstract class FrameworkFactory {

    /**
     * The default instance.
     */
    private static final FrameworkFactory defaultInstance;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
         defaultInstance = (FrameworkFactory) ExtensionClassLoader.loadExtension(
                "com.volantis.mcs.migrate.impl.framework.DefaultFrameworkFactory",
                new NoopFrameworkFactory(),
                null);

    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static FrameworkFactory getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Create a builder which can be used to build {@link ResourceMigrator}
     * instances.
     *
     * @param reporter used to report notifications to the user.
     * @return the builder created.
     */
    public abstract ResourceMigratorBuilder createResourceMigratorBuilder(
            NotificationReporter reporter);

    /**
     * Create a version object from a string representation of it's name.
     *
     * @param name the name of the version.
     * @return the version object corresponding to the name supplied.
     */
    public abstract Version createVersion(String name);

    /**
     * Create meta data to describe input data for migration.
     *
     * @param uri the URI of the input data.
     * @param requiresPathRecognition true if we can perform path recognition
     *      on the URI, false otherwise.
     * @return
     */
    public abstract InputMetadata createInputMetadata(String uri,
            boolean requiresPathRecognition);


    /**
     * A factory that does not perform migration
     */
    private static class NoopFrameworkFactory extends FrameworkFactory {

        public ResourceMigratorBuilder createResourceMigratorBuilder(NotificationReporter reporter) {
            return new ResourceMigratorBuilder() {

                // Javadoc inherited
                public ResourceMigrator getCompletedResourceMigrator() {
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

                // Javadoc inherited
                public void setTarget(Version version) {
                    // do nothing
                }

                // Javadoc inherited
                public void startType(String typeName) {
                    // do nothing
                }

                // Javadoc inherited
                public void setRegexpPathRecogniser(String re) {
                    // do nothing
                }

                // Javadoc inherited
                public void setCustomPathRecogniser(PathRecogniser pathRecogniser) {
                    // do nothing
                }

                // Javadoc inherited
                public void addRegexpContentRecogniser(Version version, String re) {
                    // do nothing
                }

                // Javadoc inherited
                public void addCustomContentRecogniser(
                        Version version,
                        ContentRecogniser contentRecogniser) {
                    // do nothing
                }

                // Javadoc inherited
                public XSLStreamMigratorBuilder createXSLStreamMigratorBuilder() {
                    return null;
                }

                // Javadoc inherited
                public void addStep(Version inputVersion,
                                    Version outputVersion,
                                    StreamMigrator streamMigrator) {
                    // do nothing
                }

                // Javadoc inherited
                public void endType() {
                    // do nothing
                }
            };
        }

        /**
         * Always return the current build version for the specified string
         *
         * @param name the string to create a version from
         * @return a Version representing the supplied string
         */
        public Version createVersion(final String name) {
            return new Version() {
                public String getName() {
                    return name;
                }
            };
        }

        // Javadoc inherited
        public InputMetadata createInputMetadata(
                final String uri, final boolean requiresPathRecognition) {
            // just return the values I was supplied with
            return new InputMetadata() {
                // Javadoc inherited
                public String getURI() {
                    return uri;
                }

                // Javadoc inherited
                public boolean applyPathRecognition() {
                    return requiresPathRecognition;
                }
            };
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	10098/3	phussain	VBM:2005110209 Migration Framework for Repository Parser - post-review amendments: new reporter type

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 19-May-05	8036/9	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8181/1	adrianj	VBM:2005050505 XDIME/CP migration CLI

 18-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
