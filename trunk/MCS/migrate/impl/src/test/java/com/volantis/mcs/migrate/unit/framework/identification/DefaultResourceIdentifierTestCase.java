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
package com.volantis.mcs.migrate.unit.framework.identification;

import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.InputMetadataMock;
import com.volantis.mcs.migrate.impl.framework.identification.DefaultResourceIdentifier;
import com.volantis.mcs.migrate.impl.framework.identification.Match;
import com.volantis.mcs.migrate.impl.framework.identification.MatchMock;
import com.volantis.mcs.migrate.impl.framework.identification.TypeIdentifierMock;
import com.volantis.mcs.migrate.impl.framework.io.RestartInputStream;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;
import java.io.InputStream;

/**
 * A test case for {@link DefaultResourceIdentifier}.
 */
public class DefaultResourceIdentifierTestCase extends TestCaseAbstract {

    private ExpectationBuilder expectations;

    private TypeIdentifierMock mockTypeIdentifier;

    private InputMetadataMock mockInputMetadata;

    private RestartInputStream input;

    protected void setUp() throws Exception {

        expectations = mockFactory.createOrderedBuilder();

        mockInputMetadata = new InputMetadataMock("path", expectations);

        mockTypeIdentifier = new TypeIdentifierMock("type", expectations);

        input = new RestartInputStream(new InputStream() {
            public int read() throws IOException {

                return -1;
            }
        });

    }

    /**
     * Test creating a resource identifier with a single type identifier which
     * matches the input.
     *
     * @throws IOException
     * @throws ResourceMigrationException
     */
    public void testSingleTypeWithMatch()
            throws IOException, ResourceMigrationException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        MatchMock mockMatch = new MatchMock("match", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        mockTypeIdentifier.expects.identifyResource(mockInputMetadata, input)
                .returns(mockMatch);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultResourceIdentifier recogniser = new DefaultResourceIdentifier();
        recogniser.addType(mockTypeIdentifier);
        Match recognised = recogniser.identifyResource(mockInputMetadata, input);

        assertNotNull("", recognised);
        assertSame("", recognised, mockMatch);
    }

    /**
     * Test creating a resource identifier with a single type identifier which
     * does not match the input.
     *
     * @throws IOException
     * @throws ResourceMigrationException
     */
    public void testSingleTypeWithoutMatch()
            throws IOException, ResourceMigrationException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // Add an expectation that the resource recogniser will call
        // recogniseResource on the type and it will return null to indicate
        // no match.
        mockTypeIdentifier.expects.identifyResource(mockInputMetadata, input)
                .returns(null);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultResourceIdentifier recogniser = new DefaultResourceIdentifier();
        recogniser.addType(mockTypeIdentifier);
        Match recognised = recogniser.identifyResource(mockInputMetadata, input);

        assertNull("", recognised);
    }

    /**
     * Test creating a resource identifier with two types, one of which matches
     * the input.
     *
     * @throws IOException
     * @throws ResourceMigrationException
     */
    public void testTwoTypesWithSingleMatch()
            throws IOException, ResourceMigrationException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        MatchMock mockMatch = new MatchMock("match", expectations);

        // Create a mock type, which will return null..
        TypeIdentifierMock mockTypeIdentifier2 = new TypeIdentifierMock(
                "type2", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        mockTypeIdentifier.expects.identifyResource(mockInputMetadata, input)
                .returns(mockMatch);

        mockTypeIdentifier2.expects.identifyResource(mockInputMetadata, input)
                .returns(null);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultResourceIdentifier recogniser = new DefaultResourceIdentifier();
        recogniser.addType(mockTypeIdentifier);
        recogniser.addType(mockTypeIdentifier2);
        Match recognised = recogniser.identifyResource(mockInputMetadata, input);

        assertNotNull("", recognised);
        assertSame("", recognised, mockMatch);
    }

    /**
     * Test creating a resource identifier with two types, both of which match
     * the input.
     * <p>
     * This should fail as we do not know what to do when both type identifiers
     * claim ownership of the content.
     *
     * @throws IOException
     * @throws ResourceMigrationException
     */
    public void testFailureTwoTypesWithTwoMatches()
            throws IOException, ResourceMigrationException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        MatchMock mockMatch = new MatchMock("match", expectations);

        TypeIdentifierMock mockTypeIdentifier2 = new TypeIdentifierMock(
                "type2", expectations);

        MatchMock mockMatch2 = new MatchMock("match2", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        mockTypeIdentifier.expects.identifyResource(mockInputMetadata, input)
                .returns(mockMatch);

        mockTypeIdentifier2.expects.identifyResource(mockInputMetadata, input)
                .returns(mockMatch2);

        mockMatch2.expects.getTypeName().returns("match2-type");
        mockMatch2.expects.getVersionName().returns("match2-version");

        mockMatch.expects.getTypeName().returns("match-type");
        mockMatch.expects.getVersionName().returns("match-version");

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultResourceIdentifier recogniser = new DefaultResourceIdentifier();
        recogniser.addType(mockTypeIdentifier);
        recogniser.addType(mockTypeIdentifier2);
        try {
            Match recognised = recogniser.identifyResource(mockInputMetadata, input);
            fail("two matches are not valid");
        } catch (Exception e) {
            // success
        }
    }

    /**
     * Test creating and querying a resource identifier which has no types.
     * <p>
     * This should fail since it makes no sense to identify something without
     * any types to do the work.
     *
     * @throws IOException
     * @throws ResourceMigrationException
     */
    public void testFailureNoType()
            throws IOException, ResourceMigrationException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // ==================================================================
        // Do the test.
        // ==================================================================

        // Do the test.
        DefaultResourceIdentifier identifier = new DefaultResourceIdentifier();
        try {
            /*Match recognised = */identifier.identifyResource(mockInputMetadata, input);
            fail("can't identify without at least one type");
        } catch (IllegalStateException e) {
            // success
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 19-May-05	8036/15	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/13	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/11	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
