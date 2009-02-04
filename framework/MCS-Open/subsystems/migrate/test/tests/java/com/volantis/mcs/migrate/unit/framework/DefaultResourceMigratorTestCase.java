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

import com.volantis.mcs.migrate.impl.framework.DefaultResourceMigrator;
import com.volantis.mcs.migrate.impl.framework.identification.MatchMock;
import com.volantis.mcs.migrate.impl.framework.identification.ResourceIdentifierMock;
import com.volantis.mcs.migrate.impl.framework.identification.StepMock;
import com.volantis.mcs.migrate.impl.framework.io.RestartInputStream;
import com.volantis.mcs.migrate.impl.framework.io.StreamBufferFactoryMock;
import com.volantis.mcs.migrate.impl.framework.io.StreamBufferMock;
import com.volantis.mcs.migrate.api.notification.Notification;
import com.volantis.mcs.migrate.api.notification.NotificationReporterMock;
import com.volantis.mcs.migrate.api.framework.OutputCreatorMock;
import com.volantis.mcs.migrate.api.framework.InputMetadataMock;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.StepType;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.value.ExpectedValue;
import mock.java.io.InputStreamMock;
import mock.java.io.OutputStreamMock;
import mock.java.util.IteratorMock;

import java.io.IOException;

/**
 * A test case for {@link DefaultResourceMigrator}.
 */
public class DefaultResourceMigratorTestCase extends TestCaseAbstract {

    private static final ExpectedValue EXPECTS_INSTANCE_OF_RESTART_INPUT_STREAM
            = mockFactory.expectsInstanceOf(RestartInputStream.class);

    private static final ExpectedValue EXPECTS_INSTANCE_OF_NOTIFICATION =
            mockFactory.expectsInstanceOf(Notification.class);

    private ExpectationBuilder expectations;

    private ResourceIdentifierMock mockResourceIdentifier;

    private InputStreamMock mockInput;

    private OutputStreamMock mockOutput;

    private OutputCreatorMock mockOutputCreator;

    private StreamBufferFactoryMock mockStreamBufferFactory;

    private NotificationReporterMock mockNotificationReporter;

    private InputMetadataMock mockInputMetadata;

    public DefaultResourceMigratorTestCase() {
    }

    protected void setUp() throws Exception {

        expectations = mockFactory.createUnorderedBuilder();

        mockInputMetadata = new InputMetadataMock("inputMetadata", expectations);

        mockResourceIdentifier = new ResourceIdentifierMock(
                "identifier", expectations);

        mockInput = new InputStreamMock("mockInput", expectations);

        mockOutput = new OutputStreamMock("mockOutput", expectations);

        mockOutputCreator = new OutputCreatorMock(
                "creator", expectations);

        mockStreamBufferFactory = new StreamBufferFactoryMock(
                "streamBufferFactory", expectations);

        mockNotificationReporter = new NotificationReporterMock(
                "notification reporter", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        mockInput.fuzzy.read(mockFactory.expectsArrayOf(byte.class))
                .returns(-1).any();
        mockInput.expects.close();

    }

    /**
     * Create a resource migrator and a resource identifier which cannot
     * identity the input resource.
     *
     * @throws ResourceMigrationException
     * @throws IOException
     */
    public void testNoMatch() throws ResourceMigrationException, IOException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // Will copy to the output stream.
        mockOutput.expects.flush().any();
        mockOutput.expects.close();

        expectations.add(new OrderedExpectations() {
            public void add() {
                mockResourceIdentifier.fuzzy.identifyResource(
                        mockInputMetadata,
                        EXPECTS_INSTANCE_OF_RESTART_INPUT_STREAM)
                        .returns(null);

                // TODO: ask paul how to do more checking of the notification content here
                mockNotificationReporter.fuzzy.reportNotification(
                        EXPECTS_INSTANCE_OF_NOTIFICATION);

                mockOutputCreator.expects.createOutputStream()
                        .returns(mockOutput);
            }
        });

        mockInputMetadata.expects.getURI().returns("URI").any();

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultResourceMigrator migrator = new DefaultResourceMigrator(
                mockStreamBufferFactory, mockResourceIdentifier,
                mockNotificationReporter);

        migrator.migrate(mockInputMetadata, mockInput, mockOutputCreator);
    }

