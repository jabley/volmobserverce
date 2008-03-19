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

package com.volantis.styling.impl.compiler;

import com.volantis.mcs.themes.constraints.Constraint;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherBuilderContext;
import com.volantis.styling.impl.engine.matchers.SimpleMatcher;
import com.volantis.styling.impl.engine.matchers.composites.CompositeStateFactory;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraint;

import java.util.List;

/**
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface MatcherFactory {

    ValueConstraint createListContainsConstraint(String value);

    SimpleMatcher createAttributeMatcher(String prefix, String localName,
                                         ValueConstraint constraint);

    Matcher createMatcherSequence(List sequence);

    SimpleMatcher createLocalNameMatcher(String localName);

    SimpleMatcher createUniversalMatcher();

    ValueConstraint createConstraint(Constraint constraint);

    ValueConstraint createMatchesLanguageConstraint(String value);

    ValueConstraint createEqualsConstraint(String value);

    ValueConstraint createContainsConstraint(String value);

    ValueConstraint createExistsConstraint();

    ValueConstraint createEndsWithConstraint(String value);

    ValueConstraint createStartsWithConstraint(String value);

    SimpleMatcher createNthChildMatcher(int a, int b);

    Matcher createCompositeMatcher(Matcher context, Matcher subject, CompositeStateFactory combinatorStateFactory, MatcherBuilderContext builderContext);

    CompositeStateFactory createDescendantStateFactory();

    CompositeStateFactory createChildStateFactory();

    CompositeStateFactory createPrecedingSiblingStateFactory();

    CompositeStateFactory createImmediatelyPrecedingSiblingStateFactory();

    /**
     * Create a matcher that will match elements in a specific namespace.
     *
     * @param namespacePrefix The namespace prefix, this is mapped internally
     *                        to the actual namespace URI.
     * @return The matcher.
     */
    SimpleMatcher createNamespaceMatcher(String namespacePrefix);

    /**
     * Create a matcher that will match when elements in the default namespace.
     *
     * <p>The default namespace is hardcoded inside to be either XDIME 1 (CDM),
     * or XHTML 2.</p>
     *
     * @return The matcher.
     */
    SimpleMatcher createDefaultNamespaceMatcher();

    /**
     * Create a matcher that will match when two element ids are equal
     * @param elementId the elementId to match against
     * @return The matcher
     */
    SimpleMatcher createInlineStyleMatcher(int elementId);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 31-Aug-05	9407/5	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
