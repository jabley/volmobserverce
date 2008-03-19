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

package com.volantis.mcs.dom;

import com.volantis.mcs.dom.impl.NodeSequenceImpl;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

/**
 * Test cases for {@link NodeSequence}.
 */
public class NodeSequenceTestCase
        extends NodeSequenceTestAbstract {

    private DOMFactory factory;
    private Text text1;
    private Text text2;
    private Text text3;
    private Text text4;

    protected void setUp() throws Exception {
        super.setUp();

        factory = DOMFactory.getDefaultInstance();

        text1 = factory.createText("text1");
        text2 = factory.createText("text2");
        text3 = factory.createText("text3");
        text4 = factory.createText("text4");
        final Element element = factory.createElement();
        element.addTail(text1);
        element.addTail(text2);
        element.addTail(text3);
        element.addTail(text4);
    }

    protected NodeSequence createEmptySequence() {
        return new NodeSequenceImpl(null, null);
    }

    /**
     * Ensure that a sequence with nodes from differing parents is invalid.
     */
    public void testDifferentParentsInvalid() throws Exception {
        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Element element = factory.createElement();
        element.addHead(text2);

        try {
            new NodeSequenceImpl(text1, text2);
            fail("Did not detect different parents");
        }
        catch (IllegalStateException expected) {
        }
    }

    /**
     * Ensure that a sequence with a single item visits that item once only.
     */
    public void testSingle() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        iterateeMock.expects.next(text1).returns(IterationAction.CONTINUE);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        NodeSequence sequence = new NodeSequenceImpl(text1, text1);
        sequence.forEach(iterateeMock);
    }

    /**
     * Ensure that a sequence with multiple items visits each item once only.
     */
    public void testMultiple() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                iterateeMock.expects.next(text1)
                        .returns(IterationAction.CONTINUE);
                iterateeMock.expects.next(text2)
                        .returns(IterationAction.CONTINUE);
                iterateeMock.expects.next(text3)
                        .returns(IterationAction.CONTINUE);
                iterateeMock.expects.next(text4)
                        .returns(IterationAction.CONTINUE);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        NodeSequence sequence = new NodeSequenceImpl(text1, text4);
        sequence.forEach(iterateeMock);
    }

    /**
     * Ensure that a sub sequence only visits the items in the sequence.
     */
    public void testSubSequence() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                iterateeMock.expects.next(text2)
                        .returns(IterationAction.CONTINUE);
                iterateeMock.expects.next(text3)
                        .returns(IterationAction.CONTINUE);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        NodeSequence sequence = new NodeSequenceImpl(text2, text3);
        sequence.forEach(iterateeMock);
    }
}
