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
package com.volantis.mcs.migrate.unit.framework;

import com.volantis.mcs.migrate.impl.framework.CoreFactoryMock;
import com.volantis.mcs.migrate.impl.framework.DefaultResourceMigratorBuilder;
import com.volantis.mcs.migrate.impl.framework.identification.IdentificationFactoryMock;
import com.volantis.mcs.migrate.impl.framework.identification.TypeIdentifierBuilderMock;
import com.volantis.mcs.migrate.impl.framework.identification.ResourceIdentifierBuilderMock;
import com.volantis.mcs.migrate.impl.framework.identification.ContentIdentifierMock;
import com.volantis.mcs.migrate.impl.framework.identification.TypeIdentifierMock;
import com.volantis.mcs.migrate.impl.framework.identification.ResourceIdentifierMock;
import com.volantis.mcs.migrate.impl.framework.graph.GraphBuilderMock;
import com.volantis.mcs.migrate.impl.framework.graph.GraphFactoryMock;
import com.volantis.mcs.migrate.impl.framework.graph.GraphMock;
import com.volantis.mcs.migrate.impl.framework.io.StreamBufferFactoryMock;
import com.volantis.mcs.migrate.impl.framework.recogniser.RecogniserFactoryMock;
import com.volantis.mcs.migrate.impl.framework.stream.StreamMigratorFactoryMock;
import com.volantis.mcs.migrate.api.framework.ResourceMigratorBuilder;
import com.volantis.mcs.migrate.api.framework.VersionMock;
import com.volantis.mcs.migrate.api.framework.PathRecogniserMock;
import com.volantis.mcs.migrate.api.framework.ContentRecogniserMock;
import com.volantis.mcs.migrate.api.framework.ResourceMigratorMock;
import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.api.framework.StreamMigratorMock;
import com.volantis.mcs.migrate.api.notification.NotificationReporterMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link DefaultResourceMigratorBuilder}.
 */
