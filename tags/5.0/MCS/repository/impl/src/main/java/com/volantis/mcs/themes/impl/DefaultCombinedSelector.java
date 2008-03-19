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

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.SelectorVisitor;
import com.volantis.mcs.themes.Subject;
import com.volantis.mcs.themes.impl.parsing.CombinedSelectorParser;
import com.volantis.mcs.themes.parsing.ObjectParser;

/**
 * Basic implementation of the {@link CombinedSelector} interface.
 */
public class DefaultCombinedSelector extends AbstractSelector
        implements CombinedSelector {

    /**
     * An ObjectParser for combined selectors, used by {@link #toString}.
     */
    private static final ObjectParser PARSER = new CombinedSelectorParser();

    /**
     * The contextual selector sequence for this combined selector.
     */
    private SelectorSequence context;

    /**
     * The subject for this combined selector.
     */
    private Subject subject;

    /**
     * The combinator for this selector.
     */
    private CombinatorEnum combinator;

    // Javadoc inherited
    public SelectorSequence getContext() {
        return context;
    }

    // Javadoc inherited
    public void setContext(SelectorSequence newContext) {
        context = newContext;
    }

    // Javadoc inherited
    public Subject getSubject() {
        return subject;
    }

    // Javadoc inherited
    public void setSubject(Subject newSubject) {
        subject = newSubject;
    }

    // Javadoc inherited
    public CombinatorEnum getCombinator() {
        return combinator;
    }

    // Javadoc inherited
    public void setCombinator(CombinatorEnum newCombinator) {
        combinator = newCombinator;
    }

    // Javadoc inherited
    public String toString() {
        return PARSER.objectToText(this);
    }

    // Javadoc inherited
    public CombinedSelector append(
            CombinatorEnum combinator, SelectorSequence sequence) {
        setSubject(getSubject().append(combinator, sequence));
        return this;
    }

    // Javadoc inherited
    public boolean equals(Object o) {
        boolean equal = false;
        if (o != null && o.getClass() == DefaultCombinedSelector.class) {
            DefaultCombinedSelector other = (DefaultCombinedSelector) o;
            equal = (context == null ? other.context == null : context.equals(other.context)) &&
                    (subject == null ? other.subject == null : subject.equals(other.subject)) &&
                    (combinator == null ? other.combinator == null : combinator.equals(other.combinator));
        }
        return equal;
    }

    // Javadoc inherited
    public int hashCode() {
        int hash = 75034;
        hash = hash * 577 + (context == null ? 0 : context.hashCode());
        hash = hash * 577 + (subject == null ? 0 : subject.hashCode());
        hash = hash * 577 + (combinator == null ? 0 : combinator.hashCode());
        return hash;
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {
        // Subject may be a selector sequence or another combined selector.
        if (subject instanceof SelectorSequence) {
            ((SelectorSequence)subject).validate(context, false);
        } else {
            subject.validate(context);
        }

        // Validate the context selector sequence.
        this.context.validate(context, true);
    }

    // Javadoc inherited.
    public void accept(SelectorVisitor visitor) {
        visitor.visit(this);
    }

    public Object copy() {
        CombinedSelector copy = new DefaultCombinedSelector();
        copy.setCombinator(combinator);
        copy.setContext(context);
        copy.setSubject(subject);
        return copy;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/2	emma	VBM:2005101811 Adding new style property validation

 05-Sep-05	9407/7	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/5	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 30-Aug-05	9407/3	pduffin	VBM:2005083007 Added SelectorVisitor

 30-Aug-05	9407/1	pduffin	VBM:2005083007 Added SelectorVisitor

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