    /**
     * Create a resource migrator and a resource identifier which identifies
     * the input resource with a match that has no steps.
     * <p>
     * This should fail as matches must always have at least one step.
     *
     * @throws IOException
     * @throws ResourceMigrationException
     */
    public void testZeroStepMatch()
            throws IOException, ResourceMigrationException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Create a mock match that will be returned by the mock resource
        // identifier.
        final MatchMock mockMatch = new MatchMock("match", expectations);

        // Create an empty mock step sequence that the mock match will return.
        final IteratorMock mockSequence = new IteratorMock(
                "iterator", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // This never uses the output stream.

        expectations.add(new OrderedExpectations() {
            public void add() {

                // Create an expectation that the mock resource identifier will return
                // the mock match.
                mockResourceIdentifier.fuzzy.identifyResource(
                        mockInputMetadata,
                        EXPECTS_INSTANCE_OF_RESTART_INPUT_STREAM)
                        .returns(mockMatch);

                // Create an expectation that the mock match will return a step
                // sequence.

                mockInputMetadata.expects.getURI().returns("uri");

                mockMatch.expects.getTypeName().returns("type");

                mockMatch.expects.getVersionName().returns("version");

                // TODO: ask paul how to do more checking of the notification content here
                mockNotificationReporter.fuzzy.reportNotification(
                        EXPECTS_INSTANCE_OF_NOTIFICATION);

                mockMatch.expects.getSequence().returns(mockSequence);

                mockSequence.expects.hasNext().returns(false);
            }
        });

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultResourceMigrator migrator = new DefaultResourceMigrator(
                mockStreamBufferFactory, mockResourceIdentifier,
                mockNotificationReporter);

        try {
            migrator.migrate(mockInputMetadata, mockInput, mockOutputCreator);
            fail("Migrator should fail if sequence empty");
        } catch (IllegalStateException e) {
            // success
        }
    }

    /**
     * Create a resource migrator and a resource identifier which identifies
     * the input resource with a match that has one steps.
     *
     * @throws IOException
     * @throws ResourceMigrationException
     */
    public void testSingleStepMatch()
            throws IOException, ResourceMigrationException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Create a mock match that will be returned by the mock resource
        // identifier.
        final MatchMock mockMatch = new MatchMock("match", expectations);

        // Create a mock step sequence that the mock match will return.
        final IteratorMock mockIterator = new IteratorMock("iterator", expectations);

        final StepMock mockStep = new StepMock("step", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // Will copy to the output stream.
        mockOutput.expects.flush().any();
        mockOutput.expects.close();

        expectations.add(new OrderedExpectations() {
            public void add() {

                // Create an expectation that the mock resource identifier will return
                // the mock match.
                mockResourceIdentifier.fuzzy.identifyResource(
                        mockInputMetadata,
                        EXPECTS_INSTANCE_OF_RESTART_INPUT_STREAM)
                        .returns(mockMatch);

                mockInputMetadata.expects.getURI().returns("uri");

                mockMatch.expects.getTypeName().returns("type");

                mockMatch.expects.getVersionName().returns("version");

                // TODO: ask paul how to do more checking of the notification content here
                mockNotificationReporter.fuzzy.reportNotification(
                        EXPECTS_INSTANCE_OF_NOTIFICATION);

                // Create an expectation that the mock match will return a step
                // sequence.
                mockMatch.expects.getSequence().returns(mockIterator);

                // todo: later: Use repeated expectation in new version of mock here.
                mockIterator.expects.hasNext().returns(true);
                mockIterator.expects.hasNext().returns(true);

                mockIterator.expects.next().returns(mockStep);

                mockIterator.expects.hasNext().returns(false);

                // Create an expectation that the output creator will be called to
                // create an output stream.
                mockOutputCreator.expects.createOutputStream()
                        .returns(mockOutput);

                mockIterator.expects.hasNext().returns(false);

                // Create an expectation that the step returned will be called with
                // the fake input and output above.
                mockStep.fuzzy.migrate(
                        EXPECTS_INSTANCE_OF_RESTART_INPUT_STREAM,
                        mockOutput, StepType.ONLY);

                mockIterator.expects.hasNext().returns(false);
            }
        });

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultResourceMigrator migrator =
                new DefaultResourceMigrator(mockStreamBufferFactory,
                                            mockResourceIdentifier, mockNotificationReporter);

        migrator.migrate(mockInputMetadata, mockInput, mockOutputCreator);
    }

