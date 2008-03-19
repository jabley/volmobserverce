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

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.ElementSelector;
import com.volantis.mcs.themes.IdSelector;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.SelectorVisitor;
import com.volantis.mcs.themes.impl.parsing.SelectorSequenceParser;
import com.volantis.styling.PseudoStyleEntities;
import com.volantis.styling.PseudoStyleEntitiesImpl;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.StatefulPseudoClassSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultSelectorSequence
        extends AbstractSelector
        implements SelectorSequence {

    /**
     * The selectors that make up this selector sequence.
     */
    private List selectors;

    // Javadoc inherited.
    public List getSelectors() {
        return selectors;
    }

    // Javadoc inherited.
    public void setSelectors(List selectors) {
        this.selectors = new ArrayList(selectors);
    }

    // Javadoc inherited.
    public void addSelector(Selector selector) {
        if (selectors == null) {
            selectors = new ArrayList();
        }
        selectors.add(selector);
    }

    // Javadoc inherited.
    public void accept(SelectorVisitor visitor) {
        visitor.visit(this);
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {
        // do most generic validation if not specified... this is not nice.
        validate(context, false);
    }

    // Javadoc inherited.
    public void validate(ValidationContext context, boolean isContext) {

        if (selectors == null || selectors.isEmpty()) {
            context.addDiagnostic(this, DiagnosticLevel.ERROR,
                    context.createMessage("theme-selector-sequence-empty"));
        } else {

            boolean foundElementSelector = false;
            boolean foundIdSelector = false;
            List statefulPseudoClassSelectors = new ArrayList();

            // validate that the selectors are of the correct type and number
            for (Iterator i = selectors.iterator(); i.hasNext();) {

                final Selector selector = (Selector) i.next();

                // Perform common validation...
                if (selector instanceof ElementSelector) {
                    // A selector sequence can only have one element selector.
                    if (foundElementSelector) {
                        context.addDiagnostic(this, DiagnosticLevel.ERROR,
                                context.createMessage("duplicate-selector",
                                        "ElementSelector"));
                    } else {
                        foundElementSelector = true;
                    }
                } else if (selector instanceof IdSelector) {
                    // A selector sequence can only have one ID selector.
                    if (foundIdSelector) {
                        context.addDiagnostic(this, DiagnosticLevel.ERROR,
                                context.createMessage("duplicate-selector",
                                        "IDSelector"));
                    } else {
                        foundIdSelector = true;
                    }
                } else if (selector instanceof PseudoClassSelector) {
                    PseudoClassSelector pcs = (PseudoClassSelector) selector;
                    if (!pcs.getPseudoClassType().isStructural()) {
                        // add it to the list of stateful pseudo class
                        // selectors so that we can check there aren't any
                        // conflicting selectors at the end.
                        statefulPseudoClassSelectors.add(selector);
                    }
                } else if ((selector instanceof PseudoElementSelector &&
                        isContext) &&
                        (!(selector instanceof AttributeSelector ||
                        selector instanceof ClassSelector))) {
                    context.addDiagnostic(this, DiagnosticLevel.ERROR,
                            context.createMessage("invalid-selector-type"));
                }

                // Now validate the selector itself...
                selector.validate(context);
            }

            // Verify that there are no mutually exclusive pseudo class
            // selectors.
            validateStatefulPseudoClassSelectors(statefulPseudoClassSelectors,
                    context, isContext);
        }
    }

    /**
     * Validate a list of stateful pseudo class selectors.
     *
     * <p>The list is invalid if:
     * <ul>
     * <li>It contains a mutually exclusive combination of stateful selectors.</li>
     * <li>It was contained in a selector sequence that was the context of a
     * combined selector.</li>
     * </ul>
     * </p>
     *
     * @param selectors The List of pseudo-class selectors to validate
     * @param context   The context within which validation takes place.
     * @param isContext true if the selector sequence which contained these
     *                  pseudo class selectors was a context, false if it was
     *                  the subject of a combined selector
     */
    public void validateStatefulPseudoClassSelectors(
            List selectors,
            ValidationContext context,
            boolean isContext) {

        if (selectors != null && !selectors.isEmpty()) {

            if (isContext) {
                // Context selector sequences cannot contain stateful pseudo
                // class selectors.
                context.addDiagnostic(this, DiagnosticLevel.ERROR,
                        context.createMessage("invalid-combined-selector",
                                new Object[]{"context",
                                             "contained "
                        + selectors.size() +
                        " stateful pseudo class selectors"}));
            }

            // Keep track of the combinations.
            PseudoStyleEntities entities = new PseudoStyleEntitiesImpl();
            StatefulPseudoClassSet combination = null;

            Iterator it = selectors.iterator();
            while (it.hasNext()) {
                PseudoClassSelector selector = (PseudoClassSelector) it.next();
                PseudoClassTypeEnum type = selector.getPseudoClassType();
                if (!PseudoClassTypeEnum.INVALID.equals(type)) {
                    if (!type.isStructural()) {
                        StatefulPseudoClass other = entities.
                                getStatefulPseudoClass(type.getType());
                        if (other == null) {
                            context.addDiagnostic(this,
                                    DiagnosticLevel.ERROR,
                                    context.createMessage(
                                            "theme-invalid-pseudo-class",
                                            type.getType()));
                        } else if (combination == null) {
                            combination = other.getSet();
                        } else {
                            try {
                                combination = combination.add(other);
                            } catch (IllegalArgumentException e) {
                                context.addDiagnostic(this,
                                        DiagnosticLevel.ERROR,
                                        context.createMessage(
                                                "theme-incompatible-combination",
                                                new Object[]{
                                                    combination.getCSSRepresentation(),
                                                    other.getCSSRepresentation()
                                                }));
                            }
                        }
                    }
                }
            }
        }
    }

    // Javadoc inherited
    public String toString() {
        return new SelectorSequenceParser().objectToText(this);
    }

    /**
     * Checks whether the contents of two instances of this class have equal
     * values.
     *
     * @param other The other default contextual selector sequence to compare
     * @return True if the two values are equal, false otherwise
     * @see #equals
     */
    protected boolean equalsImpl(DefaultSelectorSequence other) {
        return other != null && equalLists(selectors, other.selectors);
    }

    /**
     * Checks whether two lists are equal, with an empty list and a null
     * value being treated as the same.
     *
     * @param a The first list
     * @param b The second list
     * @return True if they are equal, false otherwise
     */
    protected boolean equalLists(List a, List b) {
        if (a != null && a.isEmpty()) {
            a = null;
        }
        if (b != null && b.isEmpty()) {
            b = null;
        }

        return a == null ? b == null : a.equals(b);
    }

    // Javadoc inherited
    public boolean hasSelector() {
        return selectors != null && !selectors.isEmpty();
    }

    // Javadoc inherited.
    public void visitChildren(SelectorVisitor visitor) {

        if (selectors != null) {
            for (int i = 0; i < selectors.size(); i++) {
                Selector selector = (Selector) selectors.get(i);
                selector.accept(visitor);
            }
        }
    }

    public Object copy() {
        SelectorSequence copy = new DefaultSelectorSequence();
        copy.setSelectors(selectors);
        return copy;
    }

    protected Selector copySelector(Selector selector) {
        return selector == null ? null : (Selector) selector.copy();
    }

    protected List copySelectorList(List selectors) {
        if (selectors == null) {
            return null;
        } else {
            List listCopy = new ArrayList();
            for (int i = 0; i < selectors.size(); i++) {
                Selector selector = (Selector) selectors.get(i);
                listCopy.add(selector.copy());
            }
            return listCopy;
        }
    }

    // Javadoc inherited
    public CombinedSelector append(
            CombinatorEnum combinator, SelectorSequence sequence) {

        CombinedSelector combined = new DefaultCombinedSelector();
        combined.setContext(this);
        combined.setCombinator(combinator);
        combined.setSubject(sequence);
        return combined;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/2	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 01-Nov-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 05-Sep-05	9407/7	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/5	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 ===========================================================================
*/
