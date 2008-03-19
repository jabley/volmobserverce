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
package com.volantis.mcs.migrate.unit.framework.graph;

import com.volantis.mcs.migrate.api.framework.Version;
import com.volantis.mcs.migrate.api.framework.VersionMock;
import com.volantis.mcs.migrate.impl.framework.graph.DefaultGraph;
import com.volantis.mcs.migrate.impl.framework.identification.StepMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Iterator;

/**
 * Test case for {@link DefaultGraph}.
 */
public class DefaultGraphTestCase
        extends TestCaseAbstract {

    // todo: later: use expectation sequences instead of sets?

    private ExpectationBuilder expectations;

    private Version mockReallyOld;

    private Version mockOld;

    private Version mockQuiteOld;

    private Version mockTarget;

    protected void setUp() throws Exception {

        expectations = mockFactory.createUnorderedBuilder();

        mockReallyOld = new VersionMock("reallyOld", expectations);

        mockOld = new VersionMock("old", expectations);

        mockQuiteOld = new VersionMock("quiteOld", expectations);

        mockTarget = new VersionMock("target", expectations);

    }

    /**
     * Test creating and querying the simplest possible valid graph - i.e.
     * with one step.
     * <p>
     * This graph will have a single step who's output version is the target
     * version.
     */
    public void testSuccessTrivial() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        StepMock mockOld2Target = new StepMock("old2target", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        expectZeroToManyGetIOCalls(mockOld2Target, mockOld, mockTarget);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultGraph graph = new DefaultGraph(mockTarget);
        graph.addStep(mockOld2Target);

        Iterator sequence = graph.getSequence(mockOld);

        assertNotNull("Sequence must be non null", sequence);
        assertEquals("", mockOld2Target, sequence.next());
        assertEquals("", false, sequence.hasNext());

    }

    /**
     * Test creating and querying the simplest possible graph which forms a
     * joined path.
     * <p>
     * This is a graph will have two steps, one will have an output version
     * which matches the input version of the other and the other will have an
     * output version equal to the target.
     */
    public void testSuccessSimple() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        StepMock mockReallyOld2Old = new StepMock("reallyOld2Target",
                expectations);

        StepMock mockOld2Target = new StepMock("old2target", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        expectZeroToManyGetIOCalls(mockReallyOld2Old, mockReallyOld, mockOld);

        expectZeroToManyGetIOCalls(mockOld2Target, mockOld, mockTarget);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultGraph graph = new DefaultGraph(mockTarget);
        graph.addStep(mockOld2Target);
        graph.addStep(mockReallyOld2Old);

        Iterator oldSequence = graph.getSequence(mockOld);

        assertNotNull("Sequence must be non null", oldSequence);
        assertEquals("", mockOld2Target, oldSequence.next());
        assertEquals("", oldSequence.hasNext(), false);

        Iterator reallyOldSequence = graph.getSequence(mockReallyOld);

        assertNotNull("Sequence must be non null", reallyOldSequence);
        assertEquals("", mockReallyOld2Old, reallyOldSequence.next());
        assertEquals("", mockOld2Target, reallyOldSequence.next());
        assertEquals("", reallyOldSequence.hasNext(), false);
    }

    /**
     * Test creating and querying an invalid graph which has no steps.
     * <p>
     * This should fail since it makes no sense to query a graph with no steps.
     */
    public void testFailureNoSteps() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultGraph graph = new DefaultGraph(mockTarget);

        try {
            /*List sequence =*/ graph.getSequence(mockOld);
            fail("graph has no steps");
        } catch (IllegalStateException e) {
            // success
            e.printStackTrace(System.out);
        }
    }

    /**
     * Test creating a simple valid graph and querying for a version which
     * does not exist in a the graph.
     * <p>
     * This should fail as it makes no sense to identify and then request a
     * version which isn't in the graph. Note this problem should really be
     * caught by input data validation at a higher level.
     * <p>
     * This graph will have a single step who's output version is the target
     * version.
     */
    public void testFailureNoMatch() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        VersionMock mockUnknown = new VersionMock("unknown", expectations);

        StepMock mockOld2Target = new StepMock("old2target", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        expectZeroToManyGetIOCalls(mockOld2Target, mockOld, mockTarget);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultGraph graph = new DefaultGraph(mockTarget);
        graph.addStep(mockOld2Target);

        try {
            /*Iterator sequence =*/ graph.getSequence(mockUnknown);
            fail("retrieving a sequence of unknown version should fail");
        } catch (IllegalStateException e) {
            // success
            e.printStackTrace(System.out);
        }
    }

    /**
     * Test creating and querying the simplest possible graph which does not
     * terminate in the target version.
     * <p>
     * This should fail since all paths (and we only allow one at the moment)
     * must terminate in the target version.
     * <p>
     * This graph will have one step which has an output version different to
     * the target version.
     */
    public void testFailureMissingTarget() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        StepMock mockReallyOld2Old = new StepMock("reallyOld2Old",
                expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        expectZeroToManyGetIOCalls(mockReallyOld2Old, mockReallyOld, mockOld);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultGraph graph = new DefaultGraph(mockTarget);
        graph.addStep(mockReallyOld2Old);

        try {
            /*List sequence =*/ graph.getSequence(mockOld);
            fail("target is missing");
        } catch (IllegalStateException e) {
            // success
            e.printStackTrace(System.out);
        }
    }

    /**
     * Test creating and querying the simplest possible graph where one of the
     * steps is an "orphan".
     * <p>
     * This should fail as we do not allow orphan steps in the graph.
     * <p>
     * This graph will have two steps, one which has an output version which
     * is the target version and one which has input and output version which
     * is not the target version or the input version of the other step.
     */
    public void testFailureOrphan() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        StepMock mockReallyOld2Old = new StepMock("reallyOld2Old",
                expectations);

        StepMock mockQuiteOld2Target = new StepMock("quiteOld2target",
                expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        expectZeroToManyGetIOCalls(mockReallyOld2Old, mockReallyOld, mockOld);
        expectZeroToManyGetIOCalls(mockQuiteOld2Target, mockQuiteOld,
                mockTarget);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultGraph graph = new DefaultGraph(mockTarget);
        graph.addStep(mockQuiteOld2Target);
        graph.addStep(mockReallyOld2Old);

        try {
            /*List sequence =*/ graph.getSequence(mockReallyOld);
            fail("sequence contains orphan");
        } catch (IllegalStateException e) {
            // success
            e.printStackTrace(System.out);
        }
    }

    /**
     * Test creating and querying a simple graph where there are two ways to
     * arrive at the target version.
     * <p>
     * This graph will have two steps, both of which have an output version of
     * the target version.
     */
    public void testSuccessMultiPath() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        StepMock mockReallyOld2Target = new StepMock("reallyOld2Target",
                expectations);

        StepMock mockOld2Target = new StepMock("old2Target", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        expectZeroToManyGetIOCalls(mockReallyOld2Target, mockReallyOld,
                mockTarget);

        expectZeroToManyGetIOCalls(mockOld2Target, mockOld, mockTarget);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DefaultGraph graph = new DefaultGraph(mockTarget);
        graph.addStep(mockOld2Target);
        graph.addStep(mockReallyOld2Target);

        Iterator sequence;
        sequence = graph.getSequence(mockOld);
        assertNotNull("Sequence must be non null", sequence);
        assertEquals("", mockOld2Target, sequence.next());
        assertEquals("", false, sequence.hasNext());

        sequence = graph.getSequence(mockReallyOld);
        assertNotNull("Sequence must be non null", sequence);
        assertEquals("", mockReallyOld2Target, sequence.next());
        assertEquals("", false, sequence.hasNext());
    }

    private void expectZeroToManyGetIOCalls(
            StepMock stepMock, Version input,
            Version output) {

        stepMock.expects.getInput().returns(input).any();
        stepMock.expects.getOutput().returns(output).any();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 19-May-05	8036/11	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/9	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
