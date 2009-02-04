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

import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.IdSelector;
import com.volantis.mcs.themes.InlineStyleSelector;
import com.volantis.mcs.themes.InvalidSelector;
import com.volantis.mcs.themes.NthChildSelector;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.SelectorVisitor;
import com.volantis.mcs.themes.TypeSelector;
import com.volantis.mcs.themes.UniversalSelector;
import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoStyleEntities;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.StatefulPseudoClassSet;
import com.volantis.styling.compiler.Specificity;
import com.volantis.styling.compiler.SpecificityCalculator;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherBuilderContext;
import com.volantis.styling.impl.engine.matchers.SimpleMatcher;
import com.volantis.styling.impl.engine.matchers.composites.Operator;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraint;

import java.util.ArrayList;
import java.util.List;

/**
 * Construct a {@link Matcher} from a matcher.
 */
public class MatcherBuilderImpl
        implements SelectorVisitor, MatcherBuilder {

    /**
     * The factory that is used to construct matchers and related objects.
     */
    private final MatcherFactory factory;

    private final PseudoStyleEntities pseudoStyleEntityProvider;

    /**
     * The list of matchers that have been constructed from the simple
     * selector sequence.
     */
    private List matcherSequenceList;

    /**
     * The object used to construct the specificity.
     */
    private final SpecificityCalculator specificityCalculator;

    private final MatcherBuilderContext builderContext;

    /**
     * The matcher that is being constructed.
     */
    private Matcher constructedMatcher;

    /**
     * The list of pseudo style entities.
     */
    private List pseudoStyleEntities;

    /**
     * The specificity that was calculated.
     */
    private Specificity specificity;

    public MatcherBuilderImpl(MatcherBuilderConfiguration configuration) {
        this.factory = configuration.getFactory();
        this.pseudoStyleEntityProvider = configuration.getEntities();
        this.specificityCalculator = configuration.getSpecificityCalculator();
        this.builderContext = configuration.getBuilderContext();
    }

    public Matcher getMatcher(Selector selector) {
        specificityCalculator.reset();
        pseudoStyleEntities = new ArrayList();
        matcherSequenceList = new ArrayList();
        constructedMatcher = null;
        specificity = null;

        selector.accept(this);

        specificity = specificityCalculator.getSpecificity();

        if (constructedMatcher == null) {
            throw new IllegalStateException(
                    "No matcher constructed for " + selector);
        }

        return constructedMatcher;
    }

    public List getPseudoStyleEntities() {
        return pseudoStyleEntities;
    }

    public Specificity getSpecificity() {
        return specificity;
    }

    // Javadoc inherited.
    public void visit(AttributeSelector selector) {

        // Update the specificity.
        specificityCalculator.addAttributeSelector();

        String attribute = selector.getName();
        ValueConstraint constraint = factory.createConstraint(
                selector.getConstraint());
        matcherSequenceList.add(factory.createAttributeMatcher(
                selector.getNamespacePrefix(), attribute, constraint));
    }

    // Javadoc inherited.
    public void visit(ClassSelector selector) {

        // Update the specificity.
        specificityCalculator.addClassSelector();

        // A class selector is simply an attribute selector where the
        // attribute name is "class" and the constraint is ListContains.
        String styleClass = selector.getCssClass();
        ValueConstraint constraint =
                factory.createListContainsConstraint(styleClass);

        matcherSequenceList.add(factory.createAttributeMatcher(
                null, "class", constraint));
    }

    // Javadoc inherited.
    public void visit(CombinedSelector selector) {

        // Visit the left which is the contextual selector.
        selector.getContext().accept(this);
        Matcher context = constructedMatcher;

        final CombinatorEnum combinator = selector.getCombinator();
        Operator operator = null;
        if (combinator == CombinatorEnum.DESCENDANT) {
            operator = Operator.DESCENDANT;
        } else if (combinator == CombinatorEnum.CHILD) {
            operator = Operator.CHILD;
        } else if (combinator == CombinatorEnum.INDIRECT_ADJACENT) {
            operator = Operator.SIBLING;
        } else if (combinator == CombinatorEnum.DIRECT_ADJACENT) {
            operator = Operator.ADJACENT;
        } else {
            throw new IllegalStateException("Unknown combinator " + combinator);
        }

        // Visit the right selector which is the subject selector.
        selector.getSubject().accept(this);
        Matcher subject = constructedMatcher;

        // Construct the composite matcher.
        constructedMatcher = factory.createCompositeMatcher(
                context, subject, operator, builderContext);
    }

    public void visit(InvalidSelector selector) {
        // TODO later This ought to do something...
    }

    //javadoc inherited
    public void visit(InlineStyleSelector selector) {
        matcherSequenceList.add(
                factory.createInlineStyleMatcher(selector.getElementId()));
    }

    // Javadoc inherited.
    public void visit(IdSelector selector) {

        // Update the specificity.
        specificityCalculator.addIDSelector();

        // An id selector is simply an attribute selector where the
        // attribute name is "id" and the constraint is Equals.
        String id = selector.getId();
        ValueConstraint constraint =
                factory.createEqualsConstraint(id);

        matcherSequenceList.add(factory.createAttributeMatcher(
                null, "id", constraint));
    }

    // Javadoc inherited.
    public void visit(PseudoClassSelector selector) {

        // Update the specificity.
        specificityCalculator.addPseudoClassSelector();

        String pseudoClassName = selector.getPseudoClassType().getType();

        StatefulPseudoClass pseudoClass = null;
        if (pseudoStyleEntityProvider != null) {
            pseudoClass = pseudoStyleEntityProvider.getStatefulPseudoClass(
                    pseudoClassName);
        }

        if (pseudoClass != null) {

            // Stateful pseudo class. Merge consecutive stateful pseudo classes
            // together.

            // If the last item in the list of pseudo style entities is a
            // PseudoClassSet then remove it, and merge with the current
            // class set.
            final int length = pseudoStyleEntities.size();
            StatefulPseudoClassSet classSet = pseudoClass.getSet();
            if (length > 0) {
                PseudoStyleEntity previous = (PseudoStyleEntity)
                        pseudoStyleEntities.get(length - 1);
                if (previous instanceof StatefulPseudoClassSet) {
                    StatefulPseudoClassSet previousPseudoClassSet =
                            (StatefulPseudoClassSet) previous;
                    pseudoStyleEntities.remove(length - 1);
                    classSet = previousPseudoClassSet.combine(classSet);
                }
            }

            // Add the pseudo class set to the end of the list of pseudo style
            // entities.
            pseudoStyleEntities.add(classSet);

        } else if (pseudoClassName.equals("first-child")) {
            // Structural pseudo class.

            // :first-child is equivalent to ::nth-child(1).
            matcherSequenceList.add(factory.createNthChildMatcher(0, 1));

        } else {
            throw new IllegalStateException(
                    "Unknown pseudo class '" + pseudoClassName + "'");
        }
    }

    public void visit(NthChildSelector selector) {

        int a = selector.getA();
        int b = selector.getB();

        matcherSequenceList.add(factory.createNthChildMatcher(a, b));
    }

    // Javadoc inherited.
    public void visit(PseudoElementSelector selector) {

        // Update the specificity.
        specificityCalculator.addPseudoElementSelector();

        String pseudoElementName = selector.getPseudoElementType().getType();
        PseudoElement pseudoElement;
        pseudoElement = pseudoStyleEntityProvider.getPseudoElement(
                pseudoElementName);
        pseudoStyleEntities.add(pseudoElement);
    }

    // Javadoc inherited.
    public void visit(TypeSelector selector) {

        // Update the specificity.
        specificityCalculator.addElementSelector();

        // Namespace and type are separated into two distinct matchers in the
        // style sheet because either one can be a wild card in which case
        // there is no point in checking them. Splitting them up greatly
        // simplifies the task of comparison making it much easier to do.
        addNamespaceMatcher(selector.getNamespacePrefix());

        String localName = selector.getType();
        matcherSequenceList.add(factory.createLocalNameMatcher(localName));
    }

    /**
     * Add a namespace matcher for the prefix.
     *
     * <p>If the prefix is null then a default namespace matcher is added,
     * if it is a '*' then no matcher is added, otherwise it adds a matcher
     * that is appropriate to the prefix given the hard coded mapping between
     * prefixes and namespaces.</p>
     *
     * @param namespacePrefix The namespace prefix. 
     */
    private void addNamespaceMatcher(String namespacePrefix) {
        SimpleMatcher matcher;
        if (namespacePrefix == null) {
            matcher = factory.createDefaultNamespaceMatcher();
        } else {
            matcher = factory.createNamespaceMatcher(namespacePrefix);
        }
        if (matcher != null) {
            matcherSequenceList.add(matcher);
        }
    }

    // Javadoc inherited.
    public void visit(UniversalSelector selector) {

        // Universal selectors are ignored if they are in a sequence with any
        // other selector and apply to any namespace. As this method is not
        // aware whether the selector is alone in a sequence or not it cannot
        // do anything so it defers it to the method that processes the
        // SelectorSequence.

        // Try and add a namespace matcher, this will not add anything if the
        // namespace prefix matches any namespace.
        addNamespaceMatcher(selector.getNamespacePrefix());
    }

    // Javadoc inherited.
    public void visit(SelectorSequence sequence) {

        // Clear the list that is used to collate all the matchers from this
        // sequence.
        matcherSequenceList.clear();

        // Visit all the child selectors, these should add items to the
        // list of simple matchers.
        sequence.visitChildren(this);

        // If the sequence is empty then do nothing, if there is only one then
        // return that, otherwise construct a wrapper around the list.
        int size = matcherSequenceList.size();
        if (size == 0) {
            constructedMatcher = factory.createUniversalMatcher();
        } else if (size == 1) {
            constructedMatcher = (Matcher) matcherSequenceList.get(0);
        } else {
            constructedMatcher = factory.createMatcherSequence(
                    matcherSequenceList);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 05-Sep-05	9407/12	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/10	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 30-Aug-05	9407/4	pduffin	VBM:2005083007 Added SelectorVisitor

 30-Aug-05	9407/2	pduffin	VBM:2005083007 Added SelectorVisitor

 25-Aug-05	9377/1	schaloner	VBM:2005071102 Migrated mcs-shortcut-after to mcs-shortcut and after

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