    /**
     * Create a resource migrator and a resource identifier which identifies
     * the input resource with a match that has two steps.
     *
     * @throws IOException
     * @throws ResourceMigrationException
     */
    public void testTwoStepMatch()
            throws IOException, ResourceMigrationException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Create a mock match that will be returned by the mock resource
        // identifier.
        final MatchMock mockMatch = new MatchMock("match", expectations);

        // Create a mock step sequence that the mock match will return.
        final IteratorMock mockIterator = new IteratorMock("iterator", expectations);
        final StepMock mockStep1 = new StepMock("step1", expectations);
        final StepMock mockStep2 = new StepMock("step2", expectations);

        // Create a mock stream buffer and associated buffer streams that the
        // resource migrator will use to copy from step 1 to step 2.
        final StreamBufferMock mockStreamBuffer = new StreamBufferMock(
                "streamBuffer", expectations);
        final InputStreamMock mockBufferInput = new InputStreamMock(
                "mockBufferInput", expectations);
        final OutputStreamMock mockBufferOutput = new OutputStreamMock(
                "mockBufferOutput", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // Will copy to the output stream.
        mockOutput.expects.flush().any();
        mockOutput.expects.close();

        mockBufferOutput.expects.close();
        mockBufferInput.expects.close();

        expectations.add(new OrderedExpectations() {
            public void add() {

                // Create an expectation that the mock resource identifier will return
                // the mock match.
                mockResourceIdentifier.fuzzy.identifyResource(
                        mockInputMetadata,
                        EXPECTS_INSTANCE_OF_RESTART_INPUT_STREAM)
                        .returns(mockMatch);

                mockInputMetadata.expects.getURI().returns("uri");

                mockMatch.expects.getTypeName().returns("type");

                mockMatch.expects.getVersionName().returns("version");

                // TODO: ask paul how to do more checking of the notification content here
                mockNotificationReporter.fuzzy.reportNotification(
                        EXPECTS_INSTANCE_OF_NOTIFICATION);

                // The mock match will return a step sequence.
                mockMatch.expects.getSequence().returns(mockIterator);

                // todo: later: Use repeated expectation in new version of mock here.
                mockIterator.expects.hasNext().returns(true);
                mockIterator.expects.hasNext().returns(true);

                mockIterator.expects.next().returns(mockStep1);

                mockIterator.expects.hasNext().returns(true);

                // The stream buffer factory will be called to create a stream buffer.
                mockStreamBufferFactory.expects.create()
                        .returns(mockStreamBuffer);

                // The stream buffer will be called to return it's output.
                mockStreamBuffer.expects.getOutput().returns(mockBufferOutput);

                mockIterator.expects.hasNext().returns(true);

                // The first step will be called with the fake input and fake buffer
                // output.
                mockStep1.fuzzy.migrate(
                        EXPECTS_INSTANCE_OF_RESTART_INPUT_STREAM,
                        mockBufferOutput, StepType.FIRST);

                mockIterator.expects.hasNext().returns(true);

                mockIterator.expects.next().returns(mockStep2);

                // The stream buffer will be called to return it's output.
                mockStreamBuffer.expects.getInput().returns(mockBufferInput);

                mockIterator.expects.hasNext().returns(false);

                // The output creator will be called to create an output stream.
                mockOutputCreator.expects.createOutputStream()
                        .returns(mockOutput);

                mockIterator.expects.hasNext().returns(false);

                // The second step will be called with the fake buffer input and
                // fake output.
                mockStep2.expects.migrate(mockBufferInput, mockOutput,
                    StepType.LAST);

                mockIterator.expects.hasNext().returns(false);
            }
        });

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultResourceMigrator migrator = new DefaultResourceMigrator(
                mockStreamBufferFactory, mockResourceIdentifier,
                mockNotificationReporter);

        migrator.migrate(mockInputMetadata, mockInput, mockOutputCreator);
    }

    // todo: later: a test for step exception when paul adds to MethodAction

    // todo: later: a test for three steps would be good too.
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 19-May-05	8036/11	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/8	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
