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

package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferMock;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRenderedContent;
import com.volantis.mcs.protocols.separator.SeparatorArbitrator;
import com.volantis.mcs.protocols.separator.SeparatorArbitratorMock;
import com.volantis.mcs.protocols.separator.SeparatorManager;
import com.volantis.mcs.protocols.separator.SeparatorManagerMock;
import com.volantis.mcs.protocols.separator.SeparatorRendererMock;
import com.volantis.mcs.protocols.separator.shared.DefaultSeparatorManagerTestCase;
import com.volantis.testtools.mock.MockFactory;

public class MenuSeparatorManagerTestCase
        extends DefaultSeparatorManagerTestCase {

    protected OutputBuffer outputBuffer;

    /**
     * The item group arbitrator and its mock.
     */
    private SeparatorArbitratorMock mockGroupArbitrator;

    /**
     * The item group separator renderer and its mock.
     */
    private SeparatorRendererMock mockGroupSeparator;

    /**
     * The orientation separator and its mock.
     */
    private SeparatorRendererMock mockOrientationSeparator;

    /**
     * The orientatation manager and its mock.
     */
    private SeparatorManagerMock mockOrientationManager;

    /**
     * The manager to test.
     */
    private MenuSeparatorManager manager;

    protected void setUp() throws Exception {
        super.setUp();

        //sharedExpectations = mockFactory.createOrderedBuilder();

        outputBuffer = new OutputBufferMock("outputBuffer", sharedExpectations);

        mockGroupArbitrator = new SeparatorArbitratorMock(
                "groupArbitrator", sharedExpectations);

        mockGroupSeparator = new SeparatorRendererMock(
                "groupSeparator", sharedExpectations);

        mockOrientationSeparator = new SeparatorRendererMock(
                "orientationSeparator", sharedExpectations);

        // Create a mock separator manager.
        mockOrientationManager = new SeparatorManagerMock(
                "orientationManager", sharedExpectations);

        // Create the manager to test.
        manager = new MenuSeparatorManager(outputBuffer, mockGroupArbitrator,
                                           mockOrientationSeparator,
                                           mockOrientationManager);
    }

    protected SeparatorManager createSeparatorManager(
            SeparatorArbitrator arbitrator) {

        OutputBuffer buffer = createOutputBuffer();

        // Neither the orientation separator, or manager is affected by the
        // tests in the parent test case.
        SeparatorRendererMock mockSeparator = new SeparatorRendererMock(
                "separator", sharedExpectations);

        SeparatorManagerMock mockManager = new SeparatorManagerMock(
                "manager", sharedExpectations);

        SeparatorManager manager = new MenuSeparatorManager(
                buffer, arbitrator, mockSeparator, mockManager);

        // The underlying mock manager can be called any number of times.
        mockManager.expects.queueSeparator(mockSeparator).any();
        mockManager.expects.beforeContent(mockSeparatedContent1).any();
        mockManager.expects.beforeContent(mockSeparatedContent2).any();
        mockManager.expects.beforeContent(MenuItemRenderedContent.IMAGE).any();

        return manager;
    }

    /**
     * Test that the manager delegates to the orientation one properly.
     */
    public void testInteractions()
            throws Exception {

        System.out.println("test1 start");

        try {
            // Queuing a separator should not have any external visibility.
            manager.queueSeparator(mockGroupSeparator);

            // Writing some content should trigger the following in order.
            // 1) groupArbitrator.decide(....);
            //
            // 2) orientationManager.queueSeqarator(orientationSeparator);
            //
            // 3) orientationManager.beforeContent(....);
            //    The default behaviour for the manager would cause the
            //    orientation separator to be rendered but that is outside the
            //    scope of this test.
            //
            // 4) groupSeparator.render(....);
            //
            // 5) orientationManager.queueSeparator(groupSeparator);
            //
            // 6) orientationManager.beforeContent(TEXT);

            mockGroupArbitrator.fuzzy.decide(manager, MockFactory.getDefaultInstance().expectsAny())
                    .does(USE_DEFERRED_SEPARATOR);

            mockOrientationManager.expects.queueSeparator(mockOrientationSeparator);

            mockOrientationManager.expects.beforeContent(MenuItemRenderedContent.IMAGE);

            mockGroupSeparator.expects.render(outputBuffer);

            mockOrientationManager.expects.queueSeparator(mockOrientationSeparator);

            mockOrientationManager.expects.beforeContent(MenuItemRenderedContent.TEXT);

            // Test the expectations.
            manager.beforeContent(MenuItemRenderedContent.TEXT);
        } finally {
            System.out.println("test1 end");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 20-May-05	8277/5	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 12-May-05	8208/1	pduffin	VBM:2005051208 Committing mock object framework changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 ===========================================================================
*/
