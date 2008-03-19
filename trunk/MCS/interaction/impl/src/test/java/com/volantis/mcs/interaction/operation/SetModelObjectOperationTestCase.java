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

package com.volantis.mcs.interaction.operation;

import com.volantis.mcs.interaction.impl.InternalProxyMock;
import com.volantis.mcs.interaction.impl.operation.SetModelObjectOperation;
import com.volantis.mcs.model.descriptor.OpaqueClassDescriptorMock;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.expectations.UnorderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link SetModelObjectOperation}.
 */
public class SetModelObjectOperationTestCase
        extends TestCaseAbstract {

    private static final String OLD_MODEL_OBJECT = "OLD";
    private static final String NEW_MODEL_OBJECT = "NEW";
    private InternalProxyMock internalProxyMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final OpaqueClassDescriptorMock opaqueClassDescriptorMock =
                new OpaqueClassDescriptorMock("opaqueClassDescriptorMock",
                        expectations);
        opaqueClassDescriptorMock.expects.getTypeClass()
                .returns(String.class).any();

        internalProxyMock = new InternalProxyMock("internalProxyMock", expectations);
    }

    /**
     * Test that executing the operation updates the proxy correctly.
     */
    public void testExecute() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================
        expectations.add(
                new OrderedExpectations() {
                    public void add() {
                        addInitialiseExpectations(this);
                        addExecuteExpectations(0);
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Operation operation = new SetModelObjectOperation(
                internalProxyMock, NEW_MODEL_OBJECT);
        operation.execute();
    }

    private void addInitialiseExpectations(OrderedExpectations expectations) {
        expectations.add(
                new UnorderedExpectations() {
                    public void add() {
                        // May retrieve the old value beforehand.
                        internalProxyMock.expects.getModelObject()
                                .returns(OLD_MODEL_OBJECT).optional();
                        internalProxyMock.expects.getModificationCount()
                                .description("initialise")
                                .returns(0);
                    }
                });
    }

    private void addExecuteExpectations(final int startModificationCount) {
        internalProxyMock.expects.getModificationCount()
                .description("execute")
                .returns(startModificationCount);
        internalProxyMock.expects.isReadOnly().returns(false);
        internalProxyMock.expects
                .setModelObject(NEW_MODEL_OBJECT)
                .returns(OLD_MODEL_OBJECT);
        internalProxyMock.expects.getModificationCount()
                .returns(startModificationCount + 1);
    }

    /**
     * Test that executing then undoing the operation works properly.
     */
    public void testExecuteUndo() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(
                new OrderedExpectations() {
                    public void add() {
                        addInitialiseExpectations(this);
                        addExecuteExpectations(0);
                        addUndoExpectations(1);
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Operation operation = new SetModelObjectOperation(
                internalProxyMock, NEW_MODEL_OBJECT);
        operation.execute();
        operation.undo();
    }

    /**
     * Test that executing then undoing then redoing the operation works
     * properly.
     */
    public void testExecuteUndoRedo() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(
                new OrderedExpectations() {
                    public void add() {
                        addInitialiseExpectations(this);
                        addExecuteExpectations(0);
                        addUndoExpectations(1);
                        addExecuteExpectations(2);
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Operation operation = new SetModelObjectOperation(
                internalProxyMock, NEW_MODEL_OBJECT);
        operation.execute();
        operation.undo();
        operation.redo();
    }

    /**
     * Test that executing then undoing then redoing then undoing the operation
     * works properly.
     */
    public void testExecuteUndoRedoUndo() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(
                new OrderedExpectations() {
                    public void add() {
                        addInitialiseExpectations(this);
                        addExecuteExpectations(0);
                        addUndoExpectations(1);
                        addExecuteExpectations(2);
                        addUndoExpectations(3);
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Operation operation = new SetModelObjectOperation(
                internalProxyMock, NEW_MODEL_OBJECT);
        operation.execute();
        operation.undo();
        operation.redo();
        operation.undo();
    }

    private void addUndoExpectations(final int startModificationCount) {
        internalProxyMock.expects.getModificationCount()
                .returns(startModificationCount);
        internalProxyMock.expects.isReadOnly().returns(false);
        internalProxyMock.expects
                .setModelObject(
                        OLD_MODEL_OBJECT)
                .returns(NEW_MODEL_OBJECT);
        internalProxyMock.expects.getModificationCount()
                .returns(startModificationCount + 1);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
