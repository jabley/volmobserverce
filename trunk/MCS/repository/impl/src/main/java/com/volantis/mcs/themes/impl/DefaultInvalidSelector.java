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
import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.InvalidSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.SelectorVisitor;

/**
 *  A basic implementation of the
 * {@link com.volantis.mcs.themes.InvalidSelector} interface.
 */
public class DefaultInvalidSelector extends AbstractSelector
        implements InvalidSelector {

    /**
     * The text that caused a parsing error with the selector.
     */
    private String text;

    /**
     * Initialise.
     */
    public DefaultInvalidSelector() {
    }

    /**
     * Initialise.
     *
     * @param text      The text of the selector causing the error.
     */
    public DefaultInvalidSelector(String text) {
        this.text = text;
    }

    public void accept(SelectorVisitor visitor) {
        visitor.visit((InvalidSelector) this);
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {
        // NO-OP validator - makes no sense to validate an invalid selector
    }

    //Javadoc inherited
    public String getText() {
        return text;
    }
    
    //Javadoc inherited
    public void setText(String text) {
        this.text = text;
    }

    public CombinedSelector append(
            CombinatorEnum combinator, SelectorSequence sequence) {
        throw new UnsupportedOperationException(
                "append not allowed for InvalidSelectors");
    }

    public Object copy() {
        InvalidSelector copy = new DefaultInvalidSelector(text);
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

 01-Nov-05	9992/9	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9992/8	emma	VBM:2005101811 Adding new style property validation

 30-Oct-05	9992/3	emma	VBM:2005101811 Adding new style property validation

 31-Oct-05	9965/8	ianw	VBM:2005101811 Fixed up invalid selectors

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9965/6	ianw	VBM:2005101811 update JIBX bindings

 28-Oct-05	9965/1	ianw	VBM:2005101811 Fix file locations

 ===========================================================================
*/
