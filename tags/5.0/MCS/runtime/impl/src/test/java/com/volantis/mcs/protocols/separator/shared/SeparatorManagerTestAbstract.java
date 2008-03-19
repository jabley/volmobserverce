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

package com.volantis.mcs.protocols.separator.shared;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.separator.ArbitratorDecision;
import com.volantis.mcs.protocols.separator.ArbitratorDecisionMock;
import com.volantis.mcs.protocols.separator.SeparatedContent;
import com.volantis.mcs.protocols.separator.SeparatedContentMock;
import com.volantis.mcs.protocols.separator.SeparatorArbitrator;
import com.volantis.mcs.protocols.separator.SeparatorArbitratorMock;
import com.volantis.mcs.protocols.separator.SeparatorManager;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.separator.SeparatorRendererMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.value.CompositeExpectedValue;
import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Base class for separator manager test cases.
 */
public abstract class SeparatorManagerTestAbstract
        extends TestCaseAbstract {

    /**
     * When passed to any arbitrator method this action causes it to make the
     * manager ignore the separator.
     */
    protected static final MethodAction IGNORE_SEPARATOR = new MethodAction() {
        public Object perform(MethodActionEvent event) {
            ArbitratorDecision decision
                    = (ArbitratorDecision)
                    event.getArgument(ArbitratorDecision.class);

            decision.ignore();

            // Return nothing.
            return null;
        }
    };

    /**
     * When passed to any arbitrator method this action causes it to make the
     * manager use the separator.
     */
    protected static final MethodAction USE_DEFERRED_SEPARATOR
            = new MethodAction() {

                public Object perform(MethodActionEvent event) {
                    ArbitratorDecision decision
                            = (ArbitratorDecision)
                            event.getArgument(ArbitratorDecision.class);

                    decision.use(decision.getDeferredSeparator());

                    // Return nothing.
                    return null;
                }
            };

    /**
     * When passed to any arbitrator method this action causes it to make the
     * manager defer using the separator.
     */
    protected static final MethodAction DEFER_DEFERRED_SEPARATOR = new MethodAction() {
        public Object perform(MethodActionEvent event) {
            ArbitratorDecision decision
                    = (ArbitratorDecision)
                    event.getArgument(ArbitratorDecision.class);

            decision.defer(decision.getDeferredSeparator());

            // Return nothing.
            return null;
        }
    };

    /**
     * The set of expectations shared by the mock objects.
     */
    protected ExpectationBuilder sharedExpectations;

    /**
     * The following are mock objects that are used within the test.
     *
     * <p>All of these objects share the same sequence of expectations.</p>
     */
    protected SeparatorRendererMock mockSeparatorRenderer1;
    protected SeparatorRendererMock mockSeparatorRenderer2;
    protected SeparatedContentMock mockSeparatedContent1;
    protected SeparatedContentMock mockSeparatedContent2;
    protected SeparatorArbitratorMock mockSeparatorArbitrator;

    /**
     * The {@link com.volantis.mcs.protocols.separator.SeparatorManager} under test.
     */
    protected SeparatorManager separatorManager;

    /**
     * An {@link com.volantis.mcs.protocols.OutputBuffer}, this did not need to be mocked.
     */
    protected OutputBuffer outputBuffer;

    protected void setUp() throws Exception {
        super.setUp();

        // Create a mock arbitrator and renderer that share sharedExpectations and
        // then create a separator manager to use.
        sharedExpectations = mockFactory.createUnorderedBuilder();

        mockSeparatorRenderer1 = new SeparatorRendererMock(
                "separatorRenderer1", sharedExpectations);

        mockSeparatorRenderer2 = new SeparatorRendererMock(
                "separatorRenderer2", sharedExpectations);

        mockSeparatedContent1 = new SeparatedContentMock(
                "separatedContent1", sharedExpectations);

        mockSeparatedContent2 = new SeparatedContentMock(
                "separatedContent2", sharedExpectations);

        mockSeparatorArbitrator = new SeparatorArbitratorMock(
                "separatorArbitrator", sharedExpectations);

        separatorManager = createSeparatorManager(mockSeparatorArbitrator);
        outputBuffer = separatorManager.getOutputBuffer();
    }

    /**
     * Create the SeparatorManager to test.
     * @return The SeparatorManager to test.
     */
    protected abstract SeparatorManager createSeparatorManager(SeparatorArbitrator arbitrator);

    /**
     * Test that {@link com.volantis.mcs.protocols.separator.SeparatorManager#getOutputBuffer} does not return null.
     */
    public void testGetOutputBuffer() {
        assertNotNull("outputBuffer must not be null",
                      outputBuffer);
    }

    /**
     * Test that {@link com.volantis.mcs.protocols.separator.SeparatorManager#getArbitrator} does not return null.
     */
    public void testGetArbitrator() {
        assertNotNull("arbitrator must not be null",
                      separatorManager.getArbitrator());
    }

    /**
     * Test that the flush method works.
     */
    public void testFlush()
            throws Exception {

        // Flush the separators.
        separatorManager.flush();
    }

    protected void expectDecide(SeparatorArbitratorMock mockArbitrator,
                                SeparatorManager manager,
                                SeparatedContent previousContent,
                                SeparatorRenderer deferredSeparator,
                                SeparatedContent triggeringContent,
                                SeparatorRenderer triggeringSeparator,
                                MethodAction action) {

        CompositeExpectedValue expectedDecisionValue =
            MockFactory.getDefaultInstance().expectsAll();
        ExpectedValue value;

        // All that we can tell about the decision object itself is that it is
        // a decison object, as the compiler should detect an invalid object
        // being passed we do not have anything to do.

        // We do however, know what the decision object should contain.
        value = expectsReturnValue(
                new SeparatedContentInvoker() {
                    public SeparatedContent invoke(Object object) {
                        return ((ArbitratorDecision) object).getPreviousContent();
                    }
                },
                previousContent);
        expectedDecisionValue.addExpectedValue(value);

        value = expectsReturnValue(
                new SeparatorRendererInvoker() {
                    public SeparatorRenderer invoke(Object object) {
                        return ((ArbitratorDecision) object).getDeferredSeparator();
                    }
                },
                deferredSeparator);
        expectedDecisionValue.addExpectedValue(value);

        value = expectsReturnValue(
                new SeparatedContentInvoker() {
                    public SeparatedContent invoke(Object object) {
                        return ((ArbitratorDecision) object).getTriggeringContent();
                    }
                },
                triggeringContent);
        expectedDecisionValue.addExpectedValue(value);

        value = expectsReturnValue(
                new SeparatorRendererInvoker() {
                    public SeparatorRenderer invoke(Object object) {
                        return ((ArbitratorDecision) object).getTriggeringSeparator();
                    }
                },
                triggeringSeparator);
        expectedDecisionValue.addExpectedValue(value);

        mockArbitrator.fuzzy.decide(separatorManager, expectedDecisionValue)
                .does(action);
    }

    private ExpectedReturnValue expectsReturnValue(SeparatedContentInvoker invoker, Object expectedValue) {
        return new ExpectedReturnValue(invoker, expectedValue);
    }

    private ExpectedReturnValue expectsReturnValue(SeparatorRendererInvoker invoker, Object expectedValue) {
        return new ExpectedReturnValue(invoker, expectedValue);
    }


    /**
     * Test that manager works as expected when arbitrator asks to ignore the
     * separator.
     */
    public void testIgnoreSeparator()
            throws Exception {

        // --------------------------------------------------------------------
        //   Render a separator.
        // --------------------------------------------------------------------

        // No expectations.

        separatorManager.queueSeparator(mockSeparatorRenderer1);

        // --------------------------------------------------------------------
        //   Write some content.
        // --------------------------------------------------------------------

        // Expect the arbitrator to be consulted and make it ignore the
        // separator.
        expectDecide(mockSeparatorArbitrator, separatorManager,
                     null, mockSeparatorRenderer1,
                     mockSeparatedContent1, null,
                     IGNORE_SEPARATOR);

        separatorManager.beforeContent(mockSeparatedContent1);

        // --------------------------------------------------------------------
        //   Render a separator.
        // --------------------------------------------------------------------

        // No expectations.

        separatorManager.queueSeparator(mockSeparatorRenderer2);

        // --------------------------------------------------------------------
        //   Write some content.
        // --------------------------------------------------------------------

        // Expect the arbitrator to be consulted and make it ignore the
        // separator.
        expectDecide(mockSeparatorArbitrator, separatorManager,
                     mockSeparatedContent1, mockSeparatorRenderer2,
                     mockSeparatedContent2, null,
                     IGNORE_SEPARATOR);

        separatorManager.beforeContent(mockSeparatedContent2);

        // --------------------------------------------------------------------
        //   Render a separator.
        // --------------------------------------------------------------------

        // No expectations.

        separatorManager.queueSeparator(mockSeparatorRenderer1);

        // --------------------------------------------------------------------
        //   Flush pending operations.
        // --------------------------------------------------------------------

        // Expect the arbitrator to be consulted and make it ignore the
        // separator.
        expectDecide(mockSeparatorArbitrator, separatorManager,
                     mockSeparatedContent2, mockSeparatorRenderer1,
                     null, null,
                     IGNORE_SEPARATOR);

        separatorManager.flush();
    }

    /**
     * Test that the manager works properly when the arbitrator asks to use the
     * separator.
     */
    public void testUseSeparator()
            throws Exception {

        // --------------------------------------------------------------------
        //   Render a separator.
        // --------------------------------------------------------------------

        // No expectations.

        separatorManager.queueSeparator(mockSeparatorRenderer1);

        // --------------------------------------------------------------------
        //   Write some content.
        // --------------------------------------------------------------------

        sharedExpectations.add(new OrderedExpectations() {
            public void add() {

                // Expect the arbitrator to be consulted and make it use the
                // separator.
                expectDecide(mockSeparatorArbitrator, separatorManager,
                             null, mockSeparatorRenderer1,
                             mockSeparatedContent1, null,
                             USE_DEFERRED_SEPARATOR);

                // Then expect the separator to be rendered.
                mockSeparatorRenderer1.expects.render(outputBuffer);
            }
        });

        separatorManager.beforeContent(mockSeparatedContent1);

        // --------------------------------------------------------------------
        //   Render a separator.
        // --------------------------------------------------------------------

        // No expectations.

        separatorManager.queueSeparator(mockSeparatorRenderer2);

        // --------------------------------------------------------------------
        //   Write some content.
        // --------------------------------------------------------------------

        sharedExpectations.add(new OrderedExpectations() {
            public void add() {

                // Expect the arbitrator to be consulted and make it use the
                // separator.
                expectDecide(mockSeparatorArbitrator, separatorManager,
                             mockSeparatedContent1, mockSeparatorRenderer2,
                             mockSeparatedContent2, null,
                             USE_DEFERRED_SEPARATOR);

                // Then expect the separator to be rendered.
                mockSeparatorRenderer2.expects.render(outputBuffer);
            }
        });

        separatorManager.beforeContent(mockSeparatedContent2);

        // --------------------------------------------------------------------
        //   Render a separator.
        // --------------------------------------------------------------------

        // No expectations.

        separatorManager.queueSeparator(mockSeparatorRenderer1);

        // --------------------------------------------------------------------
        //   Flush pending operations.
        // --------------------------------------------------------------------

        sharedExpectations.add(new OrderedExpectations() {
            public void add() {

                // Expect the arbitrator to be consulted and make it use the
                // separator.
                expectDecide(mockSeparatorArbitrator, separatorManager,
                             mockSeparatedContent2, mockSeparatorRenderer1,
                             null, null,
                             USE_DEFERRED_SEPARATOR);

                // Then expect the separator to be rendered.
                mockSeparatorRenderer1.expects.render(outputBuffer);
            }
        });

        separatorManager.flush();
    }

    /**
     * Test that the manager works properly when the arbitrator asks to defer
     * the separator, that is only allowed when two separators are written out
     * one after the other.
     */
    public void testDeferSeparator()
            throws Exception {

        // --------------------------------------------------------------------
        //   Render a separator.
        // --------------------------------------------------------------------

        // No expectations.

        separatorManager.queueSeparator(mockSeparatorRenderer1);

        // --------------------------------------------------------------------
        //   Render a separator.
        // --------------------------------------------------------------------

        // Expect the arbitrator to be consulted, make it defer the already
        // deferred separator.
        expectDecide(mockSeparatorArbitrator, separatorManager,
                     null, mockSeparatorRenderer1,
                     null, mockSeparatorRenderer2,
                     DEFER_DEFERRED_SEPARATOR);

        separatorManager.queueSeparator(mockSeparatorRenderer2);

        // --------------------------------------------------------------------
        //   Flush pending operations.
        // --------------------------------------------------------------------

        sharedExpectations.add(new OrderedExpectations() {
            public void add() {

                // Expect the arbitrator to be consulted and make it use the separator.
                expectDecide(mockSeparatorArbitrator, separatorManager,
                             null, mockSeparatorRenderer1,
                             null, null,
                             USE_DEFERRED_SEPARATOR);

                // Then expect the separator to be rendered.
                mockSeparatorRenderer1.expects.render(outputBuffer);
            }
        });

        separatorManager.flush();
    }

    /**
     * Test that the manager detects when an arbitrator accidentally tries to
     * defer its decision just before content is written out.
     * @throws java.lang.Exception
     */
    public void testDetectIllegalDefer()
            throws Exception {

        // --------------------------------------------------------------------
        //   Render a separator.
        // --------------------------------------------------------------------

        // No expectations.

        separatorManager.queueSeparator(mockSeparatorRenderer1);

        // --------------------------------------------------------------------
        //   Write some content.
        // --------------------------------------------------------------------

        // Expect the arbitrator to be consulted and make it defer using the
        // separator.
        expectDecide(mockSeparatorArbitrator, separatorManager,
                     null, mockSeparatorRenderer1,
                     mockSeparatedContent1, null,
                     DEFER_DEFERRED_SEPARATOR);

        try {
            // Add some content.
            separatorManager.beforeContent(mockSeparatedContent1);

            fail("Invalid deferral not detected");
        } catch (IllegalStateException e) {
            // Error detected.
        }
    }
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/1	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 20-May-05	8277/5	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 07-May-04	4164/2	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 07-Apr-04	3610/1	pduffin	VBM:2004032509 Added separator API and default implementation

 ===========================================================================
*/
