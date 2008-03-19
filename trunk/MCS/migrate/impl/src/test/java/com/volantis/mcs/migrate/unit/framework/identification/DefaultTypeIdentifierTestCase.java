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
package com.volantis.mcs.migrate.unit.framework.identification;

import com.volantis.mcs.migrate.api.framework.PathRecogniserMock;
import com.volantis.mcs.migrate.api.framework.VersionMock;
import com.volantis.mcs.migrate.api.framework.InputMetadataMock;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.impl.framework.graph.GraphMock;
import com.volantis.mcs.migrate.impl.framework.identification.ContentIdentifierMock;
import com.volantis.mcs.migrate.impl.framework.identification.DefaultTypeIdentifier;
import com.volantis.mcs.migrate.impl.framework.identification.IdentificationFactoryMock;
import com.volantis.mcs.migrate.impl.framework.identification.Match;
import com.volantis.mcs.migrate.impl.framework.identification.MatchMock;
import com.volantis.mcs.migrate.impl.framework.io.RestartInputStreamMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.java.io.InputStreamMock;
import mock.java.util.IteratorMock;

import java.io.IOException;
import java.util.Iterator;

/**
 * A test case for {@link DefaultTypeIdentifier}.
 */
public class DefaultTypeIdentifierTestCase extends TestCaseAbstract {

    private ExpectationBuilder expectations;

    private IdentificationFactoryMock mockIdentificationFactory;

    private MatchMock mockMatch;

    private PathRecogniserMock mockPathRecogniser;

    private ContentIdentifierMock mockContentIdentifier;

    private VersionMock mockVersion;

    private RestartInputStreamMock mockRestartStream;

    private GraphMock mockGraph;

    private Iterator mockIterator;

    private InputMetadataMock mockInputMetadata;

    private VersionMock targetMock;

    protected void setUp() throws Exception {

        expectations = mockFactory.createOrderedBuilder();

        mockIdentificationFactory = new IdentificationFactoryMock(
                "identificationFactory", expectations);

        mockMatch = new MatchMock("match", expectations);

        mockPathRecogniser = new PathRecogniserMock("path recog", expectations);

        mockContentIdentifier = new ContentIdentifierMock(
                "content recog", expectations);

        mockVersion = new VersionMock("version", expectations);

        final InputStreamMock mockInputStream = new InputStreamMock(
                "mockInputStream", expectations);
        mockInputStream.fuzzy.read(mockFactory.expectsArrayOf(byte.class))
                .returns(-1).any();
        mockInputStream.expects.close();

        mockRestartStream = new RestartInputStreamMock(
                "mockRestartStream", expectations, mockInputStream);
//        try {
//            mockInputStream = new RestartInputStreamMock(new InputStream() {
//                public int read() throws IOException {
//                    return -1;
//                }
//            });
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        mockGraph = new GraphMock("graph", expectations);

        mockIterator = new IteratorMock("iterator", expectations);

        mockInputMetadata = new InputMetadataMock("path", expectations);

        targetMock = new VersionMock("targetMock", expectations);
    }

    /**
     * Test creating a type identifier and calling it's getName() method.
     */
    public void testGetName()
            throws Exception {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Mocks all created in setUp().

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // None.

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultTypeIdentifier type = new DefaultTypeIdentifier(null, "name");
        assertEquals("", "name", type.getName());
    }

    /**
     * Test creating a type identifier with path and content recognisers which
     * match the input and a graph which returns a step sequence match.
     *
     * @throws IOException
     * @throws ResourceMigrationException
     */
    public void testFullMatch()
            throws IOException, ResourceMigrationException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Mocks all created in setUp().

        // ==================================================================
        // Create expectations.
        // ==================================================================

        mockInputMetadata.expects.applyPathRecognition().returns(true);
        mockInputMetadata.expects.getURI().returns("path");

        mockPathRecogniser.expects.recognisePath("path").returns(true);

        mockRestartStream.expects.restart();

        mockContentIdentifier.expects.identifyContent(mockRestartStream)
                .returns(true);
        mockContentIdentifier.expects.getVersion().returns(mockVersion);

        mockGraph.expects.getTargetVersion().returns(targetMock);
        mockGraph.expects.getSequence(mockVersion).returns(mockIterator);

