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
package com.volantis.mcs.dom2theme.impl.generator.rule.type;

import com.volantis.mcs.dom2theme.impl.generator.rule.RuleExtractor;
import com.volantis.mcs.dom2theme.impl.generator.rule.builder.RuleBuilder;
import com.volantis.mcs.dom2theme.impl.generator.rule.builder.RuleBuilderFactory;
import com.volantis.mcs.dom2theme.impl.generator.rule.builder.SimpleSelectorSequenceBuilder;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.Rule;
import com.volantis.synergetics.log.LogDispatcher;

public class TypeRuleExtractor implements RuleExtractor {

    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(TypeRuleExtractor.class);

    private final RuleBuilderFactory ruleBuilderFactory;

    private final PropertiesIntersectionCalculator intersectionCalculator =
            new PropertiesIntersectionCalculator();

    private final PropertiesDifferenceCalculator differenceCalculator =
            new PropertiesDifferenceCalculator();


    public TypeRuleExtractor(RuleBuilderFactory ruleBuilderFactory) {
        this.ruleBuilderFactory = ruleBuilderFactory;
    }

    public StyleSheet extractRules(OutputStyledElementList elementList,
            final StyleSheet styleSheet) {

        // ==================================================================
        // 1 - Extract rules from doc using type and/or pseudo selectors.
        // ==================================================================
        //

        // ------------------------------------------------------------------
        // 1.1 - Create type and pseudo selectors.
        // ------------------------------------------------------------------

        // Iterate over the list of styled entities, extracting simple selector
        // sequences containing the type and pseudo selectors which will be
        // used later.
        TypeSelectorSequenceExtractor sequenceExtractor =
                new TypeSelectorSequenceExtractor();
        TypeSelectorSequenceList sequenceList =
                sequenceExtractor.extractSequences(elementList);

        // ------------------------------------------------------------------
        // 1.2 - Extract rules for type and pseudo selectors.
        // ------------------------------------------------------------------

        extractRules(elementList, sequenceList, styleSheet);

        return styleSheet;
    }

    // todo: later: refactor this method into TypeSelectorSequenceRuleExtractor?
    private void extractRules(OutputStyledElementList elementList,
            TypeSelectorSequenceList sequenceList, final StyleSheet styleSheet) {

        // Create the type matcher we use to match selector sequences against
        // entities.
        final ElementTypeMatcher elementTypeMatcher = new ElementTypeMatcher(
                elementList);

        // Iterate over the selector sequence list.
        sequenceList.iterate(new TypeSelectorSequenceIteratee() {
            public void next(TypeSelectorSequence selectorSequence) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Extracting type rules for " +
                            selectorSequence);
                }

                // Match the selector against the styleable entities in the
                // input document, returning the set of styled entities which
                // match it's type.
                OutputStyledElementList typedElementSubset =
                        elementTypeMatcher.match(selectorSequence.getType());

                // Calculate the intersection of all the sets of properties
                // associated with the matching entities to determine whether
                // the entities have any properties in common.
                StyleProperties commonProperties =
                        intersectionCalculator.intersection(
                                typedElementSubset, selectorSequence);

                // If we found some properties which were common in every
                // member of the set of properties for this selector...
                if (commonProperties != null) {
                    // ... then create a theme rule from those properties and
                    // remove them from the input document.

                    if (logger.isDebugEnabled()) {
                        logger.debug("Found common properties for " +
                                selectorSequence);
                    }

                    // Create a rule using a type selector for the common
                    // properties.
                    RuleBuilder ruleBuilder =
                            ruleBuilderFactory.createRuleBuilder();
                    SimpleSelectorSequenceBuilder sequenceBuilder =
                            ruleBuilderFactory.createSequenceBuilder();
                    if (selectorSequence.getType() != null) {
                        sequenceBuilder.setTypeSelector(
                                selectorSequence.getType());
                    }
                    sequenceBuilder.addPseudoSelectors(
                            selectorSequence.getPath());
                    ruleBuilder.addSequence(sequenceBuilder);
                    ruleBuilder.setProperties(commonProperties);
                    Rule rule = ruleBuilder.getRule();

                    // Add the created rule to the rule set.
                    styleSheet.addRule(rule);

                    // Remove the common properties we have created a rule for
                    // from all the styled entities which contained them.

                    differenceCalculator.difference(typedElementSubset,
                            selectorSequence, commonProperties);
                }
            }
        });
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/15	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
