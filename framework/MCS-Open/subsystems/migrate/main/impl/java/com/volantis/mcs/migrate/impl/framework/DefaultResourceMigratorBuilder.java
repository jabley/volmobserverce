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
package com.volantis.mcs.migrate.impl.framework;

import com.volantis.mcs.migrate.api.framework.ContentRecogniser;
import com.volantis.mcs.migrate.api.framework.PathRecogniser;
import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.api.framework.ResourceMigratorBuilder;
import com.volantis.mcs.migrate.api.framework.StreamMigrator;
import com.volantis.mcs.migrate.api.framework.Version;
import com.volantis.mcs.migrate.api.framework.XSLStreamMigratorBuilder;
import com.volantis.mcs.migrate.impl.framework.graph.Graph;
import com.volantis.mcs.migrate.impl.framework.graph.GraphBuilder;
import com.volantis.mcs.migrate.impl.framework.graph.GraphFactory;
import com.volantis.mcs.migrate.impl.framework.identification.IdentificationFactory;
import com.volantis.mcs.migrate.impl.framework.identification.ResourceIdentifierBuilder;
import com.volantis.mcs.migrate.impl.framework.identification.TypeIdentifierBuilder;
import com.volantis.mcs.migrate.impl.framework.io.StreamBufferFactory;
import com.volantis.mcs.migrate.impl.framework.recogniser.RecogniserFactory;
import com.volantis.mcs.migrate.impl.framework.stream.StreamMigratorFactory;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;

/**
 * Default implementation of {@link ResourceMigratorBuilder}.
 */
public class DefaultResourceMigratorBuilder
        implements ResourceMigratorBuilder {

    /**
     * A factory for creating core framework objects.
     */
    private final CoreFactory coreFactory;

    /**
     * A factory for creating identification framework objects.
     */
    private final IdentificationFactory identificationFactory;

    /**
     * A factory for creating standard/built-in stream migrator framework
     * objects.
     */
    private final StreamMigratorFactory streamMigratorFactory;

    /**
     * A factory for creating graph framework objects.
     */
    private final GraphFactory graphFactory;

    /**
     * A factory for creating standard/built-in recogniser framework objects.
     */
    private final RecogniserFactory recogniserFactory;

    /**
     * A factory for creating s
     */
    private final StreamBufferFactory streamBufferFactory;

    /**
     * A builder used to create ResourceIdentifiers.
     */
    private ResourceIdentifierBuilder resourceIdentifierBuilder;

    /**
     * The version the created resource migrator will target.
     * <p>
     * This will the latest/current version of the product.
     */
    private Version targetVersion;

    /**
     * The current builder in use for creating a type identifier for the
     * resource migrator.
     */
    private TypeIdentifierBuilder currentTypeBuilder;

    /**
     * The current builder in use for creating the graph for the resource
     * migrator type identifier.
     */
    private GraphBuilder currentGraphBuilder;

    /**
     * Used to report user messages.
     */
    private NotificationReporter reporter;

    /**
     * Initialise.
     */
    public DefaultResourceMigratorBuilder(CoreFactory coreFactory,
            IdentificationFactory identificationFactory,
            StreamMigratorFactory streamMigratorFactory,
            GraphFactory graphFactory, RecogniserFactory recogniserFactory,
            StreamBufferFactory streamBufferFactory,
            NotificationReporter reporter) {

        this.coreFactory = coreFactory;
        this.identificationFactory = identificationFactory;
        this.streamMigratorFactory = streamMigratorFactory;
        this.graphFactory = graphFactory;
        this.recogniserFactory = recogniserFactory;

        resourceIdentifierBuilder =
                identificationFactory.createResourceIdentifierBuilder();
        this.streamBufferFactory = streamBufferFactory;
        this.reporter = reporter;
    }

    // Javadoc inherited.
    public void setTarget(Version targetVersion) {

        if (targetVersion == null) {
            throw new IllegalStateException();
        }
        this.targetVersion = targetVersion;
    }

    // Javadoc inherited.
    public void startType(String name) {

        setCurrentTypeIdentifierBuilder(
                identificationFactory.createTypeIdentifierBuilder());
        getCurrentTypeIdentifierBuilder().setName(name);
    }

    // Javadoc inherited.
    public void setRegexpPathRecogniser(String re) {

        getCurrentTypeIdentifierBuilder().setPathRecogniser(
                recogniserFactory.createRegexpPathRecogniser(re));
    }

    // Javadoc inherited.
    public void setCustomPathRecogniser(PathRecogniser pathRecogniser) {

        getCurrentTypeIdentifierBuilder().setPathRecogniser(pathRecogniser);
    }

    // Javadoc inherited.
    public void addRegexpContentRecogniser(Version version, String re) {

        getCurrentTypeIdentifierBuilder().addContentIdentifier(
                identificationFactory.createContentIdentifier(version,
                        recogniserFactory.createRegexpContentRecogniser(re)));
    }

    // Javadoc inherited.
    public void addCustomContentRecogniser(Version version,
            ContentRecogniser contentRecogniser) {

        getCurrentTypeIdentifierBuilder().addContentIdentifier(
                identificationFactory.createContentIdentifier(version,
                        contentRecogniser));
    }

    // Javadoc inherited.
    public XSLStreamMigratorBuilder createXSLStreamMigratorBuilder() {

        return streamMigratorFactory.createXSLStreamMigratorBuilder(reporter);
    }

    // Javadoc inherited.
    public void addStep(Version input, Version output,
            StreamMigrator streamMigrator) {

        currentGraphBuilder.addStep(identificationFactory.createStep(input,
                output, streamMigrator));
    }

    // Javadoc inherited.
    public void endType() {

        Graph graph = currentGraphBuilder.getCompletedGraph();
        getCurrentTypeIdentifierBuilder().setGraph(graph);
        resourceIdentifierBuilder.addTypeIdentifier(
                getCurrentTypeIdentifierBuilder().getCompletedTypeIdentifier());
        resetCurrentTypeIdentifier();
    }

    // Javadoc inherited.
    public ResourceMigrator getCompletedResourceMigrator() {

        return coreFactory.createResourceMigrator(streamBufferFactory,
                resourceIdentifierBuilder.getCompletedResourceIdentifier(),
                reporter);
    }

    private void setCurrentTypeIdentifierBuilder(
            TypeIdentifierBuilder typeBuilder) {

        if (currentTypeBuilder != null) {
            throw new IllegalStateException();
        }
        this.currentTypeBuilder = typeBuilder;
        this.currentGraphBuilder = graphFactory.createGraphBuilder();
        currentGraphBuilder.setTarget(targetVersion);
    }

    private TypeIdentifierBuilder getCurrentTypeIdentifierBuilder() {

        if (this.currentTypeBuilder == null) {
            throw new IllegalStateException();
        }
        return this.currentTypeBuilder;
    }

    private void resetCurrentTypeIdentifier() {

        currentTypeBuilder = null;
        currentGraphBuilder = null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 19-May-05	8036/16	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/11	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/9	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/4	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