        mockVersion.expects.getName().returns("version");

        mockIdentificationFactory.fuzzy
                .createMatch("type", "version",
                             mockFactory.expectsInstanceOf(Iterator.class))
                .returns(mockMatch);

        // ==================================================================
        // Do the test.
        // ==================================================================

        // Create the type.
        DefaultTypeIdentifier type = new DefaultTypeIdentifier(
                mockIdentificationFactory, "type");
        type.setPathRecogniser(mockPathRecogniser);
        type.addContentIdentifier(mockContentIdentifier);
        type.setGraph(mockGraph);

        // Do a query on the created type.
        Match actualMatch = type.identifyResource(mockInputMetadata, mockRestartStream);
        assertSame("", mockMatch, actualMatch);

    }

    /**
     * Test creating a type identifier with path recogniser that does not match
     * the input.
     *
     * @throws IOException
     * @throws ResourceMigrationException
     */
    public void testNoPathMatch()
            throws IOException, ResourceMigrationException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Mocks all created in setUp().

        // ==================================================================
        // Create expectations.
        // ==================================================================

        mockInputMetadata.expects.applyPathRecognition().returns(true);
        mockInputMetadata.expects.getURI().returns("path");

        mockPathRecogniser.expects.recognisePath("path").returns(false);

        // ==================================================================
        // Do the test.
        // ==================================================================

        // Create the type.
        DefaultTypeIdentifier type = new DefaultTypeIdentifier(
                mockIdentificationFactory, "type");
        type.setPathRecogniser(mockPathRecogniser);
        type.addContentIdentifier(mockContentIdentifier);
        type.setGraph(mockGraph);

        // Do a query on the created type.
        Match actualMatch = type.identifyResource(mockInputMetadata, mockRestartStream);
        assertNull("", actualMatch);

    }

    /**
     * Test creating a type identifier with path recogniser that matches
     * the input and a single content recogniser which does not match the input.
     *
     * @throws IOException
     * @throws ResourceMigrationException
     */
    public void testNoContentMatch()
            throws IOException, ResourceMigrationException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Mocks all created in setUp().

        // ==================================================================
        // Create expectations.
        // ==================================================================
        mockInputMetadata.expects.applyPathRecognition().returns(true);
        mockInputMetadata.expects.getURI().returns("path");

        mockPathRecogniser.expects.recognisePath("path").returns(true);

        mockRestartStream.expects.restart();

        mockContentIdentifier.expects.identifyContent(mockRestartStream)
                .returns(false);

        // ==================================================================
        // Do the test.
        // ==================================================================

        // Create the type.
        DefaultTypeIdentifier type = new DefaultTypeIdentifier(
                mockIdentificationFactory, "type");
        type.setPathRecogniser(mockPathRecogniser);
        type.addContentIdentifier(mockContentIdentifier);
        type.setGraph(mockGraph);

        // Do a query on the created type.
        Match actualMatch = type.identifyResource(mockInputMetadata, mockRestartStream);
        assertNull("", actualMatch);

    }

    /**
     * Test creating a type identifier with no path recogniser, when input requires this.
     * <p>
     * This should fail as a path recogniser is mandatory in this case.
     *
     * @throws ResourceMigrationException
     * @throws IOException
     */
    public void testFailureNoPathRecogniser()
            throws ResourceMigrationException, IOException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Mocks all created in setUp().

        // ==================================================================
        // Create expectations.
        // ==================================================================

        mockInputMetadata.expects.applyPathRecognition().returns(true);

        // ==================================================================
        // Do the test.
        // ==================================================================

        // Create the type.
        DefaultTypeIdentifier type = new DefaultTypeIdentifier(
                mockIdentificationFactory, "type");
        // type.setPathRecogniser(mockPathRecogniser);
        type.addContentIdentifier(mockContentIdentifier);
        type.setGraph(mockGraph);

        // Do a query on the created type.
        try {
            /*Match actualMatch = */type.identifyResource(mockInputMetadata, mockRestartStream);
        } catch (IllegalStateException e) {
            // success
        }
    }

    /**
     * Test creating a type identifier which does not need a path recognizer.
     * <p>
     * This should succeed because the input does not require path recognition.
     *
     * @throws ResourceMigrationException
     * @throws IOException
     */
    public void testPathRecogniserNotRequired()
            throws ResourceMigrationException, IOException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Mocks all created in setUp().

        // ==================================================================
        // Create expectations.
        // ==================================================================
        mockInputMetadata.expects.applyPathRecognition().returns(false);

        mockRestartStream.expects.restart();

        mockContentIdentifier.expects.identifyContent(mockRestartStream)
                .returns(true);
        mockContentIdentifier.expects.getVersion().returns(mockVersion);

        mockGraph.expects.getTargetVersion().returns(targetMock);

        mockGraph.expects.getSequence(mockVersion).returns(mockIterator);

        mockVersion.expects.getName().returns("version");

        mockIdentificationFactory.fuzzy
                .createMatch("type", "version",
                             mockFactory.expectsInstanceOf(Iterator.class))
                .returns(mockMatch);

        // None.

        // ==================================================================
        // Do the test.
        // ==================================================================

        // Create the type.
        DefaultTypeIdentifier type = new DefaultTypeIdentifier(
                mockIdentificationFactory, "type");
        // type.setPathRecogniser(mockPathRecogniser);
        type.addContentIdentifier(mockContentIdentifier);
        type.setGraph(mockGraph);

        // Do a query on the created type.
        try {
            /*Match actualMatch = */type.identifyResource(mockInputMetadata, mockRestartStream);
        } catch (IllegalStateException e) {
            fail("Path Recognition not required");
            // success
        }
    }

