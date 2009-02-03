/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package mock.java.util;

import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

/**
 * Provides support for creating collections related mocks.
 */
public class CollectionsTestHelper {

    /**
     * Set the expectations on the iterator so that it appears to iterate over
     * the array.
     * @param expectations The expectations builder.
     * @param iteratorMock The iterator mock on who the expectations must be
     * set.
     * @param contents
     */
    public static void setIteratorContents(ExpectationBuilder expectations,
                                           final IteratorMock iteratorMock,
                                           final Object [] contents) {

        expectations.add(new OrderedExpectations() {
            public void add() {
                for (int i = 0; i < contents.length; i++) {
                    Object content = contents[i];
                    iteratorMock.expects.hasNext().returns(true).atLeast(1);
                    iteratorMock.expects.next().returns(content);
                }
                iteratorMock.expects.hasNext().returns(false);
            }
        });
    }

    /**
     * Add another iteration to the mock.
     *
     * @param iteratorMock The mock to which the expectations should be added.
     * @param object The object that is returned by the next iteration.
     */
    public static void addNextIteration(final IteratorMock iteratorMock,
                                        final Object object) {
        iteratorMock.expects.hasNext().returns(true).atLeast(1);
        iteratorMock.expects.next().returns(object);
    }

    public static void endIteration(IteratorMock iteratorMock) {
        iteratorMock.expects.hasNext().returns(false).atLeast(1);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/
