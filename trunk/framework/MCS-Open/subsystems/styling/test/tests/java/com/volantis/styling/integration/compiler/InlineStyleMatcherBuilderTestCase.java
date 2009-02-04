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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.styling.integration.compiler;

import com.volantis.mcs.themes.InlineStyleSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.styling.impl.compiler.MatcherBuilder;
import com.volantis.styling.impl.engine.matchers.InlineStyleMatcher;
import com.volantis.styling.impl.engine.matchers.Matcher;

/**
 * Test case for building InlineStyleMatcher
 */
public class InlineStyleMatcherBuilderTestCase
        extends MatcherBuilderTestCaseAbstract {

    /**
     * Test the MatcherBuilder correctly creates an InlineStyleMatcher.
     */
    public void testInlineStyleMatcherBuilder() {
        InlineStyleMatcher styleMatcher = new InlineStyleMatcher(10);
        factoryMock.expects.createInlineStyleMatcher(10).returns(styleMatcher);

        StyleSheetFactory styleSheetFactory =
                StyleSheetFactory.getDefaultInstance();

        InlineStyleSelector selector =
                styleSheetFactory.createInlineStyleSelector(10);
        SelectorSequence sequence = styleSheetFactory.createSelectorSequence();
        sequence.addSelector(selector);

        MatcherBuilder matcherBuilder = this.createMatcherBuilder();

        Matcher matcher = matcherBuilder.getMatcher(sequence);

        assertSame("Selector on its own is preserved",
                   styleMatcher, matcher);

    }
}