//    private void createMinimalExpectationsForMatch() {
//        mockRestartStream.expects.restart();
//
//        mockContentIdentifier.expects.identifyContent(mockRestartStream)
//                .returns(true);
//        mockContentIdentifier.expects.getVersion().returns(mockVersion);
//
//        mockGraph.expects.getSequence(mockVersion).returns(mockIterator);
//
//        mockVersion.expects.getName().returns("version");
//
//        mockIdentificationFactory.expects
//                .createMatch("type", "version",
//                             mockFactory.expectsInstanceOf(Iterator.class))
//                .returns(mockMatch);
//    }

    /**
     * Test creating a type identifier without any content identifiers.
     * <p>
     * This should fail as at least one content identifier is mandatory.
     *
     * @throws ResourceMigrationException
     * @throws IOException
     */
    public void testFailureNoContentIdentifiers()
            throws ResourceMigrationException, IOException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Mocks all created in setUp().

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // ==================================================================
        // Do the test.
        // ==================================================================

        // Create the type.
        DefaultTypeIdentifier type = new DefaultTypeIdentifier(
                mockIdentificationFactory, "type");
        type.setPathRecogniser(mockPathRecogniser);
        // type.addContentIdentifier(mockContentIdentifier);
        type.setGraph(mockGraph);

        // Do a query on the created type.
        try {
            /*Match actualMatch = */type.identifyResource(mockInputMetadata, mockRestartStream);
        } catch (IllegalStateException e) {
            // success
        }
    }

    /**
     * Test creating a type identifier with no graph.
     * <p>
     * This should fail as a graph is mandatory.
     *
     * @throws ResourceMigrationException
     * @throws IOException
     */
    public void testFailureNoGraph()
            throws ResourceMigrationException, IOException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // Mocks all created in setUp().

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // ==================================================================
        // Do the test.
        // ==================================================================

        // Create the type.
        DefaultTypeIdentifier type = new DefaultTypeIdentifier(
                mockIdentificationFactory, "type");
        type.setPathRecogniser(mockPathRecogniser);
        type.addContentIdentifier(mockContentIdentifier);
        // type.setGraph(mockGraph);

        // Do a query on the created type.
        try {
            /*Match actualMatch = */type.identifyResource(mockInputMetadata, mockRestartStream);
        } catch (IllegalStateException e) {
            // success
        }
    }

    // todo: later: test for multiple content recognisers

    // todo: later: test for exception from path and content recognisers
    // when paul makes MethodAction allow throwing an exception.

    // todo: later: fail if added content identifier versions are not unique.
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 19-May-05	8036/10	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/4	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
