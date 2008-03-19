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

package com.volantis.mcs.protocols.html.menu;

import com.volantis.mcs.protocols.menu.shared.renderer.HorizontalSeparatorRendererMock;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRenderedContent;
import com.volantis.mcs.protocols.menu.shared.renderer.VerticalSeparatorRendererMock;
import com.volantis.mcs.protocols.separator.ArbitratorDecisionMock;
import com.volantis.mcs.protocols.separator.SeparatedContent;
import com.volantis.mcs.protocols.separator.SeparatorArbitrator;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.expectations.UnorderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class HTMLMenuOrientationSeparatorArbitratorTestCase
        extends TestCaseAbstract {

    /**
     * The following are mock objects that are used within the test.
     *
     * <p>All of these objects share the same sequence of expectations.</p>
     */
    private ArbitratorDecisionMock mockArbitratorDecision;

    private HorizontalSeparatorRendererMock mockHorizontalSeparator;

    private VerticalSeparatorRendererMock mockVerticalSeparator;

    private SeparatorArbitrator separatorArbitrator;
    private ExpectationBuilder expectations;

    protected void setUp() throws Exception {
        super.setUp();

        expectations = mockFactory.createUnorderedBuilder();

        mockArbitratorDecision = new ArbitratorDecisionMock(
                "arbitratorDecisionMock", expectations);
        //mockArbitratorDecision.setExpectations(mockFactory.createSet());

        mockHorizontalSeparator = new HorizontalSeparatorRendererMock(
                "horizontalSeparatorRendererMock", expectations);

        mockVerticalSeparator = new VerticalSeparatorRendererMock(
                "verticalSeparatorRendererMock", expectations);

        separatorArbitrator = new HTMLMenuOrientationSeparatorArbitrator();
    }

    protected void expectDecisionGetters(
            final ArbitratorDecisionMock decision,
            final SeparatedContent previousContent,
            final SeparatorRenderer deferredSeparator,
            final SeparatedContent triggeringContent,
            final SeparatorRenderer triggeringSeparator) {

        expectations.add(new UnorderedExpectations() {
            public void add() {
                decision.expects.getPreviousContent()
                        .returns(previousContent).any();

                decision.expects.getDeferredSeparator()
                        .returns(deferredSeparator).any();

                decision.expects.getTriggeringContent()
                        .returns(triggeringContent).any();

                decision.expects.getTriggeringSeparator()
                        .returns(triggeringSeparator).any();
            }
        });
    }

    /**
     * Test that the arbitrator will ignore a horizontal separator between two
     * images.
     */
    public void testHorizontalImageAndImage() {

        expectDecisionGetters(mockArbitratorDecision,
                              MenuItemRenderedContent.IMAGE,
                              mockHorizontalSeparator,
                              MenuItemRenderedContent.IMAGE,
                              null);

        // Should ignore the separator.
        mockArbitratorDecision.expects.ignore();

        // Test the object.
        separatorArbitrator.decide(null, mockArbitratorDecision);
    }

    /**
     * Test that the arbitrator will use a horizontal separator between
     * text and an image.
     */
    public void testHorizontalTextThenImage() {

        // Try text / image.
        expectDecisionGetters(mockArbitratorDecision,
                              MenuItemRenderedContent.TEXT,
                              mockHorizontalSeparator,
                              MenuItemRenderedContent.IMAGE,
                              null);

        // Should use the separator.
        mockArbitratorDecision.expects.use(mockHorizontalSeparator);

        // Test the object.
        separatorArbitrator.decide(null, mockArbitratorDecision);
    }


    /**
     * Test that the arbitrator will use a horizontal separator between
     * image and text.
     */
    public void testHorizontalImageThenText() {

        // Try text / image.
        expectDecisionGetters(mockArbitratorDecision,
                              MenuItemRenderedContent.IMAGE,
                              mockHorizontalSeparator,
                              MenuItemRenderedContent.TEXT,
                              null);

        // Should use the separator.
        mockArbitratorDecision.expects.use(mockHorizontalSeparator);

        // Test the object.
        separatorArbitrator.decide(null, mockArbitratorDecision);
    }

    /**
     * Test that the arbitrator will use a horizontal separator between
     * both and image.
     */
    public void testHorizontalBothThenImage() {
        // Try both / image.
        expectDecisionGetters(mockArbitratorDecision,
                              MenuItemRenderedContent.BOTH,
                              mockHorizontalSeparator,
                              MenuItemRenderedContent.IMAGE,
                              null);

        // Should use the separator.
        mockArbitratorDecision.expects.use(mockHorizontalSeparator);

        // Test the object.
        separatorArbitrator.decide(null, mockArbitratorDecision);
    }

    /**
     * Test that the arbitrator will use a horizontal separator between
     * image and both.
     */
    public void testHorizontalImageThenBoth() {
        // Try both / image.
        expectDecisionGetters(mockArbitratorDecision,
                              MenuItemRenderedContent.IMAGE,
                              mockHorizontalSeparator,
                              MenuItemRenderedContent.BOTH,
                              null);

        // Should use the separator.
        mockArbitratorDecision.expects.use(mockHorizontalSeparator);

        // Test the object.
        separatorArbitrator.decide(null, mockArbitratorDecision);
    }

    /**
     * Test that the arbitrator will use a vertical separator between two
     * images.
     */
    public void testVerticalImageAndImage() {

        expectDecisionGetters(mockArbitratorDecision,
                              MenuItemRenderedContent.IMAGE,
                              mockVerticalSeparator,
                              MenuItemRenderedContent.IMAGE,
                              null);

        // Should use the separator.
        mockArbitratorDecision.expects.use(mockVerticalSeparator);

        // Test the object.
        separatorArbitrator.decide(null, mockArbitratorDecision);
    }

    /**
     * Test that the arbitrator will use a vertical separator between
     * text and an image.
     */
    public void testVerticalTextThenImage() {

        // Try text / image.
        expectDecisionGetters(mockArbitratorDecision,
                              MenuItemRenderedContent.TEXT,
                              mockVerticalSeparator,
                              MenuItemRenderedContent.IMAGE,
                              null);

        // Should use the separator.
        mockArbitratorDecision.expects.use(mockVerticalSeparator);

        // Test the object.
        separatorArbitrator.decide(null, mockArbitratorDecision);
    }


    /**
     * Test that the arbitrator will use a vertical separator between
     * image and text.
     */
    public void testVerticalImageThenText() {

        // Try text / image.
        expectDecisionGetters(mockArbitratorDecision,
                              MenuItemRenderedContent.IMAGE,
                              mockVerticalSeparator,
                              MenuItemRenderedContent.TEXT,
                              null);

        // Should use the separator.
        mockArbitratorDecision.expects.use(mockVerticalSeparator);

        // Test the object.
        separatorArbitrator.decide(null, mockArbitratorDecision);
    }

    /**
     * Test that the arbitrator will use a vertical separator between
     * both and image.
     */
    public void testVerticalBothThenImage() {
        // Try both / image.
        expectDecisionGetters(mockArbitratorDecision,
                              MenuItemRenderedContent.BOTH,
                              mockVerticalSeparator,
                              MenuItemRenderedContent.IMAGE,
                              null);

        // Should use the separator.
        mockArbitratorDecision.expects.use(mockVerticalSeparator);

        // Test the object.
        separatorArbitrator.decide(null, mockArbitratorDecision);
    }

    /**
     * Test that the arbitrator will use a vertical separator between
     * image and both.
     */
    public void testVerticalImageThenBoth() {
        // Try both / image.
        expectDecisionGetters(mockArbitratorDecision,
                              MenuItemRenderedContent.IMAGE,
                              mockVerticalSeparator,
                              MenuItemRenderedContent.BOTH,
                              null);

        // Should use the separator.
        mockArbitratorDecision.expects.use(mockVerticalSeparator);

        // Test the object.
        separatorArbitrator.decide(null, mockArbitratorDecision);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 20-May-05	8277/5	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 12-May-05	8208/1	pduffin	VBM:2005051208 Committing mock object framework changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-May-04	4164/3	pduffin	VBM:2004050404 Fixed some problems with HTML orientation separator

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
