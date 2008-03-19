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
package com.volantis.mcs.dom2theme.impl.generator.rule.builder;

import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.TypeSelector;
import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoStyleEntityVisitor;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.StatefulPseudoClassIteratee;
import com.volantis.styling.StatefulPseudoClassSet;

/**
 * A class to help us build style sheet simple selector sequences.
 *
 * @todo adapt to new model
 * @todo rename to something more appropriate
 * @mock.generate
 */
public class SimpleSelectorSequenceBuilder {

    private final StyleSheetFactory themeFactory;

    private final PseudoSelectorFactory factory = new PseudoSelectorFactory();

    private final SelectorSequence selectorSequence;

    /**
     * Initialise, for testing only.
     */
    SimpleSelectorSequenceBuilder() {
        this.themeFactory = null;
        this.selectorSequence = null;
    }

    /**
     * Initialise.
     */
    public SimpleSelectorSequenceBuilder(StyleSheetFactory themeModelFactory) {
        this.themeFactory = themeModelFactory;
        selectorSequence = themeFactory.createSelectorSequence();
    }

    /**
     * Return the created sequence.
     *
     * @return the created sequence.
     */
    public SelectorSequence getSequence() {

        return selectorSequence;
    }

    /**
     * Set the type selector of the sequence being built.
     *
     * @param type the name of the type selector.
     */
    public void setTypeSelector(String type) {

        TypeSelector selector = themeFactory.createTypeSelector();
        selector.setType(type);
        selectorSequence.addSelector(selector);
    }

    /**
     * Add a class selector to the sequence being built.
     *
     * @param className the name of the class selector to add.
     */
    public void addClassSelector(String className) {

        // Add the class selector to the simple sequence selector's
        // optional selectors.
        ClassSelector selector = themeFactory.createClassSelector(className);
        selectorSequence.addSelector(selector);
    }

    /**
     * Add a sequence of pseudo selectors which "match" the sequence of pseudo
     * style entities contained in the supplied path to the sequence being
     * built.
     * <p>
     * Note that there is not a one-for-one mapping from entities to selectors
     * as stateful pseudo classes may be composite.
     *
     * @param pseudoPath the sequence of pseudo style entities that we should
     *      add selectors for.
     */
    public void addPseudoSelectors(PseudoStylePath pseudoPath) {

        pseudoPath.accept(new PseudoStyleEntityVisitor() {
            public void visit(StatefulPseudoClassSet pseudoClassSet) {
                addPseudoClassSelector(pseudoClassSet);
            }

            public void visit(PseudoElement pseudoElement) {
                addPseudoElementSelector(pseudoElement);
            }
        });
    }

    /**
     * Add the appropriate selector for pseudo element entity to the sequence
     * being built.
     *
     * @param pseudoElement the entity to add a selector for.
     */
    private void addPseudoElementSelector(PseudoElement pseudoElement) {

        addPseudoElementSelector(
                factory.createPseudoElementSelector(pseudoElement));
    }

    /**
     * Add the appropriate selector(s) for the stateful pseudo class entity to
     * the sequence being built.
     * <p>
     * These entities may be composite so we may add one or more selectors
     * for each one.
     *
     * @param pseudoClassSet the entity to add selector(s) for.
     */
    private void addPseudoClassSelector(StatefulPseudoClassSet pseudoClassSet) {

        pseudoClassSet.iterate(new StatefulPseudoClassIteratee() {
            public void next(StatefulPseudoClass statefulPseudoClass) {
                addPseudoClassSelector(factory.createPseudoClassSelector(
                        statefulPseudoClass));
            }
        });
    }

    /**
     * Add a pseudo class selector to the sequence being built.
     *
     * @param selector the selector to add.
     */
    private void addPseudoClassSelector(PseudoClassSelector selector) {

        selectorSequence.addSelector(selector);
    }

    /**
     * Add a pseudo element selector to the sequence being built.
     *
     * @param selector the selector to add.
     */
    private void addPseudoElementSelector(PseudoElementSelector selector) {

        selectorSequence.addSelector(selector);
    }

    /**
     * A factory to (slightly) abstract away the way we calculate the
     * appropriate selector for individual entities.
     */
    private class PseudoSelectorFactory {

        /**
         * Create a pseudo element selector from a pseudo element.
         *
         * @param pseudoElement the entity to create a selector for.
         *
         * @return the created selector.
         */
        public PseudoElementSelector createPseudoElementSelector(
                PseudoElement pseudoElement) {

            String identifier = calculateIdentifier(
                    pseudoElement.getCSSRepresentation());
            PseudoElementSelector selector =
                    themeFactory.createPseudoElementSelector(identifier);
            return selector;
        }

        /**
         * Create a pseudo class selector from an individual (not composite)
         * stateful pseudo class.
         *
         * @param pseudoClass the individual entity to create a selector for.
         *
         * @return the created selector.
         */
        public PseudoClassSelector createPseudoClassSelector(
                StatefulPseudoClass pseudoClass) {

            String identifier = calculateIdentifier(
                    pseudoClass.getCSSRepresentation());
            PseudoClassSelector selector =
                    themeFactory.createPseudoClassSelector(identifier);
            return selector;
        }

        /**
         * Calculate the identifier for a pseudo stylable entity.
         * <p>
         * <strong>NOTE:</strong> This implementation is a large "lump".
         * The alternative is to push some kind of typesafe enum into the style
         * sheet structure which is too big a change at the moment.
         *
         * @todo : later: propogate entities into the theme to avoid(/move?) this hack?
         * see VBM:2005071303.
         *
         * @param cssRepresentation
         * @return
         */
        private String calculateIdentifier(final String cssRepresentation) {

            if (cssRepresentation.startsWith("::")) {
                return cssRepresentation.substring(2);
            } else if (cssRepresentation.startsWith(":")) {
                return cssRepresentation.substring(1);
            } else {
                return cssRepresentation;
            }
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

 01-Nov-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 31-Aug-05	9409/1	geoff	VBM:2005083007 Move over to using the new themes model.

 18-Jul-05	8668/18	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
