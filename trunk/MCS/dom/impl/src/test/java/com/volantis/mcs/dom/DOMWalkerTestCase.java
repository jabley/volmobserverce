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

import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link DOMWalker}.
 */
public class DOMWalkerTestCase extends TestCaseAbstract {

    /**
     * Mock a simple DOM and try visiting it. Should test the basics.
     */
    public void test() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        final WalkingDOMVisitorMock visitorMock = new WalkingDOMVisitorMock(
                "visitor", expectations);

        // We have the following structure
        //                        root
        //                        /  \
        //                      c1    c2
        //                           /  \
        //                         gc1  gc2

        final DocumentMock documentMock = new DocumentMock("document", expectations);

        final ElementMock c1Mock = new ElementMock("c1", expectations);

        final ElementMock c2Mock = new ElementMock("c2", expectations);

        final ElementMock gc1Mock = new ElementMock("gc1", expectations);

        final ElementMock gc2Mock = new ElementMock("gc2", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        c1Mock.expects.getName().returns("c1").any();
        c2Mock.expects.getName().returns("c2").any();

        gc1Mock.expects.getName().returns("gc1").any();
        gc2Mock.expects.getName().returns("gc2").any();

        c1Mock.expects.isEmpty().returns(true).any();
        c2Mock.expects.isEmpty().returns(false).any();

        gc1Mock.expects.isEmpty().returns(true).any();
        gc2Mock.expects.isEmpty().returns(true).any();

        expectations.add(new OrderedExpectations() {
            public void add() {

                documentMock.expects.accept(visitorMock);

                visitorMock.expects.beforeChildren(documentMock);

                documentMock.fuzzy.forEachChild(mockFactory.expectsInstanceOf(
                        NodeIteratee.class)).does(
                                new ElementForEachMethodAction() {
                    public void iterate(NodeIteratee iteratee) {

                        iteratee.next(c1Mock);
                        iteratee.next(c2Mock);
                    }
                });

                c1Mock.expects.accept(visitorMock);

                c2Mock.expects.accept(visitorMock);

                visitorMock.expects.beforeChildren(c2Mock);

                c2Mock.fuzzy.forEachChild(mockFactory.expectsInstanceOf(
                        NodeIteratee.class)).does(
                                new ElementForEachMethodAction() {
                    public void iterate(NodeIteratee iteratee) {

                        iteratee.next(gc1Mock);
                        iteratee.next(gc2Mock);
                    }
                });

                gc1Mock.expects.accept(visitorMock);

                gc2Mock.expects.accept(visitorMock);

                visitorMock.expects.afterChildren(c2Mock);

                visitorMock.expects.afterChildren(documentMock);
            }
        });

        // ==================================================================
        // Do the test.
        // ==================================================================

        DOMWalker walker = new DOMWalker(visitorMock);

        walker.walk(documentMock);

    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 15-Jul-05	9067/1	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