public class DefaultResourceMigratorBuilderTestCase
        extends TestCaseAbstract {

    private ResourceMigratorBuilder builder;

    private ExpectationBuilder expectations;

    private CoreFactoryMock mockCoreFactory;
    private GraphFactoryMock mockGraphFactory;
    private RecogniserFactoryMock mockRecogniserFactory;
    private StreamBufferFactoryMock mockStreamBufferFactory;
    private NotificationReporterMock mockNotificationReporter;
    private VersionMock mockTargetVersion;
    private VersionMock mockContentVersion1;
    private VersionMock mockContentVersion2;
    private IdentificationFactoryMock mockIdentificationFactory;
    private TypeIdentifierBuilderMock mockTypeIdBuilder;
    private ResourceIdentifierBuilderMock mockResourceIdBuilder;
    private StreamMigratorFactoryMock mockStreamMigratorFactory;
    private GraphBuilderMock mockGraphBuilder;


    protected void setUp() throws Exception {

        expectations = mockFactory.createUnorderedBuilder();

        //Create mocks which are common to initialization of ResourceMigratorBuilder
        mockCoreFactory = new CoreFactoryMock("core factory", expectations);
        mockGraphFactory = new GraphFactoryMock("graph factory", expectations);
        mockRecogniserFactory =
                new RecogniserFactoryMock("recogniser factory", expectations);
        mockStreamBufferFactory =
                new StreamBufferFactoryMock("stream buffer factory", expectations);
        mockNotificationReporter =
                new NotificationReporterMock("notification reporter", expectations);
        mockTargetVersion = new VersionMock("target version", expectations);
        mockContentVersion1 = new VersionMock("content version 1", expectations);
        mockContentVersion2 = new VersionMock("content version 2", expectations);
        mockIdentificationFactory =
                new IdentificationFactoryMock("id factory", expectations);
        mockTypeIdBuilder =
                new TypeIdentifierBuilderMock("type id builder", expectations);
        mockResourceIdBuilder =
                new ResourceIdentifierBuilderMock("resource id builder", expectations);
        mockStreamMigratorFactory =
                new StreamMigratorFactoryMock("stream migrator factory", expectations);
        mockGraphBuilder = new GraphBuilderMock("graph builder", expectations);

    }

    //Create a basic, default builder, to which migration types (ie. start...end) can be added:
    private void initializeDefaultBuilder() {
        //Create expectations which are common to initialization of ResourceMigratorBuilder
        mockIdentificationFactory.expects.createResourceIdentifierBuilder()
                .returns(mockResourceIdBuilder);
        mockGraphFactory.expects.createGraphBuilder()
                .returns(mockGraphBuilder);
        mockIdentificationFactory.expects.createTypeIdentifierBuilder()
                .returns(mockTypeIdBuilder);
        mockTypeIdBuilder.expects.setName("custom type");
        mockGraphBuilder.expects.setTarget(mockTargetVersion);

        //Start-up the default builder...
        builder =
                new DefaultResourceMigratorBuilder(mockCoreFactory,
                        mockIdentificationFactory, mockStreamMigratorFactory,
                        mockGraphFactory, mockRecogniserFactory,
                        mockStreamBufferFactory, mockNotificationReporter);
        builder.setTarget(mockTargetVersion);
    }


    /**
     * Test that we can use the builder to create a vaid resource migrator,
     * exercising as many of the builder methods as possible.
     */
    public void testSuccess() {
        //todo: use common properties and methods as per testAddSimpleXSLStep(), etc?
        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Create a mock match that will be returned by the mock resource
        // identifier.
        CoreFactoryMock mockCoreFactory = new CoreFactoryMock("core factory", expectations);

        IdentificationFactoryMock mockIdentificationFactory =
                new IdentificationFactoryMock("id factory", expectations);

        StreamMigratorFactoryMock mockStreamMigratorFactory =
                new StreamMigratorFactoryMock("stream migrator factory", expectations);

        GraphFactoryMock mockGraphFactory = new GraphFactoryMock("graph factory", expectations);

        RecogniserFactoryMock mockRecogniserFactory =
                new RecogniserFactoryMock("recogniser factory", expectations);

        StreamBufferFactoryMock mockStreamBufferFactory =
                new StreamBufferFactoryMock("stream buffer factory", expectations);

        NotificationReporterMock mockNotificationReporter =
                new NotificationReporterMock("notification reporter", expectations);

        VersionMock mockTargetVersion = new VersionMock("target version", expectations);

        ResourceIdentifierBuilderMock mockResourceIdBuilder =
                new ResourceIdentifierBuilderMock("resource id builder", expectations);

        TypeIdentifierBuilderMock mockTypeIdBuilder =
                new TypeIdentifierBuilderMock("type id builder", expectations);

        GraphBuilderMock mockGraphBuilder = new GraphBuilderMock("graph builder", expectations);

        PathRecogniserMock mockPathRecogniser = new PathRecogniserMock("path recogniser", expectations);

        ContentRecogniserMock mockContentRecogniser1 =
                new ContentRecogniserMock("content recogniser 1", expectations);

        ContentRecogniserMock mockContentRecogniser2 =
                new ContentRecogniserMock("content recogniser 2", expectations);

        VersionMock mockContentVersion1 = new VersionMock("content version 1", expectations);

        VersionMock mockContentVersion2 = new VersionMock("content version 2", expectations);

        ContentIdentifierMock mockContentIdentifier1 =
                new ContentIdentifierMock("content identifier 1", expectations);

        ContentIdentifierMock mockContentIdentifier2 =
                new ContentIdentifierMock("content identifier 2", expectations);

        GraphMock mockGraph = new GraphMock("graph", expectations);

        TypeIdentifierMock mockTypeIdentifier = new TypeIdentifierMock("type identifier", expectations);

        ResourceIdentifierMock mockResourceIdentifier =
                new ResourceIdentifierMock("resource identifier", expectations);

        ResourceMigratorMock mockResourceMigrator =
                new ResourceMigratorMock("resource migrator", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        mockIdentificationFactory.expects.createResourceIdentifierBuilder()
                .returns(mockResourceIdBuilder);

        // todo: later: ask paul about factoring out type code below

        // Add the first type.
        {
            mockIdentificationFactory.expects.createTypeIdentifierBuilder()
                    .returns(mockTypeIdBuilder);

            mockGraphFactory.expects.createGraphBuilder()
                    .returns(mockGraphBuilder);

            mockGraphBuilder.expects.setTarget(mockTargetVersion);

            mockTypeIdBuilder.expects.setName("custom type");

            mockTypeIdBuilder.expects.setPathRecogniser(mockPathRecogniser);

            mockIdentificationFactory.expects
                    .createContentIdentifier(mockContentVersion1,
                            mockContentRecogniser1)
                    .returns(mockContentIdentifier1);

            mockTypeIdBuilder.expects.addContentIdentifier(mockContentIdentifier1);

            mockIdentificationFactory.expects
                    .createContentIdentifier(mockContentVersion2,
                            mockContentRecogniser2)
                    .returns(mockContentIdentifier2);

            mockTypeIdBuilder.expects.addContentIdentifier(mockContentIdentifier2);

            mockGraphBuilder.expects.getCompletedGraph().returns(mockGraph);

            mockTypeIdBuilder.expects.setGraph(mockGraph);

            mockTypeIdBuilder.expects.getCompletedTypeIdentifier()
                    .returns(mockTypeIdentifier);

            mockResourceIdBuilder.expects.addTypeIdentifier(mockTypeIdentifier);
        }

        // Add the second type.
        {
            mockIdentificationFactory.expects.createTypeIdentifierBuilder()
                    .returns(mockTypeIdBuilder);

            mockGraphFactory.expects.createGraphBuilder()
                    .returns(mockGraphBuilder);

            mockGraphBuilder.expects.setTarget(mockTargetVersion);

            mockTypeIdBuilder.expects.setName("regexp type");

            // Second type creates the recognisers itself.
            mockRecogniserFactory.expects
                    .createRegexpPathRecogniser("path re")
                    .returns(mockPathRecogniser);

            mockTypeIdBuilder.expects.setPathRecogniser(mockPathRecogniser);

            // Second type creates the recognisers itself.
            mockRecogniserFactory.expects
                    .createRegexpContentRecogniser("content re 1")
                    .returns(mockContentRecogniser1);

            mockIdentificationFactory.expects
                    .createContentIdentifier(mockContentVersion1,
                            mockContentRecogniser1)
                    .returns(mockContentIdentifier1);

            mockTypeIdBuilder.expects.addContentIdentifier(mockContentIdentifier1);

            // Second type creates the recognisers itself.
            mockRecogniserFactory.expects
                    .createRegexpContentRecogniser("content re 2")
                    .returns(mockContentRecogniser2);

            mockIdentificationFactory.expects
                    .createContentIdentifier(mockContentVersion2,
                            mockContentRecogniser2)
                    .returns(mockContentIdentifier2);

            mockTypeIdBuilder.expects
                    .addContentIdentifier(mockContentIdentifier2);

            mockGraphBuilder.expects.getCompletedGraph().returns(mockGraph);

            mockTypeIdBuilder.expects.setGraph(mockGraph);

            mockTypeIdBuilder.expects.getCompletedTypeIdentifier()
                    .returns(mockTypeIdentifier);

            mockResourceIdBuilder.expects
                    .addTypeIdentifier(mockTypeIdentifier);
        }

        mockResourceIdBuilder.expects.getCompletedResourceIdentifier().returns(mockResourceIdentifier);

        mockCoreFactory.expects
                .createResourceMigrator(mockStreamBufferFactory,
                        mockResourceIdentifier,
                        mockNotificationReporter)
                .returns(mockResourceMigrator);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultResourceMigratorBuilder builder =
                new DefaultResourceMigratorBuilder(mockCoreFactory,
                        mockIdentificationFactory, mockStreamMigratorFactory,
                        mockGraphFactory, mockRecogniserFactory,
                        mockStreamBufferFactory, mockNotificationReporter);

        builder.setTarget(mockTargetVersion);

        {
            builder.startType("custom type");

            builder.setCustomPathRecogniser(mockPathRecogniser);

            builder.addCustomContentRecogniser(mockContentVersion1,
                    mockContentRecogniser1);

            builder.addCustomContentRecogniser(mockContentVersion2,
                    mockContentRecogniser2);

            builder.endType();
        }

        {
            builder.startType("regexp type");

            builder.setRegexpPathRecogniser("path re");

            builder.addRegexpContentRecogniser(mockContentVersion1,
                    "content re 1");

            builder.addRegexpContentRecogniser(mockContentVersion2,
                    "content re 2");

            builder.endType();
        }

        ResourceMigrator resourceMigrator =
                builder.getCompletedResourceMigrator();

        assertSame("", mockResourceMigrator, resourceMigrator);

    }

    // todo: later: add failure test cases.

    // todo: test xsl builder
//    /**
//     * Test that a XSL Step which requires an entity resolver is processed correctly
//     */
//    public void testAddXSLStepWithEntityResolver() {
//        initializeDefaultBuilder();
//
//        // ==================================================================
//        // Create mocks.
//        // ==================================================================
//        EntityResolverMock mockEntityResolver = new EntityResolverMock("entity resolver", expectations);
//        StreamMigratorMock mockStreamMigrator = new StreamMigratorMock("stream migrator", expectations);
//        StepMock mockStep = new StepMock("step", expectations);
//
//        // ==================================================================
//        // Create expectations.
//        // ==================================================================
//
//        mockStreamMigratorFactory.expects
//                .createXSLStreamMigrator("path", mockEntityResolver, mockNotificationReporter).returns(mockStreamMigrator);
//        mockIdentificationFactory.expects
//                .createStep(mockContentVersion1, mockContentVersion2, mockStreamMigrator).returns(mockStep);
//        mockGraphBuilder.expects.addStep(mockStep);
//
//        // ==================================================================
//        // Do the test.
//        // ==================================================================
//        builder.startType("custom type");
//        builder.addXSLStep(mockContentVersion1, mockContentVersion2, "path", mockEntityResolver);
//    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 19-May-05	8036/13	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/9	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
