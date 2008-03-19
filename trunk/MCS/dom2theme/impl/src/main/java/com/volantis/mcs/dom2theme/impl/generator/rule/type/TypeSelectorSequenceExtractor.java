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

import com.volantis.mcs.dom2theme.impl.model.OutputStyledElement;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePathIteratee;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoStyleEntityVisitor;
import com.volantis.styling.StatefulPseudoClassSet;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashSet;
import java.util.Set;

// todo: later: turns out we don't need to extract all the selectors mentioned
// in AN004 5.9.3. In particular, pseudo elements selectors without a type are
// a bit of a waste of time, for example how many real work docs will have
// :first-letter set for all elements?

/**
 * Extracts the list of type selectors we will use from the list of elements.
 */
public class TypeSelectorSequenceExtractor {

    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(TypeSelectorSequenceExtractor.class);

    private final TypeSelectorSequenceList selectorList =
            new TypeSelectorSequenceList();

    private final Set typeSet = new HashSet();

    private final Set pseudoElementSet = new HashSet();

    private final Set pseudoClassSets = new HashSet();

    private final Set combinedSet = new HashSet();

    public TypeSelectorSequenceList extractSequences(
            OutputStyledElementList elementList) {

        elementList.iterate(new OutputStyledElementIteratee() {
            public IterationAction next(OutputStyledElement element) {
                extractElementSelectors(element);
                return IterationAction.CONTINUE;
            }
        });

        if (logger.isDebugEnabled()) {
            logger.debug("Sorted type selectors: " + selectorList.toString());
        }

        return selectorList;
    }

    private void extractElementSelectors(
            final OutputStyledElement outputElement) {

        OutputStyles styles = outputElement.getStyles();
        if (styles == null) {
            return;
        }

        // iterate over all the contained pseudo properties
        styles.iterate(new PseudoStylePathIteratee() {


            public void next(PseudoStylePath pseudoPath) {

                String type = outputElement.getName();
                if (pseudoPath.isEmpty()) {
                    if (!typeSet.contains(type)) {
                        typeSet.add(type);
                        // Create sequence for type selector and add.
                        TypeSelectorSequence sequence =
                                new TypeSelectorSequence();
                        sequence.setType(type);
                        selectorList.add(sequence);
                    }
                    // Note, no combined selector required when the path is
                    // empty, since we just have a type selector.
                } else {
                    final TypeSelectorSequence combined =
                            new TypeSelectorSequence();
                    if (type != null) {
                        combined.setType(type);
                    }
                    // Iterate over all the entities qualifying the properties,
                    // adding them separately and to a combined sequence as
                    // necessary.
                    pseudoPath.accept(new PseudoStyleEntityVisitor() {
                        public void visit(StatefulPseudoClassSet pseudoClassSet) {
                            // If we haven't seen this pseudo class yet...
                            if (!pseudoClassSets.contains(pseudoClassSet)) {
                                pseudoClassSets.add(pseudoClassSet);
                                // ... then create sequence for it and add to list.
                                TypeSelectorSequence sequence =
                                        new TypeSelectorSequence();
                                sequence.addPseudoClassSet(pseudoClassSet);
                                selectorList.add(sequence);
                            }
                            // Add pseudo class selector to combined selector.
                            combined.addPseudoClassSet(pseudoClassSet);
                        }

                        public void visit(PseudoElement pseudoElement) {
                            // If we haven't seen this pseudo element yet...
                            if (!pseudoElementSet.contains(pseudoElement)) {
                                pseudoElementSet.add(pseudoElement);
                                // ... then create sequence for it and add to list.
                                TypeSelectorSequence sequence =
                                        new TypeSelectorSequence();
                                sequence.addPseudoElement(pseudoElement);
                                selectorList.add(sequence);
                            }
                            // Add pseudo class to combined selector.
                            combined.addPseudoElement(pseudoElement);
                        }
                    });

                    // If the combined sequence isn't simple and we haven't seen
                    // this combined sequence yet...
                    if (combined.isComposite() &&
                            !combinedSet.contains(combined)) {
                        combinedSet.add(combined);
                        // Then add it to the list.
                        final TypeSelectorSequence sequence =
                                new TypeSelectorSequence();
                        if (combined.getType() != null) {
                            sequence.setType(combined.getType());
                        }
                        pseudoPath.accept(new PseudoStyleEntityVisitor() {
                            public void visit(StatefulPseudoClassSet pseudoClassSet) {
                                sequence.addPseudoClassSet(pseudoClassSet);
                            }

                            public void visit(PseudoElement pseudoElement) {
                                sequence.addPseudoElement(pseudoElement);
                            }
                        });
                        selectorList.add(sequence);
                    }
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

 09-Aug-05	9153/5	ianw	VBM:2005072216 Fixed rework issues

 09-Aug-05	9153/3	ianw	VBM:2005072216 Slight refactor of setting type

 05-Aug-05	9153/1	ianw	VBM:2005072216 Fix style normalizer issue with pseudo paths

 18-Jul-05	8668/16	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
