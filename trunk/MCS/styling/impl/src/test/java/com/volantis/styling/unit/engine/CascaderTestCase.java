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

package com.volantis.styling.unit.engine;

import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.StylesMock;
import com.volantis.styling.impl.engine.Cascader;
import com.volantis.styling.impl.engine.InheriterMock;
import com.volantis.styling.impl.engine.StandardStylerContext;
import com.volantis.styling.impl.engine.StandardStylerContextMock;
import com.volantis.styling.impl.engine.StylerResult;
import com.volantis.styling.impl.engine.matchers.MatcherContextMock;
import com.volantis.styling.impl.sheet.StylerIteratee;
import com.volantis.styling.impl.sheet.StylerIteratorMock;
import com.volantis.styling.impl.sheet.StylerMock;
import com.volantis.styling.values.ImmutablePropertyValues;
import com.volantis.styling.values.ImmutablePropertyValuesMock;
import com.volantis.styling.values.MutablePropertyValuesMock;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the {@link Cascader}
 */
public class CascaderTestCase
        extends TestCaseAbstract {

    public void test() {

    }

    /**
     * Test that the cascader invokes the appropriate objects in order.
     */
    public void notest() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

//        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();

        final MatcherContextMock matcherContextMock = new MatcherContextMock(
                "matcherContextMock", expectations);

        final StandardStylerContextMock stylerContextMock =
                new StandardStylerContextMock("stylerContextMock", expectations);

        final StylerIteratorMock iteratorMock = new StylerIteratorMock(
                "iteratorMock", expectations);

        final StylerMock stylerMock = new StylerMock("stylerMock", expectations);

        final StylesMock stylesMock = new StylesMock("stylesMock", expectations);

        final ImmutablePropertyValues inheritableValues =
                new ImmutablePropertyValuesMock(
                        "inheritableValues", expectations);

        final MutablePropertyValuesMock valuesMock =
                new MutablePropertyValuesMock("valuesMock", expectations);

        final InheriterMock inheriterMock =
                new InheriterMock("inheriterMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the styler context.
        stylerContextMock.expects.getMatcherContext()
                .returns(matcherContextMock).any();
        stylerContextMock.expects.getStyles()
                .returns(stylesMock).any();


        // Create an action that will invoke the iteratee's next method.
        final MethodAction iteratorAction = new MethodAction() {

            public Object perform(MethodActionEvent event)
                    throws Throwable {

                StylerIteratee iteratee = (StylerIteratee) event.getArgument(
                        StylerIteratee.class);
                assertEquals(IterationAction.CONTINUE,
                        iteratee.next(stylerMock));

                return null;
            }
        };

        // Create an action that will check that the matcher context and
        // styles is being passed through correctly.
        final MethodAction styleAction = new MethodAction() {
            public Object perform(MethodActionEvent event)
                    throws Throwable {
                StandardStylerContext context = (StandardStylerContext) event.getArgument(
                        StandardStylerContext.class);
                assertSame("Styler context", context, stylerContextMock);

                return StylerResult.STYLED;
            }
        };

        expectations.add(new OrderedExpectations() {
            public void add() {
                iteratorMock.fuzzy.iterate(
                        mockFactory.expectsInstanceOf(StylerIteratee.class))
                        .does(iteratorAction);

                stylerMock.fuzzy.style(
                        mockFactory.expectsInstanceOf(StandardStylerContext.class))
                        .does(styleAction);

                stylesMock.expects.getPropertyValues().returns(valuesMock);

                inheriterMock.expects.inherit(valuesMock, inheritableValues);

//                stylesMock.expects.iterate(mockFactory.expectsInstanceOf(
//                        PseudoStyleEntityIteratee.class));
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

//        Cascader cascader = new AbstractCascader(
//                iteratorMock, stylerContextMock, inheriterMock, null, null);
//        cascader.cascade(inheritableValues);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
