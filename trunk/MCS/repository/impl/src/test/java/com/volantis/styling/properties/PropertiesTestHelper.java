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

package com.volantis.styling.properties;

import com.volantis.shared.iteration.IterationAction;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.expectations.UnorderedExpectations;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Helper class for tests using properties.
 */
public class PropertiesTestHelper {

    private static MockFactory mockFactory = MockFactory.getDefaultInstance();

    public static StylePropertyMock createStylePropertyMock(final ExpectationBuilder expectations,
                                                            final String name) {

        final StylePropertyMock mock = new StylePropertyMock(name, expectations);

        expectations.add(new UnorderedExpectations() {
            public void add() {
                mock.expects.getName().returns(name).any();
            }
        });

        return mock;
    }

    public static StylePropertyMock createStylePropertyMock(
        final ExpectationBuilder expectations,
        final String name,
        final boolean inherited,
        final boolean computed) {

        final StylePropertyMock mock = new StylePropertyMock(name, expectations);

        final PropertyDetailsMock detailsMock =
                new PropertyDetailsMock("detailsMock", expectations);
        expectations.add(new UnorderedExpectations() {
            public void add() {
                mock.expects.getName().returns(name).any();
                mock.expects.getStandardDetails().returns(detailsMock)
                        .any();

                detailsMock.expects.isInherited().returns(inherited).any();
                detailsMock.expects.isComputable().returns(computed).any();
            }
        });

        return mock;
    }

    public static void addExpectations(final ExpectationBuilder expectations,
                                       final StylePropertyDefinitionsMock definitionsMock,
                                       final StylePropertyMock[] propertyMocks) {

        final int count = propertyMocks.length;

        expectations.add(new UnorderedExpectations() {
            public void add() {
                // count() can be called any number of times.
                definitionsMock.expects.count().returns(count).any();

                add(new OrderedExpectations() {
                    public void add() {
                        // Each of the style properties can be retrieved using index or
                        // name.
                        for (int i = 0; i < count; i += 1) {
                            final StylePropertyMock propertyMock = propertyMocks[i];
                            final int index = i;

                            definitionsMock.expects.getStyleProperty(index)
                                    .returns(propertyMock);
                            //definitionsMock.expects.getStyleProperty(name)
                            //.returns(propertyMock).any();
                        }
                    }
                }).any();

                // The properties can be iterated over as many times as they
                // want.
                final List list = Arrays.asList(propertyMocks);
                definitionsMock.expects.stylePropertyIterator().does(new MethodAction() {
                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        return list.iterator();
                    }
                }).any();

                definitionsMock.fuzzy.iterateStyleProperties(
                        mockFactory.expectsInstanceOf(StylePropertyIteratee.class))
                        .does(new IterateAction(propertyMocks))
                        .any();
            }
        });
    }

    private static class IterateAction
            implements MethodAction {

        private final StylePropertyMock[] propertyMocks;

        public IterateAction(StylePropertyMock[] propertyMocks) {
            this.propertyMocks = propertyMocks;
        }

        public Object perform(MethodActionEvent event)
                throws Throwable {

            StylePropertyIteratee iteratee = (StylePropertyIteratee)
                    event.getArgument(StylePropertyIteratee.class);
            for(int i = 0; i < propertyMocks.length; i += 1) {
                iteratee.next(propertyMocks[i]);
            }

            return IterationAction.CONTINUE;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 14-Jun-05	7997/4	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
