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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.CSSStyleSheet;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.IdSelector;
import com.volantis.mcs.themes.InvalidSelector;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.NthChildSelector;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.PseudoElementTypeEnum;
import com.volantis.mcs.themes.SelectorGroup;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.TypeSelector;
import com.volantis.mcs.themes.UniversalSelector;
import com.volantis.mcs.themes.InlineStyleSelector;
import com.volantis.mcs.themes.ThemeFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of {@link StyleSheetFactory} that uses a series of
 * simple model implementation classes.
 */
public class DefaultStyleSheetFactory extends StyleSheetFactory {

    /**
     * A map associating pseudo class type descriptions with their
     * implementation classes.
     */
    private static final Map pseudoClassSelectors = new HashMap();

    /**
     * A map associating pseudo element type descriptions with their
     * implementation classes.
     */
    private static final Map pseudoElementSelectors = new HashMap();

    static {
        pseudoClassSelectors.put(PseudoClassTypeEnum.FIRST_CHILD.getType(),
                DefaultFirstChildSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.NTH_CHILD.getType(),
                DefaultNthChildSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.LINK.getType(),
                DefaultLinkSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.VISITED.getType(),
                DefaultVisitedSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.ACTIVE.getType(),
                DefaultActiveSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.FOCUS.getType(),
                DefaultFocusSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.HOVER.getType(),
                DefaultHoverSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.MCS_CONCEALED.getType(),
                DefaultMCSConcealedSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.MCS_UNFOLDED.getType(),
                DefaultMCSUnfoldedSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.MCS_INVALID.getType(),
                DefaultMCSInvalidSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.MCS_NORMAL.getType(),
                DefaultMCSNormalSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.MCS_BUSY.getType(),
                DefaultMCSBusySelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.MCS_FAILED.getType(),
                DefaultMCSFailedSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.MCS_SUSPENDED.getType(),
                DefaultMCSSuspendedSelector.class);
        pseudoClassSelectors.put(PseudoClassTypeEnum.MCS_DISABLED.getType(),
                DefaultMCSDisabledSelector.class);

        pseudoElementSelectors.put(PseudoElementTypeEnum.AFTER.getType(),
                DefaultAfterSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.BEFORE.getType(),
                DefaultBeforeSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.FIRST_LETTER.getType(),
                DefaultFirstLetterSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.FIRST_LINE.getType(),
                DefaultFirstLineSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.MARKER.getType(),
                DefaultMarkerSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.MCS_SHORTCUT.getType(),
                DefaultMCSShortcutSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.MCS_NEXT.getType(),
                DefaultMCSNextSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.MCS_PREVIOUS.getType(),
                DefaultMCSPreviousSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.MCS_RESET.getType(),
                DefaultMCSResetSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.MCS_CANCEL.getType(),
                DefaultMCSCancelSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.MCS_COMPLETE.getType(),
                DefaultMCSCompleteSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.MCS_LABEL.getType(),
                DefaultMCSLabelSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.MCS_ITEM.getType(),
                DefaultMCSItemSelector.class);
        pseudoElementSelectors.put(PseudoElementTypeEnum.MCS_BETWEEN.getType(),
                DefaultMCSBetweenSelector.class);
    }

    // Javadoc inherited.
    public StyleSheet createStyleSheet() {
        return new StyleSheetImpl();
    }

    public CSSStyleSheet createCSSStyleSheet() {
        return new CSSStyleSheetImpl();
    }

    // Javadoc inherited
    public AttributeSelector createAttributeSelector() {
        return new DefaultAttributeSelector();
    }

    public ClassSelector createClassSelector(String cssClass) {
        return new DefaultClassSelector(cssClass);
    }

    // Javadoc inherited
    public CombinedSelector createCombinedSelector() {
        return new DefaultCombinedSelector();
    }

    // Javadoc inherited
    public SelectorSequence createSelectorSequence() {
        return new DefaultSelectorSequence();
    }

    // Javadoc inherited.
    public SelectorSequence createSelectorSequence(
            List selectors) {

        SelectorSequence sequence = new DefaultSelectorSequence();
        sequence.setSelectors(selectors);
        return sequence;
    }

    // Javadoc inherited.
    public IdSelector createIdSelector(String id) {
        return new DefaultIdSelector(id);
    }

    // Javadoc inherited
    public InlineStyleSelector createInlineStyleSelector(int elementId) {
        return new DefaultInlineStyleSelector(elementId);
    }

    // Javadoc inherited
    public SelectorGroup createSelectorGroup() {
        return new DefaultSelectorGroup();
    }

    // Javadoc inherited
    public TypeSelector createTypeSelector() {
        return new DefaultTypeSelector();
    }

    // Javadoc inherited.
    public TypeSelector createTypeSelector(String namespacePrefix, String type) {
        return new DefaultTypeSelector(namespacePrefix, type);
    }

    // Javadoc inherited
    public UniversalSelector createUniversalSelector() {
        return new DefaultUniversalSelector();
    }

    // Javadoc inherited.
    public UniversalSelector createUniversalSelector(String namespacePrefix) {
        return new DefaultUniversalSelector(namespacePrefix);
    }

    // Javadoc inherited
    public PseudoClassSelector createPseudoClassSelector(String type) {

        PseudoClassSelector selector = null;
        Class selectorClass = (Class) pseudoClassSelectors.get(type);

        if (selectorClass != null) {
            try {
                selector = (PseudoClassSelector) selectorClass.newInstance();
            } catch (InstantiationException e) {
                // do nothing here, but return an invalid selector
            } catch (IllegalAccessException e) {
                // do nothing here, but return an invalid selector
            }
        }

        if (selectorClass == null) {
            // A valid selector could not be created - return invalid selector.
            selector = new DefaultInvalidPseudoClassSelector();
            selector.setInvalidPseudoClass(type);
        }
        return selector;
    }


   // Javadoc inherited.
    public NthChildSelector createNthChildSelector(int a, int b) {
        return new DefaultNthChildSelector(a, b);
    }

    // Javadoc inherited.
    public NthChildSelector createNthChildSelector(String expression) {
        return new DefaultNthChildSelector(expression);
    }

    // Javadoc inherited
    public PseudoElementSelector createPseudoElementSelector(String type) {

        PseudoElementSelector selector = null;
        Class selectorClass = (Class) pseudoElementSelectors.get(type);

        if (selectorClass != null) {
            try {
                selector = (PseudoElementSelector) selectorClass.newInstance();
            } catch (InstantiationException e) {
                // do nothing here, but return an invalid selector
            } catch (IllegalAccessException e) {
                // do nothing here, but return an invalid selector
            }
        }

        if (selectorClass == null) {
            // A valid selector could not be created - return invalid selector.
            selector = new DefaultInvalidPseudoElementSelector();
            selector.setInvalidPseudoElement(type);
        }
        return selector;
    }

    public InvalidSelector createInvalidSelector(String text) {
        return new DefaultInvalidSelector(text);
    }

    // Javadoc inherited
    public MutableStyleProperties createStyleProperties() {
        return ThemeFactory.getDefaultInstance().createMutableStyleProperties();
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9965/1	ianw	VBM:2005101811 Fixed up invalid selectors

 27-Sep-05	9487/5	pduffin	VBM:2005091203 Committing new CSS Parser

 14-Sep-05	9380/3	adrianj	VBM:2005082401 GUI support for nth-child

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
