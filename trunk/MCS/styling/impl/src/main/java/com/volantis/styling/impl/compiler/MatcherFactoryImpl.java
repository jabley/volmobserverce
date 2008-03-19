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
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.styling.impl.engine.matchers.AttributeMatcher;
import com.volantis.styling.impl.engine.matchers.InlineStyleMatcher;
import com.volantis.styling.impl.engine.matchers.LocalNameMatcher;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherBuilderContext;
import com.volantis.styling.impl.engine.matchers.MatcherSequence;
import com.volantis.styling.impl.engine.matchers.NamespaceMatcher;
import com.volantis.styling.impl.engine.matchers.NthChildMatcher;
import com.volantis.styling.impl.engine.matchers.SimpleMatcher;
import com.volantis.styling.impl.engine.matchers.UniversalMatcher;
import com.volantis.styling.impl.engine.matchers.composites.ChildStateFactory;
import com.volantis.styling.impl.engine.matchers.composites.CompositeMatcher;
import com.volantis.styling.impl.engine.matchers.composites.CompositeStateFactory;
import com.volantis.styling.impl.engine.matchers.composites.DescendantStateFactory;
import com.volantis.styling.impl.engine.matchers.composites.ImmediatelyPrecedingSiblingStateFactory;
import com.volantis.styling.impl.engine.matchers.composites.PrecedingSiblingStateFactory;
import com.volantis.styling.impl.engine.matchers.constraints.Contains;
import com.volantis.styling.impl.engine.matchers.constraints.EndsWith;
import com.volantis.styling.impl.engine.matchers.constraints.Equals;
import com.volantis.styling.impl.engine.matchers.constraints.Exists;
import com.volantis.styling.impl.engine.matchers.constraints.ListContains;
import com.volantis.styling.impl.engine.matchers.constraints.MatchesLanguage;
import com.volantis.styling.impl.engine.matchers.constraints.StartsWith;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatcherFactoryImpl
        implements MatcherFactory {

    /**
     * A map from the namespace prefixes to the URIs.
     */
    private static final Map PREFIX_2_NS_MATCHER;
    static {
        Map map = new HashMap();
        addNamespaceMatcher(map, XDIMESchemata.DEFAULT_CDM_PREFIX,
                XDIMESchemata.CDM_NAMESPACE);

        addNamespaceMatcher(map, XDIMESchemata.DEFAULT_XHTML2_PREFIX,
                XDIMESchemata.XHTML2_NAMESPACE);
        addNamespaceMatcher(map, XDIMESchemata.DEFAULT_XFORMS_PREFIX,
                XDIMESchemata.XFORMS_NAMESPACE);
        addNamespaceMatcher(map, XDIMESchemata.DEFAULT_WIDGETS_PREFIX,
                XDIMESchemata.WIDGETS_NAMESPACE);
        addNamespaceMatcher(map, XDIMESchemata.DEFAULT_RESPONSE_PREFIX,
                XDIMESchemata.RESPONSE_NAMESPACE);
        addNamespaceMatcher(map, XDIMESchemata.DEFAULT_TICKER_PREFIX,
                XDIMESchemata.TICKER_NAMESPACE);
        addNamespaceMatcher(map, XDIMESchemata.DEFAULT_TICKER_RESPONSE_PREFIX,
                XDIMESchemata.TICKER_RESPONSE_NAMESPACE);
        addNamespaceMatcher(map, XDIMESchemata.DEFAULT_GALLERY_PREFIX,
                XDIMESchemata.GALLERY_NAMESPACE);
        PREFIX_2_NS_MATCHER = map;
    }

    private static void addNamespaceMatcher(
            final Map map, final String prefix, final String namespace) {
        map.put(prefix, new NamespaceMatcher(namespace));
    }

    private static final Exists EXISTS_VALUE_CONSTRAINT = new Exists();
    private static final CompositeStateFactory CHILD_STATE_FACTORY =
            new ChildStateFactory();
    private static final CompositeStateFactory PRECEDING_SIBLING_STATE_FACTORY =
            new PrecedingSiblingStateFactory();
    private static final CompositeStateFactory DESCENDANT_STATE_FACTORY =
            new DescendantStateFactory();
    private static final CompositeStateFactory
            IMMEDIATELY_PRECEDING_SIBLING_STATE_FACTORY =
            new ImmediatelyPrecedingSiblingStateFactory();

    /**
     * Shared instance of the universal matcher.
     */
    private static final UniversalMatcher UNIVERSAL_MATCHER =
            new UniversalMatcher();

    private final SimpleMatcher defaultNamespaceMatcher;

    public MatcherFactoryImpl(SimpleMatcher defaultNamespaceMatcher) {
        this.defaultNamespaceMatcher = defaultNamespaceMatcher;
    }

    // Javadoc inherited.
    public SimpleMatcher createUniversalMatcher() {
        return UNIVERSAL_MATCHER;
    }

    // Javadoc inherited.
    public SimpleMatcher createLocalNameMatcher(String localName) {
        return new LocalNameMatcher(localName);
    }

    // Javadoc inherited.
    public SimpleMatcher createNamespaceMatcher(String namespacePrefix) {

        SimpleMatcher matcher;

        if (namespacePrefix.equals("*")) {
            matcher = null;
        } else {
            // Map the prefix to the namespace, throwing a wobbly if it is not
            // recognized.
            matcher = (SimpleMatcher) PREFIX_2_NS_MATCHER.get(namespacePrefix);
            if (matcher == null) {
                throw new IllegalArgumentException("Unknown prefix '" +
                        namespacePrefix + "'");
            }
        }

        return matcher;
    }

    public SimpleMatcher createDefaultNamespaceMatcher() {
        return defaultNamespaceMatcher;
    }

    // Javadoc inherited
    public SimpleMatcher createInlineStyleMatcher(int elementId) {
        return new InlineStyleMatcher(elementId);
    }

    // Javadoc inherited.
    public SimpleMatcher createAttributeMatcher(String prefix, String localName,
                                                ValueConstraint constraint) {
        return new AttributeMatcher(prefix, localName, constraint);
    }

    // Javadoc inherited.
    public ValueConstraint createContainsConstraint(String value) {
        return new Contains(value);
    }

    // Javadoc inherited.
    public ValueConstraint createEqualsConstraint(String value) {
        return new Equals(value);
    }

    // Javadoc inherited.
    public ValueConstraint createExistsConstraint() {
        return EXISTS_VALUE_CONSTRAINT;
    }

    public ValueConstraint createEndsWithConstraint(String value) {
        return new EndsWith(value);
    }

    public ValueConstraint createStartsWithConstraint(String value) {
        return new StartsWith(value);
    }

    // Javadoc inherited.
    public ValueConstraint createListContainsConstraint(String value) {
        return new ListContains(value);
    }

    // Javadoc inherited.
    public ValueConstraint createMatchesLanguageConstraint(String value) {
        return new MatchesLanguage(value);
    }

    // Javadoc inherited.
    public ValueConstraint createConstraint(Constraint constraint) {
        ValueConstraint valueConstraint = null;
        String value = constraint.getValue();
        if (constraint instanceof com.volantis.mcs.themes.constraints.MatchesLanguage) {
            valueConstraint = createMatchesLanguageConstraint(value);
        } else if (constraint instanceof com.volantis.mcs.themes.constraints.Equals) {
            valueConstraint = createEqualsConstraint(value);
        } else if (constraint instanceof com.volantis.mcs.themes.constraints.ContainsWord) {
            valueConstraint = createListContainsConstraint(value);
        } else if (constraint instanceof com.volantis.mcs.themes.constraints.Set) {
            valueConstraint = createExistsConstraint();
        } else if (constraint instanceof com.volantis.mcs.themes.constraints.Contains) {
            valueConstraint = createContainsConstraint(value);
        } else if (constraint instanceof com.volantis.mcs.themes.constraints.EndsWith) {
            valueConstraint = createEndsWithConstraint(value);
        } else if (constraint instanceof com.volantis.mcs.themes.constraints.StartsWith) {
            valueConstraint = createStartsWithConstraint(value);
        } else {
            throw new IllegalStateException(
                    "Unknown attribute selector constraint: " + constraint);
        }
        return valueConstraint;
    }

    // Javadoc inherited.
    public SimpleMatcher createNthChildMatcher(int a, int b) {
        return new NthChildMatcher(a, b);
    }

    // Javadoc inherited.
    public Matcher createMatcherSequence(List sequence) {
        return new MatcherSequence(sequence);
    }

    // Javadoc inherited.
    public CompositeStateFactory createPrecedingSiblingStateFactory() {
        return PRECEDING_SIBLING_STATE_FACTORY;
    }

    public CompositeStateFactory createImmediatelyPrecedingSiblingStateFactory() {
        return IMMEDIATELY_PRECEDING_SIBLING_STATE_FACTORY;
    }

    // Javadoc inherited.
    public CompositeStateFactory createChildStateFactory() {
        return CHILD_STATE_FACTORY;
    }

    // Javadoc inherited.
    public CompositeStateFactory createDescendantStateFactory() {
        return DESCENDANT_STATE_FACTORY;
    }

    // Javadoc inherited.
    public Matcher createCompositeMatcher(
            Matcher context, Matcher subject,
            CompositeStateFactory combinatorStateFactory,
            MatcherBuilderContext builderContext) {

        return new CompositeMatcher(context, subject, combinatorStateFactory,
                                    builderContext);
    }

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
